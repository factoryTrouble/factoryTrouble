package de.uni_bremen.factroytrouble.gameobjects;

import de.uni_bremen.factroytrouble.board.Board;
import de.uni_bremen.factroytrouble.board.Field;
import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.misc.TestFactory;
import de.uni_bremen.factroytrouble.player.Master;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.awt.Point;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.*;

/**
 * 
 * @author Thorben
 */
@RunWith(MockitoJUnitRunner.class)
public class GameConveyorBeltTest {
    private static final int GAME_ID = 0;

    @Mock
    Master master;
    @Mock
    Board board;
    @Mock
    Field field;
    @Mock
    Robot robot1, robot2, robot3, robot4;
    @Mock
    Tile tile1, tile2, tile3, tile4, tile11, tile22, tile33, tile44, emptyTile, holeTile;

    Tile robotTile1, robotTile2, robotTile3, robotTile4;
    Orientation robotOrientation1, robotOrientation2, robotOrientation3, robotOrientation4;

    ConveyorBelt belt1, belt2, belt3, belt4, belt11, belt22, belt33, belt44;

    @Before
    public void setUp() {
        GameConveyorBelt.makeAllActive();

        belt1 = new GameConveyorBelt(GAME_ID, Orientation.NORTH, false);
        belt2 = new GameConveyorBelt(GAME_ID, Orientation.WEST, false);
        belt3 = new GameConveyorBelt(GAME_ID, Orientation.SOUTH, false);
        belt4 = new GameConveyorBelt(GAME_ID, Orientation.EAST, false);
        belt11 = new GameConveyorBelt(GAME_ID, Orientation.NORTH, true);
        belt22 = new GameConveyorBelt(GAME_ID, Orientation.WEST, true);
        belt33 = new GameConveyorBelt(GAME_ID, Orientation.SOUTH, true);
        belt44 = new GameConveyorBelt(GAME_ID, Orientation.EAST, true);

        mockRobots();

        when(master.getBoard()).thenReturn(board);
        TestFactory.setMaster(master);

        when(field.getCoordinates()).thenReturn(new Point(0, 1));

        mockTiles();
    }

    private void mockRobots() {
        when(robot1.getName()).thenReturn("BobMeister");
        when(robot2.getName()).thenReturn("CaptainFalko");
        when(robot3.getName()).thenReturn("MrMover");
        when(robot4.getName()).thenReturn("NeverLooksBack");
        when(robot1.getCurrentTile()).thenAnswer(invocation -> robotTile1);
        when(robot2.getCurrentTile()).thenAnswer(invocation -> robotTile2);
        when(robot3.getCurrentTile()).thenAnswer(invocation -> robotTile3);
        when(robot4.getCurrentTile()).thenAnswer(invocation -> robotTile4);
        doAnswer(invocation -> robotTile1 = null).when(robot1).setCurrentTile(null);
        doAnswer(invocation -> robotTile2 = null).when(robot2).setCurrentTile(null);
        doAnswer(invocation -> robotTile3 = null).when(robot3).setCurrentTile(null);
        doAnswer(invocation -> robotTile4 = null).when(robot4).setCurrentTile(null);
        when(robot1.getOrientation()).thenAnswer(invocation -> robotOrientation1);
        when(robot2.getOrientation()).thenAnswer(invocation -> robotOrientation2);
        when(robot3.getOrientation()).thenAnswer(invocation -> robotOrientation3);
        when(robot4.getOrientation()).thenAnswer(invocation -> robotOrientation4);
        doAnswer(invocation -> robotOrientation1 = Orientation.getNextDirection(robotOrientation1,
                invocation.getArgumentAt(0, boolean.class))).when(robot1).turn(anyBoolean());
        doAnswer(invocation -> robotOrientation2 = Orientation.getNextDirection(robotOrientation2,
                invocation.getArgumentAt(0, boolean.class))).when(robot2).turn(anyBoolean());
        doAnswer(invocation -> robotOrientation3 = Orientation.getNextDirection(robotOrientation3,
                invocation.getArgumentAt(0, boolean.class))).when(robot3).turn(anyBoolean());
        doAnswer(invocation -> robotOrientation4 = Orientation.getNextDirection(robotOrientation4,
                invocation.getArgumentAt(0, boolean.class))).when(robot4).turn(anyBoolean());
    }

