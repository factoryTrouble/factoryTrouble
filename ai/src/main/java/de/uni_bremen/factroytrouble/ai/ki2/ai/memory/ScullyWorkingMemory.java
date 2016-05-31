/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki2.ai.memory;

import java.util.List;

import de.uni_bremen.factroytrouble.ai.ais.AIPlayer2;
import de.uni_bremen.factroytrouble.ai.ki2.api.memory.LongTermMemory;
import de.uni_bremen.factroytrouble.ai.ki2.api.memory.SensoryMemory;
import de.uni_bremen.factroytrouble.ai.ki2.api.memory.ShortTermMemory;
import de.uni_bremen.factroytrouble.ai.ki2.api.memory.Thought;
import de.uni_bremen.factroytrouble.ai.ki2.api.memory.WorkingMemory;

public class ScullyWorkingMemory implements WorkingMemory {

    private ShortTermMemory shortTermMemory;
    private SensoryMemory sensMemory;
    private LongTermMemory longtTermMemory;
    private AIPlayer2 player;

    @Override
    public void run() {
        /*
         * Not done yet
         */
    }

    public ScullyWorkingMemory(AIPlayer2 pPlayer) {
        player = pPlayer;

        shortTermMemory = new ScullyShortTermMemory();
        sensMemory = new ScullySensoryMemory(
                new ScullyVisual(player.getMasterFactory().getMaster(player.getGameID()).getBoard(), player), player);
        longtTermMemory = new ScullyLongTermMemory(this);
    }

    /**
     * Zweiter Konstruktor benötigt um die anderen Memories zu mocken, sollte
     * auch in AIPlayer2 übernommen werden.
     * 
     * @param pSensoryMemory
     * @param pShortTermMemory
     * @param pLongTermMemory
     */
    public ScullyWorkingMemory(SensoryMemory pSensoryMemory, ShortTermMemory pShortTermMemory, LongTermMemory pLTM) {
        sensMemory = pSensoryMemory;
        shortTermMemory = pShortTermMemory;
        longtTermMemory = pLTM;
    }

    @Override
    public void process() {
        // Wird noch Implementiert, wenn Threads eingesetzt werden
    }

    @Override
    public Thought getInformation(List<String> keys) {
        Thought tempThought = getThoughtFromShortTerm(keys);
        if (tempThought == null || tempThought.getInformation().isEmpty()) {
            tempThought = getThoughtFromLongTermMemory(keys);
        }
        if (tempThought == null || tempThought.getInformation().isEmpty()) {
            tempThought = getThoughtFromSensoryMemory(keys);
        }

        return tempThought;
    }

    @Override
    public Thought getInformation(Thought key) {
        return null;
    }

    @Override
    public Thought getThoughtFromShortTerm(List<String> keys) {
        return shortTermMemory.getInformation(keys);
    }

    @Override
    public void storeInformation(Thought pThought){
        shortTermMemory.storeInMemory(pThought);
        longtTermMemory.storeInMemory(pThought);
    }

    private Thought getThoughtFromSensoryMemory(List<String> keys) {
        return sensMemory.getInformation(keys);
    }

    private Thought getThoughtFromLongTermMemory(List<String> keys) {
        return longtTermMemory.getInformation(keys);
    }

}
