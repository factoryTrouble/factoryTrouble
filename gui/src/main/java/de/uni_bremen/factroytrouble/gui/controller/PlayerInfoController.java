/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gui.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import de.uni_bremen.factroytrouble.gui.controller.components.RobotImageView;
import de.uni_bremen.factroytrouble.gui.services.GameEngineWrapper;
import de.uni_bremen.factroytrouble.spring.SpringConfigHolder;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;

/**
 * Eine Klasse, welche von {@link AnchorPane} erbt und alle Informationen der
 * jeweiligen Spieler wiedergibt.
 * 
 * @author Ottmar
 *
 */
public class PlayerInfoController extends AnchorPane {

    public static final String FONT_NAME = "Robotica";
    private static final Logger LOGGER = Logger.getLogger(RobotImageView.class);
    /**
     * Der Name des Spielers.
     */
    private Label playerName;
    /**
     * Die Lebenspunkte des Spielers.
     */
    private Label livePoints;
    /**
     * Die ImageView dieses Spielers
     */
    private ImageView imageView;
    /**
     * Das Bild der Schadenspunkte
     */
    private Image hpImage;
    /**
     * Das Bild der Lebenspunkte
     */
    private Image lpImage;
    /**
     * Das Flaggenbild
     */
    private Image flagImage;
    /**
     * Das Bild des Grabsteines dieses Spielers
     */
    private Image graveStoneImage;
    /**
     * Das Bild des Roboters dieses Spielers
     */
    private Image robotImage;
    /**
     * Der Schadenspunkte des Spielers.
     */
    private Label healthPoints;
    /**
     * Der Schadenspunkte des Spielers.
     */
    private Label flags;
    /**
     * Die einzelnen Leben als Kreise dargestellt.
     */
    private ImageView[] lives = new ImageView[3];
    /**
     * Die einzelnen Schadenspunkte als Rechtecke dargestellt.
     */
    private ImageView[] health = new ImageView[10];

    private GameEngineWrapper gameEngineWrapper;
    
    private int flagCount;
    
    private List<ImageView> flagViews = new ArrayList<ImageView>();

    /**
     * Erstellt ein Panel mit allen Informationen zum gegebenen Spieler.
     * 
     * @param playerName
     *            Der Name des Spielers
     */
    public PlayerInfoController(String playerName) {
        super();
        gameEngineWrapper = SpringConfigHolder.getInstance().getContext().getBean(GameEngineWrapper.class);
        setWidth(400);
        try {
            hpImage = SwingFXUtils
                    .toFXImage(ImageIO.read(getClass().getResourceAsStream("/game/tiles/gear.png")), null);
            lpImage = SwingFXUtils
                    .toFXImage(ImageIO.read(getClass().getResourceAsStream("/game/tiles/heart.png")), null);
            flagImage = SwingFXUtils
                    .toFXImage(ImageIO.read(getClass().getResourceAsStream("/game/tiles/flag_transparent.png")), null);
            graveStoneImage = SwingFXUtils
                    .toFXImage(ImageIO.read(getClass().getResourceAsStream("/game/robots/gravestone.png")), null);
            robotImage = SwingFXUtils.toFXImage(ImageIO.read(getClass().getResourceAsStream(
                    "/game/robots/side_" + gameEngineWrapper.getPlayerNumberByName(playerName) + ".png")),
                    null);
        } catch (IOException e) {
            LOGGER.error("Failed to load Images", e);
        }
        this.playerName = new Label(playerName);
        flagCount = gameEngineWrapper.getFlagCount(SpringConfigHolder.getInstance().getContext().getBean(GameFieldController.class).getBoardName());
        initName();
        initHp();
        initLp();
        initRobot();
        initFlags();
    }

    /**
     * Initiallisiert das Panel mit allen nötigen Informationen.
     */
    private void initRobot() {
        imageView = new ImageView();
        imageView.setImage(robotImage);
        imageView.setFitHeight(75);
        imageView.setFitWidth(75);
        imageView.setLayoutX(getWidth() - 85);
        imageView.setLayoutY(10);
        getChildren().add(imageView);
    }