    private void mockTiles() {
        when(tile1.getFieldObject()).thenReturn(belt1);
        when(tile2.getFieldObject()).thenReturn(belt2);
        when(tile3.getFieldObject()).thenReturn(belt3);
        when(tile4.getFieldObject()).thenReturn(belt4);
        when(tile11.getFieldObject()).thenReturn(belt11);
        when(tile22.getFieldObject()).thenReturn(belt22);
        when(tile33.getFieldObject()).thenReturn(belt33);
        when(tile44.getFieldObject()).thenReturn(belt44);
        when(tile1.getCoordinates()).thenReturn(new Point(0, 0));
        when(tile2.getCoordinates()).thenReturn(new Point(9, 9));
        when(tile3.getCoordinates()).thenReturn(new Point(5, 5));
        when(tile4.getCoordinates()).thenReturn(new Point(3, 3));
        when(tile11.getCoordinates()).thenReturn(new Point(2, 5));
        when(tile22.getCoordinates()).thenReturn(new Point(10, 8));
        when(tile33.getCoordinates()).thenReturn(new Point(1, 6));
        when(tile44.getCoordinates()).thenReturn(new Point(1, 9));
        when(board.findFieldOfTile(any())).thenReturn(field);
        doAnswer(invocation -> robotTile1 = invocation.getArgumentAt(0, Tile.class)).when(robot1).setCurrentTile(any());
        doAnswer(invocation -> robotTile2 = invocation.getArgumentAt(0, Tile.class)).when(robot2).setCurrentTile(any());
        doAnswer(invocation -> robotTile3 = invocation.getArgumentAt(0, Tile.class)).when(robot3).setCurrentTile(any());
        doAnswer(invocation -> robotTile4 = invocation.getArgumentAt(0, Tile.class)).when(robot4).setCurrentTile(any());
        when(emptyTile.getCoordinates()).thenReturn(new Point(0, 0));
        when(holeTile.getFieldObject()).thenReturn(mock(Hole.class));
        when(holeTile.getCoordinates()).thenReturn(new Point(0, 0));
    }

    @Test
    public void executeTest1() {
        // 4 Roboter bewegen sich unabhängig voneinander
        robotOrientation1 = Orientation.NORTH;
        robotOrientation2 = Orientation.NORTH;
        robotOrientation3 = Orientation.NORTH;
        robotOrientation4 = Orientation.NORTH;
        when(board.findNextTile(tile1, Orientation.NORTH)).thenReturn(tile11);
        when(board.findNextTile(tile2, Orientation.WEST)).thenReturn(tile22);
        when(board.findNextTile(tile3, Orientation.SOUTH)).thenReturn(tile33);
        when(board.findNextTile(tile4, Orientation.EAST)).thenReturn(tile44);
        when(tile1.getRobot()).thenReturn(robot1);
        when(tile2.getRobot()).thenReturn(robot2);
        when(tile3.getRobot()).thenReturn(robot3);
        when(tile4.getRobot()).thenReturn(robot4);

        belt1.execute(tile1);
        belt2.execute(tile2);
        belt3.execute(tile3);
        belt4.execute(tile4);
        assertEquals(tile11, robotTile1);
        assertEquals(tile22, robotTile2);
        assertEquals(tile33, robotTile3);
        assertEquals(tile44, robotTile4);
        assertEquals(Orientation.NORTH, robotOrientation1);
        assertEquals(Orientation.NORTH, robotOrientation2);
        assertEquals(Orientation.NORTH, robotOrientation3);
        assertEquals(Orientation.NORTH, robotOrientation4);
    }

