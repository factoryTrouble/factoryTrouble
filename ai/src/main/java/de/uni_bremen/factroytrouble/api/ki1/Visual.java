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

import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.observer.Event;
import de.uni_bremen.factroytrouble.observer.GameObserver;
import de.uni_bremen.factroytrouble.player.OptionCard;
import de.uni_bremen.factroytrouble.player.ProgramCard;

/**
 * Dieses Interface verkörpern die Augen unseres kognitiven Agenten, über welche
 * alle visuell vorhandenen Informationen verfügbar gemacht werden.
 * 
 * @author KI Gruppe
 */
public interface Visual extends GameObserver {
    /**
     * Sucht nach der Position der Figur eines Spielers.
     * 
     * @param id
     *            die id des Spielers
     * @return die Position der Spielerfigur
     */
    Placement getPlayerPlacement(int id);

    /**
     * Sucht nach der Position einer Flagge.
     * 
     * @param number
     *            die Nummer der Flagge
     * @return die Flaggenposition.
     */
    Placement getFlagPosition(int number);

    /**
     * Liefert die Umgebung um {@code center}. Der Radius wird durch die Config
     * festgelegt.
     * 
     * @param center
     *            der Mittelpunkt der {@link BoardArea}
     * @return die {@link BoardArea} um {@code center}
     */
    BoardArea getArea(Point center, int radius);

    /**
     * Liefert die Trefferpunkte eines Spielers.
     * 
     * @param playerId
     *            die id des Spielers
     * @return übrige Trefferpunkte des Spielers
     */
    int getHP(int playerId);

    /**
     * Liefert die übrigen Leben eines Spielers.
     * 
     * @param playerId
     *            die id des Spielers
     * @return übrige Leben des Spielers
     */
    int getLives(int playerId);

    /**
     * Schaut, ob ein Spieler sich im Powerdown befindet.
     * 
     * @param playerId
     *            die id des Spielers
     * @return {@code true}, falls der Roboter des Spielers im Powerdown
     *         befindet
     */
    boolean isPoweredDown(int playerId);

    /**
     * Liefert die Position des aktuellen Checkpoints eines Spielers.
     * 
     * @param playerId
     *            die id des Spielers
     * @return die aktuelle Checkpointposition.
     */
    Placement getRespawnPoint(int playerId);

    /**
     * Liefert die gelockten Karten eines Spielers.
     * 
     * @param playerId
     *            die id des Spielers
     * @return die gelockten Karten des gegebenen Spielers
     */
    List<ProgramCard> getLockedCards(int playerId);

    /**
     * Liefert die OptionCards eines Spielers.
     * 
     * @param playerId
     *            die id des Spielers
     * @return OptionCards des Spielers
     */
    List<OptionCard> getOptionCards(int playerId);

    /**
     * Liefert die {@code number}-te, eigene Karte.
     * 
     * @param number
     *            die wievielte Karte
     * @return die {@code number}-te, eigene Karte
     */
    ProgramCard getCard(int number);

    void spam(Robot robot, Event e, Object source);
}
