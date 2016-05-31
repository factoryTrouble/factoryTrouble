package de.uni_bremen.factroytrouble.gameobjects;

import org.junit.Test;

import static org.junit.Assert.*;

public class OrientationTest {

    @Test
    public void shouldGetTheOppositeDirectionOfNorth() {
        assertEquals(Orientation.SOUTH, Orientation.getOppositeDirection(Orientation.NORTH));
    }

    @Test
    public void shouldGetTheOppositeDirectionOfWest() {
        assertEquals(Orientation.EAST, Orientation.getOppositeDirection(Orientation.WEST));
    }

    @Test
    public void shouldGetTheOppositeDirectionOfSouth() {
        assertEquals(Orientation.NORTH, Orientation.getOppositeDirection(Orientation.SOUTH));
    }

    @Test
    public void shouldGetTheOppositeDirectionOfEast() {
        assertEquals(Orientation.WEST, Orientation.getOppositeDirection(Orientation.EAST));
    }

    @Test
    public void shouldGetTheLeftNeighbourOfNorth() {
        assertEquals(Orientation.WEST, Orientation.getNextDirection(Orientation.NORTH, true));
    }

    @Test
    public void shouldGetTheLeftNeighbourOfWest() {
        assertEquals(Orientation.SOUTH, Orientation.getNextDirection(Orientation.WEST, true));
    }

    @Test
    public void shouldGetTheLeftNeighbourOfSouth() {
        assertEquals(Orientation.EAST, Orientation.getNextDirection(Orientation.SOUTH, true));
    }

    @Test
    public void shouldGetTheLeftNeighbourOfEast() {
        assertEquals(Orientation.NORTH, Orientation.getNextDirection(Orientation.EAST, true));
    }

    @Test
    public void shouldGetTheRightNeighbourOfNorth() {
        assertEquals(Orientation.EAST, Orientation.getNextDirection(Orientation.NORTH, false));
    }

    @Test
    public void shouldGetTheRightNeighbourOfWest() {
        assertEquals(Orientation.NORTH, Orientation.getNextDirection(Orientation.WEST, false));
    }

    @Test
    public void shouldGetTheRightNeighbourOfSouth() {
        assertEquals(Orientation.WEST, Orientation.getNextDirection(Orientation.SOUTH, false));
    }

    @Test
    public void shouldGetTheRightNeighbourOfEast() {
        assertEquals(Orientation.SOUTH, Orientation.getNextDirection(Orientation.EAST, false));
    }

}