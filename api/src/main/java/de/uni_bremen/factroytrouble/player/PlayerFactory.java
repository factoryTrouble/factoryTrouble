/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.player;

/**
 * Erstellt eine spezielle Implementierung eines @link{Player}
 *
 * Diese Klasse soll als Spring Bean verwendet werden können und benötigt daher
 * eine @link{\@Component} Annotation
 *
 * @author Andre
 *
 */
public interface PlayerFactory {

    /**
     * Erstellt einen neuen Player
     *
     * @param robot
     *            Der Roboter des Spielers
     * 
     *            kiName Name der KI. Wenn es menschlicher Spieler ist, dann
     *            bitte "MENSCH" übergeben.
     *
     * @return Ein neuer Spieler
     */
    Player createNewPlayer(int gameId, String robotName, String kiName);

}
