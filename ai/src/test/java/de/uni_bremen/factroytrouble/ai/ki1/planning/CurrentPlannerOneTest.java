package de.uni_bremen.factroytrouble.ai.ki1.planning;

import static org.junit.Assert.*;

import java.awt.Point;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Map;

import de.uni_bremen.factroytrouble.api.ki1.Logic;
import de.uni_bremen.factroytrouble.player.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;

import de.uni_bremen.factroytrouble.api.ki1.Control;
import de.uni_bremen.factroytrouble.api.ki1.Control.RequestType;
import de.uni_bremen.factroytrouble.api.ki1.planning.FuturePlanning;
import de.uni_bremen.factroytrouble.api.ki1.planning.Goals;
import de.uni_bremen.factroytrouble.ai.ais.AIPlayer1;
import de.uni_bremen.factroytrouble.ai.ki1.LogicOne;
import de.uni_bremen.factroytrouble.ai.ki1.configreader.AgentConfigReader;
import de.uni_bremen.factroytrouble.ai.ki1.configreader.StaticBehaviourConfigReader;
import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.exceptions.KeyNotFoundException;
import de.uni_bremen.factroytrouble.gameobjects.GameRobot;
import de.uni_bremen.factroytrouble.gameobjects.Hole;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;

// FIXME: refactoring, sodass Definitionen entweder in init oder direkt in Testmethode sind
@Ignore("Original Bretter entfernt. Dadurch kommt es zu Problemen")
public class CurrentPlannerOneTest {
    // modules
    private final LogicOne logicOne = new LogicOne();
    private final Control controller = Mockito.mock(AIPlayer1.class);
    private final FuturePlanning futurePlanner = Mockito.mock(FuturePlannerOne.class);
    private final PathPlanner pathPlanner = Mockito.mock(PathPlanner.class);
    private CurrentPlannerOne cpo ;
    // own stuff
    private final Tile currentTile = Mockito.mock(Tile.class);
    // plan with stuff
    private final Tile targetTile = Mockito.mock(Tile.class);
    private final Goal goal = new GoalTile(100, targetTile);
    private final Queue<Goals> q = new PriorityQueue<Goals>(Arrays.asList(goal));
    private final Plan plan = new Plan((PriorityQueue<Goals>) q);
    // tiles
    private final Tile[][] tiles = new Tile[12][12];
    private final AgentConfigReader config = Mockito.mock(AgentConfigReader.class);
    private final StaticBehaviourConfigReader bConfig = Mockito.mock(StaticBehaviourConfigReader.class);
    
    private final MasterFactory mF = Mockito.mock(MasterFactory.class);
    private final Master m = Mockito.mock(Master.class);
    

