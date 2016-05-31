/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki_classic.path_finding.dykstra;

import de.uni_bremen.factroytrouble.ai.ki_classic.path_finding.dykstra.engine.Dystra;
import de.uni_bremen.factroytrouble.ai.ki_classic.path_finding.dykstra.engine.Edge;
import de.uni_bremen.factroytrouble.ai.ki_classic.path_finding.dykstra.engine.Graph;
import de.uni_bremen.factroytrouble.ai.ki_classic.path_finding.dykstra.engine.Vertex;
import de.uni_bremen.factroytrouble.api.ki_classic.ImpossibleFlagException;
import de.uni_bremen.factroytrouble.api.ki_classic.ImpossibleOrientationException;
import de.uni_bremen.factroytrouble.board.Field;
import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.gameobjects.*;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.player.Master;
import org.apache.log4j.Logger;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Stellt der klassischen KI den Dykstra-Algorithmus zum Bewerten verschiedener
 * Positionen zur Verfuegung.
 * 
 * @author Johannes, Markus
 */

public class DykstraHandler {
    private static final Logger LOGGER = Logger.getLogger(DykstraHandler.class);
    private static final Integer LOOKINTOHOLE = 80;
    private static final Integer DEATHPENALTY = 1000;
    private static final Integer FLAGVALUE = 10000;

    private final List<Flag> flags;
    private final Map<Integer, Map<Point, Integer>> weightFields;
    private final Graph graph;
    private final HashMap<Point, Tile> tileMap;
    private final Map<Point, Vertex> vertexList;
    private final List<Edge> edgeList;
    private Point respawnPoint = null;
    private int maxX = 0;
    private int minY = 0;
    private int minX = 0;
    private int maxY = 0;

    /**
     * Erzeugt neue Instanz.
     * 
     * @param master
     *            der Master, der die Referenz zum Board liefert.
     */
    public DykstraHandler(Master master) {
        vertexList = new HashMap<>();
        edgeList = new ArrayList<>();
        flags = new ArrayList<>();
        weightFields = new HashMap<>();
        tileMap = new HashMap<>();
        createAllValueMaps(master.getBoard().getFields());
        this.graph = new Graph(vertexList, edgeList);
        flags.forEach(flag -> {
            Dystra dystra = new Dystra(graph);
            weightFields.put(flag.getFlagNumber(), dystra.execute(flag.getPoint()));
        });
    }

    /**
     * Hat der gegebene Roboter keinen validen RespawnPoint, und es ist keiner
     * gespeichert, wird einer gesetzt.
     * 
     * @param robot
     *            Der Roboter, dessen RespawnPoint gesetzt werden soll
     */
    public void updateRespawnPoint(Robot robot) {
        if (robot.getRespawnPoint() == null && this.respawnPoint == null) {
            this.respawnPoint = robot.getCurrentTile().getAbsoluteCoordinates();
        } else {
            this.respawnPoint = robot.getRespawnPoint().getAbsoluteCoordinates();
        }
    }

    /**
     * Bewertet ein Tile; beachtet die Ausrichtung des Roboters.
     * 
     * @param flagNumber
     *            die zu erreichende Flagge.
     * @param tile
     *            Das Tile, auf dem der Roboter steht.
     * @param orientation
     *            Die Richtung, in die der Roboter zeigt.
     * @return Die Bewertung der position mit der Ausrichtung. Je kleiner desto
     *         besser.
     */
    public int getWeight(int flagNumber, Point tile, Orientation orientation) {
        int weightValue;
        weightValue = getWeightForTile(flagNumber, tile);
        weightValue += calcDirectionPenalty(flagNumber, tile, orientation);
        return weightValue;
    }

    /*
     * Bewertet ein Tile.
     * 
     */
    private int getWeightForTile(int flagNumber, Point tile) {
        int weightValue;
        weightValue = (flags.size() - flagNumber) * FLAGVALUE;
        if (weightFields.get(flagNumber) == null) {
            throw new ImpossibleFlagException();
        }
        boolean isHole = (weightFields.get(flagNumber).get(tile) == null)
                || (tile == null || tile.equals(new Point(-1, -1)));
        if (isHole) {
            return weightValue + weightFields.get(flagNumber).get(this.respawnPoint) + DEATHPENALTY;
        }
        weightValue += weightFields.get(flagNumber).get(tile);
        return weightValue;
    }

    /*
     * Bewertet für eine position die verschiedenen Richtungen.
     */
    private int calcDirectionPenalty(int flagNumber, Point tile, Orientation orientation) {
        Integer result = 0;
        switch (orientation) {
        case NORTH:
            result = weightFields.get(flagNumber).get(new Point(tile.x, tile.y + 1));
            break;
        case EAST:
            result = weightFields.get(flagNumber).get(new Point(tile.x + 1, tile.y));

            break;
        case SOUTH:
            result = weightFields.get(flagNumber).get(new Point(tile.x, tile.y - 1));

            break;
        case WEST:
            result = weightFields.get(flagNumber).get(new Point(tile.x - 1, tile.y));
            break;
        default:
            throw new ImpossibleOrientationException(orientation);
        }
        if (result == null) {
            return LOOKINTOHOLE;
        }

        if (weightFields.get(flagNumber).get(tile) == null) {
            return (result - weightFields.get(flagNumber).get(respawnPoint)) / 10;
        }

        return (result - weightFields.get(flagNumber).get(tile)) / 10;
    }

    private void createAllValueMaps(Map<Point, Field> fields) {
        convertBoardToMap(fields);
        convertMapToGraph();

    }

    private void convertMapToGraph() {
        addVertexesToGraph();
        addEdgesToGraph();

    }

