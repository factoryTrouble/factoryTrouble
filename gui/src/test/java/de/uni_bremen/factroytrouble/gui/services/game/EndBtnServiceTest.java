package de.uni_bremen.factroytrouble.gui.services.game;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import de.uni_bremen.factroytrouble.ai.AIPlayer;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.gui.controller.GameScreenController;
import de.uni_bremen.factroytrouble.gui.controller.PlayerInfoController;
import de.uni_bremen.factroytrouble.gui.services.GameEngineWrapper;
import de.uni_bremen.factroytrouble.player.Player;

@RunWith(MockitoJUnitRunner.class)
public class EndBtnServiceTest {

    @Mock
    private RegisterCardsService registerCardsService;
    @Mock
    private GameScreenController gameScreenController;
    @Mock
    private AlertService alertService;
    @Mock
    private GameEngineWrapper gameEngineWrapper;
    @Mock
    private HandCardService handCardService;
    @Mock
    private PerformRoundForPlayerService performRoundForPlayerService;
    @Mock
    private Player player;
    @Mock
    private Player player1;
    @Mock
    private Player player2;
    @Mock
    private AIPlayer player3;
    @Mock
    private AIPlayer aiPlayer;
    @Mock
    private PlayerInfoController playerInfoController;
    @Mock
    private AIExecutionService aiExecutionService;
    @Mock 
    private Robot robot;
    @InjectMocks
    private EndBtnService endBtnService = new EndBtnService();
    private List<Player> testList = new ArrayList<>();

    private void allPlayersDone() {
        testList.add(player1);

        when(registerCardsService.allRegisterCardsAreSet()).thenReturn(true);
        when(gameEngineWrapper.getPlayerByNumber(anyInt())).thenReturn(player);
        when(player.getRobot()).thenReturn(robot);
        when(player.isDone()).thenReturn(false);
        when(player.discardCards()).thenReturn(null);
        when(player.fillRegister(anyInt(),anyObject())).thenReturn(null);
        when(player.getPlayerCards()).thenReturn(null);
        when(robot.isPoweredDown()).thenReturn(false);
        when(gameEngineWrapper.getPlayerCount()).thenReturn(2);
        when(gameScreenController.getActivePlayer()).thenReturn(0);

        when(gameEngineWrapper.getPlayerByNumber(anyInt())).thenReturn(player1);
        when(player1.getRobot()).thenReturn(robot);
        List<PlayerInfoController> playerInfoControllers = new ArrayList<>();
        playerInfoControllers.add(playerInfoController);
        when(gameScreenController.getPlayerInfos()).thenReturn(playerInfoControllers);
    }

    private void allCardsChosen() {
        allPlayersDone();
        testList.add(player2);
    }

    @Test
    public void shouldCallShowNotAllCardsSetInPlayerForEndRound() {
        when(gameEngineWrapper.getPlayerByNumber(anyInt())).thenReturn(player);
        when(player.getRobot()).thenReturn(robot);
        when(player.isDone()).thenReturn(false);
        when(player.discardCards()).thenReturn(null);
        when(player.fillRegister(anyInt(),anyObject())).thenReturn(null);
        when(player.getPlayerCards()).thenReturn(null);
        when(robot.isPoweredDown()).thenReturn(false);
        endBtnService.endRound(0);
        verify(alertService).showNotAllCardsSet();
    }
    
    @Test
    public void shouldgetNextHumanPlayerAlive(){
        allCardsChosen();
        when(robot.getLives()).thenReturn(1);
        assertNotEquals(endBtnService.getNextHumanPlayer(0), -1);
        assertEquals(endBtnService.getNextHumanPlayerCount(0), 1);
    }

    @Test
    public void shouldCall_FinishTurnForPlayer_In_GameEngineWrapper_For_All_Cards_Set_And_HasNextHumanPlayer_True()
            throws Exception {
        allCardsChosen();
        when(robot.getLives()).thenReturn(1);
        endBtnService.endRound(0);
        verify(performRoundForPlayerService).start(anyInt());
    }

    @Test
    public void shouldCall_FinishTurnForPlayer_In_GameEngineWrapper_For_All_Cards_Set_And_HasNextHumanPlayer_False()
            throws Exception {
        allCardsChosen();
        when(gameEngineWrapper.getPlayerByNumber(anyInt())).thenReturn(aiPlayer);
        when(aiPlayer.getRobot()).thenReturn(robot);
        
        endBtnService.endRound(0);
        verify(aiExecutionService).endTurnForHumanPlayers();
    }

    @Test
    public void shouldCallGetPlayerByNumberInGameEngineWrapperForFinishTurnForPlayer() throws Exception {
        allCardsChosen();
        endBtnService.finishTurnForPlayer(0);
        verify(gameEngineWrapper, times(2)).getPlayerByNumber(0);
    }

    @Test
    public void shouldCallDropAllCardsInRegisterCardsServiceForFinishTurnForPlayer() throws Exception {
        allCardsChosen();
        endBtnService.finishTurnForPlayer(0);
        verify(registerCardsService).dropAllCards(0);
    }

    @Test
    public void shouldCallDropAllCardsInHandCardServiceForFinishTurnForPlayer() throws Exception {
        allCardsChosen();
        endBtnService.finishTurnForPlayer(0);
        verify(handCardService).dropAllCards();
    }

}
