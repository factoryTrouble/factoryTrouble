/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki2.ai.memory;

import java.util.List;

import org.apache.log4j.Logger;

import de.uni_bremen.factroytrouble.ai.ki2.api.memory.Thought;

/**
 * 
 * @author Thore
 * 
 *         Hilfsklasse für das LTM. Sollte nur von ScullyLongTermMemory aus
 *         aufgerufen werden. Wenn ihr sie direkt benutzt macht ihr ziemlich
 *         sicher was falsch.
 * 
 */
public class ScullyLongTermMemoryStorer {

    private static final Logger LOGGER = Logger.getLogger(ScullyLongTermMemoryStorer.class);

    ScullyLongTermMemory main;
    ScullyLongTermMemoryHelper helper;

    public ScullyLongTermMemoryStorer(ScullyLongTermMemory main) {
        this.main = main;
        helper = main.getHelper();
    }

    /**
     * Siehe {@Code ScullyLongTermMemory.java}.storeInMemory() 
     */
    public void storeInMemory(Thought thought) {
        if (thought == null) {
            LOGGER.error("StoreInMemory() wurde null übergeben anstelle eines Thoughts.");
            return;
        }
        LOGGER.debug(
                "storeInMemory: Versuche Thought " + thought.getThoughtName() + " im Langzeitgedächtnis zu speichern.");
        List<Thought> connectors = helper.findToughtsToConnectTo(thought);
        for (Thought connect : connectors) {
            if (connect != null) {
                if (connect.hasConnectionToName(thought.getThoughtName())
                        && thought.hasConnectionToName(connect.getThoughtName())) {
                    connect.power(thought, ScullyLongTermMemory.DUMMY_VALUE_FOR_CONNECTIONS);
                    thought.power(connect, ScullyLongTermMemory.DUMMY_VALUE_FOR_CONNECTIONS);
                    return;
                }
                connect.connectThoughts(thought);
                thought.connectThoughts(connect);
                LOGGER.debug("storeInMemory: Thought " + thought.getThoughtName() + " an Thought "
                        + connect.getThoughtName() + " gebunden.");
                return;
            }
        }
    }

}
