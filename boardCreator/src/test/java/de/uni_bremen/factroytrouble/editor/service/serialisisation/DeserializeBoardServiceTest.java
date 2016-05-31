package de.uni_bremen.factroytrouble.editor.service.serialisisation;

import de.saxsys.javafx.test.JfxRunner;
import de.uni_bremen.factroytrouble.editor.component.EditorTileFillToolIcon;
import de.uni_bremen.factroytrouble.editor.component.Tile;
import de.uni_bremen.factroytrouble.editor.data.FieldObject;
import de.uni_bremen.factroytrouble.editor.data.GroundFill;
import de.uni_bremen.factroytrouble.editor.data.Orientation;
import de.uni_bremen.factroytrouble.editor.service.tile.FillTileService;
import de.uni_bremen.factroytrouble.editor.service.view.GridPaneUtilService;
import javafx.scene.layout.GridPane;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(JfxRunner.class)
public class DeserializeBoardServiceTest {

    @Mock private GridPaneUtilService gridPaneUtilService;
    @Mock private FillTileService fillTileService;
    @InjectMocks  private DeserializeBoardService deserializeBoardService;

    private List<String> boardStrings;
    private GridPane gridPane;
    Tile[] tiles;

    @Before
    public void setUp() {
        deserializeBoardService = new DeserializeBoardService();
        MockitoAnnotations.initMocks(this);
        boardStrings = new ArrayList<>();
        tiles = new Tile[]{mock(Tile.class), mock(Tile.class), mock(Tile.class), mock(Tile.class)};
        gridPane = new GridPane();

        when(gridPaneUtilService.getNodeFromGridPane(gridPane, 0, 0)).thenReturn(tiles[0]);
        when(gridPaneUtilService.getNodeFromGridPane(gridPane, 1, 0)).thenReturn(tiles[1]);
        when(gridPaneUtilService.getNodeFromGridPane(gridPane, 0, 1)).thenReturn(tiles[2]);
        when(gridPaneUtilService.getNodeFromGridPane(gridPane, 1, 1)).thenReturn(tiles[3]);
    }

    @Test
    public void shouldCreateAFieldWithEmptyTilesWithOrientationNorth() {
        boardStrings.add("ti,ti");
        boardStrings.add("ti,ti");
        deserializeBoardService.deserializeBoard(gridPane, boardStrings, 2, 2, Orientation.NORTH);
        List<EditorTileFillToolIcon> editorTileFillToolIcons = argumentCaptorForEditorTileFillToolIconAndGroundFill();
        argumentCaptorForEditorTileFillToolIconAndGroundFill().forEach(icon -> {
            assertEquals(GroundFill.EMPTY, icon.getGroundFill());
        });
    }

    @Test
    public void shouldRotateTheBoardToNorth() {
        boardStrings.add("ti,ho");
        boardStrings.add("ws,ge");
        deserializeBoardService.deserializeBoard(gridPane, boardStrings, 2, 2, Orientation.NORTH);
        Map<Point, ArgumentCaptor<EditorTileFillToolIcon>> argumentCaptorsSepeatedByColumnAndRow = getArgumentCaptorsSepeatedByColumnAndRow();
        assertEquals(GroundFill.REPAIR, argumentCaptorsSepeatedByColumnAndRow.get(new Point(0, 0)).getValue().getGroundFill());
        assertEquals(GroundFill.GEAR_CW, argumentCaptorsSepeatedByColumnAndRow.get(new Point(1, 0)).getValue().getGroundFill());
        assertEquals(GroundFill.EMPTY, argumentCaptorsSepeatedByColumnAndRow.get(new Point(0, 1)).getValue().getGroundFill());
        assertEquals(GroundFill.HOLE, argumentCaptorsSepeatedByColumnAndRow.get(new Point(1, 1)).getValue().getGroundFill());
    }

    @Test
    public void shouldRotateTheBoardToSouth() {
        boardStrings.add("ti,ho");
        boardStrings.add("ws,ge");
        deserializeBoardService.deserializeBoard(gridPane, boardStrings, 2, 2, Orientation.SOUTH);
        Map<Point, ArgumentCaptor<EditorTileFillToolIcon>> argumentCaptorsSepeatedByColumnAndRow = getArgumentCaptorsSepeatedByColumnAndRow();
        assertEquals(GroundFill.HOLE, argumentCaptorsSepeatedByColumnAndRow.get(new Point(0, 0)).getValue().getGroundFill());
        assertEquals(GroundFill.EMPTY, argumentCaptorsSepeatedByColumnAndRow.get(new Point(1, 0)).getValue().getGroundFill());
        assertEquals(GroundFill.GEAR_CW, argumentCaptorsSepeatedByColumnAndRow.get(new Point(0, 1)).getValue().getGroundFill());
        assertEquals(GroundFill.REPAIR, argumentCaptorsSepeatedByColumnAndRow.get(new Point(1, 1)).getValue().getGroundFill());
    }

