package de.uni_bremen.factroytrouble.gui.services.game;

import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.gui.controller.GameScreenController;
import de.uni_bremen.factroytrouble.gui.services.GameEngineWrapper;
import de.uni_bremen.factroytrouble.spring.SpringConfigHolder;
import de.uni_bremen.factroytrouble.player.Player;
import javafx.embed.swing.JFXPanel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PerformRoundForPlayerServiceTest {

    @Mock private HandCardService handCardService;
    @Mock private RegisterCardsService registerCardsService;
    @Mock private AlertService alertService;
    @Mock private GameEngineWrapper gameEngineWrapper;
    @Mock private GameScreenController gameScreenController;
    @Mock private Player player;
    @Mock private Robot robot;
    @Mock private AnnotationConfigApplicationContext context;

    @InjectMocks private PerformRoundForPlayerService performRoundForHumanService = new PerformRoundForPlayerService();

    @Before
    public void setUp(){
        SpringConfigHolder.getInstance().setContext(context);
        when(context.getBean(eq(GameEngineWrapper.class))).thenReturn(gameEngineWrapper);
        when(gameEngineWrapper.getPlayerByNumber(anyInt())).thenReturn(player);
        when(player.getRobot()).thenReturn(robot);
        when(robot.isPoweredDown()).thenReturn(false);
        new JFXPanel(); //ansonsten kann Platform.runLater nicht gestartet werde
    }

    @Test
    public void shouldCall_GetPlayerByNumber_gameEngineWrapper_In_Start(){
        performRoundForHumanService.start(0);
        verify(gameScreenController).setActivePlayer(0);
    }

    @Test
    public void shouldCall_AlertService_ShowNextPlayerAlert_In_Prepare(){
        performRoundForHumanService.prepare(0);
        verify(alertService).showNextPlayerAlert(0);
    }

    @Test
    public void shouldCall_HandCardService_ShowCards_In_Prepare(){
        performRoundForHumanService.prepare(0);
        verify(handCardService).showCards(0);
    }

    @Test
    public void shouldCall_RegisterCardService_InitRegisters_In_Prepare(){
        performRoundForHumanService.prepare(0);
        verify(registerCardsService).initRegisters(0);
    }
}

