/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki2.api.memory;

import java.util.List;

public interface Memory {

    /**
     * Gibt den spezifisch angefragten Gedanken zurück
     * 
     * @param keys,
     *            Suchbegriffe (Anfrage) die in der Erinnerung gesucht werden
     * @return Die angefragte Erinnerung
     */
    Thought getInformation(List<String> keys);

    /**
     * Speichert Gedanken in den unterschiedlichen Memories, verbindet
     * assozierte Gedanken
     * 
     * @param thought,
     *            der zu speichernde Gedanke
     */
    void storeInMemory(Thought thought);
}
