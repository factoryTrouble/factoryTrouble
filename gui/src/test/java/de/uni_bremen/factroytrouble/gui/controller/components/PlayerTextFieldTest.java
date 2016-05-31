package de.uni_bremen.factroytrouble.gui.controller.components;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by johannes.gesenhues on 21.01.16.
 */
public class PlayerTextFieldTest {
    private PlayerTextField playerTextField;

    @Before
    public void setUp() throws Exception {
        playerTextField = new PlayerTextField("TestRobotName");
    }

    @Test
    public void shouldCreatePlayerTextFieldWithCorrectSettings() {
        System.out.println(playerTextField.getFont());
        assertTrue(playerTextField.getPrefHeight() == 45);
        assertTrue(playerTextField.getPrefWidth() == 175);
        assertTrue(playerTextField.getText().equals("TestRobotName"));
//        assertTrue(playerTextField.getFont().getName().equals("Robotica"));
        assertTrue(playerTextField.getFont().getSize() == 22);


    }
}