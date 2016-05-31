package de.uni_bremen.factroytrouble.gameobjects;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import de.uni_bremen.factroytrouble.board.Tile;
import org.mockito.InjectMocks;

import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class GameFlagTest {

    @Mock private Tile tile;
    @Mock private Robot robot;
    @InjectMocks private GameFlag gameFlag = new GameFlag(1);

    @Before
    public void setUp() {
        when(robot.getName()).thenReturn("Test");
    }

    @Test
    public void shouldDoNotInteractWithTheRobotWhenRobotIstNull() {
        gameFlag.execute(tile);
        verify(robot, never()).setRespawnPoint();
    }

    @Test
    public void shouldSetRespawnPoint() {
        when(tile.getRobot()).thenReturn(robot);
        gameFlag.execute(tile);
        verify(robot).setRespawnPoint();
    }

    @Test
    public void shouldTouchFlag() {
        when(tile.getRobot()).thenReturn(robot);
        when(robot.getFlagCounterStatus()).thenReturn(0);
        gameFlag.execute(tile);
        verify(robot).touchFlag();
    }

}
