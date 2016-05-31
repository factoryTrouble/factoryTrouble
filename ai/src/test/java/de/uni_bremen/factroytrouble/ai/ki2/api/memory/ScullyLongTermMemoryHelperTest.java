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

import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

import de.uni_bremen.factroytrouble.ai.ki2.ai.memory.ScullyLongTermMemory;
import de.uni_bremen.factroytrouble.ai.ki2.ai.memory.ScullyLongTermMemoryHelper;
import de.uni_bremen.factroytrouble.ai.ki2.ai.memory.ScullyWorkingMemory;

@RunWith(MockitoJUnitRunner.class)
public class ScullyLongTermMemoryHelperTest {

    @Mock
    private ScullyWorkingMemory wm;
    @Mock
    private ScullyLongTermMemory ltm;
    @Mock
    private List<String> namesList;
    @Mock
    private Thought thought1, thought2, boardCategory, robotCategory, planCategory, experienceCategory, miscCategory;

    private List<String> boardNames, robotNames, planNames, expierienceNames;

    private ScullyLongTermMemoryHelper ltmh;

    @Before
    public void setUp() throws Exception {
        ltmh = new ScullyLongTermMemoryHelper(ltm);
        boardNames = new ArrayList<>();
        robotNames = new ArrayList<>();
        planNames = new ArrayList<>();
        expierienceNames = new ArrayList<>();
        when(ltm.getBoardNames()).thenReturn(boardNames);
        when(ltm.getRobotsNames()).thenReturn(robotNames);
        when(ltm.getPlansNames()).thenReturn(planNames);
        when(ltm.getExpierienceNames()).thenReturn(expierienceNames);
        when(ltm.getBoardCategory()).thenReturn(boardCategory);
        when(ltm.getRobotsCategory()).thenReturn(robotCategory);
        when(ltm.getPlansCategory()).thenReturn(planCategory);
        when(ltm.getExperienceCategory()).thenReturn(experienceCategory);
        when(ltm.getMiscCategory()).thenReturn(miscCategory);
        when(boardCategory.getThoughtName()).thenReturn("boardCategory");
        when(robotCategory.getThoughtName()).thenReturn("robotCategory");
        when(planCategory.getThoughtName()).thenReturn("planCategory");
        when(experienceCategory.getThoughtName()).thenReturn("experienceCategory");
        when(miscCategory.getThoughtName()).thenReturn("miscCategory");
    }

    @After
    public void tearDown() throws Exception {
    }

    // getThoughtNameFromList

    @Test
    public void getThoughtNameFromListDoesReturnTest() {
        List<String> test = new ArrayList<>();
        test.add("test");
        assertEquals("test", ltmh.getThoughtNameFromList(test));
    }

    @Test
    public void getThoughtNameFromListDoesReturnFlagOne() {
        List<String> test = new ArrayList<>();
        test.add("flag");
        test.add("1");
        assertEquals("flag_1", ltmh.getThoughtNameFromList(test));
    }

    @Test
    public void getThoughtNameFromListDoesReturnNullWhenListEmpty() {
        List<String> test = new ArrayList<>();
        assertNull(ltmh.getThoughtNameFromList(test));
    }

    @Test
    public void getThoughtNameFromListDoesReturnTestWhenTestExpectedName() {
        List<String> test = new ArrayList<>();
        test.add("expectedName");
        test.add("test");
        assertEquals("test", ltmh.getThoughtNameFromList(test));
    }

    // findThoughtsToconnectTo

    @Test
    public void findsWantedThoughtTwoForThoughtOneWithShortTerm() {
        when(thought1.getThoughtName()).thenReturn("flag_1");
        when(thought2.getThoughtName()).thenReturn("flag");
        when(ltm.getSuperMemory()).thenReturn(wm);
        List<String> buff = new ArrayList<>();
        buff.add("flag");
        when(wm.getThoughtFromShortTerm(buff)).thenReturn(thought2);
        List<Thought> expected = new ArrayList<>();
        expected.add(thought2);
        assertEquals(expected, ltmh.findToughtsToConnectTo(thought2));
    }
    
    @Test
    public void findsWantedThoughtTwoForThoughtOneWithoutShortTerm() {
        when(thought1.getThoughtName()).thenReturn("flag_1");
        when(thought2.getThoughtName()).thenReturn("flag");
        boardNames.add("flag");
        when(ltm.getSuperMemory()).thenReturn(wm);
        List<String> buff = new ArrayList<>();
        buff.add("flag");
        when(wm.getThoughtFromShortTerm(buff)).thenReturn(null);
        List<Thought> expected = new ArrayList<>();
        expected.add(boardCategory);
        assertEquals(expected, ltmh.findToughtsToConnectTo(thought2));
    }

