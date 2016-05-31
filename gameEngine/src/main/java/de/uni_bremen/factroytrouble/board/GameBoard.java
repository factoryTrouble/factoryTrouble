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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import de.uni_bremen.factroytrouble.gameobjects.ConveyorBelt;
import de.uni_bremen.factroytrouble.gameobjects.FieldObject;
import de.uni_bremen.factroytrouble.gameobjects.Flag;
import de.uni_bremen.factroytrouble.gameobjects.GameConveyorBelt;
import de.uni_bremen.factroytrouble.gameobjects.Gear;
import de.uni_bremen.factroytrouble.gameobjects.Hole;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.gameobjects.Wall;
import de.uni_bremen.factroytrouble.gameobjects.Workshop;
import de.uni_bremen.factroytrouble.observer.Event;
import de.uni_bremen.factroytrouble.observer.GameObserver;

/**
 * Implementierung von Board, Singleton
 * 
 * @author Thorben
 */
public class GameBoard implements Board {

    private static final Logger LOGGER = Logger.getLogger(GameBoard.class);

    private Map<Point, Field> fields;
    private List<Robot> robots;
    private List<Tile> laserTiles;
    private int lastFlag;
    private List<GameObserver> observerList = new ArrayList<>();

    public GameBoard(Dock dock, Field... fields) {
        setFields(dock, fields);
        LOGGER.info("neues Board instantiiert");
    }

    @Override
    public void buildBoard(Dock dock, Field... fields) {
        setFields(dock, fields);
        LOGGER.info("Neues Board aufgebaut");
    }

    private void setFields(Dock dock, Field... fields) {
        clearObjectLists();

        lastFlag = 0;

        if (dock != null) {
            this.fields.put(dock.getCoordinates(), dock);
            fillLaserAndRobotLists(dock);
            dock.getTiles().values().forEach(tile -> {
                FieldObject object = tile.getFieldObject();
                if (object instanceof Flag) {
                    int number = ((Flag) object).getNumber();
                    lastFlag = lastFlag < number ? number : lastFlag;
                }
            });
        }
        for (Field field : fields) {
            if (field != null) {
                this.fields.put(field.getCoordinates(), field);
                fillLaserAndRobotLists(field);
            }
            field.getTiles().values().forEach(tile -> {
                FieldObject object = tile.getFieldObject();
                if (object instanceof Flag) {
                    int number = ((Flag) object).getNumber();
                    lastFlag = lastFlag < number ? number : lastFlag;
                }
            });
        }
    }

    private void fillLaserAndRobotLists(Field field) {
        for (Tile tile : field.getTiles().values()) {

            List<Wall> wallList = tile.getWalls();

            for (Wall wall : wallList) {
                if (wall.hasLaser() > 0) {
                    laserTiles.add(tile);
                    break;
                }
            }

            Robot robot = tile.getRobot();

            if (robot != null) {
                robots.add(robot);
            }
        }
    }

    private void clearObjectLists() {
        fields = new HashMap<>();
        robots = new ArrayList<>();
        laserTiles = new ArrayList<>();
    }

    @Override
    public void executeNextRegisters() {
        Map<Integer, Robot> priorityList = new HashMap<>();

        robots.forEach(robot -> {
            if ((robot.isPoweredDown()) || robot.getCurrentTile() == null) {
                return;
            }
            int priority = robot.getNextPriority();
            priorityList.put(-priority, robot);
        });

        List<Integer> priorities = new ArrayList<>();
        priorities.addAll(priorityList.keySet());
        Collections.sort(priorities);

        priorities.forEach(priority -> {
            priorityList.get(priority).executeNext();
        });
    }

    @Override
    public void moveConveyors(boolean moveOnlyExpress) {
        List<Tile> tiles = new ArrayList<>();
        robots.forEach(robot -> {
            Tile tile = robot.getCurrentTile();
            if (tile == null) {
                return;
            }
            FieldObject object = tile.getFieldObject();
            if ((object instanceof ConveyorBelt) && ((!moveOnlyExpress) || ((ConveyorBelt) object).isExpress())) {
                tiles.add(tile);
            }
        });
        tiles.forEach(tile -> tile.action());

        // Aktiviere alle ConveyorBelts
        GameConveyorBelt.makeAllActive();
    }

