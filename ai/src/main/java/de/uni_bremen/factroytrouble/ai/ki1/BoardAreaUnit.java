/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki1;

import java.awt.Point;
import java.util.Map;

import de.uni_bremen.factroytrouble.api.ki1.BoardArea;
import de.uni_bremen.factroytrouble.board.Tile;

/**
 * Repräsentiert einen quadratischen Bereich eines Spielfeldes.
 * 
 * @author tim Pablo
 */
public class BoardAreaUnit implements BoardArea {
    /**
     * Mittelpunkt
     */
    private Point center;
    /**
     * Im Bereich liegende {@link Tile}s
     */
    private Map<Point, Tile> tiles;

    /**
     * Erstellt eine neue BoardArea.
     * 
     * @param center
     *            Mittelpunkt
     * @param tiles
     *            Im Bereich liegende {@link Tile}
     */
    public BoardAreaUnit(Point center, Map<Point, Tile> tiles) {
        this.center = center;
        this.tiles = tiles;
    }

    /**
     * Liefert den Mittelpunkt.
     * 
     * @return Mittelpunkt
     */
    @Override
    public Point getCenter() {
        return center;
    }

    /**
     * Liefert im Bereich liegende {@link Tile}s.
     * 
     * @return im Bereich liegende {@link Tile}s
     */
    @Override
    public Map<Point, Tile> getTiles() {
        return tiles;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((center == null) ? 0 : center.hashCode());
        result = prime * result + ((tiles == null) ? 0 : tiles.hashCode());
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
        BoardArea other = (BoardArea) obj;
        if (center == null) {
            if (other.getCenter() != null)
                return false;
        } else if (!center.equals(other.getCenter()))
            return false;
        if (tiles == null) {
            if (other.getTiles() != null)
                return false;
        } else if (!tiles.equals(other.getTiles()))
            return false;
        return true;
    }
    
    

}
