/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.board;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uni_bremen.factroytrouble.gameobjects.Robot;

/**
 * Implementierung von Field
 * 
 * @author Thorben
 */
public class GameField implements Field {

    protected Point coordinates;
    protected Map<Point, Tile> tiles = new HashMap<>();

    public GameField(Point coordinates, Tile... tiles) {
        this.coordinates = coordinates;
        for (Tile tile : tiles) {
            if (!this.tiles.containsKey(tile.getCoordinates())) {
                this.tiles.put(tile.getCoordinates(), tile);

            }
        }
    }

    @Override
    public Map<Point, Tile> getTiles() {
        return tiles;
    }

    @Override
    public Point getCoordinates() {
        return coordinates;
    }

    @Override
    public boolean hasTile(Tile tile) {
        return tiles.containsValue(tile);
    }

    @Override
    public Field clone() {
        Point newCoordinates = new Point(coordinates.x, coordinates.y);

        List<Tile> newTiles = new ArrayList<>();
        tiles.values().forEach(tile -> newTiles.add(tile.clone()));
        
        Map<Point, Tile> newMap = new HashMap<>();
        newTiles.forEach(tile -> newMap.put(tile.getCoordinates(), tile));

        // Respawn-Punkte der geklonten Roboter setzen
        newTiles.forEach(tile -> {
            Robot robot = tile.getRobot();
            if (robot != null) {
                Tile robotTile = robot.getCurrentTile();
                Point point = robotTile.getCoordinates();
                Point respawnPoint = tiles.get(point).getRobot().getRespawnPoint().getCoordinates();
                robot.setCurrentTile(newMap.get(respawnPoint));
                robot.setRespawnPoint();
                robot.setCurrentTile(robotTile);
            }
        });

        return new GameField(newCoordinates, newTiles.toArray(new Tile[newTiles.size()]));
    }
}
