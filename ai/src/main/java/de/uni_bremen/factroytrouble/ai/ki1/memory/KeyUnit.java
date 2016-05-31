/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki1.memory;

import de.uni_bremen.factroytrouble.api.ki1.memory.MemoryEvent;
import de.uni_bremen.factroytrouble.api.ki1.memory.Key;
import de.uni_bremen.factroytrouble.gameobjects.FieldObject;
import de.uni_bremen.factroytrouble.gameobjects.Robot;

public class KeyUnit implements Key{
    private RobotEvent event;
    private Robot robot;
    private FieldObject fieldObject;
    private Object other;

    /**
     * Setzt, abhängig vom Typ des Parameters, Attribute. Ist der Parameter ein
     * Event, so werden die Attribute {@code event} und {@code robot} gesetzt.
     * Ist der Parameter ein Robot, so wird das Attribut {@code robot} gesetzt.
     * Ist der Parameter vom Typ FieldObject, so wird das Attribut
     * {@code fieldObject} gesetzt. Trifft keiner dieser Fälle zu, so wird das
     * Attribut {@code other} gesetzt. Nicht-gesetzte Attribute werden mit
     * {@code null} initialisiert.
     */
    public KeyUnit(Object keyObject) {
        event = null;
        robot = null;
        fieldObject = null;
        if (keyObject instanceof RobotEvent) {
            this.event = (RobotEvent) keyObject;
            this.robot = this.event.getRobot();
        } else if (keyObject instanceof Robot) {
            this.robot = (Robot) keyObject;
        } else if (keyObject instanceof FieldObject) {
            this.fieldObject = (FieldObject) keyObject;
        } else {
            this.other = keyObject;
        }
    }

    @Override
    public MemoryEvent getEvent() {
        return event;
    }

    @Override
    public Robot getRobot() {
        return robot;
    }

    @Override
    public FieldObject getFieldObject() {
        return fieldObject;
    }
    @Override
    public Object getOther() {
        return other;
    }

    /**
     * Gibt anhand der gesetzten Werte eine Klasse zurück.<br />
     * 1. Sind event und robot gesetzt, wird die Klasse {@link RobotEvent}
     * zurückgebeben.<br />
     * 2. Ist event nicht gesetzt und robot gesetzt, wird die Klasse
     * {@link Robot} zurückgegeben.<br />
     * 3. Ist fieldObject nicht gesetzt und robot nicht gesetzt, wird
     * {@link FieldObject} zurückgegeben.<br />
     * 4. Trifft keiner dieser Fälle zu, wird die Klasse zu {@link getOther}()
     * zurückgebeben.
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Class getResultType() {
        if (event != null && robot != null) {
            return RobotEvent.class;
        } else if (event == null && robot != null) {
            return Robot.class;
        } else if (fieldObject != null && robot == null) {
            return FieldObject.class;
        } else {
            return other.getClass();
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((event == null) ? 0 : event.hashCode());
        result = prime * result + ((fieldObject == null) ? 0 : fieldObject.hashCode());
        result = prime * result + ((other == null) ? 0 : other.hashCode());
        result = prime * result + ((robot == null) ? 0 : robot.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        KeyUnit otherKeyUnit = (KeyUnit) obj;
        if (event == null) {
            if (otherKeyUnit.event != null)
                return false;
        } else if (!event.equals(otherKeyUnit.event))
            return false;
        if (fieldObject == null) {
            if (otherKeyUnit.fieldObject != null)
                return false;
        } else if (!fieldObject.equals(otherKeyUnit.fieldObject))
            return false;
        if (this.other == null) {
            if (otherKeyUnit.other != null)
                return false;
        } else if (!this.other.equals(otherKeyUnit.other))
            return false;
        if (robot == null) {
            if (otherKeyUnit.robot != null)
                return false;
        } else if (!robot.equals(otherKeyUnit.robot))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Key [event=" + event + ", robot=" + robot + ", fieldObject=" + fieldObject + ", other=" + other + "]";
    }
}