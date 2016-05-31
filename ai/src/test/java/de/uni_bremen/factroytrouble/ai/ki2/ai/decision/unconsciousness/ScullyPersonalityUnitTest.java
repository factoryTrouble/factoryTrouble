package de.uni_bremen.factroytrouble.ai.ki2.ai.decision.unconsciousness;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import de.uni_bremen.factroytrouble.player.Master;

@RunWith(MockitoJUnitRunner.class)
public class ScullyPersonalityUnitTest {

    ScullyPersonality person;
    ScullyPersonality personDefault1, personDefault2, personDefault3, personDefault4, personDefault5, personDefault6,
            personDefault7, personDefault8;

    @Before
    public void setup() {
        person = new ScullyPersonality(Master.ROBOT_NAMES.get(0));
        personDefault1 = new ScullyPersonality("Default");
        personDefault2 = new ScullyPersonality(Master.ROBOT_NAMES.get(1));
        personDefault3 = new ScullyPersonality(Master.ROBOT_NAMES.get(2));
        personDefault4 = new ScullyPersonality(Master.ROBOT_NAMES.get(3));
        personDefault5 = new ScullyPersonality(Master.ROBOT_NAMES.get(4));
        personDefault6 = new ScullyPersonality(Master.ROBOT_NAMES.get(5));
        personDefault7 = new ScullyPersonality(Master.ROBOT_NAMES.get(6));
        personDefault8 = new ScullyPersonality(Master.ROBOT_NAMES.get(7));
    }

    @Test
    public void getAnxiety() {
        assertEquals(personDefault6.getAnxiety(), 10);
    }

    @Test
    public void getAggression() {
        assertEquals(personDefault6.getAggression(), 1);
    }

    @Test
    public void getGloomy() {
        assertEquals(personDefault6.getGloomy(), 10);
    }

    @Test
    public void getLazy() {
        assertEquals(personDefault6.getLazy(), 1);
    }

    @Test
    public void getFlurry() {
        assertEquals(personDefault6.getFlurries(), 10);
    }

}