/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki2.api.decision.unconsciousness;

import java.util.List;

import de.uni_bremen.factroytrouble.ai.ki2.api.decision.consciousness.Approach;
import de.uni_bremen.factroytrouble.ai.ki2.api.memory.Thought;

public interface UnconsciousnessUnit extends Runnable {

    /**
     * Verwendet {@link Thought} und {@link Personality} um das Bewustsein zu
     * beeinflussen
     */
    void influenceConsciousness();

    /**
     * Gibt den spezifisch angefragten Gedanken zurück
     * 
     * @param keys,
     *            Suchbegriffe (Anfrage) die in der Erinnerung gesucht werden
     * @return Die angefragte Erinnerung
     */
    Thought getInformation(List<String> keys);

    /**
     * Gibt den spezifisch angefragten Gedanken zurück
     * 
     * @param key,
     *            Anfrage (eigene Datenstruktur) die in der Erinnerung gesucht
     *            wird
     * @return Die angefragte Erinnerung
     */
    Thought getInformation(Thought key);;

    /**
     * Gibt die {@link MoodUnit} zurück
     * 
     * @return die {@link MoodUnit}
     */
    MoodUnit getMood();

    /**
     * Verändert die Stimmung in der {@link MoodUnit} anhand der Persönlichkeit
     * und den Schaden den man bekommen hat und weitere Einflüsse.
     * 
     * @param approach
     *            Der mögliche Spielzug, der die Stimmung verändert
     * @param first
     *            Ist dies der erste Approach in dieser Runde?
     */
    void changeMood(Approach approach, boolean firstApproach);

}
