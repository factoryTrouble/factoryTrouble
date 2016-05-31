package de.uni_bremen.factroytrouble.editor.service.tile;

import com.sun.javafx.collections.ObservableListWrapper;
import de.saxsys.javafx.test.JfxRunner;
import de.uni_bremen.factroytrouble.editor.component.EditorTileFillToolIcon;
import de.uni_bremen.factroytrouble.editor.component.Tile;
import de.uni_bremen.factroytrouble.editor.container.image.TileGroundImageContainer;
import de.uni_bremen.factroytrouble.editor.data.FieldData;
import de.uni_bremen.factroytrouble.editor.data.FieldObject;
import de.uni_bremen.factroytrouble.editor.data.GroundFill;
import de.uni_bremen.factroytrouble.editor.data.Orientation;
import de.uni_bremen.factroytrouble.editor.service.util.ActiveEditorService;
import de.uni_bremen.factroytrouble.editor.service.util.EvaluateClickOrientationService;
import de.uni_bremen.factroytrouble.editor.service.view.ChangeViewService;
import de.uni_bremen.factroytrouble.editor.service.view.GridPaneUtilService;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.imageio.ImageIO;

import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(JfxRunner.class)
public class FillTileServiceTest {

    @Mock private GridPaneUtilService gridPaneUtilService;
    @Mock private ConveyorBeltTileDispatcherService conveyorBeltTileDispatcherService;
    @Mock private TileGroundImageContainer tileGroundImageContainer;
    @Mock private EvaluateClickOrientationService evaluateClickOrientationService;
    @Mock private CombineTileService combineTileService;
    @Mock private ChangeViewService changeViewService;
    @Mock private ActiveEditorService activeEditorService;

    @Mock private GridPane groundGrid;
    @Mock private GridPane objectGrid;
    @Mock private GridPane markerGrid;
    @Mock private GridPane tileGridPane;
    @Mock private ScrollPane tileGridScrollPane;
    @Mock private AnchorPane tileGridAnchorPane;

    private EditorTileFillToolIcon editorTileFillToolIcon;
    private Tile tile;
    private ObservableList<Node> nodes;

    @InjectMocks private FillTileService fillTileService;

    @Before
    public void setUp() throws Exception {
        fillTileService = new FillTileService();
        MockitoAnnotations.initMocks(this);

        editorTileFillToolIcon = mock(EditorTileFillToolIcon.class);
        tile = mock(Tile.class);
        nodes = new ObservableListWrapper<Node>(Arrays.asList(new EditorTileFillToolIcon[]{editorTileFillToolIcon}));
        when(groundGrid.getChildren()).thenReturn(nodes);
        when(objectGrid.getChildren()).thenReturn(nodes);
        when(markerGrid.getChildren()).thenReturn(nodes);
    }

    @Test
    public void shouldAddAConveyorBeltAsGround() throws Exception {
        when(editorTileFillToolIcon.getGroundFill()).thenReturn(GroundFill.CONVEYOR_BELT);
        when(conveyorBeltTileDispatcherService.getConveyorBeltByEditorTool(editorTileFillToolIcon)).thenReturn(ImageIO.read(getClass().getResourceAsStream("/game/tiles/empty.png")));
        fillTileService.fillGroundWithTool(1, 1, tile, editorTileFillToolIcon, tileGridPane);
        verify(tile).setGroundFill(GroundFill.CONVEYOR_BELT);
    }

    @Test
    public void shouldSetTheOrientationForAConveyorBelt() throws Exception {
        when(editorTileFillToolIcon.getGroundFill()).thenReturn(GroundFill.CONVEYOR_BELT);
        when(editorTileFillToolIcon.getOrientation()).thenReturn(Orientation.NORTH);
        when(conveyorBeltTileDispatcherService.getConveyorBeltByEditorTool(editorTileFillToolIcon)).thenReturn(ImageIO.read(getClass().getResourceAsStream("/game/tiles/empty.png")));
        fillTileService.fillGroundWithTool(1, 1, tile, editorTileFillToolIcon, tileGridPane);
        verify(tile).setOrientation(Orientation.NORTH);
    }

