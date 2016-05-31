/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki2.ai.memory;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import de.uni_bremen.factroytrouble.board.Board;
import de.uni_bremen.factroytrouble.board.Field;
import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.gameobjects.ConveyorBelt;
import de.uni_bremen.factroytrouble.gameobjects.Flag;
import de.uni_bremen.factroytrouble.gameobjects.Gear;
import de.uni_bremen.factroytrouble.gameobjects.Hole;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.player.MasterFactory;
import de.uni_bremen.factroytrouble.player.Player;
import de.uni_bremen.factroytrouble.player.ProgramCard;

/**
 * @author Artur
 *
 */
public class ScullyVisual {

    private static final Logger LOGGER = Logger.getLogger(ScullyVisual.class);

    MasterFactory masterFactory;

    /**
     * Board auf dem die Visual arbeitet
     */
    private Board board;

    /**
     * Liste aller Spieler
     */
    private List<Player> players;

    private Player aiPlayer;

    /**
     * Konstruktor der VisualUnit. Übergeben wird ein Board auf dem gearbeitet
     * werden soll.
     * 
     * @param pBoard,
     *            Board auf dem gearbeitet werden soll
     */
    public ScullyVisual(Board pBoard, Player pAIPlayer) {
        board = pBoard;
        aiPlayer = pAIPlayer;

    }

    /**
     * Gibt as zu den übergebenen Koordinaten das dazugehörige Tile.
     * 
     * @param x,
     *            x-Koordinate
     * @param y,
     *            y-koordinate
     * @return Tile zu den übergebenen Koordinaten.
     */
    public Tile getTile(int x, int y) {
        if (x < 0) {
            throw new IllegalArgumentException("X-Koordinate ist negativ");
        }
        if (y < 0) {
            throw new IllegalArgumentException("Y-Koordinate ist negativ");
        }
        Map<Point, Field> fields = board.getFields();
        for (Field f : fields.values()) {
            for (Tile t : f.getTiles().values()) {
                Point tileCoor = board.getAbsoluteCoordinates(t);

                if (tileCoor != null && tileCoor.equals(new Point(x, y))) {

                    return t;
                }

            }
        }
        return null;
    }

    /**
     * Holt sich alle Roboter vom Board
     * 
     * @return Liste von allen Robotern, die sich auf dem Board befinden.
     */
    public List<Robot> getRobots() {

        List<Robot> tempPlayers = new ArrayList<>();
        Map<Point, Field> fields = board.getFields();
        for (Field f : fields.values()) {
            for (Tile t : f.getTiles().values()) {
                if (t.getRobot() != null) {

                    tempPlayers.add(t.getRobot());
                }

            }
        }
        return tempPlayers;

    }

    /**
     * Holt sich alle Löcher vom Board
     * 
     * @return Liste von allen RLöchern, die sich auf dem Board befinden.
     */
    public List<Point> getHoles() {

        List<Point> tempListOfholes = new ArrayList<>();
        Map<Point, Field> fields = board.getFields();
        for (Field f : fields.values()) {
            for (Tile t : f.getTiles().values()) {
                if (t.getFieldObject() instanceof Hole) {

                    tempListOfholes.add(board.getAbsoluteCoordinates(t));
                }

            }
        }
        return tempListOfholes;

    }

    /**
     * Holt sich alle Gears vom Board
     * 
     * @return Liste von allen Gears, die sich auf dem Board befinden.
     */
    public List<Point> getGears() {

        List<Point> tempListOfGears = new ArrayList<>();
        Map<Point, Field> fields = board.getFields();
        for (Field f : fields.values()) {
            for (Tile t : f.getTiles().values()) {
                if (t.getFieldObject() instanceof Gear) {

                    tempListOfGears.add(board.getAbsoluteCoordinates(t));
                }

            }
        }
        return tempListOfGears;

    }

    /**
     * Holt sich die Punkte der Tiles, auf welchen sich ein Förderband befindet.
     * 
     * @return Liste von allen Punkten, wo sich ein Förderband befindet.
     */
    public List<Point> getConveyor() {
        List<Point> tempListOfCB = new ArrayList<>();
        Map<Point, Field> fields = board.getFields();
        for (Field f : fields.values()) {
            for (Tile t : f.getTiles().values()) {
                if (t.getFieldObject() instanceof ConveyorBelt) {

                    tempListOfCB.add(board.getAbsoluteCoordinates(t));
                }

            }
        }
        return tempListOfCB;
    }

