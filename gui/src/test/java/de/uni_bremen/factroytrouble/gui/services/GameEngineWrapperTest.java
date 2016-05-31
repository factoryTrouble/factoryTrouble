package de.uni_bremen.factroytrouble.gui.services;

import de.saxsys.javafx.test.JfxRunner;
import de.uni_bremen.factroytrouble.ai.AIPlayer;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.gui.controller.GameScreenController;
import de.uni_bremen.factroytrouble.gui.controller.PlayerInfoController;
import de.uni_bremen.factroytrouble.gui.services.game.AIExecutionService;
import de.uni_bremen.factroytrouble.gui.services.game.EndBtnService;
import de.uni_bremen.factroytrouble.gui.services.game.PerformRoundForPlayerService;
import de.uni_bremen.factroytrouble.observer.GameObserver;
import de.uni_bremen.factroytrouble.player.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(JfxRunner.class)
public class GameEngineWrapperTest {
    private static final int GAME_ID = 0;

    @Mock private MasterFactory masterFactory;
    @Mock private PlayerFactory playerFactory;
    @Mock private Master master;
    @Mock private Player player;
    @Mock private AIPlayer aiPlayer;
    @Mock private ProgramCard programCard;
    @Mock private Robot robot;
    @Mock private GameObserver gameObserver;
    @Mock private GameScreenController gameScreenController;
    @Mock private AIExecutionService aiExecutionService;
    @Mock private PerformRoundForPlayerService performRoundForPlayerService;
    @Mock private EndBtnService endBtnService;
    @Mock private PlayerInfoController playerInfoController;
    @InjectMocks private GameEngineWrapper gameEngineWrapper = new GameEngineWrapper();
    private List<Player> players;
    private List<ProgramCard> programCards;
    private List<PlayerInfoController> playerInfoControllers;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        players = new ArrayList<>();
        players.add(player);
        players.add(aiPlayer);
        programCards = new ArrayList<>();
        programCards.add(programCard);
        playerInfoControllers = new ArrayList<>();
        playerInfoControllers.add(playerInfoController);

        when(masterFactory.getMaster(GAME_ID)).thenReturn(master);
        when(master.getPlayers()).thenReturn(players);
        when(player.getPlayerCards()).thenReturn(programCards);
        when(player.getRobot()).thenReturn(robot);

        when(aiPlayer.getRobot()).thenReturn(robot);

