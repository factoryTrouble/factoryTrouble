/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gui.controller.components;

import com.jfoenix.controls.JFXComboBox;
import de.uni_bremen.factroytrouble.gui.ApplicationSettings;
import de.uni_bremen.factroytrouble.player.Master;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 * Created by johannes.gesenhues on 30.12.15.
 * <p>
 * Diese Klasse entspricht einer ChoiceBox auf dem NewGameScreen, in der der Typ des Spielers
 * gewählt werden kann
 */
public class PlayerChoiceBox extends JFXComboBox<String> {


    ObservableList<String> choiceBoxEntries = FXCollections.observableArrayList(
            "Leer", "Mensch");

    public PlayerChoiceBox() {
        super();
        choiceBoxEntries.addAll(Master.KI_NAMES);
        setPrefHeight(45);
        setPrefWidth(200);
        setStyle("-fx-background-color: " + ApplicationSettings.ORANGE);
        GridPane.setHalignment(this, HPos.CENTER);
        GridPane.setValignment(this, VPos.CENTER);
        GridPane.setHgrow(this, Priority.ALWAYS);
        GridPane.setVgrow(this, Priority.ALWAYS);
        setItems(choiceBoxEntries);
        getSelectionModel().select(0);
    }
}
