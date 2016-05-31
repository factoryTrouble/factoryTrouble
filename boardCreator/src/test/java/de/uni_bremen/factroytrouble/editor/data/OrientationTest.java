package de.uni_bremen.factroytrouble.editor.data;

import org.junit.Test;

import static org.junit.Assert.*;

public class OrientationTest {

    @Test
    public void shouldGetSouthAsOppositeOrientationOfNorth() {
        assertEquals(Orientation.SOUTH, Orientation.NORTH.getOppositeDirection());
    }

    @Test
    public void shouldGetNorthAsOppositeOrientationOfSouth() {
        assertEquals(Orientation.NORTH, Orientation.SOUTH.getOppositeDirection());
    }

    @Test
    public void shouldGetWestAsOppositeOrientationOfEast() {
        assertEquals(Orientation.WEST, Orientation.EAST.getOppositeDirection());
    }

    @Test
    public void shouldGetEastAsOppositeOrientationOfWest() {
        assertEquals(Orientation.EAST, Orientation.WEST.getOppositeDirection());
    }

    @Test
    public void shouldGetWestAsRightNeighborOfNorth() {
        assertEquals(Orientation.WEST, Orientation.NORTH.getNextDirection(false));
    }

    @Test
    public void shouldGetSouthAsRightNeighborOfWest() {
        assertEquals(Orientation.SOUTH, Orientation.WEST.getNextDirection(false));
    }

    @Test
    public void shouldGetEastAsRightNeighborOfSouth() {
        assertEquals(Orientation.EAST, Orientation.SOUTH.getNextDirection(false));
    }

    @Test
    public void shouldGetNorthAsRightNeighborOfEast() {
        assertEquals(Orientation.NORTH, Orientation.EAST.getNextDirection(false));
    }

    @Test
    public void shouldGetEastAsLeftNeighborOfNorth() {
        assertEquals(Orientation.EAST, Orientation.NORTH.getNextDirection(true));
    }

    @Test
    public void shouldGetNorthAsLeftNeighborOfWest() {
        assertEquals(Orientation.NORTH, Orientation.WEST.getNextDirection(true));
    }

    @Test
    public void shouldGetWestAsLeftNeighborOfSouth() {
        assertEquals(Orientation.WEST, Orientation.SOUTH.getNextDirection(true));
    }

    @Test
    public void shouldGetSouthAsLeftNeighborOfEast() {
        assertEquals(Orientation.SOUTH, Orientation.EAST.getNextDirection(true));
    }

}