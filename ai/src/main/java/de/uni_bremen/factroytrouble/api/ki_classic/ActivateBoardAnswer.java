/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.api.ki_classic;

import de.uni_bremen.factroytrouble.gameobjects.Orientation;

import java.awt.*;

/**
 * Ein Datenobjekt, welches die Anwort veine aktivierten Boards hält
 *
 * @author Andre
 */
public class ActivateBoardAnswer {

    private Point point;
    private Orientation orientation;
    private boolean flagReached;

    public ActivateBoardAnswer(Point point, Orientation orientation, boolean flagReached) {
        this.point = point;
        this.orientation = orientation;
        this.flagReached = flagReached;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public boolean isFlagReached() {
        return flagReached;
    }

    public void setFlagReached(boolean flagReached) {
        this.flagReached = flagReached;
    }
}
