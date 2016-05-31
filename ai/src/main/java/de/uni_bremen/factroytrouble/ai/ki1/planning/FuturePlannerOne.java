/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki1.planning;

import java.io.IOException;
import java.util.*;

import org.apache.log4j.Logger;

import de.uni_bremen.factroytrouble.ai.ais.AIPlayer1;
import de.uni_bremen.factroytrouble.ai.ki1.behavior.Attitude;
import de.uni_bremen.factroytrouble.ai.ki1.behavior.Mood;
import de.uni_bremen.factroytrouble.ai.ki1.behavior.StaticBehaviour;
import de.uni_bremen.factroytrouble.ai.ki1.configreader.AgentConfigReader;
import de.uni_bremen.factroytrouble.ai.ki1.planning.TacticUnit.Tactics;
import de.uni_bremen.factroytrouble.api.ki1.Control;
import de.uni_bremen.factroytrouble.api.ki1.Timer;
import de.uni_bremen.factroytrouble.api.ki1.Control.RequestType;
import de.uni_bremen.factroytrouble.api.ki1.planning.FuturePlanning;
import de.uni_bremen.factroytrouble.api.ki1.planning.Goals;
import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.exceptions.KeyNotFoundException;
import de.uni_bremen.factroytrouble.exceptions.KeyNotFoundRuntimeException;
import de.uni_bremen.factroytrouble.gameobjects.Robot;

/**
 * Implementiert {@link FuturePlanning}. Plant in die Zukunft und erstellt einen
 * {@link Plan} mit {@link Goal}s.
 * 
 * @author Falko, Roland
 *
 */
public class FuturePlannerOne implements FuturePlanning {

    private static final Logger LOGGER = Logger.getLogger(FuturePlanning.class);

    private Control controller;
    private Timer timerUnit;

    private Plan currentPlan;
    private Tile nextFlag;
    private Robot iRobot;
    private TheSituation situation;
    private List<PlanPhase> masterPlan;
    private List<TacticUnit> availibleTactics;
    private AIPlayer1 aip1;
    private Integer iHP;
    private Integer iLP;
    private Integer dangerLevel;
    private Integer currentPhase;

    private AgentConfigReader cnfg;

    /**
     * Die Verschiedenenen Situationen in der sich der Future Planner befinden
     * kann
     * 
     * @author Falko
     *
     */
    public enum TheSituation {
        DANGER, MORTAL, CHILL, HURRY, RAGE
    }

