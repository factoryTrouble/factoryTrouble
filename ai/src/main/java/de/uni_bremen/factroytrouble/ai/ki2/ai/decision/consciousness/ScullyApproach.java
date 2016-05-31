/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki2.ai.decision.consciousness;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import de.uni_bremen.factroytrouble.ai.ais.AIPlayer2;
import de.uni_bremen.factroytrouble.ai.ki2.api.decision.consciousness.Approach;
import de.uni_bremen.factroytrouble.ai.ki2.api.decision.consciousness.ConsciousnessUnit;
import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.gameobjects.ConveyorBelt;
import de.uni_bremen.factroytrouble.gameobjects.FieldObject;
import de.uni_bremen.factroytrouble.gameobjects.Hole;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.player.GameTurnLeftCard;
import de.uni_bremen.factroytrouble.player.GameTurnRightCard;
import de.uni_bremen.factroytrouble.player.ProgramCard;

public class ScullyApproach implements Approach {

    private List<ProgramCard> cards;
    private List<Tile> tiles;
    private Tile position;
    private Orientation orientation;
    private Robot me;
    private ConsciousnessUnit con;

    /**
     * Erstellt einen Approch mit den gegebenen Karten um diesen bewerten zu
     * können.
     * 
     * @param cards
     *            Die Karten die gelegt werden sollen
     * @param aiPlayer2
     *            Instanz von AIPlayer2
     */
    public ScullyApproach(List<ProgramCard> cards, AIPlayer2 aiPlayer2, ConsciousnessUnit con) {
        this.cards = cards;
        orientation = aiPlayer2.getRobot().getOrientation();
        position = aiPlayer2.getRobot().getCurrentTile();
        me = aiPlayer2.getRobot();
        convertCardsToTile();
        this.con = con;
    }

    @Override
    public int getAmountOfNoticedTiles() {
        return tiles.size();
    }

    @Override
    public int getAmountOfNearEnemies() {
        int count = 0;
        Tile tmp = position;
        Orientation tmpOr = orientation;
        for (ProgramCard card : cards) {
            count += countSurroundingRobots(tmp);
            // Nächstes Feld simulieren
            if (getNextTile(tmpOr, tmp, card) == null) {
                break;
            } else {
                tmp = getNextTile(tmpOr, tmp, card);
                if (card instanceof GameTurnLeftCard) {
                    tmpOr = Orientation.getNextDirection(tmpOr, true);
                }
                if (card instanceof GameTurnRightCard) {
                    tmpOr = Orientation.getNextDirection(tmpOr, false);
                }
            }
        }
        return count + roboterAimingPath();
    }

    @Override
    public int getExpectedDamage() {
        if (cards.size() >= tiles.size() - 1) { // Wenn über den Rand läuft
            return 9000;
        }
        return laserAimingPath() + roboterAimingPath();
    }

    @Override
    public int getDistanceInCards(FieldObject fieldObject) {
        int count = 0;
        for (Tile tile : tiles) {
            if (tile.getFieldObject() == fieldObject) {
                return count;
            }
        }
        return -1;
    }

    @Override
    public int getDistanceToInTiles(FieldObject fieldObjcet) {
        return 0;
    }

    @Override
    public int getDistanceToRobotInCards(Robot robot) {
        int count = 0;
        for (Tile tile : tiles) {
            if (tile != me.getCurrentTile()) {
                count++;
            }
            if (tile.getRobot() == robot) {
                return count;
            }
        }
        return -1;
    }

    @Override
    public int getDistanceToRobotInTiles(Robot robot) {
        int difX = (int) Math
                .abs(robot.getCurrentTile().getAbsoluteCoordinates().getX() - position.getAbsoluteCoordinates().getX());
        int difY = (int) Math
                .abs(robot.getCurrentTile().getAbsoluteCoordinates().getY() - position.getAbsoluteCoordinates().getY());
        return difX + difY;
    }

    @Override
    public int getAmountOfHoles() {
        int count = 0;
        for (Tile tile : tiles) {
            for (Orientation or : Orientation.values()) {
                if (tile.getNeighbors().get(or) != null && tile.getNeighbors().get(or) instanceof Hole) {
                    count++;
                }
            }
            if (tile.getFieldObject() instanceof Hole) {
                count++;
            }
        }
        return count;
    }

    @Override
    public int getAmountOfLasers() {
        return laserAimingPath();
    }

    @Override
    public int getAmountOfConveyer() {
        int count = 0;
        for (Tile tile : tiles) {
            if (tile.getFieldObject() instanceof ConveyorBelt) {
                count++;
            }
        }
        return count;
    }

