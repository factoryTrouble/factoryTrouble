package de.uni_bremen.factroytrouble.ai.ki2.api.memory;


import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

import de.uni_bremen.factroytrouble.ai.ki2.ai.memory.ScullyLongTermMemory;

@RunWith(MockitoJUnitRunner.class)
public class LongTermMemoryTest {

    private ScullyLongTermMemory ltm;
    @Mock
    private Thought thought1, thought2, thought3, thought4;
    @Mock
    private WorkingMemory wm;

    @Before
    public void setUp() throws Exception {
        ltm = new ScullyLongTermMemory(wm);
        when(wm.getThoughtFromShortTerm(Mockito.any())).thenReturn(null);
        when(thought1.getThoughtName()).thenReturn("flag_1");
        when(thought2.getThoughtName()).thenReturn("test_2");
        when(thought3.getThoughtName()).thenReturn("test_3");
        when(thought4.getThoughtName()).thenReturn("test");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void shouldStoreAndGetInformationWithListOfKeys() {
        List<String> l = new ArrayList<String>(); 
        l.add("flag");
        l.add("1");
        ltm.storeInMemory(thought1);
    }
}
