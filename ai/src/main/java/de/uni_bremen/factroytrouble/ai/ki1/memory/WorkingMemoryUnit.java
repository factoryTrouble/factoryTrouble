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
import java.util.Set;

import javax.management.RuntimeErrorException;

import org.apache.log4j.Logger;

import de.uni_bremen.factroytrouble.api.ki1.memory.Chunk;
import de.uni_bremen.factroytrouble.api.ki1.memory.Edge;
import de.uni_bremen.factroytrouble.api.ki1.memory.Key;
import de.uni_bremen.factroytrouble.exceptions.KeyNotFoundException;

/**
 *
 * @author Simon
 *
 */
public class WorkingMemoryUnit {
    private static final Logger LOGGER = Logger.getLogger("workingMemory");
    private static final Integer MAXSIZE = 7;
    private Integer currentSize;

    private Integer time;  // virtuelle Zeit, um Chunks Zeitstempel zuzuweisen

    private Chunk root;

    public WorkingMemoryUnit() {
        time = 0;
        currentSize = 0;
        root = new ChunkUnit();
        root.setSecEdges(new ArrayList<Edge>());
    }

    public Chunk getRoot() {
        return root;
    }

    /**
     * Gibt die Anzahl der Elemente im Working Memory zurück.
     */
    public Integer getSize() {
        return currentSize;
    }

    /**
     * Durchsucht das gesamte Working Memory anhand des Schlüssels {@code key}.
     * Ist die Suche nicht erfolgreich, so wird {@code null} zurückgegeben.
     *
     * @param key
     */
    public Chunk retrieveChunk(Key key) {
        List<Edge> edges = root.getSecEdges();
        Chunk chunk;
        for (Edge edge : edges) {
            chunk = edge.getSucc();
            if (chunk != null && chunk.getKey() != null && chunk.getKey().equals(key)) {
                return chunk;
            }
        }
        return null;
    }

    /**
     * Fügt dem Working Memory einen neuen Wert {@code data} mit dem Schlüssel
     * {@code key} hinzu. Existiert bereits ein solcher Schlüssel, wird der
     * vorherige Wert überschrieben.
     *
     * Ist die maximale Größe erreicht, wird der älteste Wert vor dem Hinzufügen
     * entfernt.
     *
     * @param key
     * @param data
     * @throws IOException
     */
    public void storeChunk(KeyUnit key, Object data) throws IOException {
        Chunk chunkToAdd = new ChunkUnit(new ArrayList<Edge>(), key, data);
        chunkToAdd.setTimestampAdded(time);
        Set<Chunk> adjacentChunks = root.getAdjacentChunks();
        // Memory ist leer: neue Kante mit neuem Chunk anlegen
        if (adjacentChunks.isEmpty()) {
            root.addEdge(addEdge(chunkToAdd, true));
            currentSize++;
            time++;
            return;
        }
        // überschreibe Wert von existierenden Knoten,
        // wenn einer mit key gefunden werden kann
        for (Chunk chunk : adjacentChunks) {
            if (chunk.getKey() != null && chunk.getKey().equals(key)) {
                chunk.setData(data);
                time++;
                return;
            }
        }
        List<Edge> edgesToBeRemoved = new ArrayList<Edge>();
        // maximale Größe wurde erreicht -> lösche ältestes Element
        if (currentSize.equals(MAXSIZE)) {
            Chunk oldestChunk = getOldestChunk();
            for (Edge edge : root.getSecEdges()) {
                if (edge.getSucc() != null && edge.getSucc().equals(oldestChunk)) {
                    // entferne oldestChunk von der Kante: setSucc(null)
                    edge.setSucc(null);
                    // entferne die Kante, die mit dem ältesten Chunk verbunden war, von der Wurzel
                    edgesToBeRemoved.add(edge);
                    currentSize--;
                }
            }
        }
        // entferne alle Kanten, die auf den ältesten Chunk zeigen
        for (Edge edge : edgesToBeRemoved) {
            root.removeEdge(edge);
        }

        // chunk mit diesem key existiert noch nicht --> lege neuen chunk mit
        // neuer edge an
        root.addEdge(addEdge(chunkToAdd, true));
        currentSize++;
        time++;
    }

    public void memoryDecay(int passedTime) throws KeyNotFoundException {
        for (Edge e : root.getSecEdges()) {
            e.decay(passedTime);
        }
    }

    /**
     * Bestimmt den Chunk, dessen Zeitstempel am kleinsten ist, d.h. dessen
     * Hinzufügezeit am längsten her ist.
     */
    public Chunk getOldestChunk() {
        Set<Chunk> adjacentChunks = root.getAdjacentChunks();
        Chunk oldestChunk = adjacentChunks.iterator().next();
        for (Chunk chunk : adjacentChunks) {
            if (chunk.getTimestampAdded() < oldestChunk.getTimestampAdded()) {
                oldestChunk = chunk;
            }
        }
        return oldestChunk;
    }

    /**
     * Erzeugt eine neue Kante, die an root gebunden ist und als
     * Nachfolgerknoten den übergebenen chunk enthält.
     *
     * @return die erzeugte Kante
     */
    private Edge addEdge(Chunk chunk, Boolean isSuccessor) {
        Edge edge;
        try {
            if (isSuccessor) {
                edge = new EdgeUnit(root, chunk);
            } else {
                edge = new EdgeUnit(chunk, root);
            }
        } catch (KeyNotFoundException | IOException e) {
            // Fall wird nicht auftreten bei bugfreier Software
            LOGGER.error("Exception:", e);
            throw new RuntimeErrorException(new Error(e.getMessage(), e));
        }
        return edge;
    }
}
