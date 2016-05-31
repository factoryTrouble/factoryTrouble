(defvar *response* nil)
(defvar *response-time* nil)

(defconstant *unit4-exp-data* '(2.25 2.8  2.3  2.75
                                2.55 2.95 2.55 2.95))

(defvar *demo4-study-set*
   '("The painter visited the missionary"
     "The missionary refused the painter"
     "The painter was chased by the missionary"
     "The painter was protected by the sailor"
     "The missionary shot the sailor"
     "The cannibal questioned the painter"
     "The missionary was accused by the painter"
     "The cannibal was feared by the missionary"))

(defvar *demo4-test-set*
   '((1 "k" "The painter visited the missionary")
     (2 "k" "The painter was refused by the missionary")
     (3 "k" "The missionary chased the painter")
     (4 "k" "The painter was protected by the sailor")
     (5 "d" "The sailor shot the missionary")
     (6 "d" "The cannibal was questioned by the painter")
     (7 "d" "The missionary accused the painter")
     (8 "d" "The missionary was feared by the cannibal")))


(defun study-sentence (text time)
  (if *actr-enabled-p*
      (study-sentence-model text time)
    (study-sentence-person text time)))

(defun study-sentence-model (text time)
  (let ((window (open-exp-window "Sentence Experiment" :visible t :width 500)))
    (install-device window)
    (add-text-to-exp-window :text text :x 25 :y 150 :width 250)
    (proc-display :clear t)
    (run-full-time time :real-time t)))

(defun study-sentence-person (text time)
  (let ((window (open-exp-window "Sentence Experiment" :visible t :width 500)))
    (add-text-to-exp-window :text text :x 25 :y 150 :width 250)
    (sleep time)))

(defun test-sentence (test)
  (if *actr-enabled-p*
      (test-sentence-model test)
    (test-sentence-person test)))
 
(defun test-sentence-model (test)
  (let ((window (open-exp-window "Sentence Experiment" :visible t :width 500)))
    (install-device window)
    (add-text-to-exp-window :text (third test) :x 25 :y 150 :width 250)
    (proc-display :clear t)
    
    (setf *response-time* nil)
    (setf *response* nil)
    
    (let ((start-time (get-time)))
      (run 30 :real-time t)
      (setf *response-time* (if *response-time*  
                                (- *response-time* start-time)
                              30000)))
    
    (list (first test) (/ *response-time* 1000.0)
          (string-equal *response* (second test)))))

(defun test-sentence-person (test)
  (let ((window (open-exp-window "Sentence Experiment" :visible t :width 500)))
    (add-text-to-exp-window :text (third test) :x 25 :y 150 :width 250)
    
    (setf *response-time* nil)
    (setf *response* nil)
    (sgp :v nil)

    (let ((start-time (get-time))) 
      (while (null *response*)
             (allow-event-manager window))
      (setf *response-time* (- *response-time* start-time)))
    
    (list (first test) (/ *response-time* 1000.0)
          (string-equal *response* (second test)))))

(defun study-sentences (set time)
   (dolist (x set)
     (study-sentence x time)))

