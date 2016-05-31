/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.player;

import de.uni_bremen.factroytrouble.board.*;
import de.uni_bremen.factroytrouble.gameobjects.*;
import org.apache.log4j.Logger;

import java.awt.Point;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Die Klasse erzeugt GameBoards und stellt diese bereit. Hierfür wird die Datei
 * 'COURSE_MANUAL' eingelesen, welche die verfügbaren GameBoards definiert. Zu
 * einem GameBoard gehören ein GameDock, mindestens ein GameField und
 * Flaggenpositionen. Die Information über existierende GameField und GameDock
 * holt sich die Klasse vom FieldLoader, ebenso wie die Startpostionen der
 * Docks. Um ein GameBoard zu erhalten ist es notwendig zunächst die verfügbaren
 * GameBoards mittels getAvailableBoards abzufragen und eines an getBoard zu
 * übergeben.
 *
 * @author Stefan
 */
public class BoardManager {

    private static final Logger LOGGER = Logger.getLogger(FieldLoader.class);
    private static final Pattern FIELD_NAME_PATTERN  = Pattern.compile("(FIELD_[^_]*)_([NORTHWESAU]{4,5})");

    private final FieldLoader fieldLoader;

    private final int gameId;
    private BufferedReader bufReader;
    private Map<String, List<String>> boards;
    private Map<String, Point[]> flags;
    private List<String> exFields; // exstingFields
    private List<String> exDocks; // existingDocks

    private Map<String, Map<String, Point>> robotStarts; // Board, konkrete

    BoardManager(int gameId,FieldLoader fieldLoader) {
        this.gameId = gameId;
        this.fieldLoader = fieldLoader;
        init();
    }

    public void init() {
        exFields = Arrays.asList(fieldLoader.getAvailableFields());
        exDocks = Arrays.asList(fieldLoader.getAvailableDocks());
        boards = new HashMap<>();
        flags = new HashMap<>();
        robotStarts = new HashMap<>();
        readAvailableBoards();
        fieldLoader.init();
    }

    /**
     * Gibt die Startpositionen der Docks zurück, wie diese vom FieldLoader
     * eingelesen worden sind. Diese werden zur Initialisierung eines Spiels
     * benötigt.
     *
     * @param board
     *            GameBoard-Name
     * @return Mögliche Startpositionen gemäß des im GameBoard enthaltenen Docks
     */
    public Point[] getStartPositions(String board) {
        if (!boards.keySet().contains(board)) {
            LOGGER.error("Für das GameBoard ".concat(board)
                    .concat(" können keine Startpositionen ermittelt werden (existiert nicht!)."));
            return new Point[] {};
        }
        return fieldLoader.getStartPoints(boards.get(board).get(0));
    }

    /**
     * Die Methode erzeugt einzelne GameBoards, wie diese im COURSE_MANUAL
     * berschrieben sind.
     *
     * @param board
     *            GameBoard-Name
     * @return GameBoard
     */
    public Board buildBoard(String board) {
        if (!boards.keySet().contains(board)) {
            LOGGER.error("Das GameBoard ".concat(board).concat(" kann nicht aufgebaut werden (existiert nicht!)."));
        }
        List<Field> gameFields = new ArrayList<>();
        Dock dock = null;
        int yPosition = 0;
        for (String field : boards.get(board)) {
            // nur EIN Dock pro Spielfeld
            if (field.contains("DOCK") && exDocks.contains(field) && dock == null) {
                dock = (Dock) fieldLoader.getField(new Point(0, 0), field);
            }
            if (field.contains("FIELD")) {
                gameFields.add(getTurnedField(board, field, yPosition));
            }
            yPosition++;
        }
        gameFields = setFlags(board, gameFields);
        return new GameBoard(dock, gameFields.toArray(new Field[gameFields.size()]));
    }

