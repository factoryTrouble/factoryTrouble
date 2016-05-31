package de.uni_bremen.factroytrouble.ai.ki3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import de.uni_bremen.factroytrouble.ai.ais.ActRAIPlayer;
import de.uni_bremen.factroytrouble.board.Board;
import de.uni_bremen.factroytrouble.board.Dock;
import de.uni_bremen.factroytrouble.board.Field;
import de.uni_bremen.factroytrouble.board.GameTile;
import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.gameobjects.ConveyorBelt;
import de.uni_bremen.factroytrouble.gameobjects.FieldObject;
import de.uni_bremen.factroytrouble.gameobjects.Flag;
import de.uni_bremen.factroytrouble.gameobjects.Gear;
import de.uni_bremen.factroytrouble.gameobjects.Hole;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.gameobjects.Wall;
import de.uni_bremen.factroytrouble.gameobjects.Workshop;
import de.uni_bremen.factroytrouble.observer.Event;
import de.uni_bremen.factroytrouble.player.GameMasterFactory;
import de.uni_bremen.factroytrouble.player.Master;
import de.uni_bremen.factroytrouble.player.Player;
import de.uni_bremen.factroytrouble.player.ProgramCard;
import de.uni_bremen.factroytrouble.player.TurnLeftCard;
import de.uni_bremen.factroytrouble.player.TurnRightCard;
import de.uni_bremen.factroytrouble.player.UturnCard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class ScoreManagerTest {
    private static final int GAME_ID = 0;

    @Mock
    private GameMasterFactory factory;
    @Mock
    private Master gameMaster;
    @Mock
    private Board robotStew, whirlwind;
    @Mock
    private Dock dockA,dockB;
    @Mock
    private Field fieldE,fieldH;
    @Mock
    private ProgramCard forward1, forward2, forward3, backward;
    @Mock
    private TurnLeftCard turnleft;
    @Mock
    private TurnRightCard turnright;
    @Mock
    private UturnCard uturn;

    @Mock
    private ActRAIPlayer player;
    @Mock
    private Player player_1;
    @Mock
    private Robot robot, robot_1;
    @Mock
    private Tile current, respawn, current_1, respawn_1;

    @Mock
    private Wall wno, wea, wso, wwe, wwel, wnol, wnoll, wnolll, weal, wsol, 
        wnop24, wnop135, weap24, weap135, wsop24, wsop135, wwep24, wwep135;
    @Mock
    private ConveyorBelt comNo, comEa, comSo, comWe, expNo, expEa, expSo, expWe;
    @Mock
    private Hole hole;
    @Mock
    private Gear rotL, rotR;
    @Mock
    private Workshop shop, adShop;
    @Mock
    private Flag flag1,flag2,flag3;
    
    private int[] pusherPhases = { -1 };
    private int[] pusher24 = {2,4};
    private int[] pusher135 = {1,3,5};
    
    private List<Player> masterPlayers;
    
    private Map<Point,Field> stewFields, whirlFields; 
    private Map<Point, Tile> tilesFieldE,tilesFieldH,tilesDockA,tilesDockB;
    
    private ScoreManager manager;
    
    @Before
    public void initTest() {
        //Walls, Fieldobjects
        when(wno.getOrientation()).thenReturn(Orientation.NORTH);
        when(wnol.getOrientation()).thenReturn(Orientation.NORTH);
        when(wnoll.getOrientation()).thenReturn(Orientation.NORTH);
        when(wnolll.getOrientation()).thenReturn(Orientation.NORTH);
        when(wea.getOrientation()).thenReturn(Orientation.EAST);
        when(weal.getOrientation()).thenReturn(Orientation.EAST);
        when(wso.getOrientation()).thenReturn(Orientation.SOUTH);
        when(wsol.getOrientation()).thenReturn(Orientation.SOUTH);
        when(wwe.getOrientation()).thenReturn(Orientation.WEST);
        when(wwel.getOrientation()).thenReturn(Orientation.WEST);
        when(wnop24.getOrientation()).thenReturn(Orientation.NORTH);
        when(wnop135.getOrientation()).thenReturn(Orientation.NORTH);
        when(weap24.getOrientation()).thenReturn(Orientation.EAST);
        when(weap135.getOrientation()).thenReturn(Orientation.EAST);
        when(wsop24.getOrientation()).thenReturn(Orientation.SOUTH);
        when(wsop135.getOrientation()).thenReturn(Orientation.SOUTH);
        when(wwep24.getOrientation()).thenReturn(Orientation.WEST);
        when(wwep135.getOrientation()).thenReturn(Orientation.WEST);
        
        when(wno.hasLaser()).thenReturn(0);
        when(wnol.hasLaser()).thenReturn(1);
        when(wnoll.hasLaser()).thenReturn(2);
        when(wnolll.hasLaser()).thenReturn(3);
        when(wea.hasLaser()).thenReturn(0);
        when(weal.hasLaser()).thenReturn(1);
        when(wso.hasLaser()).thenReturn(0);
        when(wsol.hasLaser()).thenReturn(1);
        when(wwe.hasLaser()).thenReturn(0);
        when(wwel.hasLaser()).thenReturn(1);
        when(wnop24.hasLaser()).thenReturn(0); 
        when(wnop135.hasLaser()).thenReturn(0);
        when(weap24.hasLaser()).thenReturn(0);
        when(weap135.hasLaser()).thenReturn(0);
        when(wsop24.hasLaser()).thenReturn(0);
        when(wsop135.hasLaser()).thenReturn(0);
        when(wwep24.hasLaser()).thenReturn(0);
        when(wwep135.hasLaser()).thenReturn(0);
        
        when(wno.getPusherPhases()).thenReturn(pusherPhases);
        when(wnol.getPusherPhases()).thenReturn(pusherPhases);
        when(wnoll.getPusherPhases()).thenReturn(pusherPhases);
        when(wnolll.getPusherPhases()).thenReturn(pusherPhases);
        when(wea.getPusherPhases()).thenReturn(pusherPhases);
        when(weal.getPusherPhases()).thenReturn(pusherPhases);
        when(wso.getPusherPhases()).thenReturn(pusherPhases);
        when(wsol.getPusherPhases()).thenReturn(pusherPhases);
        when(wwe.getPusherPhases()).thenReturn(pusherPhases);
        when(wwel.getPusherPhases()).thenReturn(pusherPhases);
        when(wnop24.getPusherPhases()).thenReturn(pusher24);
        when(wnop135.getPusherPhases()).thenReturn(pusher135); 
        when(weap24.getPusherPhases()).thenReturn(pusher24);
        when(weap135.getPusherPhases()).thenReturn(pusher135); 
        when(wsop24.getPusherPhases()).thenReturn(pusher24);
        when(wsop135.getPusherPhases()).thenReturn(pusher135);
        when(wwep24.getPusherPhases()).thenReturn(pusher24);
        when(wwep135.getPusherPhases()).thenReturn(pusher135);
        
        when(comNo.getOrientation()).thenReturn(Orientation.NORTH);
        when(comEa.getOrientation()).thenReturn(Orientation.EAST);
        when(comSo.getOrientation()).thenReturn(Orientation.SOUTH);
        when(comWe.getOrientation()).thenReturn(Orientation.WEST);
        
        when(comNo.isExpress()).thenReturn(false);
        when(comEa.isExpress()).thenReturn(false);
        when(comSo.isExpress()).thenReturn(false);
        when(comWe.isExpress()).thenReturn(false);
        
        when(expNo.getOrientation()).thenReturn(Orientation.NORTH);
        when(expEa.getOrientation()).thenReturn(Orientation.EAST);
        when(expSo.getOrientation()).thenReturn(Orientation.SOUTH);
        when(expWe.getOrientation()).thenReturn(Orientation.WEST);
        
        when(expNo.isExpress()).thenReturn(true);
        when(expEa.isExpress()).thenReturn(true);
        when(expSo.isExpress()).thenReturn(true);
        when(expWe.isExpress()).thenReturn(true);
        
        when(rotL.rotatesLeft()).thenReturn(true);
        when(rotR.rotatesLeft()).thenReturn(false);
        
        when(shop.isAdvancedWorkshop()).thenReturn(false);
        when(shop.isAdvancedWorkshop()).thenReturn(true);
        
        when(flag1.getNumber()).thenReturn(1);
        when(flag2.getNumber()).thenReturn(2);
        when(flag3.getNumber()).thenReturn(3);
        //Handkarten und Spieler
        when(forward1.getRange()).thenReturn(1);
        when(forward2.getRange()).thenReturn(2);
        when(forward3.getRange()).thenReturn(3);
        when(backward.getRange()).thenReturn(-1);
        when(turnleft.getRange()).thenReturn(0);
        when(turnright.getRange()).thenReturn(0);
        when(uturn.getRange()).thenReturn(0);

        when(forward1.getPriority()).thenReturn(200);
        when(forward2.getPriority()).thenReturn(300);
        when(forward3.getPriority()).thenReturn(400);
        when(backward.getPriority()).thenReturn(100);
        when(turnleft.getPriority()).thenReturn(50);
        when(turnright.getPriority()).thenReturn(80);
        when(uturn.getPriority()).thenReturn(30);
        
        when(player.getRobot()).thenReturn(robot);
        when(player.getPlayerCards()).thenReturn(Arrays.asList(forward1, forward2, forward3, backward, turnleft, turnright, uturn));
        when(player_1.getRobot()).thenReturn(robot_1);
        //Roboter samt Attribute
        when(current.getAbsoluteCoordinates()).thenReturn(new Point(7, 11));
        when(respawn.getAbsoluteCoordinates()).thenReturn(new Point(1, 1));

        when(current_1.getAbsoluteCoordinates()).thenReturn(new Point(11, 11));
        when(respawn_1.getAbsoluteCoordinates()).thenReturn(new Point(4, 2));

        when(robot.getName()).thenReturn("first second");
        when(robot.getLives()).thenReturn(2);
        when(robot.getHP()).thenReturn(3);
        when(robot.getOrientation()).thenReturn(Orientation.NORTH);
        when(robot.getRegisters()).thenReturn(new ProgramCard[] { null, null, forward1, backward, uturn });
        when(robot.isPoweredDown()).thenReturn(false);
        when(robot.getFlagCounterStatus()).thenReturn(2);
        when(robot.getCurrentTile()).thenReturn(current);
        when(robot.getRespawnPoint()).thenReturn(respawn);

        when(robot_1.getName()).thenReturn("second third");
        when(robot_1.getLives()).thenReturn(3);
        when(robot_1.getHP()).thenReturn(10);
        when(robot_1.getOrientation()).thenReturn(Orientation.EAST);
        when(robot_1.getRegisters()).thenReturn(new ProgramCard[] { null, null, null, null, null });
        when(robot_1.isPoweredDown()).thenReturn(true);
        when(robot_1.getFlagCounterStatus()).thenReturn(0);
        when(robot_1.getCurrentTile()).thenReturn(current_1);
        when(robot_1.getRespawnPoint()).thenReturn(respawn_1);
        //sonstiges Setup
        masterPlayers = new ArrayList<>();
        masterPlayers.add(player);
        
        when(factory.getMaster(GAME_ID)).thenReturn(gameMaster);
        when(gameMaster.getBoard()).thenReturn(robotStew);
        
        when(robotStew.getAbsoluteCoordinates(any(GameTile.class))).thenAnswer(invocation -> {
            return ((Tile) invocation.getArguments()[0]).getAbsoluteCoordinates();   
        });
        
        when(gameMaster.getPlayers()).thenReturn(masterPlayers);
        
        stewFields = new HashMap<>();
        stewFields.put(new Point(0,0), (Field) dockB);
        stewFields.put(new Point(0,1), fieldE);
        
        whirlFields = new HashMap<>();
        whirlFields.put(new Point(0,0), (Field) dockA);
        whirlFields.put(new Point(0,1), fieldH);
        
        manager = new ScoreManager(factory, player);
    }

    @Test
    public void encodeHandTest() {
        String expected = "\"a1_200,a2_300,a3_400,b1_100,tl_50,tr_80,tu_30;\"";
        manager.getHandState();

        assertEquals(expected, manager.getHandState());
    }

    @Test
    public void encodeRobotStewTest() {
        String dockStrB = "ti,ti,1e,1e,1e,ti,ti_ww,1w,1w,1w,ti,ti;" 
                + "1e,1e,1s,ti,ti,ti,ti_ww,ti,ti,1s,1w,1w;"
                + "ti,ti_ww,ti_ww,ti,ti,ti,ti,ti,ti,ti_we,ti_we,ti;"
                + "ti,ti,ti_wn,ti,ti_wn_ww,ti,ti,ti_wn_we,ti,ti_wn,ti,ti;";
        String fieldStrE = "re,ti,ti_ws,ti,ti_ws,1s,1n,ti_ws,ti,ti_ws,ti,ti;" + "ti,de,f3,ti,ti,1s,1e,1e,1e,1e,ti_wn_ws_ls3,1e;"
                + "ti_ww,ti_wn,ti_ww_lw1,ti_lw1,ti_lw1,rr_lw1,rl_lw1,ti_we_lw1,ti_wn,re,ti,ti_we;"
                + "2w,2w_ls1,2w,2w,2w,2w,2w,2w,ti_ls2,1w,1w,1w;" + "ti_ww,ti_ls1,2s,ti,ti,ti,ti,ti,rr_ls2,f2,ti,ti_we;"
                + "2e,2e_wn_ls1,2s,ti_we_ws,1n,re,ti,ti,rl_ls2,de,ti,ti;"
                + "1w,1w,1w,1w,rl,1e_ws,1e,1e,ti_wn_ls2,ti,ti,ti;" + "f1_ww,ti,ti,ti,ti,1s,de,ti,ti,ti,ti_ws_ln1,ti_we;"
                + "1e,1e,1e,ti,ti_ww_lw1,rr_lw1,ti_we_lw1,ti,rl,1e,1e_ln1,1e;"
                + "ti_ww,1e,1e,de,re,1s,ti,ti,1s,de,ti_ln1,ti_we;" + "ti,1s,ti,ti,ti,1s,ti_we,ti,1s,ti,ti_ws,ti;"
                + "ti,1s,ti_wn,ti,ti_wn,1s,ti,ti_wn,1s,ti_wn,ti,re;";
        String expected = "\"".concat(dockStrB).concat(fieldStrE).concat("\"");         
        
        //cs = comSo, ew = expWe, ...
        String[][] stewDock = new String[][]{
            {"ti","ti","ce","ce","ce","ti","ww","cw","cw","cw","ti","ti"},
            {"ce","ce","cs","ti","ti","ti","ww","ti","ti","cs","cw","cw"},
            {"ti","ww","ww","ti","ti","ti","ti","ti","ti","we","we","ti"},
            {"ti","ti","wn","ti","wnww","ti","ti","wnwe","ti","wn","ti","ti"},
        };
        String[][] stewField = new String[][]{
            {"cr","ti","ws","ti","ws","cs","cn","ws","ti","ws","ti","ti"},
            {"ti","ho","f3","ti","ti","cs","ce","ce","ce","ce","wnlllws","ce"},
            {"ww","wn","ww","ti","ti","rr","rl","wel","wn","ar","ti","we"},
            {"ew","ew","ew","ew","ew","ew","ew","ew","ti","cw","cw","cw"},
            {"ww","ti","es","ti","ti","ti","ti","ti","rr","f2","ti","we"},
            {"ee","eewnl","es","wews","cn","ar","ti","ti","rl","ho","ti","ti"},
            {"cw","cw","cw","cw","rl","cews","ce","ce","wnll","ti","ti","ti"},
            {"f1ww","ti","ti","ti","ti","cs","ho","ti","ti","ti","wsl","we"},
            {"ce","ce","ce","ti","ww","rr","wel","ti","rl","ce","ce","ce"},
            {"ww","ce","ce","ho","ar","cs","ti","ti","cs","ho","ti","we"},
            {"ti","cs","ti","ti","ti","cs","we","ti","cs","ti","ws","ti"},
            {"ti","cs","wn","ti","wn","cs","ti","wn","cs","wn","ti","cr"}
        };
        tilesDockB = createTileMap(robotStew, 0, stewDock);
        tilesFieldE = createTileMap(robotStew, 4, stewField);
        when(robotStew.getFields()).thenReturn(stewFields);
        when(fieldE.getTiles()).thenReturn(tilesFieldE);
        when(dockB.getTiles()).thenReturn(tilesDockB);
        
        manager = new ScoreManager(factory, player);
        
        assertEquals(expected, manager.getBoardState());
    }

    @Test
    public void encodeWhirlwindTest() {
        when(gameMaster.getBoard()).thenReturn(whirlwind);
        
        when(whirlwind.getAbsoluteCoordinates(any(GameTile.class))).thenAnswer(invocation -> {
            return ((Tile) invocation.getArguments()[0]).getAbsoluteCoordinates();   
        });
        
        String dockStrA = "ti,ti,ti_ws,ti,ti_ws,ti,ti,ti_ws,ti,ti_ws,ti,ti;"
                + "ti,ti_ww,ti,ti_ww,ti,ti_ww,ti_ww,ti_ww,ti,ti_ww,ti,ti_ww;" 
                + "ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti;"
                + "ti,ti,ti_wn,ti,ti_wn,ti,ti,ti_wn,ti,ti_wn,ti,ti;";
        String fieldStrH = "ti,ti,ti_ws_pn2,f2,ti_ws_pn3,ti,2n,ti_ws_pn3,ti,ti_ws_pn2,2n,re;"
                + "2e,2n,2w,2w,2w,2w,2w,2w,2w,2w,2w,ti;" 
                + "ti_ww_pe2,2n,1n,1w,1w,1w,1w,1w,1w,1w,1w,ti_we_pw2;"
                + "re,2n,1n,2n,2w,2w,2w_ws_ln1,2w,2w,2w,1s,ti;"
                + "ti_ww_pe3,2n,1n,2n,1n,1w_ws_ln1,1w_ln1,1w,1w,2s,1s,ti_we_pw3;"
                + "2e,2n,1n,2n_ww_le1,1n_le1,de_le1_ln1,de_le1_ln1,2w_we_le1,1s,2s,1s,f3;"
                + "1w,2n,1n,2n,1e_ww_le1,de_ln1_le1,de_ln1_le1,2s_le1,1s_we_le1,2s,1s,1w;"
                + "ti_ww_pe3,2n,1n,2e,2e,2e_ln1,2e_wn_ln1,2s,1s,2s,1s,ti_we_pw3;"
                + "ti,2n,1e,1e,1e,1e_wn_ln1,1e,1e,1s,2s,1s,re;" 
                + "ti_ww_pe2,2e,2e,2e,2e,2e,2e,2e,2e,2s,1s,ti_we_pw2;"
                + "ti,1e,1e,1e,1e,1e,1e,1e,1e,1e,1s,1w;"
                + "re,1s,ti_wn_ps2,ti,ti_wn_ps3,1s,1n,ti_wn_ps3,f1,ti_wn_ps2,ti,ti;";
        String expected = "\"".concat(dockStrA).concat(fieldStrH).concat("\"");
        
        String[][] whirlDock = new String[][]{
            {"ti","ti","ws","ti","ws","ti","ti","ws","ti","ws","ti","ti"},
            {"ti","ww","ti","ww","ti","ww","ww","ww","ti","ww","ti","ww"},
            {"ti","ti","ti","ti","ti","ti","ti","ti","ti","ti","ti","ti"},
            {"ti","ti","wn","ti","wn","ti","ti","wn","ti","wn","ti","ti"},
        };
        String[][] whirlField = new String[][]{
            {"ti","ti","wsp24","f2","wsp135","ti","en","wsp135","ti","wsp24","en","cr"},
            {"ee","en","ew","ew","ew","ew","ew","ew","ew","ew","ew","ti"},
            {"wwp24","en","cn","cw","cw","cw","cw","cw","cw","cw","cw","wep24"},
            {"ar","en","cn","en","ew","ew","ewwsl","ew","ew","ew","cs","ti"},
            {"wwp135","en","cn","en","cn","cwwsl","cw","cw","cw","es","cs","wep135"},
            {"ee","en","cn","enwwl","cn","ho","ho","ewwe","cs","es","cs","f3"},
            {"cw","en","cn","en","cewwl","ho","ho","es","cswe","es","cs","cw"},
            {"wwp135","en","cn","ee","ee","ee","eewn","es","cs","es","cs","wep135"},
            {"ti","en","ce","ce","ce","cewn","ce","ce","cs","es","cs","ar"},
            {"wwp24","ee","ee","ee","ee","ee","ee","ee","ee","es","cs","wep24"},
            {"ti","ce","ce","ce","ce","ce","ce","ce","ce","ce","cs","cw"},
            {"cr","cs","wnp24","ti","wnp135","cs","cn","wnp135","f1","wnp24","ti","ti"}
        };
        tilesDockA = createTileMap(whirlwind, 0, whirlDock);
        tilesFieldH = createTileMap(whirlwind, 4, whirlField);
        
        when(whirlwind.getFields()).thenReturn(whirlFields);
        when(fieldH.getTiles()).thenReturn(tilesFieldH);
        when(dockA.getTiles()).thenReturn(tilesDockA);
        
        manager = new ScoreManager(factory,player);
        
        assertEquals(expected, manager.getBoardState());
    }

    @Test
    public void encodeSingleRobotTest() {
        String expected = "\"first,t,2,3,n,nil-nil-a1_200-b1_100-tu_30,nil,2,7:11,1:1;\"";
        
        manager.spam(robot, Event.SHOT, null);

        assertEquals(expected, manager.getRobotsState());
    }
    
    @Test
    public void refreshRobotsStateTest(){
        String expected = "\"first,t,2,10,n,nil-nil-nil-nil-nil,nil,2,7:11,1:1;\"";
        manager.spam(robot, Event.SHOT, null);
        
        when(robot.getName()).thenReturn("first second");
        when(robot.getLives()).thenReturn(2);
        when(robot.getHP()).thenReturn(10);
        when(robot.getOrientation()).thenReturn(Orientation.NORTH);
        when(robot.getRegisters()).thenReturn(new ProgramCard[] {null, null, null, null, null});
        when(robot.isPoweredDown()).thenReturn(false);
        when(robot.getFlagCounterStatus()).thenReturn(2);
        when(robot.getCurrentTile()).thenReturn(current);
        when(robot.getRespawnPoint()).thenReturn(respawn);
        
        manager.spam(robot, Event.HEALED, null);
        
        assertEquals(expected, manager.getRobotsState());
    }

    @Test
    public void encodeMoreRobotsTest() {
        masterPlayers.add(player_1);
        String expected = "\"second,nil,3,10,e,nil-nil-nil-nil-nil,t,0,11:11,4:2;"
                + "first,t,2,3,n,nil-nil-a1_200-b1_100-tu_30,nil,2,7:11,1:1;\"";
        String expected1 = "\"first,t,2,3,n,nil-nil-a1_200-b1_100-tu_30,nil,2,7:11,1:1;"
                + "second,nil,3,10,e,nil-nil-nil-nil-nil,t,0,11:11,4:2;\"";

        manager.spam(robot, Event.HEALED, null);
        manager.spam(robot_1, Event.HEALED, null);

        assertTrue(expected.equals(manager.getRobotsState()) || expected1.equals(manager.getRobotsState()));
    }
    
    private Map<Point,Tile> createTileMap(Board board, int yOffset, String[][] code){
        Map<Point,Tile> field = new HashMap<>();
        
        for(int row = 0; row < code.length; row++){
            for(int col = 0; col < code[0].length; col++){
                field.put(new Point(col,row), makeTile(board, new Point(col,row+yOffset), new Point(col,row), code[row][col]));
            }
        }
        
        return field;
    }
    
    /*
     * 
     */
    private Tile makeTile(Board board, Point absolute, Point relative, String tile){
        switch(tile.toLowerCase()){
        case "ti": return createTile(board,absolute,relative,new ArrayList<Wall>(),null);
        //Walls
        case "ww": 
            return createTile(board,absolute,relative,new ArrayList<Wall>(Arrays.asList(new Wall[]{wwe})),null);
        case "wwp24":
            return createTile(board,absolute,relative,new ArrayList<Wall>(Arrays.asList(new Wall[]{wwep24})),null);
        case "wwp135":
            return createTile(board,absolute,relative,new ArrayList<Wall>(Arrays.asList(new Wall[]{wwep135})),null);
        case "we": 
            return createTile(board,absolute,relative,new ArrayList<Wall>(Arrays.asList(new Wall[]{wea})),null);
        case "wel":
            return createTile(board,absolute,relative,new ArrayList<Wall>(Arrays.asList(new Wall[]{weal})),null);
        case "wep24":
            return createTile(board,absolute,relative,new ArrayList<Wall>(Arrays.asList(new Wall[]{weap24})),null);
        case "wep135":
            return createTile(board,absolute,relative,new ArrayList<Wall>(Arrays.asList(new Wall[]{weap135})),null);
        case "ws": 
            return createTile(board,absolute,relative,new ArrayList<Wall>(Arrays.asList(new Wall[]{wso})),null);
        case "wsl":
            return createTile(board,absolute,relative,new ArrayList<Wall>(Arrays.asList(new Wall[]{wsol})),null);
        case "wsp24":
            return createTile(board,absolute,relative,new ArrayList<Wall>(Arrays.asList(new Wall[]{wsop24})),null);
        case "wsp135":
            return createTile(board,absolute,relative,new ArrayList<Wall>(Arrays.asList(new Wall[]{wsop135})),null);
        case "wn": 
            return createTile(board,absolute,relative,new ArrayList<Wall>(Arrays.asList(new Wall[]{wno})),null);
        case "wnll":
            return createTile(board,absolute,relative,new ArrayList<Wall>(Arrays.asList(new Wall[]{wnoll})),null);
        case "wnwe": 
            return createTile(board,absolute,relative,new ArrayList<Wall>(Arrays.asList(new Wall[]{wno,wea})),null);
        case "wnww": 
            return createTile(board,absolute,relative,new ArrayList<Wall>(Arrays.asList(new Wall[]{wno,wwe})),null);
        case "wnlllws":
            return createTile(board,absolute,relative,new ArrayList<Wall>(Arrays.asList(new Wall[]{wnolll,wso})),null);
        case "wnp24":
            return createTile(board,absolute,relative,new ArrayList<Wall>(Arrays.asList(new Wall[]{wnop24})),null);
        case "wnp135":
            return createTile(board,absolute,relative,new ArrayList<Wall>(Arrays.asList(new Wall[]{wnop135})),null);
        case "wews":
            return createTile(board,absolute,relative,new ArrayList<Wall>(Arrays.asList(new Wall[]{wea,wso})),null);
        //Workshops
        case "cr": return createTile(board,absolute,relative,new ArrayList<Wall>(),shop);
        case "ar": return createTile(board,absolute,relative,new ArrayList<Wall>(),adShop);
        //Holes
        case "ho": return createTile(board,absolute,relative,new ArrayList<Wall>(),hole);
        //Gears
        case "rr": return createTile(board,absolute,relative,new ArrayList<Wall>(),rotR);
        case "rl": return createTile(board,absolute,relative,new ArrayList<Wall>(),rotL);
        //Förderbänder
        case "cn": return createTile(board,absolute,relative,new ArrayList<Wall>(),comNo);
        case "cnwn":
            return createTile(board,absolute,relative,new ArrayList<Wall>(Arrays.asList(new Wall[]{wno})),comNo);
        case "ce": return createTile(board,absolute,relative,new ArrayList<Wall>(),comEa);
        case "cewn":
            return createTile(board,absolute,relative,new ArrayList<Wall>(Arrays.asList(new Wall[]{wno})),comEa);
        case "cewwl":
            return createTile(board,absolute,relative,new ArrayList<Wall>(Arrays.asList(new Wall[]{wwel})),comEa);
        case "cs": return createTile(board,absolute,relative,new ArrayList<Wall>(),comSo);
        case "cswe":
            return createTile(board,absolute,relative,new ArrayList<Wall>(Arrays.asList(new Wall[]{wea})),comSo);
        case "cw": return createTile(board,absolute,relative,new ArrayList<Wall>(),comWe);
        case "cews":
            return createTile(board,absolute,relative,new ArrayList<Wall>(Arrays.asList(new Wall[]{wso})),comEa);
        case "cwwsl":
            return createTile(board,absolute,relative,new ArrayList<Wall>(Arrays.asList(new Wall[]{wsol})),comWe);
        //Expressförderbänder
        case "en": return createTile(board,absolute,relative,new ArrayList<Wall>(),expNo);
        case "eewn":
            return createTile(board,absolute,relative,new ArrayList<Wall>(Arrays.asList(new Wall[]{wno})),expEa);
        case "enwwl":
            return createTile(board,absolute,relative,new ArrayList<Wall>(Arrays.asList(new Wall[]{wwel})),expNo);
        case "ee": return createTile(board,absolute,relative,new ArrayList<Wall>(),expEa);
        case "es": return createTile(board,absolute,relative,new ArrayList<Wall>(),expSo);
        case "ew": return createTile(board,absolute,relative,new ArrayList<Wall>(),expWe);
        case "ewwe":
            return createTile(board,absolute,relative,new ArrayList<Wall>(Arrays.asList(new Wall[]{wea})),expWe);
        case "ewwsl":
            return createTile(board,absolute,relative,new ArrayList<Wall>(Arrays.asList(new Wall[]{wsol})),expWe);
        case "eewnl": 
            return createTile(board,absolute,relative,new ArrayList<Wall>(Arrays.asList(new Wall[]{wnol})),expEa);
        //Flaggen
        case "f1":
            return createTile(board,absolute,relative,new ArrayList<Wall>(),flag1);
        case "f1ww":
            return createTile(board,absolute,relative,new ArrayList<Wall>(Arrays.asList(new Wall[]{wwe})),flag1);
        case "f2": return createTile(board,absolute,relative,new ArrayList<Wall>(),flag2);
        case "f3": return createTile(board,absolute,relative,new ArrayList<Wall>(),flag3);
        }
        return null;
    }
    
    /*
     * Nachbarn auf der x-Achse ermitteln
     */
    private Map<Orientation,Tile> findNeighbors(Map<Point,Field> board, Tile tile){
        Map<Orientation,Tile> neighbor = new HashMap<>();
        
        Field field = null;
        Map<Point,Tile> tiles = new HashMap<>();
        for(Point pp:board.keySet()){
            if(board.get(pp).getTiles().values().contains(tile)){
                field = board.get(pp);
                tiles = field.getTiles();
                break;
            }
        }
        
        if(tiles.isEmpty()){
            return null;
        }
        
        Point relative = tile.getCoordinates();
        Point fieldPoint = field.getCoordinates();
        
        switch(relative.x){
        case 0:
            Field nextField = board.get(new Point(fieldPoint.x-1,fieldPoint.y));
            
            neighbor.put(Orientation.EAST, tiles.get(new Point(relative.x+1,relative.y)));
            if(nextField != null){
                neighbor.put(Orientation.WEST, nextField.getTiles().get(new Point(11,relative.y)));
            }
            return getYNeighbor(board,field,relative,neighbor);
        case 11:
            nextField = board.get(new Point(fieldPoint.x+1,fieldPoint.y));
            
            if(nextField != null){
                neighbor.put(Orientation.EAST, tiles.get(new Point(0,relative.y)));
            }
            neighbor.put(Orientation.WEST, tiles.get(new Point(relative.x-1,relative.y)));
            return getYNeighbor(board,field,relative,neighbor);
        default:
            neighbor.put(Orientation.EAST, tiles.get(new Point(relative.x+1,relative.y)));
            neighbor.put(Orientation.WEST, tiles.get(new Point(relative.x-1,relative.y)));
            return getYNeighbor(board,field,relative,neighbor);
        }
    }
    
    /*
     * Neighbor um Nachbarn auf der y-Achse erweitern
     */
    private Map<Orientation,Tile> getYNeighbor(Map<Point,Field> board, Field field, Point relative, Map<Orientation,Tile> neighbor){
        Point fieldPoint = field.getCoordinates();
        Map<Point, Tile> tiles = field.getTiles();
        
        switch(relative.y){
        case 0:
            Field nextField = board.get(new Point(fieldPoint.x,fieldPoint.y-1));
            
            neighbor.put(Orientation.NORTH, tiles.get(new Point(relative.x,relative.y+1)));
            if(nextField != null){
                neighbor.put(Orientation.SOUTH, nextField.getTiles().get(new Point(relative.x,11)));
            }
            return neighbor;
        case 11:
            nextField = board.get(new Point(fieldPoint.x,fieldPoint.y+1));
            
            if(nextField != null){
                neighbor.put(Orientation.NORTH, nextField.getTiles().get(new Point(relative.x,0)));
            }
            neighbor.put(Orientation.SOUTH, tiles.get(new Point(relative.x,relative.y-1)));
            return neighbor;
            default:
                neighbor.put(Orientation.NORTH,tiles.get(new Point(relative.x,relative.y+1)));
                neighbor.put(Orientation.SOUTH,tiles.get(new Point(relative.x,relative.y-1)));
                return neighbor;
        }
    }
    
    /*
     * Test-Tiles erzeugen
     */
    private Tile createTile(Board board, Point absolute, Point relative, List<Wall> walls, FieldObject object) {
        Tile tile = new Tile() {

            @Override
            public void action() { }

            @Override
            public void setFieldObject(FieldObject object) { }

            @Override
            public void removeFieldObject() { }

            @Override
            public FieldObject getFieldObject() {
                return object;
            }

            @Override
            public void setRobot(Robot robot) { }

            @Override
            public Robot getRobot() {
                return null;
            }

            @Override
            public List<Wall> getWalls() {
                return walls;
            }

            @Override
            public Wall getWall(Orientation direction) {
                for(Wall wall:walls){
                    if(wall.getOrientation().equals(direction))
                        return wall;
                }
                return null;
            }

            @Override
            public boolean hasWall(Orientation direction) {
                for(Wall wall:walls){
                    if(wall.getOrientation().equals(direction))
                        return true;
                }
                return false;
            }

            @Override
            public Point getCoordinates() {
                return relative;
            }

            @Override
            public Tile clone() {
                return this;
            }

            @Override
            public Point getAbsoluteCoordinates() {
                return absolute;
            }

            @Override
            public Map<Orientation, Tile> getNeighbors() {
                return findNeighbors(board.getFields(), this);
            }

        };
        return tile;
    }
}