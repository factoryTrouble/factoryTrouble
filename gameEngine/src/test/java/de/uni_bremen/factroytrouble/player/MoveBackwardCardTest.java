package de.uni_bremen.factroytrouble.player;

import de.uni_bremen.factroytrouble.board.Board;
import de.uni_bremen.factroytrouble.board.GameTile;
import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.gameobjects.GameRobot;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.gameobjects.Wall;
import de.uni_bremen.factroytrouble.misc.TestFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.awt.Point;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MoveBackwardCardTest {
    private static final int GAME_ID = 0;

    @Mock
    private Wall wall;

    @Mock
    private Tile respawn;
    @Mock
    private Player player;
    @Mock
    private Robot robot1;
    @Mock
    Master master;
    @Mock
    Board board;

    private MoveBackwardCard card1;
    private MoveBackwardCard card2;

    @Before
    public void setUp() {
        when(player.getRobot()).thenReturn(robot1);
        when(robot1.getCurrentTile()).thenReturn(respawn);
        when(respawn.getCoordinates()).thenReturn(new Point(2, 1));

        when(master.getBoard()).thenReturn(board);

        TestFactory.setMaster(master);

        when(board.backupRobot(robot1)).thenReturn(respawn);

        Point point = new Point(2, 2);
        respawn = new GameTile(GAME_ID, point, wall);
        robot1 = new GameRobot(GAME_ID, respawn, Orientation.NORTH, "test");
        card1 = new GameMoveBackwardCard(GAME_ID, 10);
        card2 = new GameMoveBackwardCard(GAME_ID, 20);
    }

    @Test
    public void testExecute() {
        // testet ob sich der Roboter richtig bewegt, wenn execute aufegrufen
        // wird
        Point start = player.getRobot().getCurrentTile().getCoordinates();
        start.setLocation(start.getX(), start.getY() - 1);
        card1.execute(robot1);
        assertTrue(player.getRobot().getCurrentTile().getCoordinates() == start);

        start.setLocation(start.getX(), start.getY() - 1);
        card1.execute(robot1);
        assertTrue(player.getRobot().getCurrentTile().getCoordinates() == start);

    }

    @Test
    public void testGetPriority() {
        // testet ob die richtige Priorität zurückgegeben wird
        assertTrue(card1.getPriority() == 10);
        assertFalse(card2.getPriority() == 10);
    }

    @Test
    public void testGetRange() {
        // testet ob die richtige Range zurückgegeben wird
        assertTrue(card1.getRange() == -1);

    }

}
