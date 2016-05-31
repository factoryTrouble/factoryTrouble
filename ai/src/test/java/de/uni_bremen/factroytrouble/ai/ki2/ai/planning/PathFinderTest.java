package de.uni_bremen.factroytrouble.ai.ki2.ai.planning;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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

import de.uni_bremen.factroytrouble.ai.ki2.ai.memory.ScullyConveyorInfo;
import de.uni_bremen.factroytrouble.ai.ki2.api.decision.unconsciousness.UnconsciousnessUnit;
import de.uni_bremen.factroytrouble.ai.ki2.api.memory.Thought;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.player.GameMoveBackwardCard;
import de.uni_bremen.factroytrouble.player.GameMoveForwardCard;
import de.uni_bremen.factroytrouble.player.GameTurnLeftCard;
import de.uni_bremen.factroytrouble.player.GameTurnRightCard;
import de.uni_bremen.factroytrouble.player.GameUturnCard;

@RunWith(MockitoJUnitRunner.class)
public class PathFinderTest {
    @Mock
    GameTurnRightCard right1, right2, right3, right4;

    @Mock
    GameTurnLeftCard left1, left2, left3, left4;

    @Mock
    GameUturnCard uTurn1;

    @Mock
    GameMoveForwardCard move1_1, move1_2, move1_3, move2_1, move2_2, move3_1, move3_2;

    @Mock
    GameMoveBackwardCard back1;

    @Mock
    Thought thought1, thought2, thought3, thought4, thought5, thought6, thought7, thought8, thought9, thought10,
            thought11, thought12, thought13, thought14, thought15, thought16;
    @Mock
    UnconsciousnessUnit uncons;

    private PathFinder pathFinder;

    @Before
    public void setUp() throws Exception {
        pathFinder = new PathFinder(uncons);

        when(uncons.getInformation(Arrays.asList("left", "gears"))).thenReturn(thought5);
        when(thought5.getInformation()).thenReturn(Arrays.asList(Arrays.asList()));

        when(uncons.getInformation(Arrays.asList("right", "gears"))).thenReturn(thought6);
        when(thought6.getInformation()).thenReturn(Arrays.asList(Arrays.asList()));

        when(uncons.getInformation(Arrays.asList("highest", "point"))).thenReturn(thought3);
        when(thought3.getInformation()).thenReturn(Arrays.asList(new Point(11, 25)));

        when(uncons.getInformation(Arrays.asList("cards", "locked"))).thenReturn(thought2);
        when(thought2.getInformation()).thenReturn(Arrays.asList(Arrays.asList(move3_2)));

        when(uncons.getInformation(Arrays.asList("all", "conveyor"))).thenReturn(thought7);
        when(thought7.getInformation()).thenReturn(Arrays.asList(new HashMap<Point, ScullyConveyorInfo>()));

        when(uncons.getInformation(Arrays.asList("flag", "next"))).thenReturn(thought9);
        when(thought9.getInformation()).thenReturn(Arrays.asList(new Point(3, 4)));

        when(uncons.getInformation(Arrays.asList("my", "position"))).thenReturn(thought10);
        when(thought10.getInformation()).thenReturn(Arrays.asList(new Point(1, 1)));

        when(uncons.getInformation(Arrays.asList("my", "ori"))).thenReturn(thought11);
        when(thought11.getInformation()).thenReturn(Arrays.asList(Orientation.NORTH));

        List<Point> holes = new ArrayList<>();
        holes.addAll(Arrays.asList(new Point(1, -1), new Point(0, 1), new Point(2, -1), new Point(3, 2)));
        when(uncons.getInformation(Arrays.asList("holes"))).thenReturn(thought12);
        when(thought12.getInformation()).thenReturn(Arrays.asList(holes));

        when(uncons.getInformation(Arrays.asList("walls", "north"))).thenReturn(thought13);
        when(thought13.getInformation()).thenReturn(Arrays.asList(Arrays.asList(new Point(1, 3))));

        when(uncons.getInformation(Arrays.asList("walls", "east"))).thenReturn(thought14);
        when(thought14.getInformation()).thenReturn(Arrays.asList(Arrays.asList(new Point(9, 9))));

        when(uncons.getInformation(Arrays.asList("walls", "south"))).thenReturn(thought15);
        when(thought15.getInformation()).thenReturn(Arrays.asList(Arrays.asList(new Point(2, 4))));

        when(uncons.getInformation(Arrays.asList("walls", "west"))).thenReturn(thought16);
        when(thought16.getInformation()).thenReturn(Arrays.asList(Arrays.asList(new Point(6, 1))));

        when(uncons.getInformation(Arrays.asList("cards", "player"))).thenReturn(thought1);
        when(thought1.getInformation()).thenReturn(Arrays.asList(left1, left2, left3, left4, uTurn1,
                back1, right1, right2, right3, right4, move1_1, move1_2, move1_3, move2_1, move2_2, move3_1, move3_2));

        when(move1_1.getRange()).thenReturn(1);
        when(move1_2.getRange()).thenReturn(1);
        when(move1_3.getRange()).thenReturn(1);
        when(move2_1.getRange()).thenReturn(2);
        when(move2_2.getRange()).thenReturn(2);
        when(move3_1.getRange()).thenReturn(3);
        when(move3_2.getRange()).thenReturn(3);
    }

