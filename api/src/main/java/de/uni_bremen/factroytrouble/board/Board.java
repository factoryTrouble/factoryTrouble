/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.board;

import java.awt.Point;
import java.util.List;
import java.util.Map;

import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.observer.GameObserver;

/**
 * Das Interface für das gesamte Spielfeld.
 * 
 * @author Ottmar, Thorben
 *
 */
public interface Board {

    /**
     * Baut ein neues Board mit den übergebenen Parametern
     * 
     * @param dock
     * @param fields
     */
    void buildBoard(Dock dock, Field... fields);

    /**
     * Führt die jeweils nächste Registerphase aller Roboter aus, die sich nicht
     * im PowerDown befinden
     */
    void executeNextRegisters();

    /**
     * Bewegt einen Roboter um eine Kachel nach vorne
     * 
     * @param robot
     *            der zu bewegende Roboter
     * @return die Kachel, auf der der Roboter nach der Bewegung landet
     *         oder @{code null}, wenn er sich von Spielbrett runter bewegt
     */
    Tile moveRobot(final Robot robot);

    /**
     * Bewegt einen Roboter um eine Kachel zurück
     * 
     * @param robot
     *            der zu bewegende Roboter
     * @return die Kachel, auf der der Roboter nach der Bewegung landet
     *         oder @{code null}, wenn er sich von Spielbrett runter bewegt
     */
    Tile backupRobot(final Robot robot);

    /**
     * Bewegt den zu schiebenden Roboter in die angegebene Richtung
     * 
     * @param robot
     *            der zu schiebende Roboter
     * @param direction
     *            die Richtung, in die der Roboter geschoben werden soll
     * @return die Kachel, auf der der Roboter nach der Bewegung landet
     *         oder @{code null}, wenn er sich von Spielbrett runter bewegt
     */
    Tile pushRobot(Robot robot, Orientation direction);

    /**
     * Bewegt alle Förderbänder
     * 
     * @param moveOnlyExpress
     *            gibt an, ob sich nur die Expressförderbänder bewegen sollen
     */
    void moveConveyors(boolean moveOnlyExpress);

    /**
     * Bewegt alle Zahnräder
     */
    void rotateGears();

    /**
     * Aktiviert alle Pusher, die in der übergebenen Registerphase aktiv sind
     */
    void activatePushers(int registerPhase);

    /**
     * Leite Berührung aller Checkpoints und Flaggen ein
     */
    void touchCheckpointsAndFlags();

    /**
     * Bewegt einen Roboter zu seinem respawn-Punkt; dieser ist initial der
     * Startpunkt des Roboters
     * 
     * @param robot
     *            der zu bewegende Roboter
     * @return die Kachel, auf der der Roboter respawnt ist
     */
    Tile respawnRobot(final Robot robot);

    /**
     * Feuert alle Laser auf dem Spielbrett (Laser von Feldern und Laser von
     * Robotern)
     */
    void fireLasers();

    /**
     * Tötet alle Roboter, die genug Schaden genommen haben
     * 
     * @return Eine Liste aller getöteten Roboter
     */
    List<Robot> killRobots();

    Map<Point, Field> getFields();

    /**
     * Gibt zu einem Tile das entsprechende Field zurück
     * 
     * @param tile
     * @return
     */
    Field findFieldOfTile(Tile tile);

    /**
     * Findet das jeweils nächste Tile zu dem gegebenen Tile und der gegebenen
     * direction
     * 
     * @param tile
     * @param direction
     * @return
     */
    Tile findNextTile(Tile tile, Orientation direction);

    /**
     * Gibt die absoluten Koordinaten eines Tiles auf dem Board zurück, auf dem
     * sie sich befindet
     * 
     * @param tile
     *            das entsprechende Tile
     * @return einen Punkt, der die absoluten Koordinaten des Tiles angibt
     */
    Point getAbsoluteCoordinates(Tile tile);

    /**
     * Gibt die Nummer der letzten anzufahrenden Flagge zurück
     * 
     * @return die Nummer der letzten anzufahrenden Flagge
     */
    int getHighestFlagNumber();

    /**
     * Klont dieses Board
     * 
     * @return Einen Klon dieses Boards
     */
    Board clone();
    
    void attachObserver(GameObserver observer);
    
    void removeObserver(GameObserver observer);
}
