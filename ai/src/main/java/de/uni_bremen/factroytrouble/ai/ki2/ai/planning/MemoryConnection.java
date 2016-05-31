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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uni_bremen.factroytrouble.ai.ki2.ai.memory.ScullyConveyorInfo;
import de.uni_bremen.factroytrouble.ai.ki2.api.decision.unconsciousness.UnconsciousnessUnit;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.player.GameMoveBackwardCard;
import de.uni_bremen.factroytrouble.player.GameMoveForwardCard;
import de.uni_bremen.factroytrouble.player.GameTurnLeftCard;
import de.uni_bremen.factroytrouble.player.GameTurnRightCard;
import de.uni_bremen.factroytrouble.player.GameUturnCard;
import de.uni_bremen.factroytrouble.player.ProgramCard;

public class MemoryConnection {
    private List<ProgramCard> handCards;
    private HashMap<String, List<Point>> walls;
    private List<Object> holes;
    private UnconsciousnessUnit uncons;
    private Orientation orient;
    private Point pos;
    private Point flagPos;
    private Object lockedCards;
    private Object conveyors;
    private Object leftGears;
    private Object rightGears;
    private Object highestPoint;
    private Object enemies;

    public MemoryConnection(UnconsciousnessUnit unconsciousness) {
        uncons = unconsciousness;
        walls = new HashMap<>();
    }

    /**
     * Schaut ob eine Information vom richtigen Typ ist
     * 
     * @param test,
     *            zu testende information
     * @param type,
     *            erwarteter Typ
     */
    public void validateType(Object test, @SuppressWarnings("rawtypes") Class type) {
        if (!type.isInstance(test)) {
            throw new RuntimeException("Information ist null oder vom falschen Typ");
        }
    }

    /**
     * Holt Handkarten eines Typs aus der Memory
     * 
     * @param type,
     *            Kartentyp
     * @return Die Karten
     */
    @SuppressWarnings("unchecked")
    public List<ProgramCard> getHandCards(String type) {
        List<ProgramCard> cards;
        if (handCards == null) {
            List<String> getCardKey = new ArrayList<>();
            getCardKey.add("cards");
            getCardKey.add("player");
            Object playerCards = uncons.getInformation(getCardKey).getInformation();
            if (!((List<Object>) playerCards).isEmpty()) {
                validateType(((List<Object>) playerCards).get(0), ProgramCard.class);
            }
            handCards = (List<ProgramCard>) playerCards;
        }
        switch (type) {
        case "Rechtsdrehkarten":
            cards = getCard(new GameTurnRightCard(0), 0);
            break;
        case "Linksdrehkarten":
            cards = getCard(new GameTurnLeftCard(0), 0);
            break;
        case "Uturnkarten":
            cards = getCard(new GameUturnCard(0), 0);
            break;
        case "Rückwärtskarten":
            cards = getCard(new GameMoveBackwardCard(0, 0), -1);
            break;
        case "1vorwärtskarten":
            cards = getCard(new GameMoveForwardCard(0, 0, 0), 1);
            break;
        case "2vorwärtskarten":
            cards = getCard(new GameMoveForwardCard(0, 0, 0), 2);
            break;
        case "3vorwärtskarten":
            cards = getCard(new GameMoveForwardCard(0, 0, 0), 3);
            break;
        default:
            cards = handCards;
            break;
        }
        return cards;
    }

    /**
     * Holt die eigene Orientierung aus der Memory
     * 
     * @return Die Orientierung
     */
    public Orientation getOrientation() {
        if (orient == null) {
            List<String> orientKey = new ArrayList<>();
            orientKey.add("my");
            orientKey.add("ori");
            Object orients = uncons.getInformation(orientKey).getInformation().get(0);
            validateType(orients, Orientation.class);
            orient = (Orientation) orients;
        }
        return orient;
    }

    /**
     * Holt die eigene Position aus der Memory
     * 
     * @return Die Position
     */
    public Point getPos() {
        if (pos == null) {
            List<String> posKey = new ArrayList<>();
            posKey.add("my");
            posKey.add("position");
            Object myPos = uncons.getInformation(posKey).getInformation().get(0);
            validateType(myPos, Point.class);
            pos = (Point) myPos;
        }
        return pos;
    }

