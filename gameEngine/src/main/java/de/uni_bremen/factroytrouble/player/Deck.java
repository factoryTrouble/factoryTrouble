/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.player;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Bisher nur Hilfsklasse fuer Master, kann auch veroeffentlicht werden.
 *
 * @author Thore
 */
public class Deck {

    private static final Logger LOGGER = Logger.getLogger(Deck.class);
    private final int gameId;
    // RandomNumbeGenertor. Wird unter anderem in "shuffle()" benutzt, um den
    // "tray" zu mischen.
    Random rngesus;
    // Das Deck, aus dem gezogen wird.
    private List<ProgramCard> cardDeck;
    // Der Ablagestapel.
    private List<ProgramCard> tray;

    public Deck(int gameId) {
        this.gameId = gameId;
        tray = new ArrayList<ProgramCard>();
        rngesus = new Random();
        generateDeck();
    }

    /*
     * Erzeugt das Deck neu. Das Deck wird mit allen Karten aus der
     * Spielanleitung gefüllt.
     */
    public void generateDeck() {
        tray = new ArrayList<ProgramCard>();
        cardDeck = new ArrayList<ProgramCard>();
        int[] uTurnPriorities = generatePriorityArray(10, 60, 10);
        UturnCard[] uTurns = generateUturns(uTurnPriorities);
        for (UturnCard u : uTurns) {
            tray.add(u);
        }
        int[] rightPriorities = generatePriorityArray(80, 420, 20);
        TurnRightCard[] rights = generateTurnRights(rightPriorities);
        for (TurnRightCard r : rights) {
            tray.add(r);
        }
        int[] leftPriorities = generatePriorityArray(70, 410, 20);
        TurnLeftCard[] lefts = generateTurnLefts(leftPriorities);
        for (TurnLeftCard l : lefts) {
            tray.add(l);
        }
        int[] backupPriorities = generatePriorityArray(430, 480, 10);
        MoveBackwardCard[] backups = generateMoveBackwards(backupPriorities);
        for (MoveBackwardCard b : backups) {
            tray.add(b);
        }
        int[] moveOnePriorities = generatePriorityArray(490, 660, 10);
        MoveForwardCard[] moveOnes = generateMoveForwards(moveOnePriorities, 1);
        for (MoveForwardCard m : moveOnes) {
            tray.add(m);
        }
        int[] moveTwoPriorities = generatePriorityArray(670, 780, 10);
        MoveForwardCard[] moveTwos = generateMoveForwards(moveTwoPriorities, 2);
        for (MoveForwardCard m : moveTwos) {
            tray.add(m);
        }
        int[] moveThreePriorities = generatePriorityArray(790, 840, 10);
        MoveForwardCard[] moveThrees = generateMoveForwards(moveThreePriorities, 3);
        for (MoveForwardCard m : moveThrees) {
            tray.add(m);
        }
        shuffle();
    }

    private int[] generatePriorityArray(int start, int stop, int step) {
        int number = (stop / step) - ((start / step) - 1);
        int[] priorities = new int[number];
        int index = 0;
        for (int i = start; i <= stop; i += step) {
            priorities[index] = i;
            index++;
        }
        return priorities;
    }

    /*
     * Generiert für jede übergebene Priorität eine UturnCard
     * 
     * @param Ein Array von Prioritäten
     * 
     * @return Ein Array von UturnCards
     */
    private UturnCard[] generateUturns(int[] priorities) {
        UturnCard[] cards = new UturnCard[priorities.length];
        for (int i = 0; i < priorities.length; i++) {
            cards[i] = new GameUturnCard(priorities[i]);
        }
        return cards;
    }

