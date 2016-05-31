package de.uni_bremen.factroytrouble.ai.ki1.planning;

import java.awt.Point;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import de.uni_bremen.factroytrouble.ai.ki1.LogicOne;
import de.uni_bremen.factroytrouble.board.Tile;

public class GoalTileTest {
    Tile tile2_2=Mockito.mock(Tile.class);
    @Before
    public void setUp(){
        Mockito.when(tile2_2.getAbsoluteCoordinates()).thenReturn(new Point(2,2));
    }
    @Test
    public void testGoalsAreNotEqual2() {
        Goal goal = new GoalTile(10, tile2_2);
        assertNotNull(goal);
    }

    @Test
    public void testGoalsAreNotEqual4() {
        Goal goal = new GoalTile(10, null);
        Goal goal2 = new GoalTile(10, tile2_2);
        assertNotEquals(goal, goal2);
    }

    @Test
    public void testGoalsAreNotEqual3() {
        Goal goal = new GoalTile(10, tile2_2);
        assertNotEquals(goal, new LogicOne());
    }

    @Test
    public void testGoalsAreEqual() {
        Tile tileOne = Mockito.mock(Tile.class);
        Tile tileTwo = Mockito.mock(Tile.class);
        Mockito.when(tileOne.getAbsoluteCoordinates()).thenReturn(new Point(2, 2));
        Mockito.when(tileTwo.getAbsoluteCoordinates()).thenReturn(new Point(2, 2));
        Goal goal = new GoalTile(10, tileOne);
        Goal goal2 = new GoalTile(10, tileTwo);
        assertEquals(goal, goal2);
    }

    @Test
    public void testGoalsAreEqual2() {
        Goal goal = new GoalTile(10, tile2_2);
        assertEquals(goal, goal);
    }

    @Test
    public void testGoalsAreNotEqual() {
        Tile tileOne = Mockito.mock(Tile.class);
        Tile tileTwo = Mockito.mock(Tile.class);
        Mockito.when(tileOne.getAbsoluteCoordinates()).thenReturn(new Point(2, 2));
        Mockito.when(tileTwo.getAbsoluteCoordinates()).thenReturn(new Point(3, 2));
        Goal goal = new GoalTile(10, tileOne);
        Goal goal2 = new GoalTile(10, tileTwo);
        assertNotEquals(goal, goal2);
    }
}
