package de.uni_bremen.factroytrouble.ai.ki2.api.memory;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import de.uni_bremen.factroytrouble.ai.ki2.ai.memory.ScullyThought;

@RunWith(MockitoJUnitRunner.class)
public class ThoughtTest {

	private Thought testThought;
	
	private Thought thought1, thought2, thought3;

	@Before
	public void setUp() throws Exception {
		testThought = new ScullyThought("test");
		thought1 = new ScullyThought("thought1");
		thought3 = new ScullyThought("thought1");
		thought2 = new ScullyThought("thought2");
	}

	@Test
	public void shouldConnectThoughts() {
		testThought.connectThoughts(thought2);
		assertTrue(testThought.getThoughts().containsKey(thought2));
	}

	@Test
	public void hasConnectionToThoughtReturnsTrueWhenHasConnection() {
		testThought.connectThoughts(thought2);
		assertTrue(testThought.hasConnectionToThought(thought2));
	}

	@Test
	public void hasConnectionToNameReturnsTrueWhenHasConnection() {
		testThought.connectThoughts(thought2);
		assertTrue(testThought.hasConnectionToName("thought2"));
	}

	@Test
	public void getConnectedThoughtByNameReturnsCorrectThoughtWhenGivenCorrectName() {
		testThought.connectThoughts(thought2);
		assertEquals(thought2, testThought.getConnectedThoughtByName("thought2"));
	}

	@Test
	public void shouldPowerConnection() {
		thought1.connectThoughts(thought2);
		thought2.connectThoughts(thought1);
		int startValue1 = thought1.getThoughts().get(thought2);
		int startValue2 = thought2.getThoughts().get(thought1);
		thought1.power(thought2, 2);
		int endValue1 = thought1.getThoughts().get(thought2);
		int endValue2 = thought2.getThoughts().get(thought1);

		assertEquals(startValue1 + 2, endValue1 & startValue2 + 2, endValue2);
	}

	@Test
	public void shouldWeakConnection() {
		thought1.connectThoughts(thought2);
		thought2.connectThoughts(thought1);
		thought1.power(thought2, 2);
		int startValue1 = thought1.getThoughts().get(thought2);
		int startValue2 = thought2.getThoughts().get(thought1);
		thought1.weak(thought2, 2);
		int endValue1 = thought1.getThoughts().get(thought2);
		int endValue2 = thought2.getThoughts().get(thought1);

		assertEquals(startValue1 - 2, endValue1 & startValue2 - 2, endValue2);

	}

	@Test
	public void shouldCutConnection() {
		thought1.connectThoughts(thought2);
		thought2.connectThoughts(thought1);
		thought1.power(thought2, 2);
		thought1.weak(thought2, thought1.getThoughts().get(thought2));

		assertEquals((Integer)0, thought1.getThoughts().get(thought2));


	}

	
	@Test
	public void shouldBeNotEqualNull(){
	    assertFalse(thought1.equals(null));
	}
	
	@Test
	public void thoughtsShouldBeEqual(){
	    assertTrue(thought1.equals(thought1));
	}
	
	@Test
    public void thoughtsShouldBeEqualSameName(){
	    
        assertTrue(thought1.equals(thought3));
    }

	
	@Test
    public void thoughtsShouldBeNotEqualDifferentName(){
        
        assertFalse(thought1.equals(thought2));
    }
	
	@Test
    public void thoughtsShouldBeNotEqualDifferentInfo(){
        thought1.addInformationToThought("Test");
        thought3.addInformationToThought("Test2");
        assertFalse(thought1.equals(thought3));
    }

	@Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentNameNull(){
        Thought thought = new ScullyThought(null);
    }
}
