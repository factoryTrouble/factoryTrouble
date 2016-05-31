package de.uni_bremen.factroytrouble.gui.services.game;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;

import static org.mockito.Mockito.*;

import java.awt.Point;
import java.util.Map;

import de.saxsys.javafx.test.JfxRunner;
import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.gui.controller.GameFieldController;
import de.uni_bremen.factroytrouble.gui.controller.RobotController;
import de.uni_bremen.factroytrouble.gui.controller.components.RespawnImageView;
import de.uni_bremen.factroytrouble.gui.controller.components.RobotImageView;
import de.uni_bremen.factroytrouble.gui.generator.board.BoardConverterService;
import de.uni_bremen.factroytrouble.spring.SpringConfigHolder;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

@RunWith(JfxRunner.class)
public class AnimationServiceTest {

    @Mock private GameFieldController gameFieldController;
    @Mock private ApplicationContext context;
    @Mock private Robot robot1;
    @Mock private Robot robot2;
    @Mock private Map<String, RobotController> robots;
    @Mock private Map<String, RespawnImageView> respawns;
    @Mock private RobotController robotController;
    @Mock private RobotImageView robotImageView;
    @Mock private RespawnImageView respawnImageView;
    @Mock private AnchorPane anchorPane;
    @Mock private DoubleProperty zoom;
    @Mock private Tile tile;
    @Mock private ObservableList<Node> observableList;
    @Mock private BoardConverterService boardConverterService;
    @InjectMocks private AnimationService animationService = new AnimationService();
    
    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        SpringConfigHolder.getInstance().setContext(context);
        when(context.getBean(GameFieldController.class)).thenReturn(gameFieldController);
        when(gameFieldController.getRobots()).thenReturn(robots);
        when(robots.get(any())).thenReturn(robotController);
        when(robotController.getRobotImageView()).thenReturn(robotImageView);
        when(gameFieldController.getRespawns()).thenReturn(respawns);
        when(respawns.get(any())).thenReturn(respawnImageView);
        when(gameFieldController.getAnchorPane()).thenReturn(anchorPane);
        when(gameFieldController.getZoom()).thenReturn(zoom);
        when(zoom.get()).thenReturn(1D);
        when(robot1.getCurrentTile()).thenReturn(tile);
        when(robot2.getCurrentTile()).thenReturn(tile);
        when(anchorPane.getChildren()).thenReturn(observableList);
        when(boardConverterService.getMaxY()).thenReturn(500);
    }
    
    @Test
    public void shouldKillAndRespawn(){
        when(robot1.getLives()).thenReturn(1);
        animationService.kill(robot1);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        verify(robotImageView).kill();
    }
    
    @Test
    public void shouldKillAndNotRespawn(){
        when(robot1.getLives()).thenReturn(0);
        animationService.kill(robot1);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        verify(robotImageView).kill();
        verify(observableList).remove(robotImageView);
        verify(observableList).remove(respawnImageView);
    }
    
    @Test
    public void shouldRespawn(){
        animationService.respawn(robot1);
        verify(robotImageView).respawn();
    }
    
    @Test
    public void shouldVerticalLaser(){
        when(tile.getAbsoluteCoordinates()).thenReturn(new Point(1,1), new Point(1,5), new Point(1,1), new Point(1, 5));
        animationService.init();
        animationService.laserShot(robot1, robot2);
        try {
            Thread.sleep(500);      //nötig, damit platfomThread hinterherkommt
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        verify(observableList, times(3)).add(any());
        verify(observableList, times(3)).remove(any());
    }
    
    @Test
    public void shouldVerticalLaserSouth(){
        when(tile.getAbsoluteCoordinates()).thenReturn(new Point(1,1), new Point(1,5), new Point(1,1), new Point(1, 5));
        when(robot2.getOrientation()).thenReturn(Orientation.SOUTH);
        animationService.init();
        animationService.laserShot(robot1, robot2);
        try {
            Thread.sleep(500);      //nötig, damit platfomThread hinterherkommt
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        verify(observableList, times(3)).add(any());
        verify(observableList, times(3)).remove(any());
    }
    
    @Test
    public void shouldHorizontalLaser(){
        when(tile.getAbsoluteCoordinates()).thenReturn(new Point(1,1), new Point(5,1), new Point(1,1), new Point(5, 1));
        animationService.init();
        animationService.laserShot(robot1, robot2);
        try {
            Thread.sleep(500);      //nötig, damit platfomThread hinterherkommt
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        verify(observableList, times(3)).add(any());
        verify(observableList, times(3)).remove(any());
    }
    
    @Test
    public void shouldHorizontalLaserWest(){
        when(tile.getAbsoluteCoordinates()).thenReturn(new Point(1,1), new Point(5,1), new Point(1,1), new Point(5, 1));
        when(robot2.getOrientation()).thenReturn(Orientation.WEST);
        animationService.init();
        animationService.laserShot(robot1, robot2);
        try {
            Thread.sleep(500);      //nötig, damit platfomThread hinterherkommt
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        verify(observableList, times(3)).add(any());
        verify(observableList, times(3)).remove(any());
    }
}
