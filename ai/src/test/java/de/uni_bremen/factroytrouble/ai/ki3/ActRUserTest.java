package de.uni_bremen.factroytrouble.ai.ki3;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.stream.IntStream;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author Thorben, Tim, Stefan
 */
@RunWith(MockitoJUnitRunner.class)
public class ActRUserTest {

    private static final Logger LOGGO = Logger.getLogger(ActRUserTest.class);
    
    @Mock
    ScoreManager manager, trivPathManager, obstacleManager, conveyorManager, sacrificeManager, scrapPressManager;

    ActRUser user, trivPathUser, obstacleUser, conveyorUser, sacrificeUser,scrapPressUser;
    //whirlwind
    String moveTestBoard = "\"ti_ww_lw3,ti_lw3,ti_lw3,ti_lw3,ti_lw3,ti_lw3,ti_lw3,ti_lw3,ti_lw3,ti_lw3,ti_lw3,ti_we_lw3;ti,ti_ww,ti_ww,ti_ww,ti_ww,ti_ww,ti_ww,ti_ww,ti_ww,ti_ww,ti_ww,ti_ww;ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;re,1n,ti_ws,1n,ti_ws,1n,1n,ti_ws,1n,ti_ws,1n,ti;ti,1n,ti,1n,f2,1n,1n,ti,1n,ti,1n,ti;ti_ww,1n_ww_pe2,de,1n_we_pw2,de,1n_we_pw2,1n_ww_pe2,de,1n_ww_pe2,de,1n_we_pw2,ti_we;rr,2w,2w,2w,2w,2w,2e,2e,2e,2e,2e,rl;ti_ww,ti,ti,ti,ti,ti,ti,ti,re,ti,ti,ti_we;ti,rr,ti,ti_we_ws,1n,re,ti,ti,ti,de,ti,ti;1w,1w_wn_ps3,1w,1w,rl,ti,ti,ti,rl,ti,ti,ti;ti_ww,ti,ti,ti_ws_ls2,ti,2e,2e,2e,ti_wn_ps3,ti,ti,ti_we;1e,1e,1e,ti_ls2,ti,ti_we_ww_lw3,ti,ti,1e,1e,de,ti;ti_ww,1e,1e,ti_ls2,re,1s,ti,ti,1s,ti,ti,ti_we;f3,1s,ti,ti_wn_ls2,ti,1s,ti,ti_ww_lw1,1s_lw1,ti_we_lw1,ti,ti;ti,1s,ti_wn,ti,ti_wn,1s,ti,ti_wn,1s,ti_wn,f1,re;\"";
    
    String obstacleBoard = 
            "\"ti,ti,ti_ws,ti,ti_ws,ti,ti,ti_ws,ti,ti_ws,ti,ti;"
          + "ti,ti_ww,ti,ti_ww,ti,ti_ww,ti_ww,ti_ww,ti,ti_ww,ti,ti_ww;"
          + "ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;"
          + "ti,ti,ti_wn,ti,ti_wn,ti,ti,ti_wn,ti,ti_wn,ti,ti;"
          + "ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;"
          + "ti,ti,ti,ti,ti,ti,ti,ti,ti,de,ti,ti;"
          + "ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,f2,ti;"
          + "ti,ti,ti,ti,ti,f3_ws_we_ww,ti,ti,ti_ws_ls1,ti_ww_le1,ti_le1,ti_we_le1;"
          + "ti,ti,ti,ti,ti,ti,ti,ti_ws_pn3,ti_ls1,ti_ws_ln1,ti,ti;"
          + "ti,ti,ti,ti,ti,ti,ti,ti,ti_wn_ls1,ti_wn_ln1,ti,ti;"
          + "ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;"
          + "ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;"
          + "ti,de,rl,ti,ti,ti,ti,ti,ti,ti,ti,ti;"
          + "ti,f1,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;"
          + "ti,ti,de,ti_ws,ti,ti,ti,ti,ti,ti,ti,ti;"
          + "ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;\"";
    
    String trivPathBoard = 
              "\"ti,ti,ti_ws,ti,ti_ws,ti,ti,ti_ws,ti,ti_ws,ti,ti;"
            + "ti,ti_ww,ti,ti_ww,ti,ti_ww,ti_ww,ti_ww,ti,ti_ww,ti,ti_ww;"
            + "ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;"
            + "ti,ti,ti_wn,ti,ti_wn,ti,ti,ti_wn,ti,ti_wn,ti,ti;"
            + "ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;"
            + "ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;"
            + "ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,f2,ti;"
            + "ti,ti,ti,ti,ti,f3,ti,ti,ti,ti,ti,ti;"
            + "ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;"
            + "ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;"
            + "ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;"
            + "ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;"
            + "ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;"
            + "ti,f1,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;"
            + "ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;"
            + "ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;\"";
    
    String conveyorBoard = "\""
            + "ti,1n,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;"
            + "ti,1n,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;"
            + "ti,1n,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;"
            + "ti,1n,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;"
            + "ti,f1,ti,2w,2w,2w,2w,2w,ti,ti,ti,ti;"
            + "ti,ti,ti,1w,1w,1w,1w,1w,ti,ti,ti,ti;"
            + "ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;"
            + "ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;"
            + "ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;"
            + "ti,ti,ti,1e,1e,ti,ti,1n,ti,f3,ti,ti;"
            + "ti,ti,ti,ti,ti,ti,ti,de,ti,ti,ti,ti;"
            + "ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;"
            + "ti,f2,ti_wn,ti_wn,ti,ti,ti,ti,ti,ti,ti,ti;"
            + "ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;"
            + "ti,rl,ti,rr,ti,ti,ti,ti,ti,ti,ti,ti;"
            + "ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;\"";
    
    String sacrificeBoard = "\""
            + "ti,ti,ti,de,ti,ti,ti,ti,ti,ti,ti,ti;"
            + "de,de,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;"
            + "ti,ti,f1,ti,ti,ti,ti,ti,ti,ti,ti,ti;"
            + "ti,ti,de,ti,ti,ti,ti,ti,ti,ti,ti,ti;"
            + "ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,de;"
            + "ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,de,ti;"
            + "ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,de,ti;"
            + "ti,ti,ti,ti,ti,ti,ti,ti,de,f2,ti,ti;"
            + "ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;"
            + "ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;"
            + "ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;"
            + "ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;"
            + "ti,ti,ti,ti,ti,f3,ti,ti,ti,ti,ti,ti;"
            + "ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;"
            + "ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;"
            + "ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;\"";
    
