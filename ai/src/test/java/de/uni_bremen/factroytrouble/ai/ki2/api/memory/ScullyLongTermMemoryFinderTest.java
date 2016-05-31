package de.uni_bremen.factroytrouble.ai.ki2.api.memory;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import de.uni_bremen.factroytrouble.ai.ki2.ai.memory.ScullyLongTermMemory;
import de.uni_bremen.factroytrouble.ai.ki2.ai.memory.ScullyLongTermMemoryFinder;
import de.uni_bremen.factroytrouble.ai.ki2.ai.memory.ScullyLongTermMemoryHelper;

@RunWith(MockitoJUnitRunner.class)
public class ScullyLongTermMemoryFinderTest {

    private ScullyLongTermMemoryFinder ltmf;

    @Mock
    private ScullyLongTermMemory ltm;
    @Mock
    private ScullyLongTermMemoryHelper ltmh;
    @Mock
    private Random rng;
    @Mock
    private Thought thought1, thought2, thought3;

    private HashMap<Thought, Integer> thought1Thoughts, thought2Thoughts, thought3Thoughts;

    private String name1 = "test";
    private String name2 = "test_1";
    private String name3 = "test_1_position";
    private String name4 = "NotTest_1";
    private String name5 = "NotAtest";
    private String nameRobotCategory = "robotCategory";

    @Before
    public void setUp() throws Exception {
        when(ltmh.correlatingStringFactor("test", "test")).thenReturn(100);
        when(ltmh.correlatingStringFactor("test", "test_1")).thenReturn(50);
        when(ltmh.correlatingStringFactor("test", "test_1_position")).thenReturn(30);
        when(ltmh.correlatingStringFactor("test", "NotTest_1")).thenReturn(0);
        when(ltmh.correlatingStringFactor("test", "NotAtest")).thenReturn(0);
        when(ltmh.correlatingStringFactor("test_1", "test")).thenReturn(50);
        when(ltmh.correlatingStringFactor("test_1", "test_1")).thenReturn(100);
        when(ltmh.correlatingStringFactor("test_1", "test_1_position")).thenReturn(60);
        when(ltmh.correlatingStringFactor("test_1", "NotTest_1")).thenReturn(50);
        when(ltmh.correlatingStringFactor("test_1", "NotAtest")).thenReturn(0);
        when(ltmh.correlatingStringFactor("test_1_position", "test")).thenReturn(30);
        when(ltmh.correlatingStringFactor("test_1_position", "test_1")).thenReturn(60);
        when(ltmh.correlatingStringFactor("test_1_position", "test_1_position")).thenReturn(100);
        when(ltmh.correlatingStringFactor("test_1_position", "NotTest_1")).thenReturn(30);
        when(ltmh.correlatingStringFactor("test_1_position", "NotAtest")).thenReturn(0);
        when(ltmh.correlatingStringFactor("NotTest_1", "test")).thenReturn(0);
        when(ltmh.correlatingStringFactor("NotTest_1", "test_1")).thenReturn(50);
        when(ltmh.correlatingStringFactor("NotTest_1", "test_1_position")).thenReturn(30);
        when(ltmh.correlatingStringFactor("NotTest_1", "NotTest_1")).thenReturn(100);
        when(ltmh.correlatingStringFactor("NotTest_1", "NotAtest")).thenReturn(0);
        when(ltmh.correlatingStringFactor("NotAtest", "test")).thenReturn(0);
        when(ltmh.correlatingStringFactor("NotAtest", "test_1")).thenReturn(0);
        when(ltmh.correlatingStringFactor("NotAtest", "test_1_position")).thenReturn(0);
        when(ltmh.correlatingStringFactor("NotAtest", "NotTest_1")).thenReturn(0);
        when(ltmh.correlatingStringFactor("NotAtest", "NotAtest")).thenReturn(0);
        when(ltmh.correlatingStringFactor(name1, "robot_test")).thenReturn(50);
        when(ltmh.correlatingStringFactor("robot_test", "robot_test")).thenReturn(100);
        when(ltmh.correlatingStringFactor("robot_test", name1)).thenReturn(50);
        when(ltmh.correlatingStringFactor(name1, nameRobotCategory)).thenReturn(30);
        when(ltmh.correlatingStringFactor("robot_test", nameRobotCategory)).thenReturn(30);
        when(ltmh.correlatingStringFactor(nameRobotCategory, name1)).thenReturn(30);
        when(ltmh.correlatingStringFactor(nameRobotCategory, "robot_test")).thenReturn(30);

        thought1Thoughts = new HashMap<>();
        thought2Thoughts = new HashMap<>();
        thought3Thoughts = new HashMap<>();
        when(thought1.getThoughts()).thenReturn(thought1Thoughts);
        when(thought2.getThoughts()).thenReturn(thought2Thoughts);
        when(thought3.getThoughts()).thenReturn(thought3Thoughts);

        when(ltm.getHelper()).thenReturn(ltmh);
        when(rng.nextInt(100)).thenReturn(0);
        when(ltm.getRNGesus()).thenReturn(rng);
        ltmf = new ScullyLongTermMemoryFinder(ltm);
    }

