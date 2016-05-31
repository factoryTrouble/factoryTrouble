package de.uni_bremen.factroytrouble.player;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class DeckTest {
    private static final int GAME_ID = 0;

    private static final Logger DECKTEST_LOGGER = Logger.getLogger(DeckTest.class);

    private Deck testDeck;

    @Before
    public void setUp() {
        testDeck = new Deck(GAME_ID);
    }

    @Test
    public void trayEmptyAtStart() {
        assertEquals(0, testDeck.getTray().size());
    }

    @Test
    public void deckHasCorrectSize() {
        assertEquals(84, testDeck.getDeck().size());
    }

    @Test
    public void deckContainsEveryUturn() {
        for (int i = 10; i <= 60; i += 10) {
            ProgramCard u = new GameUturnCard(i);
            assertTrue(testDeck.getDeck().contains(u));
        }
    }

    @Test
    public void deckContainsEveryLeft() {
        for (int i = 70; i <= 410; i += 20) {
            ProgramCard u = new GameTurnLeftCard(i);
            assertTrue(testDeck.getDeck().contains(u));
        }
    }

    @Test
    public void deckContainsEveryRight() {
        for (int i = 80; i <= 420; i += 20) {
            ProgramCard u = new GameTurnRightCard(i);
            assertTrue(testDeck.getDeck().contains(u));
        }
    }

    @Test
    public void deckContainsEveryBack() {
        for (int i = 430; i <= 480; i += 10) {
            ProgramCard u = new GameMoveBackwardCard(GAME_ID, i);
            assertTrue(testDeck.getDeck().contains(u));
        }
    }

    @Test
    public void deckContainsEveryForward1() {
        for (int i = 490; i <= 660; i += 10) {
            ProgramCard u = new GameMoveForwardCard(GAME_ID, i, 1);
            assertTrue(testDeck.getDeck().contains(u));
        }
    }

    @Test
    public void deckContainsEveryForward2() {
        for (int i = 670; i <= 780; i += 10) {
            ProgramCard u = new GameMoveForwardCard(GAME_ID, i, 2);
            assertTrue(testDeck.getDeck().contains(u));
        }
    }

    @Test
    public void deckContainsEveryForward3() {
        for (int i = 790; i <= 840; i += 10) {
            ProgramCard u = new GameMoveForwardCard(GAME_ID, i, 3);
            assertTrue(testDeck.getDeck().contains(u));
        }
    }

    @Test
    public void noCardDouble() {
        List<ProgramCard> allCards = testDeck.getDeck();
        for (ProgramCard card : allCards) {
            assertTrue(allCards.lastIndexOf(card) == allCards.indexOf(card));
        }
    }

    @Test
    public void noPriorityDouble() {
        List<ProgramCard> allCards = new ArrayList<ProgramCard>();
        allCards.addAll(testDeck.getDeck());
        List<Integer> ints = new ArrayList<Integer>();
        for (ProgramCard card : allCards) {
            assertFalse(ints.contains(card.getPriority()));
            ints.add(card.getPriority());
        }
    }

    @Test
    public void noCardWithoutPriority() {

    }

    public List<ProgramCard> allCards() {
        List<ProgramCard> allCards = new ArrayList<ProgramCard>();
        return allCards;
    }

    @Test
    public void notNull84Times() {
        for (int i = 1; i <= 84; i++) {
            ProgramCard card = testDeck.dealCard();
            assertNotNull(card);
        }
    }

    @Test
    public void nullAt85thTime() {
        for (int i = 0; i < 84; i++) {
            testDeck.dealCard();
        }
        assertNull(testDeck.dealCard());
    }

    @Test
    public void cannotLayOffCardAlreadyInDeck() {
        final TestAppender appender = new TestAppender();
        Logger.getLogger(Deck.class).addAppender(appender);
        ProgramCard c = new GameUturnCard(10);
        testDeck.layOffCard(c);
        final List<LoggingEvent> log = appender.getLog();
        boolean hasError = false;
        for (LoggingEvent l : log) {
            if (l.getLevel().toString().equals("ERROR") && l.getMessage()
                    .equals("Es wurde eine Karte zurück in das Deck gelegt, die bereits im Deck ist. "
                            + "Dies sollte nicht passieren, da alle Karten einzigartig sind."
                            + "Die Karte wurde nicht angenommen.")) {
                hasError = true;
            }
        }
        assertTrue(hasError);
    }

    @Test
    public void cannotLayOffCardAlreadyInTray() {
        final TestAppender appender = new TestAppender();
        Logger.getLogger(Deck.class).addAppender(appender);
        ProgramCard c = new GameUturnCard(10);
        for (int i = 0; i < 84; i++) {
            ProgramCard u = testDeck.dealCard();
            testDeck.layOffCard(u);
        }
        testDeck.layOffCard(c);
        final List<LoggingEvent> log = appender.getLog();
        boolean hasError = false;
        for (LoggingEvent l : log) {
            if (l.getLevel().toString().equals("ERROR") && l.getMessage()
                    .equals("Es wurde eine Karte zurück in das Deck gelegt, die bereits im Deck ist. "
                            + "Dies sollte nicht passieren, da alle Karten einzigartig sind."
                            + "Die Karte wurde nicht angenommen.")) {
                hasError = true;
            }
        }
        assertTrue(hasError);
    }

    @Test
    public void canLayOfIfEveryCardOnce() {
        final TestAppender appender = new TestAppender();
        Logger.getLogger(Deck.class).addAppender(appender);
        for (int i = 0; i < 84; i++) {
            ProgramCard u = testDeck.dealCard();
            testDeck.layOffCard(u);
        }
        final List<LoggingEvent> log = appender.getLog();
        boolean hasError = false;
        for (LoggingEvent l : log) {
            if (l.getLevel().toString().equals("ERROR") && l.getMessage()
                    .equals("Es wurde eine Karte zurück in das Deck gelegt, die bereits im Deck ist. "
                            + "Dies sollte nicht passieren, da alle Karten einzigartig sind."
                            + "Die Karte wurde nicht angenommen.")) {
                hasError = true;
            }
        }
        assertFalse(hasError);
    }

    @Test
    public void canDrawCardWhenDeckEmptyAndTrayFull() {
        List<ProgramCard> cards = new ArrayList<ProgramCard>();
        for (int i = 0; i < 84; i++) {
            cards.add(testDeck.dealCard());
        }
        for (ProgramCard c : cards) {
            testDeck.layOffCard(c);
        }
        assertNotNull(testDeck.dealCard());
    }

    @Test
    public void nullCardIsError() {
        final TestAppender appender = new TestAppender();
        Logger.getLogger(Deck.class).addAppender(appender);
        testDeck.layOffCard(null);
        final List<LoggingEvent> log = appender.getLog();
        boolean hasError = false;
        for (LoggingEvent l : log) {
            if (l.getLevel().toString().equals("ERROR") && l.getMessage().equals("Karte ist null")) {
                hasError = true;
            }
        }
        assertTrue(hasError);
    }

    @After
    public void cleanUp() {
        DECKTEST_LOGGER.removeAllAppenders();
    }
}