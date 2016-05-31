/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gui.generator.board.tile;

import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.gameobjects.Wall;
import de.uni_bremen.factroytrouble.gui.generator.board.BoardGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Generiert die Wände, initialen Laser und Pusher auf ein Tile
 */
@Component
public class TileWallGenerator {

    @Autowired private WallPusherDispatcher wallPusherDispatcher;

    private BufferedImage wallUp;
    private BufferedImage wallDown;
    private BufferedImage wallRight;
    private BufferedImage wallLeft;

    /**
     * Initialisiert die Bilder
     */
    @PostConstruct
    public void init() {
        try {
            wallUp = ImageIO.read(getClass().getResourceAsStream("/game/tiles/wall/wall_up.png"));
            wallDown = ImageIO.read(getClass().getResourceAsStream("/game/tiles/wall/wall_down.png"));
            wallRight = ImageIO.read(getClass().getResourceAsStream("/game/tiles/wall/wall_right.png"));
            wallLeft = ImageIO.read(getClass().getResourceAsStream("/game/tiles/wall/wall_left.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Generiert alle Wände und die Laser an den Wänden auf das gegebende Tile-Image
     * @param tile
     *      Das Tile
     * @param tileImage
     *      Das aktuelle Bild des Tiles
     * @return
     *      Neuer Bild für das Tile mit Wänden, Lasern und Pushern
     */
    public BufferedImage generateTileWithWall(Tile tile, BufferedImage tileImage) {
        BufferedImage combinedFieldWithWall = new BufferedImage(BoardGenerator.DEFAULT_TILE_WIDTH, BoardGenerator.DEFAULT_TILE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics combinedGraphic = combinedFieldWithWall.getGraphics();
        combinedGraphic.drawImage(tileImage, 0, 0, null);
        for(Wall wall : tile.getWalls()) {
            if(wall.getOrientation().equals(Orientation.NORTH)) {
                combinedGraphic.drawImage(wallUp, 0, 0, null);
            }
            if(wall.getOrientation().equals(Orientation.SOUTH)) {
                combinedGraphic.drawImage(wallDown, 0, 0, null);
            }
            if(wall.getOrientation().equals(Orientation.WEST)) {
                combinedGraphic.drawImage(wallLeft, 0, 0, null);
            }
            if(wall.getOrientation().equals(Orientation.EAST)) {
                combinedGraphic.drawImage(wallRight, 0, 0, null);
            }
            combinedGraphic.drawImage(wallPusherDispatcher.dispatchPusher(wall), 0, 0, null);
        }
        return combinedFieldWithWall;
    }

}