package de.uni_bremen.factroytrouble.player;

import de.uni_bremen.factroytrouble.board.Board;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Field;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GameMoveBackwardCardTest {
    private static final int GAME_ID = 0;

    @Mock
    private Robot robot;
    @Mock private GameMasterFactory gameMasterFactory;
    @Mock private Board board;
    @Mock private Master master;
    private GameMoveBackwardCard gameMoveBackwardCard;

    @Before
    public void setUp() throws Exception {
        gameMoveBackwardCard = new GameMoveBackwardCard(GAME_ID, 123);
        when(robot.getName()).thenReturn("Test");
        when(gameMasterFactory.getMaster(GAME_ID)).thenReturn(master);
        when(master.getBoard()).thenReturn(board);

        Field javaFxElementField = gameMoveBackwardCard.getClass().getDeclaredField("factory");
        javaFxElementField.setAccessible(true);
        javaFxElementField.set(gameMoveBackwardCard, gameMasterFactory);
    }

    @Test
    public void shouldMoveBackward() {
        gameMoveBackwardCard.execute(robot);
        verify(board).backupRobot(eq(robot));
    }

    @Test
    public void shouldBeEqualsWithAnotherClassWithTheSameProirityAndRange() {
        assertTrue(gameMoveBackwardCard.equals(new GameMoveBackwardCard(GAME_ID, 123)));
    }

}