    @Test
    public void initiateList() {
        pathFinder.initiateList();
        HashMap<List<Integer>, List<Point>> results = pathFinder.planningHelper;
        List<Integer> north = Arrays.asList(5);
        List<Integer> east = Arrays.asList(-3, 7);
        List<Integer> south = Arrays.asList(-2, 1);
        assertTrue(results.keySet().containsAll((Arrays.asList(north, east, south))));
    }

    @Test
	public void addNewWaysRight() {
		Map<List<Integer>, List<Point>> results = pathFinder.addNewWays(
				Arrays.asList(-3, 4), new Point(2, 1), false);
        assertTrue(results.keySet().containsAll((Arrays.asList(Arrays.asList(-3, 1, -3, 1)))));
    }

    @Test
    public void addNewWaysLeftWithHole() {
        Map<List<Integer>, List<Point>> results = pathFinder.addNewWays(Arrays.asList(-3, 4), new Point(3, 1), true);
        assertTrue(results.keySet().isEmpty());
    }

    @Test
    public void sddNewWaysWithEmptyWay() {
        Map<List<Integer>, List<Point>> results = pathFinder.addNewWays(Arrays.asList(), new Point(2, 1), false);
        assertTrue(results.keySet().isEmpty());
    }

    @Test
    public void editOne() {
        List<Point> path = new ArrayList<>();
        path.addAll(Arrays.asList(new Point(2, 1), new Point(2, 2), new Point(2, 3), new Point(2, 4)));
        Map<List<Integer>, List<Point>> results = pathFinder.editOne(Arrays.asList(-3, 4), path, new Point(3, 1));
        assertTrue(results.keySet().containsAll((Arrays.asList(Arrays.asList(-3, 2)))));
    }

    @Test
    public void editOneWithEmptyWay() {
        List<Point> path = new ArrayList<>();
        path.addAll(Arrays.asList(new Point(2, 1), new Point(2, 2), new Point(2, 3), new Point(2, 4)));
        Map<List<Integer>, List<Point>> results = pathFinder.editOne(Arrays.asList(), path, new Point(3, 1));
        assertTrue(results.keySet().isEmpty());
    }

    @Test
    public void getClosestToGoal() {
        Point results = pathFinder.getClosestToGoal(Arrays.asList(new Point(4, 2), new Point(7, 2), new Point(2, 2)));
        assertEquals(results, new Point(4, 2));
    }

    @Test
    public void findPathOneStep() {
        List<List<Integer>> results = pathFinder.findPath();

        List<Integer> north = Arrays.asList(5);
        List<Integer> east = Arrays.asList(-3, 7);
        List<Integer> south = Arrays.asList(-2, 1);
        assertEquals(results, Arrays.asList(north, south, east));
    }

