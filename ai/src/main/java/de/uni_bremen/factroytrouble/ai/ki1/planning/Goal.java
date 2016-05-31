/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki1.planning;

import org.apache.log4j.Logger;

import de.uni_bremen.factroytrouble.api.ki1.planning.FuturePlanning;
import de.uni_bremen.factroytrouble.api.ki1.planning.Goals;

/**
 * Repräsentiert ein priorisiertes Ziel für den Agenten. Ist nach dieser
 * sortierbar. Ein höherer Prioritätswert ist besser.
 * 
 * @author Roland, Falko
 *
 */
public abstract class Goal implements Comparable<Goal>, Goals {
    private int priority;
    protected static final Logger LOGGER = Logger.getLogger(FuturePlanning.class);
    
    public Goal(int priority) {
        this.priority = priority;
    }
    @Override
    public int getPriority() {
        return priority;
    }
    @Override
    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(Goal otherGoal) {
        return Integer.compare(this.getPriority(), otherGoal.getPriority());
    }

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + priority;
        return result;
    }
}