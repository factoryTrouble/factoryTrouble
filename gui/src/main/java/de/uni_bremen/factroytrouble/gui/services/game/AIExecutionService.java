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
import de.uni_bremen.factroytrouble.spring.PostConstructTaskScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AIExecutionService{
    private Map<String, Boolean> aiReadyMap;
    @Autowired private GameEngineWrapper gameEngineWrapper;
    @Autowired private AlertService alertService;
    @Autowired protected PostConstructTaskScheduler postConstructTaskScheduler;
    private GameScreenController gameScreenController;
    private Boolean waitForAIs = false;



    /**
     * Führt für alle AIPlayer executeAI aus
     */
    public void executeAIs() {
        gameScreenController = gameEngineWrapper.getGameScreenController();
        waitForAIs = false;
        aiReadyMap = new HashMap<>();
        gameEngineWrapper.getPlayers().forEach(player -> {
            if (player instanceof AIPlayer)
                executeAI((AIPlayer) player);
            }
        );
    }

    /**
     * Führt für einen KI Spieler executeTurn in einem eigenen Thread aus
     *
     * @param aiPlayer die auszuführende KI
     */
    public void executeAI(AIPlayer aiPlayer) {
        new Thread(() -> {
            startAIExecution(aiPlayer);
        }).start();
    }

    /**
     * Wartet, bis die AIs fertig sind
     */
    public void endTurnForHumanPlayers() {
        waitForAIs = true;
        checkIfAllAisReady();
    }

    /**
     * Diese Methode beendet die Runde und startet die Auswertung
     */
    public void endTurn() {
        alertService.showEndRoundAlert();
        new Thread(() -> gameEngineWrapper.startRound()).start();
    }

    private void startAIExecution(AIPlayer aiPlayer) {
        if (aiPlayer.getRobot().getLives() == 0) {
            return;
        }
        String robotName = aiPlayer.getRobot().getName();
        aiReadyMap.put(robotName, false);
        aiPlayer.executeTurn();
        setAIReady(robotName);
    }

    private void setAIReady(String robotName) {
        aiReadyMap.replace(robotName, true);
        int playerNumber = gameEngineWrapper.getPlayerIndexInPlayersByName(robotName);
        gameScreenController.getPlayerInfos().get(playerNumber).setFinished(true);
        checkIfAllAisReady();
    }

    private void checkIfAllAisReady() {
        if(!waitForAIs) {
            return;
        }
        aiReadyMap.forEach((name, ready) -> {
            if (!ready) {
                alertService.showAiNotReadyAlert();
                return;
            }
        });
        endTurn();
    }

}