    @Test
    public void executeTest2() {
        // 4 Roboter bewegen sich unabhängig voneinander, Roboter 2 wird nach
        // links, Roboter 3 nach rechts gedreht
        robotOrientation1 = Orientation.NORTH;
        robotOrientation2 = Orientation.NORTH;
        robotOrientation3 = Orientation.NORTH;
        robotOrientation4 = Orientation.NORTH;
        when(board.findNextTile(tile1, Orientation.NORTH)).thenReturn(tile11);
        when(board.findNextTile(tile2, Orientation.WEST)).thenReturn(tile33);
        when(board.findNextTile(tile3, Orientation.SOUTH)).thenReturn(tile22);
        when(board.findNextTile(tile4, Orientation.EAST)).thenReturn(tile44);
        when(tile1.getRobot()).thenReturn(robot1);
        when(tile2.getRobot()).thenReturn(robot2);
        when(tile3.getRobot()).thenReturn(robot3);
        when(tile4.getRobot()).thenReturn(robot4);

        belt1.execute(tile1);
        belt2.execute(tile2);
        belt3.execute(tile3);
        belt4.execute(tile4);
        assertEquals(tile11, robotTile1);
        assertEquals(tile33, robotTile2);
        assertEquals(tile22, robotTile3);
        assertEquals(tile44, robotTile4);
        assertEquals(Orientation.NORTH, robotOrientation1);
        assertEquals(Orientation.WEST, robotOrientation2);
        assertEquals(Orientation.EAST, robotOrientation3);
        assertEquals(Orientation.NORTH, robotOrientation4);
    }

    @Test
    public void executeTest3() {
        // Nur Expressförderbänder: Roboter 1 bewegt sich, Roboter 3
        // fällt vom Brett, Roboter 2 und 4 bleiben stehen, weil keine
        // Expressförderbänder
        robotOrientation1 = Orientation.NORTH;
        robotOrientation2 = Orientation.NORTH;
        robotOrientation3 = Orientation.NORTH;
        robotOrientation4 = Orientation.NORTH;
        when(board.findNextTile(tile11, Orientation.NORTH)).thenReturn(tile4);
        when(board.findNextTile(tile2, Orientation.WEST)).thenReturn(tile1);
        when(board.findNextTile(tile44, Orientation.EAST)).thenReturn(null);
        when(board.findNextTile(tile4, Orientation.EAST)).thenReturn(tile2);
        when(tile11.getRobot()).thenReturn(robot1);
        when(tile2.getRobot()).thenReturn(robot2);
        when(tile44.getRobot()).thenReturn(robot3);
        when(tile3.getRobot()).thenReturn(robot4);
        robotTile2 = tile2;
        robotTile4 = tile1;

        belt11.execute(tile11);
        belt22.execute(tile22);
        belt33.execute(tile33);
        belt44.execute(tile44);
        assertEquals(tile4, robotTile1);
        assertEquals(tile2, robotTile2);
        assertEquals(null, robotTile3);
        assertEquals(tile1, robotTile4);
        assertEquals(Orientation.EAST, robotOrientation1);
        assertEquals(Orientation.NORTH, robotOrientation2);
        assertEquals(Orientation.NORTH, robotOrientation3);
        assertEquals(Orientation.NORTH, robotOrientation4);
    }

    @Test
    public void executeTest4() {
        // Alle Roboter in einer Linie: Roboter 1 wird blockiert durch die
        // anderen,
        // welche dann zuerst angetriggert werden;
        //
        // erster Versuch funktioniert nicht, weil letztes Förderband in Kette
        // ein Expressförderband ist
        robotOrientation1 = Orientation.NORTH;
        robotOrientation2 = Orientation.NORTH;
        robotOrientation3 = Orientation.NORTH;
        robotOrientation4 = Orientation.NORTH;
        when(board.findNextTile(tile1, Orientation.NORTH)).thenReturn(tile2);
        when(board.findNextTile(tile2, Orientation.WEST)).thenReturn(tile3);
        when(board.findNextTile(tile3, Orientation.SOUTH)).thenReturn(tile33);
        when(board.findNextTile(tile33, Orientation.SOUTH)).thenReturn(tile44);
        when(tile1.getRobot()).thenAnswer(invocation -> robotTile1 == tile1 ? robot1 : null);
        when(tile2.getRobot()).thenAnswer(invocation -> robotTile2 == tile2 ? robot2 : null);
        when(tile3.getRobot()).thenAnswer(invocation -> robotTile3 == tile3 ? robot3 : null);
        when(tile33.getRobot()).thenAnswer(invocation -> robotTile4 == tile33 ? robot4 : null);
        robotTile1 = tile1;
        robotTile2 = tile2;
        robotTile3 = tile3;
        robotTile4 = tile33;

        belt1.execute(tile1);
        assertEquals(tile1, robotTile1);
        assertEquals(tile2, robotTile2);
        assertEquals(tile3, robotTile3);
        assertEquals(tile33, robotTile4);
        assertEquals(Orientation.NORTH, robotOrientation1);
        assertEquals(Orientation.NORTH, robotOrientation2);
        assertEquals(Orientation.NORTH, robotOrientation3);
        assertEquals(Orientation.NORTH, robotOrientation4);

        GameConveyorBelt.makeAllActive();

        // zweiter Versuch klappt: Alle Förderbandtypen gleich
        when(board.findNextTile(tile3, Orientation.SOUTH)).thenReturn(tile4);
        when(board.findNextTile(tile4, Orientation.EAST)).thenReturn(tile44);
        when(tile4.getRobot()).thenAnswer(invocation -> robotTile4 == tile4 ? robot4 : null);
        robotTile4 = tile4;

        belt1.execute(tile1);
        assertEquals(tile2, robotTile1);
        assertEquals(tile3, robotTile2);
        assertEquals(tile4, robotTile3);
        assertEquals(tile44, robotTile4);
        assertEquals(Orientation.WEST, robotOrientation1);
        assertEquals(Orientation.WEST, robotOrientation2);
        assertEquals(Orientation.WEST, robotOrientation3);
        assertEquals(Orientation.NORTH, robotOrientation4);
    }

