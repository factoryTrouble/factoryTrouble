package de.uni_bremen.factroytrouble.ai.ki1.memory;

import de.uni_bremen.factroytrouble.api.ki1.memory.Chunk;

import de.uni_bremen.factroytrouble.gameobjects.Robot;

import org.junit.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class MemoryUnitTest {
    private MemoryUnit memoryUnit;
    private static final int round = 3;

    @Mock
    private Robot robot;

    @Before
    public void create() {
        memoryUnit = new MemoryUnit();
    }

    @Test
    public void testRetrieveSimple() {
        memoryUnit.storeChunk(new KeyUnit("whoop, whoop"), (Object) new Integer(23));
        Chunk chunk = memoryUnit.retrieveChunk(new KeyUnit("whoop, whoop"));
        Integer data = (Integer) chunk.getData();
        assertEquals(data, new Integer(23));
    }

    @Test
    public void testRetrieveNotAccessible() {
        memoryUnit.storeChunk(new KeyUnit(new RobotEvent(robot, RobotEvent.EventType.KILLEDME, round)), (Object) new Integer(23));
        Chunk chunk = memoryUnit.retrieveChunk(new KeyUnit("gibt's ja gar nicht"));
        assertEquals(chunk, null);
    }
}