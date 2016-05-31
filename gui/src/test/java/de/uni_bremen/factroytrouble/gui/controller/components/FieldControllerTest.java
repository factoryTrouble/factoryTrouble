package de.uni_bremen.factroytrouble.gui.controller.components;

import de.saxsys.javafx.test.JfxRunner;
import de.uni_bremen.factroytrouble.gui.generator.board.ImageBoardGenerator;
import javafx.beans.property.SimpleDoubleProperty;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.awt.image.BufferedImage;

import static org.junit.Assert.*;

@RunWith(JfxRunner.class)
public class FieldControllerTest {

    @Mock private ImageBoardGenerator imageBoardGenerator;
    @InjectMocks private TestFieldController testFieldController;

    @Before
    public void setUp() {
        testFieldController = new TestFieldController();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldZoomOut() {
        testFieldController.zoom = new SimpleDoubleProperty(2.71);
        testFieldController.zoom(0.0000000000000000000001);
        assertEquals(2.981, testFieldController.zoom.get(), 0.05);
    }

    @Test
    public void shouldZoomIn() {
        testFieldController.zoom = new SimpleDoubleProperty(0.219999999999999);
        testFieldController.zoom(-0.0000000000000000000001);
        assertEquals(0.2, testFieldController.zoom.get(), 0.05);
    }

    @Test
    public void shouldNotZoomOutWhenDelterLessThanZero() {
        testFieldController.zoom = new SimpleDoubleProperty(0.2);
        testFieldController.zoom(-0.000000000000001);
        assertEquals(0.2, testFieldController.zoom.get(), 0.05);
    }

    @Test
    public void shouldNotZoomInWhenDelterGreaterThanZero() {
        testFieldController.zoom = new SimpleDoubleProperty(0.2);
        testFieldController.zoom(+0.0000000000000001);
        assertEquals(0.219999999999999, testFieldController.zoom.get(), 0.05);
    }

    @Test
    public void shouldNotZoomOutWhenZoomGetGreaterThree() {
        testFieldController.zoom = new SimpleDoubleProperty(2.727272727272727272727272727);
        testFieldController.zoom(+0.0000000000000001);
        assertEquals(2.727272727272727272727272727, testFieldController.zoom.get(), 0.05);
    }

    private class TestFieldController extends FieldController {

        @Override
        public BufferedImage getImage(String name) {
            return null;
        }
    }

}