    @Test
    public void shouldRotateTheBoardToEast() {
        boardStrings.add("ti,ho");
        boardStrings.add("ws,ge");
        deserializeBoardService.deserializeBoard(gridPane, boardStrings, 2, 2, Orientation.EAST);
        Map<Point, ArgumentCaptor<EditorTileFillToolIcon>> argumentCaptorsSepeatedByColumnAndRow = getArgumentCaptorsSepeatedByColumnAndRow();
        assertEquals(GroundFill.EMPTY, argumentCaptorsSepeatedByColumnAndRow.get(new Point(0, 0)).getValue().getGroundFill());
        assertEquals(GroundFill.REPAIR, argumentCaptorsSepeatedByColumnAndRow.get(new Point(1, 0)).getValue().getGroundFill());
        assertEquals(GroundFill.HOLE, argumentCaptorsSepeatedByColumnAndRow.get(new Point(0, 1)).getValue().getGroundFill());
        assertEquals(GroundFill.GEAR_CW, argumentCaptorsSepeatedByColumnAndRow.get(new Point(1, 1)).getValue().getGroundFill());
    }

    @Test
    public void shouldRotateTheBoardToWest() {
        boardStrings.add("ti,ho");
        boardStrings.add("ws,ge");
        deserializeBoardService.deserializeBoard(gridPane, boardStrings, 2, 2, Orientation.WEST);
        Map<Point, ArgumentCaptor<EditorTileFillToolIcon>> argumentCaptorsSepeatedByColumnAndRow = getArgumentCaptorsSepeatedByColumnAndRow();
        assertEquals(GroundFill.GEAR_CW, argumentCaptorsSepeatedByColumnAndRow.get(new Point(0, 0)).getValue().getGroundFill());
        assertEquals(GroundFill.HOLE, argumentCaptorsSepeatedByColumnAndRow.get(new Point(1, 0)).getValue().getGroundFill());
        assertEquals(GroundFill.REPAIR, argumentCaptorsSepeatedByColumnAndRow.get(new Point(0, 1)).getValue().getGroundFill());
        assertEquals(GroundFill.EMPTY, argumentCaptorsSepeatedByColumnAndRow.get(new Point(1, 1)).getValue().getGroundFill());
    }

    @Test
    public void shouldDispatchAEmptyTile() {
        boardStrings.add("ti");
        deserializeBoardService.deserializeBoard(gridPane, boardStrings, 1, 1, Orientation.NORTH);
        assertEquals(GroundFill.EMPTY, argumentCaptorForASingleCallForGround().getGroundFill());
    }

    @Test
    public void shouldDispatchAHole() {
        boardStrings.add("ho");
        deserializeBoardService.deserializeBoard(gridPane, boardStrings, 1, 1, Orientation.NORTH);
        assertEquals(GroundFill.HOLE, argumentCaptorForASingleCallForGround().getGroundFill());
    }

    @Test
    public void shouldDispatchARepair() {
        boardStrings.add("ws");
        deserializeBoardService.deserializeBoard(gridPane, boardStrings, 1, 1, Orientation.NORTH);
        assertEquals(GroundFill.REPAIR, argumentCaptorForASingleCallForGround().getGroundFill());
    }

    @Test
    public void shouldDispatchAGearClockwise() {
        boardStrings.add("ge");
        deserializeBoardService.deserializeBoard(gridPane, boardStrings, 1, 1, Orientation.NORTH);
        assertEquals(GroundFill.GEAR_CW, argumentCaptorForASingleCallForGround().getGroundFill());
    }

    @Test
    public void shouldDispatchAGearCounterClockwise() {
        boardStrings.add("ge_rl");
        deserializeBoardService.deserializeBoard(gridPane, boardStrings, 1, 1, Orientation.NORTH);
        assertEquals(GroundFill.GEAR_CCW, argumentCaptorForASingleCallForGround().getGroundFill());
    }

