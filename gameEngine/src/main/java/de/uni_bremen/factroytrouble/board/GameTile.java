/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.board;

import de.uni_bremen.factroytrouble.gameobjects.FieldObject;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.gameobjects.Wall;
import de.uni_bremen.factroytrouble.player.GameMasterFactory;
import de.uni_bremen.factroytrouble.player.MasterFactory;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementierung von Tile
 * 
 * @author Thorben
 */
public class GameTile implements Tile {

    private final int gameId;
    private Point coordinates;
    private List<Wall> walls = new ArrayList<>();
    private FieldObject fieldObject;
    private Robot robot;
    private Map<Orientation, Tile> neighbors;

    private MasterFactory factory = new GameMasterFactory();

    public GameTile(int gameId, Point coordinates, Wall... walls) {
        this.gameId = gameId;
        init(coordinates, walls, null, null);
    }

    public GameTile(int gameId, Point coordinates, FieldObject fieldObject, Robot robot, Wall... walls) {
        this.gameId = gameId;
        init(coordinates, walls, fieldObject, robot);
    }

    private void init(Point coordinates, Wall[] walls, FieldObject fieldObject, Robot robot) {
        this.coordinates = coordinates;
        for (Wall wall : walls) {
            this.walls.add(wall);
        }
        this.fieldObject = fieldObject;
        this.robot = robot;
    }

    @Override
    public void action() {
        if (fieldObject != null) {
            fieldObject.execute(this);
        }
    }

    @Override
    public void setFieldObject(FieldObject object) {
        this.fieldObject = object;
    }

    @Override
    public void removeFieldObject() {
        fieldObject = null;
    }

    @Override
    public FieldObject getFieldObject() {
        return fieldObject;
    }

    @Override
    public void setRobot(Robot robot) {
        this.robot = robot;
    }

    @Override
    public Robot getRobot() {
        return robot;
    }

    @Override
    public List<Wall> getWalls() {
        return walls;
    }

    @Override
    public Wall getWall(Orientation direction) {
        for (Wall wall : walls) {
            if (wall.getOrientation() == direction) {
                return wall;
            }
        }
        return null;
    }

    @Override
    public Point getCoordinates() {
        return coordinates;
    }

    @Override
    public boolean hasWall(Orientation direction) {
        for (Wall wall : walls) {
            if (wall.getOrientation() == direction) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Tile clone() {
        Point newCoordinates = new Point(coordinates.x, coordinates.y);
        List<Wall> newWalls = new ArrayList<>();

        walls.forEach(wall -> newWalls.add(wall.clone()));

        FieldObject objectClone = fieldObject == null ? null : fieldObject.clone();

        Tile clone = new GameTile(gameId, newCoordinates, objectClone, null, newWalls.toArray(new Wall[newWalls.size()]));
        Robot clonedRobot = robot == null ? null : robot.clone(clone);

        clone.setRobot(clonedRobot);

        return clone;
    }

    @Override
    public Point getAbsoluteCoordinates() {
        if (factory == null) {
            factory = new GameMasterFactory();
        }

        return factory.getMaster(gameId).getBoard().getAbsoluteCoordinates(this);
    }

    @Override
    public Map<Orientation, Tile> getNeighbors() {
        if (neighbors == null) {
            findNeighbors();
        }
        return neighbors;
    }

    private void findNeighbors() {
        neighbors = new HashMap<>();
        Board board = factory.getMaster(gameId).getBoard();
        for (Orientation direction : Orientation.values()) {
            Tile nextTile = board.findNextTile(this, direction);
            if (nextTile != null) {
                neighbors.put(direction, nextTile);
            }
        }
    }
}
