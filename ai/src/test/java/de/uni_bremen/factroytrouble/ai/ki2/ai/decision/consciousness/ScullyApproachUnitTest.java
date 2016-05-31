package de.uni_bremen.factroytrouble.ai.ki2.ai.decision.consciousness;

import static org.junit.Assert.assertEquals;
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
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import de.uni_bremen.factroytrouble.ai.ais.AIPlayer2;
import de.uni_bremen.factroytrouble.ai.ki2.ai.decision.unconsciousness.ScullyMoodUnit;
import de.uni_bremen.factroytrouble.ai.ki2.ai.decision.unconsciousness.ScullyPersonality;
import de.uni_bremen.factroytrouble.ai.ki2.api.memory.Thought;
import de.uni_bremen.factroytrouble.ai.ki2.api.memory.WorkingMemory;
import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.gameobjects.ConveyorBelt;
import de.uni_bremen.factroytrouble.gameobjects.Flag;
import de.uni_bremen.factroytrouble.gameobjects.GameWall;
import de.uni_bremen.factroytrouble.gameobjects.Hole;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.player.GameMoveBackwardCard;
import de.uni_bremen.factroytrouble.player.GameMoveForwardCard;
import de.uni_bremen.factroytrouble.player.GameTurnLeftCard;
import de.uni_bremen.factroytrouble.player.GameTurnRightCard;
import de.uni_bremen.factroytrouble.player.GameUturnCard;
import de.uni_bremen.factroytrouble.player.ProgramCard;

@RunWith(MockitoJUnitRunner.class)
public class ScullyApproachUnitTest {

    ScullyApproach approach;

    private Map<Point, Tile> mockTiles;
    private ArrayList<ArrayList<Point>> points;

    @Mock
    AIPlayer2 aiPlayer2;

    @Mock
    Hole hole1, hole2, hole3;

    @Mock
    private Robot robot1, robot2;

    @Mock
    ConveyorBelt belt1, belt2;

    @Mock
    GameTurnRightCard right1, right2, right3, right4;

    @Mock
    GameTurnLeftCard left1, left2, left3, left4;

    @Mock
    GameUturnCard uTurn1;

    @Mock
    GameMoveForwardCard move1_1, move1_2, move1_3, move2_1, move2_2, move3_1, move_back_1;

    @Mock
    GameMoveBackwardCard back1;

    @Mock
    GameWall wall1, wall2;

    @Mock
    Flag flag1;

    @Mock
    WorkingMemory work;

    @Mock
    Thought thought;

    @Mock
    AIPlayer2 ai;

    private ScullyMoodUnit mood;

    private ScullyPersonality pers;

    private ScullyConsciousnessUnit con;

    @Before
    public void setup() {
        when(move1_1.getRange()).thenReturn(1);
        when(move1_2.getRange()).thenReturn(1);
        when(move1_3.getRange()).thenReturn(1);
        when(move2_1.getRange()).thenReturn(2);
        when(move2_2.getRange()).thenReturn(2);
        when(move3_1.getRange()).thenReturn(3);
        when(move_back_1.getRange()).thenReturn(-1);

        when(work.getInformation(Arrays.asList("flag", "next"))).thenReturn(thought);
        when(thought.getInformation()).thenReturn(Arrays.asList(new Point(5, 4)));

        mockTiles = defaultTileMap();
        List<ProgramCard> cards = new ArrayList<>();
        when(aiPlayer2.getRobot()).thenReturn(robot1);

        cards.add(move1_3);
        cards.add(move1_1);

        cards.add(move1_2);
        cards.add(move2_1);
        cards.add(move_back_1);

        when(wall1.getOrientation()).thenReturn(Orientation.EAST);
        when(wall1.hasLaser()).thenReturn(1);
        when(mockTiles.get(points.get(6).get(3)).hasWall(Orientation.EAST)).thenReturn(true);
        when(mockTiles.get(points.get(6).get(3)).getWall(Orientation.EAST)).thenReturn(wall1);

        when(wall2.getOrientation()).thenReturn(Orientation.EAST);
        when(mockTiles.get(points.get(4).get(3)).hasWall(Orientation.EAST)).thenReturn(true);
        when(mockTiles.get(points.get(4).get(3)).getWall(Orientation.EAST)).thenReturn(wall2);

        when(mockTiles.get(points.get(5).get(2)).getRobot()).thenReturn(robot1);
        when(mockTiles.get(points.get(5).get(5)).getRobot()).thenReturn(robot2);
        when(robot1.getOrientation()).thenReturn(Orientation.NORTH);
        when(robot1.getCurrentTile()).thenReturn(mockTiles.get(points.get(5).get(2)));
        when(robot2.getCurrentTile()).thenReturn(mockTiles.get(points.get(5).get(5)));
        when(ai.getRobot()).thenReturn(robot1);
        when(ai.getRobot().getHP()).thenReturn(10);
        when(ai.getRobot().getLives()).thenReturn(3);

        pers = new ScullyPersonality("");
        mood = new ScullyMoodUnit(robot1, ai);
        con = new ScullyConsciousnessUnit(work, ai, mood, pers);
        when(ai.getPersonality()).thenReturn(pers);
        approach = new ScullyApproach(cards, ai, con);

    }

