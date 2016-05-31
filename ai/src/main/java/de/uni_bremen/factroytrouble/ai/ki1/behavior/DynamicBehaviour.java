/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki1.behavior;

import java.util.ArrayList;
import java.util.List;

import de.uni_bremen.factroytrouble.api.ki1.memory.Chunk;
import de.uni_bremen.factroytrouble.api.ki1.memory.MemoryEvent;
import de.uni_bremen.factroytrouble.ai.ki1.memory.RobotEvent;
import de.uni_bremen.factroytrouble.ai.ais.AIPlayer1;
import de.uni_bremen.factroytrouble.ai.ki1.memory.MemoryUnit;
import de.uni_bremen.factroytrouble.ai.ki1.memory.ReachedFlagEvent;

public class DynamicBehaviour {

    // Bonus needs fitting
    public static final int reachedFlagBonus = 10;
    public static final int shotMeBonus = 2;
    public static final int shovedMeBonus = 1;
    public static final int killedMeBonus = 5;

    // Threshold needs fitting
    public static final int moodChangeThreshold = 10;
    public static final int attitudeChangeThreshold = 10;
    public static final int riskReadinessChangeThreshold = 10;

    private int happiness;
    private int aggrodeffi;
    private int schadenfreude;
    private Mood mood;
    private Boolean moodAlternates;
    private Attitude attitude;
    private Boolean attitudeIsAggro;
    private Boolean attitudeIsDeffi;
    private RiskReadiness riskReadiness;
    private Boolean riskReadinessAlternates;
    private StaticBehaviour staticBehaviour;
    private MemoryUnit memory;
    private AIPlayer1 control;

    public DynamicBehaviour(String name, AIPlayer1 control) {
        happiness = 0;
        aggrodeffi = 0;
        schadenfreude = 0;
        staticBehaviour = new StaticBehaviour(name);
        this.control = control;
        retrieveStaticBehav();
    }

    public void calcEmotions() {
        List<MemoryEvent> latestEvents = getLatestEvents();
        for (RobotEvent actualEvent : getLatestRobotEvents(latestEvents)) {
            if (actualEvent.getEventType().equals(RobotEvent.EventType.SHOTME)) {
                if (actualEvent.isActive()) {
                    schadenfreude += actualEvent.getCount() * shotMeBonus;
                    happiness += actualEvent.getCount() * shotMeBonus;
                } else {
                    aggrodeffi += actualEvent.getCount() * shotMeBonus;
                    happiness -= actualEvent.getCount() * shotMeBonus;
                }
            } else if (actualEvent.getEventType().equals(RobotEvent.EventType.SHOVEDME)) {
                if (actualEvent.isActive()) {
                    schadenfreude += actualEvent.getCount() * shovedMeBonus;
                    happiness += actualEvent.getCount() * shovedMeBonus;
                } else {
                    aggrodeffi += actualEvent.getCount() * shovedMeBonus;
                    happiness -= actualEvent.getCount() * shovedMeBonus;
                }
            } else if (actualEvent.getEventType().equals(RobotEvent.EventType.KILLEDME)) {
                if (actualEvent.isActive()) {
                    schadenfreude += actualEvent.getCount() * killedMeBonus;
                    happiness += actualEvent.getCount() * killedMeBonus;
                } else {
                    aggrodeffi += actualEvent.getCount() * killedMeBonus;
                    happiness -= actualEvent.getCount() * killedMeBonus;
                }
            }

        }
        for (ReachedFlagEvent actualEvent : getLatestReachedFlagEvents(latestEvents)) {
            happiness += reachedFlagBonus;
        }
        changeRiskReadiness();
        changeMood();
        changeAttitude();
    }

    private ArrayList<RobotEvent> getLatestRobotEvents(List<MemoryEvent> latestEvents) {
        ArrayList<RobotEvent> roboEvents = new ArrayList<RobotEvent>();
        for (MemoryEvent event : latestEvents) {
            if (event instanceof RobotEvent) {
                roboEvents.add((RobotEvent) event);
            }
        }
        return roboEvents;
    }

    private ArrayList<ReachedFlagEvent> getLatestReachedFlagEvents(List<MemoryEvent> latestEvents) {
        ArrayList<ReachedFlagEvent> reachEvents = new ArrayList<ReachedFlagEvent>();
        for (MemoryEvent event : latestEvents) {
            if (event instanceof ReachedFlagEvent) {
                reachEvents.add((ReachedFlagEvent) event);
            }
        }
        return reachEvents;
    }

