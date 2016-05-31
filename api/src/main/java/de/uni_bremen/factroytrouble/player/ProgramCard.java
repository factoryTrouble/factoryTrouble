/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.player;

import de.uni_bremen.factroytrouble.gameobjects.Robot;

/**
 * Das Interface für Programmkarten. Gibt an wie der Roboter sich bewegen soll.
 * 
 * @author Thorben
 */
public interface ProgramCard {

    /**
     * Führt die jeweilige Aktion dieses Kartentyps bei einem Roboter aus
     */
    void execute(Robot robot);

    int getPriority();

    /**
     * Die Range von move-karten wird zurückgegeben; die Range von Backup sollte
     * -1, die von Turn-Karten 0 betragen
     * 
     * @return die Range der jeweiligen Karte
     */
    int getRange();

}
