/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gui.controller;

import de.uni_bremen.factroytrouble.gui.ApplicationSettings;
import de.uni_bremen.factroytrouble.gui.controller.components.PlayerChoiceBox;
import de.uni_bremen.factroytrouble.gui.controller.components.PlayerTextField;
import de.uni_bremen.factroytrouble.gui.controller.components.PreviewBoardImageView;
import de.uni_bremen.factroytrouble.gui.controller.components.PreviewRobotImageView;
import de.uni_bremen.factroytrouble.gui.services.game.StartGameService;
import de.uni_bremen.factroytrouble.player.Master;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Controller für den NewGameScreen.
 *
 * @author pudels
 */
@Controller
public class NewGameScreenController extends FXMLController {

    @FXML private Button startGameBtn;
    @FXML private Button backToMenuBtn;
    @FXML private ScrollPane scrlPane;
    @FXML private ListView gameFieldList;
    @FXML private GridPane gridPane;

    @Autowired private StartGameService startGameService;

    private List<ComboBox<String>> choosePlayerFields;
    private List<ImageView> robotImageViews;
    private List<Label> playerNameLabels;
    private String activeBoard = "";

    private Map<Integer, String> selectedPlayers;

    /**
     * Initialisiert den NewGameScreen. Hierbei werden die Namen der Spieler gesetzt,
     * die Liste der Spielfelder befüllt, die ChoiceBox für die Spielertypen befüllt und
     * die Bilder der Roboter gezeichnet.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        choosePlayerFields = new ArrayList<>();
        playerNameLabels = new ArrayList();
        playerNameLabels = new ArrayList<>();
        previewGameFieldController.addGameField(scrlPane);
        selectedPlayers = new HashMap<>();
        fillPlayerNameFields();
        fillGameList();
        fillSelectPlayerTypeChoiceBox();
        drawRobots();
    }

    @Override
    public Parent getView() {
        gameEngineWrapper.initMaster();
        return super.getView();
    }

    /**
     * Befüllt die Liste mit Spielbrettern mit entsprechenden Previews und zeigt die
     * entsprechend ausgewählten an.
     */
    private void fillGameList() {
        ObservableList gameFields = FXCollections.observableArrayList();
        List<String> boardNames = new ArrayList<>();
        gameEngineWrapper.getAvailableBoards().forEach(boardName -> {
            boardNames.add(boardName);
            BufferedImage bufferedImage = (BufferedImage) newGameScreenBoardService.getPreviewImage(boardName);
            ImageView boardView = new PreviewBoardImageView(bufferedImage);
            gameFields.add(boardView);
        });

        gameFieldList.setOrientation(Orientation.HORIZONTAL);
        gameFieldList.setItems(gameFields);
        gameFieldList.getSelectionModel().selectedIndexProperty().addListener(event ->
        {
            activeBoard = boardNames.get(gameFieldList.getSelectionModel().getSelectedIndex());
            previewGameFieldController.showPreview();
        });
    }

    @Override
    @Value("/views/NewGameScreen.fxml")
    public void setFxmlFilePath(String filePath) {
        fxmlFilePath = filePath;
    }

    /**
     * Setzt die Namen der Roboter.
     */
    private void fillPlayerNameFields() {
        Master.ROBOT_NAMES.forEach(robotName -> {
            Label playerTextField = new PlayerTextField(robotName);
            playerNameLabels.add(playerTextField);
            playerTextField.setTextFill(Color.web(ApplicationSettings.CLOUDS));
            gridPane.add(playerTextField, 1, Master.ROBOT_NAMES.indexOf(robotName));

        });
    }

    /**
     * Füllt die Choiceboxes mit den Spielertypen.
     */
    private void fillSelectPlayerTypeChoiceBox() {
        choosePlayerFields = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            ComboBox<String> choosePlayerField = new PlayerChoiceBox();
            final int finalI = i;
            choosePlayerField.setOnAction(event -> {
                selectedPlayers.remove(finalI);

                String playerType = choosePlayerField.getSelectionModel().getSelectedItem();
                if(!"Leer".equals(playerType)){
                    selectedPlayers.put(finalI, playerType);
                }

                previewGameFieldController.showPreview();

            });
            choosePlayerFields.add(choosePlayerField);
            gridPane.add(choosePlayerField, 2, i);
        }

    }

    /**
     * Zeichnet die Anzeigebilder der Roboter.
     */
    private void drawRobots() {
        robotImageViews = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            ImageView robotImageView = new PreviewRobotImageView(i);
            robotImageViews.add(robotImageView);
            gridPane.add(robotImageView, 0, i);
        }
    }

    /**
     * Springt zum MenuScreen, wenn der Back Button betätigt wird.
     *
     * @param event
     * @throws IOException
     */
    @FXML
    public void handleBackBtnAction(ActionEvent event) throws IOException {
        changeView(menuScreenController.getView());
        activeBoard = "";
    }

    /**
     * Wechselt zum GameScreen, wenn der Start Button betätigt wird.
     *
     * @param event
     * @throws IOException
     */
    @FXML
    private void handleStartBtnAction(ActionEvent event) throws IOException {
        new Thread(() -> startGameService.startGame()).start();
    }

    public String getActiveBoard() {
        return activeBoard;
    }

    public Map<Integer, String> getSelectedPlayers() {
        return selectedPlayers;

    }

}
