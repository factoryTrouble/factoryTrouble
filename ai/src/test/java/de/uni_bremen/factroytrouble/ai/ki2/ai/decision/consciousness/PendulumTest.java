package de.uni_bremen.factroytrouble.ai.ki2.ai.decision.consciousness;

import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import de.uni_bremen.factroytrouble.ai.ais.AIPlayer2;
import de.uni_bremen.factroytrouble.ai.ki2.api.decision.unconsciousness.Personality;
import de.uni_bremen.factroytrouble.gameobjects.Robot;

@RunWith(MockitoJUnitRunner.class)
public class PendulumTest {

    @Mock
    private AIPlayer2 ai;
    @Mock
    private DecisionFlow flow;
    @Mock
    private Personality person;
    @Mock
    private Robot robot;
    @Mock
    private Condition condition;

    private Pendulum pendulum;

    @Before
    public void setUp() {
        pendulum = new Pendulum(flow, ai);
        when(ai.getPersonality()).thenReturn(person);
        when(ai.getRobot()).thenReturn(robot);
        when(person.getSensitivity()).thenReturn(5);
        when(flow.getCondition()).thenReturn(condition);
        when(condition.getHealth()).thenReturn(10);
        when(condition.getLives()).thenReturn(3);
        when(robot.getHP()).thenReturn(10);
        when(robot.getLives()).thenReturn(3);
    }

    @Test
    public void startDownNotIntoMemory() {
        pendulum.fall();
        pendulum.start();
        Mockito.verify(flow, Mockito.never()).intoMemory();
    }

    @Test
    public void startHighHP() {
        when(condition.getHealth()).thenReturn(10);
        when(condition.getLives()).thenReturn(3);
        when(robot.getHP()).thenReturn(10);
        when(robot.getLives()).thenReturn(3);

        pendulum.fall();
        pendulum.start();
        Mockito.verify(flow, Mockito.never()).intoMemory();
    }

    @Test
    public void startLowHP() {
        when(condition.getHealth()).thenReturn(10);
        when(condition.getLives()).thenReturn(3);
        when(robot.getHP()).thenReturn(1);
        when(robot.getLives()).thenReturn(1);

        pendulum.fall();
        pendulum.start();
        Mockito.verify(flow, Mockito.never()).intoMemory();
    }

}
