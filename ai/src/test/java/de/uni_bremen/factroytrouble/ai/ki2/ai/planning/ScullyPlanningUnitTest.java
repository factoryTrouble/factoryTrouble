package de.uni_bremen.factroytrouble.ai.ki2.ai.planning;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import de.uni_bremen.factroytrouble.ai.ais.AIPlayer2;
import de.uni_bremen.factroytrouble.ai.ki2.ai.memory.ScullyConveyorInfo;
import de.uni_bremen.factroytrouble.ai.ki2.api.decision.consciousness.Approach;
import de.uni_bremen.factroytrouble.ai.ki2.api.decision.consciousness.ConsciousnessUnit;
import de.uni_bremen.factroytrouble.ai.ki2.api.decision.unconsciousness.UnconsciousnessUnit;
import de.uni_bremen.factroytrouble.ai.ki2.api.memory.Thought;
import de.uni_bremen.factroytrouble.ai.ki2.api.planning.Strategy;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.player.GameMoveBackwardCard;
import de.uni_bremen.factroytrouble.player.GameMoveForwardCard;
import de.uni_bremen.factroytrouble.player.GameTurnLeftCard;
import de.uni_bremen.factroytrouble.player.GameTurnRightCard;
import de.uni_bremen.factroytrouble.player.GameUturnCard;
import de.uni_bremen.factroytrouble.player.Master;
import de.uni_bremen.factroytrouble.player.MasterFactory;
import de.uni_bremen.factroytrouble.player.ProgramCard;

@RunWith(MockitoJUnitRunner.class)
public class ScullyPlanningUnitTest {

    ScullyPlanningUnit planning;

    @Mock
    Strategy strategy1;

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
    ConsciousnessUnit cons;

    @Mock
    UnconsciousnessUnit uncons;

    @Mock
    AIPlayer2 player;

    @Mock
    MasterFactory mf;

    @Mock
    Master m;

    @Mock
    Robot robot;

