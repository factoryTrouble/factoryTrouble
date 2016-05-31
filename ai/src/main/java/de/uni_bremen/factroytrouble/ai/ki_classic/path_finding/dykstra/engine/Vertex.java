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

public class Vertex {
    final private Point point;
    private int weight = -1;


    public Vertex(Point point) {
        this.point = point;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vertex other = (Vertex) obj;
        if (point == null) {
            if (other.point != null)
                return false;
        } else if (!point.equals(other.point))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Node: " + point.x + ", " + point.y;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Point getPoint() {
        return point;
    }
}