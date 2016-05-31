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

public class PathFinder extends Finder {
    HashMap<List<Integer>, List<Point>> planningHelper;

    public PathFinder(UnconsciousnessUnit uncons) {
        super(uncons);
    }

    /**
     * Findet einen möglichen Weg im momentanen Planungsschritt
     * 
     * @return Die zu legenden Karten
     */
    public List<List<Integer>> findPath() {
        if (planningHelper == null) {
            initiateList();
        }
        List<List<Integer>> possibleWays = new ArrayList<>();
        HashMap<List<Integer>, List<Point>> planningHelperCopy = new HashMap<>();

        for (Entry<List<Integer>, List<Point>> way : planningHelper.entrySet()) {
            if (!way.getValue().isEmpty() && way.getKey().size() < 5) {
                Point closestPoint = getClosestToGoal(way.getValue());
                planningHelperCopy.putAll(addNewWays(way.getKey(), closestPoint, true));
                planningHelperCopy.putAll(addNewWays(way.getKey(), closestPoint, false));
                planningHelperCopy.putAll(addNewWaysForward(way.getKey(), closestPoint));
                planningHelperCopy.putAll(editOne(way.getKey(), way.getValue(), closestPoint));
            }
            possibleWays.add(way.getKey());
        }
        for (Entry<List<Integer>, List<Point>> a : planningHelperCopy.entrySet()) {
            planningHelper.put(checkDistance(a.getKey()), a.getValue());
        }
        return possibleWays;
    }

    /**
     * Schaut ob man überhaupt passende Karten für einen Weg hat, und
     * modifiziert den Plan gegebenenfalls
     * 
     * @param way,
     *            der Weg
     * @return der modifizierte Weg
     */
    public List<Integer> checkDistance(List<Integer> way) {
        List<Integer> newWay = checkWay(way);
        if (newWay.size() > 4) {
            newWay = newWay.subList(0, 5);
        }
        return newWay;
    }

    /**
     * Fügt Anfangswege zur Planung hinzu
     * 
     * @return Erste Planung
     */
    public void initiateList() {
        planningHelper = new HashMap<>();
        Point pos = getPos();
        Orientation orient = getOrientation();

        for (Orientation possibleOrient : Orientation.values()) {
            List<Point> start = findNextObstacle(pos, possibleOrient);
            List<Integer> way = new ArrayList<>();
            if (!start.isEmpty()) {
                int turn = orient.ordinal() - possibleOrient.ordinal();
                turn = (turn > 0) ? turn - 4 : turn;
                if (turn != 0) {
                    way.add(turn);
                }
                way.add(start.size());
                planningHelper.put(way, start);
            }
        }
    }

    /**
     * Fügt neue mögliche Wege zur Planung hinzu
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
        List<Point> start = findNextObstacle(newPoint, Orientation.getNextDirection(newOrient, left));
        if (!start.isEmpty() && !wayCopy.isEmpty()) {
            if (wayCopy.get(way.size() - 1) <= 0) {
                return new HashMap<>();
            }
            wayCopy.remove(way.size() - 1);
            int indexOfPoint = findNextObstacle(getEndpoint(wayCopy), findOrientation(wayCopy)).indexOf(point) + 1;
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
     * Fügt neue mögliche Wege zur Planung hinzu
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
            int indexOfPoint = findNextObstacle(getEndpoint(wayCopy), findOrientation(wayCopy)).indexOf(point) + 1;
            if (indexOfPoint == 0) {
                return new HashMap<>();
            }
            wayCopy.add(indexOfPoint);
            Point newPoint = findNewPoint(getEndpoint(wayCopy));
            List<Point> start = findNextObstacle(newPoint, findOrientation(wayCopy));
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
            int indexOfPoint = findNextObstacle(getEndpoint(wayCopy), findOrientation(wayCopy)).indexOf(point) + 1;
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
     * gibt zurück, welches Feld aus der Liste ungefähr am nächsten am
     * Zielbereich ist. Bei zu viele Hindernissen wird geschätzt.
     * 
     * @param path,
     *            Felder zur Überprüfung
     * @param goals,
     *            zu erreichender Bereich
     * 
     *            @return, das nächstliegende Feld,
     */
    public Point getClosestToGoal(List<Point> path) {
        Point closest = null;
        int distance = Integer.MAX_VALUE;
        for (Point point : path) {
            Point newPoint = point;
            if (getConveyor().containsKey(point)) {
                newPoint = getConveyor().get(point).getPointAfterConveyor();
            }
            int currentDistance = getDistanceToGoal(newPoint);
            if (currentDistance < distance) {
                distance = currentDistance;
                closest = point;
            }
        }
        return closest;
    }

