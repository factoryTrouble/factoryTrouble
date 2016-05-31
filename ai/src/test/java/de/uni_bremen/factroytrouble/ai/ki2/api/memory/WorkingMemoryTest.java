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

import de.uni_bremen.factroytrouble.ai.ki2.ai.memory.ScullyWorkingMemory;

@RunWith(MockitoJUnitRunner.class)
public class WorkingMemoryTest {

    private WorkingMemory wm;

    @Mock
    private ShortTermMemory stm;

    @Mock
    private LongTermMemory ltm;
    
    @Mock
    private SensoryMemory sm;
    
   
    @Mock
    private Thought thought1, thought2;

    @Before
    public void setUp() throws Exception {
        wm = new ScullyWorkingMemory(sm, stm, ltm);
        
        
    }

    @After
    public void tearDown() throws Exception {
    }

    
    @Test
    public void shouldGetThoughtFromShortTermMemory(){
        List<String> tempList = new ArrayList<>();
        List<Object> objectListForThought = new ArrayList<>();
        objectListForThought.add(tempList);
        tempList.add("thought1");
        Mockito.when(stm.getInformation(tempList)).thenReturn(thought1);
        Mockito.when(thought1.getInformation()).thenReturn(objectListForThought);
        assertEquals(thought1, wm.getInformation(tempList));
    }
    
    @Test
    public void shouldGetThoughtFromLongTermMemory(){
        List<String> tempList = new ArrayList<>();
        List<Object> objectListForThought = new ArrayList<>();
        objectListForThought.add(tempList);
        tempList.add("thought1");
        Mockito.when(stm.getInformation(tempList)).thenReturn(null);
        Mockito.when(ltm.getInformation(tempList)).thenReturn(thought1);
        Mockito.when(thought1.getInformation()).thenReturn(objectListForThought);
        assertEquals(thought1, wm.getInformation(tempList));
    }
    
   
    
  

    @Test
    public void shouldGetThoughtFromSensoryMemory(){
        List<String> tempList = new ArrayList<>();
        List<Object> objectListForThought = new ArrayList<>();
        objectListForThought.add(tempList);
        tempList.add("thought1");
        Mockito.when(stm.getInformation(tempList)).thenReturn(null);
        Mockito.when(ltm.getInformation(tempList)).thenReturn(null);
        Mockito.when(sm.getInformation(tempList)).thenReturn(thought1);
        Mockito.when(thought1.getInformation()).thenReturn(objectListForThought);
        assertEquals(thought1, wm.getInformation(tempList));
    }
    

   
  
   

    
}
