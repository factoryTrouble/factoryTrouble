/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gui.controller;

import de.uni_bremen.factroytrouble.gui.controller.components.FieldController;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;

import java.awt.image.BufferedImage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Enthält das Spielfeld sammt Logik
 *
 * @author Johannes und
 */

@Controller
public class GameFieldController extends FieldController {
    
    @Autowired private PreviewGameFieldController previewGameFieldController;

    /**
     * Erstellt die Preview des Spielfeldes für den Spieler im NewGameScreen.
     * 
     * @param scrollPane
     * @param activeBoard
     */
    public void setUp(ScrollPane scrollPane, String activeBoard) {
        addGameField(scrollPane);
        initBoard(activeBoard);
        this.robots = previewGameFieldController.getRobots();
        this.respawns = previewGameFieldController.getRespawns();
        robots.values().forEach(robotController -> {
                robotController.setZoom(zoom.get());    //trotz zoomen im preview an der richtigen stelle starten
                robotController.getRobotImageView().setPos(robotController.getPosition());
                anchorPane.getChildren().add(robotController.getRobotImageView());
        });
        respawns.values().forEach(respawn -> {
                respawn.setZoom(zoom.get());    //trotz zoomen im preview an der richtigen stelle starten
                respawn.setPos(respawn.getPos(), getRespawnsOnPos(respawn, respawn.getPos()));
                anchorPane.getChildren().add(respawn);
        });
    }

    @Override
    public BufferedImage getImage(String name) {
        return previewGameFieldController.getImage();
    }

    public AnchorPane getAnchorPane(){
        return anchorPane;
    }
    
}
