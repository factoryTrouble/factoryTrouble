/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki2.api.planning;

/**
 * Interface für eine Strategie. Jede Strategie wird hardgecodet und wird sich
 * in den jeweiligen Werten unterscheiden.
 * 
 * getter für Prioritäten - gegner töten - Flaggen schnell erreichen - Gegner
 * vermeiden - Überleben - ...
 * 
 * @author Artur
 *
 */
public interface Strategy {

    /**
     * Ein Wert zwischen 0 und 10, der die Priorität zum töten anderer Roboter
     * liefert. 0 ist der niedrigste Wert und 10 der höchste.
     * 
     * @return, Priorität zum töten von Gegnern
     */
    int getKillPriority();

    /**
     * Ein Wert zwischen 0 und 10, der die Priorität zum schnellen erreichen von
     * Flaggen liefert. 0 ist der niedrigste Wert und 10 der höchste.
     * 
     * @return, Priorität zum schnellen erreichen von Flaggen
     */
    int getReachFlagsPriority();

    /**
     * Ein Wert zwischen 0 und 10, der die Priorität zum meiden von Gegnern
     * liefert. 0 ist der niedrigste Wert und 10 der höchste.
     * 
     * @return, Priorität zum meiden von Gegnern
     */
    int getAvoidEnemyPriority();

    /**
     * Ein Wert zwischen 0 und 10, der die Priorität überleben liefert. 0 ist
     * der niedrigste Wert und 10 der höchste.
     * 
     * @return, Priorität überleben
     */
    int getSurvivePriority();

    /**
     * Ein Wert zwischen 0 und 10, der die Risikobereitschaft liefert. 0 ist der
     * niedrigste Wert und 10 der höchste.
     * 
     * @return, Höhe der Risikobereitschaft
     */
    int getRiskPriority();

}
