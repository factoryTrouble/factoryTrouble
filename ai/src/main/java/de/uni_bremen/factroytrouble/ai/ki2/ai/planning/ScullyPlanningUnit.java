/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki2.ai.planning;

import java.util.ArrayList;
import java.util.List;

import de.uni_bremen.factroytrouble.ai.ais.AIPlayer2;
import de.uni_bremen.factroytrouble.ai.ki2.api.decision.consciousness.ConsciousnessUnit;
import de.uni_bremen.factroytrouble.ai.ki2.api.decision.unconsciousness.UnconsciousnessUnit;
import de.uni_bremen.factroytrouble.ai.ki2.api.planning.PlanningUnit;
import de.uni_bremen.factroytrouble.ai.ki2.api.planning.Strategy;
import de.uni_bremen.factroytrouble.player.ProgramCard;

public class ScullyPlanningUnit implements PlanningUnit {
    private ConsciousnessUnit cons;
    private UnconsciousnessUnit uncons;
    private KillpathFinder killpathFinder;
    private PathFinder pathFinder;
    private CardFinder cardFinder;
    private SecureWayFinder secureWayFinder;
    private AIPlayer2 player;

    public ScullyPlanningUnit(UnconsciousnessUnit unconsciousness, ConsciousnessUnit consciousness, AIPlayer2 pPlayer) {
        player = pPlayer;
        cons = consciousness;
        uncons = unconsciousness;
        pathFinder = new PathFinder(uncons);
        killpathFinder = new KillpathFinder(uncons, player);
        cardFinder = new CardFinder(cons, uncons, player);
        secureWayFinder = new SecureWayFinder(uncons);
    }

    @Override
    public List<ProgramCard> startPlanning(Strategy strategy) {
        List<ProgramCard> plan = startFastPlanning();
        if (strategy instanceof ScullyStrategySafe) {
            plan = startSecurePlanning();
        }
        if (strategy instanceof ScullyStrategyFlag) {
            plan = startFastPlanning();
        }
        if (strategy instanceof ScullyStrategyInterfere) {
            plan = startKillPlanning();
        }
        return plan;
    }

    @Override
    public PlanningUnit getInstance() {
        return this;
    }

    private void powerDown() {
        player.getMasterFactory().getMaster(player.getGameID()).requestPowerDownStatusChange(player.getRobot());
    }

    /**
     * Findet einen möglichst direkten Weg zur Flagge
     * 
     * @return Die zu legenden Karten
     */
    private List<ProgramCard> startFastPlanning() {
        if (player.getRobot().getHP() < 5) {
            powerDown();
        }
        if (pathFinder.getHandCards("").isEmpty()) {
            pathFinder = new PathFinder(uncons);
            cardFinder = new CardFinder(cons, uncons, player);
            return new ArrayList<>();
        }

        List<ProgramCard> specialCaseCards = cardFinder.specialCase();
        if (!specialCaseCards.isEmpty()) {
            pathFinder = new PathFinder(uncons);
            cardFinder = new CardFinder(cons, uncons, player);
            return specialCaseCards;
        }

        List<List<Integer>> possibleWays;
        do {
            possibleWays = pathFinder.findPath();
            for (List<Integer> way : possibleWays) {
                List<ProgramCard> cards = cardFinder.findCards(way);
                if (!cards.isEmpty()) {
                    pathFinder = new PathFinder(uncons);
                    cardFinder = new CardFinder(cons, uncons, player);
                    return cards;
                }
            }
        } while (!possibleWays.isEmpty());
        return new ArrayList<>();
    }

    /**
     * Findet einen Weg um einem Spieler möglichst viel Schaden zu machen
     * 
     * @return Die zu legenden Karten
     */
    private List<ProgramCard> startKillPlanning() {
        if (player.getRobot().getHP() < 4) {
            powerDown();
        }
        if (killpathFinder.getHandCards("").isEmpty()) {
            killpathFinder = new KillpathFinder(uncons, player);
            cardFinder = new CardFinder(cons, uncons, player);
            return new ArrayList<>();
        }
        List<ProgramCard> specialCaseCards = cardFinder.specialCase();
        if (!specialCaseCards.isEmpty()) {
            killpathFinder = new KillpathFinder(uncons, player);
            cardFinder = new CardFinder(cons, uncons, player);
            return specialCaseCards;
        }

        List<List<Integer>> possibleWays;
        do {
            possibleWays = killpathFinder.findKillPath();
            for (List<Integer> way : possibleWays) {
                List<ProgramCard> cards = cardFinder.findCards(way);
                if (!cards.isEmpty()) {
                    killpathFinder = new KillpathFinder(uncons, player);
                    cardFinder = new CardFinder(cons, uncons, player);
                    return cards;
                }
            }
        } while (!possibleWays.isEmpty());
        return new ArrayList<>();
    }

    /**
     * Findet einen sicheren Weg zur Flagge
     * 
     * @return Die zu legenden Karten
     */
    private List<ProgramCard> startSecurePlanning() {
    	if (player.getRobot().getHP() < 7) {
            powerDown();
        }
    	
    	if (secureWayFinder.getHandCards("").isEmpty()) {
    		secureWayFinder = new SecureWayFinder(uncons);
    		cardFinder = new CardFinder(cons, uncons, player);
            return new ArrayList<>();
        }
    	
    	List<ProgramCard> specialCaseCards = cardFinder.specialCase();
        if (!specialCaseCards.isEmpty()) {
        	secureWayFinder = new SecureWayFinder(uncons);
            cardFinder = new CardFinder(cons, uncons, player);
            return specialCaseCards;
        }
        
        List<List<Integer>> possibleWays;
        do {
            possibleWays = secureWayFinder.findPath();
            for (List<Integer> way : possibleWays) {
                List<ProgramCard> cards = cardFinder.findCards(way);
                if (!cards.isEmpty()) {
                	secureWayFinder = new SecureWayFinder(uncons);
                    cardFinder = new CardFinder(cons, uncons, player);
                    return cards;
                }
            }
        } while (!possibleWays.isEmpty());
        return new ArrayList<>();
    }
}