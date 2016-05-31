/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.editor.data;

/**
 * Art von Single Linked List, welche Wände und angeschlossene Objekt beschreiben kann
 *
 * @author Andre
 */
public class FieldData {

    private FieldObject fieldObject;
    private FieldData connectedTo;
    private Orientation orientation;

    /**
     * Gibt das aktuelle FeldObjekt (wand oder ein mit der Wand verbundenes Objekt) zurück
     * @return
     *      Das Feldobjekt
     */
    public FieldObject getFieldObject() {
        return fieldObject;
    }

    /**
     * Setzt das aktuelle FeldObjekt (wand oder ein mit der Wand verbundenes Objekt)
     *
     * @param fieldObject
     *      Das Feldobjekt
     */
    public void setFieldObject(FieldObject fieldObject) {
        this.fieldObject = fieldObject;
    }

    /**
     * Gibt angeschlossene FeldObjekte zurück
     *
     * @return
     *      Ein angeschlossenes Feldobjekt
     */
    public FieldData getConnectedTo() {
        return connectedTo;
    }

    /**
     * Schließt ein anderes Feldobjekt diesem an
     *
     * @param connectedTo
     *      Ein anzuschließendes Feldobjekt
     */
    public void setConnectedTo(FieldData connectedTo) {
        this.connectedTo = connectedTo;
    }

    /**
     * Gibt die Richtung des aktuellen Feldobjektes zurück
     *
     * @return
     *      Die Richtung
     */
    public Orientation getOrientation() {
        return orientation;
    }

    /**
     * Setzt die Richtung für das aktuelle Feldobjekt
     *
     * @param orientation
     *      Die Richtung
     */
    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }
}
