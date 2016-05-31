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
import de.uni_bremen.factroytrouble.ai.ki2.ai.memory.ScullyLongTermMemoryHelper;
import de.uni_bremen.factroytrouble.ai.ki2.ai.memory.ScullyLongTermMemoryStorer;

@RunWith(MockitoJUnitRunner.class)
public class ScullyLongTermMemoryStorerTest {

    @Mock
    Thought thought2, miscCategory;

    @Mock
    private ScullyLongTermMemory ltm;
    @Mock
    private ScullyLongTermMemoryHelper ltmh;

    private ScullyLongTermMemoryStorer ltms;

    @Before
    public void setUp() throws Exception {
        when(thought2.getThoughtName()).thenReturn("test_2");
        when(miscCategory.getThoughtName()).thenReturn("miscCategory");
        when(ltm.getHelper()).thenReturn(ltmh);
        ltms = new ScullyLongTermMemoryStorer(ltm);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void shouldStoreNothingWhenGivenNull() {
        ltms.storeInMemory(null);
        Mockito.verify(ltmh, Mockito.never()).findToughtsToConnectTo(Mockito.any());
    }

    @Test
    public void shouldStoreMiscInformationInMiscIfNotAlreadyConnected() {
        List<Thought> conns = new ArrayList<>();
        conns.add(miscCategory);
        when(ltmh.findToughtsToConnectTo(thought2)).thenReturn(conns);
        List<String> l = new ArrayList<String>();
        l.add("miscCategory");
        ltms.storeInMemory(thought2);
        Mockito.verify(thought2).connectThoughts(miscCategory);
        Mockito.verify(miscCategory).connectThoughts(thought2);
    }

    @Test
    public void shouldPowerCooectionWhenAlreadyConnected() {
        when(thought2.hasConnectionToName("miscCategory")).thenReturn(true);
        when(miscCategory.hasConnectionToName("test_2")).thenReturn(true);
        List<Thought> conns = new ArrayList<>();
        conns.add(miscCategory);
        when(ltmh.findToughtsToConnectTo(thought2)).thenReturn(conns);
        List<String> l = new ArrayList<String>();
        l.add("miscCategory");
        ltms.storeInMemory(thought2);
        Mockito.verify(thought2).power(miscCategory, 1);
        Mockito.verify(miscCategory).power(thought2, 1);
    }

}
