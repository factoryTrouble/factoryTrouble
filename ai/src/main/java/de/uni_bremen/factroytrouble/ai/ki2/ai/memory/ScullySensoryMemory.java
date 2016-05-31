/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki2.ai.memory;

import java.util.List;

import org.apache.log4j.Logger;

import de.uni_bremen.factroytrouble.ai.ais.AIPlayer2;
import de.uni_bremen.factroytrouble.ai.ki2.api.memory.SensoryMemory;
import de.uni_bremen.factroytrouble.ai.ki2.api.memory.Thought;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.player.ProgramCard;

public class ScullySensoryMemory implements SensoryMemory {
    private static final Logger LOGGER = Logger.getLogger(ScullySensoryMemory.class);

    /**
     * Nichtkognitive VisualUnit
     */
    private ScullyVisual visual;
    private AIPlayer2 player;

    /**
     * Privater Konstruktor wegen Singelton
     */
    public ScullySensoryMemory(ScullyVisual pVisual, AIPlayer2 pPlayer) {
        player = pPlayer;
        visual = pVisual;
    }

    @Override
    public Thought getInformation(List<String> keys) {
        if (keys.isEmpty()) {
            LOGGER.error("getInformation: Der gesuchte Gedanke ist null. Null wird zurückgegeben");
            return null;
        }
        LOGGER.debug("getInformation: Suchanfrage " + keys + " erhalten");

        if (keys.contains("flag")) {
            if (keys.contains("next")) {
                LOGGER.debug("getInformation: Es wird nach derr Position der nächsten Flagge gesucht. " + keys);
                return getNextFlag();
            }
            LOGGER.debug("getInformation: Es wird nach einer Position einer Flagge gesucht. " + keys);
            return getSearchedFlag(keys);
        }
        if (keys.contains("my")) {
            if (keys.contains("ori")) {
                LOGGER.debug("getInformation: Es wird nach eigener Orientierung gesucht. " + keys);
                return getMyOrientation();
            } else if (keys.contains("position")) {
                LOGGER.debug("getInformation: Es wird nacheigener Position gesucht. " + keys);
                return getMyPosition();
            }
        }
        if (keys.contains("holes")) {
            LOGGER.debug("getInformation: Es wird nach allen Löchern gesucht. " + keys);
            return getAllHoles();
        }
        if (keys.contains("all") && keys.contains("conveyor")) {

            LOGGER.debug("getInformation: Es wird nach allen Bändern gesucht. " + keys);
            return getAllConveyor();

        }
        if (keys.contains("right") && keys.contains("gears")) {

            LOGGER.debug("getInformation: Es wird nach Zahnrädern gesucht, die nach rechts drehen. " + keys);
            return getRightGears();

        }
        if (keys.contains("left") && keys.contains("gears")) {

            LOGGER.debug("getInformation: Es wird nach Zahnrädern gesucht, die nach links drehen. " + keys);
            return getLeftGears();

        }
        if (keys.contains("highest") && keys.contains("point")) {

            LOGGER.debug("getInformation: Es wird nach dem höchsten Punkt gesucht. " + keys);
            return getHighestPoint();

        }
        if (keys.contains("walls")) {
            LOGGER.debug("getInformation: Es wird nach Wänden gesucht. " + keys);
            if (keys.contains("north")) {
                LOGGER.debug("getInformation: Es wird nach Wänden gesucht, die die Orientierung richtung Norden haben. "
                        + keys);
                return getAllWallsNorth();
            } else if (keys.contains("east")) {
                LOGGER.debug("getInformation: Es wird nach Wänden gesucht, die die Orientierung richtung Osten haben. "
                        + keys);
                return getAllWallsEast();
            } else if (keys.contains("south")) {
                LOGGER.debug("getInformation: Es wird nach Wänden gesucht, die die Orientierung richtung Süden haben. "
                        + keys);
                return getAllWallsSouth();
            } else if (keys.contains("west")) {
                LOGGER.debug("getInformation: Es wird nach Wänden gesucht, die die Orientierung richtung Westen haben. "
                        + keys);
                return getAllWallsWest();
            }
        }
        if (keys.contains("cards")) {

            if (keys.contains("locked")) {
                LOGGER.debug("getInformation: Es wird nach gelockten Karten gesucht" + keys);
                return getLockedCards();
            }
            if (keys.contains("player")) {
                LOGGER.debug("getInformation: Es wird nach Handarten gesucht" + keys);
                return getPlayerCards();
            }
        }
        LOGGER.debug("getInformation: Der angefragte Gedanke wurde nicht gefunden");
        return null;
    }

    /**
     * Keys: Positionen: robot, position Wände: wall,xy
     *
     *
     */

    @Override
    public void storeInMemory(Thought thought) {
        /*
         * Not done yet
         */
    }

    /**
     * Gibt den Punkt oben rechts dem Feld zurück
     * 
     * @return der Punkt oben rechts
     */
    private Thought getHighestPoint() {
        Thought tmpThought = new ScullyThought("highest_Point");
        tmpThought.addInformationToThought(visual.getHighestPoint());
        return tmpThought;
    }

