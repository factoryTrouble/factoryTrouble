;TESTCODE ANFANG
; BOARDS
;;;
;;; set Whirlwind Tour
; "tr,ti,ti_ws,ti,ti_ws,ti,ti,ti_ws,ti,ti_ws,ti,ti;ti,ti_ww,ti,ti_ww,ti,ti_ww,ti_ww,ti_ww,ti,ti_ww,ti,ti_ww;ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;ti,ti,ti_wn,ti,ti_wn,ti,ti,ti_wn,ti,ti_wn,ti,ti;ti,ti,ti_ws_pn2,f2,ti_ws_pn3,ti,2n,ti_ws_pn3,ti,ti_ws_pn2,2n,re;2e,2n,2w,2w,2w,2w,2w,2w,2w,2w,2w,ti;ti_ww_pe2,2n,1n,1w,1w,1w,1w,1w,1w,1w,1w,ti_we_pw2;re,2n,1n,2n,2w,2w,2w_ws_ln1,2w,2w,2w,1s,ti;ti_ww_pe3,2n,1n,2n,1n,1w_ws_ln1,1w_ln1,1w,1w,2s,1s,ti_we_pw3;2e,2n,1n,2n_ww_le1,1n_le1,de_le1_ln1,de_le1_ln1,2w_we_le1,1s,2s,1s,f3;1w,2n,1n,2n,1e_ww_le1,de_ln1_le1,de_ln1_le1,2s_le1,1s_we_le1,2s,1s,1w;ti_ww_pe3,2n,1n,2e,2e,2e_ln1,2e_wn_ln1,2s,1s,2s,1s,ti_we_pw3;ti,2n,1e,1e,1e,1e_wn_ln1,1e,1e,1s,2s,1s,re;ti_ww_pe2,2e,2e,2e,2e,2e,2e,2e,2e,2s,1s,ti_we_pw2;ti,1e,1e,1e,1e,1e,1e,1e,1e,1e,1s,1w;re,1s,ti_wn_ps2,ti,ti_wn_ps3,1s,1n,ti_wn_ps3,f1,ti_wn_ps2,ti,ti;"
;;; leeres Board bis auf Flaggen
; "ti,ti,ti_ws,ti,ti_ws,ti,ti,ti_ws,ti,ti_ws,ti,ti;ti,ti_ww,ti,ti_ww,ti,ti_ww,ti_ww,ti_ww,ti,ti_ww,ti,ti_ww;ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;ti,ti,ti_wn,ti,ti_wn,ti,ti,ti_wn,ti,ti_wn,ti,ti;ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,f2,ti;ti,ti,ti,ti,ti,f3,ti,ti,ti,ti,ti,ti;ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;ti,f1,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;"
;;; scrapPress
; "ti_ww_lw3,ti_lw3,ti_lw3,ti_lw3,ti_lw3,ti_lw3,ti_lw3,ti_lw3,ti_lw3,ti_lw3,ti_lw3,ti_we_lw3;ti,ti_ww,ti_ww,ti_ww,ti_ww,ti_ww,ti_ww,ti_ww,ti_ww,ti_ww,ti_ww,ti_ww;ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;ti,ti,ti_wn,ti,ti_wn,ti,ti,ti_wnps3,ti,ti_wn,ti,ti;re,1n,ti_ws,1n,ti_ws,1n,1n,ti_ws,1n,ti_ws,1n,ti;ti,1n,ti,1n,f2,1n,1n,ti,1n,ti,1n,ti;ti_ww,1n_ww_pe2,de,1n_we_pw2,de,1n_we_pw2,1n_ww_pe2,de,1n_ww_pe2,de,1n_we_pw2,ti_we;rr,2w,2w,2w,2w,2w,2e,2e,2e,2e,2e,rl;ti_ww,ti,ti,ti,ti,ti,ti,ti,re,ti,ti,ti_we;ti,rr,ti,ti_we_ws,1n,re,ti,ti,ti,de,ti,ti;1w,1w_wn_ps3,1w,1w,rl,ti,ti,ti,rl,ti,ti,ti;ti_ww,ti,ti,ti_ws_ls2,ti,2e,2e,2e,ti_wn_ps3,ti,ti,ti_we;1e,1e,1e,ti_ls2,ti,ti_we_ww_lw3,ti,ti,1e,1e,de,ti;ti_ww,1e,1e,ti_ls2,re,1s,ti,ti,1s,ti,ti,ti_we;f3,1s,ti,ti_wn_ls2,ti,1s,ti,ti_ww_lw1,1s_lw1,ti_we_lw1,rl,ti;ti,1s,ti_wn,ti,ti_wn,1s,ti,ti_wn,1s,ti_wn,f1,re;"