    /**
     * Gibt eine Liste von Punkten zurück, auf denen sich eine Wand Richtung
     * Norden zeigen.
     * 
     * @return Liste von Punkten von Wänden Richting Norden.
     */
    public List<Point> getWallsNorth() {
        List<Point> tempListOfWalls = new ArrayList<>();
        Map<Point, Field> fields = board.getFields();
        for (Field f : fields.values()) {
            for (Tile t : f.getTiles().values()) {
                if (t.getWall(Orientation.NORTH) != null) {

                    tempListOfWalls.add(t.getAbsoluteCoordinates());
                }

            }
        }

        return tempListOfWalls;

    }

    /**
     * Gibt eine Liste von Punkten zurück, auf denen sich eine Wand Richtung
     * Osten zeigen.
     * 
     * @return Liste von Punkten von Wänden Richting Osten.
     */
    public List<Point> getWallsEast() {
        List<Point> tempListOfWalls = new ArrayList<>();
        Map<Point, Field> fields = board.getFields();
        for (Field f : fields.values()) {
            for (Tile t : f.getTiles().values()) {
                if (t.getWall(Orientation.EAST) != null) {

                    tempListOfWalls.add(t.getAbsoluteCoordinates());
                }

            }
        }

        return tempListOfWalls;

    }

    /**
     * Gibt eine Liste von Punkten zurück, auf denen sich eine Wand Richtung
     * Süden zeigen.
     * 
     * @return Liste von Punkten von Wänden Richting Süden.
     */
    public List<Point> getWallsSouth() {
        List<Point> tempListOfWalls = new ArrayList<>();
        Map<Point, Field> fields = board.getFields();
        for (Field f : fields.values()) {
            for (Tile t : f.getTiles().values()) {
                if (t.getWall(Orientation.SOUTH) != null) {

                    tempListOfWalls.add(t.getAbsoluteCoordinates());
                }

            }
        }
        return tempListOfWalls;

    }

    /**
     * Gibt eine Liste von Punkten zurück, auf denen sich eine Wand Richtung
     * Westen zeigen.
     * 
     * @return Liste von Punkten von Wänden Richting Westen.
     */
    public List<Point> getWallsWest() {
        List<Point> tempListOfWalls = new ArrayList<>();
        Map<Point, Field> fields = board.getFields();
        for (Field f : fields.values()) {
            for (Tile t : f.getTiles().values()) {
                if (t.getWall(Orientation.WEST) != null) {
                    tempListOfWalls.add(t.getAbsoluteCoordinates());
                }

            }
        }

        return tempListOfWalls;

    }

    /**
     * Gibt eine Liste von allen Tiles zurück.
     * 
     * @return Liste mit allen Tiles auf dem Spielfeld.
     */
    public List<Tile> getTiles() {
        List<Tile> tempListOfTiles = new ArrayList<>();
        Map<Point, Field> fields = board.getFields();
        for (Field f : fields.values()) {
            for (Tile t : f.getTiles().values()) {
                tempListOfTiles.add(t);
                board.getAbsoluteCoordinates(t);
            }

        }
        return tempListOfTiles;
    }

    /**
     * Gibt ein Punkt zurück, auf dem sich die angefragte Flagge befindet.
     * 
     * @param number,
     *            nummer der zu suchenden Flagge.
     * @return Flagge zu der gesuchten Nummer.
     */
    public Point getFlagPosition(int number) {
        for (Tile tile : getFlags()) {
            if (tile.getFieldObject() instanceof Flag) {
                Flag f = (Flag) tile.getFieldObject();
                if (f.getNumber() == number) {
                    return board.getAbsoluteCoordinates(tile);
                }
            }
        }
        return null;
    }

    /**
     * Gibt die Liste aller Spieler zurück.
     * 
     * @return Liste aller Spieler.
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Gibt das aktuelle Board zurück.
     * 
     * @return Das aktuelle Board.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Rechnet Feldkoordinaten in absolute Koordinaten um.
     * 
     * @param tile,
     *            tile mit dem Feldkoordinaten.
     * @return Absolute Koordinaten des Tiles auf dem Board.
     */
    public Point getAbsolutePoint(Tile tile) {
        return board.getAbsoluteCoordinates(tile);
    }

    /**
     * Gibt alle Handkarten zurück.
     * 
     * @return Liste mit den Handkarten.
     */
    public List<ProgramCard> getCards() {
        return aiPlayer.getPlayerCards();
        
    }

