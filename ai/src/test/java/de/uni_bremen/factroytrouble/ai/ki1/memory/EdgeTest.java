package de.uni_bremen.factroytrouble.ai.ki1.memory;

import static org.junit.Assert.*;

import java.io.IOException;

import de.uni_bremen.factroytrouble.ai.ki1.configreader.AgentConfigReader;
import de.uni_bremen.factroytrouble.ai.ki1.configreader.ConfigReader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameter;

import de.uni_bremen.factroytrouble.exceptions.KeyNotFoundException;

public class EdgeTest {

    @Parameter
    EdgeUnit e;
    ConfigReader cnfg;

    @Before
    public void create() throws KeyNotFoundException, IOException {
        e = new EdgeUnit();
        cnfg = AgentConfigReader.getInstance(1);
        e.setStrength(cnfg.getDoubleProperty("Edge.DefaultStrength"));
        e.setLength(cnfg.getIntProperty("Edge.DefaultLength"));
    }

    @Test
    public void testDecay() throws KeyNotFoundException {
        e.setDeclerative(true);
        e.decay(10000);
        assertTrue("Declerative Edges should not grow in length",
                cnfg.getIntProperty("Edge.DefaultLength") == e.getLength());

        e.setDeclerative(false);
        e.decay(10000);
        assertTrue("Non-Declerative Edges should grow in length",
                cnfg.getIntProperty("Edge.DefaultLength") < e.getLength());

        Integer x = 100;
        e.setLength(x);
        e.decay(0);
        assertTrue("When no time has passed the edge should not grow", e.getLength() == x);
    }

    @Test
    public void testHarden() throws KeyNotFoundException {
        e.harden();
        assertTrue("Edge should be harder (lower)", e.getStrength() < cnfg.getDoubleProperty("Edge.DefaultStrength"));
    }

    @Test
    public void testLengthen() throws KeyNotFoundException {
        Integer x = 10;
        e.lengthen(x);
        assertTrue("Edge should change by the appropriate amount",
                cnfg.getIntProperty("Edge.DefaultLength") + x == e.getLength());
    }

    @Test
    public void testShorten() throws KeyNotFoundException {
        e.setLength(2 * cnfg.getIntProperty("Edge.DefaultLength"));
        e.shorten();
        assertTrue("Edge should be at default length after shortening",
                e.getLength() == cnfg.getIntProperty("Edge.DefaultLength"));
    }

    @Test
    public void testWeaken() throws KeyNotFoundException {
        e.setStrength(1.0);
        e.weaken();
        assertTrue("When at minimal strength and weakend the strength should remain at it", e.getStrength() == 1.0);
        e.setStrength(0.5);
        e.weaken();
        assertTrue(0.5 < e.getStrength());
    }
}
