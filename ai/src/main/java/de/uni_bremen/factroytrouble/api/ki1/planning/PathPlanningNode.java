/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.api.ki1.planning;

/**
 * Interface für Knoten des {@link PathPlanner}.
 * 
 * @author Roland
 *
 */
public interface PathPlanningNode {
    /**
     * Vergleich 2 Nodes. Sie sind gleich, wenn ihre {@link Tile}s die selben
     * absoluten Koordinaten sind.
     * 
     * @param obj
     * @return
     */
    @Override
    boolean equals(Object obj);
}
