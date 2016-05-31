package de.uni_bremen.factroytrouble.player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.gameobjects.Wall;

@RunWith(MockitoJUnitRunner.class)
public class UturnCardTest {

    @Mock
    private Tile respawn;
    @Mock
    private Wall wall;

    @Mock
    private Player player;
    @Mock
    Robot robot1;

    @Mock
    Robot robot2;
    @Mock
    Robot robot3;
    @Mock
    Robot robot4;
    private UturnCard card1;
    private UturnCard card2;

    @Before
    public void setUp() {

        when(robot1.getOrientation()).thenReturn(Orientation.SOUTH);
        when(robot2.getOrientation()).thenReturn(Orientation.WEST);
        when(robot3.getOrientation()).thenReturn(Orientation.NORTH);
        when(robot4.getOrientation()).thenReturn(Orientation.EAST);
        when(robot1.getName()).thenReturn("CaptainFalko");
        when(robot2.getName()).thenReturn("FalkoPunch");
        when(robot3.getName()).thenReturn("BlueFalko");
        when(robot4.getName()).thenReturn("BloodFalko");

        card1 = new GameUturnCard(10);
        card2 = new GameUturnCard(20);
    }

    @Test
    public void testExecute() throws Exception {
        // testet ob sich der Roboter richtig dreht, wenn execute aufegrufen
        // wird
        card1.execute(robot1);
        assertTrue(robot1.getOrientation() == Orientation.SOUTH);

        card1.execute(robot2);
        assertTrue(robot2.getOrientation() == Orientation.WEST);

        card1.execute(robot3);
        assertTrue(robot3.getOrientation() == Orientation.NORTH);

        card1.execute(robot4);
        assertTrue(robot4.getOrientation() == Orientation.EAST);

    }

    @Test
    public void testGetPriority() throws Exception {
        // testet ob die richtige Priorität zurückgegeben wird
        assertEquals(card1.getPriority(), 10);
        assertFalse(card2.getPriority() == 10);
    }

}
