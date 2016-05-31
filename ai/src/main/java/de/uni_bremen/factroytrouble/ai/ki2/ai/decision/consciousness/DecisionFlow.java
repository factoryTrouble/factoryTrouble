/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki2.ai.decision.consciousness;

import java.util.List;

import de.uni_bremen.factroytrouble.ai.ki2.ai.memory.ScullyThought;
import de.uni_bremen.factroytrouble.ai.ki2.api.memory.Thought;
import de.uni_bremen.factroytrouble.ai.ki2.api.memory.WorkingMemory;

public class DecisionFlow {

	private WorkingMemory work;
	private Thought flow;
	private Aim aim;
	private Condition condition;
	private int nameCounter;
	private int ideaCounter = -1;

	/**
	 * Stellt die Orientierung des Agenten dar.
	 * 
	 * Enthält einen Thought, der Aim und zugehörige Condition enthält.
	 */
	public DecisionFlow(WorkingMemory work) {
		this.work = work;
		flow = createThought(null);
		aim = new Aim();
		condition = new Condition();
		flow.addInformationToThought(aim);
		flow.addInformationToThought(condition);
	}

	public void setAim(Aim aim) {
		if (flow == null) {
			flow = createThought(null);
		}
		this.aim = aim;
		flow.addInformationToThought(aim);
	}

	public Aim getAim() {
		return aim;
	}

	public void setCondition(Condition condition) {
		if (flow == null) {
			flow = createThought(null);
		}
		this.condition = condition;
		flow.addInformationToThought(condition);
	}

	public Condition getCondition() {
		return condition;
	}

	/**
	 * Erstellt neuen Gedanken, für neuen Aim und Condition
	 */
	public Thought createThought(List<Object> aimList) {
		Thought thought = new ScullyThought("state_" + nameCounter);
		/**
		 * if (aimList != null) { for (Object object : aimList) {
		 * thought.addInformationToThought(object); } } nameCounter++;
		 */
		return thought;
	}

	/**
	 * Memory wird im mom noch nicht benutzt
	 * 
	 * hier wird noch der Thought in die Memory geschickt
	 */
	public void intoMemory() {
		work.storeInformation(flow);
		flow = createThought(null);
	}

	/**
	 * Gibt an, ob der 'Flow', also die Orientierung noch im Sinn oder bereits
	 * Erinnerung ist.
	 */
	public boolean flowIsEmpty() {
		if (flow.getInformation().isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	public void increaseIdeaCounter() {
		ideaCounter--;
	}

	public void resetIdeaCounter() {
		ideaCounter = -1;
	}

	public int getIdeaCounter() {
		return ideaCounter;
	}

	public void setIdeaCounter(int ideaCounter) {
		this.ideaCounter = ideaCounter;
	}

}
