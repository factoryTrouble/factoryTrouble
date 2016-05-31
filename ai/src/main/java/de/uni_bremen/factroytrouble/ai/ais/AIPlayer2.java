/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ais;

import java.util.List;

import de.uni_bremen.factroytrouble.ai.ki2.ai.decision.consciousness.ScullyConsciousnessUnit;
import de.uni_bremen.factroytrouble.ai.ki2.ai.decision.unconsciousness.ScullyPersonality;
import de.uni_bremen.factroytrouble.ai.ki2.ai.decision.unconsciousness.ScullyThreader;
import de.uni_bremen.factroytrouble.ai.ki2.ai.decision.unconsciousness.ScullyUnconsciousnessUnit;
import de.uni_bremen.factroytrouble.ai.ki2.ai.memory.ScullyWorkingMemory;
import de.uni_bremen.factroytrouble.ai.ki2.ai.planning.ScullyPlanningUnit;
import de.uni_bremen.factroytrouble.ai.ki2.api.decision.consciousness.ConsciousnessUnit;
import de.uni_bremen.factroytrouble.ai.ki2.api.decision.unconsciousness.Personality;
import de.uni_bremen.factroytrouble.ai.ki2.api.decision.unconsciousness.UnconsciousnessUnit;
import de.uni_bremen.factroytrouble.ai.ki2.api.memory.WorkingMemory;
import de.uni_bremen.factroytrouble.ai.ki2.api.planning.PlanningUnit;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.player.Master;
import de.uni_bremen.factroytrouble.player.MasterFactory;
import de.uni_bremen.factroytrouble.player.Player;
import de.uni_bremen.factroytrouble.player.ProgramCard;

public class AIPlayer2 extends AIBase {

    private ConsciousnessUnit consc;

    private UnconsciousnessUnit unconsc;
    private WorkingMemory workM;
    private PlanningUnit planing;
    private ScullyPersonality personality;
    private MasterFactory masterFactory;
    private int gameId;
    ScullyThreader thread;

    public ConsciousnessUnit getConsc() {
        return consc;
    }

    public AIPlayer2(Integer gameID, Player player) {
        super(gameID, player);
        masterFactory = mFactory;
        gameId = gameID;
        Robot robot = player.getRobot();
        if (robot != null) {
            personality = new ScullyPersonality(robot.getName());
        } else {
            personality = new ScullyPersonality("Default");
        }
    }

    public ConsciousnessUnit retriveConsc() {
        if (consc == null) {
            consc = new ScullyConsciousnessUnit(getWorkM(), this);
        }
        return consc;
    }

    public UnconsciousnessUnit getUnconsc() {
        if (unconsc == null) {
            unconsc = new ScullyUnconsciousnessUnit(getRobot(), this);
        }
        return unconsc;
    }

    public WorkingMemory getWorkM() {
        if (workM == null) {
            workM = new ScullyWorkingMemory(this);
        }
        return workM;
    }

    public PlanningUnit getPlaning() {
        if (planing == null) {
            planing = new ScullyPlanningUnit(getUnconsc(), retriveConsc(), this);
        }
        return planing;
    }

    public Personality getPersonality() {
        return personality;
    }

    public MasterFactory getMasterFactory() {
        return masterFactory;
    }

    public int getGameID() {
        return gameId;
    }

    @Override
    public void executeTurn() {
        if (thread == null) {
            thread = new ScullyThreader(getUnconsc(), retriveConsc());
        }
        thread.wakeUp();
        List<ProgramCard> cards = getPlaning().startPlanning(retriveConsc().decideForStrategy());
       
        for (int i = 0; i < cards.size(); i++) {
            fillRegister(i, cards.get(i));
        }
        finishTurn();
    }

    public Master getGameMaster() {
        return getMasterFactory().getMaster(gameId);
    }

    @Override
    public void terminateTurn() {
        // TODO Auto-generated method stub
        
    }

}
