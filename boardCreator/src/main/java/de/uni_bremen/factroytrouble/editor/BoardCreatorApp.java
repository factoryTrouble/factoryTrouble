/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.editor;

import de.uni_bremen.factroytrouble.editor.controller.BoardCreateController;
import de.uni_bremen.factroytrouble.editor.service.util.ActiveEditorService;
import de.uni_bremen.factroytrouble.editor.spring.SpringConfig;
import de.uni_bremen.factroytrouble.editor.spring.SpringConfigHolder;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Startet unsere App
 *
 * @author Andre
 */
public class BoardCreatorApp extends Application {
    
    /**
     *  Logger der Klasse BoardCreatorApp.
     */
    private static final Logger LOGGER = Logger.getLogger(BoardCreatorApp.class);

    public static void main(String[] args) {
        launch(args);
    }
    
    /**
     * Startet den grafischen Rahmen der Applikation.
     */
    @Override
    public void start(Stage primaryStage) {
        ApplicationContext springContext = initSpring();

        LOGGER.info("Start Application Frame");
        BoardCreateController boardCreateController = springContext.getBean(BoardCreateController.class);
        springContext.getBean(ActiveEditorService.class).setActiveEditor(boardCreateController);
        Scene scene = new Scene(boardCreateController.getView());
        scene.getStylesheets().add( getClass().getResource(ApplicationSettings.JAVA_FX_VIEW_PATH + "application.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setHeight(ApplicationSettings.HEIGHT);
        primaryStage.setWidth(ApplicationSettings.WIDTH);
        primaryStage.show();
    }

    /**
     * Initialisiert den Spring Kontext für die Applikation und gibt diesen zurück.
     * 
     * @return Spring Kontext
     */
    private ApplicationContext initSpring() {
        LOGGER.info("Init Spring Context");
        ApplicationContext springContext = new AnnotationConfigApplicationContext(SpringConfig.class);
        SpringConfigHolder.getInstance().setContext(springContext);
        return springContext;
    }
}
