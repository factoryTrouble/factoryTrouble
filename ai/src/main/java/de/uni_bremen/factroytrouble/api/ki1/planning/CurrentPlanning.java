/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.api.ki1.planning;

import java.util.List;

import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.exceptions.KeyNotFoundException;
import de.uni_bremen.factroytrouble.ai.ki1.planning.TacticUnit.Tactics;
import de.uni_bremen.factroytrouble.api.ki1.Control;
import de.uni_bremen.factroytrouble.player.ProgramCard;

/**
 * Interface zur Planung des Zuges der aktuellen Runde.
 * 
 * @author Roland
 *
 */
public interface CurrentPlanning {
    /**
     * Plant mit einem {@link Plan} vom {@link FuturePlanning} die aktuelle
     * Runde, versucht das bestmögliche Ziel zu erreichen und wählt die
     * {@link ProgramCard}s entsprechend. Nutzt dazu einen {@link PathPlanner}.
     * 
     * @param plan
     *            Der Plan mit den Zielen.
     * @return Die gewählten Programmkarten
     * @throws KeyNotFoundException 
     */
    public List<ProgramCard> planTurn();
    
    public void setController(Control control);

    public Tile getGoalTile();
    public int getTacticPower();
    public Tactics getTactic();

    int getRelPosStraight(Tile t1, Tile t2);
    int getRelPosDiag(Tile t1, Tile t2, int ori);
}
