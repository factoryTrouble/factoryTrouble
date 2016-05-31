/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki1.planning;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import de.uni_bremen.factroytrouble.ai.ais.AIPlayer1;
import de.uni_bremen.factroytrouble.ai.ki1.configreader.AgentConfigReader;
import de.uni_bremen.factroytrouble.ai.ki1.configreader.StaticBehaviourConfigReader;
import de.uni_bremen.factroytrouble.api.ki1.Control;
import de.uni_bremen.factroytrouble.api.ki1.Logic;
import de.uni_bremen.factroytrouble.api.ki1.Control.RequestType;
import de.uni_bremen.factroytrouble.api.ki1.planning.CurrentPlanning;
import de.uni_bremen.factroytrouble.api.ki1.planning.PathPlanningNode;
import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.exceptions.KeyNotFoundException;
import de.uni_bremen.factroytrouble.gameobjects.ConveyorBelt;
import de.uni_bremen.factroytrouble.gameobjects.Gear;

/**
 * Repräsentiert einen Knoten im Graph des {@link PathPlanner}s. Hat bis zu 4
 * Nachbarn, enthält ein {@link Tile} des Spielbretts, die Kosten eines
 * bisherigen Weges und die geschätzen Gesamtkosten von einem Start- zu einem
 * Zielpunkt. Sortierbar nach den Gesamtkosten.
 * 
 * @author Roland
 *
 */
public class PathNode implements Comparable<PathNode>, PathPlanningNode {
    private static final Logger LOGGER = Logger.getLogger(PathNode.class);
    private int accumCost;
    private List<PathNode> neighbours = new LinkedList<PathNode>();
    private Tile tile;
    private int pre;
    private int overallCost;
    private AgentConfigReader config;
    private StaticBehaviourConfigReader bConfig;
    private CurrentPlanning cpo;
    private Logic logic;

    public PathNode(Tile tile, CurrentPlanning cpo, Logic logic) {
        this.tile = tile;
        this.cpo = cpo;
        this.logic = logic;
    }