    @Before
    public void setUp() {
        planning = new ScullyPlanningUnit(uncons, cons, player);

        when(m.requestPowerDownStatusChange(robot)).thenReturn(true);
        when(mf.getMaster(1)).thenReturn(m);
        when(player.getGameID()).thenReturn(1);
        when(player.getMasterFactory()).thenReturn(mf);

        when(cons.decide(any(Approach.class), any(Integer.class))).thenReturn(true);

        when(player.getRobot()).thenReturn(robot);

        when(uncons.getInformation(Arrays.asList("cards", "locked"))).thenReturn(thought2);
        when(thought2.getInformation()).thenReturn(Arrays.asList(Arrays.asList(move3_2)));

        when(uncons.getInformation(Arrays.asList("left", "gears"))).thenReturn(thought5);
        when(thought5.getInformation()).thenReturn(Arrays.asList(Arrays.asList()));

        when(uncons.getInformation(Arrays.asList("highest", "point"))).thenReturn(thought3);
        when(thought3.getInformation()).thenReturn(Arrays.asList(new Point(11, 25)));

        when(uncons.getInformation(Arrays.asList("right", "gears"))).thenReturn(thought6);
        when(thought6.getInformation()).thenReturn(Arrays.asList(Arrays.asList()));

        when(uncons.getInformation(Arrays.asList("HP"))).thenReturn(thought8);
        when(thought8.getInformation()).thenReturn(Arrays.asList(5));

        when(uncons.getInformation(Arrays.asList("all", "conveyor"))).thenReturn(thought7);
        when(thought7.getInformation()).thenReturn(Arrays.asList(new HashMap<Point, ScullyConveyorInfo>()));

        when(uncons.getInformation(Arrays.asList("flag", "next"))).thenReturn(thought9);
        when(thought9.getInformation()).thenReturn(Arrays.asList(new Point(3, 4)));

        when(uncons.getInformation(Arrays.asList("my", "position"))).thenReturn(thought10);
        when(thought10.getInformation()).thenReturn(Arrays.asList(new Point(1, 1)));

        when(uncons.getInformation(Arrays.asList("my", "ori"))).thenReturn(thought11);
        when(thought11.getInformation()).thenReturn(Arrays.asList(Orientation.NORTH));

        when(uncons.getInformation(Arrays.asList("holes"))).thenReturn(thought12);
        when(thought12.getInformation()).thenReturn(Arrays.asList(generateGameborder(12, 28)));

        when(uncons.getInformation(Arrays.asList("walls", "north"))).thenReturn(thought13);
        when(thought13.getInformation()).thenReturn(Arrays.asList(Arrays.asList(new Point(9, 9))));

        when(uncons.getInformation(Arrays.asList("walls", "east"))).thenReturn(thought14);
        when(thought14.getInformation()).thenReturn(Arrays.asList(Arrays.asList(new Point(9, 9))));

        when(uncons.getInformation(Arrays.asList("walls", "south"))).thenReturn(thought15);
        when(thought15.getInformation()).thenReturn(Arrays.asList(Arrays.asList(new Point(9, 9))));

        when(uncons.getInformation(Arrays.asList("walls", "west"))).thenReturn(thought16);
        when(thought16.getInformation()).thenReturn(Arrays.asList(Arrays.asList(new Point(9, 9))));

        when(move1_1.getRange()).thenReturn(1);
        when(move1_2.getRange()).thenReturn(1);
        when(move1_3.getRange()).thenReturn(1);
        when(move2_1.getRange()).thenReturn(2);
        when(move2_2.getRange()).thenReturn(2);
        when(move3_1.getRange()).thenReturn(3);
        when(move3_2.getRange()).thenReturn(3);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void startPlanningGivesFiveCardsWithMixedCards() {

        when(uncons.getInformation(Arrays.asList("cards", "player"))).thenReturn(thought1);
        when(thought1.getInformation())
                .thenReturn(Arrays.asList(right1, left1, move1_1, move2_1, move3_1, uTurn1, back1));

        List<ProgramCard> returns = planning.startPlanning(null);
        assertEquals(5, returns.size());
    }
    
    @Test
    public void startInterferePlanningGivesFiveCardsWithMixedCards() {

        when(uncons.getInformation(Arrays.asList("cards", "player"))).thenReturn(thought1);
        when(thought1.getInformation())
                .thenReturn(Arrays.asList(right1, left1, move1_1, move2_1, move3_1, uTurn1, back1));

        List<ProgramCard> returns = planning.startPlanning(new ScullyStrategyInterfere());
        assertEquals(5, returns.size());
    }

    @Test
    public void startPlanningGivesFiveCardsWithNoMove() {
        when(uncons.getInformation(Arrays.asList("cards", "player"))).thenReturn(thought1);
        when(thought1.getInformation()).thenReturn(Arrays.asList(right1, right2, right3, right4, back1));

        List<ProgramCard> returns = planning.startPlanning(null);
        assertEquals(5, returns.size());
    }
    
    @Test
    public void startInterferePlanningGivesFiveCardsWithNoMove() {
        when(uncons.getInformation(Arrays.asList("cards", "player"))).thenReturn(thought1);
        when(thought1.getInformation()).thenReturn(Arrays.asList(right1, right2, right3, right4, back1));

        List<ProgramCard> returns = planning.startPlanning(new ScullyStrategyInterfere());
        assertEquals(5, returns.size());
    }

    @Test
    public void startPlanningGivesFiveCardsWithNoMoveAndNoRight() {

        when(uncons.getInformation(Arrays.asList("cards", "player"))).thenReturn(thought1);
        when(thought1.getInformation()).thenReturn(Arrays.asList(left1, left2, left3, left4, back1));

        List<ProgramCard> returns = planning.startPlanning(null);
        assertEquals(5, returns.size());
    }

    @Test
    public void startPlanningGivesFiveCardsWithOnlyMove() {

        when(uncons.getInformation(Arrays.asList("cards", "player"))).thenReturn(thought1);
        when(thought1.getInformation()).thenReturn(Arrays.asList(move1_1, move1_2, move2_1, move3_1, move1_3, move3_2));

        List<ProgramCard> returns = planning.startPlanning(null);
        assertEquals(5, returns.size());

    }

    @Test
    public void startPlanningGivesFiveCardsWithCardsAwayFromGoal() {
        when(uncons.getInformation(Arrays.asList("flag", "next"))).thenReturn(thought9);
        when(thought9.getInformation()).thenReturn(Arrays.asList(new Point(6, 12)));

        when(uncons.getInformation(Arrays.asList("my", "position"))).thenReturn(thought10);
        when(thought10.getInformation()).thenReturn(Arrays.asList(new Point(6, 6)));

        when(uncons.getInformation(Arrays.asList("my", "ori"))).thenReturn(thought11);
        when(thought11.getInformation()).thenReturn(Arrays.asList(Orientation.SOUTH));

        when(uncons.getInformation(Arrays.asList("cards", "player"))).thenReturn(thought1);
        when(thought1.getInformation())
                .thenReturn(Arrays.asList(move1_1, move1_2, move2_1, move3_1, move1_3, move3_2, left1));

        List<ProgramCard> returns = planning.startPlanning(null);
        assertEquals(5, returns.size());

    }

    @Test
    public void startPlanningGivesFiveCardsWithHoleInWay() {
        when(uncons.getInformation(Arrays.asList("flag", "next"))).thenReturn(thought9);
        when(thought9.getInformation()).thenReturn(Arrays.asList(new Point(10, 16)));

        when(uncons.getInformation(Arrays.asList("my", "position"))).thenReturn(thought10);
        when(thought10.getInformation()).thenReturn(Arrays.asList(new Point(6, 6)));

        when(uncons.getInformation(Arrays.asList("my", "ori"))).thenReturn(thought11);
        when(thought11.getInformation()).thenReturn(Arrays.asList(Orientation.NORTH));

        when(uncons.getInformation(Arrays.asList("cards", "player"))).thenReturn(thought1);
        when(thought1.getInformation())
                .thenReturn(Arrays.asList(move1_1, move1_2, right1, move3_1, move1_3, move3_2, left1));

        when(uncons.getInformation(Arrays.asList("holes"))).thenReturn(thought12);
        List<Object> a = generateGameborder(12, 28);
        a.add(new Point(6, 10));
        when(thought12.getInformation()).thenReturn(Arrays.asList(a));

        List<ProgramCard> returns = planning.startPlanning(null);
        assertEquals(5, returns.size());

    }

    @Test
    public void checkWayTrue() {
        when(uncons.getInformation(Arrays.asList("flag", "next"))).thenReturn(thought9);
        when(thought9.getInformation()).thenReturn(Arrays.asList(new Point(6, 12)));

        when(uncons.getInformation(Arrays.asList("my", "position"))).thenReturn(thought10);
        when(thought10.getInformation()).thenReturn(Arrays.asList(new Point(6, 6)));

        when(uncons.getInformation(Arrays.asList("my", "ori"))).thenReturn(thought11);
        when(thought11.getInformation()).thenReturn(Arrays.asList(Orientation.SOUTH));

        when(uncons.getInformation(Arrays.asList("cards", "player"))).thenReturn(thought1);
        when(thought1.getInformation())
                .thenReturn(Arrays.asList(move1_1, move1_2, move2_1, move3_1, move1_3, move3_2, left1));
        List<Integer> weg = new ArrayList<>();
        weg.add(-3);
        weg.add(8);
        List<Integer> returns = new PathFinder(uncons).checkDistance(weg);
        assertEquals(new ArrayList<>(), returns);

    }

    @Test
    public void startPlanningNoWay() {
        List<Point> holes = new ArrayList<>();
        when(uncons.getInformation(Arrays.asList("cards", "player"))).thenReturn(thought1);
        when(thought1.getInformation()).thenReturn(Arrays.asList());

        holes.addAll(Arrays.asList(new Point(1, 0), new Point(0, 1), new Point(1, 2), new Point(2, 1)));
        when(thought12.getInformation()).thenReturn(Arrays.asList(holes));

        List<ProgramCard> results = planning.startPlanning(null);

        assertTrue(results.isEmpty());
    }
    
    @Test
    public void startInterferePlanningNoWay() {
        List<Point> holes = new ArrayList<>();
        when(uncons.getInformation(Arrays.asList("cards", "player"))).thenReturn(thought1);
        when(thought1.getInformation()).thenReturn(Arrays.asList());

        holes.addAll(Arrays.asList(new Point(1, 0), new Point(0, 1), new Point(1, 2), new Point(2, 1)));
        when(thought12.getInformation()).thenReturn(Arrays.asList(holes));

        List<ProgramCard> results = planning.startPlanning(new ScullyStrategyInterfere());

        assertTrue(results.isEmpty());
    }
    
    

    @Test
    public void startPlanningWhileusingAConveyor() {
        when(uncons.getInformation(Arrays.asList("cards", "locked"))).thenReturn(thought2);
        when(thought2.getInformation()).thenReturn(Arrays.asList(Arrays.asList()));

        when(uncons.getInformation(Arrays.asList("cards", "player"))).thenReturn(thought1);
        when(thought1.getInformation())
                .thenReturn(Arrays.asList(right1, left1, move1_1, move2_1, move3_1, uTurn1, back1));

        when(uncons.getInformation(Arrays.asList("walls", "west"))).thenReturn(thought16);
        when(thought16.getInformation()).thenReturn(Arrays.asList(Arrays.asList(new Point(3, 4), new Point(3, 1))));

        when(uncons.getInformation(Arrays.asList("conveyor"))).thenReturn(thought7);
        HashMap<Point, ScullyConveyorInfo> a = new HashMap<Point, ScullyConveyorInfo>();
        a.put(new Point(1, 3), new ScullyConveyorInfo(new Point(3, 3), 0));
        when(thought7.getInformation()).thenReturn(Arrays.asList(a));

        List<ProgramCard> results = planning.startPlanning(null);
        assertTrue(results.get(0) == move2_1 && results.get(1) == move1_1);
    }

    /**
     * Baut den Spielfeldrand
     * 
     * @param x,
     *            Höchste x Koordinate des Feldes
     * @param y,
     *            Höchste y Koordinate des Feldes
     * 
     * @return Spielfeldrand als Löcher
     */
    private List<Object> generateGameborder(int x, int y) {
        List<Object> border = new ArrayList<>();
        for (int c = 0; c < y; c++) {
            Point point = new Point(-1, c);
            border.add(point);
        }
        for (int c = 0; c < x; c++) {
            Point point = new Point(c, -1);
            border.add(point);
        }
        for (int c = 0; c < y; c++) {
            Point point = new Point(x, c);
            border.add(point);
        }
        for (int c = 0; c < x; c++) {
            Point point = new Point(c, y);
            border.add(point);
        }
        return border;
    }
}
