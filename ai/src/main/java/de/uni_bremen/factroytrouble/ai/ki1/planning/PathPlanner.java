/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki1.planning;

import java.awt.Point;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import de.uni_bremen.factroytrouble.ai.ais.AIPlayer1;
import de.uni_bremen.factroytrouble.ai.ki1.configreader.AgentConfigReader;
import de.uni_bremen.factroytrouble.ai.ki1.configreader.ConfigReader;
import de.uni_bremen.factroytrouble.ai.ki1.planning.TacticUnit.Tactics;
import de.uni_bremen.factroytrouble.api.ki1.Control;
import de.uni_bremen.factroytrouble.api.ki1.Control.RequestType;
import de.uni_bremen.factroytrouble.api.ki1.Logic;
import de.uni_bremen.factroytrouble.api.ki1.Placement;
import de.uni_bremen.factroytrouble.api.ki1.Timer;
import de.uni_bremen.factroytrouble.api.ki1.planning.CurrentPlanning;
import de.uni_bremen.factroytrouble.api.ki1.planning.FuturePlanning;
import de.uni_bremen.factroytrouble.api.ki1.planning.PathPlanning;
import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.exceptions.ConfigPropertyNotFoundRuntimeException;
import de.uni_bremen.factroytrouble.exceptions.KeyNotFoundException;
import de.uni_bremen.factroytrouble.gameobjects.Hole;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.gameobjects.Wall;

/**
 * Future: ggf Heuristik um turns und straightforward erweitern Berechnet mit A*
 * einen kürzesten Weg von einem Start{@link Tile} bis zu einem Ziel. Nutzt eine
 * Distanz als Heuristik und {@link PathNode}s zur Repräsentation der einzelnen
 * Felder.
 * 
 * @author Roland
 *
 */
public class PathPlanner implements PathPlanning {
    private Timer timerUnit;
    private Control controller;
    private CurrentPlanning cpo;
    private Logic logic;
    private PriorityQueue<PathNode> fringe;
    private Set<PathNode> visitedNodes;

