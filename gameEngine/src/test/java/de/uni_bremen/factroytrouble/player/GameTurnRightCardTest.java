package de.uni_bremen.factroytrouble.player;

import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GameTurnRightCardTest {

    @Mock private Robot robot;
    private GameTurnRightCard gameTurnRightCard;

    @Before
    public void setUp() {
        gameTurnRightCard = new GameTurnRightCard(123);
        when(robot.getName()).thenReturn("Test");
        when(robot.getOrientation()).thenReturn(Orientation.NORTH);
    }

    @Test
    public void shouldExecuteTheCard() {
        gameTurnRightCard.execute(robot);
        verify(robot).turn(false);
    }

    @Test
    public void shouldBeEqualsWithAnotherClassWithTheSameProirity() {
        assertTrue(gameTurnRightCard.equals(new GameTurnRightCard(123)));
    }

}