    @Test
    public void distanceInTiles() {
        assertEquals(3, approach.getDistanceToRobotInCards(robot2));
    }

    @Test
    public void distanceInTilesFail() {
        when(mockTiles.get(points.get(5).get(5)).getRobot()).thenReturn(null);
        when(mockTiles.get(points.get(5).get(2)).getRobot()).thenReturn(null);
        when(robot2.getCurrentTile()).thenReturn(mockTiles.get(points.get(2).get(5)));
        when(robot1.getCurrentTile()).thenReturn(mockTiles.get(points.get(3).get(2)));
        assertEquals(-1, approach.getDistanceToRobotInCards(robot2));
    }

    @Test
    public void getDistanceToRobot() {
        assertEquals(3, approach.getDistanceToRobotInTiles(robot2));
    }

    @Test
    public void getNumberOfBelts() {
        when(mockTiles.get(points.get(5).get(3)).getFieldObject()).thenReturn(belt1);
        when(mockTiles.get(points.get(5).get(4)).getFieldObject()).thenReturn(belt2);
        assertEquals(2, approach.getAmountOfConveyer());
    }

    @Test
    public void getAmmountOfHoles() {
        when(mockTiles.get(points.get(5).get(3)).getFieldObject()).thenReturn(hole1);
        when(mockTiles.get(points.get(5).get(4)).getFieldObject()).thenReturn(hole2);
        when(mockTiles.get(points.get(5).get(5)).getFieldObject()).thenReturn(hole3);
        assertEquals(3, approach.getAmountOfHoles());
    }

    /**
     * An einem vorbeifahren
     */
    @Test
    public void surroundingRobots() {
        when(robot2.getOrientation()).thenReturn(Orientation.NORTH);
        setRobotTo(robot1, 5, 5);
        setRobotTo(robot2, 4, 4);
        assertEquals(1, approach.getAmountOfNearEnemies());
    }

    @Test
    public void countLaser() {
        when(robot1.getOrientation()).thenReturn(Orientation.NORTH);
        setRobotTo(robot1, 5, 4);
        assertEquals(1, approach.getAmountOfLasers());
    }

    @Test
    public void damage() {
        when(robot2.getOrientation()).thenReturn(Orientation.EAST);
        when(robot1.getOrientation()).thenReturn(Orientation.NORTH);
        setRobotTo(robot1, 5, 5);
        setRobotTo(robot2, 4, 7);
        assertEquals(9000, approach.getExpectedDamage());
    }

    @Test
    public void canReachFlag() {
        setRobotTo(robot1, 5, 5);
        when(mockTiles.get(points.get(5).get(3)).getFieldObject()).thenReturn(flag1);
        assertEquals(true, approach.canReachNextFlag());
    }

    private void setRobotTo(Robot robot, int x, int y) {
        if (robot == robot1) {
            when(mockTiles.get(points.get(5).get(2)).getRobot()).thenReturn(null);
            when(robot2.getCurrentTile()).thenReturn(mockTiles.get(points.get(x).get(y)));
            when(mockTiles.get(points.get(x).get(y)).getRobot()).thenReturn(robot1);
        }
        if (robot == robot2) {
            when(mockTiles.get(points.get(5).get(5)).getRobot()).thenReturn(null);
            when(robot2.getCurrentTile()).thenReturn(mockTiles.get(points.get(x).get(y)));
            when(mockTiles.get(points.get(x).get(y)).getRobot()).thenReturn(robot2);
        }
    }

    private Map<Point, Tile> defaultTileMap() {
        Map<Point, Tile> m = new HashMap<>();
        points = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            ArrayList<Point> tmpList = new ArrayList<>();
            for (int j = 0; j < 12; j++) {
                Point tmpPoint = new Point(i, j);
                Tile tile = Mockito.mock(Tile.class);
                when(tile.getCoordinates()).thenReturn(tmpPoint);
                when(tile.getAbsoluteCoordinates()).thenReturn(tmpPoint);
                when(tile.getRobot()).thenReturn(null);
                tmpList.add(tmpPoint);
                m.put(tmpPoint, tile);
            }
            points.add(tmpList);
        }
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                Tile tmpTile = m.get(points.get(i).get(j));
                HashMap<Orientation, Tile> tmp = new HashMap<>();
                if (i < 11) {
                    tmp.put(Orientation.values()[0], m.get(points.get(i + 1).get(j)));
                }
                if (j < 11) {
                    tmp.put(Orientation.values()[1], m.get(points.get(i).get(j + 1)));
                }
                if (i > 0) {
                    tmp.put(Orientation.values()[2], m.get(points.get(i - 1).get(j)));
                }
                if (j > 0) {
                    tmp.put(Orientation.values()[3], m.get(points.get(i).get(j - 1)));
                }
                when(tmpTile.getNeighbors()).thenReturn(tmp);
            }
        }
        return m;
    }
}