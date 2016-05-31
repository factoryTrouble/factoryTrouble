/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.api.ki1.memory;

import de.uni_bremen.factroytrouble.exceptions.KeyNotFoundException;

/**
 * Stellt die Verbinung zwischen {@link ChunkUnit}s dar. Besitzt eine Distanz die
 * bestimmt, wie lange der Abruf eines {@link ChunkUnit}s dauert und eine Stärke,
 * die beinflusst, wie schnell die Distanz über die Zeit zunimmt.
 * 
 * @author Pablo
 *
 */

public interface Edge {

    
    /**
     * Lässt die Edge anhand der übergebenen Zeit abschwächen. Zeitangaben in
     * milli-sekunden
     * 
     * @param timePassed
     *            in ms
     * @throws KeyNotFoundException
     */
    public void decay(Integer timePassed) throws KeyNotFoundException;

    /**
     * Erhöht die Verbindungsstärke der Edge. Nutzt dafür die Edge.Harden
     * Property aus agent_1.properties
     * 
     * @throws KeyNotFoundException
     */
    public void harden() throws KeyNotFoundException;

    /**
     * Verlängert die Edge um den übergebenen Wert
     * 
     * @param increase
     */
    public void lengthen(Integer increase);

    /**
     * Verringert die Länge der Edge auf den Default Wert. Nutzt dafür den
     * Edge.DefaultLength wert aus agent_1.properties.
     * 
     * @throws KeyNotFoundException
     */
    public void shorten() throws KeyNotFoundException;

    /**
     * Verringert die Verbindungsstärke der Edge. Ist die Umkehrfunktion zu
     * Edge.harden. Nutzt dafür den Edge.Harden wert aus agent_1.properties.
     * 
     * @throws KeyNotFoundException
     */
    public void weaken() throws KeyNotFoundException;

    /**
     * Gibt den Vorgägner Chunk an der Edge zurück.
     * 
     * @return Vorgänger Chunk
     */
    public Chunk getPre();

    public void setPre(Chunk pre);

    /**
     * Gibt den Nachfolger Chunk an der Edge zurück.
     * 
     * @param pre
     *            Nachfolger Chunk
     */
    public Chunk getSucc();

    public void setSucc(Chunk succ);

    public Double getStrength();

    public void setStrength(Double strength);

    public int getLength();

    public void setLength(int length);

    public void setDeclerative(Boolean d);

    public Boolean getDeclerative();

    @Override
    public String toString();
    
}
