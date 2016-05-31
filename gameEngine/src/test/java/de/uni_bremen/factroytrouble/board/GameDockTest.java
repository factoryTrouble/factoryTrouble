package de.uni_bremen.factroytrouble.board;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.awt.Point;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import de.uni_bremen.factroytrouble.gameobjects.Robot;

/**
 * 
 * @author Thorben
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class GameDockTest {

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
    @Mock
    private Tile tile6;
    @Mock
    private Tile tile7;
    @Mock
    private Tile tile8;
    @Mock
    private Tile tile9;
    @Mock
    private Tile tile10;
    @Mock
    private Tile tile11;
    @Mock
    private Tile tile12;
    @Mock
    private Robot robot1;
    @Mock
    private Robot robot2;
    @Mock
    private Robot robot3;
    @Mock
    private Robot robot4;
    @Mock
    private Robot robot5;
    @Mock
    private Robot robot6;
    @Mock
    private Robot robot7;
    @Mock
    private Robot robot8;
    @Mock
    private Robot robot9;

    private Dock dock;

    @Before
    public void setUp() {
        when(tile1.getCoordinates()).thenReturn(new Point(1, 4));
        when(tile1.getRobot()).thenReturn(robot1);
        when(tile2.getCoordinates()).thenReturn(new Point(1, 5));
        when(tile3.getCoordinates()).thenReturn(new Point(2, 3));
        when(tile3.getRobot()).thenReturn(robot2);
        when(tile4.getCoordinates()).thenReturn(new Point(8, 9));
        when(tile4.getRobot()).thenReturn(robot3);
        when(tile5.getCoordinates()).thenReturn(new Point(11, 5));
        when(tile5.getRobot()).thenReturn(robot4);
        when(tile6.getCoordinates()).thenReturn(new Point(10, 7));
        when(tile6.getRobot()).thenReturn(robot5);
        when(tile7.getCoordinates()).thenReturn(new Point(1, 1));
        when(tile8.getCoordinates()).thenReturn(new Point(10, 11));
        when(tile8.getRobot()).thenReturn(robot6);
        when(tile9.getCoordinates()).thenReturn(new Point(3, 4));
        when(tile9.getRobot()).thenReturn(robot7);
        when(tile10.getCoordinates()).thenReturn(new Point(4, 3));
        when(tile11.getCoordinates()).thenReturn(new Point(1, 5));
        when(tile11.getRobot()).thenReturn(robot8);
        when(tile12.getCoordinates()).thenReturn(new Point(1, 5));
        when(tile12.getRobot()).thenReturn(robot9);

        dock = new GameDock(new Point(11, 4), tile1, tile3, tile4, tile5, tile6, tile7, tile8, tile9, tile10, tile11,
                tile12);
    }

    @Test
    public void getStartPosition() {
        assertEquals(tile1, dock.getStartPosition(robot1));
        assertEquals(tile3, dock.getStartPosition(robot2));
        assertEquals(tile4, dock.getStartPosition(robot3));
        assertEquals(tile5, dock.getStartPosition(robot4));
        assertEquals(tile6, dock.getStartPosition(robot5));
        assertEquals(tile8, dock.getStartPosition(robot6));
        assertEquals(tile9, dock.getStartPosition(robot7));
        assertEquals(tile11, dock.getStartPosition(robot8));
        assertEquals(null, dock.getStartPosition(robot9));
    }

}
