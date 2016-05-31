/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import de.uni_bremen.factroytrouble.gui.services.game.SaveGameStateService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller für den MenuScreen.
 * 
 * @author pudels
 */

@Controller
public class MenuScreenController extends FXMLController {
    
    @FXML private Button newGameBtn;
    @FXML private Button optionBtn;
    @FXML private Button exitBtn;
    
    private boolean savedGame;

    @Autowired private GameScreenController gameScreenController;
    @Autowired private NewGameScreenController newGameScreenController;
    @Autowired private OptionScreenController optionScreenController;
    @Autowired private SaveGameStateService saveGameStateService;

    @Override
    @Value("/views/MenuScreen.fxml")
    public void setFxmlFilePath(String filePath) {
        fxmlFilePath = filePath;
    }
    
    /**
     * Initialisiert den MenuScreen.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
    }

    /**
     * Beendet die Applikation, wenn der Exit-Button betätigt wird.
     * 
     * @param event
     * @throws IOException
     */
    @FXML
    private void handleExitBtnAction(ActionEvent event) throws IOException {
        exitApplication();
    }

    /**
     * Springt zum NewGameScreen, wenn der New Game Button betätigt wird.
     * 
     * @param event
     * @throws IOException
     */
    @FXML
    private void handleNewGameBtnAction(ActionEvent event) throws IOException {
        saveGameStateService.finishGame();
        changeView(newGameScreenController.getView());
    }

    /**
     * Springt zum OptionScreen, wenn der Option Screen Button betätigt wird.
     * 
     * @param event
     * @throws IOException
     */
    @FXML
    private void handleOptionBtnAction(ActionEvent event) throws IOException {
        changeView(optionScreenController.getView());
    }
    
    /**
     * Setzt die Variable, welche angibt ob der continueGame-Button aktiviert ist oder nicht.
     * @param savedGame
     */
    public void setContinueGameBtn(boolean savedGame){
        this.savedGame = savedGame;
    }

}
