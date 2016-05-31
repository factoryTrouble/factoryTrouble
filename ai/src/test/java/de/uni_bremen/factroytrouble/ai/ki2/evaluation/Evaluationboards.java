package de.uni_bremen.factroytrouble.ai.ki2.evaluation;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import de.uni_bremen.factroytrouble.ai.ais.AIPlayer2;
import de.uni_bremen.factroytrouble.ai.ki2.ai.memory.ScullyThought;
import de.uni_bremen.factroytrouble.ai.ki2.api.memory.Thought;
import de.uni_bremen.factroytrouble.board.Board;
import de.uni_bremen.factroytrouble.board.Field;
import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.gameobjects.ConveyorBelt;
import de.uni_bremen.factroytrouble.gameobjects.Flag;
import de.uni_bremen.factroytrouble.gameobjects.GameRobot;
import de.uni_bremen.factroytrouble.gameobjects.Hole;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.gameobjects.Wall;
import de.uni_bremen.factroytrouble.player.GamePlayer;
import de.uni_bremen.factroytrouble.player.Player;
import de.uni_bremen.factroytrouble.player.ProgramCard;

@RunWith(MockitoJUnitRunner.class)
public class Evaluationboards {

    @Mock
    private Board mockBoard;
    @Mock
    private Field mockField;
    @Mock
    private Hole hole1, hole2, hole3, hole4, hole5, hole6, hole7, hole8, hole9, hole10, hole11, hole12, hole13, hole14,
            hole15, hole16, hole17, hole18, hole19;
    @Mock
    private Flag flag1;
    @Mock
    private Wall wall1, wall2, wall3, wall4;
    @Mock
    private ConveyorBelt cb1, cb2, cb3, cb4;
    
    @Mock
    private ProgramCard moveOne, moveTwo, moveThree, moveBack, uTurn, turnLeft, turnRight;

    private AIPlayer2 ai;

    private Player player;

    private Map<Point, Tile> mockTiles;

    private Robot robot;

