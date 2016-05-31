;(defun make-move ()
(defun make-move (realtime)
	(setf *actr-enabled-p* t)
	;(run-full-time 1000000 :real-time t)
	(run-full-time 1000000 :real-time (not (null realtime)))
)

(clear-all)

(reset)

(define-model Chunky

(sgp :v t :esc t :lf 0.05 :trace-detail high)

;; Fuer Roboterzustaende. Infos zu Wertebereichen (nicht genanntes wie gewohnt): 
;; orientation {n,e,s,w}, locked {0,...,5}, regN {-1,1,2,3,left,right,uturn}, off {t,nil}, Koordinaten sind absFolut
(chunk-type robot own rname lp hp orientation reg0 reg1 reg2 reg3 reg4 off flag xCurrent yCurrent xRespawn yRespawn)

;; Fuer Tiles des GameBoards. Infos zu Wertebereichen:
(chunk-type tile xCoordinate yCoordinate wallN wallE wallS wallW laserN laserE laserS laserW pusherN pusherE pusherS pusherW flag gear conveyor express workshop)

;; Handkarten
(chunk-type hand card0 card1 card2 card3 card4 card5 card6 card7 card8)

;; Programmkarten
(chunk-type progcard direction range prio index)

;; Start-Ziel-Chunktyp, enthaelt Entfernung vom Ziel
(chunk-type connection fromX fromY fromTurn card index direction range toX toY toTurn diffX diffY diff flag xTurn yTurn pushphase put0 put1 put2 put3 damage)

;; Chunk-Typ fuer den imaginal-buffer; "turn" ist nur gesetzt, wenn es sich um einen imaginaeren Zug handelt
(chunk-type image turn put0 put1 put2 put3 put4 card handcounter regcounter slot1 slot2 slot3 slot4 slot5 slot6 slot7 slot8 slot9 reg0 reg1 reg2 reg3 reg4 card0 card1 card2 card3 card4 card5 card6 card7 card8 rname currDiff con-turn con-right con-left con-forward)

;; chunk-type fuer den goal-buffer
(chunk-type todo state tile flag xCoordinate yCoordinate currRegSlot currHandSlot currIndex won)

;; chunk-type fuer Himmelsrichtungen
(chunk-type direction forward left right turn)

;; chunk-type fuer Waende abhaengig von Blickrichtung
(chunk-type direction-walls direction forward left right turn)

(add-dm

	;; nicht vorhandenes Feld
	(not-there ISA tile)

	;; start-Chunk fuer goal-buffer
	(start ISA chunk)
	(init-locked ISA chunk)
	(init-hand ISA chunk)
	(find-goal ISA chunk)
	(look ISA chunk)
	(load-connection ISA chunk)
	(find ISA chunk)
	(check-taken-path ISA chunk)
	(check-obstacle ISA chunk)
	(check-walls ISA chunk)
	(memorize-destination ISA chunk)
	(check-for-dead-end ISA chunk)
	(conveyor-turn ISA chunk)
	(remove-slots ISA chunk)
	(handle-blank-tile ISA chunk)
	(check-for-blockades ISA chunk)
	(choose ISA	chunk)
	(set-taken-path ISA chunk)
	(put ISA chunk)
	(update ISA chunk)
	(finish ISA chunk)
	(finished ISA chunk)
	(hole ISA chunk)
	(play-randomly ISA chunk)
	(modify-connection ISA chunk)
	
	;; Slot-Chunks
	
	(reg0 ISA chunk)
	(reg1 ISA chunk)
	(reg2 ISA chunk)
	(reg3 ISA chunk)
	(reg4 ISA chunk)
	
	(card0 ISA chunk)
	(card1 ISA chunk)
	(card2 ISA chunk)
	(card3 ISA chunk)
	(card4 ISA chunk)
	(card5 ISA chunk)
	(card6 ISA chunk)
	(card7 ISA chunk)
	(card8 ISA chunk)
	(card9 ISA chunk)
	
	(put0 ISA chunk)
	(put1 ISA chunk)
	(put2 ISA chunk)
	(put3 ISA chunk)
	(put4 ISA chunk)
	
	(express ISA chunk)
	(conveyor ISA chunk)
	
	(pusherN ISA chunk)
	(pusherW ISA chunk)
	(pusherS ISA chunk)
	(pusherE ISA chunk)
	
	(laserN ISA chunk)
	(laserE ISA chunk)
	(laserS ISA chunk)
	(laserW ISA chunk)
	
	(toX ISA chunk)
	(toY ISA chunk)
	(xCoordinate ISA chunk)
	(yCoordinate ISA chunk)
	(xCurrent ISA chunk)
	(yCurrent ISA chunk)
	
	(con-forward ISA chunk)
	(con-left ISA chunk)
	(con-right ISA chunk)
	(con-turn ISA chunk)
	
	(none ISA chunk)
	
	;; Himmelsrichtungen und benachbarte Himmelsrichtungen
	(forward ISA chunk)
	(turn ISA chunk)
	(north ISA direction forward north left west right east turn south)
	(west ISA direction forward west left south right north turn east)
	(south ISA direction forward south left east right west turn north)
	(east ISA direction forward east left north right south turn west)
	
	;; Himmelsrichtungen und benachbarte Waende
	(wallN ISA direction-walls direction north forward wallN left wallW right wallE turn wallS)
	(wallE ISA direction-walls direction east forward wallE left wallS right wallN turn wallW)
	(wallS ISA direction-walls direction south forward wallS left wallE right wallW turn wallN)
	(wallW ISA direction-walls direction west forward wallW left wallN right wallS turn wallE)

	(main ISA todo state start)
	
)

(goal-focus main)

;; roboter laden, gelockte Register pruefen - wenn alle Register gelockt, wird Zug sofort beendet

(P load-robot "Lade Roboter, initialisiere imaginal-buffer"
	=goal>
		ISA		todo
		state	start
	?retrieval>
		state	free
	?imaginal>
		state	free
==>
	=goal>
		state	init-locked
	+imaginal>
		regcounter 4
	+retrieval>
		ISA		robot
		- own	nil
)

(P decrement-regcounter "Verringere regcounter um 1 und setze slot fuer gelockten Register"
	=goal>
		ISA		todo
		state	init-locked
	=imaginal>
		ISA		image
		regcounter	=count
		slot1	nil
	!bind!	=slot1	(read-from-string (concatenate 'string "reg" (write-to-string =count)))
	!bind!	=slot2	(read-from-string (concatenate 'string "put" (write-to-string =count)))
	!bind!	=next	(- =count 1)
==>
	=goal>
	=imaginal>
		regcounter	=next
		slot1	=slot1
		slot2	=slot2
)

(P* set-locked "Setze gelockten Register auf -1, sofern vorhanden"
	=goal>
		ISA		todo
		state	init-locked
	=imaginal>
		ISA		image
		slot1	=slot1
		slot2	=slot2
	=retrieval>
		ISA		robot
		- =slot1	nil
==>
	=goal>
	=imaginal>
		=slot2	-1
		slot1	nil
		slot2	nil
	=retrieval>
)

(P* all-locked "Wenn Register alle gelockt, leite Zugende ein"
	=goal>
		ISA		todo
		state	init-locked
	=imaginal>
		ISA		image
		- put0	nil
		- put1	nil
		- put2	nil
		- put3	nil
		- put4	nil
==>
	=goal>
		state finish
	=imaginal>
)

(P* init-counters "Setze counter auf 0 und entferne Slotnamen-Slots"
	=goal>
		ISA		todo
		state	init-locked
	=imaginal>
		ISA		image
		put0	nil
		slot1	=slot1
	=retrieval>
		ISA		robot
		LP		=lp
		HP		=hp
		=slot1	nil
	!bind! =output (if (> =lp 1) (if (< =hp 5) (read-from-string "POWERDOWN") (read-from-string "STAYACTIVE"))
				   (if (< =hp 7) (read-from-string "POWERDOWN") (read-from-string "STAYACTIVE"))) 
==>
	=goal>
		state	init-hand
	=imaginal>
		handcounter	0
		regcounter	0
		slot1	nil
		slot2	nil
	=retrieval>
	!output!	=output
)

;; Handkarten laden

(P* load-hand "Lade Roboter in imaginal-buffer und Handkarten in retrieval-buffer"
	=goal>
		ISA		todo
		state	init-hand
	=imaginal>
		ISA		image
		handcounter	0
		regcounter	0
	=retrieval>
		ISA		hand
		card0	nil
	?retrieval>
		state	free
==>
	=goal>
	=imaginal> =retrieval
	+retrieval>
		ISA		hand
		- card0	nil
)

(P set-hand "Lade Handkarten in imaginal-buffer"
	=goal>
		ISA		todo
		state	init-hand
	=imaginal>
	=retrieval>
		ISA		hand
		- card0	nil
==>
	=goal>
		state	find-goal
	=imaginal> =retrieval
)

;; Zielfeld finden und laden

(P load-goal-tile "Lade Tile mit als naechstes zu erreichender Zielflagge"
	=goal>
		ISA		todo
		state	find-goal
		flag	nil
	=imaginal>
		ISA		robot
		flag	=flag
	?retrieval>
		state	free
		buffer 	empty   
    !bind! =next (+ =flag 1)
==>
	=goal>
	=imaginal>
		flag	=next
	+retrieval>
		ISA		tile
        flag	=next
		- xCoordinate	nil
)

(P set-goal-tile "Lade Slots der Zieltile in goal-buffer, leere retrieval-buffer"
	=goal>
		ISA		todo
		state 	find-goal
		flag	nil
    =imaginal>
		ISA		robot
        flag	=flag
    =retrieval>
		ISA		tile
		flag	=flag
==>
	=goal> =retrieval
	=imaginal>
		flag	=flag
)

(P goal-tile-not-found "Keine Zielflagge vorhanden: Steuere Flagge 1 an und markiere Spiel als gewonnen"
	=goal>
		ISA		todo
		state 	find-goal
		flag	nil
    =imaginal>
    ?retrieval>
		state	free
		buffer	failure
==>
	=goal>
		won		t
	=imaginal>
		flag	1
	+retrieval>
		ISA		tile
        flag	1
		- xCoordinate	nil
)

(P finish-init "Beende Initialisierungsvorgang; setze Registerslot"
	=goal>
		ISA		todo
		state	find-goal
		xCoordinate	=x
	=imaginal>
		ISA		image
		regcounter	=reg
	!bind! =slot (read-from-string (concatenate 'string "put" (write-to-string =reg)))
==>
	=goal>
		state	look
		xCoordinate	=x
		currRegSlot	=slot
	=imaginal>
)

;; Auf Karten schauen und jeweils naechste auswaehlen

(P select-next-card "Waehle naechste Handkarte und Register aus, Zaehle Zaehler jeweils um 1 nach oben"
	=goal>
		ISA		todo
		state	look
	=imaginal>
		ISA		image
		handcounter	=hand
		slot1	nil
		card	nil
	!bind! =slot1 (read-from-string (concatenate 'string "card" (write-to-string =hand)))
	!bind! =next (+ =hand 1)
==>
	=goal>
	=imaginal>
		handcounter	=next
		slot1	=slot1
)

(P* set-next-card "Setze naechste Karte in imaginal-buffer, sofern moeglich"
	=goal>
		ISA		todo
		state	look
	=imaginal>
		ISA		image
		slot1	=slot1
		=slot1	=card
	?retrieval>
		state	free
==>
	=goal>
		state	load-connection
	=imaginal>
		slot1	nil
		card	=card
	-retrieval>
)

(P* finish-looking "Beende Betrachtungsphase, wenn keine Karten mehr zum Betrachten da"
	=goal>
		ISA		todo
		state	look
	=imaginal>
		ISA		image
		slot1	=slot1
		=slot1	nil
		handcounter	=count
	!bind! =lastCard (- =count 2)
==>
	=goal>
		state	put
	=imaginal>
		currDiff	nil
		handcounter	=lastCard
)

;; Verbindung und Zielfeld berechnen bzw. aus dem dm laden

(P add-card-to-imaginal "Fuege Kartenslots dem imaginal-buffer hinzu"
	=goal>
		ISA		todo
		state	load-connection
	=imaginal>
		card	=card
		direction	nil
		range	nil
==>
	=goal>
	=imaginal> =card
)

(P get-hole-connection-from-dm "Lade Verbindung, die spaeter zu einem Loch fuert aus dm, wenn vorhanden"
	=goal>
		ISA	todo
		state	load-connection
		xCoordinate	=goalX
		yCoordinate	=goalY
		flag	=flag
	=imaginal>
		xCurrent =x
		yCurrent =y
		orientation =turn
		direction	=direction
		range	=range
		regcounter	=count
		slot1	nil
	?retrieval>
		state		free
		- buffer	full
	!bind!	=phase (if (or (= =count 0)(= =count 2)(= =count 4)) 3 2)
==>
	=goal>
	=imaginal>
		slot1		t
	+retrieval>
		ISA	connection
		fromX	=x
		fromY	=y
		fromTurn	=turn
		direction	=direction
		range	=range
		flag	=flag
		diff	1000
		pushphase	=phase
)

(P empty-connection-loaded "Verbindung konnte geladen werden; Gehe zur Ueberpruefung der Verbindung zu diesem Tile"
	=goal>
		ISA		todo
		state	load-connection
	=imaginal>
		card	=card
		index	=index
		slot1	t
	?retrieval>
		buffer	full
==>
	=goal>
		state		check-taken-path
	=imaginal>
		slot8		load-connection
)

(P get-connection-from-dm "Lade Verbindung aus dm, wenn bereits vorhanden"
	=goal>
		ISA	todo
		state	load-connection
		xCoordinate	=goalX
		yCoordinate	=goalY
		flag	=flag
	=imaginal>
		xCurrent =x
		yCurrent =y
		orientation =turn
		direction	=direction
		range	=range
		slot1	t
		regcounter	=count
	?retrieval>
		state	free
		buffer  failure
	!bind!	=phase (if (or (= =count 0)(= =count 2)(= =count 4)) 3 2)
==>
	=goal>
		state	load-connection
	=imaginal>
		slot1	nil
	+retrieval>
		ISA	connection
		fromX	=x
		fromY	=y
		fromTurn	=turn
		direction	=direction
		range	=range
		flag	=flag
		- diff	1000
		pushphase	=phase
)

(P indirect-hole-connection-not-loaded "Verbindung fuert nicht indirekt in ein Loch; fahre normal fort"
	=goal>
		ISA		todo
		state	check-taken-path
	=imaginal>
		slot1		t
		slot2		nil
		slot3		nil
		slot8		=state
	=retrieval>
		ISA		connection
		put0	nil
==>
	=goal>
		state	=state
	=imaginal>
		slot1	nil
		slot8	nil
	=retrieval>
)

(P indirect-hole-connection-loaded "Verbindung fuehrt indirekt in ein Loch; pruefe bisherigen Pfad"
	=goal>
		ISA		todo
		state	check-taken-path
	=imaginal>
		slot1	t
		slot2	nil
		slot3	nil
	=retrieval>
		ISA		connection
		- put0	nil
==>
	=goal>
	=imaginal>
		slot1	nil
		slot2	0
		slot3	put0
	=retrieval>
)

(P* check-indirect-hole-connection "Pruefe, ob die bisher gespielten Karten identisch zu denen der Verbindung sind"
	=goal>
		ISA		todo
		state	check-taken-path
	=imaginal>
		regcounter	=max
		< slot2		=max
		slot2		=count
		slot3		=regslot
		=regslot	=index
	=retrieval>
		ISA		connection
		=regslot	=index
	!bind! =next (+ =count 1)
	!bind! =nextreg (read-from-string (concatenate 'string "put" (write-to-string (+ =count 1))))
==>
	=goal>
	=imaginal>
		slot2	=next
		slot3	=nextreg
	=retrieval>
)

(P indirect-hole-connection-not-true "Wenn gesamte Verbindung nicht passt, versuche stattdessen eine andere Verbindung zu finden"
	=goal>
		ISA		todo
		state	check-taken-path
	=imaginal>
		slot3		=regslot
		=regslot	=index
		slot8		=state
	=retrieval>
		ISA		connection
		- =regslot	=index
	?retrieval>
		state	free
==>
	=goal>
		state	=state
	=imaginal>
		slot1	t
		slot2	nil
		slot3	nil
		slot8	nil
	+retrieval>
		ISA		connection
		diff	2000
)

(P indirect-hole-connection-true "Wenn gesamte Verbindung passt, fahre mit Behandlung der Lochverbindung fort"
	=goal>
		ISA		todo
		state	check-taken-path
	=imaginal>
		regcounter	=max
		slot2		=max
		slot8		=state
==>
	=goal>
		state	=state
	=imaginal>
		slot1	nil
		slot2	nil
		slot3	nil
		slot8	nil
)

(P connection-loaded "Verbindung konnte geladen werden; Ersetze Karte in Chunk durch jetzige und lade Kartenslots in imaginal-buffer"
	=goal>
		ISA		todo
		state	load-connection
	=imaginal>
		card	=card
		index	=index
		slot1	nil
	=retrieval>
==>
	=goal>
		state	choose
	=imaginal>
		card	nil
		direction	nil
		range	nil
		prio	nil
		index	nil
	=retrieval>
		card	=card
		index	=index
)

(P connection-not-loaded "Verbindung konnte nicht geladen werden"
	=goal>
		ISA		todo
		state	load-connection
	=imaginal>
		slot1	nil
	?retrieval>
		state	free
		buffer	failure
==>
	=goal>
		state	find
	=imaginal>
)

(P add-orientation "Lade entsprechenden Orientierungs-Chunk ebenfalls in den imaginal-buffer"
	=goal>
		ISA		todo
		state	find
	=imaginal>
		orientation	=orientation
		forward		nil
		toTurn		nil
==>
	=goal>
	=imaginal> =orientation
)

(P set-move-direction-x "Setze Slots abhaengig von Bewegungsrichtung"
	=goal>
		ISA		todo
		state	find
	=imaginal>
		- orientation north
		- orientation south
		slot1	nil
		slot2	nil
		slot3	nil
		slot4	nil
		- card	nil
		toTurn	nil
==>
	=goal>
	=imaginal>
		slot1	xCurrent
		slot2	toX
		slot3	yCurrent
		slot4	toY
)

(P set-move-direction-y "Setze Slots abhaengig von Bewegungsrichtung"
	=goal>
		ISA		todo
		state	find
	=imaginal>
		- orientation east
		- orientation west
		slot1	nil
		slot2	nil
		slot3	nil
		slot4	nil
		- card	nil
		toTurn	nil
==>
	=goal>
	=imaginal>
		slot1	yCurrent
		slot2	toY
		slot3	xCurrent
		slot4	toX
)

(P* calculate-goal-tile "Gehe in Zielberechnungs-Zustand"
	=goal>
		ISA		todo
		state	find
	=imaginal>
		slot1		=start
		=start		=current
		slot3		=otherstart
		=otherstart	=zz
		slot4		=otherdest
		direction	=direction
		=direction	=newDir
		orientation	=orientation
		range		=range
==>
	=goal>
		state 	check-obstacle
	=imaginal>
		slot3		=current
		slot4		nil
		prio		nil
		left		nil
		right		nil
		=otherdest	=zz
		toTurn	=newDir
		slot9	=range
)

;; Waende und Loecher

(P* range-zero "Wenn range (die Anzahl der zu ueberbrueckenden Felder) gleich 0 ist, gehe zum Laden des Zielfeldes"
	=goal>
		ISA		todo
		state	check-obstacle
	=imaginal>
		slot2	=dest
		slot3	=to
		slot9	=range
		range	0
==>
	=goal>
		state	find
	=imaginal>
		slot1	nil
		slot2	nil
		slot4	nil
		slot3	nil
		slot5	nil
		slot6	nil
		slot7	nil
		slot8	nil
		slot9	nil
		turn		nil
		forward		nil
		range	=range
		=dest	=to
)

(P range-greater-zero "Lade derzeitiges Feld in den retrieval-buffer, veraendere range und gehe zum Betrachen von Waenden/ Loechern"
	=goal>
		ISA		todo
		state	check-obstacle
	=imaginal>
		xCurrent	=x
		yCurrent	=y
		forward		=orientation
		slot1		=start
		slot3		=from
		range		=range
		> range		0
	?retrieval>
		state	free
		buffer	empty
	!bind!	=toX (if (eq =start (read-from-string "xCurrent")) =from =x)
	!bind!	=toY (if (eq =start (read-from-string "yCurrent")) =from =y)
	!bind!	=wall (cond ((eq =orientation (read-from-string "north"))(read-from-string "wallN"))
						((eq =orientation (read-from-string "west"))(read-from-string "wallW"))
						((eq =orientation (read-from-string "south"))(read-from-string "wallS"))
						((eq =orientation (read-from-string "east"))(read-from-string "wallE")))
	!bind!	=plus (if (or (eq =orientation (read-from-string "north")) (eq =orientation (read-from-string "east"))) 1 -1)
	!bind!	=new-range (- =range 1)
==>
	=goal>
		state	check-walls
	=imaginal>
		slot4	=orientation
		slot5	=wall
		slot6	=toX
		slot7	=toY
		slot8	=plus
		range	=new-range
	+retrieval>
		ISA		tile
		xCoordinate		=toX
		yCoordinate		=toY
)

(P range-less-zero "Lade derzeitiges Feld in den retrieval-buffer, veraendere range und gehe zum Betrachen von Waenden/ Loechern"
	=goal>
		ISA		todo
		state	check-obstacle
	=imaginal>
		xCurrent	=x
		yCurrent	=y
		turn		=orientation
		slot1		=start
		slot3		=from
		range		=range
		< range		0
	?retrieval>
		state	free
		buffer	empty
	!bind!	=toX (if (eq =start (read-from-string "xCurrent")) =from =x)
	!bind!	=toY (if (eq =start (read-from-string "yCurrent")) =from =y)
	!bind!	=wall (cond ((eq =orientation (read-from-string "north"))(read-from-string "wallN"))
						((eq =orientation (read-from-string "west"))(read-from-string "wallW"))
						((eq =orientation (read-from-string "south"))(read-from-string "wallS"))
						((eq =orientation (read-from-string "east"))(read-from-string "wallE")))
	!bind!	=plus (if (or (eq =orientation (read-from-string "north")) (eq =orientation (read-from-string "east"))) 1 -1)
	!bind!	=new-range (+ =range 1)
==>
	=goal>
		state	check-walls
	=imaginal>
		slot4	=orientation
		slot5	=wall
		slot6	=toX
		slot7	=toY
		slot8	=plus
		range	=new-range
	+retrieval>
		ISA		tile
		xCoordinate		=toX
		yCoordinate		=toY
)

(P* wall-on-tile "Wenn Wand auf Tile, setze range auf 0, leere retrieval-buffer und brich Vorgang ab"
	=goal>
		ISA		todo
		state	check-walls
	=imaginal>
		slot4	=direction
		slot5	=wall
	=retrieval>
		ISA		tile
		- =wall	nil
==>
	=goal>
		state	check-obstacle
	=imaginal>
		range	0
		slot4	nil
		slot5	nil
)

(P* no-wall-on-current-tile "Wenn keine Wand auf aktueller Tile, lade naechste Tile und pruefe diese auf Waende"
	=goal>
		ISA		todo
		state	check-walls
	=imaginal>
		slot1	=start
		slot3	=from
		slot4	=direction
		slot5	=wall
		slot6	=x
		slot7	=y
		slot8	=plus
	=retrieval>
		ISA		tile
		xCoordinate =x
		yCoordinate	=y
		=wall	nil
	?retrieval>
		state	free
	!bind!	=next-wall (cond ((eq =wall (read-from-string "wallN"))(read-from-string "wallS"))
							 ((eq =wall (read-from-string "wallW"))(read-from-string "wallE"))
							 ((eq =wall (read-from-string "wallS"))(read-from-string "wallN"))
							 ((eq =wall (read-from-string "wallE"))(read-from-string "wallW")))
	!bind!	=toX (if (eq =start (read-from-string "xCurrent")) (+ =from =plus) =x)
	!bind!	=toY (if (eq =start (read-from-string "yCurrent")) (+ =from =plus) =y)
==>
	=goal>
	=imaginal>
		slot5	=next-wall
		slot6	nil
		slot7	nil
	+retrieval>
		ISA		tile
		xCoordinate	=toX
		yCoordinate	=toY
)

(P* no-wall-on-next-tile "Wenn keine Wand auf dem naechsten Tile ist, zaehle Koordinate um 1 weiter und leere retrieval-buffer"
	=goal>
		ISA		todo
		state	check-walls
	=imaginal>
		slot3	=from
		slot5	=wall
		slot6	nil
		slot7	nil
		slot8	=plus
	=retrieval>
		ISA		tile
		- xCoordinate nil
		- yCoordinate nil
		=wall	nil
	?retrieval>
		buffer	full
	!bind!	=to (+ =from =plus)
==>
	=goal>
		state	check-obstacle
	=imaginal>
		slot4	nil
		slot5	nil
		slot6	nil
		slot7	nil
		slot8	nil
		slot3	=to
)

(P hole-on-tile "Wenn auf dem tile ein Loch ist, bereite Laden von leerem tile vor und brich Vorgang ab"
	=goal>
		ISA		todo
		state	check-walls
	=imaginal>
		slot3	=from
		slot8	=plus
	?retrieval>
		buffer	failure
	!bind!	=to (+ =from =plus)
==>
	=goal>
		state	check-obstacle
	=imaginal>
		range	0
		slot3	=to
)

(P load-destination-tile "Lade Zielfeld in retrieval-buffer, sofern vorhanden"
	=goal>
		ISA		todo
		state	find
	=imaginal>
		ISA		connection
		toX		=x
		toY		=y
	?retrieval>
		state	free
==>
	=goal>
		state	memorize-destination
	=imaginal>
		damage	0
	+retrieval>
		ISA		tile
		xCoordinate	=x
		yCoordinate	=y
)

;; Beachte Feldobjekte

(P load-blank-tile "Lade leeren Chunk in retrieval-buffer, wenn Tile nicht vorhanden (neben Spielbrett oder Loch); dann kehre zurueck zum Betrachten der Karten"
	=goal>
		ISA		todo
		state	memorize-destination
	=imaginal>
	?retrieval>
		buffer	failure
==>
	=goal>
	=imaginal>
		damage	nil
	+retrieval> not-there
)

(P create-empty-connection "Erstelle Verbindung zum nicht vorhandenen Tile"
	=goal>
		ISA		todo
		state	memorize-destination
		flag	=flag
	=imaginal>
		xCurrent	=x
		yCurrent	=y
		orientation	=orientation
		card	=card
		index	=index
		direction	=direction
		range	=range
		regcounter	=count
	=retrieval>
		xCoordinate	nil
		yCoordinate	nil
		fromX	nil
		fromY	nil
		fromTurn	nil
	!bind!	=phase (if (or (= =count 0)(= =count 2)(= =count 4)) 3 2)
==>
	=goal>
		state	handle-blank-tile
	=imaginal>	
		toX			nil
		toY			nil
		toTurn		nil
		card		nil
		index		nil
		direction	nil
		range	nil
	=retrieval>
		fromX	=x
		fromY	=y
		fromTurn	=orientation
		card	=card
		index	=index
		direction	=direction
		range	=range
		flag	=flag
		diff	1000
		pushphase	=phase
)

(P handle-blank-tile-first "Waehle Karte aus, sofern noch keine Karte ausgewaehlt wurde, lege Entfernung auf 1000 fest"
	=goal>
		ISA		todo
		state	handle-blank-tile
		currHandSlot	nil
		currIndex	nil
	=imaginal>
		handcounter	=count
	=retrieval>
		ISA		connection
		index	=index
	!bind! =slot (read-from-string (concatenate 'string "card" (write-to-string (- =count 1))))
==>
	=goal>
		state	look
		currHandSlot	=slot
		currIndex	=index
	=imaginal>
		currDiff	1000
)

(P handle-blank-tile "Waehle Karte nicht aus, sofern schon eine Karte ausgewaehlt wurde"
	=goal>
		ISA		todo
		state	handle-blank-tile
		- currHandSlot	nil
		- currIndex	nil
==>
	=goal>
		state	look
	-retrieval>
)

(P handle-express-conveyor "Berechnet neues Zielfeld, wenn derzeitiges Zielfeld Expressfoerderband"
	=goal>
		ISA		todo
		state	memorize-destination
	=imaginal>
		toTurn	=orientation
	=retrieval>
		ISA		tile
		- express 	nil
		conveyor 	=conveyor
	?retrieval>
		state	free
==>
	=goal>
		state	conveyor-turn
	=imaginal>
		conveyor 	=conveyor
		slot1	express
		slot2	not-there
	=retrieval>
		xCoordinate		nil
		yCoordinate		nil
	+retrieval> =orientation
)

(P handle-conveyor "Berechnet neues Zielfeld, wenn derzeitiges Zielfeld Foerderband"
	=goal>
		ISA		todo
		state	memorize-destination
	=imaginal>
		toTurn	=orientation
	=retrieval>
		ISA		tile
		express 	nil
		conveyor 	=conveyor
	?retrieval>
		state	free
==>
	=goal>
		state	conveyor-turn
	=imaginal>
		conveyor 	=conveyor
		slot1	express
		slot2	conveyor
	=retrieval>
		xCoordinate		nil
		yCoordinate		nil
	+retrieval> =orientation
)

(P load-conveyor-directions "Lade die Nachbarrichtungen des Foerderbandes"
	=goal>
		ISA		todo
		state	conveyor-turn
	=imaginal>
		forward	nil
		left	nil
		right	nil
		turn	nil
		toX		=x
		toY		=y
		conveyor 	=conveyor
		turn	nil
	=retrieval>
		ISA		direction
		forward	=forward
		left	=left
		right	=right
		turn	=turn
	?retrieval>
		state	free
==>
	=goal>
	=imaginal>
		forward	=forward
		left	=left
		right	=right
		turn	=turn
	+retrieval> =conveyor
)

(P load-conveyor-destination "Lade das Ziel des Foerderbands und uebernehme die Richtungs-slot des Foerderbands"
	=goal>
		ISA		todo
		state	conveyor-turn
	=imaginal>
		- forward	nil
		- left	nil
		- right	nil
		- turn	nil
		con-forward	nil
		con-left	nil
		con-right	nil
		con-turn	nil
		toX		=x
		toY		=y
		conveyor 	=conveyor
	=retrieval>
		ISA		direction
		forward	=forward
		left	=left
		right	=right
		turn	=turn
	?retrieval>
		state	free
	!bind!	=toX (cond ((eq =conveyor (read-from-string "east"))(+ =x 1))
					   ((eq =conveyor (read-from-string "west"))(- =x 1))
					   (t =x))
	!bind!	=toY (cond ((eq =conveyor (read-from-string "north"))(+ =y 1))
					   ((eq =conveyor (read-from-string "south"))(- =y 1))
					   (t =y))
==>
	=goal>
	=imaginal>
		con-forward	=forward
		con-left	=left
		con-right	=right
		con-turn	=turn
		toX		=toX
		toY		=toY
	+retrieval>
		ISA		tile
		xCoordinate	=toX
		yCoordinate	=toY
)

(P* handle-conveyor-turn-left "Linksdrehung durch Folgefoerderband"
	=goal>
		ISA		todo
		state	conveyor-turn
	=imaginal>
		left	=left
		con-left	=conveyor
		slot1	=remove1
		slot2	=remove2
	=retrieval>
		ISA		tile
		conveyor 	=conveyor
==>
	=goal>
		state	memorize-destination
	=imaginal>
		conveyor	nil
		forward	nil
		left	nil
		right	nil
		turn	nil
		con-forward	nil
		con-left	nil
		con-right	nil
		con-turn	nil
		toTurn	=left
	=retrieval>
		=remove1	nil
		=remove2	nil
)

(P* handle-conveyor-turn-right "Rechtsdrehung durch Folgefoerderband"
	=goal>
		ISA		todo
		state	conveyor-turn
	=imaginal>
		right	=right
		con-right	=conveyor
		slot1	=remove1
		slot2	=remove2
	=retrieval>
		ISA		tile
		conveyor 	=conveyor
==>
	=goal>
		state	memorize-destination
	=imaginal>
		conveyor	nil
		forward	nil
		left	nil
		right	nil
		turn	nil
		con-forward	nil
		con-left	nil
		con-right	nil
		con-turn	nil
		toTurn	=right
	=retrieval>
		=remove1	nil
		=remove2	nil
)

(P* handle-conveyor-no-turn "Keine Drehung durch Folgefoerderband, weil es in gleiche/ entgegengesetzte Richtung zeigt"
	=goal>
		ISA		todo
		state	conveyor-turn
	=imaginal>
		right	=right
		- con-right	=conveyor
		- con-left	=conveyor
		slot1	=remove1
		slot2	=remove2
	=retrieval>
		ISA		tile
		conveyor 	=conveyor
==>
	=goal>
		state	memorize-destination
	=imaginal>
		conveyor	nil
		forward	nil
		left	nil
		right	nil
		turn	nil
		con-forward	nil
		con-left	nil
		con-right	nil
		con-turn	nil
	=retrieval>
		=remove1	nil
		=remove2	nil
)

(P no-conveyor-at-destination "Wenn Foerderband auf kein weiteres Foerderband fuehrt, gehe so zurueck zur Objektbehandlung"
	=goal>
		ISA		todo
		state	conveyor-turn
	=imaginal>
		- turn	nil
		- slot1	nil
	=retrieval>
		ISA		tile
		- xCoordinate	nil
		conveyor 	nil
		express		nil
==>
	=goal>
		state	memorize-destination
	=imaginal>
		conveyor	nil
		forward	nil
		left	nil
		right	nil
		turn	nil
		con-forward	nil
		con-left	nil
		con-right	nil
		con-turn	nil
		slot1	nil
	=retrieval>
)

(P conveyor-has-no-destination "Foerderband fuehrt ins Loch; lade leeren Chunk in retrieval-buffer"
	=goal>
		ISA		todo
		state	conveyor-turn
	=imaginal>
		- turn	nil
		- slot1	nil
		- slot2	nil
	?retrieval>
		state	error
		buffer	failure
==>
	=goal>
		state	memorize-destination
	=imaginal>
		conveyor	nil
		forward	nil
		left	nil
		right	nil
		turn	nil
		con-forward	nil
		con-left	nil
		con-right	nil
		con-turn	nil
		slot1	nil
		slot2	nil
	+retrieval>	not-there
)

(P handle-pusherN "Berechne neues Zielfeld, wenn sich ein aktiver pusher auf dem Feld befindet"
	=goal>
		ISA		todo
		state	memorize-destination
	=imaginal>
		regcounter	=reg
		orientation	=orientation
		toX		=x
		toY		=y
	=retrieval>
		ISA		tile
		xCoordinate	=x
		yCoordinate	=y
		conveyor 	nil
		express 	nil
		pusherN		=pusher
	?retrieval>
		state	free
	!bind!	=toY (if (if (= =pusher 3) (or (= =reg 0) (= =reg 2) (= =reg 4))
									   (or (= =reg 1) (= =reg 3)))
				 (+ =y 1) =y)
==>
	=goal>
		state	remove-slots
	=imaginal>
		toY		=toY
		slot1	express
		slot2	conveyor
		slot3	pusherN
		slot4	pusherW
		slot5	pusherS
		slot6	pusherE
	=retrieval>
		xCoordinate		nil
		yCoordinate		nil
	+retrieval>
		ISA		tile
		xCoordinate	=x
		yCoordinate	=toY
)

(P handle-pusherS "Berechne neues Zielfeld, wenn sich ein aktiver pusher auf dem Feld befindet"
	=goal>
		ISA		todo
		state	memorize-destination
	=imaginal>
		regcounter	=reg
		orientation	=orientation
		toX		=x
		toY		=y
	=retrieval>
		ISA		tile
		xCoordinate	=x
		yCoordinate	=y
		conveyor 	nil
		express 	nil
		pusherS		=pusher
	?retrieval>
		state	free
	!bind!	=toY (if (if (= =pusher 3) (or (= =reg 0) (= =reg 2) (= =reg 4))
									   (or (= =reg 1) (= =reg 3)))
				 (- =y 1) =y)
==>
	=goal>
		state	remove-slots
	=imaginal>
		toY		=toY
		slot1	express
		slot2	conveyor
		slot3	pusherN
		slot4	pusherW
		slot5	pusherS
		slot6	pusherE
	=retrieval>
		xCoordinate		nil
		yCoordinate		nil
	+retrieval>
		ISA		tile
		xCoordinate	=x
		yCoordinate	=toY
)

(P handle-pusherE "Berechne neues Zielfeld, wenn sich ein aktiver pusher auf dem Feld befindet"
	=goal>
		ISA		todo
		state	memorize-destination
	=imaginal>
		regcounter	=reg
		orientation	=orientation
		toX		=x
		toY		=y
	=retrieval>
		ISA		tile
		xCoordinate	=x
		yCoordinate	=y
		conveyor 	nil
		express 	nil
		pusherE		=pusher
	?retrieval>
		state	free
	!bind!	=toX (if (if (= =pusher 3) (or (= =reg 0) (= =reg 2) (= =reg 4))
									   (or (= =reg 1) (= =reg 3)))
				 (+ =x 1) =x)
==>
	=goal>
		state	remove-slots
	=imaginal>
		toX		=toX
		slot1	express
		slot2	conveyor
		slot3	pusherN
		slot4	pusherW
		slot5	pusherS
		slot6	pusherE
	=retrieval>
		xCoordinate		nil
		yCoordinate		nil
	+retrieval>
		ISA		tile
		xCoordinate	=toX
		yCoordinate	=y
)

(P handle-pusherW "Berechne neues Zielfeld, wenn sich ein aktiver pusher auf dem Feld befindet"
	=goal>
		ISA		todo
		state	memorize-destination
	=imaginal>
		regcounter	=reg
		orientation	=orientation
		toX		=x
		toY		=y
	=retrieval>
		ISA		tile
		xCoordinate	=x
		yCoordinate	=y
		conveyor 	nil
		express 	nil
		pusherW		=pusher
	?retrieval>
		state	free
	!bind!	=toX (if (if (= =pusher 3) (or (= =reg 0) (= =reg 2) (= =reg 4))
									   (or (= =reg 1) (= =reg 3)))
				 (- =x 1) =x)
==>
	=goal>
		state	remove-slots
	=imaginal>
		toX		=toX
		slot1	express
		slot2	conveyor
		slot3	pusherN
		slot4	pusherW
		slot5	pusherS
		slot6	pusherE
	=retrieval>
		xCoordinate		nil
		yCoordinate		nil
	+retrieval>
		ISA		tile
		xCoordinate	=toX
		yCoordinate	=y
)

(P* remove-slots "Entferne angegebene Slots"
	=goal>
		ISA		todo
		state	remove-slots
	=imaginal>
		ISA		image
		slot1	=remove1
		slot2	=remove2
		slot3	=remove3
		slot4	=remove4
		slot5	=remove5
		slot6	=remove6
	=retrieval>
		ISA		tile
		- xCoordinate	nil
		- yCoordinate	nil
==>
	=goal>
		state	memorize-destination
	=imaginal>
		slot1	nil
		slot2	nil
		slot3	nil
		slot4	nil
		slot5	nil
		slot6	nil
	=retrieval>
		=remove1	nil
		=remove2	nil
		=remove3	nil
		=remove4	nil
		=remove5	nil
		=remove6	nil
)

(P pusher-has-no-destination "Ziel des Pushers ist ein Loch; Lade leeren Chunk"
	=goal>
		ISA		todo
		state	remove-slots
	=imaginal>
		ISA		image
		- slot1	nil
		- slot2	nil
		- slot3	nil
		- slot4	nil
		- slot5	nil
		- slot6	nil
	?retrieval>
		buffer	failure
==>
	=goal>
		state	memorize-destination
	=imaginal>
		slot1	nil
		slot2	nil
		slot3	nil
		slot4	nil
		slot5	nil
		slot6	nil
	+retrieval>	not-there
)

(P handle-gear "Aendere Zielrichtung, wenn Zielfeld ein Zahnrad"
	=goal>
		ISA		todo
		state	memorize-destination
	=imaginal>
		toTurn	=orientation
	=retrieval>
		ISA		tile
		conveyor 	nil
		express 	nil
		pusherN		nil
		pusherW		nil
		pusherS		nil
		pusherE		nil
		- gear		nil
==>
	=goal>
	=imaginal>
	=retrieval> =orientation
)

(P* move-gear "Aendere Zielrichtung, wenn Zielfeld ein Zahnrad"
	=goal>
		ISA		todo
		state	memorize-destination
	=imaginal>
		orientation	=orientation
	=retrieval>
		ISA		tile
		gear	=gear
		=gear	=direction
==>
	=goal>
	=imaginal>
		toTurn	=direction
	=retrieval>
		gear	nil
		forward	nil
		left	nil
		right	nil
		turn	nil
)

(P handle-laserN "wenn Laser ueber das Zielfeld hinwegschießen, berechne den Schaden, den der Roboter nehmen wuerde"
	=goal>
		ISA		todo
		state	memorize-destination
	=imaginal>
		damage		=damage
	=retrieval>
		ISA		tile
		conveyor 	nil
		express 	nil
		pusherN		nil
		pusherW		nil
		pusherS		nil
		pusherE		nil
		gear		nil
		laserN		=plus
	!bind!	=damage-plus (+ =damage =plus)
==>
	=goal>
	=imaginal>
		damage	=damage-plus
	=retrieval>
		laserN	nil
)

(P handle-laserE "wenn Laser ueber das Zielfeld hinwegschießen, berechne den Schaden, den der Roboter nehmen wuerde"
	=goal>
		ISA		todo
		state	memorize-destination
	=imaginal>
		damage		=damage
	=retrieval>
		ISA		tile
		conveyor 	nil
		express 	nil
		pusherN		nil
		pusherW		nil
		pusherS		nil
		pusherE		nil
		gear		nil
		laserE		=plus
	!bind!	=damage-plus (+ =damage =plus)
==>
	=goal>
	=imaginal>
		damage	=damage-plus
	=retrieval>
		laserE	nil
)

(P handle-laserS "wenn Laser ueber das Zielfeld hinwegschießen, berechne den Schaden, den der Roboter nehmen wuerde"
	=goal>
		ISA		todo
		state	memorize-destination
	=imaginal>
		damage		=damage
	=retrieval>
		ISA		tile
		conveyor 	nil
		express 	nil
		pusherN		nil
		pusherW		nil
		pusherS		nil
		pusherE		nil
		gear		nil
		laserS		=plus
	!bind!	=damage-plus (+ =damage =plus)
==>
	=goal>
	=imaginal>
		damage	=damage-plus
	=retrieval>
		laserS	nil
)

(P handle-laserW "wenn Laser ueber das Zielfeld hinwegschießen, berechne den Schaden, den der Roboter nehmen wuerde"
	=goal>
		ISA		todo
		state	memorize-destination
	=imaginal>
		damage		=damage
	=retrieval>
		ISA		tile
		conveyor 	nil
		express 	nil
		pusherN		nil
		pusherW		nil
		pusherS		nil
		pusherE		nil
		gear		nil
		laserW		=plus
	!bind!	=damage-plus (+ =damage =plus)
==>
	=goal>
	=imaginal>
		damage	=damage-plus
	=retrieval>
		laserW	nil
)

;; Erstellung des connection-Chunks

(P create-connection-chunk "Erstelle Chunk, der die Bewegung darstellt"
	=goal>
		ISA			todo
		state		memorize-destination
		xCoordinate	=goalX
		yCoordinate	=goalY
		flag		=flag
	=imaginal>
		xCurrent	=x
		yCurrent	=y
		orientation	=orientation
		toTurn		=turn
		card		=card
		index		=index
		direction	=direction
		range		=range
		regcounter	=count
		damage		=damage
	=retrieval>
		ISA			tile
		xCoordinate	=toX
		yCoordinate	=toY
		express 	nil
		conveyor 	nil
		pusherN 	nil
		pusherE 	nil
		pusherS 	nil
		pusherW 	nil
		gear 		nil
		laserN		nil
		laserE		nil
		laserS		nil
		laserW		nil
	!bind!	=diffX (- =goalX =toX)
	!bind!	=diffY (- =goalY =toY)
	!bind!	=phase (if (or (= =count 0)(= =count 2)(= =count 4)) 3 2)
==>
	=goal>
	=imaginal>
		toX			nil
		toY			nil
		toTurn		nil
		card		nil
		index		nil
		direction	nil
		range		nil
		damage		nil
		slot1		nil
		slot2		nil
		slot3		nil
		slot4		nil
		slot5		nil
		slot6		nil
		slot7		nil
	=retrieval>
		xCoordinate	nil
		yCoordinate nil
		wallN 		nil
		wallE 		nil
		wallS 		nil
		wallW 		nil
		laserN 		nil
		laserE 		nil
		laserS 		nil
		laserW 		nil
		workshop 	nil
		slot1		nil
		slot2		nil
		slot3		nil
		slot4		nil
		slot5		nil
		slot6		nil
		fromX		=x
		fromY		=y
		fromTurn	=orientation
		card		=card
		index		=index
		direction	=direction
		range		=range
		toX			=toX
		toY			=toY
		toTurn		=turn
		flag		=flag
		diffX		=diffX
		diffY		=diffY
		pushphase	=phase
		damage		=damage
)

(P calculate-diff "Berechne Abstand vom Zielfeld; Lade Richtungs-chunk in imaginal-buffer"
	=goal>
		ISA		todo
		state	memorize-destination
	=imaginal>
	=retrieval>
		toTurn	=turn
		diffX	=diffX
		diffY	=diffY
	!bind! =xTurn (cond ((> =diffX 0) (read-from-string "east")) ((< =diffX 0) (read-from-string "west")) ((eq =diffX 0) (read-from-string "none")))
	!bind! =yTurn (cond ((> =diffY 0) (read-from-string "north")) ((< =diffY 0) (read-from-string "south")) ((eq =diffY 0) (read-from-string "none")))
	!bind! =diff (+ (abs =diffX) (abs =diffY))
==>
	=goal>
	=imaginal>	=turn
	=retrieval>
		diffX	nil
		diffY	nil
		xTurn	=xTurn
		yTurn	=yTurn
		diff	=diff
)

(P goal-reached "Zielflagge erreicht, wenn x- und y-Richtungsabstand beide auf none; initialisiere sofort das Spielen der gewaehlten Karte"
	=goal>
		ISA		todo
		state	memorize-destination
	=imaginal>
		ISA		image
		handcounter	=count
		slot1	nil
	=retrieval>
		xTurn	none
		yTurn	none
	!bind! =nexthand (read-from-string (concatenate 'string "card" (write-to-string =count)))
	!bind! =slot (read-from-string (concatenate 'string "card" (write-to-string (- =count 1))))
	!bind! =next (+ =count 1)
==>
	=goal>
		currHandSlot	=slot
	=imaginal>
		forward	nil
		left	nil
		right	nil
		turn	nil
		currDiff	0
		handcounter =next
		slot1	=nexthand
	=retrieval>
)

(P* count-to-last-handcard "Zaehle handcounter hoch, bis letzte Handkarte erreicht"
	=goal>
		ISA		todo
		state	memorize-destination
	=imaginal>
		ISA		image
		handcounter	=count
		slot1	=slot
		- =slot	nil
	=retrieval>
		xTurn	none
		yTurn	none
	!bind! =nexthand (read-from-string (concatenate 'string "card" (write-to-string =count)))
	!bind! =next (+ =count 1)
==>
	=goal>
	=imaginal>
		handcounter	=next
		slot1	=nexthand
	=retrieval>
)

(P* play-directly "Gehe direkt zum Spielen der derzeitigen Karte; leere retrieval-buffer"
	=goal>
		ISA		todo
		state	memorize-destination
	=imaginal>
		ISA		image
		handcounter =count
		slot1	=slot
		=slot	nil
	=retrieval>
		xTurn	none
		yTurn	none
		index	=index
	!bind! =before (- =count 1)
==>
	=goal>
		state	look
		currIndex	=index
	=imaginal>
		handcounter =before
		slot1	nil
)

(P calculate-diff-value "Berechne Abstand vom Zielfeld als Zahlenwert, dann leere retrieval-buffer und schaue weitere Karten an; keine gerade Linie zum Zielfeld"
	=goal>
		ISA		todo
		state	memorize-destination
	=imaginal>
	=retrieval>
		toTurn	=turn
		- xTurn	none
		xTurn	=xTurn
		- yTurn	none
		yTurn	=yTurn
		diff	=diff
	!bind! =plus (+ =diff (if (or (eq =turn =xTurn) (eq =turn =yTurn)) 0 1))
==>
	=goal>
		state	check-for-dead-end
	=imaginal>
		forward	nil
		left	nil
		right	nil
		turn	nil
	=retrieval>
		xTurn	nil
		yTurn	nil
		diff	=plus
)

(P calculate-diff-value-x "Berechne Abstand vom Zielfeld als Zahlenwert, dann gehe zum Kartenvergleich"
	=goal>
		ISA		todo
		state	memorize-destination
	=imaginal>
		turn	=negTurn
	=retrieval>
		toTurn	=turn
		xTurn	none
		- yTurn	none
		yTurn	=yTurn
		diff	=diff
	!bind! =plus (+ =diff (cond ((eq =yTurn =turn) 0) ((eq =yTurn =negTurn) 0.5) (T 0.75)))
==>
	=goal>
		state	check-for-dead-end
	=imaginal>
		forward	nil
		left	nil
		right	nil
		turn	nil
	=retrieval>
		xTurn	nil
		yTurn	nil
		diff	=plus
)

(P calculate-diff-value-y "Berechne Abstand vom Zielfeld als Zahlenwert, dann gehe zum Kartenvergleich"
	=goal>
		ISA		todo
		state	memorize-destination
	=imaginal>
		turn	=negTurn
	=retrieval>
		toTurn	=turn
		- xTurn	none
		xTurn	=xTurn
		yTurn	none
		diff	=diff
	!bind! =plus (+ =diff (cond ((eq =xTurn =turn) 0) ((eq =xTurn =negTurn) 0.5) (T 0.75)))
==>
	=goal>
		state	check-for-dead-end
	=imaginal>
		forward	nil
		left	nil
		right	nil
		turn	nil
	=retrieval>
		xTurn	nil
		yTurn	nil
		diff	=plus
)

;; Pruefe auf angrenzende Waende und Loecher, erhoehe Kosten der Verbindung entsprechend

(P goal-is-hole "Ziel ist ein Loch; gehe direkt zur Kartenauswahl"
	=goal>
		ISA		todo
		state	check-for-dead-end
	=imaginal>
		slot1	nil
		slot2	nil
	=retrieval>
		ISA		connection
		toX		nil
		toY		nil
		toTurn	nil
==>
	=goal>
		state	choose
	=imaginal>
	=retrieval>
)

(P goal-is-start "Start- und Zielzustand identisch; erhoehe Kosten um 3 und gehe zur Kartenauswahl"
	=goal>
		ISA		todo
		state	check-for-dead-end
	=imaginal>
		slot1	nil
		slot2	nil
	=retrieval>
		ISA			connection
		fromX		=x
		fromY		=y
		fromTurn	=turn
		toX			=x
		toY			=y
		toTurn		=turn
		diff		=diff
	!bind!	=newDiff (+ =diff 3)
==>
	=goal>
		state	choose
	=imaginal>
	=retrieval>
		diff	=newDiff
)

(P goal-is-not-start "Ziel ist ein anderes Tile; Berechne Nachbartilekoordinaten in Flaggenrichtung und lade richtungs-Chunk des Start-Tiles in retrieval-buffer"
	=goal>
		ISA			todo
		state		check-for-dead-end
		xCoordinate =xFlag
		yCoordinate	=yFlag
	=imaginal>
		slot1	nil
		slot2	nil
		slot3	nil
		slot4	nil
		slot5	nil
		slot6	nil
	=retrieval>
		ISA			connection
		fromX		=fromX
		fromY		=fromY
		fromTurn	=fromTurn
		toX			=x
		toY			=y
		toTurn		=turn
		diff		=diff
		pushphase	=pushphase
		damage		=damage
		index		=index
	?retrieval>
		state	free
	!eval!	(not (and (= =fromX =x) (= =fromY =y) (eq =fromTurn =turn)))
	!bind!  =xDiff (cond ((> (- =xFlag =x) 0) 1)
						 ((< (- =xFlag =x) 0) -1)
						 ((= (- =xFlag =x) 0) 0))
	!bind!  =yDiff (cond ((> (- =yFlag =y) 0) 1)
						 ((< (- =yFlag =y) 0) -1)
						 ((= (- =yFlag =y) 0) 0))
==>
	=goal>
	=imaginal>
		toX			=x
		toY			=y
		toTurn		=turn
		diff		=diff
		pushphase	=pushphase
		damage		=damage
		slot1		=xDiff
		slot2		=yDiff
		index		=index
	=retrieval>
		fromX		nil
		fromY		nil
		fromTurn	nil
	+retrieval>		=fromTurn
)

(P load-next-directions "Lade richtungs-Chunk des Zietiles"
	=goal>
		ISA		todo
		state	check-for-dead-end
	=imaginal>
		toTurn	=turn
		slot6	nil
		slot7	nil
	=retrieval>
		ISA			direction
		turn		=uturn
	?retrieval>
		state	free
==>
	=goal>
	=imaginal>
		slot7	=uturn
	+retrieval>		=turn
)

(P load-next-x-Tile "Lade naechstes Tile Richtung Flagge in x-Richtung in den imaginal-buffer"
	=goal>
		ISA		todo
		state	check-for-dead-end
	=imaginal>
		toX		=x
		toY		=y
		- slot1	0
		slot1	=xDiff
		- slot2	nil
		slot3	nil
		slot4	nil
		slot5	nil
		slot6	nil
		- slot7	nil
	=retrieval>
		ISA			direction
		turn		=turn
	?retrieval>
		state	free
	!bind!	=nextX (+ =x =xDiff)
	!bind!	=wall (if (> =xDiff 0) (read-from-string "wallW") (read-from-string "wallE"))
==>
	=goal>
	=imaginal>
		slot5	=wall
		slot6	=turn
	+retrieval>
		ISA		tile
		xCoordinate	=nextX
		yCoordinate =y
)

(P no-next-x-Tile "Zielflagge auf gleicher x-Achse; halte Information fest und gehe zum Laden des naesten Felds in y-Richtung"
	=goal>
		ISA		todo
		state	check-for-dead-end
	=imaginal>
		slot1	0
		- slot2	nil
		slot3	nil
		slot4	nil
		slot6	nil
		- slot7	nil
	=retrieval>
		ISA			direction
		turn		=turn
==>
	=goal>
	=imaginal>
		slot1	nil
		slot3	none
		slot6	=turn
	-retrieval>
)

(P* next-x-tile-has-wall "Naechstes Tile in x-Richtung hat Wand in angegebener Richtung; vermerke entsprechende Richtung"
	=goal>
		ISA		todo
		state	check-for-dead-end
	=imaginal>
		slot1	=xDiff
		- slot2	nil
		slot3	nil
		slot4	nil
		slot5	=wall
	=retrieval>
		ISA		tile
		- xCoordinate	nil
		- yCoordinate	nil
		- =wall			nil
	!bind!	=direction (if (> =xDiff 0) (read-from-string "east") (read-from-string "west"))
==>
	=goal>
	=imaginal>
		slot1	nil
		slot3	=direction
		slot5	nil
)

(P* next-x-tile-has-no-wall "Naechstes Tile in x-Richtung hat keine Wand in angegebener Richtung; vermerke entsprechende Richtung"
	=goal>
		ISA		todo
		state	check-for-dead-end
	=imaginal>
		- slot1	nil
		- slot2	nil
		slot3	nil
		slot4	nil
		slot5	=wall
	=retrieval>
		ISA		tile
		- xCoordinate	nil
		- yCoordinate	nil
		=wall			nil
==>
	=goal>
	=imaginal>
		slot1	nil
		slot3	none
		slot5	nil
)

(P next-x-tile-is-hole "Naechste Tile in x-Richtung ist Loch; vermerke entsprechende Richtung"
	=goal>
		ISA		todo
		state	check-for-dead-end
	=imaginal>
		slot1	=xDiff
		- slot2	nil
		slot3	nil
		slot4	nil
		- slot5	nil
	?retrieval>
		buffer	failure
	!bind!	=direction (if (> =xDiff 0) (read-from-string "east") (read-from-string "west"))
==>
	=goal>
	=imaginal>
		slot1	nil
		slot3	=direction
		slot5	nil
)

(P load-next-y-Tile "Lade naechstes Tile Richtung Flagge in y-Richtung in den imaginal-buffer"
	=goal>
		ISA		todo
		state	check-for-dead-end
	=imaginal>
		toX		=x
		toY		=y
		slot1	nil
		- slot2	0
		slot2	=yDiff
		- slot3	nil
		slot4	nil
		slot5	nil
	?retrieval>
		state		free
	!bind!	=nextY (+ =y =yDiff)
	!bind!	=wall (if (> =yDiff 0) (read-from-string "wallS") (read-from-string "wallN"))
==>
	=goal>
	=imaginal>
		slot5	=wall
	+retrieval>
		ISA		tile
		xCoordinate	=x
		yCoordinate =nextY
)

(P no-next-y-Tile "Zielflagge auf gleicher y-Achse; halte Information fest und gehe zur Auswertung"
	=goal>
		ISA		todo
		state	check-for-dead-end
	=imaginal>
		slot1	nil
		slot2	0
		- slot3	nil
		slot4	nil
==>
	=goal>
	=imaginal>
		slot2	nil
		slot4	none
	-retrieval>
)

(P* next-y-tile-has-wall "Naechstes Tile in y-Richtung hat Wand in angegebener Richtung; vermerke entsprechende Richtung"
	=goal>
		ISA		todo
		state	check-for-dead-end
	=imaginal>
		slot1	nil
		slot2	=yDiff
		- slot3	nil
		slot4	nil
		slot5	=wall
	=retrieval>
		ISA		tile
		- xCoordinate	nil
		- yCoordinate	nil
		- =wall			nil
	!bind!	=direction (if (> =yDiff 0) (read-from-string "north") (read-from-string "south"))
==>
	=goal>
	=imaginal>
		slot2	nil
		slot4	=direction
		slot5	nil
)

(P* next-y-tile-has-no-wall "Naechstes Tile in y-Richtung hat keine Wand in angegebener Richtung; vermerke entsprechende Richtung"
	=goal>
		ISA		todo
		state	check-for-dead-end
	=imaginal>
		slot1	nil
		- slot2	nil
		- slot3	nil
		slot4	nil
		slot5	=wall
	=retrieval>
		ISA		tile
		- xCoordinate	nil
		- yCoordinate	nil
		=wall			nil
==>
	=goal>
	=imaginal>
		slot2	nil
		slot4	none
		slot5	nil
)

(P next-y-tile-is-hole "Naechste Tile in y-Richtung ist Loch; vermerke entsprechende Richtung"
	=goal>
		ISA		todo
		state	check-for-dead-end
	=imaginal>
		slot1	nil
		slot2	=yDiff
		- slot3	nil
		slot4	nil
		- slot5	nil
	?retrieval>
		buffer	failure
	!bind!	=direction (if (> =yDiff 0) (read-from-string "north") (read-from-string "south"))
==>
	=goal>
	=imaginal>
		slot2	nil
		slot4	=direction
		slot5	nil
)

(P no-relevant-blockade "Keine relevante Wand/ relevantes Loch vorhanden; lade Verbindung wieder in retrieval-buffer"
	=goal>
		ISA		todo
		state	check-for-dead-end
	=imaginal>
		toX			=x
		toY			=y
		toTurn		=turn
		diff		=diff
		pushphase	=pushphase
		damage		=damage
		index		=index
		slot1		nil
		slot2		nil
		slot3		none
		slot4		none
		slot5		nil
	?retrieval>
		state	free
==>
	=goal>
	=imaginal>
		toX			nil
		toY			nil
		toTurn		nil
		diff		nil
		pushphase	nil
		damage		nil
		index		nil
		slot5		0
	+retrieval>
		ISA		connection
		fromX		nil
		fromY		nil
		fromTurn	nil
		toX			=x
		toY			=y
		toTurn		=turn
		diff		=diff
		pushphase	=pushphase
		damage		=damage
		index		=index
)

(P two-blockades "Zwei Waende/ Loecher; Setze zusaetzliche Kosten auf 6 und lade Verbindung wieder in retrieval-buffer"
	=goal>
		ISA		todo
		state	check-for-dead-end
	=imaginal>
		toX			=x
		toY			=y
		toTurn		=turn
		diff		=diff
		pushphase	=pushphase
		damage		=damage
		index		=index
		slot1		nil
		slot2		nil
		- slot3		nil
		- slot4		nil
		- slot3		none
		- slot4		none
		slot5		nil
	?retrieval>
		state	free
==>
	=goal>
	=imaginal>
		toX			nil
		toY			nil
		toTurn		nil
		diff		nil
		pushphase	nil
		damage		nil
		index		nil
		slot3		none
		slot4		none
		slot5		6
	+retrieval>
		ISA		connection
		fromX		nil
		fromY		nil
		fromTurn	nil
		toX			=x
		toY			=y
		toTurn		=turn
		diff		=diff
		pushphase	=pushphase
		damage		=damage
		index		=index
)

;; Fallunterscheidung: Berechne jeweils spezifische Zusatzkosten pro Fall

(P blockade-move-not-on-line "Eine Blockade, Verbindungstyp Bewegung, nicht auf einer Linie mit Flagge; addiere Kosten von 1 oder 0,5"
	=goal>
		ISA		todo
		state	check-for-dead-end
		xCoordinate		=flagX
		yCoordinate		=flagY
	=imaginal>
		xCurrent		=currentX
		yCurrent		=currentY
		toX				=x
		toY				=y
		toTurn			=orientation
		diff			=diff
		pushphase		=pushphase
		damage			=damage
		index			=index
		slot1			nil
		slot2			nil
		slot3			=turn1
		- slot4			=turn1
		slot4			=turn2
		slot5			nil
		slot6			=uturn
	?retrieval>
		state	free
	!eval! (or (eq =turn1 (read-from-string "none")) (eq =turn2 (read-from-string "none")))
	!eval! (not (and (= =currentX =x) (= =currentY =y)))
	!eval! (not (or (= =x =flagX) (= =y =flagY)))
	!bind! =plus (if (eq =turn2 (read-from-string "none")) (if (or (eq =turn1 =orientation) (eq =turn1 =uturn)) 1 0.5)
														   (if (or (eq =turn2 =orientation) (eq =turn2 =uturn)) 1 0.5))
==>
	=goal>
	=imaginal>
		toX			nil
		toY			nil
		toTurn		nil
		diff		nil
		pushphase	nil
		damage		nil
		index		nil
		slot3		none
		slot4		none
		slot5		=plus
	+retrieval>
		ISA		connection
		fromX		nil
		fromY		nil
		fromTurn	nil
		toX			=x
		toY			=y
		toTurn		=orientation
		diff		=diff
		pushphase	=pushphase
		damage		=damage
		index		=index
)

(P blockade-move-on-line "Eine Blockade, Verbindungstyp Bewegung, auf einer Linie mit Flagge, vorher noch nicht; addiere Kosten von 6 oder 3"
	=goal>
		ISA		todo
		state	check-for-dead-end
		xCoordinate		=flagX
		yCoordinate		=flagY
	=imaginal>
		xCurrent		=currentX
		yCurrent		=currentY
		toX				=x
		toY				=y
		toTurn			=orientation
		diff			=diff
		pushphase		=pushphase
		damage			=damage
		index			=index
		slot1			nil
		slot2			nil
		slot3			=turn1
		- slot4			=turn1
		slot4			=turn2
		slot5			nil
		slot6			=uturn
	?retrieval>
		state	free
	!eval! (or (eq =turn1 (read-from-string "none")) (eq =turn2 (read-from-string "none")))
	!eval! (not (and (= =currentX =x) (= =currentY =y)))
	!eval! (or (= =x =flagX) (= =y =flagY))
	!eval! (not (or (= =currentX =flagX) (= =currentY =flagY)))
	!bind! =plus (if (eq =turn2 (read-from-string "none")) (if (or (eq =turn1 =orientation) (eq =turn1 =uturn)) 6 3)
														   (if (or (eq =turn2 =orientation) (eq =turn2 =uturn)) 6 3))
==>
	=goal>
	=imaginal>
		toX			nil
		toY			nil
		toTurn		nil
		diff		nil
		pushphase	nil
		damage		nil
		index		nil
		slot3		none
		slot4		none
		slot5		=plus
	+retrieval>
		ISA		connection
		fromX		nil
		fromY		nil
		fromTurn	nil
		toX			=x
		toY			=y
		toTurn		=orientation
		diff		=diff
		pushphase	=pushphase
		damage		=damage
		index		=index
)

(P blockade-move-and-stay-on-line "Eine Blockade, Verbindungstyp Bewegung, auf einer Linie mit Flagge, vorher ebenfalls schon; addiere Kosten von 0 oder -1"
	=goal>
		ISA		todo
		state	check-for-dead-end
		xCoordinate		=flagX
		yCoordinate		=flagY
	=imaginal>
		xCurrent		=currentX
		yCurrent		=currentY
		toX				=x
		toY				=y
		toTurn			=orientation
		diff			=diff
		pushphase		=pushphase
		damage			=damage
		index			=index
		slot1			nil
		slot2			nil
		slot3			=turn1
		- slot4			=turn1
		slot4			=turn2
		slot5			nil
		slot6			=uturn
	?retrieval>
		state	free
	!eval! (or (eq =turn1 (read-from-string "none")) (eq =turn2 (read-from-string "none")))
	!eval! (not (and (= =currentX =x) (= =currentY =y)))
	!eval! (or (= =x =flagX) (= =y =flagY))
	!eval! (or (= =currentX =flagX) (= =currentY =flagY))
	!bind! =plus (if (eq =turn2 (read-from-string "none")) (if (or (eq =turn1 =orientation) (eq =turn1 =uturn)) 0 -1)
														   (if (or (eq =turn2 =orientation) (eq =turn2 =uturn)) 0 -1))
==>
	=goal>
	=imaginal>
		toX			nil
		toY			nil
		toTurn		nil
		diff		nil
		pushphase	nil
		damage		nil
		index		nil
		slot3		none
		slot4		none
		slot5		=plus
	+retrieval>
		ISA		connection
		fromX		nil
		fromY		nil
		fromTurn	nil
		toX			=x
		toY			=y
		toTurn		=orientation
		diff		=diff
		pushphase	=pushphase
		damage		=damage
		index		=index
)

(P blockade-turn-not-on-line "Eine Blockade, Verbindungstyp Drehung, nicht auf einer Linie mit Flagge; addiere Kosten von 1 oder 0"
	=goal>
		ISA		todo
		state	check-for-dead-end
		xCoordinate		=flagX
		yCoordinate		=flagY
	=imaginal>
		xCurrent		=x
		yCurrent		=y
		- orientation	=orientation
		toX				=x
		toY				=y
		toTurn			=orientation
		diff			=diff
		pushphase		=pushphase
		damage			=damage
		index			=index
		slot1			nil
		slot2			nil
		slot3			=turn1
		- slot4			=turn1
		slot4			=turn2
		slot5			nil
		slot6			=uturn
	?retrieval>
		state	free
	!eval! (or (eq =turn1 (read-from-string "none")) (eq =turn2 (read-from-string "none")))
	!eval! (not (or (= =x =flagX) (= =y =flagY)))
	!bind! =plus (if (eq =turn2 (read-from-string "none")) (if (or (eq =turn1 =orientation) (eq =turn1 =uturn)) 1 0)
														   (if (or (eq =turn2 =orientation) (eq =turn2 =uturn)) 1 0))
==>
	=goal>
	=imaginal>
		toX			nil
		toY			nil
		toTurn		nil
		diff		nil
		pushphase	nil
		damage		nil
		index		nil
		slot3		none
		slot4		none
		slot5		=plus
	+retrieval>
		ISA		connection
		fromX		nil
		fromY		nil
		fromTurn	nil
		toX			=x
		toY			=y
		toTurn		=orientation
		diff		=diff
		pushphase	=pushphase
		damage		=damage
		index		=index
)

(P blockade-turn-on-line-turned-at-wall "Eine Blockade, Verbindungstyp Drehung, auf einer Linie mit Flagge, zeige auf Wand; addiere Kosten von 3 oder -1"
	=goal>
		ISA		todo
		state	check-for-dead-end
		xCoordinate		=flagX
		yCoordinate		=flagY
	=imaginal>
		xCurrent		=x
		yCurrent		=y
		- orientation	=orientation
		orientation		=turnBefore
		toX				=x
		toY				=y
		toTurn			=orientation
		diff			=diff
		pushphase		=pushphase
		damage			=damage
		index			=index
		slot1			nil
		slot2			nil
		slot3			=turn1
		- slot4			=turn1
		slot4			=turn2
		slot5			nil
		slot6			=uturn
		slot7			=uturnBefore
	?retrieval>
		state	free
	!eval! (or (eq =turn1 (read-from-string "none")) (eq =turn2 (read-from-string "none")))
	!eval! (or (= =x =flagX) (= =y =flagY))
	!eval! (if (eq =turn2 (read-from-string "none")) (or (eq =turn1 =turnBefore) (eq =turn1 =uturnBefore))
													 (or (eq =turn2 =turnBefore) (eq =turn2 =uturnBefore)))
	!bind! =plus (if (eq =turn2 (read-from-string "none")) (if (or (eq =turn1 =orientation) (eq =turn1 =uturn)) 3 -1)
														   (if (or (eq =turn2 =orientation) (eq =turn2 =uturn)) 3 -1))
==>
	=goal>
	=imaginal>
		toX			nil
		toY			nil
		toTurn		nil
		diff		nil
		pushphase	nil
		damage		nil
		index		nil
		slot3		none
		slot4		none
		slot5		=plus
	+retrieval>
		ISA		connection
		fromX		nil
		fromY		nil
		fromTurn	nil
		toX			=x
		toY			=y
		toTurn		=orientation
		diff		=diff
		pushphase	=pushphase
		damage		=damage
		index		=index
)

(P blockade-turn-on-line-not-turned-at-wall "Eine Blockade, Verbindungstyp Drehung, auf einer Linie mit Flagge, zeige nicht auf Wand; addiere Kosten von 6 oder 4"
	=goal>
		ISA		todo
		state	check-for-dead-end
		xCoordinate		=flagX
		yCoordinate		=flagY
	=imaginal>
		xCurrent		=x
		yCurrent		=y
		- orientation	=orientation
		orientation		=turnBefore
		toX				=x
		toY				=y
		toTurn			=orientation
		diff			=diff
		pushphase		=pushphase
		damage			=damage
		index			=index
		slot1			nil
		slot2			nil
		slot3			=turn1
		- slot4			=turn1
		slot4			=turn2
		slot5			nil
		slot6			=uturn
		slot7			=uturnBefore
	?retrieval>
		state	free
	!eval! (or (eq =turn1 (read-from-string "none")) (eq =turn2 (read-from-string "none")))
	!eval! (or (= =x =flagX) (= =y =flagY))
	!eval! (not (if (eq =turn2 (read-from-string "none")) (or (eq =turn1 =turnBefore) (eq =turn1 =uturnBefore))
														  (or (eq =turn2 =turnBefore) (eq =turn2 =uturnBefore))))
	!bind! =plus (if (eq =turn2 (read-from-string "none")) (if (or (eq =turn1 =orientation) (eq =turn1 =uturn)) 6 4)
														   (if (or (eq =turn2 =orientation) (eq =turn2 =uturn)) 6 4))
==>
	=goal>
	=imaginal>
		toX			nil
		toY			nil
		toTurn		nil
		diff		nil
		pushphase	nil
		damage		nil
		index		nil
		slot3		none
		slot4		none
		slot5		=plus
	+retrieval>
		ISA		connection
		fromX		nil
		fromY		nil
		fromTurn	nil
		toX			=x
		toY			=y
		toTurn		=orientation
		diff		=diff
		pushphase	=pushphase
		damage		=damage
		index		=index
)

(P add-blockade-cost "Addiere Blockadenkosten (falls vorhanden) zu Entfernung und gehe zur Kartenauswahl"
	=goal>
		ISA		todo
		state	check-for-dead-end
	=imaginal>
		xCurrent	=x
		yCurrent	=y
		orientation	=orientation
		toX			nil
		toY			nil
		toTurn		nil
		diff		nil
		pushphase	nil
		damage		nil
		slot3		none
		slot4		none
		slot5		=plus
		- slot6		nil
		- slot7		nil
	=retrieval>
		ISA		connection
		fromX		nil
		fromY		nil
		fromTurn	nil
		diff		=diff
	!bind!	=newDiff (+ =diff =plus)
==>
	=goal>
		state	choose
	=imaginal>
		slot3	nil
		slot4	nil
		slot5	nil
		slot6	nil
		slot7	nil
	=retrieval>
		fromX		=x
		fromY		=y
		fromTurn	=orientation
		diff		=newDiff
)

;; Waehle bessere Karte (bisherige oder derzeitige)

(P no-card-to-play "Bisher keine Karte ausgewaehlt; waehle derzeitige Karte aus und leere retrieval-buffer"
	=goal>
		ISA		todo
		state	choose
	=imaginal>
		ISA		image
		currDiff	nil
		handcounter	=count
	=retrieval>
		ISA		connection
		index	=index
		diff	=diff
	!bind! =slot (read-from-string (concatenate 'string "card" (write-to-string (- =count 1))))
==>
	=goal>
		state	look
		currIndex 	=index
		currHandSlot	=slot
	=imaginal>
		currDiff	=diff
)

(P new-card-better "Neue Karte besser; Waehle neue Karte aus"
	=goal>
		ISA		todo
		state	choose
	=imaginal>
		ISA		image
		currDiff	=diff
		handcounter	=count
	=retrieval>
		ISA		connection
		card	=card
		index	=index
		< diff	=diff
		diff	=newDiff
	!bind! =slot (read-from-string (concatenate 'string "card" (write-to-string (- =count 1))))
==>
	=goal>
		state	look
		currIndex 	=index
		currHandSlot	=slot
	=imaginal>
		currDiff	=newDiff
)

(P old-card-better "Alte Karte besser"
	=goal>
		ISA		todo
		state	choose
	=imaginal>
		ISA		image
		currDiff	=diff
	=retrieval>
		ISA		connection
		>= diff	=diff
==>
	=goal>
		state	look
	=imaginal>
)

;; Karte spielen und zur naechsten Registerphase

(P load-chosen-hole-connection "lade Verbindung passend zur ausgewaehlten Karte in retrieval-buffer, die in ein Loch fuehrt"
	=goal>
		ISA		todo
		state	put
		currIndex	=index
		currRegSlot	=regslot
		flag	=flag
	=imaginal>
		=regslot	nil
		xCurrent	=x
		yCurrent	=y
		orientation	=turn
		slot1		=maxhand
		regcounter	=count
	?retrieval>
		state	free
		buffer	empty
	!bind!	=phase (if (or (= =count 0)(= =count 2)(= =count 4)) 3 2)
==>
	=goal>
		state	hole
	=imaginal>
		slot1	t
		slot7	=maxhand
	+retrieval>
		ISA		connection
		index	=index
		fromX	=x
		fromY	=y
		fromTurn	=turn
		flag	=flag
		diff	1000
		pushphase	=phase
)

(P hole-connection-loaded "Verbindung zu einem Loch geladen; Pruefe, ob die Verbindung zu diesem Tile die richtige ist"
	=goal>
		ISA		todo
		state	hole
		won		nil
	=imaginal>
		slot1	t
	=retrieval>
		ISA		connection
		diff	1000
==>
	=goal>
		state	check-taken-path
	=imaginal>
		slot8	hole
	=retrieval>
)

(P load-chosen-connection "lade Verbindung passend zur ausgewaehlten Karte in retrieval-buffer"
	=goal>
		ISA		todo
		state	hole
		currIndex	=index
		currRegSlot	=regslot
		flag	=flag
	=imaginal>
		=regslot	nil
		xCurrent	=x
		yCurrent	=y
		orientation	=turn
		regcounter	=count
		- slot1		nil
	?retrieval>
		state	error
		buffer	failure
	!bind!	=phase (if (or (= =count 0)(= =count 2)(= =count 4)) 3 2)
==>
	=goal>
	=imaginal>
		slot1	nil
	+retrieval>
		ISA		connection
		index	=index
		fromX	=x
		fromY	=y
		fromTurn	=turn
		flag	=flag
		pushphase	=phase
)

(P not-kills-robot "Ausgewaehlte Verbindung fuert in kein Loch und toetet Roboter nicht durch Laser; mache weiter wie gewohnt"
	=goal>
		ISA		todo
		state	hole
	=imaginal>
		HP		=hp
		slot1	nil
		slot2	nil
		slot7	=maxhand
	=retrieval>
		ISA		connection
		< damage	=hp
		- toX		nil
		- toY		nil
		- toTurn	nil
		- diff		1000
==>
	=goal>
		state	put
	=imaginal>
		damage	nil
		slot1	=maxhand
		slot7	nil
	=retrieval>
)

(P falls-into-hole "Wenn es keine andere Moeglichkeit gibt, als in ein Loch zu fahren, mache letzten Zug rueckgaengig"
	=goal>
		ISA		todo
		state	hole
		won		nil
	=imaginal>
		> regcounter	0
		handcounter		=hand
		regcounter		=count
		slot1			nil
		slot2			nil
		slot7			=maxhand
	=retrieval>
		ISA		connection
		toX		nil
		toY		nil
		toTurn	nil
		diff	1000
	!bind! =before (- =count 1)
	!bind! =regslot (read-from-string (concatenate 'string "put" (write-to-string (- =count 1))))
==>
	=goal>
		currRegSlot	=regslot
	=imaginal>
		damage		nil
		regcounter	=before
		slot1		=maxhand
		slot7		nil
)

(P is-killed-by-lasers "Wenn es keine andere Moeglichkeit gibt, als durch Laser zerstoert zu werden, mache diese Verbindung zu einer Lochverbindung"
	=goal>
		ISA		todo
		state	hole
		won		nil
	=imaginal>
		HP		=hp
		slot1	nil
		slot2	nil
		slot7	=maxhand
	=retrieval>
		ISA		connection
		>= damage	=hp
==>
	=goal>
	=imaginal>
		damage	nil
	=retrieval>
		toX		nil
		toY		nil
		toTurn	nil
		damage	nil
		diff	1000
)

(P* load-old-connection "lade Verbindung von letzter Registerphase"
	=goal>
		ISA			todo
		state		hole
		currRegSlot	=regslot
	=imaginal>
		xCurrent		=x
		yCurrent		=y
		orientation		=turn
		regcounter		=count
		=regslot		=index
	?retrieval>
		state	free
		buffer	empty
	!bind!	=phase (if (or (= =count 0)(= =count 2)(= =count 4)) 3 2)
==>
	=goal>
	=imaginal>
		slot2		t
	+retrieval>
		ISA		connection
		- fromX	nil
		- fromY	nil
		- fromTurn	nil
		index	=index
		toX		=x
		toY		=y
		toTurn	=turn
		pushphase	=phase
)

(P* old-connection-loaded "Setze Karte wieder ein, mache evt. erhaltenen Schaden rueckgaengig und markiere Verbindung als Lochverbindung"
	=goal>
		ISA			todo
		state		hole
		flag		=flag
		currRegSlot	=regslot
	=imaginal>
		HP			=hp
		slot1		=lasthand
		=regslot	=index
		regcounter	=count
	=retrieval>
		ISA			connection
		flag		=flag
		index		=index
		card		=new
		fromX		=x
		fromY		=y
		fromTurn	=turn
		damage		=damage
	!bind! =old-hp (- =hp =damage)
==>
	=goal>
		state			set-taken-path
		currHandSlot	nil
		currIndex		nil
	=imaginal>
		handcounter	0
		hp			=old-hp
		xCurrent	=x
		yCurrent	=y
		orientation	=turn
		=lasthand	=new
		=regslot	nil
		slot1		0
		slot2		put0
	=retrieval>
		damage		nil
		toX			nil
		toY			nil
		toTurn		nil
		diff		1000
	!output!	(REMOVE CARD FROM REGISTER =count)
)

(P* set-taken-path "Setze die Karten ein, welche bis zu diesem Zeitpunkt gespielt wurden und gib ggf. Power-Down-Request aus"
	=goal>
		ISA		todo
		state	set-taken-path
	=imaginal>
		LP			=lp
		HP			=hp
		regcounter	=max
		< slot1		=max
		slot1		=count
		slot2		=regslot
		=regslot	=reg
	=retrieval>
	!bind! =next (+ =count 1)
	!bind! =nextreg (read-from-string (concatenate 'string "put" (write-to-string (+ =count 1))))
	!bind! =output (if (> =lp 1) (if (< =hp 5) (read-from-string "POWERDOWN") (read-from-string "STAYACTIVE"))
				   (if (< =hp 7) (read-from-string "POWERDOWN") (read-from-string "STAYACTIVE"))) 
==>
	=goal>
	=imaginal>
		slot1		=next
		slot2		=nextreg
	=retrieval>
		=regslot	=reg
	!output!	=output
)

(P redo-move "Leere retrieval-buffer und gehe zurueck zur Kartenauswahl"
	=goal>
		ISA		todo
		state	set-taken-path
	=imaginal>
		regcounter	=max
		slot1		=max
==>
	=goal>
		state		look
	=imaginal>
		slot1		nil
		slot2		nil
	-retrieval>
)

(P* old-connection-reached-flag "Wenn die alte Verbindung eine neue Flagge erreicht hat, waehle sie dennoch aus und spiele danach zufaellige Karten"
	=goal>
		ISA			todo
		state		hole
		flag		=flag
		won			nil
	=imaginal>
		xCurrent	=x
		yCurrent	=y
		orientation	=turn
		regcounter	=count
		> LP		1
	=retrieval>
		ISA			connection
		- flag		=flag
	!bind! =next (+ =count 2)
	!bind! =regslot (read-from-string (concatenate 'string "put" (write-to-string (+ =count 1))))
==>
	=goal>
		state			play-randomly
		currHandSlot	card0
		currRegSlot		=regslot
	=imaginal>
		handcounter	1
		regcounter	=next
)

(P old-connection-reached-last-flag "Wenn letzte Flagge erreicht, spiele Karte und spiele weitere Karten zufällig"
	=goal>
		ISA			todo
		state		hole
		won			t
	=imaginal>
		xCurrent		=x
		yCurrent		=y
		orientation		=turn
		regcounter	=count
	=retrieval>
		ISA		connection
		diff	1000
	!bind! =next (+ =count 1)
==>
	=goal>
		state			play-randomly
		currHandSlot	card0
	=imaginal>
		handcounter	1
		regcounter	=next
)

(P* old-connection-reached-flag-no-lives-left "Wenn die alte Verbindung eine neue, aber nicht die letzte Flagge erreicht hat, aber keine Leben mehr verbleiben, spiele Karte nicht"
	=goal>
		ISA			todo
		state		hole
		flag		=flag
		won			nil
	=imaginal>
		LP			1
	=retrieval>
		ISA			connection
		- flag		=flag
		flag		=old-flag
	!bind! =next (+ =old-flag 1)
==>
	=goal>
		state			modify-connection
	=imaginal>
		LP			1
	=retrieval>
)

(P update-flag-connection "Passe Flaggenverbindung an, sodass sie in ein Loch fuehrt"
	=goal>
		ISA			todo
		state		modify-connection
		currRegSlot	=regslot
	=imaginal>
		slot1		=lasthand
		=regslot	=index
	=retrieval>
		ISA			connection
		flag		=flag
		index		=index
		card		=new
		fromX		=x
		fromY		=y
		fromTurn	=turn
		toX			=toX
		toY			=toY
==>
	=goal>
		state			set-taken-path
		currHandSlot	nil
		currIndex		nil
		flag			=flag
		xCoordinate		=toX
		yCoordinate		=toY
	=imaginal>
		handcounter	0
		xCurrent	=x
		yCurrent	=y
		orientation	=turn
		flag		=flag
		=lasthand	=new
		=regslot	nil
		slot1		0
		slot2		put0
	=retrieval>
		toX			nil
		toY			nil
		toTurn		nil
		diff		1000
)

(P kills-robot-first-register "Wenn sogar die erste Registerphase den Roboter toetet, spiele zufaellige Karten"
	=goal>
		ISA			todo
		state		hole
		flag		=flag
		currIndex	=index
	=imaginal>
		put0	nil
		regcounter	0
	=retrieval>
		ISA		connection
		toX		nil
		toY		nil
		toTurn	nil
		diff	1000
==>
	=goal>
		state			play-randomly
		currHandSlot	card0
		currRegSlot		put0
	=imaginal>
		handcounter	1
		regcounter	1
)

(P* play-next-card-randomly "Spiele naechste Karte, sofern noch ein Register frei"
	=goal>
		ISA				todo
		state			play-randomly
		currHandSlot	=hand
		currRegSlot		=reg
	=imaginal>
		handcounter	=count
		=hand		=card
		=reg		nil
	?retrieval>
		state	free
		buffer	empty
	!bind!	=next (+ =count 1)
	!bind!  =handslot (read-from-string (concatenate 'string "card" (write-to-string =count)))
==>
	=goal>
		currHandSlot	=handslot
	=imaginal>
		handcounter		=next
	+retrieval>	=card
)

(P* go-to-next-card "Spiele naechste Karte und gehe eine Karte weiter"
	=goal>
		ISA				todo
		state			play-randomly
		currRegSlot		=reg
	=imaginal>
		regcounter	=count
		=reg		nil
	=retrieval>
		ISA		progcard
		index	=index
	!bind!	=next (+ =count 1)
	!bind!  =regslot (read-from-string (concatenate 'string "put" (write-to-string =count)))
==>
	=goal>
		currRegSlot	=regslot
	=imaginal>
		regcounter	=next
		=reg		=index
)

(P* end-random-turn "Sobald alle Register belegt, beende Zug"
	=goal>
		ISA				todo
		state			play-randomly
	=imaginal>
		- put0	nil
		- put1	nil
		- put2	nil
		- put3	nil
		- put4	nil
==>
	=goal>
		state	finish
	=imaginal>
)

(P* put-card "Lege Karte in naechsten freien Register ab und zaehle Registercounter um 1 hoch"
	=goal>
		ISA		todo
		state	put
		currRegSlot	=regslot
		currIndex	=index
	=imaginal>
		ISA		image
		handcounter	=hand
		regcounter	=reg
	=retrieval>
		- toX			nil
		- toY			nil
		- toTurn		nil
	!bind! =nextReg (+ =reg 1)
	!bind! =lasthand (read-from-string (concatenate 'string "card" (write-to-string =hand)))
==>
	=goal>
		state	update
		currIndex	nil
	=imaginal>
		handcounter	0
		=regslot	=index
		regcounter	=nextReg
		slot1		=lastHand
	=retrieval>
	!output!	(FILL REGISTER =reg WITH CARD =index)
)

(P* update-hand-not-last-card "Passe Handkarten an und lade naechsten Registerslot"
	=goal>
		ISA		todo
		state	update
		currHandSlot	=handslot
	=imaginal>
		ISA		image
		regcounter	=count
		< regcounter 5
		slot1	=lastHand
		=lastHand =card
		- =handslot =card
	!bind! =slot (read-from-string (concatenate 'string "put" (write-to-string =count)))
==>
	=goal>
		currHandSlot	nil
		currRegSlot	=slot
	=imaginal>
		=lastHand	nil
		=handslot	=card
		slot1	nil
)

(P* update-hand-last-card "Passe Handkarten an und lade naechsten Registerslot"
	=goal>
		ISA		todo
		state	update
		currHandSlot	=handslot
	=imaginal>
		ISA		image
		regcounter	=count
		< regcounter 5
		slot1	=lastHand
		=lastHand =card
		=handslot =card
	!bind! =slot (read-from-string (concatenate 'string "put" (write-to-string =count)))
==>
	=goal>
		currHandSlot	nil
		currRegSlot	=slot
	=imaginal>
		=lastHand	nil
		slot1	nil
)

(P update-starting-position "Uebernehme neue Startposition, ziehe Schaden von HP ab, gib ggf. einen Power-Down-Request aus und gehe zur weiteren Kartenauswahl"
	=goal>
		ISA		todo
		state	update
	=imaginal>
		LP		=lp
		HP		=hp
		slot1	nil
	=retrieval>
		ISA		connection
		toX		=x
		toY		=y
		toTurn	=turn
		damage	=damage
		> diff	0
	!bind! =new-hp (- =hp =damage)
	!bind! =output (if (> =lp 1) (if (< =hp 5) (read-from-string "POWERDOWN") (read-from-string "STAYACTIVE"))
				   (if (< =hp 7) (read-from-string "POWERDOWN") (read-from-string "STAYACTIVE"))) 
==>
	=goal>
		state		look
	=imaginal>
		HP			=new-hp
		xCurrent	=x
		yCurrent	=y
		orientation	=turn
		slot1		nil
		slot2		nil
	!output!	=output
)

(P update-starting-position-and-trigger-goal-retrieval "Uebernehme neue Startposition, ziehe Schaden von HP ab, gib ggf. einen Power-Down-Request aus und gehe zur Zielfindungssphase"
	=goal>
		ISA			todo
		state		update
		xCoordinate	=x
		yCoordinate	=y
	=imaginal>
		LP		=lp
		HP		=hp
		slot1	nil
	=retrieval>
		ISA		connection
		toX		=x
		toY		=y
		toTurn	=turn
		damage	=damage
		diff	0
	!bind! =new-hp (- =hp =damage)
	!bind! =output (if (> =lp 1) (if (< =hp 5) (read-from-string "POWERDOWN") (read-from-string "STAYACTIVE"))
				   (if (< =hp 7) (read-from-string "POWERDOWN") (read-from-string "STAYACTIVE"))) 
==>
	=goal>
		state		find-goal
		flag  		nil
		xCoordinate	nil
		yCoordinate	nil
	=imaginal>
		xCurrent	=x
		yCurrent	=y
		orientation	=turn
		slot1		nil
		slot2		nil
	!output!	=output
)

;;Beende Zug

(P end-turn "Wenn Register alle gefuellt, leite Zugende ein"
	=goal>
		ISA		todo
		state	update
	=imaginal>
		ISA		image
		put0	=put0
		put1	=put1
		put2	=put2
		put3	=put3
		put4	=put4
==>
	=goal>
		state finish
	=imaginal>
		put0	=put0
		put1	=put1
		put2	=put2
		put3	=put3
		put4	=put4
)

(P finish "Beende Zug; gib zu spielende Karten auf Konsole aus"
	=goal>
		ISA		todo
		state	finish
	=imaginal>
		put0	=c0
		put1	=c1
		put2	=c2
		put3	=c3
		put4	=c4
==>
	=goal>
		state	finished
	!output!	(FILL REGISTER WITH CARDS =c0 =c1 =c2 =c3 =c4)
	!stop!
)

)