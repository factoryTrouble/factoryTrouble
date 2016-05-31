package de.uni_bremen.factroytrouble.ai.ki2.integration;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import de.uni_bremen.factroytrouble.ai.ais.AIPlayer2;
import de.uni_bremen.factroytrouble.ai.ki2.ai.memory.ScullyThought;
import de.uni_bremen.factroytrouble.ai.ki2.ai.memory.ScullyVisual;
import de.uni_bremen.factroytrouble.ai.ki2.ai.memory.ScullyWorkingMemory;
import de.uni_bremen.factroytrouble.ai.ki2.api.memory.LongTermMemory;
import de.uni_bremen.factroytrouble.ai.ki2.api.memory.SensoryMemory;
import de.uni_bremen.factroytrouble.ai.ki2.api.memory.ShortTermMemory;
import de.uni_bremen.factroytrouble.ai.ki2.api.memory.Thought;
import de.uni_bremen.factroytrouble.ai.ki2.api.memory.WorkingMemory;
import de.uni_bremen.factroytrouble.board.Board;
import de.uni_bremen.factroytrouble.board.Field;
import de.uni_bremen.factroytrouble.board.GameTile;
import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.gameobjects.GameRobot;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.player.GameMaster;
import de.uni_bremen.factroytrouble.player.GamePlayer;
import de.uni_bremen.factroytrouble.player.Player;

@RunWith(MockitoJUnitRunner.class)
public class MemoryIntegrationTest {

    private WorkingMemory wm;
    
    private LongTermMemory ltm;
    
    private ShortTermMemory stm;
    
    private SensoryMemory sm;
    
    private ScullyVisual visual;
    
    @Mock
    private Board mockBoard;
    @Mock
    private Field mockField;
    
    private static final int GAME_ID = 0;
    
    private AIPlayer2 ai2;
    
    private Player ownPlayer;


    private Map<Point, Tile> mockTiles;

    private Robot ownRobot;

    private List<Robot> robotList;
    
    @Before
    public void setUp() throws Exception {
        
        robotList = getDefaultRobotList();
        ownRobot = robotList.get(GAME_ID);
        ownPlayer = new GamePlayer(ownRobot);
        ai2 = new AIPlayer2(GAME_ID, ownPlayer);
        
        mockTiles = defaultTileMap();
        
        //visual = new ScullyVisual(mockBoard , ai2);
        
        //sm = new ScullySensoryMemory(visual, ai2);
        
        //stm = new ScullyShortTermMemory();
        
        wm = new ScullyWorkingMemory(ai2);
        
        //ltm = new ScullyLongTermMemory(wm);
        
                HashMap<Point, Field> map = new HashMap<>();
        map.put(new Point(0, 0), mockField);
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                Mockito.when(mockBoard.getAbsoluteCoordinates(mockTiles.get(new Point(i, j))))
                        .thenReturn(new Point(i, j));

            }
        }
        
        
    }
    
    @Test@Ignore
    public void shouldGetInfoFromLTM(){
        Thought thought1 = new ScullyThought("test");
        wm.storeInformation(thought1);
        List<String> search = new ArrayList<>();
        search.add("test");
        assertEquals(thought1, wm.getInformation(search));
    }

    @After
    public void tearDown() throws Exception {
    }
    
    private Map<Point, Tile> defaultTileMap() {
        Map<Point, Tile> m = new HashMap<>();
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 13; j++) {
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
        }System.out.println(m);
        return m;
    }
    
    private List<Robot> getDefaultRobotList(){
        List<Robot> tmpList = new ArrayList<Robot>();
        for(String name:GameMaster.ROBOT_NAMES){
            tmpList.add(new GameRobot(GAME_ID, new GameTile(GAME_ID, new Point(0, 0)), Orientation.NORTH, name));
        }
        return tmpList;
    }
}
