/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.api.ki1.memory;

/**
 * Ereignisse, die im Gedächtnis gespeichert werden. Zu jedem Gedächtnis wird
 * die Runde gespeichert, in der dieses Ereignis passierte und wie oft das in
 * dieser Runde der Fall war.
 *
 * Wenn sich das gleiche Ereignis in einer späteren Runde ereignet, sollte
 * also ein neuen Objekt erzeugt werden.
 *
 * @author Simon Liedtke
 */
public abstract class MemoryEvent {
    /** wie oft das Event in Runde {@link round} passiert ist */
    protected Integer count;

    /** in welcher Runde sich das Event ereignet hat */
    protected Integer round;
    
    public MemoryEvent(int round) {
        count = 1;
        this.round= round;
    }

    /**
     * Erhöht den Zähler innerhalb des Events.
     */
    public void increment() {
        count++;
    }

    public Integer getCount() {
        return count;
    }

    public Integer getRound() {
        return round;
    }

    public abstract boolean equals(Object other);
    
    public abstract int hashCode();
}
