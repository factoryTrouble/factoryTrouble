package de.uni_bremen.factroytrouble.editor.service.view;

import de.saxsys.javafx.test.JfxRunner;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(JfxRunner.class)
public class GridPaneUtilServiceTest {

    private GridPaneUtilService gridPaneUtilService;
    private GridPane gridPane;

    private Node node1;
    private Node node2;

    @Before
    public void setUp() {
        gridPaneUtilService = new GridPaneUtilService();
        gridPane = new GridPane();
        node1 = new ImageView();
        node2 = new ImageView();
        gridPane.add(node1, 0, 0);
        gridPane.add(node2, 1, 0);
    }

    @Test
    public void shouldGetANodeFromGridPane() {
        assertEquals(node1, gridPaneUtilService.getNodeFromGridPane(gridPane, 0, 0));
    }

    @Test
    public void shouldReturnNullWhenNodeDonNotExists() {
        assertNull(gridPaneUtilService.getNodeFromGridPane(gridPane, 0, 1));
    }
}