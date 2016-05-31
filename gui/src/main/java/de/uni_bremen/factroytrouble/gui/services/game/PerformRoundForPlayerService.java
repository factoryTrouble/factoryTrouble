/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gui.services.game;

import de.uni_bremen.factroytrouble.gui.controller.GameScreenController;
import de.uni_bremen.factroytrouble.gui.services.GameEngineWrapper;
import de.uni_bremen.factroytrouble.spring.SpringConfigHolder;
import javafx.application.Platform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Verwaltet das Ausführen einer Runde für einen Spieler.
 * <p>
 * Created by johannes.gesenhues on 22.12.15.
 */
@Service
public class PerformRoundForPlayerService {

    @Autowired private HandCardService handCardService;
    @Autowired private RegisterCardsService registerCardsService;
    @Autowired private AlertService alertService;
    @Autowired private EndBtnService endBtnService;
    @Autowired private GameScreenController gameScreenController;

    private GameEngineWrapper gameEngineWrapper;

    /**
     * Startet die Runde für den Spieler.
     *
     * @param playerNumber Der aktive Spieler
     */
    public void start(int playerNumber) {
        gameEngineWrapper = SpringConfigHolder.getInstance().getContext().getBean(GameEngineWrapper.class);
        gameScreenController.setActivePlayer(playerNumber);
        Platform.runLater(() -> prepare(playerNumber));
    }

    /**
     * Zeigt nächster Spieler Alert, und zeigt danach die Spielkarten für diesen Spieler an
     *
     * @param activePlayer Der aktive Spieler
     */
    public void prepare(int activePlayer) {
        if(gameEngineWrapper.getPlayerByNumber(activePlayer).getRobot().isPoweredDown()){
            alertService.showPowerDownAlert(activePlayer);
            endBtnService.endRound(activePlayer);
            return;
        }
        alertService.showNextPlayerAlert(activePlayer);
        handCardService.showCards(activePlayer);
        registerCardsService.initRegisters(activePlayer);
    }
}
