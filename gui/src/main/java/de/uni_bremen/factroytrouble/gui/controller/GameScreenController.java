/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gui.controller;

import de.uni_bremen.factroytrouble.gui.services.game.*;
import de.uni_bremen.factroytrouble.player.ProgramCard;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller für den GameScreen.
 */
@Controller
public class GameScreenController extends FXMLController {

    public static final String NEXT_PLAYER = "Nächster Spieler";
    public static final String END_ROUND = "Beende Runde";
    @FXML private Button endRoundBtn;
    @FXML private ToggleButton powerDownbtn;
    @FXML private ScrollPane scrlPane;
    @FXML private AnchorPane registerPane;
    @FXML private AnchorPane cardPane;
    @FXML private GridPane playerPane;
    @FXML private ScrollPane playerScrlPane;

    @Autowired protected RegisterCardsService addCardsToRegister;
    @Autowired protected EndBtnService endBtnService;
    @Autowired protected PerformRoundForPlayerService performRoundForPlayerService;
    @Autowired protected PowerDownService powerDownService;
    @Autowired protected PlayerGridService playerGridService;
    @Autowired protected StartGameService startGameService;
    @Autowired protected SaveGameStateService saveGameStateService;

    private ProgramCard[] registers = new ProgramCard[5];
    private ProgramCard[] cards = new ProgramCard[10];
    private List<PlayerInfoController> playerInfos;
    private int activePlayer = 0;

    /**
     * Initialisiert den GameScreen.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        gameFieldController.addGameField(scrlPane);
    }

    @Override
    @Value("/views/GameScreen.fxml")
    public void setFxmlFilePath(String filePath) {
        fxmlFilePath = filePath;
    }


    /**
     * Beendet das Spiel, indem zum Ergebnisbildschirm gewechselt wird.
     *
     * @throws IOException
     */
    public void endGame(){
        resultScreenController.setWinnerLabel(gameEngineWrapper.getWinner().getRobot().getName());
        saveGameStateService.finishGame();
        changeView(resultScreenController.getView());
        resultScreenController.setWinnerLabel(gameEngineWrapper.getWinner().getRobot().getName());
    }

    /**
     * Startet das Spiel.
     *
     * @param activeBoard
     */
    public void start(String activeBoard) {
        this.playerInfos = new ArrayList<>();
        gameFieldController.setUp(scrlPane, activeBoard);
        playerGridService.initPlayersGrid();
        gameEngineWrapper.prepareRound();
    }

    public int getActivePlayer() {
        return activePlayer;
    }

    public AnchorPane getCardPane() {
        return cardPane;
    }


    public ProgramCard[] getCards() {
        return cards;
    }

    public AnchorPane getRegisterPane() {
        return registerPane;
    }

    public ProgramCard[] getRegisters() {
        return registers;
    }

    public List<PlayerInfoController> getPlayerInfos() {
        return playerInfos;
    }

    public ScrollPane getPlayerScrlPane() {
        return playerScrlPane;
    }

    public void setActivePlayer(int activePlayer) {
        playerInfos.get(activePlayer).setActive(true);
        this.activePlayer = activePlayer;
    }

    public void setNextButton(boolean nextPlayer){
        if(nextPlayer)
            endRoundBtn.setText(NEXT_PLAYER);
        else
            endRoundBtn.setText(END_ROUND);


    }

    /**
     * Versteckt die Button während der Anzeige der Züge
     * @param hide true wenn Versteckt
     */
    public void hideButtons(boolean hide) {
        endRoundBtn.setVisible(!hide);
        powerDownbtn.setVisible(!hide);
    }


    /**
     * Beendet die aktuelle Runde des aktuellen Spielers, sofern alle offenen Kartenslots
     * des Spielers mit Karten belegt sind. Ist der aktuelle Spieler der letzte Spieler, der
     * seinen Zug machen muss, beginnt die Auswertungsphase. Ansonsten ist der nächste Spieler
     * am Zug.
     *
     * @param event
     * @throws IOException
     */
    @FXML
    private void handleEndRoundBtnAction(ActionEvent event) throws IOException {
        if(powerDownbtn.isSelected()){
            powerDownService.perform(activePlayer);
            powerDownbtn.setSelected(false);
        }
        endBtnService.endRound(activePlayer);
    }
    
    /**
     * Gibt die ScrollPane zurück, in welcher sich das SPielfeld befindet.
     * @return
     */
    public ScrollPane getScrlPane(){
        return scrlPane;
    }
    /**
     * Wechselt beim Drücken des Menüeintrags "Main Menu" ins Hauptmenü.
     * @throws IOException
     */
    @FXML
    @Override
    protected void handleMenuScreenItemAction(ActionEvent event) throws IOException {
        saveGameStateService.saveGameState(playerInfos, gameFieldController);
        menuScreenController.setContinueGameBtn(true);
        super.handleMenuScreenItemAction(event);
    }
    
    /**
     * Wechselt beim Drücken des Menüeintrags "Options" in den Option Screen.
     * @throws IOException
     */
    @FXML
    @Override
    protected void handleOptionScreenItemAction(ActionEvent event) throws IOException {
        saveGameStateService.saveGameState(playerInfos, gameFieldController);
        menuScreenController.setContinueGameBtn(true);
        changeView(optionScreenController.getView());
    }
}