    /*
     * Generiert für jede übergebene Priorität eine TurnLeftCard
     * 
     * @param Ein Array von Prioritäten
     * 
     * @return Ein Array von TurnLeftCards
     */
    private TurnLeftCard[] generateTurnLefts(int[] priorities) {
        TurnLeftCard[] cards = new TurnLeftCard[priorities.length];
        for (int i = 0; i < priorities.length; i++) {
            cards[i] = new GameTurnLeftCard(priorities[i]);
        }
        return cards;
    }

    /*
     * Generiert für jede übergebene Priorität eine TurnRightCard
     * 
     * @param Ein Array von Prioritäten
     * 
     * @return Ein Array von TurnRightCards
     */
    private TurnRightCard[] generateTurnRights(int[] priorities) {
        TurnRightCard[] cards = new TurnRightCard[priorities.length];
        for (int i = 0; i < priorities.length; i++) {
            cards[i] = new GameTurnRightCard(priorities[i]);
        }
        return cards;
    }

    /*
     * Generiert für jede übergebene Priorität eine MoveForwardCard, mit der
     * übergebenen Reichweite
     * 
     * @param Ein Array von Prioritäten
     * 
     * @param Die Reichweite
     * 
     * @return Ein Array von MoveForwardCards
     */
    private MoveForwardCard[] generateMoveForwards(int[] priorities, int range) {
        MoveForwardCard[] cards = new MoveForwardCard[priorities.length];
        for (int i = 0; i < priorities.length; i++) {
            cards[i] = new GameMoveForwardCard(gameId, priorities[i], range);
        }
        return cards;
    }

    /*
     * Generiert für jede übergebene Priorität eine MoveBackwardCard
     * 
     * @param Ein Array von Prioritäten
     * 
     * @return Ein Array von MoveBackwardCards
     */
    private MoveBackwardCard[] generateMoveBackwards(int[] priorities) {
        MoveBackwardCard[] cards = new MoveBackwardCard[priorities.length];
        for (int i = 0; i < priorities.length; i++) {
            cards[i] = new GameMoveBackwardCard(gameId, priorities[i]);
        }
        return cards;
    }

    /*
     * Mischt den "tray" in zufälliger Reihenfolge in das Deck. Soll nur
     * ausgeführt werden wenn das Deck leer ist.
     */
    private void shuffle() {
        Collections.shuffle(tray);
        cardDeck.addAll(tray);
        tray.clear();
    }

    /*
     * Gibt eine Karte aus dem Deck aus. Sollte das Deck leer sein, wird
     * "shuffle()" aufgerufen. Es wird die Karte an Position 0 ausgegeben.
     * 
     * @return Die ausgegebene Karte
     */
    public ProgramCard dealCard() {
        if (cardDeck.isEmpty()) {
            shuffle();
            if (cardDeck.isEmpty()) {
                LOGGER.error("! Es wurde versucht eine Karte zu ziehen als keine mehr im Deck war. "
                        + "Dies sollte niemals passieren, da mehr Karten im Deck sind als an 8 Spieler verteilt werden können.");
                return null;
            }
        }
        ProgramCard card = cardDeck.remove(0);
        LOGGER.info("Programmkarte " + card + " wird vom Deck gezogen");
        return card;
    }

    /*
     * Legt eine Karte auf den Ablagestapel
     * 
     * @param Die abzulegende Karte.
     */
    public void layOffCard(ProgramCard card) {
        if (card == null) {
            LOGGER.error("Karte ist null");
            return;
        }
        if (cardDeck.contains(card) || tray.contains(card)) {
            LOGGER.error("Es wurde eine Karte zurück in das Deck gelegt, die bereits im Deck ist. "
                    + "Dies sollte nicht passieren, da alle Karten einzigartig sind."
                    + "Die Karte wurde nicht angenommen.");
            return;
        }
        tray.add(card);
        LOGGER.info("Programmkarte " + card + " wird auf den Ablagestapel gelegt");
    }

    public List<ProgramCard> getDeck() {
        return cardDeck;
    }

    public List<ProgramCard> getTray() {
        return tray;
    }

}
