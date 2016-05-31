package de.uni_bremen.factroytrouble.ai.ki2.ai.decision.unconsciousness;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import de.uni_bremen.factroytrouble.ai.ais.AIPlayer2;
import de.uni_bremen.factroytrouble.ai.ki2.api.decision.consciousness.Approach;
import de.uni_bremen.factroytrouble.ai.ki2.api.decision.unconsciousness.UnconsciousnessUnit;
import de.uni_bremen.factroytrouble.gameobjects.Robot;

@RunWith(MockitoJUnitRunner.class)
public class ScullyUnconsciousnessUnitTest {

    UnconsciousnessUnit uncon;

    @Mock
    Robot robot;

    @Mock
    Approach approach;

    @Mock
    ScullyPersonality personality;

    @Mock
    AIPlayer2 player;

    @Before
    public void setup() {
        when(robot.getHP()).thenReturn(10);
        uncon = new ScullyUnconsciousnessUnit(robot, player, personality, 20);
        when(robot.getHP()).thenReturn(2);
        when(personality.getAnxiety()).thenReturn(10);
    }

    @Test
    public void changeMood() { // Test nicht vollständig!
        uncon.changeMood(approach, true);
        assertEquals(5, uncon.getMood().getAggressivity());
    }

}