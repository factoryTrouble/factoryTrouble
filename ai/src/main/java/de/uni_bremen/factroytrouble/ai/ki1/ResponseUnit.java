/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki1;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.uni_bremen.factroytrouble.ai.ais.AIPlayer1;
import de.uni_bremen.factroytrouble.api.ki1.Response;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.player.ProgramCard;

public class ResponseUnit implements Response {

    /**
     * Der verwaltete Spieler.
     */
    private Robot robot;

    private AIPlayer1 control;

    /**
     * Erstellt eine ResponseUnit.
     * 
     * @param player
     *            der verwaltete Spieler
     */
    public ResponseUnit(Robot robot, AIPlayer1 control) {
        this.robot = robot;
        this.control = control;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uni_bremen.factroytrouble.ai.Response#setCards(java.util.List)
     */
    @Override
    public void setCards(List<ProgramCard> cards) {
        if (cards == null) {
            throw new IllegalArgumentException("The given list of cards is null!");
        }
        checkCardsAmount(cards);
        checkForDuplicates(cards);
        for (int i = 0; i < cards.size(); i++) {
            if (!robot.fillRegister(i, cards.get(i))) {
                throw new IllegalStateException("couldn't fill register " + i);
            }
        }
    }

    private void checkCardsAmount(List<ProgramCard> cards) {
        int nbCardsToPlace = 0;
        for (int i = 0; i < 5; i++) {
            if(!robot.registerLockStatus()[i]) {
                nbCardsToPlace++;
            }
        }
        if (nbCardsToPlace != cards.size()) {
            throw new IllegalStateException(robot.getName()+": expected "+nbCardsToPlace+" cards but got "+cards.size());
        }
    }
    
    private void checkForDuplicates(List<ProgramCard> cards) {
        Set<ProgramCard> items = new HashSet<ProgramCard>();
        Set<ProgramCard> duplicates = new HashSet<ProgramCard>();
        for (ProgramCard item : cards) {
            if (items.contains(item)) {
                duplicates.add(item);
            } else { 
                items.add(item);
            } 
        } 
        if (!duplicates.isEmpty()) {
            throw new IllegalStateException("got duplicates "+duplicates);
        }
    }
}
