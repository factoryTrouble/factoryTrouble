package de.uni_bremen.factroytrouble.board;

import de.uni_bremen.factroytrouble.gameobjects.FieldObject;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.gameobjects.Wall;
import de.uni_bremen.factroytrouble.misc.TestFactory;
import de.uni_bremen.factroytrouble.player.Master;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.awt.Point;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * 
 * @author Thorben
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class GameTileTest {
    private static final int GAME_ID = 0;

    @Mock
    Wall eastWall;
    @Mock
    Wall northWall;
    @Mock
    FieldObject object;
    @Mock
    Robot robot;
    @Mock
    Master master;
    @Mock
    Board board;

    private Tile tile1;
    private Tile tile2;
    private Tile tile3;
    private Tile tile4;
    private Tile tile5;
    private Tile tile6;

    @Before
    public void setUp() {
        when(master.getBoard()).thenReturn(board);

        TestFactory.setMaster(master);

        when(eastWall.getOrientation()).thenReturn(Orientation.EAST);
        when(northWall.getOrientation()).thenReturn(Orientation.NORTH);

        tile1 = new GameTile(GAME_ID, new Point(3, 5), eastWall, northWall);
        tile2 = new GameTile(GAME_ID, new Point(7, 8), object, robot);
    }

    @Test
    public void actionTest() {
        tile1.action();
        tile2.action();
    }

    @Test
    public void removeFieldObjectTest() {
        assertEquals(object, tile2.getFieldObject());
        tile2.removeFieldObject();
        assertEquals(null, tile2.getFieldObject());
        tile2.setFieldObject(object);
    }

    @Test
    public void hasWallTest() {
        assertTrue(tile1.hasWall(Orientation.EAST));
        assertTrue(tile1.hasWall(Orientation.NORTH));
        assertFalse(tile1.hasWall(Orientation.WEST));
        assertFalse(tile1.hasWall(Orientation.SOUTH));
        assertFalse(tile2.hasWall(Orientation.EAST));
        assertFalse(tile2.hasWall(Orientation.NORTH));
        assertFalse(tile2.hasWall(Orientation.WEST));
        assertFalse(tile2.hasWall(Orientation.SOUTH));
    }

    @Test
    public void getCoordinatesTest() {
        assertEquals(new Point(3, 5), tile1.getCoordinates());
        assertEquals(new Point(7, 8), tile2.getCoordinates());
    }

    @Test
    public void getRobotTest() {
        assertEquals(null, tile1.getRobot());
        assertEquals(robot, tile2.getRobot());
        tile2.setRobot(robot);
    }

    @Test
    public void getWallsTest() {
        List<Wall> walls = tile1.getWalls();

        assertEquals(0, tile2.getWalls().size());
        assertEquals(2, walls.size());
        assertTrue(walls.contains(eastWall));
        assertTrue(walls.contains(northWall));
    }

    @Test
    public void getWallTest() {
        assertEquals(northWall, tile1.getWall(Orientation.NORTH));
        assertEquals(null, tile1.getWall(Orientation.WEST));
        assertEquals(null, tile1.getWall(Orientation.SOUTH));
        assertEquals(eastWall, tile1.getWall(Orientation.EAST));
    }

    @Test
    public void getAbsoluteCoordinatesTest() {
        when(board.getAbsoluteCoordinates(tile1)).thenReturn(null);
        when(board.getAbsoluteCoordinates(tile2)).thenReturn(new Point(6, 6));

        assertEquals(null, tile1.getAbsoluteCoordinates());
        assertEquals(new Point(6, 6), tile2.getAbsoluteCoordinates());
    }

    @Test
    public void findNeighborsTest() {
        when(board.findNextTile(tile1, Orientation.NORTH)).thenReturn(tile3);
        when(board.findNextTile(tile1, Orientation.WEST)).thenReturn(tile4);
        when(board.findNextTile(tile1, Orientation.SOUTH)).thenReturn(tile5);
        when(board.findNextTile(tile1, Orientation.EAST)).thenReturn(tile6);
        when(board.findNextTile(tile2, Orientation.NORTH)).thenReturn(tile1);
        when(board.findNextTile(tile2, Orientation.EAST)).thenReturn(tile6);

        Map<Orientation, Tile> neighbors = tile1.getNeighbors();
        assertTrue(neighbors.get(Orientation.NORTH) == tile3);
        assertTrue(neighbors.get(Orientation.WEST) == tile4);
        assertTrue(neighbors.get(Orientation.SOUTH) == tile5);
        assertTrue(neighbors.get(Orientation.EAST) == tile6);

        neighbors = tile2.getNeighbors();
        assertTrue(neighbors.get(Orientation.NORTH) == tile1);
        assertTrue(neighbors.get(Orientation.WEST) == null);
        assertTrue(neighbors.get(Orientation.SOUTH) == null);
        assertTrue(neighbors.get(Orientation.EAST) == tile6);
    }

}
