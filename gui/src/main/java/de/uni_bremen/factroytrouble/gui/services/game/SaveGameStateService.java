/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gui.services.game;

import de.uni_bremen.factroytrouble.gui.controller.*;
import de.uni_bremen.factroytrouble.gui.controller.components.RespawnImageView;
import de.uni_bremen.factroytrouble.spring.SpringConfigHolder;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SaveGameStateService {

    @Autowired private PlayerGridService playerGridService;
    @Autowired private PerformRoundForPlayerService performRoundForPlayerService;
    
    private GameScreenController gameScreenController;
    private GameFieldController gameFieldController;
    private List<PlayerInfoController> playerInfoController;
    private AnchorPane gamePane;
    private boolean gameFinished = false;
    protected Map<String, RobotController> robots;
    protected Map<String, RespawnImageView> respawns;

    /**
     * Speichert das aktuelle Spiel ab.
     * @param playerInfoController
     * @param gameFieldController
     */
    public void saveGameState(List<PlayerInfoController> playerInfoController, GameFieldController gameFieldController) {
        gameScreenController = SpringConfigHolder.getInstance().getContext().getBean(GameScreenController.class);
        this.playerInfoController = playerInfoController;
        this.gamePane = gameFieldController.getAnchorPane();
        this.gameFieldController = gameFieldController;
        this.robots = gameFieldController.getRobots();
        this.respawns = gameFieldController.getRespawns();
    }
    
    /**
     * Setzt das zuletzt gespielte Spiel fort, sofert es noch nicht beendet wurde.
     */
    public void continueGame(){
        gameScreenController.getScrlPane().setContent(gamePane);
        gameFieldController.setRobots(robots);
        gameFieldController.setRespawns(respawns);
        playerGridService.reclaimPlayerGrid(playerInfoController);
        performRoundForPlayerService.start(gameScreenController.getActivePlayer());
    }

    /**
     * Macht das gespeicherte Spiel unerreichbar
     */
    public void finishGame(){
        gameFinished = true;
        SpringConfigHolder.getInstance().getContext().getBean(MenuScreenController.class).setContinueGameBtn(false);
    }
    
    /**
     * Gibt zurück ob das Spiel bereits zu Ende ist.
     * @return
     */
    public boolean getFinished(){
        return gameFinished;
    }
}
