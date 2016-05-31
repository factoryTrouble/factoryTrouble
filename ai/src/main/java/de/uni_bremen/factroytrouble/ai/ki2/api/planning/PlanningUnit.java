/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki2.api.planning;

import java.util.List;

import de.uni_bremen.factroytrouble.player.ProgramCard;

/**
 * Beinhaltet zwei private Methoden, planStepByStep, planInOneGo
 * 
 * @author Artur
 *
 */
public interface PlanningUnit {

    /**
     * plant Züge, wählt Karte(n), sendet eine Vorgehensweise, wartet auf
     * Rückgabe (Freigabe). Ist die Rückgabe true dann wird die nächste Karte
     * geplant, sonst Neustart des Plannungsvorgangs.
     * 
     * 
     * 
     * Ist Letzte Karte zugelassen, dann wird der Zug an die Responseunit
     * geschickt.
     * 
     * @param strategy
     * @return
     */
    List<ProgramCard> startPlanning(Strategy strategy);

    /**
     * Alle wissen was Singleton ist, muss halt public static und so.
     */
    PlanningUnit getInstance();
}
