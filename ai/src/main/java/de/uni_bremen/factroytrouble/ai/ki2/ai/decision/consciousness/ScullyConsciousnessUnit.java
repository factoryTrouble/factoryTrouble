/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki2.ai.decision.consciousness;

import java.util.List;

import org.apache.log4j.Logger;

import de.uni_bremen.factroytrouble.ai.ais.AIPlayer2;
import de.uni_bremen.factroytrouble.ai.ki2.ai.decision.unconsciousness.ScullyPersonality;
import de.uni_bremen.factroytrouble.ai.ki2.ai.planning.ScullyStrategyFlag;
import de.uni_bremen.factroytrouble.ai.ki2.ai.planning.ScullyStrategyInterfere;
import de.uni_bremen.factroytrouble.ai.ki2.ai.planning.ScullyStrategySafe;
import de.uni_bremen.factroytrouble.ai.ki2.api.decision.consciousness.Approach;
import de.uni_bremen.factroytrouble.ai.ki2.api.decision.consciousness.ConsciousnessUnit;
import de.uni_bremen.factroytrouble.ai.ki2.api.decision.unconsciousness.MoodUnit;
import de.uni_bremen.factroytrouble.ai.ki2.api.memory.Thought;
import de.uni_bremen.factroytrouble.ai.ki2.api.memory.WorkingMemory;
import de.uni_bremen.factroytrouble.ai.ki2.api.planning.Strategy;

/**
 * 
 * @author Tobias, Sven
 * @version 0.1
 *
 *          Implementierung des Beswusstseins
 */

public class ScullyConsciousnessUnit implements ConsciousnessUnit {

	private static final Logger LOGGER = Logger
			.getLogger(ScullyConsciousnessUnit.class);

	private WorkingMemory work;
	private AIPlayer2 ai;
	private DecisionFlow flow;
	private Pendulum pendulum;
	private boolean panicMode;

	private ScullyPersonality personality;
	private InfluenceBase influence;
	private static final int MAX_VALUE = 10;

	public ScullyConsciousnessUnit(WorkingMemory work, AIPlayer2 ai) {
		this.work = work;
		this.ai = ai;
		flow = new DecisionFlow(work);
		pendulum = new Pendulum(flow, ai);
		personality = (ScullyPersonality) ai.getPersonality();
		startBeing();
		pendulum.start();
	}

	/**
	 * Zum Testen benötigt
	 * 
	 * @param work
	 * @param ai
	 */
	public ScullyConsciousnessUnit(WorkingMemory work, AIPlayer2 ai,
			MoodUnit mood, ScullyPersonality person) {
		this.work = work;
		this.ai = ai;
		flow = new DecisionFlow(work);
		pendulum = new Pendulum(flow, ai);
		personality = person;
		startBeing(mood);
		pendulum.start();
	}

	@Override
	/**
	 * Hier wird ein neuer Aim und eine neue Condition erstellt. Beide werden in
	 * den DecisionFlow gepackt.
	 * 
	 * Der Aufruf dieser Methode stellt den Prozess einer neuen Betrachtung des
	 * Spielfeldes dar, bei denen die momentanen Umstände( Condition ) und
	 * Prioritäten( Aim ) gesetzt werden.
	 */
	public void startBeing() {

		Condition condition = createCondition();
		getFlow().setCondition(condition);

		Aim aim = createAim();
		getFlow().setAim(aim);

		pendulum.fall();
		panicMode = false;

	}

	@Override
	public Thought getInformation(List<String> keys) {
		return work.getInformation(keys);
	}

	@Override
	public Thought getInformation(Thought key) {
		return work.getInformation(key);
	}

	/**
	 *Entscheidet sich zu Anfang einer jeder Runde für zu verwendene Strategie
	 *
	 *Gibt ein Objekt strategy zurück
	 */
	@Override
    public Strategy decideForStrategy() {
        Strategy strategy;
        
        if (getFlow().flowIsEmpty()) {
            startBeing();  
        }

        if (getFlow().getAim().getMaxEnemies() > 2 && getFlow().getAim().getMaxTiles()> 5 ) {
            strategy = new ScullyStrategyFlag();
        } else {
            if (getFlow().getAim().getMaxEnemies() > 6 && getFlow().getAim().getMaxDamage() > 3
            		|| panicMode) {
                strategy = new ScullyStrategyInterfere();
            } else {
                strategy = new ScullyStrategySafe();
            }
        }  	
        
        return strategy;
    }

	/**
	 * 
	 * Wenn KI Neubetrachtung macht, wird startBeing() aufgerufen
	 * 
	 * Wenn Aim und Condition parat sind, wird ausgerechnet
	 */
	@Override
	public boolean decide(Approach approach, int amountOfPlans) {

		if (flow.flowIsEmpty()) {
			startBeing();
		}

		if (getFlow().getIdeaCounter() == -1) {
			getFlow().setIdeaCounter(amountOfPlans);
		}

		/** Wenn der Approach akzeptabel ist */
		if (isSuitingApproach(approach, getFlow().getAim())) {
			if(panicMode){
				return true;
			}
			if (getFlow().getIdeaCounter() != 0) {

				if ((getInfluence().getMood().getFlurry() > MAX_VALUE - 3)
						&& (getInfluence().getMood().getAnxiety() > MAX_VALUE - 3)) {

					getFlow().resetIdeaCounter();
					return true;

				}
				getFlow().increaseIdeaCounter();
				return false;
			} else {
				getFlow().resetIdeaCounter();
				return true;
			}
		} else {

			getFlow().increaseIdeaCounter();
			if (getFlow().getIdeaCounter() == 0) {
				getFlow().setAim(makeCompromiss(getFlow().getAim()));

			}
			return false;
		}

	}