    /**
     * Holt die Position der nächsten Flagge aus der Memory
     * 
     * @return Die Position der Flagge
     */
    public Point getFlagPos() {
        if (flagPos == null) {
            List<String> flagKey = new ArrayList<>();
            flagKey.add("flag");
            flagKey.add("next");
            Object nextFlagPos = uncons.getInformation(flagKey).getInformation().get(0);
            validateType(nextFlagPos, Point.class);
            flagPos = (Point) nextFlagPos;
        }
        return flagPos;
    }

    /**
     * Holt Wände aus der Memory
     * 
     * @param orient,
     *            Die Richtung, in welche die Wand schaut
     * @return Die Wände
     */
    @SuppressWarnings("unchecked")
    public List<Point> getWalls(Orientation orient) {
        List<String> wallKey = new ArrayList<>();
        wallKey.add("walls");
        switch (orient) {
        case NORTH:
            wallKey.add("north");
            break;
        case EAST:
            wallKey.add("east");
            break;
        case SOUTH:
            wallKey.add("south");
            break;
        default:
            wallKey.add("west");
        }
        if (walls.get(wallKey.get(1)) == null) {
            Object allWalls = uncons.getInformation(wallKey).getInformation();
            if (!((List<List<Object>>) allWalls).get(0).isEmpty()) {
                validateType((((List<List<Object>>) allWalls).get(0)).get(0), Point.class);
            }
            walls.put(wallKey.get(1), ((List<List<Point>>) allWalls).get(0));
        }
        return walls.get(wallKey.get(1));
    }

    /**
     * Holt Löcher aus der Memory
     * 
     * @return Die Löcher
     */
    @SuppressWarnings("unchecked")
    public List<Point> getHoles() {
        Point highestPoint2 = getHighestPoint();
        if (holes == null) {
            List<String> holeKey = new ArrayList<>();
            holeKey.add("holes");
            Object hole = uncons.getInformation(holeKey).getInformation();
            if (!((List<List<Object>>) hole).get(0).isEmpty()) {
                validateType((((List<List<Object>>) hole).get(0)).get(0), Point.class);
            }
            holes = (List<Object>) hole;
        }
        List<Point> richtigeHoles = (List<Point>) holes.get(0);
        richtigeHoles.addAll(generateGameborder(highestPoint2.x + 1, highestPoint2.y + 1));
        return richtigeHoles;
    }

    /**
     * Holt gelockte Karten im Register aus der Memory
     * 
     * @return Die Karten
     */
    @SuppressWarnings("unchecked")
    public List<ProgramCard> getLockedCards() {
        if (lockedCards == null) {
            List<String> lockedCardsKey = new ArrayList<>();
            lockedCardsKey.add("cards");
            lockedCardsKey.add("locked");
            List<Object> lockedPlayerCards = (List<Object>) uncons.getInformation(lockedCardsKey).getInformation()
                    .get(0);
            if (!lockedPlayerCards.isEmpty()) {
                validateType(lockedPlayerCards.get(0), ProgramCard.class);
            }
            lockedCards = lockedPlayerCards;
        }
        return (List<ProgramCard>) lockedCards;
    }

    /**
     * Holt alle Zahnräder, die nach Rechts drehen
     * 
     * @return, Zahnräder
     */
    @SuppressWarnings("unchecked")
    public List<Point> getRightGears() {
        if (rightGears == null) {
            List<String> rightGearsKey = new ArrayList<>();
            rightGearsKey.add("right");
            rightGearsKey.add("gears");
            List<Object> rightGear = (List<Object>) uncons.getInformation(rightGearsKey).getInformation().get(0);
            if (!rightGear.isEmpty()) {
                validateType(rightGear.get(0), Point.class);
            }
            rightGears = rightGear;
        }
        return (List<Point>) rightGears;
    }

