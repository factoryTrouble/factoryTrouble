/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gui.controller;

import de.uni_bremen.factroytrouble.gui.services.game.SoundService;
import de.uni_bremen.factroytrouble.gui.sound.MediaPlayerControls;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
/**
 * Controller für den OptionScreenController.
 */
@Controller
public class OptionScreenController extends FXMLController {

    private static final Logger LOGGER = Logger.getLogger(OptionScreenController.class);

    @FXML private Slider volumeSlider;
    @FXML private CheckBox soundCheckBox;
    @FXML private CheckBox musicCheckBox;

    @Autowired private MediaPlayerControls mediaPlayerControls;
    @Autowired private SoundService soundService;

    @Autowired private MenuScreenController menuScreenController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        this.addVolumeSliderListener();
    }

    @Override
    public Parent getView() {
        Parent view = super.getView();
        musicCheckBox.setSelected(mediaPlayerControls.isPlaying());
        volumeSlider.setValue(mediaPlayerControls.getVolume() * 100);
        return view;
    }

    @Override
    @Value("/views/OptionScreen.fxml")
    public void setFxmlFilePath(String filePath) {
        fxmlFilePath = filePath;
    }

    /**
     * Wechselt zum MenuScreen, wenn der Back Button betätigt wird.
     * 
     * @param event
     * @throws IOException
     */
    @FXML
    private void handleBackBtnAction(ActionEvent event) throws IOException {
        changeView(menuScreenController.getView());
    }

    /**
     * Schaltet die Musik ab, wenn die Checkbox ausgewählt wird. Setzt außerdem
     * den schicken Haken.
     */
    @FXML
    private void handleMusicCheckbox(ActionEvent event) throws IOException {
        if (!musicCheckBox.isSelected()) {
            mediaPlayerControls.stop();
            return;
        }
        mediaPlayerControls.play();
    }
    
    /**
     * Schaltet die Musik ab, wenn die Checkbox ausgewählt wird. Setzt außerdem
     * den schicken Haken.
     */
    @FXML
    private void handleSoundCheckbox(ActionEvent event) throws IOException {
        if (!soundCheckBox.isSelected()) {
            soundService.setActivated(false);
            return;
        }
        soundService.setActivated(true);
    }

    /**
     * Passt die Lautstärke entsprechend dem VolumeSlider an. Sollte die Musik ausgestellt
     * sein, wird sie beim Bewegen des Sliders wieder angestellt.
     * 
     * @param event
     * @throws IOException
     */
    @FXML
    private void handleVolumeDrag(ActionEvent event) throws IOException {
        mediaPlayerControls.setVolume(volumeSlider.getValue() / 100);
        musicCheckBox.setSelected(true);
    }

    /**
     * Registriert einen Listener für den Volume Slider. Dieser ändert die Lautstärke entsprechend,
     * wenn eine Veränderung am Slider vorgenommen wird.
     */
    private void addVolumeSliderListener() {
        volumeSlider.valueProperty().addListener((ov, oldValue, newValue) -> {
            LOGGER.debug(newValue.doubleValue());
            mediaPlayerControls.setVolume(volumeSlider.getValue() / 100);
        });

    }


    private class Resolution {
        private final int width;
        private final int height;

        public Resolution(int width, int height) {
            this.width = width;
            this.height = height;
        }

        @Override
        public String toString() {
            return width + " X " + height;
        }
    }
}
