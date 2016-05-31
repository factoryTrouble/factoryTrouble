package de.uni_bremen.factroytrouble.board;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import de.uni_bremen.factroytrouble.gameobjects.ConveyorBelt;
import de.uni_bremen.factroytrouble.gameobjects.Flag;
import de.uni_bremen.factroytrouble.gameobjects.Gear;
import de.uni_bremen.factroytrouble.gameobjects.Hole;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.gameobjects.Wall;
import de.uni_bremen.factroytrouble.gameobjects.Workshop;

/**
 * 
 * @author Thorben
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class GameBoardTest {

    @Mock
    private Dock dock;
    @Mock
    private Field field1;
    @Mock
    private Field field2;
    @Mock
    private Robot robot1;
    @Mock
    private Robot robot2;
    @Mock
    private Robot robot3;
    @Mock
    private Robot robot4;
    @Mock
    private Hole hole;
    @Mock
    private Wall wall1;
    @Mock
    private Wall wall2;
    @Mock
    private Wall wall3;
    @Mock
    private Wall wall4;
    @Mock
    private Wall wall5;
    @Mock
    private ConveyorBelt belt1;
    @Mock
    private ConveyorBelt belt2;
    @Mock
    private Flag flag1;
    @Mock
    private Flag flag2;
    @Mock
    private Flag flag3;
    @Mock
    private Flag flag4;

    private ArrayList<Robot> robotList = new ArrayList<>();
    private Tile robotTile1;
    private Tile robotTile2;
    private Tile robotTile3;
    private Tile robotTile4;
    private Tile respawnTile1;
    private Tile respawnTile2;
    private Tile respawnTile3;
    private Tile respawnTile4;
    private Tile destinationTile;
    private Orientation robotOrientation1;
    private Orientation robotOrientation2;
    private Orientation robotOrientation3;
    private Orientation robotOrientation4;
    private int robot1Damage = 0;
    private int robot2Damage = 0;
    private int robot3Damage = 0;
    private int robot4Damage = 0;

    private Board board;

    @Before
    public void setup() {
        when(dock.getCoordinates()).thenReturn(new Point(0, 0));
        when(dock.hasTile(any())).thenReturn(false);
        when(field1.getCoordinates()).thenReturn(new Point(0, 1));
        when(field1.hasTile(any())).thenReturn(false);
        when(field2.getCoordinates()).thenReturn(new Point(1, 1));
        when(field2.hasTile(any())).thenReturn(false);

        when(robot1.getName()).thenReturn("Bob Meister");
        when(robot2.getName()).thenReturn("Saturn Vulkan Vulcan");
        when(robot3.getName()).thenReturn("AndréOhlroBot");
        when(robot4.getName()).thenReturn("CaptainFalko");

        when(robot1.getCurrentTile()).thenAnswer(invocation -> robotTile1);
        when(robot2.getCurrentTile()).thenAnswer(invocation -> robotTile2);
        when(robot3.getCurrentTile()).thenAnswer(invocation -> robotTile3);
        when(robot4.getCurrentTile()).thenAnswer(invocation -> robotTile4);
        when(robot1.getOrientation()).thenAnswer(invocation -> robotOrientation1);
        when(robot2.getOrientation()).thenAnswer(invocation -> robotOrientation2);
        when(robot3.getOrientation()).thenAnswer(invocation -> robotOrientation3);
        when(robot4.getOrientation()).thenAnswer(invocation -> robotOrientation4);
        when(robot1.getRespawnPoint()).thenAnswer(invocation -> respawnTile1);
        when(robot2.getRespawnPoint()).thenAnswer(invocation -> respawnTile2);
        when(robot3.getRespawnPoint()).thenAnswer(invocation -> respawnTile3);
        when(robot4.getRespawnPoint()).thenAnswer(invocation -> respawnTile4);
        doAnswer(invocation -> robotOrientation1 = Orientation.values()[(robotOrientation1.ordinal() + 1) % 4])
                .when(robot1).turn(true);
        doAnswer(invocation -> robotOrientation1 = Orientation.values()[(robotOrientation1.ordinal() - 1) % 4])
                .when(robot1).turn(false);
        doAnswer(invocation -> robotOrientation2 = Orientation.values()[(robotOrientation2.ordinal() + 1) % 4])
                .when(robot2).turn(true);
        doAnswer(invocation -> robotOrientation2 = Orientation.values()[(robotOrientation2.ordinal() - 1) % 4])
                .when(robot2).turn(false);
        doAnswer(invocation -> robotOrientation3 = Orientation.values()[(robotOrientation3.ordinal() + 1) % 4])
                .when(robot3).turn(true);
        doAnswer(invocation -> robotOrientation3 = Orientation.values()[(robotOrientation3.ordinal() - 1) % 4])
                .when(robot3).turn(false);
        doAnswer(invocation -> robotOrientation4 = Orientation.values()[(robotOrientation4.ordinal() + 1) % 4])
                .when(robot4).turn(true);
        doAnswer(invocation -> robotOrientation4 = Orientation.values()[(robotOrientation4.ordinal() - 1) % 4])
                .when(robot4).turn(false);
        doAnswer(invocation -> robotTile1 = null).when(robot1).setCurrentTile(null);
        doAnswer(invocation -> robotTile2 = null).when(robot2).setCurrentTile(null);
        doAnswer(invocation -> robotTile3 = null).when(robot3).setCurrentTile(null);
        doAnswer(invocation -> robotTile4 = null).when(robot4).setCurrentTile(null);
        doAnswer(invocation -> robotTile1 = null).when(robot1).kill();
        doAnswer(invocation -> robotTile2 = null).when(robot2).kill();
        doAnswer(invocation -> robotTile3 = null).when(robot3).kill();
        doAnswer(invocation -> robotTile4 = null).when(robot4).kill();

        robotList.add(robot1);
        robotList.add(robot2);
        robotList.add(robot3);
        robotList.add(robot4);

        HashMap<Point, Tile> dockMap = buildTileMap(Field.DIMENSION, Dock.SHORT_SIDE, dock);
        HashMap<Point, Tile> field1Map = buildTileMap(Field.DIMENSION, Field.DIMENSION, field1);
        HashMap<Point, Tile> field2Map = buildTileMap(Field.DIMENSION, Field.DIMENSION, field2);

        when(dock.getTiles()).thenReturn(dockMap);
        when(field1.getTiles()).thenReturn(field1Map);
        when(field2.getTiles()).thenReturn(field2Map);

        initWalls();
        initHoles();
        initConveyors();
        initGears();
        initPushers();
        initWorkshopsAndFlags();
        initDamage();
        initRegisters();
        initFlags();

        robotTile1 = dockMap.get(new Point(0, 0));
        robotTile2 = dockMap.get(new Point(1, 1));
        robotTile3 = dockMap.get(new Point(2, 2));
        robotTile4 = dockMap.get(new Point(3, 3));
        robotOrientation1 = Orientation.NORTH;
        robotOrientation2 = Orientation.NORTH;
        robotOrientation3 = Orientation.NORTH;
        robotOrientation4 = Orientation.NORTH;
        when(robot1.getHP()).thenReturn(1);
        when(robot2.getHP()).thenReturn(1);
        when(robot3.getHP()).thenReturn(1);
        when(robot4.getHP()).thenReturn(1);

        board = new GameBoard(dock, field1, field2);
    }

    private void initConveyors() {
        when(belt1.isExpress()).thenReturn(true);
        when(belt2.isExpress()).thenReturn(false);
        when(field2.getTiles().get(new Point(0, 0)).getFieldObject()).thenReturn(belt1);
        when(field2.getTiles().get(new Point(1, 0)).getFieldObject()).thenReturn(belt2);
    }

    private void initGears() {
        when(field2.getTiles().get(new Point(0, 1)).getFieldObject()).thenReturn(mock(Gear.class));
    }

    private void initPushers() {
        int[] activePhases = { 0, 2, 4 };
        when(field1.getTiles().get(new Point(5, 5)).getWalls().get(0).getPusherPhases()).thenReturn(activePhases);
    }

    private void initWorkshopsAndFlags() {
        when(field2.getTiles().get(new Point(1, 1)).getFieldObject()).thenReturn(mock(Workshop.class));
        when(field2.getTiles().get(new Point(1, 2)).getFieldObject()).thenReturn(mock(Flag.class));
    }

    // Mocke Registerkarten
    private void initRegisters() {
        // Roboter 1 bewegt sich 3 Felder vorwärts
        doAnswer(invocation -> {
            board.moveRobot(robot1);
            board.moveRobot(robot1);
            board.moveRobot(robot1);
            return null;
        }).when(robot1).executeNext();
        when(robot1.getNextPriority()).thenReturn(310);
        // Roboter 2 dreht sich nach links
        doAnswer(invocation -> {
            robot2.turn(true);
            return null;
        }).when(robot2).executeNext();
        when(robot2.getNextPriority()).thenReturn(120);
        // Roboter 3 bewegt sich zurück
        doAnswer(invocation -> board.backupRobot(robot3)).when(robot3).executeNext();
        when(robot3.getNextPriority()).thenReturn(50);
        // Roboter 4 mach eine U-Turn
        doAnswer(invocation -> {
            robot4.turn(true);
            robot4.turn(true);
            return null;
        }).when(robot4).executeNext();
        when(robot4.getNextPriority()).thenReturn(60);
    }

    private void initFlags() {
        when(field2.getTiles().get(new Point(11, 11)).getFieldObject()).thenReturn(flag1);
        when(field1.getTiles().get(new Point(11, 4)).getFieldObject()).thenReturn(flag2);
        when(field1.getTiles().get(new Point(11, 11)).getFieldObject()).thenReturn(flag3);
        when(field2.getTiles().get(new Point(4, 11)).getFieldObject()).thenReturn(flag4);
        when(flag1.getNumber()).thenReturn(1);
        when(flag1.getNumber()).thenReturn(2);
        when(flag1.getNumber()).thenReturn(3);
        when(flag1.getNumber()).thenReturn(4);
    }

    // mockt TileMaps für Fields
    private HashMap<Point, Tile> buildTileMap(int x, int y, Field field) {
        HashMap<Point, Tile> tileMap = new HashMap<>();

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                Tile tile = mock(Tile.class);
                Point point = new Point(i, j);
                when(tile.getCoordinates()).thenReturn(point);
                when(tile.hasWall(any())).thenAnswer(invocation -> {
                    for (Wall wall : tile.getWalls()) {
                        if (wall.getOrientation() == invocation.getArguments()[0]) {
                            return true;
                        }
                    }
                    return false;
                });
                when(tile.getRobot()).thenAnswer(invocation -> {
                    for (Robot robot : robotList) {
                        if (robot.getCurrentTile() == tile) {
                            return robot;
                        }
                    }
                    return null;
                });
                when(field.hasTile(tile)).thenReturn(true);
                doAnswer(invocation -> robotTile1 = tile).when(robot1).setCurrentTile(tile);
                doAnswer(invocation -> robotTile2 = tile).when(robot2).setCurrentTile(tile);
                doAnswer(invocation -> robotTile3 = tile).when(robot3).setCurrentTile(tile);
                doAnswer(invocation -> robotTile4 = tile).when(robot4).setCurrentTile(tile);
                tileMap.put(tile.getCoordinates(), tile);
            }
        }

        return tileMap;
    }

    // mockt Wände
    private void initWalls() {
        when(wall1.getOrientation()).thenReturn(Orientation.EAST);
        when(wall1.hasLaser()).thenReturn(1);
        when(wall1.getPusherPhases()).thenReturn(new int[0]);
        when(wall2.getOrientation()).thenReturn(Orientation.NORTH);
        when(wall2.hasLaser()).thenReturn(1);
        when(wall2.getPusherPhases()).thenReturn(new int[0]);
        when(wall3.getOrientation()).thenReturn(Orientation.EAST);
        when(wall3.hasLaser()).thenReturn(0);
        when(wall3.getPusherPhases()).thenReturn(new int[0]);
        when(wall4.getOrientation()).thenReturn(Orientation.WEST);
        when(wall4.hasLaser()).thenReturn(0);
        when(wall4.getPusherPhases()).thenReturn(new int[0]);
        when(wall5.getOrientation()).thenReturn(Orientation.SOUTH);
        when(wall5.hasLaser()).thenReturn(0);
        when(wall5.getPusherPhases()).thenReturn(new int[0]);

        ArrayList<Wall> wallList1 = new ArrayList<>();
        ArrayList<Wall> wallList2 = new ArrayList<>();
        ArrayList<Wall> wallList3 = new ArrayList<>();
        ArrayList<Wall> wallList4 = new ArrayList<>();
        ArrayList<Wall> wallList5 = new ArrayList<>();
        wallList1.add(wall1);
        wallList2.add(wall2);
        wallList3.add(wall3);
        wallList4.add(wall4);
        wallList5.add(wall3);
        wallList5.add(wall4);
        wallList5.add(wall5);

        when(field1.getTiles().get(new Point(5, 5)).getWalls()).thenReturn(wallList1);
        when(field2.getTiles().get(new Point(8, 7)).getWalls()).thenReturn(wallList1);
        when(field1.getTiles().get(new Point(3, 11)).getWalls()).thenReturn(wallList2);
        when(field1.getTiles().get(new Point(1, 5)).getWalls()).thenReturn(wallList3);
        when(field1.getTiles().get(new Point(5, 7)).getWalls()).thenReturn(wallList4);
        when(field2.getTiles().get(new Point(0, 2)).getWalls()).thenReturn(wallList5);
    }

    // mockt Löcher
    private void initHoles() {
        when(dock.getTiles().get(new Point(7, 0)).getFieldObject()).thenReturn(hole);
        when(dock.getTiles().get(new Point(9, 0)).getFieldObject()).thenReturn(hole);
        when(dock.getTiles().get(new Point(9, 1)).getFieldObject()).thenReturn(hole);
        when(dock.getTiles().get(new Point(10, 0)).getFieldObject()).thenReturn(hole);
        when(field2.getTiles().get(new Point(6, 11)).getFieldObject()).thenReturn(hole);
    }

    // mockt takeDamage()
    private void initDamage() {
        doAnswer(invocation -> robot1Damage++).when(robot1).takeDamage(any());
        doAnswer(invocation -> robot2Damage++).when(robot2).takeDamage(any());
        doAnswer(invocation -> robot3Damage++).when(robot3).takeDamage(any());
        doAnswer(invocation -> robot4Damage++).when(robot4).takeDamage(any());
    }

    @Test
    public void moveRobotTest() {
        moveRobotBasicTest();
        moveRobotBasicTest();
        moveRobotBorderTest();
        moveRobotWallTest();
        moveRobotPushTest();

    }

    // Testet Bewegungen in alle Richtungen
    private void moveRobotBasicTest() {
        // Bewegung auf Feld1 nach Osten
        robotTile1 = field1.getTiles().get(new Point(5, 6));
        robotOrientation1 = Orientation.EAST;
        destinationTile = board.moveRobot(robot1);
        assertEquals(new Point(6, 6), destinationTile.getCoordinates());
        assertTrue(field1.hasTile(destinationTile));

        // Bewegung auf Feld1 nach Norden
        robotTile1 = field1.getTiles().get(new Point(5, 6));
        robotOrientation1 = Orientation.NORTH;
        destinationTile = board.moveRobot(robot1);
        assertEquals(new Point(5, 7), destinationTile.getCoordinates());
        assertTrue(field1.hasTile(destinationTile));

        // Bewegung auf Feld1 nach Süden
        robotTile1 = field1.getTiles().get(new Point(5, 6));
        robotOrientation1 = Orientation.SOUTH;
        destinationTile = board.moveRobot(robot1);
        assertEquals(new Point(5, 5), destinationTile.getCoordinates());
        assertTrue(field1.hasTile(destinationTile));

        // Bewegung auf Feld1 nach Westen
        robotTile1 = field1.getTiles().get(new Point(5, 6));
        robotOrientation1 = Orientation.WEST;
        destinationTile = board.moveRobot(robot1);
        assertEquals(new Point(4, 6), destinationTile.getCoordinates());
        assertTrue(field1.hasTile(destinationTile));

        // Bewegung auf Dock nach Norden
        robotTile1 = dock.getTiles().get(new Point(4, 2));
        robotOrientation1 = Orientation.NORTH;
        destinationTile = board.moveRobot(robot1);
        assertEquals(new Point(4, 3), destinationTile.getCoordinates());
        assertTrue(dock.hasTile(destinationTile));

        // Bewegung nicht möglich; Roboter kaputt
        robotTile1 = null;
        robotOrientation1 = Orientation.NORTH;
        destinationTile = board.moveRobot(robot1);
        assertEquals(null, destinationTile);
    }

    // Testet Bewegungen runter vom Rand des jeweiligen Feldes
    private void moveRobotBorderTest() {
        // Bewegung am Rand von Feld1 nach Norden, kein Nachbarfeld vorhanden
        robotTile1 = field1.getTiles().get(new Point(5, 11));
        robotOrientation1 = Orientation.NORTH;
        destinationTile = board.moveRobot(robot1);
        assertEquals(null, destinationTile);

        // Bewegung am Rand von Feld1 nach Westen, kein Nachbarfeld vorhanden
        robotTile1 = field1.getTiles().get(new Point(0, 8));
        robotOrientation1 = Orientation.WEST;
        destinationTile = board.moveRobot(robot1);
        assertEquals(null, destinationTile);

        // Bewegung am Rand von Feld1 nach Osten, auf Feld2
        robotTile1 = field1.getTiles().get(new Point(11, 6));
        robotOrientation1 = Orientation.EAST;
        destinationTile = board.moveRobot(robot1);
        assertEquals(new Point(0, 6), destinationTile.getCoordinates());
        assertFalse(field1.hasTile(destinationTile));
        assertTrue(field2.hasTile(destinationTile));

        // Bewegung am Rand von Feld1 nach Süden, auf Dock
        robotTile1 = field1.getTiles().get(new Point(5, 0));
        robotOrientation1 = Orientation.SOUTH;
        destinationTile = board.moveRobot(robot1);
        assertEquals(new Point(5, 3), destinationTile.getCoordinates());
        assertFalse(field1.hasTile(destinationTile));
        assertTrue(dock.hasTile(destinationTile));

        // Bewegung am Rand von Feld2 nach Westen, auf Feld1
        robotTile1 = field2.getTiles().get(new Point(0, 10));
        robotOrientation1 = Orientation.WEST;
        destinationTile = board.moveRobot(robot1);
        assertEquals(new Point(11, 10), destinationTile.getCoordinates());
        assertFalse(field2.hasTile(destinationTile));
        assertTrue(field1.hasTile(destinationTile));
    }

    private void moveRobotWallTest() {
        // Bewegung nicht möglich: Wand auf gleicher Kachel
        robotTile1 = field1.getTiles().get(new Point(5, 5));
        robotOrientation1 = Orientation.EAST;
        destinationTile = board.moveRobot(robot1);
        assertEquals(new Point(5, 5), destinationTile.getCoordinates());
        assertTrue(field1.hasTile(destinationTile));

        // Bewegung nicht möglich: Wand auf Nachbarkachel
        robotTile1 = field1.getTiles().get(new Point(2, 5));
        robotOrientation1 = Orientation.WEST;
        destinationTile = board.moveRobot(robot1);
        assertEquals(new Point(2, 5), destinationTile.getCoordinates());
        assertTrue(field1.hasTile(destinationTile));

        // Bewegung nicht möglich: Wand auf Nachbarkachel, auf Nachbarfeld
        robotTile1 = field1.getTiles().get(new Point(11, 2));
        robotOrientation1 = Orientation.EAST;
        destinationTile = board.moveRobot(robot1);
        assertEquals(new Point(11, 2), destinationTile.getCoordinates());
        assertTrue(field1.hasTile(destinationTile));

        // Bewegung möglich: Wände nur in alle anderen Richtungen
        robotTile1 = field2.getTiles().get(new Point(0, 2));
        robotOrientation1 = Orientation.NORTH;
        destinationTile = board.moveRobot(robot1);
        assertEquals(new Point(0, 3), destinationTile.getCoordinates());
        assertTrue(field2.hasTile(destinationTile));
        assertFalse(field1.hasTile(destinationTile));
    }

    // Testet Bewegung + Schieben anderer Roboter
    private void moveRobotPushTest() {
        // Bewegung + Schieben eines anderen Roboters
        robotTile1 = dock.getTiles().get(new Point(5, 2));
        robotOrientation1 = Orientation.SOUTH;
        robotTile2 = dock.getTiles().get(new Point(5, 1));
        robotOrientation2 = Orientation.WEST;
        destinationTile = board.moveRobot(robot1);
        assertEquals(new Point(5, 1), destinationTile.getCoordinates());
        assertTrue(dock.hasTile(destinationTile));

        // Bewegung + Schieben zweier anderer Roboter; dritter Roboter auf
        // anderem Feld
        robotTile1 = field2.getTiles().get(new Point(1, 10));
        robotOrientation1 = Orientation.WEST;
        robotTile2 = field2.getTiles().get(new Point(0, 10));
        robotOrientation2 = Orientation.WEST;
        robotTile3 = field1.getTiles().get(new Point(11, 10));
        robotOrientation3 = Orientation.NORTH;
        destinationTile = board.moveRobot(robot1);
        assertEquals(new Point(0, 10), destinationTile.getCoordinates());
        assertTrue(field2.hasTile(destinationTile));

        // Bewegung + Schieben zweier anderer Roboter; nicht möglich wegen Wand
        // hinter drittem Roboter
        robotTile1 = field1.getTiles().get(new Point(3, 9));
        robotOrientation1 = Orientation.NORTH;
        robotTile2 = field1.getTiles().get(new Point(3, 10));
        robotOrientation2 = Orientation.WEST;
        robotTile3 = field1.getTiles().get(new Point(3, 11));
        robotOrientation3 = Orientation.SOUTH;
        destinationTile = board.moveRobot(robot1);
        assertEquals(new Point(3, 9), destinationTile.getCoordinates());
        assertTrue(field1.hasTile(destinationTile));
    }

    @Test
    public void backupRobotTest() {
        // Bewegung auf Feld1 nach Westen
        robotTile1 = field1.getTiles().get(new Point(5, 6));
        robotOrientation1 = Orientation.EAST;
        destinationTile = board.backupRobot(robot1);
        assertEquals(new Point(4, 6), destinationTile.getCoordinates());
        assertTrue(field1.hasTile(destinationTile));

        // Bewegung auf Feld1 nach Süden
        robotTile1 = field1.getTiles().get(new Point(5, 6));
        robotOrientation1 = Orientation.NORTH;
        destinationTile = board.backupRobot(robot1);
        assertEquals(new Point(5, 5), destinationTile.getCoordinates());
        assertTrue(field1.hasTile(destinationTile));

        // Bewegung auf Feld1 nach Norden
        robotTile1 = field1.getTiles().get(new Point(5, 6));
        robotOrientation1 = Orientation.SOUTH;
        destinationTile = board.backupRobot(robot1);
        assertEquals(new Point(5, 7), destinationTile.getCoordinates());
        assertTrue(field1.hasTile(destinationTile));

        // Bewegung auf Feld1 nach Osten
        robotTile1 = field1.getTiles().get(new Point(5, 6));
        robotOrientation1 = Orientation.WEST;
        destinationTile = board.backupRobot(robot1);
        assertEquals(new Point(6, 6), destinationTile.getCoordinates());
        assertTrue(field1.hasTile(destinationTile));

        // Bewegung von Feld 1 auf Dock nach Süden, schiebe anderen Roboter
        robotTile1 = field1.getTiles().get(new Point(4, 0));
        robotOrientation1 = Orientation.NORTH;
        robotTile2 = dock.getTiles().get(new Point(4, 3));
        robotOrientation2 = Orientation.WEST;
        destinationTile = board.backupRobot(robot1);
        assertEquals(new Point(4, 3), destinationTile.getCoordinates());
        assertTrue(dock.hasTile(destinationTile));
    }

    @Test
    public void pushRobotTest() {
        // Schiebung auf Feld1 nach Westen
        robotTile1 = field1.getTiles().get(new Point(5, 6));
        destinationTile = board.pushRobot(robot1, Orientation.WEST);
        assertEquals(new Point(4, 6), destinationTile.getCoordinates());
        assertTrue(field1.hasTile(destinationTile));

        // Schiebung auf Feld1 nach Süden
        robotTile1 = field1.getTiles().get(new Point(5, 6));
        destinationTile = board.pushRobot(robot1, Orientation.SOUTH);
        assertEquals(new Point(5, 5), destinationTile.getCoordinates());
        assertTrue(field1.hasTile(destinationTile));
    }

    @Test
    public void respawnRobotTest() {
        // Einfacher Respawn: Feld ist leer
        respawnTile1 = field2.getTiles().get(new Point(6, 9));
        destinationTile = board.respawnRobot(robot1);
        assertEquals(new Point(6, 9), destinationTile.getCoordinates());

        // Respawn: Feld ist besetzt
        respawnTile1 = field1.getTiles().get(new Point(8, 2));
        robotTile2 = field1.getTiles().get(new Point(8, 2));
        destinationTile = board.respawnRobot(robot1);
        assertEquals(new Point(8, 1), destinationTile.getCoordinates());

        // Respawn: Feld und erste sechs Ausweichfelder sind besetzt, Löcher
        // oder
        // nicht vorhanden
        respawnTile1 = dock.getTiles().get(new Point(8, 0));
        robotTile2 = dock.getTiles().get(new Point(8, 0));
        robotTile3 = dock.getTiles().get(new Point(8, 1));
        destinationTile = board.respawnRobot(robot1);
        assertEquals(new Point(8, 2), destinationTile.getCoordinates());
    }

    @Test
    public void fireLasersTest() {

        // Alle Roboter außer der vierte werden einmal getroffen (Feldlaser)
        robotTile1 = field1.getTiles().get(new Point(5, 5));
        robotTile2 = field1.getTiles().get(new Point(9, 7));
        robotTile3 = field1.getTiles().get(new Point(3, 9));
        robotTile4 = dock.getTiles().get(new Point(11, 0));
        robotOrientation1 = Orientation.WEST;
        robotOrientation2 = Orientation.WEST;
        robotOrientation3 = Orientation.SOUTH;
        robotOrientation4 = Orientation.SOUTH;
        board.fireLasers();
        assertEquals(1, robot1Damage);
        assertEquals(1, robot2Damage);
        assertEquals(1, robot3Damage);
        assertEquals(0, robot4Damage);

        // Roboter 1 sicher hinter Wand, Roboter 4 sicher hinter Roboter 3, alle
        // anderen werden einmal getroffen (Feldlaser)
        robot1Damage = 0;
        robot2Damage = 0;
        robot3Damage = 0;
        robot4Damage = 0;
        robotTile1 = field1.getTiles().get(new Point(1, 5));
        robotTile2 = field1.getTiles().get(new Point(5, 7));
        robotTile3 = field1.getTiles().get(new Point(3, 9));
        robotTile4 = field1.getTiles().get(new Point(3, 6));
        robotOrientation1 = Orientation.WEST;
        robotOrientation2 = Orientation.WEST;
        robotOrientation3 = Orientation.NORTH;
        robotOrientation4 = Orientation.SOUTH;
        board.fireLasers();
        assertEquals(0, robot1Damage);
        assertEquals(1, robot2Damage);
        assertEquals(1, robot3Damage);
        assertEquals(0, robot4Damage);

        // Roboter 1 nimmt 3 Schaden (Laser + Roboter 2), Roboter 2 nimmt 2
        // Schaden (Roboter 1 + Roboter 3),
        // Roboter 3 nicht in Schusslinie, Roboter 4 sicher hinter Roboter 2
        robot1Damage = 0;
        robot2Damage = 0;
        robot3Damage = 0;
        robot4Damage = 0;
        robotTile1 = field1.getTiles().get(new Point(3, 5));
        robotTile2 = field1.getTiles().get(new Point(3, 2));
        robotTile3 = field1.getTiles().get(new Point(6, 2));
        robotTile4 = field1.getTiles().get(new Point(1, 2));
        robotOrientation1 = Orientation.SOUTH;
        robotOrientation2 = Orientation.NORTH;
        robotOrientation3 = Orientation.WEST;
        robotOrientation4 = Orientation.NORTH;
        board.fireLasers();
        assertEquals(3, robot1Damage);
        assertEquals(2, robot2Damage);
        assertEquals(0, robot3Damage);
        assertEquals(0, robot4Damage);

        // Roboter 1 schießt ins Leere, Roboter 2 ist im PowerDown-Modus,
        // Roboter 3 und 4 schießen gegen eine Wand
        robot1Damage = 0;
        robot2Damage = 0;
        robot3Damage = 0;
        robot4Damage = 0;
        robotTile1 = field1.getTiles().get(new Point(2, 2));
        robotTile2 = field1.getTiles().get(new Point(4, 2));
        robotTile3 = field2.getTiles().get(new Point(0, 2));
        robotTile4 = field2.getTiles().get(new Point(0, 1));
        robotOrientation1 = Orientation.WEST;
        robotOrientation2 = Orientation.WEST;
        robotOrientation3 = Orientation.SOUTH;
        robotOrientation4 = Orientation.NORTH;
        when(robot2.isPoweredDown()).thenReturn(true);
        board.fireLasers();
        assertEquals(0, robot1Damage);
        assertEquals(0, robot2Damage);
        assertEquals(0, robot3Damage);
        assertEquals(0, robot4Damage);

        // Alle Roboter sind kaputt
        robot1Damage = 0;
        robot2Damage = 0;
        robot3Damage = 0;
        robot4Damage = 0;
        robotTile1 = null;
        robotTile2 = null;
        robotTile3 = null;
        robotTile4 = null;
        robotOrientation1 = Orientation.WEST;
        robotOrientation2 = Orientation.WEST;
        robotOrientation3 = Orientation.SOUTH;
        robotOrientation4 = Orientation.NORTH;
        board.fireLasers();
        assertEquals(0, robot1Damage);
        assertEquals(0, robot2Damage);
        assertEquals(0, robot3Damage);
        assertEquals(0, robot4Damage);
    }

    @Test
    public void moveConveyorsTest() {
        Tile tile1 = field2.getTiles().get(new Point(0, 0));
        Tile tile2 = field2.getTiles().get(new Point(1, 0));

        doAnswer(invocation -> {
            Tile tile = field2.getTiles().get(new Point(0, 1));
            field2.getTiles().get(new Point(0, 0)).getRobot().setCurrentTile(tile);
            return null;
        }).when(tile1).action();

        doAnswer(invocation -> {
            Tile tile = field2.getTiles().get(new Point(1, 1));
            field2.getTiles().get(new Point(1, 0)).getRobot().setCurrentTile(tile);
            return null;
        }).when(tile2).action();

        // Alle Roboter bewegen sich
        robotTile1 = field2.getTiles().get(new Point(0, 0));
        robotTile2 = field2.getTiles().get(new Point(1, 0));
        robotTile3 = field2.getTiles().get(new Point(0, 0));
        robotTile4 = field2.getTiles().get(new Point(1, 0));

        board.moveConveyors(true);
        board.moveConveyors(false);
        assertEquals(new Point(0, 1), robotTile1.getCoordinates());
        assertEquals(new Point(1, 1), robotTile2.getCoordinates());
        assertEquals(new Point(0, 1), robotTile3.getCoordinates());
        assertEquals(new Point(1, 1), robotTile4.getCoordinates());

        // Nur Roboter 1 und 2 bewegen sich: Roboter 3 ist tot, Roboter 4 nicht
        // auf Förderband
        robotTile1 = field2.getTiles().get(new Point(1, 0));
        robotTile2 = field2.getTiles().get(new Point(0, 0));
        robotTile3 = null;
        robotTile4 = field1.getTiles().get(new Point(5, 7));

        board.moveConveyors(true);
        board.moveConveyors(false);
        assertEquals(new Point(1, 1), robotTile1.getCoordinates());
        assertEquals(new Point(0, 1), robotTile2.getCoordinates());
        assertEquals(null, robotTile3);
        assertEquals(new Point(5, 7), robotTile4.getCoordinates());

        // Roboter 1 und 2 bewegen sich; Roboter 2 blockiert Roboter 1
        robotTile1 = field2.getTiles().get(new Point(0, 0));
        robotTile2 = field2.getTiles().get(new Point(1, 0));
        robotTile3 = null;
        robotTile4 = field1.getTiles().get(new Point(0, 0));

        doAnswer(invocation -> {
            if (robotTile2.getCoordinates().equals(new Point(1, 0))) {
                robotTile2.action();
                return null;
            } else {
                Tile tile = field2.getTiles().get(new Point(0, 1));
                field2.getTiles().get(new Point(0, 0)).getRobot().setCurrentTile(tile);
                return null;
            }
        }).when(tile1).action();

        doAnswer(invocation -> {
            Tile tile = field2.getTiles().get(new Point(1, 1));
            field2.getTiles().get(new Point(1, 0)).getRobot().setCurrentTile(tile);
            return null;
        }).when(tile2).action();

        board.moveConveyors(true);
        board.moveConveyors(false);
        assertEquals(new Point(0, 1), robotTile1.getCoordinates());
        assertEquals(new Point(1, 1), robotTile2.getCoordinates());
        assertEquals(null, robotTile3);
        assertEquals(new Point(0, 0), robotTile4.getCoordinates());
    }

    @Test
    public void rotateGearsTest() {
        board.rotateGears();
    }

    @Test
    public void activatePushersTest() {
        robotTile1 = field1.getTiles().get(new Point(5, 5));
        board.activatePushers(0);
        assertEquals(new Point(4, 5), robot1.getCurrentTile().getCoordinates());

        robotTile1 = field1.getTiles().get(new Point(5, 5));
        board.activatePushers(1);
        assertEquals(new Point(5, 5), robot1.getCurrentTile().getCoordinates());

        board.activatePushers(2);
        assertEquals(new Point(4, 5), robot1.getCurrentTile().getCoordinates());

        robotTile1 = field1.getTiles().get(new Point(5, 5));
        board.activatePushers(3);
        assertEquals(new Point(5, 5), robot1.getCurrentTile().getCoordinates());

        board.activatePushers(4);
        assertEquals(new Point(4, 5), robot1.getCurrentTile().getCoordinates());
    }

    @Test
    public void touchCheckpointsAndFlagsTest() {
        board.touchCheckpointsAndFlags();
    }

    @Test
    public void killRobotsTest() {
        // Roboter 1 lebt, Roboter 2 wurde vom Brett geschoben, Roboter 3 hat 0
        // HP, Roboter 4 wurde in ein Loch geschoben
        when(robot1.getHP()).thenReturn(5);
        when(robot2.getHP()).thenReturn(8);
        robotTile2 = null;
        when(robot3.getHP()).thenReturn(0);
        when(robot4.getHP()).thenReturn(8);
        robotTile4 = field2.getTiles().get((new Point(6, 11)));
        List<Robot> killedRobots = board.killRobots();
        assertEquals(3, killedRobots.size());
        assertFalse(killedRobots.contains(robot1));
        assertTrue(killedRobots.contains(robot2));
        assertTrue(killedRobots.contains(robot3));
        assertTrue(killedRobots.contains(robot4));
    }

    @Test
    public void executeNextRegistersTest() {
        // Alle Roboter bewegen sich unabhängig voneinander, Roboter 3 fällt vom
        // Brett
        robotTile1 = field2.getTiles().get(new Point(0, 2));
        robotTile2 = field2.getTiles().get(new Point(0, 7));
        robotTile3 = field2.getTiles().get(new Point(0, 11));
        robotTile4 = field2.getTiles().get(new Point(6, 6));
        robotOrientation1 = Orientation.NORTH;
        robotOrientation2 = Orientation.WEST;
        robotOrientation3 = Orientation.SOUTH;
        robotOrientation4 = Orientation.WEST;
        board.executeNextRegisters();
        assertEquals(new Point(0, 5), robotTile1.getCoordinates());
        assertEquals(Orientation.NORTH, robotOrientation1);
        assertEquals(new Point(0, 7), robotTile2.getCoordinates());
        assertEquals(Orientation.SOUTH, robotOrientation2);
        assertEquals(null, robotTile3);
        assertEquals(Orientation.SOUTH, robotOrientation3);
        assertEquals(new Point(6, 6), robotTile4.getCoordinates());
        assertEquals(Orientation.EAST, robotOrientation4);

        // Roboter 2 und 4 sind im Power Down, Roboter 1 bewegt sich erst und
        // schiebt Roboter 4 in ein Loch, wird dann von Roboter 3 vom Brett
        // geschoben, der sich als zweites bewegt
        robotTile1 = field2.getTiles().get(new Point(2, 11));
        robotTile2 = field2.getTiles().get(new Point(1, 1));
        robotTile3 = field2.getTiles().get(new Point(5, 10));
        robotTile4 = field2.getTiles().get(new Point(4, 11));
        when(robot2.isPoweredDown()).thenReturn(true);
        when(robot4.isPoweredDown()).thenReturn(true);
        robotOrientation1 = Orientation.EAST;
        robotOrientation2 = Orientation.WEST;
        robotOrientation3 = Orientation.SOUTH;
        robotOrientation4 = Orientation.WEST;
        board.executeNextRegisters();
        board.killRobots();
        assertEquals(null, robot1.getCurrentTile());
        assertEquals(Orientation.EAST, robotOrientation1);
        assertEquals(new Point(1, 1), robot2.getCurrentTile().getCoordinates());
        assertEquals(Orientation.WEST, robotOrientation2);
        assertEquals(new Point(5, 11), robot3.getCurrentTile().getCoordinates());
        assertEquals(Orientation.SOUTH, robotOrientation3);
        assertEquals(null, robot4.getCurrentTile());
        assertEquals(Orientation.WEST, robotOrientation4);

        // Alle Roboter zerstört oder im PowerDown
        robotTile1 = null;
        robotTile2 = field2.getTiles().get(new Point(1, 1));
        robotTile3 = null;
        robotTile4 = field2.getTiles().get(new Point(4, 11));
        when(robot2.isPoweredDown()).thenReturn(true);
        when(robot4.isPoweredDown()).thenReturn(true);
        robotOrientation1 = Orientation.EAST;
        robotOrientation2 = Orientation.WEST;
        robotOrientation3 = Orientation.SOUTH;
        robotOrientation4 = Orientation.WEST;
        board.executeNextRegisters();
        board.killRobots();
        assertEquals(null, robot1.getCurrentTile());
        assertEquals(Orientation.EAST, robotOrientation1);
        assertEquals(new Point(1, 1), robot2.getCurrentTile().getCoordinates());
        assertEquals(Orientation.WEST, robotOrientation2);
        assertEquals(null, robot3.getCurrentTile());
        assertEquals(Orientation.SOUTH, robotOrientation3);
        assertEquals(new Point(4, 11), robot4.getCurrentTile().getCoordinates());
        assertEquals(Orientation.WEST, robotOrientation4);
    }

    @Test
    public void getAbsoluteCoordinatesTest() {
        Tile tile;
        Point point;

        tile = board.getFields().get(new Point(0, 0)).getTiles().get(new Point(4, 2));
        point = board.getAbsoluteCoordinates(tile);
        assertEquals(new Point(4, 2), point);

        tile = board.getFields().get(new Point(0, 1)).getTiles().get(new Point(4, 2));
        point = board.getAbsoluteCoordinates(tile);
        assertEquals(new Point(4, 6), point);

        tile = board.getFields().get(new Point(1, 1)).getTiles().get(new Point(4, 2));
        point = board.getAbsoluteCoordinates(tile);
        assertEquals(new Point(16, 6), point);

        tile = mock(Tile.class);
        when(tile.getCoordinates()).thenReturn(new Point(6, 7));
        point = board.getAbsoluteCoordinates(tile);
        assertEquals(null, point);
    }

    @Test
    public void getHighestFlagNumberTest() {
        assertEquals(4, board.getHighestFlagNumber());
    }

}