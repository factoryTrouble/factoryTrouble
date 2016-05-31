package de.uni_bremen.factroytrouble.ai.ki1.planning;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import de.uni_bremen.factroytrouble.ai.ais.AIPlayer1;
import de.uni_bremen.factroytrouble.ai.ki1.configreader.ConfigReader;
import de.uni_bremen.factroytrouble.ai.ki1.planning.TacticUnit.Tactics;
import de.uni_bremen.factroytrouble.api.ki1.Control;
import de.uni_bremen.factroytrouble.api.ki1.Logic;
import de.uni_bremen.factroytrouble.api.ki1.Control.RequestType;
import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.gameobjects.Hole;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.gameobjects.Wall;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class PathPlanningTest {
    Control controller = Mockito.mock(AIPlayer1.class);
    Logic logic = Mockito.mock(Logic.class);
    PathPlanner pathPlanner = new PathPlanner(null, controller, null, logic);
    Tile[][] tiles = new Tile[12][12];
    Wall wallS1_4 = Mockito.mock(Wall.class);
    Wall wallS1_8 = Mockito.mock(Wall.class);
    Wall wallW1_8 = Mockito.mock(Wall.class);
    Wall wallW2_7 = Mockito.mock(Wall.class);
    Hole hole3_7 = Mockito.mock(Hole.class);
    Hole hole6_7 = Mockito.mock(Hole.class);
    Hole hole7_7 = Mockito.mock(Hole.class);
    Hole hole8_6 = Mockito.mock(Hole.class);
    Hole hole9_7 = Mockito.mock(Hole.class);
    ConfigReader config = Mockito.mock(ConfigReader.class);
    ConfigReader bConfig = Mockito.mock(ConfigReader.class);
    
    private void setTilesAndNodes() {
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                Tile tile = Mockito.mock(Tile.class);
                Mockito.when(tile.getAbsoluteCoordinates()).thenReturn(new Point(i, j));
                tiles[i][j] = tile;
                Mockito.when(controller.requestData(null, new Point (i,j))).thenReturn(tiles[i][j]);
            }
        }
    }
    @Before
    public void setup(){
        setTilesAndNodes();
        List<Wall> walls1_4= new LinkedList<Wall>(Arrays.asList(wallS1_4));
        List<Wall> walls1_8=new LinkedList<Wall>(Arrays.asList(wallS1_8,wallW1_8));
        List<Wall> walls2_7=new LinkedList<Wall>(Arrays.asList(wallW2_7));
        Mockito.when(wallS1_4.getOrientation()).thenReturn(Orientation.EAST);
        Mockito.when(wallS1_8.getOrientation()).thenReturn(Orientation.NORTH);
        Mockito.when(wallW1_8.getOrientation()).thenReturn(Orientation.EAST);
        Mockito.when(wallW2_7.getOrientation()).thenReturn(Orientation.EAST);
        Mockito.when(tiles[3][7].getFieldObject()).thenReturn(hole3_7);
        Mockito.when(tiles[6][7].getFieldObject()).thenReturn(hole6_7);
        Mockito.when(tiles[7][7].getFieldObject()).thenReturn(hole7_7);
        Mockito.when(tiles[9][7].getFieldObject()).thenReturn(hole9_7);
        Mockito.when(tiles[8][6].getFieldObject()).thenReturn(hole8_6);
        Mockito.when(tiles[1][4].getWalls()).thenReturn(walls1_4);
        Mockito.when(tiles[1][8].getWalls()).thenReturn(walls1_8);
        Mockito.when(tiles[2][7].getWalls()).thenReturn(walls2_7);
        Mockito.when(controller.getAgentNumber()).thenReturn(1);
        Mockito.when(controller.getRobotName()).thenReturn("spinbot");
        Mockito.when(controller.requestData(RequestType.TACTICPOWER, null)).thenReturn(0);
        Mockito.when(controller.requestData(RequestType.TACTIC, null)).thenReturn(Tactics.ROBINSONCRUSO);
        pathPlanner.setCurrentPlanner(new CurrentPlannerOne(controller, null, null, logic, pathPlanner));
    }
    

    @Test
    public void returnOfPathPlannerShouldNotBeNull() {
        Mockito.when(controller.requestData(RequestType.HP, null)).thenReturn(10);
        assertFalse("Path is null", pathPlanner.getPath(pathPlanner.getTile(new Point(2,2)), pathPlanner.getTile(new Point(3,5)))==null);
    }
    @Test
    public void returnOfPathShouldBeStraightLineTillEdgeOfBoard() {
        List<Tile> comparePath = new LinkedList<Tile>(); 
        comparePath.add(tiles[2][1]);
        comparePath.add(tiles[2][0]);
        Mockito.when(controller.requestData(RequestType.HP, null)).thenReturn(10);
        assertEquals(comparePath, pathPlanner.getPath(pathPlanner.getTile(new Point(2,2)), pathPlanner.getTile(new Point(2,0))));
    }
    @Test
    public void returnOfPathFromCornerTest() {
        List<Tile> comparePath = new LinkedList<Tile>();
        comparePath.add(tiles[10][0]);
        comparePath.add(tiles[10][1]);
        comparePath.add(tiles[10][2]);
        comparePath.add(tiles[10][3]);
        comparePath.add(tiles[9][3]);
        Mockito.when(controller.requestData(RequestType.HP, null)).thenReturn(10);
        assertEquals(comparePath, pathPlanner.getPath(pathPlanner.getTile(new Point(11,0)), pathPlanner.getTile(new Point(9,3))));
    }
    @Test
    public void returnOfPathShouldAvoidHole() {//loop
        List<Tile> comparePath = new LinkedList<Tile>();
        comparePath.add(tiles[3][8]);
        comparePath.add(tiles[4][8]);
        comparePath.add(tiles[4][7]);
        comparePath.add(tiles[4][6]);
        comparePath.add(tiles[3][6]);
        Mockito.when(controller.requestData(RequestType.HP, null)).thenReturn(10);
        assertEquals(comparePath, pathPlanner.getPath(pathPlanner.getTile(new Point(3,9)), pathPlanner.getTile(new Point(3,6))));
    }
    @Test
    public void returnOfPathShouldAvoidHoles() {//loop
        List<Tile> comparePath = new LinkedList<Tile>();
        comparePath.add(tiles[8][8]);
        comparePath.add(tiles[7][8]);
        comparePath.add(tiles[6][8]);
        comparePath.add(tiles[5][8]);
        comparePath.add(tiles[5][7]);
        comparePath.add(tiles[5][6]);
        comparePath.add(tiles[6][6]);
        comparePath.add(tiles[7][6]);
        Mockito.when(controller.requestData(RequestType.HP, null)).thenReturn(10);
        assertEquals(comparePath, pathPlanner.getPath(pathPlanner.getTile(new Point(8,7)), pathPlanner.getTile(new Point(7,6))));
    }
    @Test
    public void returnOfPathShouldAvoidWall() {
        List<Tile> comparePath = new LinkedList<Tile>();
        comparePath.add(tiles[1][4]);
        comparePath.add(tiles[2][4]);
        comparePath.add(tiles[2][5]);
        comparePath.add(tiles[1][5]);
        Mockito.when(controller.requestData(RequestType.HP, null)).thenReturn(10);
        assertEquals(comparePath, pathPlanner.getPath(pathPlanner.getTile(new Point(1,3)), pathPlanner.getTile(new Point(1,5))));
    }
    @Test
    public void returnOfPathShouldAvoidWalls() {
        List<Tile> comparePath = new LinkedList<Tile>();
        comparePath.add(tiles[1][7]);
        comparePath.add(tiles[2][7]);
        comparePath.add(tiles[2][8]);
        Mockito.when(controller.requestData(RequestType.HP, null)).thenReturn(10);
        assertEquals(comparePath, pathPlanner.getPath(pathPlanner.getTile(new Point(1,8)), pathPlanner.getTile(new Point(2,8))));
    }
}