    // findCategoryToConnectTo

    @Test
    public void emptyThoughtNamedTestConnectToBoardWhenFittingOnlyToBoard() {
        String s = "test";
        when(thought1.getThoughtName()).thenReturn(s);
        boardNames.add(s);
        assertEquals(boardCategory, ltmh.findCategoryToConnectTo(thought1));
    }

    @Test
    public void emptyThoughtNamedTestConnectToMiscWhenNotFitting() {
        String s = "test";
        when(thought1.getThoughtName()).thenReturn(s);
        assertEquals(miscCategory, ltmh.findCategoryToConnectTo(thought1));
    }

    @Test
    public void emptyThoughtNamedTestBlaConnectToMiscWhenFittingToMultiple() {
        String s = "test_bla";
        when(thought1.getThoughtName()).thenReturn(s);
        boardNames.add("test");
        robotNames.add("bla");
        assertEquals(miscCategory, ltmh.findCategoryToConnectTo(thought1));
    }

    // findCategoryToString

    @Test
    public void boardWhenBoard() {
        String s = "test";
        boardNames.add(s);
        assertEquals(boardCategory, ltmh.findCategoryToString(s));
    }

    @Test
    public void robotWhenRobot() {
        String s = "test";
        robotNames.add(s);
        assertEquals(robotCategory, ltmh.findCategoryToString(s));
    }

    @Test
    public void planWhenPlan() {
        String s = "test";
        planNames.add(s);
        assertEquals(planCategory, ltmh.findCategoryToString(s));
    }

    @Test
    public void experienceWhenExperience() {
        String s = "test";
        expierienceNames.add(s);
        assertEquals(experienceCategory, ltmh.findCategoryToString(s));
    }

    @Test
    public void miscWhenMisc() {
        String s = "test";
        assertEquals(miscCategory, ltmh.findCategoryToString(s));
    }

    @Test
    public void notNullWhenBoard() {
        String s = "test";
        boardNames.add(s);
        assertNotNull(ltmh.findCategoryToString(s));
    }

    @Test
    public void notNullWhenRobot() {
        String s = "test";
        robotNames.add(s);
        assertNotNull(ltmh.findCategoryToString(s));
    }

    @Test
    public void notNullWhenPlan() {
        String s = "test";
        planNames.add(s);
        assertNotNull(ltmh.findCategoryToString(s));
    }

    @Test
    public void notNullWhenExperience() {
        String s = "test";
        expierienceNames.add(s);
        assertNotNull(ltmh.findCategoryToString(s));
    }

    @Test
    public void notNullWhenMisc() {
        String s = "test";
        assertNotNull(ltmh.findCategoryToString(s));
    }

    // possibleNamesToConnectTo

    @Test
    public void helpMethodsWorkTogetherWhenThoughtBelongsToAllCategorys() {
        List<String> blub = new ArrayList<>();
        blub.add("robotTest");
        when(ltm.getRobotsNames()).thenReturn(blub);
        when(thought1.getThoughtName()).thenReturn("robotTest_nextFlag");
        List<String> expected = new ArrayList<>();
        expected.add("flag");
        expected.add("robotTest");
        assertEquals(expected, ltmh.possibleNamesToConnectTo(thought1));
    }

    @Test
    public void helpMethodsWorkTogetherWhenThoughtBelongsToNoCategory() {
        List<String> blub = new ArrayList<>();
        when(ltm.getRobotsNames()).thenReturn(blub);
        when(thought1.getThoughtName()).thenReturn("robotTest_notAFlag");
        assertTrue(ltmh.possibleNamesToConnectTo(thought1).isEmpty());
    }

    // findConnectionsForFlag

    @Test
    public void findsConnectionToFlag1() {
        List<String> connect = new ArrayList<>();
        List<String> nameParts = new ArrayList<>();
        nameParts.add("get");
        nameParts.add("flag");
        nameParts.add("1");
        nameParts.add("position");
        ltmh.findConnectionsForFlag(connect, nameParts);
        assertEquals("flag_1", connect.get(0));
    }

    @Test
    public void findsConnectionToFlagWhenLookingForNextflag() {
        List<String> connect = new ArrayList<>();
        List<String> nameParts = new ArrayList<>();
        nameParts.add("get");
        nameParts.add("nextFlag");
        ltmh.findConnectionsForFlag(connect, nameParts);
        assertEquals("flag", connect.get(0));
    }

