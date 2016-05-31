package de.uni_bremen.factroytrouble.gui.generator.card;

import de.uni_bremen.factroytrouble.player.*;
import org.junit.Before;
import org.junit.Test;

import javax.imageio.ImageIO;

import static de.uni_bremen.factroytrouble.gui.TestUtil.assertImageEquals;
import static org.mockito.Mockito.*;

public class CardImageDispatcherTest {

    private CardImageDispatcher cardImageDispatcher;

    @Before
    public void setUp() {
        cardImageDispatcher = new CardImageDispatcher();
        cardImageDispatcher.init();
    }

    @Test
    public void shouldDispatchAMoveBackwardCard() throws Exception {
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/cards/back.png")), cardImageDispatcher.dispatch(mock(MoveBackwardCard.class)));
    }

    @Test
    public void shouldDispatchATurnLeftCard() throws Exception {
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/cards/left.png")), cardImageDispatcher.dispatch(mock(TurnLeftCard.class)));
    }

    @Test
    public void shouldDispatchATurnRightCard() throws Exception {
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/cards/right.png")), cardImageDispatcher.dispatch(mock(TurnRightCard.class)));
    }

    @Test
    public void shouldDispatchAnUTurnCard() throws Exception {
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/cards/u-turn.png")), cardImageDispatcher.dispatch(mock(UturnCard.class)));
    }

    @Test
    public void shouldDispatchAMoveOneCard() throws Exception {
        MoveForwardCard moveCard = mock(MoveForwardCard.class);
        when(moveCard.getRange()).thenReturn(1);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/cards/move_1.png")), cardImageDispatcher.dispatch(moveCard));
    }

    @Test
    public void shouldDispatchAMoveTwoCard() throws Exception {
        MoveForwardCard moveCard = mock(MoveForwardCard.class);
        when(moveCard.getRange()).thenReturn(2);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/cards/move_2.png")), cardImageDispatcher.dispatch(moveCard));
    }

    @Test
    public void shouldDispatchAMoveThreeCard() throws Exception {
        MoveForwardCard moveCard = mock(MoveForwardCard.class);
        when(moveCard.getRange()).thenReturn(3);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/cards/move_3.png")), cardImageDispatcher.dispatch(moveCard));
    }

    @Test
    public void shouldDispatchAnEmptyCardWhenRangeIsLessThenOne() throws Exception {
        MoveForwardCard moveCard = mock(MoveForwardCard.class);
        when(moveCard.getRange()).thenReturn(0);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/cards/empty.png")), cardImageDispatcher.dispatch(moveCard));
    }

    @Test
    public void shouldDispatchAnEmptyCardWhenRangeIsGreateThenThree() throws Exception {
        MoveForwardCard moveCard = mock(MoveForwardCard.class);
        when(moveCard.getRange()).thenReturn(4);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/cards/empty.png")), cardImageDispatcher.dispatch(moveCard));
    }

    @Test
    public void shouldDispatchAnEpmtyCardWhenCanNotDispatchProgrammCard() throws Exception {
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/cards/empty.png")), cardImageDispatcher.dispatch(mock(ProgramCard.class)));
    }

}