/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki2.ai.memory;

import java.awt.Point;

/**
 * Hilfsklasse für die Plannung. Zeigt ein Punkt und eine Richtung nach
 * Ausführung einer CB-Bewegung.
 * 
 * @author Artur
 *
 */
public class ScullyConveyorInfo {
    private Point pointAfterConveyor;
    private int direction;

    public ScullyConveyorInfo(Point pPoint, int pDirection) {
        setPointAfterConveyor(pPoint);
        setDirection(pDirection);
    }

    public Point getPointAfterConveyor() {
        return pointAfterConveyor;
    }

    public void setPointAfterConveyor(Point pointAfterConveyor) {
        this.pointAfterConveyor = pointAfterConveyor;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return pointAfterConveyor + ", " + direction;
    }

}
