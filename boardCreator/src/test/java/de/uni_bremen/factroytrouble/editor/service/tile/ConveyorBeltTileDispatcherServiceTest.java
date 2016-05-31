package de.uni_bremen.factroytrouble.editor.service.tile;

import de.uni_bremen.factroytrouble.editor.component.EditorTileFillToolIcon;
import de.uni_bremen.factroytrouble.editor.component.Tile;
import de.uni_bremen.factroytrouble.editor.container.image.ConveyorBeltImageContainer;
import de.uni_bremen.factroytrouble.editor.data.ConveyorBeltTileImage;
import de.uni_bremen.factroytrouble.editor.data.GroundFill;
import de.uni_bremen.factroytrouble.editor.data.Orientation;
import de.uni_bremen.factroytrouble.editor.service.view.GridPaneUtilService;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ConveyorBeltTileDispatcherServiceTest {

    @Mock private ConveyorBeltImageContainer conveyorBeltImageContainer;
    @Mock private GridPaneUtilService gridPaneUtilService;
    @InjectMocks private ConveyorBeltTileDispatcherService conveyorBeltTileDispatcherService = new ConveyorBeltTileDispatcherService();

    @Mock private EditorTileFillToolIcon editorTileFillToolIcon;

    private GridPane tileGrid;
    @Mock private Tile topLeftTile;
    @Mock private Tile topCenterTile;
    @Mock private Tile topRightTile;
    @Mock private Tile centerLeftTile;
    @Mock private Tile centerTile;
    @Mock private Tile centerRightTile;
    @Mock private Tile bottomLeftTile;
    @Mock private Tile bottomCenterTile;
    @Mock private Tile bottomRightTile;

    @Mock private Image conveyorBeltImage;

    private BufferedImage testTileImage;

    @Before
    public void setUp() throws Exception {
        initMiniGrid();
        testTileImage = ImageIO.read(getClass().getResourceAsStream("/game/tiles/empty.png"));
        when(conveyorBeltImageContainer.getConyorbeltImage(any(ConveyorBeltTileImage.class))).thenReturn(testTileImage);
    }

    @Test
    public void shouldGetAConveyorBeltForTheEditorTool() {
        when(editorTileFillToolIcon.getOrientation()).thenReturn(Orientation.NORTH);
        when(editorTileFillToolIcon.getGroundFill()).thenReturn(GroundFill.CONVEYOR_BELT);
        ArgumentCaptor<ConveyorBeltTileImage> argumentCaptor = ArgumentCaptor.forClass(ConveyorBeltTileImage.class);

        conveyorBeltTileDispatcherService.getConveyorBeltByEditorTool(editorTileFillToolIcon);
        verify(conveyorBeltImageContainer).getConyorbeltImage(argumentCaptor.capture());
        assertEquals(new ConveyorBeltTileImage(Orientation.NORTH, false, Orientation.SOUTH), argumentCaptor.getValue());
    }

    @Test
    public void shouldGetAnExpressConveyorBeltForTheEditorTool() {
        when(editorTileFillToolIcon.getOrientation()).thenReturn(Orientation.NORTH);
        when(editorTileFillToolIcon.getGroundFill()).thenReturn(GroundFill.EXPRESS_CONVEYOR_BELT);
        ArgumentCaptor<ConveyorBeltTileImage> argumentCaptor = ArgumentCaptor.forClass(ConveyorBeltTileImage.class);

        conveyorBeltTileDispatcherService.getConveyorBeltByEditorTool(editorTileFillToolIcon);
        verify(conveyorBeltImageContainer).getConyorbeltImage(argumentCaptor.capture());
        assertEquals(new ConveyorBeltTileImage(Orientation.NORTH, true, Orientation.SOUTH), argumentCaptor.getValue());
    }

    @Test
    public void shouldNotDispatchTheTileWhenIsNotAConveyorBeltOrAnExpressConveyorBelt() {
        when(centerTile.getGroundFill()).thenReturn(GroundFill.EMPTY);
        conveyorBeltTileDispatcherService.dispatchNeighborsInGrid(tileGrid, 1, 1);
        verify(conveyorBeltImageContainer, never()).getConyorbeltImage(any(ConveyorBeltTileImage.class));
    }

    @Test
    public void shouldAddAStraightConveyorBeltWithNoNeighbors() {
        when(centerTile.getGroundFill()).thenReturn(GroundFill.CONVEYOR_BELT);
        when(centerTile.getOrientation()).thenReturn(Orientation.NORTH);
        conveyorBeltTileDispatcherService.dispatchNeighborsInGrid(tileGrid, 1, 1);
        verify(conveyorBeltImageContainer).getConyorbeltImage(new ConveyorBeltTileImage(Orientation.NORTH, false, Orientation.SOUTH));
    }

    @Test
    public void shouldAddAStraightExpressConveyorBeltWithNoNeighbors() {
        when(centerTile.getGroundFill()).thenReturn(GroundFill.EXPRESS_CONVEYOR_BELT);
        when(centerTile.getOrientation()).thenReturn(Orientation.NORTH);
        conveyorBeltTileDispatcherService.dispatchNeighborsInGrid(tileGrid, 1, 1);
        verify(conveyorBeltImageContainer).getConyorbeltImage(new ConveyorBeltTileImage(Orientation.NORTH, true, Orientation.SOUTH));
    }

    @Test
    public void shouldADispatchTheConveyorBeltInTheNorthAsCurve() {
        when(centerTile.getGroundFill()).thenReturn(GroundFill.CONVEYOR_BELT);
        when(centerTile.getOrientation()).thenReturn(Orientation.NORTH);

        when(topCenterTile.getGroundFill()).thenReturn(GroundFill.CONVEYOR_BELT);
        when(topCenterTile.getOrientation()).thenReturn(Orientation.EAST);

        conveyorBeltTileDispatcherService.dispatchNeighborsInGrid(tileGrid, 1, 1);
        verify(conveyorBeltImageContainer).getConyorbeltImage(new ConveyorBeltTileImage(Orientation.NORTH, false, Orientation.SOUTH));
        verify(conveyorBeltImageContainer).getConyorbeltImage(new ConveyorBeltTileImage(Orientation.EAST, false, Orientation.SOUTH));
    }

    @Test
    public void shouldADispatchTheExpressConveyorBeltInTheNorthAsCurve() {
        when(centerTile.getGroundFill()).thenReturn(GroundFill.EXPRESS_CONVEYOR_BELT);
        when(centerTile.getOrientation()).thenReturn(Orientation.NORTH);

        when(topCenterTile.getGroundFill()).thenReturn(GroundFill.EXPRESS_CONVEYOR_BELT);
        when(topCenterTile.getOrientation()).thenReturn(Orientation.EAST);

        conveyorBeltTileDispatcherService.dispatchNeighborsInGrid(tileGrid, 1, 1);
        verify(conveyorBeltImageContainer).getConyorbeltImage(new ConveyorBeltTileImage(Orientation.NORTH, true, Orientation.SOUTH));
        verify(conveyorBeltImageContainer).getConyorbeltImage(new ConveyorBeltTileImage(Orientation.EAST, true, Orientation.SOUTH));
    }

    @Test
    public void shouldNotDispatchAnyNeighborWhenTileIsNull() {
        when(topCenterTile.getGroundFill()).thenReturn(GroundFill.CONVEYOR_BELT);
        when(topCenterTile.getOrientation()).thenReturn(Orientation.NORTH);

        conveyorBeltTileDispatcherService.dispatchNeighborsInGrid(tileGrid, 1, 0);
        verify(gridPaneUtilService, times(5)).getNodeFromGridPane(eq(tileGrid), anyInt(), anyInt());
        verify(gridPaneUtilService).getNodeFromGridPane(tileGrid, 1, -1);
    }

    private void initMiniGrid() {
        tileGrid = new GridPane();

        when(gridPaneUtilService.getNodeFromGridPane(tileGrid, 0, 0)).thenReturn(topLeftTile);
        when(gridPaneUtilService.getNodeFromGridPane(tileGrid, 1, 0)).thenReturn(topCenterTile);
        when(gridPaneUtilService.getNodeFromGridPane(tileGrid, 2, 0)).thenReturn(topRightTile);

        when(gridPaneUtilService.getNodeFromGridPane(tileGrid, 0, 1)).thenReturn(centerLeftTile);
        when(gridPaneUtilService.getNodeFromGridPane(tileGrid, 1, 1)).thenReturn(centerTile);
        when(gridPaneUtilService.getNodeFromGridPane(tileGrid, 2, 1)).thenReturn(centerRightTile);

        when(gridPaneUtilService.getNodeFromGridPane(tileGrid, 0, 2)).thenReturn(bottomLeftTile);
        when(gridPaneUtilService.getNodeFromGridPane(tileGrid, 1, 2)).thenReturn(bottomCenterTile);
        when(gridPaneUtilService.getNodeFromGridPane(tileGrid, 2, 2)).thenReturn(bottomRightTile);
    }

}