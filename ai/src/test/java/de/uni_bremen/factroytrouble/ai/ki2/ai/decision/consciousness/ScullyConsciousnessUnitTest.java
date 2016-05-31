package de.uni_bremen.factroytrouble.ai.ki2.ai.decision.consciousness;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import de.uni_bremen.factroytrouble.ai.ais.AIPlayer2;
import de.uni_bremen.factroytrouble.ai.ki2.ai.decision.unconsciousness.ScullyPersonality;
import de.uni_bremen.factroytrouble.ai.ki2.api.decision.consciousness.Approach;
import de.uni_bremen.factroytrouble.ai.ki2.api.decision.consciousness.ConsciousnessUnit;
import de.uni_bremen.factroytrouble.ai.ki2.api.decision.unconsciousness.MoodUnit;
import de.uni_bremen.factroytrouble.ai.ki2.api.decision.unconsciousness.UnconsciousnessUnit;
import de.uni_bremen.factroytrouble.ai.ki2.api.memory.WorkingMemory;
import de.uni_bremen.factroytrouble.gameobjects.Robot;

@RunWith(MockitoJUnitRunner.class)
public class ScullyConsciousnessUnitTest {

    private ConsciousnessUnit consUnit;

    @Mock
    private WorkingMemory wm;
    @Mock
    private AIPlayer2 ai2;
    @Mock
    private MoodUnit mood;
    @Mock
    private ScullyPersonality personality;
    @Mock
    private Robot robot;
    @Mock
    private Approach approach;
    @Mock
    private Aim aim;
    @Mock
    private UnconsciousnessUnit unCons;
    @Mock
    private DecisionFlow flow;

    @Before
    public void setup() {
        // flow = new DecisionFlow(wm);
        Mockito.when(ai2.getRobot()).thenReturn(robot);
        Mockito.when(ai2.getPersonality()).thenReturn(personality);
        Mockito.when(personality.getSensitivity()).thenReturn(5);
        Mockito.when(personality.getFlurries()).thenReturn(10);
        Mockito.when(robot.getHP()).thenReturn(10);
        Mockito.when(ai2.getUnconsc()).thenReturn(unCons);
        Mockito.when(unCons.getMood()).thenReturn(mood);
        Mockito.when(mood.getFlurry()).thenReturn(10);

        consUnit = new ScullyConsciousnessUnit(wm, ai2, mood, personality);
    }

    @Test
    public void shouldDecideForApproach() {
        Mockito.when(mood.getFlurry()).thenReturn(10);
        Mockito.when(mood.getAnxiety()).thenReturn(10);
        Mockito.when(approach.getAmountOfNearEnemies()).thenReturn(0);
        Mockito.when(aim.getMaxEnemies()).thenReturn(3);
        assertTrue(consUnit.decide(approach, 3));
    }

    @Test
    public void shouldNotDecideForApproachToFlurry() {
        Mockito.when(mood.getFlurry()).thenReturn(1);
        Mockito.when(mood.getAnxiety()).thenReturn(10);
        Mockito.when(approach.getAmountOfNearEnemies()).thenReturn(0);
        Mockito.when(aim.getMaxEnemies()).thenReturn(3);
        assertFalse(consUnit.decide(approach, 3));
    }

    @Test
    public void shouldNotDecideForApproachToAnxiety() {
        Mockito.when(mood.getFlurry()).thenReturn(10);
        Mockito.when(mood.getAnxiety()).thenReturn(1);
        Mockito.when(approach.getAmountOfNearEnemies()).thenReturn(0);
        Mockito.when(aim.getMaxEnemies()).thenReturn(3);
        assertFalse(consUnit.decide(approach, 3));
    }

    @Test
    public void shouldNotDecideForApproachNoSuitable() {
        Mockito.when(mood.getFlurry()).thenReturn(10);
        Mockito.when(mood.getAnxiety()).thenReturn(10);
        Mockito.when(approach.getAmountOfNearEnemies()).thenReturn(300);
        Mockito.when(aim.getMaxEnemies()).thenReturn(3);
        assertFalse(consUnit.decide(approach, 3));
    }

    @Test
    public void shouldBeSuitingApproach() {
        Mockito.when(approach.getAmountOfNearEnemies()).thenReturn(2);
        Mockito.when(approach.getAmountOfHoles()).thenReturn(2);
        Mockito.when(approach.getAmountOfLasers()).thenReturn(2);
        Mockito.when(approach.getAmountOfConveyer()).thenReturn(2);
        Mockito.when(approach.getAmountOfNoticedTiles()).thenReturn(10);
        Mockito.when(aim.getMaxTiles()).thenReturn(10);
        Mockito.when(aim.getMaxConveyor()).thenReturn(2);
        Mockito.when(aim.getMaxLasers()).thenReturn(2);
        Mockito.when(aim.getMaxHoles()).thenReturn(2);
        Mockito.when(aim.getMaxEnemies()).thenReturn(2);

        assertTrue(consUnit.isSuitingApproach(approach, aim));
    }

