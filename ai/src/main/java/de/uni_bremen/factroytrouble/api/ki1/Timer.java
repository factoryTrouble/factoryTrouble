/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.api.ki1;

/**
 * Ein Timer führt Funktionen aus und sorgt ggf. dafür, dass die für Menschen
 * angedachte Ausführungszeit einer solchen Funktion eingehalten wird.
 * 
 * @author tim
 */
public interface Timer {

    /**
     * Einfaches funktionales Interface für getimete Funktionen.
     *
     * @param <T>
     *            Typ des Resultats
     */
    @FunctionalInterface
    interface Calculation<T> {
        T exec();
    }

    /**
     * Berechnet etwas, was ähnlich lange wie bei einem Menschen dauern soll.
     * 
     * @param func
     *            was berechnet werden soll
     * @param humanTime
     *            wie lange ein Mensch für die Berechnung brauchen würde
     * @param desc
     *            Beschreibung der Berechnung
     * @return das berechnete Ergebnis
     * @throws InterruptedException
     *             falls der Thread unterbrochen wird während er schläft
     */
    <T> T run(Calculation<T> func, int humanTime, String desc) throws InterruptedException;
}