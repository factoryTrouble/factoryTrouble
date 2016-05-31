package de.uni_bremen.factroytrouble.editor.service.serialisisation;

import de.uni_bremen.factroytrouble.editor.component.Tile;
import de.uni_bremen.factroytrouble.editor.data.FieldData;
import de.uni_bremen.factroytrouble.editor.data.FieldObject;
import de.uni_bremen.factroytrouble.editor.data.GroundFill;
import de.uni_bremen.factroytrouble.editor.data.Orientation;
import de.uni_bremen.factroytrouble.editor.service.view.GridPaneUtilService;
import javafx.scene.layout.GridPane;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SerializeBoardServiceTest {

    @Mock private GridPaneUtilService gridPaneUtilService;
    @InjectMocks private SerializeBoardService serializeBoardService = new SerializeBoardService();

    @Mock private GridPane gridPane;
    @Mock private Tile tile;
    @Mock private FieldData fieldData;
    @Mock private FieldData fieldData2;

    @Before
    public void setUp() {
        when(gridPaneUtilService.getNodeFromGridPane(eq(gridPane), anyInt(), anyInt())).thenReturn(tile);
    }

    @Test
    public void shouldSerializeAEmptyTile() {
        when(tile.getGroundFill()).thenReturn(GroundFill.EMPTY);
        String serializedBoard = serializeBoardService.serializeBoardGrid(2, 2, gridPane);
        assertEquals("ti,ti" + System.getProperty("line.separator") + "ti,ti", serializedBoard);
    }

    @Test
    public void shouldSerializeAGearCCW() {
        when(tile.getGroundFill()).thenReturn(GroundFill.GEAR_CCW);
        String serializedBoard = serializeBoardService.serializeBoardGrid(2, 2, gridPane);
        assertEquals("ge_rl,ge_rl" + System.getProperty("line.separator") + "ge_rl,ge_rl", serializedBoard);
    }

    @Test
    public void shouldSerializeAGearCW() {
        when(tile.getGroundFill()).thenReturn(GroundFill.GEAR_CW);
        String serializedBoard = serializeBoardService.serializeBoardGrid(2, 2, gridPane);
        assertEquals("ge,ge" + System.getProperty("line.separator") + "ge,ge", serializedBoard);
    }

    @Test
    public void shouldSerializeARepair() {
        when(tile.getGroundFill()).thenReturn(GroundFill.REPAIR);
        String serializedBoard = serializeBoardService.serializeBoardGrid(2, 2, gridPane);
        assertEquals("ws,ws" + System.getProperty("line.separator") + "ws,ws", serializedBoard);
    }

    @Test
    public void shouldSerializeAHole() {
        when(tile.getGroundFill()).thenReturn(GroundFill.HOLE);
        String serializedBoard = serializeBoardService.serializeBoardGrid(2, 2, gridPane);
        assertEquals("ho,ho" + System.getProperty("line.separator") + "ho,ho", serializedBoard);
    }

    @Test
     public void shouldSerializeAConveyorBeltInAllOrientations() {
        when(tile.getGroundFill()).thenReturn(GroundFill.CONVEYOR_BELT);
        when(tile.getOrientation()).thenReturn(Orientation.NORTH, Orientation.WEST, Orientation.SOUTH, Orientation.EAST);
        String serializedBoard = serializeBoardService.serializeBoardGrid(2, 2, gridPane);
        assertEquals("be_no,be_we" + System.getProperty("line.separator") + "be_so,be_ea", serializedBoard);
    }

    @Test
    public void shouldSerializeAnExpressConveyorBeltInAllOrientations() {
        when(tile.getGroundFill()).thenReturn(GroundFill.EXPRESS_CONVEYOR_BELT);
        when(tile.getOrientation()).thenReturn(Orientation.NORTH, Orientation.WEST, Orientation.SOUTH, Orientation.EAST);
        String serializedBoard = serializeBoardService.serializeBoardGrid(2, 2, gridPane);
        assertEquals("be_no_ex,be_we_ex" + System.getProperty("line.separator") + "be_so_ex,be_ea_ex", serializedBoard);
    }

    @Test
    public void shouldSerializeAStartPosition() {
        when(tile.getGroundFill()).thenReturn(GroundFill.EMPTY);
        when(tile.isStartPosition()).thenReturn(true);
        String serializedBoard = serializeBoardService.serializeBoardGrid(2, 2, gridPane);
        assertEquals("ti_s1,ti_s2" + System.getProperty("line.separator") + "ti_s3,ti_s4", serializedBoard);
    }

    @Test
    public void shouldSerializeAEmptyTileWithAWall() {
        when(tile.getGroundFill()).thenReturn(GroundFill.EMPTY);
        when(tile.getFieldData(any(Orientation.class))).thenReturn(fieldData);
        when(fieldData.getFieldObject()).thenReturn(FieldObject.WALL);
        String serializedBoard = serializeBoardService.serializeBoardGrid(2, 2, gridPane);
        assertEquals("ti_wea_wno_wwe_wso,ti_wea_wno_wwe_wso" + System.getProperty("line.separator") + "ti_wea_wno_wwe_wso,ti_wea_wno_wwe_wso", serializedBoard);
    }

    @Test
    public void shouldSerializeAEmptyTileWithAWallAndASingleLaser() {
        when(tile.getGroundFill()).thenReturn(GroundFill.EMPTY);
        when(tile.getFieldData(any(Orientation.class))).thenReturn(fieldData, null);
        when(fieldData.getFieldObject()).thenReturn(FieldObject.WALL);
        when(fieldData.getConnectedTo()).thenReturn(fieldData2, null);
        when(fieldData2.getFieldObject()).thenReturn(FieldObject.LASER_SINGLE);
        String serializedBoard = serializeBoardService.serializeBoardGrid(2, 2, gridPane);
        assertEquals("ti_weal,ti" + System.getProperty("line.separator") + "ti,ti", serializedBoard);
    }

    @Test
    public void shouldSerializeAEmptyTileWithAWallAndADoubleLaser() {
        when(tile.getGroundFill()).thenReturn(GroundFill.EMPTY);
        when(tile.getFieldData(any(Orientation.class))).thenReturn(fieldData, null);
        when(fieldData.getFieldObject()).thenReturn(FieldObject.WALL);
        when(fieldData.getConnectedTo()).thenReturn(fieldData2, null);
        when(fieldData2.getFieldObject()).thenReturn(FieldObject.LASER_DOUBLE);
        String serializedBoard = serializeBoardService.serializeBoardGrid(2, 2, gridPane);
        assertEquals("ti_weall,ti" + System.getProperty("line.separator") + "ti,ti", serializedBoard);
    }

    @Test
     public void shouldSerializeAEmptyTileWithAWallAndATripleLaser() {
        when(tile.getGroundFill()).thenReturn(GroundFill.EMPTY);
        when(tile.getFieldData(any(Orientation.class))).thenReturn(fieldData, null);
        when(fieldData.getFieldObject()).thenReturn(FieldObject.WALL);
        when(fieldData.getConnectedTo()).thenReturn(fieldData2, null);
        when(fieldData2.getFieldObject()).thenReturn(FieldObject.LASER_TRIPLE);
        String serializedBoard = serializeBoardService.serializeBoardGrid(2, 2, gridPane);
        assertEquals("ti_wealll,ti" + System.getProperty("line.separator") + "ti,ti", serializedBoard);
    }

    @Test
    public void shouldSerializeAEmptyTileWithAWallAndAPusherInTheEvenPhase() {
        when(tile.getGroundFill()).thenReturn(GroundFill.EMPTY);
        when(tile.getFieldData(any(Orientation.class))).thenReturn(fieldData, null);
        when(fieldData.getFieldObject()).thenReturn(FieldObject.WALL);
        when(fieldData.getConnectedTo()).thenReturn(fieldData2, null);
        when(fieldData2.getFieldObject()).thenReturn(FieldObject.PUSHER_EVEN);
        String serializedBoard = serializeBoardService.serializeBoardGrid(2, 2, gridPane);
        assertEquals("ti_weap24,ti" + System.getProperty("line.separator") + "ti,ti", serializedBoard);
    }

    @Test
    public void shouldSerializeAEmptyTileWithAWallAndAPusherInTheOddPhase() {
        when(tile.getGroundFill()).thenReturn(GroundFill.EMPTY);
        when(tile.getFieldData(any(Orientation.class))).thenReturn(fieldData, null);
        when(fieldData.getFieldObject()).thenReturn(FieldObject.WALL);
        when(fieldData.getConnectedTo()).thenReturn(fieldData2, null);
        when(fieldData2.getFieldObject()).thenReturn(FieldObject.PUSHER_ODD);
        String serializedBoard = serializeBoardService.serializeBoardGrid(2, 2, gridPane);
        assertEquals("ti_weap135,ti" + System.getProperty("line.separator") + "ti,ti", serializedBoard);
    }

}