    @Test
    public void shouldAddAnExpressConveyorBeltAsGround() throws Exception {
        when(editorTileFillToolIcon.getGroundFill()).thenReturn(GroundFill.EXPRESS_CONVEYOR_BELT);
        when(conveyorBeltTileDispatcherService.getConveyorBeltByEditorTool(editorTileFillToolIcon)).thenReturn(ImageIO.read(getClass().getResourceAsStream("/game/tiles/empty.png")));
        fillTileService.fillGroundWithTool(1, 1, tile, editorTileFillToolIcon, tileGridPane);
        verify(tile).setGroundFill(GroundFill.EXPRESS_CONVEYOR_BELT);
    }

    @Test
    public void shouldDispatchConveyorBeltNeighborsWhenNewConveyorBeltSet() throws Exception {
        when(editorTileFillToolIcon.getGroundFill()).thenReturn(GroundFill.CONVEYOR_BELT);
        when(editorTileFillToolIcon.getOrientation()).thenReturn(Orientation.NORTH);
        when(conveyorBeltTileDispatcherService.getConveyorBeltByEditorTool(editorTileFillToolIcon)).thenReturn(ImageIO.read(getClass().getResourceAsStream("/game/tiles/empty.png")));
        fillTileService.fillGroundWithTool(1, 1, tile, editorTileFillToolIcon, tileGridPane);
        verify(conveyorBeltTileDispatcherService).dispatchNeighborsInGrid(eq(tileGridPane), eq(1), eq(1));
    }

    @Test
    public void shouldDispatchConveyorBeltNeighborsWhenNewExpressConveyorBeltSet() throws Exception {
        when(editorTileFillToolIcon.getGroundFill()).thenReturn(GroundFill.EXPRESS_CONVEYOR_BELT);
        when(editorTileFillToolIcon.getOrientation()).thenReturn(Orientation.NORTH);
        when(conveyorBeltTileDispatcherService.getConveyorBeltByEditorTool(editorTileFillToolIcon)).thenReturn(ImageIO.read(getClass().getResourceAsStream("/game/tiles/empty.png")));
        fillTileService.fillGroundWithTool(1, 1, tile, editorTileFillToolIcon, tileGridPane);
        verify(conveyorBeltTileDispatcherService).dispatchNeighborsInGrid(eq(tileGridPane), eq(1), eq(1));
    }

    @Test
    public void shouldSetAnEmptyTileAsGround() throws Exception {
        when(editorTileFillToolIcon.getGroundFill()).thenReturn(GroundFill.EMPTY);
        when(tileGroundImageContainer.getImageForGround(GroundFill.EMPTY)).thenReturn(ImageIO.read(getClass().getResourceAsStream("/game/tiles/empty.png")));
        fillTileService.fillGroundWithTool(1, 1, tile, editorTileFillToolIcon, tileGridPane);
        verify(tile).setGroundFill(GroundFill.EMPTY);
    }

    @Test
    public void shouldDispatchConveyorBeltNeighborsWhenNewNonConveyorBeltSet() throws Exception {
        when(editorTileFillToolIcon.getGroundFill()).thenReturn(GroundFill.EMPTY);
        when(tileGroundImageContainer.getImageForGround(GroundFill.EMPTY)).thenReturn(ImageIO.read(getClass().getResourceAsStream("/game/tiles/empty.png")));
        fillTileService.fillGroundWithTool(1, 1, tile, editorTileFillToolIcon, tileGridPane);
        verify(conveyorBeltTileDispatcherService).dispatchNeighborsInGrid(eq(tileGridPane), eq(1), eq(1));
    }

