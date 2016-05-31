package de.uni_bremen.factroytrouble.gameobjects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import de.uni_bremen.factroytrouble.board.Tile;

/**
 * 
 * @author Lukas
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class GameGearTest {

    @Mock
    Robot robot;
    @Mock
    Robot robot2;
    @Mock
    Tile tile;
    @Mock
    Tile tile2;

    private Orientation robotOrientation1;
    private GameGear gear;
    private GameGear gear2;

    @Before
    public void setUp() {

        when(robot.getOrientation()).thenAnswer(invocation -> robotOrientation1);
        doAnswer(invocation -> robotOrientation1 = Orientation.values()[(robotOrientation1.ordinal() + 1) % 4])
                .when(robot).turn(true);

        when(tile.getRobot()).thenReturn(robot);
        when(robot.getName()).thenReturn("CaptainFalko");

        gear = new GameGear(true);
        gear2 = new GameGear(false);

    }

    @Test
    public void testExecute() {

        robotOrientation1 = Orientation.NORTH;
        gear.execute(tile);
        assertEquals(robot.getOrientation(), Orientation.WEST);

    }

    @Test
    public void testRotatesLeft() {
        assertTrue(gear.rotatesLeft());
        assertFalse(gear2.rotatesLeft());
    }

    @Test
    public void shouldDoNothingWhenRobotIsNull() {
        when(tile.getRobot()).thenReturn(null);
        gear.execute(tile);
        verify(robot, never()).turn(anyBoolean());
    }

}