    @Override
    public void rotateGears() {
        robots.forEach(robot -> {
            Tile tile = robot.getCurrentTile();
            if (tile == null) {
                return;
            }
            FieldObject object = tile.getFieldObject();
            if (object instanceof Gear) {
                robot.getCurrentTile().action();
            }
        });
    }

    @Override
    public void activatePushers(int registerPhase) {
        robots.forEach(robot -> {
            Tile tile = robot.getCurrentTile();
            if (tile == null) {
                return;
            }
            tile.getWalls().forEach(wall -> {
                for (int phase : wall.getPusherPhases()) {
                    if (phase == registerPhase) {
                        Tile destination = pushRobot(tile.getRobot(), Orientation.getOppositeDirection(wall.getOrientation()));
                        if((destination == null)||(!destination.equals(tile))) {
                            spamAll(robot, Event.PUSHED, null);
                        }
                        break;
                    }
                }
            });
        });
    }

    @Override
    public void touchCheckpointsAndFlags() {
        robots.forEach(robot -> {
            Tile tile = robot.getCurrentTile();
            if (tile == null) {
                return;
            }
            FieldObject object = tile.getFieldObject();
            if ((object instanceof Flag) || (object instanceof Workshop)) {
                tile.action();
            }
        });
    }

    @Override
    public Tile moveRobot(Robot robot) {
        Tile before = robot.getCurrentTile();
        Tile after = moveRobot(robot, robot.getOrientation());

        loggerRhytmus(robot, before, after, false);

        killRobots();

        return after;
    }

    @Override
    public Tile backupRobot(Robot robot) {
        Tile before = robot.getCurrentTile();
        Tile after = moveRobot(robot, Orientation.getOppositeDirection(robot.getOrientation()));

        loggerRhytmus(robot, before, after, false);

        killRobots();

        return after;
    }

    @Override
    public Tile pushRobot(Robot robot, Orientation direction) {
        if (robot == null) {
            return null;
        }

        Tile before = robot.getCurrentTile();
        Tile after = moveRobot(robot, direction);

        loggerRhytmus(robot, before, after, true);

        killRobots();

        return after;
    }

    private Tile moveRobot(Robot robot, Orientation direction) {
        Tile tile = robot.getCurrentTile();

        // Wenn der Roboter kaputt ist, kann er sich nicht bewegen
        // Wenn es eine Wand in Zugrichtung gibt, ist keine Bewegung möglich
        if ((tile == null) || (tile.hasWall(direction))) {
            return tile;
        }

        Tile nextTile = findNextTile(tile, direction);

        nextTile = moveToNextTile(robot, direction, tile, nextTile);

        tile.setRobot(null);
        robot.setCurrentTile(nextTile);
        if (nextTile != null) {
            nextTile.setRobot(robot);
        }

        return nextTile;
    }

    private Tile moveToNextTile(Robot robot, Orientation direction, Tile from, Tile to) {
        if (to != null) {
            // Wenn die Zielkachel eine Wand entgegengesetzt der Zugrichtung
            // hat,
            // ist keine Bewegung möglich
            if (to.hasWall(Orientation.getOppositeDirection(direction))) {
                return from;
            }

            // Schiebe evt. vorhandenen Roboter auf Zielkachel; wenn nicht
            // möglich,
            // ist keine Bewegung möglich
            Robot otherRobot = to.getRobot();
            if (otherRobot != null) {
                Tile pushedDestination = pushRobot(otherRobot, direction);
                if ((pushedDestination != null) && (pushedDestination.equals(to))) {
                    return from;
                }
                spamAll(otherRobot, Event.PUSHED, robot);
            }

            to.setRobot(robot);
        }

        return to;
    }

