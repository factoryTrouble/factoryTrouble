/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.api.ki1;

import java.awt.Point;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uni_bremen.factroytrouble.ai.ki1.planning.CurrentPlannerOne;
import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.player.ProgramCard;

/**
 * Interface zur Simulierung der Ausführung von {@link ProgramCard}s und 
 * logischen Aktionen.
 * 
 * @author Roland
 *
 */
public interface Logic {
    /** Die verschiedenen möglichen Bewegungen, die durch eine oder verschiedene Programmkarten ausgeführt werden können.
     * @author Roland
     *
     */
    public enum Move {
        LEFT, RIGHT, UTURN, THREE, TWO, ONE, MINUSONE
    }
    public Placement simulateGearAndBelt(Placement oldPlace, Control controller);

    /**Simuliert die ausführung einer Karte und gibt das neue {@link PlacementUnit} zurück.
     * Berücksichtigt keine Boardelemente oder den Kartenrand.
     * @param tile Das starttile 
     * @param ori Die StartOrientierung in int
     * @param card Die zu simulierende Programmkarte 
     * @param control controller
     * @return die neue Position und Orientierung
     */
    Placement simulateTurn(Tile tile, int ori, ProgramCard card, Control control);

    /**Berechnet den Punkt nach einem Move (1 bis 3) und gibt ihn zurück.
     * @param start StartPunkt
     * @param ori Startorientierung
     * @param tilesMoved die zu gehende Anzahl an Feldern
     * @return Der Punkt nach der Bewegung
     */
    Point calcMovePos(Point start, int ori, int tilesMoved);
    
    /** Rechnet die Orientierung als int in {@link Orientation} um und gibt diese aus.
     * @param ori Orientierung als int
     * @return Orientierung als {@ link Orientation}
     */
    Orientation oriAsOri(int ori);
    
    /** Rechnet die Orientierung als {@ link Orientation} in int um und gibt diese aus. 
     * @param ori Orientierung.
     * @return Orientierung als int
     */
    int oriAsInt(Orientation ori);
    
    /** Gibt die verschiedenen für einen {@link Move} nötigen Kartenkombinationen als Liste von einer Liste von Strings zurück.
     * @param move die auszuführende Bewegung
     * @return mögliche Kartenkombinationen zur durchführung der Bewegung
     */
    List<List<String>> getNeededCardsForMove(Move move);

    /**
     *Gibt alle Programmkarten zurück, die noch benutzbar sind
     * @param cpo
     * @return
     */
    List<ProgramCard> retrieveUsableCards(CurrentPlannerOne cpo);

    /**
     * Gibt eine Menge von möglichen äquivalenten Bewegungen, die Spielkarten
     * entsprechen, zurück.
     *
     * Die Länge der Listen in der Rückgabe ist jeweils nicht größer als 3.
     *
     * Es werden jeweils Move- statt ProgrammCard-Werte zurückgegeben, weil
     * die Priorität und Range nicht berücksichtigt wird.
     *
     * @param move die auszuführende Bewegung
     * @author Simon Liedtke
     */
    Set<Map<Move, Integer>> getEquivalentMoves(Move move);

    /**
     * Überprüft, ob der gewünschte Move anhand der zur Verfügung stehenden
     * Bewegungen durchgeführt werden kann.
     *
     * @return Die Bewegungen, die ausgeführt werden können
     * (als Häufigkeitstabelle)
     * @author Simon Liedtke
     */
    Map<Move, Integer> canPerformMove(Move move, List<Move> cards);

    Map<Move, Integer> getFrequencyTable(List<Move> moves);

    /**
     * Versuche, möglichst viele Drehungen aufzurufen, die sich gegenseitig
     * aufheben. Geht davon aus, dass der erste Parameter nur aus
     * Drehbewegungen besteht. Ansonsten ist das Verhalten undefiniert.
     *
     * @author Simon Liedtke
     */
    Map<Move, Integer> tryMinTurns(List<Move> moves);

    /**
     * Versuche, {@code n} Schritte vorwärts zu gehen.
     * Geht davon aus, dass die vorhandenen Moves nur Vorwärts- und
     * Rückwärtsbewegungen sind, also keine Drehbewegungen.
     *
     * @author Simon Liedtke
     */
    Map<Move, Integer> goMovesOnly(List<Move> moves, int steps);

    /**
     * Konvertiert eine Liste von Programmkarten in entsprechende
     * {@code Move}-Instanzen.
     *
     * @author Simon Liedtke
     */
    List<Move> programCards2Moves(List<ProgramCard> cards);

    /**
     * Kovertiert eine einzelne Programmkarte in eine entsprechende
     * {@code Move}-Instanz.
     * @author Simon Liedtke
     */
    Move programCard2Move(ProgramCard card);

    /**
     * Gibt eine Liste von Programmkarten anhand der gewünschten Bewegungen zurück.
     * Die Prioritäten der Karten werden dabei nicht berücksichtigt.
     * @param moves Häufigkeitstabelle von gewünschten Bewegungen
     * @param cards Karten, die zur Verfügung stehen
     * @return
     */
    List<ProgramCard> frequencytable2cardlist(Map<Move, Integer> moves, List<ProgramCard> cards);

    /**
     * Gibt {@code true} zurück, wenn Karte und Bewegung zusammen passen
     * (z.B. {@code GameTurnLeftCard} und {@code Move.Left}), sonst {@code false}.
     * @param card
     * @param move
     * @return
     */
    boolean cardAndMoveMatch(ProgramCard card, Move move);
}