;;; Roboter: Reihenfolge der Attribute
;;; Name, eigener Roboter, LP, HP, Orientierung, Registerinhalt, Powerdown, Flaggenstatus, Position, Respawn
; Bsp: "hammer,t,2,3,n,nil-nil-a1_200-b1_100-tu_30,nil,2,7:11,1:1;"

;;; Handkarten
;;; Vorwaerts -> 'a1'-'3', Rueckwaerts -> 'b1', Rechtsdrehung -> 'rr', Linksdrehung -> 'rl', Uturn -> 'tu', '_xxx' -> Prioritaet
; Bsp.: "a1_200,a2_300,a3_400,b1_100,tl_50,tr_80,tu_30;"

; Variablen fuer wechselnde Tests: Kodierungen je nach Testfall ersetzen.
(defvar test-board)
(setf test-board "ti_ww_lw3,ti_lw3,ti_lw3,ti_lw3,ti_lw3,ti_lw3,ti_lw3,ti_lw3,ti_lw3,ti_lw3,ti_lw3,ti_we_lw3;ti,ti_ww,ti_ww,ti_ww,ti_ww,ti_ww,ti_ww,ti_ww,ti_ww,ti_ww,ti_ww,ti_ww;ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;ti,ti,ti_wn,ti,ti_wn,ti,ti,ti_wnps3,ti,ti_wn,ti,ti;re,1n,ti_ws,1n,ti_ws,1n,1n,ti_ws,1n,ti_ws,1n,ti;ti,1n,ti,1n,f2,1n,1n,ti,1n,ti,1n,ti;ti_ww,1n_ww_pe2,de,1n_we_pw2,de,1n_we_pw2,1n_ww_pe2,de,1n_ww_pe2,de,1n_we_pw2,ti_we;rr,2w,2w,2w,2w,2w,2e,2e,2e,2e,2e,rl;ti_ww,ti,ti,ti,ti,ti,ti,ti,re,ti,ti,ti_we;ti,rr,ti,ti_we_ws,1n,re,ti,ti,ti,de,ti,ti;1w,1w_wn_ps3,1w,1w,rl,ti,ti,ti,rl,ti,ti,ti;ti_ww,ti,ti,ti_ws_ls2,ti,2e,2e,2e,ti_wn_ps3,ti,ti,ti_we;1e,1e,1e,ti_ls2,ti,ti_we_ww_lw3,ti,ti,1e,1e,de,ti;ti_ww,1e,1e,ti_ls2,re,1s,ti,ti,1s,ti,ti,ti_we;f3,1s,ti,ti_wn_ls2,ti,1s,ti,ti_ww_lw1,1s_lw1,ti_we_lw1,rl,ti;ti,1s,ti_wn,ti,ti_wn,1s,ti,ti_wn,1s,ti_wn,f1,re;")
(defvar test-robot)
(setf test-robot "hammer,t,3,10,n,nil-nil-nil-nil-nil,nil,0,10:8,0:0;")
(defvar test-hand)
(setf test-hand "a1_100,a1_100,a2_100,a1_100,a1_100,a1_100,a2_100,a1_100,a1_100;")
;(setf test-hand "a3_540,tu_50,a1_550,tr_380,a1_560,a1_570,a1_580,tl_130,b1_460")
;TESTCODE ENDE
(defstruct robot 
    rname
    own
    lp
    hp
    orientation
    reg0
    reg1
    reg2
    reg3
    reg4
    off
    flag
    xCurrent
    yCurrent
    xRespawn
    yRespawn
)

