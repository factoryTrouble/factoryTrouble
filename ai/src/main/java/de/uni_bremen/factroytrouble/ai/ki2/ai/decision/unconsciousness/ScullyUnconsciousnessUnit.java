/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki2.ai.decision.unconsciousness;

import java.util.List;
import java.util.Random;

import de.uni_bremen.factroytrouble.ai.ais.AIPlayer2;
import de.uni_bremen.factroytrouble.ai.ki2.ai.decision.consciousness.InfluenceBase;
import de.uni_bremen.factroytrouble.ai.ki2.api.decision.consciousness.Approach;
import de.uni_bremen.factroytrouble.ai.ki2.api.decision.unconsciousness.MoodUnit;
import de.uni_bremen.factroytrouble.ai.ki2.api.decision.unconsciousness.Personality;
import de.uni_bremen.factroytrouble.ai.ki2.api.decision.unconsciousness.UnconsciousnessUnit;
import de.uni_bremen.factroytrouble.ai.ki2.api.memory.Thought;
import de.uni_bremen.factroytrouble.gameobjects.Robot;

public class ScullyUnconsciousnessUnit implements UnconsciousnessUnit {

    private MoodUnit mood;
    private AIPlayer2 player;
    private InfluenceBase influence;

    public ScullyUnconsciousnessUnit(Robot robot, AIPlayer2 pPlayer) {
        player = pPlayer;
        mood = new ScullyMoodUnit(robot, pPlayer);
    }

    /**
     * Konstruktor für Tests
     * 
     * @param robot
     *            Der die Instanz des Robters
     * @param personality
     *            Die Instanz der Persönlichkeit
     */
    public ScullyUnconsciousnessUnit(Robot robot, AIPlayer2 pPlayer, Personality personality, int seedForRandom) {
        player = pPlayer;
        mood = new ScullyMoodUnit(robot, personality, new Random(seedForRandom));
    }

    @Override
    public void run() {
        /*
         * Not done Yet
         */
    }

    @Override
    public void changeMood(Approach approach, boolean firstApproach) {
        mood.updateMood(approach, firstApproach);
    }

    @Override
    public void influenceConsciousness() {
        if (influence == null) {
            influence = player.getConsc().getInfluence();
        }
        influence.gimmeMood(mood);
    }

    @Override
    public Thought getInformation(List<String> keys) {
        return player.getWorkM().getInformation(keys);
    }

    @Override
    public Thought getInformation(Thought key) {
        return player.getWorkM().getInformation(key);
    }

    @Override
    public MoodUnit getMood() {
        return mood;
    }

}
