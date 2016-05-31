/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki2.ai.planning;

import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uni_bremen.factroytrouble.ai.ki2.api.decision.unconsciousness.UnconsciousnessUnit;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;

public class Finder extends MemoryConnection {

    private Map<Point, Integer> distances;

    public Finder(UnconsciousnessUnit unconsciousness) {
        super(unconsciousness);
        distances = new HashMap<>();
    }

    /**
     * gibt den nächsten Punkt in einer Richtung zurück
     * 
     * @param pos,
     *            alter Punkt
     * @param orientation,
     *            Richtung
     * @return benachbarter Punkt
     */
    public Point getNeighbors(Point pos, Orientation orientation) {
        Point point = pos.getLocation();
        switch (orientation) {
        case NORTH:
            point.translate(0, 1);
            break;
        case EAST:
            point.translate(1, 0);
            break;
        case SOUTH:
            point.translate(0, -1);
            break;
        default:
            point.translate(-1, 0);
        }
        return point;
    }

    /**
     * gibt die Distanz eines Feldes zum Zielbereich, bei Berücksichtigung von
     * Wänden und Löchern, an.
     * 
     * @param point
     *            Startposition
     * @return die Distanz
     */
    public int getDistanceToGoal(Point point) {
        if (distances.isEmpty()) {
            distances = buildDistances(getFlagPos());
        }
        if (distances.containsKey(point)) {
            return distances.get(point);
        }
        return 999;
    }

    /**
     * Findet heraus, an welcher Position der Roboter in diesem Planungsschritt
     * steht
     * 
     * @param way
     *            der gewählte Weg, -1 = Rechts, -2 = U-turn, -3 = Links,-4 =
     *            Rückwärts positive = Vorwärtsbewegungen
     * @return die Position
     */
    public Point getEndpoint(List<Integer> way) {
        Point currentPos = getPos();
        Orientation currentOrient = getOrientation();
        for (int step : way) {
            currentPos = moveRobot(step, currentPos, currentOrient);
            if (getFlagPos().equals(currentPos)) {
                return getFlagPos();
            }
            if (getConveyor().containsKey(currentPos)) {
                currentPos = getConveyor().get(currentPos).getPointAfterConveyor();
            }
            if (getHoles().contains(currentPos)) {
                return new Point(-99, -99);
            }
            currentOrient = updateOrientation(currentOrient, currentPos, step);

        }
        return currentPos;
    }

    /**
     * Findet heraus, in welche Richtung der Roboter in diesem Planungsschritt
     * schaut
     * 
     * @param way,
     *            der gewählte Weg, -1 = Rechts, -2 = U-turn, -3 = Links,
     *            positive = Vorwärtsbewegungen
     * @return die Richtung
     */
    public Orientation findOrientation(List<Integer> way) {
        Point currentPos = getPos();
        Orientation currentOrient = getOrientation();
        for (int step : way) {
            currentPos = moveRobot(step, currentPos, currentOrient);
            if (getConveyor().containsKey(currentPos)) {
                currentPos = getConveyor().get(currentPos).getPointAfterConveyor();
            }
            currentOrient = updateOrientation(currentOrient, currentPos, step);
        }
        return currentOrient;
    }

    /**
     * Schaut was mit der Orientierung passiert wenn man den übergebenen Schritt
     * macht
     * 
     * @param oldOrient,
     *            momentane Orientierung
     * @param currentPoint,
     *            momentane Position
     * @param step,
     *            Schritt
     * @return neue Orientierung
     */
    public Orientation updateOrientation(Orientation oldOrient, Point currentPoint, int step) {
        Orientation newOrient = oldOrient;
        switch (step) {
        case -1:
            newOrient = Orientation.getNextDirection((Orientation) newOrient, true);
            break;
        case -2:
            newOrient = Orientation.getOppositeDirection((Orientation) newOrient);
            break;
        case -3:
            newOrient = Orientation.getNextDirection((Orientation) newOrient, false);
            break;
        default:
            break;
        }
        newOrient = turnWithBoardElements(currentPoint, newOrient);
        return newOrient;
    }

