/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki2.ai.decision.unconsciousness;

import java.util.Random;

import de.uni_bremen.factroytrouble.ai.ais.AIPlayer2;
import de.uni_bremen.factroytrouble.ai.ki2.api.decision.consciousness.Approach;
import de.uni_bremen.factroytrouble.ai.ki2.api.decision.unconsciousness.MoodUnit;
import de.uni_bremen.factroytrouble.ai.ki2.api.decision.unconsciousness.Personality;
import de.uni_bremen.factroytrouble.gameobjects.Robot;

/**
 * 
 * @author Sven
 * @version 0.1
 *
 *          Implementiert den Gemütszustand
 */
public class ScullyMoodUnit implements MoodUnit {

    /**
     * Einfluss der Persönlichkeit in %
     */
    private static final int P_INFLUENCE = 80;
    /**
     * Einfluss der Stimmung in %
     */
    private static final int M_INFLUENCE = 19;
    /**
     * Einfluss der Änderung in %
     */
    private static final int C_INFLUENCE = 100 - M_INFLUENCE - P_INFLUENCE;

    /**
     * Gibt an, nach wievielen Approaches der Charakter nervöser wird
     */
    private static final int NR_OF_APPROACHES = 10;

    /**
     * Standartwert
     */
    private static final int DEFAULT = 5;

    /**
     * Maximal Wert
     */
    private static final int MAX = 10;

    /**
     * Minimalwert
     */
    private static final int MIN = 1;

    /**
     * Die höchste Zahl die als Random ausgegeben werden soll
     */
    private static final int RAN_MAX = 100;

    /**
     * gibt an, wie hoch die Chance ist sich vom Approach die Stimmung ändern zu
     * lassen in %
     */
    private static final int CHANCE_TO_CHANGE_MOOD = 20;

    private static final int MAX_CHANGE_VALUE = 5;

    private Random random;

    /**
     * Parameter die die Stimmung ausmachen
     */
    private int aggression;
    private int anxiety;
    private int gloomy;
    private int lazy;
    private int flurries;

    private int health;
    private Robot robot;

    private ScullyPersonality person;
    private int approachCount;

    /**
     * Konstruktor für Standartwerte bei 5.
     * 
     * @param robot
     */
    public ScullyMoodUnit(Robot robot, AIPlayer2 ai) {
        setAttr(DEFAULT, DEFAULT, DEFAULT, DEFAULT, MIN);
        this.robot = robot;
        health = robot.getHP();
        person = (ScullyPersonality) ai.getPersonality();
        random = new Random();
    }

    /**
     * Konstruktor NUR für Tests
     * 
     * @param robot
     * @param personality
     * @param rand
     *            Seeded random
     */
    public ScullyMoodUnit(Robot robot, Personality personality, Random rand) {
        setAttr(DEFAULT, DEFAULT, DEFAULT, DEFAULT, MIN);
        this.robot = robot;
        health = robot.getHP();
        person = (ScullyPersonality) personality;
        random = rand;
    }

    @Override
    public int getAggressivity() {
        return aggression;
    }

    @Override
    public void setAggressivity(int aggro) {
        if (aggro > MAX) {
            aggression = MAX;
        }
        if (aggro < MIN) {
            aggression = MIN;
        }
        if (aggro >= MIN && aggro <= MAX) {
            aggression = aggro;
        }
    }

    @Override
    public int getAnxiety() {
        return anxiety;
    }

    @Override
    public void setAnxiety(int anxiety) {
        if (anxiety > MAX) {
            this.anxiety = MAX;
        }
        if (anxiety < MIN) {
            this.anxiety = MIN;
        }
        if (anxiety >= MIN && anxiety <= MAX) {
            this.anxiety = anxiety;
        }
    }

    @Override
    public int getGloomyness() {
        return gloomy;
    }

    @Override
    public void setGloomyness(int gloomyness) {

        if (gloomyness > MAX) {
            gloomy = MAX;
        }
        if (gloomyness < MIN) {
            gloomy = MIN;
        }
        if (gloomyness >= MIN && gloomyness <= MAX) {
            gloomy = gloomyness;
        }
    }

    @Override
    public int getLazyness() {
        return lazy;
    }

    @Override
    public void setLazyness(int lazyness) {
        if (lazyness > MAX) {
            lazy = MAX;
        }
        if (lazyness < MIN) {
            lazy = MIN;
        }
        if (lazyness >= MIN && lazyness <= MAX) {
            lazy = lazyness;
        }
    }

    @Override
    public int getFlurry() {
        return flurries;
    }

    @Override
    public void setFlurry(int pFlurry) {
        if (pFlurry > MAX) {
            flurries = MAX;
        }
        if (pFlurry < MIN) {
            flurries = MIN;
        }
        if (pFlurry >= MIN && pFlurry <= MAX) {
            flurries = pFlurry;
        }
    }

    @Override
    public void updateMood(Approach approach, boolean first) {
        if (!first) {
            approachCount++;
        } else {
            approachCount = 1;
        }
        if (getRandom() % (100 / CHANCE_TO_CHANGE_MOOD) != 0) {
            return; // Bricht ab, wenn Stimmung nicht beeinflusst wird
        }
        if (health != robot.getHP() && first) { // Hat schaden bekommen
            health = robot.getHP();
            int dif = health - robot.getHP();
            setAggressivity((person.getAggression() * P_INFLUENCE + getAggressivity() * M_INFLUENCE
                    - C_INFLUENCE * dif * salt()) / 100);
        }

        flagPossible(approach);

        if (approachCount % NR_OF_APPROACHES == 0) {
            setFlurry((person.getFlurries() * P_INFLUENCE + getFlurry() * M_INFLUENCE + C_INFLUENCE * salt()) / 100);
        }

    }

    /**
     * Gibt einen "zufälligen" Wert zurück, der maximal {@code MAX_CHANGE_VALUE}
     * ist um nicht immer den Selben Wert für veränderungen zu haben
     * 
     * @return Eine Zahl zwischen 0 und {@code MAX_CHANGE_VALUE}
     */
    private int salt() {
        return random.nextInt((MAX_CHANGE_VALUE) + 1);
    }

    /**
     * Gibt eine "zufällige" Zahl zwischen 1 und {@code RAN_MAX} zurück.
     * 
     * @return Die zufällige Zahl
     */
    private int getRandom() {
        int i = random.nextInt((RAN_MAX) + 1);
        return i == 0 ? 1 : i;
    }

    /**
     * Verändert die Aggression und Gloomyness wenn eine Flagge erreicht werden
     * kann. Ferner wird die Flurries wieder auf default gesetzt.
     * 
     * @param approach
     *            Der Approach der ggf die Flagge erreichen kann
     */
    private void flagPossible(Approach approach) {
        if (approach.canReachNextFlag()) {
            setGloomyness(
                    (person.getGloomy() * P_INFLUENCE + getGloomyness() * M_INFLUENCE - C_INFLUENCE * salt()) / 100);
            setAggressivity(
                    (person.getAggression() * P_INFLUENCE + getAggressivity() * M_INFLUENCE - C_INFLUENCE * salt())
                            / 100);
            setFlurry(person.getFlurries());
        }
    }

    private void setAttr(int pAgression, int pAnexity, int pGloomy, int pLazy, int pFlurries) {
        aggression = pAgression;
        anxiety = pAnexity;
        gloomy = pGloomy;
        lazy = pLazy;
        flurries = pFlurries;
    }

}