    private void setTiles() {
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                Tile tile = Mockito.mock(Tile.class);
                Mockito.when(tile.getAbsoluteCoordinates()).thenReturn(new Point(i, j));
                tiles[i][j] = tile;
                Mockito.when(pathPlanner.getTile(new Point(i, j))).thenReturn(tiles[i][j]);
                Mockito.when(tiles[i][j].getFieldObject()).thenReturn(null);
                Mockito.when(controller.requestData(null, new Point(i, j))).thenReturn(tiles[i][j]);
            }
        }
    }

    @Before
    public void init() throws KeyNotFoundException, IOException {
        
        setTiles();
        Mockito.when(controller.requestData(RequestType.CURRENTTILE, null)).thenReturn(currentTile);
        Mockito.when(futurePlanner.getCurrentPlan()).thenReturn(plan);
        Mockito.when(controller.getAgentNumber()).thenReturn(1);
        Mockito.when(controller.getRobotName()).thenReturn("spinbot");
        Mockito.when(config.getIntProperty("Planning.ReachableDist")).thenReturn(8);
        Mockito.when(config.getDoubleProperty("Planning.DirectionPrio")).thenReturn(0.75);
        Mockito.when(config.getIntProperty("CPO.DefaultTileCost")).thenReturn(10);
        Mockito.when(bConfig.getIntProperty("Planning.GearCost")).thenReturn(10);
        Mockito.when(bConfig.getIntProperty("Planning.LaserCostFactor")).thenReturn(15);
        Mockito.when(bConfig.getDoubleProperty("Planning.Risk")).thenReturn(0.5);
        
        Mockito.when(((AIPlayer1) controller).getRobot()).thenReturn(Mockito.mock(GameRobot.class));
        Mockito.when(((AIPlayer1) controller).getRobotName()).thenReturn("WHIRLBOT");
        
        Mockito.when(((AIPlayer1) controller).getMasterFactory()).thenReturn(mF);
        Mockito.when(((AIPlayer1) controller).getGameId()).thenReturn(0);
        Mockito.when(mF.getMaster(anyInt())).thenReturn(m);
        Mockito.when(m.requestPowerDownStatusChange(any())).thenReturn(true);
        cpo= new CurrentPlannerOne(controller, null, futurePlanner, logicOne, pathPlanner);
        cpo.setController(controller);
        cpo.setConfigReader(config);
        cpo.setBConfigReader(bConfig);
    }
    

    @Test
    public void calcDistTest1() {
        Mockito.when(currentTile.getAbsoluteCoordinates()).thenReturn(new Point(4, 5));
        Mockito.when(targetTile.getAbsoluteCoordinates()).thenReturn(new Point(4, 7));

        assertEquals(3, cpo.calcDist(currentTile, targetTile, 2));
    }

    @Test
    public void calcDistTest2() {
        Mockito.when(currentTile.getAbsoluteCoordinates()).thenReturn(new Point(5, 5));
        Mockito.when(targetTile.getAbsoluteCoordinates()).thenReturn(new Point(4, 7));

        assertEquals(5, cpo.calcDist(currentTile, targetTile, 2));
    }

    @Test
    public void calcDistTest3() {
        Mockito.when(currentTile.getAbsoluteCoordinates()).thenReturn(new Point(2, 10));
        Mockito.when(targetTile.getAbsoluteCoordinates()).thenReturn(new Point(4, 7));

        assertEquals(7, cpo.calcDist(currentTile, targetTile, 1));
    }

    @Test
    public void calcDistTest4() {
        Mockito.when(currentTile.getAbsoluteCoordinates()).thenReturn(new Point(2, 10));
        Mockito.when(targetTile.getAbsoluteCoordinates()).thenReturn(new Point(0, 8));

        assertEquals(5, cpo.calcDist(currentTile, targetTile, 3));
    }

    @Test
    public void calcDistTest5() {
        Mockito.when(currentTile.getAbsoluteCoordinates()).thenReturn(new Point(3, 3));
        Mockito.when(targetTile.getAbsoluteCoordinates()).thenReturn(new Point(5, 6));

        assertEquals(7, cpo.calcDist(currentTile, targetTile, 0));
    }

    @Test
    public void planTurnShouldhandleSpecialCase() {
        ProgramCard card1 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card2 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card3 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card4 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card5 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card6 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card7 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card8 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card9 = Mockito.mock(MoveForwardCard.class);// 1
        Mockito.when(card1.getRange()).thenReturn(1);
        Mockito.when(card2.getRange()).thenReturn(1);
        Mockito.when(card3.getRange()).thenReturn(1);
        Mockito.when(card4.getRange()).thenReturn(1);
        Mockito.when(card5.getRange()).thenReturn(1);
        Mockito.when(card6.getRange()).thenReturn(1);
        Mockito.when(card7.getRange()).thenReturn(1);
        Mockito.when(card8.getRange()).thenReturn(1);
        Mockito.when(card9.getRange()).thenReturn(1);

        Mockito.when(controller.requestData(RequestType.HP, null)).thenReturn(10);
        Mockito.when(currentTile.getAbsoluteCoordinates()).thenReturn(new Point(2, 2));
        Mockito.when(targetTile.getAbsoluteCoordinates()).thenReturn(new Point(3, 5));
        Mockito.when(controller.requestData(null, 1)).thenReturn(card1);
        Mockito.when(controller.requestData(null, 2)).thenReturn(card2);
        Mockito.when(controller.requestData(null, 3)).thenReturn(card3);
        Mockito.when(controller.requestData(null, 4)).thenReturn(card4);
        Mockito.when(controller.requestData(null, 5)).thenReturn(card5);
        Mockito.when(controller.requestData(null, 6)).thenReturn(card6);
        Mockito.when(controller.requestData(null, 7)).thenReturn(card7);
        Mockito.when(controller.requestData(null, 8)).thenReturn(card8);
        Mockito.when(controller.requestData(null, 9)).thenReturn(card9);
        List<ProgramCard> cardsPlaced = cpo.planTurn();

        assertEquals(5, cardsPlaced.size());
        assertEquals(card1, cardsPlaced.get(0));
        assertEquals(card2, cardsPlaced.get(1));
        assertEquals(card3, cardsPlaced.get(2));
    }

    @Test
    public void planTurnShouldhandleSpecialCaseWithDamage() {
        ProgramCard card1 = Mockito.mock(TurnLeftCard.class);
        ProgramCard card2 = Mockito.mock(TurnLeftCard.class);
        ProgramCard card3 = Mockito.mock(TurnLeftCard.class);

        Mockito.when(controller.requestData(RequestType.HP, null)).thenReturn(4);
        Mockito.when(currentTile.getAbsoluteCoordinates()).thenReturn(new Point(2, 2));
        Mockito.when(targetTile.getAbsoluteCoordinates()).thenReturn(new Point(3, 5));
        Mockito.when(controller.requestData(null, 1)).thenReturn(card1);
        Mockito.when(controller.requestData(null, 2)).thenReturn(card2);
        Mockito.when(controller.requestData(null, 3)).thenReturn(card3);
        List<ProgramCard> cardsPlaced = cpo.planTurn();

        assertEquals(3, cardsPlaced.size());
        assertEquals(card1, cardsPlaced.get(0));
        assertEquals(card3, cardsPlaced.get(2));

    }

    @Test
    public void planTurnSucceed() {
        ProgramCard card1 = Mockito.mock(TurnLeftCard.class);
        ProgramCard card2 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card3 = Mockito.mock(TurnRightCard.class);
        ProgramCard card4 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card5 = Mockito.mock(MoveForwardCard.class);// 2
        ProgramCard card6 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card7 = Mockito.mock(TurnLeftCard.class);
        ProgramCard card8 = Mockito.mock(MoveForwardCard.class);// 2
        ProgramCard card9 = Mockito.mock(TurnRightCard.class);
        Mockito.when(card2.getRange()).thenReturn(1);
        Mockito.when(card4.getRange()).thenReturn(1);
        Mockito.when(card5.getRange()).thenReturn(2);
        Mockito.when(card6.getRange()).thenReturn(1);
        Mockito.when(card8.getRange()).thenReturn(2);
        Orientation ori = Orientation.NORTH;
        List<Tile> path = new LinkedList<Tile>(
                Arrays.asList(tiles[3][2], tiles[3][3], tiles[3][4], tiles[3][5], tiles[3][6]));

        Mockito.when(controller.requestData(RequestType.HP, null)).thenReturn(10);
        Mockito.when(currentTile.getAbsoluteCoordinates()).thenReturn(new Point(2, 2));
        Mockito.when(targetTile.getAbsoluteCoordinates()).thenReturn(new Point(3, 6));
        Mockito.when(controller.requestData(null, 1)).thenReturn(card1);
        Mockito.when(controller.requestData(null, 2)).thenReturn(card2);
        Mockito.when(controller.requestData(null, 3)).thenReturn(card3);
        Mockito.when(controller.requestData(null, 4)).thenReturn(card4);
        Mockito.when(controller.requestData(null, 5)).thenReturn(card5);
        Mockito.when(controller.requestData(null, 6)).thenReturn(card6);
        Mockito.when(controller.requestData(null, 7)).thenReturn(card7);
        Mockito.when(controller.requestData(null, 8)).thenReturn(card8);
        Mockito.when(controller.requestData(null, 9)).thenReturn(card9);
        Mockito.when(controller.requestData(RequestType.ORIENTATION, null)).thenReturn(ori);
        Mockito.when(pathPlanner.getPath(currentTile, targetTile)).thenReturn(path);
        List<ProgramCard> cardsPlaced = cpo.planTurn();

        assertEquals(5, cardsPlaced.size());
        assertEquals(card3, cardsPlaced.get(0));
        assertEquals(card2, cardsPlaced.get(1));
        assertEquals(card1, cardsPlaced.get(2));
        assertEquals(card5, cardsPlaced.get(3));
        assertEquals(card8, cardsPlaced.get(4));

    }

    @Test
    public void planTurnSucceedWithDamage() {
        ProgramCard card1 = Mockito.mock(TurnLeftCard.class);
        ProgramCard card2 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card3 = Mockito.mock(MoveForwardCard.class);// 2
        Mockito.when(card2.getRange()).thenReturn(1);
        Mockito.when(card3.getRange()).thenReturn(2);
        Orientation ori = Orientation.SOUTH;
        List<Tile> path = new LinkedList<Tile>(Arrays.asList(tiles[2][1], tiles[3][1], tiles[4][1]));

        Mockito.when(controller.requestData(RequestType.HP, null)).thenReturn(4);
        Mockito.when(currentTile.getAbsoluteCoordinates()).thenReturn(new Point(2, 2));
        Mockito.when(targetTile.getAbsoluteCoordinates()).thenReturn(new Point(4, 1));
        Mockito.when(controller.requestData(null, 1)).thenReturn(card1);
        Mockito.when(controller.requestData(null, 2)).thenReturn(card2);
        Mockito.when(controller.requestData(null, 3)).thenReturn(card3);
        Mockito.when(controller.requestData(RequestType.ORIENTATION, null)).thenReturn(ori);
        Mockito.when(pathPlanner.getPath(currentTile, targetTile)).thenReturn(path);
        List<ProgramCard> cardsPlaced = cpo.planTurn();

        assertEquals(3, cardsPlaced.size());
        assertEquals(card2, cardsPlaced.get(0));
        assertEquals(card1, cardsPlaced.get(1));
        assertEquals(card3, cardsPlaced.get(2));

    }

    @Test
    public void planTurnSucceed2() {
        ProgramCard card1 = Mockito.mock(TurnLeftCard.class);
        ProgramCard card2 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card3 = Mockito.mock(TurnRightCard.class);
        ProgramCard card4 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card5 = Mockito.mock(MoveForwardCard.class);// 2
        ProgramCard card6 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card7 = Mockito.mock(TurnLeftCard.class);
        ProgramCard card8 = Mockito.mock(MoveForwardCard.class);// 2
        ProgramCard card9 = Mockito.mock(TurnRightCard.class);
        Mockito.when(card2.getRange()).thenReturn(1);
        Mockito.when(card4.getRange()).thenReturn(1);
        Mockito.when(card5.getRange()).thenReturn(2);
        Mockito.when(card6.getRange()).thenReturn(1);
        Mockito.when(card8.getRange()).thenReturn(2);
        Orientation ori = Orientation.NORTH;
        List<Tile> path = new LinkedList<Tile>(Arrays.asList(tiles[1][2]));

        Mockito.when(controller.requestData(RequestType.HP, null)).thenReturn(10);
        Mockito.when(currentTile.getAbsoluteCoordinates()).thenReturn(new Point(2, 2));
        Mockito.when(targetTile.getAbsoluteCoordinates()).thenReturn(new Point(1, 2));
        Mockito.when(controller.requestData(null, 1)).thenReturn(card1);
        Mockito.when(controller.requestData(null, 2)).thenReturn(card2);
        Mockito.when(controller.requestData(null, 3)).thenReturn(card3);
        Mockito.when(controller.requestData(null, 4)).thenReturn(card4);
        Mockito.when(controller.requestData(null, 5)).thenReturn(card5);
        Mockito.when(controller.requestData(null, 6)).thenReturn(card6);
        Mockito.when(controller.requestData(null, 7)).thenReturn(card7);
        Mockito.when(controller.requestData(null, 8)).thenReturn(card8);
        Mockito.when(controller.requestData(null, 9)).thenReturn(card9);
        Mockito.when(controller.requestData(RequestType.ORIENTATION, null)).thenReturn(ori);
        Mockito.when(pathPlanner.getPath(currentTile, targetTile)).thenReturn(path);
        List<ProgramCard> cardsPlaced = cpo.planTurn();

        assertEquals(card1, cardsPlaced.get(0));
        assertEquals(card2, cardsPlaced.get(1));

    }

    @Test
    public void planTurnSecondPathDirClipCardsTest() {
        ProgramCard card1 = Mockito.mock(TurnLeftCard.class);
        ProgramCard card2 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card3 = Mockito.mock(TurnRightCard.class);
        ProgramCard card4 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card5 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card6 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card7 = Mockito.mock(UturnCard.class);
        ProgramCard card8 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card9 = Mockito.mock(MoveBackwardCard.class);
        Mockito.when(card2.getRange()).thenReturn(1);
        Mockito.when(card4.getRange()).thenReturn(1);
        Mockito.when(card5.getRange()).thenReturn(1);
        Mockito.when(card6.getRange()).thenReturn(1);
        Mockito.when(card8.getRange()).thenReturn(1);
        Orientation ori = Orientation.SOUTH;
        List<Tile> path = new LinkedList<Tile>(
                Arrays.asList(tiles[2][3], tiles[2][4], tiles[2][5], tiles[1][5], tiles[1][6]));
        // Path2
        List<Point> blockedPoints = new LinkedList<Point>(Arrays.asList(new Point(2, 3)));
        List<Tile> path2 = new LinkedList<Tile>(
                Arrays.asList(tiles[1][2], tiles[1][3], tiles[1][4], tiles[1][5], tiles[1][6]));

        Mockito.when(controller.requestData(RequestType.HP, null)).thenReturn(10);
        Mockito.when(currentTile.getAbsoluteCoordinates()).thenReturn(new Point(2, 2));
        Mockito.when(targetTile.getAbsoluteCoordinates()).thenReturn(new Point(1, 6));
        Mockito.when(futurePlanner.getCurrentPlan()).thenReturn(plan);
        Mockito.when(controller.requestData(null, 1)).thenReturn(card1);
        Mockito.when(controller.requestData(null, 2)).thenReturn(card2);
        Mockito.when(controller.requestData(null, 3)).thenReturn(card3);
        Mockito.when(controller.requestData(null, 4)).thenReturn(card4);
        Mockito.when(controller.requestData(null, 5)).thenReturn(card5);
        Mockito.when(controller.requestData(null, 6)).thenReturn(card6);
        Mockito.when(controller.requestData(null, 7)).thenReturn(card7);
        Mockito.when(controller.requestData(null, 8)).thenReturn(card8);
        Mockito.when(controller.requestData(null, 9)).thenReturn(card9);
        Mockito.when(controller.requestData(RequestType.ORIENTATION, null)).thenReturn(ori);
        Mockito.when(pathPlanner.getPath(currentTile, targetTile)).thenReturn(path);
        Mockito.when(pathPlanner.getAlternativePath(currentTile, targetTile, blockedPoints)).thenReturn(path2);
        List<ProgramCard> cardsPlaced = cpo.planTurn();

        assertEquals(5, cardsPlaced.size());
        assertEquals(card7, cardsPlaced.get(0));
        assertEquals(card2, cardsPlaced.get(1));
        assertEquals(card4, cardsPlaced.get(2));
        assertEquals(card5, cardsPlaced.get(3));
        assertEquals(card1, cardsPlaced.get(4));

    }

    @Test
    public void planTurnSecondPathDirClipCardsTestWithDamage() {
        ProgramCard card1 = Mockito.mock(TurnLeftCard.class);
        ProgramCard card2 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card3 = Mockito.mock(TurnRightCard.class);
        ProgramCard card4 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card5 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card6 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card7 = Mockito.mock(UturnCard.class);
        Mockito.when(card2.getRange()).thenReturn(1);
        Mockito.when(card4.getRange()).thenReturn(1);
        Mockito.when(card5.getRange()).thenReturn(1);
        Mockito.when(card6.getRange()).thenReturn(1);
        Orientation ori = Orientation.SOUTH;
        List<Tile> path = new LinkedList<Tile>(
                Arrays.asList(tiles[2][3], tiles[2][4], tiles[2][5], tiles[1][5], tiles[1][6]));
        // Path2
        List<Point> blockedPoints = new LinkedList<Point>(Arrays.asList(new Point(2, 3)));
        List<Tile> path2 = new LinkedList<Tile>(
                Arrays.asList(tiles[1][2], tiles[1][3], tiles[1][4], tiles[1][5], tiles[1][6]));

        Mockito.when(controller.requestData(RequestType.HP, null)).thenReturn(8);
        Mockito.when(currentTile.getAbsoluteCoordinates()).thenReturn(new Point(2, 2));
        Mockito.when(targetTile.getAbsoluteCoordinates()).thenReturn(new Point(1, 6));
        Mockito.when(futurePlanner.getCurrentPlan()).thenReturn(plan);
        Mockito.when(controller.requestData(null, 1)).thenReturn(card1);
        Mockito.when(controller.requestData(null, 2)).thenReturn(card2);
        Mockito.when(controller.requestData(null, 3)).thenReturn(card3);
        Mockito.when(controller.requestData(null, 4)).thenReturn(card4);
        Mockito.when(controller.requestData(null, 5)).thenReturn(card5);
        Mockito.when(controller.requestData(null, 6)).thenReturn(card6);
        Mockito.when(controller.requestData(null, 7)).thenReturn(card7);
        Mockito.when(controller.requestData(RequestType.ORIENTATION, null)).thenReturn(ori);
        Mockito.when(pathPlanner.getPath(currentTile, targetTile)).thenReturn(path);
        Mockito.when(pathPlanner.getAlternativePath(currentTile, targetTile, blockedPoints)).thenReturn(path2);
        List<ProgramCard> cardsPlaced = cpo.planTurn();

        assertEquals(5, cardsPlaced.size());
        assertEquals(card7, cardsPlaced.get(0));
        assertEquals(card2, cardsPlaced.get(1));
        assertEquals(card4, cardsPlaced.get(2));
        assertEquals(card5, cardsPlaced.get(3));
        assertEquals(card1, cardsPlaced.get(4));

    }

    @Test
    public void planTurnSecondPathDirTooFewCardsTest2() {
        ProgramCard card1 = Mockito.mock(TurnLeftCard.class);
        ProgramCard card2 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card3 = Mockito.mock(TurnRightCard.class);
        ProgramCard card4 = Mockito.mock(MoveForwardCard.class);// 3
        ProgramCard card5 = Mockito.mock(MoveForwardCard.class);// 2
        ProgramCard card6 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card7 = Mockito.mock(MoveForwardCard.class);// 2
        ProgramCard card8 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card9 = Mockito.mock(MoveBackwardCard.class);
        Mockito.when(card2.getRange()).thenReturn(1);
        Mockito.when(card4.getRange()).thenReturn(3);
        Mockito.when(card5.getRange()).thenReturn(2);
        Mockito.when(card6.getRange()).thenReturn(1);
        Mockito.when(card7.getRange()).thenReturn(2);
        Mockito.when(card8.getRange()).thenReturn(1);
        Orientation ori = Orientation.SOUTH;
        List<Tile> path = new LinkedList<Tile>(
                Arrays.asList(tiles[2][3], tiles[2][4], tiles[2][5], tiles[1][5], tiles[1][6]));
        // Path2
        List<Point> blockedPoints = new LinkedList<Point>(Arrays.asList(new Point(2, 3)));
        List<Tile> path2 = new LinkedList<Tile>(
                Arrays.asList(tiles[1][2], tiles[1][3], tiles[1][4], tiles[1][5], tiles[1][6]));

        Mockito.when(futurePlanner.getCurrentPlan()).thenReturn(plan);
        Mockito.when(controller.requestData(RequestType.HP, null)).thenReturn(10);
        Mockito.when(currentTile.getAbsoluteCoordinates()).thenReturn(new Point(2, 2));
        Mockito.when(targetTile.getAbsoluteCoordinates()).thenReturn(new Point(1, 6));
        Mockito.when(controller.requestData(null, 1)).thenReturn(card1);
        Mockito.when(controller.requestData(null, 2)).thenReturn(card2);
        Mockito.when(controller.requestData(null, 3)).thenReturn(card3);
        Mockito.when(controller.requestData(null, 4)).thenReturn(card4);
        Mockito.when(controller.requestData(null, 5)).thenReturn(card5);
        Mockito.when(controller.requestData(null, 6)).thenReturn(card6);
        Mockito.when(controller.requestData(null, 7)).thenReturn(card7);
        Mockito.when(controller.requestData(null, 8)).thenReturn(card8);
        Mockito.when(controller.requestData(null, 9)).thenReturn(card9);
        Mockito.when(controller.requestData(RequestType.ORIENTATION, null)).thenReturn(ori);
        Mockito.when(pathPlanner.getPath(currentTile, targetTile)).thenReturn(path);
        Mockito.when(pathPlanner.getAlternativePath(currentTile, targetTile, blockedPoints)).thenReturn(path2);
        List<ProgramCard> cardsPlaced = cpo.planTurn();

        assertEquals(card3, cardsPlaced.get(0));
        assertEquals(card2, cardsPlaced.get(1));

    }

    @Test
    public void LeastBadMovesOverBoardTest() {
        ProgramCard card1 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card2 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card9 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card4 = Mockito.mock(MoveForwardCard.class);// 3
        ProgramCard card5 = Mockito.mock(MoveForwardCard.class);// 2
        ProgramCard card6 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card7 = Mockito.mock(MoveForwardCard.class);// 2
        ProgramCard card8 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card3 = Mockito.mock(MoveBackwardCard.class);
        Mockito.when(card1.getRange()).thenReturn(1);
        Mockito.when(card2.getRange()).thenReturn(1);
        Mockito.when(card3.getRange()).thenReturn(1);
        Mockito.when(card4.getRange()).thenReturn(3);
        Mockito.when(card5.getRange()).thenReturn(2);
        Mockito.when(card6.getRange()).thenReturn(1);
        Mockito.when(card7.getRange()).thenReturn(2);
        Mockito.when(card8.getRange()).thenReturn(1);
        Orientation ori = Orientation.SOUTH;
        List<Tile> path = new LinkedList<Tile>(
                Arrays.asList(tiles[2][5], tiles[2][6], tiles[1][6]));
        // Path2
        List<Point> blockedPoints = new LinkedList<Point>(Arrays.asList(new Point(2, 3)));
        List<Tile> path2 = new LinkedList<Tile>(
                Arrays.asList(tiles[1][4], tiles[1][5], tiles[1][6]));

        Mockito.when(futurePlanner.getCurrentPlan()).thenReturn(plan);
        Mockito.when(controller.requestData(RequestType.HP, null)).thenReturn(10);
        Mockito.when(currentTile.getAbsoluteCoordinates()).thenReturn(new Point(2, 4));
        Mockito.when(targetTile.getAbsoluteCoordinates()).thenReturn(new Point(1, 6));
        Mockito.when(controller.requestData(null, 1)).thenReturn(card1);
        Mockito.when(controller.requestData(null, 2)).thenReturn(card2);
        Mockito.when(controller.requestData(null, 3)).thenReturn(card3);
        Mockito.when(controller.requestData(null, 4)).thenReturn(card4);
        Mockito.when(controller.requestData(null, 5)).thenReturn(card5);
        Mockito.when(controller.requestData(null, 6)).thenReturn(card6);
        Mockito.when(controller.requestData(null, 7)).thenReturn(card7);
        Mockito.when(controller.requestData(null, 8)).thenReturn(card8);
        Mockito.when(controller.requestData(null, 9)).thenReturn(card9);
        Mockito.when(controller.requestData(RequestType.ORIENTATION, null)).thenReturn(ori);
        Mockito.when(pathPlanner.getPath(currentTile, targetTile)).thenReturn(path);
        Mockito.when(pathPlanner.getAlternativePath(currentTile, targetTile, blockedPoints)).thenReturn(path2);

        List<ProgramCard> cardsPlaced = cpo.planTurn();
        assertEquals(5, cardsPlaced.size());
        assertTrue(cardsPlaced.contains(card3));

    }

    @Test
    public void isPlaceableMove2Variant2True() {
        ProgramCard card1 = Mockito.mock(TurnLeftCard.class);
        ProgramCard card2 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card3 = Mockito.mock(TurnRightCard.class);
        ProgramCard card4 = Mockito.mock(MoveForwardCard.class);// 3
        ProgramCard card5 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card6 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card7 = Mockito.mock(TurnLeftCard.class);
        ProgramCard card8 = Mockito.mock(MoveBackwardCard.class);
        ProgramCard card9 = Mockito.mock(TurnLeftCard.class);
        Mockito.when(card2.getRange()).thenReturn(1);
        Mockito.when(card4.getRange()).thenReturn(3);
        Mockito.when(card5.getRange()).thenReturn(1);
        Mockito.when(card6.getRange()).thenReturn(1);
        List<ProgramCard> allCards = new LinkedList<ProgramCard>(
                Arrays.asList(card1, card2, card3, card4, card5, card6, card7, card8, card9));
        List<ProgramCard> cards = new LinkedList<ProgramCard>(Arrays.asList(card2, card5));
        List<String> variant = new LinkedList<String>(Arrays.asList("One", "One"));
        cpo.setCards(allCards);

        assertEquals(cards, cpo.isPlaceable(variant));
    }

    @Test
    public void isPlaceableMoveLeftVariant2True() {
        ProgramCard card1 = Mockito.mock(TurnLeftCard.class);
        ProgramCard card2 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card3 = Mockito.mock(TurnRightCard.class);
        ProgramCard card4 = Mockito.mock(UturnCard.class);
        ProgramCard card5 = Mockito.mock(MoveForwardCard.class);// 1
        Mockito.when(card2.getRange()).thenReturn(1);
        Mockito.when(card5.getRange()).thenReturn(1);
        List<ProgramCard> allCards = new LinkedList<ProgramCard>(Arrays.asList(card1, card2, card3, card4, card5));
        List<ProgramCard> cards = new LinkedList<ProgramCard>(Arrays.asList(card3, card4));
        List<String> variant = new LinkedList<String>(Arrays.asList("Right", "UTurn"));
        cpo.setCards(allCards);

        assertEquals(cards, cpo.isPlaceable(variant));
    }

    @Test
    public void isPlaceableMove3False() {
        ProgramCard card1 = Mockito.mock(TurnLeftCard.class);
        ProgramCard card2 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card3 = Mockito.mock(TurnRightCard.class);
        ProgramCard card4 = Mockito.mock(UturnCard.class);
        ProgramCard card5 = Mockito.mock(MoveForwardCard.class);// 1
        Mockito.when(card2.getRange()).thenReturn(1);
        Mockito.when(card5.getRange()).thenReturn(1);
        List<ProgramCard> allCards = new LinkedList<ProgramCard>(Arrays.asList(card1, card2, card3, card4, card5));
        List<String> variant = new LinkedList<String>(Arrays.asList("Three"));
        cpo.setCards(allCards);

        assertEquals(new LinkedList<ProgramCard>(), cpo.isPlaceable(variant));
    }

    @Test
    public void isPlaceableMoveRightVariant3False() {
        ProgramCard card1 = Mockito.mock(TurnLeftCard.class);
        ProgramCard card2 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card3 = Mockito.mock(TurnLeftCard.class);
        ProgramCard card4 = Mockito.mock(UturnCard.class);
        ProgramCard card5 = Mockito.mock(MoveForwardCard.class);// 1
        Mockito.when(card2.getRange()).thenReturn(1);
        Mockito.when(card5.getRange()).thenReturn(1);
        List<ProgramCard> allCards = new LinkedList<ProgramCard>(Arrays.asList(card1, card2, card3, card4, card5));
        List<String> variant = new LinkedList<String>(Arrays.asList("Left", "Left", "Left"));
        cpo.setCards(allCards);

        assertEquals(new LinkedList<ProgramCard>(), cpo.isPlaceable(variant));
    }

    @Test
    public void doLeastBadMoveTest1() {
        ProgramCard card1 = Mockito.mock(TurnLeftCard.class);
        ProgramCard card2 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card3 = Mockito.mock(MoveForwardCard.class);// 1
        Mockito.when(card2.getRange()).thenReturn(1);
        Mockito.when(card3.getRange()).thenReturn(1);
        cpo.setSimulationOrientation(1);
        cpo.setSimulationTile(currentTile);
        List<ProgramCard> cards = new LinkedList<ProgramCard>(Arrays.asList(card1, card2, card3));
        cpo.setCards(cards);
        List<Integer> used = new LinkedList<Integer>(Arrays.asList(1));
        cpo.setGlobalUsedCards(used);
        cpo.setCurrentGoal(new GoalTile(20, targetTile));
        List<ProgramCard> cardsPlaced = new LinkedList<ProgramCard>();
        cardsPlaced.add(card3);
        Mockito.when(controller.requestData(RequestType.HP, null)).thenReturn(9);
        Mockito.when(currentTile.getAbsoluteCoordinates()).thenReturn(new Point(2, 2));
        Mockito.when(targetTile.getAbsoluteCoordinates()).thenReturn(new Point(3, 3));

        assertEquals(cardsPlaced, cpo.doLeastBadMoves(1));
    }

    @Test
    public void doLeastBadMoveTest2() {
        ProgramCard card1 = Mockito.mock(TurnLeftCard.class);
        ProgramCard card2 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card3 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card4 = Mockito.mock(TurnLeftCard.class);
        ProgramCard card5 = Mockito.mock(MoveForwardCard.class);// 2
        ProgramCard card6 = Mockito.mock(MoveForwardCard.class);// 3
        ProgramCard card7 = Mockito.mock(MoveForwardCard.class);// 1
        Mockito.when(card2.getRange()).thenReturn(1);
        Mockito.when(card3.getRange()).thenReturn(1);
        Mockito.when(card5.getRange()).thenReturn(2);
        Mockito.when(card6.getRange()).thenReturn(3);
        Mockito.when(card7.getRange()).thenReturn(1);
        cpo.setSimulationOrientation(3);
        cpo.setSimulationTile(currentTile);
        List<ProgramCard> cards = new LinkedList<ProgramCard>(
                Arrays.asList(card1, card2, card3, card4, card5, card6, card7));
        cpo.setCards(cards);
        List<Integer> used = new LinkedList<Integer>(Arrays.asList(0, 6));
        cpo.setGlobalUsedCards(used);
        cpo.setCurrentGoal(new GoalTile(20, targetTile));
        List<ProgramCard> cardsPlaced = new LinkedList<ProgramCard>(Arrays.asList(card5, card4, card2, card3));
        Mockito.when(controller.requestData(RequestType.HP, null)).thenReturn(9);
        Mockito.when(currentTile.getAbsoluteCoordinates()).thenReturn(new Point(5, 5));
        Mockito.when(targetTile.getAbsoluteCoordinates()).thenReturn(new Point(2, 3));

        assertEquals(cardsPlaced, cpo.doLeastBadMoves(4));
    }

    @Test
    public void doLeastBadMoveWithHoleAndGetToStartTile() {
        ProgramCard card1 = Mockito.mock(TurnLeftCard.class);
        ProgramCard card2 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card3 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card4 = Mockito.mock(TurnLeftCard.class);
        ProgramCard card5 = Mockito.mock(MoveForwardCard.class);// 2
        ProgramCard card6 = Mockito.mock(MoveForwardCard.class);// 3
        ProgramCard card7 = Mockito.mock(MoveForwardCard.class);// 1
        Mockito.when(card2.getRange()).thenReturn(1);
        Mockito.when(card3.getRange()).thenReturn(1);
        Mockito.when(card5.getRange()).thenReturn(2);
        Mockito.when(card6.getRange()).thenReturn(3);
        Mockito.when(card7.getRange()).thenReturn(1);
        cpo.setSimulationOrientation(3);
        cpo.setSimulationTile(currentTile);
        List<ProgramCard> cards = new LinkedList<ProgramCard>(
                Arrays.asList(card1, card2, card3, card4, card5, card6, card7));
        cpo.setCards(cards);
        List<Integer> used = new LinkedList<Integer>(Arrays.asList(6));
        cpo.setGlobalUsedCards(used);
        cpo.setCurrentGoal(new GoalTile(20, targetTile));
        List<ProgramCard> cardsPlaced = new LinkedList<ProgramCard>(Arrays.asList(card5, card1, card4, card2, card3));
        Mockito.when(controller.requestData(RequestType.HP, null)).thenReturn(9);
        Mockito.when(currentTile.getAbsoluteCoordinates()).thenReturn(new Point(5, 5));
        Mockito.when(targetTile.getAbsoluteCoordinates()).thenReturn(new Point(2, 3));
        Hole hole8_3 = Mockito.mock(Hole.class);
        Mockito.when(tiles[8][3].getFieldObject()).thenReturn(hole8_3);

        assertEquals(cardsPlaced, cpo.doLeastBadMoves(5));
    }

    @Test
    public void doLeastBadMoveAndRejectTurnBecauseOfHole() {
        ProgramCard card1 = Mockito.mock(TurnLeftCard.class);
        ProgramCard card2 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card3 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card4 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card5 = Mockito.mock(MoveForwardCard.class);// 2
        ProgramCard card6 = Mockito.mock(MoveForwardCard.class);// 3
        ProgramCard card7 = Mockito.mock(MoveForwardCard.class);// 1
        Mockito.when(card2.getRange()).thenReturn(1);
        Mockito.when(card3.getRange()).thenReturn(1);
        Mockito.when(card4.getRange()).thenReturn(1);
        Mockito.when(card5.getRange()).thenReturn(2);
        Mockito.when(card6.getRange()).thenReturn(3);
        Mockito.when(card7.getRange()).thenReturn(1);
        cpo.setSimulationOrientation(3);
        cpo.setSimulationTile(currentTile);
        List<ProgramCard> cards = new LinkedList<ProgramCard>(
                Arrays.asList(card1, card2, card3, card4, card5, card6, card7));
        cpo.setCards(cards);
        List<Integer> used = new LinkedList<Integer>(Arrays.asList());
        cpo.setGlobalUsedCards(used);
        cpo.setCurrentGoal(new GoalTile(20, targetTile));
        Mockito.when(controller.requestData(RequestType.HP, null)).thenReturn(9);
        Mockito.when(currentTile.getAbsoluteCoordinates()).thenReturn(new Point(5, 5));
        Mockito.when(targetTile.getAbsoluteCoordinates()).thenReturn(new Point(2, 3));
        Hole hole7_3 = Mockito.mock(Hole.class);
        Mockito.when(tiles[7][3].getFieldObject()).thenReturn(hole7_3);
        List<ProgramCard> cardsPlaced = cpo.doLeastBadMoves(4);

        assertEquals(card5, cardsPlaced.get(0));
        assertEquals(card2, cardsPlaced.get(1));

    }

    @Test
    public void doLeastBadMoveAndTurnBecauseOfTooDistantHole() {
        ProgramCard card1 = Mockito.mock(TurnLeftCard.class);
        ProgramCard card2 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card3 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card4 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card5 = Mockito.mock(MoveForwardCard.class);// 2
        ProgramCard card6 = Mockito.mock(MoveForwardCard.class);// 3
        ProgramCard card7 = Mockito.mock(MoveForwardCard.class);// 1
        Mockito.when(card2.getRange()).thenReturn(1);
        Mockito.when(card3.getRange()).thenReturn(1);
        Mockito.when(card4.getRange()).thenReturn(1);
        Mockito.when(card5.getRange()).thenReturn(2);
        Mockito.when(card6.getRange()).thenReturn(3);
        Mockito.when(card7.getRange()).thenReturn(1);
        cpo.setSimulationOrientation(3);
        cpo.setSimulationTile(currentTile);
        List<ProgramCard> cards = new LinkedList<ProgramCard>(
                Arrays.asList(card1, card2, card3, card4, card5, card6, card7));
        cpo.setCards(cards);
        List<Integer> used = new LinkedList<Integer>(Arrays.asList());
        cpo.setGlobalUsedCards(used);
        cpo.setCurrentGoal(new GoalTile(20, targetTile));
        List<ProgramCard> cardsPlaced = new LinkedList<ProgramCard>(Arrays.asList(card5, card1, card2, card3));
        Mockito.when(controller.requestData(RequestType.HP, null)).thenReturn(9);
        Mockito.when(currentTile.getAbsoluteCoordinates()).thenReturn(new Point(5, 5));
        Mockito.when(targetTile.getAbsoluteCoordinates()).thenReturn(new Point(2, 3));
        Hole hole8_3 = Mockito.mock(Hole.class);
        Mockito.when(tiles[8][3].getFieldObject()).thenReturn(hole8_3);

        assertEquals(cardsPlaced, cpo.doLeastBadMoves(4));
    }

    @Test
    public void minTilesToMoveTest() {
        ProgramCard card1 = Mockito.mock(MoveBackwardCard.class);
        ProgramCard card2 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card3 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card4 = Mockito.mock(TurnLeftCard.class);
        ProgramCard card5 = Mockito.mock(MoveForwardCard.class);// 2
        ProgramCard card6 = Mockito.mock(MoveForwardCard.class);// 3
        ProgramCard card7 = Mockito.mock(MoveForwardCard.class);// 1
        Mockito.when(card2.getRange()).thenReturn(1);
        Mockito.when(card3.getRange()).thenReturn(1);
        Mockito.when(card5.getRange()).thenReturn(2);
        Mockito.when(card6.getRange()).thenReturn(3);
        Mockito.when(card7.getRange()).thenReturn(1);
        List<ProgramCard> cards = new LinkedList<ProgramCard>(
                Arrays.asList(card1, card2, card3, card4, card5, card6, card7));
        cpo.setCards(cards);
        List<Integer> used = new LinkedList<Integer>(Arrays.asList(3));
        cpo.setGlobalUsedCards(used);

        assertEquals(3, cpo.minTilesToMove(3));

    }

    @Test
    public void minTilesToMoveTest2() {
        ProgramCard card1 = Mockito.mock(MoveBackwardCard.class);
        ProgramCard card2 = Mockito.mock(MoveForwardCard.class);// 2
        ProgramCard card3 = Mockito.mock(MoveForwardCard.class);// 3
        ProgramCard card4 = Mockito.mock(TurnLeftCard.class);
        ProgramCard card5 = Mockito.mock(MoveForwardCard.class);// 2
        ProgramCard card6 = Mockito.mock(MoveForwardCard.class);// 3
        ProgramCard card7 = Mockito.mock(MoveForwardCard.class);// 1
        Mockito.when(card2.getRange()).thenReturn(2);
        Mockito.when(card3.getRange()).thenReturn(3);
        Mockito.when(card5.getRange()).thenReturn(2);
        Mockito.when(card6.getRange()).thenReturn(3);
        Mockito.when(card7.getRange()).thenReturn(1);
        List<ProgramCard> cards = new LinkedList<ProgramCard>(
                Arrays.asList(card1, card2, card3, card4, card5, card6, card7));
        cpo.setCards(cards);
        List<Integer> used = new LinkedList<Integer>(Arrays.asList(3));
        cpo.setGlobalUsedCards(used);

        assertEquals(3, cpo.minTilesToMove(2));

    }

    @Test
    public void minTilesToMoveTest3() {
        ProgramCard card1 = Mockito.mock(MoveBackwardCard.class);
        ProgramCard card2 = Mockito.mock(MoveForwardCard.class);// 2
        ProgramCard card3 = Mockito.mock(MoveForwardCard.class);// 3
        ProgramCard card4 = Mockito.mock(TurnLeftCard.class);
        ProgramCard card5 = Mockito.mock(MoveForwardCard.class);// 2
        ProgramCard card6 = Mockito.mock(MoveForwardCard.class);// 3
        ProgramCard card7 = Mockito.mock(MoveForwardCard.class);// 1
        Mockito.when(card2.getRange()).thenReturn(2);
        Mockito.when(card3.getRange()).thenReturn(3);
        Mockito.when(card5.getRange()).thenReturn(2);
        Mockito.when(card6.getRange()).thenReturn(3);
        Mockito.when(card7.getRange()).thenReturn(1);
        List<ProgramCard> cards = new LinkedList<ProgramCard>(
                Arrays.asList(card1, card2, card3, card4, card5, card6, card7));
        cpo.setCards(cards);
        List<Integer> used = new LinkedList<Integer>(Arrays.asList(3));
        cpo.setGlobalUsedCards(used);

        assertEquals(5, cpo.minTilesToMove(3));

    }

    @Test
    public void minTilesToMoveTest4() {
        ProgramCard card1 = Mockito.mock(MoveBackwardCard.class);
        ProgramCard card2 = Mockito.mock(MoveForwardCard.class);// 1
        ProgramCard card3 = Mockito.mock(MoveForwardCard.class);// 3
        ProgramCard card4 = Mockito.mock(TurnLeftCard.class);
        ProgramCard card5 = Mockito.mock(MoveForwardCard.class);// 2
        ProgramCard card6 = Mockito.mock(MoveForwardCard.class);// 3
        ProgramCard card7 = Mockito.mock(MoveForwardCard.class);// 1
        Mockito.when(card2.getRange()).thenReturn(1);
        Mockito.when(card3.getRange()).thenReturn(3);
        Mockito.when(card5.getRange()).thenReturn(2);
        Mockito.when(card6.getRange()).thenReturn(3);
        Mockito.when(card7.getRange()).thenReturn(1);
        List<ProgramCard> cards = new LinkedList<ProgramCard>(
                Arrays.asList(card1, card2, card3, card4, card5, card6, card7));
        cpo.setCards(cards);
        List<Integer> used = new LinkedList<Integer>(Arrays.asList(1, 2));
        cpo.setGlobalUsedCards(used);

        assertEquals(6, cpo.minTilesToMove(3));

    }

    @Test
    public void minTilesToMoveTest5() {
        ProgramCard card1 = Mockito.mock(MoveBackwardCard.class);
        ProgramCard card2 = Mockito.mock(MoveForwardCard.class);// 2
        ProgramCard card3 = Mockito.mock(MoveForwardCard.class);// 3
        ProgramCard card4 = Mockito.mock(TurnLeftCard.class);
        ProgramCard card5 = Mockito.mock(MoveForwardCard.class);// 2
        ProgramCard card6 = Mockito.mock(MoveForwardCard.class);// 3
        ProgramCard card7 = Mockito.mock(MoveForwardCard.class);// 1
        Mockito.when(card2.getRange()).thenReturn(2);
        Mockito.when(card3.getRange()).thenReturn(3);
        Mockito.when(card5.getRange()).thenReturn(2);
        Mockito.when(card6.getRange()).thenReturn(3);
        Mockito.when(card7.getRange()).thenReturn(1);
        List<ProgramCard> cards = new LinkedList<ProgramCard>(
                Arrays.asList(card1, card2, card3, card4, card5, card6, card7));
        cpo.setCards(cards);
        List<Integer> used = new LinkedList<Integer>(Arrays.asList(0, 1, 2, 3, 4, 5, 6));
        cpo.setGlobalUsedCards(used);

        assertEquals(0, cpo.minTilesToMove(2));

    }

    @Test
    public void testHandleOnlyTurnCardsMoveForward() throws Exception {
        CurrentPlannerOne planner = preparePlanner();
        List<ProgramCard> cards = planner.planTurn();
        assertNotNull(cards);
        assertEquals(5, cards.size());
        List<ProgramCard> expectedCards = Arrays.asList(new GameTurnRightCard(-1), new GameTurnRightCard(-1),
                new GameTurnLeftCard(-1), new GameTurnLeftCard(-1), new GameTurnLeftCard(-1));
        Map<Logic.Move, Integer> expectedFrequencyTable = logicOne.getFrequencyTable(
                logicOne.programCards2Moves(expectedCards));
        Map<Logic.Move, Integer> actualFrequencyTable = logicOne.getFrequencyTable(
                logicOne.programCards2Moves(cards));
        assertEquals(expectedFrequencyTable, actualFrequencyTable);
    }
    
    @Test
    public void shouldCallPowerdownFromRobot() throws KeyNotFoundException{
        GameRobot rob = Mockito.mock(GameRobot.class);
        
        CurrentPlannerOne currentPlanner = createCPO((AIPlayer1) controller,5, 0.5, rob);   
        currentPlanner.evaluatePowerDown();
        Mockito.verify(m, Mockito.times(1)).requestPowerDownStatusChange(rob);
        
//        Mockito.verify(rob, Mockito.times(1)).powerDown();
    }
    
    @Test
    public void shouldNotCallPowerdownFromRobot() throws KeyNotFoundException{
        GameRobot rob = Mockito.mock(GameRobot.class);
        CurrentPlannerOne currentPlanner = createCPO((AIPlayer1)controller,6, 0.5, rob);   
        currentPlanner.evaluatePowerDown();
        Mockito.verify(m, Mockito.never()).requestPowerDownStatusChange(rob);
//        Mockito.verify(rob, Mockito.never()).powerDown();
    }
    
    private CurrentPlannerOne createCPO(AIPlayer1 con,int HP, double risk, GameRobot rob) throws KeyNotFoundException{
       
        
        Mockito.when(con.getHP(anyInt())).thenReturn(HP);
        Mockito.when(con.getRobot()).thenReturn(rob);
        
        AgentConfigReader config = Mockito.mock(AgentConfigReader.class);
        Mockito.when(config.getDoubleProperty(anyString())).thenReturn(risk);
        
        CurrentPlannerOne currentPlanner = new CurrentPlannerOne(con, null, futurePlanner, logicOne, pathPlanner);
        currentPlanner.setConfigReader(config);
        
        return currentPlanner;
        
    }

    private CurrentPlannerOne preparePlanner() throws KeyNotFoundException {
        // mock config
        AgentConfigReader config = Mockito.mock(AgentConfigReader.class);
        Mockito.when(config.getIntProperty(anyString())).thenReturn(10); // set
                                                                         // arbitrarily
        Mockito.when(config.getDoubleProperty(anyString())).thenReturn(1.0); // set
                                                                             // arbitrarily

        Plan plans = Mockito.mock(Plan.class);

        // mock future planner
        FuturePlanning futurePlanner = Mockito.mock(FuturePlannerOne.class);
        Mockito.when(futurePlanner.getCurrentPlan()).thenReturn(plans);

        // mock controller
        Control controller = Mockito.mock(Control.class);
        Tile tile = Mockito.mock(Tile.class);
        Mockito.when(controller.requestData(RequestType.CURRENTTILE, null)).thenReturn(tile);

        // set cards
        List<ProgramCard> availableCards = Arrays.asList(new GameTurnLeftCard(-1), new GameTurnLeftCard(-1),
                new GameTurnRightCard(-1), new GameTurnRightCard(-1), new GameTurnLeftCard(-1));
        Mockito.when(controller.requestData(null, 1)).thenReturn(availableCards.get(0));
        Mockito.when(controller.requestData(null, 2)).thenReturn(availableCards.get(1));
        Mockito.when(controller.requestData(null, 3)).thenReturn(availableCards.get(2));
        Mockito.when(controller.requestData(null, 4)).thenReturn(availableCards.get(3));
        Mockito.when(controller.requestData(null, 5)).thenReturn(availableCards.get(4));
        
        //mock robotName
        Mockito.when(controller.getRobotName()).thenReturn("WHIRLBOT");

        // define HP
        Mockito.when(controller.requestData(RequestType.HP, null)).thenReturn(5);

        // mock logic
        LogicOne logic = new LogicOne();
        LogicOne logicSpy = Mockito.spy(logic);
        Mockito.doReturn(availableCards).when(logicSpy).retrieveUsableCards(any());

        PathPlanner pathPlanner = Mockito.mock(PathPlanner.class);

        CurrentPlannerOne planner = new CurrentPlannerOne(controller, null, futurePlanner, logicSpy, pathPlanner);
        CurrentPlannerOne plannerSpy = Mockito.spy(planner);
        plannerSpy.setConfigReader(config);

        Goals goals = Mockito.mock(Goals.class);
        PriorityQueue prioQueue = Mockito.mock(PriorityQueue.class);
        Mockito.when(prioQueue.poll()).thenReturn(goals);

        Mockito.when(plans.getGoals()).thenReturn(prioQueue);

        plannerSpy.setCurrentPlan(plans);

        // tell planner that the goal is to move forward (hence -1)
        Mockito.doReturn(-1).when(plannerSpy).getRelPosStraight(any(), any());
        return plannerSpy;
    }
}