    @Test
    public void shouldDispatchAConveyorBelt() {
        boardStrings.add("be_no");
        deserializeBoardService.deserializeBoard(gridPane, boardStrings, 1, 1, Orientation.NORTH);
        assertEquals(GroundFill.CONVEYOR_BELT, argumentCaptorForASingleCallForGround().getGroundFill());
    }

    @Test
    public void shouldDispatchTheOrienationForAConveyorBeltWithOrientationNorthAndFieldOrientationNorth() {
        boardStrings.add("be_no");
        deserializeBoardService.deserializeBoard(gridPane, boardStrings, 1, 1, Orientation.NORTH);
        assertEquals(Orientation.NORTH, argumentCaptorForASingleCallForGround().getOrientation());
    }

    @Test
    public void shouldDispatchTheOrienationForAConveyorBeltWithOrientationNorthAndFieldOrientationEast() {
        boardStrings.add("be_no");
        deserializeBoardService.deserializeBoard(gridPane, boardStrings, 1, 1, Orientation.EAST);
        assertEquals(Orientation.EAST, argumentCaptorForASingleCallForGround().getOrientation());
    }

    @Test
    public void shouldDispatchTheOrienationForAConveyorBeltWithOrientationNorthAndFieldOrientationSouth() {
        boardStrings.add("be_no");
        deserializeBoardService.deserializeBoard(gridPane, boardStrings, 1, 1, Orientation.SOUTH);
        assertEquals(Orientation.SOUTH, argumentCaptorForASingleCallForGround().getOrientation());
    }

    @Test
    public void shouldDispatchTheOrienationForAConveyorBeltWithOrientationNorthAndFieldOrientationWest() {
        boardStrings.add("be_no");
        deserializeBoardService.deserializeBoard(gridPane, boardStrings, 1, 1, Orientation.WEST);
        assertEquals(Orientation.WEST, argumentCaptorForASingleCallForGround().getOrientation());
    }

    @Test
    public void shouldDispatchAnExpressConveyorBelt() {
        boardStrings.add("be_no_ex");
        deserializeBoardService.deserializeBoard(gridPane, boardStrings, 1, 1, Orientation.NORTH);
        assertEquals(GroundFill.EXPRESS_CONVEYOR_BELT, argumentCaptorForASingleCallForGround().getGroundFill());
    }

    @Test
    public void shouldDispatchTheOrienationForAnExpressConveyorBeltWithOrientationNorthAndFieldOrientationNorth() {
        boardStrings.add("be_no_ex");
        deserializeBoardService.deserializeBoard(gridPane, boardStrings, 1, 1, Orientation.NORTH);
        assertEquals(Orientation.NORTH, argumentCaptorForASingleCallForGround().getOrientation());
    }

    @Test
    public void shouldDispatchTheOrienationForAnExpressConveyorBeltWithOrientationNorthAndFieldOrientationEast() {
        boardStrings.add("be_no_ex");
        deserializeBoardService.deserializeBoard(gridPane, boardStrings, 1, 1, Orientation.EAST);
        assertEquals(Orientation.EAST, argumentCaptorForASingleCallForGround().getOrientation());
    }

    @Test
    public void shouldDispatchTheOrienationForAnExpressConveyorBeltWithOrientationNorthAndFieldOrientationSouth() {
        boardStrings.add("be_no_ex");
        deserializeBoardService.deserializeBoard(gridPane, boardStrings, 1, 1, Orientation.SOUTH);
        assertEquals(Orientation.SOUTH, argumentCaptorForASingleCallForGround().getOrientation());
    }

    @Test
    public void shouldDispatchTheOrienationForAnExpressConveyorBeltWithOrientationNorthAndFieldOrientationWest() {
        boardStrings.add("be_no_ex");
        deserializeBoardService.deserializeBoard(gridPane, boardStrings, 1, 1, Orientation.WEST);
        assertEquals(Orientation.WEST, argumentCaptorForASingleCallForGround().getOrientation());
    }

    @Test
    public void shouldRotatAConveyorBeltToWest() {
        boardStrings.add("be_we");
        deserializeBoardService.deserializeBoard(gridPane, boardStrings, 1, 1, Orientation.NORTH);
        assertEquals(Orientation.WEST, argumentCaptorForASingleCallForGround().getOrientation());
    }

    @Test
    public void shouldRotatAConveyorBeltToSouth() {
        boardStrings.add("be_so");
        deserializeBoardService.deserializeBoard(gridPane, boardStrings, 1, 1, Orientation.NORTH);
        assertEquals(Orientation.SOUTH, argumentCaptorForASingleCallForGround().getOrientation());
    }

