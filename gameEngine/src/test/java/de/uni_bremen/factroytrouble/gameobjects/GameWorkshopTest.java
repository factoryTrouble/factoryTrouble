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

@RunWith(MockitoJUnitRunner.class)
public class GameWorkshopTest {

    @Mock
    private Robot robot;

    @Mock
    private Tile tile;

    private Tile robotsTile = null;

    private Workshop workshop;

    @Before
    public void setUp() throws Exception {
        workshop = new GameWorkshop(true);

        when(tile.getRobot()).thenReturn(robot);
        doAnswer(invocation -> robotsTile = tile).when(robot).setRespawnPoint();

        when(robot.getName()).thenReturn("SchrottBot");
    }

    @Test
    public void testExecute() {
        workshop.execute(tile);

        assertEquals(robotsTile, tile);
    }

    @Test
    public void testIsAdvancedWorkshop() {
        assertTrue(workshop.isAdvancedWorkshop());
        workshop = new GameWorkshop(false);
        assertFalse(workshop.isAdvancedWorkshop());
    }

    @Test
    public void shouldNotSetTheResparnPointWhenRobotIsNull() {
        when(tile.getRobot()).thenReturn(null);
        workshop.execute(tile);
        verify(robot, never()).setRespawnPoint();
    }

}
