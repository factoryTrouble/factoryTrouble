/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.api.ki1.memory;

import de.uni_bremen.factroytrouble.gameobjects.FieldObject;
import de.uni_bremen.factroytrouble.gameobjects.Robot;

/**
 * Interface für die KeyUnit welche Keys erstellt die gebraucht werden um 
 * Informationen im Memory zu speichern und zu finden.
 * 
 * @author Pablo
 */

public interface Key {

    public MemoryEvent getEvent();

    public Robot getRobot();

    public FieldObject getFieldObject();

    public Object getOther();

    /**
     * Gibt anhand der gesetzten Werte eine Klasse zurück.<br />
     * 1. Sind event und robot gesetzt, wird die Klasse {@link EventUnit}
     * zurückgebeben.<br />
     * 2. Ist event nicht gesetzt und robot gesetzt, wird die Klasse
     * {@link Robot} zurückgegeben.<br />
     * 3. Ist fieldObject nicht gesetzt und robot nicht gesetzt, wird
     * {@link FieldObject} zurückgegeben.<br />
     * 4. Trifft keiner dieser Fälle zu, wird die Klasse zu {@link getOther}()
     * zurückgebeben.
     */
    @SuppressWarnings("rawtypes")
    public Class getResultType();

    @Override
    public int hashCode();

    @Override
    public boolean equals(Object obj);

    @Override
    public String toString();
    
}
