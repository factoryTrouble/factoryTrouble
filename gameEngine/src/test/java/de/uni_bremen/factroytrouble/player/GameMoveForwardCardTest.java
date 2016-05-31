package de.uni_bremen.factroytrouble.player;

import de.uni_bremen.factroytrouble.board.Board;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Field;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GameMoveForwardCardTest {
    private static final int GAME_ID = 0;

    @Mock private Robot robot;
    @Mock private GameMasterFactory gameMasterFactory;
    @Mock private Board board;
    @Mock private Master master;
    private GameMoveForwardCard gameMoveForwardCard;

    @Before
    public void setUp() throws Exception {
        gameMoveForwardCard = new GameMoveForwardCard(GAME_ID,123, 2);
        when(robot.getName()).thenReturn("Test");
        when(gameMasterFactory.getMaster(GAME_ID)).thenReturn(master);
        when(master.getBoard()).thenReturn(board);

        Field javaFxElementField = gameMoveForwardCard.getClass().getDeclaredField("factory");
        javaFxElementField.setAccessible(true);
        javaFxElementField.set(gameMoveForwardCard, gameMasterFactory);
    }

    @Test
    public void shouldMoveForward() {
        gameMoveForwardCard.execute(robot);
        verify(board, times(2)).moveRobot(eq(robot));
    }

    @Test
    public void shouldBeEqualsWithAnotherClassWithTheSameProirityAndRange() {
        assertTrue(gameMoveForwardCard.equals(new GameMoveForwardCard(GAME_ID,123, 2)));
    }

    @Test
    public void shouldNotBeEqualsWithAnotherClassWithTheSameProirityAndDiffernetRange() {
        assertFalse(gameMoveForwardCard.equals(new GameMoveForwardCard(GAME_ID,123, 1)));
    }

    @Test
    public void shouldNotBeEqualsWithAnotherClassWithTheSameRangeAndDiffernetProirity() {
        assertFalse(gameMoveForwardCard.equals(new GameMoveForwardCard(GAME_ID,122, 2)));
    }

}