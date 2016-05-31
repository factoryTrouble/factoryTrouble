/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki1.planning;

import de.uni_bremen.factroytrouble.board.Tile;

/**
 * Spezialisiert {@link Goal} für eine umzusetzende {@link TacticUnit}.
 * @author Falko
 *
 */
public class GoalTactic extends GoalTile{
    private TacticUnit tactic;
    
    
    public GoalTactic(int priority, Tile tile, TacticUnit tactic) {
        super(priority, tile);
        setTactic(tactic);
    }

    

    public TacticUnit getTactic() {
        return tactic;
    }

    public void setTactic(TacticUnit tactic) {
        this.tactic = tactic;
    }


    @Override
    public boolean equals(Object obj) {
        return tactic.equals(obj);
    }
    
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    

}