    /**
     * Gibt alle Zahnräder zurück die nach rechts drehen
     * 
     * @return Die Zahnräder
     */
    private Thought getRightGears() {
        Thought tmpThought = new ScullyThought("gears_right");
        tmpThought.addInformationToThought(visual.getGearsRight());
        return tmpThought;
    }

    /**
     * Gibt alle Zahnräder zurück die nach links drehen
     * 
     * @return Die Zahnräder
     */
    private Thought getLeftGears() {
        Thought tmpThought = new ScullyThought("gears_left");
        tmpThought.addInformationToThought(visual.getGearsLeft());
        return tmpThought;
    }

    /**
     * Eine private Methode die eine Flagge anhand ihrer Nummer sucht
     * 
     * @param keys,
     *            Anfrage aus der die Nummer der Flagge entnommen wird
     * 
     * @return Koordinaten der Flagge
     */
    private Thought getSearchedFlag(List<String> keys) {
        int flagNumber = -1;
        for (String string : keys) {
            char c = string.charAt(0);
            if (c >= '1' && c <= '4') {
                flagNumber = Character.getNumericValue(c);
                break;
            }

        }
        Thought thoughtFromSensory = new ScullyThought("flag_" + flagNumber);
        thoughtFromSensory.addInformationToThought(visual.getFlagPosition(flagNumber));
        return thoughtFromSensory;
    }

    /**
     * Gibt die aktuelle Position in absoluten Koordinaten des eigenen Roboters
     * zurück.
     * 
     * @return Gedanke mit mit der aktuellen Position der eigenen Roboters.
     */
    private Thought getMyPosition() {
        Thought tempThought = new ScullyThought("my_position");
        tempThought.addInformationToThought(getMyRobot().getCurrentTile().getAbsoluteCoordinates());
        return tempThought;
    }

    /**
     * Gibt die Orientierung des eigenen Roboters zurück.
     * 
     * @return Gedanke mit der Orientierung des eigenen Roboters.
     */
    private Thought getMyOrientation() {
        Thought tempThought = new ScullyThought("my_orientation");
        tempThought.addInformationToThought(getMyRobot().getOrientation());
        return tempThought;
    }

    /**
     * Holt sich die Instanz des eigenen Roboter.
     * 
     * @return Instanz des eigenen Roboter.
     */
    private Robot getMyRobot() {
        return player.getRobot();
    }

    /**
     * Gibt die absolute Position der Löcher zurück.
     * 
     * @return absolute Position aller Löcher.
     */
    private Thought getAllHoles() {
        Thought tmpThought = new ScullyThought("holes");
        tmpThought.addInformationToThought(visual.getHoles());
        return tmpThought;
    }

    /**
     * Gibt die absolute Position der Bänder zurück.
     * 
     * @return absolute Position aller Bänder.
     */
    private Thought getAllConveyor() {
        Thought tmpThought = new ScullyThought("conveyor_all");
        tmpThought.addInformationToThought(visual.getConveyorMove());
        return tmpThought;
    }

    /**
     * Gibt alle wände zurück die Richtung Westen zeigen
     * 
     * @return Alle Wände Richtung Westen
     */
    private Thought getAllWallsWest() {
        Thought tmpThought = new ScullyThought("walls_west");
        tmpThought.addInformationToThought(visual.getWallsWest());
        return tmpThought;
    }

    /**
     * Gibt alle wände zurück die Richtung Süden zeigen
     * 
     * @return Alle Wände Richtung Süden
     */
    private Thought getAllWallsSouth() {
        Thought tmpThought = new ScullyThought("walls_south");
        tmpThought.addInformationToThought(visual.getWallsSouth());
        return tmpThought;
    }

    /**
     * Gibt alle wände zurück die Richtung Osten zeigen
     * 
     * @return Alle Wände Richtung Osten
     */
    private Thought getAllWallsEast() {
        Thought tmpThought = new ScullyThought("walls_east");
        tmpThought.addInformationToThought(visual.getWallsEast());
        return tmpThought;
    }

    /**
     * Gibt alle wände zurück die Richtung Norden zeigen
     * 
     * @return Alle Wände Richtung Norden
     */
    private Thought getAllWallsNorth() {
        Thought tmpThought = new ScullyThought("walls_north");
        tmpThought.addInformationToThought(visual.getWallsNorth());
        return tmpThought;
    }

    /**
     * Gibt die gelockten Karten zurück
     * 
     * @return Liste von gelockten Karten
     */
    private Thought getLockedCards() {
        Thought tmpThought = new ScullyThought("cards_locked");
        tmpThought.addInformationToThought(visual.getLockedCards());
        return tmpThought;
    }

    /**
     * Gibt den Standort der nächsten Flagge zurück.
     * 
     * @return Standort der näxhsten Flagge.
     */
    private Thought getNextFlag() {
        Thought tmpThought = new ScullyThought("flag_next");
        tmpThought.addInformationToThought(visual.getNextFlag());
        return tmpThought;
    }

    /**
     * Gibt die Karten des Spielers zurück.
     * 
     * @return Karten des Spielers.
     */
    private Thought getPlayerCards() {
        Thought tmpThought = new ScullyThought("cards_player");
        for (ProgramCard card : visual.getCards()) {
            tmpThought.addInformationToThought(card);
        }

        return tmpThought;
    }
}