    String scrapPress = "\"ti_ww_lw3,ti_lw3,ti_lw3,ti_lw3,ti_lw3,ti_lw3,ti_lw3,ti_lw3,ti_lw3,ti_lw3,ti_lw3,ti_we_lw3;"
            + "ti,ti_ww,ti_ww,ti_ww,ti_ww,ti_ww,ti_ww,ti_ww,ti_ww,ti_ww,ti_ww,ti_ww;"
            + "ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;"
            + "ti,ti,ti_wn,ti,ti_wn,ti,ti,ti_wn_ps3,ti,ti_wn,ti,ti;"
            + "re,1n,ti_ws,1n,ti_ws,1n,1n,ti_ws,1n,ti_ws,1n,ti;"
            + "ti,1n,ti,1n,f2,1n,1n,ti,1n,ti,1n,ti;"
            + "ti_ww,1n_ww_pe2,de,1n_we_pw2,de,1n_we_pw2,1n_ww_pe2,de,1n_ww_pe2,de,1n_we_pw2,ti_we;"
            + "rr,2w,2w,2w,2w,2w,2e,2e,2e,2e,2e,rl;"
            + "ti_ww,ti,ti,ti,ti,ti,ti,ti,re,ti,ti,ti_we;"
            + "ti,rr,ti,ti_we_ws,1n,re,ti,ti,ti,de,ti,ti;"
            + "1w,1w_wn_ps3,1w,1w,rl,ti,ti,ti,rl,ti,ti,ti;"
            + "ti_ww,ti,ti,ti_ws_ls2,ti,2e,2e,2e,ti_wn_ps3,ti,ti,ti_we;"
            + "1e,1e,1e,ti_ls2,ti,ti_we_ww_lw3,ti,ti,1e,1e,de,ti;"
            + "ti_ww,1e,1e,ti_ls2,re,1s,ti,ti,1s,ti,ti,ti_we;"
            + "f3,1s,ti,ti_wn_ls2,ti,1s,ti,ti_ww_lw1,1s_lw1,ti_we_lw1,rl,ti;"
            + "ti,1s,ti_wn,ti,ti_wn,1s,ti,ti_wn,1s,ti_wn,f1,re;\"";
    
    String trivPathHand = "\"a1_540,tu_50,a1_550,tr_380,a1_560,a1_570,a1_580,tl_130,b1_460;\"";
    
    String obstacleHand = "\"a1_540,tl_50,a2_550,tl_380,a1_560,a1_570,tr_580,tu_130,b1_460;\"";
    
    String trivPathRobotLPHP = "\"hammer,t,3,10,"; //dann Orientation
    String trivPathEmptyRegisterOff = ",nil-nil-nil-nil-nil,nil,"; //dann Flag, Current Position
    String trivPathRespawn = ",1:1;\"";
    
    String moveTestRobots = "\"hammer,t,2,3,n,nil-nil-a1_200-b1_100-tu_30,nil,2,6:11,1:1;enemy,nil,3,10,e,nil-nil-nil-nil-nil,t,0,11:11,4:2;\"";
    String moveTestHand = "\"a3_400,tl_50;\"";
    
    String scrapPressHand = "\"a1_200,a2_300,a3_400,b1_100,tl_50,tr_80,tu_30,b1_460,tu_130;\"";
    
    @Before
    public void setUp() throws Exception {
        when(scrapPressManager.getBoardState()).thenReturn(scrapPress);
        when(scrapPressManager.getHandState()).thenReturn(scrapPressHand);
        scrapPressUser = new ActRUser(scrapPressManager);
        
        when(manager.getBoardState()).thenReturn(moveTestBoard);
        when(manager.getRobotsState()).thenReturn(moveTestRobots);
        when(manager.getHandState()).thenReturn(moveTestHand);
        user = new ActRUser(manager);
        
        when(trivPathManager.getBoardState()).thenReturn(trivPathBoard);
        when(trivPathManager.getHandState()).thenReturn(trivPathHand);
        trivPathUser = new ActRUser(trivPathManager);
        
        when(obstacleManager.getBoardState()).thenReturn(obstacleBoard);
        when(obstacleManager.getHandState()).thenReturn(obstacleHand);
        obstacleUser = new ActRUser(obstacleManager);
        
        when(conveyorManager.getBoardState()).thenReturn(conveyorBoard);
        conveyorUser = new ActRUser(conveyorManager);
        
        when(sacrificeManager.getBoardState()).thenReturn(sacrificeBoard);
        sacrificeUser = new ActRUser(sacrificeManager);
    }
    
    @Test
    public void moveTest() {
        int[] answer = executeTestMove(user, "Chunky");

        assertEquals(5, answer.length);
        assertEquals(0, answer[0]);
        assertEquals(1, answer[1]);
        assertEquals(-1, answer[2]);
        assertEquals(-1, answer[3]);
        assertEquals(-1, answer[4]);
    }

    /*
     * Stefan
     */
    @Test
    public void chunkyOrientationNorthFlag3Position5_2MatchingTest(){
        when(trivPathManager.getRobotsState()).thenReturn(
                trivPathRobotLPHP.concat("n").concat(trivPathEmptyRegisterOff).concat("2,5:2").concat(trivPathRespawn));
        
        String[] hand = setHand(trivPathHand);
        int[] answer = executeTestMove(trivPathUser, "Chunky");
        
        String[] selected = new String[]{hand[answer[0]],hand[answer[1]],hand[answer[2]],hand[answer[3]],hand[answer[4]]};
        //Index ungleich:
        differentIndicesTest(answer);
        
        //korrekte Kartenauswahl
        assertArrayEquals(new String[]{"a1","a1","a1","a1","a1"}, selected);
    }
    
    /*
     * Stefan
     */
    @Test
    public void chunkyOrientationSouthFlag3Position5_3MatchingTest(){
        when(trivPathManager.getRobotsState()).thenReturn(
                trivPathRobotLPHP.concat("s").concat(trivPathEmptyRegisterOff).concat("2,5:3").concat(trivPathRespawn));
        
        String[] hand = setHand(trivPathHand);
        int[] answer = executeTestMove(trivPathUser, "Chunky");
        
        String[] selected = new String[]{hand[answer[0]],hand[answer[1]],hand[answer[2]],hand[answer[3]],hand[answer[4]]};
        //Index ungleich:
        differentIndicesTest(answer);
        
        //korrekte Kartenauswahl
        assertTrue(Arrays.deepEquals(new String[]{"tu","a1","a1","a1","a1"}, selected) || 
                Arrays.deepEquals(new String[]{"b1","tu","a1","a1","a1"}, selected));
    }
    
    /*
     * Stefan
     */
    @Test
    public void chunkyOrientationEastFlag3Position5_3MatchingTest(){
        when(trivPathManager.getRobotsState()).thenReturn(
                trivPathRobotLPHP.concat("e").concat(trivPathEmptyRegisterOff).concat("2,5:3").concat(trivPathRespawn));
        
        String[] hand = setHand(trivPathHand);
        int[] answer = executeTestMove(trivPathUser, "Chunky");
        
        String[] selected = new String[]{hand[answer[0]],hand[answer[1]],hand[answer[2]],hand[answer[3]],hand[answer[4]]};
        //Index ungleich:
        differentIndicesTest(answer);
        
        //korrekte Kartenauswahl
        assertArrayEquals(new String[]{"tl","a1","a1","a1","a1"}, selected);
    }
    