    @Test
    public void shouldRotatAConveyorBeltToEast() {
        boardStrings.add("be_ea");
        deserializeBoardService.deserializeBoard(gridPane, boardStrings, 1, 1, Orientation.NORTH);
        assertEquals(Orientation.EAST, argumentCaptorForASingleCallForGround().getOrientation());
    }

    @Test
    public void shouldAddAWallInTheNorth() {
        boardStrings.add("ti_wno");
        deserializeBoardService.deserializeBoard(gridPane, boardStrings, 1, 1, Orientation.NORTH);
        assertEquals(FieldObject.WALL, argumentCaptorForASingleCallForWallAndObjects(25.0, 10.0).getFieldObject());
    }

    @Test
    public void shouldAddAWallInTheEast() {
        boardStrings.add("ti_wea");
        deserializeBoardService.deserializeBoard(gridPane, boardStrings, 1, 1, Orientation.NORTH);
        assertEquals(FieldObject.WALL, argumentCaptorForASingleCallForWallAndObjects(40.0, 25.0).getFieldObject());
    }

    @Test
    public void shouldAddAWallInTheSouth() {
        boardStrings.add("ti_wso");
        deserializeBoardService.deserializeBoard(gridPane, boardStrings, 1, 1, Orientation.NORTH);
        assertEquals(FieldObject.WALL, argumentCaptorForASingleCallForWallAndObjects(25.0, 40.0).getFieldObject());
    }

    @Test
    public void shouldAddAWallInTheWest() {
        boardStrings.add("ti_wwe");
        deserializeBoardService.deserializeBoard(gridPane, boardStrings, 1, 1, Orientation.NORTH);
        assertEquals(FieldObject.WALL, argumentCaptorForASingleCallForWallAndObjects(10.0, 25.0).getFieldObject());
    }

    @Test
    public void shouldAddAWallInTheNorthFieldRotateEast() {
        boardStrings.add("ti_wno");
        deserializeBoardService.deserializeBoard(gridPane, boardStrings, 1, 1, Orientation.EAST);
        assertEquals(FieldObject.WALL, argumentCaptorForASingleCallForWallAndObjects(40.0, 25.0).getFieldObject());
    }

    @Test
    public void shouldAddAWallInTheEastFieldRotateEast() {
        boardStrings.add("ti_wea");
        deserializeBoardService.deserializeBoard(gridPane, boardStrings, 1, 1, Orientation.EAST);
        assertEquals(FieldObject.WALL, argumentCaptorForASingleCallForWallAndObjects(25.0, 40.0).getFieldObject());
    }

    @Test
    public void shouldAddAWallInTheSouthFieldRotateEast() {
        boardStrings.add("ti_wso");
        deserializeBoardService.deserializeBoard(gridPane, boardStrings, 1, 1, Orientation.EAST);
        assertEquals(FieldObject.WALL, argumentCaptorForASingleCallForWallAndObjects(10.0, 25.0).getFieldObject());
    }

    @Test
    public void shouldAddAWallInTheWestFieldRotateEast() {
        boardStrings.add("ti_wwe");
        deserializeBoardService.deserializeBoard(gridPane, boardStrings, 1, 1, Orientation.EAST);
        assertEquals(FieldObject.WALL, argumentCaptorForASingleCallForWallAndObjects(25.0, 10.0).getFieldObject());
    }

    @Test
    public void shouldAddAWallInTheNorthFieldRotateSouth() {
        boardStrings.add("ti_wno");
        deserializeBoardService.deserializeBoard(gridPane, boardStrings, 1, 1, Orientation.SOUTH);
        assertEquals(FieldObject.WALL, argumentCaptorForASingleCallForWallAndObjects(25.0, 40.0).getFieldObject());
    }

    @Test
    public void shouldAddAWallInTheEastFieldRotateSouth() {
        boardStrings.add("ti_wea");
        deserializeBoardService.deserializeBoard(gridPane, boardStrings, 1, 1, Orientation.SOUTH);
        assertEquals(FieldObject.WALL, argumentCaptorForASingleCallForWallAndObjects(10.0, 25.0).getFieldObject());
    }

    @Test
    public void shouldAddAWallInTheSouthFieldRotateSouth() {
        boardStrings.add("ti_wso");
        deserializeBoardService.deserializeBoard(gridPane, boardStrings, 1, 1, Orientation.SOUTH);
        assertEquals(FieldObject.WALL, argumentCaptorForASingleCallForWallAndObjects(25.0, 10.0).getFieldObject());
    }

