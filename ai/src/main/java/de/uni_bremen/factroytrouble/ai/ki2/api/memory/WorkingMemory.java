/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki2.api.memory;

import java.util.List;
/**
 * Keys:
 * Flaggen : flag, nr; nextFlag
 * Positionen: my, position; robot, position
 * (Bänder): conveyor, all
 * Orientierung: my, ori
 * Löcher: holes
 * Wände: wall, all; wall,xy
 *
 *
 */

public interface WorkingMemory extends Runnable {

    /**
     * Die von der Visual erhaltenen Informationen werden verarbeitet/gefiltert.
     * Gefilterte Informationen werden an die STM und die LTM geschickt.
     * Gefilterte Informationen sind z.B. Positionen von Feldobjekten.
     */
    void process();

    /**
     * Gibt den spezifisch angefragten Gedanken zurück
     * Bitte Namenskonvention im Javadoc von der getThoughtName Methode von Thougt beachten
     * 
     * @param keys,
     *            Suchbegriffe (Anfrage) die in der Erinnerung gesucht werden
     * @return Die angefragte Erinnerung
     */
    Thought getInformation(List<String> keys);

    /**
     * Gibt den spezifisch angefragten Gedanken zurück
     * Bitte Namenskonvention im Javadoc von der getThoughtName Methode von Thougt beachten
     * 
     * @param key,
     *            Anfrage (eigene Datenstruktur) die in der Erinnerung gesucht
     *            wird
     * @return Die angefragte Erinnerung
     */
    Thought getInformation(Thought key);

    /**
     * Nur von anderer Memory Klasse aus benutzen.
     * @param keys
     * @return
     */
    @Deprecated
    Thought getThoughtFromShortTerm(List<String> keys);
    
    
    void storeInformation(Thought pThought);
}
