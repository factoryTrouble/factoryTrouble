package de.uni_bremen.factroytrouble.ai.ki1;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import de.uni_bremen.factroytrouble.board.Tile;

/**
 * Testet die {@link BoardAreaUnit}.
 * 
 * @author Pablo
 */

public class BoardAreaUnitTest {

    Point center;
    Point center2;
    Map<Point, Tile> tiles;
    Map<Point, Tile> tiles2;
    VisualUnitTest visualUnitTest;
    BoardAreaUnit boardAreaUnit;
    BoardAreaUnit boardAreaUnit2;
    BoardAreaUnit boardAreaUnit3;
    BoardAreaUnit boardAreaUnit4;
    BoardAreaUnit boardAreaUnit5;
    BoardAreaUnit boardAreaUnit6;
    
    @Before
    public void setUp(){
        center = new Point();
        center.setLocation(6, 6);
        tiles = defaulteTileMap();
        center2 = new Point();
        center2.setLocation(3, 3);
        tiles2 = defaulteTileMap();
        boardAreaUnit = new BoardAreaUnit(center,  tiles);
        boardAreaUnit2 = new BoardAreaUnit(null,  tiles);
        boardAreaUnit3 = new BoardAreaUnit(center,  null);
        boardAreaUnit4 = new BoardAreaUnit(center2,  tiles2);
        boardAreaUnit5 = new BoardAreaUnit(center,  tiles2);
        boardAreaUnit6 = new BoardAreaUnit(center, tiles);
    }
    

    @Test
    public void testEqualsObjectSameObjekt() {
        assertEquals(boardAreaUnit,boardAreaUnit);
    }
    
    @Test
    public void testEqualsObjectWithNull() {
        assertFalse(boardAreaUnit.equals(null));
    }
    
    @Test
    public void testEqualsObjectWithNewObject() {
        assertFalse(boardAreaUnit.equals(new Object()));
    }
    
    @Test
    public void testEqualsObjectWithCenterNull() {
        assertFalse(boardAreaUnit2.equals(boardAreaUnit));
    }
    
    @Test
    public void testEqualsObjectWithDifferentCenterNull() {
        assertFalse(boardAreaUnit.equals(boardAreaUnit2));
    }
    
    @Test
    public void testEqualsObjectWithOtherBoard() {
        assertFalse(boardAreaUnit.equals(boardAreaUnit4));
    }
    
    @Test
    public void testEqualsObjectWithoutTiles() {
        assertFalse(boardAreaUnit3.equals(boardAreaUnit));
    }
    
    @Test
    public void testEqualsObjectWithOtherTiles() {
        assertFalse(boardAreaUnit.equals(boardAreaUnit5));
    }
    
    @Test
    public void testEqualsObjectWithSameTiles() {
        assertTrue(boardAreaUnit.equals(boardAreaUnit6));
    }
    
    private Map<Point, Tile> defaulteTileMap() {
        Map<Point, Tile> m = new HashMap<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Tile mockTile = mock(Tile.class);
                when(mockTile.getCoordinates()).thenReturn(new Point (j,i));
                m.put(new Point(j, i), mockTile);
            }
        }
        return m;
    }
    
}