    @Before
    public void setUp() throws Exception {
        robot = new GameRobot(0, defaultTileMap().get(new Point(5,0)), Orientation.NORTH , "Twonkey");
                player = new GamePlayer(robot);
                ai = new AIPlayer2(0, player);
        mockTiles = defaultTileMap();
        HashMap<Point, Field> map = new HashMap<>();
        map.put(new Point(0, 0), mockField);

        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                Mockito.when(mockBoard.getAbsoluteCoordinates(mockTiles.get(new Point(i, j))))
                        .thenReturn(new Point(i, j));

            }
        }
    }

    @Test
    public void testTiles() {
        defaultTileMap();
    }

    /*
     * Ein 12 * 12 Board mit 2*2 Löchern (5,3 bis 6,4) und einer Flagge dahinter
     * (5,5). Roboter startet unterhalb der Löcher.
     * 
     */
    @Test
    public void testBoardWithFourHolesFlagBehind() {

        // Löcher
        Mockito.when(mockTiles.get(new Point(5, 3)).getFieldObject()).thenReturn(hole1);
        Mockito.when(mockTiles.get(new Point(6, 3)).getFieldObject()).thenReturn(hole2);
        Mockito.when(mockTiles.get(new Point(5, 4)).getFieldObject()).thenReturn(hole3);
        Mockito.when(mockTiles.get(new Point(6, 4)).getFieldObject()).thenReturn(hole4);
        // Flagge
        Mockito.when(mockTiles.get(new Point(5, 5)).getFieldObject()).thenReturn(flag1);
        
        Thought thought = new ScullyThought("cards");
        thought.addInformationToThought(moveOne);
        thought.addInformationToThought(moveOne);
        thought.addInformationToThought(moveOne);
        thought.addInformationToThought(moveOne);
        thought.addInformationToThought(moveOne);
        thought.addInformationToThought(moveOne);
        thought.addInformationToThought(moveOne);
        thought.addInformationToThought(moveOne);
        thought.addInformationToThought(moveOne);
        
        
    }

    /*
     * Ein 12 * 12 Board mit einem Korridor zwischen Löchern mit einer Flagge am
     * ende des Korridors
     * 
     */
    @Test
    public void testBoardCorridorFlagAtEnd() {
        // Löcher
        Mockito.when(mockTiles.get(new Point(5, 3)).getFieldObject()).thenReturn(hole1);
        Mockito.when(mockTiles.get(new Point(5, 4)).getFieldObject()).thenReturn(hole2);
        Mockito.when(mockTiles.get(new Point(5, 5)).getFieldObject()).thenReturn(hole3);
        Mockito.when(mockTiles.get(new Point(5, 6)).getFieldObject()).thenReturn(hole4);
        Mockito.when(mockTiles.get(new Point(7, 3)).getFieldObject()).thenReturn(hole5);
        Mockito.when(mockTiles.get(new Point(7, 4)).getFieldObject()).thenReturn(hole6);
        Mockito.when(mockTiles.get(new Point(7, 5)).getFieldObject()).thenReturn(hole7);
        Mockito.when(mockTiles.get(new Point(7, 6)).getFieldObject()).thenReturn(hole8);
        // Flagge
        Mockito.when(mockTiles.get(new Point(6, 7)).getFieldObject()).thenReturn(flag1);
    }

    /*
     * Ein 12 * 12 Board mit einem Korridor zwischen Löchern mit einer Flagge am
     * ende des Korridors. Ein 3-facher Laser im Korridor.
     * 
     * Geht der Roboter durch den Korridor oder außenrum?
     */
    @Test
    public void testBoardCorridorFlagAtEndLaserInCorrdor() {

        // Laser
        Mockito.when(wall1.getOrientation()).thenReturn(Orientation.EAST);
        Mockito.when(wall1.hasLaser()).thenReturn(3);
        Mockito.when(mockTiles.get(new Point(5, 5)).getWall(Orientation.EAST)).thenReturn(wall1);
        // Löcher
        Mockito.when(mockTiles.get(new Point(5, 3)).getFieldObject()).thenReturn(hole1);
        Mockito.when(mockTiles.get(new Point(5, 4)).getFieldObject()).thenReturn(hole2);
        Mockito.when(mockTiles.get(new Point(5, 5)).getFieldObject()).thenReturn(hole3);
        Mockito.when(mockTiles.get(new Point(5, 6)).getFieldObject()).thenReturn(hole4);
        Mockito.when(mockTiles.get(new Point(7, 3)).getFieldObject()).thenReturn(hole5);
        Mockito.when(mockTiles.get(new Point(7, 4)).getFieldObject()).thenReturn(hole6);
        Mockito.when(mockTiles.get(new Point(7, 5)).getFieldObject()).thenReturn(hole7);
        Mockito.when(mockTiles.get(new Point(7, 6)).getFieldObject()).thenReturn(hole8);
        // Flagge
        Mockito.when(mockTiles.get(new Point(6, 7)).getFieldObject()).thenReturn(flag1);
    }

    /*
     * Ein 12 * 12 Board mit einem kleinen Labyrinth. Flagge am ende des
     * Labyrinths.
     * 
     * Geht der Roboter durch das Labyrinth oder außenrum?
     */
    @Test
    public void testBoardLabyrinth() {

        // Löcher
        Mockito.when(mockTiles.get(new Point(4, 4)).getFieldObject()).thenReturn(hole1);
        Mockito.when(mockTiles.get(new Point(4, 5)).getFieldObject()).thenReturn(hole2);
        Mockito.when(mockTiles.get(new Point(4, 6)).getFieldObject()).thenReturn(hole3);
        Mockito.when(mockTiles.get(new Point(4, 7)).getFieldObject()).thenReturn(hole4);

        Mockito.when(mockTiles.get(new Point(7, 4)).getFieldObject()).thenReturn(hole5);
        Mockito.when(mockTiles.get(new Point(7, 5)).getFieldObject()).thenReturn(hole6);
        Mockito.when(mockTiles.get(new Point(7, 6)).getFieldObject()).thenReturn(hole7);
        Mockito.when(mockTiles.get(new Point(7, 7)).getFieldObject()).thenReturn(hole8);

        Mockito.when(mockTiles.get(new Point(5, 4)).getFieldObject()).thenReturn(hole9);
        Mockito.when(mockTiles.get(new Point(6, 6)).getFieldObject()).thenReturn(hole10);

        // Flagge
        Mockito.when(mockTiles.get(new Point(5, 8)).getFieldObject()).thenReturn(flag1);
    }

    /*
     * Ein 12 * 12 Board mit zwei Korridoren umgeben mit Löchern. Ein Korridor
     * enthält zwei Förderbänder die direkt in Loch führen. Flagge am ende der
     * Korridore.
     * 
     * Geht der Roboter der Roboter mit entsprechenden Karten den Richtigen weg?
     */
    @Test
    public void testBoardTwoCorridorsOneWithConveyors() {

        Mockito.when(mockTiles.get(new Point(5, 2)).getFieldObject()).thenReturn(cb1);
        Mockito.when(mockTiles.get(new Point(5, 3)).getFieldObject()).thenReturn(cb2);
        Mockito.when(cb1.getOrientation()).thenReturn(Orientation.WEST);
        Mockito.when(cb2.getOrientation()).thenReturn(Orientation.WEST);

        // Löcher
        Mockito.when(mockTiles.get(new Point(0, 1)).getFieldObject()).thenReturn(hole1);
        Mockito.when(mockTiles.get(new Point(1, 1)).getFieldObject()).thenReturn(hole2);
        Mockito.when(mockTiles.get(new Point(2, 1)).getFieldObject()).thenReturn(hole3);
        Mockito.when(mockTiles.get(new Point(3, 1)).getFieldObject()).thenReturn(hole4);
        Mockito.when(mockTiles.get(new Point(4, 1)).getFieldObject()).thenReturn(hole5);

        Mockito.when(mockTiles.get(new Point(4, 2)).getFieldObject()).thenReturn(hole6);
        Mockito.when(mockTiles.get(new Point(4, 3)).getFieldObject()).thenReturn(hole7);
        Mockito.when(mockTiles.get(new Point(4, 4)).getFieldObject()).thenReturn(hole8);

        Mockito.when(mockTiles.get(new Point(6, 1)).getFieldObject()).thenReturn(hole9);
        Mockito.when(mockTiles.get(new Point(6, 2)).getFieldObject()).thenReturn(hole10);
        Mockito.when(mockTiles.get(new Point(6, 3)).getFieldObject()).thenReturn(hole11);
        Mockito.when(mockTiles.get(new Point(6, 4)).getFieldObject()).thenReturn(hole12);

        Mockito.when(mockTiles.get(new Point(8, 1)).getFieldObject()).thenReturn(hole13);
        Mockito.when(mockTiles.get(new Point(8, 2)).getFieldObject()).thenReturn(hole14);
        Mockito.when(mockTiles.get(new Point(8, 3)).getFieldObject()).thenReturn(hole15);
        Mockito.when(mockTiles.get(new Point(8, 4)).getFieldObject()).thenReturn(hole16);

        Mockito.when(mockTiles.get(new Point(9, 1)).getFieldObject()).thenReturn(hole17);
        Mockito.when(mockTiles.get(new Point(10, 1)).getFieldObject()).thenReturn(hole18);
        Mockito.when(mockTiles.get(new Point(11, 1)).getFieldObject()).thenReturn(hole19);

        // Flagge
        Mockito.when(mockTiles.get(new Point(6, 5)).getFieldObject()).thenReturn(flag1);
    }

    /*
     * Ein 12 * 12 Board mit zwei Durchgängen. Ein Durchgang enthält
     * Förderbänder, die den Roboter zur Flagge führen. Der andere Durchgang
     * enthält keine Färdebänder.
     * 
     * Geht der Roboter der Roboter mit entsprechenden Karten den Richtigen weg?
     */
    @Test
    public void testBoardTwoWaysOneWithConveyorOneWithout() {
        Mockito.when(mockTiles.get(new Point(5, 2)).getFieldObject()).thenReturn(cb1);
        Mockito.when(mockTiles.get(new Point(5, 3)).getFieldObject()).thenReturn(cb2);
        Mockito.when(mockTiles.get(new Point(5, 4)).getFieldObject()).thenReturn(cb3);
        Mockito.when(mockTiles.get(new Point(5, 5)).getFieldObject()).thenReturn(cb4);
        Mockito.when(cb1.getOrientation()).thenReturn(Orientation.NORTH);
        Mockito.when(cb2.getOrientation()).thenReturn(Orientation.NORTH);
        Mockito.when(cb1.getOrientation()).thenReturn(Orientation.NORTH);
        Mockito.when(cb2.getOrientation()).thenReturn(Orientation.EAST);

        // Wände

        Mockito.when(wall1.getOrientation()).thenReturn(Orientation.NORTH);
        Mockito.when(wall2.getOrientation()).thenReturn(Orientation.EAST);
        Mockito.when(wall3.getOrientation()).thenReturn(Orientation.WEST);
        Mockito.when(wall4.getOrientation()).thenReturn(Orientation.SOUTH);
        Mockito.when(mockTiles.get(new Point(0, 2)).getWall(Orientation.NORTH)).thenReturn(wall1);
        Mockito.when(mockTiles.get(new Point(1, 2)).getWall(Orientation.NORTH)).thenReturn(wall1);
        Mockito.when(mockTiles.get(new Point(2, 2)).getWall(Orientation.NORTH)).thenReturn(wall1);
        Mockito.when(mockTiles.get(new Point(3, 2)).getWall(Orientation.NORTH)).thenReturn(wall1);
        Mockito.when(mockTiles.get(new Point(4, 2)).getWall(Orientation.NORTH)).thenReturn(wall1);

        Mockito.when(mockTiles.get(new Point(6, 2)).getWall(Orientation.NORTH)).thenReturn(wall1);
        Mockito.when(mockTiles.get(new Point(6, 2)).getWall(Orientation.EAST)).thenReturn(wall2);
        Mockito.when(mockTiles.get(new Point(6, 2)).getWall(Orientation.WEST)).thenReturn(wall3);
        Mockito.when(mockTiles.get(new Point(6, 3)).getWall(Orientation.EAST)).thenReturn(wall2);
        Mockito.when(mockTiles.get(new Point(6, 3)).getWall(Orientation.WEST)).thenReturn(wall3);
        Mockito.when(mockTiles.get(new Point(6, 4)).getWall(Orientation.EAST)).thenReturn(wall2);
        Mockito.when(mockTiles.get(new Point(6, 4)).getWall(Orientation.WEST)).thenReturn(wall3);
        Mockito.when(mockTiles.get(new Point(6, 4)).getWall(Orientation.SOUTH)).thenReturn(wall4);

        Mockito.when(mockTiles.get(new Point(8, 2)).getWall(Orientation.NORTH)).thenReturn(wall1);
        Mockito.when(mockTiles.get(new Point(9, 2)).getWall(Orientation.NORTH)).thenReturn(wall1);
        Mockito.when(mockTiles.get(new Point(10, 2)).getWall(Orientation.NORTH)).thenReturn(wall1);
        Mockito.when(mockTiles.get(new Point(11, 2)).getWall(Orientation.NORTH)).thenReturn(wall1);
        // Flagge
        Mockito.when(mockTiles.get(new Point(6, 5)).getFieldObject()).thenReturn(flag1);

    }

    /*
     * Ein 12 * 12 Board mit einem Laser zwischen dem Roboter und der Flagge
     * Laser schießt von West nach Ost, ab dem Tile 4,3.
     * 
     * Geht der Roboter durch den Laser oder außenrum?
     */
    @Test
    public void testBoardLaserBetweenFlagAndRobot() {

        // Laser
        Mockito.when(wall1.getOrientation()).thenReturn(Orientation.WEST);
        Mockito.when(wall1.hasLaser()).thenReturn(3);
        Mockito.when(mockTiles.get(new Point(4, 3)).getWall(Orientation.WEST)).thenReturn(wall1);

        // Flagge
        Mockito.when(mockTiles.get(new Point(5, 5)).getFieldObject()).thenReturn(flag1);
    }

    private Map<Point, Tile> defaultTileMap() {
        Map<Point, Tile> m = new HashMap<>();
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                Tile tile = Mockito.mock(Tile.class);
                Mockito.when(tile.getCoordinates()).thenReturn(new Point(i, j));
                Mockito.when(tile.getAbsoluteCoordinates()).thenReturn(new Point(i, j));

                m.put(new Point(i, j), tile);
            }
        }
        for (int k = 0; k < 12; k++) {
            for (int l = 0; l < 12; l++) {

                Tile tile = m.get(new Point(k, l));
                HashMap<Orientation, Tile> mockNeighbours = new HashMap<>();
                if (l < 12) {
                    mockNeighbours.put(Orientation.EAST, m.get(new Point(k, l + 1)));
                }

                if (k < 12) {
                    mockNeighbours.put(Orientation.NORTH, m.get(new Point(k + 1, l)));
                }
                Mockito.when(tile.getNeighbors()).thenReturn(mockNeighbours);

            }
        }
        return m;
    }

}
