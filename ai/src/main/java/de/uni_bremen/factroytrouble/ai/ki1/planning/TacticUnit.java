/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki1.planning;

import java.io.IOException;
/**
 * Falko
 */
import java.util.Random;

import org.apache.log4j.Logger;

import de.uni_bremen.factroytrouble.ai.ki1.configreader.AgentConfigReader;
import de.uni_bremen.factroytrouble.api.ki1.planning.FuturePlanning;
import de.uni_bremen.factroytrouble.exceptions.KeyNotFoundException;
import de.uni_bremen.factroytrouble.exceptions.KeyNotFoundRuntimeException;
import de.uni_bremen.factroytrouble.gameobjects.Robot;

public class TacticUnit {
    public enum Tactics {
        CAMPEN, PUPETEER, FACEMELT, VINDIESEL, LAZARUS, VENDETTA, ROBINSONCRUSO, PANIC
    }

    private static final Logger LOGGER = Logger.getLogger(FuturePlanning.class);
    private Tactics tactic;
    private Robot target;
    private int powerLevel;
    private Random random;
    private AgentConfigReader cnfg;

    public TacticUnit(Tactics tactic) {
        this.setTactic(tactic);
    }

    public TacticUnit(Tactics tactic, Robot target) {
        try {
            cnfg = AgentConfigReader.getInstance(1);
        } catch (IOException e) {
            LOGGER.error("TaticUnit Key not found!", e);
            throw new KeyNotFoundRuntimeException();
        }
        this.setTactic(tactic);
        this.setTarget(target);
        try {
            this.setPowerLevel(cnfg.getIntProperty("Tactics." + tactic.toString()));
        } catch (KeyNotFoundException e) {
            LOGGER.error("TacticUnit Key not found!", e);
            throw new KeyNotFoundRuntimeException();
        }
    }

    public Tactics getTactic() {
        return tactic;
    }

    public void setTactic(Tactics tactic) {
        this.tactic = tactic;
    }

    public Robot getTarget() {
        return target;
    }

    public void setTarget(Robot target) {
        this.target = target;
    }

    public int getPowerLevel() {
        return powerLevel;
    }

    public void setPowerLevel(int powerLevel) {
        this.powerLevel = powerLevel;
    }
}