    /*
     * Stefan
     */
    @Test
    public void chunkyOrientationWestFlag3Position5_3MatchingTest(){
        when(trivPathManager.getRobotsState()).thenReturn(
                trivPathRobotLPHP.concat("w").concat(trivPathEmptyRegisterOff).concat("2,5:3").concat(trivPathRespawn));
        
        String[] hand = setHand(trivPathHand);
        int[] answer = executeTestMove(trivPathUser, "Chunky");
        
        String[] selected = new String[]{hand[answer[0]],hand[answer[1]],hand[answer[2]],hand[answer[3]],hand[answer[4]]};
        //Index ungleich:
        differentIndicesTest(answer);
        
        //korrekte Kartenauswahl
        assertArrayEquals(new String[]{"tr","a1","a1","a1","a1"}, selected);
    }
    
    /*
     * Stefan
     */
    @Test
    public void chunkyOrientationNorthFlag3Position4_4MatchingTest(){
        when(trivPathManager.getRobotsState()).thenReturn(
                trivPathRobotLPHP.concat("n").concat(trivPathEmptyRegisterOff).concat("2,4:4").concat(trivPathRespawn));
        
        String[] hand = setHand(trivPathHand);
        int[] answer = executeTestMove(trivPathUser, "Chunky");
        
        String[] selected = new String[]{hand[answer[0]],hand[answer[1]],hand[answer[2]],hand[answer[3]],hand[answer[4]]};
        //Index ungleich:
        differentIndicesTest(answer);
        
        //korrekte Kartenauswahl
        assertArrayEquals(new String[]{"a1","a1","a1","tr","a1"}, selected);
    }
    
    /*
     * Stefan
     */
    @Test
    public void chunkyOrientationNorthFlag3Position6_4MatchingTest(){
        when(trivPathManager.getRobotsState()).thenReturn(
                trivPathRobotLPHP.concat("n").concat(trivPathEmptyRegisterOff).concat("2,6:4").concat(trivPathRespawn));
        
        String[] hand = setHand(trivPathHand);
        int[] answer = executeTestMove(trivPathUser, "Chunky");
        
        String[] selected = new String[]{hand[answer[0]],hand[answer[1]],hand[answer[2]],hand[answer[3]],hand[answer[4]]};
        //Index ungleich:
        differentIndicesTest(answer);
        
        //korrekte Kartenauswahl
        assertArrayEquals(new String[]{"a1","a1","a1","tl","a1"}, selected);
    }
    
    /*
     * Stefan
     */
    @Test
    public void chunkyOrientationEastFlag3Position0_7MatchingTest(){
        when(trivPathManager.getRobotsState()).thenReturn(
                trivPathRobotLPHP.concat("e").concat(trivPathEmptyRegisterOff).concat("2,0:7").concat(trivPathRespawn));
        
        String[] hand = setHand(trivPathHand);
        int[] answer = executeTestMove(trivPathUser, "Chunky");
        
        String[] selected = new String[]{hand[answer[0]],hand[answer[1]],hand[answer[2]],hand[answer[3]],hand[answer[4]]};
        //Index ungleich:
        differentIndicesTest(answer);
        
        //korrekte Kartenauswahl
        assertArrayEquals(new String[]{"a1","a1","a1","a1","a1"}, selected);
    }
    
    /*
     * Stefan
     */
    @Test
    public void chunkyOrientationWestFlag3Position1_7MatchingTest(){
        when(trivPathManager.getRobotsState()).thenReturn(
                trivPathRobotLPHP.concat("w").concat(trivPathEmptyRegisterOff).concat("2,1:7").concat(trivPathRespawn));
        
        String[] hand = setHand(trivPathHand);
        int[] answer = executeTestMove(trivPathUser, "Chunky");
        
        String[] selected = new String[]{hand[answer[0]],hand[answer[1]],hand[answer[2]],hand[answer[3]],hand[answer[4]]};
        //Index ungleich:
        differentIndicesTest(answer);
        
        //korrekte Kartenauswahl
        assertTrue(Arrays.deepEquals(new String[]{"tu","a1","a1","a1","a1"}, selected) ||
                Arrays.deepEquals(new String[]{"b1","tu","a1","a1","a1"}, selected));
    }
    
    /*
     * Stefan
     */
    @Test
    public void chunkyOrientationNorthFlag3Position1_7MatchingTest(){
        when(trivPathManager.getRobotsState()).thenReturn(
                trivPathRobotLPHP.concat("n").concat(trivPathEmptyRegisterOff).concat("2,1:7").concat(trivPathRespawn));
        
        String[] hand = setHand(trivPathHand);
        int[] answer = executeTestMove(trivPathUser, "Chunky");
        
        String[] selected = new String[]{hand[answer[0]],hand[answer[1]],hand[answer[2]],hand[answer[3]],hand[answer[4]]};
        //Index ungleich:
        differentIndicesTest(answer);
        
        //korrekte Kartenauswahl
        assertArrayEquals(new String[]{"tr","a1","a1","a1","a1"}, selected);
    }
    
    /*
     * Stefan
     */
    @Test
    public void chunkyOrientationSouthFlag3Position1_7MatchingTest(){
        when(trivPathManager.getRobotsState()).thenReturn(
                trivPathRobotLPHP.concat("s").concat(trivPathEmptyRegisterOff).concat("2,1:7").concat(trivPathRespawn));
        
        String[] hand = setHand(trivPathHand);
        int[] answer = executeTestMove(trivPathUser, "Chunky");
        
        String[] selected = new String[]{hand[answer[0]],hand[answer[1]],hand[answer[2]],hand[answer[3]],hand[answer[4]]};
        //Index ungleich:
        differentIndicesTest(answer);
        
        //korrekte Kartenauswahl
        assertArrayEquals(new String[]{"tl","a1","a1","a1","a1"}, selected);
    }
    
    /*
     * Stefan
     */
    @Test
    public void chunkyOrientationEast3Position2_8MatchingTest(){
        when(trivPathManager.getRobotsState()).thenReturn(
                trivPathRobotLPHP.concat("e").concat(trivPathEmptyRegisterOff).concat("2,2:8").concat(trivPathRespawn));
        
        String[] hand = setHand(trivPathHand);
        int[] answer = executeTestMove(trivPathUser, "Chunky");
        
        String[] selected = new String[]{hand[answer[0]],hand[answer[1]],hand[answer[2]],hand[answer[3]],hand[answer[4]]};
        //Index ungleich:
        differentIndicesTest(answer);
        
        //korrekte Kartenauswahl
        assertArrayEquals(new String[]{"a1","a1","a1","tr","a1"}, selected);
    }
    
