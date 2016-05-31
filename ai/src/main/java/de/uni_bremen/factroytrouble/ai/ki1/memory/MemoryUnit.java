/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki1.memory;

import de.uni_bremen.factroytrouble.api.ki1.memory.Chunk;
import de.uni_bremen.factroytrouble.api.ki1.memory.Key;
import de.uni_bremen.factroytrouble.api.ki1.memory.LongtermMemory;
import de.uni_bremen.factroytrouble.api.ki1.memory.Memory;
import de.uni_bremen.factroytrouble.exceptions.KeyNotFoundException;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Die MemoryUnit abstrahiert die anderen Memory-Klassen und stellt eine
 * Schnittstelle für alle Klassen bereit, die auf das Gedächtnis zugreifen
 * müssen.
 * 
 * @author Simon
 *
 */
public class MemoryUnit implements Memory{
    private static final Logger LOGGER = Logger.getLogger("memory");

    private LTMUnit longtermMemory;
    private WorkingMemoryUnit workingMemory;

    public MemoryUnit() {
        // declarativeMemory = new DeclarativeMemory();  // WTF
        try {
            longtermMemory = new LTMUnit();
        } catch (IOException e) {
            LOGGER.error(e);
            throw new MemoryRuntimeException(e);
        }
        workingMemory = new WorkingMemoryUnit();
    }

    /**
     * Sucht den key zunächst im working memory. Kann dieser dort nicht gefunden
     * werden, wird im long-term memory weitergesucht. Kann der key auch dort
     * nicht gefunden werden, wird {@code null} zurückgegeben.
     */
    public Chunk retrieveChunk(Key key) {
        Chunk chunk = workingMemory.retrieveChunk(key);
        if (chunk == null) {
            try {
                chunk = longtermMemory.retrieveChunk(key);
            } catch (KeyNotFoundException e) {
                LOGGER.info(e);
                chunk = null;
            }
        }
        return chunk;
    }

    /**
     * 
     * @param key
     * @param data
     */
    public void storeChunk(KeyUnit key, Object data) {
        try {
            workingMemory.storeChunk(key, data);
        } catch (IOException e) {
            LOGGER.error(e);
            throw new MemoryRuntimeException(e);
        }
    }

    public LongtermMemory getLongTermMemory(){
        return longtermMemory;
    }
    
    public WorkingMemoryUnit getWorkingMemory(){
        return workingMemory;
    }
}
