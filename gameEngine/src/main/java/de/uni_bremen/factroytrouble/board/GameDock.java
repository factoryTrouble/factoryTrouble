/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.board;

import java.awt.Point;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.player.Master;

/**
 * Implementierung von Dock
 * 
 * @author Thorben
 */
public class GameDock extends GameField implements Dock {

    private Map<Robot, Tile> startPositions = new HashMap<>();

    public GameDock(Point coordinates, Tile... tiles) {
        super(coordinates, tiles);
        setStartPositions();
    }

    private void setStartPositions() {
        int robotCounter = 0;
        for (Tile tile : tiles.values()) {
            Robot robot = tile.getRobot();
            if (robot != null) {
                if (robotCounter == Master.MAX_PLAYERS) {
                    tile.setRobot(null);
                    break;
                } else {
                    startPositions.put(robot, tile);
                    robotCounter++;
                }
            }
        }
    }

    @Override
    public Tile getStartPosition(Robot robot) {
        return startPositions.get(robot);
    }

    @Override
    public Dock clone() {
        Field fieldClone = super.clone();
        Collection<Tile> newTiles = fieldClone.getTiles().values();

        return new GameDock(fieldClone.getCoordinates(), newTiles.toArray(new Tile[newTiles.size()]));
    }

}
