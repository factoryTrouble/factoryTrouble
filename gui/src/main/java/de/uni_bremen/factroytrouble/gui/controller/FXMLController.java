/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gui.controller;

import de.uni_bremen.factroytrouble.gui.services.GameEngineWrapper;
import de.uni_bremen.factroytrouble.gui.services.game.AlertService;
import de.uni_bremen.factroytrouble.gui.services.game.SaveGameStateService;
import de.uni_bremen.factroytrouble.gui.controller.util.ChangeViewService;
import de.uni_bremen.factroytrouble.gui.generator.board.NewGameScreenBoardService;
import de.uni_bremen.factroytrouble.spring.PostConstructTask;
import de.uni_bremen.factroytrouble.spring.PostConstructTaskScheduler;
import de.uni_bremen.factroytrouble.spring.SpringConfigHolder;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Abstrakte Oberklasse für alle Controller.
 */
public abstract class FXMLController implements InitializingBean, Initializable, PostConstructTask {

    /**
     * Der Logger für die FXMLController Klasse.
     */
    private static final Logger LOGGER = Logger.getLogger(FXMLController.class);
    private static final String MAC_FILE_MENU_LABLE = "Datei";

    /**
     * Die Menu Items.
     */
    @FXML protected MenuBar menuBar;
    @FXML protected MenuItem newGameScreenMenuItem;
    @FXML protected MenuItem menuScreenMenuItem;
    @FXML protected MenuItem optionScreenMenuItem;
    @FXML protected MenuItem exitMenuItem;

    @Autowired protected NewGameScreenBoardService newGameScreenBoardService;
    @Autowired protected PostConstructTaskScheduler postConstructTaskScheduler;
    @Autowired protected GameFieldController gameFieldController;
    @Autowired protected PreviewGameFieldController previewGameFieldController;
    @Autowired protected GameEngineWrapper gameEngineWrapper;
    @Autowired protected ChangeViewService changeViewService;
    @Autowired protected SaveGameStateService saveGameStateService;
    @Autowired protected AlertService alertService;

    protected Parent view;
    protected String fxmlFilePath;
    protected MenuScreenController menuScreenController;
    protected ResultScreenController resultScreenController;
    protected GameScreenController gameScreenController;
    protected NewGameScreenController newGameScreenController;
    protected OptionScreenController optionScreenController;


    @Override
    public void postConstructTask() {
        menuScreenController = SpringConfigHolder.getInstance().getContext().getBean(MenuScreenController.class);
        resultScreenController = SpringConfigHolder.getInstance().getContext().getBean(ResultScreenController.class);
        gameScreenController = SpringConfigHolder.getInstance().getContext().getBean(GameScreenController.class);
        newGameScreenController = SpringConfigHolder.getInstance().getContext().getBean(NewGameScreenController.class);
        optionScreenController = SpringConfigHolder.getInstance().getContext().getBean(OptionScreenController.class);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.loadFXML();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.info("Initialize Controller");
        postConstructTaskScheduler.add(this);
        final String os = System.getProperty ("os.name");
        if (os != null && os.startsWith ("Mac")) {
            menuBar.getMenus().get(0).setText(MAC_FILE_MENU_LABLE);
            menuBar.useSystemMenuBarProperty().set(true);
        }
    }

    /**
     * Gibt die View des Controlles zurück
     *
     * @return
     *      Die View
     */
    public Parent getView() {
        try {
            this.loadFXML();
        } catch (final IOException e) {
            LOGGER.error("Unable to reload view", e);
        }
        return view;
    }

    /**
     * Setzt den FXML Filepath auf den, des entsprechenden Screens
     * @param filePath der Filepath des Screens.
     */
    public abstract void setFxmlFilePath(String filePath);

    /**
     * Wechselt die Ansicht auf die übergebene.
     * @param view die Ansicht, auf die gewechselt werden soll.
     */
    public void changeView(Parent view) {
        changeViewService.changeView(this.view, view);
    }

    /**
     * Wechselt beim Drücken des Menüeintrags "New Game" in den New Game Screen.
     * @throws IOException
     */
    @FXML
    protected void handleNewGameScreenItemAction(ActionEvent event) throws IOException {
        if(showAlertWhenGameScreen()) {
            changeView(newGameScreenController.getView());
        }
    }

    /**
     * Wechselt beim Drücken des Menüeintrags "Options" in den Option Screen.
     * @throws IOException
     */
    @FXML
    protected void handleOptionScreenItemAction(ActionEvent event) throws IOException {
        if(showAlertWhenGameScreen()) {
            changeView(optionScreenController.getView());
        }
    }

    /**
     * Wechselt beim Drücken des Menüeintrags "Main Menu" ins Hauptmenü.
     * @throws IOException
     */
    @FXML
    protected void handleMenuScreenItemAction(ActionEvent event) throws IOException {
        if(showAlertWhenGameScreen()) {
            changeView(menuScreenController.getView());
        }
    }

    /**
     *
     * @throws IOException
     */
    protected final void loadFXML() throws IOException {
        InputStream fxmlStream = this.getClass().getResourceAsStream(fxmlFilePath);
        final FXMLLoader loader = new FXMLLoader();
        loader.setController(this);
        view = loader.load(fxmlStream);
    }

    /**
     * Beendet die Applikation.
     */
    protected void exitApplication() {
        Platform.exit();
    }

    /**
     * Beendet beim Drücken des Menüeintrags "Exit" die Applikation.
     */
    @FXML
    private void handleExitMenuItemAction(ActionEvent event) {
        exitApplication();
    }

    private boolean showAlertWhenGameScreen() {
        if(this instanceof GameScreenController) {
            return alertService.showConfirm("Spiel beenden", "Sind Sie sich sicher?" + System.getProperty("line.separator") + "Dadurch wird das Spiel beendet und kann icht wieder gestartet werden", "Ja. Spiel beenden", "Nein. Weiter spielen");
        }
        return true;
    }

}