    /*
     * Stefan
     */
    @Test
    public void chunkyOrientationEast3Position2_6MatchingTest(){
        when(trivPathManager.getRobotsState()).thenReturn(
                trivPathRobotLPHP.concat("e").concat(trivPathEmptyRegisterOff).concat("2,2:6").concat(trivPathRespawn));
        
        String[] hand = setHand(trivPathHand);
        int[] answer = executeTestMove(trivPathUser, "Chunky");
        
        String[] selected = new String[]{hand[answer[0]],hand[answer[1]],hand[answer[2]],hand[answer[3]],hand[answer[4]]};
        //Index ungleich:
        differentIndicesTest(answer);
        
        //korrekte Kartenauswahl
        assertArrayEquals(new String[]{"a1","a1","a1","tl","a1"}, selected);
    }
    
    /*
     * Stefan
     */
    @Test
    public void chunkyOrientationSouth3Position5_12MatchingTest(){
        when(trivPathManager.getRobotsState()).thenReturn(
                trivPathRobotLPHP.concat("s").concat(trivPathEmptyRegisterOff).concat("2,5:12").concat(trivPathRespawn));
        
        String[] hand = setHand(trivPathHand);
        int[] answer = executeTestMove(trivPathUser, "Chunky");
        
        String[] selected = new String[]{hand[answer[0]],hand[answer[1]],hand[answer[2]],hand[answer[3]],hand[answer[4]]};
        //Index ungleich:
        differentIndicesTest(answer);
        
        //korrekte Kartenauswahl
        assertArrayEquals(new String[]{"a1","a1","a1","a1","a1"}, selected);
    }
    
    /*
     * Stefan
     */
    @Test
    public void chunkyOrientationNorth3Position5_11MatchingTest(){
        when(trivPathManager.getRobotsState()).thenReturn(
                trivPathRobotLPHP.concat("n").concat(trivPathEmptyRegisterOff).concat("2,5:11").concat(trivPathRespawn));
        
        String[] hand = setHand(trivPathHand);
        int[] answer = executeTestMove(trivPathUser, "Chunky");
        
        String[] selected = new String[]{hand[answer[0]],hand[answer[1]],hand[answer[2]],hand[answer[3]],hand[answer[4]]};
        //Index ungleich:
        differentIndicesTest(answer);
        
        //korrekte Kartenauswahl
        assertTrue(Arrays.deepEquals(new String[]{"b1","tu","a1","a1","a1"}, selected) ||
                Arrays.deepEquals(new String[]{"tu","a1","a1","a1","a1"}, selected));
    }
    
    /*
     * Stefan
     */
    @Test
    public void chunkyOrientationWest3Position5_11MatchingTest(){
        when(trivPathManager.getRobotsState()).thenReturn(
                trivPathRobotLPHP.concat("w").concat(trivPathEmptyRegisterOff).concat("2,5:11").concat(trivPathRespawn));
        
        String[] hand = setHand(trivPathHand);
        int[] answer = executeTestMove(trivPathUser, "Chunky");
        
        String[] selected = new String[]{hand[answer[0]],hand[answer[1]],hand[answer[2]],hand[answer[3]],hand[answer[4]]};
        //Index ungleich:
        differentIndicesTest(answer);
        
        //korrekte Kartenauswahl
        assertArrayEquals(new String[]{"tl","a1","a1","a1","a1"}, selected);
    }
    
    /*
     * Stefan
     */
    @Test
    public void chunkyOrientationEast3Position5_11MatchingTest(){
        when(trivPathManager.getRobotsState()).thenReturn(
                trivPathRobotLPHP.concat("e").concat(trivPathEmptyRegisterOff).concat("2,5:11").concat(trivPathRespawn));
        
        String[] hand = setHand(trivPathHand);
        int[] answer = executeTestMove(trivPathUser, "Chunky");
        
        String[] selected = new String[]{hand[answer[0]],hand[answer[1]],hand[answer[2]],hand[answer[3]],hand[answer[4]]};
        //Index ungleich:
        differentIndicesTest(answer);
        
        //korrekte Kartenauswahl
        assertArrayEquals(new String[]{"tr","a1","a1","a1","a1"}, selected);
    }
    
    /*
     * Stefan
     */
    @Test
    public void chunkyOrientationSouth3Position4_10MatchingTest(){
        when(trivPathManager.getRobotsState()).thenReturn(
                trivPathRobotLPHP.concat("s").concat(trivPathEmptyRegisterOff).concat("2,4:10").concat(trivPathRespawn));
        
        String[] hand = setHand(trivPathHand);
        int[] answer = executeTestMove(trivPathUser, "Chunky");
        
        String[] selected = new String[]{hand[answer[0]],hand[answer[1]],hand[answer[2]],hand[answer[3]],hand[answer[4]]};
        //Index ungleich:
        differentIndicesTest(answer);
        
        //korrekte Kartenauswahl
        assertArrayEquals(new String[]{"a1","a1","a1","tl","a1"}, selected);
    }
    
    /*
     * Stefan
     */
    @Test
    public void chunkyOrientationSouth3Position6_10MatchingTest(){
        when(trivPathManager.getRobotsState()).thenReturn(
                trivPathRobotLPHP.concat("s").concat(trivPathEmptyRegisterOff).concat("2,6:10").concat(trivPathRespawn));
        
        String[] hand = setHand(trivPathHand);
        int[] answer = executeTestMove(trivPathUser, "Chunky");
        
        String[] selected = new String[]{hand[answer[0]],hand[answer[1]],hand[answer[2]],hand[answer[3]],hand[answer[4]]};
        //Index ungleich:
        differentIndicesTest(answer);
        
        //korrekte Kartenauswahl
        assertArrayEquals(new String[]{"a1","a1","a1","tr","a1"}, selected);
    }
    
    /*
     * Stefan
     */
    @Test
    public void chunkyOrientationWest3Position10_7MatchingTest(){
        when(trivPathManager.getRobotsState()).thenReturn(
                trivPathRobotLPHP.concat("w").concat(trivPathEmptyRegisterOff).concat("2,10:7").concat(trivPathRespawn));
        
        String[] hand = setHand(trivPathHand);
        int[] answer = executeTestMove(trivPathUser, "Chunky");
        
        String[] selected = new String[]{hand[answer[0]],hand[answer[1]],hand[answer[2]],hand[answer[3]],hand[answer[4]]};
        //Index ungleich:
        differentIndicesTest(answer);
        
        //korrekte Kartenauswahl
        assertArrayEquals(new String[]{"a1","a1","a1","a1","a1"}, selected);
    }
    
