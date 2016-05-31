/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki2.ai.decision.unconsciousness;

import de.uni_bremen.factroytrouble.ai.ki2.api.decision.unconsciousness.Personality;
import de.uni_bremen.factroytrouble.player.Master;

public class ScullyPersonality implements Personality {
    /*
     * Bot names: "MACE BOT", "TITAN T17", "WHIRLBOT", "POUND BOT",
     * "LETHAR BOT", "FLINCH BOT", "FUNKEY", "SQUINT BOT"
     */

    /**
     * Wie schnell er Aggressiv wird
     */
    private int aggression;

    /**
     * Ängstlichkeit beinflusst die sicherheit der Spielweise
     */
    private int anxiety;

    /**
     * Optimist oder Pessimist ( 0 = Optimist; 10 = Pessimist)
     */
    private int gloomy;

    /**
     * Beschreibt Bereitschaft, Aktionen außerhalb der Gewohnheit anzugehen
     */
    private int lazy;

    /**
     * gibt an wie schnell der Charakter Nervös wird
     */
    private int flurries;

    /**
     * Beschreibt Bereitschaft, Entscheidungen neu zu überdenken
     */
    private int sensitivity;

    private static final int DEFAULT = 5;
    private static final int MAX = 10;
    private static final int MIN = 1;

    /**
     * @author Sven
     * 
     *         Konstrukktor der die Persönlichkeit anhand des Robooternamens
     *         erstellt. Default ist ohne besondere Eigenschaften.
     */
    public ScullyPersonality(String name) {
        if (!Master.ROBOT_NAMES.contains(name)) {
            initPersonalityByName("");
        } else {
            initPersonalityByName(name);
        }
    }

    /**
     * Gibt das Agressions-Potenzial des Charakters zurück
     * 
     * @return 1 = Minimum, 10 = Maximum. Der Wert der Agression
     */
    public int getAggression() {
        return aggression;
    }

    /**
     * Gibt das Ängstlichkeits-Potenzial des Charakters zurück
     * 
     * @return 1 = Minimum, 10 = Maximum. Der Wert der Angst
     */
    public int getAnxiety() {
        return anxiety;
    }

    /**
     * Gibt an ob der Spieler eher Pessimist oder Optimist ist.
     * 
     * @return 1 = Pessimist, 10 = Optimist.
     */
    public int getGloomy() {
        return gloomy;
    }

    /**
     * Gibt das Faulheits-Potenzial des Charakters zurück. Faul bedeutet, dass
     * er seine gewohnte Spielweise selten ändert.
     * 
     * @return 1 = Minimum, 10 = Maximum. Der Wert der Faulheit
     */
    public int getLazy() {
        return lazy;
    }

    /**
     * Gibt an, wie schnell der Charakter Nervös wird: 1 = wenig; 10 = sehr
     * schnell
     * 
     * @return der Wert der Schnelligkeit
     */
    public int getFlurries() {
        return flurries;
    }

    /**
     * Gibt an, wie schnell der Charakter Nervös wird: 1 = wenig; 10 = sehr
     * schnell
     * 
     * @return der Wert der Schnelligkeit
     */
    public int getSensitivity() {
        return sensitivity;
    }

    /**
     * @author Sven
     * 
     *         Initialisiert die Persönlichkeit anhand des Namens.
     */
    private void initPersonalityByName(String name) {
        switch (name) {
        case "FLINCH BOT":
            setAttr(MIN, MAX, MAX, 1, MAX, 6);
            break;
        case "MACE BOT":
            setAttr(7, 3, MIN, 5, MIN, 3);
            break;
        case "TITAN T17":
            setAttr(8, MIN, MIN, MAX, MIN, MIN);
            break;
        case "WHIRLBOT":
            setAttr(MIN, MIN, MAX, 5, 7, 4);
            break;
        case "POUND BOT":
            setAttr(MAX, MIN, MIN, 8, 2, 5);
            break;
        case "LETHAR BOT":
            setAttr(8, MIN, MIN, MIN, 2, 7);
            break;
        case "FUNKEY":
            setAttr(6, 8, MIN, 5, 4, MAX);
            break;
        case "SQUINT BOT":
            setAttr(MIN, MIN, MIN, MIN, MAX, MAX);
            break;
        default:
            setAttr(DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);
            break;
        }
    }

    private void setAttr(int pAgression, int pAnexity, int pGloomy, int pLazy, int pFlurries, int pSensitivity) {
        aggression = pAgression;
        anxiety = pAnexity;
        gloomy = pGloomy;
        lazy = pLazy;
        flurries = pFlurries;
        sensitivity = pSensitivity;
    }

}
