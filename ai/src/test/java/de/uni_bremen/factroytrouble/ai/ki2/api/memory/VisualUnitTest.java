package de.uni_bremen.factroytrouble.ai.ki2.api.memory;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import de.uni_bremen.factroytrouble.ai.ais.AIPlayer2;
import de.uni_bremen.factroytrouble.ai.ki2.ai.memory.ScullyConveyorInfo;
import de.uni_bremen.factroytrouble.ai.ki2.ai.memory.ScullyVisual;
import de.uni_bremen.factroytrouble.board.Board;
import de.uni_bremen.factroytrouble.board.Field;
import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.gameobjects.ConveyorBelt;
import de.uni_bremen.factroytrouble.gameobjects.Flag;
import de.uni_bremen.factroytrouble.gameobjects.Gear;
import de.uni_bremen.factroytrouble.gameobjects.Hole;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.gameobjects.Wall;

@RunWith(MockitoJUnitRunner.class)
public class VisualUnitTest {

    @Mock
    private Robot robot1, robot2;
    @Mock
    private Board mockBoard;
    @Mock
    private Field mockField;
    @Mock
    private Hole hole1, hole2;
    @Mock
    private Gear gear1, gear2;
    @Mock
    private Flag flag1, flag2;
    @Mock
    private ConveyorBelt cb1, cb2, cb3, cb4;
    @Mock
    private Wall wall1, wall2;
    @Mock
    private AIPlayer2 ai2;

    @Mock
    ScullyConveyorInfo sci;

    private Map<Point, Tile> mockTiles;

