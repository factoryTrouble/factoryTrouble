/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gui.controller;

import de.uni_bremen.factroytrouble.gui.controller.components.FieldController;
import de.uni_bremen.factroytrouble.gui.controller.components.RespawnImageView;
import de.uni_bremen.factroytrouble.gui.services.GameEngineWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 * Enthält das Spielfeld sammt Logik
 *
 * @author Johannes und
 */

@Controller
public class PreviewGameFieldController extends FieldController {

    @Autowired private GameEngineWrapper gameEngineWrapper;
    @Autowired private NewGameScreenController newGameScreenController;

    /**
     * Erstellt und zeigt die Preview für ein Spielfeld an.
     */
    public void showPreview() {
        String boardName = newGameScreenController.getActiveBoard();
        if (boardName.isEmpty())
            return;
        

        gameEngineWrapper.removeAllPlayers();
        deleteRobots();
        setGameBoard(boardName);

        newGameScreenController.getSelectedPlayers().forEach((playerNumber, playerType) -> {
            String playerName = gameEngineWrapper.getRobotNameByNumber(playerNumber);
            gameEngineWrapper.createPlayer(playerName, playerType);
            RobotController robotController = new RobotController(playerName);
            robotController.setZoom(zoom.get());
            RespawnImageView respawn = new RespawnImageView(playerNumber);
            respawn.setZoom(zoom.get()); // respawnpunkte setzen
            anchorPane.getChildren().add(robotController.getRobotImageView());
            anchorPane.getChildren().add(respawn);
            respawns.put(playerName, respawn);
            robots.put(playerName, robotController);

        });
        gameEngineWrapper.initialiseBoard(boardName);
        gameEngineWrapper.getPlayers().forEach(player -> {
            RobotController robot = robots.get(player.getRobot().getName());
            if(robot != null) {
                Point startPoint = gameEngineWrapper.getActiveBoard().getAbsoluteCoordinates(player.getRobot().getCurrentTile());
                robot.refresh(startPoint, player.getRobot().getOrientation());
                RespawnImageView respawn = respawns.get(player.getRobot().getName());
                respawn.setPos(startPoint, this.getRespawnsOnPos(respawn, startPoint));
            }
        });
    }

    @Override
    public BufferedImage getImage(String name) {
        return (BufferedImage) imageBoardGenerator.generateBoard(name);
    }

    /**
     * Entfernt die Roboter aus der Preview.
     */
    private void deleteRobots() {

        robots.values().forEach(robot -> anchorPane.getChildren().remove(robot.getRobotImageView()));
        respawns.values().forEach(respawn -> anchorPane.getChildren().remove(respawn));
        robots.clear();
        respawns.clear();
    }
}
