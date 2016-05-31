/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.player;

import de.uni_bremen.factroytrouble.board.Board;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.observer.GameObserver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Repräsentiert den Spielleiter. Der Spielleiter hat Zugriff auf das Spielbrett
 * und alle teilnehmenden Spieler.
 * 
 * @author Thorben
 *
 */
public interface Master {
    /**
     * Gibt die verfügbaren Roboternamen an
     */
    public static final List<String> ROBOT_NAMES = new ArrayList<>(Arrays.asList(new String[] { "MACE BOT",
            "TITAN T17", "WHIRLBOT", "POUND BOT", "LETHAR BOT", "FLINCH BOT", "FUNKEY", "SQUINT BOT" }));

    /**
     * Gibt die verfügbaren KI-Namen an. namen können ohne viel Aufwand
     * angepasst werden
     */
    public static final List<String> KI_NAMES = new ArrayList<>(Arrays.asList(new String[] { "AIPlayer1", "AIPlayer2", "ActRAIPlayer", "AIPlayer4", "AI_Classic" }));

    /**
     * Gibt die maximale Spielzeit für jenen Spieler an, der als letztes noch
     * seine Programmkarten verteilen muss
     */
    public static final int TIMER = 30;

    /**
     * Gibt die Maximalanzahl an Spielern an
     */
    public static final int MAX_PLAYERS = 8;

    /**
     * Wird ausgeführt wenn alle bis auf den letzten Spieler mit ihren Zug
     * fertig sind. Zählt TIMER Sekunden runter. Ist die Zeit abgelaufen, werden
     * noch leere Register zufällig mit Programmkarten aus der Hand des Spielers
     * aufgefüllt
     * 
     * @return 0
     */
    double countdown();

    /**
     * @author ToFy
     * 
     *         Wird von GUI nach Mitteilung von dessen Observer abgefragt, um
     *         die aktuelle Countdownzahl abzufragen.
     * 
     * @return den aktuellen Tick des Countdown
     */
    int getCountdownTick();

    /**
     * Initialisiert den Master
     */
    void init();

    /**
     * Setzt das Spielbrett und registriert bisher hinzugefügten Spieler.
     * 
     * @param board
     *            der Name des zu verwendenden Boards
     * @return true, wenn das Board initialisiert werden konne, false wenn es
     *         kein Board mit der gegebenen Bezeichnung gibt
     */
    boolean initialiseBoard(String board);

    Board getBoard();

    /**
     * @author Stefan / Thore
     * 
     *         Gibt eine Auflistung aller verfügbaren GameBoards geschlüsselt
     *         über deren Namen zurück
     * 
     * @return Zur Verfügung stehende GameBoards
     */
    List<String> getAvailableBoards();

    /**
     * Füegt einen Spieler zum Spiel hinzu. Überprüft ob die Maximalanzahl an
     * Spielern überschritten wurde.
     * 
     * @param player
     *            , der zu hinzufügende Spieler
     * @return {@code false}, falls die Maximalanzahl an Spielern überschritten
     *         wurde, sonst {@code true}.
     */
    boolean addPlayer(Player player);

    void removeAllPlayers();

    /**
     * Versetzt Roboter ggf. in den PowerDown-Modus bzw. weckt sie auf und führt
     * dann nacheinander alle Registerphasen aus
     */
    void activateBoard();

    List<Player> getPlayers();

    /**
     * Gibt den Gewinner des Spiels zurück oder null, wenn es noch keinen gibt
     * 
     * @return der Gewinner des aktuellen Spiels
     */
    Player getWinner();

    /**
     * Gibt an, ob das derzeitige Spiel ein Unentschieden erreicht hat (alle
     * Roboter tot)
     * 
     * @return true, wenn alle Roboter endgültig tot sind, false sonst
     */
    boolean isDraw();

    /**
     * Überprüft ob die Bewegung vom Roboter ausgeführt werden kann
     * 
     * @param robot
     *            , der Roboter, der die Bewegung ausführen soll.
     * @param forward
     *            , Wenn @{code true}, soll Roboter vorwärts bewegt werden,
     *            sonst rückwärts.
     * @return @{code true}, falls der Roboter sich bewegen kann, @{code false}
     *         sonst
     */
    boolean canMove(Robot robot, boolean forward);

    /**
     * Überprüft ob ein Roboter einen anderen schieben kann.
     * 
     * @param robot1
     *            , der schiebende Roboter
     * @param robot2
     *            , der zu schiebende Roboter
     * @return @{code true}, falls der erste Roboter den zweiten schieben kann,
     * @{code false} sonst
     */
    boolean canPushOtherRobot(Robot robot1, Robot robot2);

    void attachObserver(GameObserver observer);

    void removeObserver(GameObserver observer);

    List<GameObserver> getObserverList();

    /**
     * Gibt die nächste Programmkarte vom Deck zurück; Wenn das Deck leer ist,
     * wir der Ablagestapel als neues Deck gemischt
     * 
     * @return
     */
    ProgramCard dealCard();

    /**
     * Legt die angegebene Karte auf den Ablagestapel
     * 
     * @param card
     *            die abzulegende Karte
     */
    void layOffCard(ProgramCard card);

    /**
     * Heilt Roboter auf Werkstätten/ Flaggen, respawnt tote Roboter und
     * verteilt ggf. Optionskarten
     * 
     * @return eine Liste aller Roboter, die endgültig zerstört sind
     */
    List<Robot> cleanup();

    /**
     * Teilt an alle Registrierten Spieler Karten aus
     */
    void dealCardsToPlayers();

    /**
     * Stellt eine Anfrage für das Ändern des PowerDown-Status zu Beginn der
     * nächsten Runde; Funktioniert nur für Roboter, die zu im Master
     * registrierten Spielern gehören
     * 
     * @param robot
     * @return Gibt an, ob die Operation funktioniert hat
     */
    boolean requestPowerDownStatusChange(Robot robot);

    int getFlagCount(String board);
}