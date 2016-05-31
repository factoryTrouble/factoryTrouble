package de.uni_bremen.factroytrouble.gameobjects;

import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.player.MasterFactory;
import de.uni_bremen.factroytrouble.player.ProgramCard;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GameRobotTest {
    private static final int GAME_ID = 0;
    @Mock
    private Tile respawn, current;
    @Mock
    private ProgramCard card1, card2, card3, card4, card5, card42, card84, card126;
    @Mock
    private MasterFactory factory;

    private ProgramCard[] regCompare, regRemoved, hand;
    private GameRobot robot;
    private List<ProgramCard> removed;

    @Before
    public void setUp() {
        regCompare = new ProgramCard[] { card1, card2, card3, card4, card5 };
        hand = new ProgramCard[] { card42, card84, card126 };
        robot = new GameRobot(GAME_ID, respawn, Orientation.NORTH, "testRobot");
        fillRegisters(Robot.MAX_REGISTERS);
        robot.setCurrentTile(respawn);
        robot.setRespawnPoint();
        robot.setCurrentTile(current);
        // Damagetoken, ohne dass ein Register gesperrt wird.
        for (int dam = 1; dam < 5; dam++) {
            robot.takeDamage(null);
        }

        when(card1.getPriority()).thenReturn(100);
        when(card2.getPriority()).thenReturn(200);
        when(card3.getPriority()).thenReturn(300);
        when(card4.getPriority()).thenReturn(60);
        when(card5.getPriority()).thenReturn(110);

        when(current.getCoordinates()).thenReturn(new Point(6, 6));
        when(respawn.getCoordinates()).thenReturn(new Point(6, 1));

        doAnswer(invocation -> robot).when(respawn).setRobot(robot);

    }

    @Test
    public void turnTest() {
        System.out.println("führe turntest aus");
        // testet turn auf korrekte Abfolge der Orientierungen
        robot.turn(true);
        assertEquals(Orientation.WEST, robot.getOrientation());
        robot.turn(true);
        assertEquals(Orientation.SOUTH, robot.getOrientation());
        robot.turn(true);
        assertEquals(Orientation.EAST, robot.getOrientation());
        robot.turn(true);
        assertEquals(Orientation.NORTH, robot.getOrientation());

        robot.turn(false);
        assertEquals(Orientation.EAST, robot.getOrientation());
        robot.turn(false);
        assertEquals(Orientation.SOUTH, robot.getOrientation());
        robot.turn(false);
        assertEquals(Orientation.WEST, robot.getOrientation());
        robot.turn(false);
        assertEquals(Orientation.NORTH, robot.getOrientation());
    }

    // lock5-1Test decken einige Testfälle von takeDamage mit ab
    // lock5-1Test decken das Locken der Register der Reihe nach mit ab
    // lock5-1Test decken die meisten Testfälle von emptyAllRegister mit ab
    @Test
    public void sequenceLock5Test() {
        robot.takeDamage(null);
        boolean[] locked = robot.registerLockStatus();
        assertFalse(locked[0]);
        assertFalse(locked[1]);
        assertFalse(locked[2]);
        assertFalse(locked[3]);
        assertTrue(locked[4]);
    }

    @Test
    public void sequenceLock4Test() {
        robot.takeDamage(null);
        robot.takeDamage(null);
        removed = robot.emptyAllRegister();
        assertEquals(3, removed.size());
        for (int ii = 0; ii < removed.size(); ii++) {
            assertEquals(regCompare[ii], removed.get(ii));
        }
    }

    @Test
    public void sequenceLock3Test() {
        for (int dd = 0; dd < 3; dd++) {
            robot.takeDamage(null);
        }
        removed = robot.emptyAllRegister();
        assertEquals(2, removed.size());
        for (int ii = 0; ii < removed.size(); ii++) {
            assertEquals(regCompare[ii], removed.get(ii));
        }
    }

    @Test
    public void sequenceLock2Test() {
        for (int dd = 0; dd < 4; dd++) {
            robot.takeDamage(null);
        }
        removed = robot.emptyAllRegister();
        assertEquals(1, removed.size());
        for (int ii = 0; ii < removed.size(); ii++) {
            assertEquals(regCompare[ii], removed.get(ii));
        }
    }

    @Test
    public void sequenceLock1Test() {
        for (int dd = 0; dd < 5; dd++) {
            robot.takeDamage(null);
        }
        removed = robot.emptyAllRegister();
        assertTrue(removed.isEmpty());
    }

    @Test
    public void emptyAllRegister() {
        regRemoved = robot.emptyAllRegister().toArray(new ProgramCard[Robot.MAX_REGISTERS]);
        assertTrue(Arrays.deepEquals(regCompare, regRemoved));
    }

    @Test
    public void singleLock5Test() {
        robot.lockRegister(4);
        removed = robot.emptyAllRegister();
        assertEquals(4, removed.size());
        for (int ii = 0; ii < removed.size(); ii++) {
            assertEquals(regCompare[ii], removed.get(ii));
        }
    }

    @Test
    public void singleLock4Test() {
        robot.lockRegister(3);
        regRemoved = robot.emptyAllRegister().toArray(new ProgramCard[Robot.MAX_REGISTERS - 1]);
        System.out.println(regRemoved[0] + "," + regRemoved[1] + "," + regRemoved[2] + "," + regRemoved[3]);
        assertTrue(Arrays.deepEquals(new ProgramCard[] { card1, card2, card3, card5 }, regRemoved));
    }

    @Test
    public void singleLock3Test() {
        robot.lockRegister(2);
        regRemoved = robot.emptyAllRegister().toArray(new ProgramCard[Robot.MAX_REGISTERS - 1]);
        System.out.println(regRemoved[0] + "," + regRemoved[1] + "," + regRemoved[2] + "," + regRemoved[3]);
        assertTrue(Arrays.deepEquals(new ProgramCard[] { card1, card2, card4, card5 }, regRemoved));
    }

    @Test
    public void singleLock2Test() {
        robot.lockRegister(1);
        regRemoved = robot.emptyAllRegister().toArray(new ProgramCard[Robot.MAX_REGISTERS - 1]);
        System.out.println(regRemoved[0] + "," + regRemoved[1] + "," + regRemoved[2] + "," + regRemoved[3]);
        assertTrue(Arrays.deepEquals(new ProgramCard[] { card1, card3, card4, card5 }, regRemoved));
    }

    @Test
    public void singleLock1Test() {
        robot.lockRegister(0);
        regRemoved = robot.emptyAllRegister().toArray(new ProgramCard[Robot.MAX_REGISTERS - 1]);
        System.out.println(regRemoved[0] + "," + regRemoved[1] + "," + regRemoved[2] + "," + regRemoved[3]);
        assertTrue(Arrays.deepEquals(new ProgramCard[] { card2, card3, card4, card5 }, regRemoved));
    }

    @Test
    public void singleFreeRegisterTest() {
        for (int dd = 0; dd < 5; dd++) {
            robot.takeDamage(null);
        }
        robot.freeRegister(2);
        removed = robot.emptyAllRegister();
        assertEquals(1, removed.size());
        assertNull(removed.get(0));
    }

    @Test
    public void freeNextRegisterTest() {
        robot.lockRegister(1);
        robot.lockRegister(3);
        robot.freeNextRegister();
        regRemoved = robot.emptyAllRegister().toArray(new ProgramCard[Robot.MAX_REGISTERS - 1]);
        assertTrue(Arrays.deepEquals(new ProgramCard[] { card1, null, card3, card5 }, regRemoved));
    }

    @Test
    public void lockNextRegister5Test() {
        robot.lockRegister(1);
        robot.lockRegister(3);
        robot.lockNextRegister();
        regRemoved = robot.emptyAllRegister().toArray(new ProgramCard[Robot.MAX_REGISTERS - 3]);
        assertTrue(Arrays.deepEquals(new ProgramCard[] { card1, card3 }, regRemoved));
    }

    @Test
    public void lockNextRegister4Test() {
        robot.lockRegister(2);
        robot.lockRegister(4);
        robot.lockNextRegister();
        regRemoved = robot.emptyAllRegister().toArray(new ProgramCard[Robot.MAX_REGISTERS - 3]);
        assertTrue(Arrays.deepEquals(new ProgramCard[] { card1, card2 }, regRemoved));
    }

    @Test
    public void lockNextRegisterOverflowTest() {
        for (int ii = 0; ii < Robot.MAX_REGISTERS + 1; ii++) {
            robot.lockNextRegister();
        }
        assertEquals(0, robot.emptyAllRegister().size());
    }

    @Test
    public void emptyRegisterSuccessTest() {
        regRemoved = new ProgramCard[Robot.MAX_REGISTERS];
        for (int ii = 0; ii < regRemoved.length; ii++) {
            regRemoved[ii] = robot.emptyRegister(ii);
        }
        assertTrue(Arrays.deepEquals(regCompare, regRemoved));
    }

    @Test
    public void emptyRegisterFailTest() {
        robot.lockRegister(3);
        assertEquals(null, robot.emptyRegister(3));
    }

    @Test
    public void heal1HPTest() {
        int hpBefore = robot.getHP();
        assertEquals(6, hpBefore);
        robot.heal();
        assertEquals(hpBefore + 1, robot.getHP());
    }

    @Test
    public void healFailDeadTest() {
        for (int dd = 0; dd < 6; dd++) {
            robot.takeDamage(null);
        }
        int hpBefore = robot.getHP();
        assertEquals(0, hpBefore);
        robot.heal();
        assertEquals(hpBefore, robot.getHP());
    }

    @Test
    public void healFailUndamagedTest() {
        for (int dd = 0; dd < 4; dd++) {
            robot.heal();
        }
        int hpBefore = robot.getHP();
        assertEquals(10, hpBefore);
        robot.heal();
        assertEquals(hpBefore, robot.getHP());
    }

    @Test
    public void healFreeNextRegisterTest() {
        robot.takeDamage(null);
        robot.takeDamage(null);
        robot.heal();
        regRemoved = robot.emptyAllRegister().toArray(new ProgramCard[Robot.MAX_REGISTERS - 1]);
        assertTrue(Arrays.deepEquals(new ProgramCard[] { card1, card2, card3, null }, regRemoved));
    }

    @Test
    public void healFullyTest() {
        while (robot.getHP() > 0) {
            robot.takeDamage(null);
        }
        robot.healFully();
        regRemoved = robot.emptyAllRegister().toArray(new ProgramCard[Robot.MAX_REGISTERS]);
        assertTrue(Arrays.deepEquals(regCompare, regRemoved) && robot.getHP() == 10);
    }

    @Test
    public void touchFlagTest() {
        robot.touchFlag();
        robot.touchFlag();
        assertEquals(2, robot.getFlagCounterStatus());
    }

    @Test
    public void killLPTest() {
        robot.kill();
        assertEquals(2, robot.getLives());
    }

    @Test
    public void killFreeAllRegisterTest() {
        while (robot.getHP() > 0) {
            robot.takeDamage(null);
        }
        robot.kill();
        regRemoved = robot.emptyAllRegister().toArray(new ProgramCard[Robot.MAX_REGISTERS]);
        ProgramCard[] expected = new ProgramCard[] { null, null, null, null, null };
        assertTrue(Arrays.deepEquals(expected, regRemoved));
    }

    @Test
    public void killCurrentTileTest() {
        robot.kill();
        assertEquals(null, robot.getCurrentTile());
    }

    @Test
    public void killGameOverTest() {
        robot.kill();
        robot.respawn(robot.getRespawnPoint());
        robot.kill();
        robot.respawn(robot.getRespawnPoint());
        robot.kill();
        assertEquals(null, robot.getRespawnPoint());
    }

    @Test
    public void doubleKillTest() {
        robot.kill();
        robot.kill();
        assertEquals(2, robot.getLives());
    }

    @Test
    public void fillSingleRegisterSuccessTest() {
        robot.emptyRegister(3);
        robot.fillRegister(3, card42);
        regRemoved = robot.emptyAllRegister().toArray(new ProgramCard[Robot.MAX_REGISTERS]);
        assertTrue(Arrays.deepEquals(new ProgramCard[] { card1, card2, card3, card42, card5 }, regRemoved));
    }

    @Test
    public void fillSingleFullRegisterTest() {
        assertTrue(!robot.fillRegister(3, card42));
    }

    @Test
    public void fillSingleLockedEmptyRegister() {
        robot.emptyRegister(4);
        robot.takeDamage(null);
        assertTrue(!robot.fillRegister(4, card42));
    }

    @Test
    public void fillEmptyRegisterOrderTest() {
        robot.emptyRegister(4);
        robot.emptyRegister(3);
        List<ProgramCard> handList = new ArrayList<>();
        for (ProgramCard card : hand) {
            handList.add(card);
        }
        robot.fillEmptyRegisters(handList);
        List<ProgramCard> registerCards = robot.emptyAllRegister();
        assertEquals(5, registerCards.size());
        assertTrue(registerCards.containsAll(Arrays.asList(card1, card2, card3)));
        boolean combo1 = registerCards.containsAll(Arrays.asList(card42, card84));
        boolean combo2 = registerCards.containsAll(Arrays.asList(card42, card126));
        boolean combo3 = registerCards.containsAll(Arrays.asList(card84, card126));
        assertTrue(combo1 || combo2 || combo3);
    }

    @Test
    public void fillEmptyRegisterConfusedTest() {
        robot.emptyRegister(1);
        robot.emptyRegister(3);
        List<ProgramCard> handList = new ArrayList<>();
        for (ProgramCard card : hand) {
            handList.add(card);
        }
        robot.fillEmptyRegisters(handList);
        List<ProgramCard> registerCards = robot.emptyAllRegister();
        assertEquals(5, registerCards.size());
        assertTrue(registerCards.containsAll(Arrays.asList(card1, card3, card5)));
        boolean combo1 = registerCards.containsAll(Arrays.asList(card42, card84));
        boolean combo2 = registerCards.containsAll(Arrays.asList(card42, card126));
        boolean combo3 = registerCards.containsAll(Arrays.asList(card84, card126));
        assertTrue(combo1 || combo2 || combo3);
    }

    @Test
    public void executeRegisterTest() {
        int[] prioritiesCompare = new int[] { 100, 200, 300, 60, 110 };
        for (int ii = 0; ii < Robot.MAX_REGISTERS; ii++) {
            assertEquals(prioritiesCompare[ii], robot.getNextPriority());
            robot.executeNext();
        }
    }

    @Test
    public void executeNextPoweredDownTest() {
        robot.powerDown();
        for (int ii = 0; ii < Robot.MAX_REGISTERS; ii++) {
            assertEquals(100, robot.getNextPriority());
        }
    }

    @Test
    public void executeNextPhaseTest() {
        robot.executeNext();
        robot.executeNext();
        assertEquals(2, robot.getRegPhase());
    }

    @Test
    public void executeNextRegisterOverflowTest() {
        for (int ii = 0; ii < Robot.MAX_REGISTERS; ii++) {
            robot.executeNext();
        }
        robot.executeNext();
        assertEquals(200, robot.getNextPriority());
    }

    @Test
    public void singleExecutePoweredDownTest() {
        robot.powerDown();
        robot.executeNext();
        assertTrue(100 == robot.getNextPriority() && robot.isPoweredDown());
    }

    @Test
    public void powerDownTest() {
        robot.powerDown();
        assertTrue(robot.isPoweredDown());
    }

    @Test
    public void wakeUpTest() {
        robot.powerDown();
        robot.wakeUp();
        assertTrue(!robot.isPoweredDown());
    }

    @Test
    public void respawnRobotSuccesTest() {
        robot.kill();
        robot.respawn(robot.getRespawnPoint());
        assertEquals(respawn, robot.getCurrentTile());
    }

    @Test
    public void respawnRobotFailTest() {
        robot.kill();
        robot.respawn(robot.getRespawnPoint());
        robot.kill();
        robot.respawn(robot.getRespawnPoint());
        robot.kill();
        assertTrue(!robot.respawn(robot.getRespawnPoint()));
    }

    @Test
    public void robotCloneTest() {
        robot.touchFlag();
        robot.turn(true);
        robot.executeNext();
        robot.powerDown();
        robot.kill();
        robot.respawn(respawn);
        robot.kill();
        robot.respawn(respawn);
        GameRobot cloned = (GameRobot) robot.clone(robot.getCurrentTile());
        assertEquals(robot.getName(), cloned.getName());
        assertEquals(robot.getCurrentTile(), cloned.getCurrentTile());
        assertEquals(robot.getHP(), cloned.getHP());
        assertEquals(robot.getLives(), cloned.getLives());
        assertEquals(robot.getFlagCounterStatus(), cloned.getFlagCounterStatus());
        assertEquals(robot.getObserverList(), cloned.getObserverList());
        assertEquals(robot.getOptions(), cloned.getOptions());
        assertEquals(robot.getOrientation(), cloned.getOrientation());
    }

    // füllt die Register wieder auf beginnend mit card1 beim ersten Register.
    private void fillRegisters(int toFill) {
        for (int ii = 0; ii < toFill; ii++) {
            robot.fillRegister(ii, regCompare[ii]);
        }
    }
}