/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki1.memory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.uni_bremen.factroytrouble.api.ki1.memory.Chunk;
import de.uni_bremen.factroytrouble.api.ki1.memory.Edge;
import de.uni_bremen.factroytrouble.api.ki1.memory.Key;

/**
 * Repräsentiert einen semantisch zusammenhängenden Satz von Daten, welcher
 * durch {@link EdgeUnit}s mit anderen Datensätzen im Speicher verbunden ist.
 * 
 * @author Roland
 *
 */
public class ChunkUnit implements Chunk {
    private Edge core;
    private List<Edge> secEdges;
    private Key key;
    private Object data;
    private Boolean declerative;
    private Integer threshHold;
    private Integer timestampAdded;

    public ChunkUnit() {
        this.declerative = false;
        secEdges = new ArrayList<Edge>();
    }

    public ChunkUnit(Key key, Object data) {
        this.declerative = false;
        secEdges = new ArrayList<Edge>();
        this.key = key;
        this.data = data;
    }

    public ChunkUnit(List<Edge> secEdges, Key key, Object data) {
        this.declerative = false;
        this.secEdges = secEdges;
        this.key = key;
        this.data = data;
    }

    @Override
    public Edge getCore() {
        return core;
    }

    @Override
    public void setCore(Edge core) {
        this.core = core;
    }

    @Override
    public List<Edge> getSecEdges() {
        return new ArrayList<Edge>(secEdges);
    }

    @Override
    public void setSecEdges(List<Edge> secEdges) {
        this.secEdges = secEdges;
    }

    @Override
    public void removeEdge(Edge edge) {
        this.secEdges.remove(edge);
    }

    @Override
    public Key getKey() {
        return key;
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public void addEdge(Edge edge) {
        this.secEdges.add(edge);
    }

    @Override
    public void setDeclerative(Boolean d) {
        this.declerative = d;
    }

    @Override
    public boolean getDeclerative() {
        return this.declerative;
    }

    @Override
    public void setThreshold(Integer threshold) {
        this.threshHold = threshold;
    }

    @Override
    public Integer getThreshold() {
        return this.threshHold;
    }

    @Override
    public void setKey(Key key) {
        this.key = key;
    }

    @Override
    public Integer getTimestampAdded() {
        return timestampAdded;
    }

    @Override
    public void setTimestampAdded(Integer timestampAdded) {
        this.timestampAdded = timestampAdded;
    }

    /**
     * @return eine Menge aller mit diesem Chunk verbundenen Chunks
     */
    @Override
    public Set<Chunk> getAdjacentChunks() {
        Set<Chunk> chunks = new HashSet<Chunk>();
        for (Edge edge : getSecEdges()) {
            if(edge.getSucc() == null) {
                continue;
            }
            chunks.add(edge.getSucc());
        }
        return chunks;
    }

    @Override
    public String toString() {
        return "Chunk [key=" + key + ", data=" + data + ", declerative=" + declerative + "]";
    }

    /**
     * Zwei Chunks sind dann ungleich, wenn einer der folgenden Fälle zutrifft
     * (sonst sind sie gleich):
     *      - anderer Chunk ist null oder implementiert nicht das Chunk-Interface
     *      - Anzahl der Nachbarknoten stimmt nicht überein
     *      - Schlüssel sind ungleich
     *      - Schlüssel sind gleich, aber Werte sind ungleich
     *      - Menge der Nachbarknoten stimmen nicht überein
     */
    @Override
    public boolean equals(Object otherChunk) {
        if (otherChunk == null || !(otherChunk instanceof Chunk)) {
            return false;
        }
        ChunkUnit other = (ChunkUnit) otherChunk;
        Set<Chunk> adjacentChunks = getAdjacentChunks();
        Set<Chunk> adjacentChunksOther = other.getAdjacentChunks();
        // Zwei chunks sind ungleich, wenn sie nicht die gleiche Anzahl an
        // Nachbarn haben
        if (adjacentChunks.size() != adjacentChunksOther.size()) {
            return false;
        }
        // Zwei chunks sind ungleich, wenn ihre Schlüssel-Wert-Paare nicht
        // übereinstimmen
        Key otherKey = other.getKey();
        Object otherData = other.getData();
        if (!(getKey() != null && getKey().equals(otherKey) && getData() != null && getData().equals(otherData))) {
            return false;
        }
        // Jeder Nachbarknoten aus dem einen chunk muss in der Menge
        // der Nachbarknoten des anderen chunks vorkommen
        outerLoop: for (Chunk chunk : adjacentChunks) {
            for (Chunk chunk_ : adjacentChunksOther) {
                if (chunk.getKey().equals(chunk_.getKey())) {
                    if (!(chunk.getData().equals(chunk_.getData()))) {
                        return false;
                    }
                    // Key und Value stimmen überein
                    // --> mach mit dem nächsten Item aus adjacentChunks weiter
                    continue outerLoop;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((data == null) ? 0 : data.hashCode());
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        return result;
    }
}