    @Override
    public Field findFieldOfTile(Tile tile) {
        for (Field field : fields.values()) {
            if (field.hasTile(tile)) {
                return field;
            }
        }
        return null;
    }

    @Override
    public Tile findNextTile(Tile tile, Orientation direction) {
        Field field = findFieldOfTile(tile);
        int xLength = Field.DIMENSION;
        int yLength = field instanceof Dock ? Dock.SHORT_SIDE : Field.DIMENSION;

        Point currentField = (Point) field.getCoordinates().clone();
        Point currentTile = (Point) tile.getCoordinates().clone();

        switch (direction) {
        case NORTH:
            currentTile.y++;
            break;
        case SOUTH:
            currentTile.y--;
            break;
        case WEST:
            currentTile.x--;
            break;
        case EAST:
            currentTile.x++;
            break;
        default:
            LOGGER.error("No directtion found");
        }

        currentTile = findNextField(currentField, currentTile, xLength, yLength);

        field = fields.get(currentField);
        if (field instanceof Dock) {
            currentTile.y = currentTile.y >= Dock.SHORT_SIDE ? Dock.SHORT_SIDE - 1 : currentTile.y;
        }

        return field == null ? null : field.getTiles().get(currentTile);
    }

    private Point findNextField(Point currentField, Point currentTile, int xLength, int yLength) {
        if (currentTile.x < 0) {
            currentField.x--;
            currentTile.x = xLength - 1;
        } else if (currentTile.x >= xLength) {
            currentField.x++;
            currentTile.x = 0;
        } else if (currentTile.y < 0) {
            currentField.y--;
            currentTile.y = yLength - 1;
        } else if (currentTile.y >= yLength) {
            currentField.y++;
            currentTile.y = 0;
        }

        return currentTile;
    }

    @Override
    public Tile respawnRobot(Robot robot) {

        if (robot.getCurrentTile() != null) {
            robot.getCurrentTile().setRobot(null);
            LOGGER.warn("Roboter " + robot.getName() + " respawnt, obwohl er nicht tot ist!");
        }

        Tile destination = robot.getRespawnPoint();
        Robot otherRobot = destination.getRobot();

        if (otherRobot != null) {
            List<Tile> blockedTileList = new ArrayList<>();
            blockedTileList.add(destination);
            destination = findFreeTile(robot, blockedTileList, new ArrayList<>());
        }

        robot.respawn(destination);

        loggerRhytmus(robot, null, destination, false);

        return destination;
    }

    private Tile findFreeTile(Robot robot, List<Tile> blockedTileList, List<Tile> ignoreTileList) {
        Orientation direction = Orientation.SOUTH;

        List<Tile> toAdd = new ArrayList<>();

        for (Tile blockedTile : blockedTileList) {
            if (ignoreTileList.contains(blockedTile)) {
                continue;
            }

            for (int i = 0; i < 4; i++) {

                Tile nextTile = findNextTile(blockedTile, direction);

                direction = Orientation.values()[(direction.ordinal() + 1) % 4];

                if ((nextTile == null) || (blockedTileList.contains(nextTile))) {
                    continue;
                }

                if ((nextTile.getFieldObject() instanceof Hole) || (nextTile.getRobot() != null)) {
                    toAdd.add(nextTile);
                } else {
                    return nextTile;
                }

            }

            ignoreTileList.add(blockedTile);

        }

        blockedTileList.addAll(toAdd);

        return findFreeTile(robot, blockedTileList, ignoreTileList);
    }

    @Override
    public void fireLasers() {
        // feuere zuerst die Laser von den Wänden eines Feldes...
        laserTiles.forEach(tile -> {
            for (Wall wall : tile.getWalls()) {
                for (int i = 0; i < wall.hasLaser(); i++) {
                    fireLaser(tile, Orientation.getOppositeDirection(wall.getOrientation()), wall);
                }
            }
        });
        // ...dann feuere die Laser der aktiven Roboter
        robots.forEach(robot -> {
            if (!robot.isPoweredDown()) {
                Tile tile = robot.getCurrentTile();
                Orientation direction = robot.getOrientation();
                if ((tile == null) || (tile.hasWall(direction))) {
                    return;
                }
                Tile nextTile = findNextTile(tile, direction);
                if ((nextTile == null) || (nextTile.hasWall(Orientation.getOppositeDirection(direction)))) {
                    return;
                }

                fireLaser(nextTile, direction, robot);
            }
        });

        killRobots();
    }

