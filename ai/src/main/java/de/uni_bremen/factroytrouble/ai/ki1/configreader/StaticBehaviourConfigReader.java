/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki1.configreader;

import de.uni_bremen.factroytrouble.ai.ki1.behavior.Attitude;
import de.uni_bremen.factroytrouble.ai.ki1.behavior.Mood;
import de.uni_bremen.factroytrouble.ai.ki1.behavior.RiskReadiness;
import de.uni_bremen.factroytrouble.exceptions.KeyNotFoundException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * In jeder *.properties-Datei im Verzeichnis behaviour werden die statischen
 * Charakter-Eigenschaften gespeichert. Unterstützt werden die Eigenschaften
 * mood, attitude und riskreadiness.
 *
 * @author Simon Liedtke
 */
public class StaticBehaviourConfigReader extends ConfigReader {
    
    private static final Logger LOGGER = Logger.getLogger(StaticBehaviourConfigReader.class);
    
    private static Map<String, StaticBehaviourConfigReader> instances;

    private StaticBehaviourConfigReader(String path) throws IOException {
        super(path);
    }

    public static synchronized StaticBehaviourConfigReader getInstance(String name) throws IOException {
        String newname;
        newname=name.replace(" ", "").toLowerCase();
        String path = String.format("/behaviour/%s.properties", newname);
        if (instances == null) {
            instances = new HashMap<String, StaticBehaviourConfigReader>();
            instances.put(newname, new StaticBehaviourConfigReader(path));
        } else if (!instances.containsKey(newname)) {
            instances.put(newname, new StaticBehaviourConfigReader(path));
        }
        return instances.get(newname);
    }

    /**
     * @return {@code true}, wenn sich die Mood während der Laufzeit ändern kann,
     *         sonst {@code false}
     */
    public boolean doesMoodAlternate() {
        try {
            return getBoolProperty("mood_alternates");
        } catch (KeyNotFoundException e) {
            LOGGER.error("Der Key mood_alternates konnte nicht gefunden werden, return standartwert true", e);
            return true;
        }
    }

    /**
     * @return {@code true}, wenn sich die RiskReadiness während der Laufzeit ändern kann,
     *         sonst {@code false}
     */
    public boolean doesRiskReadinessAlternate() {
        try {
            return getBoolProperty("riskreadiness_alternates");
        } catch (KeyNotFoundException e) {
            LOGGER.error("Der Key riskreadiness_alternates konnte nicht gefunden werden, return standartwert true", e);
            return true;
        }
    }
    
    public boolean doesAttitudeGetAggro() {
        try {
            return getBoolProperty("attitude_aggro");
        } catch (KeyNotFoundException e) {
            LOGGER.error("Der Key attitude_aggro konnte nicht gefunden werden, return standartwert false", e);
            return false;
        }
    }

    public boolean doesAttitudeGetDeffi() {
        try {
            return getBoolProperty("attitude_deffi");
        } catch (KeyNotFoundException e) {
            LOGGER.error("Der Key attitude_deffi konnte nicht gefunden werden, return standartwert false", e);
            return false;
        }
    }
    
    public Mood getMood() {
        try {
            String mood = getStringProperty("mood");
            return Mood.valueOf(mood.toUpperCase());
        } catch (KeyNotFoundException e) {
            LOGGER.error("Der Key mood konnte nicht gefunden werden, return standartwert happy", e);
            return Mood.HAPPY;
        }
    }

    public Attitude getAttitude() {
        try {
            String attitude = getStringProperty("attitude");
            return Attitude.valueOf(attitude.toUpperCase());
        } catch (KeyNotFoundException e) {
            LOGGER.error("Der Key attitude konnte nicht gefunden werden, return standartwert neutral", e);
            return Attitude.NEUTRAL;
        }
    }

    public RiskReadiness getRiskReadiness() {
        try {
            String riskreadiness = getStringProperty("riskreadiness");
            return RiskReadiness.valueOf(riskreadiness.toUpperCase());
        } catch (KeyNotFoundException e) {
            LOGGER.error("Der Key riskreadiness konnte nicht gefunden werden, return standartwert average", e);
            return RiskReadiness.AVERAGE;
        }
    }
}
