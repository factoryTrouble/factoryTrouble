package de.uni_bremen.factroytrouble.ai.ki1.planning;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import de.uni_bremen.factroytrouble.ai.ais.AIPlayer1;
import de.uni_bremen.factroytrouble.api.ki1.Control;
import de.uni_bremen.factroytrouble.api.ki1.Logic;
import de.uni_bremen.factroytrouble.api.ki1.Control.RequestType;
import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.gameobjects.ConveyorBelt;
import de.uni_bremen.factroytrouble.gameobjects.Gear;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.gameobjects.Wall;

/**
 * @author Artur
 *
 */
public class PathNodeTest {
    Control controller = Mockito.mock(AIPlayer1.class);
    CurrentPlannerOne cpo = Mockito.mock(CurrentPlannerOne.class);
    Logic logic = Mockito.mock(Logic.class);
    Tile[][] tiles = new Tile[14][14];
    PathNode[][] nodes = new PathNode[14][14];
    PathNode currentNode = new PathNode(tiles[3][3], cpo, logic);
    Wall wall3_3 = Mockito.mock(Wall.class);
    Wall wall4_3 = Mockito.mock(Wall.class);
    Wall wall3_3_south = Mockito.mock(Wall.class);
    Gear gear3_3 = Mockito.mock(Gear.class);

    private void setTilesAndNodes() {
        for (int i = 0; i < 14; i++) {
            for (int j = 0; j < 14; j++) {
                // if (i == 3 && j == 3) {
                // continue;
                if (i < 1 || i > 12 || j < 1 || j > 12) {
                    tiles[i][j] = null;
                    nodes[i][j] = null;
                } else {
                    Tile tile = Mockito.mock(Tile.class);
                    PathNode node = Mockito.mock(PathNode.class);
                    tiles[i][j] = tile;
                    nodes[i][j] = node;
                    Mockito.when(tile.getAbsoluteCoordinates()).thenReturn(new Point(i, j));
                    Mockito.when(tile.getFieldObject()).thenReturn(null);
                    Mockito.when(tile.getRobot()).thenReturn(null);
                    Mockito.when(node.getNeighbours()).thenReturn(
                            Arrays.asList(nodes[i][j + 1], nodes[i][j - 1], nodes[i + 1][j], nodes[i - 1][j]));
                    Mockito.when(node.getTile()).thenReturn(tiles[i][j]);
                    Mockito.when(controller.requestData(null, new Point(i, j))).thenReturn(tiles[i][j]);
                }
            }
        }
        for (int i = 0; i < 14; i++) {
            for (int j = 0; j < 14; j++) {
                if (i < 1 || i > 12 || j < 1 || j > 12) {
                    continue;
                } else {
                    Mockito.when(cpo.getRelPosStraight(tiles[i][j], tiles[i][j + 1])).thenReturn(1);
                    Mockito.when(cpo.getRelPosStraight(tiles[i][j], tiles[i][j - 1])).thenReturn(3);
                    Mockito.when(cpo.getRelPosStraight(tiles[i][j], tiles[i + 1][j])).thenReturn(2);
                    Mockito.when(cpo.getRelPosStraight(tiles[i][j], tiles[i - 1][j])).thenReturn(0);
                }
            }
        }
        currentNode.setNeighbour(nodes[3][2]);
        currentNode.setNeighbour(nodes[3][4]);
        currentNode.setNeighbour(nodes[4][3]);
        currentNode.setNeighbour(nodes[2][3]);
        currentNode.setTile(tiles[3][3]);

    }

    @Before
    public void setUp() {
        setTilesAndNodes();
        Mockito.when(logic.oriAsOri(2)).thenReturn(Orientation.EAST);
        Mockito.when(logic.oriAsOri(0)).thenReturn(Orientation.WEST);
        Mockito.when(logic.oriAsOri(1)).thenReturn(Orientation.NORTH);
        Mockito.when(logic.oriAsOri(3)).thenReturn(Orientation.SOUTH);
        Mockito.when(controller.getAgentNumber()).thenReturn(1);
        Mockito.when(controller.getRobotName()).thenReturn("spinbot");
    }

    @Test
    public void getTileCostTestShouldBeDefaultTileCostBNoLaser() {
        Mockito.when(controller.requestData(RequestType.HP, null)).thenReturn(10);
        assertEquals(10, currentNode.getTileCost(controller));
    }

    @Test
    public void getTileCostTestShouldBe1000BNoHP() {
        Mockito.when(controller.requestData(RequestType.HP, null)).thenReturn(1);
        Mockito.when(wall3_3.hasLaser()).thenReturn(1);
        Mockito.when(wall3_3.getOrientation()).thenReturn(Orientation.EAST);
        Mockito.when(tiles[3][3].hasWall(Orientation.EAST)).thenReturn(true);
        Mockito.when(tiles[3][3].getWall(Orientation.EAST)).thenReturn(wall3_3);
        List<Wall> walls = new ArrayList<Wall>(Arrays.asList(wall3_3));
        Mockito.when(tiles[3][3].getWalls()).thenReturn(walls);
        assertEquals(1000, currentNode.getTileCost(controller));
    }

