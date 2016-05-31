package de.uni_bremen.factroytrouble.gui.controller.components;

import de.uni_bremen.factroytrouble.gui.generator.board.BoardConverterService;
import de.uni_bremen.factroytrouble.spring.SpringConfigHolder;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;

import java.awt.Point;

import static de.uni_bremen.factroytrouble.gui.TestUtil.assertImageEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Created by johannes.gesenhues on 21.01.16.
 */
public class RespawnImageViewTest {
    @Mock private BoardConverterService boardConverterService;
    @Mock private ApplicationContext applicationContext;
    private RespawnImageView respawnImageView;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        SpringConfigHolder.getInstance().setContext(applicationContext);
        when(applicationContext.getBean(BoardConverterService.class)).thenReturn(boardConverterService);
        when(boardConverterService.getMaxY()).thenReturn(50);

        respawnImageView = new RespawnImageView(1);

    }

    @Test
    public void shouldCreateRespawnImageViewWithCorrectSettings() {
        assertTrue(respawnImageView.getFitWidth() == RespawnImageView.ROBOT_SIZE);
        assertTrue(respawnImageView.isPreserveRatio());
        Image image = new Image("/game/robots/side_" + 1 + ".png");
        assertImageEquals(SwingFXUtils.fromFXImage(respawnImageView.getImage(), null),SwingFXUtils.fromFXImage(image, null));

    }

    @Test
    public void shouldChangeFitWidthAfterSetZoomWith100() {
        assertTrue(respawnImageView.getFitWidth() == RespawnImageView.ROBOT_SIZE);
        respawnImageView.setZoom(100);
        assertTrue(respawnImageView.getFitWidth() == RespawnImageView.ROBOT_SIZE * 100);
    }

    @Test
    public void shouldChangePosAfterSetPos() {
        assertTrue(respawnImageView.getX() == 0);
        assertTrue(respawnImageView.getY() == 0);
        respawnImageView.setZoom(1);
        respawnImageView.setPos(new Point(1,1),5);
        System.out.println(respawnImageView.getX());
        System.out.println(respawnImageView.getY());
        assertTrue(respawnImageView.getX() == 300.0);
        assertTrue(respawnImageView.getY() == 7350.0);
    }

}