    /*
     * Stefan
     */
    @Test
    public void chunkyOrientationEast3Position9_7MatchingTest(){
        when(trivPathManager.getRobotsState()).thenReturn(
                trivPathRobotLPHP.concat("e").concat(trivPathEmptyRegisterOff).concat("2,9:7").concat(trivPathRespawn));
        
        String[] hand = setHand(trivPathHand);
        int[] answer = executeTestMove(trivPathUser, "Chunky");
        
        String[] selected = new String[]{hand[answer[0]],hand[answer[1]],hand[answer[2]],hand[answer[3]],hand[answer[4]]};
        //Index ungleich:
        differentIndicesTest(answer);
        
        //korrekte Kartenauswahl
        assertTrue(Arrays.deepEquals(new String[]{"b1","tu","a1","a1","a1"}, selected) ||
                Arrays.deepEquals(new String[]{"tu","a1","a1","a1","a1"}, selected));
    }
    
    /*
     * Stefan
     */
    @Test
    public void chunkyOrientationNorth3Position9_7MatchingTest(){
        when(trivPathManager.getRobotsState()).thenReturn(
                trivPathRobotLPHP.concat("n").concat(trivPathEmptyRegisterOff).concat("2,9:7").concat(trivPathRespawn));
        
        String[] hand = setHand(trivPathHand);
        int[] answer = executeTestMove(trivPathUser, "Chunky");
        
        String[] selected = new String[]{hand[answer[0]],hand[answer[1]],hand[answer[2]],hand[answer[3]],hand[answer[4]]};
        //Index ungleich:
        differentIndicesTest(answer);
        
        //korrekte Kartenauswahl
        assertArrayEquals(new String[]{"tl","a1","a1","a1","a1"}, selected);
    }
    
    /*
     * Stefan
     */
    @Test
    public void chunkyOrientationSouth3Position9_7MatchingTest(){
        when(trivPathManager.getRobotsState()).thenReturn(
                trivPathRobotLPHP.concat("s").concat(trivPathEmptyRegisterOff).concat("2,9:7").concat(trivPathRespawn));
        
        String[] hand = setHand(trivPathHand);
        int[] answer = executeTestMove(trivPathUser, "Chunky");
        
        String[] selected = new String[]{hand[answer[0]],hand[answer[1]],hand[answer[2]],hand[answer[3]],hand[answer[4]]};
        //Index ungleich:
        differentIndicesTest(answer);
        
        //korrekte Kartenauswahl
        assertArrayEquals(new String[]{"tr","a1","a1","a1","a1"}, selected);
    }
    
    /*
     * Stefan
     */
    @Test
    public void chunkyOrientationWest3Position8_8MatchingTest(){
        when(trivPathManager.getRobotsState()).thenReturn(
                trivPathRobotLPHP.concat("w").concat(trivPathEmptyRegisterOff).concat("2,8:8").concat(trivPathRespawn));
        
        String[] hand = setHand(trivPathHand);
        int[] answer = executeTestMove(trivPathUser, "Chunky");
        
        String[] selected = new String[]{hand[answer[0]],hand[answer[1]],hand[answer[2]],hand[answer[3]],hand[answer[4]]};
        //Index ungleich:
        differentIndicesTest(answer);
        
        //korrekte Kartenauswahl
        assertArrayEquals(new String[]{"a1","a1","a1","tl","a1"}, selected);
    }
    
    /*
     * Stefan
     */
    @Test
    public void chunkyOrientationWest3Position8_6MatchingTest(){
        when(trivPathManager.getRobotsState()).thenReturn(
                trivPathRobotLPHP.concat("w").concat(trivPathEmptyRegisterOff).concat("2,8:6").concat(trivPathRespawn));
        
        String[] hand = setHand(trivPathHand);
        int[] answer = executeTestMove(trivPathUser, "Chunky");
        
        String[] selected = new String[]{hand[answer[0]],hand[answer[1]],hand[answer[2]],hand[answer[3]],hand[answer[4]]};
        
        //Index ungleich:
        differentIndicesTest(answer);
        //korrekte Kartenauswahl
        assertArrayEquals(new String[]{"a1","a1","a1","tr","a1"}, selected);
    }
    
    /*
     * Stefan
     */
    @Test
    public void chunkyBorderTest(){
        when(trivPathManager.getRobotsState()).thenReturn(
                trivPathRobotLPHP.concat("n").concat(trivPathEmptyRegisterOff).concat("0,0:10").concat(trivPathRespawn));
        
        String[] hand = setHand(trivPathHand);
        int[] answer = executeTestMove(trivPathUser, "Chunky");
        
        String[] selected = new String[]{hand[answer[0]],hand[answer[1]],hand[answer[2]],hand[answer[3]],hand[answer[4]]};
        //Index ungleich:
        differentIndicesTest(answer);
        
        //korrekte Kartenauswahl
        assertArrayEquals(new String[]{"a1","a1","a1","tr","a1"}, selected);
    }
    
    /*
     * Stefan
     */
    @Test
    public void handleBlankTileFirstTest(){
        when(trivPathManager.getRobotsState()).thenReturn(
                trivPathRobotLPHP.concat("w").concat(trivPathEmptyRegisterOff).concat("0,0:10").concat(trivPathRespawn));
        
        String[] hand = setHand(trivPathHand);
        int[] answer = executeTestMove(trivPathUser, "Chunky");
        
        String[] selected = new String[]{hand[answer[0]],hand[answer[1]],hand[answer[2]],hand[answer[3]],hand[answer[4]]};
        //Index ungleich:
        differentIndicesTest(answer);
        
        //korrekte Kartenauswahl
        assertArrayEquals(new String[]{"b1","tr","a1","a1","a1"}, selected);
    }
    
    /*
     * Stefan
     */
    @Test
    public void finishGameTriggerSomethingNextTest(){
        String cards = "\"a1_530,tl_130,a3_810,b1_460,a1_600,tu_50,a2_780,tr_380,a3_840;\"";
        when(trivPathManager.getRobotsState()).thenReturn(
                trivPathRobotLPHP.concat("w").concat(trivPathEmptyRegisterOff).concat("2,10:6").concat(trivPathRespawn));
        when(trivPathManager.getHandState()).thenReturn(cards);
        trivPathUser = new ActRUser(trivPathManager);
        
        String[] hand = setHand(cards);
        int[] answer = executeTestMove(trivPathUser,"Chunky");
        
        String[] selected = new String[]{hand[answer[0]],hand[answer[1]],hand[answer[2]],hand[answer[3]]};
        
        //Index ungleich:
        differentIndicesTest(answer);
        
        //um Flagge zu erreichen:
        assertArrayEquals(new String[]{"a3","a2","tr","a1"}, selected);
        //letzte Karte beliebig:
        assertTrue(hand[answer[4]] != null && hand[answer[4]] != "-1");
    }
    