    /*
     * Ein Laser auf dem Tile 2,3 schiesst Richtung Osten über dem Tile 3,3,
     * worauf sich der Roboter befindet
     */
    @Test
    public void shouldCalcCostsOfNeighbourTileCostsShouldBe1000() {
        Mockito.when(controller.requestData(RequestType.HP, null)).thenReturn(1);
        Mockito.when(cpo.checkForMoveabilityOnBoard(tiles[3][3], tiles[4][3], 2, true)).thenReturn(true);

        Mockito.when(wall4_3.hasLaser()).thenReturn(1);
        Mockito.when(wall4_3.getOrientation()).thenReturn(Orientation.EAST);
        Mockito.when(tiles[4][3].hasWall(Orientation.EAST)).thenReturn(true);
        Mockito.when(tiles[4][3].getWall(Orientation.EAST)).thenReturn(wall4_3);
        List<Wall> walls = new ArrayList<Wall>(Arrays.asList(wall4_3));
        Mockito.when(tiles[4][3].getWalls()).thenReturn(walls);
        Mockito.when(nodes[4][3].getNeighbours())
                .thenReturn(Arrays.asList(currentNode, nodes[5][3], nodes[4][4], nodes[4][2]));
        assertEquals(1000, currentNode.getTileCost(controller));
    }
    /*
     * defaultTileCost +  1/(hp - laserCount) * laserCostFactor * (1 - risk)
     */
    @Test
    public void shouldCalcCostsOfNeighbourTileCostsShouldWithTwoHP() {
        Mockito.when(controller.requestData(RequestType.HP, null)).thenReturn(2);
        Mockito.when(cpo.checkForMoveabilityOnBoard(tiles[3][3], tiles[4][3], 2, true)).thenReturn(true);

        Mockito.when(wall4_3.hasLaser()).thenReturn(1);
        Mockito.when(wall4_3.getOrientation()).thenReturn(Orientation.EAST);
        Mockito.when(tiles[4][3].hasWall(Orientation.EAST)).thenReturn(true);
        Mockito.when(tiles[4][3].getWall(Orientation.EAST)).thenReturn(wall4_3);
        List<Wall> walls = new ArrayList<Wall>(Arrays.asList(wall4_3));
        Mockito.when(tiles[4][3].getWalls()).thenReturn(walls);
        Mockito.when(nodes[4][3].getNeighbours())
                .thenReturn(Arrays.asList(currentNode, nodes[5][3], nodes[4][4], nodes[4][2]));
        assertEquals(260, currentNode.getTileCost(controller));
    }

    /*
     * Das Tile 3,3 wird von zwei Lasern beschossen (Ost, Süd). Der Roboter hat
     * zwei HP, also sollten die Kosten auf 1000 gesetzt werden
     */
    @Test
    public void getTileCostTestShouldBe1000BNoHPTwoLasers() {
        Mockito.when(controller.requestData(RequestType.HP, null)).thenReturn(2);
        Mockito.when(wall3_3.hasLaser()).thenReturn(1);
        Mockito.when(wall3_3_south.hasLaser()).thenReturn(1);
        Mockito.when(wall3_3.getOrientation()).thenReturn(Orientation.EAST);
        Mockito.when(wall3_3_south.getOrientation()).thenReturn(Orientation.SOUTH);
        Mockito.when(tiles[3][3].hasWall(Orientation.EAST)).thenReturn(true);
        Mockito.when(tiles[3][3].hasWall(Orientation.SOUTH)).thenReturn(true);
        Mockito.when(tiles[3][3].getWall(Orientation.EAST)).thenReturn(wall3_3);
        Mockito.when(tiles[3][3].getWall(Orientation.SOUTH)).thenReturn(wall3_3_south);
        List<Wall> walls = new ArrayList<Wall>(Arrays.asList(wall3_3, wall3_3_south));
        Mockito.when(tiles[3][3].getWalls()).thenReturn(walls);
        assertEquals(1000, currentNode.getTileCost(controller));
    }

