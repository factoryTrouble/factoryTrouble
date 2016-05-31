/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.network;

import de.uni_bremen.factroytrouble.board.Board;
import de.uni_bremen.factroytrouble.observer.GameObserver;
import de.uni_bremen.factroytrouble.player.Player;
import de.uni_bremen.factroytrouble.player.ProgramCard;

import java.util.List;

/**
 * Stellt die Methoden zum Zugriff auf die Spielmechanik zur Verfügung
 */
public interface EngineWrapper {

    /**
     * Resettet den Master
     */
    void initMaster();

    /**
     * Gibt den Spieler an dem übergebenen Index zurück
     *
     * @param playerNumber der Spieler Index
     * @return der Spieler
     */
    Player getPlayerByNumber(int playerNumber);

    /**
     * Erzeugt einen Spieler mit dem übergebenen Namen und mit dem übergebenen Typ
     *
     * @param robotName der Name des Roboters
     * @param kiName    der Typ des Spielers(Human,AI_Agent1,...)
     */
    void createPlayer(String robotName, String kiName);

    /**
     * Gibt die Anzahl der Lebenspunkte des Spielers mit dem gegebenen Index zurück
     *
     * @param playerNumber der Spieler Index
     * @return Anzahl der Lebenspunkte
     */
    int getPlayerHP(int playerNumber);

    /**
     * Gibt die Karte aus den Handkarten an der übergebenen Position, des übergebenen Spieler Indexes zurück
     *
     * @param playerNumber der Spieler Index
     * @param cardPos      der Karten Index
     * @return die Karte
     */
    ProgramCard getCard(int playerNumber, int cardPos);

    /**
     * Gibt die Karte aus dem Register an der übergebenen Position, des übergebenen Spieler Indexes zurück
     *
     * @param playerNumber der Spieler Index
     * @param cardPos      der Karten Index
     * @return die Karte
     */
    ProgramCard getCardInRegister(int playerNumber, int cardPos);

    /**
     * Leer den Register des Spielers mit dem übergebenen Index an der übergebenen Position
     *
     * @param playerNumber der Spieler Index
     * @param cardPos      der Register Index
     */
    void emptyRegister(int playerNumber, int cardPos);

    /**
     * Setzt die übergebene Karte an der übergebenen Position für den Spieler an dem übergebenen Index
     *
     * @param playerNumber der Spieler Index
     * @param cardPos      der Register Index
     * @param programCard  die einzusetzende Karte
     */
    void fillRegister(int playerNumber, int cardPos, ProgramCard programCard);

    /**
     * Gibt das Array der gelockeden Register zurück
     *
     * @param playerNumber der Index des Spielers
     * @return die gelockeden Register
     */
    boolean[] getLockedRegisters(int playerNumber);

    /**
     * Gibt die Anzahl der Spieler zurück
     *
     * @return Anzahl der Spieler
     */
    int getPlayerCount();
    /**
     * Startet die Runden Evaluation
     */
    void startRound();

    /**
     * Wechselt auf den ResultScreen im GUI Thread
     */
    void endGame();

    /**
     * Startet eine neue Runde
     */
    void prepareRound();


    /**
     * Gibt die Spieler zurück
     *
     * @return die Spieler
     */
    List<Player> getPlayers();

    /**
     * Gibt die Karten für einen Spieler zurück
     *
     * @param playerNumber die Nummer des Spielers
     * @return die Karten des Spielers
     */
    List<ProgramCard> getCards(int playerNumber);

    /**
     * Fügt den Observer dem Master hinzu
     *
     * @param guiEngineObserver den Observer der Hinzugefügt werden soll
     */
    void attachObserver(GameObserver guiEngineObserver);

    /**
     * Gibt das aktive Board zurück
     *
     * @return das aktive Board
     */
    Board getActiveBoard();

    /**
     * Erzeugt das Board mit dem übergebenen Namen im Master und liefert das erzeugte Board zurück
     *
     * @param boardName der Name des Boards
     * @return das Board mit dem gegebenen Namen
     */
    Board getBoard(String boardName);

    /**
     * Erzeugt das Board auf dem Master
     *
     * @param boardName der Boardname
     */
    void initialiseBoard(String boardName);

    /**
     * Verteilt die Karten auf die Spieler
     */
    void dealCards();

    /**
     * Gibt eine Liste mit den Namen aller Boards zurück
     *
     * @return Liste der Boardnamen
     */
    List<String> getAvailableBoards();

    /**
     * Gibt den
     *
     * @param robotNumber
     * @return
     */
    String getRobotNameByNumber(int robotNumber);
    /**
     * Gibt die Roboter
     *
     * @param robotName
     * @return
     */
    int getPlayerNumberByName(String robotName);

    /**
     * Gibt den Index des Roboters mit diesem Namen im Playerarray im Master zurück
     *
     * @param robotName
     * @return
     */
    int getPlayerIndexInPlayersByName(String robotName);

    /**
     * Entfernt alle Spieler aus dem Spiel
     */
    void removeAllPlayers();

    /**
     * Gibt den Gewinner Spieler zurück oder null, wenn es keinen gibt
     *
     * @return der Gewinner oder Null
     */
    Player getWinner();

    /**
     * Wechselt den Power Down Status für einen Spieler
     * @param playerNumber
     */
    void changePowerDownForPlayer(int playerNumber);

    /**
     * Löscht einen GameMaster
     * @param gameId
     */
    void deleteMaster(int gameId);

    /**
     * Gibt die Anzahl der Flaggen zurück
     * @param board
     * @return
     */
    int getFlagCount(String board);

}
