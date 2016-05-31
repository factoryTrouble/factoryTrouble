/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ais;

import de.uni_bremen.factroytrouble.ai.ki_classic.path_finding.dykstra.DykstraHandler;
import de.uni_bremen.factroytrouble.ai.ki_classic.VirtualBoard;
import de.uni_bremen.factroytrouble.player.Player;
import de.uni_bremen.factroytrouble.player.ProgramCard;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * KI_Agent mit klassischem Brute-Force-Ansatz. Benutzt VirtualBoard und
 * DykstraHandler.
 *
 * @author Markus
 */
public class AI_Classic extends AIBase {

    private static final Logger LOGGER = Logger.getLogger(AI_Classic.class);
    private final DykstraHandler dykstra;

    /**
     * Erzeugt eine neue Instanz der klassischen KI fuer den angegebenen Spieler
     * im angegebenen Spiel.
     * 
     * @param gameId
     *            die ID des Spiels mit dem angegebenen Spieler
     * @param player
     *            der Spieler, den diese KI kontrollieren soll
     */
    public AI_Classic(Integer gameId, Player player) {
        super(gameId, player);
        dykstra = new DykstraHandler(mFactory.getMaster(gameId));
    }

    /**
     * macht einen Zug. Liesst eigenstaendig die Informationen, die gebraucht
     * werden, und fuellt die Register.
     * 
     */
    @Override
    public void executeTurn() {
        dykstra.updateRespawnPoint(getRobot());
        VirtualBoard virtualBoard = new VirtualBoard(mFactory.getMaster(gameId), dykstra);
        if (player.getPlayerCards().size() == 0) {// zB powerdown
            return;
        }
        int[] cardChoices = virtualBoard.performRoundVirtualRound(player);
        addCardsToRegisters(cardChoices);
        if (getRobot().getHP() <= 5) {
            getRobot().powerDown();
        }

    }

    /**
     * Hier passiert momentan nichts; der executeTurn-Vorgang ist schnell und
     * hat keine wirklichen Zwischenergebnisse.
     */
    @Override
    public void terminateTurn() {
        LOGGER.debug("Not implemented");
    }

    private void addCardsToRegisters(int[] cardChoices) {
        List<ProgramCard> cards = getPlayerCards();
        for (int i = 0; i < getRobot().getRegisters().length && i < cardChoices.length; ++i) {
            if (cardChoices[i] >= 0) {
                getRobot().fillRegister(i, cards.get(cardChoices[i]));
            }
        }
    }
}
