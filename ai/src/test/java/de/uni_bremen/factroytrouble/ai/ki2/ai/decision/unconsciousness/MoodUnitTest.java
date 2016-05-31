package de.uni_bremen.factroytrouble.ai.ki2.ai.decision.unconsciousness;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import de.uni_bremen.factroytrouble.ai.ki2.api.decision.consciousness.Approach;
import de.uni_bremen.factroytrouble.gameobjects.Robot;

@RunWith(MockitoJUnitRunner.class)
public class MoodUnitTest {

    ScullyMoodUnit mood;
    Random rand;

    @Mock
    Robot robot;

    @Mock
    Approach approach;

    ScullyPersonality personality;

    @Before
    public void setUp() throws Exception {
        rand = new Random(1);
        when(robot.getHP()).thenReturn(10);
        personality = new ScullyPersonality("FLINCH BOT");
        when(robot.getName()).thenReturn("FLINCH BOT");
        mood = new ScullyMoodUnit(robot, personality, rand);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void agression() {
        mood.setAggressivity(-100);
        assertEquals(1, mood.getAggressivity());
        mood.setAggressivity(100);
        assertEquals(10, mood.getAggressivity());
        mood.setAggressivity(8);
        assertEquals(8, mood.getAggressivity());
    }

    @Test
    public void anexity() {
        mood.setAnxiety(-100);
        assertEquals(1, mood.getAnxiety());
        mood.setAnxiety(100);
        assertEquals(10, mood.getAnxiety());
        mood.setAnxiety(8);
        assertEquals(8, mood.getAnxiety());
    }

    @Test
    public void lazzy() {
        mood.setLazyness(-100);
        assertEquals(1, mood.getLazyness());
        mood.setLazyness(100);
        assertEquals(10, mood.getLazyness());
        mood.setLazyness(8);
        assertEquals(8, mood.getLazyness());
    }

    @Test
    public void gloomy() {
        mood.setGloomyness(-100);
        assertEquals(1, mood.getGloomyness());
        mood.setGloomyness(100);
        assertEquals(10, mood.getGloomyness());
        mood.setGloomyness(8);
        assertEquals(8, mood.getGloomyness());
    }

    @Test
    public void flurry() {
        mood.setFlurry(-100);
        assertEquals(1, mood.getFlurry());
        mood.setFlurry(100);
        assertEquals(10, mood.getFlurry());
        mood.setFlurry(8);
        assertEquals(8, mood.getFlurry());
    }

    @Test
    public void updateMooodAgression() {
        when(approach.canReachNextFlag()).thenReturn(true);
        mood.updateMood(approach, true);
        assertEquals(5, mood.getAggressivity());
        mood.updateMood(approach, false);
        assertEquals(1, mood.getAggressivity());
    }

    @Test
    public void updateMooodGloomy() {
        when(approach.canReachNextFlag()).thenReturn(true);
        mood.updateMood(approach, true);
        assertEquals(5, mood.getGloomyness());
        mood.updateMood(approach, false);
        assertEquals(8, mood.getGloomyness());
    }
}