    @Override
    public int compareTo(PathNode other) {
        return Integer.compare(this.getOverallCost(), other.getOverallCost());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((tile == null) ? 0 : tile.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PathNode other = (PathNode) obj;
        if (tile == null) {
            if (other.tile != null)
                return false;
        } else if (!tile.getAbsoluteCoordinates().equals(other.tile.getAbsoluteCoordinates()))
            return false;
        return true;
    }

    /**
     * Gibt die Kosten des Tiles zurück, zur Berechnung der Kosten werden
     * Feldobjekte und einige Spielerabhängige Variablen heran gezogen.
     * 
     * @param control
     * @return Kosten des Tiles
     */
    public int getTileCost(Control control) {
        int gearCost;
        double risk;
        int laserCostFactor;
        int defaultTileCost;
        try {
            config = AgentConfigReader.getInstance(((AIPlayer1) control).getAgentNumber());
            bConfig = StaticBehaviourConfigReader.getInstance(((AIPlayer1) control).getRobotName());
            risk = bConfig.getDoubleProperty("Planning.Risk");
            laserCostFactor = bConfig.getIntProperty("Planning.LaserCostFactor");
            gearCost = bConfig.getIntProperty("Planning.GearCost");
            defaultTileCost = config.getIntProperty("Planning.DefaultTileCost");
        } catch (IOException | KeyNotFoundException e) {
            throw new RuntimeException(e);
        }
        int cost;
        // Berechnet die Kosten durch Laser
        cost = calcLaserCostOfTile(control, defaultTileCost, laserCostFactor, risk);
        if (this.getTile().getFieldObject() != null && tile.getFieldObject() instanceof Gear) {
            // addiert Kosten eines Gears
            cost += gearCost;
        } else if (this.getTile().getFieldObject() != null && tile.getFieldObject() instanceof ConveyorBelt) {
            // addiert kosten eines conveyorbelts
            cost += calcCBCost(control, defaultTileCost, risk);
        }

        return cost;
    }

    /**
     * Berechnet den Conveyorbeltkostenanteil eiens Feldes, setzt voraus, dass
     * das Tile dieses pathnodes ein conveyorbelt enthält.
     * 
     * @param control
     *            controlUnit
     * @param defaultTileCost
     *            Standardkosten
     * @param risk
     *            Risikoparamater
     * @return CBkostenanteil
     */
    public int calcCBCost(Control control, int defaultTileCost, double risk) {
        int cost = defaultTileCost;
        Tile currentTile = (Tile) control.requestData(RequestType.CURRENTTILE, null);
        Tile goalTile = cpo.getGoalTile();
        if (goalTile != null) {
            // relative Pos von aktuellem zu Zieltile
            int relPos = cpo.getRelPosStraight(currentTile, goalTile);
            // wenn nicht in gerader Linie, berechne Richtung mit größter
            // Differenz
            if (relPos == -1) {
                int diffX = (int) (goalTile.getAbsoluteCoordinates().getX()
                        - currentTile.getAbsoluteCoordinates().getX());
                int diffY = (int) (goalTile.getAbsoluteCoordinates().getY()
                        - currentTile.getAbsoluteCoordinates().getY());
                boolean mainAxisIsX = Math.abs(diffX) >= Math.abs(diffY) ? true : false;
                if (mainAxisIsX && diffX >= 0) {
                    relPos = 2;
                } else if (mainAxisIsX && diffX < 0) {
                    relPos = 0;
                } else if (!mainAxisIsX && diffY >= 0) {
                    relPos = 1;
                } else {
                    relPos = 3;
                }
            }
            int CBOri = logic.oriAsInt(((ConveyorBelt) this.getTile().getFieldObject()).getOrientation());
            // wenn cb in richtige Richtung zeigt, werden die Kosten günstiger,
            // wenn in Gegenrichtung sehr teuer, sonst etwas teurer.
            if (CBOri == relPos) {
                return (int) (cost * getCBCostFactor(1, control));
            } else if (CBOri == (relPos + 2) % 4) {
                return (int) (cost * getCBCostFactor(-1, control));
            } else {
                return (int) (cost * getCBCostFactor(0, control));
            }
        }
        return defaultTileCost;
    }

    /**
     * Berechnet den Laserkostenanteil eines Feldes.
     * 
     * @param control
     *            Controlunit
     * @param defaultTileCost
     *            Standardkosten eines Tiles
     * @param laserCostFactor
     *            Kostenfaktor pro Laser
     * @param risk
     *            Risikobereitschaft des Spielers
     * @return Laserkosten des Tiles
     */
    public int calcLaserCostOfTile(Control control, int defaultTileCost, int laserCostFactor, double risk) {
        int hp = (int) control.requestData(RequestType.HP, null);
        int laserCount = countLasers(this.tile);
        // wenn Roboter nach Lasern tot wäre, setze Tilekosten extrem hoch
        if ((hp - laserCount) <= 0) {
            return 1000;
        }
        // wenn kein Laser das Feld trifft, keine Zusatzkosten
        else if (laserCount == 0) {
            return defaultTileCost;
        }
        // Kosten steigen exponentiell mit abnahme der nach Laseranwendung
        // verbleibenden HP und
        // sind zusätzlich abhängig von der Risikobereitschaft des Spielers, ein
        // kleiner Riskwert bedeutet dabei eine höhere Riskikobereitschaft
        return (int) (defaultTileCost + 1 / (hp - laserCount) * laserCostFactor * (1 - risk));
    }

    /**
     * Zählt die Anzahl der Laser die ein Tile Treffen
     * 
     * @param control
     *            Controlunit
     * @param tile
     *            das Tile
     * @return Anzahl der Laser
     */
    public int countLasers(Tile tile) {
        int count = 0;
        // Nachbarknoten
        PathNode northNode;
        PathNode eastNode;
        PathNode southNode;
        PathNode westNode;
        // zähle in jede Richtung die Laser
        for (PathNode node : neighbours) {
            if (node != null) {
                int relPos = ((CurrentPlannerOne) cpo).getRelPosStraight(this.tile, node.getTile());
                switch (relPos) {
                case 0:
                    westNode = node;
                    count += countLaserInDir(this.tile, westNode, 0);
                    break;
                case 1:
                    northNode = node;
                    count += countLaserInDir(this.tile, northNode, 1);
                    break;
                case 2:
                    eastNode = node;
                    count += countLaserInDir(this.tile, eastNode, 2);
                    break;
                default:
                    southNode = node;
                    count += countLaserInDir(this.tile, southNode, 3);
                }
            }
        }
        return count;
    }

    /**
     * markiert einen der Nachbarknoten als Vorgänger in einem Pfad.
     * 
     * @param preNode
     *            Vorgängerknoten
     */
    public void setPreNode(PathNode preNode) {
        PathNode neighbourNode;
        for (int i = 0; i < neighbours.size(); i++) {
            neighbourNode = neighbours.get(i);
            if (neighbourNode != null && neighbourNode.equals(preNode)) {
                this.pre = i;
            }
        }
    }

    public int getOverallCost() {
        return overallCost;
    }

    public void setOverallCost(int overallCost) {
        this.overallCost = overallCost;
    }

    public PathNode getNeighbour(int i) {
        return neighbours.get(i);
    }

    public List<PathNode> getNeighbours() {
        return neighbours;
    }

    public void setNeighbour(PathNode node) {
        neighbours.add(node);
    }

    public PathNode getPreNode() {
        return getNeighbour(pre);
    }

    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public int getAccumCost() {
        return accumCost;
    }

    public void setAccumCost(int costToTarget) {
        this.accumCost = costToTarget;
    }

    /**
     * Holt die richtige Kostenvariable aus dem Confighandler in abhängigkeit
     * einer Variable.
     * 
     * @param caseNum
     *            Variable für die zu holende Kostenvariable. 1:good, -1:bad,
     *            0:neutral
     * @return Kostenfaktor
     */
    private double getCBCostFactor(int caseNum, Control control) {
        try {
            bConfig = StaticBehaviourConfigReader.getInstance(((AIPlayer1) control).getRobotName());
            switch (caseNum) {
            case 1:
                return bConfig.getDoubleProperty("Planning.GoodCBCostFactor");
            case -1:
                return bConfig.getDoubleProperty("Planning.BadCBCostFactor");
            case 0:
                return bConfig.getDoubleProperty("Planning.NeutralCBCostFactor");
            default:
                return 0;
            }
        } catch (KeyNotFoundException e) {
            LOGGER.error(
                    "Einer der drei Keys Planning.GoodCBCostFactor, Planning.BadCBCostFactor oder Planning.NeutralCBCostFactor konnte nicht gefunden werden",
                    e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gibt den Nachbarknoten in eine gegebene Richtung zurück.
     * 
     * @param startNode
     *            Startknoten
     * @param dir
     *            Richtung in der der Nachbar gesucht weren soll
     * @return der Nachbar
     */
    private PathNode getNeighbourNodeInDir(PathNode startNode, int dir) {
        for (PathNode node : startNode.getNeighbours()) {
            if (node != null
                    && ((CurrentPlannerOne) cpo).getRelPosStraight(startNode.getTile(), node.getTile()) == dir) {
                return node;
            }
        }
        return null;
    }

    /**
     * Berechnet die Anzahl der Laser die ein Tile aus einer gegebene Richtung
     * treffen,
     * 
     * @param startTile
     *            Starttile
     * @param nextNode
     *            Nachbarknoten in gegebene Richtung
     * @param dir
     *            gegebene Richtung
     * @return Anzahl der Laser
     */
    private int countLaserInDir(Tile startTile, PathNode nextNode, int dir) {
        // wenn Wand mit Laser auf Starttile in gegebene Richtung
        int laserCount = laserOnTileInDir(startTile, dir);
        if (laserCount > 0) {
            return laserCount;
        } // annahme: laser schieß nicht über loch, wenn Nachbartile Rand/Loch
          // oder Wand/Spieler im Weg
        else if (nextNode == null
                || !((CurrentPlannerOne) cpo).checkForMoveabilityOnBoard(startTile, nextNode.getTile(), dir, true)
                || nextNode.getTile().getRobot() != null) {
            return 0;
        } else {// berechne Rekrusiv über die Nachbarn die Laseranzahl
            return countLaserInDir(nextNode.getTile(), getNeighbourNodeInDir(nextNode, dir), dir);
        }
    }

    /**
     * Prüft, ob auf einem Tile in einer bestimmten Richtung ein oder mehrere
     * Laser hat und gibt die Anzahl zurück.
     * 
     * @param tile
     *            das Tile
     * @param dir
     *            die Richtung
     * @return Anzahl der Laser an der Wand
     */
    private int laserOnTileInDir(Tile tile, int dir) {
        if (tile.hasWall(logic.oriAsOri(dir))) {
            return tile.getWall(logic.oriAsOri(dir)).hasLaser();
        }
        return 0;
    }
}
