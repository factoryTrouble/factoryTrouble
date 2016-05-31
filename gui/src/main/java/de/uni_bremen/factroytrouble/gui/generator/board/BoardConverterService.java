/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gui.generator.board;

import de.uni_bremen.factroytrouble.board.Field;
import de.uni_bremen.factroytrouble.board.Tile;
import org.springframework.stereotype.Service;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

/**
 * Konvertiert das Board aus der Spielmechanik in ein GUI verarbeitbares Format
 *
 * @author Andre und Johannes
 */
@Service
public class BoardConverterService {

    private Integer maxX;
    private Integer maxY;
    private Integer minX;
    private Integer minY;
    private Map<Point, Tile> tileMap;

    /**
     * Convertiert die Map von Feldern in eine Map mit allen Tiles, mit den
     * Absoluten Coordinaten als Key
     *
     * @param fields Alle Feld in Form einer Map
     * @return Eine Map aller Tiles mit ihren absoluten Coordinaten als key
     */
    public Map<Point, Tile> convertBoardToMap(Map<Point, Field> fields) {
        maxX = 0;
        maxY = 0;
        minX = 0;
        minY = 0;
        tileMap = new HashMap<>();
        fields.values().forEach(field -> {
            addAllTilesToMap(field.getTiles());
        });
        return tileMap;
    }

    /**
     * Diese Methode fügt alle Tiles eines Feldes zur tileMap hinzu, wobei die
     * max und min Coordinaten gefunden werden und die Positionen in Absolute Positionen
     * umgerechnet werdeniein Feld
     *
     * @param map Die Tile Map eines Feldes
     */
    private void addAllTilesToMap(Map<Point, Tile> map) {
        map.values().forEach(tile -> {
            Point absolutePoint = tile.getAbsoluteCoordinates();
            if (maxX < absolutePoint.x)
                maxX = absolutePoint.x;
            if (maxY < absolutePoint.y)
                maxY = absolutePoint.y;
            if (minX > absolutePoint.x)
                minX = absolutePoint.x;
            if (minY > absolutePoint.y)
                minY = absolutePoint.y;
            tileMap.put(absolutePoint, tile);
        });
    }

    /**
     * Der Maximale Ausschlag in X Richtung
     *
     * @return Das Maximale X
     */
    public Integer getMaxX() {
        return maxX;
    }

    /**
     * Der Maximale Ausschlag in Y Richtung
     *
     * @return Das Maximale Y
     */
    public Integer getMaxY() {
        return maxY;
    }

    /**
     * Der Minimale Ausschlag in X Richtung
     *
     * @return Das Minimale X
     */
    public Integer getMinX() {
        return minX;
    }

    /**
     * Der Minimale Ausschlag in Y Richtung
     *
     * @return Das Minimale Y
     */
    public Integer getMinY() {
        return minY;
    }
}