(defun test-sentences (set)
   (let ((results nil))
     (dolist (x set)
       (push (test-sentence x) results))
     (mapcar #'cdr (sort results #'< :key #'first))))

(defun do-experiment (&key (in-order t))
  (let ((study (if in-order
                   *demo4-study-set*
                 (permute-list *demo4-study-set*)))
        (tests (if in-order
                   *demo4-test-set*
                 (permute-list *demo4-test-set*))))
    
    (reset)

    (setf *actr-enabled-p* t)
    (study-sentences study 5)
    (study-sentence "                            test" 2)
    (report-data (test-sentences tests))
))

 (defun report-data (lis)
   (let ((rts (mapcar #'first lis)))
     (correlation rts *unit4-exp-data*)
     (mean-deviation rts *unit4-exp-data*)
     (format t "          Active-Active Active-Passive Passive-Active Passive-Passive~%")

     (format t "True: ~:{~9,2F (~3s)~}~%" (subseq lis 0 4))
     (format t "False:~:{~9,2F (~3s)~}~%" (subseq lis 4 8))))


(defmethod rpm-window-key-event-handler ((win rpm-window) key)
  (setf *response-time* (get-time))
  (setf *response* (string key)))

(clear-all)
(reset)

(define-model comPro

(sgp :v t :esc t :lf 0.15 :ga 0.0 :show-focus t)
(chunk-type comprehend-sentence agent action object purpose word state passive)
(chunk-type meaning word (mean t))

(add-dm
    (painter ISA meaning word "painter")
    (missionary ISA meaning word "missionary")
    (sailor ISA meaning word "sailor")
    (cannibal ISA meaning word "cannibal")
    (visit ISA meaning word "visited")
    (refuse ISA meaning word "refused")
    (chase ISA meaning word "chased")
    (shoot ISA meaning word "shot")
    (question ISA meaning word "questioned")
    (accuse ISA meaning word "accused")
    (fear ISA meaning word "feared")
    (protect ISA meaning word "protected")
    (test ISA meaning word "test")
    (goal ISA comprehend-sentence purpose "study"))

(P found-new-word
    =goal>
       ISA         comprehend-sentence
       state       nil
    =visual-location>
    ?visual>
       state    free
==>
   -manual>
   =goal>
       state       "attending"
    +visual>
       ISA         move-attention
       screen-pos  =visual-location
)

(P find-next-word
    =goal>
       ISA         comprehend-sentence
       state       "find"
==>
    +visual-location>
       ISA         visual-location		> screen-x 	current
       :nearest     current
    =goal>
       state       "looking"
)

(P attend-word
    =goal>
       ISA         comprehend-sentence
       state       "looking"
    =visual-location>
    ?visual>
       state    free
==>
    =goal>
       state       "attending"
    +visual>
       ISA         move-attention
       screen-pos  =visual-location
)

(P read-word
    =goal>
       ISA         comprehend-sentence
       state       "attending"
       word        nil
    =visual>
       ISA         visual-object
       value       =word
==>
    -visual-location>
    =goal>
       word        =word
       state       "read"
    +retrieval>
       mean	   t
       word        =word
)

(P skip-the
    =goal>
       ISA         comprehend-sentence
       word        "the"
       state       "read"
==>
    =goal>
       state       "find"
       word        nil
   )

(P check-was
    =goal>
       ISA         comprehend-sentence
       word        "was"
       state       "read"              passive      nil
==>
    =goal>
       state       "find"
       word        nil              passive     "was"
   )

(P was-by
    =goal>
       ISA         comprehend-sentence
       word        "by"
       state       "read"              passive     "was"
==>
    =goal>
       state       "find"
       word        nil              passive     "true"
   )   (P skip-by    =goal>       ISA         comprehend-sentence       word        "by"       state       "read"              passive     nil==>    =goal>       state       "find"       word        nil          )


(P respond-to-test
    =goal>
       ISA         comprehend-sentence
       word        "test"
    =retrieval>
       mean           t
       word        "test"
==>
   -visual>
   +goal>
       ISA         comprehend-sentence
       purpose     "test"
)

(P process-first-noun
    =goal>
       ISA         comprehend-sentence
       agent       nil
       action      nil
       word        =word
     - word        "test"
       state       "read"
    =retrieval>
       mean           t
       word        =word
==>
    =goal>
       agent       =retrieval
       word        nil
       state       "find"
)

(P process-verb
    =goal>
       ISA         comprehend-sentence
       agent       =val
       action      nil
       word        =word
       state       "read"
    =retrieval>
       mean           t
       word        =word
==>
    =goal>
       action      =retrieval
       word        nil
       state       "find"
)

(P process-last-word-object
    =goal>
       ISA         comprehend-sentence
       object      nil
       agent       =agent
       action      =action
       word        =word
       state       "read"
    =retrieval>
       mean           t
       word        =word
==>
    =goal>
       state       "is-passive"
       object      =retrieval
)(P sentence-active             =goal>              ISA         comprehend-sentence              agent       =agent              object      =object              action      =action              passive     nil              state       "is-passive"       ==>          =goal>                state      "sentence-complete")(P sentence-passive             =goal>              ISA         comprehend-sentence              agent       =agent              object      =object              action      =action              passive     "true"              state       "is-passive"       ==>          =goal>                state      "sentence-complete"                object     =agent                agent      =object                passive    nil)


(P study-sentence-read-wait
    =goal>
       ISA         comprehend-sentence
       state       "sentence-complete"
       purpose     "study"
==>
    -visual>
    +goal>
       ISA         comprehend-sentence
       purpose     "study"
)(P remember-sentence        =goal>                ISA        comprehend-sentence                state      "sentence-complete"                agent      =agent                object     =object                action     =action                purpose    "test"          ==>    +retrieval>                agent      =agent                object     =object                action     =action    =goal>            state      "compare-sentence")(P compare-sentence    =goal>            ISA         comprehend-sentence                agent      =agent                object     =object                action     =action              state       "compare-sentence"          =retrieval>                agent      =agent                object     =object                action     =action    ==>      =goal>               state     "press-k" )(P press-k          =goal>               state     "press-k"               ?manual>                 state     free==>      +manual>              ISA        press-key                  key        "K"               -visual>      +goal>         ISA         comprehend-sentence         purpose     "test")
(P unknown-sentence     =goal>            ISA         comprehend-sentence              state       "compare-sentence"     ?retrieval>             state       error==>    =goal>                state       "press-d")
(P press-d          =goal>               state     "press-d"               ?manual>                 state     free==>      +manual>              ISA        press-key                  key        "D"               -visual>      +goal>         ISA         comprehend-sentence         purpose     "test")
 (set-visloc-default isa visual-location :attended new screen-x lowest)


(setf *actr-enabled-p* t)
(goal-focus goal))