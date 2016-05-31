package de.uni_bremen.factroytrouble.editor.controller;

import com.sun.javafx.collections.ObservableListWrapper;
import de.saxsys.javafx.test.JfxRunner;
import de.uni_bremen.factroytrouble.editor.component.EditorTileFillToolIcon;
import de.uni_bremen.factroytrouble.editor.component.Tile;
import de.uni_bremen.factroytrouble.editor.container.image.TileGroundImageContainer;
import de.uni_bremen.factroytrouble.editor.service.tile.CombineTileService;
import de.uni_bremen.factroytrouble.editor.service.tile.ConveyorBeltTileDispatcherService;
import de.uni_bremen.factroytrouble.editor.service.util.ActiveEditorService;
import de.uni_bremen.factroytrouble.editor.service.util.EvaluateClickOrientationService;
import de.uni_bremen.factroytrouble.editor.service.view.ChangeViewService;
import de.uni_bremen.factroytrouble.editor.service.view.GridPaneUtilService;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;

import java.util.Arrays;

import static de.uni_bremen.factroytrouble.editor.TestUtil.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(JfxRunner.class)
public class AbstractEditorControllerTest {

    @Mock private GridPaneUtilService gridPaneUtilService;
    @Mock private ConveyorBeltTileDispatcherService conveyorBeltTileDispatcherService;
    @Mock private TileGroundImageContainer tileGroundImageContainer;
    @Mock private EvaluateClickOrientationService evaluateClickOrientationService;
    @Mock private CombineTileService combineTileService;
    @Mock private ChangeViewService changeViewService;
    @Mock private ActiveEditorService activeEditorService;

    @Mock protected GridPane groundGrid;
    @Mock protected GridPane objectGrid;
    @Mock protected GridPane markerGrid;
    @Mock protected GridPane tileGridPane;
    @Mock protected ScrollPane tileGridScrollPane;
    @Mock protected AnchorPane tileGridAnchorPane;

    private EditorTileFillToolIcon editorTileFillToolIcon;
    private Tile tile;
    private ObservableList<Node> nodes;

    @InjectMocks private TestAbstractEditorController testAbstractEditorController;

    @Before
    public void setUp() throws Exception {
        testAbstractEditorController = new TestAbstractEditorController();
        MockitoAnnotations.initMocks(this);

        editorTileFillToolIcon = mock(EditorTileFillToolIcon.class);
        tile = mock(Tile.class);
        nodes = new ObservableListWrapper<Node>(Arrays.asList(new EditorTileFillToolIcon[]{editorTileFillToolIcon}));
        when(groundGrid.getChildren()).thenReturn(nodes);
        when(objectGrid.getChildren()).thenReturn(nodes);
        when(markerGrid.getChildren()).thenReturn(nodes);

        injectPrivateFieldIntoParent("groundGrid", testAbstractEditorController, groundGrid);
        injectPrivateFieldIntoParent("objectGrid", testAbstractEditorController, objectGrid);
        injectPrivateFieldIntoParent("markerGrid", testAbstractEditorController, markerGrid);
        injectPrivateFieldIntoParent("tileGridPane", testAbstractEditorController, tileGridPane);
        injectPrivateFieldIntoParent("tileGridScrollPane", testAbstractEditorController, tileGridScrollPane);
        injectPrivateFieldIntoParent("tileGridAnchorPane", testAbstractEditorController, tileGridAnchorPane);
    }

    @Test
    public void shouldSetActiveTool() {
        testAbstractEditorController.setActiveTool(editorTileFillToolIcon);
        assertEquals(editorTileFillToolIcon, testAbstractEditorController.activeTool);
    }

    @Test
    public void shouldResetAllOtherToolsWhenSetNewEditorTool() {
        testAbstractEditorController.setActiveTool(mock(EditorTileFillToolIcon.class));
        verify(editorTileFillToolIcon, times(3)).resetClickedStatus();
    }

    @Test
    public void shouldNotTryToResetTheToolsWhenActiveToolSetToNull() {
        testAbstractEditorController.setActiveTool(null);
        verify(editorTileFillToolIcon, never()).resetClickedStatus();
    }

    private class TestAbstractEditorController extends AbstractEditorController {
        @Override
        @Value("/views/boardCreateView.fxml")
        public void setFxmlFilePath(String filePath) {

        }
    }

}