    /**
     * Gibt die Karten zurück, die im Register gesperrt sind.
     * 
     * @return Listeb der gesperrten Karten.
     */
    public List<ProgramCard> getLockedCards() {
        boolean[] locked = aiPlayer.getRobot().registerLockStatus();
        ProgramCard[] cardsInRegister = aiPlayer.getRobot().getRegisters();
        List<ProgramCard> lockedCards = new ArrayList<>();
        for (int i = 0; i < locked.length; i++) {
            if (locked[i]) {
                lockedCards.add(cardsInRegister[i]);
            }
        }

        return lockedCards;
    }

    /**
     * Gibt den Standort der Nächsten zu erreichenden Flagge zurück.
     * 
     * @return Punkt auf dem sich die nächste Flagge befindet.
     */
    public Point getNextFlag() {
        int nextFlag = aiPlayer.getRobot().getFlagCounterStatus() + 1;
        return getFlagPosition(nextFlag);

    }

    /**
     * Brechnet den Punkt und Richtung des Standortes nach Ausführung einer
     * CB-Bwewgung.
     * 
     * @return Startpunkt mit Endpunkt und Orientierung.
     */
    public Map<Point, ScullyConveyorInfo> getConveyorMove() {
        HashMap<Point, ScullyConveyorInfo> returnMap = new HashMap<>();
        for (Tile tempTile : getFieldObjects()) {
            if (tempTile.getFieldObject() instanceof ConveyorBelt) {
                int c = ((ConveyorBelt) tempTile.getFieldObject()).isExpress() ? 2 : 1;
                Point afterConveyor = null;
                for (int x = c; x > 0; x--) {
                    Tile tempTileCopy = tempTile;
                    afterConveyor = computeAfterConveyor(tempTileCopy, tempTile, afterConveyor, returnMap);
                }
            }
        }

        handleTurns(returnMap);

        return returnMap;
    }

    /**
     * Gibt eine Liste mit allen Feldobjekten zurück.
     * 
     * @return Liste mit allen Feldobjekten.
     */
    public List<Tile> getFieldObjects() {
        List<Tile> tempListOfTiles = new ArrayList<>();
        Map<Point, Field> fields = board.getFields();
        for (Field f : fields.values()) {
            for (Tile t : f.getTiles().values()) {
                if (t.getFieldObject() != null) {
                    tempListOfTiles.add(t);
                }
            }
    
        }
        return tempListOfTiles;
    
    }

    /**
     * Gibt die Zahnräder zurück die nach Rechts drehen
     * 
     * @return die Zahnräder
     */
    public List<Point> getGearsRight() {
        List<Point> tempListOfGears = new ArrayList<>();
        for (Tile t : getFieldObjects()) {
            if (t.getFieldObject() instanceof Gear) {
                Gear gear = (Gear) t.getFieldObject();
                if (!gear.rotatesLeft()) {
                    tempListOfGears.add(t.getAbsoluteCoordinates());
                }
            }
        }
        return tempListOfGears;
    }

    /**
     * Gibt die Zahnräder zurück die nach Links drehen
     * 
     * @return die Zahnräder
     */
    public List<Point> getGearsLeft() {
        List<Point> tempListOfGears = new ArrayList<>();
        for (Tile t : getFieldObjects()) {
            if (t.getFieldObject() instanceof Gear) {
                Gear gear = (Gear) t.getFieldObject();
                if (gear.rotatesLeft()) {
                    tempListOfGears.add(t.getAbsoluteCoordinates());
                }
            }
        }
        return tempListOfGears;
    }

    /**
     * Gibt den Punkt oben rechts dem Feld zurück
     * 
     * @return der Punkt oben rechts
     */
    public Point getHighestPoint() {
        Tile tempTile = getTile(0, 0);
        Point result = tempTile.getAbsoluteCoordinates();
        while (tempTile != null) {
            Tile notNullTile = tempTile;
            while (tempTile != null) {
                result = tempTile.getAbsoluteCoordinates();
                tempTile = tempTile.getNeighbors().get(Orientation.EAST);
            }
            tempTile = notNullTile;
            tempTile = tempTile.getNeighbors().get(Orientation.NORTH);
        }
        return result;
    }

    private Point computeAfterConveyor(Tile pTempTileCopy, Tile tempTile, Point pAfterConveyor,
            Map<Point, ScullyConveyorInfo> returnMap) {
        Tile tempTileCopy = pTempTileCopy;
        Point afterConveyor = pAfterConveyor;
        if (tempTileCopy.getFieldObject() instanceof ConveyorBelt) {
            if (afterConveyor != null) {
                try {
                    tempTileCopy = getTile(afterConveyor.x, afterConveyor.y);
                } catch (IllegalArgumentException e) {
                    LOGGER.debug("Loch gefunden." + e);
                }
            }

            if (tempTileCopy != null && tempTileCopy.getFieldObject() instanceof ConveyorBelt) {
                afterConveyor = moveAfterConveyor(tempTileCopy, afterConveyor);
                returnMap.put(tempTile.getAbsoluteCoordinates(), new ScullyConveyorInfo(afterConveyor, 0));
            }
        }
        return afterConveyor;
    }

