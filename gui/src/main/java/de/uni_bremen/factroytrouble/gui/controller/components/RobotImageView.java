/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gui.controller.components;

import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.gui.generator.board.BoardConverterService;
import de.uni_bremen.factroytrouble.spring.SpringConfigHolder;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.log4j.Logger;

import java.awt.*;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Created by johannes.gesenhues on 30.12.15.
 * <p>
 * Diese Klasse entspricht einem Roboter Bild auf dem GameFieldController
 */
public class RobotImageView extends ImageView {
    private static final Logger LOGGER = Logger.getLogger(RobotImageView.class);
    
    public static final String ROBOT_PATH = "/game/robots/top_";
    public static final int ROBOT_SIZE = 150;
    private Image robotImage;
    private Image explosionImage;

    private BoardConverterService boardConverterService;
    private double zoom = 0.0;

    public RobotImageView(int robotNumber) {
        super();
        robotImage = new Image(ROBOT_PATH + robotNumber + ".png");
        try {
            explosionImage = SwingFXUtils
                    .toFXImage(ImageIO.read(getClass().getResourceAsStream("/game/robots/explosion.png")), null);
        } catch (IOException e) {
            LOGGER.error("Failed to load explosionImage", e);
        }
        setImage(robotImage);
        setPreserveRatio(true);
        setFitWidth(ROBOT_SIZE);
        boardConverterService = SpringConfigHolder.getInstance().getContext().getBean(BoardConverterService.class);
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
        setFitWidth(ROBOT_SIZE * zoom);
    }

    public void setPos(Point point) {
        setX((zoom * ROBOT_SIZE) * point.x);
        setY((zoom * ROBOT_SIZE) * (boardConverterService.getMaxY() - point.y));
    }

    public void respawn() {
        Platform.runLater(() -> setImage(robotImage));
    }

    public void kill() {
        Platform.runLater(() -> setImage(explosionImage));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            LOGGER.error("sleeping Thread interupted", e);
        }
    }

    public void setOrientation(Orientation orientation) {
        switch (orientation) {
        case NORTH:
            setRotate(0);
            break;
        case EAST:
            setRotate(90.0);
            break;
        case SOUTH:
            setRotate(180.0);
            break;
        case WEST:
            setRotate(270.0);
            break;
        default:
            LOGGER.error("Fail to get orientation");
        }
    }
}