    @After
    public void tearDown() throws Exception {
    }
    
    // getInformation
    
    @Test
    public void canFindRobotNamedTest(){
        List<String> search = new ArrayList<>();
        search.add("robot");
        search.add("test");
        when(thought1.getThoughtName()).thenReturn(name1);
        when(thought2.getThoughtName()).thenReturn(nameRobotCategory);
        when(thought1.hasConnectionToName(name2)).thenReturn(true);
        when(thought2.hasConnectionToName(name1)).thenReturn(true);
        when(thought1.getStrengthOfConnection(thought2)).thenReturn(100);
        when(thought2.getStrengthOfConnection(thought1)).thenReturn(100);
        when(thought1.getConnectedThoughtByName(name2)).thenReturn(thought2);
        when(thought2.getConnectedThoughtByName(name1)).thenReturn(thought1);
        when(ltmh.findCategoryToString("test")).thenReturn(thought2);
        when(ltmh.findCategoryToString("robot")).thenReturn(thought2);
        when(ltmh.getThoughtNameFromList(search)).thenReturn("test");
        thought1Thoughts.put(thought2, 100);
        thought2Thoughts.put(thought1, 100);

        List<String> robotNames = new ArrayList<>();
        search.add("test");
        when(ltm.getRobotsNames()).thenReturn(robotNames);
        when(ltm.getRobotsCategory()).thenReturn(thought2);
        assertEquals(thought1, ltmf.getInformation(search));
    }
    
    // searchMap

    @Test
    public void canFind0LevelConnection() {
        when(thought1.getThoughtName()).thenReturn(name1);
        List<String> asked = new ArrayList<>();
        asked.add(name1);
        when(ltmh.getThoughtNameFromList(asked)).thenReturn("test");
        assertEquals(thought1, ltmf.searchMap(asked, thought1));
    }

    @Test
    public void canFindFirstLevel100ConnectionWithCorrelatingNames() {
        when(thought1.getThoughtName()).thenReturn(name1);
        when(thought2.getThoughtName()).thenReturn(name2);
        when(thought1.hasConnectionToName(name2)).thenReturn(true);
        when(thought2.hasConnectionToName(name1)).thenReturn(true);
        when(thought1.getStrengthOfConnection(thought2)).thenReturn(100);
        when(thought2.getStrengthOfConnection(thought1)).thenReturn(100);
        when(thought1.getConnectedThoughtByName(name2)).thenReturn(thought2);
        when(thought2.getConnectedThoughtByName(name1)).thenReturn(thought1);

        List<String> asked = new ArrayList<>();
        asked.add("test");
        asked.add("1");
        when(ltmh.getThoughtNameFromList(asked)).thenReturn("test_1");
        assertEquals(thought2, ltmf.searchMap(asked, thought1));
    }

