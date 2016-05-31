/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki1.planning;

import java.io.IOException;

import de.uni_bremen.factroytrouble.ai.ki1.configreader.AgentConfigReader;
import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.exceptions.KeyNotFoundException;
import de.uni_bremen.factroytrouble.exceptions.KeyNotFoundRuntimeException;

/**
 * 
 * @author Roland, Falko
 *
 */
public class GoalTile extends Goal {
    private Tile tile;
    private AgentConfigReader cnfg;

    public GoalTile(int priority, Tile tile) {
        super(priority);
        try {
            cnfg = AgentConfigReader.getInstance(1);
        } catch (IOException e) {
            LOGGER.error("GoalTile Key not found!", e);
            throw new KeyNotFoundRuntimeException();
        }

        setTile(tile);

    }

    public float getDirectionPriority() {
        try {
            return getPriority() - cnfg.getIntProperty("FPlanning.directionPriority");
        } catch (KeyNotFoundException e) {
            LOGGER.error("GoalTile Key not found!", e);
            throw new KeyNotFoundRuntimeException();
        }
    }

    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((tile == null) ? 0 : tile.hashCode());
        return result;
    }

    /**
     * Vergleich 2 {@link GoalTile}s, sie sind gleich, wenn ihre {@link Tile}s
     * die gleichen absoluten Koordinaten besitzen.
     * 
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null && this != null) {
            return false;
        }
        if (getClass() != obj.getClass())
            return false;
        GoalTile other = (GoalTile) obj;
        if (tile == null) {
            if (other.tile != null)
                return false;
        } else if (!tile.getAbsoluteCoordinates().equals(other.tile.getAbsoluteCoordinates()))
            return false;
        return true;
    }

}
