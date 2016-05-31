package de.uni_bremen.factroytrouble.gameobjects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * 
 * @author Lukas
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class GameWallTest {

    private Wall wall;
    private Wall wall2;
    private int[] phase;

    @Before
    public void setUp() {
        phase = new int[] { 1, 3 };
        wall = new GameWall(Orientation.SOUTH, phase);
        wall2 = new GameWall(Orientation.SOUTH);
    }

    @Test
    public void testGetOrientation() {
        assertEquals(wall.getOrientation(), Orientation.SOUTH);
    }

    @Test
    public void testSetLaser() {
        assertEquals(wall.hasLaser(), 0);
        wall.setLaser();
        assertEquals(wall.hasLaser(), 1);
        wall.setLaser();
        assertEquals(wall.hasLaser(), 2);
    }

    @Test
    public void testHasLaser() {
        assertFalse(wall.hasLaser() > 0);
        wall.setLaser();
        assertTrue(wall.hasLaser() > 0);
    }

    @Test
    public void testGetPusherPhases() {
        assertEquals(wall.getPusherPhases(), phase);
        assertFalse(wall2.getPusherPhases() == phase);
    }

}
