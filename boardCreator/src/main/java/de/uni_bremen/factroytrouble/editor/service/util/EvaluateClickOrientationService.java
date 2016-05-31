/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.editor.service.util;

import de.uni_bremen.factroytrouble.editor.data.Orientation;
import org.springframework.stereotype.Service;

/**
 * Prüft die Richtung auf einen Tile, in die geklickt wurde
 *
 * @author Andre
 */
@Service
public class EvaluateClickOrientationService {

    /**
     * Prüft die Richtung auf einen Tile, in die geklickt wurde. Dabei wird von den Koordinaten und der größe des Tiles ausgegangen
     *
     * @param tileRelativeX
     *      Die relative zum Tile geklickte X Koordinate
     *
     * @param tileRelativeY
     *      Die relative zum Tile geklickte Y Koordinate
     *
     * @param clickableAreaSize
     *      Breite der Klickbaren Fläche in Pixel
     *
     * @param clickBorder
     *      Aktiv klickbare Fläche an einer Seite
     *
     * @return
     *      Richtung, in die geklickt wurde oder null, falls ausserhalb der Aktiven Fläche geklickt wurde
     */
    public Orientation evaluate(Double tileRelativeX, Double tileRelativeY, Integer clickableAreaSize, Double clickBorder) {
        if(isInDeadRegion(tileRelativeX, tileRelativeY, clickableAreaSize, clickBorder)) {
            return null;
        }
        if(tileRelativeX < clickBorder && isInDiagonalArea(tileRelativeX, tileRelativeY, clickableAreaSize)) {
            return Orientation.WEST;
        }
        if(tileRelativeX > clickableAreaSize - clickBorder && isInDiagonalAreaBackside(tileRelativeX, tileRelativeY, clickableAreaSize)) {
            return Orientation.EAST;
        }
        if(tileRelativeY < clickBorder && isInDiagonalArea(tileRelativeY, tileRelativeX, clickableAreaSize)) {
            return Orientation.NORTH;
        }
        if(tileRelativeY > clickableAreaSize - clickBorder && isInDiagonalAreaBackside(tileRelativeY, tileRelativeX, clickableAreaSize)) {
            return Orientation.SOUTH;
        }
        return null;
    }

    private boolean isInDiagonalArea(Double otherDimension, Double ownValue, Integer clickableAreaSize) {
        return ownValue > otherDimension && ownValue < clickableAreaSize - otherDimension;
    }

    private boolean isInDiagonalAreaBackside(Double otherDimension, Double ownValue, Integer clickableAreaSize) {
        return ownValue < otherDimension && ownValue > clickableAreaSize - otherDimension;
    }

    private boolean isInDeadRegion(Double tileRelativeX, Double tileRelativeY, Integer clickableAreaSize, Double clickBorder) {
        return tileRelativeX > clickBorder && tileRelativeX < clickableAreaSize - clickBorder && tileRelativeY > clickBorder && tileRelativeY < clickableAreaSize - clickBorder;
    }

}
