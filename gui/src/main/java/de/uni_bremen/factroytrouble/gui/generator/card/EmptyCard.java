/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gui.generator.card;

import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.player.ProgramCard;
import org.apache.log4j.Logger;

public class EmptyCard implements ProgramCard {

    private static final Logger LOGGER = Logger.getLogger(EmptyCard.class);

    /**
     * Führt die jeweilige Aktion dieses Kartentyps bei einem Roboter aus
     *
     * @param robot
     */
    @Override
    public void execute(Robot robot) {
        LOGGER.info("An empty card can not execute");
    }

    @Override
    public int getPriority() {
        return 0;
    }

    /**
     * Die Range von move-karten wird zurückgegeben; die Range von Backup sollte
     * -1, die von Turn-Karten 0 betragen
     *
     * @return die Range der jeweiligen Karte
     */
    @Override
    public int getRange() {
        return 0;
    }
}
