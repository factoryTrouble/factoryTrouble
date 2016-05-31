package de.uni_bremen.factroytrouble.gui.controller.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by johannes.gesenhues on 21.01.16.
 */
public class PlayerChoiceBoxTest {
    private PlayerChoiceBox playerChoiceBox;
    ObservableList<String> choiceBoxEntries = FXCollections.observableArrayList(
            "Leer", "Mensch");

    @Before
    public void setUp() throws Exception {
        playerChoiceBox = new PlayerChoiceBox();

    }


    @Test
    public void shouldCreateHandCardImageWithCorrectSettings(){
        assertTrue(playerChoiceBox.getPrefHeight() == 45);
        assertTrue(playerChoiceBox.getPrefWidth() == 200);
        assertTrue(playerChoiceBox.getItems().containsAll(choiceBoxEntries));
        assertTrue(playerChoiceBox.getSelectionModel().getSelectedItem().equals("Leer"));
    }

}