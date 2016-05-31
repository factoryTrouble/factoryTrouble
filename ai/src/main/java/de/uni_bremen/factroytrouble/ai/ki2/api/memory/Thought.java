/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki2.api.memory;

import java.util.List;
import java.util.Map;

public interface Thought {
    static final int DEFAULT_CONNECTION = 100;

    /**
     * Verbindet den gedanken mit einem anderen
     * 
     * @param thought,
     *            der Gedanke der mit diesem verbunden werden soll
     */
    void connectThoughts(Thought thought);

    /**
     * Gibt den Gedanken mit den nachfolgenden Gedanken zurück
     * 
     * @return
     */
    Map<Thought, Integer> getThoughts();

    /**
     * Stärkt die Verbindung zwischen den und den übergebenen Gedanken
     * 
     * @param thought,
     *            der Gedanke, der mit dem aktuellen Gedanken gestärkt werden
     *            soll
     * @param value,
     *            Wert um den gestärkt werden soll
     */
    void power(Thought thought, int value);

    /**
     * Schwächt die Verbindung zwischen den und den übergebenen Gedanken. (wird
     * differenziert)
     * 
     * @param thought,
     *            der Gedanke, der mit dem aktuellen Gedanken geschwächt werden
     *            soll
     * @param value,
     *            Wert um den geschwächt werden soll
     */
    void weak(Thought thought, int value);

    /**
     * Gibt den Inhalt des Gedankens wieder, etwa ein String oder Robot
     * Thought Namenskonvention: kleingeschriebene wörter getrennt durch unterstrich
     * Beispiel: flag_1_position
     * 
     * @return
     */
    String getThoughtName();

    /**
     * Ein gedanke kann mehrere Informationen enthalten, können mithilfe dieser
     * methode hinzugefügt werden
     * 
     * @param information,
     *            Information die einem Ggedanken hinzugefügt werden soll
     */
    void addInformationToThought(Object information);

    /**
     * Gibt den Gedanken mit Inhalt zurück
     * 
     * @return, Gedanke mit Inhalt
     */
    List<Object> getInformation();
    
    /**
     * Gibt an ob dieser Gedanke eine Verbindung zu genau dem übergebenen Gedanken hat
     * 
     * @param der Gedanke zu dem eine Verbindung bestehen soll
     * @return true wenn eine Verbindung besteht, sonst false
     */
    boolean hasConnectionToThought(Thought thought);

    /**
     * Gibt an ob dieser Gedanke eine Verbindung zu einem Gedanken hat, der den übergebenen Namen hat
     * 
     * @param der Name den ein Gedanke haben soll, zu dem eine Verbindung besteht
     * @return true wenn eine Verbindung besteht, sonst false
     */
    boolean hasConnectionToName(String name);
    
    
    /** Gibt den Thought mit dem übergebenen Name aus der Connected Liste
     * 
     * @param Der Name des gesuchten Thought
     * @return der Thought oder null
     */
    Thought getConnectedThoughtByName(String name); 
    
    /** Gibt die Stärke der Verbindung zum übergebenen Thought zurück
     * 
     * @param der gesuchte Thought
     * @return die Stärke oder null
     */
    Integer getStrengthOfConnection(Thought other);
}