    private Point moveAfterConveyor(Tile tempTileCopy, Point pAfterConveyor) {
        Point afterConveyor = pAfterConveyor;
        if (((ConveyorBelt) tempTileCopy.getFieldObject()).getOrientation() == Orientation.NORTH) {
            afterConveyor = new Point(tempTileCopy.getAbsoluteCoordinates().x,
                    tempTileCopy.getAbsoluteCoordinates().y + 1);
        }
        if (((ConveyorBelt) tempTileCopy.getFieldObject()).getOrientation() == Orientation.SOUTH) {
            afterConveyor = new Point(tempTileCopy.getAbsoluteCoordinates().x,
                    tempTileCopy.getAbsoluteCoordinates().y - 1);
        }
        if (((ConveyorBelt) tempTileCopy.getFieldObject()).getOrientation() == Orientation.WEST) {
            afterConveyor = new Point(tempTileCopy.getAbsoluteCoordinates().x - 1,
                    tempTileCopy.getAbsoluteCoordinates().y);
        }
        if (((ConveyorBelt) tempTileCopy.getFieldObject()).getOrientation() == Orientation.EAST) {
            afterConveyor = new Point(tempTileCopy.getAbsoluteCoordinates().x + 1,
                    tempTileCopy.getAbsoluteCoordinates().y);
        }
        return afterConveyor;
    }

    /**
     * Holt sich alle Flaggen vom Board
     * 
     * @return Liste von allen Flaggen, die sich auf dem Board befinden.
     */
    private List<Tile> getFlags() {
        List<Tile> tempListOfFlags = new ArrayList<>();
        Map<Point, Field> fields = board.getFields();
        for (Field f : fields.values()) {
            for (Tile t : f.getTiles().values()) {
                if (t.getFieldObject() instanceof Flag) {
    
                    tempListOfFlags.add(t);
                }
    
            }
        }
        return tempListOfFlags;
    }

    private void handleTurns(Map<Point, ScullyConveyorInfo> pMap) {
        for (Point startTpoint : pMap.keySet()) {
            ScullyConveyorInfo conveyorInfo = pMap.get(startTpoint);
            Orientation startOrientation = ((ConveyorBelt) getTile(startTpoint.x, startTpoint.y).getFieldObject())
                    .getOrientation();
            int x = conveyorInfo.getPointAfterConveyor().x;
            int y = conveyorInfo.getPointAfterConveyor().y;
            try {
                if (getTile(x, y) == null || !(getTile(x, y).getFieldObject() instanceof ConveyorBelt)) {
                    continue;
                }
            } catch (IllegalArgumentException e) {
                LOGGER.debug("Loch gefunden." + e);
                continue;
            }

            Orientation end2Orientation = ((ConveyorBelt) getTile(x, y).getFieldObject()).getOrientation();

            if (isTurnRight(startOrientation, end2Orientation)) {
                conveyorInfo.setDirection(2);
            } else if (isTurnLeft(startOrientation, end2Orientation)) {
                conveyorInfo.setDirection(1);
            }
        }
    }

    private boolean isTurnRight(Orientation pStartOrientation, Orientation pEndOrientation) {
        if (pStartOrientation == Orientation.NORTH && pEndOrientation == Orientation.EAST) {
            return true;
        }
        if (pStartOrientation == Orientation.EAST && pEndOrientation == Orientation.SOUTH) {
            return true;
        }
        if (pStartOrientation == Orientation.SOUTH && pEndOrientation == Orientation.WEST) {
            return true;
        }
        if (pStartOrientation == Orientation.WEST && pEndOrientation == Orientation.NORTH) {
            return true;
        }
        return false;
    }

    private boolean isTurnLeft(Orientation pStartOrientation, Orientation pEndOrientation) {
        if (pStartOrientation == Orientation.NORTH && pEndOrientation == Orientation.WEST) {
            return true;
        }
        if (pStartOrientation == Orientation.EAST && pEndOrientation == Orientation.NORTH) {
            return true;
        }
        if (pStartOrientation == Orientation.SOUTH && pEndOrientation == Orientation.EAST) {
            return true;
        }
        if (pStartOrientation == Orientation.WEST && pEndOrientation == Orientation.SOUTH) {
            return true;
        }
        return false;
    }

}
