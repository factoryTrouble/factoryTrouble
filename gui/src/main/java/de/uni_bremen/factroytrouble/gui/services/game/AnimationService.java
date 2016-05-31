/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gui.services.game;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.gui.controller.GameFieldController;
import de.uni_bremen.factroytrouble.gui.generator.board.BoardConverterService;
import de.uni_bremen.factroytrouble.gui.generator.board.BoardGenerator;
import de.uni_bremen.factroytrouble.spring.SpringConfigHolder;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

@Service
public class AnimationService {

    private static final Logger LOGGER = Logger.getLogger(AnimationService.class);

    @Autowired
    private BoardConverterService boardConverterService;

    private GameFieldController gameFieldController;

    private BufferedImage verticalLaser;
    private BufferedImage horizontalLaser;
    ImageView[] imageViews;

    @PostConstruct
    public void init() {
        try {
            verticalLaser = ImageIO.read(getClass().getResourceAsStream("/game/tiles/laser/laser.png"));
            horizontalLaser = ImageIO.read(getClass().getResourceAsStream("/game/tiles/laser/laserHorizontal.png"));
        } catch (IOException e) {
            LOGGER.error("Failed to load Laserimages", e);
        }

    }

    public void kill(Robot target) {
        gameFieldController = SpringConfigHolder.getInstance().getContext().getBean(GameFieldController.class);
        gameFieldController.getRobots().get(target.getName()).getRobotImageView().kill();
        if (target.getLives() > 0) {
            return;
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                gameFieldController.getAnchorPane().getChildren()
                        .remove(gameFieldController.getRobots().get(target.getName()).getRobotImageView());
                gameFieldController.getRobots().remove(target.getName());
                gameFieldController.getAnchorPane().getChildren()
                        .remove(gameFieldController.getRespawns().get(target.getName()));
                gameFieldController.getRespawns().remove(target.getName());
            }
        });
    }

    public void respawn(Robot target) {
        gameFieldController = SpringConfigHolder.getInstance().getContext().getBean(GameFieldController.class);
        gameFieldController.getRobots().get(target.getName()).getRobotImageView().respawn();
    }

    public void laserShot(Robot target, Robot source) {
        gameFieldController = SpringConfigHolder.getInstance().getContext().getBean(GameFieldController.class);
        AnchorPane anchorPane = gameFieldController.getAnchorPane();
        double zoom = gameFieldController.getZoom().get();
        int xSource = source.getCurrentTile().getAbsoluteCoordinates().x;
        int xTarget = target.getCurrentTile().getAbsoluteCoordinates().x;
        int ySource = source.getCurrentTile().getAbsoluteCoordinates().y;
        int yTarget = target.getCurrentTile().getAbsoluteCoordinates().y;
        if (xSource == xTarget) {
            imageViews = new ImageView[Math.abs(ySource - yTarget)];
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    for (int i = 1; i < imageViews.length; i++) {
                        imageViews[i] = new ImageView(SwingFXUtils.toFXImage(verticalLaser, null));
                        imageViews[i].setFitWidth(BoardGenerator.DEFAULT_TILE_WIDTH * zoom);
                        imageViews[i].setFitHeight(BoardGenerator.DEFAULT_TILE_HEIGHT * zoom);
                        imageViews[i].setX((zoom * BoardGenerator.DEFAULT_TILE_WIDTH) * xSource);
                        if (source.getOrientation() == Orientation.SOUTH) {
                            imageViews[i].setY((zoom * BoardGenerator.DEFAULT_TILE_HEIGHT)
                                    * (boardConverterService.getMaxY() - (ySource - i)));
                        } else {
                            imageViews[i].setY((zoom * BoardGenerator.DEFAULT_TILE_HEIGHT)
                                    * (boardConverterService.getMaxY() - (ySource + i)));
                        }
                        anchorPane.getChildren().add(imageViews[i]);
                    }
                }
            });
        }
        if (ySource == yTarget) {
            imageViews = new ImageView[Math.abs(xSource - xTarget)];
            Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    for (int i = 1; i < imageViews.length; i++) {
                        imageViews[i] = new ImageView(SwingFXUtils.toFXImage(horizontalLaser, null));
                        imageViews[i].setFitWidth(BoardGenerator.DEFAULT_TILE_WIDTH * zoom);
                        imageViews[i].setFitHeight(BoardGenerator.DEFAULT_TILE_HEIGHT * zoom);
                        if (source.getOrientation() == Orientation.WEST) {
                            imageViews[i].setX((zoom * BoardGenerator.DEFAULT_TILE_WIDTH) * (xSource - i));
                        } else {
                            imageViews[i].setX((zoom * BoardGenerator.DEFAULT_TILE_WIDTH) * (xSource + i));
                        }
                        imageViews[i].setY((zoom * BoardGenerator.DEFAULT_TILE_HEIGHT)
                                * (boardConverterService.getMaxY() - ySource));
                        anchorPane.getChildren().add(imageViews[i]);
                    }
                }
            });
        }
        gameFieldController.getRobots().get(target.getName()).getRobotImageView().setOpacity(0.5);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            LOGGER.error("sleeping Thread interrupted", e);
        }
        gameFieldController.getRobots().get(target.getName()).getRobotImageView().setOpacity(1);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i < imageViews.length; i++) {
                    anchorPane.getChildren().remove(imageViews[i]);
                }
            }
        });
    }

}
