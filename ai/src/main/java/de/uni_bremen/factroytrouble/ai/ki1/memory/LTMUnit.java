/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki1.memory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.uni_bremen.factroytrouble.ai.ki1.configreader.AgentConfigReader;
import de.uni_bremen.factroytrouble.api.ki1.memory.Chunk;
import de.uni_bremen.factroytrouble.api.ki1.memory.Edge;
import de.uni_bremen.factroytrouble.api.ki1.memory.MemoryEvent;
import de.uni_bremen.factroytrouble.api.ki1.memory.Key;
import de.uni_bremen.factroytrouble.api.ki1.memory.LongtermMemory;
import de.uni_bremen.factroytrouble.exceptions.DeclarativeMemoryException;
import de.uni_bremen.factroytrouble.exceptions.KeyNotFoundException;
import de.uni_bremen.factroytrouble.exceptions.LongTermStoreChunkException;

/**
 * Implementiertes Longterm Memory. Speichert Chunks mit Edges am Nexus,
 * verbindet diese und ermöglicht es dend ecay der Edges umzusetzen.
 * 
 * @author Falko
 *
 */
public class LTMUnit implements LongtermMemory {

    // Variables
    private Chunk nexus;
    private List<Edge> edges;
    private List<Chunk> chunks;

    private AgentConfigReader cnfg;

    public LTMUnit() throws IOException {
        nexus = new ChunkUnit();
        edges = new ArrayList<Edge>();
        chunks = new ArrayList<Chunk>();
        cnfg = AgentConfigReader.getInstance(1);
    }

    // FUNKTIONEN

    @Override
    public Chunk retrieveChunk(Key key) throws KeyNotFoundException {
        for (Edge e : nexus.getSecEdges()) {
            if (e.getSucc().getKey().equals(key) && e.getLength() <= cnfg.getIntProperty("LTM.RetrievalThreshold")) {
                e.harden();
                e.shorten();
                return e.getSucc();
            } else {
                for (Edge e2 : e.getSucc().getSecEdges()) {
                    if (e2.getSucc().getKey().equals(key)
                            && (e2.getLength() + e.getLength()) <= (cnfg.getIntProperty("LTM.RetrievalThreshold")
                                    + cnfg.getIntProperty("Chunk.RetrievalThreshold"))) {
                        e.harden(); // TODO Corner Scale
                        e2.harden();
                        e2.shorten();
                        e2.getSucc().getCore().harden();
                        e2.getSucc().getCore().shorten();
                        return e2.getSucc();
                    }
                }
            }
        }
        return null;
    }

    /**
     * Speichert einen Chunk im LTM, nutzt für Strength und Length der Edges die
     * Default Werte.
     * 
     * @throws KeyNotFoundException
     * @throws IOException
     */
    @Override
    public void storeChunk(Chunk chunk) throws LongTermStoreChunkException {
        try {
            if (chunk.getData() instanceof MemoryEvent) {
                storeEvent(chunk);
            } else if (containsChunk(chunk)) {
                Chunk tmpC2 = getExistingChunk(chunk);
                tmpC2.getCore().harden();
                tmpC2.getCore().shorten();
            } else {
                connectChunks(nexus, chunk);
            }
        } catch (IOException | KeyNotFoundException e) {
            throw new LongTermStoreChunkException(chunk.toString(), e);
        }
    }

    /**
     * Sorgt dafürdas alle Edges innehalb des LTM vom decay betroffen werden
     * 
     * @throws KeyNotFoundException
     */
    @Override
    public void memoryDecay(int passedTime) throws KeyNotFoundException {
        for (Edge e : edges) {
            e.decay(passedTime);
        }
    }

    /**
     * Hol einen Chunk anhand eines Keys aus dem LTM Graphen ohne dieses dabei
     * zu beinflussen
     * 
     * @param key
     * @return
     */
    private Chunk internRetrieveChunk(Key key) {
        for (Edge e : this.edges) {
            if (e.getSucc().getKey().equals(key)) {
                return e.getSucc();
            }
        }
        return null;
    }

