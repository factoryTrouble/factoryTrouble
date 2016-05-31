/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gui.services.game;

import de.uni_bremen.factroytrouble.ai.AIPlayer;
import de.uni_bremen.factroytrouble.gui.controller.GameScreenController;
import de.uni_bremen.factroytrouble.gui.services.GameEngineWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Verwaltet das beenden der Runde.
 * <p>
 * Created by johannes.gesenhues on 22.12.15.
 */
@Service
public class EndBtnService {

    @Autowired private AIExecutionService aiExecutionService;
    @Autowired private RegisterCardsService registerCardsService;
    @Autowired private GameScreenController gameScreenController;
    @Autowired private GameEngineWrapper gameEngineWrapper;
    @Autowired private HandCardService handCardService;
    @Autowired private AlertService alertService;
    @Autowired private PerformRoundForPlayerService performRoundForPlayerService;

    /**
     * Beendet die Runde, sofern ausreichend Karten gewählt wurden. Falls noch Spieler an
     * der Reihe sind, ist der nächste Spieler dran. Ansonsten werden die Registerphasen
     * ausgeführt. Sollte dann ein Gewinner feststehen, wird das Spiel beendet.
     *
     * @return der Spieler Index
     */
    public void endRound(int playerNumber) {
        boolean notAllCardsSet = !registerCardsService.allRegisterCardsAreSet();
        boolean isHumanPlayer = !(gameEngineWrapper.getPlayerByNumber(playerNumber)instanceof AIPlayer);
        boolean isPoweredDown = gameEngineWrapper.getPlayerByNumber(playerNumber).getRobot().isPoweredDown();
        if (notAllCardsSet && isHumanPlayer && !isPoweredDown) {
            alertService.showNotAllCardsSet();
        } else if (hasNextHumanPlayer(playerNumber)) {
            finishTurnForPlayer(playerNumber);
            int nextPlayerNumber = getNextHumanPlayer(playerNumber);
            performRoundForPlayerService.start(nextPlayerNumber);
        } else {
            finishTurnForPlayer(playerNumber);
            aiExecutionService.endTurnForHumanPlayers();
        }
    }

    /**
     * Diese Methode beendet die Runde für einen Menschlichen Spieler
     *
     * @param playerNumber der Aktuelle Spieler
     */
    public void finishTurnForPlayer(int playerNumber) {
        boolean isHuman = !(gameEngineWrapper.getPlayerByNumber(playerNumber) instanceof AIPlayer);
        if (isHuman) {
            gameScreenController.setNextButton(getNextHumanPlayerCount(playerNumber)>1);
            gameScreenController.getPlayerInfos().get(playerNumber).setFinished(true);
            registerCardsService.dropAllCards(playerNumber);
            handCardService.dropAllCards();
            gameEngineWrapper.getPlayerByNumber(playerNumber).finishTurn();
        }
    }

    /**
     * Prüft, ob es noch einen folgenden, menschlichen Spieler gibt
     *
     * @param playerNumber
     *      Aktuelle Spielernummer
     * @return
     *      true, falls einer vorhanden ist, false sonst
     */
    public boolean hasNextHumanPlayer(int playerNumber) {
        for (int i = playerNumber+1; i < gameEngineWrapper.getPlayerCount(); i++) {
            if (isHuman(i) && isAlive(i)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Prüft ob ein Spieler ein Mensch ist
     *
     * @param playerNumber
     *      Die Nummer des Spielers
     * @return
     *      true, wenn es ein Spieler ist, sonst false
     */
    public boolean isHuman (int  playerNumber){
        return !(gameEngineWrapper.getPlayerByNumber(playerNumber) instanceof AIPlayer);
    }
    
    public boolean isAlive(int playerNumber){
        return gameEngineWrapper.getPlayerByNumber(playerNumber).getRobot().getLives() > 0;
    }

    /**
     * Gibt die Nummer des nächsten menschlichen Spielers zurück
     *
     * @param activePlayer
     *      Aktuelle Spielernummer
     *
     * @return
     *      Die Nummer des nächsten Spieles. Falls keiner vorhanden ist, -1
     */
    public int getNextHumanPlayer(int activePlayer) {
        for (int i = activePlayer+1; i < gameEngineWrapper.getPlayerCount(); i++) {
            if (isHuman(i) && isAlive(i)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Gibt die Anzahl der noch folgenden menschlichen Spielern zurück
     *
     * @param activePlayer
     *      Der aktuelle Spieler
     *
     * @return
     *      Anzahl der noch folgenden Spieler
     */
    public int getNextHumanPlayerCount(int activePlayer) {
        int nextHumanPlayerCounter = 0;
        for (int i = activePlayer+1; i < gameEngineWrapper.getPlayerCount(); i++) {
            if (isHuman(i) && isAlive(i)) {
                nextHumanPlayerCounter++;
            }
        }
        return nextHumanPlayerCounter;
    }


}
