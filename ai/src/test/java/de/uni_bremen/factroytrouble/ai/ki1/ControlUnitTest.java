package de.uni_bremen.factroytrouble.ai.ki1;

import de.uni_bremen.factroytrouble.ai.ais.AIPlayer1;
import de.uni_bremen.factroytrouble.api.ki1.Control.GoalType;
import de.uni_bremen.factroytrouble.exceptions.KeyNotFoundException;
import de.uni_bremen.factroytrouble.gameobjects.GameRobot;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.player.*;
import de.uni_bremen.factroytrouble.spring.InitSpring;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Ignore;
import org.junit.runner.RunWith;

import de.uni_bremen.factroytrouble.ai.ais.GlobalAIFactory;
import de.uni_bremen.factroytrouble.board.Board;
import de.uni_bremen.factroytrouble.board.Dock;
import de.uni_bremen.factroytrouble.board.Field;
import de.uni_bremen.factroytrouble.board.GameTile;
import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.player.GameMaster;
import de.uni_bremen.factroytrouble.player.GameMasterFactory;
import de.uni_bremen.factroytrouble.player.GameMoveForwardCard;
import de.uni_bremen.factroytrouble.player.GameTurnLeftCard;
import de.uni_bremen.factroytrouble.player.GameTurnRightCard;
import de.uni_bremen.factroytrouble.player.GameUturnCard;
import de.uni_bremen.factroytrouble.player.Master;
import de.uni_bremen.factroytrouble.player.Player;
import de.uni_bremen.factroytrouble.player.ProgramCard;

import static org.junit.Assert.assertEquals;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/ki1/test-context.xml"})
@Ignore("Es werden Statischen Daten aus dem Board verwendet")
public class ControlUnitTest {
    private static final int GAME_ID = 0;
    
    private static final String MAP = "deathTrap";
    
    private AIPlayer1 controlUnit;
    
    private Master master;
    
    private List<Robot> robotList;
    private List<Player> playerList;
    private Map<String, Map<String,Point>> startPositions;
    private Map<String,Board> boardMap;
    private Board board;
    private Map<Point, Tile> absCoor;
    
    private Robot ownRobot;
    
    GameMasterFactory gmfactory;

    @Autowired
    GlobalAIFactory gaif;
        
    private Player ownPlayer;

    @Before
    public void setUp() throws KeyNotFoundException, IOException{
        InitSpring.init();
        gmfactory = new GameMasterFactory();
        master = gmfactory.getMaster(GAME_ID);
        master.removeAllPlayers();
        robotList = getDefaultRobotList();
        ownRobot = robotList.get(0);
        ownPlayer = new GamePlayer(ownRobot);
        controlUnit = new AIPlayer1(GAME_ID, ownPlayer);
        controlUnit.setMasterFactory(gmfactory);
        playerList = getDefaultPlayerList(robotList);
        addPlayerToMaster(playerList);
        master.initialiseBoard(MAP);
        board = master.getBoard();
        absCoor = translateCoordinates(board);
        controlUnit.initialize();
        //controlUnit.giveCards(getTestCards());
    }
    
    @After
    public void setAllToNull(){
        master.removeAllPlayers();
        controlUnit = null;
        master = null;                                  
        robotList = null;                          
        playerList = null;                        
        startPositions = null;  
        boardMap = null;                     
        board = null;                                    
        absCoor = null;                       
        ownRobot = null;
        //gmfactory = null;
    }
    
    @Test@Ignore
    public void shouldRemovePlayedCards(){
        List<ProgramCard> cards = getTestCards();
        controlUnit.giveCards(cards);
        List<ProgramCard> playedCards = new ArrayList<ProgramCard>();
        for(int i = 0; i < 5; i++){
            playedCards.add(cards.get(i));
        }
        List<ProgramCard> remainingCards = new ArrayList<ProgramCard>();
        for(int i = 5; i < 9; i++){
            remainingCards.add(cards.get(i));
        }
        controlUnit.setCards(playedCards);
        assertEquals(controlUnit.getPlayerCards(), remainingCards);
    }

    @Test
    public void shouldIncreaseNextFlagCounter(){
        int oldFlag = controlUnit.getNextFlag();
        controlUnit.setPotentialReachedGoal(GoalType.FLAG);
        assertEquals(oldFlag + 1, controlUnit.getNextFlag());
    }
    
