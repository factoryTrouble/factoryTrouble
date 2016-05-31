package de.uni_bremen.factroytrouble.player;

import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class GameUturnCardTest {

    @Mock private Robot robot;
    private GameUturnCard gameUturnCard;

    @Before
    public void setUp() {
        gameUturnCard = new GameUturnCard(123);
        when(robot.getName()).thenReturn("Test");
        when(robot.getOrientation()).thenReturn(Orientation.NORTH);
    }

    @Test
    public void shouldExecuteTheCard() {
        gameUturnCard.execute(robot);
        verify(robot, times(2)).turn(anyBoolean());
    }

    @Test
    public void shouldBeEqualsWithAnotherClassWithTheSameProirity() {
        assertTrue(gameUturnCard.equals(new GameUturnCard(123)));
    }

}