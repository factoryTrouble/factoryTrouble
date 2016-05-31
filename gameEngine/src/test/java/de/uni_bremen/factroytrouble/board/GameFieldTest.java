package de.uni_bremen.factroytrouble.board;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.awt.Point;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * 
 * @author Thorben
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class GameFieldTest {

    @Mock
    private Tile tile1;
    @Mock
    private Tile tile2;
    @Mock
    private Tile tile3;
    @Mock
    private Tile tile4;
    @Mock
    private Tile tile5;

    private Field field;

    @Before
    public void setUp() {
        when(tile1.getCoordinates()).thenReturn(new Point(1, 4));
        when(tile3.getCoordinates()).thenReturn(new Point(6, 7));
        when(tile4.getCoordinates()).thenReturn(new Point(1, 4));

        field = new GameField(new Point(11, 4), tile1, tile3, tile4);
    }

    @Test
    public void getTiles() {
        Map<Point, Tile> tiles = field.getTiles();

        assertEquals(2, tiles.size());
    }

    @Test
    public void getCoordinates() {
        assertEquals(new Point(11, 4), field.getCoordinates());
    }

    @Test
    public void hasTileTest() {
        assertTrue(field.hasTile(tile1));
        assertFalse(field.hasTile(tile2));
        assertTrue(field.hasTile(tile3));
        assertFalse(field.hasTile(tile4));
        assertFalse(field.hasTile(tile5));
    }

}