    /**
     * Schaut wie weit man vorwärts laufen kann, ohne in ein Loch oder eine Wand
     * zu laufen
     * 
     * @param start
     *            Startposition
     * @param orientation
     *            Richtung in die gelaufen werden soll
     * @return List mit Feldern die erreichbar sind
     */
    public List<Point> findNextObstacle(Point start, Orientation orientation) {
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
            position = getNeighbors(position, orientation);
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
     * Geht den Weg durch und kürzt ihn
     * 
     * @param way,
     *            der Weg
     * @return der gekürzte Weg
     */
    private List<Integer> checkWay(List<Integer> way) {
        int right = getHandCards("Rechtsdrehkarten").size();
        int left = getHandCards("Linksdrehkarten").size();
        int uTurn = getHandCards("Uturnkarten").size();
        int back = getHandCards("Rückwärtskarten").size();
        int forward1 = getHandCards("1vorwärtskarten").size();
        int forward2 = getHandCards("2vorwärtskarten").size();
        int forward3 = getHandCards("3vorwärtskarten").size();
        boolean stop;
        List<Integer> newWay = new ArrayList<>();
        for (int key : way) {
            switch (key) {
            case -1:
                stop = !checkLeft(left, right, uTurn, newWay);
                break;
            case -2:
                stop = !checkUTurn(left, right, uTurn, newWay);
                break;
            case -3:
                stop = !checkRight(left, right, uTurn, newWay);
                break;
            default:
                stop = !checkForward(key, forward1, forward2, forward3, back, newWay);
            }
            if (stop) {
                return newWay;
            }
        }
        if (newWay.size() > 4) {
            return newWay.subList(0, 5);
        }
        return newWay;
    }

    /**
     * Schaut ob genug Karten für eine Linksdrehung da sind
     * 
     * @param left
     *            linksdrehkarten
     * @param right
     *            rechtsdrehkarten
     * @param uTurn
     *            uturnkarten
     * @param newWay
     *            bisheriger weg
     */
    private boolean checkLeft(int left, int right, int uTurn, List<Integer> newWay) {
        if (left >= 1) {
            newWay.add(-1);
            return true;
        } else {
            if (getConveyor().containsKey(getEndpoint(newWay))) {
                return false;
            }
            if (uTurn >= 1 && right >= 1) {
                newWay.add(-2);
                newWay.add(-3);
                return true;
            } else {
                if (right >= 3) {
                    newWay.add(-3);
                    newWay.add(-3);
                    newWay.add(-3);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Schaut ob genug Karten für eine Rechtsdrehung da sind
     * 
     * @param left
     *            linksdrehkarten
     * @param right
     *            rechtsdrehkarten
     * @param uTurn
     *            uturnkarten
     * @param newWay
     *            bisheriger weg
     */
    private boolean checkRight(int left, int right, int uTurn, List<Integer> newWay) {
        if (right >= 1) {
            newWay.add(-3);
            return true;

        } else {
            if (getConveyor().containsKey(getEndpoint(newWay))) {
                return false;
            }
            if (uTurn >= 1 && left >= 1) {
                newWay.add(-2);
                newWay.add(-1);
                return true;

            } else {
                if (left >= 3) {
                    newWay.add(-1);
                    newWay.add(-1);
                    newWay.add(-1);
                    return true;

                }
            }
        }
        return false;
    }

    /**
     * Schaut ob genug Karten für eine UTurn da sind
     * 
     * @param left
     *            linksdrehkarten
     * @param right
     *            rechtsdrehkarten
     * @param uTurn
     *            uturnkarten
     * @param newWay
     *            bisheriger weg
     */
    private boolean checkUTurn(int left, int right, int uTurn, List<Integer> newWay) {
        if (uTurn >= 1) {
            newWay.add(-2);
            return true;

        } else {
            if (getConveyor().containsKey(getEndpoint(newWay))) {
                return false;
            }
            if (left >= 2) {
                newWay.add(-1);
                newWay.add(-1);
                return true;

            } else {
                if (right >= 2) {
                    newWay.add(-3);
                    newWay.add(-3);
                    return true;

                }
            }
        }
        return false;
    }

    /**
     * Schaut ob genug Karten für die Vorwärtsbewegung da sind
     * 
     * @param key
     *            Anzahl an schritten
     * @param forward1
     *            move1 Karten
     * @param forward2
     *            move2 Karten
     * @param forward3
     *            move3 Karten
     * @param back
     *            Rückwärts Karten
     * @param newWay
     *            bisheriger weg
     * @return {@code true} Wenn der Weg nur teilweise gegangen werden kann
     */
    private boolean checkForward(int key, int forward1, int forward2, int forward3, int back, List<Integer> newWay) {
        int forward3Copy = forward3;
        int forward2Copy = forward2;
        int forward1Copy = forward1;
        int backCopy = back;
        int steps = key;
        int dist = 0;
        int move;
        // forward3
        move = forwardCheckHelper(3, forward3Copy, steps);
        steps -= move * 3;
        forward3Copy -= move;
        dist += move * 3;
        // forward2
        move = forwardCheckHelper(2, forward2Copy, steps);
        steps -= move * 2;
        forward2Copy -= move;
        dist += move * 2;
        // forward1
        move = forwardCheckHelper(1, forward1Copy, steps);
        steps -= move;
        forward1Copy -= move;
        dist += move;
        // Back
        move = forwardCheckHelper(1, backCopy, steps);
        steps -= move;
        backCopy -= move;
        dist += move;

        if (steps > 0) {
            newWay.add(dist);
            return false;
        } else {
            newWay.add(key);
        }
        return true;
    }

    private int forwardCheckHelper(int move, int cards, int steps) {
        int move2 = move;
        int cards2 = cards;
        int dist = 0;
        while (steps > move2 - 1 && cards2 > 0) {
            move2 -= move;
            cards2 -= 1;
            dist++;
        }
        return dist;
    }
}
