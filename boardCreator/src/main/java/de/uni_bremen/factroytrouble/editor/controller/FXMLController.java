/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.editor.controller;

import de.uni_bremen.factroytrouble.editor.service.util.ActiveEditorService;
import de.uni_bremen.factroytrouble.editor.service.view.ChangeViewService;
import de.uni_bremen.factroytrouble.editor.spring.SpringConfigHolder;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.MenuBar;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Abstrakte Oberklasse für alle Controller.
 *
 * @author Andre
 */
public abstract class FXMLController implements InitializingBean, Initializable {

    /**
     * Der Logger für die FXMLController Klasse.
     */
    private static final Logger LOGGER = Logger.getLogger(FXMLController.class);

    @Autowired private ChangeViewService changeViewService;
    @Autowired private ActiveEditorService activeEditorService;

    /**
     * Die Menu Items.
     */
    @FXML protected MenuBar menuBar;

    protected Parent view;
    protected String fxmlFilePath;


    /**
     * Lädt die View initial
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        this.loadFXML();
    }

    /**
     * Initalisiert die Komponente
     *
     * @param location
     *      s. JavaDoc von Initializable
     * @param resources
     *      s. JavaDoc von Initializable
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.info("Initialize Controller");
        initMacMenu();
    }

    /**
     * Gibt die View zurück
     *
     * @return
     *      Container der View
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
     * Wechselt die Ansicht auf die übergebene.
     *
     * @param view
     *      die Ansicht, auf die gewechselt werden soll.
     */
    public void changeView(Parent view) {
        if(view.equals(this.view)) {
            LOGGER.info("Try to reopen current view. Prevent this");
            return;
        }
        changeViewService.changeView(this.view, view);
    }

    /**
     * Setzt den FXML Filepath auf den, des entsprechenden Screens
     *
     * @param filePath
     *      der Filepath des Screens.
     */
    public abstract void setFxmlFilePath(String filePath);

    /**
     * Lädt die View
     *
     * @throws java.io.IOException
     *      Wenn die View nicht vorhanden ist
     */
    protected void loadFXML() throws IOException {
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

    protected void initMacMenu() {
        final String os = System.getProperty ("os.name");
        if (os != null && os.startsWith ("Mac")) {
            menuBar.useSystemMenuBarProperty().set(true);
        }
    }

    @FXML
    private void openCreateBoard(ActionEvent actionEvent) {
        BoardCreateController boardCreateController = SpringConfigHolder.getInstance().getContext().getBean(BoardCreateController.class);
        changeView(boardCreateController.getView());
        activeEditorService.setActiveEditor(boardCreateController);
    }

    @FXML
    private void openCreateDock(ActionEvent actionEvent) {
        DockCreateController dockCreateController = SpringConfigHolder.getInstance().getContext().getBean(DockCreateController.class);
        changeView(dockCreateController.getView());
        activeEditorService.setActiveEditor(dockCreateController);
    }

    @FXML
    private void openCreateCourse(ActionEvent actionEvent) {
        CourseCreateController courseCreateController = SpringConfigHolder.getInstance().getContext().getBean(CourseCreateController.class);
        changeView(courseCreateController.getView());
        activeEditorService.setActiveEditor(courseCreateController);
    }

    @FXML
    private void closeApplication(ActionEvent actionEvent) {
        exitApplication();
    }

}