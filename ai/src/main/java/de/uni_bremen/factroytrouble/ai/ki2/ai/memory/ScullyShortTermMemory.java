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

import de.uni_bremen.factroytrouble.ai.ki2.api.memory.ShortTermMemory;
import de.uni_bremen.factroytrouble.ai.ki2.api.memory.Thought;

/**
 * @author Artur
 *
 */

public class ScullyShortTermMemory implements ShortTermMemory {

    private static final Logger LOGGER = Logger.getLogger(ScullyShortTermMemory.class);
    static final int MEMORY_SIZE = 7;
    private Thought[] thoughts;
    private int counter = 0;

    public ScullyShortTermMemory() {
        thoughts = new Thought[MEMORY_SIZE];
    }

    @Override
    public Thought getInformation(List<String> keys) {
        if (keys == null || keys.isEmpty()) {
            // LOGGER.error("Der gesuchte Gedanke ist null");
            return null;
        }
        LOGGER.debug("getInformation: Suchanfrage " + keys + " erhalten.");
        LOGGER.debug("getInformation: Das STM enthält folgende Gedanken: " + thoughts);

        Thought tempThought = null;

        LOGGER.debug("getInformation: Das STM wird nach " + keys + " durchsucht");

        if (!memoryIsEmpty()) {
            for (int i = 0; i < thoughts.length; i++) {
                if (thoughts[i] == null) {
                    return tempThought;
                }
                if (checkIfThoughtContainsAllKeys(thoughts[i].getThoughtName(), keys)) {
                    return thoughts[i];
                }
            }
        }

        LOGGER.debug("getInformation: Suchanfrage" + keys + "wurde im STM nicht gefunden");
        return tempThought;

    }

    @Override
    public void storeInMemory(Thought thought) {
        if (isAlreadyInSTM(thought)) {
            LOGGER.debug("Der Gedanke " + thought.getThoughtName() + " ist bereits im STM");
            return;
        }
        thoughts[(counter) % MEMORY_SIZE] = thought;// FIFO
        counter++;

    }

    /**
     * Überprüft ob das STM leer ist.
     * 
     * @return <code>true</code> falls das STM leer ist, sonst
     *         <code>false</code>.
     */
    private boolean memoryIsEmpty() {

        for (int i = 0; i < thoughts.length; i++) {
            if (thoughts[i] != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Überprüft ob ein Gedanke der im STM gespeichert werden soll, soch im STM
     * vorhanden ist.
     * 
     * @param thought,
     *            Gedanke, dessen bereits vorhandene, Existenz im STM überprüft
     *            wird.
     * @return <code>true</code>, fall der Gedanke schon im STM vorhanden ist,
     *         sonst <code>false</code>
     */
    private boolean isAlreadyInSTM(Thought thought) {
        boolean isInSTM = false;
        LOGGER.debug("Das STM überprüft ob der Gedanke " + thought.getThoughtName() + " schon enthalten ist");
        for (int i = 0; i < thoughts.length; i++) {
            if (thoughts[i] == null) {
                continue;
            }
            if (thoughts[i].equals(thought.getThoughtName())) {
                isInSTM = true;
            }

        }
        return isInSTM;
    }

    /**
     * Private Methode, die überprüft, ob ein Gedanke alle Wörter der Anfrage
     * enthält.
     * 
     * @param thought,
     *            der Gedanke der nach alles Wörtern durchsucht wird
     * @param keys,
     *            Wörter der Anfrage, die in dem Gedanken gesucht erden
     * @return true, falls alle Wörter der Anfrage in einem Gedanken enthalten
     *         sind, sonst false
     */
    private boolean checkIfThoughtContainsAllKeys(String thought, List<String> keys) {
        if (thought == null) {
            return false;
        }
        boolean isInThought = false;
        int numberOfKeys = keys.size();
        for (int i = 0; i < numberOfKeys; i++) {
            if (thought.contains(keys.get(i))) {
                isInThought = true;
                LOGGER.debug("Das Wort: " + keys.get(i) + " wurde im Gedanken: " + thought + " gefunden.");
            } else {
                LOGGER.debug("Das Wort: " + keys.get(i) + " wurde nicht im Gedanken: " + thought + " gefunden.");
                return false;
            }
        }

        LOGGER.debug("Der Gedanke " + thought + " wurde im STM gefunden.");
        return isInThought;
    }

}
