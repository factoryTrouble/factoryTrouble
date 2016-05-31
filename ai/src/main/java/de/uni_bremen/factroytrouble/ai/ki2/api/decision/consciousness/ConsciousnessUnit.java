/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki2.api.decision.consciousness;

import java.util.List;

import de.uni_bremen.factroytrouble.ai.ki2.ai.decision.consciousness.Aim;
import de.uni_bremen.factroytrouble.ai.ki2.ai.decision.consciousness.DecisionFlow;
import de.uni_bremen.factroytrouble.ai.ki2.ai.decision.consciousness.InfluenceBase;
import de.uni_bremen.factroytrouble.ai.ki2.api.memory.Thought;
import de.uni_bremen.factroytrouble.ai.ki2.api.planning.Strategy;

public interface ConsciousnessUnit {
    /**
     * eigene Main-Methode
     */
    void startBeing();

    /**
     * Gibt den spezifisch angefragten Gedanken zurück
     * 
     * @param keys,
     *            Suchbegriffe (Anfrage) die in der Erinnerung gesucht werden
     * @return Die angefragte Erinnerung
     */
    Thought getInformation(List<String> keys);

    /**
     * Gibt den spezifisch angefragten Gedanken zurück
     * 
     * @param key,
     *            Anfrage (eigene Datenstruktur) die in der Erinnerung gesucht
     *            wird
     * @return Die angefragte Erinnerung
     */
    Thought getInformation(Thought key);

    /**
     * Entscheidet welche Strategie gewählt wird um zu plannen. Wird von der
     * Persönlichkeit und der Laune beeinflusst
     * 
     * @return, die zur Plannung gewählte strategie
     */
    Strategy decideForStrategy();

    /**
     * Bewertet den Approach (Vorgehensweise) nach bestimmten Kriterien.
     * 
     * @param approach,
     *            zu überprüfende Vorgehensweise
     * @return {@code false}, eine neue Vorgehensweise angefordert, sonst wird
     *         die Vorgehensweise akzeptiert
     */
    boolean decide(Approach approach, int amountOfPlans);

    /**
     * Versetzt die PlanningUnit in ein Panicmodus, in dem schneller geplant
     * werden muss
     */
    void panic();

    /**
     * Gibt die Restzeit des Zuges zurück
     * 
     * @return die Anzahl der Restzeit in Sekunden
     */
    int getTime();
    
    
    boolean isSuitingApproach(Approach approach, Aim aim);
    
    
    DecisionFlow getFlow();

    /**
     * Gibt die InfluenceBase zurück
     * 
     * @return die InfluenceBase
     */
    InfluenceBase getInfluence();

    
    Aim makeCompromiss(Aim aim);
}