    @Override
    public boolean canReachNextFlag() {
        ArrayList<String> tmp = new ArrayList<>();
        tmp.add("flag");
        tmp.add("next");
        Point posi = (Point) con.getInformation(tmp).getInformation().get(0);
        for (Tile t : tiles) {
            if (t.getCoordinates().equals(posi)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gibt das Tile zurück, was von den gegebenen Tile aus auf dem man landen
     * würde wenn man an {@code fromHere} stehen würde mit der Orientierung
     * {@code or} und {@code withThisCard} legt.
     * 
     * @param orientation
     *            Die Orientierung ver der Ausgegangen werden soll
     * @param fromHere
     *            Das Feld, von dem aus das nächste errechnet werden soll
     * @param withThisCard
     *            Die Karte mit der man das Ziel erreichen will.
     * @return Das Feld was mit der was mit der gegebenen Karte und der
     *         Orientierung erreicht wird.
     */
    private Tile getNextTile(Orientation orientation, Tile fromHere, ProgramCard withThisCard) {
        if (withThisCard.getRange() == -1) {
            return fromHere.getNeighbors().get(Orientation.getOppositeDirection(orientation));
        }
        Tile tmp = fromHere;
        for (int i = withThisCard.getRange(); i > 0; i--) {
            if (tmp == null || tmp.getNeighbors().get(orientation) == null) {
                return null;
            }
            tmp = tmp.getNeighbors().get(orientation);
        }
        return tmp;
    }

    /**
     * Zählt die Roboter die auf Feldern stehen, die an das gegebene Feld
     * grenzen.
     * 
     * @param tile
     *            Das Feld von dem die Angrenzenden berachtet werden sollen
     * @return Die Anzahl der angrenzenden Roboter
     */
    private int countSurroundingRobots(Tile tile) {
        int tmp = 0;
        for (Orientation pOrientation : Orientation.values()) {
            if (tile.getNeighbors().get(pOrientation) != null
                    && tile.getNeighbors().get(pOrientation).getRobot() != null
                    && tile.getNeighbors().get(pOrientation).getRobot() != me) {
                tmp++;
            }
        }
        return tmp;
    }

    /**
     * Nimmt die gelegten Karten und packt die Felder die besucht werden in eine
     * Liste
     */
    private void convertCardsToTile() {
        tiles = new ArrayList<>();
        Tile tmp = me.getCurrentTile();
        // Das feld auf dem wir stehen hinzufügen
        tiles.add(tmp);
        Orientation tmpOr = orientation;
        Tile t;
        for (ProgramCard card : cards) {
            t = getNextTile(tmpOr, tmp, card);
            // Nächstes Feld simulieren
            if (t != null) {
                tiles.add(t);
                tmp = t;
            }
        }
    }

    /**
     * Zählt die Roboter, die in die Richtung der Kacheln zielen, die betreten
     * werden sollen.
     * 
     * @return Die Anzahl der Felder die "beschossen" werden.
     */
    private int roboterAimingPath() {
        int count = 0;
        // Alle geplante Zielkacheln
        for (Tile tile : tiles) {
            if (tile != position) {
                count += calcCount(count, tile);
            }
        }
        return count;
    }

    private int calcCount(int pCount, Tile tile) {
        int count = pCount;
        for (Orientation direction : Orientation.values()) {
            Tile tmp = tile.getNeighbors().get(direction);
            // Bis zum Rand
            if (tmp == null) {
                count += countRobots(tmp, direction);
            }
        }
        return count;
    }

    private int countRobots(Tile tile, Orientation direction) {
        int count = 0;
        Tile t = tile;

        while (t != null && t.getNeighbors().get(direction) != null) {
            // Treffen auf Wand an der Seite richtung dieser Kachel
            if (t.hasWall(Orientation.getOppositeDirection(direction))) {
                return count; // Abbruch wenn Wannd im Weg
            }
            if (t.getRobot() != null && t.getRobot().getOrientation() == Orientation.getOppositeDirection(direction)) {
                count++;
            }
            // Treffen auf Wand in auf der von dieser Kachel
            // entfernten Seite
            if (t.hasWall(direction)) {
                return count;
            }
            t = t.getNeighbors().get(direction);
        }
        return count;
    }

    /**
     * Zählt den Schaden der Laser, die in die Richtung der Kacheln zielen, die
     * betreten werden sollen.
     * 
     * @return Die Schadenspunkte der Laser die die Felder die "beschossen"
     *         werden treffen.
     */
    private int laserAimingPath() {
        int count = 0;
        // Alle geplante Zielkacheln
        for (Tile tile : tiles) {
            if (tile != position) {
                // Alle Richtungen
                for (Orientation direction : Orientation.values()) {
                    count += countLaser(direction, tile);
                }
            }
        }
        return count;
    }

    private int countLaser(Orientation ori, Tile tile) {
        Tile tmp = tile;
        int count = 0;
        // Bis zum Rand
        while (tmp.getNeighbors().get(ori) != null) {
            // Treffen auf Wand an der Seite richtung dieser Kachel
            if (tmp.hasWall(Orientation.getOppositeDirection(ori))) {
                return count; // Abbruch wenn Wand im Weg
            }
            // Treffen auf Wand in auf der von dieser Kachel
            // entfernten Seite
            if (tmp.hasWall(ori)) {
                count += tmp.getWall(ori).hasLaser();
                return count;
            }
            tmp = tmp.getNeighbors().get(ori);
        }
        return count;
    }
}
