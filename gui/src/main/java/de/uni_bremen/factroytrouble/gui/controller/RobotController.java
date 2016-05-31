/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gui.controller;

import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.gui.controller.components.RobotImageView;
import de.uni_bremen.factroytrouble.gui.services.GameEngineWrapper;
import de.uni_bremen.factroytrouble.spring.SpringConfigHolder;
import javafx.application.Platform;
import org.apache.log4j.Logger;

import java.awt.Point;

public class RobotController {

    private static final Logger LOGGER = Logger.getLogger(RobotController.class);

    private final int playerNumber;
    private Point position;
    private Orientation orientation;
    private RobotImageView robotImageView;

    private GameEngineWrapper gameEngineWrapper;

    /**
     * Ermittelt die Spielernummer entsprechend dem übergebenen Namen @param{playerName}
     * und setzt das entsprechende RoboterBild.
     *
     * @param playerName Der Spieler Name
     */
    public RobotController(String playerName) {
        orientation = null;
        position = null;
        gameEngineWrapper = SpringConfigHolder.getInstance().getContext().getBean(GameEngineWrapper.class);
        this.playerNumber = gameEngineWrapper.getPlayerNumberByName(playerName);
        this.robotImageView = new RobotImageView(playerNumber);
    }

    public Point getPosition() {
        return position;
    }

    /**
     * Aktualisiert die Position entsprechend der Eingabe @param{position} sowie
     * die Orientierung @param{orientation}.
     *
     * @param position
     * @param orientation
     */
    public void refresh(Point position, Orientation orientation) {
        // First Call
        if (this.position == null) {
            setPosition(position);
            setOrientation(orientation);
            return;
        }

        if (!isChanged(position, orientation)) {
            return;
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            LOGGER.error(e);
        }
        Platform.runLater(() -> {
            setPosition(position);
            setOrientation(orientation);
        });
    }

    public RobotImageView getRobotImageView() {
        return robotImageView;
    }

    public void setZoom(double zoom) {
        robotImageView.setZoom(zoom);
    }

    private void setPosition(Point position) {
        robotImageView.setPos(position);
        this.position = position;
    }

    private void setOrientation(Orientation orientation) {
        robotImageView.setOrientation(orientation);
        this.orientation = orientation;
    }

    private boolean isChanged(Point position, Orientation orientation) {
        return !(this.position.equals(position) && this.orientation == orientation);
    }
}
