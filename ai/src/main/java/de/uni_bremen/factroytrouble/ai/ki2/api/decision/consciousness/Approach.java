/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki2.api.decision.consciousness;

import de.uni_bremen.factroytrouble.gameobjects.FieldObject;
import de.uni_bremen.factroytrouble.gameobjects.Robot;

/**
 * Interface für eine Vorgehensweise. Eine Vorgehensweise kann von dem
 * Bewusstsein akzeptiert oder abgelehnt werden. Ob eine Vorgehensweise
 * angenommen oder abgelehnt wird entscheidet das Bewusstsein, welches von der
 * Persönlichkeit und der Stimmung beeinflusst wird.
 * 
 * @author Artur
 *
 */

/**
 * getter für Werte
 * 
 * - Anzahl an beachteten Kacheln - Anzahl an Gegnern in der Nähe - erwarteter
 * Schaden - zurückgelegte Distanz - entfernung zur Flagge - ...
 */
public interface Approach {

    /**
     * Gibt die Anzahl der beachteten Kacheln, die gebraucht werden um den Weg
     * auszurechnen.
     * 
     * @return, Anzahl der beachteten Kacheln
     */
    int getAmountOfNoticedTiles();

    /**
     * Gibt die Anzahl der Gegner die auf dem Weg zum Ziel zum Hinderniss werden
     * können. Ein Roboter ist ein Hinderniss fals er in einer Entfernung von 1
     * zum Pfad steht oder in die Richtung zielt
     * 
     * @return, Anzahl der gegnerischen Roboter, die zum Hindernis werden
     * können
     */
    int getAmountOfNearEnemies();

    /**
     * Gibt die Anzahl an möglichem Schaden auf einem Weg
     * 
     * @return, Anzahl an möglichem Schaden
     */
    int getExpectedDamage();

    /**
     * Entfernung zum gewünschten Feldobjekt, gezählt in der Menge an gelegten
     * Karten. Gibt -1 zurück wenn nicht erreichbar.
     * 
     * @param fieldObject,
     *            das zu erreichende Feldobjekt
     * 
     *            @return, Anzahl an gelegten Karten
     */
    int getDistanceInCards(FieldObject fieldObject);

    /**
     * Entefernung zum gewünschtem Feldobjekt, gezählt in der Anzahl an Kacheln
     * 
     * @param fieldObjcet,
     *            {@link FieldObject}, welches erreicht werden soll
     * 
     *            @return, Anzahl an Kacheln
     * 
     */
    int getDistanceToInTiles(FieldObject fieldObjcet);

    /**
     * Entfernung zum gewünschten Roboter, gezählt in der Menge an gelegten
     * Karten. Gibt -1 zurück wenn nicht erreichbar.
     * 
     * @param robot
     *            {@link Robot}, welcher erreicht werden soll
     * 
     * @return Anzahl an Karten, die gelegt werden müssen um den {@link Robot}
     *         zu erreichen
     */
    int getDistanceToRobotInCards(Robot robot);

    /**
     * Entfernung zum gewünschten Roboter, gezählt in Anzahl an Kacheln
     * 
     * 
     * @param robot
     *            {@link Robot}, welcher erreicht werden soll
     * 
     * @return Anzahl an Kacheln, die gegangen werden müssen um den
     *         {@link Robot} zu Erreichen
     */
    int getDistanceToRobotInTiles(Robot robot);

    /**
     * Gibt die Anzahl an Löchern die zum Hindernis werden können. Ein loch wird
     * zum hindernis, falls es sich in einer Entfernung von 1er Kachel vom weg
     * befindet.
     * 
     * @return, Anzahl an relevanten Kacheln
     */
    int getAmountOfHoles();

    /**
     * Gibt die Anzahl des Schadens durch Laser, die den Weg mit ihren Starhl
     * kreuzen
     * 
     * @return, Anzahl des Schadens durch Laser, die den Weg kreuzen
     */
    int getAmountOfLasers();

    /**
     * Gibt die Anzahl an Förderbändern, die sich auf dem Weg befinden
     * 
     * @return, Anzahl an Förderbändern auf dem Weg.
     */
    int getAmountOfConveyer();

    /**
     * Gibt an, ob mit diesem Approach die nächste Flagge erreicht werden kann
     * 
     * @return {@code true} wenn ja, sonst {@code false}
     */
    public boolean canReachNextFlag();

}