    @Test
    public void shouldMarkTileAsStartPositionWhenGroundFillIsStart() throws Exception {
        when(editorTileFillToolIcon.getGroundFill()).thenReturn(GroundFill.START);
        when(tileGroundImageContainer.getImageForGround(GroundFill.START)).thenReturn(ImageIO.read(getClass().getResourceAsStream("/game/tiles/empty.png")));
        fillTileService.fillGroundWithTool(1, 1, tile, editorTileFillToolIcon, tileGridPane);
        verify(tile).setStartPosition(true);
    }

    @Test
    public void shouldNotMarkTileAsStartPositionWhenGroundFillIsNotStart() throws Exception {
        when(editorTileFillToolIcon.getGroundFill()).thenReturn(GroundFill.EMPTY);
        when(tileGroundImageContainer.getImageForGround(GroundFill.EMPTY)).thenReturn(ImageIO.read(getClass().getResourceAsStream("/game/tiles/empty.png")));
        fillTileService.fillGroundWithTool(1, 1, tile, editorTileFillToolIcon, tileGridPane);
        verify(tile, never()).setStartPosition(anyBoolean());
    }

    @Test
    public void shouldNotSetAnyFieldDataWhenOrientationIsNull() throws Exception {
        when(evaluateClickOrientationService.evaluate(anyDouble(), anyDouble(), anyInt(), anyDouble())).thenReturn(null);
        fillTileService.addFieldObject(10.0, 10.0, tile, 50, 20.0, editorTileFillToolIcon);
        verify(tile, never()).getFieldData(any(Orientation.class));
    }

    @Test
    public void shouldAddAWall() throws Exception {
        when(evaluateClickOrientationService.evaluate(anyDouble(), anyDouble(), anyInt(), anyDouble())).thenReturn(Orientation.NORTH);
        when(editorTileFillToolIcon.getFieldObject()).thenReturn(FieldObject.WALL);
        when(combineTileService.combineGroundWithWallObject(any(Image.class), eq(FieldObject.WALL), eq(Orientation.NORTH), anyInt())).thenReturn(ImageIO.read(getClass().getResourceAsStream("/game/tiles/empty.png")));
        fillTileService.addFieldObject(10.0, 10.0, tile, 50, 20.0, editorTileFillToolIcon);
        verify(tile).addFieldData(any(FieldData.class), eq(Orientation.NORTH));
    }

    @Test
    public void shouldAddAnWallFirstBeforeOtherFieldObjectAdded() throws Exception {
        when(evaluateClickOrientationService.evaluate(anyDouble(), anyDouble(), anyInt(), anyDouble())).thenReturn(Orientation.NORTH);
        when(editorTileFillToolIcon.getFieldObject()).thenReturn(FieldObject.LASER_SINGLE);
        when(combineTileService.combineGroundWithWallObject(any(Image.class), eq(FieldObject.WALL), eq(Orientation.NORTH), anyInt())).thenReturn(ImageIO.read(getClass().getResourceAsStream("/game/tiles/empty.png")));
        when(combineTileService.combineGroundWithWallObject(any(Image.class), eq(FieldObject.LASER_SINGLE), eq(Orientation.NORTH), anyInt())).thenReturn(ImageIO.read(getClass().getResourceAsStream("/game/tiles/empty.png")));

        ArgumentCaptor<FieldData> fieldDataArgumentCaptor = ArgumentCaptor.forClass(FieldData.class);

        fillTileService.addFieldObject(10.0, 10.0, tile, 50, 20.0, editorTileFillToolIcon);
        verify(tile).addFieldData(fieldDataArgumentCaptor.capture(), eq(Orientation.NORTH));
        assertEquals(FieldObject.WALL, fieldDataArgumentCaptor.getValue().getFieldObject());
    }