    /*
     * defaultTileCost +  1/(hp - laserCount) * laserCostFactor * (1 - risk)
     * Ist die Rechnung in ordnung? (Siehe Block) Kosten betragen 260 Das Tile
     * 3,3 wird von zwei Lasern beschossen (Ost, Süd). Der Roboter hat drei HP.
     */
    @Test
    public void getTileCostTestShouldBeNot1000ThreeHPTwoLasers() {
        Mockito.when(controller.requestData(RequestType.HP, null)).thenReturn(3);
        Mockito.when(wall3_3.hasLaser()).thenReturn(1);
        Mockito.when(wall3_3_south.hasLaser()).thenReturn(1);
        Mockito.when(wall3_3.getOrientation()).thenReturn(Orientation.EAST);
        Mockito.when(wall3_3_south.getOrientation()).thenReturn(Orientation.SOUTH);
        Mockito.when(tiles[3][3].hasWall(Orientation.EAST)).thenReturn(true);
        Mockito.when(tiles[3][3].hasWall(Orientation.SOUTH)).thenReturn(true);
        Mockito.when(tiles[3][3].getWall(Orientation.EAST)).thenReturn(wall3_3);
        Mockito.when(tiles[3][3].getWall(Orientation.SOUTH)).thenReturn(wall3_3_south);
        List<Wall> walls = new ArrayList<Wall>(Arrays.asList(wall3_3, wall3_3_south));
        Mockito.when(tiles[3][3].getWalls()).thenReturn(walls);
        assertNotEquals(1000, currentNode.getTileCost(controller));
        assertEquals(260, currentNode.getTileCost(controller));
    }

    /*
     * Gleiches Problem wie in der oberen Testmethode Kosten ergeben 10
     */
    @Test
    public void getTileCostTestShouldBeNot1000FourHPTwoLasers() {
        Mockito.when(controller.requestData(RequestType.HP, null)).thenReturn(4);
        Mockito.when(wall3_3.hasLaser()).thenReturn(1);
        Mockito.when(wall3_3_south.hasLaser()).thenReturn(1);
        Mockito.when(wall3_3.getOrientation()).thenReturn(Orientation.EAST);
        Mockito.when(wall3_3_south.getOrientation()).thenReturn(Orientation.SOUTH);
        Mockito.when(tiles[3][3].hasWall(Orientation.EAST)).thenReturn(true);
        Mockito.when(tiles[3][3].hasWall(Orientation.SOUTH)).thenReturn(true);
        Mockito.when(tiles[3][3].getWall(Orientation.EAST)).thenReturn(wall3_3);
        Mockito.when(tiles[3][3].getWall(Orientation.SOUTH)).thenReturn(wall3_3_south);
        List<Wall> walls = new ArrayList<Wall>(Arrays.asList(wall3_3, wall3_3_south));
        Mockito.when(tiles[3][3].getWalls()).thenReturn(walls);
        assertNotEquals(1000, currentNode.getTileCost(controller));
        assertEquals(10, currentNode.getTileCost(controller));
    }

    /*
     * Berechnet die Kosten eines Tiles mit einem Gear
     * (defaultTileCost + GearCost)
     */
    @Test
    public void shouldCalcDefaultGearCosts() {
        Mockito.when(controller.requestData(RequestType.HP, null)).thenReturn(10);
        Mockito.when(tiles[3][3].getFieldObject()).thenReturn(gear3_3);
        assertEquals(52, currentNode.getTileCost(controller));
    }

    /*
     * Sollte auch 52 sein?
     * (defaultTileCost + GearCost)
     */
    @Test
    public void shouldCalcGearCostsOneHP() {
        Mockito.when(controller.requestData(RequestType.HP, null)).thenReturn(1);
        Mockito.when(tiles[3][3].getFieldObject()).thenReturn(gear3_3);
        assertEquals(52, currentNode.getTileCost(controller));
    }
    
    
    @Test 
    public void CBTilesShouldBeCheap(){
        Mockito.when(controller.requestData(RequestType.CURRENTTILE, null)).thenReturn(tiles[3][3]);
        Mockito.when(cpo.getGoalTile()).thenReturn(tiles[3][6]);
        Mockito.when(cpo.getRelPosStraight(tiles[3][3],tiles[3][6])).thenReturn(1);
        ConveyorBelt cb=Mockito.mock(ConveyorBelt.class);
        Mockito.when(cb.getOrientation()).thenReturn(Orientation.NORTH);
        Mockito.when(tiles[3][3].getFieldObject()).thenReturn(cb);
        Mockito.when(logic.oriAsInt(Orientation.NORTH)).thenReturn(1);
        assertEquals(5, currentNode.calcCBCost(controller, 10, 0.5));
    }
    
    @Test 
    public void CBTilesShouldBeExpensive(){
        Mockito.when(controller.requestData(RequestType.CURRENTTILE, null)).thenReturn(tiles[3][3]);
        Mockito.when(cpo.getGoalTile()).thenReturn(tiles[3][6]);
        Mockito.when(cpo.getRelPosStraight(tiles[3][3],tiles[3][6])).thenReturn(1);
        ConveyorBelt cb=Mockito.mock(ConveyorBelt.class);
        Mockito.when(cb.getOrientation()).thenReturn(Orientation.WEST);
        Mockito.when(tiles[3][3].getFieldObject()).thenReturn(cb);
        Mockito.when(logic.oriAsInt(Orientation.NORTH)).thenReturn(1);
        assertEquals(20, currentNode.calcCBCost(controller, 10, 0.5));
    }
    