(defstruct tile
    xCoordinate
    yCoordinate
    wallN
    wallE
    wallS
    wallW
    laserN
    laserE
    laserS
    laserW
    pusherN
    pusherE
    pusherS
    pusherW
    conveyor
    express
    flag 
    gear
    workshop
)

(defstruct progcard
    direction
    range
    priority
    index
)

(defun set-test () "TEST-METHODE! Setzt Testwerte, die vorher eingetragen sein muessen"
    (set-data test-board test-robot test-hand)
    (return-from set-test nil)
)

(defun set-data (boardCode robotCode handCode) "Initialisierung des Models: Uebersetzen der Zustandskodierungen und Laden ins DM"
	;; Board ins deklarative Memory laden. Slots mit wert NIL gelten als nicht vorhanden und werden daher im deklarativen nicht angezeigt!
	(let ((xx) (yy) (board (set-board boardCode)) (robots (set-robots robotCode)) (hand (set-hand handCode)) (tt) (cname) (index 0) (handchunks))
    
    (setf xx (car (rest (array-dimensions board))))
    (setf yy (car (array-dimensions board)))
    (do ((row 0 (incf row 1)))
        ((= row yy) (return))
        
        (do ((col 0 (incf col 1)))
            ((= col xx) (return xx))
            
            (setf tt (aref board row col))
            (when (not (null (tile-xCoordinate tt)))
            (add-dm-fct 
                (list (list (read-from-string (concatenate 'string "tile" (write-to-string (tile-xCoordinate tt)) "-" (write-to-string (tile-yCoordinate tt)) ))
                'ISA 'tile 'xCoordinate (tile-xCoordinate tt) 'yCoordinate (tile-yCoordinate tt)
                'wallN (tile-wallN tt) 'wallE (tile-wallE tt) 'wallS (tile-wallS tt) 'wallW (tile-WallW tt) 
                'laserN (tile-laserN tt) 'laserE (tile-laserE tt) 'laserS (tile-laserS tt) 'laserW (tile-laserW tt) 
                'pusherN (if (not (null (tile-pusherN tt))) (parse-integer (tile-pusherN tt))) 'pusherE (if (not (null (tile-pusherE tt))) (parse-integer (tile-pusherE tt))) 'pusherS (if (not (null (tile-pusherS tt))) (parse-integer (tile-pusherS tt))) 'pusherW (if (not (null (tile-pusherW tt))) (parse-integer (tile-pusherW tt)))
                'conveyor (tile-conveyor tt) 'express (tile-express tt) 'flag (tile-flag tt) 'gear (tile-gear tt) 'workshop (tile-workshop tt)
                ))
            ))
        )
    )
	
	;; Roboterzustaende ins deklarative Memory laden
    (dolist (rob robots)
        (add-dm-fct 
            (list (list (read-from-string (robot-rname rob)) 'ISA 'robot 'rname (robot-rname rob) 'own (robot-own rob) 'lp (robot-lp rob) 'hp (robot-hp rob) 
            'orientation (robot-orientation rob) 'reg0 (replace-card rob 0) 'reg1 (replace-card rob 1) 'reg2 (replace-card rob 2) 'reg3 (replace-card rob 3) 
            'reg4 (replace-card rob 4) 'off (robot-off rob) 'flag (robot-flag rob) 
            'xCurrent (robot-xCurrent rob) 'yCurrent (robot-yCurrent rob) 'xRespawn (robot-xRespawn rob) 'yRespawn (robot-yRespawn rob)))
        )
    )
    
	;; Handkarten ins deklaratve Memory laden
    (if(null hand);;;
        (return-from set-data nil))
        
    (dolist (card hand)
        (setf cname (read-from-string (concatenate 'string "handcard" (write-to-string index))))
        (setf handchunks (append handchunks (list cname)))
        (add-dm-fct
            (list (list cname 'ISA 'progcard 'direction (progcard-direction card) 'range (progcard-range card) 'prio (progcard-priority card) 'index (progcard-index card)))
        )
        (incf index 1)
    )
    ;; Handkartenchunk setzen
    (add-dm-fct
        (list (list (read-from-string "hand") 'ISA 'hand 'card0 (first handchunks) 'card1 (nth 1 handchunks) 'card2 (nth 2 handchunks) 'card3 (nth 3 handchunks) 
        'card4 (nth 4 handchunks) 'card5 (nth 5 handchunks) 'card6 (nth 6 handchunks) 'card7 (nth 7 handchunks) 'card8 (nth 8 handchunks) ))
    )
    (return-from set-data nil)
))

(defun set-board (code) "Spielfeldkodierung uebersetzen, tile-Structs. Rueckgabe: Board als tile-Array"
    (let ((gBoard (fill-array code)) (xx) (yy) (strTile) (objTile) (board))
        
    (setf yy (car (array-dimensions gBoard)))
    (setf xx (car (rest (array-dimensions gBoard))))
    (setf board (make-array `(,yy ,xx)))
    
    (do ((row 0 (incf row 1)))
        ((= row yy) (return))
        
        (do ((col 0 (incf col 1)))
            ((= col xx) (return xx))
            (setf objTile (make-tile :xCoordinate col :yCoordinate row))
            (setf strTile (split-str (concatenate 'string (aref gBoard row col) "_") "_"))
            
            (dolist (ele strTile)
                (cond ((string= ele "wn") (setf (tile-wallN objTile) T))
                    ((string= ele "we") (setf (tile-wallE objTile) T))
                    ((string= ele "ws") (setf (tile-wallS objTile) T))
                    ((string= ele "ww") (setf (tile-wallW objTile) T))
                    ((string= (subseq ele 0 2) "ln") (setf (tile-laserN objTile) (parse-integer (subseq ele 2))))
                    ((string= (subseq ele 0 2) "le") (setf (tile-laserE objTile) (parse-integer (subseq ele 2))))
                    ((string= (subseq ele 0 2) "ls") (setf (tile-laserS objTile) (parse-integer (subseq ele 2))))
                    ((string= (subseq ele 0 2) "lw") (setf (tile-laserW objTile) (parse-integer (subseq ele 2))))
                    ((string= (subseq ele 0 2) "pn") (setf (tile-pusherN objTile) (subseq ele 2)))
                    ((string= (subseq ele 0 2) "pe") (setf (tile-pusherE objTile) (subseq ele 2)))
                    ((string= (subseq ele 0 2) "ps") (setf (tile-pusherS objTile) (subseq ele 2)))
                    ((string= (subseq ele 0 2) "pw") (setf (tile-pusherW objTile) (subseq ele 2)))
                    ;Feldobjekte setzen
                    ((string= ele "de") (setf (tile-xCoordinate objTile) nil)
                        (setf (tile-yCoordinate objTile) nil) )
                    ((or (string= (subseq ele 0 1) "1") (string= (subseq ele 0 1) "2"))
                        (setf (tile-conveyor objTile) (get-orientation (subseq ele 1)))
                        (if (string= (subseq ele 0 1) "2")
                            (setf (tile-express objTile) T)
                        (setf (tile-express objTile) nil)))
                    ((string= (subseq ele 0 1) "f") (setf (tile-flag objTile) (parse-integer (subseq ele 1)))) ;Flagge setzen
                    ((string= (subseq ele 0 1) "r") ;Zahnrad setzen
                        (if (string= (subseq ele 1) "r") 
                            (setf (tile-gear objTile) (read-from-string "right"))
                        (setf (tile-gear objTile) (read-from-string "left")) ))
                    ((string= ele "re") (setf (tile-workshop objTile) (read-from-string "repair"))) ;Werkstatt setzen
                )
            )
            (setf (aref board row col) objTile)
        )
    )
    (return-from set-board (duplicate-walls board))
))

(defun duplicate-walls (board) "Dupliziert die Waende des Boards: hat ein Tile eine wso, dann bekommt unterer Nachbar wno"
    (let ((xx)(yy)(tile-west)(tile-south)(tile-current))
  
    (setf yy (car (array-dimensions Board)))
    (setf xx (car (rest (array-dimensions Board))))
  
    (do ((row 0 (incf row 1)))
        ((= row yy) (return))
    
        (do ((col 1 (incf col 1)))
            ((= col xx) (return xx))
            (setf tile-west (aref board row (- col 1)))
            (setf tile-current (aref board row col))
            
            (if (and (not(null(tile-wallE tile-west))) (null(tile-WallW tile-current)))
                (setf (tile-wallW tile-current) T))
            (if (and (not(null(tile-wallW tile-current))) (null(tile-wallE tile-west)))
                (setf (tile-wallE tile-west) T))

            (cond ((> row 0)
                (setf tile-south (aref board (- row 1) col))
            
                (if (and (not(null(tile-wallN tile-south))) (null(tile-wallS tile-current)))
                    (setf (tile-wallS tile-current) T))
                (if(and (not(null(tile-wallS tile-current))) (null(tile-wallN tile-south)))
                    (setf (tile-wallN tile-south) T))
                
                (setf (aref board (- row 1) col) tile-south)
            ))
        
            (setf (aref board row col) tile-current)
            (setf (aref board row (- col 1)) tile-west)
        )
    )
    (return-from duplicate-walls board)
))


(defun set-robots (code) "Uebersetzung der Robotercodes, erzeugen von robot-Structs. Ausgabe: robot-Liste"
    (let ((maxY (count #\; code)) (rob (fill-array code)) (regCards) (robots))
    
    (do ((col 0 (incf col 1)))
        ((= col maxY) (return))
        (setf regCards (split-str (concatenate 'string (aref rob col 5) "-") "-"))
        (setf robots (append robots (list (make-robot
            :rname (aref rob col 0)
            :own (string= (aref rob col 1) "t")
            :lp (parse-integer (aref rob col 2))
            :hp (parse-integer (aref rob col 3))
            :orientation (get-orientation (aref rob col 4))
            :reg0 (when (string/= (nth 0 regCards) "nil") (set-register (nth 0 regCards)))
            :reg1 (when (string/= (nth 1 regCards) "nil") (set-register (nth 1 regCards)))
            :reg2 (when (string/= (nth 2 regCards) "nil") (set-register (nth 2 regCards)))
            :reg3 (when (string/= (nth 3 regCards) "nil") (set-register (nth 3 regCards)))
            :reg4 (when (string/= (nth 4 regCards) "nil") (set-register (nth 4 regCards)))
            :off (string= (aref rob col 6) "t")
            :flag (parse-integer (aref rob col 7))
            :xCurrent (parse-integer (car (split-str (concatenate 'string (aref rob col 8) ":") ":")))
            :yCurrent (parse-integer (car (rest (split-str (concatenate 'string (aref rob col 8) ":") ":"))))
            :xRespawn (parse-integer (car (split-str (concatenate 'string (aref rob col 9) ":") ":")))
            :yRespawn (parse-integer (car (rest (split-str (concatenate 'string (aref rob col 9) ":") ":"))))
        ))))
    )
    (return-from set-robots robots)
))

(defun set-register (card) "Hilfsmethode: umsetzen von Kodierung auf Chunk-Slot fuer Programmkarten"
    (let ((regcard) (rcard (split-str (concatenate 'string card "_") "_")) (prefix))

    (setf prefix (first rcard))
    (setf regcard (make-progcard))
        
    (cond ;direction setzen
        ((or (string= (subseq prefix 0 1) "a") (string= prefix "b1")) (setf (progcard-direction regcard) (read-from-string "forward")) )
        ((string= prefix "tl") (setf (progcard-direction regcard) (read-from-string "left")) )
        ((string= prefix "tr") (setf (progcard-direction regcard) (read-from-string "right")) )
        ((string= prefix "tu") (setf (progcard-direction regcard) (read-from-string "turn")) )
    )
    (cond ;range setzen
        ((or (string= prefix "tl") (string= prefix "tr") (string= prefix "tu")) (setf (progcard-range regcard) 0))
        ((string= prefix "b1") (setf (progcard-range regcard) -1))
        ((or (string= prefix "a1") (string= prefix "a2") (string= prefix "a3")) (setf (progcard-range regcard) (parse-integer (subseq prefix 1))))
    )
    (setf (progcard-priority regcard) (parse-integer (car (rest rcard)))) ;priority setzen
    
    (return-from set-register regcard)
))

(defun set-hand (code) "Uebersetzung der Kartencodes, erzeugen von progcard-Structs. Ausgabe: progcard-Liste"
    ;(let ((cards (split-str (concatenate 'string (subseq code 0 (- (length code) 1)) ",") ",")) (card) (hand) (index 0))
    (let ((cards) (card) (hand) (index 0))
    
    (if (string= code ";");;;
        (return-from set-hand nil))
        
    (setf cards (split-str (concatenate 'string (subseq code 0 (- (length code) 1)) ",") ","))
    
    (dolist (cc cards)
        (setf card (set-register cc))
        (setf (progcard-index card) index)
        (incf index 1)
        
        (setf hand (append hand (list card)))
    )
    (return-from set-hand hand)
))

(defun get-orientation (orient)
    (cond ((string= orient "n") (return-from get-orientation (read-from-string "north")))
        ((string= orient "e") (return-from get-orientation (read-from-string "east")))
        ((string= orient "s") (return-from get-orientation (read-from-string "south")))
        ((string= orient "w") (return-from get-orientation (read-from-string "west")))
    )
)

(defun replace-card (rob register)
    (let ((cardname (read-from-string (concatenate 'string (robot-rname rob) "-reg" (write-to-string register)))) (card))
    
    (case register
        (0 (setf card (robot-reg0 rob)) )
        (1 (setf card (robot-reg1 rob)) )
        (2 (setf card (robot-reg2 rob)) )
        (3 (setf card (robot-reg3 rob)) )
        (4 (setf card (robot-reg4 rob)) )
    )
    
    (when (null card) (return-from replace-card nil))
    (add-dm-fct
        (list (list cardname 'ISA 'progcard 'direction (progcard-direction card) 'range (progcard-range card) 'prio (progcard-priority card) ))
    )
    (return-from replace-card cardname)
))

(defun fill-array (code) "Hilfsmethode zum Auffuellen des Board- und Robots-Arrays"
    (let ((xx) (yy (count #\; code)) (once (split-str code ";")) (line) (temp))
    
    (setf xx (/ (+ (count #\, code) yy) yy))
    (setf temp (make-array `(,yy ,xx)))
    
    (do ((row 0 (incf row 1)))
        ((= row yy) (return))
        (setf line (split-str (concatenate 'string (nth row once) ",") ","))
        (do ((col 0 (incf col 1)))
            ((= col xx) (return xx))
            (setf (aref temp row col) (nth col line))
        )
    )
    (return-from fill-array temp)
))

(defun split-str (string sep) "Splitted String gemaeß sep. Rueckgabe: Liste"
    (let ((pos (search sep string)) (splitted) (next))    
    
    (setf splitted (list (subseq string 0 pos)))
    (setf next (subseq string (+ pos 1)))
        
    (if (string/= next "")
        (loop
            (setf pos (search sep next))
            (setf splitted (append splitted (list (subseq next 0 pos))))
            (setf next (subseq next (+ pos 1)))
       
            (when (< (length next) 2) (return splitted))
        )
    )
    (return-from split-str splitted)
))