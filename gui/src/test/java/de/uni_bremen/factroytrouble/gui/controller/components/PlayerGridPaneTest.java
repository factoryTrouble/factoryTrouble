package de.uni_bremen.factroytrouble.gui.controller.components;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by johannes.gesenhues on 21.01.16.
 */
public class PlayerGridPaneTest {
    private PlayerGridPane playerGridPane;

    @Before
    public void setUp() throws Exception {
        playerGridPane = new PlayerGridPane();
    }

    @Test
    public void shouldCreatePlayerGridPaneWithCorrectSettings(){
        assertTrue(playerGridPane.isGridLinesVisible());
        assertTrue(playerGridPane.getPrefWidth() == 400);
        assertTrue(playerGridPane.getMinWidth() == 400);
        assertTrue(playerGridPane.getMaxWidth() == 400);
        assertTrue(playerGridPane.getPrefHeight() == 800);

        ColumnConstraints ccs = playerGridPane.getColumnConstraints().get(0);
        assertTrue(ccs.getMinWidth()== 400);
        assertTrue(ccs.getPrefWidth()== 400);
        assertTrue(ccs.getMaxWidth()== 400);
        assertTrue(ccs.getHgrow() == Priority.ALWAYS);
    }
}