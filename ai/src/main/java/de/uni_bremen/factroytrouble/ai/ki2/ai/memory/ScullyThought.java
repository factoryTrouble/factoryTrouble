/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki2.ai.memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uni_bremen.factroytrouble.ai.ki2.api.memory.Thought;

public class ScullyThought implements Thought {

    private HashMap<Thought, Integer> connections;
    private String thoughtName;
    private List<Object> informations;

    public ScullyThought(String thoughtName) {
        if (thoughtName == null) {
            throw new IllegalArgumentException("Thought name is null!");
        }
        connections = new HashMap<Thought, Integer>();
        informations = new ArrayList<>();
        this.thoughtName = thoughtName;
    }

    @Override
    public void connectThoughts(Thought thought) {
        if (connections.get(thought) == null) {
            connections.put(thought, DEFAULT_CONNECTION);
        }
    }

    @Override
    public Map<Thought, Integer> getThoughts() {
        return connections;
    }

    @Override
    public void power(Thought thought, int value) {
        Integer strength = connections.get(thought);
        if (strength == null) {
            connectThoughts(thought);
        }
        connections.put(thought, connections.get(thought) + value);
    }

    @Override
    public void weak(Thought thought, int value) {
        Integer strength = connections.get(thought);
        if (strength != null) {
            strength -= value;
            if (strength < 0) {
                strength = 0;
            }
            connections.put(thought, strength);
        }
    }

    @Override
    public String getThoughtName() {
        return thoughtName;
    }

    @Override
    public void addInformationToThought(Object information) {
        informations.add(information);

    }

    @Override
    public List<Object> getInformation() {
        return informations;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((informations == null) ? 0 : informations.hashCode());
        result = prime * result + ((thoughtName == null) ? 0 : thoughtName.hashCode());
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
        ScullyThought other = (ScullyThought) obj;
        if (connections == null) {
            if (other.connections != null)
                return false;
        } else if (!connections.equals(other.connections))
            return false;
        if (informations == null) {
            if (other.informations != null)
                return false;
        } else if (!informations.equals(other.informations))
            return false;
        if (thoughtName == null) {
            if (other.thoughtName != null)
                return false;
        } else if (!thoughtName.equals(other.thoughtName))
            return false;
        return true;
    }

    @Override
    public boolean hasConnectionToThought(Thought thought) {
        if (connections.containsKey(thought)) {
            power(thought, 1);
            thought.power(this, 1);
            return true;
        }
        return false;
    }

    @Override
    public boolean hasConnectionToName(String name) {
        for (Thought key : connections.keySet()) {
            if (key.getThoughtName().equals(name)) {
                power(key, 1);
                key.power(this, 1);
                return true;
            }
        }
        return false;
    }

    @Override
    public Thought getConnectedThoughtByName(String name) {
        for (Thought t : connections.keySet()) {
            if (t.getThoughtName().equals(name)) {
                power(t, 1);
                t.power(this, 1);
                return t;
            }
        }
        return null;
    }

    @Override
    public Integer getStrengthOfConnection(Thought other) {
        return connections.get(other);
    }

}