    @Test
    public void executeTest5() {
        // Alle Roboter im Kreis: Keiner kann sich bewegen aufgrund von Deadlock
        robotOrientation1 = Orientation.NORTH;
        robotOrientation2 = Orientation.NORTH;
        robotOrientation3 = Orientation.NORTH;
        robotOrientation4 = Orientation.NORTH;
        when(board.findNextTile(tile1, Orientation.NORTH)).thenReturn(tile2);
        when(board.findNextTile(tile2, Orientation.WEST)).thenReturn(tile3);
        when(board.findNextTile(tile3, Orientation.SOUTH)).thenReturn(tile4);
        when(board.findNextTile(tile4, Orientation.EAST)).thenReturn(tile1);
        when(tile1.getRobot()).thenAnswer(invocation -> robotTile1 == tile1 ? robot1 : null);
        when(tile2.getRobot()).thenAnswer(invocation -> robotTile2 == tile2 ? robot2 : null);
        when(tile3.getRobot()).thenAnswer(invocation -> robotTile3 == tile3 ? robot3 : null);
        when(tile4.getRobot()).thenAnswer(invocation -> robotTile4 == tile4 ? robot4 : null);
        robotTile1 = tile1;
        robotTile2 = tile2;
        robotTile3 = tile3;
        robotTile4 = tile4;

        belt3.execute(tile3);
        belt4.execute(tile4);
        belt2.execute(tile2);
        belt1.execute(tile1);
        assertEquals(tile1, robotTile1);
        assertEquals(tile2, robotTile2);
        assertEquals(tile3, robotTile3);
        assertEquals(tile4, robotTile4);
        assertEquals(Orientation.NORTH, robotOrientation1);
        assertEquals(Orientation.NORTH, robotOrientation2);
        assertEquals(Orientation.NORTH, robotOrientation3);
        assertEquals(Orientation.NORTH, robotOrientation4);
    }

    @Test
    public void executeTest6() {
        // Roboter können nicht verschoben werden wegen Wänden, Zielfelder haben
        // keine Förderbänder
        robotOrientation1 = Orientation.NORTH;
        robotOrientation2 = Orientation.NORTH;
        when(board.findNextTile(tile1, Orientation.NORTH)).thenReturn(tile3);
        when(board.findNextTile(tile2, Orientation.WEST)).thenReturn(emptyTile);
        when(tile1.getRobot()).thenAnswer(invocation -> robotTile1 == tile1 ? robot1 : null);
        when(tile2.getRobot()).thenAnswer(invocation -> robotTile2 == tile2 ? robot2 : null);
        robotTile1 = tile1;
        robotTile2 = tile2;
        when(tile1.hasWall(Orientation.NORTH)).thenReturn(true);
        when(emptyTile.hasWall(Orientation.EAST)).thenReturn(true);

        belt1.execute(tile1);
        belt2.execute(tile2);
        assertEquals(tile1, robotTile1);
        assertEquals(tile2, robotTile2);
        assertEquals(Orientation.NORTH, robotOrientation1);
        assertEquals(Orientation.NORTH, robotOrientation2);
    }

