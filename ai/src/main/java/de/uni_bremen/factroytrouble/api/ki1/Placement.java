/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.api.ki1;

import java.awt.Point;

import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;

/**
 * Repräsentiert die Lage eines Objektes auf dem {@link Board}. Vereint Position
 * und Orientierung.
 * 
 * @author Roland, Tim
 *
 */
public interface Placement {
    
    public Point getPosition();
    
    public void setPosition(Point position);
    
    public Orientation getOrientation();
    
    public void setOrientation(Orientation orientation) ;
    
    public Tile getTile();
    
    public void setTile(Tile tile);
    
    @Override
    public int hashCode();
    
    @Override
    public boolean equals(Object obj);
    
    @Override
    public String toString();
    
}
