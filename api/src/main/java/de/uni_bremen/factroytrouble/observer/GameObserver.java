/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.observer;

import de.uni_bremen.factroytrouble.gameobjects.Robot;

/**
 * Observer für den GameMaster, zu nutzen von der GUI und KI
 * 
 * @author Thorben
 */
public interface GameObserver {

    /**
     * Wird von der SPM aufgerufen, wenn eine Veränderung vom Spielzustand
     * auftritt; von der GUI und ggf. KI zu implementieren, um darauf angemessen
     * zu reagieren
     * 
     * @param robot
     *            Der Roboter, der sich verändert hat
     * @param event
     *            Die Art der Veränderung
     * @param source
     *            Der Ursprung der Veränderung, sofern wichtig (ansonsten null)
     */
    void spam(Robot robot, Event event, Object source);

}
