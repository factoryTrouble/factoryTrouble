package de.uni_bremen.factroytrouble.ai.ki2.ai.planning;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import de.uni_bremen.factroytrouble.ai.ais.AIPlayer2;
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
public class KillpathFinderTest {
    @Mock
    private Point flagPos;

    @Mock
    private Point myPos;

    @Mock
    private HashMap<List<Integer>, List<Point>> planningHelper;

    @Mock
    private AIPlayer2 player;

    @Mock
    private UnconsciousnessUnit uncons;

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
            thought11, thought12, thought13, thought14, thought15, thought16, thought33;

    private KillpathFinder killpathFinder;

    @Before
    public void setUp() {
        killpathFinder = new KillpathFinder(uncons, player);

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

        when(uncons.getInformation(Arrays.asList("all", "enemies"))).thenReturn(thought33);
        HashMap<Point, Point> enemies = new HashMap<>();
        when(thought33.getInformation()).thenReturn(Arrays.asList(enemies));

        when(uncons.getInformation(Arrays.asList("flag", "next"))).thenReturn(thought9);
        when(thought9.getInformation()).thenReturn(Arrays.asList(new Point(3, 4)));

        when(uncons.getInformation(Arrays.asList("my", "position"))).thenReturn(thought10);
        when(thought10.getInformation()).thenReturn(Arrays.asList(new Point(1, 1)));

        when(uncons.getInformation(Arrays.asList("my", "ori"))).thenReturn(thought11);
        when(thought11.getInformation()).thenReturn(Arrays.asList(Orientation.NORTH));

        List<Point> holes = new ArrayList<>();
        holes.addAll(Arrays.asList(new Point(1, 3), new Point(0, 1), new Point(2, -1), new Point(3, 2)));
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
        when(thought1.getInformation()).thenReturn(Arrays.asList(left1, left2, left3, left4, uTurn1, back1, right1,
                right2, right3, right4, move1_1, move1_2, move1_3, move2_1, move2_2, move3_1, move3_2));

        when(move1_1.getRange()).thenReturn(1);
        when(move1_2.getRange()).thenReturn(1);
        when(move1_3.getRange()).thenReturn(1);
        when(move2_1.getRange()).thenReturn(2);
        when(move2_2.getRange()).thenReturn(2);
        when(move3_1.getRange()).thenReturn(3);
        when(move3_2.getRange()).thenReturn(3);
    }

    @Test
    public void findKillPathWithoutEnemyToKill() {
        HashMap<Point, Point> enemies = new HashMap<>();
        enemies.put(new Point(9, 6), new Point(5, 5));
        when(uncons.getInformation(Arrays.asList("all", "enemies"))).thenReturn(thought33);
        when(thought33.getInformation()).thenReturn(Arrays.asList(enemies));
        assertTrue(!killpathFinder.findKillPath().isEmpty());
    }

    @Test
    public void findKillPathWithEnemyToKill() {
        HashMap<Point, Point> enemies = new HashMap<>();
        enemies.put(new Point(1, 2), new Point(5, 5));
        enemies.put(new Point(3, 4), new Point(5, 5));
        when(uncons.getInformation(Arrays.asList("all", "enemies"))).thenReturn(thought33);
        when(thought33.getInformation()).thenReturn(Arrays.asList(enemies));
        assertTrue(!killpathFinder.findKillPath().isEmpty());
    }

    @Test
    public void findKillPathWithHole2() {
        HashMap<Point, Point> enemies = new HashMap<>();
        List<Point> holes = new ArrayList<>();
        holes.addAll(Arrays.asList(new Point(1, 4), new Point(0, 1), new Point(2, -1), new Point(3, 2)));
        when(uncons.getInformation(Arrays.asList("holes"))).thenReturn(thought12);
        when(thought12.getInformation()).thenReturn(Arrays.asList(holes));
        enemies.put(new Point(1, 2), new Point(5, 5));
        enemies.put(new Point(3, 4), new Point(5, 5));
        when(uncons.getInformation(Arrays.asList("all", "enemies"))).thenReturn(thought33);
        when(thought33.getInformation()).thenReturn(Arrays.asList(enemies));
        assertTrue(!killpathFinder.findKillPath().isEmpty());
    }

    @Test
    public void findKillPathWithHole3() {
        HashMap<Point, Point> enemies = new HashMap<>();
        List<Point> holes = new ArrayList<>();
        holes.addAll(Arrays.asList(new Point(1, 5), new Point(0, 1), new Point(2, -1), new Point(3, 2)));
        when(uncons.getInformation(Arrays.asList("holes"))).thenReturn(thought12);
        when(thought12.getInformation()).thenReturn(Arrays.asList(holes));
        enemies.put(new Point(1, 2), new Point(5, 5));
        enemies.put(new Point(3, 4), new Point(5, 5));
        when(uncons.getInformation(Arrays.asList("all", "enemies"))).thenReturn(thought33);
        when(thought33.getInformation()).thenReturn(Arrays.asList(enemies));
        assertTrue(!killpathFinder.findKillPath().isEmpty());
    }

}
