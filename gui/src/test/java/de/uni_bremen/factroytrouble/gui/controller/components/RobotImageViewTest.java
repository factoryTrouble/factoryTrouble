package de.uni_bremen.factroytrouble.gui.controller.components;

import de.saxsys.javafx.test.JfxRunner;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.gui.generator.board.BoardConverterService;
import de.uni_bremen.factroytrouble.spring.SpringConfigHolder;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;

import java.awt.Point;

import static de.uni_bremen.factroytrouble.gui.TestUtil.assertImageEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Created by johannes.gesenhues on 21.01.16.
 */
@RunWith(JfxRunner.class)
public class RobotImageViewTest {
    @Mock private BoardConverterService boardConverterService;
    @Mock private ApplicationContext applicationContext;
    private RobotImageView robotImageView;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        SpringConfigHolder.getInstance().setContext(applicationContext);
        when(applicationContext.getBean(BoardConverterService.class)).thenReturn(boardConverterService);
        when(boardConverterService.getMaxY()).thenReturn(50);

        robotImageView = new RobotImageView(1);

    }

    @Test
    public void shouldCreateRespawnImageViewWithCorrectSettings() {
        assertTrue(robotImageView.getFitWidth() == RobotImageView.ROBOT_SIZE);
        assertTrue(robotImageView.isPreserveRatio());
        Image image = new Image("/game/robots/top_" + 1 + ".png");
        assertImageEquals(SwingFXUtils.fromFXImage(robotImageView.getImage(), null),SwingFXUtils.fromFXImage(image, null));

    }
    
    @Test
    public void shouldRespawn(){
        robotImageView.respawn();
        Image image = new Image("/game/robots/top_" + 1 + ".png");
        assertImageEquals(SwingFXUtils.fromFXImage(robotImageView.getImage(), null),SwingFXUtils.fromFXImage(image, null));

    }
    
    @Test
    public void shouldKill(){
        robotImageView.kill();
        Image image = new Image("/game/robots/explosion.png");
        assertImageEquals(SwingFXUtils.fromFXImage(robotImageView.getImage(), null),SwingFXUtils.fromFXImage(image, null));

    }

    @Test
    public void shouldChangeFitWidthAfterSetZoomWith100() {
        assertTrue((int)robotImageView.getFitWidth() == RobotImageView.ROBOT_SIZE);
        robotImageView.setZoom(100);
        assertTrue((int)robotImageView.getFitWidth() == RobotImageView.ROBOT_SIZE * 100);
    }

    @Test
    public void shouldChangePosAfterSetPos() {
            assertTrue(robotImageView.getX() == 0);
            assertTrue(robotImageView.getY() == 0);
            robotImageView.setZoom(1);
            robotImageView.setPos(new Point(1,1));
            assertTrue(robotImageView.getX() == 150.0);
            assertTrue(robotImageView.getY() == 7350.0);
    }

    @Test
    public void shouldChangeRotationAfterSetOrientationWithSouth() {
        assertTrue((int)robotImageView.getRotate() == 0);
        robotImageView.setOrientation(Orientation.SOUTH);
        assertTrue((int)robotImageView.getRotate() == 180);
    }

    @Test
    public void shouldChangeRotationAfterSetOrientationWithNorth() {
        assertTrue((int)robotImageView.getRotate() == 0);
        robotImageView.setOrientation(Orientation.NORTH);
        assertTrue((int)robotImageView.getRotate() == 0);
    }

    @Test
    public void shouldChangeRotationAfterSetOrientationWithWest() {
        assertTrue((int)robotImageView.getRotate() == 0);
        robotImageView.setOrientation(Orientation.WEST);
        assertTrue((int)robotImageView.getRotate() == 270);
    }

    @Test
    public void shouldChangeRotationAfterSetOrientationWithEast() {
        assertTrue((int)robotImageView.getRotate() == 0);
        robotImageView.setOrientation(Orientation.EAST);
        assertTrue((int)robotImageView.getRotate() == 90);
    }

}