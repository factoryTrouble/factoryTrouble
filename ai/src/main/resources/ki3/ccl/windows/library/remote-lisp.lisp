;;;-*- Mode: Lisp; Package: CCL -*-
;;;
;;;   Copyright (C) 2011 Clozure Associates
;;;   This file is part of Clozure CL.  
;;;
;;;   Clozure CL is licensed under the terms of the Lisp Lesser GNU Public
;;;   License , known as the LLGPL and distributed with Clozure CL as the
;;;   file "LICENSE".  The LLGPL consists of a preamble and the LGPL,
;;;   which is distributed with Clozure CL as the file "LGPL".  Where these
;;;   conflict, the preamble takes precedence.  
;;;
;;;   Clozure CL is referenced in the preamble as the "LIBRARY."
;;;
;;;   The LLGPL is also available online at
;;;   http://opensource.franz.com/preamble.html
;;;

(in-package :ccl)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; swink client -- use this ccl to debug a remote ccl.
;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defclass remote-lisp-connection (swink:connection)
  ((features :initform nil :accessor rlisp-features)
   (lisp-implementation-type :initform "???" :accessor rlisp-lisp-implementation-type)
   (lisp-implementation-version :initform "???" :accessor rlisp-lisp-implementation-version)
   (machine-instance :initform "???" :accessor rlisp-machine-instance)
   (proxies :initform (make-hash-table :test 'eql :weak :value) :reader connection-proxies)))

(defmethod swink:thread-id ((conn remote-lisp-connection)) nil)

(defmethod update-rlisp-connection-info ((conn remote-lisp-connection)
                                         &key lisp-implementation-type
                                              lisp-implementation-version
                                              machine-instance
                                              (features nil featuresp)
                                         &allow-other-keys)
  (swink:with-connection-lock (conn)
    (when featuresp
      (setf (rlisp-features conn) features))
    (when machine-instance
      (setf (rlisp-machine-instance conn) machine-instance))
    (when lisp-implementation-type
      (setf (rlisp-lisp-implementation-type conn) lisp-implementation-type))
    (when lisp-implementation-version
      (setf (rlisp-lisp-implementation-version conn) lisp-implementation-version))))

;; Proxy for a thread on the remote server. 
(defclass remote-lisp-thread (swink:thread)
  (;; Local process running the local repl: interacting with user, sending to remote for execution.
   ;;    (the swink:thread-process slot has thread-id of the remote process)
   (control-process :initform nil :accessor swink:thread-control-process)
   (break-level :initform nil :accessor rthread-break-level)))

(defmethod swink:thread-class ((conn remote-lisp-connection)) 'remote-lisp-thread)

(defmethod rlisp-host-description ((rthread remote-lisp-thread))
  (rlisp-host-description (swink:thread-connection rthread)))

(defmethod rlisp-thread-description ((rthread remote-lisp-thread))
  (format nil "~a thread ~a" (rlisp-host-description rthread) (swink:thread-id rthread)))

(defmethod print-object ((rthread remote-lisp-thread) stream)
  (print-unreadable-object (rthread stream :type t :identity t)
    (princ (rlisp-thread-description rthread) stream)))

(defmethod rlisp/invoke-restart ((rthread remote-lisp-thread) name)
  (swink:send-event rthread `(:invoke-restart ,name)))

(defmethod rlisp/invoke-restart-in-context ((rthread remote-lisp-thread) index)
  (swink:send-event rthread `(:invoke-restart-in-context ,index)))

(defmethod rlisp/toplevel ((rthread remote-lisp-thread))
  (swink:send-event rthread `(:toplevel)))

(defmethod rlisp/interrupt ((rthread remote-lisp-thread))
  (swink:send-event rthread `(:interrupt)))

(defmethod rlisp-host-description ((conn remote-lisp-connection))
  (let ((socket (swink:connection-control-stream conn)))
    (if (open-stream-p socket)
      (format nil "~a:~a" (ipaddr-to-dotted (remote-host socket)) (remote-port socket))
      ":CLOSED")))

(defmethod print-object ((conn remote-lisp-connection) stream)
  (print-unreadable-object (conn stream :type t :identity t)
    (format stream "~a @~a"
            (rlisp-host-description conn)
            (rlisp-machine-instance conn))))


(defmethod start-rlisp-process ((conn remote-lisp-connection))
  (assert (null (swink:connection-control-process conn)))
  (setf (swink:connection-control-process conn)
        (process-run-function (format nil "swank-event-loop ~a" (remote-port (swink:connection-control-stream conn)))
          (lambda ()
            (setf (swink:connection-control-process conn) *current-process*)
            (with-simple-restart (swink:close-connection "Close connection")
              (loop (dispatch-event conn (swink:read-sexp conn)))))))
  (let ((info (swink:send-event-for-value conn `(:connection-info))))
    (when info
      (apply #'update-rlisp-connection-info conn info)))
  conn)


(defmethod dispatch-event ((conn remote-lisp-connection) thread.event)
  (swink::log-event "Dispatch-event ~s" thread.event)
  (destructuring-bind (sender-id . event) thread.event
    (swink:destructure-case event
      ((:end-connection condition)
       (declare (ignore condition))
       (swink:close-connection conn))
      ((:start-repl break-level)
       ;; Starting a new repl (possibly due to an error in a non-repl process)
       (let ((rthread (swink:make-new-thread conn sender-id)))
         (start-remote-listener rthread break-level)))
      ((:exit-repl)
       (let ((rthread (swink:find-thread conn sender-id)))
         (when (and rthread (swink:thread-control-process rthread))
           (exit-remote-listener rthread))))
      ((:return local-tag &rest values)
       ;; Note this interrupts the process rather than going through the event mechanism,
       ;; the caller has to set up the callback environment before sending the request.
       (when local-tag
         (apply #'swink:invoke-callback conn local-tag values)))
      ((:cancel-return local-tag)
       (when local-tag
         (swink:abort-callback conn local-tag)))
      (((:read-string :abort-read :write-string) stream-thread-id &rest args)
       ;; Do I/O stuff in the stream listener process, not the caller's listener
       ;; process (which might not even exist)
       (let ((stream-listener (swink:find-thread conn stream-thread-id)))
         (if stream-listener
           (swink:signal-event stream-listener (cons (car event) args))
           (warn "Missing listener for ~s" event))))
      ((:inspect remote-inspector)
       (let ((proxy (new-inspector-proxy conn remote-inspector)))
         (spawn-inspector *application* proxy)))
      ((:inspector-data tag segment)
       (let ((proxy (inspector-proxy-for-tag conn tag)))
         (register-inspector-proxy-segment proxy segment)))
      (t (let ((thread (swink:find-thread conn sender-id)))
           (when thread
             (swink:signal-event thread event))))))
  (swink::log-event "Dispatch-event done"))

(define-condition rlisp-read-aborted ()
  ((tag :initarg :tag :reader rlisp-read-aborted-tag)))

(defun rlisp-read-string (rthread tag)
  (handler-bind ((rlisp-read-aborted (lambda (c)
                                       (when (eql tag (rlisp-read-aborted-tag c))
                                         (return-from rlisp-read-string)))))
    (let ((text (and (swink:with-event-handling (rthread :restart t)
                       (peek-char nil *standard-input* nil)) ;; wait for first one, nil means eof
		     (read-available-text *standard-input*))))
      (swink:send-event (swink:thread-connection rthread) `(:return ,tag ,text)))))


(defclass remote-backtrace-context ()
  ((thread :initarg :thread :reader backtrace-context-thread)
   (break-level :initarg :break-level :reader backtrace-context-break-level)
   (continuable-p :initarg :continuable-p :reader backtrace-context-continuable-p)
   (restarts :initarg :restarts :reader backtrace-context-restarts)))

(defmethod remote-context-class ((application application)) 'remote-backtrace-context)

(defmethod swink:handle-event ((rthread remote-lisp-thread) event)
  (assert (eq (swink:thread-control-process rthread) *current-process*))
  (swink::log-event "Handle-event in thread ~s: ~s" (swink:thread-id rthread) event)
  (swink:destructure-case event
    ((:read-string remote-tag)
     (rlisp-read-string rthread remote-tag))
    ((:abort-read remote-tag)
     (signal 'rlisp-read-aborted :tag remote-tag))
    ((:write-string string)
     (write-string string))
    ((:read-loop level)
     (unless (eql level *break-level*)
       (warn ":READ-LOOP level confusion got ~s expected ~s" level (1+ *break-level*)))
     (invoke-restart 'debug-restart level)) ;; restart at same level, aborted current expression.
    ((:enter-break context-plist)
     (let* ((rcontext (apply #'make-instance (remote-context-class *application*)
                             :thread rthread
                             context-plist))
            (level (backtrace-context-break-level rcontext)))
       (unless (or (eql level 0) (eql level (1+ *break-level*)))
         (warn ":ENTER-BREAK level confusion got ~s expected ~s" level (1+ *break-level*)))
       ;(format t "~&Error: ~a" condition-text)
       ;(when *show-restarts-on-break*
       ;  (format t "~&Remote restarts:")
       ;  (loop for (name description) in restarts
       ;    do (format t "~&~a ~a" name description))
       ;  (fresh-line))
       (unwind-protect
           (progn
             (application-ui-operation *application* :enter-backtrace-context rcontext)
             (rlisp-read-loop rthread :break-level level))
         (application-ui-operation *application* :exit-backtrace-context rcontext))))
    ((:debug-return level) ;; return from level LEVEL read loop
     (invoke-restart 'debug-return level))))

(defmethod make-rrepl-thread ((conn remote-lisp-connection) name)
  (swink:send-event conn `(:spawn-repl ,name)))

(defun connect-to-swink (host port)
  (let* ((socket (make-socket :remote-host host :remote-port port :nodelay t))
         (conn (make-instance 'remote-lisp-connection :control-stream socket)))
    (start-rlisp-process conn)))

(defmethod close ((conn remote-lisp-connection) &key abort)
  ;; TODO: kill process.
  (close (swink:connection-control-stream conn) :abort abort))

(defun read-available-text (stream)
  (loop with buffer = (make-array 100 :element-type 'character :adjustable t :fill-pointer 0)
    for ch = (stream-read-char-no-hang stream)
    until (or (eq ch :eof) (null ch))
    do (vector-push-extend ch buffer)
    finally (return buffer)))

;; Return text for remote evaluation.
(defmethod wait-for-toplevel-form ((stream input-stream)) (peek-char t stream nil))
(defmethod toplevel-form-text ((stream input-stream)) (read-available-text stream))

(defmethod wait-for-toplevel-form ((stream synonym-stream))
  (wait-for-toplevel-form (symbol-value (synonym-stream-symbol stream))))
(defmethod toplevel-form-text ((stream synonym-stream))
  (toplevel-form-text (symbol-value (synonym-stream-symbol stream))))

(defmethod wait-for-toplevel-form ((stream two-way-stream))
  (if (typep stream 'echo-stream)
    (call-next-method)
    (wait-for-toplevel-form (two-way-stream-input-stream stream))))
(defmethod toplevel-form-text ((stream two-way-stream))
  (if (typep stream 'echo-stream)
    (call-next-method)
    (toplevel-form-text (two-way-stream-input-stream stream))))


(defmethod start-remote-listener ((rthread remote-lisp-thread) break-level)
  (when (swink:thread-control-process rthread) (error "Attempting to re-enter active listener"))
  (setf (rthread-break-level rthread) break-level)
  (create-rlisp-listener *application* rthread)
  ;; This is running in the server control process.  Don't process any other events until
  ;; the thread actually starts up.
  (process-wait "REPL startup" #'swink:thread-control-process rthread))

;; This can be invoked when the connection dies or break-loop is exited in a non-repl process.
(defmethod exit-remote-listener ((rthread remote-lisp-thread))
  (application-ui-operation *application* :deactivate-rlisp-listener rthread) ;; deactivate listener window
  (let ((process (swink:thread-control-process rthread)))
    (setf (swink:thread-control-process rthread) nil)
    (when process
      ;; This runs unwind-protects, which should clean up any streams
      (process-kill process))))

;; pass this as the initial-function in make-mcl-listener-process
(defmethod remote-listener-function ((rthread remote-lisp-thread))
  (setf (swink:thread-control-process rthread) *current-process*)
  (unless (or *inhibit-greeting* *quiet-flag*)
    (let ((conn (swink:thread-connection rthread)))
      (format t "~&Welcome to ~A ~A on ~A!"
              (rlisp-lisp-implementation-type conn)
              (rlisp-lisp-implementation-version conn)
              (rlisp-machine-instance conn))))
  (rlisp-read-loop rthread :break-level (rthread-break-level rthread)))

(defmethod create-rlisp-listener ((application application) rthread)
  (assert (null (swink:thread-control-process rthread)))
  ;; see make-mcl-listener-process
  (error "Not implemented yet"))

;; IDE read-loop with remote evaluation.
(defmethod rlisp-read-loop ((rthread remote-lisp-thread) &key break-level)
  (let* ((*break-level* break-level)  ;; used by prompt printing
         (*last-break-level* break-level)  ;; ditto
         (debug-return nil))
    (unwind-protect
        (loop
          (setf (rthread-break-level rthread) break-level)
          (restart-case
              ;; There are some UI actions that invoke local restarts by name, e.g. cmd-/ will invoke 'continue.
              ;; Catch those and just pass them to the remote.  The remote will then do whatever the restart
              ;; does, and will send back unwinding directions if appropriate.
              ;; Do continue with a restart-bind because don't want to abort whatever form is
              ;; about to be sent for evaluation, just in case the continue doesn't end up doing
              ;; anything on the remote end.
              (restart-bind ((continue (lambda () (rlisp/invoke-restart rthread 'continue))))
                (catch :toplevel
                  (loop
                    (catch :abort
                      (loop
                        (catch-cancel ;; exactly like :abort except prints Cancelled.
                         (rlisp-read-loop-internal rthread))
                        (rlisp/invoke-restart rthread 'abort)
                        (format *terminal-io* "~&Cancelled")))
                    (rlisp/invoke-restart rthread 'abort)))
                (rlisp/toplevel rthread))
            ;; These are invoked via invoke-restart-no-return, so must take non-local exit.
            (abort () (rlisp/invoke-restart rthread 'abort))
            (abort-break () (if (eql break-level 0)
                              (rlisp/invoke-restart rthread 'abort)
                              (rlisp/invoke-restart rthread 'abort-break)))
            ;; This is invoked when remote unwinds
            (debug-return (target-level)
               (setq debug-return t)
               (when (eql target-level break-level)
                 (return-from rlisp-read-loop))
               (when (> target-level break-level)
                 (error "Missed target level in debug-return - want ~s have ~s" target-level break-level))
               (invoke-restart 'debug-return target-level))
            (debug-restart (target-level)
               (unless (eql target-level break-level)
                 (when (> target-level break-level)
                   (error "Missed target level in debug-restart - want ~s have ~s" target-level break-level))
                 (setq debug-return t)
                 (invoke-restart 'debug-restart target-level))))
          (clear-input)
          (fresh-line))
      (unless debug-return
        (warn "Unknown exit from rlisp-read-loop!")))))

(defmethod rlisp-read-loop-internal ((rthread remote-lisp-thread))
  (let* ((input-stream *standard-input*)
         (sem (make-semaphore))
         (eof-count 0))
    (loop
      (force-output)
      (print-listener-prompt *standard-output* t)
      
      (swink:with-event-handling (rthread :restart t)
        (wait-for-toplevel-form input-stream))
      (multiple-value-bind (text env) (toplevel-form-text input-stream)
        (if (null text) ;; eof
          (progn
            (when (> (incf eof-count) *consecutive-eof-limit*)
              (#_ _exit 0))
            (unless (and (not *batch-flag*)
                         (not *quit-on-eof*)
                         (stream-eof-transient-p input-stream))
              (exit-interactive-process *current-process*))
            (stream-clear-input input-stream)
            (rlisp/invoke-restart rthread 'abort-break))
          (progn
            (setq eof-count 0)
            ;;(let* ((values (toplevel-eval form env)))
            ;;      (if print-result (toplevel-print values)))
            (let* ((package-name (loop for sym in (car env) for val in (cdr env)
                                   when (eq sym '*package*) do (return val))))
              (if *verbose-eval-selection*
                (let ((state (swink:send-event-for-value rthread
                                                         `(:read-eval-print-one ,text ,package-name)
                                                         :semaphore sem)))
                  (loop while state
                    do (force-output)
                    do (print-listener-prompt *standard-output* t)
                    do (swink:send-event-for-value rthread
                                                   `(:read-eval-print-next ,state)
                                                   :semaphore sem)))
                (swink:send-event-for-value rthread
                                            `(:read-eval-all-print-last ,text ,package-name)
                                            :semaphore sem)))))))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; inspector support

;; TODO: tell server no longer need tag when the proxy gets gc'd.
(defclass remote-inspector-proxy (inspector::inspector)
  ((connection :initarg :connection :reader remote-inspector-proxy-connection)
   (tag :initarg :tag :reader remote-inspector-proxy-tag)
   (string :initarg :string :reader inspector::inspector-object-string)
   (count :initarg :count :reader inspector::inspector-line-count)
   ;; This is accumulating strings info from remote.
   (segments :initform nil)))

(defmethod initialize-instance :after ((proxy remote-inspector-proxy) &rest args)
  (declare (ignore args))
  (let ((conn (remote-inspector-proxy-connection proxy))
        (tag (remote-inspector-proxy-tag proxy)))
    (swink:with-connection-lock (conn)
      (assert (null (gethash tag (connection-proxies conn))))
      (setf (gethash tag (connection-proxies conn)) proxy))))

(defmethod spawn-inspector ((application application) (proxy remote-inspector-proxy))
  ;; Artist conception... we don't really support non-GUI client side, but it would
  ;; go something like this.
  (let* ((conn (remote-inspector-proxy-connection proxy))
         (thread (swink:find-thread conn t :key #'true))) ;; any thread.
    (when thread
      (process-interrupt (swink:thread-control-process thread) 'inspect proxy))))

(defmethod inspector::note-inspecting-item ((proxy remote-inspector-proxy))
  (let ((conn (remote-inspector-proxy-connection proxy))
        (tag (remote-inspector-proxy-tag proxy)))
    (swink:send-event conn `(:inspecting-item ,tag))))

(defmethod inspector::refresh-inspector ((proxy remote-inspector-proxy))
  (let ((conn (remote-inspector-proxy-connection proxy))
        (tag (remote-inspector-proxy-tag proxy)))
    (let ((remote-inspector (swink:send-event-for-value conn `(:refresh-inspector ,tag))))
      (new-inspector-proxy conn remote-inspector))))

(defmethod new-inspector-proxy ((conn remote-lisp-connection) remote-inspector)
  (destructuring-bind (remote-tag new-count . new-string) remote-inspector
    (let ((i (inspector-proxy-for-tag conn remote-tag)))
      (with-slots (string count) i
        ;; The proxy might have already existed, if received some segments for it before we got
        ;; here, but it better be uninitialized.
        (when count (error "Duplicate proxy for ~s" remote-tag))
        (setf string new-string count new-count))
      i)))

(defmethod inspector-proxy-for-tag ((conn remote-lisp-connection) remote-tag)
  (or (gethash remote-tag (connection-proxies conn))
      ;; Make a blank proxy to catch any segments that come in while we're initializing.
      (setf (gethash remote-tag (connection-proxies conn))
            (make-instance 'remote-inspector-proxy
              :connection conn
              :tag remote-tag
              :count nil))))

(defmethod register-inspector-proxy-segment ((proxy remote-inspector-proxy) segment)
  (with-slots (connection segments) proxy
    (swink:with-connection-lock (connection)
      (push segment segments))))

;; Get the strings for given line, pinging server if we don't already have it.
(defmethod remote-inspector-proxy-strings ((proxy remote-inspector-proxy) index)
  (with-slots (connection tag segments) proxy
    ;; No need to lock because we only ever push onto segments.
    (let ((last-segments nil)
          (result nil))
      (flet ((lookup (index segs)
               (loop for tail on segs until (eq tail last-segments)
                 as (start-index . strings) = (car tail) as pos = (- index start-index)
                 when (and (<= 0 pos) (< pos (length strings)))
                 do (progn
                      (setq result (aref strings pos))
                      (return t))
                 finally (setq last-segments segs))))
        (unless (lookup index segments)
          (swink:send-event connection `(:describe-more ,tag ,index))
          (process-wait "Remote Describe" (lambda ()
                                            (and (neq segments last-segments)
                                                 ;; something new has arrived
                                                 (lookup index segments)))))
        result))))


(defclass remote-inspector-line (inspector::inspector)
  ((parent :initarg :parent :reader remote-inspector-line-parent)
   (index :initarg :index :reader remote-inspector-line-index)
   ;; Lazily computed remote inspector proxy
   (proxy :initform nil)))

(defmethod inspector::inspector-line ((proxy remote-inspector-proxy) index)
  (destructuring-bind (label-string . value-string)
                      (remote-inspector-proxy-strings proxy index)
    (values (make-instance 'remote-inspector-line :parent proxy :index index)
            label-string
            value-string)))

(defmethod remote-inspector-line-proxy ((line remote-inspector-line))
  (with-slots (parent index proxy) line
    (or proxy
        (setf proxy
              (with-slots (connection tag) parent
                (let ((remote-inspector
                       (swink:send-event-for-value connection
                                                   `(:line-inspector ,tag ,index))))
                  (new-inspector-proxy connection remote-inspector)))))))

(defmethod inspector::inspector-line-count ((line remote-inspector-line))
  (inspector::inspector-line-count (remote-inspector-line-proxy line)))

(defmethod inspector::inspector-object-string ((line remote-inspector-line))
  (inspector::inspector-object-string (remote-inspector-line-proxy line)))

(defmethod inspector::inspector-line ((line remote-inspector-line) index)
  (inspector::inspector-line (remote-inspector-line-proxy line) index))

(defmethod inspector::note-inspecting-item ((line remote-inspector-line))
  (inspector::note-inspecting-item (remote-inspector-line-proxy line)))
