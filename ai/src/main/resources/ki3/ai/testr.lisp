(defun set-board (board-state)
)
		
(defun make-move (hand-state robots-state)
	(setf *actr-enabled-p* t)
	(run-full-time 100 :real-time t)
)

(clear-all)

(reset)
	
(define-model test

(sgp :v t :esc t :lf 0.05 :trace-detail high)

(chunk-type hand hand card1 card2 card3 card4 card5 card6 card7 card8 card9)

(chunk-type reg reg rcard1 rcard2 rcard3 rcard4 rcard5)

(chunk-type order num1 num2)

(chunk-type todo tsk card count next phase)

;; Zum Zwischenspeichern von Ergebnissen/ gelockten Registern
;;(chunk-type slot card number)

(add-dm

	(handcards ISA hand hand 0 card1 4 card2 1 card3 3 card4 2 card5 0 card6 5 card7 6 card8 7 card9 8)
	
	(register ISA reg reg 0 rcard1 "" rcard2 "" rcard3 "" rcard4 "" rcard5 "")
	
	(eins ISA order num1 0 num2 1)
	
	(zwei ISA order num1 1 num2 2)
	
	(drei ISA order num1 2 num2 3)
	
	(vier ISA order num1 3 num2 4)
	
	(fünf ISA order num1 4 num2 5)
	
	(next ISA todo tsk "look" count 0 next 1 phase "real")
	
	(try ISA todo phase "imagine")
	
)

;;
;; Züge ausprobieren/ Ergebnisse speichern: neue Chunks erstellen und durch Produktionen
;; auf Vorhandensein dieser prüfen!
;;
;; Buffer:
;;
;; goal: 	  +>/ => verändert bestehende Chunks
;; retrieval: +> holt Chunk aus declarative memory; mehrere Chunks zugleich möglich (bilden dann einen neuen Chunk))
;;			  => von Attributen schreibt neuen Chunk ins declarative memory
;; imaginary: +> erstellt direkt neuen Chunk im Buffer bzw. fügt Daten zum bestehenden Chunk hinzu
;;			  => manipuliert Chunk im buffer
;; 			  ?> status abfragen (z.B. state	free)
;;			  wird automatisch nach nächster Produktion geleert, wenn nicht explizit verhindert (durch =>)

(goal-focus next)

;; Lade Karten in retrieval-buffer
(P look
	=goal>
		ISA		todo
		tsk		"look"
		count	=eins
==>
	=goal>
		tsk		"choose"
	+retrieval>
		ISA		hand
		hand 	=eins
)

;; Nächste Karte auswählen
(P choose
	=goal>
		ISA		todo
		tsk		"choose"
		count	=eins
		next	=zwei
	=retrieval>
		ISA		hand
		card1	=choice
		card2	=next1
		card3	=next2
		card4	=next3
		card5	=next4
		card6	=next5
		card7	=next6
		card8	=next7
		card9	=next8
==>
	=goal>
		card	=choice
		tsk		"viewregs"
	=retrieval>
		hand	=zwei
		card1	=next1
		card2	=next2
		card3	=next3
		card4	=next4
		card5	=next5
		card6	=next6
		card7	=next7
		card8	=next8
		card9	""
	-retrieval>
)

;; Register laden
(P view-regs
	=goal>
		ISA		todo
		tsk		"viewregs"
		count	=eins
==>
	=goal>
		tsk		"put"
	+retrieval>
		ISA		reg
		reg		=eins
)

;; Karte spielen
(P put
	=goal>
		ISA		todo
		tsk		"put"
		card	=choice
		count	=eins
		next	=zwei
	=retrieval>
		ISA		reg
		reg		=eins
		rcard1	=card1
		rcard2	=card2
		rcard3	=card3
		rcard4	=card4
		rcard5	=card5
==>
	=goal>
		tsk		"get-order"
		card	nil
	=retrieval>
		reg		=zwei
		rcard1	=card2
		rcard2	=card3
		rcard3	=card4
		rcard4	=card5
		rcard5	=choice
	-retrieval>
)

;; Ordnung laden, sofern möglich
(P load-order
	=goal>
		ISA		todo
		tsk		"get-order"
		next	=zwei
==>
	=goal>
		tsk		"count"
		count	=zwei
	+retrieval>
		ISA		order
		num1	=zwei
)

;; hochzählen
(P count
	=goal>
		ISA		todo
		tsk		"count"
	=retrieval>
		ISA		order
		num2	=zwei
==>
	=goal>
		tsk		"look"
		next	=zwei
	-retrieval>
)

;; Zugende
(P finish
	=goal>
		ISA		todo
		tsk		"count"
		phase 	"real"
		count	5
==>
	=goal>
		tsk		"out"
	+retrieval>
		ISA		reg
		reg		5
)

;; Ausgabe
(P output
	=goal>
		ISA 	todo
		tsk		"out"
	=retrieval>
		ISA		reg
		reg		5
		rcard1	=card1
		rcard2	=card2
		rcard3	=card3
		rcard4	=card4
		rcard5	=card5
==>
	-goal>
	-retrieval>
	!output!	(FILL REGISTER WITH CARDS =card1 =card2 =card3 =card4 =card5)
	!stop!
)

)