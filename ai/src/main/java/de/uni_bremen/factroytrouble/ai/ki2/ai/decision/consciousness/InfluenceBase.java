/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki2.ai.decision.consciousness;

import de.uni_bremen.factroytrouble.ai.ais.AIPlayer2;
import de.uni_bremen.factroytrouble.ai.ki2.api.decision.unconsciousness.MoodUnit;

/**
 * Über diese Klasse kann das UnconsciousnessUnit mit der aktuellen Stimmmung
 * das ConsciosnessUnit beeinflussen
 */
public class InfluenceBase {

    private MoodUnit mood;

    /**
     * Erstellt eine InfluenceBase, welche die verzögerte MoodUnit speichert und
     * verwaltet
     * 
     * @param ai
     *            Die KI-Instanz
     */
    public InfluenceBase(AIPlayer2 ai) {
        mood = ai.getUnconsc().getMood();
    }

    /**
     * Setter für die MoodUnit
     * 
     * @param mood
     *            Die Unit, die neu gepeichert werden soll
     */
    public void gimmeMood(MoodUnit mood) {
        this.mood = mood;
    }

    /**
     * Getter für die nicht aktuelle Moodunit
     * 
     * @return die nicht aktuelle Moodunit
     */
    public MoodUnit getMood() {
        return mood;
    }

}
