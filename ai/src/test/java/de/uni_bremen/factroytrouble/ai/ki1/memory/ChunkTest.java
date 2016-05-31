package de.uni_bremen.factroytrouble.ai.ki1.memory;

import org.junit.Test;

import de.uni_bremen.factroytrouble.api.ki1.memory.Chunk;
import de.uni_bremen.factroytrouble.api.ki1.memory.Edge;
import de.uni_bremen.factroytrouble.api.ki1.memory.Key;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Set;

public class ChunkTest {
    @Test
    public void testGetAdjacentChunksEmptyChunk() {
        Chunk chunk = new ChunkUnit();
        Set<Chunk> chunks = chunk.getAdjacentChunks();
        assertTrue(chunks.isEmpty());
    }

    @Test
    public void testGetAdjacentChunks() throws Exception {
        ChunkUnit root = new ChunkUnit();
        EdgeUnit edge;
        Chunk chunk = new ChunkUnit(new ArrayList<Edge>(), new KeyUnit("x"), new Integer(5));
        edge = new EdgeUnit(root, chunk);
        root.addEdge(edge);

        Chunk chunk2 = new ChunkUnit(new ArrayList<Edge>(), new KeyUnit("y"), new Integer(10));
        edge = new EdgeUnit(root, chunk2);
        root.addEdge(edge);

        Set<Chunk> chunks = root.getAdjacentChunks();
        assertEquals(chunks.size(), 2);
        assertTrue(chunks.contains(chunk));
        assertTrue(chunks.contains(chunk2));
    }

    @Test
    public void testEqualsOtherNull() {
        assertNotEquals(new ChunkUnit(), null);
    }

    @Test
    public void testEqualsEmptyChunks() {
        assertNotEquals(new ChunkUnit(), new ChunkUnit());
    }

    @Test
    public void testEqualsOneEmpty() throws Exception {
        ChunkUnit root = new ChunkUnit();
        Edge edge = makeChunk(root, new KeyUnit("x"), new Integer(5));
        root.addEdge(edge);
        Chunk chunk = edge.getSucc();
        assertNotEquals(new ChunkUnit(), chunk);
    }

    @Test
    public void testEqualsOtherEmpty() throws Exception {
        ChunkUnit root = new ChunkUnit();
        Edge edge = makeChunk(root, new KeyUnit("x"), new Integer(5));
        root.addEdge(edge);
        Chunk chunk = edge.getSucc();
        assertNotEquals(chunk, new ChunkUnit());
    }

    /**
     * Erstellt folgenden Graphen zwei mal.
     * Testet, ob Knoten b gleich Knoten b im anderen Graphen ist.
     *
     *  /--->(a=3)
     * O       |
     * \---->(b=2
     *         \---->(c=6))
     * @throws Exception 
     */
    @Test
    public void testEqualsComplex() throws Exception {
        ChunkUnit root = new ChunkUnit();

        Edge edgeRootA = makeChunk(root, new KeyUnit("a"), new Integer(3));
        Edge edgeRootB = makeChunk(root, new KeyUnit("b"), new Integer(2));

        root.addEdge(edgeRootA);
        root.addEdge(edgeRootB);

        Chunk chunkA = edgeRootA.getSucc();
        Chunk chunkB = edgeRootB.getSucc();
        Edge AtoB = null;
        Edge BtoA = null;
        AtoB = new EdgeUnit(chunkA, chunkB);
        BtoA = new EdgeUnit(chunkB, chunkA);
        chunkA.addEdge(AtoB);
        chunkB.addEdge(BtoA);

        Edge edgeBtoC = makeChunk(chunkB, new KeyUnit("c"), new Integer(6));
        chunkB.addEdge(edgeBtoC);

        // Erzeugen Graph 2 //
        ChunkUnit root2 = new ChunkUnit();

        Edge edgeRootA2 = makeChunk(root, new KeyUnit("a"), new Integer(3));
        Edge edgeRootB2 = makeChunk(root, new KeyUnit("b"), new Integer(2));

        root2.addEdge(edgeRootA2);
        root2.addEdge(edgeRootB2);

        Chunk chunkA2 = edgeRootA2.getSucc();
        Chunk chunkB2 = edgeRootB2.getSucc();
        EdgeUnit AtoB2 = null;
        EdgeUnit BtoA2 = null;
        AtoB2 = new EdgeUnit(chunkA2, chunkB2);
        BtoA2 = new EdgeUnit(chunkB2, chunkA2);
        chunkA2.addEdge(AtoB2);
        chunkB2.addEdge(BtoA2);

        Edge edgeBtoC2 = makeChunk(chunkB2, new KeyUnit("c"), new Integer(6));
        chunkB2.addEdge(edgeBtoC2);

        assertEquals(chunkB, chunkB2);
    }

    private Edge makeChunk(Chunk root, Key key, Object value) throws Exception {
        Chunk chunk = new ChunkUnit(new ArrayList<Edge>(), key, value);
        Edge edge = null;
        edge = new EdgeUnit(root, chunk);
        return edge;
    }
}