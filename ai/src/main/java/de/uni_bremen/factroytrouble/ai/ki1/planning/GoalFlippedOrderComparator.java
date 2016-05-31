/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki1.planning;

import java.util.Comparator;

import de.uni_bremen.factroytrouble.api.ki1.planning.Goals;

/**
 * Comparator für {@link Goal}s um ihre Ordnung für {@link PriorityQueue}s zu
 * drehen. Höherer Wert der Priorität wird als niedriger gewertet.
 * 
 * @author Roland
 *
 */
public class GoalFlippedOrderComparator implements Comparator<Goals> {

    @Override
    public int compare(Goals one, Goals two) {
        if (one != null && two != null) {
            int onePrio = one.getPriority();
            int twoPrio = two.getPriority();
            if (onePrio > twoPrio) {
                return -1;
            } else if (onePrio < twoPrio) {
                return +1;
            }
        }
        return 0;
    }

}
