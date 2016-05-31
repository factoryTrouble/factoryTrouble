/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ais;

import de.uni_bremen.factroytrouble.ai.AIPlayer;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.observer.GameObserver;
import de.uni_bremen.factroytrouble.player.MasterFactory;
import de.uni_bremen.factroytrouble.player.Player;
import de.uni_bremen.factroytrouble.player.ProgramCard;
import de.uni_bremen.factroytrouble.spring.SpringConfigHolder;

import java.util.List;

/**
 * Basis-Klasse für alle KIs; Lagert Funktionalität vom Player in die
 * Player-Implementation der Spielmechanik aus
 *
 * @author Thorben
 */
public abstract class AIBase implements AIPlayer {
//  Dies ist die Spiel gameId
    protected final int gameId;
    protected MasterFactory mFactory;
    protected Player player;

    public AIBase(int gameId, Player player) {
        this.gameId = gameId;
        this.mFactory = SpringConfigHolder.getInstance().getContext().getBean(MasterFactory.class);
        this.player = player;
    }

    @Override
    public void giveCards(List<ProgramCard> cards) {
        player.giveCards(cards);
    }

    @Override
    public List<ProgramCard> discardCards() {
        return player.discardCards();
    }

    @Override
    public ProgramCard fillRegister(int register, ProgramCard card) {
        return player.fillRegister(register, card);
    }
    
    @Override
    public void emptyRegister(int register) {
        player.emptyRegister(register);
    }    

    @Override
    public void fillEmptyRegisters() {
        player.fillEmptyRegisters();
    }

    @Override
    public void finishTurn() {
        player.finishTurn();
    }

    @Override
    public boolean isDone() {
        return player.isDone();
    }

    @Override
    public Robot getRobot() {
        return player.getRobot();
    }

    @Override
    public List<ProgramCard> getPlayerCards() {
        return player.getPlayerCards();
    }

    @Override
    public void attachObserver(GameObserver observer) {
        player.attachObserver(observer);
    }

    @Override
    public void removeObserver(GameObserver observer) {
        player.removeObserver(observer);
    }

}