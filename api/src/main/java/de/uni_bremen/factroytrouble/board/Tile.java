/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.board;

import java.awt.Point;
import java.util.List;
import java.util.Map;

import de.uni_bremen.factroytrouble.gameobjects.FieldObject;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.gameobjects.Wall;

/**
 * Das Interface welches die Funktionalität für eine einzelne Kachel liefert.
 * 
 * @author Ottmar, Thorben
 *
 */
public interface Tile {

    /**
     * Führt eine nach Kacheltyp abhängige Aktion mit dem auf dieser Kachel
     * befindlichen Roboter (falls vorhanden) aus, z.B. drehen, weiterbewegen
     * oder Schaden verursachen
     */
    void action();

    void setFieldObject(FieldObject object);

    void removeFieldObject();

    FieldObject getFieldObject();

    void setRobot(Robot robot);

    Robot getRobot();

    List<Wall> getWalls();

    Wall getWall(Orientation direction);
    
    boolean hasWall(Orientation direction);

    Point getCoordinates();

    Tile clone();

    /**
     * Gibt die absoluten Koordinaten des Tiles auf dem Brett zurück oder null,
     * wenn es sich nicht auf dem Brett befindet
     * 
     * @return die Absoluten Koordinaten des Tiles auf dem Board
     */
    Point getAbsoluteCoordinates();

    /**
     * Gibt eine Map zurück, die jeder Himmelsrichtung das entsprechende
     * Nachbartile dieses Tiles zuordnet
     * 
     * @return eine Map mit den Nachbartiles
     */
    Map<Orientation, Tile> getNeighbors();

}
