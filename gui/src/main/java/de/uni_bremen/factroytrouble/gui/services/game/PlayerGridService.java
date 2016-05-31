/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gui.services.game;

import de.uni_bremen.factroytrouble.gui.controller.GameScreenController;
import de.uni_bremen.factroytrouble.gui.controller.PlayerInfoController;
import de.uni_bremen.factroytrouble.gui.controller.components.PlayerGridPane;
import de.uni_bremen.factroytrouble.gui.services.GameEngineWrapper;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Verwaltet die Übersicht der Spieler auf dem GameScreen.
 * <p>
 * Created by johannes.gesenhues on 22.12.15.
 */
@Service
public class PlayerGridService {

    @Autowired
    private GameScreenController gameScreenController;
    @Autowired
    private GameEngineWrapper gameEngineWrapper;

    /**
     * Zeichnet auf dem GameScreen die Übersicht der Spieler.
     */
    public void initPlayersGrid() {
        PlayerGridPane playerGridPane = new PlayerGridPane();
        gameEngineWrapper.getPlayers().forEach(player -> {
            String name = player.getRobot().getName();
            RowConstraints rc = new RowConstraints();
            rc.setVgrow(Priority.ALWAYS);
            rc.setMaxHeight(150);
            playerGridPane.getRowConstraints().add(rc);
            int row = gameEngineWrapper.getPlayers().indexOf(player);
            gameScreenController.getPlayerInfos().add(row, new PlayerInfoController(name));
            playerGridPane.add(gameScreenController.getPlayerInfos().get(row), 0, row);
        });
        gameScreenController.getPlayerInfos().get(0).setActive(true);
        gameScreenController.getPlayerScrlPane().setContent(playerGridPane);
    }

    public void reclaimPlayerGrid(List<PlayerInfoController> playerInfoController) {
        PlayerGridPane playerGridPane = new PlayerGridPane();
        for(PlayerInfoController controller : playerInfoController){
            RowConstraints rc = new RowConstraints();
            rc.setVgrow(Priority.ALWAYS);
            rc.setMaxHeight(120);
            playerGridPane.getRowConstraints().add(rc);
            int row = playerInfoController.indexOf(controller);
            playerGridPane.add(controller, 0, row);
        }
        gameScreenController.getPlayerInfos().get(0).setActive(true);
        gameScreenController.getPlayerScrlPane().setContent(playerGridPane);
    }
}