    @Test
    public void canFindFirstLevel100ConnectionWithoutCorrelatingNames() {
        when(thought1.getThoughtName()).thenReturn(name1);
        when(thought2.getThoughtName()).thenReturn(name4);
        when(thought1.hasConnectionToName(name4)).thenReturn(true);
        when(thought2.hasConnectionToName(name1)).thenReturn(true);
        when(thought1.getStrengthOfConnection(thought2)).thenReturn(100);
        when(thought2.getStrengthOfConnection(thought1)).thenReturn(100);
        when(thought1.getConnectedThoughtByName(name4)).thenReturn(thought2);
        when(thought2.getConnectedThoughtByName(name1)).thenReturn(thought1);

        List<String> asked = new ArrayList<>();
        asked.add("NotTest");
        asked.add("1");
        when(ltmh.getThoughtNameFromList(asked)).thenReturn("NotTest_1");
        assertEquals(thought2, ltmf.searchMap(asked, thought1));
    }

    @Test
    public void cannotFindConnectionWith0Strength() {
        when(thought1.getThoughtName()).thenReturn(name1);
        when(thought2.getThoughtName()).thenReturn(name4);
        when(thought1.hasConnectionToName(name4)).thenReturn(true);
        when(thought2.hasConnectionToName(name1)).thenReturn(true);
        when(thought1.getStrengthOfConnection(thought2)).thenReturn(0);
        when(thought2.getStrengthOfConnection(thought1)).thenReturn(0);
        when(thought1.getConnectedThoughtByName(name4)).thenReturn(thought2);
        when(thought2.getConnectedThoughtByName(name1)).thenReturn(thought1);

        List<String> asked = new ArrayList<>();
        asked.add("NotTest");
        asked.add("1");
        when(ltmh.getThoughtNameFromList(asked)).thenReturn("NotTest_1");
        assertEquals(null, ltmf.searchMap(asked, thought1));
    }

    @Test
    public void canFindSecoundLevel100ConnectionWithCorrelatingNames() {
        when(thought1.getThoughtName()).thenReturn(name1);
        when(thought2.getThoughtName()).thenReturn(name2);
        when(thought3.getThoughtName()).thenReturn(name3);
        when(thought1.hasConnectionToName(name3)).thenReturn(false);
        when(thought3.hasConnectionToName(name1)).thenReturn(false);
        when(thought1.hasConnectionToName(name2)).thenReturn(true);
        when(thought2.hasConnectionToName(name1)).thenReturn(true);
        when(thought2.hasConnectionToName(name3)).thenReturn(true);
        when(thought3.hasConnectionToName(name2)).thenReturn(true);
        when(thought1.getStrengthOfConnection(thought2)).thenReturn(100);
        when(thought2.getStrengthOfConnection(thought1)).thenReturn(100);
        when(thought1.getStrengthOfConnection(thought3)).thenReturn(0);
        when(thought3.getStrengthOfConnection(thought1)).thenReturn(0);
        when(thought3.getStrengthOfConnection(thought2)).thenReturn(100);
        when(thought2.getStrengthOfConnection(thought3)).thenReturn(100);
        when(thought1.getConnectedThoughtByName(name2)).thenReturn(thought2);
        when(thought2.getConnectedThoughtByName(name1)).thenReturn(thought1);
        when(thought2.getConnectedThoughtByName(name3)).thenReturn(thought3);
        when(thought3.getConnectedThoughtByName(name2)).thenReturn(thought2);
        thought1Thoughts.put(thought2, 100);
        thought2Thoughts.put(thought1, 100);
        thought3Thoughts.put(thought2, 100);
        thought2Thoughts.put(thought3, 100);

        List<String> asked = new ArrayList<>();
        asked.add("test");
        asked.add("1");
        asked.add("position");
        when(ltmh.getThoughtNameFromList(asked)).thenReturn("test_1_position");
        assertEquals(thought3, ltmf.searchMap(asked, thought1));
    }

