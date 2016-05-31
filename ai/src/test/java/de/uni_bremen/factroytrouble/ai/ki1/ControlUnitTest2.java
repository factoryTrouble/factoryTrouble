package de.uni_bremen.factroytrouble.ai.ki1;

import de.uni_bremen.factroytrouble.ai.ais.AIPlayer1;
import de.uni_bremen.factroytrouble.api.ki1.Placement;
import de.uni_bremen.factroytrouble.api.ki1.Response;
import de.uni_bremen.factroytrouble.api.ki1.Visual;
import de.uni_bremen.factroytrouble.api.ki1.planning.CurrentPlanning;
import de.uni_bremen.factroytrouble.api.ki1.planning.FuturePlanning;
import de.uni_bremen.factroytrouble.board.Board;
import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.player.GamePlayer;
import de.uni_bremen.factroytrouble.player.Master;
import de.uni_bremen.factroytrouble.player.MasterFactory;
import de.uni_bremen.factroytrouble.player.Player;
import de.uni_bremen.factroytrouble.player.ProgramCard;
import de.uni_bremen.factroytrouble.spring.InitSpring;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.Mockito.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import de.uni_bremen.factroytrouble.ai.ais.GlobalAIFactory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

//@RunWith(MockitoJUnitRunner.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/ki1/test-context.xml"})
public class ControlUnitTest2 {
    private List<ProgramCard> cards;
    
    @Autowired
    private GlobalAIFactory gaif;

    private static final int GAME_ID = 0;

    @Mock private Robot robotMock;
    @Mock private Robot robotMock2;
    @Mock private Robot robotMock3;
    @Mock private Robot robotMock4;
    @Mock private Robot robotMock5;
    @Mock private Robot robotMock6;
    
    
    
    @Mock private MasterFactory mFactoryMock;
    @Mock private Visual visualMock;
    @Mock private FuturePlanning futureMock;
    @Mock private CurrentPlanning currentMock;
    @Mock private Response responseMock;
    @Mock private Board boardMock;
    @Mock private Master masterMock;
    
    @Mock private Player player2;
    @Mock private Player player3;
    @Mock private Player player4;
    @Mock private Player player5;
    @Mock private Player player6;
        
    @Mock private Placement placement;
    @Mock private Tile tile;
    
    private PlacementUnit place0 = new PlacementUnit(new Point(1, 0), Orientation.NORTH , tile);
    private PlacementUnit place1 = new PlacementUnit(new Point(1, 1), Orientation.NORTH , tile);
    private PlacementUnit place2 = new PlacementUnit(new Point(1, 2), Orientation.EAST , tile);
    private PlacementUnit place3 = new PlacementUnit(new Point(1, 3), Orientation.SOUTH , tile);
    private PlacementUnit place4 = new PlacementUnit(new Point(1, 4), Orientation.WEST , tile);
    private PlacementUnit place5 = new PlacementUnit(new Point(1, 5), Orientation.NORTH , tile);

    @Mock private ProgramCard card1;
    @Mock private ProgramCard card2;
    @Mock private ProgramCard card3;
    @Mock private ProgramCard card4;
    @Mock private ProgramCard card5;
    @Mock private ProgramCard card6;
    @Mock private ProgramCard card7;
    @Mock private ProgramCard card8;
    @Mock private ProgramCard card9;
    @Mock
    private Player mockPlayer;

    private Player player = new GamePlayer(robotMock);
    private AIPlayer1 controlUnitInjected;

    @Before
    public void setUp(){
        InitSpring.init();
        MockitoAnnotations.initMocks(this);
      Mockito.when(mockPlayer.getRobot()).thenReturn(robotMock);
       // Mockito.when(player.getRobot()).thenReturn(robotMock);
        Mockito.when(player2.getRobot()).thenReturn(robotMock2);
        Mockito.when(player3.getRobot()).thenReturn(robotMock3);
        Mockito.when(player4.getRobot()).thenReturn(robotMock4);
        Mockito.when(player5.getRobot()).thenReturn(robotMock5);
        Mockito.when(player6.getRobot()).thenReturn(robotMock6);
        
        Mockito.when(robotMock.getName()).thenReturn("spinbot");
        Mockito.when(robotMock2.getName()).thenReturn("HulkX90");
        Mockito.when(robotMock3.getName()).thenReturn("SquashBot");
        Mockito.when(robotMock4.getName()).thenReturn("TrundleBot");
        Mockito.when(robotMock5.getName()).thenReturn("Twnonkey");
        Mockito.when(robotMock6.getName()).thenReturn("Hammerbot");
        controlUnitInjected = (AIPlayer1) gaif.getAIPlayer(-1, "AIPlayer1", mockPlayer);
        controlUnitInjected.setVisual(visualMock);
        controlUnitInjected.setCurrent(currentMock);
        controlUnitInjected.setFuture(futureMock);
        controlUnitInjected.setResponse(responseMock);
        controlUnitInjected.setInit(true);
        
       
    }


    @Test
    public void shouldCallGetPlayerPlacement() {
        controlUnitInjected.getPlayerPlacement(anyInt());
        verify(visualMock).getPlayerPlacement(anyInt());
    }

