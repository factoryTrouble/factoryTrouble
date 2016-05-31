package de.uni_bremen.factroytrouble.gui.services.game;

import de.saxsys.javafx.test.JfxRunner;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.gui.controller.GameScreenController;
import de.uni_bremen.factroytrouble.gui.controller.PlayerInfoController;
import de.uni_bremen.factroytrouble.gui.services.GameEngineWrapper;
import de.uni_bremen.factroytrouble.spring.SpringConfigHolder;
import de.uni_bremen.factroytrouble.player.Player;
import javafx.scene.control.ScrollPane;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(JfxRunner.class)
public class PlayerGridServiceTest {
    
    @Mock private AnnotationConfigApplicationContext context;
    @Mock private GameScreenController gameScreenController;
    @Mock private GameEngineWrapper gameEngineWrapper;
    @Mock private Player player;
    @Mock private Robot robot;
    @Mock private ScrollPane scrlPane;
    @Mock private PlayerInfoController playerInfoController;
    @InjectMocks private PlayerGridService playerGridService = new PlayerGridService();
    
    private List<Player> testList = new ArrayList<>();
    @Mock private List<PlayerInfoController> playerInfoControllers = new ArrayList<>();
    
    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        SpringConfigHolder.getInstance().setContext(context);
        when(context.getBean(GameEngineWrapper.class)).thenReturn(gameEngineWrapper);
        testList.add(player);
        when(gameEngineWrapper.getPlayers()).thenReturn(testList);
        when(player.getRobot()).thenReturn(robot);
        when(robot.getName()).thenReturn("as");
        when(gameScreenController.getPlayerInfos()).thenReturn(playerInfoControllers);
        when(gameScreenController.getPlayerScrlPane()).thenReturn(scrlPane);
        when(playerInfoControllers.get(0)).thenReturn(playerInfoController);
    }

    @Test@Ignore
    public void shouldInitPlayerGrid(){
        playerGridService.initPlayersGrid();
        verify(robot).getName();
//        verify(playerInfoControllers).add(anyInt(), any());
//        verify(scrlPane).setContent(any());
    }
    
}