    @Test
    public void findsConnectionToFlagsInGeneral() {
        List<String> connect = new ArrayList<>();
        List<String> nameParts = new ArrayList<>();
        nameParts.add("get");
        nameParts.add("flag");
        ltmh.findConnectionsForFlag(connect, nameParts);
        assertEquals("flag", connect.get(0));
    }

    @Test
    public void findsNoConnectionToFlagsWhenNamePartsDoesntContainFlagOrNextFlag() {
        List<String> connect = new ArrayList<>();
        List<String> nameParts = new ArrayList<>();
        nameParts.add("get");
        nameParts.add("test");
        nameParts.add("Position");
        ltmh.findConnectionsForFlag(connect, nameParts);
        assertTrue(connect.isEmpty());
    }

    @Test
    public void findsNoConnectionToFlagsWhenFlagNumberToBig() {
        List<String> connect = new ArrayList<>();
        List<String> nameParts = new ArrayList<>();
        nameParts.add("get");
        nameParts.add("flag");
        nameParts.add("20");
        nameParts.add("Position");
        ltmh.findConnectionsForFlag(connect, nameParts);
        assertEquals("flag", connect.get(0));
    }

    @Test
    public void findsOnlyConnectionToFlagsInGeneralWhenFlagNumberInvalid() {
        List<String> connect = new ArrayList<>();
        List<String> nameParts = new ArrayList<>();
        nameParts.add("get");
        nameParts.add("flag");
        nameParts.add("dickbutt");
        nameParts.add("Position");
        ltmh.findConnectionsForFlag(connect, nameParts);
        assertEquals("flag", connect.get(0));
    }

    @Test
    public void findsOnlyConnectionToFlagsInGeneralWhenFlagNumberNegative() {
        List<String> connect = new ArrayList<>();
        List<String> nameParts = new ArrayList<>();
        nameParts.add("get");
        nameParts.add("flag");
        nameParts.add("-1");
        nameParts.add("Position");
        ltmh.findConnectionsForFlag(connect, nameParts);
        assertEquals("flag", connect.get(0));
    }

    // findConnectionsToRobot

    @Test
    public void findsConnectionToTestRobot() {
        when(ltm.getRobotsNames()).thenReturn(namesList);
        when(namesList.contains(Mockito.any())).thenReturn(false);
        when(namesList.contains("test")).thenReturn(true);
        List<String> connect = new ArrayList<>();
        List<String> nameParts = new ArrayList<>();
        nameParts.add("get");
        nameParts.add("test");
        nameParts.add("position");
        ltmh.findConnectionsToRobot(connect, nameParts);
        assertEquals("test", connect.get(0));
    }

    @Test
    public void findsConnectionToRobotsInGeneralWhenTestIsRobot() {
        when(ltm.getRobotsNames()).thenReturn(namesList);
        when(namesList.contains(Mockito.any())).thenReturn(false);
        when(namesList.contains("test")).thenReturn(true);
        List<String> connect = new ArrayList<>();
        List<String> nameParts = new ArrayList<>();
        nameParts.add("get");
        nameParts.add("test");
        ltmh.findConnectionsToRobot(connect, nameParts);
        assertEquals("robot", connect.get(0));
    }

    @Test
    public void findsNoConnectionToRobotsWhenTestNotRobot() {
        when(ltm.getRobotsNames()).thenReturn(namesList);
        when(namesList.contains(Mockito.any())).thenReturn(false);
        List<String> connect = new ArrayList<>();
        List<String> nameParts = new ArrayList<>();
        nameParts.add("get");
        nameParts.add("test");
        nameParts.add("Position");
        ltmh.findConnectionsToRobot(connect, nameParts);
        assertTrue(connect.isEmpty());
    }

    // listsShareOneElement

    @Test
    public void falseWhenNoSimilarElements() {
        List<String> one = new ArrayList<>();
        one.add("The");
        one.add("Cake");
        List<String> two = new ArrayList<>();
        two.add("Is");
        two.add("A");
        two.add("Lie");
        assertFalse(ltmh.stringListsShareOneElement(one, two));
    }

    @Test
    public void trueWhenOneSimilarElement() {
        List<String> one = new ArrayList<>();
        one.add("The");
        one.add("Cake");
        List<String> two = new ArrayList<>();
        two.add("Is");
        two.add("A");
        two.add("Cake");
        assertTrue(ltmh.stringListsShareOneElement(one, two));
    }