    @Test
    public void shouldCallGetFlagPosition() {
        controlUnitInjected.getFlagPosition(anyInt());
        verify(visualMock).getFlagPosition(anyInt());
    }

    @Test
    public void shouldCallGetArea() {
        controlUnitInjected.getArea(anyObject(), anyInt());
        verify(visualMock).getArea(anyObject(), anyInt());
    }

    @Test
    public void shouldCallStartPlanning() {
        controlUnitInjected.startPlanning();
        verify(futureMock).startPlanning();
    }

    @Test
    public void shouldCallGetCurrentPlan() {
        controlUnitInjected.getCurrentPlan();
        verify(futureMock).getCurrentPlan();
    }

    @Test
    public void shouldCallPlanTurn() {
        controlUnitInjected.planTurn();
        verify(currentMock).planTurn();
    }

    @Test
    public void shouldCallSetCards() {
        controlUnitInjected.setCards(anyObject());
        verify(responseMock).setCards(anyObject());

    }

    @Ignore
    @Test
    public void shouldReturnDiscardedCardsAndDeletCardsFromHand() {
        List<ProgramCard> cardList = getDefaultCards();
        controlUnitInjected.giveCards(cardList);
        for (int i = 0; i < cardList.size(); i++) {
            assertEquals(cardList.get(i),controlUnitInjected.discardCards().get(i));
        }
        assertTrue(controlUnitInjected.getPlayerCards().isEmpty());
    }

    @Test
    public void shouldCallGetHP() {
        controlUnitInjected.getHP(anyInt());
        verify(visualMock).getHP(anyInt());
    }

    @Test
    public void shouldCallGetLives() {
        controlUnitInjected.getLives(anyInt());
        verify(visualMock).getLives(anyInt());
    }

    @Test
    public void shouldCallIsPoweredDown() {
        controlUnitInjected.isPoweredDown(anyInt());
        verify(visualMock).isPoweredDown(anyInt());
    }

    @Test
    public void shouldCallGetRespawnPoint() {
        controlUnitInjected.getRespawnPoint(anyInt());
        verify(visualMock).getRespawnPoint(anyInt());
    }

    @Test
    public void shouldCallGetLockedCards() {
        controlUnitInjected.getLockedCards(anyInt());
        verify(visualMock).getLockedCards(anyInt());
    }

    @Test
    public void shouldCallGetOptionCards() {
        controlUnitInjected.getOptionCards(anyInt());
        verify(visualMock).getOptionCards(anyInt());
    }

    @Test
    public void shouldCallGetCard() {
        controlUnitInjected.getCard(anyInt());
        verify(visualMock).getCard(anyInt());
    }

    @Ignore
    @Test
    public void shouldCallStartPlanningAndPlanTurn() {
        when(robotMock.getFlagCounterStatus()).thenReturn(0);
        when(robotMock.getRegisters()).thenReturn(getDefaultAnswer());
        controlUnitInjected.executeTurn();
        verify(futureMock).startPlanning();
        verify(currentMock).planTurn();
    }
    @Test(expected=RuntimeException.class)
    public void shouldThrowRuntimeException(){
        when(robotMock.getFlagCounterStatus()).thenReturn(0);
        when(robotMock.getRegisters()).thenReturn(getWrongAnswer());
        controlUnitInjected.executeTurn();
    }


    private List<ProgramCard> getDefaultCards(){
        List<ProgramCard> cards = new ArrayList<>();
        cards.add(card1);
        cards.add(card2);
        cards.add(card3);
        cards.add(card4);
        cards.add(card5);
        cards.add(card6);
        cards.add(card7);
        cards.add(card8);
        cards.add(card9);
        return cards;
    }
    @Test
    public void shouldCreateMapOfPlayerPlacements(){
        when(mFactoryMock.getMaster(anyInt())).thenReturn(masterMock);
        when(masterMock.getBoard()).thenReturn(boardMock);
        when(masterMock.getPlayers()).thenReturn(getPlayerList());
        when(visualMock.getPlayerPlacement(0)).thenReturn(place0);
        when(visualMock.getPlayerPlacement(1)).thenReturn(place1);
        when(visualMock.getPlayerPlacement(2)).thenReturn(place2);
        when(visualMock.getPlayerPlacement(3)).thenReturn(place3);
        when(visualMock.getPlayerPlacement(4)).thenReturn(place4);
        when(visualMock.getPlayerPlacement(5)).thenReturn(place5);
        controlUnitInjected.setMasterFactory(mFactoryMock);
        controlUnitInjected.initialize();
        controlUnitInjected.setVisual(visualMock);
        assertEquals(getPlayerList().size()-1, controlUnitInjected.getAllPlayerPlacement().size());
        
    }

    private ProgramCard[] getDefaultAnswer(){
        return new ProgramCard[]{card1,card2,card3,card4,card5};
    }

    private ProgramCard[] getWrongAnswer(){
        return new ProgramCard[]{card1,card2,card3,card4,null};
    }

    private List<Player> getPlayerList(){
        List<Player> pList = new ArrayList<>();
        pList.add(mockPlayer);
        pList.add(player2);
        pList.add(player3);
        pList.add(player4);
        pList.add(player5);
        pList.add(player6);
        return pList;
    }
    
}
