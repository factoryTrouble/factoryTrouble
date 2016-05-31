package de.uni_bremen.factroytrouble.gui.services.game;

import de.uni_bremen.factroytrouble.ai.AIPlayer;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.gui.controller.GameScreenController;
import de.uni_bremen.factroytrouble.gui.controller.PlayerInfoController;
import de.uni_bremen.factroytrouble.gui.services.GameEngineWrapper;
import de.uni_bremen.factroytrouble.spring.PostConstructTaskScheduler;
import de.uni_bremen.factroytrouble.player.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static de.uni_bremen.factroytrouble.gui.TestUtil.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AIExecutionServiceTest {

    @Mock private GameEngineWrapper gameEngineWrapper;
    @Mock private AlertService alertService;
    @Mock private PostConstructTaskScheduler postConstructTaskScheduler;
    @Mock private GameScreenController gameScreenController;
    @InjectMocks private AIExecutionService aiExecutionService;

    @Mock private AIPlayer aiPlayer;
    @Mock private Player player;
    @Mock private Robot robot;
    @Mock private PlayerInfoController playerInfoController;

    private List<PlayerInfoController> playerInfoControllerList;
    private List<Player> players;
    private Map<String, Boolean> aiReadyMap;

    @Before
    public void setUp() throws Exception{
        playerInfoControllerList = new ArrayList<>();
        playerInfoControllerList.add(playerInfoController);

        players = new ArrayList<>();
        players.add(player);

        aiReadyMap = new HashMap<>();
        injectPrivateField("aiReadyMap", aiExecutionService, aiReadyMap);

        when(aiPlayer.getRobot()).thenReturn(robot);
        when(robot.getName()).thenReturn("test");
        when(robot.getLives()).thenReturn(1);
        when(gameEngineWrapper.getPlayerIndexInPlayersByName(anyString())).thenReturn(0);
        when(gameScreenController.getPlayerInfos()).thenReturn(playerInfoControllerList);
        when(gameEngineWrapper.getPlayers()).thenReturn(players);
    }

    @Test
    public void shouldExecuteTheAI() throws Exception {
        injectPrivateField("aiReadyMap", aiExecutionService, new HashMap<String, Boolean>());
        callPrivateMethodeWithParameter("startAIExecution", aiExecutionService, new Class[]{AIPlayer.class}, aiPlayer);
        verify(aiPlayer).executeTurn();
    }

    @Test
    public void shouldSetAIFinshed() throws Exception {
        injectPrivateField("aiReadyMap", aiExecutionService, new HashMap<String, Boolean>());
        callPrivateMethodeWithParameter("startAIExecution", aiExecutionService, new Class[]{AIPlayer.class}, aiPlayer);
        verify(playerInfoController).setFinished(true);
    }

    @Test
    public void shouldThrowAnAlertWhenAnAIIsNotReady() throws Exception {
        aiReadyMap.put("otherTestRobot", false);
        injectPrivateField("waitForAIs", aiExecutionService, true);
        callPrivateMethodeWithParameter("startAIExecution", aiExecutionService, new Class[]{AIPlayer.class}, aiPlayer);
        verify(alertService).showAiNotReadyAlert();
    }

    @Test
    public void shouldThrowAnRoundEndAlertWhenAllAIsAreReady() throws Exception {
        aiReadyMap.put("otherTestRobot", true);
        injectPrivateField("waitForAIs", aiExecutionService, true);
        callPrivateMethodeWithParameter("startAIExecution", aiExecutionService, new Class[]{AIPlayer.class}, aiPlayer);
        verify(alertService).showEndRoundAlert();
    }

    @Test
    public void shouldNotThrowAnyAlertWhenNotWaitingForAIs () throws Exception {
        aiReadyMap.put("otherTestRobot", false);
        callPrivateMethodeWithParameter("startAIExecution", aiExecutionService, new Class[]{AIPlayer.class}, aiPlayer);
        verify(alertService, never()).showAiNotReadyAlert();
        verify(alertService, never()).showEndRoundAlert();
    }

    @Test
    public void shouldCheckIfAllAIsAreReadyWhenHumanPlayerIsReady() throws Exception {
        aiReadyMap.put("otherTestRobot", true);
        aiExecutionService.endTurnForHumanPlayers();
        verify(alertService).showEndRoundAlert();
    }

    @Test
    public void shouldExecuteAllAIs() throws Exception {
        players.add(aiPlayer);
        injectPrivateField("aiReadyMap", aiExecutionService, new HashMap<String, Boolean>());
        aiExecutionService.executeAIs();
        verify(aiPlayer, timeout(1000)).executeTurn();
    }

    @Test
    public void shouldNotExecuteAllAIsWhenNoAIIsInThePlayerList() throws Exception {
        injectPrivateField("aiReadyMap", aiExecutionService, new HashMap<String, Boolean>());
        aiExecutionService.executeAIs();
        verify(aiPlayer, never()).executeTurn();
    }

}