/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ais;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.uni_bremen.factroytrouble.ai.ki3.ActRUser;
import de.uni_bremen.factroytrouble.ai.ki3.ScoreManager;
import de.uni_bremen.factroytrouble.player.Player;
import de.uni_bremen.factroytrouble.player.ProgramCard;

/**
 * Erste Version eines Act-R-basierten Agenten
 * 
 * @author Thorben
 */
public class ActRAIPlayer extends AIBase {
    private static final String AI_NAME = "Chunky";

    private ActRUser user;
    private static final Logger LOGGER = Logger.getLogger(ActRAIPlayer.class);

    public ActRAIPlayer(Integer gameId, Player player) {
        super(gameId, player);
        user = new ActRUser(new ScoreManager(gameId, this));
    }

    public ActRAIPlayer(int gameId, Player player, ActRUser user) {
        super(gameId, player);
        this.user = user;
    }

    @Override
    public void executeTurn() {
        user.saveCurrentGameStatus();
        user.loadActR(AI_NAME);
        int[] cardsToPut = user.makeMove("t");

        if (cardsToPut == null) {
            return;
        }

        fillRegistersWithSelection(cardsToPut);

        if (user.getPowerDownRequest()) {
            mFactory.getMaster(gameId).requestPowerDownStatusChange(getRobot());
        }

        finishTurn();
    }

    @Override
    public void terminateTurn() {
        LOGGER.info("Unterbreche Spielzug von ".concat(this.toString()));
        fillRegistersWithSelection(user.terminateTurn());
    }

    private void fillRegistersWithSelection(int[] selected) {
        List<ProgramCard> cards = getPlayerCards();
        List<ProgramCard> toPut = new ArrayList<>();
        toPut.addAll(cards);

        for (int i = 0; i < selected.length; i++) {
            if (selected[i] < 0) {
                break;
            }
            fillRegister(i, toPut.get(selected[i]));
        }
    }
}
