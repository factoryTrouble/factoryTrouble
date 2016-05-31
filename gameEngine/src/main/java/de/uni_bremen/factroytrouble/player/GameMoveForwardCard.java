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

public class GameMoveForwardCard implements MoveForwardCard {

    private final int gameId;
    private int priority;
    private int range;
    private Board board;

    private MasterFactory factory = new GameMasterFactory();

    /**
     * 
     * @param priority
     * @param range
     */
    public GameMoveForwardCard(int gameId, int priority, int range) {
        this.gameId = gameId;
        this.priority = priority;
        this.range = range;
    }

    @Override
    public void execute(Robot robot) {
        if (board == null) {
            board = factory.getMaster(gameId).getBoard();
        }
        for (int i = 0; i < range; i++) {
            board.moveRobot(robot);
        }
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public int getRange() {
        return range;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        GameMoveForwardCard that = (GameMoveForwardCard) o;

        if (priority != that.priority)
            return false;
        return range == that.range;

    }

    @Override
    public int hashCode() {
        int result = priority;
        result = 31 * result + range;
        return result;
    }

    @Override
    public String toString() {
        return "[Move " + range + "]; Priorität [" + priority + "]";
    }
}
