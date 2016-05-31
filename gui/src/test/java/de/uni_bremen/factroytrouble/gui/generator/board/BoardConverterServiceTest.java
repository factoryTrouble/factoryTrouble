package de.uni_bremen.factroytrouble.gui.generator.board;

import de.uni_bremen.factroytrouble.board.Field;
import de.uni_bremen.factroytrouble.board.Tile;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BoardConverterServiceTest {

    private BoardConverterService boardConverterService;
    @Mock private Tile tile1;
    @Mock private Tile tile2;
    @Mock private Tile tile3;
    @Mock private Tile tile4;
    @Mock private Field field1;
    @Mock private Field field2;
    private Map<Point, Field> pointFieldMap;

    @Before
    public void setUp() {
        boardConverterService = new BoardConverterService();
        prepareTestFields();
    }

    @Test
    public void shouldUseTheAbsoluteCoordinateForTheFirstTile() {
        Map<Point, Tile> pointTileMap = boardConverterService.convertBoardToMap(pointFieldMap);
        assertEquals(tile1, pointTileMap.get(new Point(0,0)));
    }

    @Test
    public void shouldUseTheAbsoluteCoordinateForTheSecondTile() {
        Map<Point, Tile> pointTileMap = boardConverterService.convertBoardToMap(pointFieldMap);
        assertEquals(tile2, pointTileMap.get(new Point(0,1)));
    }

    @Test
    public void shouldUseTheAbsoluteCoordinateForTheThirdTile() {
        Map<Point, Tile> pointTileMap = boardConverterService.convertBoardToMap(pointFieldMap);
        assertEquals(tile3, pointTileMap.get(new Point(1,0)));
    }

    @Test
    public void shouldUseTheAbsoluteCoordinateForTheFourthTile() {
        Map<Point, Tile> pointTileMap = boardConverterService.convertBoardToMap(pointFieldMap);
        assertEquals(tile4, pointTileMap.get(new Point(1,1)));
    }

    @Test
    public void shouldSet4ElementsIntoMap() {
        Map<Point, Tile> pointTileMap = boardConverterService.convertBoardToMap(pointFieldMap);
        assertEquals(4, pointTileMap.size());
    }

    @Test
    public void shouldSetTheMaximumXTo1() {
        boardConverterService.convertBoardToMap(pointFieldMap);
        assertEquals(Integer.valueOf(1), boardConverterService.getMaxX());
    }

    @Test
    public void shouldSetTheMaximumYTo1() {
        boardConverterService.convertBoardToMap(pointFieldMap);
        assertEquals(Integer.valueOf(1), boardConverterService.getMaxY());
    }

    @Test
    public void shouldSetTheMinimumXTo0() {
        boardConverterService.convertBoardToMap(pointFieldMap);
        assertEquals(Integer.valueOf(0), boardConverterService.getMinX());
    }

    @Test
    public void shouldSetTheMinimumYTo0() {
        boardConverterService.convertBoardToMap(pointFieldMap);
        assertEquals(Integer.valueOf(0), boardConverterService.getMinY());
    }

    private void prepareTestFields() {
        when(tile1.getAbsoluteCoordinates()).thenReturn(new Point(0, 0));
        when(tile2.getAbsoluteCoordinates()).thenReturn(new Point(0, 1));
        when(tile3.getAbsoluteCoordinates()).thenReturn(new Point(1, 0));
        when(tile4.getAbsoluteCoordinates()).thenReturn(new Point(1, 1));

        pointFieldMap = new HashMap<>();
        pointFieldMap.put(new Point(0,0), field1);
        pointFieldMap.put(new Point(0,1), field2);

        Map<Point, Tile> pointTileMap1 = new HashMap<>();
        Map<Point, Tile> pointTileMap2 = new HashMap<>();
        pointTileMap1.put(new Point(0, 0), tile1);
        pointTileMap1.put(new Point(1, 0), tile2);
        pointTileMap2.put(new Point(0, 0), tile3);
        pointTileMap2.put(new Point(1, 0), tile4);

        when(field1.getTiles()).thenReturn(pointTileMap1);
        when(field2.getTiles()).thenReturn(pointTileMap2);
    }


}