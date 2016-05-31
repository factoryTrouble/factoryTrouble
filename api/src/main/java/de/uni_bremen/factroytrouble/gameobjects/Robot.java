/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gameobjects;

import java.util.List;

import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.observer.GameObserver;
import de.uni_bremen.factroytrouble.player.OptionCard;
import de.uni_bremen.factroytrouble.player.ProgramCard;

/**
 * Dieses Interface repräsentiert die Roboter.
 * 
 * @author Thorben
 *
 */
public interface Robot {

    /**
     * Gibt die maximale Anzahl an Registern an, die mit Programmkarten befüllt
     * werden können.
     */
    public static final int MAX_REGISTERS = 5;

    /**
     * Ändert die Ausrichtung des Roboters
     * 
     * @param turnLeft
     *            gibt an, ob sich der Roboter nach links oder rechts drehen
     *            soll
     */
    void turn(boolean turnLeft);

    /**
     * Versetzt den Roboter in den PowerDown-Zustand
     */
    void powerDown();

    /**
     * Versetzt den Roboter in den Wachzustand (= Gegenoperation zu PowerDown)
     */
    void wakeUp();

    /**
     * Senkt die HP des Roboters um 1
     */
    void takeDamage(Object shooter);

    /**
     * Erhöht die HP des Roboters um 1
     */
    void heal();

    /**
     * Erhöht die HP des Roboters auf das Maximum
     */
    void healFully();

    /**
     * Zerstört den Roboter
     */
    void kill();

    /**
     * Füllt einen Register mit einer Programmkarte
     * 
     * @param register
     *            die Nummer des Registers
     * @param card
     *            die Programmkarte zum Befüllen
     * 
     * @return {@code false}, falls das Register gelockt ist
     */
    boolean fillRegister(final int register, final ProgramCard card);

    /**
     * füllt alle noch leeren Register dieses Roboters mit zufällig ausgewählten
     * Karten aus der übergebenen Liste; gibt nicht verwendete Karten zurück
     * 
     * @param cards
     *            die zu verwendende Kartenliste
     * @return eine Liste aller nicht verwendeten Karten
     */
    List<ProgramCard> fillEmptyRegisters(List<ProgramCard> cards);

    /**
     * Leert das angegebene Register
     * 
     * @param register
     *            die Nummer des Registers
     * 
     * @return Aus dem angegebenen Register entfernte Programmkarte
     */
    ProgramCard emptyRegister(final int register);

    /**
     * Leert das gesamte Register des Roboters, sofern kein Register gelockt
     * ist.
     * 
     * @author Stefan
     * 
     * @return Aus den Registern entfernte Programm Karten
     */
    List<ProgramCard> emptyAllRegister();

    /**
     * Ruft die jeweils nächste Programmkarte auf
     */
    void executeNext();

    /**
     * Lockt das nächste freie Register; füllt es mit einer zufälligen
     * Programmkarte vom Deck auf, falls es noch leer ist
     */
    void lockNextRegister();

    /**
     * Lockt das angegebene Register; füllt es mit einer zufälligen
     * Programmkarte vom Deck auf, falls es noch leer ist
     * 
     * @param register
     *            das zu lockende Register
     */
    void lockRegister(final int register);

    /**
     * Entlockt das nächste gelockte Register
     */
    void freeNextRegister();

    /**
     * Entlockt das angegebene Register
     * 
     * @param register
     *            das zu entlockende Register
     */
    void freeRegister(final int register);

    /**
     * Erhöht den Flaggenzähler des Roboters um 1
     */
    void touchFlag();

    Orientation getOrientation();

    int getHP();

    int getLives();

    ProgramCard[] getRegisters();

    /**
     * Gibt den locked-Status aller Register zurück
     * 
     * @return ein Array, welches angibt, welche register gelockt sind
     */
    boolean[] registerLockStatus();

    void setCurrentTile(Tile tile);

    Tile getCurrentTile();

    /**
     * Setzt cen respawn-Punkt des Roboters auf die Kachel, auf der er sich
     * gerade befindet
     */
    void setRespawnPoint();

    Tile getRespawnPoint();

    /**
     * Respawnt den Roboter, sofern möglich
     * 
     * @param Das
     *            Tile, auf dem der Roboter respawnen soll
     * 
     * @return false, falls der Roboter endgültig Tot ist, true sonst
     */
    boolean respawn(Tile tile);

    int getFlagCounterStatus();

    /**
     * Gibt die Priorität der zunächst auszuführenden Programmkarte zurück
     */
    int getNextPriority();

    void addOption(final OptionCard card);

    void removeOption(final OptionCard card);

    List<OptionCard> getOptions();

    boolean isPoweredDown();

    String getName();

    void attachObserver(GameObserver observer);

    void removeObserver(GameObserver observer);

    List<GameObserver> getObserverList();

    Robot clone(Tile clonedTile);
    
}
