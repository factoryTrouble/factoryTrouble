/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gameobjects;

import org.apache.log4j.Logger;

import de.uni_bremen.factroytrouble.board.Tile;

public class GameGear implements Gear {

    private static final Logger LOGGER = Logger.getLogger(Gear.class);

    private boolean rotatesLeft;

    public GameGear(boolean rotatesLeft) {
        this.rotatesLeft = rotatesLeft;
    }

    @Override
    public void execute(Tile tile) {
        Robot robot = tile.getRobot();
        if (robot == null) {
            return;
        }
        robot.turn(rotatesLeft);
        LOGGER.info(("Roboter ").concat(robot.getName()).concat(" von Zahnrad gedreht, schaut jetzt nach ")
                .concat(robot.getOrientation().toString()));
    }

    @Override
    public boolean rotatesLeft() {
        return rotatesLeft;
    }

    @Override
    public Gear clone() {
        return new GameGear(rotatesLeft);
    }

}