    @Test
    public void shouldAddAWallInTheWestFieldRotateSouth() {
        boardStrings.add("ti_wwe");
        deserializeBoardService.deserializeBoard(gridPane, boardStrings, 1, 1, Orientation.SOUTH);
        assertEquals(FieldObject.WALL, argumentCaptorForASingleCallForWallAndObjects(40.0, 25.0).getFieldObject());
    }

    @Test
    public void shouldAddAWallInTheNorthFieldRotateWest() {
        boardStrings.add("ti_wno");
        deserializeBoardService.deserializeBoard(gridPane, boardStrings, 1, 1, Orientation.WEST);
        assertEquals(FieldObject.WALL, argumentCaptorForASingleCallForWallAndObjects(10.0, 25.0).getFieldObject());
    }

    @Test
    public void shouldAddAWallInTheEastFieldRotateWest() {
        boardStrings.add("ti_wea");
        deserializeBoardService.deserializeBoard(gridPane, boardStrings, 1, 1, Orientation.WEST);
        assertEquals(FieldObject.WALL, argumentCaptorForASingleCallForWallAndObjects(25.0, 10.0).getFieldObject());
    }

    @Test
    public void shouldAddAWallInTheSouthFieldRotateWest() {
        boardStrings.add("ti_wso");
        deserializeBoardService.deserializeBoard(gridPane, boardStrings, 1, 1, Orientation.WEST);
        assertEquals(FieldObject.WALL, argumentCaptorForASingleCallForWallAndObjects(40.0, 25.0).getFieldObject());
    }

    @Test
    public void shouldAddAWallInTheWestFieldRotateWest() {
        boardStrings.add("ti_wwe");
        deserializeBoardService.deserializeBoard(gridPane, boardStrings, 1, 1, Orientation.WEST);
        assertEquals(FieldObject.WALL, argumentCaptorForASingleCallForWallAndObjects(25.0, 40.0).getFieldObject());
    }

    @Test
    public void shouldAddAnEvenPusherToAWall() {
        boardStrings.add("ti_wnop24");
        deserializeBoardService.deserializeBoard(gridPane, boardStrings, 1, 1, Orientation.NORTH);
        List<EditorTileFillToolIcon> icons = argumentCaptorForASingleCallForWallAndObjects(25.0, 10.0, 2);
        assertEquals(FieldObject.PUSHER_EVEN, icons.get(1).getFieldObject());
    }

    @Test
    public void shouldAddAnOddPusherToAWall() {
        boardStrings.add("ti_wnop135");
        deserializeBoardService.deserializeBoard(gridPane, boardStrings, 1, 1, Orientation.NORTH);
        List<EditorTileFillToolIcon> icons = argumentCaptorForASingleCallForWallAndObjects(25.0, 10.0, 2);
        assertEquals(FieldObject.PUSHER_ODD, icons.get(1).getFieldObject());
    }

    @Test
    public void shouldAddASingleLaserToAWall() {
        boardStrings.add("ti_wnol");
        deserializeBoardService.deserializeBoard(gridPane, boardStrings, 1, 1, Orientation.NORTH);
        List<EditorTileFillToolIcon> icons = argumentCaptorForASingleCallForWallAndObjects(25.0, 10.0, 2);
        assertEquals(FieldObject.LASER_SINGLE, icons.get(1).getFieldObject());
    }

    @Test
    public void shouldAddADoubleLaserToAWall() {
        boardStrings.add("ti_wnoll");
        deserializeBoardService.deserializeBoard(gridPane, boardStrings, 1, 1, Orientation.NORTH);
        List<EditorTileFillToolIcon> icons = argumentCaptorForASingleCallForWallAndObjects(25.0, 10.0, 2);
        assertEquals(FieldObject.LASER_DOUBLE, icons.get(1).getFieldObject());
    }

    @Test
    public void shouldAddATripleLaserToAWall() {
        boardStrings.add("ti_wnolll");
        deserializeBoardService.deserializeBoard(gridPane, boardStrings, 1, 1, Orientation.NORTH);
        List<EditorTileFillToolIcon> icons = argumentCaptorForASingleCallForWallAndObjects(25.0, 10.0, 2);
        assertEquals(FieldObject.LASER_TRIPLE, icons.get(1).getFieldObject());
    }

