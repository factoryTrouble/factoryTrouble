package de.uni_bremen.factroytrouble.ai.ki_classic;

import java.awt.Point;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import de.uni_bremen.factroytrouble.ai.ki_classic.path_finding.dykstra.DykstraHandler;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;

public class DykstraHandlerTest {

    private BoardMocker bm;

    @Before
    public void setup() {
        bm = new BoardMocker();
    }

    @Test
    public void shouldGiveValueOverZero() {
        bm.fillField(2);
        bm.setFlagTile(new Point(1, 1));
        bm.setStartTile(new Point(0, 0), Orientation.NORTH);
        DykstraHandler handler = new DykstraHandler(bm.getMaster());
        assertTrue(0 < handler.getWeight(1, new Point(1, 0), Orientation.NORTH));
    }

}
