/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gui.services.game;

import de.uni_bremen.factroytrouble.gui.ApplicationSettings;
import de.uni_bremen.factroytrouble.gui.controller.AISimulateController;
import de.uni_bremen.factroytrouble.gui.controller.GameScreenController;
import de.uni_bremen.factroytrouble.gui.controller.NewGameScreenController;
import de.uni_bremen.factroytrouble.gui.observer.AISimulateObserver;
import de.uni_bremen.factroytrouble.gui.observer.GUIEngineObserver;
import de.uni_bremen.factroytrouble.gui.services.GameEngineWrapper;
import de.uni_bremen.factroytrouble.gui.services.util.ApplicationArgumentsService;
import de.uni_bremen.factroytrouble.player.Player;
import javafx.application.Platform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Verwaltet das Starten eines Spiels.
 * <p>
 * Created by johannes.gesenhues on 22.12.15.
 */
@Service
public class StartGameService {

    @Autowired private NewGameScreenController newGameScreenController;
    @Autowired private GameScreenController gameScreenController;
    @Autowired private AISimulateController aiSimulateController;
    @Autowired private GameEngineWrapper gameEngineWrapper;
    @Autowired private AlertService alertService;
    @Autowired private ApplicationArgumentsService applicationArgumentsService;


    /**
     * Startet das Game, sofern ein Board ausgewählt sind und genügend Spieler teilnehmen
     * (mindestens zwei).
     */
    public void startGame() {
        List<Player> players = gameEngineWrapper.getPlayers();
        if (newGameScreenController.getActiveBoard().isEmpty()) {
            Platform.runLater(() -> alertService.showNoBoardChosenAlert());
            return;
        }
        if (players == null || gameEngineWrapper.getPlayerCount() < 2) {
            Platform.runLater(() -> alertService.showTooFewPlayersAlert());
            return;
        }
        if(applicationArgumentsService.getArgumentValue(ApplicationSettings.AI_TEST_MODE)) {
            gameEngineWrapper.attachObserver(new AISimulateObserver());
            Platform.runLater(() -> {
                aiSimulateController.setGameCountAsString(alertService.showTextAlert("Anzahl der Spiele", "Wähle die Anzahl der zu spielenden Spiele", "100"));
                newGameScreenController.changeView(aiSimulateController.getView());
                aiSimulateController.setActiveBoard(newGameScreenController.getActiveBoard());
            });

            return;
        }
        gameEngineWrapper.attachObserver(new GUIEngineObserver());
        Platform.runLater(() -> newGameScreenController.changeView(gameScreenController.getView()));
        Platform.runLater(() -> gameScreenController.start(newGameScreenController.getActiveBoard()));
    }


}