    /**
     * Die Methode gibt im COURSE_MANUAL beschriebenen GameBoards mit den
     * zugehörigen Startpostionen in Form einer Map<String,List<Point>> zurück.
     *
     * @return Bekannte Boards mit Startposionen
     */
    public Map<String, List<Point>> getAvailableBoards() {
        Map<String, List<Point>> available = new HashMap<>();
        String[] gameBoards = boards.keySet().toArray(new String[boards.keySet().size()]);
        for (String bb : gameBoards) {
            Point[] points = fieldLoader.getStartPoints(boards.get(bb).get(0));
            List<Point> pointList = new ArrayList<>();
            for (Point point : points) {
                pointList.add(point);
            }
            available.put(bb, pointList);
        }
        return available;
    }

    /*
     * Durch GameMaster aufzurufen, damit dieser an die GUI die konkreten
     * Roboter-Startpositionen an die GUI übermitteln kann.
     */
    public Map<String, Map<String, Point>> getRobotStarts() {
        return robotStarts;
    }
    
    public int getFlagCount(String board){
        return flags.get(board).length;
    }

    /*
     * Die Methode liest die im COURSE_MANUAL hinterlegten GameBoards ein
     */
    private void readAvailableBoards() {
        readCourseManual(getClass().getResourceAsStream("/boards/COURSE_MANUAL"));
        File customCourseManual = new File(System.getProperty("user.home") + File.separator + "factoryTrouble" + File.separator + "course"+ File.separator + "CUSTOM_COURSE_MANUAL");
        if(customCourseManual.exists()) {
            try {
                readCourseManual(new FileInputStream(customCourseManual));
            } catch (FileNotFoundException e) {
                LOGGER.error("Fail to load custom course manual", e);
            }
        }
    }

    private void readCourseManual(InputStream courseManualInputStream) {
        bufReader = null;
        try {
            bufReader = new BufferedReader(new InputStreamReader(courseManualInputStream));
            String line = "";
            while ((line = bufReader.readLine()) != null) {
                String[] board = line.split(",");
                boards.put(board[0], readFields(board));
                putRobotStarts(board[0], board[1]);
                flags.put(board[0], readFlags(board));
            }
        } catch (IOException e) {
            LOGGER.error("COURSE_MANUAL konnte nicht geladen werden.", e);
        } finally {
            if (bufReader != null) {
                try {
                    bufReader.close();
                } catch (IOException e) {
                    LOGGER.error("Der BufferedReader zum Auslesen des COURSE_MANUAL konnte nicht geschlossen werden.",
                            e);
                }
            }
        }
    }

    /*
     * Hilfsmethode für readAvailableBoards. Setzt für EIN Board für Roboter,
     * die durch Namen gegeben sind, konkrete Startpositionen, sodass die GUI
     * diese in der Vorschau auf dem Board platzieren kann.
     */
    private void putRobotStarts(String board, String dock) {
        Point[] starts = fieldLoader.getStartPoints(dock);
        if (GameMaster.ROBOT_NAMES.size() != starts.length) {
            LOGGER.error("Anzahl der Roboter und der Startpositionen stimmen nicht überein!");
        }
        Map<String, Point> robotPoints = new HashMap<>();
        List<String> robotNames = GameMaster.ROBOT_NAMES;
        for (int ii = 0; ii < robotNames.size(); ii++) {
            robotPoints.put(robotNames.get(ii), starts[ii]);
        }
        robotStarts.put(board, robotPoints);
    }

    /*
     * Die Methode ermittelt aus den eingelesenen Zeilen das Dock, die Fields
     * und die Flaggenpunkte. Das Dock und die Fields werden als String durch
     * den Aufrufer in boards festgehalten.
     */
    private List<String> readFields(String... board) {
        List<String> fields = new ArrayList<>();
        for (String ff : board) {
            if (ff.contains("DOCK") || ff.contains("FIELD")) {
                fields.add(ff);
            }
        }
        return fields;
    }