    @Test
    public void shouldAddToDiffernetWalls() {
        boardStrings.add("ti_wno_wea");
        deserializeBoardService.deserializeBoard(gridPane, boardStrings, 1, 1, Orientation.NORTH);
        ArgumentCaptor<EditorTileFillToolIcon> northWallCaptor = ArgumentCaptor.forClass(EditorTileFillToolIcon.class);
        ArgumentCaptor<EditorTileFillToolIcon> eastWallCaptor = ArgumentCaptor.forClass(EditorTileFillToolIcon.class);
        verify(fillTileService).addFieldObject(eq(25.0), eq(10.0), any(Tile.class), anyInt(), anyDouble(), northWallCaptor.capture());
        verify(fillTileService).addFieldObject(eq(40.0), eq(25.0), any(Tile.class), anyInt(), anyDouble(), eastWallCaptor.capture());
        assertEquals(FieldObject.WALL, northWallCaptor.getValue().getFieldObject());
        assertEquals(FieldObject.WALL, eastWallCaptor.getValue().getFieldObject());
    }

    private List<EditorTileFillToolIcon> argumentCaptorForASingleCallForWallAndObjects(Double clickX, Double clickY, Integer objectCount) {
        ArgumentCaptor<EditorTileFillToolIcon> argumentCaptor = ArgumentCaptor.forClass(EditorTileFillToolIcon.class);
        verify(fillTileService, times(objectCount)).addFieldObject(eq(clickX), eq(clickY), any(Tile.class), anyInt(), anyDouble(), argumentCaptor.capture());
        return argumentCaptor.getAllValues();
    }

    private EditorTileFillToolIcon argumentCaptorForASingleCallForWallAndObjects(Double clickX, Double clickY) {
        ArgumentCaptor<EditorTileFillToolIcon> argumentCaptor = ArgumentCaptor.forClass(EditorTileFillToolIcon.class);
        verify(fillTileService).addFieldObject(eq(clickX), eq(clickY), any(Tile.class), anyInt(), anyDouble(), argumentCaptor.capture());
        return argumentCaptor.getValue();
    }

    private EditorTileFillToolIcon argumentCaptorForASingleCallForGround() {
        ArgumentCaptor<EditorTileFillToolIcon> argumentCaptor = ArgumentCaptor.forClass(EditorTileFillToolIcon.class);
        verify(fillTileService).fillGroundWithTool(anyInt(), anyInt(), any(Tile.class), argumentCaptor.capture(), any(GridPane.class));
        return argumentCaptor.getValue();
    }

    private List<EditorTileFillToolIcon> argumentCaptorForEditorTileFillToolIconAndGroundFill() {
        ArgumentCaptor<EditorTileFillToolIcon> argumentCaptor = ArgumentCaptor.forClass(EditorTileFillToolIcon.class);
        verify(fillTileService, times(4)).fillGroundWithTool(anyInt(), anyInt(), any(Tile.class), argumentCaptor.capture(), any(GridPane.class));
        return argumentCaptor.getAllValues();
    }

    private Map<Point, ArgumentCaptor<EditorTileFillToolIcon>> getArgumentCaptorsSepeatedByColumnAndRow() {
        Map<Point, ArgumentCaptor<EditorTileFillToolIcon>> captorMap = new HashMap<>();
        captorMap.put(new Point(0, 0), ArgumentCaptor.forClass(EditorTileFillToolIcon.class));
        captorMap.put(new Point(0, 1), ArgumentCaptor.forClass(EditorTileFillToolIcon.class));
        captorMap.put(new Point(1, 0), ArgumentCaptor.forClass(EditorTileFillToolIcon.class));
        captorMap.put(new Point(1, 1), ArgumentCaptor.forClass(EditorTileFillToolIcon.class));

        verify(fillTileService).fillGroundWithTool(eq(0), eq(0), any(Tile.class), captorMap.get(new Point(0, 0)).capture(), any(GridPane.class));
        verify(fillTileService).fillGroundWithTool(eq(1), eq(0), any(Tile.class), captorMap.get(new Point(1, 0)).capture(), any(GridPane.class));
        verify(fillTileService).fillGroundWithTool(eq(0), eq(1), any(Tile.class), captorMap.get(new Point(0, 1)).capture(), any(GridPane.class));
        verify(fillTileService).fillGroundWithTool(eq(1), eq(1), any(Tile.class), captorMap.get(new Point(1, 1)).capture(), any(GridPane.class));

        return captorMap;
    }

}