    @Test
    public void executeTest7() {
        // Roboter 1 und 2 blockieren sich nicht: 2 steht nicht auf ConveyorBelt
        // Robiter 3 und 4 blockieren sich nicht: Beide werden ins Loch
        // geschoben
        robotOrientation1 = Orientation.NORTH;
        robotOrientation2 = Orientation.NORTH;
        robotOrientation3 = Orientation.NORTH;
        robotOrientation4 = Orientation.NORTH;
        when(board.findNextTile(tile1, Orientation.NORTH)).thenReturn(tile11);
        when(board.findNextTile(tile2, Orientation.WEST)).thenReturn(holeTile);
        when(board.findNextTile(tile3, Orientation.SOUTH)).thenReturn(holeTile);
        when(board.findNextTile(tile11, Orientation.EAST)).thenReturn(emptyTile);
        when(tile1.getRobot()).thenReturn(robot1);
        when(tile2.getRobot()).thenReturn(robot3);
        when(tile3.getRobot()).thenReturn(robot4);
        when(emptyTile.getRobot()).thenReturn(robot2);
        when(board.findNextTile(holeTile, Orientation.EAST)).thenReturn(tile2);
        when(board.findNextTile(holeTile, Orientation.NORTH)).thenReturn(tile3);
        robotTile2 = emptyTile;

        belt1.execute(tile1);
        belt2.execute(tile2);
        belt3.execute(tile3);
        assertEquals(tile11, robotTile1);
        assertEquals(emptyTile, robotTile2);
        assertEquals(holeTile, robotTile3);
        assertEquals(holeTile, robotTile4);
        assertEquals(Orientation.NORTH, robotOrientation1);
        assertEquals(Orientation.NORTH, robotOrientation2);
        assertEquals(Orientation.NORTH, robotOrientation3);
        assertEquals(Orientation.NORTH, robotOrientation4);
    }

    @Test
    public void executeTest8() {
        // Roboter 2 blockiert 1 nicht trotz gleichem Ziel: 2 steht nicht auf
        // Expressförderband
        // Roboter 3 und 4 blockieren sich, weil Zielfeld gleich und
        // Förderbandtyp gleich
        robotOrientation1 = Orientation.NORTH;
        robotOrientation2 = Orientation.NORTH;
        robotOrientation3 = Orientation.NORTH;
        robotOrientation4 = Orientation.NORTH;
        when(board.findNextTile(tile11, Orientation.NORTH)).thenReturn(emptyTile);
        when(board.findNextTile(tile2, Orientation.WEST)).thenReturn(emptyTile);
        when(board.findNextTile(tile3, Orientation.SOUTH)).thenReturn(tile22);
        when(board.findNextTile(tile4, Orientation.EAST)).thenReturn(tile22);
        when(tile11.getRobot()).thenReturn(robot1);
        when(tile2.getRobot()).thenReturn(robot2);
        when(tile22.getRobot()).thenReturn(robot3);
        when(tile4.getRobot()).thenReturn(robot4);
        when(board.findNextTile(holeTile, Orientation.SOUTH)).thenReturn(tile11);
        when(board.findNextTile(holeTile, Orientation.EAST)).thenReturn(tile2);
        when(board.findNextTile(tile22, Orientation.NORTH)).thenReturn(tile3);
        when(board.findNextTile(tile22, Orientation.WEST)).thenReturn(tile4);
        robotTile1 = tile11;
        robotTile2 = tile2;
        robotTile3 = tile3;
        robotTile4 = tile4;

        belt11.execute(tile11);
        belt3.execute(tile3);
        belt4.execute(tile4);
        assertEquals(emptyTile, robotTile1);
        assertEquals(tile2, robotTile2);
        assertEquals(tile3, robotTile3);
        assertEquals(tile4, robotTile4);
        assertEquals(Orientation.NORTH, robotOrientation1);
        assertEquals(Orientation.NORTH, robotOrientation2);
        assertEquals(Orientation.NORTH, robotOrientation3);
        assertEquals(Orientation.NORTH, robotOrientation4);
    }

}