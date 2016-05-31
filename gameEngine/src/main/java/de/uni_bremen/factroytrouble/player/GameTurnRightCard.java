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

public class GameTurnRightCard implements TurnRightCard {

    private static final Logger LOGGER = Logger.getLogger(GameTurnRightCard.class);

    private int priority;

    /**
     * 
     * @param priority
     */
    public GameTurnRightCard(int priority) {
        this.priority = priority;
    }

    @Override
    public void execute(Robot robot) {
        robot.turn(false);
        LOGGER.info("Roboter ".concat(robot.getName()).concat(" nach rechts gedreht, schaut nun nach ").concat(robot.getOrientation().toString()));
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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + priority;
        return result;
    }

    
    /** Von der KI gewünscht*/
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GameTurnRightCard other = (GameTurnRightCard) obj;
        if (priority != other.priority)
            return false;
        return true;
    } 
    
    @Override
    public String toString() {
        return "[Rightturn]; Priorität [" + priority + "]";
    }
}
