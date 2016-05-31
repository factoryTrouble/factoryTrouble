package de.uni_bremen.factroytrouble.gui.controller;

import de.saxsys.javafx.test.JfxRunner;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.gui.controller.components.RobotImageView;
import de.uni_bremen.factroytrouble.gui.generator.board.BoardConverterService;
import de.uni_bremen.factroytrouble.gui.services.GameEngineWrapper;
import de.uni_bremen.factroytrouble.spring.SpringConfigHolder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.awt.Point;

import static de.uni_bremen.factroytrouble.gui.TestUtil.injectPrivateField;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(JfxRunner.class)
public class RobotControllerTest {

    @Mock private AnnotationConfigApplicationContext context;
    @Mock private GameEngineWrapper gameEngineWrapper;
    @Mock private RobotImageView robotImageView;
    @Mock private Point point;
    @Mock private Point point2;
    @Mock private BoardConverterService boardConverterService;
    private RobotController robotController;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        SpringConfigHolder.getInstance().setContext(context);
        when(context.getBean(eq(GameEngineWrapper.class))).thenReturn(gameEngineWrapper);
        when(context.getBean(eq(BoardConverterService.class))).thenReturn(boardConverterService);
        when(gameEngineWrapper.getPlayerNumberByName(anyString())).thenReturn(0);
        
        robotController = new RobotController("test");
        injectPrivateField("robotImageView", robotController, robotImageView);

        when(boardConverterService.getMaxY()).thenReturn(0);
    }

    @Test
    public void shouldSetThePositionForTheFirstCall() {
        robotController.refresh(new Point(0,0), Orientation.NORTH);
        assertEquals(new Point(0,0), robotController.getPosition());
    }

    @Test
    public void shouldSetThePositionInsideTheRobotImageView() {
        robotController.refresh(new Point(0,0), Orientation.NORTH);
        verify(robotImageView).setPos(any(Point.class));
    }

    @Test
    public void shouldSetTheOrientationInsideTheRobotImageView() {
        robotController.refresh(new Point(0,0), Orientation.NORTH);
        verify(robotImageView).setOrientation(Orientation.NORTH);
    }

    @Test
    public void shouldNotChangeTheImageViewWhenNoChangeDetacted() throws Exception {
        injectPrivateField("position", robotController, new Point(0,0));
        injectPrivateField("orientation", robotController, Orientation.NORTH);
        robotController.refresh(new Point(0,0), Orientation.NORTH);

        verify(robotImageView, never()).setOrientation(Orientation.NORTH);
        verify(robotImageView, never()).setPos(any(Point.class));
    }

    @Test
    public void shouldChangeTheImageViewWhenOrientationChnage() throws Exception {
        injectPrivateField("position", robotController, new Point(0,0));
        injectPrivateField("orientation", robotController, Orientation.SOUTH);
        robotController.refresh(new Point(0,0), Orientation.NORTH);

        verify(robotImageView, timeout(1000)).setOrientation(Orientation.NORTH);
        verify(robotImageView, timeout(1000)).setPos(any(Point.class));
    }

    @Test
    public void shouldChangeTheImageViewWhenPositionChnage() throws Exception {
        injectPrivateField("position", robotController, new Point(0,1));
        injectPrivateField("orientation", robotController, Orientation.NORTH);
        robotController.refresh(new Point(0,0), Orientation.NORTH);

        verify(robotImageView, timeout(1000)).setOrientation(Orientation.NORTH);
        verify(robotImageView, timeout(1000)).setPos(any(Point.class));
    }

    @Test
    public void shouldSetTheZommInsideTheRobotImageView() {
        robotController.setZoom(1.0);
        verify(robotImageView).setZoom(1.0);
    }

}