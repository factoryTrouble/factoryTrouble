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

public class GameMoveBackwardCard implements MoveBackwardCard {

    private final int gameId;
    private int priority;
    private Board board;

    private MasterFactory factory = new GameMasterFactory();

    /**
     * @param priority
     */
    public GameMoveBackwardCard(int gameId, int priority) {
        this.gameId = gameId;
        this.priority = priority;
    }

    @Override
    public void execute(Robot robot) {
        if (board == null) {
            board = factory.getMaster(gameId).getBoard();
        }
        board.backupRobot(robot);
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public int getRange() {
        return -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        GameMoveBackwardCard that = (GameMoveBackwardCard) o;

        return priority == that.priority;

    }

    @Override
    public int hashCode() {
        return priority;
    }

    @Override
    public String toString() {
        return "[Backup]; Priorität [" + priority + "]";
    }
}
