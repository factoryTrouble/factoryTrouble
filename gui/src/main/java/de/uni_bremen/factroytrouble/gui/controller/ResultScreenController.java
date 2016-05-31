/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gui.controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

@Controller
public class ResultScreenController extends FXMLController {

    private static final Logger LOGGER = Logger.getLogger(ResultScreenController.class);

    @FXML private Button backToMenuBtn;
    @FXML private Label winnerLabel;
    @FXML private ImageView imageView;

    @Autowired private MenuScreenController menuScreenController;

    @Override
    @Value("/views/ResultScreen.fxml")
    public void setFxmlFilePath(String filePath) {
        fxmlFilePath = filePath;
    }

    /**
     * Initialisiert den ResultScreen.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        try {
            imageView.setImage(SwingFXUtils.toFXImage(ImageIO.read(getClass().getResourceAsStream("/pictures/resultScreen.png")), null));
        } catch (IOException e) {
            LOGGER.error("Fail to load the victory image", e);
        }
    }
    
    /**
     * Wechselt zum MenuScreen, wenn der Back To Menu Button betätigt wird.
     * 
     * @param event
     * @throws IOException
     */
    @FXML
    private void handleBackToMenuBtnAction(ActionEvent event) throws IOException {
        changeView(menuScreenController.getView());
        gameEngineWrapper.deleteMaster(0);
    }
    
    public void setWinnerLabel(String winner){
        winnerLabel.setText(winner);
    }

    /**
     * Wechselt beim Drücken des Menüeintrags "New Game" in den New Game Screen.
     * @throws IOException
     */
    @FXML
    protected void handleNewGameScreenItemAction(ActionEvent event) throws IOException {
        super.handleNewGameScreenItemAction(event);
        gameEngineWrapper.deleteMaster(0);
    }

    /**
     * Wechselt beim Drücken des Menüeintrags "Options" in den Option Screen.
     * @throws IOException
     */
    @FXML
    protected void handleOptionScreenItemAction(ActionEvent event) throws IOException {
        super.handleOptionScreenItemAction(event);
        gameEngineWrapper.deleteMaster(0);
    }

    /**
     * Wechselt beim Drücken des Menüeintrags "Main Menu" ins Hauptmenü.
     * @throws IOException
     */
    @Override
    @FXML
    protected void handleMenuScreenItemAction(ActionEvent event) throws IOException {
        super.handleMenuScreenItemAction(event);
        gameEngineWrapper.deleteMaster(0);
    }
    

}
