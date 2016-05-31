package de.uni_bremen.factroytrouble.ai.ki1.memory;

import static org.junit.Assert.*;

import java.io.IOException;

import de.uni_bremen.factroytrouble.api.ki1.memory.MemoryEvent;
import de.uni_bremen.factroytrouble.exceptions.DeclarativeMemoryException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameter;
import org.mockito.Mockito;

import de.uni_bremen.factroytrouble.exceptions.KeyNotFoundException;
import de.uni_bremen.factroytrouble.gameobjects.Robot;

public class LTMUnitTest {
    @Parameter
    private LTMUnit ltm;
    private ChunkUnit c;
    private static final int round = 3;

    @Before
    public void create() throws IOException {
        ltm = new LTMUnit();
        c = new ChunkUnit();
    }

    @Test
    public void testretrieveNull() throws KeyNotFoundException{
        assertTrue(null ==ltm.retrieveChunk(new KeyUnit("Banana")));
    }
    
    @Test
    public void testStoreChunk() throws KeyNotFoundException, IOException {
        Robot r = Mockito.mock(Robot.class);
        c.setData(r);
        c.setKey(new KeyUnit(r));
        ltm.storeChunk(c);

        assertEquals(c, ltm.retrieveChunk(new KeyUnit(r)));
    }

    @Test
    public void testStoreChunkEvent() throws KeyNotFoundException, IOException {
        Robot r = Mockito.mock(Robot.class);
        c.setData(r);
        c.setKey(new KeyUnit(r));
        ltm.storeChunk(c);
        RobotEvent e2 = new RobotEvent(r, RobotEvent.EventType.KILLEDME, round);
        ChunkUnit c2 = new ChunkUnit();
        c2.setData(e2);
        c2.setKey(new KeyUnit(e2));
        ltm.storeChunk(c2);

        assertEquals(e2, ltm.retrieveChunk(new KeyUnit(e2)).getData());
    }

    @Test
    public void testStoreChunkEventInkrement() throws KeyNotFoundException, IOException {
        Robot r = Mockito.mock(Robot.class);
        c.setData(r);
        c.setKey(new KeyUnit(r));
        ltm.storeChunk(c);
        MemoryEvent e2 = new RobotEvent(r, RobotEvent.EventType.KILLEDME, round);
        ChunkUnit c2 = new ChunkUnit();
        c2.setData(e2);
        c2.setKey(new KeyUnit(e2));
        ltm.storeChunk(c2);
        RobotEvent e3 = new RobotEvent(r, RobotEvent.EventType.KILLEDME, round);
        ChunkUnit c3 = new ChunkUnit();
        c3.setData(e3);
        c3.setKey(new KeyUnit(e3));
        ltm.storeChunk(c3);

        assertEquals(new Integer(2), e2.getCount());
    }

    @Test
    public void testDeclerativeKnowledge() throws DeclarativeMemoryException {
        c.setDeclerative(true);
        ltm.addDeclerativeKnowledge(c);

    }

    @Test
    public void testDecay() throws KeyNotFoundException, IOException {
        ltm.storeChunk(c);
        EdgeUnit e = new EdgeUnit();
        ltm.memoryDecay(10000);
        assertTrue(c.getCore().getLength() > e.getLength());
    }

    @Test
    public void testAddToChunks() throws KeyNotFoundException, IOException {
        String arr = "Banana";
        c.setData(arr);
        ChunkUnit cx = new ChunkUnit();
        cx.setData(arr);
        ltm.storeChunk(c);
        ltm.storeChunk(cx);
        EdgeUnit e = new EdgeUnit();
        assertTrue(c.getCore().getStrength() < e.getStrength());
    }
    
    @Test
    public void testGetEdges(){
        assertNotNull(ltm.getEdges());
    }
    
}