    /**
     * Private Funktion für den Falld das ein Event gespeichert werden soll.
     * 
     * @param chunk
     * @throws KeyNotFoundException
     * @throws IOException
     */
    private void storeEvent(Chunk chunk) throws KeyNotFoundException, IOException {
        MemoryEvent e = (MemoryEvent) chunk.getData();
        Chunk tmpRobo = this.internRetrieveChunk(new KeyUnit(((RobotEvent) e).getRobot()));

        Boolean added = false;

        for (Edge edge : tmpRobo.getSecEdges()) {
            if (edge.getSucc().getData() instanceof MemoryEvent
                    && ((RobotEvent) e).getEventType().equals(((RobotEvent) edge.getSucc().getData()).getEventType())) {
                MemoryEvent tmpEvent = (RobotEvent) edge.getSucc().getData();
                tmpEvent.increment();
                edge.harden();
                added = true;
            }
        }
        if (!added) {
            connectChunks(nexus, chunk);
            connectChunks(tmpRobo, chunk);
        }

    }

    /**
     * Verbindet zwei Chunks mit einer Edge. LTM Nexus Chunk muss immer der
     * "pre" Uebergabewert sein
     * 
     * @param pre
     *            Vorhergänger
     * @param succ
     *            Nachfolger
     * @throws KeyNotFoundException
     * @throws IOException
     */
    private void connectChunks(Chunk pre, Chunk succ) throws KeyNotFoundException, IOException {

        Edge edge2 = new EdgeUnit();

        edge2.setPre(pre);
        edge2.setSucc(succ);
        edge2.setLength(cnfg.getIntProperty("Edge.DefaultLength"));
        edge2.setStrength(cnfg.getDoubleProperty("Edge.DefaultStrength"));

        pre.addEdge(edge2);
        if (pre == nexus) {
            succ.setCore(edge2);
        } else {
            succ.addEdge(edge2);
        }
        edges.add(edge2);
        addToChunks(pre);
        addToChunks(succ);
    }

    /**
     * Fügt einen Chunk it deklarativem Wissen hinzu, dieses wird vom LTM
     * niemals(!) vergessen.
     * 
     * @param chunk
     * @throws KeyNotFoundException
     * @throws IOException
     */
    public void addDeclerativeKnowledge(ChunkUnit chunk) throws DeclarativeMemoryException {
        chunk.setDeclerative(true);

        EdgeUnit edge3;
        try {
            edge3 = new EdgeUnit();
            edge3.setDeclerative(true);
            edge3.setPre(nexus);
            edge3.setSucc(chunk);
            edge3.setLength(cnfg.getIntProperty("Edge.DeclerativeLength"));
            edge3.setStrength(cnfg.getDoubleProperty("Edge.DeclerativeStrength"));
        } catch (IOException | KeyNotFoundException e) {
            throw new DeclarativeMemoryException(e);
        }

        nexus.addEdge(edge3);
        chunk.setCore(edge3);
        edges.add(edge3);
        addToChunks(chunk);
    }

    public List<Edge> getEdges() {
        return this.edges;
    }

    private Boolean containsChunk(Chunk c) {
        for (Chunk tmpC : chunks) {
            if (tmpC.getData() != null && tmpC.getData().equals(c.getData())) {
                return true;
            }
        }
        return false;
    }

    private Chunk getExistingChunk(Chunk c) {
        for (Chunk tmpC : chunks) {
            if (tmpC.getData() != null && tmpC.getData().equals(c.getData())) {
                return tmpC;
            }
        }
        return null;
    }

    private void addToChunks(Chunk c3) {
        if (!containsChunk(c3)) {
            chunks.add(c3);
        }
    }
    
    @Override
    public List<Chunk> getChunks(){
        return chunks;
    }

}
