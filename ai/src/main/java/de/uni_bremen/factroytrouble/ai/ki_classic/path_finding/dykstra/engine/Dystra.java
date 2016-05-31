/*
 * BASED ON Dijkstra Java code from
 * 
 * http://www.vogella.com/articles/JavaAlgorithmsDijkstra/article.html
 * 
 * Version 1.1 - Copyright 2009, 2010, 2011, 2011 Lars Vogel
 * 
 * MODIFIED BY Gregory Norton to address performance concerns.
 * 
 * Eclipse Public License
 * 
 * HT for Fork/Join HowTo: http://www.javacodegeeks.com/2011/02/
 * 							 java-forkjoin-parallel-programming.html
 * 
 */

package de.uni_bremen.factroytrouble.ai.ki_classic.path_finding.dykstra.engine;

import de.uni_bremen.factroytrouble.api.ki_classic.CouldNotFindEdgeWeightException;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Dystra {

    private Map<Point, Vertex> nodes;
    private List<Edge> edges;
    private Set<Point> settledNodes;
    private Set<Point> unSettledNodes;
    private Map<Point, Point> predecessors;
    private Map<Point, Integer> distance;


    public Dystra(Graph graph) {
        // create a copy of the array so that we can operate on this array
        nodes = new HashMap<>(graph.getVertexes());
        edges = new ArrayList<>(graph.getEdges());
        settledNodes = new HashSet<>();
        unSettledNodes = new HashSet<>();
        distance = new HashMap<>();
        predecessors = new HashMap<>();
    }

    public Map<Point, Integer> execute(Point source) {
        distance.put(source, 0);
        unSettledNodes.add(source);
        while (unSettledNodes.size() > 0) {
            Point node = getMinimum(unSettledNodes);
            settledNodes.add(node);
            unSettledNodes.remove(node);
            findMinimalDistances(node);
        }
        return distance;
    }

    private void findMinimalDistances(Point node) {
        List<Point> adjacentNodes = getNeighbors(node);
        adjacentNodes.stream().filter(target -> getShortestDistance(target) > getShortestDistance(node)
                + getDistance(node, target)).forEach(target -> {
            nodes.get(target).setWeight(getShortestDistance(node));
            distance.put(target, getShortestDistance(node)
                    + getDistance(node, target));
            predecessors.put(target, node);
            unSettledNodes.add(target);
        });

    }

    private int getDistance(Point node, Point target) {
        for (Edge edge : edges) {
            if (edge.getSource().equals(node) && edge.getDestination().equals(target)) {
                return edge.getWeight();
            }
        }
        throw new CouldNotFindEdgeWeightException();
    }

    private List<Point> getNeighbors(Point node) {
        return edges.stream().filter(edge -> edge.getSource().equals(node)
                && !isSettled(nodes.get(edge.getDestination()))).map(Edge::getDestination).collect(Collectors.toList());
    }

    private Point getMinimum(Set<Point> vertexes) {
        final Point[] minimum = {null};
        vertexes.forEach(vertex -> {
            if (minimum[0] == null) {
                minimum[0] = vertex;
            } else {
                if (getShortestDistance(vertex) < getShortestDistance(minimum[0])) {
                    minimum[0] = vertex;
                }
            }
        });
        return minimum[0];
    }

    private boolean isSettled(Vertex vertex) {
        return settledNodes.contains(vertex);
    }

    private int getShortestDistance(Point destination) {
        Integer d = distance.get(destination);
        if (d == null) {
            return Integer.MAX_VALUE;
        }
        return d;
    }
}

