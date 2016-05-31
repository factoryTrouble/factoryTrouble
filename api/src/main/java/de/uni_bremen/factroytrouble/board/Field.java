/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.board;

import java.awt.Point;
import java.util.Map;

/**
 * Das Interface repräsentiert ein Teil des Spielfeldes mit der der Größe 12*12
 * 
 * @author Ottmar, Artur, Thorben
 *
 */
public interface Field {

    /**
     * Größe des Spielfeldes
     */
    public static final int DIMENSION = 12;

    Map<Point, Tile> getTiles();

    Point getCoordinates();

    boolean hasTile(Tile tile);

    Field clone();

}
