package de.uni_bremen.factroytrouble.player;

import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GameTurnLeftCardTest {

    @Mock private Robot robot;
    private GameTurnLeftCard gameTurnLeftCard;

    @Before
    public void setUp() {
        gameTurnLeftCard = new GameTurnLeftCard(123);
        when(robot.getName()).thenReturn("Test");
        when(robot.getOrientation()).thenReturn(Orientation.NORTH);
    }

    @Test
    public void shouldExecuteTheCard() {
        gameTurnLeftCard.execute(robot);
        verify(robot).turn(true);
    }

    @Test
    public void shouldBeEqualsWithAnotherClassWithTheSameProirity() {
        assertTrue(gameTurnLeftCard.equals(new GameTurnLeftCard(123)));
    }

}