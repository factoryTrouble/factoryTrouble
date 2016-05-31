/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki2.api.decision.unconsciousness;

import de.uni_bremen.factroytrouble.ai.ki2.api.decision.consciousness.Approach;

/**
 * Dies Räpräsentiert die Stimmung des Charakters. Der Charakter besteht aus
 * einer Anzahl von Werten, die von 1 -10 gehen. Ferner wird hier berechnet, wie
 * sich die Stimmung, unter Einfluss der Persönlichkeit, ändert.
 * 
 * @author Sven
 *
 */
public interface MoodUnit {

    /**
     * Wert zwischen 1 und 10. Beschreibt ruhige bzw. aggresive Haltung. 1 ist
     * echt aggresiv.
     * 
     * @return, Priorität zum schnellen erreichen von Flaggen
     */
    int getAggressivity();

    /**
     * Wert zwischen 1 und 10. Beschreibt ruhige bzw. aggresive Haltung. 1 ist
     * echt aggresiv. Setzt den Wert auf den gegebenen.
     * 
     * @param der
     *            Wert auf den es gesetzt werden soll
     */
    void setAggressivity(int aggro);

    /**
     * Wert zwischen 1 und 10. Beschreibt zuversichtliche bzw. ängstliche
     * Haltung. 10 ist echt ängstlich.
     * 
     * @param der
     *            Wert auf den es gesetzt werden soll
     */
    int getAnxiety();

    /**
     * Wert zwischen 1 und 10. Beschreibt zuversichtliche bzw. ängstliche
     * Haltung. 10 ist echt ängstlich. Setzt den Wert auf den gegebenen.
     * 
     * @param der
     *            Wert auf den es gesetzt werden soll
     */
    void setAnxiety(int anxiety);

    /**
     * Wert zwischen 1 und 10. Beschreibt heitere bzw. träge/pessimistische
     * Haltung. 10 ist echt gloomy.
     * 
     * @param der
     *            Wert auf den es gesetzt werden soll
     */
    int getGloomyness();

    /**
     * Wert zwischen 1 und 10. Beschreibt heitere bzw. träge/pessimistische
     * Haltung. 10 ist echt gloomy. Setzt den Wert auf den gegebenen.
     * 
     * @param der
     *            Wert auf den es gesetzt werden soll
     */
    void setGloomyness(int gloomyness);

    /**
     * Wert zwischen 1 und 10. 10 ist echt faul. Beschreibt Bereitschaft,
     * Aktionen außerhalb der Gewohnheit anzugehen.
     * 
     * @return, Priorität zum schnellen erreichen von Flaggen
     */
    int getLazyness();

    /**
     * Wert zwischen 1 und 10. 10 ist echt faul. Beschreibt Bereitschaft,
     * Aktionen außerhalb der Gewohnheit anzugehen. Setzt den Wert auf den
     * gegebenen.
     * 
     * @param der
     *            Wert auf den es gesetzt werden soll
     */
    void setLazyness(int lazyness);

    /**
     * Wert zwischen 1 und 10. 1 ist nicht nervös, 10 ist sehr nervös
     * 
     * @return der Wert der Nervosität
     */
    public int getFlurry();

    /**
     * Setz den Wert der nervosität. Werte zwischen 1 und 10.
     * 
     * @param pFlurry
     *            der Wert der gesetzt werden soll
     */
    public void setFlurry(int pFlurry);

    /**
     * Verändert die Stimmung anhand der Persönlichkeit und den Schaden den man
     * bekommen hat und weitere Einflüsse.
     * 
     * @param der
     *            Approach mit den die Stimmung sich ändern soll
     *
     */
    public void updateMood(Approach approach, boolean first);
}
