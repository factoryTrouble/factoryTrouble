/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.player;

import java.util.List;

import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.observer.GameObserver;

/**
 * Repräsentiert einen Spieler. Jeder Spieler kann auf seine Programmkarten und
 * seinen zugehörigen Roboter zugreifen.
 *
 * @author Simon, Thorben
 *
 */
public interface Player {

    /**
     * Gibt dem Spieler die übergebenen Programmkarten auf die Hand.
     *
     * @param cards
     */
    void giveCards(final List<ProgramCard> cards);

    /**
     * Entfernt alle Programmkarten auf der Hand des Spielers.
     */
    List<ProgramCard> discardCards();

    /**
     * Füllt den angegebenen Register des Roboters, sofern dieser nicht bereits
     * gefüllt ist, mit der übergebenen Karte und entfernt sie aus der Hand des
     * Spielers; befindet sich in dem Register bereits eine Karte, wird diese
     * aus dem Register entfernt, der Hand des Spielers hinzugefügt und
     * zurückgegeben; Ist das Register gelockt, wird die gleiche Karte
     * zurückgegeben, die in das Register gefüllt werden sollte
     * 
     * @param register
     *            die Nummer des Registers
     * @param card
     *            die Programmkarte, mit der das Register gefüllt werden soll
     * @return die ProgramCard, die sich nach der Operation nicht (mehr) im
     *         Register befindet
     */
    ProgramCard fillRegister(int register, ProgramCard card);
    
    /**
     * Entfernt die Karte im angegebenen Register und fügt diese wieder den 
     * Handkarten des Spielers hinzu.
     * 
     * @param register
     *          Index des zu leerenden Registers
     */
    void emptyRegister(int register);

    /**
     * Füllt alle leeren Register des Roboters mit den übrigen Handkarten des
     * Spielers zufällig auf
     */
    void fillEmptyRegisters();

    /**
     * Beendet den aktiven Zug des Spielers.
     */
    void finishTurn();

    boolean isDone();

    Robot getRobot();

    List<ProgramCard> getPlayerCards();

    /**
     * Fügt dem Spieler einen Observer hinzu
     * 
     * @param observer
     */
    void attachObserver(GameObserver observer);

    /**
     * Entfernt einen Observer wieder
     */
    void removeObserver(GameObserver observer);

}
