package de.uni_bremen.factroytrouble.editor.service.util;

import de.uni_bremen.factroytrouble.editor.data.Orientation;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EvaluateClickOrientationServiceTest {

    private EvaluateClickOrientationService evaluateClickOrientationService;

    @Before
    public void setUp() {
        evaluateClickOrientationService = new EvaluateClickOrientationService();
    }

    @Test
    public void shouldReturnNullWhenUserClickedInTheCenter() {
        assertNull(evaluateClickOrientationService.evaluate(25.0, 25.0, 50, 20.0));
    }

    @Test
    public void shouldReturnNullWhenUserClickedInLeftTopCornerOfTheDeadArea() {
        assertNull(evaluateClickOrientationService.evaluate(20.0, 30.0, 50, 20.0));
    }

    @Test
    public void shouldReturnNullWhenUserClickedInRightTopCornerOfTheDeadArea() {
        assertNull(evaluateClickOrientationService.evaluate(30.0, 30.0, 50, 20.0));
    }

    @Test
    public void shouldReturnNullWhenUserClickedInLeftBottomCornerOfTheDeadArea() {
        assertNull(evaluateClickOrientationService.evaluate(20.0, 20.0, 50, 20.0));
    }

    @Test
    public void shouldReturnNullWhenUserClickedInRightBottomCornerOfTheDeadArea() {
        assertNull(evaluateClickOrientationService.evaluate(30.0, 20.0, 50, 20.0));
    }

    @Test
    public void shouldReturnNullWhenUserClickedInTheTopLeftCorner() {
        assertNull(evaluateClickOrientationService.evaluate(0.0, 0.0, 50, 20.0));
    }

    @Test
    public void shouldReturnNullWhenUserClickedInTheTopRightCorner() {
        assertNull(evaluateClickOrientationService.evaluate(0.0, 50.0, 50, 20.0));
    }

    @Test
    public void shouldReturnNullWhenUserClickedInTheBottomLeftCorner() {
        assertNull(evaluateClickOrientationService.evaluate(0.0, 50.0, 50, 20.0));
    }

    @Test
    public void shouldReturnNullWhenUserClickedInTheBottomRightCorner() {
        assertNull(evaluateClickOrientationService.evaluate(49.0, 49.0, 50, 20.0));
    }

    @Test
    public void shouldReturnNorthWhenTheUserClickedInTopArea() {
        assertEquals(Orientation.NORTH, evaluateClickOrientationService.evaluate(25.0, 0.0, 50, 20.0));
    }

    @Test
    public void shouldReturnSouthWhenTheUserClickedInBottomArea() {
        assertEquals(Orientation.SOUTH, evaluateClickOrientationService.evaluate(25.0, 50.0, 50, 20.0));
    }

    @Test
    public void shouldReturnWestWhenTheUserClickedInLeftArea() {
        assertEquals(Orientation.WEST, evaluateClickOrientationService.evaluate(0.0, 25.0, 50, 20.0));
    }

    @Test
    public void shouldReturnEastWhenTheUserClickedInRightArea() {
        assertEquals(Orientation.EAST, evaluateClickOrientationService.evaluate(50.0, 25.0, 50, 20.0));
    }

}