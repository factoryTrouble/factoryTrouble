package de.uni_bremen.factroytrouble.gui.services.game;

import de.saxsys.javafx.test.JfxRunner;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;

import static org.mockito.Mockito.*;

import de.uni_bremen.factroytrouble.gui.controller.GameFieldController;
import de.uni_bremen.factroytrouble.gui.controller.GameScreenController;
import de.uni_bremen.factroytrouble.gui.controller.PlayerInfoController;
import de.uni_bremen.factroytrouble.gui.controller.RobotController;
import de.uni_bremen.factroytrouble.gui.controller.components.RespawnImageView;
import de.uni_bremen.factroytrouble.spring.SpringConfigHolder;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;

@RunWith(JfxRunner.class)
public class SaveGameStateServiceTest {

    @Mock
    private ApplicationContext context;
    @Mock
    private GameScreenController gameScreenController;
    @Mock
    private PlayerGridService playerGridService;
    @Mock
    private PerformRoundForPlayerService performRoundForPlayerService;
    @Mock
    private GameFieldController gameFieldController;
    @Mock
    private List<PlayerInfoController> playerInfoControllers;
    @Mock
    private AnchorPane anchorPane;
    @Mock
    private Map<String, RobotController> robots;
    @Mock
    private Map<String, RespawnImageView> respawns;
    @InjectMocks
    private SaveGameStateService saveGameStateService = new SaveGameStateService();
    
    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        SpringConfigHolder.getInstance().setContext(context);
        when(context.getBean(GameScreenController.class)).thenReturn(gameScreenController);
        when(gameFieldController.getRespawns()).thenReturn(respawns);
        when(gameFieldController.getRobots()).thenReturn(robots);
        when(gameFieldController.getAnchorPane()).thenReturn(anchorPane);
        when(gameScreenController.getActivePlayer()).thenReturn(0);
    }
    
    @Test
    public void shouldSaveGameState(){
        saveGameStateService.saveGameState(playerInfoControllers, gameFieldController);
        verify(gameFieldController).getRespawns();
        verify(gameFieldController).getRobots();
        verify(gameFieldController).getAnchorPane();
    }
    
    @Test
    public void shouldContinueGame(){
        ScrollPane scrlPane = Mockito.mock(ScrollPane.class);
        when(gameScreenController.getScrlPane()).thenReturn(scrlPane);
        saveGameStateService.continueGame();
        verify(gameScreenController).getScrlPane();
        verify(gameFieldController).setRespawns(respawns);
        verify(gameFieldController).setRobots(robots);
        verify(playerGridService).reclaimPlayerGrid(playerInfoControllers);
        verify(performRoundForPlayerService).start(anyInt());
    }
}
