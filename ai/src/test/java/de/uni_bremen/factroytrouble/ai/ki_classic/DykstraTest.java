package de.uni_bremen.factroytrouble.ai.ki_classic;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import static org.junit.Assert.*;

import de.uni_bremen.factroytrouble.ai.ki_classic.path_finding.dykstra.engine.Dystra;
import de.uni_bremen.factroytrouble.ai.ki_classic.path_finding.dykstra.engine.Edge;
import de.uni_bremen.factroytrouble.ai.ki_classic.path_finding.dykstra.engine.Graph;
import de.uni_bremen.factroytrouble.ai.ki_classic.path_finding.dykstra.engine.Vertex;

public class DykstraTest {

    @Test
    public void shouldReturnSource() {
        Map<Point, Vertex> vertices = new HashMap<>();
        vertices.put(new Point(0, 0), new Vertex(new Point(0, 0)));
        Graph graph = new Graph(vertices, new ArrayList<>());
        Dystra dykstra = new Dystra(graph);
        
        Map<Point, Integer> expected = new HashMap<>();
        expected.put(new Point(0, 0), 0);
        assertEquals(expected, dykstra.execute(new Point(0, 0)));
    }
    
    @Test
    public void shouldReturnOneOther() {
        Map<Point, Vertex> vertices = new HashMap<>();
        vertices.put(new Point(0, 0), new Vertex(new Point(0, 0)));
        vertices.put(new Point(1, 0), new Vertex(new Point(1, 0)));
        
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(new Point(0, 0), new Point(1, 0), 5));
        
        Graph graph = new Graph(vertices, edges);
        Dystra dykstra = new Dystra(graph);
        
        Map<Point, Integer> expected = new HashMap<>();
        expected.put(new Point(0, 0), 0);
        expected.put(new Point(1, 0), 5);
        assertEquals(expected, dykstra.execute(new Point(0, 0)));
    }
    
    @Test
    public void shouldSelectShorterDistance() {
        Map<Point, Vertex> vertices = new HashMap<>();
        vertices.put(new Point(0, 0), new Vertex(new Point(0, 0)));
        vertices.put(new Point(1, 0), new Vertex(new Point(1, 0)));
        vertices.put(new Point(0, 1), new Vertex(new Point(0, 1)));
        vertices.put(new Point(1, 1), new Vertex(new Point(1, 1)));
        
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(new Point(0, 0), new Point(1, 0), 1));
        edges.add(new Edge(new Point(1, 0), new Point(1, 1), 1));
        
        edges.add(new Edge(new Point(0, 0), new Point(0, 1), 1));
        edges.add(new Edge(new Point(0, 1), new Point(1, 1), 2));
        
        Graph graph = new Graph(vertices, edges);
        Dystra dykstra = new Dystra(graph);
        
        Map<Point, Integer> expected = new HashMap<>();
        expected.put(new Point(0, 0), 0);
        expected.put(new Point(1, 0), 1);
        expected.put(new Point(0, 1), 1);
        expected.put(new Point(1, 1), 2);
        assertEquals(expected, dykstra.execute(new Point(0, 0)));
    }
}