    @Test
    public void canFindNoSecoundConnectionWithoutCorrelatingNames() {
        when(thought1.getThoughtName()).thenReturn(name5);
        when(thought2.getThoughtName()).thenReturn(name4);
        when(thought3.getThoughtName()).thenReturn(name3);
        when(thought1.hasConnectionToName(name3)).thenReturn(false);
        when(thought3.hasConnectionToName(name5)).thenReturn(false);
        when(thought1.hasConnectionToName(name4)).thenReturn(true);
        when(thought2.hasConnectionToName(name5)).thenReturn(true);
        when(thought2.hasConnectionToName(name3)).thenReturn(true);
        when(thought3.hasConnectionToName(name4)).thenReturn(true);
        when(thought1.getStrengthOfConnection(thought2)).thenReturn(100);
        when(thought2.getStrengthOfConnection(thought1)).thenReturn(100);
        when(thought1.getStrengthOfConnection(thought3)).thenReturn(0);
        when(thought3.getStrengthOfConnection(thought1)).thenReturn(0);
        when(thought3.getStrengthOfConnection(thought2)).thenReturn(100);
        when(thought2.getStrengthOfConnection(thought3)).thenReturn(100);
        when(thought1.getConnectedThoughtByName(name4)).thenReturn(thought2);
        when(thought2.getConnectedThoughtByName(name5)).thenReturn(thought1);
        when(thought2.getConnectedThoughtByName(name3)).thenReturn(thought3);
        when(thought3.getConnectedThoughtByName(name4)).thenReturn(thought2);

        List<String> asked = new ArrayList<>();
        asked.add("test");
        asked.add("1");
        asked.add("position");
        when(ltmh.getThoughtNameFromList(asked)).thenReturn("test_1_position");
        assertEquals(null, ltmf.searchMap(asked, thought1));
    }

    @Test
    public void canFindWrongConnectionWithPartlyCorrelatingNames() {
        when(thought1.getThoughtName()).thenReturn(name1);
        when(thought2.getThoughtName()).thenReturn(name5);
        when(thought3.getThoughtName()).thenReturn(name3);
        when(thought1.hasConnectionToName(name3)).thenReturn(false);
        when(thought3.hasConnectionToName(name1)).thenReturn(false);
        when(thought1.hasConnectionToName(name5)).thenReturn(true);
        when(thought2.hasConnectionToName(name1)).thenReturn(true);
        when(thought2.hasConnectionToName(name3)).thenReturn(true);
        when(thought3.hasConnectionToName(name5)).thenReturn(true);
        when(thought1.getStrengthOfConnection(thought2)).thenReturn(100);
        when(thought2.getStrengthOfConnection(thought1)).thenReturn(100);
        when(thought1.getStrengthOfConnection(thought3)).thenReturn(0);
        when(thought3.getStrengthOfConnection(thought1)).thenReturn(0);
        when(thought3.getStrengthOfConnection(thought2)).thenReturn(100);
        when(thought2.getStrengthOfConnection(thought3)).thenReturn(100);
        when(thought1.getConnectedThoughtByName(name5)).thenReturn(thought2);
        when(thought2.getConnectedThoughtByName(name1)).thenReturn(thought1);
        when(thought2.getConnectedThoughtByName(name3)).thenReturn(thought3);
        when(thought3.getConnectedThoughtByName(name5)).thenReturn(thought2);
        thought1Thoughts.put(thought2, 100);
        thought2Thoughts.put(thought1, 100);
        thought3Thoughts.put(thought2, 100);
        thought2Thoughts.put(thought3, 100);

        List<String> asked = new ArrayList<>();
        asked.add("test");
        asked.add("1");
        asked.add("position");
        when(ltmh.getThoughtNameFromList(asked)).thenReturn("test_1_position");
        assertEquals(thought1, ltmf.searchMap(asked, thought1));
    }
}
