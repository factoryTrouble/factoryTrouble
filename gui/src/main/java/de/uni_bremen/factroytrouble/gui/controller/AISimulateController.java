/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gui.controller;

import de.uni_bremen.factroytrouble.ai.AIPlayer;
import de.uni_bremen.factroytrouble.gui.services.GameEngineWrapper;
import de.uni_bremen.factroytrouble.gui.services.game.AlertService;
import de.uni_bremen.factroytrouble.gui.util.TextAreaAppender;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Oberfläche, um mehrere spiele mehrerer Agenten zu starten.
 *
 * @author André
 */
@Controller
public class AISimulateController extends FXMLController {

    private static final Logger LOGGER = Logger.getLogger(AISimulateController.class);
    private static final String CURRENT_GAME_LABEL_PREFIX = "Aktuelles Spiel: ";
    private static final String CURRENT_ROUND_LABEL_PREFIX = "Aktuelle Runde:: ";

    private Integer maxGameCount;
    private PrintStream defaultInputStream;
    private PrintStream defaultErrorStream;
    private String activeBoard;
    private Integer currentRound = 1;

    private TextAreaAppender textAreaAppender;

    private List<String> aiClazzes;
    private List<String> robotNames;

    @FXML private Label currentGameLabel;
    @FXML private Label currentRoundLabel;
    @FXML private TextArea logWindow;

    @Autowired private GameEngineWrapper gameEngineWrapper;
    @Autowired private MenuScreenController menuScreenController;
    @Autowired private AlertService alertService;

    private Thread thread;

    @Override
    @Value("/views/simulateAI.fxml")
    public void setFxmlFilePath(String filePath) {
        this.fxmlFilePath = filePath;
    }

    /**
     * Setzt die maximale Anzahl an Spielen
     * @param gameCountNumber
     *      Anzahl der Spiel als String. Diese werden in einen Integer konvertiert
     */
    public void setGameCountAsString(String gameCountNumber) {
        if(gameCountNumber == null) {
            maxGameCount = 1;
        }
        maxGameCount = Integer.valueOf(gameCountNumber);
    }

    /**
     * Bricht die die Simulation ab
     *
     * @param event
     */
    @FXML
    public void handleAbortButton(ActionEvent event) {
        Logger.getRootLogger().removeAppender(textAreaAppender);
        if(thread != null) {
            thread.stop();
        }
        thread = null;
        changeView(menuScreenController.getView());
    }

    /**
     * Startet die Simualtion in einen neuen Thread
     * @param event
     */
    @FXML
    public void handleStartButton(ActionEvent event) {
        ExecuteAISimulation task = new ExecuteAISimulation();
        thread = new Thread(task);
        thread.start();
    }

    public void setActiveBoard(String activeBoard) {
        this.activeBoard = activeBoard;
    }

    /**
     * Startet die Simulation
     */
    public void start() {
        Integer currentGameCount = 1;
        startFirstGame();
        gameLoop(activeBoard, currentGameCount);
    }

    private void startFirstGame() {
        aiClazzes = new ArrayList<>();
        robotNames = new ArrayList<>();
        gameEngineWrapper.getPlayers().forEach(player -> {
            if((player instanceof AIPlayer)) {
                aiClazzes.add(player.getClass().getSimpleName());
                robotNames.add(player.getRobot().getName());
            }
        });
        defaultInputStream = System.out;
        defaultErrorStream = System.err;
        Console console = new Console(logWindow);
        PrintStream ps = new PrintStream(console, true);
        textAreaAppender = new TextAreaAppender(logWindow);
        Logger.getRootLogger().addAppender(textAreaAppender);
        System.setOut(ps);
        System.setErr(ps);
    }

    private void gameLoop(String activeBoard, Integer currentGameCount) {
        while (currentGameCount <= maxGameCount) {
            Integer finalCurrentGameCount = currentGameCount;
            Platform.runLater(() -> currentGameLabel.setText(CURRENT_GAME_LABEL_PREFIX + finalCurrentGameCount));
            currentRound = 1;
            try {
                playGames();
            } catch (Exception e) {
                LOGGER.error("Error while execute (Current game: " + currentGameCount +", current round: " + currentRound, e);
            }
            currentGameCount++;
            resetGame(activeBoard);
        }
        alertService.showAlert("Fertig", "Die Simualtion ist abgeschlossen", "Schaue in den Log für die Auswertung");
    }

    private void playGames() {
        while (gameEngineWrapper.getWinner() == null) {
            Platform.runLater(() -> currentRoundLabel.setText(CURRENT_ROUND_LABEL_PREFIX + currentRound));
            gameEngineWrapper.dealCards();
            startAIs();
            gameEngineWrapper.getMaster().activateBoard();
            gameEngineWrapper.getMaster().cleanup();
            currentRound++;
        }
        LOGGER.info(System.getProperty("line.separator") + System.getProperty("line.separator") + System.getProperty("line.separator") +
                "Found winner: " + gameEngineWrapper.getWinner().getRobot().getName() +
                System.getProperty("line.separator") + System.getProperty("line.separator") + System.getProperty("line.separator"));
    }

    private void resetGame(String activeBoard) {
        gameEngineWrapper.initMaster();
        for(int i = 0; i < aiClazzes.size(); i++) {
            gameEngineWrapper.createPlayer(robotNames.get(i), aiClazzes.get(i));
        }
        gameEngineWrapper.initialiseBoard(activeBoard);
    }

    private void startAIs() {
        gameEngineWrapper.getPlayers().forEach(player -> {
            if(!(player instanceof AIPlayer)) {
                return;
            }
            if(player.getRobot().getLives() <= 0) {
                return;
            }
            ((AIPlayer)player).executeTurn();
        });
    }

    private class Console extends OutputStream {

        private TextArea output;

        public Console(TextArea ta) {
            this.output = ta;
        }

        @Override
        public void write(int i) throws IOException {
            output.appendText(String.valueOf((char) i));
        }
    }

    private class ExecuteAISimulation extends Task<String> {

        @Override
        protected String call() throws Exception {
            start();
            return "The end";
        }
    }

}
