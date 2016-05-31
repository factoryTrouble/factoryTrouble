/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki2.api.memory;

public interface LongTermMemory extends Memory {
    /**
     * Stärkt die Verbindung zwischen zwei Erinnerungen, wenn eine der
     * Erinnerungen aufgerufen wird
     * 
     * @param thought1,
     *            der aufgerufene Gedanke
     * @param thought2,
     *            der assoziierte Gedanke
     * @param factor,
     *            der Wert um den die Assoziation gestärkt wird
     *
     * @throws IllegalArgumentException
     */
    void power(Thought thought1, Thought thought2, int factor) throws IllegalArgumentException;

    /**
     * Schwächst die Verbindung zwischen zwei Gedanken
     * 
     * @param thought1,
     *            erster Gedanke
     * @param thought2,
     *            zweiter Gedanke
     * @param factor,
     *            der Wert um den die Assoziation geschwächt wird
     */
    void weakness(Thought thought1, Thought thought2, int factor);
    

    
    /*
     * Ermöglicht dem Ticker der Memory mitzuteilen, dass Zeit vergangen ist.
     */
    void tickle();
}
