/*
 * Dijkstra Java code from
 * 
 * http://www.vogella.com/articles/JavaAlgorithmsDijkstra/article.html
 * 
 * Version 1.1 - Copyright 2009, 2010, 2011, 2011 Lars Vogel
 * 
 * Eclipse Public License
 */

package de.uni_bremen.factroytrouble.ai.ki_classic.path_finding.dykstra.engine;

import java.awt.*;

public class Edge {
    private final Point source;
    private final Point destination;
    private int weight;

    public Edge(Point source, Point destination, int weight) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    public Point getDestination() {
        return destination;
    }

    public Point getSource() {
        return source;
    }

    public int getWeight() {
        return weight;
    }

    public void modifyWeight(int modifyer) {
        weight = weight + modifyer;
    }


    @Override
    public String toString() {
        return source + " " + destination;
    }


}
