package de.uni_bremen.factroytrouble.ai.ki2.api.memory;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import de.uni_bremen.factroytrouble.ai.ki2.ai.memory.ScullyShortTermMemory;

@RunWith(MockitoJUnitRunner.class)
public class ShortTermMemoryTest {

    private ShortTermMemory stm;
    @Mock
    private Thought thought1, thought2, thought3, thought4, thought5, thought6, thought7, thought8;

    @Before
    public void setUp() throws Exception {
        stm = new ScullyShortTermMemory();
        Mockito.when(thought1.getThoughtName()).thenReturn("flag_1");
        Mockito.when(thought2.getThoughtName()).thenReturn("flag_2");
        Mockito.when(thought3.getThoughtName()).thenReturn("THOUGHT_3");
        Mockito.when(thought4.getThoughtName()).thenReturn("THOUGHT_4");
        Mockito.when(thought5.getThoughtName()).thenReturn("THOUGHT_5");
        Mockito.when(thought6.getThoughtName()).thenReturn("THOUGHT_6");
        Mockito.when(thought7.getThoughtName()).thenReturn("THOUGHT_7");
        Mockito.when(thought8.getThoughtName()).thenReturn("THOUGHT_8");
    }

    @After
    public void tearDown() throws Exception {
        stm = new ScullyShortTermMemory();
    }

    @Test
    public void shouldbeNullAskedInformationIsNull() {

        List<String> l = new ArrayList<String>();
        stm.storeInMemory(thought1);
        assertEquals(null, stm.getInformation(l));
    }

    @Test
    public void shouldStoreAndGetInformation() {

        List<String> l = new ArrayList<String>();
        l.add("flag");
        l.add("1");
        stm.storeInMemory(thought1);
        assertEquals(thought1, stm.getInformation(l));
    }

    @Test
    public void shouldStoreAndGetInformationTwoFlags() {

        List<String> l = new ArrayList<String>();
        l.add("flag");
        l.add("2");

        stm.storeInMemory(thought1);
        stm.storeInMemory(thought2);
        assertEquals(thought2, stm.getInformation(l));
    }

    @Test
    public void shouldGetNoInformationMemEmpty() {
        List<String> l = new ArrayList<String>();
        l.add("flag_42");
        assertEquals(null, stm.getInformation(l));
    }

    @Test
    public void shouldGetNoInformationBecauseNotInMem() {
        List<String> l = new ArrayList<String>();
        l.add("ShouldBeNotInMem");
        stm.storeInMemory(thought1);
        stm.storeInMemory(thought2);
        assertEquals(null, stm.getInformation(l));
    }

    @Test
    public void shouldGetNoInformationBecauseKeysIsEmpty() {
        List<String> l = new ArrayList<String>();
        stm.storeInMemory(thought1);
        stm.storeInMemory(thought2);
        assertEquals(null, stm.getInformation(l));
    }

    @Test
    public void thought1shouldBeNoMoreInMemory() {
        List<String> l = new ArrayList<String>();
        l.add("flag");
        l.add("1");
        stm.storeInMemory(thought1);
        stm.storeInMemory(thought2);
        stm.storeInMemory(thought3);
        stm.storeInMemory(thought4);
        stm.storeInMemory(thought5);
        stm.storeInMemory(thought6);
        stm.storeInMemory(thought7);
        stm.storeInMemory(thought8);
        assertEquals(null, stm.getInformation(l));

    }

    @Test
    public void shouldReplaceThought1WithThought8() {
        List<String> l = new ArrayList<String>();
        l.add("THOUGHT");
        l.add("8");
        stm.storeInMemory(thought1);
        stm.storeInMemory(thought2);
        stm.storeInMemory(thought3);
        stm.storeInMemory(thought4);
        stm.storeInMemory(thought5);
        stm.storeInMemory(thought6);
        stm.storeInMemory(thought7);
        stm.storeInMemory(thought8);
        assertEquals(thought8, stm.getInformation(l));

    }

}