	@Override
	public void panic() {
		panicMode = true;
	}

	@Override
	public int getTime() {
		return ai.getGameMaster().getCountdownTick();
	}

	@Override
	public InfluenceBase getInfluence() {
		if (influence == null) {
			influence = new InfluenceBase(ai);
		}
		return influence;
	}

	/**
	 * Setzt Anspruch herunter, um einen der Plaene zu waehlen.
	 * 
	 * @param aim
	 * @return
	 */
	@Override
	public Aim makeCompromiss(Aim aim) {
		aim.setMaxConveyor(aim.getMaxConveyor() + 1);
		aim.setMaxDamage(aim.getMaxDamage() + 1);
		aim.setMaxEnemies(aim.getMaxEnemies() + 1);
		aim.setMaxHoles(aim.getMaxHoles() + 1);
		aim.setMaxLasers(aim.getMaxLasers() + 1);
		aim.setMaxTiles(aim.getMaxTiles() + 1);

		return aim;
	}

	/**
	 * Hilfsmethode für decide(). Vegleicht alle Werte des Approachs mit denen
	 * des Aims
	 * 
	 * @param approach
	 * @param aim
	 * @return true, wenn Approach passend
	 */
	@Override
	public boolean isSuitingApproach(Approach approach, Aim aim) {

		if (!(approach.getAmountOfNearEnemies() <= aim.getMaxEnemies())) {
			return false;
		}
		if (!(approach.getAmountOfHoles() <= aim.getMaxHoles())) {
			return false;
		}
		if (!(approach.getAmountOfLasers() <= aim.getMaxHoles())) {
			return false;
		}
		if (!(approach.getAmountOfConveyer() <= aim.getMaxConveyor())) {
			return false;
		}
		return approach.getAmountOfNoticedTiles() <= aim.getMaxTiles();
	}

	@Override
	public DecisionFlow getFlow() {
		return flow;
	}

	/**
	 * Condition wird erstellt; Hilfsmethode für startBeing()
	 *
	 * @return Die erstellte Condition
	 */
	private Condition createCondition() {
		Condition condition = new Condition();

		condition.setHealth(ai.getRobot().getHP());
		condition.setLives(ai.getRobot().getLives());

		return condition;
	}

	/**
	 * Aim wird erstellt; Hilfsmethode für startBeing()
	 *
	 * @return Der erstellte Aim
	 */
	private Aim createAim() {
		Aim aim = new Aim();

		int avoidDeath = calculateAvoidDeath();

		int avoidDamage = calculateAvoidDamage();

		int avoidWork = calculateAvoidWork();

		aim.setMaxHoles(avoidDeath);

		aim.setMaxDamage(avoidDamage);

		aim.setMaxLasers(avoidDamage);

		aim.setMaxConveyor(avoidWork);

		aim.setMaxEnemies(avoidDamage - 2);

		aim.setMaxTiles(avoidWork);

		return aim;
	}

	/**
	 * Berechnet Dringlichkeit der Priorität avoidDamage
	 * 
	 * @return Dringlichkeit als Integer
	 */
	private int calculateAvoidDamage() {
		if (getFlow().getCondition().getHealth() < 3) {
			return 0;
		} else {
			int standard = (MAX_VALUE + 3)
					- getFlow().getCondition().getHealth();

			int diff = 5 - personality.getAnxiety();
			if (diff < 0) {
				diff = 0;
			}

			int result = diff + standard;
			if (result < 0) {
				result = 0;
			}
			if (result > 10) {
				result = MAX_VALUE;
			}

			return result;
		}
	}

	/**
	 * Berechnet Dringlichkeit der Priorität avoidDeath
	 * 
	 * @return Dringlichkeit als Integer
	 */
	private int calculateAvoidDeath() {
		if (getFlow().getCondition().getHealth() < 3
				&& getFlow().getCondition().getLives() > 1) {
			return 0;
		} else {

			if (getFlow().getCondition().getLives() == 1) {
				return MAX_VALUE;
			}

			return getFlow().getCondition().getHealth() + 1;

		}
	}

	/**
	 * Berechnet Dringlichkeit der Priorität avoidDeath
	 * 
	 * @return Dringlichkeit als Integer
	 */
	private int calculateAvoidWork() {

		return (int) ((personality.getLazy() / 2) + (personality.getFlurries() / 2));
	}

	/**
	 * Wird zum testen benötigt
	 * 
	 * @param mood
	 */
	private void startBeing(MoodUnit mood) {

		Condition condition = createCondition();

		getFlow().setCondition(condition);

		Aim aim = createAim();
		getFlow().setAim(aim);

		pendulum.fall();

	}
}
