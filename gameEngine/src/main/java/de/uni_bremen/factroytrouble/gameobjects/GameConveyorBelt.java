/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gameobjects;

import de.uni_bremen.factroytrouble.board.Board;
import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.player.GameMasterFactory;
import de.uni_bremen.factroytrouble.player.MasterFactory;
import org.apache.log4j.Logger;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementierung der ConveyorBelts.
 *
 * @author Thorben
 */

public class GameConveyorBelt implements ConveyorBelt {

    public static final String ROBOTER_WITH_SPACE_AT_END = "Roboter ";
    private static final Logger LOGGER = Logger.getLogger(ConveyorBelt.class);
    private static List<ConveyorBelt> movedConveyors = new ArrayList<>();
    private final int gameId;

    private Orientation orientation;
    private boolean isExpress;

    private MasterFactory factory = new GameMasterFactory();

    public GameConveyorBelt(int gameId, Orientation orientation, boolean isExpress) {
        this.gameId = gameId;
        this.orientation = orientation;
        this.isExpress = isExpress;
    }

    public static void makeAllActive() {
        movedConveyors = new ArrayList<>();
    }

    @Override
    public void execute(Tile tile) {
        Robot robot = tile.getRobot();
        if (robot == null) {
            return;
        }

        if (movedConveyors.contains(this)) {
            return;
        }

        movedConveyors.add(this);

        Board board = factory.getMaster(gameId).getBoard();
        Tile destination = board.findNextTile(tile, orientation);

        if (destination == null) {
            robot.setCurrentTile(destination);
            tile.setRobot(null);

            StringBuilder builder = new StringBuilder();
            builder.append(ROBOTER_WITH_SPACE_AT_END).append(robot.getName())
                    .append(" von Förderband von Brett runter geschoben");
            LOGGER.info(builder.toString());
        } else {
            if (destination.getFieldObject() instanceof ConveyorBelt) {
                moveOtherFirstIfNecessary(destination);
            }

            if ((!tile.hasWall(orientation)) && (!destinationBlocked(board, tile, destination))) {
                destination.setRobot(robot);
                robot.setCurrentTile(destination);
                tile.setRobot(null);

                StringBuilder builder = new StringBuilder();
                builder.append("Roboter ").append(robot.getName()).append(" von Förderband verschoben auf Kachel ")
                        .append(getStringOfPoint(destination.getCoordinates())).append(" des Feldes ")
                        .append(getStringOfPoint(board.findFieldOfTile(destination).getCoordinates()));
                LOGGER.info(builder.toString());

                turnRobot(destination, robot);
            }
        }
    }

    private void moveOtherFirstIfNecessary(Tile destination) {
        ConveyorBelt otherBelt = (ConveyorBelt) destination.getFieldObject();
        if (sameTypeAs(otherBelt)) {
            otherBelt.execute(destination);
        }
        return;
    }

    private boolean sameTypeAs(ConveyorBelt otherBelt) {
        return (isExpress && otherBelt.isExpress()) || (!isExpress && !otherBelt.isExpress());
    }

    private boolean destinationBlocked(Board board, Tile start, Tile destination) {
        if (destination.getRobot() != null) {
            return true;
        }
        if (destination.hasWall(Orientation.getOppositeDirection(orientation))) {
            return true;
        }

        Tile[] neighbors = findNeighbors(board, destination);

        for (Tile tile : neighbors) {
            // nicht blockiert, wenn Nachbar nicht existent,
            // wenn gleich Ausgangsfeld, wenn kein Roboter auf Nachbar
            if ((tile == null) || (tile == start) || (tile.getRobot() == null)) {
                continue;
            }
            FieldObject object = tile.getFieldObject();
            // nicht blockiert, wenn Nachbar kein Förderband hat
            if (!(object instanceof ConveyorBelt)) {
                continue;
            }
            // nicht blockiert, wenn Nachbarförderband in eine andere Richtung
            // zeigt
            if (!(board.findNextTile(tile, ((ConveyorBelt) object).getOrientation()) == destination)) {
                continue;
            }
            // nicht blockiert, wenn auf besagtem Feld ein Loch ist
            if (destination.getFieldObject() instanceof Hole) {
                continue;
            }
            // nur blockiert, wenn beide Förderbänder express/ nicht express
            // sind
            if (sameTypeAs((ConveyorBelt) object)) {
                return true;
            }
        }
        return false;
    }

    private Tile[] findNeighbors(Board board, Tile tile) {
        Tile[] neighbors = new Tile[4];
        for (int i = 0; i < 4; i++) {
            neighbors[i] = board.findNextTile(tile, Orientation.values()[i]);
        }
        return neighbors;
    }

    @Override
    public boolean isExpress() {
        return isExpress;
    }

    @Override
    public Orientation getOrientation() {
        return orientation;
    }

    private void turnRobot(Tile destination, Robot robot) {
        FieldObject object = destination.getFieldObject();
        if (!(object instanceof ConveyorBelt)) {
            return;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("Roboter ").append(robot.getName()).append(" von Förderband gedreht; schaut jetzt nach ");
        Orientation otherOrientation = ((ConveyorBelt) object).getOrientation();
        if (Orientation.getNextDirection(orientation, true) == otherOrientation) {
            robot.turn(true);
            builder.append(robot.getOrientation());
            LOGGER.info(builder.toString());
        } else if (Orientation.getNextDirection(orientation, false) == otherOrientation) {
            robot.turn(false);
            builder.append(robot.getOrientation());
            LOGGER.info(builder.toString());
        }
    }

    private String getStringOfPoint(Point point) {
        StringBuilder builder = new StringBuilder();

        builder.append('(').append(point.x).append(',').append(point.y).append(')');

        return builder.toString();
    }

    @Override
    public ConveyorBelt clone() {
        return new GameConveyorBelt(gameId, orientation, isExpress);
    }
}