    @Test
    public void findPathTwoSteps() {
        List<List<Integer>> results = pathFinder.findPath();
        results = pathFinder.findPath();

        List<Integer> north = Arrays.asList(2);
        List<Integer> north2 = Arrays.asList(5);
        List<Integer> north3 = Arrays.asList(2, 1);
        List<Integer> northLeft = Arrays.asList(2, -1, 1);
        List<Integer> south = Arrays.asList(-2, 1);
        List<Integer> southRight = Arrays.asList(-2, 1, -3, 1);
        List<Integer> northRight = Arrays.asList(2, -3, 2);
        List<Integer> eastRight = Arrays.asList(-3, 1, -3, 1);
        List<Integer> eastLeft = Arrays.asList(-3, 1, -1, 2);
        List<Integer> east3 = Arrays.asList(-3, 1, 2);
        List<Integer> east2 = Arrays.asList(-3, 7);
        List<Integer> east = Arrays.asList(-3, 1);
        List<Integer> southLeft = Arrays.asList(-2, 1, -1, 1);

        assertEquals(results, Arrays.asList(north3, north, northLeft, north2, south, eastLeft, southRight, eastRight,
                east, northRight, east2, southLeft, east3));
    }

    @Test
    public void findPathThreeSteps() {
        List<List<Integer>> results = pathFinder.findPath();
        results = pathFinder.findPath();
        results = pathFinder.findPath();
        assertEquals(results.size(), 37);
    }

    @Test
    public void checkDistanceLeft1() {
        when(thought1.getInformation()).thenReturn(Arrays.asList(right1, right2, right3));

        List<Integer> results = pathFinder.checkDistance(Arrays.asList(-1));
        assertEquals(results, Arrays.asList(-3, -3, -3));
    }

    @Test
    public void checkDistanceLeft2() {
        when(thought1.getInformation()).thenReturn(Arrays.asList(right1, uTurn1));

        List<Integer> results = pathFinder.checkDistance(Arrays.asList(-1));
        assertEquals(results, Arrays.asList(-2, -3));
    }

    @Test
    public void checkDistanceLeft3() {
        when(thought1.getInformation()).thenReturn(Arrays.asList(move1_1));

        List<Integer> results = pathFinder.checkDistance(Arrays.asList(-1));
        assertEquals(results, Arrays.asList());
    }

    @Test
    public void checkDistanceLeft4() {
        when(thought1.getInformation()).thenReturn(Arrays.asList(move1_1, uTurn1));

        List<Integer> results = pathFinder.checkDistance(Arrays.asList(-1));
        assertEquals(results, Arrays.asList());
    }

    @Test
    public void checkDistanceRight1() {
        when(thought1.getInformation()).thenReturn(Arrays.asList(left1, left2, left3));

        List<Integer> results = pathFinder.checkDistance(Arrays.asList(-3));
        assertEquals(results, Arrays.asList(-1, -1, -1));
    }

    @Test
    public void checkDistanceRight2() {
        when(thought1.getInformation()).thenReturn(Arrays.asList(left1, uTurn1));

        List<Integer> results = pathFinder.checkDistance(Arrays.asList(-3));
        assertEquals(results, Arrays.asList(-2, -1));
    }

    @Test
    public void checkDistanceRight3() {
        when(thought1.getInformation()).thenReturn(Arrays.asList(move1_1));

        List<Integer> results = pathFinder.checkDistance(Arrays.asList(-3));
        assertEquals(results, Arrays.asList());
    }

    @Test
    public void checkDistanceRight4() {
        when(thought1.getInformation()).thenReturn(Arrays.asList(move1_1, uTurn1));

        List<Integer> results = pathFinder.checkDistance(Arrays.asList(-3));
        assertEquals(results, Arrays.asList());
    }

    @Test
    public void checkDistanceUTurn1() {
        when(thought1.getInformation()).thenReturn(Arrays.asList(left1, left2));

        List<Integer> results = pathFinder.checkDistance(Arrays.asList(-2));
        assertEquals(results, Arrays.asList(-1, -1));
    }

    @Test
    public void checkDistanceUTurn2() {
        when(thought1.getInformation()).thenReturn(Arrays.asList(right1, right2));

        List<Integer> results = pathFinder.checkDistance(Arrays.asList(-2));
        assertEquals(results, Arrays.asList(-3, -3));
    }

    @Test
    public void checkDistanceUTurn3() {
        when(thought1.getInformation()).thenReturn(Arrays.asList(move1_1));

        List<Integer> results = pathFinder.checkDistance(Arrays.asList(-2));
        assertEquals(results, Arrays.asList());
    }

    @Test
    public void checkDistanceForward() {
        when(thought1.getInformation()).thenReturn(Arrays.asList(move1_1, move2_2, move3_1, move3_2, back1));

        List<Integer> results = pathFinder.checkDistance(Arrays.asList(300));
        assertEquals(results, Arrays.asList(9));
    }
}
