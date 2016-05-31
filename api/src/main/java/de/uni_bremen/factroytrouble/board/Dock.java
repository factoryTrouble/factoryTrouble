/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.board;

import de.uni_bremen.factroytrouble.gameobjects.Robot;

/**
 * Das Interface für das Dock. Das Dock ist ein Feld und erweitert somit das
 * Interface {@link Field}. Eine Dockingstation hat die Maße 4x12.
 * 
 * @author Ottmar, Thorben
 *
 */
public interface Dock extends Field {

    /**
     * Diese Konstante gibt die Anzahl der Felder in einer Reihe auf der kurzen
     * Seite dieses Feldes an.
     */
    public static final int SHORT_SIDE = 4;

    /**
     * Gibt die Startposition des übergebenen Roboters wieder.
     * 
     * @param roboter
     *            Der Roboter dessen Startposition gesucht wird.
     * @return Das Tile, auf welchem der Roboters das Spiel gestartet hat.
     */
    Tile getStartPosition(Robot robot);

    @Override
    Dock clone();

}
