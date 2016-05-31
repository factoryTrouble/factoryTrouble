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
import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Stefan
 * 
 *         FieldLoader liest GameFields und Docks ein, die in einer Textdatei
 *         kodiert sind, erzeugt die benötigten GameTiles und FieldObjects und
 *         hält diese vor. Auf Anfrage werden GameField-Instanzen erzeugt und
 *         zurück gegeben. Kodierung:
 * 
 *         GameObjects Orientation Options Startpunkte =========== ===========
 *         ======= =========== ConveyorBelt -> be Orientation.WEST -> we
 *         isExpress -> ex Punkt 1 -> s1 Workshop -> ws Orientation.NORTH -> no
 *         isAdvanced -> ad Punkt 2 -> s2 gear -> ge Orientation.EAST -> ea
 *         rotatesLeft -> rl ... -> ... hole -> ho Orientation.SOUTH -> so Punkt
 *         MAX_PLAYERS -> sMAX_PLAYERS tile -> ti
 * 
 *         Wall ==== Orientation.WEST -> wwe Orientation.NORTH -> wno
 *         Orientation.EAST -> wea Orientation.SOUTH -> wso Laser -> l (pro
 *         Laser ein l) Pusher -> p1-MAX_REGISTERS (Maximal 1 Pusher pro Tile.
 *         Laser und Pusher schließen sich gegenseitig aus)
 * 
 *         Grundlegender Aufbau: GameObject_Orientation_Option_Wall_Start
 *         Beispiele: Tile mit Wall im Westen und Osten: -> ti_wwe_wea Tile mit
 *         Wall im Norden und 2 Lasern: -> ti_wnoll Tile mit Wall im Süden und
 *         Pusher, aktiv in Registerphase 2 -> ti_wsop2 Express ConveyorBelt
 *         nach Osten mit Wall im Norden: -> be_ea_ex_wno ConveyorBelt nach
 *         Westen ohne Wall -> be_we ...
 */
public class FieldLoader {

    private static final Logger LOGGER = Logger.getLogger(FieldLoader.class);
    private final int gameId;

    private BufferedReader lineReader;

    private Map<String, Field> fields;
    private Map<String, Dock> docks;

    private Map<String, Point[]> startDocks;
    private List<Tile> tiles;
    private Point[] startPoints;

    FieldLoader(int gameId) {
        this.gameId = gameId;
        init();
    }

    public void init() {
        fields = new HashMap<>();
        docks = new HashMap<>();
        startDocks = new HashMap<>();
        loadFields();
    }

    protected void loadFields() {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] classpathResources = null;
        Resource[] userHomeResources = null;
        int row = -1;
        try {
            classpathResources = resolver.getResources("classpath*:fields/*");
            userHomeResources = resolver.getResources("file:" + System.getProperty("user.home") + File.separator + "factoryTrouble" + File.separator + "boards" + File.separator + "descriptions/*");
        } catch (IOException e1) {
            LOGGER.error("Der angegebene Pfad für die Feld-Dateien ist falsch", e1);
        }