    /*
     * Die Methode ermittelt aus den eingelesenen Zeilen die Flaggenpunkte,
     * welche als String durch den Aufrufer in flags festgehalten werden.
     */
    private Point[] readFlags(String... board) {
        List<Point> boardFlags = new ArrayList<>();
        for (String pp : board) {
            if (pp.matches("-?[0-9]+_[0-9]+")) {
                String[] flagPoint = pp.split("_");
                boardFlags.add(new Point(Integer.parseInt(flagPoint[0]), Integer.parseInt(flagPoint[1])));
            }
        }
        return boardFlags.toArray(new Point[boardFlags.size()]);
    }

    /*
     * Hilfsmethode von BuildBoard zur Verringerung der Komplexität
     */
    private Field getTurnedField(String board, String field, int yPosition) {
        // 7 entspricht "_NORTH".length() + 1, 6 entspricht "_EAST".length() + 1
        Matcher fieldNameMatcher = FIELD_NAME_PATTERN.matcher(field);
        if (!fieldNameMatcher.find()) {
            LOGGER.error("Das GameField ".concat(field).concat(" vom GameBoard ".concat(board).concat(" konnte nicht geladen werden.")));
            return null;
        }
        String nameGroup = fieldNameMatcher.group(1);
        String orientationGroup = fieldNameMatcher.group(2);
        if ("EAST".equals(orientationGroup) || "WEST".equals(orientationGroup)) {
            return turn(yPosition, nameGroup, orientationGroup);
        } else if ("NORTH".equals(orientationGroup)) {
            Field boardField = fieldLoader.getField(new Point(0, yPosition), nameGroup);
            for(Tile tile:boardField.getTiles().values()){
                if(tile.getFieldObject() instanceof Flag){
                    tile.setFieldObject(null);
                }
            }
            return boardField;
        } else {
            return turn(yPosition, nameGroup, orientationGroup);
        }
    }

    /*
     * Die Methode steuert das Drehen einzelner Fields gemäß des Kursbuches.
     * yPosition steht für die Position des Fields auf dem GameBoard, field für
     * den Dateinamen/die Bezeichnung des GameFields und Orieantation der in
     * COURSE_MANUAL hinterlegten Orientierung des Fields auf dem spezifischen
     * GameBoard. Für das Drehen wird das GameField durch den FieldLoader
     * eingelesen und die entsprechenden Drehungen veranlasst.
     */
    private Field turn(int yPosition, String field, String orientation) {
        Field twist = fieldLoader.getField(new Point(0, yPosition), field);
        switch (orientation) {
        case "EAST":
            return turn(twist, true);
        case "SOUTH":
            return turn(twist, false);
        case "WEST":
            return turn(turn(twist, true), false);
        default:
            LOGGER.error("No direction found");
        }
        return null;
    }

    /*
     * Die Methode dreht ein übergebenes Field nach rechts und passt wenn
     * notwendig die Orientierung von Komponenten (Wall, ConveyorBelt) an.
     */
    private Field turn(final Field field, boolean turnRight) {
        Map<Point, Tile> tiles = field.getTiles();
        List<Tile> turned = new ArrayList<>();
        Wall[] walls = new Wall[0];
        Tile oldTile = null;
        Point newPoint = null;
        Tile newTile = null;
        for (int yy = 0; yy < Field.DIMENSION; yy++) {
            for (int xx = 0; xx < Field.DIMENSION; xx++) {
                newPoint = new Point(xx, yy);
                oldTile = tiles.get(newPoint);
                if(oldTile.getFieldObject() instanceof Flag){
                    oldTile.setFieldObject(null);
                }
                if (turnRight) {
                    newPoint.move(yy, Field.DIMENSION - 1 - xx);
                } else {
                    newPoint.move(Field.DIMENSION - xx - 1, Field.DIMENSION - yy - 1);
                }
                if (!oldTile.getWalls().isEmpty()) {
                    walls = turnWalls(oldTile.getWalls(), turnRight);
                }
                newTile = new GameTile(gameId,newPoint, walls);
                newTile.setFieldObject(turnFieldObject(oldTile.getFieldObject(), turnRight));
                turned.add(newTile);
                walls = new Wall[0];
            }
        }
        return new GameField((Point) field.getCoordinates().clone(), turned.toArray(new Tile[turned.size()]));
    }

