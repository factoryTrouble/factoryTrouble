/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.player;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.observer.GameObserver;

/**
 * 
 * 
 * @author ToFy, Lukas
 */
public class GamePlayer implements Player {

    private static final Logger LOGGER = Logger.getLogger(GamePlayer.class);

    private Robot ownRobot;
    private List<ProgramCard> cards;
    private List<GameObserver> observerlist;
    private boolean isDone;

    public GamePlayer(Robot robot) {
        ownRobot = robot;
        cards = new ArrayList<ProgramCard>();
    }

    @Override
    public void giveCards(List<ProgramCard> cards) {
        for (int i = 0; i < cards.size(); i++) {
            LOGGER.info("Der Spieler des Roboters " + ownRobot.getName() + " erhält die Programmkarte " + cards.get(i));
        }
        this.cards = cards;
        isDone = false;
    }

    @Override
    /**
     * Bisher: Karten müssen abgelegt sein, um Zug zu beenden.
     */
    public List<ProgramCard> discardCards() {
        List<ProgramCard> cards2 = new ArrayList<ProgramCard>();
        cards.forEach(card -> {
            cards2.add(card);
            LOGGER.info("Programmkarte " + card + " wird aus der Hand des Spielers von Roboter " + ownRobot.getName()
                    + " zurückgegeben");
        });
        cards.clear();
        return cards2;
    }

    @Override
    public void finishTurn() {
        isDone = true;
    }

    @Override
    public boolean isDone() {
        return isDone;
    }

    @Override
    public Robot getRobot() {
        return ownRobot;
    }

    @Override
    public List<ProgramCard> getPlayerCards() {
        return cards;
    }

    @Override
    public void attachObserver(GameObserver observer) {
        observerlist.add(observer);
    }

    @Override
    public void removeObserver(GameObserver observer) {
        observerlist.remove(observer);
    }

    @Override
    public ProgramCard fillRegister(int register, ProgramCard card) {
        ProgramCard inRegister = ownRobot.getRegisters()[register];
        if (inRegister == null) {
            ownRobot.fillRegister(register, card);
            cards.remove(card);
            return null;
        } else if (!ownRobot.registerLockStatus()[register]) {
            ownRobot.fillRegister(register, null);
            ownRobot.fillRegister(register, card);
            cards.remove(card);
            cards.add(inRegister);
            return inRegister;
        }
        return card;
    }
    
    @Override
    public void emptyRegister(int register){
        cards.add(ownRobot.emptyRegister(register));
    }

    @Override
    public void fillEmptyRegisters() {
        cards = ownRobot.fillEmptyRegisters(cards);
    }
}
