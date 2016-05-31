package de.uni_bremen.factroytrouble.gui.services.game;

import de.saxsys.javafx.test.JfxRunner;
import de.uni_bremen.factroytrouble.gui.ApplicationSettings;
import de.uni_bremen.factroytrouble.gui.controller.AISimulateController;
import de.uni_bremen.factroytrouble.gui.controller.GameScreenController;
import de.uni_bremen.factroytrouble.gui.controller.NewGameScreenController;
import de.uni_bremen.factroytrouble.gui.observer.GUIEngineObserver;
import de.uni_bremen.factroytrouble.gui.services.GameEngineWrapper;
import de.uni_bremen.factroytrouble.gui.services.util.ApplicationArgumentsService;
import de.uni_bremen.factroytrouble.spring.SpringConfigHolder;
import de.uni_bremen.factroytrouble.player.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JfxRunner.class)
public class StartGameServiceTest {

    @Mock private GUIEngineObserver guiEngineObserver;
    @Mock private AnnotationConfigApplicationContext context;
    @Mock private NewGameScreenController newGameScreenController;
    @Mock private GameEngineWrapper gameEngineWrapper;
    @Mock private AlertService alertService;
    @Mock private Player player;
    @Mock private ApplicationArgumentsService applicationArgumentsService;
    @Mock private AISimulateController aiSimulateController;
    @InjectMocks private StartGameService startGameService;

    private List<Player> playerList = new ArrayList<>();

    @Before
    public void setUp() {
        startGameService = new StartGameService();
        MockitoAnnotations.initMocks(this);
        SpringConfigHolder.getInstance().setContext(context);
        when(context.getBean(eq(GameScreenController.class))).thenReturn(null);
    }

    @Test
    public void shouldCall_GameEngineWrapper_getPlayers_In_startGame() {
        noBoard();
        startGameService.startGame();
        verify(gameEngineWrapper).getPlayers();
    }

    @Test
    public void shouldCall_NewGameScreenController_getActiveBoard_In_startGame() {
        noBoard();
        startGameService.startGame();
        verify(newGameScreenController).getActiveBoard();
    }

    @Test
    public void shouldCall_GameEngineWrapper_GetPlayerCount_In_startGame() {
        boardChosenAndNotEnoughPlayers();
        startGameService.startGame();
        verify(newGameScreenController).getActiveBoard();
    }

    @Test
    public void shouldOpenTheAISimulateScreenWhenArgumentSetAtApplicationLaunch() {
        boardChosenAndEnoughPlayers();
        when(applicationArgumentsService.getArgumentValue(ApplicationSettings.AI_TEST_MODE)).thenReturn(true);
        startGameService.startGame();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        verify(aiSimulateController).getView();
    }

    private void noBoard() {
        String board = "";
        when(newGameScreenController.getActiveBoard()).thenReturn(board);
    }

    private void boardChosen() {
        String board = "test123";
        when(newGameScreenController.getActiveBoard()).thenReturn(board);
        when(gameEngineWrapper.getPlayers()).thenReturn(null);
    }

    private void boardChosenAndEnoughPlayers() {
        String board = "test123";
        when(newGameScreenController.getActiveBoard()).thenReturn(board);
        playerList.add(player);
        when(gameEngineWrapper.getPlayerCount()).thenReturn(2);
    }

    private void boardChosenAndNotEnoughPlayers() {
        String board = "test123";
        when(newGameScreenController.getActiveBoard()).thenReturn(board);
        playerList.add(player);
        when(gameEngineWrapper.getPlayerCount()).thenReturn(1);
    }

}