    /*
     * Hilfsmethode zum Drehen von GameFields. Die Methode dreht übergebene
     * Wände: true = Rechtsdrehung, false = backflip
     */
    private Wall[] turnWalls(List<Wall> oldWalls, boolean turnRight) {
        List<Wall> walls = new ArrayList<>();
        GameWall wall = null;
        for (Wall ww : oldWalls) {
            wall = turnRight
                    ? new GameWall(Orientation.getNextDirection(ww.getOrientation(), false), ww.getPusherPhases())
                    : new GameWall(Orientation.getOppositeDirection(ww.getOrientation()), ww.getPusherPhases());
            if (ww.hasLaser() > 0) { // Laser setzen falls vorhanden
                for (int ll = 0; ll < ww.hasLaser(); ll++) {
                    wall.setLaser();
                }
            }
            walls.add(wall);
        }
        return walls.toArray(new Wall[walls.size()]);
    }

    /*
     * Hilfsmethode zum Drehen von GameFields. Die Methode passt die
     * Orientierung eines ConveyorBelts der Drehung entsprechend an.
     */
    private ConveyorBelt turnConveyorBelt(ConveyorBelt oldBelt, boolean turnRight) {
        if (turnRight) {
            return new GameConveyorBelt(gameId,Orientation.getNextDirection(oldBelt.getOrientation(), false),
                    oldBelt.isExpress());
        } else {
            return new GameConveyorBelt(gameId,Orientation.getOppositeDirection(oldBelt.getOrientation()),
                    oldBelt.isExpress());
        }
    }

    /*
     * Hilfsmethode, die unmittelbar vor der Ausgab die Fahnen gemäß
     * COURSE_MANUAL auf den Fields setzt, ehe das GameBoard erzeugt wird.
     */
    private List<Field> setFlags(String board, List<Field> fields) {
        Point[] checkpoints = flags.get(board);
        for (int ii = 0; ii < checkpoints.length; ii++) {
            int xField = (int) checkpoints[ii].getX();
            int yField = (int) checkpoints[ii].getY();
            xField = (xField < 0) ? (xField - Field.DIMENSION) / Field.DIMENSION : xField / Field.DIMENSION;
            yField = yField / Field.DIMENSION + 1;
            for (Field field : fields) {
                if (field.getCoordinates().equals(new Point(xField, yField))) {
                    int yCoord = (int) checkpoints[ii].getY() % (Field.DIMENSION);
                    int xCoord = 0;
                    xCoord = (xField < 0) ? (int) checkpoints[ii].getX() % (Field.DIMENSION - 1) + 11
                            : (int) checkpoints[ii].getX() % (Field.DIMENSION);
                    field.getTiles().get(new Point(xCoord, yCoord)).setFieldObject(new GameFlag(ii + 1));
                }
            }
        }
        return fields;
    }

    /*
     * Die Hilfsmethode erzeugt für ein erhaltenes FieldObject eine neue Instanz
     * und dreht diese, sofern das FieldObject eine Orientierung besitzt.
     */
    private FieldObject turnFieldObject(FieldObject object, boolean turnRight) {
        if (object instanceof Hole) {
            return new GameHole();
        } else if (object instanceof Gear) {
            return new GameGear(((Gear) object).rotatesLeft());
        } else if (object instanceof Workshop) {
            return new GameWorkshop(((Workshop) object).isAdvancedWorkshop());
        } else if (object instanceof ConveyorBelt) {
            return turnConveyorBelt((GameConveyorBelt) object, turnRight);
        }
        return null;
    }
}