    @Test
    public void trueWhenTwoSimilarElements() {
        List<String> one = new ArrayList<>();
        one.add("The");
        one.add("Cake");
        List<String> two = new ArrayList<>();
        two.add("Is");
        two.add("The");
        two.add("Cake");
        assertTrue(ltmh.stringListsShareOneElement(one, two));
    }

    // stringListToLowerCase

    @Test
    public void lowerStayLower() {
        List<String> given = new ArrayList<>();
        given.add("test");
        assertEquals("test", ltmh.stringListToLowerCase(given).get(0));
    }

    @Test
    public void upperBecomeLower() {
        List<String> given = new ArrayList<>();
        given.add("Test");
        assertEquals("test", ltmh.stringListToLowerCase(given).get(0));
    }

    // splitUpThoughtName

    @Test
    public void shouldSplitUpAtEveryUnderscore() {
        String given = "test_fall_1";
        List<String> expected = new ArrayList<String>();
        expected.add("test");
        expected.add("fall");
        expected.add("1");
        assertEquals(expected, ltmh.splitUpThoughtName(given));
    }

    @Test
    public void shouldJustReturnOneElementIfNoUnderscore() {
        String given = "testfall1";
        List<String> expected = new ArrayList<String>();
        expected.add("testfall1");
        assertEquals(expected, ltmh.splitUpThoughtName(given));
    }

    @Test
    public void shouldReturnEmptyListIfStringEmpty() {
        String given = "";
        assertTrue(ltmh.splitUpThoughtName(given).isEmpty());
    }
    
    // correlatingStringFactor

    @Test
    public void nullStrings0(){
        assertEquals(0, ltmh.correlatingStringFactor("equal", null));
        assertEquals(0, ltmh.correlatingStringFactor(null, null));
        assertEquals(0, ltmh.correlatingStringFactor(null, "equal"));
    }
    
    @Test
    public void equalStrings100(){
        assertEquals(100, ltmh.correlatingStringFactor("equal", "equal"));
    }
    
    @Test
    public void totalyDifferentStrings0(){
        assertEquals(0, ltmh.correlatingStringFactor("equal", "else"));
    }
    
    @Test
    public void halfEqualStrings50(){
        assertEquals(50, ltmh.correlatingStringFactor("neary_equal", "half_equal"));
    }

    // wasThisStringMeant

    @Test
    public void recognizesEqualEmptyStringsAsMeant() {
        assertTrue(ltmh.wasThisStringMeant("", ""));
    }

    @Test
    public void recognizesEqualStringsAsMeant() {
        assertTrue(ltmh.wasThisStringMeant("test", "test"));
    }

    @Test
    public void recognizesNotSimilarStringsWithDifferentLengthAsNotMeant() {
        assertFalse(ltmh.wasThisStringMeant("test", "totalyNotTest"));
    }

    @Test
    public void recognizesNotSimilarStringAsNotMeant() {
        assertFalse(ltmh.wasThisStringMeant("test", "fail"));
    }

    @Test
    public void ignoresCase() {
        assertTrue(ltmh.wasThisStringMeant("Test", "test"));
    }

    // wasThisStringListMeant

    @Test
    public void recognizesEqualListsWithNoElementAsMeant() {
        List<String> one = new ArrayList<String>();
        List<String> two = new ArrayList<String>();
        assertTrue(ltmh.wasThisStringListMeant(one, two));
    }

    @Test
    public void recognizesEqualListsWithOneElementAsMeant() {
        List<String> one = new ArrayList<String>();
        one.add("test");
        List<String> two = new ArrayList<String>();
        two.add("test");
        assertTrue(ltmh.wasThisStringListMeant(one, two));
    }

    @Test
    public void recognizesEqualListsWithTwoElementsAsMeant() {
        List<String> one = new ArrayList<String>();
        one.add("test");
        one.add("case");
        List<String> two = new ArrayList<String>();
        two.add("test");
        two.add("case");
        assertTrue(ltmh.wasThisStringListMeant(one, two));
    }

    @Test
    public void recognizesNotSimilarListsWithDifferentLengthAsNotMeant() {
        List<String> one = new ArrayList<String>();
        one.add("test");
        List<String> two = new ArrayList<String>();
        two.add("totaly");
        two.add("Not");
        two.add("Test");
        assertFalse(ltmh.wasThisStringListMeant(one, two));
    }

    @Test
    public void recognizesNotSimilarListsWithOneElementAsNotMeant() {
        List<String> one = new ArrayList<String>();
        one.add("test");
        List<String> two = new ArrayList<String>();
        two.add("totalyNotTest");
        assertFalse(ltmh.wasThisStringListMeant(one, two));
    }
}