    @Test
    public void shouldAddObjectToConnectedWall() throws Exception {
        when(evaluateClickOrientationService.evaluate(anyDouble(), anyDouble(), anyInt(), anyDouble())).thenReturn(Orientation.NORTH);
        when(editorTileFillToolIcon.getFieldObject()).thenReturn(FieldObject.LASER_SINGLE);
        when(combineTileService.combineGroundWithWallObject(any(Image.class), eq(FieldObject.WALL), eq(Orientation.NORTH), anyInt())).thenReturn(ImageIO.read(getClass().getResourceAsStream("/game/tiles/empty.png")));
        when(combineTileService.combineGroundWithWallObject(any(Image.class), eq(FieldObject.LASER_SINGLE), eq(Orientation.NORTH), anyInt())).thenReturn(ImageIO.read(getClass().getResourceAsStream("/game/tiles/empty.png")));

        ArgumentCaptor<FieldData> fieldDataArgumentCaptor = ArgumentCaptor.forClass(FieldData.class);

        fillTileService.addFieldObject(10.0, 10.0, tile, 50, 20.0, editorTileFillToolIcon);
        verify(tile).addFieldData(fieldDataArgumentCaptor.capture(), eq(Orientation.NORTH));
        assertEquals(FieldObject.LASER_SINGLE, fieldDataArgumentCaptor.getValue().getConnectedTo().getFieldObject());
    }

    @Test
    public void shouldNotCreateANewWallWhenTileAlreadyHasWall() throws Exception {
        FieldData fieldData = mock(FieldData.class);
        when(evaluateClickOrientationService.evaluate(anyDouble(), anyDouble(), anyInt(), anyDouble())).thenReturn(Orientation.NORTH);
        when(editorTileFillToolIcon.getFieldObject()).thenReturn(FieldObject.LASER_SINGLE);
        when(combineTileService.combineGroundWithWallObject(any(Image.class), eq(FieldObject.WALL), eq(Orientation.NORTH), anyInt())).thenReturn(ImageIO.read(getClass().getResourceAsStream("/game/tiles/empty.png")));
        when(combineTileService.combineGroundWithWallObject(any(Image.class), eq(FieldObject.LASER_SINGLE), eq(Orientation.NORTH), anyInt())).thenReturn(ImageIO.read(getClass().getResourceAsStream("/game/tiles/empty.png")));
        when(tile.getFieldData(Orientation.NORTH)).thenReturn(fieldData);

        ArgumentCaptor<FieldData> fieldDataArgumentCaptor = ArgumentCaptor.forClass(FieldData.class);

        fillTileService.addFieldObject(10.0, 10.0, tile, 50, 20.0, editorTileFillToolIcon);
        verify(tile).addFieldData(fieldDataArgumentCaptor.capture(), eq(Orientation.NORTH));
        assertEquals(fieldData, fieldDataArgumentCaptor.getValue());
    }

    @Test
    public void shouldFindTehNextEmptyPlaceForFieldObeject() throws Exception {
        FieldData fieldData = mock(FieldData.class);
        when(evaluateClickOrientationService.evaluate(anyDouble(), anyDouble(), anyInt(), anyDouble())).thenReturn(Orientation.NORTH);
        when(editorTileFillToolIcon.getFieldObject()).thenReturn(FieldObject.LASER_SINGLE);
        when(combineTileService.combineGroundWithWallObject(any(Image.class), eq(FieldObject.WALL), eq(Orientation.NORTH), anyInt())).thenReturn(ImageIO.read(getClass().getResourceAsStream("/game/tiles/empty.png")));
        when(combineTileService.combineGroundWithWallObject(any(Image.class), eq(FieldObject.LASER_SINGLE), eq(Orientation.NORTH), anyInt())).thenReturn(ImageIO.read(getClass().getResourceAsStream("/game/tiles/empty.png")));
        when(tile.getFieldData(Orientation.NORTH)).thenReturn(fieldData);
        when(fieldData.getConnectedTo()).thenReturn(fieldData, fieldData, null);

        fillTileService.addFieldObject(10.0, 10.0, tile, 50, 20.0, editorTileFillToolIcon);
        verify(fieldData, times(3)).getConnectedTo();
    }

}