    /*
     * Stefan
     */
    @Test
    public void reachFlagTriggerNextFlagTest(){
        String cards = "\"a1_530,tl_130,a3_810,b1_460,a1_600,tu_50,a2_780,tr_380,a3_840;\"";
        when(trivPathManager.getRobotsState()).thenReturn(
                trivPathRobotLPHP.concat("s").concat(trivPathEmptyRegisterOff).concat("1,10:10").concat(trivPathRespawn));
        when(trivPathManager.getHandState()).thenReturn(cards);
        trivPathUser = new ActRUser(trivPathManager);
        
        String[] hand = setHand(cards);
        int[] answer = executeTestMove(trivPathUser,"Chunky");
        
        String[] selected = new String[]{hand[answer[0]],hand[answer[1]]};
        
        //Index ungleich:
        differentIndicesTest(answer);
        
        //um Flagge zu erreichen:
        assertArrayEquals(new String[]{"a3","a1"}, selected);
        //um nächste anzusteuern
        selected = new String[]{hand[answer[2]],hand[answer[3]],hand[answer[4]]};
        assertArrayEquals(new String[]{"b1","tr","a3"}, selected);
    }

    /*
     * Tim
     * obstacle board, um Loch bei 1,12 herum auf Flagge bei 1,13. spawne bei 1,11
     */
    @Test
    public void moveAroundHoleTest() {
        when(obstacleManager.getRobotsState()).thenReturn(
            "\"hammer,t,3,10,n,nil-nil-nil-nil-nil,nil,0,1:11,0:0;\""
        );
        String[] expectation = new String[] {
            "tl", "a1", "tr", "a2", "tr"
        };
        String[] expectation2 = new String[]{
          "tl","b1","tr","a2"      
        };
        String[] given = new String[] {
            "a1_530", "tl_130", "a3_810",
            "b1_460", "a1_600", "tu_50",
            "a2_780", "tr_380", "tr_380"
        };
        String givenStr = "\"" + String.join(",", given) + ";\"";
        
        when(obstacleManager.getHandState()).thenReturn(givenStr);
        obstacleUser = new ActRUser(obstacleManager);
        String[] hand = setHand(givenStr);
        int[] answer = executeTestMove(obstacleUser,"Chunky");
        
        String[] actual = IntStream.range(0, answer.length)
                .mapToObj(i -> hand[answer[i]])
                .toArray(String[]::new);
        
        boolean sample1 = Arrays.deepEquals(expectation,actual);
        boolean sample2 = Arrays.deepEquals(expectation2,Arrays.copyOfRange(actual,0,4)) && (actual[4].equals("tr") || actual[4].equals("tu"));
        //sollte auf (2,13) mit Blickrichtung East oder South stehen oder aber auf (0,13) mit Blickrichtung Flagge
        assertTrue(sample1||sample2);
    }
    
    /*
     * Stefan
     */
    @Test
    public void moveAroundHoleDiagReachFlagTriggerNextTest(){
        when(obstacleManager.getRobotsState()).thenReturn(
                trivPathRobotLPHP.concat("n").concat(trivPathEmptyRegisterOff).concat("1,9:4").concat(trivPathRespawn));
        
        String[] hand = setHand(obstacleHand);
        
        int[] answer = executeTestMove(obstacleUser, "Chunky");
        
        String[] selected = new String[]{hand[answer[0]],hand[answer[1]],hand[answer[2]],hand[answer[3]],hand[answer[4]]};
        //Index ungleich:
        differentIndicesTest(answer);
        
        //korrekte Kartenauswahl
        assertArrayEquals(new String[]{"tr","a1","tl","a2","a1"}, selected);    
    }
    
    /*
     * Tim
     */
    @Test
    public void useConveyorBeltToReachFlag() {
        when(conveyorManager.getRobotsState()).thenReturn(
            "\"hammer,t,3,10,e,nil-nil-nil-nil-nil,nil,0,0:0,0:0;\""
        );
        String[] given = new String[] {
            "a1_530", "tl_130", "a3_810",
            "b1_460", "a1_600", "tu_50",
            "a2_780", "tr_380", "tr_380"
        };
        String givenStr = "\"" + String.join(",", given) + ";\"";
        when(conveyorManager.getHandState()).thenReturn(givenStr);
        
        conveyorUser = new ActRUser(conveyorManager);
        String[] hand = setHand(givenStr);
        int[] answer = executeTestMove(conveyorUser,"Chunky");
        String[] actual = IntStream.range(0, answer.length)
                .mapToObj(i -> hand[answer[i]])
                .toArray(String[]::new);
        
        //expect: a1,tl,(a1)||(a2),(a2,a3)||(a3,a2)
        assertEquals("a1", actual[0]);
        assertEquals("tl", actual[1]);
        assertTrue(actual[2].equals("a1")||actual[2].equals("a2"));
        assertTrue(actual[3].concat(actual[4]).equals("a2a3")||actual[3].concat(actual[4]).equals("a3a2"));
    }
    
    /*
     * Tim
     * conveyorBoard, spawn (3,15) Richtung Süden
     * bekommt nur einfache Forward Karten und muss genau 5 davon nutzen, 
     * um mithilfe von 2 Gears die Flagge zu erreichen
     */
    @Test
    public void useGearToReachFlag() {
        when(conveyorManager.getRobotsState()).thenReturn(
            "\"hammer,t,3,10,s,nil-nil-nil-nil-nil,nil,1,3:15,0:0;\""
        );
        String[] given = new String[] {
            "a1_100", "tl_100", "a2_100", 
            "a1_100", "tr_100","tu_100",
            "tl_100", "a1_100", "a1_100"
        };
        String givenStr = "\"" + String.join(",", given) + ";\"";
        when(conveyorManager.getHandState()).thenReturn(givenStr);
        
        String[] expectation = new String[] {
            "a1", "a2", "a1", "a1", "a1"
        };
        
        conveyorUser = new ActRUser(conveyorManager);
        String[] hand = setHand(givenStr);
        int[] answer = executeTestMove(conveyorUser,"Chunky");
        String[] actual = IntStream.range(0, answer.length)
                .mapToObj(i -> hand[answer[i]])
                .toArray(String[]::new);
        
        assertArrayEquals(expectation, actual);
    }
    