    private void fireLaser(Tile tile, Orientation direction, Object shooter) {

        if (tile == null) {
            return;
        }

        Robot robotToDamage = tile.getRobot();

        if (robotToDamage != null) {
            robotToDamage.takeDamage(shooter);
            return;
        }

        if (tile.hasWall(direction)) {
            return;
        }

        Tile nextTile = findNextTile(tile, direction);

        if ((nextTile != null) && (nextTile.hasWall(Orientation.getOppositeDirection(direction)))) {
            return;
        }

        fireLaser(nextTile, direction, shooter);
    }

    @Override
    public Map<Point, Field> getFields() {
        return fields;
    }

    @Override
    public List<Robot> killRobots() {
        List<Robot> list = new ArrayList<>();
        robots.forEach(robot -> {
            Tile tile = robot.getCurrentTile();
            if ((tile == null) || (tile.getFieldObject() instanceof Hole) || (robot.getHP() <= 0)) {
                robot.kill();
                list.add(robot);
            }
        });
        return list;
    }

    private void loggerRhytmus(Robot robot, Tile before, Tile after, boolean pushed) {
        if (before != after) {
            StringBuilder builder = new StringBuilder();

            builder.append("Roboter ").append(robot.getName());

            if (before == null) {
                builder.append(" respawnt");
            } else {
                String action = pushed ? " geschoben von Kachel " : " bewegt von Kachel ";

                builder.append(action).append(getStringOfPoint(before.getCoordinates())).append(" des Feldes ")
                        .append(getStringOfPoint(findFieldOfTile(before).getCoordinates()));
            }

            if (after == null) {
                builder.append(" in den Abgrund neben dem Spielbrett");
            } else {
                builder.append(" auf Kachel ").append(getStringOfPoint(after.getCoordinates())).append(" des Feldes ")
                        .append(getStringOfPoint(findFieldOfTile(after).getCoordinates()));
            }
            LOGGER.info(builder.toString());
        }
    }

    private String getStringOfPoint(Point point) {
        StringBuilder builder = new StringBuilder();

        builder.append('(').append(point.x).append(',').append(point.y).append(')');

        return builder.toString();
    }

    @Override
    public Point getAbsoluteCoordinates(Tile tile) {
        Field field = findFieldOfTile(tile);
        if (field == null) {
            return null;
        }

        Point fieldPoint = field.getCoordinates();
        Point tilePoint = tile.getCoordinates();

        int x = tilePoint.x + (fieldPoint.x * Field.DIMENSION);
        int y = fieldPoint.y > 0 ? tilePoint.y + Dock.SHORT_SIDE + ((fieldPoint.y - 1) * Field.DIMENSION) : tilePoint.y;

        return new Point(x, y);
    }

    @Override
    public int getHighestFlagNumber() {
        return lastFlag;
    }

    @Override
    public Board clone() {
        List<Field> newFields = new ArrayList<>();
        Dock newDock = null;

        for (Field field : fields.values()) {
            if (field instanceof Dock) {
                Dock oldDock = (Dock) field;
                newDock = oldDock.clone();
            } else {
                newFields.add(field.clone());
            }
        }

        LOGGER.info("Board wird geklont");
        return new GameBoard(newDock, newFields.toArray(new Field[newFields.size()]));
    }

    @Override
    public void attachObserver(GameObserver observer) {
        if (!observerList.contains(observer)) {
            observerList.add(observer);
        }
    }

    @Override
    public void removeObserver(GameObserver observer) {
        
        observerList.remove(observer);
    }
    
    private void spamAll(Robot robot, Event event, Object source) {
        observerList.forEach(observer -> observer.spam(robot, event, source));
    }
}
