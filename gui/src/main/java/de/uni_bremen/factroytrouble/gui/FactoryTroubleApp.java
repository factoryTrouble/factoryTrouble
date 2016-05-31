/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gui;

import de.uni_bremen.factroytrouble.gui.controller.MenuScreenController;
import de.uni_bremen.factroytrouble.gui.services.util.ApplicationArgumentsService;
import de.uni_bremen.factroytrouble.gui.sound.MediaPlayerControls;
import de.uni_bremen.factroytrouble.spring.InitSpring;
import de.uni_bremen.factroytrouble.spring.SpringConfigHolder;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

/**
 * Startet unsere App. Basis von: https://gist.github.com/jewelsea/2305098
 *
 * @author Andre
 */
public class FactoryTroubleApp extends Application {

    private static final Logger LOGGER = Logger.getLogger(FactoryTroubleApp.class);

    private Pane splashLayout;
    private Stage mainStage;
    private static String[] applicationArguments;

    /**
     * Startet die Anwendung
     *
     * @param args
     *      Argumente für die Anwendung
     *
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        applicationArguments = args;
        launch(args);
    }

    /**
     * Initalisiert den Splashscreen
     */
    @Override
    public void init() {
        ImageView splash = new ImageView(ApplicationSettings.SPLASH_SCREEN);
        splash.setFitWidth(ApplicationSettings.SPLASH_SCREEN_WIDTH);
        splash.setFitHeight(ApplicationSettings.SPLASH_SCREEN_HEIGHT);
        splashLayout = new Pane();
        splashLayout.getChildren().add(splash);
        splashLayout.setEffect(new DropShadow());
    }

    /**
     * Startet das Fenster, sowie den Spring Kontext.
     *
     * @param initStage
     *      Die Stage, die inital genutzt wird
     * @throws Exception
     */
    @Override
    public void start(final Stage initStage) throws Exception {
        final Task<ApplicationContext> springInitTask = new InitSpringTask();
        showSplash(initStage, springInitTask, () -> showMainStage() );
        new Thread(springInitTask).start();
    }

    private void showMainStage() {
        mainStage = new Stage(StageStyle.DECORATED);
        mainStage.setTitle(ApplicationSettings.APPLICATION_NAME);
        mainStage.getIcons().add(ApplicationSettings.APPLICATION_ICON);

        mainStage.setScene(new Scene(SpringConfigHolder.getInstance().getContext().getBean(MenuScreenController.class).getView()));
        mainStage.setMinHeight(ApplicationSettings.MIN_HEIGHT);
        mainStage.setMinWidth(ApplicationSettings.MIN_WIDTH);
        mainStage.show();
    }

    private void showSplash(Stage initStage, Task<?> task, InitCompletionHandler initCompletionHandler) {
        task.stateProperty().addListener((observableValue, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                initStage.toFront();
                FadeTransition fadeSplash = new FadeTransition(Duration.seconds(1.2), splashLayout);
                fadeSplash.setFromValue(1.0);
                fadeSplash.setToValue(0.0);
                fadeSplash.setOnFinished(actionEvent -> initStage.hide());
                fadeSplash.play();
                initCompletionHandler.complete();
            }
        });

        Scene splashScene = new Scene(splashLayout);
        splashScene.setFill(Color.TRANSPARENT);
        initStage.initStyle(StageStyle.UNDECORATED);
        initStage.setScene(splashScene);
        initStage.centerOnScreen();
        initStage.show();
    }

    /**
     * Task zum erstellen des Spring Context
     */
    private class InitSpringTask extends Task<ApplicationContext> {
        @Override
        protected ApplicationContext call() throws InterruptedException {
            ApplicationContext springContext = InitSpring.init();
            springContext.getBean(MediaPlayerControls.class).setup();
            springContext.getBean(ApplicationArgumentsService.class).parseArguments(applicationArguments);
            if(springContext.getBean(ApplicationArgumentsService.class).getArgumentValue(ApplicationSettings.DEBUG_MODE)) {
                Logger.getRootLogger().setLevel(Level.ALL);
            }
            return springContext;
        }
    }

    public interface InitCompletionHandler {
        public void complete();
    }

}