    /*
     * Stefan
     */
    @Test
    public void skipGearReachFlagTriggerNextTest(){
        when(obstacleManager.getRobotsState()).thenReturn(
                trivPathRobotLPHP.concat("n").concat(trivPathEmptyRegisterOff).concat("0,2:10").concat(trivPathRespawn));
        
        String[] hand = setHand(obstacleHand);
        
        int[] answer = executeTestMove(obstacleUser, "Chunky");
        
        String[] selected = new String[]{hand[answer[0]],hand[answer[1]],hand[answer[2]],hand[answer[3]],hand[answer[4]]};
        //Index ungleich:
        differentIndicesTest(answer);
        
        //korrekte Kartenauswahl 
        assertArrayEquals(new String[]{"a1","a2","tl","a1"}, Arrays.copyOfRange(selected, 0, 4));
        assertTrue(selected[4].equals("b1")||selected[4].equals("tu"));
    }
    
    /*
     * Tim
     * Der Agent soll eine Flagge erreichen, auch wenn er aufgrund
     * seiner übrig bleibenden Karten in ein Loch fahren wird.
     */
    @Test
    public void sacrificeLiveForFlag() {
        when(sacrificeManager.getRobotsState()).thenReturn(
            "\"hammer,t,3,10,n,nil-nil-nil-nil-nil,nil,0,0:0,0:0;\""
        );
        String[] given = new String[] {
            "a2_100", "a2_100", "a3_100", "a3_100", "a3_100",
            
            "tr_100", "a2_100", "tl_100", "a2_100" // für Flagge
        };
        String givenStr = "\"" + String.join(",", given) + ";\"";
        when(sacrificeManager.getHandState()).thenReturn(givenStr);
        sacrificeUser = new ActRUser(sacrificeManager);
        
        String[] expectation = new String[] {
            "tr", "a2", "tl", "a2"
        };
        
        String[] hand = setHand(givenStr);
        int[] answer = executeTestMove(sacrificeUser,"Chunky");
        String[] actual = IntStream.range(0, answer.length)
                .mapToObj(i -> hand[answer[i]])
                .toArray(String[]::new);
        
        assertArrayEquals(expectation, Arrays.copyOfRange(actual, 0, 4));
        assertTrue(actual[4].equals("a2")||actual[4].equals("a3"));
    }
    
    /*
     * Stefan
     */
    @Test
    public void dontSacrificeLastLiveForFlag() {        
        when(sacrificeManager.getRobotsState()).thenReturn("\"hammer,t,1,10,w,nil-nil-nil-nil-nil,nil,1,11:5,0:0;\"");
        when(sacrificeManager.getHandState()).thenReturn("\"a2_100,a2_100,a3_100,a3_100,a3_100,tr_100,a2_100,tl_100,a2_100;\"");
        
        String hand[] = setHand("a2_100,a2_100,a3_100,a3_100,a3_100,tr_100,a2_100,tl_100,a2_100;");

        int[] answer = executeTestMove(sacrificeUser,"Chunky");

        String[] selected = new String[]{hand[answer[0]],hand[answer[1]],hand[answer[2]],hand[answer[3]],hand[answer[4]]};
      //Index ungleich:
        differentIndicesTest(answer);
        //Steuere nicht die aktuelle Flagge an
        assertTrue(!Arrays.deepEquals(new String[]{"tr","a2","tl","a2"},Arrays.copyOfRange(selected,0,4)));
    }
    
    /*
     * Stefan
     */
    @Test
    public void avoidPusherTest(){
        when(scrapPressManager.getRobotsState()).thenReturn("\"hammer,t,3,10,n,nil-nil-nil-nil-nil,nil,2,5:3,1:1;\"");

        String[] hand = setHand(scrapPressHand);
        
        int[] answer = executeTestMove(scrapPressUser, "Chunky");
        
        String[] selected = new String[]{hand[answer[0]],hand[answer[1]],hand[answer[2]],hand[answer[3]],hand[answer[4]]};
        //Index ungleich:
        differentIndicesTest(answer);

        //entweder move 3 oder move 1 gefolgt von move 2 oder move 3 
        assertTrue(selected[0].equals("a3")||(selected[0].equals("a1")&&(selected[1].equals("a2")||selected[1].equals("a3"))));
    }
    
    /*
     * Stefan
     */
    @Test
    public void allRegisterLockedTest(){
        when(scrapPressManager.getRobotsState()).thenReturn("\"hammer,t,3,0,e,tl_50-tr_80-tu_30-b1_460-tu_130,nil,0,8:12,1:1;\"");
        when(scrapPressManager.getHandState()).thenReturn("\";\"");
        
        int[] answer = executeTestMove(scrapPressUser, "Chunky");

        assertArrayEquals(new int[]{-1,-1,-1,-1,-1}, answer);
    }
    
    /*
     * Stefan
     */
    @Test
    public void skipConveyorToHoleTest(){
        when(scrapPressManager.getRobotsState()).thenReturn("\"hammer,t,3,10,n,nil-nil-nil-nil-nil,nil,0,9:10,1:1;\"");
        when(scrapPressManager.getHandState()).thenReturn("\"a1_200,a1_300,a2_400,b1_100,tl_50,tr_80,tu_30,b1_460,tu_130;\"");
        
        String[] hand = setHand("a1_200,a1_300,a2_400,b1_100,tr_80,a1_50,tu_30,b1_460,tu_130;");
        
        int[] answer = executeTestMove(scrapPressUser, "Chunky");
        
        String[] selected = new String[]{hand[answer[0]],hand[answer[1]],hand[answer[2]],hand[answer[3]],hand[answer[4]]};
        //Index ungleich:
        differentIndicesTest(answer);
        
        //korrekte Kartenauswahl 
        assertArrayEquals(new String[]{"a1","a2","a1","tu","b1"}, selected);
    }
    
    /*
     * Stefan
     */
    @Test
    public void passPassivePusherTest(){
        when(scrapPressManager.getRobotsState()).thenReturn("\"hammer,t,3,10,n,nil-nil-nil-nil-nil,nil,0,8:2,1:1;\"");
        when(scrapPressManager.getHandState()).thenReturn("\"a1_200,a1_300,a2_400,b1_100,tr_80,a1_50,a3_830,b1_460,tu_130;\"");
        
        String[] hand = setHand("a1_200,a1_300,a2_400,b1_100,tr_80,a1_50,a3_830,b1_460,tu_130;");
        
        int[] answer = executeTestMove(scrapPressUser, "Chunky");
        
        String[] selected = new String[]{hand[answer[0]],hand[answer[1]],hand[answer[2]],hand[answer[3]],hand[answer[4]]};
        //Index ungleich:
        differentIndicesTest(answer);
        
        //korrekte Kartenauswahl 
        assertArrayEquals(new String[]{"a3","a1","a2","a1","a1"}, selected);
    }
    
