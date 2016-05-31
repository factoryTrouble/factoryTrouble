package de.uni_bremen.factroytrouble.ai.ki1;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import de.uni_bremen.factroytrouble.ai.ais.AIPlayer1;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.player.MoveBackwardCard;
import de.uni_bremen.factroytrouble.player.MoveForwardCard;
import de.uni_bremen.factroytrouble.player.TurnLeftCard;
import de.uni_bremen.factroytrouble.player.TurnRightCard;
import de.uni_bremen.factroytrouble.player.UturnCard;
import de.uni_bremen.factroytrouble.player.ProgramCard;

public class ResponseUnitTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    Robot robot;
    AIPlayer1 control;
    List<ProgramCard> cards;
    ResponseUnit ru;
    MoveForwardCard forward1;
    MoveForwardCard forward2;
    MoveForwardCard forward3;
    MoveBackwardCard backwards;
    TurnLeftCard turnLeft;
    TurnRightCard turnRight;
    UturnCard uturn;
    MoveForwardCard forward1_1;
    MoveForwardCard forward1_2;

    @Before
    public void setUp() throws Exception {
        cards = new ArrayList<ProgramCard>();
        robot = Mockito.mock(Robot.class);
        ru = new ResponseUnit(robot,control);
        forward1 = Mockito.mock(MoveForwardCard.class);
        forward2 = Mockito.mock(MoveForwardCard.class);
        forward3 = Mockito.mock(MoveForwardCard.class);
        backwards = Mockito.mock(MoveBackwardCard.class);
        turnLeft = Mockito.mock(TurnLeftCard.class);
        turnRight = Mockito.mock(TurnRightCard.class);
        uturn = Mockito.mock(UturnCard.class);
        forward1_1 = Mockito.mock(MoveForwardCard.class);
        forward1_2 = Mockito.mock(MoveForwardCard.class);
    }

    /**
     * Eine IllegalArgumentException sollte geworfen werden, da {@code null}
     * übergeben wurde.
     */
    @Test
    public void shouldThrowExceptionCardsAreNull() {
        cards = null;
        exception.expect(IllegalArgumentException.class);
        ru.setCards(cards);
    }

    @Test@Ignore
    public void shouldFillAllRegisters() {
        List<ProgramCard> cards = new ArrayList<ProgramCard>(Arrays.asList(forward1, forward2, forward3, backwards,
                turnLeft, turnRight, uturn, forward1_1, forward1_2));
        List<ProgramCard> expectedCards = new ArrayList<ProgramCard>(Arrays.asList(forward1, forward2, forward3,
                backwards, turnLeft, turnRight, uturn, forward1_1, forward1_2));
        cards.add(backwards);
        ru.setCards(cards);
        for (int i = 0; i < 9; i++) {
            robot.executeNext();
        }
        assertEquals(cards, expectedCards);
    }

    @Test
    public void shouldNotFillLockedRegister() {
        // TODO kann man machen, wenn man 100% Abdeckung möchte
    }
}
