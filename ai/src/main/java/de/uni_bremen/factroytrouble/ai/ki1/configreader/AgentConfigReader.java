/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki1.configreader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Der AgentConfigReader liest Eigenschaften der Roboter aus einer Datei aus.
 * Die Datei liegt im ressource-Ordner.
 * 
 * @author Simon Liedtke
 */
public class AgentConfigReader extends ConfigReader {
    private static Map<Integer, AgentConfigReader> instances;

    private AgentConfigReader(String path) throws IOException {
        super(path);
    }

    /**
     * Gibt die Instanz des ConfigReaders für den Agenten mit der Übergebenen
     * Nummer zurück.<br>
     * Die übergebene Nummer wird auf die properties Datei mit selbigen Nummer
     * gemapped.<br>
     * Wird der Int ''1'' übergeben, wird die properties Datei
     * ''agent_1.properties'' geladen.
     *
     * @param number
     *            des Agenten
     * @return Instanz des ConfigReader für Agenten
     * @throws IOException
     */
    public static synchronized AgentConfigReader getInstance(int number) throws IOException {
        String path = String.format("/ki1/config/agent_%d.properties", number);
        if (instances == null) {
            instances = new HashMap<Integer, AgentConfigReader>();
            instances.put(number, new AgentConfigReader(path));
        } else if (!instances.containsKey(number)) {
            instances.put(number, new AgentConfigReader(path));
        }
        return instances.get(number);
    }
}