    @Test
    public void testExecuteTurn() {
        List<ProgramCard> cards = new ArrayList<>();
        
        cards.add(new GameMoveForwardCard(GAME_ID, 100, 1));
        cards.add(new GameMoveForwardCard(GAME_ID, 110, 1));
        cards.add(new GameMoveForwardCard(GAME_ID, 120, 1));
        cards.add(new GameMoveForwardCard(GAME_ID, 130, 1));
        cards.add(new GameTurnLeftCard(100));
        cards.add(new GameTurnLeftCard(100));
        cards.add(new GameTurnRightCard(100));
        cards.add(new GameTurnRightCard(100));
        cards.add(new GameUturnCard(100));
        
        controlUnit.giveCards(cards);
        
        controlUnit.executeTurn();
        
//        for(ProgramCard card: controlUnit.getRobot().getRegister()){
//            System.out.println(card);
//        }
        
        master.activateBoard();
        
//        System.out.println(ownRobot.getCurrentTile().getAbsoluteCoordinates());
//        assertTrue(true);
    }
    
    private List<Player> getDefaultPlayerList(List<Robot> robotList){
        
        List<Player> tmpList = new ArrayList<Player>();
        tmpList.add(controlUnit);
//        for(int i = 1; i < robotList.size(); i++){
//            tmpList.add(new GamePlayer(robotList.get(i)));
//        }
        return tmpList;
    }
    
    private List<Robot> getDefaultRobotList(){
        List<Robot> tmpList = new ArrayList<Robot>();
        for(String name:GameMaster.ROBOT_NAMES){
            tmpList.add(new GameRobot(GAME_ID, new GameTile(GAME_ID, new Point(0, 0)), Orientation.NORTH, name));
        }
        return tmpList;
    }
    
    private void addPlayerToMaster(List<Player> playerList){
        for(Player player : playerList){
            master.addPlayer(player);
        }
    }
    
    private void setStartPositons(List<Robot> robotList){
        for(Robot robot : robotList){
            robot.setCurrentTile(absCoor.get(startPositions.get(MAP).get(robot.getName())));
        }
    }
    
    /**
     * Liefert alle {@link Tile}s eines {@link Board}s gemappt auf ihre absolute
     * Position.
     * 
     * @param board
     *            das Board
     * @return Map von Position auf Tiles
     */
    private Map<Point, Tile> translateCoordinates(Board board) {
        Map<Point, Tile> result = new HashMap<Point, Tile>();
        for (Field field : board.getFields().values()) {
            result.putAll(translateCoordinates(field));
        }
        return result;
    }

    /**
     * Liefert alle {@link Tile}s eines {@link Field}s gemappt auf ihre absolute
     * Position. Geht davon aus, dass Dock bei Point(0, 0) liegt.
     * 
     * @param field
     *            das Field
     * @return Map von Position auf Tiles
     */
    private Map<Point, Tile> translateCoordinates(Field field) {
        Map<Point, Tile> result = new HashMap<Point, Tile>();
        int fieldX = field.getCoordinates().x;
        int fieldY = field.getCoordinates().y;

        int rootY = fieldY == 0 && fieldX == 0 ? 0 : (fieldY - 1) * Field.DIMENSION + Dock.SHORT_SIDE;
        int rootX = fieldX * Field.DIMENSION;

        for (Entry<Point, Tile> entry : field.getTiles().entrySet()) {
            int absoluteX = entry.getKey().x + rootX;
            int absoluteY = entry.getKey().y + rootY;
            Point pt = new Point(absoluteX, absoluteY);
            result.put(pt, entry.getValue());
        }
        return result;
    }
    
    private List<ProgramCard> getTestCards(){
        List<ProgramCard> cards = new ArrayList<>();
        
        cards.add(new GameMoveForwardCard(GAME_ID, 1, 1));
        cards.add(new GameMoveForwardCard(GAME_ID, 2, 1));
        cards.add(new GameMoveForwardCard(GAME_ID, 3, 1));
        cards.add(new GameMoveForwardCard(GAME_ID, 4, 1));
        cards.add(new GameTurnLeftCard(5));
        cards.add(new GameTurnLeftCard(6));
        cards.add(new GameTurnRightCard(7));
        cards.add(new GameTurnRightCard(8));
        cards.add(new GameUturnCard(9));
        
        return cards;
    }

}
