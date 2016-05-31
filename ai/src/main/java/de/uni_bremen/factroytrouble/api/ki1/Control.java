/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.api.ki1;

import java.util.Map;

import de.uni_bremen.factroytrouble.ai.ki1.behavior.DynamicBehaviour;
import de.uni_bremen.factroytrouble.ai.ki1.behavior.StaticBehaviour;
import de.uni_bremen.factroytrouble.api.ki1.planning.CurrentPlanning;
import de.uni_bremen.factroytrouble.api.ki1.planning.FuturePlanning;
import de.uni_bremen.factroytrouble.player.Player;
import de.uni_bremen.factroytrouble.player.ProgramCard;

/**
 * Zentrale Einheit als allgemeine Schnittstelle für alle Module. Units greifen
 * über Control auf andere Units zu. Control ist ein {@link Player} und kann so
 * zu einem Spiel hinzugefügt werden.
 * 
 * @author Frederik, Roland
 *
 */
public interface Control extends Visual, FuturePlanning, CurrentPlanning, Response {
    public enum RequestType {
        CURRENTTILE, NEXTFLAG, HP, LP, ORIENTATION, RESPAWNPOINT, POWERDOWN, REGISTERS, TACTICPOWER, TACTIC;
    }

    public enum GoalType {
        FLAG;
    }

    /**
     * Gibt dem Aufrufer die angefragten Daten zurück. Sucht dazu im Memory und
     * der Visual.
     * 
     * @param request
     *            Kodiert die Anfrage nach dem {@link Tile} wo sich der Roboter
     *            befindet, dem {@link Tile} der nächsten Flagge oder dem
     *            eigenen Roboter. Null falls keins davon angefragt.
     * @param otherRequest
     *            Kodiert andere anfragen, z.B. {@ link Point} für ein
     *            bestimmtes {@link Tile} oder int für die entsprechende
     *            {@link ProgramCard}. Null falls keins davon angefragt.
     * @return Die geforderten Daten oder null
     */
    public Object requestData(RequestType request, Object otherRequest);

    /**
     * Setzt ein gegebenes Ziel für den Agenten als erreicht. Dieses Ziel wird
     * von der Planung erwartet erreicht zu werden.
     * 
     * @param reachedGoal
     *            Typ des Ziels
     */
    public void setPotentialReachedGoal(GoalType reachedGoal);

    
    /**
     * Initialisiert alle Module die der Agent benötigt
     * @param board
     */
    public void initialize();
//    /**
//     * Entfernt die Karte mit dem übergebenen Indes aus der Hand.
//     * @param index
//     */
//    public void removeCardFromHand(ProgramCard card);
    
    public int getAgentNumber();
    
    public String getRobotName();

    public int getGameId();
    /**
     * Gibt eine Map von PlayerID auf Placements zurück.
     * @return Map<Integer,Placement>
     */
    public Map<Integer,Placement> getAllPlayerPlacement();
    
    public StaticBehaviour getStaticBehaviour();
    
    public DynamicBehaviour getDynamicBehaviour();
    
    public int getOwnId();
}
