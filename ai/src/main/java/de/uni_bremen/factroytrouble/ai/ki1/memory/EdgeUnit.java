/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki1.memory;

import java.io.IOException;

import de.uni_bremen.factroytrouble.ai.ki1.configreader.AgentConfigReader;
import de.uni_bremen.factroytrouble.api.ki1.memory.Chunk;
import de.uni_bremen.factroytrouble.api.ki1.memory.Edge;
import de.uni_bremen.factroytrouble.exceptions.KeyNotFoundException;

/**
 * Stellt die Verbinung zwischen {@link ChunkUnit}s dar. Besitzt eine Distanz die
 * bestimmt, wie lange der Abruf eines {@link ChunkUnit}s dauert und eine Stärke,
 * die beinflusst, wie schnell die Distanz über die Zeit zunimmt.
 * 
 * @author Roland, Falko
 *
 */
public class EdgeUnit implements Edge{
    private AgentConfigReader cnfg;
    private Chunk pre;
    private Chunk succ;
    private Double strength;
    private int length;
    private Boolean declerative;

    public EdgeUnit(Chunk pre, Chunk succ) throws IOException, KeyNotFoundException {
        declerative = false;
        cnfg = AgentConfigReader.getInstance(1);
        strength = cnfg.getDoubleProperty("Edge.DefaultStrength");
        this.pre = pre;
        this.succ = succ;
    }

    /**
     * Erstellt eine neue nicht-deklarative Edge
     * 
     * @throws KeyNotFoundException
     * @throws IOException 
     */
    public EdgeUnit() throws KeyNotFoundException, IOException {
        this.declerative = false;
        cnfg = AgentConfigReader.getInstance(1);
        this.strength = cnfg.getDoubleProperty("Edge.DefaultStrength");
    }

    /**
     * Lässt die Edge anhand der übergebenen Zeit abschwächen. Zeitangaben in
     * milli-sekunden
     * 
     * @param timePassed
     *            in ms
     * @throws KeyNotFoundException
     */
    @Override
    public void decay(Integer timePassed) throws KeyNotFoundException {
        if (!this.declerative) {
            length = (int) java.lang.Math
                    .round(length + (timePassed * strength * cnfg.getDoubleProperty("Edge.LengthDecay")));
        }
    }

    /**
     * Erhöht die Verbindungsstärke der Edge. Nutzt dafür die Edge.Harden
     * Property aus agent_1.properties
     * 
     * @throws KeyNotFoundException
     */
    @Override
    public void harden() throws KeyNotFoundException {
        this.strength = this.strength * cnfg.getDoubleProperty("Edge.Harden");
    }

    /**
     * Verlängert die Edge um den übergebenen Wert
     * 
     * @param increase
     */
    @Override
    public void lengthen(Integer increase) {
        this.length = length + increase;
    }

    /**
     * Verringert die Länge der Edge auf den Default Wert. Nutzt dafür den
     * Edge.DefaultLength wert aus agent_1.properties.
     * 
     * @throws KeyNotFoundException
     */
    @Override
    public void shorten() throws KeyNotFoundException {
        this.length = cnfg.getIntProperty("Edge.DefaultLength");
    }

    /**
     * Verringert die Verbindungsstärke der Edge. Ist die Umkehrfunktion zu
     * Edge.harden. Nutzt dafür den Edge.Harden wert aus agent_1.properties.
     * 
     * @throws KeyNotFoundException
     */
    @Override
    public void weaken() throws KeyNotFoundException {
        this.strength = this.strength * (1 / cnfg.getDoubleProperty("Edge.Harden"));
        if (1 < strength) {
            strength = 1.0;
        }
    }

    /**
     * Gibt den Vorgägner Chunk an der Edge zurück.
     * 
     * @return Vorgänger Chunk
     */
    @Override
    public Chunk getPre() {
        return pre;
    }
    
    @Override
    public void setPre(Chunk pre) {
        this.pre = pre;
    }

    /**
     * Gibt den Nachfolger Chunk an der Edge zurück.
     * 
     * @param pre
     *            Nachfolger Chunk
     */
    @Override
    public Chunk getSucc() {
        return succ;
    }

    @Override
    public void setSucc(Chunk succ) {
        this.succ = succ;
    }

    @Override
    public Double getStrength() {
        return strength;
    }

    @Override
    public void setStrength(Double strength) {
        this.strength = strength;
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public void setDeclerative(Boolean d) {
        this.declerative = d;
    }

    @Override
    public Boolean getDeclerative() {
        return this.declerative;
    }

    @Override
    public String toString() {
        return "Edge [pre=" + pre + ", succ=" + succ + ", strength=" + strength + ", length=" + length
                + ", declerative=" + declerative + "]";
    }
}