    public PathPlanner(Timer timerUnit, Control controller, FuturePlanning futurePlanner, Logic logic) {
        this.timerUnit = timerUnit;
        this.controller = controller;
        this.logic = logic;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.uni_bremen.factroytrouble.ai.planning.PathPlanning#getPath(de.uni_bremen.
     * factroytrouble.board.Tile, de.uni_bremen.factroytrouble.board.Tile)
     */
    @Override
    public List<Tile> getPath(Tile start, Tile target) {
        List<Point> emptyList = new LinkedList<Point>();
        return calcPath(start, target, emptyList);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.uni_bremen.factroytrouble.ai.planning.PathPlanning#getAlternativePath(de.
     * uni_bremen.factroytrouble.board.Tile, de.uni_bremen.factroytrouble.board.Tile,
     * java.util.List)
     */
    @Override
    public List<Tile> getAlternativePath(Tile start, Tile target, List<Point> blockedPoints) {
        return calcPath(start, target, blockedPoints);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uni_bremen.factroytrouble.ai.planning.PathPlanning#isWallInWay(int,
     * de.uni_bremen.factroytrouble.board.Tile, de.uni_bremen.factroytrouble.board.Tile)
     */
    @Override
    public boolean isWallInWay(int direction, Tile startTile, Tile targetTile) {
        List<Wall> startWalls = startTile.getWalls();
        List<Wall> targetWalls = targetTile.getWalls();
        // prüft jeweils die Felder auf eine behindernde Wand
        if (isWallInDir(direction, startWalls) || isWallInDir(changeDir(direction), targetWalls)) {
            return true;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.uni_bremen.factroytrouble.ai.planning.PathPlanning#getTile(java.awt.Point)
     */
    @Override
    public Tile getTile(Point coordinates) {
        Object potTile = controller.requestData(null, coordinates);
        if (!(potTile instanceof Tile)) {
            return null;
        }
        return (Tile) potTile;
    }

    @Override
    public void setController(Control control) {
        this.controller = control;

    }

    public void setCurrentPlanner(CurrentPlanning cpo) {
        this.cpo = cpo;
    }

    /**
     * Berechnet einen Weg mit geringsten Kosten von Start zu Ziel. Nutz dazu
     * eine Heuristik, hier die Entfernung in Tiles ohne Berücksichtigung von
     * Hindernissen oder Drehungen.
     * 
     * @param start
     *            StartFeld
     * @param target
     *            ZielFeld
     * @param blockedPoints
     *            Felder, die nicht benutzt werden sollen.
     * @return der gefundene Weg als Liste von Feldern
     */
    private List<Tile> calcPath(Tile start, Tile target, List<Point> blockedPoints) {
        // bekannte, aber noch nicht ausgewertete Felder
        fringe = new PriorityQueue<PathNode>();
        // besuchte und ausgewertete Felder (entsprchen nicht dem bisherigen
        // Weg!)
        visitedNodes = new HashSet<PathNode>();
        // definiere Start-,End- und alle anderen Knoten
        PathNode startNode = new PathNode(start, cpo, logic);
        Map<Point, PathNode> graph = generateGraph(startNode);
        startNode.setAccumCost(0);
        startNode.setOverallCost(0);
        PathNode endNode = graph.get(target.getAbsoluteCoordinates());

        PathNode currentNode = null;
        // starte mit dem Startknoten als bekannten Knoten und iteriere über
        // diese, bis es keinen mehr gibt (oder das Ziel
        // erreicht wurde
        fringe.add(startNode);
        while (!fringe.isEmpty()) {
            // entnehme bekannten Knoten mit geringsten Kosten, falls Zielknoten
            // gefunden, generiere Pfad
            currentNode = fringe.poll();
            if (currentNode.equals(endNode)) {
                return pathAsTileList(startNode, currentNode);
            }
            // markiere Knoten als expandiert und expandiere ihn
            visitedNodes.add(currentNode);
            expandNode(currentNode, endNode, blockedPoints);

        }
        return new LinkedList<Tile>();
    }

    /**
     * Expandiert einen bekannten Knoten. Dabei werden alle Nachbarknoten
     * evaluiert (begehbar, bereits besucht, bereits kürzerer Weg bekannt) Ist
     * ein Knoten neu oder ein kürzerer Weg gefunden, wird der Gesamtweg mit dem
     * Knoten geschätzt und der Knoten mit diesem und einer Referenz auf den
     * Vorgänger in die Liste der bekannten Knoten eingereiht.
     * 
     * @param currentNode
     *            der Knoten, der expandiert wird
     * @param endNode
     *            der Zielknoten
     * @param blockedPoints
     *            Punkte aus einem vorherigen Weg, die nicht mehr abgelaufen
     *            werden sollen
     */
    private void expandNode(PathNode currentNode, PathNode endNode, List<Point> blockedPoints) {
        PathNode neighbour;
        List<PathNode> neighbours = currentNode.getNeighbours();
        // iteriert über die Liste der Nachbarknoten
        for (int i = 0; i < neighbours.size(); i++) {
            neighbour = neighbours.get(i);
            // falls Nachbarknoten nicht begehbar oder bereits besucht, breche
            // iteration ab
            if (neighbour == null
                    || (neighbour.getTile().getFieldObject() != null
                            && neighbour.getTile().getFieldObject() instanceof Hole)
                    || isWallInWay(i, currentNode.getTile(), neighbour.getTile()) || visitedNodes.contains(neighbour)) {
                continue;
            }
            // berechne die bisherigen Pfadkosten über den expandierten Knoten
            // bis zum Nachbarknoten
            int newG = currentNode.getAccumCost() + neighbour.getTileCost(controller);
            // prüfe, ob Nachbarknoten bereits bekannt, falls ja und bisheriger
            // Weg kürzer, breche iteration ab
            boolean contains = fringe.contains(neighbour);
            if (contains && newG >= neighbour.getAccumCost()) {
                continue;
            }
            // wenn Nachbarknoten nicht bekannt, oder kürzerer Weg, setze neuen
            // Vorgänger, bisherige Pfadkosten und berechne
            // und setze geschätze Gesamtkosten
            neighbour.setPreNode(currentNode);
            // falls der Knoten nicht mehr abgelaufen werden soll(zwecks
            // alternativen Pfad), erhöhe kosten drastisch
            if (nodeIsBlocked(neighbour, blockedPoints)) {
                newG = 10000;
            }
            neighbour.setAccumCost(newG);
            int overallCost = newG + getExpectedCost(neighbour, endNode);

            int robinsonPower = (int) controller.requestData(RequestType.TACTICPOWER, null);
            Tactics currentTactic = (Tactics) controller.requestData(RequestType.TACTIC, null);
            if (currentTactic == Tactics.ROBINSONCRUSO && robinsonPower != 0) {
                overallCost += calcRobinsonCost(robinsonPower, neighbour);
            }
            neighbour.setOverallCost(overallCost);
            // wenn bereits bekannt, entnehme Knoten, füge ihn auf jeden Fall
            // mit neu berechneten Werten ein
            if (contains) {
                fringe.remove(neighbour);
            }
            fringe.add(neighbour);
        }
    }

    /**
     * Berechnet Zusatzkosten für ein Feld, falls ein Gegnerischer Spieler in
     * der nähe ist.
     * 
     * @param maxDist
     *            maximale Distanz auf die die Kosten erhöht werden sollen
     * @param repelFactor
     *            Faktor der die Kraft des abstoßens definiert
     * @param node
     *            Knoten für dessen Feld die Kosten berechnet werden sollen
     * @return Zusatzkosten
     */
    private int calcRobinsonCost(int robinsonPower, PathNode node) {
        int maxDist = 1 + robinsonPower / 10;
        float repelFactor = 1 + robinsonPower / 10; // TODO evaluate function
        int dist = Integer.MAX_VALUE;
        Map<Integer, Placement> players = controller.getAllPlayerPlacement();
        for (Map.Entry<Integer, Placement> entry : players.entrySet()) {
            Point otherPos = ((Placement) entry).getPosition();
            Point ownPos = node.getTile().getAbsoluteCoordinates();
            if (Math.abs(otherPos.x - ownPos.x) < dist) {
                dist = Math.abs(otherPos.x - ownPos.x);
            } else if (Math.abs(otherPos.y - ownPos.y) < dist) {
                dist = Math.abs(otherPos.y - ownPos.y);
            }
        }
        return (int) Math.exp(Math.max(0, maxDist - dist) * repelFactor) - 1;// TODO
                                                                             // evaluate
                                                                             // function
    }

    /**
     * Ermittelt, ob der aktuelle Knoten ein geblockter Knoten ist.
     * 
     * @param node
     *            der aktuelle Knoten
     * @param blockedPoints
     *            die geblockten Knoten
     * @return geblockt, oder nicht
     */
    private boolean nodeIsBlocked(PathNode node, List<Point> blockedPoints) {
        Point ownPoint = node.getTile().getAbsoluteCoordinates();
        for (Point coords : blockedPoints) {
            if (ownPoint.equals(coords)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Berechnet die geschätzten Kosten des noch verbleibenden Weges bis zum
     * Ziel.
     * 
     * @param start
     *            Startknoten
     * @param end
     *            Endknoten
     * @return geschätzte verbleibende Kosten
     */
    private int getExpectedCost(PathNode start, PathNode end) {
        int defaultTileCost = 10;
        try {
            ConfigReader config = AgentConfigReader.getInstance(((AIPlayer1) controller).getAgentNumber());
            defaultTileCost = config.getIntProperty("Planning.DefaultTileCost");
        } catch (IOException | KeyNotFoundException e) {
            throw new ConfigPropertyNotFoundRuntimeException(e);
        }
        Tile startTile = start.getTile();
        Tile endTile = end.getTile();
        int currentX = startTile.getAbsoluteCoordinates().x;
        int currentY = startTile.getAbsoluteCoordinates().y;
        int targetX = endTile.getAbsoluteCoordinates().x;
        int targetY = endTile.getAbsoluteCoordinates().y;

        int tileDist = Math.abs(targetX - currentX);
        tileDist += Math.abs(targetY - currentY);
        return tileDist * defaultTileCost;
    }

    /**
     * Prüft, ob eine {@link Wall} auf einem {@link Tile} in Gehrichtung im Weg
     * steht.
     * 
     * @param direction
     *            die Gehrichtung
     * @param walls
     *            Liste der Wände auf dem {@link Tile}
     * @return Im Weg oder nicht
     */
    private boolean isWallInDir(int direction, List<Wall> walls) {
        if (walls != null && !walls.isEmpty()) {
            for (Wall wall : walls) {
                if (direction == 0 && wall.getOrientation() == Orientation.WEST) {
                    return true;
                } else if (direction == 1 && wall.getOrientation() == Orientation.NORTH) {
                    return true;
                } else if (direction == 2 && wall.getOrientation() == Orientation.EAST) {
                    return true;
                } else if (direction == 3 && wall.getOrientation() == Orientation.SOUTH) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Berechnet die Richtung einer störenden {@link Wall} auf einem Zielfeld,
     * aus Sicht eines angrenzenden Startfeldes.
     * 
     * @param direction
     *            Gehrichtung auf dem Startfeld
     * @return
     */
    private int changeDir(int direction) {
        if (direction == 0 || direction == 1) {
            return direction + 2;
        }
        return direction - 2;
    }

    /**
     * Erstellt eine Map mit Knoten aller Kacheln auf dem Spielfeld und setzt
     * alle Nachbarschaftsbeziehungen.
     * 
     * @param node
     *            Startknoten
     */
    private Map<Point, PathNode> generateGraph(PathNode start) {
        Map<Point, PathNode> graph = new HashMap<Point, PathNode>();
        Queue<PathNode> nodesToProcess = new LinkedList<PathNode>();
        graph.put(start.getTile().getAbsoluteCoordinates(), start);
        nodesToProcess.add(start);
        // iteriert über alle gefundenen Knoten, bis alle abgearbeitet sind
        while (!nodesToProcess.isEmpty()) {
            PathNode currentNode = nodesToProcess.poll();
            Point ownPoint = currentNode.getTile().getAbsoluteCoordinates();
            PathNode neighbourNode;
            Point neighbourPoint;
            // prüfe alle 4 möglichen Nachbarn
            for (int i = 0; i < 4; i++) {
                switch (i) {
                case 0:
                    neighbourPoint = new Point(ownPoint.x - 1, ownPoint.y);
                    break;
                case 1:
                    neighbourPoint = new Point(ownPoint.x, ownPoint.y + 1);
                    break;
                case 2:
                    neighbourPoint = new Point(ownPoint.x + 1, ownPoint.y);
                    break;
                default:
                    neighbourPoint = new Point(ownPoint.x, ownPoint.y - 1);
                    break;
                }
                // wenn neuer Knoten
                if (!graph.containsKey(neighbourPoint)) {
                    Tile neighbourTile = getTile(neighbourPoint);
                    if (neighbourTile == null) {
                        currentNode.setNeighbour(null);
                    } else {
                        // setzt bidirektional die Nachbarknotenreferenz
                        neighbourNode = new PathNode(neighbourTile, cpo, logic);
                        currentNode.setNeighbour(neighbourNode);
                        neighbourNode.setNeighbour(currentNode);
                        graph.put(neighbourPoint, neighbourNode);
                        nodesToProcess.add(neighbourNode);
                    }

                } else {
                    // setzt unidirektional noch fehlende
                    // Nachbarschaftsbeziehung
                    PathNode knownNode = graph.get(neighbourPoint);
                    if (!currentNode.getNeighbours().contains(knownNode)) {
                        currentNode.setNeighbour(knownNode);
                    }
                    if (!knownNode.getNeighbours().contains(currentNode)) {
                        knownNode.setNeighbour(currentNode);
                    }
                }

            }

        }
        return graph;
    }

    /**
     * Gibt den gefunden Weg von Start zum Ziel als Liste von {@link Tile}s
     * zurück. Dazu wird vom Zielknoten aus iterativ der Vorgänger ausgelesen.
     * 
     * @param startNode
     *            Startknoten
     * @param endNode
     *            Ziellnoten
     * @return der Weg
     */
    private List<Tile> pathAsTileList(PathNode startNode, PathNode endNode) {
        List<Tile> path = new LinkedList<Tile>();
        PathNode currentNode = endNode;
        while (!currentNode.equals(startNode)) {
            ((LinkedList<Tile>) path).addFirst(currentNode.getTile());
            currentNode = currentNode.getPreNode();
        }
        return path;
    }
}