    @Test 
    public void CBTilesShouldBeVeryExpensive(){
        Mockito.when(controller.requestData(RequestType.CURRENTTILE, null)).thenReturn(tiles[3][3]);
        Mockito.when(cpo.getGoalTile()).thenReturn(tiles[5][3]);
        Mockito.when(cpo.getRelPosStraight(tiles[3][3],tiles[5][3])).thenReturn(2);
        ConveyorBelt cb=Mockito.mock(ConveyorBelt.class);
        Mockito.when(cb.getOrientation()).thenReturn(Orientation.WEST);
        Mockito.when(tiles[3][3].getFieldObject()).thenReturn(cb);
        Mockito.when(logic.oriAsInt(Orientation.NORTH)).thenReturn(1);
        assertEquals(100, currentNode.calcCBCost(controller, 10, 0.5));
    }
    
    @Test 
    public void CBTilesShouldBeCheapDiagY(){
        Mockito.when(controller.requestData(RequestType.CURRENTTILE, null)).thenReturn(tiles[3][3]);
        Mockito.when(cpo.getGoalTile()).thenReturn(tiles[4][7]);
        Mockito.when(cpo.getRelPosStraight(tiles[3][3],tiles[4][7])).thenReturn(-1);
        ConveyorBelt cb=Mockito.mock(ConveyorBelt.class);
        Mockito.when(cb.getOrientation()).thenReturn(Orientation.NORTH);
        Mockito.when(tiles[3][3].getFieldObject()).thenReturn(cb);
        Mockito.when(logic.oriAsInt(Orientation.NORTH)).thenReturn(1);
        assertEquals(5, currentNode.calcCBCost(controller, 10, 0.5));
    }
    
    @Test 
    public void CBTilesShouldBeCheapDiagX(){
        Mockito.when(controller.requestData(RequestType.CURRENTTILE, null)).thenReturn(tiles[3][3]);
        Mockito.when(cpo.getGoalTile()).thenReturn(tiles[8][5]);
        Mockito.when(cpo.getRelPosStraight(tiles[3][3],tiles[8][5])).thenReturn(-1);
        ConveyorBelt cb=Mockito.mock(ConveyorBelt.class);
        Mockito.when(cb.getOrientation()).thenReturn(Orientation.EAST);
        Mockito.when(tiles[3][3].getFieldObject()).thenReturn(cb);
        Mockito.when(logic.oriAsInt(Orientation.EAST)).thenReturn(2);
        assertEquals(5, currentNode.calcCBCost(controller, 10, 0.5));
    }
    
    @Test 
    public void CBTilesShouldBeCheapDiagMinusX(){
        Mockito.when(controller.requestData(RequestType.CURRENTTILE, null)).thenReturn(tiles[3][3]);
        Mockito.when(cpo.getGoalTile()).thenReturn(tiles[1][4]);
        Mockito.when(cpo.getRelPosStraight(tiles[3][3],tiles[1][4])).thenReturn(-1);
        ConveyorBelt cb=Mockito.mock(ConveyorBelt.class);
        Mockito.when(cb.getOrientation()).thenReturn(Orientation.WEST);
        Mockito.when(tiles[3][3].getFieldObject()).thenReturn(cb);
        Mockito.when(logic.oriAsInt(Orientation.WEST)).thenReturn(0);
        assertEquals(5, currentNode.calcCBCost(controller, 10, 0.5));
    }
    
    @Test
    public void equalsShoudSucceed(){
        assertTrue(currentNode.equals(currentNode));
    }
    
    @Test
    public void equalsShoudBeFalseNull(){
        assertFalse(currentNode.equals(null));
    }
    
    @Test
    public void equalsShoudBeFalseWrongClass(){
        assertFalse(currentNode.equals(new Point(1,2)));
    }
    
    @Test
    public void equalsShoudBeFalseNoTile(){
        PathNode otherNode=new PathNode(null,cpo,logic);
        assertFalse(otherNode.equals(currentNode));
    }
    
    @Test
    public void equalsShoudBeFalseWrongTile(){
        assertFalse(currentNode.equals(new PathNode(tiles[1][1],cpo,logic)));
    }
    
    @Test
    public void equalsShoudBeTrueSameTile(){
        assertTrue(currentNode.equals(new PathNode(tiles[3][3],cpo,logic)));
    }


    @After
    public void tearDown() {
        tiles = new Tile[14][14];
        nodes = new PathNode[14][14];
        currentNode = new PathNode(tiles[3][3], cpo, logic);
    }

}
