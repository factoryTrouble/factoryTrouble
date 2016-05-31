/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki1.behavior;

import de.uni_bremen.factroytrouble.ai.ki1.configreader.StaticBehaviourConfigReader;

import java.io.IOException;

import org.apache.log4j.Logger;

public class StaticBehaviour {
    /**
     * Der Name des Charakters. Wird von {@link readFile()} verwendet, um die dazugehörige Datei zu
     * laden, in der die statischen Informationen zu diesem Charakter gespeichert
     * werden.
     */
    private String name;

    private Mood mood;

    private Boolean moodAlternates;

    private Attitude attitude;

    private RiskReadiness riskReadiness;

    private Boolean riskReadinessAlternates;
    
    private Boolean attitudeIsAggro;
    
    private Boolean attitudeIsDeffi;
    
    private static final Logger LOGGER = Logger.getLogger(StaticBehaviour.class.getName());

    public StaticBehaviour(String name) {
        this.name = name;
    }

    public void readFile(){
        StaticBehaviourConfigReader reader;
        try {
            reader = StaticBehaviourConfigReader.getInstance(name);
        } catch (IOException e) {
            LOGGER.error(e.toString(),e);
            return;
        }
        this.mood = reader.getMood();
        setMoodAlternates(reader.doesMoodAlternate());
        this.attitude = reader.getAttitude();
        setAttitudeIsAggro(reader.doesAttitudeGetAggro());
        setAttitudeIsDeffi(reader.doesAttitudeGetDeffi());
        this.riskReadiness = reader.getRiskReadiness();
        setRiskReadinessAlternates(reader.doesRiskReadinessAlternate());
    }

    public String getName() {
        return name;
    }

    public Mood getMood() {
        return mood;
    }

    public void setMood(Mood mood) {
        if(getMoodAlternates()==null){
            return;
        }
        if (!getMoodAlternates()) {
            throw new RuntimeException(
                    String.format("mood of %s may not change", getName()));
        }
        this.mood = mood;
    }

    public Attitude getAttitude() {
        return attitude;
    }

    public void setAttitude(Attitude attitude) {
        if(getAttitudeIsAggro()== null || getAttitudeIsDeffi()== null){
            return;
        }
        if (getAttitudeIsAggro() && getAttitudeIsDeffi() || !getAttitudeIsAggro() && !getAttitudeIsDeffi()) {
            throw new RuntimeException(
                    String.format("attitude of %s may not change", getName()));
        }
        this.attitude = attitude;
    }

    public RiskReadiness getRiskReadiness() {
        return riskReadiness;
    }

    public void setRiskReadiness(RiskReadiness riskReadiness) {
        if(getRiskReadinessAlternates()==null){
            return;
        }
        if (!getRiskReadinessAlternates()) {
            throw new RuntimeException(
                    String.format("risk readiness of %s may not change", getName()));
        }
        this.riskReadiness = riskReadiness;
    }

    public Boolean getMoodAlternates() {
        return moodAlternates;
    }

    private void setMoodAlternates(Boolean alternates) {
        this.moodAlternates = alternates;
    }

    public Boolean getRiskReadinessAlternates() {
        return riskReadinessAlternates;
    }

    private void setRiskReadinessAlternates(Boolean alternates) {
        this.riskReadinessAlternates = alternates;
    }
    
    public Boolean getAttitudeIsAggro(){
        return attitudeIsAggro;
    }
    
    private void setAttitudeIsAggro(Boolean isAggro){
        this.attitudeIsAggro = isAggro;
    }
    
    public Boolean getAttitudeIsDeffi(){
        return attitudeIsDeffi;
    }
    
    private void setAttitudeIsDeffi(Boolean isDeffi){
        this.attitudeIsDeffi = isDeffi;
    }
}
