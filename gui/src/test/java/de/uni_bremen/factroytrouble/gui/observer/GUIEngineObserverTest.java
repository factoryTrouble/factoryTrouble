package de.uni_bremen.factroytrouble.gui.observer;

import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.gui.controller.GameFieldController;
import de.uni_bremen.factroytrouble.gui.controller.GameScreenController;
import de.uni_bremen.factroytrouble.gui.controller.PlayerInfoController;
import de.uni_bremen.factroytrouble.gui.controller.RobotController;
import de.uni_bremen.factroytrouble.gui.controller.components.RespawnImageView;
import de.uni_bremen.factroytrouble.gui.services.game.AnimationService;
import de.uni_bremen.factroytrouble.gui.services.game.SoundService;
import de.uni_bremen.factroytrouble.observer.Event;
import de.uni_bremen.factroytrouble.player.GameMaster;
import de.uni_bremen.factroytrouble.spring.SpringConfigHolder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GUIEngineObserverTest {

    @Mock private AnnotationConfigApplicationContext context;
    @Mock private GameMaster gameMaster;
    @Mock private Robot robot;
    @Mock private Robot robot2;
    @Mock private GameScreenController gameScreenController;
    @Mock private PlayerInfoController playerInfoController;
    @Mock private GameFieldController gameFieldController;
    @Mock private Tile newRespawnPoint;
    @Mock private RespawnImageView respawn;
    @Mock private RobotController robotController;
    @Mock private Point point;
    @Mock private AnimationService animationService;
    @Mock private SoundService soundService;
    private GUIEngineObserver guiEngineObserver;

    @Before
    public void setUp() {
        SpringConfigHolder.getInstance().setContext(context);
        when(context.getBean(eq(GameMaster.class))).thenReturn(gameMaster);
        when(context.getBean(eq(GameScreenController.class))).thenReturn(gameScreenController);
        when(context.getBean(eq(PlayerInfoController.class))).thenReturn(playerInfoController);
        when(context.getBean(eq(GameFieldController.class))).thenReturn(gameFieldController);
        when(context.getBean(eq(SoundService.class))).thenReturn(soundService);
        when(context.getBean(eq(AnimationService.class))).thenReturn(animationService);
        when(context.getBean(eq(Robot.class))).thenReturn(robot);
        when(robot.getName()).thenReturn("hans");
        when(robot.getHP()).thenReturn(5);
        when(robot.getLives()).thenReturn(2);

        guiEngineObserver = new GUIEngineObserver();

        List<PlayerInfoController> playerInfoControllers = new ArrayList<PlayerInfoController>();
        playerInfoControllers.add(playerInfoController);
        when(gameScreenController.getPlayerInfos()).thenReturn(playerInfoControllers);
        when(playerInfoController.getName()).thenReturn("hans");
        when(robot.getRespawnPoint()).thenReturn(newRespawnPoint);
        when(robot.getCurrentTile()).thenReturn(newRespawnPoint);
        Map<String, RespawnImageView> respawns = new HashMap<String, RespawnImageView>();
        respawns.put("hans", respawn);
        when(gameFieldController.getRespawns()).thenReturn(respawns);
        Map<String, RobotController> robotControllers = new HashMap<String, RobotController>();
        robotControllers.put("hans", robotController);
        when(gameFieldController.getRobots()).thenReturn(robotControllers);
        when(newRespawnPoint.getCoordinates()).thenReturn(new Point(1,1));
        when(respawn.getPos()).thenReturn(point);
        when(newRespawnPoint.getAbsoluteCoordinates()).thenReturn(point);
        when(robot.getOrientation()).thenReturn(Orientation.NORTH);
    }

    @After
    public void tearDown() {
        SpringConfigHolder.getInstance().setContext(null);
    }

    @Test
    public void shouldUpdatePlayerInfoController() {
        guiEngineObserver.spam(robot, Event.SHOT, null);
       verify(playerInfoController).setHp(5);
       verify(playerInfoController).setLp(2);
    }
    
    @Test
    public void shouldTouchFlag(){
        guiEngineObserver.spam(robot, Event.TOUCH_FLAG, null);
        verify(playerInfoController).hitFlag(anyInt());
    }
    
    @Test
    public void shouldUpdateRespawn(){
        guiEngineObserver.spam(robot, Event.MOVED, null);
        verify(respawn).setPos(point, 0);
    }
    
    @Test
    public void shouldUpdateRobot(){
        guiEngineObserver.spam(robot, Event.MOVED, null);
        verify(robotController).refresh(point, Orientation.NORTH);
    }

    @Test
    public void shouldNotGetTheTheRobotFromTheFieldControllerWhenTheRobotHasNoTile() {
        when(robot.getCurrentTile()).thenReturn(null);
        guiEngineObserver.spam(robot, Event.OPTION_RECEIVED, null);
        verify(gameFieldController, never()).getRobots();
    }

    @Test
    public void shouldNotGetTheTheRobotFromTheFieldControllerWhenRobotNotExist() {
        when(robot.getCurrentTile()).thenReturn(null);
        when(robot.getName()).thenReturn("noRobotHasThisName");
        verify(gameFieldController, never()).getRobots();
    }
    
    @Test
    public void shouldTriggerKill(){
        guiEngineObserver.spam(robot, Event.KILLED, null);
        verify(animationService).kill(robot);
        verify(soundService).playSound(anyString());
    }
    
    @Test
    public void shouldTriggerHeal(){
        guiEngineObserver.spam(robot, Event.HEALED, null);
        verify(soundService).playSound(anyString());
    }
    
    @Test
    public void shouldTriggerRespawn(){
        guiEngineObserver.spam(robot, Event.RESPAWN, null);
        verify(animationService).respawn(robot);
    }
    
    @Test
    public void shouldTriggerTakeDamage(){
        guiEngineObserver.spam(robot, Event.SHOT, robot2);
        verify(animationService).laserShot(robot, robot2);
        verify(soundService).playSound(anyString());
    }
    
    @Test
    public void shouldTriggerTakeDamageNoAnimation(){
        guiEngineObserver.spam(robot, Event.SHOT, null);
        verify(animationService, never()).laserShot(robot, robot2);
        verify(soundService).playSound(anyString());
    }

}
