package de.uni_bremen.factroytrouble.gui.controller;

import de.saxsys.javafx.test.JfxRunner;
import de.uni_bremen.factroytrouble.gui.TestUtil;
import de.uni_bremen.factroytrouble.gui.controller.components.RespawnImageView;
import de.uni_bremen.factroytrouble.gui.controller.components.RobotImageView;
import de.uni_bremen.factroytrouble.gui.testrunner.JavaFXControllerTestRunner;
import javafx.beans.property.DoubleProperty;
import javafx.scene.control.ScrollPane;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.imageio.ImageIO;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

@RunWith(JfxRunner.class)
public class GameFieldControllerTest extends JavaFXControllerTestRunner{
    
    @Mock private PreviewGameFieldController previewGameFieldController;
    @Mock private ScrollPane scrlPane;
    @Mock private RobotController robotController;
    @Mock private RespawnImageView respawnImageView;
    @Mock private DoubleProperty zoom;
    @Mock private RobotImageView robotImageView;
    @Mock private Point point;
    @InjectMocks private GameFieldController gameFieldController = new GameFieldController();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Map<String, RobotController> robots = new HashMap<String, RobotController>();
        robots.put("robot1", robotController);
        when(previewGameFieldController.getRobots()).thenReturn(robots);
        Map<String, RespawnImageView> respawns = new HashMap<String, RespawnImageView>();
        respawns.put("robot1", respawnImageView);
        when(previewGameFieldController.getRespawns()).thenReturn(respawns);
        when(zoom.get()).thenReturn(5D);
        when(robotController.getRobotImageView()).thenReturn(robotImageView);
        when(robotController.getPosition()).thenReturn(point);
        when(respawnImageView.getPos()).thenReturn(point);
        when(previewGameFieldController.getImage()).thenReturn(ImageIO.read(getClass().getResourceAsStream("/game/testBoard.png")));
    }
    
    @Test
    public void shouldSetUp(){
        gameFieldController.setUp(scrlPane, " ");
        verify(previewGameFieldController).getRobots();
        verify(previewGameFieldController).getRespawns();
        verify(robotController, times(1)).setZoom(anyDouble());
        verify(robotImageView, times(1)).setPos(point);
        verify(respawnImageView, times(1)).setZoom(anyDouble());
        verify(respawnImageView, times(1)).setPos(point, 0);
    }

    @Test
    public void shouldGetImage(){
        gameFieldController.getImage(" ");
        verify(previewGameFieldController).getImage();
    }

    @Test
    @Ignore
    public void shouldZoomWith100() throws NoSuchFieldException, IllegalAccessException {
        gameFieldController.zoom(100);
        System.out.println(TestUtil.getPrivateField("boardHeight",gameFieldController));
//        assertTrue(TestUtil.getPrivateField("zoom",gameFieldController)==
//        verify(previewGameFieldController).getImage();
    }

}
