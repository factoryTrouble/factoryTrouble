package de.uni_bremen.factroytrouble.gameobjects;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

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
public class GameHoleTest {

    @Mock
    Tile tile;
    @Mock
    Tile tile2;
    @Mock
    Robot robot;

    int robotLives;
    Hole hole;

    @Before
    public void setUp() {
        when(tile.getRobot()).thenReturn(robot);
        when(robot.getLives()).thenAnswer(invocation -> robotLives);
        doAnswer(invocation -> robotLives -= 1).when(robot).kill();

        hole = new GameHole();

    }

    @Test
    public void testExecute() throws Exception {
        robotLives = 3;
        tile.setRobot(robot);
        
        hole.execute(tile2);
        assertTrue(robot.getLives() == 3);

        hole.execute(tile);
        assertTrue(robot.getLives() == 2);
    }

}