    /**
     * Schaut ob Boardelemente die Orientierung ändern
     * 
     * @param oldOrient,
     *            momentane Orientierung
     * @param currentPoint,
     *            momentane Position
     * @return neue Orientierung
     */
    public Orientation turnWithBoardElements(Point currentPoint, Orientation oldOrient) {
        Orientation newOrient = oldOrient;
        if (getLeftGears().contains(currentPoint)) {
            newOrient = Orientation.getNextDirection((Orientation) newOrient, true);
            return newOrient;
        }
        if (getRightGears().contains(currentPoint)) {
            newOrient = Orientation.getNextDirection((Orientation) newOrient, false);
            return newOrient;
        }
        if (getConveyor().containsKey(currentPoint)) {
            int dir = getConveyor().get(currentPoint).getDirection();
            if (dir == 1) {
                // links
                newOrient = Orientation.getNextDirection((Orientation) newOrient, true);
            }
            if (dir == 2) {
                // rechts
                newOrient = Orientation.getNextDirection((Orientation) newOrient, false);
            }
        }
        return newOrient;
    }

    /**
     * Schaut ob man ein Feld in eine übergebene Richtung gehen kann
     * 
     * @param start,
     *            Startposition
     * @param orientation,
     *            Richtung
     * @return {@code True} Wenn Kein Hindernis da ist
     */
    public boolean noObstacleInWay(Point start, Orientation orientation) {
        if (getWalls(orientation).contains(start)
                || getWalls(Orientation.getOppositeDirection(orientation)).contains(getNeighbors(start, orientation))
                || getHoles().contains(getNeighbors(start, orientation))) {
            return false;
        }
        return true;
    }

    /**
     * Schaut ob man ein Feld in eine übergebene Richtung gehen kann ohne eine
     * Wand zu treffen
     * 
     * @param start,
     *            Startposition
     * @param orientation,
     *            Richtung
     * @return {@code True} Wenn Keine Wand da ist
     */
    public boolean noWallInWay(Point start, Orientation orientation) {
        if (getWalls(orientation).contains(start)
                || getWalls(Orientation.getOppositeDirection(orientation)).contains(getNeighbors(start, orientation))) {
            return false;
        }
        return true;
    }

    /**
     * Schaut wie weit alles andere von einen bestimmten Punkt entfernt ist
     * 
     * @param point,
     *            der Ausgangspunkt
     * @return alle anderen Punkte mit Entfernung
     */
    public Map<Point, Integer> buildDistances(Point point) {
        Point currentPoint = point;
        HashMap<Point, Integer> queue = new HashMap<>();
        queue.put(point, 0);
        HashMap<Point, Integer> ready = new HashMap<>();
        while (!queue.isEmpty()) {
            // Finde den nähesten Punkt
            int distance = Integer.MAX_VALUE;
            for (Entry<Point, Integer> dist : queue.entrySet()) {
                if (dist.getValue() < distance) {
                    distance = dist.getValue();
                    currentPoint = dist.getKey();
                }
            }
            // Füge für den Punkt neue Punkte ein
            addToQueue(currentPoint, ready, queue);
            // Entferne den bearbeiteten Punkt
            ready.put(currentPoint, queue.get(currentPoint));
            queue.remove(currentPoint);
        }
        return ready;
    }

    private void addToQueue(Point currentPoint, HashMap<Point, Integer> ready, HashMap<Point, Integer> queue) {
        for (Orientation orienta : Orientation.values()) {
            if (noObstacleInWay(currentPoint, orienta)) {
                Point newPoint = getNeighbors(currentPoint, orienta);
                if (!ready.containsKey(newPoint)) {
                    queue.put(newPoint, queue.get(currentPoint) + 1);
                }
            }
        }
    }

    private Point moveRobot(int step, Point currentPos, Orientation currentOrient) {
        Point newPos = currentPos;
        if (step == -4 && noWallInWay(newPos, Orientation.getOppositeDirection(currentOrient))) {
            newPos = getNeighbors(newPos, Orientation.getOppositeDirection(currentOrient));
        }
        if (step > 0) {
            for (int b = step; b > 0; b--) {
                if (noWallInWay(newPos, currentOrient)) {
                    newPos = getNeighbors(newPos, currentOrient);
                }
            }
        }
        return newPos;
    }

}
