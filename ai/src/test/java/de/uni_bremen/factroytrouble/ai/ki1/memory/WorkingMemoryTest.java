package de.uni_bremen.factroytrouble.ai.ki1.memory;

import org.junit.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.junit.Assert.*;

import de.uni_bremen.factroytrouble.api.ki1.memory.Chunk;
import de.uni_bremen.factroytrouble.gameobjects.Robot;

@RunWith(MockitoJUnitRunner.class)
public class WorkingMemoryTest {
    @InjectMocks
    private KeyUnit key;
    private static final int round = 3;
    @Mock
    private Robot robot;

    private WorkingMemoryUnit memory;

    @Before
    public void setUp() {
        memory = new WorkingMemoryUnit();
        key = new KeyUnit(new RobotEvent(robot, RobotEvent.EventType.KILLEDME, round));
    }

    @Test
    public void testRetrieveChunkKeyNull() {
        assertNull(memory.retrieveChunk(null));
    }

    @Test
    public void testInitialSize() {
        assertEquals(new Integer(0), memory.getSize());
    }

    @Test
    public void testSizeAfterAdd() throws Exception {
        memory.storeChunk(key, null);
        assertEquals(new Integer(1), memory.getSize());
    }

    @Test
    public void testSizeAfterAddingWithSameKeyTwice() throws Exception {
        memory.storeChunk(key, new Integer(23));
        memory.storeChunk(key, new Integer(42));
        assertEquals(new Integer(1), memory.getSize());
    }

    @Test
    public void testAddToEmpty() throws Exception {
        memory.storeChunk(key, new Integer(5));
        assertEquals(new ChunkUnit(key, new Integer(5)),
                memory.retrieveChunk(key));
    }

    @Test
    public void testRetrieveChunkOneItemAdded() throws Exception {
        Integer value = 5;
        memory.storeChunk(key, value);
        Chunk chunk = memory.retrieveChunk(key);
        Chunk expectedChunk = new ChunkUnit(key, value);
        assertEquals(expectedChunk, chunk);
    }

    @Test
    public void testRetrieveChunkOverwritten() throws Exception {
        memory.storeChunk(key, new Integer(5));
        memory.storeChunk(key, new Integer(10));
        assertEquals(new ChunkUnit(key, new Integer(10)), memory.retrieveChunk(key));
    }

    @Test
    public void testGetOldestChunk() throws Exception {
        addThreeChunks();
        Chunk oldestChunk = memory.getOldestChunk();
        assertEquals(new Integer(1), oldestChunk.getData());
    }

    @Test
    public void testStoreMoreThanSeven() throws Exception {
        addEightChunks();
        Chunk oldestChunk = memory.getOldestChunk();
        assertEquals(new Integer(2), oldestChunk.getData());
        assertEquals(new Integer(7), memory.getSize());
    }

    private void addThreeChunks() throws Exception {
        memory.storeChunk(new KeyUnit("one"), new Integer(1));
        memory.storeChunk(new KeyUnit("two"), new Integer(2));
        memory.storeChunk(new KeyUnit("three"), new Integer(3));
    }

    private void addEightChunks() throws Exception {
        memory.storeChunk(new KeyUnit("one"), new Integer(1));
        memory.storeChunk(new KeyUnit("two"), new Integer(2));
        memory.storeChunk(new KeyUnit("three"), new Integer(3));
        memory.storeChunk(new KeyUnit("four"), new Integer(4));
        memory.storeChunk(new KeyUnit("five"), new Integer(5));
        memory.storeChunk(new KeyUnit("six"), new Integer(6));
        memory.storeChunk(new KeyUnit("seven"), new Integer(7));
        memory.storeChunk(new KeyUnit("eight"), new Integer(8));
    }
}