        for (Resource resource : ArrayUtils.addAll(classpathResources, userHomeResources)) {
            String fileName = null;
            tiles = new ArrayList<>();
            startPoints = new Point[Master.MAX_PLAYERS];
            String line = "";
            row = 0;
            try {
                fileName = resource.getFilename();
                if(fileName.contains(".DS_Store")) {
                    continue;
                }
                lineReader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
                while ((line = lineReader.readLine()) != null) {
                    createLine(row, line.split(","));
                    row++;
                }
            } catch (IOException e1) {
                LOGGER.error("Die Datei oder Zeile zur angegebenen Ressource konnte nicht gefunden werden", e1);
            } finally {
                try {
                    lineReader.close();
                } catch (IOException e) {
                    LOGGER.error("Fehler beim Schließen des BufferedReaders", e);
                }
            }
            if (fileName.contains("DOCK")) {
                docks.put(fileName, new GameDock(new Point(42, 42), tiles.toArray(new Tile[tiles.size()])));
                startDocks.put(fileName, startPoints);
            } else {
                fields.put(fileName, new GameField(new Point(42, 42), tiles.toArray(new Tile[tiles.size()])));
            }
        }
    }

    /**
     * @author Stefan
     * 
     *         Die Methode erzeugt eine GameField-Instanz basierend auf den
     *         Initial eingelesenen Kodierungen.
     * 
     * @param point
     *            Position des GameFields auf dem GameBoard.
     * @param field
     *            Schlüssel zur Erzeugung eines GameField.
     * @return Angeforderte GameField-Instanz.
     */
    protected Field getField(Point point, String field) {
        if (!fields.keySet().contains(field) && !docks.keySet().contains(field)) {
            LOGGER.error("Das Field ".concat(field).concat(" existiert nicht!"));
            return null;
        }
        if (fields.keySet().contains(field)) {
            return new GameField(point,
                    tileMapConvert(fields.get(field).clone().getTiles(), Field.DIMENSION, Field.DIMENSION));

        } else {
            return new GameDock(point,
                    tileMapConvert(docks.get(field).clone().getTiles(), Dock.DIMENSION, Dock.SHORT_SIDE));
        }
    }

    protected String[] getAvailableFields() {
        return fields.keySet().toArray(new String[fields.keySet().size()]);
    }

    protected String[] getAvailableDocks() {
        return docks.keySet().toArray(new String[docks.keySet().size()]);
    }

    protected Point[] getStartPoints(String dock) {
        if (!docks.keySet().contains(dock)) {
            LOGGER.error("Das Dock ".concat(dock).concat(" existiert nicht!"));
            return new Point[] {};
        }
        return startDocks.get(dock);
    }

    /*
     * Hilfsmethode für die Rückgabe geklonter Fields und Docks. Die Übergebene
     * Map wird in ein Array übertragen, dessen Form derjenigen beim Einlesen
     * von Fields entspricht.
     */
    private Tile[] tileMapConvert(Map<Point, Tile> tiles, int xDimension, int yDimension) {
        int tileIndex = 0;
        Tile[] converted = new Tile[tiles.size()];
        for (int yy = 0; yy < yDimension; yy++) {
            for (int xx = 0; xx < xDimension; xx++) {
                converted[tileIndex] = tiles.get(new Point(xx, yy));
                tileIndex++;
            }
        }
        return converted;
    }

    /**
     * @author Stefan
     * 
     * @param row
     *            Zeile der Textdatei und GameField
     * @param line
     *            eingelesene Kodierung einer GameField-Zeile
     */
    private void createLine(int row, String[] line) {
        Tile tile = null;
        for (int ii = 0; ii < line.length; ii++) {
            if (line[ii].contains("wwe") || line[ii].contains("wno") || line[ii].contains("wea")
                    || line[ii].contains("wso")) {
                tile = new GameTile(gameId,new Point(ii, row), createWalls(line[ii].split("_")));
            } else {
                tile = new GameTile(gameId,new Point(ii, row), new GameWall[] {});
            }
            if (line[ii].matches("[_a-z]{3,}s[1-8]")) {
                startPoints[Integer.parseInt(line[ii].substring(line[ii].length() - 1)) - 1] = new Point(ii, row);
            }
            tile.setFieldObject(createFieldObject(line[ii]));
            tiles.add(tile);
        }
    }

    /**
     * @author Stefan
     * 
     * @param objWalls
     *            Tile mit oder ohne FieldObject, welches mit Wänden versehen
     *            ist
     * @return GameWall-Array zur Erzeugung eines GameTile
     */
    private Wall[] createWalls(String[] objWalls) {
        List<Wall> tileWalls = new ArrayList<>();
        Orientation orientation = Orientation.NORTH;
        for (String opt : objWalls) {
            if (opt.length() > 2) {
                switch (opt.substring(0, 3)) {
                case "wwe":
                    orientation = Orientation.WEST;
                    break;
                case "wea":
                    orientation = Orientation.EAST;
                    break;
                case "wso":
                    orientation = Orientation.SOUTH;
                    break;
                default:
                }
                tileWalls.add(wallAttachment(opt.substring(3), orientation));
            }
            orientation = Orientation.NORTH;
        }
        return tileWalls.toArray(new Wall[tileWalls.size()]);
    }

    /*
     * Hilsmethode für createWalls: Pusher oder Laser anhängen
     */
    private Wall wallAttachment(String attach, Orientation orient) {
        if (attach.length() > 0 && attach.matches("p[1-" + Robot.MAX_REGISTERS + "]+?")) {
            int[] pusher = new int[attach.length() - 1];
            for (int ii = 0; ii < attach.substring(1).length(); ii++) {
                pusher[ii] = Character.getNumericValue(attach.substring(1).charAt(ii));
            }
            return new GameWall(orient, pusher);
        } else if (attach.length() > 0 && attach.matches("l+")) { // Laser
                                                                  // setzen
            GameWall wall = new GameWall(orient);
            for (int ll = attach.length(); ll > 0; ll--) {
                wall.setLaser();
            }
            return wall;
        }
        return new GameWall(orient);
    }

    /**
     * @author Stefan
     * 
     * @param fieldObject
     *            In Textdatei kodiertes FieldObject, z.B. be_ea_ex für
     *            GameConveyorBelt mit Orientation = East und isExpress = true
     * @return FieldObject, das auf einem GameTile zu platzieren ist
     */
    private FieldObject createFieldObject(String fieldObject) {
        String[] elem = fieldObject.split("_");
        switch (elem[0]) {
        case "be":
            return new GameConveyorBelt(gameId,getOrientation(elem[1]), checkOptions(elem, "ex"));
        case "ge":
            return new GameGear(checkOptions(elem, "rl"));
        case "ho":
            return new GameHole();
        case "ws":
            return new GameWorkshop(checkOptions(elem, "ad"));
        default:
            return null;
        }
    }

    /**
     * @author Stefan
     * 
     *         Prüft, ob eine Option (isExpress, ...) gesetzt ist.
     * 
     * @param elements
     *            Kodierung einer Tile
     * @param regex
     *            Zu prüfende Option
     * @return
     */
    private boolean checkOptions(String[] elements, String regex) {
        for (String option : elements) {
            if (option.matches(regex)) {
                return true;
            }
        }
        return false;
    }

    private Orientation getOrientation(String orientation) {
        switch (orientation) {
        case "we":
            return Orientation.WEST;
        case "no":
            return Orientation.NORTH;
        case "ea":
            return Orientation.EAST;
        case "so":
            return Orientation.SOUTH;
        default:
            return null;
        }
    }
}