    @Before
    public void setUp() throws Exception {
        mockTiles = defaultTileMap();
        HashMap<Point, Field> map = new HashMap<>();
        map.put(new Point(0, 0), mockField);
        Mockito.when(gear1.rotatesLeft()).thenReturn(true);
        Mockito.when(gear2.rotatesLeft()).thenReturn(false);
        Mockito.when(mockBoard.getFields()).thenReturn(map);
        Mockito.when(mockField.getTiles()).thenReturn(mockTiles);
        Mockito.when(ai2.getRobot()).thenReturn(robot1);
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                Mockito.when(mockBoard.getAbsoluteCoordinates(mockTiles.get(new Point(i, j))))
                        .thenReturn(new Point(i, j));

            }
        }

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void shouldGetNoRobotFieldEmpty() {
        ScullyVisual vu = new ScullyVisual(mockBoard, ai2);
        assertEquals(0, vu.getRobots().size());
    }

    @Test
    public void shouldGetOneRobot() {

        Mockito.when(mockTiles.get(new Point(5, 5)).getRobot()).thenReturn(robot1);
        ScullyVisual vu = new ScullyVisual(mockBoard, ai2);
        assertEquals(1, vu.getRobots().size());
    }

    @Test
    public void shouldGetTwoRobots() {
        Mockito.when(mockTiles.get(new Point(5, 5)).getRobot()).thenReturn(robot1);
        Mockito.when(mockTiles.get(new Point(1, 1)).getRobot()).thenReturn(robot2);
        ScullyVisual vu = new ScullyVisual(mockBoard, ai2);
        assertEquals(2, vu.getRobots().size());
    }

    @Test
    public void shouldGetNoHoles() {
        ScullyVisual vu = new ScullyVisual(mockBoard, ai2);
        assertEquals(0, vu.getHoles().size());
    }

    @Test
    public void shouldGetOneHole() {
        Mockito.when(mockTiles.get(new Point(5, 5)).getFieldObject()).thenReturn(hole1);
        ScullyVisual vu = new ScullyVisual(mockBoard, ai2);
        assertEquals(1, vu.getHoles().size());
    }

    @Test
    public void shouldGetTwoHoles() {
        Mockito.when(mockTiles.get(new Point(5, 5)).getFieldObject()).thenReturn(hole1);
        Mockito.when(mockTiles.get(new Point(1, 1)).getFieldObject()).thenReturn(hole2);
        ScullyVisual vu = new ScullyVisual(mockBoard, ai2);
        assertEquals(2, vu.getHoles().size());
    }

    @Test
    public void shouldGetNoGear() {
        ScullyVisual vu = new ScullyVisual(mockBoard, ai2);
        assertEquals(0, vu.getGears().size());
    }

    @Test
    public void shouldGetOneGear() {
        Mockito.when(mockTiles.get(new Point(5, 5)).getFieldObject()).thenReturn(gear1);
        ScullyVisual vu = new ScullyVisual(mockBoard, ai2);
        assertEquals(1, vu.getGears().size());
    }

    @Test
    public void shouldGetTwoGears() {
        Mockito.when(mockTiles.get(new Point(5, 5)).getFieldObject()).thenReturn(gear1);
        Mockito.when(mockTiles.get(new Point(1, 1)).getFieldObject()).thenReturn(gear2);
        ScullyVisual vu = new ScullyVisual(mockBoard, ai2);
        assertEquals(2, vu.getGears().size());
    }

    @Test
    public void shouldGetOneLeftGear() {
        Mockito.when(mockTiles.get(new Point(5, 5)).getFieldObject()).thenReturn(gear1);
        Mockito.when(mockTiles.get(new Point(3, 3)).getFieldObject()).thenReturn(flag1);
        Mockito.when(mockTiles.get(new Point(2, 2)).getFieldObject()).thenReturn(hole1);
        Mockito.when(mockTiles.get(new Point(1, 1)).getFieldObject()).thenReturn(gear2);
        ScullyVisual vu = new ScullyVisual(mockBoard, ai2);
        assertEquals(Arrays.asList(new Point(5, 5)), vu.getGearsLeft());
    }

    @Test
    public void shouldGetOneRightGear() {
        Mockito.when(mockTiles.get(new Point(5, 5)).getFieldObject()).thenReturn(gear1);
        Mockito.when(mockTiles.get(new Point(3, 3)).getFieldObject()).thenReturn(flag1);
        Mockito.when(mockTiles.get(new Point(2, 2)).getFieldObject()).thenReturn(hole1);
        Mockito.when(mockTiles.get(new Point(1, 1)).getFieldObject()).thenReturn(gear2);
        ScullyVisual vu = new ScullyVisual(mockBoard, ai2);
        assertEquals(Arrays.asList(new Point(1, 1)), vu.getGearsRight());
    }
    
    @Test
    public void shouldGetHighestPoint() {
        ScullyVisual vu = new ScullyVisual(mockBoard, ai2);
        assertEquals(new Point(12, 0), vu.getHighestPoint());
    }

    @Test
    public void shouldGetNoBelts() {

        ScullyVisual vu = new ScullyVisual(mockBoard, ai2);
        assertEquals(0, vu.getConveyor().size());

    }

    @Test
    public void shouldGetFourBelts() {
        Mockito.when(mockTiles.get(new Point(5, 5)).getFieldObject()).thenReturn(cb1);
        Mockito.when(mockTiles.get(new Point(5, 6)).getFieldObject()).thenReturn(cb2);
        Mockito.when(mockTiles.get(new Point(5, 7)).getFieldObject()).thenReturn(cb3);
        Mockito.when(mockTiles.get(new Point(5, 8)).getFieldObject()).thenReturn(cb4);

        ScullyVisual vu = new ScullyVisual(mockBoard, ai2);
        assertEquals(4, vu.getConveyor().size());

    }

    @Test
    public void shouldGetOneWallNorth() {
        Mockito.when(wall1.getOrientation()).thenReturn(Orientation.NORTH);
        Mockito.when(mockTiles.get(new Point(5, 5)).getWall(Orientation.NORTH)).thenReturn(wall1);
        ScullyVisual vu = new ScullyVisual(mockBoard, ai2);
        assertEquals(1, vu.getWallsNorth().size());

    }

    @Test
    public void shouldGetOneWallEast() {
        Mockito.when(wall1.getOrientation()).thenReturn(Orientation.EAST);
        Mockito.when(mockTiles.get(new Point(5, 5)).getWall(Orientation.EAST)).thenReturn(wall1);
        ScullyVisual vu = new ScullyVisual(mockBoard, ai2);
        assertEquals(1, vu.getWallsEast().size());

    }

    @Test
    public void shouldGetOneWallSouth() {
        Mockito.when(wall1.getOrientation()).thenReturn(Orientation.SOUTH);
        Mockito.when(mockTiles.get(new Point(5, 5)).getWall(Orientation.SOUTH)).thenReturn(wall1);
        ScullyVisual vu = new ScullyVisual(mockBoard, ai2);
        assertEquals(1, vu.getWallsSouth().size());

    }

    @Test
    public void shouldGetAllTiles() {
        ScullyVisual vu = new ScullyVisual(mockBoard, ai2);
        assertEquals(13 * 13, vu.getTiles().size());
    }

    @Test
    public void shouldGetOneTile() {
        Mockito.when(mockBoard.getAbsoluteCoordinates(mockTiles.get(new Point(0, 0)))).thenReturn(new Point(0, 0));
        ScullyVisual vu = new ScullyVisual(mockBoard, ai2);
        int x = vu.getTile(0, 0).getAbsoluteCoordinates().x;
        int y = vu.getTile(0, 0).getAbsoluteCoordinates().y;
        assertEquals(0, x);
        assertEquals(0, y);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgunemtXToSmall() {
        ScullyVisual vu = new ScullyVisual(mockBoard, ai2);
        vu.getTile(-1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgunemtYToSmall() {
        ScullyVisual vu = new ScullyVisual(mockBoard, ai2);
        vu.getTile(0, -1);
    }

    @Test
    public void shouldGetOneWallWest() {
        Mockito.when(wall1.getOrientation()).thenReturn(Orientation.WEST);
        Mockito.when(wall2.getOrientation()).thenReturn(Orientation.NORTH);
        Mockito.when(mockTiles.get(new Point(5, 5)).getWall(Orientation.WEST)).thenReturn(wall1);
        Mockito.when(mockTiles.get(new Point(5, 5)).getWall(Orientation.NORTH)).thenReturn(wall2);
        ScullyVisual vu = new ScullyVisual(mockBoard, ai2);
        assertEquals(1, vu.getWallsWest().size());
        assertEquals(0, vu.getWallsEast().size());
        assertEquals(1, vu.getWallsNorth().size());

    }

    @Test
    public void shouldGetNoFlag() {
        ScullyVisual vu = new ScullyVisual(mockBoard, ai2);
        assertEquals(null, vu.getFlagPosition(1));

    }

    @Test
    public void shouldGetOneFlag() {
        Mockito.when(flag1.getNumber()).thenReturn(1);
        Mockito.when(mockTiles.get(new Point(0, 0)).getFieldObject()).thenReturn(flag1);
        Mockito.when(mockBoard.getAbsoluteCoordinates(mockTiles.get(new Point(0, 0)))).thenReturn(new Point(0, 0));
        ScullyVisual vu = new ScullyVisual(mockBoard, ai2);
        assertEquals(new Point(0, 0), vu.getFlagPosition(1));

    }

    @Test
    public void shouldGetNoFieldObjects() {
        ScullyVisual vu = new ScullyVisual(mockBoard, ai2);
        assertEquals(0, vu.getFieldObjects().size());
    }

    @Test
    public void shouldGetOneFieldObjects() {
        Mockito.when(mockTiles.get(new Point(5, 5)).getFieldObject()).thenReturn(cb1);
        ScullyVisual vu = new ScullyVisual(mockBoard, ai2);
        assertEquals(1, vu.getFieldObjects().size());
    }

    @Test
    public void shouldGetThreeFieldObjects() {
        Mockito.when(mockTiles.get(new Point(5, 5)).getFieldObject()).thenReturn(cb1);
        Mockito.when(mockTiles.get(new Point(5, 0)).getFieldObject()).thenReturn(flag1);
        Mockito.when(mockTiles.get(new Point(6, 5)).getFieldObject()).thenReturn(hole1);
        ScullyVisual vu = new ScullyVisual(mockBoard, ai2);
        assertEquals(3, vu.getFieldObjects().size());
    }

    @Test
    public void shouldGetConveyorInfoNorth() {
        Mockito.when(cb1.getOrientation()).thenReturn(Orientation.NORTH);
        Mockito.when(mockTiles.get(new Point(1, 1)).getFieldObject()).thenReturn(cb1);
        ScullyVisual vu = new ScullyVisual(mockBoard, ai2);
        assertNotNull(vu.getConveyorMove());
        assertEquals(new Point(1, 2), vu.getConveyorMove().get(new Point(1, 1)).getPointAfterConveyor());
        assertEquals(0, vu.getConveyorMove().get(new Point(1, 1)).getDirection());
    }

    @Test
    public void shouldGetTwoConveyorInfoNorth() {
        Mockito.when(cb1.getOrientation()).thenReturn(Orientation.NORTH);
        Mockito.when(cb2.getOrientation()).thenReturn(Orientation.NORTH);
        Mockito.when(mockTiles.get(new Point(1, 1)).getFieldObject()).thenReturn(cb1);
        Mockito.when(mockTiles.get(new Point(1, 2)).getFieldObject()).thenReturn(cb2);
        ScullyVisual vu = new ScullyVisual(mockBoard, ai2);
        assertNotNull(vu.getConveyorMove());
        assertEquals(new Point(1, 2), vu.getConveyorMove().get(new Point(1, 1)).getPointAfterConveyor());
        assertEquals(new Point(1, 3), vu.getConveyorMove().get(new Point(1, 2)).getPointAfterConveyor());
        assertEquals(0, vu.getConveyorMove().get(new Point(1, 1)).getDirection());
        assertEquals(0, vu.getConveyorMove().get(new Point(1, 2)).getDirection());
    }

    @Test
    public void shouldGetConveyorInfoSouth() {
        Mockito.when(cb1.getOrientation()).thenReturn(Orientation.SOUTH);
        Mockito.when(mockTiles.get(new Point(1, 1)).getFieldObject()).thenReturn(cb1);
        ScullyVisual vu = new ScullyVisual(mockBoard, ai2);
        assertNotNull(vu.getConveyorMove());
        assertEquals(new Point(1, 0), vu.getConveyorMove().get(new Point(1, 1)).getPointAfterConveyor());
        assertEquals(0, vu.getConveyorMove().get(new Point(1, 1)).getDirection());
    }

    @Test
    public void shouldGetConveyorInfoEast() {
        Mockito.when(cb1.getOrientation()).thenReturn(Orientation.EAST);
        Mockito.when(mockTiles.get(new Point(1, 1)).getFieldObject()).thenReturn(cb1);
        ScullyVisual vu = new ScullyVisual(mockBoard, ai2);
        assertNotNull(vu.getConveyorMove());
        assertEquals(new Point(2, 1), vu.getConveyorMove().get(new Point(1, 1)).getPointAfterConveyor());
        assertEquals(0, vu.getConveyorMove().get(new Point(1, 1)).getDirection());
    }

    @Test
    public void shouldGetConveyorInfoWest() {
        Mockito.when(cb1.getOrientation()).thenReturn(Orientation.WEST);
        Mockito.when(mockTiles.get(new Point(1, 1)).getFieldObject()).thenReturn(cb1);
        ScullyVisual vu = new ScullyVisual(mockBoard, ai2);
        assertNotNull(vu.getConveyorMove());
        assertEquals(new Point(0, 1), vu.getConveyorMove().get(new Point(1, 1)).getPointAfterConveyor());
        assertEquals(0, vu.getConveyorMove().get(new Point(1, 1)).getDirection());
    }

    @Test
    public void shouldGetConveyorExpressInfoNorth() {
        Mockito.when(cb1.getOrientation()).thenReturn(Orientation.NORTH);
        Mockito.when(cb1.isExpress()).thenReturn(true);
        Mockito.when(cb2.getOrientation()).thenReturn(Orientation.NORTH);
        Mockito.when(cb2.isExpress()).thenReturn(true);
        Mockito.when(mockTiles.get(new Point(1, 1)).getFieldObject()).thenReturn(cb1);
        Mockito.when(mockTiles.get(new Point(1, 2)).getFieldObject()).thenReturn(cb2);
        ScullyVisual vu = new ScullyVisual(mockBoard, ai2);
        assertNotNull(vu.getConveyorMove());
        assertEquals(new Point(1, 3), vu.getConveyorMove().get(new Point(1, 1)).getPointAfterConveyor());
        assertEquals(0, vu.getConveyorMove().get(new Point(1, 1)).getDirection());
    }

    @Test
    public void shouldGetConveyorExpressInfoSouth() {
        Mockito.when(cb1.getOrientation()).thenReturn(Orientation.SOUTH);
        Mockito.when(cb1.isExpress()).thenReturn(true);
        Mockito.when(cb2.getOrientation()).thenReturn(Orientation.SOUTH);
        Mockito.when(cb2.isExpress()).thenReturn(true);
        Mockito.when(mockTiles.get(new Point(5, 5)).getFieldObject()).thenReturn(cb1);
        Mockito.when(mockTiles.get(new Point(5, 4)).getFieldObject()).thenReturn(cb2);
        ScullyVisual vu = new ScullyVisual(mockBoard, ai2);
        assertNotNull(vu.getConveyorMove());
        assertEquals(new Point(5, 3), vu.getConveyorMove().get(new Point(5, 5)).getPointAfterConveyor());
        assertEquals(0, vu.getConveyorMove().get(new Point(5, 5)).getDirection());
    }

    @Test
    public void shouldGetConveyorExpressInfoEast() {
        Mockito.when(cb1.getOrientation()).thenReturn(Orientation.EAST);
        Mockito.when(cb1.isExpress()).thenReturn(true);
        Mockito.when(cb2.getOrientation()).thenReturn(Orientation.EAST);
        Mockito.when(cb2.isExpress()).thenReturn(true);
        Mockito.when(mockTiles.get(new Point(5, 5)).getFieldObject()).thenReturn(cb1);
        Mockito.when(mockTiles.get(new Point(6,5)).getFieldObject()).thenReturn(cb2);
        ScullyVisual vu = new ScullyVisual(mockBoard, ai2);
        assertNotNull(vu.getConveyorMove());
        assertEquals(new Point(7, 5), vu.getConveyorMove().get(new Point(5, 5)).getPointAfterConveyor());
        assertEquals(0, vu.getConveyorMove().get(new Point(5, 5)).getDirection());
    }
    
    @Test
    public void shouldGetConveyorInfoNorthTurnRight() {
        Mockito.when(cb1.getOrientation()).thenReturn(Orientation.NORTH);
        Mockito.when(cb2.getOrientation()).thenReturn(Orientation.EAST);
        Mockito.when(mockTiles.get(new Point(1, 1)).getFieldObject()).thenReturn(cb1);
        Mockito.when(mockTiles.get(new Point(1, 2)).getFieldObject()).thenReturn(cb2);
        ScullyVisual vu = new ScullyVisual(mockBoard, ai2);
        assertNotNull(vu.getConveyorMove());
        assertEquals(new Point(1, 2), vu.getConveyorMove().get(new Point(1, 1)).getPointAfterConveyor());
        assertEquals(2, vu.getConveyorMove().get(new Point(1, 1)).getDirection());
    }
    
    @Test
    public void shouldGetConveyorInfoEastTurnRight() {
        Mockito.when(cb1.getOrientation()).thenReturn(Orientation.EAST);
        Mockito.when(cb2.getOrientation()).thenReturn(Orientation.SOUTH);
        Mockito.when(mockTiles.get(new Point(1, 1)).getFieldObject()).thenReturn(cb1);
        Mockito.when(mockTiles.get(new Point(2, 1)).getFieldObject()).thenReturn(cb2);
        ScullyVisual vu = new ScullyVisual(mockBoard, ai2);
        assertNotNull(vu.getConveyorMove());
        assertEquals(new Point(2, 1), vu.getConveyorMove().get(new Point(1, 1)).getPointAfterConveyor());
        assertEquals(2, vu.getConveyorMove().get(new Point(1, 1)).getDirection());
    }
    
    @Test
    public void shouldGetConveyorInfoSouthTurnRight() {
        Mockito.when(cb1.getOrientation()).thenReturn(Orientation.SOUTH);
        Mockito.when(cb2.getOrientation()).thenReturn(Orientation.WEST);
        Mockito.when(mockTiles.get(new Point(2,2)).getFieldObject()).thenReturn(cb1);
        Mockito.when(mockTiles.get(new Point(2,1)).getFieldObject()).thenReturn(cb2);
        ScullyVisual vu = new ScullyVisual(mockBoard, ai2);
        assertNotNull(vu.getConveyorMove());
        assertEquals(new Point(2,1), vu.getConveyorMove().get(new Point(2,2)).getPointAfterConveyor());
        assertEquals(2, vu.getConveyorMove().get(new Point(2,2)).getDirection());
    }
    
    @Test
    public void shouldGetConveyorInfoWestTurnRight() {
        Mockito.when(cb1.getOrientation()).thenReturn(Orientation.WEST);
        Mockito.when(cb2.getOrientation()).thenReturn(Orientation.NORTH);
        Mockito.when(mockTiles.get(new Point(2, 2)).getFieldObject()).thenReturn(cb1);
        Mockito.when(mockTiles.get(new Point(1,2)).getFieldObject()).thenReturn(cb2);
        ScullyVisual vu = new ScullyVisual(mockBoard, ai2);
        assertNotNull(vu.getConveyorMove());
        assertEquals(new Point(1,2), vu.getConveyorMove().get(new Point(2, 2)).getPointAfterConveyor());
        assertEquals(2, vu.getConveyorMove().get(new Point(2, 2)).getDirection());
    }
    
    @Test
    public void shouldGetConveyorInfoNorthTurnLeft() {
        Mockito.when(cb1.getOrientation()).thenReturn(Orientation.NORTH);
        Mockito.when(cb2.getOrientation()).thenReturn(Orientation.WEST);
        Mockito.when(mockTiles.get(new Point(1, 1)).getFieldObject()).thenReturn(cb1);
        Mockito.when(mockTiles.get(new Point(1, 2)).getFieldObject()).thenReturn(cb2);
        ScullyVisual vu = new ScullyVisual(mockBoard, ai2);
        assertNotNull(vu.getConveyorMove());
        assertEquals(new Point(1, 2), vu.getConveyorMove().get(new Point(1, 1)).getPointAfterConveyor());
        assertEquals(1, vu.getConveyorMove().get(new Point(1, 1)).getDirection());
    }
    
    @Test
    public void shouldGetConveyorInfoEastTurnLeft() {
        Mockito.when(cb1.getOrientation()).thenReturn(Orientation.EAST);
        Mockito.when(cb2.getOrientation()).thenReturn(Orientation.NORTH);
        Mockito.when(mockTiles.get(new Point(2, 2)).getFieldObject()).thenReturn(cb1);
        Mockito.when(mockTiles.get(new Point(3, 2)).getFieldObject()).thenReturn(cb2);
        ScullyVisual vu = new ScullyVisual(mockBoard, ai2);
        assertNotNull(vu.getConveyorMove());
        assertEquals(new Point(3,2), vu.getConveyorMove().get(new Point(2,2)).getPointAfterConveyor());
        assertEquals(1, vu.getConveyorMove().get(new Point(2,2)).getDirection());
    }

    
    @Test
    public void shouldGetConveyorInfoSouthTurnLeft() {
        Mockito.when(cb1.getOrientation()).thenReturn(Orientation.SOUTH);
        Mockito.when(cb2.getOrientation()).thenReturn(Orientation.EAST);
        Mockito.when(mockTiles.get(new Point(2, 2)).getFieldObject()).thenReturn(cb1);
        Mockito.when(mockTiles.get(new Point(2, 1)).getFieldObject()).thenReturn(cb2);
        ScullyVisual vu = new ScullyVisual(mockBoard, ai2);
        assertNotNull(vu.getConveyorMove());
        assertEquals(new Point(2,1), vu.getConveyorMove().get(new Point(2,2)).getPointAfterConveyor());
        assertEquals(1, vu.getConveyorMove().get(new Point(2,2)).getDirection());
    }

    private Map<Point, Tile> defaultTileMap() {
        Map<Point, Tile> m = new HashMap<>();
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 13; j++) {
                Tile tile = Mockito.mock(Tile.class);
                Mockito.when(tile.getCoordinates()).thenReturn(new Point(i, j));
                Mockito.when(tile.getAbsoluteCoordinates()).thenReturn(new Point(i, j));

                m.put(new Point(i, j), tile);
            }
        }
        for (int k = 0; k < 12; k++) {
            for (int l = 0; l < 12; l++) {

                Tile tile = m.get(new Point(k, l));
                HashMap<Orientation, Tile> mockNeighbours = new HashMap<>();
                if (l < 12) {
                    mockNeighbours.put(Orientation.EAST, m.get(new Point(k, l + 1)));
                }

                if (k < 12) {
                    mockNeighbours.put(Orientation.NORTH, m.get(new Point(k + 1, l)));
                }
                Mockito.when(tile.getNeighbors()).thenReturn(mockNeighbours);

            }
        }System.out.println(m);
        return m;
    }

}
