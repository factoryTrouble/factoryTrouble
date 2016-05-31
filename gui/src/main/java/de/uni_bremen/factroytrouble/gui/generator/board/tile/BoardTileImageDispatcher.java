/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gui.generator.board.tile;

import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.gameobjects.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Component
public class BoardTileImageDispatcher {

    @Autowired private FlagTileGenerator flagTileGenerator;
    @Autowired private ConveyorBeltTileGenerator conveyorBeltTileGenerator;
    @Autowired private TileWallGenerator tileWallGenerator;
    @Autowired private TileLaserDispatcher tileLaserDispatcher;

    private BufferedImage emptyTile;
    private BufferedImage gearCCW;
    private BufferedImage gearCW;
    private BufferedImage repair;
    private BufferedImage hole;

    /**
     * Initalisiert alle Bilder
     */
    @PostConstruct
    public void init() {
        try {
            emptyTile = ImageIO.read(getClass().getResourceAsStream("/game/tiles/empty.png"));
            gearCCW = ImageIO.read(getClass().getResourceAsStream("/game/tiles/gear-ccw.png"));
            gearCW = ImageIO.read(getClass().getResourceAsStream("/game/tiles/gear-cw.png"));
            repair = ImageIO.read(getClass().getResourceAsStream("/game/tiles/repair.png"));
            hole = ImageIO.read(getClass().getResourceAsStream("/game/tiles/void.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Erzeugt das Bild einer einzelnen Kachel.
     * 
     * @param tile
     * @return
     */
    public BufferedImage dispatch(Tile tile) {
        BufferedImage tileGround = dispatchTileGround(tile);
        if(!tile.getWalls().isEmpty()) {
            return tileLaserDispatcher.getTileWithLaser(tile, tileWallGenerator.generateTileWithWall(tile, tileGround));
        }
        return tileLaserDispatcher.getTileWithLaser(tile, tileGround);
    }

    private BufferedImage dispatchTileGround(Tile tile) {
        if(tile.getFieldObject() instanceof Hole) {
            return hole;
        }
        if(tile.getFieldObject() instanceof Workshop) {
            return repair;
        }
        if(tile.getFieldObject() instanceof ConveyorBelt) {
            return conveyorBeltTileGenerator.generateImage(tile);
        }
        if(tile.getFieldObject() instanceof Gear) {
            if(((Gear) tile.getFieldObject()).rotatesLeft()) {
                return gearCCW;
            }
            return gearCW;
        }
        if(tile.getFieldObject() instanceof Flag) {
            return flagTileGenerator.generateFlagTileImage(tile);
        }
        return emptyTile;
    }

}
