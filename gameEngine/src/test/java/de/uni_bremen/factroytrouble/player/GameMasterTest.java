package de.uni_bremen.factroytrouble.player;

import de.uni_bremen.factroytrouble.ai.AIPlayer;
import de.uni_bremen.factroytrouble.board.Board;
import de.uni_bremen.factroytrouble.board.Dock;
import de.uni_bremen.factroytrouble.board.Field;
import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.gameobjects.Flag;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.gameobjects.Workshop;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.awt.Point;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GameMasterTest {
    private static final int GAME_ID = 0;

    @Mock private Board board;

    @Mock private Board availableTestBoard1, availableTestBoard2;

    @Mock
    private Player player1, player2, player3, player4, player5, player6, player7, player8, player9;
    
    @Mock
    private AIPlayer aiPlayer1, aiPlayer2;

    @Mock
    private Robot robot1, robot2, robot3, robot4, robot5, robot6, robot7, robot8, robot9;

    @Mock
    private Tile pushTestTileOne;

    @Mock
    private Tile pushTestTileTwo;

    @Mock
    private Tile pushTestTileThree;

    @Mock
    private Tile pushTestTileFour;

    @Mock
    private Tile pushTestTileFive;

    @Mock
    private Tile tile;
    @Mock
    private Tile workshopTile;
    @Mock
    private Tile flagTile;
    @Mock
    private Dock dock;
    @Mock
    private Field field;
    @Mock
    Map<Point, Tile> dockMapMock;

    private Map<String, List<Point>> startPointMap = new HashMap<>();
    private List<Point> startPoints = new ArrayList<>();
    private Map<Point, Field> fields = new HashMap<>();

    @Mock
    private BoardManager manager;

    private int robot1HP = 0;
    private int robot2HP = 0;

    private int stripPlayerFromCardsCalls = 0;
    private int stripRobotFromCardsCalls = 0;

    private Master master;

    private TestAppender appender;

    @Before
    public void setUp() {
        stripPlayerFromCardsCalls = 0;
        stripRobotFromCardsCalls = 0;
        startPoints.add(new Point(1, 3));
        startPoints.add(new Point(3, 2));
        startPointMap.put("checkmate", startPoints);
        when(manager.getAvailableBoards()).thenReturn(startPointMap);

        when(manager.buildBoard(anyString())).thenReturn(board);

        fields.put(new Point(0, 0), dock);
        fields.put(new Point(0, 1), field);

        master = new GameMaster(GAME_ID, manager);

        when(board.getFields()).thenReturn(fields);

        when(player1.isDone()).thenReturn(false);
        when(player2.isDone()).thenReturn(false);

        when(robot1.getName()).thenReturn("robot1");
        when(robot2.getName()).thenReturn("robot2");
        when(robot3.getName()).thenReturn("robot3");
        when(robot4.getName()).thenReturn("robot4");
        when(robot5.getName()).thenReturn("robot5");
        when(robot6.getName()).thenReturn("robot6");
        when(robot7.getName()).thenReturn("robot7");
        when(robot8.getName()).thenReturn("robot8");

        when(player1.getRobot()).thenReturn(robot1);
        when(player2.getRobot()).thenReturn(robot2);
        when(player3.getRobot()).thenReturn(robot3);
        when(player4.getRobot()).thenReturn(robot4);
        when(player5.getRobot()).thenReturn(robot5);
        when(player6.getRobot()).thenReturn(robot6);
        when(player7.getRobot()).thenReturn(robot7);
        when(player8.getRobot()).thenReturn(robot8);
        when(player9.getRobot()).thenReturn(robot9);

        ArrayList<ProgramCard> player1Hand = new ArrayList<ProgramCard>();
        player1Hand.add(master.dealCard());
        player1Hand.add(master.dealCard());
        player1Hand.add(master.dealCard());
        player1Hand.add(master.dealCard());

        ArrayList<ProgramCard> player2Hand = new ArrayList<ProgramCard>();
        player2Hand.add(master.dealCard());
        player2Hand.add(master.dealCard());
        player2Hand.add(master.dealCard());
        player2Hand.add(master.dealCard());

        ArrayList<ProgramCard> player3Hand = new ArrayList<ProgramCard>();
        player3Hand.add(master.dealCard());
        player3Hand.add(master.dealCard());
        player3Hand.add(master.dealCard());
        player3Hand.add(master.dealCard());

        ArrayList<ProgramCard> player4Hand = new ArrayList<ProgramCard>();
        player4Hand.add(master.dealCard());
        player4Hand.add(master.dealCard());
        player4Hand.add(master.dealCard());
        player4Hand.add(master.dealCard());

        ArrayList<ProgramCard> player5Hand = new ArrayList<ProgramCard>();
        player5Hand.add(master.dealCard());
        player5Hand.add(master.dealCard());
        player5Hand.add(master.dealCard());
        player5Hand.add(master.dealCard());

        ArrayList<ProgramCard> player6Hand = new ArrayList<ProgramCard>();
        player6Hand.add(master.dealCard());
        player6Hand.add(master.dealCard());
        player6Hand.add(master.dealCard());
        player6Hand.add(master.dealCard());

        ArrayList<ProgramCard> player7Hand = new ArrayList<ProgramCard>();
        player7Hand.add(master.dealCard());
        player7Hand.add(master.dealCard());
        player7Hand.add(master.dealCard());
        player7Hand.add(master.dealCard());

        ArrayList<ProgramCard> player8Hand = new ArrayList<ProgramCard>();
        player8Hand.add(master.dealCard());
        player8Hand.add(master.dealCard());
        player8Hand.add(master.dealCard());
        player8Hand.add(master.dealCard());

        when(player1.discardCards()).thenReturn(player1Hand).then(invocation -> stripPlayerFromCardsCalls++);
        when(player2.discardCards()).thenReturn(player2Hand).then(invocation -> stripPlayerFromCardsCalls++);
        when(player3.discardCards()).thenReturn(player3Hand).then(invocation -> stripPlayerFromCardsCalls++);
        when(player4.discardCards()).thenReturn(player4Hand).then(invocation -> stripPlayerFromCardsCalls++);
        when(player5.discardCards()).thenReturn(player5Hand).then(invocation -> stripPlayerFromCardsCalls++);
        when(player6.discardCards()).thenReturn(player6Hand).then(invocation -> stripPlayerFromCardsCalls++);
        when(player7.discardCards()).thenReturn(player7Hand).then(invocation -> stripPlayerFromCardsCalls++);
        when(player8.discardCards()).thenReturn(player8Hand).then(invocation -> stripPlayerFromCardsCalls++);

        List<ProgramCard> robot1Hand = new ArrayList<>();
        robot1Hand.add(master.dealCard());
        robot1Hand.add(master.dealCard());
        robot1Hand.add(master.dealCard());
        robot1Hand.add(master.dealCard());
        robot1Hand.add(master.dealCard());

        List<ProgramCard> robot2Hand = new ArrayList<>();
        robot2Hand.add(master.dealCard());
        robot2Hand.add(master.dealCard());
        robot2Hand.add(master.dealCard());
        robot2Hand.add(master.dealCard());
        robot2Hand.add(master.dealCard());

        List<ProgramCard> robot3Hand = new ArrayList<>();
        robot3Hand.add(master.dealCard());
        robot3Hand.add(master.dealCard());
        robot3Hand.add(master.dealCard());
        robot3Hand.add(master.dealCard());
        robot3Hand.add(master.dealCard());

        List<ProgramCard> robot4Hand = new ArrayList<>();
        robot4Hand.add(master.dealCard());
        robot4Hand.add(master.dealCard());
        robot4Hand.add(master.dealCard());
        robot4Hand.add(master.dealCard());
        robot4Hand.add(master.dealCard());

        List<ProgramCard> robot5Hand = new ArrayList<>();
        robot5Hand.add(master.dealCard());
        robot5Hand.add(master.dealCard());
        robot5Hand.add(master.dealCard());
        robot5Hand.add(master.dealCard());
        robot5Hand.add(master.dealCard());

        List<ProgramCard> robot6Hand = new ArrayList<>();
        robot6Hand.add(master.dealCard());
        robot6Hand.add(master.dealCard());
        robot6Hand.add(master.dealCard());
        robot6Hand.add(master.dealCard());
        robot6Hand.add(master.dealCard());

        List<ProgramCard> robot7Hand = new ArrayList<>();
        robot7Hand.add(master.dealCard());
        robot7Hand.add(master.dealCard());
        robot7Hand.add(master.dealCard());
        robot7Hand.add(master.dealCard());
        robot7Hand.add(master.dealCard());

        List<ProgramCard> robot8Hand = new ArrayList<>();
        robot8Hand.add(master.dealCard());
        robot8Hand.add(master.dealCard());
        robot8Hand.add(master.dealCard());
        robot8Hand.add(master.dealCard());
        robot8Hand.add(master.dealCard());

        when(robot1.emptyAllRegister()).thenReturn(robot1Hand).then(invocation -> stripRobotFromCardsCalls++);
        when(robot2.emptyAllRegister()).thenReturn(robot2Hand).then(invocation -> stripRobotFromCardsCalls++);
        when(robot3.emptyAllRegister()).thenReturn(robot3Hand).then(invocation -> stripRobotFromCardsCalls++);
        when(robot4.emptyAllRegister()).thenReturn(robot4Hand).then(invocation -> stripRobotFromCardsCalls++);
        when(robot5.emptyAllRegister()).thenReturn(robot5Hand).then(invocation -> stripRobotFromCardsCalls++);
        when(robot6.emptyAllRegister()).thenReturn(robot6Hand).then(invocation -> stripRobotFromCardsCalls++);
        when(robot7.emptyAllRegister()).thenReturn(robot7Hand).then(invocation -> stripRobotFromCardsCalls++);
        when(robot8.emptyAllRegister()).thenReturn(robot8Hand).then(invocation -> stripRobotFromCardsCalls++);

        when(player1.toString()).thenReturn("player1");
        when(player2.toString()).thenReturn("player2");
        when(player3.toString()).thenReturn("player3");
        when(player4.toString()).thenReturn("player4");
        when(player5.toString()).thenReturn("player5");
        when(player6.toString()).thenReturn("player6");
        when(player7.toString()).thenReturn("player7");
        when(player8.toString()).thenReturn("player8");
        when(player9.toString()).thenReturn("player9");

        master.removeAllPlayers();
        master.addPlayer(player1);
        master.addPlayer(player2);

        when(tile.getCoordinates()).thenReturn(new Point(1, 2));

        when(workshopTile.getFieldObject()).thenReturn(mock(Workshop.class));
        when(flagTile.getFieldObject()).thenReturn(mock(Flag.class));

        when(robot1.getHP()).thenAnswer(invocation -> robot1HP);
        when(robot2.getHP()).thenAnswer(invocation -> robot2HP);
        doAnswer(invocation -> robot1HP++).when(robot1).heal();
        doAnswer(invocation -> robot2HP++).when(robot2).heal();

        when(manager.getStartPositions("checkmate"))
                .thenReturn(new Point[] { new Point(0, 1), new Point(1, 1), new Point(3, 1), new Point(5, 1),
                        new Point(6, 1), new Point(8, 1), new Point(10, 1), new Point(11, 1) });

        Tile tileMock = mock(Tile.class);
        when(dockMapMock.get(any())).thenReturn(tileMock);
        when(dock.getTiles()).thenReturn(dockMapMock);

        master.initialiseBoard("checkmate");
        appender = new TestAppender();
        Logger.getLogger(GameMaster.class).addAppender(appender);
    }
    
    @Test
    public void changeRobotToPoweredDownTest(){
        when(player1.isDone()).thenReturn(true);
        when(player1.discardCards()).thenReturn(new ArrayList<ProgramCard>());
        when(player2.isDone()).thenReturn(true);
        when(player2.discardCards()).thenReturn(new ArrayList<ProgramCard>());
        
        when(robot1.registerLockStatus()).thenReturn(new boolean[]{false,false,false,false,false});
        when(robot1.isPoweredDown()).thenReturn(false);
        when(robot1.emptyAllRegister()).thenReturn(new ArrayList<ProgramCard>());
        when(robot2.registerLockStatus()).thenReturn(new boolean[]{false,false,false,false,false});
        when(robot2.emptyAllRegister()).thenReturn(new ArrayList<ProgramCard>());
        
        doAnswer(invocation -> {
            when(robot1.isPoweredDown()).thenReturn(true);
            return true;
            }).when(robot1).powerDown();

        master.requestPowerDownStatusChange(player1.getRobot());
        master.activateBoard();
        //robot1 abgeschaltet
        assertTrue(master.getPlayers().get(0).getRobot().isPoweredDown());
    }
    
    @Test
    public void changeRobotToAwakeTest(){
        when(player1.isDone()).thenReturn(true);
        when(player1.discardCards()).thenReturn(new ArrayList<ProgramCard>());
        when(player2.isDone()).thenReturn(true);
        when(player2.discardCards()).thenReturn(new ArrayList<ProgramCard>());
        
        when(robot1.registerLockStatus()).thenReturn(new boolean[]{false,false,false,false,false});
        when(robot1.isPoweredDown()).thenReturn(true);
        when(robot1.emptyAllRegister()).thenReturn(new ArrayList<ProgramCard>());
        when(robot2.registerLockStatus()).thenReturn(new boolean[]{false,false,false,false,false});
        when(robot2.emptyAllRegister()).thenReturn(new ArrayList<ProgramCard>());
        
        doAnswer(invocation -> {
            when(robot1.isPoweredDown()).thenReturn(false);
            return true;
            }).when(robot1).wakeUp();

        master.requestPowerDownStatusChange(player1.getRobot());
        master.activateBoard();
        //robot1 angeschaltet
        assertTrue(!master.getPlayers().get(0).getRobot().isPoweredDown());
    }

    // Dieser Test funktioniert nur auf einem System auf dem es 23:59:45 ist, um
    // zu überprüfen ob der Countdown Tage, Stunden und Minuten richtig
    // überprüft. Calendar berechnet die Sprünge korrekt.
    @Test
    public void testCountdownAtDaySwitch() {
        Calendar now = Calendar.getInstance();
        Calendar then = Calendar.getInstance();
        then.add(Calendar.SECOND, 30);
        if (now.get(Calendar.HOUR) == 23 && now.get(Calendar.MINUTE) == 59 && now.get(Calendar.SECOND) == 45) {
            when(player1.isDone()).thenReturn(true);
            when(player2.isDone()).thenReturn(false);
            double returns = master.countdown();
            assertEquals(0, returns, 0);
            now = Calendar.getInstance();
            assertEquals((double) then.getTime().getTime(), (double) now.getTime().getTime(), 2);
        }
    }

    @Test
    public void countdownStartetNichtWennMehrAls1SpielerNichtReady() {
        assertEquals(-1, master.countdown(), 0);
        List<LoggingEvent> log = appender.getLog();
        LoggingEvent l0 = log.get(0);
        assertEquals("ERROR", l0.getLevel().toString());
        assertEquals("Noch nicht fertige KI-Agenten: 0. Noch nicht fertige menschl. Spieler: 2.", l0.getMessage());
    }
    
    @Test
    public void dontStartCountdownWhenHumanAndAINotReadyTest(){
        when(player1.isDone()).thenReturn(false);
        when(aiPlayer1.isDone()).thenReturn(false);
        when(aiPlayer2.isDone()).thenReturn(false);
        master.removeAllPlayers();
        master.addPlayer(player1);
        master.addPlayer(aiPlayer1);
        master.addPlayer(aiPlayer2);
        
        int countdown = (int)master.countdown();
        assertEquals(-1, countdown);
    }
    
    @Test
    public void unsopportedHumanCountdownTest(){
        when(player1.isDone()).thenReturn(true);
        assertEquals(-1, master.countdown(), 0);
        LoggingEvent l0 = appender.getLog().get(0);
        assertEquals("INFO", l0.getLevel().toString());
        assertEquals("Countdown für menschliche Spieler wird noch nicht unterstützt!", l0.getMessage());
    }

    @Test
    public void countdownTest(){
        when(player1.isDone()).thenReturn(true);
        when(aiPlayer1.isDone()).thenReturn(false);
        when(aiPlayer2.isDone()).thenReturn(false);
        master.removeAllPlayers();
        master.addPlayer(player1);
        master.addPlayer(aiPlayer1);
        master.addPlayer(aiPlayer2);
        
        double returns = master.countdown();
        
        assertEquals(0, (int)returns);
    }
    
    @Test
    public void after10SecondsAllAIFinishedTest(){
        when(aiPlayer1.isDone()).thenReturn(false);
        when(aiPlayer2.isDone()).thenReturn(false);
        master.removeAllPlayers();
        master.addPlayer(aiPlayer1);
        master.addPlayer(aiPlayer2);
        
        TestThread t = new TestThread();
        t.start();
        
        double returns = master.countdown();
        
        //assertTrue((int) returns == 10 || (int)returns == 11);//
        assertEquals(10, (int)returns);
    }

    private class TestThread extends Thread {

        public void run() {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                return;
            }
            when(aiPlayer1.isDone()).thenReturn(true);
            try{
                Thread.sleep(4500);
            }catch(InterruptedException e){
                return;
            }
            when(aiPlayer2.isDone()).thenReturn(true);
        }
    }

    @Test
    public void cleanupTest() {
        when(robot1.getCurrentTile()).thenReturn(workshopTile);
        when(robot1.getLives()).thenReturn(1);
        when(robot2.getCurrentTile()).thenReturn(flagTile);
        when(robot2.getLives()).thenReturn(3);

        List<Robot> deadRobots = master.cleanup();

        assertEquals(0, deadRobots.size());
        assertEquals(1, robot1HP);
        assertEquals(1, robot2HP);

        when(robot1.getCurrentTile()).thenReturn(tile);
        when(robot1.getLives()).thenReturn(1);
        when(robot2.getCurrentTile()).thenReturn(workshopTile);
        when(robot2.getLives()).thenReturn(3);

        deadRobots = master.cleanup();
        assertEquals(0, deadRobots.size());
        assertEquals(1, robot1HP);
        assertEquals(2, robot2HP);

        when(robot1.getCurrentTile()).thenReturn(null);
        when(robot1.getLives()).thenReturn(2);
        when(robot2.getCurrentTile()).thenReturn(null);
        when(robot2.getLives()).thenReturn(0);

        deadRobots = master.cleanup();
        assertEquals(1, deadRobots.size());
        assertTrue(deadRobots.contains(robot2));
        assertEquals(1, robot1HP);
        assertEquals(2, robot2HP);
    }

    @Test
    public void addPlayerTest() {
        assertTrue(master.addPlayer(player3));
        assertTrue(master.addPlayer(player4));
        assertTrue(master.addPlayer(player5));
        assertTrue(master.addPlayer(player6));
        assertTrue(master.addPlayer(player7));
        assertFalse(master.addPlayer(player7));
        assertTrue(master.addPlayer(player8));
        assertFalse(master.addPlayer(player9));
    }

    @Test
    public void canMoveTest() {
        // Test one: true, forward

        Orientation pushDirection = Orientation.NORTH;
        Orientation oppositeDirection = Orientation.SOUTH;
        when(robot1.getOrientation()).thenReturn(pushDirection);
        when(robot1.getCurrentTile()).thenReturn(pushTestTileOne);
        when(board.findNextTile(pushTestTileOne, pushDirection)).thenReturn(pushTestTileTwo);
        when(pushTestTileOne.hasWall(pushDirection)).thenReturn(false);
        when(pushTestTileTwo.hasWall(oppositeDirection)).thenReturn(false);

        assertTrue(master.canMove(robot1, true));

        // Test Two: true, backwards

        when(board.findNextTile(pushTestTileOne, oppositeDirection)).thenReturn(pushTestTileTwo);
        when(pushTestTileOne.hasWall(oppositeDirection)).thenReturn(false);
        when(pushTestTileTwo.hasWall(pushDirection)).thenReturn(false);

        assertTrue(master.canMove(robot1, false));

        when(board.findNextTile(pushTestTileOne, pushDirection)).thenReturn(pushTestTileTwo);
        when(pushTestTileOne.hasWall(pushDirection)).thenReturn(false);
        when(pushTestTileTwo.hasWall(oppositeDirection)).thenReturn(false);

        // Test Three: false, forward, if 1 false

        when(pushTestTileOne.hasWall(pushDirection)).thenReturn(true);
        when(pushTestTileTwo.hasWall(oppositeDirection)).thenReturn(false);

        assertFalse(master.canMove(robot1, true));

        when(pushTestTileOne.hasWall(pushDirection)).thenReturn(false);
        when(pushTestTileTwo.hasWall(oppositeDirection)).thenReturn(true);

        assertFalse(master.canMove(robot1, true));

        when(pushTestTileOne.hasWall(pushDirection)).thenReturn(false);
        when(pushTestTileTwo.hasWall(oppositeDirection)).thenReturn(false);

        // Test Four: true, forward, if 2 true, recusion

        when(robot1.getCurrentTile()).thenReturn(pushTestTileOne); // 282
        when(robot2.getCurrentTile()).thenReturn(pushTestTileTwo); // 283

        Orientation pushDirection2 = Orientation.EAST;
        Orientation oppositeDirection2 = Orientation.WEST;

        // tileNextToTile() sagt true bei EAST -> pushDirection = EAST;
        when(board.findNextTile(pushTestTileOne, pushDirection2)).thenReturn(pushTestTileTwo); // 284
                                                                                               // ->
                                                                                               // 320,
                                                                                               // 291

        // orientationToTile() sagt EAST
        Point pointOne = new Point(1, 0);
        when(pushTestTileOne.getCoordinates()).thenReturn(pointOne); // 284 ->
                                                                     // 323
        Point pointTwo = new Point(2, 0);
        when(pushTestTileTwo.getCoordinates()).thenReturn(pointTwo); // 284 ->
                                                                     // 324

        when(board.findNextTile(pushTestTileTwo, pushDirection2)).thenReturn(pushTestTileThree); // 285

        when(robot1.getOrientation()).thenReturn(pushDirection2); // 286

        when(pushTestTileOne.hasWall(pushDirection2)).thenReturn(false);
        when(pushTestTileTwo.hasWall(pushDirection2)).thenReturn(false);
        when(pushTestTileTwo.hasWall(oppositeDirection2)).thenReturn(false);
        when(pushTestTileThree.hasWall(oppositeDirection2)).thenReturn(false);

        when(pushTestTileThree.getRobot()).thenReturn(null);

        when(pushTestTileTwo.getRobot()).thenReturn(robot2);

        assertTrue(master.canPushOtherRobot(robot1, robot2));

        assertTrue(master.canMove(robot1, true));

        // Test five: false, forward, if 2.2 true, recursion

        when(pushTestTileThree.hasWall(oppositeDirection2)).thenReturn(true);
        assertFalse(master.canPushOtherRobot(robot1, robot2));
        assertFalse(master.canMove(robot1, true));
        when(pushTestTileThree.hasWall(oppositeDirection2)).thenReturn(false);

    }

    @Test
    public void canPushOtherRobotTest() {
        // Test One: True without
        // recursion--------------------------------------

        when(robot1.getCurrentTile()).thenReturn(pushTestTileOne); // 282
        when(robot2.getCurrentTile()).thenReturn(pushTestTileTwo); // 283

        Orientation pushDirection = Orientation.EAST;
        Orientation oppositeDirection = Orientation.WEST;

        // tileNextToTile() sagt true bei EAST -> pushDirection = EAST;
        when(board.findNextTile(pushTestTileOne, pushDirection)).thenReturn(pushTestTileTwo); // 284
                                                                                              // ->
                                                                                              // 320,
                                                                                              // 291

        // orientationToTile() sagt EAST
        Point pointOne = new Point(1, 0);
        when(pushTestTileOne.getCoordinates()).thenReturn(pointOne); // 284 ->
                                                                     // 323
        Point pointTwo = new Point(2, 0);
        when(pushTestTileTwo.getCoordinates()).thenReturn(pointTwo); // 284 ->
                                                                     // 324

        when(board.findNextTile(pushTestTileTwo, pushDirection)).thenReturn(pushTestTileThree); // 285

        when(robot1.getOrientation()).thenReturn(pushDirection); // 286

        when(pushTestTileOne.hasWall(pushDirection)).thenReturn(false);
        when(pushTestTileTwo.hasWall(pushDirection)).thenReturn(false);
        when(pushTestTileTwo.hasWall(oppositeDirection)).thenReturn(false);
        when(pushTestTileTwo.hasWall(oppositeDirection)).thenReturn(false);

        when(pushTestTileThree.getRobot()).thenReturn(null);

        assertTrue(master.canPushOtherRobot(robot1, robot2));

        // Test Two: True without recursion, if 2
        // true---------------------------------

        // tileNextToTile() sagt true bei EAST -> pushDirection = EAST;
        when(board.findNextTile(pushTestTileOne, pushDirection)).thenReturn(pushTestTileTwo); // 284
                                                                                              // ->
                                                                                              // 320,
                                                                                              // 291

        when(board.findNextTile(pushTestTileTwo, pushDirection)).thenReturn(pushTestTileThree); // 285

        when(robot1.getOrientation()).thenReturn(Orientation.NORTH); // 286
        when(board.findNextTile(pushTestTileOne, oppositeDirection)).thenReturn(pushTestTileFour); // 287
        when(pushTestTileFour.getRobot()).thenReturn(robot4);

        when(pushTestTileOne.hasWall(pushDirection)).thenReturn(false);
        when(pushTestTileTwo.hasWall(pushDirection)).thenReturn(false);
        when(pushTestTileTwo.hasWall(oppositeDirection)).thenReturn(false);
        when(pushTestTileTwo.hasWall(oppositeDirection)).thenReturn(false);

        when(pushTestTileThree.getRobot()).thenReturn(null);

        assertTrue(master.canPushOtherRobot(robot1, robot2));

        // Test Three: True without recursion, if 2 & 2.2
        // true---------------------------------

        // tileNextToTile() sagt true bei EAST -> pushDirection = EAST;
        when(board.findNextTile(pushTestTileOne, pushDirection)).thenReturn(pushTestTileTwo); // 284
                                                                                              // ->
                                                                                              // 320,
                                                                                              // 291

        when(board.findNextTile(pushTestTileTwo, pushDirection)).thenReturn(pushTestTileThree); // 285

        when(robot1.getOrientation()).thenReturn(Orientation.NORTH); // 286
        when(board.findNextTile(pushTestTileOne, oppositeDirection)).thenReturn(pushTestTileFour); // 287
        when(pushTestTileFour.getRobot()).thenReturn(null);

        assertFalse(master.canPushOtherRobot(robot1, robot2));

        when(pushTestTileFour.getRobot()).thenReturn(robot4);

        // Test Four: False without recursion, if 1
        // true--------------------------------

        // tileNextToTile() sagt true bei EAST -> pushDirection = EAST;
        when(board.findNextTile(pushTestTileOne, pushDirection)).thenReturn(null); // 284
                                                                                   // ->
                                                                                   // 320

        assertFalse(master.canPushOtherRobot(robot1, robot2));

        // Test Five: False without recursion, if 3
        // true--------------------------------

        // tileNextToTile() sagt true bei EAST -> pushDirection = EAST;
        when(board.findNextTile(pushTestTileOne, pushDirection)).thenReturn(pushTestTileTwo); // 284
                                                                                              // ->
                                                                                              // 320

        when(pushTestTileOne.hasWall(pushDirection)).thenReturn(true);

        assertFalse(master.canPushOtherRobot(robot1, robot2));

        when(pushTestTileOne.hasWall(pushDirection)).thenReturn(false);
        when(pushTestTileTwo.hasWall(pushDirection)).thenReturn(true);

        assertFalse(master.canPushOtherRobot(robot1, robot2));

        when(pushTestTileOne.hasWall(pushDirection)).thenReturn(false);
        when(pushTestTileTwo.hasWall(pushDirection)).thenReturn(false);
        when(pushTestTileTwo.hasWall(oppositeDirection)).thenReturn(true);

        assertFalse(master.canPushOtherRobot(robot1, robot2));

        when(pushTestTileOne.hasWall(pushDirection)).thenReturn(false);
        when(pushTestTileTwo.hasWall(pushDirection)).thenReturn(false);
        when(pushTestTileTwo.hasWall(oppositeDirection)).thenReturn(false);
        when(pushTestTileThree.hasWall(oppositeDirection)).thenReturn(true);

        assertFalse(master.canPushOtherRobot(robot1, robot2));

        when(pushTestTileThree.hasWall(oppositeDirection)).thenReturn(false);

        // Test Six: True with recursion

        when(board.findNextTile(pushTestTileTwo, pushDirection)).thenReturn(pushTestTileThree);
        when(pushTestTileThree.getRobot()).thenReturn(robot5);
        Point pointThree = new Point(3, 0);
        when(pushTestTileThree.getCoordinates()).thenReturn(pointThree);
        when(robot5.getCurrentTile()).thenReturn(pushTestTileThree);
        when(robot2.getOrientation()).thenReturn(Orientation.NORTH);
        when(board.findNextTile(pushTestTileTwo, Orientation.WEST)).thenReturn(pushTestTileThree);
        when(board.findNextTile(pushTestTileThree, pushDirection)).thenReturn(pushTestTileFive);
        when(pushTestTileFive.hasWall(Orientation.WEST)).thenReturn(false);
        assertTrue(master.canPushOtherRobot(robot1, robot2));

        // test seven: False with recursion

        when(pushTestTileFive.hasWall(Orientation.WEST)).thenReturn(true);
        assertFalse(master.canPushOtherRobot(robot1, robot2));

    }

    @Test
    public void getAvailableBoardsTest() {
        List<String> boardList = new ArrayList<>();
        boardList.add("checkmate");

        assertEquals(boardList, master.getAvailableBoards());
    }

    @Test
    public void activateBoardTest() {
        master.addPlayer(player3);
        master.addPlayer(player4);
        master.addPlayer(player5);
        master.addPlayer(player6);
        master.addPlayer(player7);
        master.addPlayer(player8);

    }

    @Test
    public void testInitialiseBoard() {
        List<Point> po = new ArrayList<Point>();
        Point p1 = new Point(1, 0);
        Point p2 = new Point(2, 0);
        Point p3 = new Point(3, 0);
        Point p4 = new Point(4, 0);
        Point p5 = new Point(5, 0);
        Point p6 = new Point(6, 0);
        Point p7 = new Point(7, 0);
        Point p8 = new Point(8, 0);
        po.add(p1);
        po.add(p2);
        po.add(p3);
        po.add(p4);
        po.add(p5);
        po.add(p6);
        po.add(p7);
        po.add(p8);
        Map<String, List<Point>> bo = new HashMap<String, List<Point>>();
        bo.put("checkmate", po);
        when(manager.getAvailableBoards()).thenReturn(bo);
        master = new GameMaster(GAME_ID,manager);

        master.addPlayer(player1);
        master.addPlayer(player2);
        master.addPlayer(player3);
        master.addPlayer(player4);
        master.addPlayer(player5);
        master.addPlayer(player6);
        master.addPlayer(player7);
        master.addPlayer(player8);
        master.initialiseBoard("checkmate");
        assertEquals(master.getPlayers().size(), 8);
    }

    @Test
    public void getWinnerTest() {
        master.init();
        master.addPlayer(player1);
        master.addPlayer(player2);
        master.addPlayer(player2);
        master.addPlayer(player4);
        master.addPlayer(player5);
        master.addPlayer(player6);
        master.addPlayer(player7);
        master.addPlayer(player8);
        when(board.getHighestFlagNumber()).thenReturn(5);
        master.initialiseBoard("checkmate");

        // kein Gewinner
        when(robot1.getLives()).thenReturn(3);
        when(robot2.getLives()).thenReturn(3);
        when(robot3.getLives()).thenReturn(3);
        when(robot4.getLives()).thenReturn(3);
        when(robot5.getLives()).thenReturn(3);
        when(robot6.getLives()).thenReturn(3);
        when(robot7.getLives()).thenReturn(3);
        when(robot8.getLives()).thenReturn(3);
        when(robot1.getFlagCounterStatus()).thenReturn(0);
        when(robot2.getFlagCounterStatus()).thenReturn(4);
        when(robot3.getFlagCounterStatus()).thenReturn(3);
        when(robot4.getFlagCounterStatus()).thenReturn(1);
        when(robot5.getFlagCounterStatus()).thenReturn(0);
        when(robot6.getFlagCounterStatus()).thenReturn(3);
        when(robot7.getFlagCounterStatus()).thenReturn(4);
        when(robot8.getFlagCounterStatus()).thenReturn(2);

        assertEquals(null, master.getWinner());

        // Gewinner durch Flagge
        when(robot5.getFlagCounterStatus()).thenReturn(5);
        when(robot4.getCurrentTile()).thenReturn(mock(Tile.class));
        when(robot6.getCurrentTile()).thenReturn(mock(Tile.class));

        assertEquals(player5, master.getWinner());

        // Gewinner durch Überleben (funktioniert nicht; Alter Gewinner noch
        // gesetzt)
        when(robot5.getFlagCounterStatus()).thenReturn(2);
        when(robot1.getLives()).thenReturn(0);
        when(robot2.getLives()).thenReturn(0);
        when(robot3.getLives()).thenReturn(0);
        when(robot5.getLives()).thenReturn(0);
        when(robot6.getLives()).thenReturn(0);
        when(robot7.getLives()).thenReturn(0);
        when(robot8.getLives()).thenReturn(0);

        assertEquals(player5, master.getWinner());

        // Gewinner durch Überleben
        master.init();
        master.addPlayer(player1);
        master.addPlayer(player2);
        master.addPlayer(player3);
        master.addPlayer(player4);
        master.addPlayer(player5);
        master.addPlayer(player6);
        master.addPlayer(player7);
        master.addPlayer(player8);
        when(board.getHighestFlagNumber()).thenReturn(5);
        master.initialiseBoard("checkmate");

        when(robot5.getFlagCounterStatus()).thenReturn(2);
        when(robot1.getLives()).thenReturn(0);
        when(robot2.getLives()).thenReturn(0);
        when(robot3.getLives()).thenReturn(0);
        when(robot5.getLives()).thenReturn(0);
        when(robot6.getLives()).thenReturn(0);
        when(robot7.getLives()).thenReturn(0);
        when(robot8.getLives()).thenReturn(0);

        assertEquals(player4, master.getWinner());
    }
    
    @Test
    public void isDrawTest() {
        master.init();
        master.addPlayer(player1);
        master.addPlayer(player2);
        master.addPlayer(player2);
        master.addPlayer(player4);
        master.addPlayer(player5);
        master.addPlayer(player6);
        master.addPlayer(player7);
        master.addPlayer(player8);
        when(board.getHighestFlagNumber()).thenReturn(5);
        master.initialiseBoard("checkmate");

        // Noch kein Unentschieden
        when(robot1.getLives()).thenReturn(3);
        when(robot2.getLives()).thenReturn(3);
        when(robot3.getLives()).thenReturn(3);
        when(robot4.getLives()).thenReturn(3);
        when(robot5.getLives()).thenReturn(3);
        when(robot6.getLives()).thenReturn(3);
        when(robot7.getLives()).thenReturn(3);
        when(robot8.getLives()).thenReturn(3);
        when(robot1.getFlagCounterStatus()).thenReturn(0);
        when(robot2.getFlagCounterStatus()).thenReturn(4);
        when(robot3.getFlagCounterStatus()).thenReturn(3);
        when(robot4.getFlagCounterStatus()).thenReturn(1);
        when(robot5.getFlagCounterStatus()).thenReturn(0);
        when(robot6.getFlagCounterStatus()).thenReturn(3);
        when(robot7.getFlagCounterStatus()).thenReturn(4);
        when(robot8.getFlagCounterStatus()).thenReturn(2);

        assertEquals(false, master.isDraw());
        
        
        // Unentschieden
        when(robot1.getLives()).thenReturn(0);
        when(robot2.getLives()).thenReturn(0);
        when(robot3.getLives()).thenReturn(0);
        when(robot4.getLives()).thenReturn(0);
        when(robot5.getLives()).thenReturn(0);
        when(robot6.getLives()).thenReturn(0);
        when(robot7.getLives()).thenReturn(0);
        when(robot8.getLives()).thenReturn(0);
        when(robot1.getFlagCounterStatus()).thenReturn(0);
        when(robot2.getFlagCounterStatus()).thenReturn(4);
        when(robot3.getFlagCounterStatus()).thenReturn(3);
        when(robot4.getFlagCounterStatus()).thenReturn(1);
        when(robot5.getFlagCounterStatus()).thenReturn(0);
        when(robot6.getFlagCounterStatus()).thenReturn(3);
        when(robot7.getFlagCounterStatus()).thenReturn(4);
        when(robot8.getFlagCounterStatus()).thenReturn(2);

        assertEquals(true, master.isDraw());
    }

}