    /**
     * Initialisiert den Spielernamen.
     */
    private void initName() {
        playerName.setFont(new Font(FONT_NAME, 20));
        playerName.setLayoutX(getWidth() / 2 - playerName.getText().length() * 4);
        getChildren().add(playerName);
    }

    /**
     * Initialisiert und zeichnet die Health Points.
     */
    private void initHp() {
        healthPoints = new Label("Schaden");
        healthPoints.setLayoutX(0);
        healthPoints.setLayoutY(80);
        healthPoints.setFont(new Font(FONT_NAME, 20));
        getChildren().add(healthPoints);
        for (int i = 0; i < 10; i++) {
            ImageView hp = new ImageView(hpImage);
            hp.setFitWidth(20);
            hp.setFitHeight(20);
            hp.setLayoutX(95 + 22*i);
            hp.setLayoutY(80);
            getChildren().add(hp);
            health[i] = hp;
        }
    }

    private void initLp() {
        livePoints = new Label("Leben");
        livePoints.setLayoutX(80);
        livePoints.setLayoutY(45);
        livePoints.setFont(new Font(FONT_NAME, 20));
        getChildren().add(livePoints);
        for (int i = 0; i < 3; i++) {
            ImageView lp = new ImageView(lpImage);
            lp.setFitWidth(20);
            lp.setFitHeight(20);
            lp.setLayoutX(160 + 25*i);
            lp.setLayoutY(45);
            getChildren().add(lp);
            lives[i] = lp;
        }
    }
    
    private void initFlags() {
        flags = new Label("Flaggen");
        flags.setLayoutX(0);
        flags.setLayoutY(120);
        flags.setFont(new Font(FONT_NAME, 20));
        getChildren().add(flags);
        for (int i = 0; i < flagCount; i++) {
            ImageView flagView = new ImageView(flagImage);
            flagView.setFitWidth(24);
            flagView.setFitHeight(24);
            flagView.setLayoutX(95 + 25*i);
            flagView.setLayoutY(120);
            flagView.setOpacity(0.3);
            getChildren().add(flagView);
            flagViews.add(flagView);
        }
    }
    
    public void hitFlag(int flagsTouched){
        for (int i = 0; i < flagsTouched; i++) {
            flagViews.get(i).setOpacity(1);
        }
    }

    /**
     * Setzt die Schadenspunkte auf den übergebenen Wert.
     *
     * @param hp
     */
    public void setHp(int hp) {
        if (hp < 0 || hp > 10) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < health.length; i++) {
            if (i >= hp) {
                health[i].setOpacity(0.3);
            } else {
                health[i].setOpacity(1);
            }
        }
    }

    /**
     * Setzt die Lebenspunkte auf den übergebenen Wert. Falls noch Leben
     * vorhanden sind werden die Schadenspunkte reduziert, sonst wird das
     * Fenster ausgegraut
     *
     * @param lp
     *            Die Lebenspunkte
     */
    public void setLp(int lp) {
        if (lp < 0 || lp > 3) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < lives.length; i++) {
            if (i >= lp) {
                lives[i].setOpacity(0.3);
            }
        }
        if (lp == 0) {
            imageView.setImage(graveStoneImage);
        }
    }

    /**
     * Makiert den Controller als aktiv
     * 
     * @param active
     *            aktiv
     */
    public void setActive(boolean active) {
        if (active)
            this.setStyle("-fx-background-color: #00ff19;");
        else
            this.setStyle("-fx-background-color: #ffffff;");
    }

    /**
     * Makiert den Controller als finished
     * 
     * @param finished
     *            finished
     */
    public synchronized void setFinished(boolean finished) {
        if (finished) {
            this.setStyle("-fx-background-color: #ff898e;");
        }
        else {
            this.setStyle("-fx-background-color: #ffffff;");
        }
    }

    public String getName() {
        return playerName.getText();
    }
    
    
}
