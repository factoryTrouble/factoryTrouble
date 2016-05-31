/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki2.ai.memory;

import java.util.ArrayList;
import java.util.List;

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
public class ScullyLongTermMemoryFinder {

    ScullyLongTermMemory main;
    ScullyLongTermMemoryHelper helper;

    public ScullyLongTermMemoryFinder(ScullyLongTermMemory main) {
        this.main = main;
        helper = main.getHelper();
    }

    /**
     * Wird noch Implementiert
     */
    public Thought getInformation(List<String> keys) {
        List<Thought> possibleCategory = new ArrayList<>();
        for (String s : keys) {
            possibleCategory.add(helper.findCategoryToString(s));
        }
        List<Thought> possible = new ArrayList<>();
        for (Thought c : possibleCategory) {
            Thought f = searchMap(keys, c);
            if (f != null) {
                possible.add(f);
            }
        }
        return chooseThoughtFromPossibleList(possible, keys);
    }

    public Thought searchMap(List<String> asked, Thought search) {
        List<Thought> already = new ArrayList<>();
        return searchMap(asked, search, already);
    }

    /**
     * Siehe {@Code ScullyLongTermMemory.java}.searchMap()
     * 
     */
    public Thought searchMap(List<String> asked, Thought search, List<Thought> already) {
        if (already.contains(search)) {
            return null;
        }
        already.add(search);
        // 0Level
        List<Thought> possible = new ArrayList<>();
        String searchedName = helper.getThoughtNameFromList(asked);
        if (helper.correlatingStringFactor(searchedName, search.getThoughtName()) > 0) {
            possible.add(search);
        }
        // 1Level
        if (search.hasConnectionToName(searchedName)) {
            Thought found = search.getConnectedThoughtByName(searchedName);
            if (connectionFound(search, found)) {
                possible.add(found);
            }
        }
        // 2Level
        for (Thought tt : search.getThoughts().keySet()) {
            if (helper.correlatingStringFactor(tt.getThoughtName(), searchedName) > 0) {
                Thought find = searchMap(asked, tt, already);
                if (find != null) {
                    possible.add(find);
                }
            }
        }
        // Choose
        return chooseThoughtFromPossibleList(possible, asked);
    }

    private Thought chooseThoughtFromPossibleList(List<Thought> possible, List<String> asked) {
        String searchedName = helper.getThoughtNameFromList(asked);
        Thought likeliest = null;
        int factor = 20;
        for (Thought t : possible) {
            int newFactor = helper.correlatingStringFactor(t.getThoughtName(), searchedName);
            if (newFactor > factor) {
                factor = newFactor;
                likeliest = t;
            }
        }
        return likeliest;
    }

    private boolean connectionFound(Thought thought1, Thought thought2) {
        int dice = main.getRNGesus().nextInt(100);
        return thought1.getStrengthOfConnection(thought2) > dice;
    }
}
