package de.uni_bremen.factroytrouble.player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.observer.GameObserver;

/**
 * 
 * 
 * @author Lukas
 */
@RunWith(MockitoJUnitRunner.class)
public class GamePlayerTest {

    @Mock
    private Robot robot;
    @Mock
    private UturnCard uTurn;
    @Mock
    private GameObserver observer;
    @Mock
    ProgramCard card1, card2, card3, card4, card5;

    private GamePlayer player;
    List<ProgramCard> cards;

    @Before
    public void setUp() throws Exception {

        player = new GamePlayer(robot);
        cards = new ArrayList<ProgramCard>();

    }

    @Test
    public void testGiveCards() throws Exception {
        cards.add(uTurn);
        player.giveCards(cards);
        assertEquals(player.getPlayerCards(), cards);
    }

    @Test
    public void testDiscardCards() throws Exception {
        cards.add(uTurn);
        player.giveCards(cards);
        player.discardCards();
        assertTrue(player.getPlayerCards().isEmpty());
    }

    @Test
    public void testFinishTurn() throws Exception {
        /*cards.add(uTurn);
        player.giveCards(cards);*/
        player.finishTurn();
        //assertTrue(player.getPlayerCards().isEmpty());
        assertTrue(player.isDone());
    }

    @Test
    public void testIsDone() throws Exception {
        //cards.add(uTurn);
        //player.giveCards(cards);
        assertFalse(player.isDone());
        //player.discardCards();
        player.finishTurn();
        assertTrue(player.isDone());
    }

    @Test
    public void testGetRobot() throws Exception {
        assertEquals(player.getRobot(), robot);
    }

    @Test
    public void testGetPlayerCards() throws Exception {
        cards.add(uTurn);
        player.giveCards(cards);
        assertEquals(player.getPlayerCards(), cards);
    }

    @Test
    public void fillRegisterTest() {
        ProgramCard[] cards = { card1, null, card3, null, card5 };
        boolean[] locked = { true, false, true, true, false };

        List<ProgramCard> handCards = new ArrayList<>();
        handCards.add(card1);
        handCards.add(card4);
        handCards.add(uTurn);
        handCards.add(card3);
        handCards.add(card2);
        handCards.add(card5);
        player.giveCards(handCards);

        when(robot.getRegisters()).thenReturn(cards);
        when(robot.registerLockStatus()).thenReturn(locked);

        ProgramCard card = player.fillRegister(3, card5);
        assertEquals(null, card);
        assertTrue(player.getPlayerCards().contains(card1));
        assertTrue(player.getPlayerCards().contains(card2));
        assertTrue(player.getPlayerCards().contains(card3));
        assertTrue(player.getPlayerCards().contains(card4));
        assertTrue(player.getPlayerCards().contains(uTurn));
        assertFalse(player.getPlayerCards().contains(card5));
        
        handCards.add(card5);
        card = player.fillRegister(4, card3);
        assertEquals(card5, card);
        assertTrue(player.getPlayerCards().contains(card1));
        assertTrue(player.getPlayerCards().contains(card2));
        assertTrue(player.getPlayerCards().contains(card4));
        assertTrue(player.getPlayerCards().contains(card5));
        assertTrue(player.getPlayerCards().contains(uTurn));
        assertFalse(player.getPlayerCards().contains(card3));
        
        card = player.fillRegister(0, card4);
        assertEquals(card4, card);
        assertTrue(player.getPlayerCards().contains(card1));
        assertTrue(player.getPlayerCards().contains(card2));
        assertTrue(player.getPlayerCards().contains(card4));
        assertTrue(player.getPlayerCards().contains(card5));
        assertTrue(player.getPlayerCards().contains(uTurn));
        assertFalse(player.getPlayerCards().contains(card3));
    }
    
    @Test
    public void fillEmptyRegistersTest() {
        player.fillEmptyRegisters();
    }

}