    private void retrieveStaticBehav() {
        staticBehaviour.readFile();
        mood = staticBehaviour.getMood();
        moodAlternates = staticBehaviour.getMoodAlternates();
        attitude = staticBehaviour.getAttitude();
        riskReadiness = staticBehaviour.getRiskReadiness();
        riskReadinessAlternates = staticBehaviour.getRiskReadinessAlternates();
        attitudeIsAggro = staticBehaviour.getAttitudeIsAggro();
        attitudeIsDeffi = staticBehaviour.getAttitudeIsDeffi();

    }

    public List<MemoryEvent> getLatestEvents() {
        ArrayList<MemoryEvent> memlist = new ArrayList<MemoryEvent>();
        for (Chunk chunk : memory.getLongTermMemory().getChunks()) {
            if (chunk.getData() instanceof MemoryEvent
                    && chunk.getKey().getEvent().getRound() == control.getRound() - 1) {
                memlist.add((MemoryEvent) chunk);
            }
        }
        return memlist;
    }

    public void changeRiskReadiness() {
        if (riskReadinessAlternates) {
            if (schadenfreude >= riskReadinessChangeThreshold) {
                if (riskReadiness.ordinal() != riskReadiness.values().length - 1) {
                    riskReadiness = riskReadiness.values()[riskReadiness.ordinal() + 1];
                }
                schadenfreude -= riskReadinessChangeThreshold;
            }
            if (schadenfreude <= -riskReadinessChangeThreshold) {
                if (riskReadiness.ordinal() != 0) {
                    riskReadiness = riskReadiness.values()[riskReadiness.ordinal() - 1];
                }
                schadenfreude += riskReadinessChangeThreshold;
            }
        }
    }

    public void changeMood() {
        if (moodAlternates) {
            if (happiness >= moodChangeThreshold) {
                if (mood.ordinal() != mood.values().length - 1) {
                    mood = mood.values()[mood.ordinal() + 1];
                }
                happiness -= moodChangeThreshold;
            }
            if (happiness <= -moodChangeThreshold) {
                if (mood.ordinal() != 0) {
                    mood = mood.values()[mood.ordinal() - 1];
                }
                happiness += moodChangeThreshold;
            }
        }
    }

    public void changeAttitude() {
        if (attitudeIsAggro) {
            if (aggrodeffi >= attitudeChangeThreshold) {
                if (attitude.ordinal() != attitude.values().length - 1) {
                    attitude = attitude.values()[attitude.ordinal() + 1];
                }
                aggrodeffi -= attitudeChangeThreshold;
            }
            if (aggrodeffi <= -attitudeChangeThreshold) {
                if (attitude.ordinal() > attitude.values().length / 2) {
                    attitude = attitude.values()[attitude.ordinal() - 1];
                }
                aggrodeffi += attitudeChangeThreshold;
            }
        }

        if (attitudeIsDeffi) {
            if (aggrodeffi >= attitudeChangeThreshold) {
                if (attitude.ordinal() != 0) {
                    attitude = attitude.values()[attitude.ordinal() - 1];
                }
                aggrodeffi -= attitudeChangeThreshold;
            }
            if (aggrodeffi <= -attitudeChangeThreshold) {
                if (attitude.ordinal() < attitude.values().length / 2) {
                    attitude = attitude.values()[attitude.ordinal() + 1];
                }
                aggrodeffi += attitudeChangeThreshold;
            }
        }
    }

    public void setMemoryUnit(MemoryUnit memory) {
        this.memory = memory;
    }

    public Mood getMood() {
        return mood;
    }

    public Attitude getAttitude() {
        return attitude;
    }

    public RiskReadiness getRiskReadiness() {
        return riskReadiness;
    }

    public int getHappiness() {
        return happiness;
    }

    public void setHappiness(int happiness) {
        this.happiness = happiness;
    }

    public int getAggroDeffi() {
        return happiness;
    }

    public void setAggroDeffi(int aggrodeffi) {
        this.aggrodeffi = aggrodeffi;
    }

    public int getSchadenfreude() {
        return happiness;
    }

    public void setSchadenfreude(int schadenfreude) {
        this.schadenfreude = schadenfreude;
    }
}
