/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki2.ai.decision.consciousness;

import org.apache.log4j.Logger;

import de.uni_bremen.factroytrouble.ai.ais.AIPlayer2;

public class Pendulum {

	private static final Logger LOGGER = Logger.getLogger(Pendulum.class);
	private boolean isUp = true;
	private DecisionFlow flow;
	private AIPlayer2 ai;
	private Thread pendulumThread;
	private int MAX_VALUE = 10;

	/**
	 * Hier wird ein Thread erstellt, der wie ein schwingendes Chaospendel in
	 * die oberste Position geht. Wenn es das tut, dann wird der momentane
	 * DecisionFlow in die Gedächtnisabgrände geschickt und es wird wieder neu
	 * betrachtet.
	 * 
	 * in swing() entscheidet sich, was mit dem Pendel passiert
	 */
	public Pendulum(DecisionFlow flow, AIPlayer2 ai) {

		this.ai = ai;
		this.flow = flow;
		pendulumThread = new Thread() {
			@Override
			public void run() {
				while (true) {

					if (isUp) {
						flow.intoMemory();
					} else {
						swing();
					}

					try {
						Thread.sleep((long) (20000 / (ai.getPersonality()
								.getSensitivity() * 20)));
					} catch (InterruptedException e) {
						LOGGER.debug(e);
					}

				}
			}
		};
	}

	/**
	 * Methode wird von ConsciousnessUnit aufgerufen
	 */
	public void start() {
		pendulumThread.start();
	}

	/**
	 * Ob Pendel oben ist
	 */
	public boolean isUp() {
		return isUp;
	}

	public void fall() {
		isUp = false;
	}

	/**
	 * Fragt ab, ob momentane Condition abgeschlossen oder sich stark geändert
	 * haben.
	 * 
	 * Beurteilung auch abhängig von Persönlichkeit
	 */
	public void swing() {

		if (conditionsGone()) {
			isUp = true;
		}

	}

	/**
	 * Hilfsmethode für swing()
	 * 
	 * @return
	 */
	private boolean conditionsGone() {

		int sensitivity = ai.getPersonality().getSensitivity();
		int diffs = 0;
		Condition con = flow.getCondition();

		diffs += con.getHealth() - ai.getRobot().getHP();

		diffs += (con.getLives() - ai.getRobot().getLives()) * 2;

		if (diffs > MAX_VALUE) {
			diffs = MAX_VALUE;
		}

		return diffs > (MAX_VALUE - 3) - sensitivity;
	}
}