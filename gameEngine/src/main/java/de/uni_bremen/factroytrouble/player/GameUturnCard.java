/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.player;

import org.apache.log4j.Logger;

import de.uni_bremen.factroytrouble.gameobjects.Robot;

public class GameUturnCard implements UturnCard {

    private static final Logger LOGGER = Logger.getLogger(GameUturnCard.class);

    private int priority;

    /**
     * 
     * @param priority
     */
    public GameUturnCard(int priority) {
        this.priority = priority;
    }

    @Override
    public void execute(Robot robot) {
        robot.turn(true);
        robot.turn(true);
        LOGGER.info("Roboter ".concat(robot.getName()).concat(" umgedreht, schaut nun nach ").concat(robot.getOrientation().toString()));
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public int getRange() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameUturnCard that = (GameUturnCard) o;

        return priority == that.priority;

    }

    @Override
    public int hashCode() {
        return priority;
    }
    
    @Override
    public String toString() {
        return "[Uturn]; Priorität [" + priority + "]";
    }
}