        when(playerFactory.createNewPlayer(anyInt(), anyString(), anyString())).thenReturn(player);
        when(master.getWinner()).thenReturn(player);
        when(robot.getRegisters()).thenReturn(new ProgramCard[1]);
        when(gameScreenController.getPlayerInfos()).thenReturn(playerInfoControllers);
    }

    @Test
    public void shouldGetThePlayerListFromMaster() {
        Player testplayer = gameEngineWrapper.getPlayerByNumber(0);
        assertEquals(player, testplayer);
    }

    @Test
    public void shouldGetThePlayerByNumber() {
        assertEquals(player, gameEngineWrapper.getPlayerByNumber(0));
    }

    @Test
    public void shouldCreateAPlayer() {
        gameEngineWrapper.createPlayer("test", "test");
        verify(playerFactory).createNewPlayer(GAME_ID, "test", "test");
    }

    @Test
    public void schouldAddANewPlayerIntoTheGameMaster() {
        gameEngineWrapper.createPlayer("test", "test");
        verify(master).addPlayer(player);
    }

    @Test
    public void shouldGetTheHPOfAnRobot() {
        gameEngineWrapper.getPlayerHP(0);
        verify(robot).getHP();
    }

    @Test
    public void shouldGetAProgrammCardForAPlayer() {
        assertEquals(programCard, gameEngineWrapper.getCard(0, 0));
        verify(player).getPlayerCards();
    }

    @Test
    public void shouldEmptyACardRegister() {
        gameEngineWrapper.emptyRegister(0, 0);
        verify(player).emptyRegister(0);
    }

    @Test
    public void shouldSetAProgrammCard() {

        gameEngineWrapper.fillRegister(0, 0, programCard);
        verify(player).fillRegister(eq(0), eq(programCard));
    }

    @Test
    public void shouldGetTheLockedRegisters() {
        gameEngineWrapper.getLockedRegisters(0);
        verify(robot).registerLockStatus();
    }

    @Test
    public void shouldGetThePlayerCount() {
        assertEquals(2, gameEngineWrapper.getPlayerCount());
    }

    @Test
    public void shouldStartToEvaluateAndActivateTheBoard() {
        gameEngineWrapper.startRound();
        verify(master).activateBoard();
    }

    @Test
    public void shouldStartToEvaluateTheBoardAndActivateTheBoard() {
        gameEngineWrapper.startRound();
        verify(master).activateBoard();
    }

    @Test
    public void shouldStartToEvaluateTheBoardAndCleanUp() {

        gameEngineWrapper.startRound();
        verify(master).cleanup();
    }

    @Test
    public void shouldStartToEvaluateTheBoardAndGetTheWinner() {
        gameEngineWrapper.startRound();
        verify(master).getWinner();
    }

    @Test
    public void shouldGetAllPlayers() {
        assertEquals(players, gameEngineWrapper.getPlayers());
    }

    @Test
    public void shouldGetTheProgrammCardsForAPlayer() {
        assertEquals(programCards, gameEngineWrapper.getCards(0));
    }

    @Test
    public void shouldAttachAnObserver() {
        gameEngineWrapper.attachObserver(gameObserver);
        verify(master).attachObserver(gameObserver);
    }

    @Test
    public void shouldGetTheCurrentBoard() {
        gameEngineWrapper.getActiveBoard();
        verify(master).getBoard();
    }

    @Test
    public void shouldGetABoardByName() {
        gameEngineWrapper.getBoard("test");
        verify(master).initialiseBoard("test");
        verify(master).getBoard();
    }

    @Test
    public void shouldDealCards() {
        gameEngineWrapper.dealCards();
        verify(master).dealCardsToPlayers();
    }

    @Test
    public void shouldGetAvailableBoards() {
        gameEngineWrapper.getAvailableBoards();
        verify(master).getAvailableBoards();
    }

    @Test
    public void shouldGetTheWinner() {
        gameEngineWrapper.getWinner();
        verify(master).getWinner();
    }

    @Test
    public void shouldGetACardFromAPlayerRegister() {
        gameEngineWrapper.getCardInRegister(0,0);
        verify(robot).getRegisters();
    }

    @Test
    public void shouldSetAllPlayerNotFinishedWhilePerpareTheRound() {
        gameEngineWrapper.prepareRound();
        verify(playerInfoController).setFinished(false);
    }

    @Test
    public void shouldExecuteTheAIs() {
        gameEngineWrapper.prepareRound();
        verify(aiExecutionService).executeAIs();
    }

    @Test
    public void shouldPerformTheRoundForTheFirstPlayerWhilePrepareingTheRound() {
        when(endBtnService.hasNextHumanPlayer(-1)).thenReturn(true);
        gameEngineWrapper.prepareRound();
        verify(performRoundForPlayerService).start(anyInt());
    }

    @Test
    public void shouldInitializeTheBoard() {
        gameEngineWrapper.initialiseBoard("test");
        verify(master).initialiseBoard("test");
    }

    @Test
    public void shouldRemoveAllPlayers() {
        gameEngineWrapper.removeAllPlayers();
        verify(master).removeAllPlayers();
    }

    @Test
    public void shouldGetTheIndexForAPlayerName() {
        when(robot.getName()).thenReturn("test");
        assertEquals(0,gameEngineWrapper.getPlayerIndexInPlayersByName("test"));
    }

    @Test
    public void shouldGetAUndefinedIndexForAPlayerNameWhenRobotDoNotExist() {
        when(robot.getName()).thenReturn("test");
        assertEquals(-1,gameEngineWrapper.getPlayerIndexInPlayersByName("noName"));
    }

}