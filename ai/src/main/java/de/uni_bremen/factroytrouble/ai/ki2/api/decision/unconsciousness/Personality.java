/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki2.api.decision.unconsciousness;

/**
 * getter und setter
 * 
 * @author Artur
 *
 */
public interface Personality {

	/**
	 * Gibt das Agressions-Potenzial des Charakters zurück
	 * 
	 * @return 1 = Minimum, 10 = Maximum. Der Wert der Agression
	 */
	public int getAggression();

	/**
	 * Gibt das Ängstlichkeits-Potenzial des Charakters zurück
	 * 
	 * @return 1 = Minimum, 10 = Maximum. Der Wert der Angst
	 */
	public int getAnxiety();

	/**
	 * Gibt an ob der Spieler eher Pessimist oder Optimist ist.
	 * 
	 * @return 1 = Pessimist, 10 = Optimist.
	 */
	public int getGloomy();

	/**
	 * Gibt das Faulheits-Potenzial des Charakters zurück. Faul bedeutet, dass
	 * er seine gewohnte Spielweise selten ändert.
	 * 
	 * @return 1 = Minimum, 10 = Maximum. Der Wert der Faulheit
	 */
	public int getLazy();

	/**
	 * Gibt an, wie schnell der Charakter Nervös wird: 1 = wenig; 10 = sehr
	 * schnell
	 * 
	 * @return der Wert der Schnelligkeit
	 */
	public int getFlurries();

	/**
	 * Gibt an, wie schnell der Charakter Nervös wird: 1 = wenig; 10 = sehr
	 * schnell
	 * 
	 * @return der Wert der Schnelligkeit
	 */
	public int getSensitivity();
}
