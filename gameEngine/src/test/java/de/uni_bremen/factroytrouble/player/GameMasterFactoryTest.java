package de.uni_bremen.factroytrouble.player;

import de.uni_bremen.factroytrouble.board.Board;
import de.uni_bremen.factroytrouble.misc.TestFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GameMasterFactoryTest {
    private static final int GAME_ID = 0;

    @Mock
    Master master;

    @Mock
    Board board, boardClone;

    MasterFactory factory;

    @Before
    public void setUp() {
        when(master.getBoard()).thenReturn(board);
        when(board.clone()).thenReturn(boardClone);

        TestFactory.setMaster(master);
        factory = new GameMasterFactory();
    }

    @Test
    public void getMasterTest() {
        Master master1 = factory.getMaster(GAME_ID);
        Master master2 = factory.getMaster(GAME_ID);
        assertEquals(master1, master2);
    }

    @Test
    public void getBoardCloneTest() {
        Board board1 = factory.getMaster(GAME_ID).getBoard();
        Board board2 = factory.getBoardClone(GAME_ID);
        Board board3 = factory.getBoardClone(board2);
        assertFalse(board1 == board2);
        assertFalse(board1 == board3);
        assertFalse(board2 == board3);
    }

}