    private void addEdgesToGraph() {
        tileMap.forEach(this::createEdgesForTile);

    }

    private void createEdgesForTile(Point point, Tile tile) {
        if (tile == null) {
            throw new IllegalArgumentException("Tile darf nicht Null sein");
        }
        Edge north = null;
        Edge east = null;
        Edge south = null;
        Edge west = null;

        Point neighborNorth = new Point(point.x, point.y + 1);
        Point neighborEast = new Point(point.x + 1, point.y);
        Point neighborWest = new Point(point.x - 1, point.y);
        Point neighborSouth = new Point(point.x, point.y - 1);

        final int baseValue = 100;

        if (point.y + 1 <= maxY)
            north = new Edge(point, neighborNorth, baseValue);
        if (point.x - 1 >= minX)
            west = new Edge(point, neighborWest, baseValue);
        if (point.y - 1 >= minY)
            south = new Edge(point, neighborSouth, baseValue);
        if (point.x + 1 <= maxX)
            east = new Edge(point, neighborEast, baseValue);

        FieldObject fieldObj = tile.getFieldObject();
        if (fieldObj != null) {
            if (fieldObj instanceof de.uni_bremen.factroytrouble.gameobjects.Flag) {
                de.uni_bremen.factroytrouble.gameobjects.Flag flag = (de.uni_bremen.factroytrouble.gameobjects.Flag) fieldObj;
                flags.add(new Flag(point, flag.getNumber()));
            } else if (fieldObj instanceof ConveyorBelt) {
                conveyorBeltEvaluation(tile, north, east, south, west);
            }
        }
        if (north != null && noWall(point, Orientation.NORTH) && noHole(neighborNorth)
                && noWall(neighborNorth, Orientation.SOUTH)) {
            edgeList.add(north);
        }
        if (south != null && noWall(point, Orientation.SOUTH) && noHole(neighborSouth)
                && noWall(neighborSouth, Orientation.NORTH)) {
            edgeList.add(south);
        }

        if (east != null && noWall(point, Orientation.EAST) && noHole(neighborEast)
                && noWall(neighborEast, Orientation.WEST)) {
            edgeList.add(east);
        }
        if (west != null && noWall(point, Orientation.WEST) && noHole(neighborWest)
                && noWall(neighborWest, Orientation.EAST)) {
            edgeList.add(west);
        }
    }

    private void conveyorBeltEvaluation(Tile tile, Edge north, Edge east, Edge south, Edge west) {
        ConveyorBelt belt = (GameConveyorBelt) tile.getFieldObject();
        switch (belt.getOrientation()) {
        case NORTH:
            modifyWeightForConveyorBeltEdge(north, belt, -1);
            modifyWeightForConveyorBeltEdge(south, belt, 1);
            break;
        case SOUTH:
            modifyWeightForConveyorBeltEdge(north, belt, 1);
            modifyWeightForConveyorBeltEdge(south, belt, -1);
            break;
        case EAST:
            modifyWeightForConveyorBeltEdge(east, belt, -1);
            modifyWeightForConveyorBeltEdge(west, belt, 1);
            break;
        case WEST:
            modifyWeightForConveyorBeltEdge(east, belt, 1);
            modifyWeightForConveyorBeltEdge(west, belt, -1);
            break;
        default:
            throw new ImpossibleOrientationException(belt.getOrientation());
        }
    }

    private void modifyWeightForConveyorBeltEdge(Edge edge, ConveyorBelt belt, Integer factor) {
        if (edge != null) {
            if (belt.isExpress()) {
                edge.modifyWeight(40 * factor);
            } else {
                edge.modifyWeight(20 * factor);
            }
        }
    }

    private boolean noHole(Point point) {
        return !(tileMap.get(point).getFieldObject() instanceof GameHole);
    }

    private boolean noWall(Point point, Orientation orientation) {
        return !tileMap.get(point).hasWall(orientation);
    }

    private void addVertexesToGraph() {
        tileMap.keySet().forEach(tile -> vertexList.put(tile, new Vertex(tile)));
    }

    /**
     * Convertiert die Map von Feldern in eine Map mit allen Tiles, mit den
     * Absoluten Coordinaten als Key
     *
     * @param fields
     *            Alle Feld in Form einer Map
     */
    private void convertBoardToMap(Map<Point, Field> fields) {
        fields.values().forEach(field -> addAllTilesToMap(field.getTiles()));
    }

    /**
     * Diese Methode fügt alle Tiles eines Feldes zur tileMap hinzu, wobei die
     * max und min Coordinaten gefunden werden und die Positionen in Absolute
     * Positionen umgerechnet werdeniein Feld
     *
     * @param map
     *            Die Tile Map eines Feldes
     */
    private void addAllTilesToMap(Map<Point, Tile> map) {
        map.values().forEach(tile -> {
            Point absolutePoint = tile.getAbsoluteCoordinates();
            if (maxX < absolutePoint.x) {
                maxX = absolutePoint.x;
            }
            if (maxY < absolutePoint.y) {
                maxY = absolutePoint.y;
            }
            if (minX > absolutePoint.x) {
                minX = absolutePoint.x;
            }
            if (minY > absolutePoint.y) {
                minY = absolutePoint.y;
            }
            tileMap.put(absolutePoint, tile);
        });
    }

    private class Flag {
        private final Point point;
        private final int flagNumber;

        private Flag(Point point, int flagNumber) {
            this.flagNumber = flagNumber;
            this.point = point;
        }

        private Point getPoint() {
            return point;
        }

        private int getFlagNumber() {
            return flagNumber;
        }
    }
}
