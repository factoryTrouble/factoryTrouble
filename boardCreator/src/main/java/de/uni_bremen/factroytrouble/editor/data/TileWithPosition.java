/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.editor.data;

import de.uni_bremen.factroytrouble.editor.component.Tile;

import java.awt.*;

/**
 * Verbindet eine Tile mit einer Postion im Grid
 *
 * @author André
 */
public class TileWithPosition {
    private Tile tile;
    private Point point;

    /**
     * Erstellt eine neue, solche Verbindung
     * @param tile
     * @param point
     */
    public TileWithPosition(Tile tile, Point point) {
        this.tile = tile;
        this.point = point;
    }

    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }
}