    /**
     * Holt alle Zahnräder, die nach Links drehen
     * 
     * @return, Zahnräder
     */
    @SuppressWarnings("unchecked")
    public List<Point> getLeftGears() {
        if (leftGears == null) {
            List<String> leftGearsKey = new ArrayList<>();
            leftGearsKey.add("left");
            leftGearsKey.add("gears");
            List<Object> leftGear = (List<Object>) uncons.getInformation(leftGearsKey).getInformation().get(0);
            if (!leftGear.isEmpty()) {
                validateType(leftGear.get(0), Point.class);
            }
            leftGears = leftGear;
        }
        return (List<Point>) leftGears;

    }

    /**
     * Baut den Spielfeldrand
     * 
     * @param x,
     *            Höchste x Koordinate des Feldes
     * @param y,
     *            Höchste y Koordinate des Feldes
     * 
     * @return Spielfeldrand als Löcher
     */
    public List<Point> generateGameborder(int x, int y) {
        List<Point> border = new ArrayList<>();
        for (int c = 0; c < y; c++) {
            Point point = new Point(-1, c);
            border.add(point);
        }
        for (int c = 0; c < x; c++) {
            Point point = new Point(c, -1);
            border.add(point);
        }
        for (int c = 0; c < y; c++) {
            Point point = new Point(x, c);
            border.add(point);
        }
        for (int c = 0; c < x; c++) {
            Point point = new Point(c, y);
            border.add(point);
        }
        return border;
    }

    /**
     * Holt alle Förderbänder aus der Memory
     * 
     * @return die Förderbänder
     */
    @SuppressWarnings("unchecked")
    public Map<Point, ScullyConveyorInfo> getConveyor() {
        if (conveyors == null) {
            List<String> conveyorKey = new ArrayList<>();
            conveyorKey.add("all");
            conveyorKey.add("conveyor");
            Object conveyorList = uncons.getInformation(conveyorKey).getInformation().get(0);
            validateType(conveyorList, HashMap.class);
            conveyors = conveyorList;
        }
        return (Map<Point, ScullyConveyorInfo>) conveyors;
    }

    /**
     * Holt die Position aller Gegner und die Position der Flagge, die sie als
     * nächstes erreichen sollen aus der Memory
     * 
     * @return die Position und die Flagge alle Gegner
     */
    @SuppressWarnings("unchecked")
    public Map<Point, Point> getEnemies() {
        if (enemies == null) {
            List<String> enemiesKey = new ArrayList<>();
            enemiesKey.add("all");
            enemiesKey.add("enemies");
            if (uncons.getInformation(enemiesKey) != null) {
                Object enemiesList = uncons.getInformation(enemiesKey).getInformation().get(0);
                validateType(enemiesList, HashMap.class);
                enemies = enemiesList;
            } else {
                enemies = new HashMap<>();
            }
        }
        return (Map<Point, Point>) enemies;
    }

    /**
     * Ändert die Flaggen Position manuell
     */
    public void setFlagPos(Point newFlagPos) {
        flagPos = newFlagPos;
    }

    /**
     * Ändert die eigene Position manuell
     */
    public void setMyPos(Point myPos) {
        pos = myPos;
    }

    /**
     * Ändert die eigene Orientierung manuell
     */
    public void setMyOrient(Orientation myOrient) {
        orient = myOrient;
    }

    @SuppressWarnings("unchecked")
    private Point getHighestPoint() {
        if (highestPoint == null) {
            List<String> highestPointKey = new ArrayList<>();
            highestPointKey.add("highest");
            highestPointKey.add("point");
            Object highestPoint2 = uncons.getInformation(highestPointKey).getInformation();
            validateType(((List<Point>) highestPoint2).get(0), Point.class);
            highestPoint = ((List<Point>) highestPoint2).get(0);
        }
        return (Point) highestPoint;
    }

    /**
     * Prüft nach einen Typ Karte und holt alle passende Karten raus
     * 
     * @param type,
     *            der Typ
     * @param move,
     *            die Range der Karte
     * @return Karten des Types
     */
    private List<ProgramCard> getCard(ProgramCard type, int move) {
        List<ProgramCard> cards = new ArrayList<>();
        for (Object card : handCards) {
            if (type.getClass().isInstance(card) && ((ProgramCard) card).getRange() == move) {
                cards.add((ProgramCard) card);
            }
        }
        return cards;
    }
}
