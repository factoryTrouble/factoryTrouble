package de.uni_bremen.factroytrouble.gui.controller;

import de.saxsys.javafx.test.JfxRunner;
import de.uni_bremen.factroytrouble.ai.AIPlayer;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.gui.TestUtil;
import de.uni_bremen.factroytrouble.gui.services.GameEngineWrapper;
import de.uni_bremen.factroytrouble.gui.services.game.AlertService;
import de.uni_bremen.factroytrouble.player.Master;
import de.uni_bremen.factroytrouble.player.Player;
import javafx.scene.control.Label;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(JfxRunner.class)
public class AISimulateControllerTest {

    @Mock private GameEngineWrapper gameEngineWrapper;
    @Mock private MenuScreenController menuScreenController;
    @Mock private AlertService alertService;
    @InjectMocks private AISimulateController aiSimulateController;

    @Mock private Master master;
    @Mock private AIPlayer player;
    @Mock private Robot robot;

    private Label currentGameLabel;
    private Label currentRoundLabel;

    @Before
    public void setUp() throws Exception {
        aiSimulateController = new AISimulateController();
        MockitoAnnotations.initMocks(this);
        when(gameEngineWrapper.getMaster()).thenReturn(master);
        when(player.getRobot()).thenReturn(robot);
        when(robot.getName()).thenReturn("Test");
        currentGameLabel = new Label();
        currentRoundLabel = new Label();
        TestUtil.injectPrivateField("currentGameLabel", aiSimulateController, currentGameLabel);
        TestUtil.injectPrivateField("currentRoundLabel", aiSimulateController, currentRoundLabel);
    }

    @Test
    public void shouldStartAGivenTimesTheGame() throws Exception {
        aiSimulateController.setGameCountAsString("10");
        when(gameEngineWrapper.getWinner()).thenReturn(player);
        TestUtil.injectPrivateField("aiClazzes", aiSimulateController, new ArrayList<>());
        TestUtil.injectPrivateField("robotNames", aiSimulateController, new ArrayList<>());

        TestUtil.callPrivateMethodeWithParameter("gameLoop", aiSimulateController, new Class[]{String.class, Integer.class}, "testBoard", 1);
        verify(gameEngineWrapper, times(10)).initMaster();
    }

    @Test
    public void shouldLoopTheRounds() throws Exception {
        when(gameEngineWrapper.getWinner()).thenReturn(null, null, player);
        TestUtil.callPrivateMethodeWithoutParameter("playGames", aiSimulateController);
        verify(gameEngineWrapper, times(2)).dealCards();
    }

    @Test
    public void shouldStartAnAIWhenItLives() throws Exception {
        initGameEngineWrapperWithPlayerList();
        when(robot.getLives()).thenReturn(1);
        TestUtil.callPrivateMethodeWithoutParameter("startAIs", aiSimulateController);
        verify(player).executeTurn();
    }

    @Test
    public void shouldNotStartAnAIWhenItDiedAlready() throws Exception {
        initGameEngineWrapperWithPlayerList();
        when(robot.getLives()).thenReturn(0);
        TestUtil.callPrivateMethodeWithoutParameter("startAIs", aiSimulateController);
        verify(player, never()).executeTurn();
    }

    private void initGameEngineWrapperWithPlayerList() {
        List<Player> players = new ArrayList<>();
        players.add(player);
        when(gameEngineWrapper.getPlayers()).thenReturn(players);
    }

}