    /*
     * Stefan
     */
    @Test
    public void goAroundHole1Test(){
        when(scrapPressManager.getRobotsState()).thenReturn("\"hammer,t,3,10,n,nil-nil-nil-nil-nil,nil,0,10:8,1:1;\"");
        when(scrapPressManager.getHandState()).thenReturn("\"a1_200,a1_300,a2_400,b1_100,tr_80,a1_50,a2_500,b1_460,tu_130;\"");
        
        String[] hand = setHand("a1_200,a1_300,a2_400,b1_100,tr_80,a1_50,a2_500,b1_460,tu_130;");
        
        int[] answer = executeTestMove(scrapPressUser, "Chunky");
        
        String[] selected = new String[]{hand[answer[0]],hand[answer[1]],hand[answer[2]],hand[answer[3]],hand[answer[4]]};
        //Index ungleich:
        differentIndicesTest(answer);
        
        //korrekte Kartenauswahl 
        assertArrayEquals(new String[]{"a2","a1","tr","b1","tu"}, selected);
    }
    
    /*
     * Stefan
     */
    @Test
    public void handlePusherAndLaserTest(){
        when(obstacleManager.getRobotsState()).thenReturn("\"hammer,t,3,10,e,nil-nil-nil-nil-nil,nil,1,5:8,1:1;\"");
        when(obstacleManager.getHandState()).thenReturn("\"a1_540,tu_50,a2_550,tr_380,a2_560,a2_570,a2_580,tl_130,a1_460\"");
        
        String[] hand = setHand("a1_540,tu_50,a2_550,tr_380,a2_560,a2_570,a2_580,tl_130,a1_460");
        int[] answer = executeTestMove(obstacleUser,"Chunky");
        
        String[] selected = new String[]{hand[answer[0]],hand[answer[1]],hand[answer[2]],hand[answer[3]],hand[answer[4]]};
      //Index ungleich:
        differentIndicesTest(answer);
        assertArrayEquals(new String[]{"a1","a2","a2","tr","a2"},selected);
    }
    
    /*
     * Stefan
     */
    @Test
    public void fireIndirectHoleConnectionNotTrue(){
        when(conveyorManager.getRobotsState()).thenReturn("\"hammer,t,3,10,e,nil-nil-nil-nil-nil,nil,2,3:9,0:0;\"");
        when(conveyorManager.getHandState()).thenReturn("\"a2_100,a1_100,a1_100,a1_100,a1_100,a1_100,a1_100,a1_100,a1_100;\"");
        
        String[] hand = setHand("a2_100,a1_100,a1_100,a1_100,a1_100,a1_100,a1_100,a1_100,a1_100");
        int[] answer = executeTestMove(conveyorUser,"Chunky");
        
        String[] selected = new String[]{hand[answer[0]],hand[answer[1]],hand[answer[2]],hand[answer[3]],hand[answer[4]]};
        //Index ungleich:
        differentIndicesTest(answer);
        assertArrayEquals(new String[]{"a1","a1","a2","a1","a1"},selected);
    }
    
    /*
     * Stefan
     */
    @Test
    public void killRobotByLasersTest(){
        when(scrapPressManager.getRobotsState()).thenReturn("\"hammer,t,3,3,e,nil-nil-a1_100-a1_100-a1_100,nil,2,0:0,0:0;\"");
        when(scrapPressManager.getHandState()).thenReturn("\"a2_100,a1_100;\"");
        
        String[] hand = setHand("a2_100,a1_100;");
        int[] answer = executeTestMove(scrapPressUser,"Chunky");
        
        String[] selected = new String[]{hand[answer[0]],hand[answer[1]]};
        //Index ungleich:
        differentIndicesTest(answer);
        
        assertArrayEquals(new String[]{"a2","a1"},selected);
    }
    
    /*
     * Stefan
     */
    @Test
    public void twoBlockadesTest(){
        when(obstacleManager.getRobotsState()).thenReturn("\"hammer,t,3,10,w,nil-nil-nil-nil-nil,nil,0,5:14,0:0;\"");
        when(obstacleManager.getHandState()).thenReturn("\"a1_200,a2_300,a3_400,b1_100,tl_50,tr_80,a1_30,b1_460,tu_130;\"");
        
        String[] hand = setHand("a1_200,a2_300,a3_400,b1_100,tl_50,tr_80,a1_30,b1_460,tu_130;");
        
        int[] answer = executeTestMove(obstacleUser,"Chunky");
        
        String[] selected = new String[]{hand[answer[0]],hand[answer[1]],hand[answer[2]],hand[answer[3]],hand[answer[4]]};
        //Index ungleich:
        differentIndicesTest(answer);
        
        assertArrayEquals(new String[]{"a1","tl","a1","tr","a3"},selected);
    }
    
    /*
     * Stefan
     */
    @Test
    @Ignore
    public void terminateTurnTest(){
        when(trivPathManager.getRobotsState()).thenReturn(
                trivPathRobotLPHP.concat("n").concat(trivPathEmptyRegisterOff).concat("2,5:2").concat(trivPathRespawn));
        trivPathUser.saveCurrentGameStatus();
        
        new Thread(() -> {
            LOGGO.info("<<< Starte lade und starte Chunky, Echtzeit!");
            trivPathUser.loadActR("Chunky");
            trivPathUser.makeMove("t");
        }).start();
        
        try {
            LOGGO.info("<<< try Thread-Sleep");
            Thread.currentThread().sleep(11000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /*TimerThread wait = new TimerThread();
        wait.start();
        
        while(wait.isAlive());*/
        
        int[] selected = trivPathUser.terminateTurn();
        LOGGO.info("<<< bis Abbruch ausgewählt: " + selected[0] + " " + selected[1]  + " " + selected[2] + " " + selected[3] + " " + selected[4]);
        
        assertNotNull(selected);
        
        int emptyRegister = 0;
        for(int ii:selected){
            if(ii == -1){
                emptyRegister++;
            }
        }
        assertTrue(emptyRegister > 0);
        assertTrue(emptyRegister < 5);
    }
    
    private class TimerThread extends Thread {
        public void run() {
            try {
                Thread.sleep(11000);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
    
    /*
     * Stefan
     */
    private int[] executeTestMove(ActRUser user, String ai){
        user.saveCurrentGameStatus();
        user.loadActR(ai);
        int[] answer = user.makeMove("nil");
        return answer;
    }
    
    private void differentIndicesTest(int[] array){
        for(int ii = 0;ii < array.length; ii++){
            if(array[ii] != -1){
                for(int jj = ii+1; jj < array.length; jj++){
                    assertTrue("Mindestens ein Index doppelt gewählt!",array[ii] != array[jj]);
                }
            }
        }
    }
    
    /*
     * Stefan
     */
    private String[] setHand(String handCode){
        String[] hand = (handCode.replaceAll("\"", "")).split(";")[0].split(",");
        for(int ii = 0; ii < hand.length; ii ++){
            hand[ii] = hand[ii].split("_")[0];
        }
        return hand;
    }
}