    @Test
    public void shouldBeNotSuitingApproachToManyEnemies() {
        Mockito.when(approach.getAmountOfNearEnemies()).thenReturn(100);
        Mockito.when(approach.getAmountOfHoles()).thenReturn(2);
        Mockito.when(approach.getAmountOfLasers()).thenReturn(2);
        Mockito.when(approach.getAmountOfConveyer()).thenReturn(2);
        Mockito.when(approach.getAmountOfNoticedTiles()).thenReturn(10);
        Mockito.when(aim.getMaxTiles()).thenReturn(10);
        Mockito.when(aim.getMaxConveyor()).thenReturn(2);
        Mockito.when(aim.getMaxLasers()).thenReturn(2);
        Mockito.when(aim.getMaxHoles()).thenReturn(2);
        Mockito.when(aim.getMaxEnemies()).thenReturn(2);

        assertFalse(consUnit.isSuitingApproach(approach, aim));
    }

    @Test
    public void shouldBeNotSuitingApproachToManyHoles() {
        Mockito.when(approach.getAmountOfNearEnemies()).thenReturn(2);
        Mockito.when(approach.getAmountOfHoles()).thenReturn(100);
        Mockito.when(approach.getAmountOfLasers()).thenReturn(2);
        Mockito.when(approach.getAmountOfConveyer()).thenReturn(2);
        Mockito.when(approach.getAmountOfNoticedTiles()).thenReturn(10);
        Mockito.when(aim.getMaxTiles()).thenReturn(10);
        Mockito.when(aim.getMaxConveyor()).thenReturn(2);
        Mockito.when(aim.getMaxLasers()).thenReturn(2);
        Mockito.when(aim.getMaxHoles()).thenReturn(2);
        Mockito.when(aim.getMaxEnemies()).thenReturn(2);

        assertFalse(consUnit.isSuitingApproach(approach, aim));
    }

    @Test
    public void shouldBeNotSuitingApproachToManyLasers() {
        Mockito.when(approach.getAmountOfNearEnemies()).thenReturn(2);
        Mockito.when(approach.getAmountOfHoles()).thenReturn(2);
        Mockito.when(approach.getAmountOfLasers()).thenReturn(100);
        Mockito.when(approach.getAmountOfConveyer()).thenReturn(2);
        Mockito.when(approach.getAmountOfNoticedTiles()).thenReturn(10);
        Mockito.when(aim.getMaxTiles()).thenReturn(10);
        Mockito.when(aim.getMaxConveyor()).thenReturn(2);
        Mockito.when(aim.getMaxLasers()).thenReturn(2);
        Mockito.when(aim.getMaxHoles()).thenReturn(2);
        Mockito.when(aim.getMaxEnemies()).thenReturn(2);

        assertFalse(consUnit.isSuitingApproach(approach, aim));
    }

    @Test
    public void shouldBeNotSuitingApproachToManyConveyor() {
        Mockito.when(approach.getAmountOfNearEnemies()).thenReturn(2);
        Mockito.when(approach.getAmountOfHoles()).thenReturn(2);
        Mockito.when(approach.getAmountOfLasers()).thenReturn(2);
        Mockito.when(approach.getAmountOfConveyer()).thenReturn(100);
        Mockito.when(approach.getAmountOfNoticedTiles()).thenReturn(10);
        Mockito.when(aim.getMaxTiles()).thenReturn(10);
        Mockito.when(aim.getMaxConveyor()).thenReturn(2);
        Mockito.when(aim.getMaxLasers()).thenReturn(2);
        Mockito.when(aim.getMaxHoles()).thenReturn(2);
        Mockito.when(aim.getMaxEnemies()).thenReturn(2);

        assertFalse(consUnit.isSuitingApproach(approach, aim));
    }

    @Test
    public void shouldBeNotSuitingApproachToManyTiles() {
        Mockito.when(approach.getAmountOfNearEnemies()).thenReturn(2);
        Mockito.when(approach.getAmountOfHoles()).thenReturn(2);
        Mockito.when(approach.getAmountOfLasers()).thenReturn(2);
        Mockito.when(approach.getAmountOfConveyer()).thenReturn(2);
        Mockito.when(approach.getAmountOfNoticedTiles()).thenReturn(100);
        Mockito.when(aim.getMaxTiles()).thenReturn(10);
        Mockito.when(aim.getMaxConveyor()).thenReturn(2);
        Mockito.when(aim.getMaxLasers()).thenReturn(2);
        Mockito.when(aim.getMaxHoles()).thenReturn(2);
        Mockito.when(aim.getMaxEnemies()).thenReturn(2);

        assertFalse(consUnit.isSuitingApproach(approach, aim));
    }

    @Test
    public void influanceBaseShouldBeNotNull() {
        assertNotNull(consUnit.getInfluence());
    }

    @Test
    public void shouldCreateStrategy() {
        assertNotNull(consUnit.decideForStrategy());
    }

    @Test
    public void shouldMakeCompromiss() {
        Aim aim = new Aim();
        consUnit.makeCompromiss(aim);
        assertEquals(0, aim.getMaxConveyor());
        assertEquals(0, aim.getMaxDamage());
        assertEquals(0, aim.getMaxEnemies());
        assertEquals(0, aim.getMaxHoles());
        assertEquals(0, aim.getMaxLasers());
        assertEquals(0, aim.getMaxTiles());

    }

}