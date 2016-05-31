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

/**
 * @author Oleg Manov
 */
public class GameWorkshop implements Workshop {

    private static final Logger LOGGER = Logger.getLogger(Workshop.class);

    private boolean isAdvancedWorkshop;

    public GameWorkshop(boolean isAdvancedWorkshop) {
        this.isAdvancedWorkshop = isAdvancedWorkshop;
    }

    @Override
    public void execute(Tile tile) {
        Robot robot = tile.getRobot();
        if (robot == null) {
            return;
        }
        robot.setRespawnPoint();
        LOGGER.info("Roboter ".concat(robot.getName()).concat(" berührt Werkstatt; Respawn-Punkt aktualisiert"));
    }

    @Override
    public boolean isAdvancedWorkshop() {
        return isAdvancedWorkshop;
    }

    @Override
    public Workshop clone() {
        return new GameWorkshop(isAdvancedWorkshop);
    }
}