    /**
     * Erstellt einen neuen FuturePlanner
     * 
     * @param controller
     * @param timerUnit
     * @param staticB
     * @throws IOException
     */
    public FuturePlannerOne(Control controller, Timer timerUnit, StaticBehaviour staticB) throws IOException {
        setController(controller);
        setTimerUnit(timerUnit);
        aip1 = (AIPlayer1) controller;
        cnfg = AgentConfigReader.getInstance(1);
        // FPO v2
        this.availibleTactics = new ArrayList<TacticUnit>();
        initializeTactics();
        situation = this.parseMoodAttitudetoSituation();
        masterPlan = new ArrayList<PlanPhase>();
        currentPhase = 0;
        dangerLevel = 0;
        createMasterPlan();
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uni_bremen.factroytrouble.ai.planning.FuturePlanning#startPlanning()
     */
    @Override
    public void startPlanning() {
        subPlanning();
        // potTile kann nicht null sein, da es immer eine nächste Flagge gibt,
        // oder das Spiel gewonnen wurde
        // iRobot = controller.get eigenen roboter holen

        Object potTile = controller.requestData(RequestType.NEXTFLAG, null);
        nextFlag = (Tile) potTile;
        PriorityQueue<Goals> goals = new PriorityQueue<Goals>();
        GoalTile nextFlagGoal = new GoalTile(100, nextFlag);
        goals.add(nextFlagGoal);
        currentPlan = new Plan(goals);
    }

    /**
     * Generiert anhand der aktiuellen situation und einer übergeben tactict die
     * prio que mit den entsprechenden Goals
     * 
     * @param tu
     */
    public void generateGoals(TacticUnit tu) {
        PriorityQueue<Goals> tmpGoals = new PriorityQueue<Goals>();
        int goalAmount = 0;
        try {
            goalAmount = cnfg.getIntProperty("FPlanning.Goals");
        } catch (KeyNotFoundException e) {
            LOGGER.error("FuturePlanning Key not found!", e);
            throw new KeyNotFoundRuntimeException();
        }
        int maxPrio = 1000;
        for (int x = 0; x < goalAmount; x++) {
            try {
                maxPrio = maxPrio - cnfg.getIntProperty("FPlanning.goalPrioReduction");
            } catch (KeyNotFoundException e) {
                LOGGER.error("FuturePlanning Key not found!", e);
                throw new KeyNotFoundRuntimeException();
            }
            if (x == 0) {
                tmpGoals.add(new GoalTactic(maxPrio, (Tile) controller.requestData(RequestType.NEXTFLAG, null), tu));
            } else if (x == 1) {
                Object potTile = controller.requestData(RequestType.NEXTFLAG, null);
                nextFlag = (Tile) potTile;
                GoalTile nextFlagGoal = new GoalTile(100, nextFlag);
                tmpGoals.add(nextFlagGoal);
            }
        }
    }

    @Override
    public Plan getCurrentPlan() {
        return currentPlan;
    }

    @Override
    public void setController(Control controller) {
        this.controller = controller;
    }

    public void setTimerUnit(Timer timerUnit) {
        this.timerUnit = timerUnit;
    }

    public List<TacticUnit> getAvailibleTactics() {
        return availibleTactics;
    }

    /**
     * Kreeiert ein MasterPLan wenn noch keiner existiert
     */
    private void createMasterPlan() {
        Integer planAhead;
        masterPlan = new ArrayList<PlanPhase>();
        try {
            planAhead = cnfg.getIntProperty("FPlanning.PlanAhead");
        } catch (KeyNotFoundException e) {
            LOGGER.error("FuturePlanning Key not found!", e);
            throw new KeyNotFoundRuntimeException();
        }
        for (int i = 0; i <= planAhead; i++) {
            PlanPhase tmp1 = new PlanPhase(i, this.situation);
            masterPlan.add(tmp1);
        }
    }

    /**
     * Erweitertes Planning, im Momment noch nicht eingebunden
     */
    private void subPlanning() {
        try {
            analyzeSituation();
        } catch (KeyNotFoundException e) {
            LOGGER.error("FuturePlanning Key not found!", e);
            throw new KeyNotFoundRuntimeException();
        }
        // Tactiken auswählen zur mood
        TacticUnit tu = chooseTactic();
    
        // n zeiele auswählen und in goal listen packen
        generateGoals(tu);
    
        // done
    
    }

    /**
     * Wählt die passende Tactic zur Mood
     * 
     * @return
     */
    private TacticUnit chooseTactic() {
        if (this.situation == TheSituation.DANGER) {
            return new TacticUnit(Tactics.ROBINSONCRUSO);
        } else if (this.situation == TheSituation.HURRY) {
            return new TacticUnit(Tactics.VINDIESEL);
        } else {
            return new TacticUnit(Tactics.ROBINSONCRUSO);
        }
    }

    /**
     * überarbeitet den MasterPlan. Im momment noch nicht in benutzung
     */
    private void reevaluateMasterPlan() {
        try {
            analyzeSituation();
        } catch (KeyNotFoundException e) {
            LOGGER.error("FuturePlanning Key not found!", e);
            throw new KeyNotFoundRuntimeException();
        }
    }

    /**
     * Analysiert die aktuelle Situation, setzt das DangerLevel und
     * aktualisiertt gegenenfalls die Mood.
     * 
     * @throws KeyNotFoundException
     */
    private void analyzeSituation() throws KeyNotFoundException {
        iHP = (Integer) controller.requestData(RequestType.HP, null);
        iLP = (Integer) controller.requestData(RequestType.LP, null);
    
        if (iHP < cnfg.getIntProperty("FPlanning.DangerThreshold")) {
            dangerLevel += cnfg.getIntProperty("FPlanning.LowHPRisk");
        }
        if (iLP == 2) {
            dangerLevel += cnfg.getIntProperty("FPlanning.2LPRisk");
        } else if (iLP == 3) {
            dangerLevel += cnfg.getIntProperty("FPlanning.1LPRisk");
        }
        if (cnfg.getIntProperty("FPlanning.DangerZone") <= dangerLevel) {
            this.situation = TheSituation.DANGER;
        }
        if (!masterPlan.isEmpty()) {
            this.situation = masterPlan.get(this.currentPhase).getMood();
        }
    }

    /**
     * Initialisiert die tictac Taktiken
     */
    private void initializeTactics() {
        Tactics[] tictacs = Tactics.values();
        for (Tactics tictac : tictacs) {
            this.availibleTactics.add(new TacticUnit(tictac));
        }
    }

    /**
     * parsed modd und attitude von den behaviour units in eine situation für FP
     * 
     * @return
     */
    private TheSituation parseMoodAttitudetoSituation() {
        // control.
        Mood md = aip1.getDynamicBehaviour().getMood();
        Attitude atd = aip1.getDynamicBehaviour().getAttitude();
        if ((atd.equals(Attitude.DEFENSIVE) || (atd.equals(Attitude.VERYDEFENSIVE)))
                && (((md.equals(Mood.SAD)) || md.equals(Mood.VERYSAD)))) {
            return TheSituation.DANGER;
        } else if ((atd.equals(Attitude.AGGRESSIVE) || (atd.equals(Attitude.VERYAGGRESSIVE)))
                && (((md.equals(Mood.VERYHAPPY)) || md.equals(Mood.HAPPY)))) {
            return TheSituation.HURRY;
        } else {
            return TheSituation.CHILL;
        }
    }
}
