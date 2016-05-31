/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki1.memory;

import de.uni_bremen.factroytrouble.api.ki1.memory.MemoryEvent;
import de.uni_bremen.factroytrouble.gameobjects.Robot;

/**
 * Events die im Memory gespeichert werden
 * 
 * @author Falko
 *
 */
public class RobotEvent extends MemoryEvent {
    /**
     * 
     * Shotme - merkt sich welcher Roboter auf uns geschossen hat
     * 
     * Shovedme - merkt sich welcher Roboter uns geschoben hat
     * 
     * Killedme - merkt sich welcher Roboter uns getötet hat
     *
     */
    public enum EventType {
        SHOTME, SHOVEDME, KILLEDME
    }

    /**
     * Roboter, der Das Event ausgeführt hat.
     */
    private Robot robotName;

    /**
     * Event, das auf den eigenen Roboter ausgeführt wurde.
     */
    private EventType eventType;
    /**
     * true if {@link robotName} is the "victim",false if if {@link robotName}
     * is the attacker
     */
    private boolean active;

    /**
     * @param robotName,
     *            Name des Roboters, der das Event ausgeführt hat
     * @param eventType,
     *            Event, welches ausgeführt wurde
     * @param round,
     *            Runde in der das Event ausgeführt wurde
     */
    public RobotEvent(Robot robotName, EventType eventType, int round) {
        super(round);
        this.robotName = robotName;
        this.eventType = eventType;
        this.active = false;
    }

    /**
     * @param robotName,
     *            Name des Roboters, der das Event ausgeführt hat
     * @param eventType,
     *            Event, welches ausgeführt wurde
     * @param active,
     *            zeigt an ob das Event von oder gegen den Eigenen Roboter
     *            ausgeführt wurde
     * 
     * @param round,
     *            Runde in der das Event ausgeführt wurde
     * 
     */
    public RobotEvent(Robot robotName, EventType eventType, boolean active, int round) {
        super(round);
        this.robotName = robotName;
        this.eventType = eventType;
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    // FIXME: wirklich sinnvoll? was ist use case von "nachträglich die Runde
    // eines Events ändern"?
    public void setRound(Integer newRound) {
        this.round = newRound;
    }

    public Robot getRobot() {
        return robotName;
    }

    public EventType getEventType() {
        return this.eventType;
    }

    @Override
    public String toString() {
        return "Event [robotName=" + robotName + ", count=" + count + ", eventType=" + eventType + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + count.hashCode();
        result = prime * result + ((eventType == null) ? 0 : eventType.hashCode());
        result = prime * result + ((robotName == null) ? 0 : robotName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RobotEvent other = (RobotEvent) obj;
        if (!count.equals(other.count))
            return false;
        if (eventType != other.eventType)
            return false;
        if (robotName == null && other.robotName != null) {
            return false;
        } else if (!robotName.equals(other.robotName))
            return false;
        return true;
    }
}
