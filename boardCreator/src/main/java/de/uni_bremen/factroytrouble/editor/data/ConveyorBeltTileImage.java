/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.editor.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Beschreibung eines Förderbandes
 *
 * @author Andre
 */
public class ConveyorBeltTileImage {

    private Orientation outputOrientation;
    private List<Orientation> inputOrientations = new ArrayList<>();
    private boolean express;

    /**
     * Erzeugt eine leere Beschreibung
     */
    public ConveyorBeltTileImage() {

    }

    /**
     * Erzeugt eine Beschreibung mit alle beschreibungsrelvanten Parametern
     * @param outputOrientation
     *      In welche Richtung führt das Förderband
     * @param express
     *      ist das Förderband ein Express-Förderband
     * @param inputOrientations
     *      Aus welchen Richtungen kommen andere Förderbänder
     */
    public ConveyorBeltTileImage(Orientation outputOrientation, boolean express, Orientation... inputOrientations) {
        this.outputOrientation = outputOrientation;
        this.express = express;
        for(Orientation orientation : inputOrientations) {
            this.inputOrientations.add(orientation);
        }
    }

    /**
     * Gibt die Richtung, in die das Förderband zeigt zurück
     *
     * @return
     *      Wohin zeigt da Förderband
     */
    public Orientation getOutputOrientation() {
        return outputOrientation;
    }

    /**
     * Setzt die Richtung, in die das Förderband zeigt
     *
     * @param outputOrientation
     *      Wohin zeigt da Förderband
     */
    public void setOutputOrientation(Orientation outputOrientation) {
        this.outputOrientation = outputOrientation;
    }

    /**
     * Gibt eine Liste alle Richtungen zurück, aus der benachbarte Förderbänder kommen und die auf das aktuelle Förderband zeigen
     * @return
     *      Liste alle Eingangsrichtungen
     */
    public List<Orientation> getInputOrientations() {
        return inputOrientations;
    }

    /**
     * Handelt es sich um ein Express-Förderband
     *
     * @return
     *      True = Ja, sonst False
     */
    public boolean isExpress() {
        return express;
    }

    /**
     * Handelt es sich um ein Express-Förderband
     *
     * @param express
     *      True = Ja, sonst False
     */
    public void setExpress(boolean express) {
        this.express = express;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConveyorBeltTileImage that = (ConveyorBeltTileImage) o;

        if (express != that.express) return false;
        if (outputOrientation != that.outputOrientation) return false;
        if (that.getInputOrientations().size() != inputOrientations.size()) return false;
        for(Orientation orientation : that.getInputOrientations()) {
            if(!inputOrientations.contains(orientation)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = outputOrientation != null ? outputOrientation.hashCode() : 0;
        result = 31 * result + (express ? 1 : 0);
        int inputOrientationHashCode = 0;
        for(Orientation orientation : inputOrientations) {
            inputOrientationHashCode = inputOrientationHashCode + orientation.hashCode();
        }
        result = 31 * result + inputOrientationHashCode;
        return result;
    }
}

