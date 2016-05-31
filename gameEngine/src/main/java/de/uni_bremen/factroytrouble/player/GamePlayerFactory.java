/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.player;

import de.uni_bremen.factroytrouble.ai.AIFactory;
import de.uni_bremen.factroytrouble.gameobjects.GameRobot;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Implementierung der PlayerFactory
 *
 * @author Thorben
 */
@Component
public class GamePlayerFactory implements PlayerFactory {

    @Autowired
    private AIFactory aiFactory;

    @Override
    public Player createNewPlayer(int gameId, String robotName, String kiName) {
        Robot newRobot = new GameRobot(gameId, null, Orientation.NORTH, robotName);
        Player newPlayer = new GamePlayer(newRobot);
        if (GameMaster.KI_NAMES.contains(kiName)) {
            return aiFactory.getAIPlayer(gameId, kiName, newPlayer);
        }
        return new GamePlayer(new GameRobot(gameId, null, Orientation.NORTH, robotName));
    }

}
