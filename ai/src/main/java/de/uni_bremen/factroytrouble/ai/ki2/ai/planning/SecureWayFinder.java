/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki2.ai.planning;

import java.awt.Point;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uni_bremen.factroytrouble.ai.ki2.api.decision.unconsciousness.UnconsciousnessUnit;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;

public class SecureWayFinder extends PathFinder {
	
    public SecureWayFinder(UnconsciousnessUnit uncons) {
        super(uncons);
    }

    /**
     * Fügt neue mögliche sichere Wege zur Planung hinzu
     * 
     * @param way,
     *            Bisher genutzter Weg
     * @param point,
     *            Ausgangsfeld
     * @param left,
     *            Soll der neue Weg nach links gehen?
     */
    public Map<List<Integer>, List<Point>> addNewWays(List<Integer> way, Point point, Boolean left) {
        HashMap<List<Integer>, List<Point>> addedWays = new HashMap<>();
        int turn = left ? -1 : -3;
        List<Integer> wayCopy = new ArrayList<>();
        for (int a : way) {
            wayCopy.add(a);
        }
        Point newPoint = findNewPoint(point);
        newPoint = findNewPoint(newPoint);
        Orientation newOrient = turnWithBoardElements(point, findOrientation(wayCopy));
        List<Point> start = findNextEndPoint(newPoint, Orientation.getNextDirection(newOrient, left));
        if (!start.isEmpty() && !wayCopy.isEmpty()) {
            if (wayCopy.get(way.size() - 1) <= 0) {
                return new HashMap<>();
            }
            wayCopy.remove(way.size() - 1);
            int indexOfPoint = findNextEndPoint(getEndpoint(wayCopy), findOrientation(wayCopy)).indexOf(point) + 1;
            if (indexOfPoint == 0) {
                return new HashMap<>();
            }
            wayCopy.add(indexOfPoint);
            wayCopy.add(turn);
            getClosestToGoal(start);
            wayCopy.add(start.indexOf(getClosestToGoal(start)) + 1);
            addedWays.put(wayCopy, start);
        }
        return addedWays;
    }

    /**
     * Fügt neue mögliche sichere Wege zur Planung hinzu
     * 
     * @param way,
     *            Bisher genutzter Weg
     * @param point,
     *            Ausgangsfeld
     */
    public Map<List<Integer>, List<Point>> addNewWaysForward(List<Integer> way, Point point) {
        HashMap<List<Integer>, List<Point>> addedWays = new HashMap<>();
        List<Integer> wayCopy = new ArrayList<>();
        for (int a : way) {
            wayCopy.add(a);
        }
        if (!wayCopy.isEmpty()) {
            if (wayCopy.get(way.size() - 1) <= 0) {
                return new HashMap<>();
            }
            wayCopy.remove(way.size() - 1);
            int indexOfPoint = findNextEndPoint(getEndpoint(wayCopy), findOrientation(wayCopy)).indexOf(point) + 1;
            if (indexOfPoint == 0) {
                return new HashMap<>();
            }
            wayCopy.add(indexOfPoint);
            Point newPoint = findNewPoint(getEndpoint(wayCopy));
            List<Point> start = findNextEndPoint(newPoint, findOrientation(wayCopy));
            if (start.isEmpty()) {
                return new HashMap<>();
            }
            getClosestToGoal(start);
            wayCopy.add(start.indexOf(getClosestToGoal(start)) + 1);
            addedWays.put(wayCopy, start);
        }
        return addedWays;
    }

    /**
     * Bearbeitet den bisher genutzen Weg
     * 
     * @param way,
     *            Bisher genutzter Weg
     * @param path,
     *            Ausgangspfad
     * @param point,
     *            Ausgangsfeld
     */
    public Map<List<Integer>, List<Point>> editOne(List<Integer> way, List<Point> path, Point point) {
        HashMap<List<Integer>, List<Point>> addedWays = new HashMap<>();
        List<Integer> wayCopy = new ArrayList<>();
        for (int a : way) {
            wayCopy.add(a);
        }
        if (!wayCopy.isEmpty()) {
            if (wayCopy.get(way.size() - 1) <= 0) {
                return new HashMap<>();
            }
            wayCopy.remove(way.size() - 1);
            int indexOfPoint = findNextEndPoint(getEndpoint(wayCopy), findOrientation(wayCopy)).indexOf(point) + 1;
            if (indexOfPoint == 0) {
                return new HashMap<>();
            }
            wayCopy.add(indexOfPoint);
            path.remove(point);
            addedWays.put(wayCopy, path);
        }
        return addedWays;
    }

    /**
     * Schaut wie weit man vorwärts laufen kann, ohne in ein Loch oder eine Wand
     * zu laufen. Unsichere Felder werden nicht berücksichtigt.
     * 
     * @param start
     *            Startposition
     * @param orientation
     *            Richtung in die gelaufen werden soll
     * @return List mit Feldern die erreichbar sind
     */
    public List<Point> findNextEndPoint(Point start, Orientation orientation) {
        List<Point> holes = getHoles();
        EnumMap<Orientation, List<Point>> walls = new EnumMap<>(Orientation.class);
        walls.put(Orientation.NORTH, getWalls(Orientation.NORTH));
        walls.put(Orientation.EAST, getWalls(Orientation.EAST));
        walls.put(Orientation.SOUTH, getWalls(Orientation.SOUTH));
        walls.put(Orientation.WEST, getWalls(Orientation.WEST));
        List<Point> result = new ArrayList<>();
        Point position = start.getLocation();
        while (true) {
            result.add(position.getLocation());
            if (holes.contains(getNeighbors(position, orientation))) {
                result.remove(start);
                return result;
            }
            if (walls.get(orientation).contains(position) || walls.get(Orientation.getOppositeDirection(orientation))
                    .contains(getNeighbors(position, orientation))) {
                for (int i = 0; i < 3; i++) {
                    result.add(position);
                }
                result.remove(start);
                return result;
            }
            Point positionTemp = getNeighbors(position, orientation);
            if (holes.contains(getNeighbors(positionTemp, orientation))) {
                result.remove(start);
                return result;
            }
            if (getEnemy(positionTemp)) {
                result.remove(start);
                return result;
            }
            position = positionTemp;
        }
    }

    private Point findNewPoint(Point point) {
        Point newPoint = point;
        if (getConveyor().containsKey(point)) {
            newPoint = getConveyor().get(point).getPointAfterConveyor();
        }
        return newPoint;
    }
    	
	/**
     * Steht ein Gegner auf diese Position?
     * 
     * @return ob ein Gegner da steht
    */
    private boolean getEnemy(Point position) {
        for (Entry<Point, Point> enemy : getEnemies().entrySet()) {
            Point enemyPos = enemy.getKey();
            if (enemyPos.equals(position)) {
                return true;
            }
        }
        return false;
    }
}