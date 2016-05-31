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
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Fügt einen Feld einen eventuellen Laser hinzu
 */
@Component
public class TileLaserDispatcher {

    private static final Logger LOGGER = Logger.getLogger(TileLaserDispatcher.class);

    private BufferedImage laser;
    private BufferedImage laserHorizontal;
    private BufferedImage laserDouble;
    private BufferedImage laserDoubleHorizontal;
    private BufferedImage laserTriple;
    private BufferedImage laserTripleHorizontal;

    /**
     * Initialisiert die Bilder
     */
    @PostConstruct
    public void init() {
        try {
            laser = ImageIO.read(getClass().getResourceAsStream("/game/tiles/laser/laser.png"));
            laserHorizontal = ImageIO.read(getClass().getResourceAsStream("/game/tiles/laser/laserHorizontal.png"));
            laserDouble = ImageIO.read(getClass().getResourceAsStream("/game/tiles/laser/doublelaser.png"));
            laserDoubleHorizontal = ImageIO.read(getClass().getResourceAsStream("/game/tiles/laser/doublelaserHorizontal.png"));
            laserTriple = ImageIO.read(getClass().getResourceAsStream("/game/tiles/laser/triplelaser.png"));
            laserTripleHorizontal = ImageIO.read(getClass().getResourceAsStream("/game/tiles/laser/triplelaserHorizontal.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Fügt den Bild eines Tiles einen Laser hinzu, sofern einer vorhanden ist
     * @param tile
     *      Das Tile, welches geprüft werden soll
     *
     * @param currentTileImage
     *      Das Bild des aktuellen Bildes des Tiles
     *
     * @return
     *      Tile mit Laser oder ohne, sofern kein Laser über das Feld verläuft
     */
    public BufferedImage getTileWithLaser(Tile tile, BufferedImage currentTileImage) {
        Integer verticalLaser = getLaserCountForATile(tile, Orientation.NORTH);
        Integer horizontalLaser = getLaserCountForATile(tile, Orientation.WEST);;
        verticalLaser = (verticalLaser.equals(0)) ? getLaserCountInDirection(tile, Orientation.NORTH) : verticalLaser;
        verticalLaser = (verticalLaser.equals(0)) ? getLaserCountInDirection(tile, Orientation.SOUTH) : verticalLaser;
        horizontalLaser = (horizontalLaser.equals(0)) ? getLaserCountInDirection(tile, Orientation.WEST) : horizontalLaser;
        horizontalLaser = (horizontalLaser.equals(0)) ? getLaserCountInDirection(tile, Orientation.EAST) : horizontalLaser;

        BufferedImage returnImage = new BufferedImage(BoardGenerator.DEFAULT_TILE_WIDTH, BoardGenerator.DEFAULT_TILE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics combinedTileGraphics = returnImage.getGraphics();
        combinedTileGraphics.drawImage(currentTileImage, 0, 0, null);
        switch (verticalLaser) {
            case 1:
                combinedTileGraphics.drawImage(laser, 0, 0, null);
                break;
            case 2:
                combinedTileGraphics.drawImage(laserDouble, 0, 0, null);
                break;
            case 3:
                combinedTileGraphics.drawImage(laserTriple, 0, 0, null);
                break;
            default:
                LOGGER.debug("No vertical laser detected");
        }

        switch (horizontalLaser) {
            case 1:
                combinedTileGraphics.drawImage(laserHorizontal, 0, 0, null);
                break;
            case 2:
                combinedTileGraphics.drawImage(laserDoubleHorizontal, 0, 0, null);
                break;
            case 3:
                combinedTileGraphics.drawImage(laserTripleHorizontal, 0, 0, null);
                break;
            default:
                LOGGER.debug("No horizontal laser detected");
        }

        return returnImage;
    }

    private Integer getLaserCountForATile(Tile tile, Orientation orientation) {
        Integer count = 0;
        Wall wall = getWallInDirection(tile, orientation);
        if(wall != null) {
            count = wall.hasLaser();
        }
        wall = getWallInDirection(tile, Orientation.getOppositeDirection(orientation));
        if(wall != null && count.equals(0)) {
            count = wall.hasLaser();
        }
        return count;
    }

    private Integer getLaserCountInDirection(Tile tile, Orientation orientation) {
        Tile currentWorkingTile = tile;
        if(getWallInDirection(tile, orientation) != null) {
            return 0;
        }
        while (currentWorkingTile.getNeighbors().containsKey(orientation)) {
            currentWorkingTile = currentWorkingTile.getNeighbors().get(orientation);
            if(getWallInDirection(currentWorkingTile, Orientation.getOppositeDirection(orientation)) != null) {
                return 0;
            }
            Wall wall = getWallInDirection(currentWorkingTile, orientation);
            if(wall == null) {
                continue;
            }
            return wall.hasLaser();
        }
        return 0;
    }

    private Wall getWallInDirection(Tile tile, Orientation orientation) {
        for(Wall wall : tile.getWalls()) {
            if(wall.getOrientation().equals(orientation)) {
                return wall;
            }
        }
        return null;
    }

}
