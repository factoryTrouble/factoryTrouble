package de.uni_bremen.factroytrouble.editor.data;

import org.junit.Test;

import static org.junit.Assert.*;

public class ConveyorBeltTileImageTest {

    private ConveyorBeltTileImage conveyorBeltTileImage;

    @Test
    public void shouldBeEqualToAnotherImageWithSameOutputAndInput() {
        conveyorBeltTileImage = new ConveyorBeltTileImage(Orientation.NORTH, false, Orientation.SOUTH);
        assertTrue(conveyorBeltTileImage.equals(new ConveyorBeltTileImage(Orientation.NORTH, false, Orientation.SOUTH)));
    }

    @Test
    public void shouldBeEqualToAnotherImageWithSameOutputAndInputIgnoreInputOrder() {
        conveyorBeltTileImage = new ConveyorBeltTileImage(Orientation.NORTH, false, Orientation.SOUTH, Orientation.EAST);
        assertTrue(conveyorBeltTileImage.equals(new ConveyorBeltTileImage(Orientation.NORTH, false, Orientation.EAST, Orientation.SOUTH)));
    }

    @Test
    public void shouldHasTehSameHashCodeAsAnotherImageWithSameOutputAndInput() {
        conveyorBeltTileImage = new ConveyorBeltTileImage(Orientation.NORTH, false, Orientation.SOUTH);
        assertEquals(conveyorBeltTileImage.hashCode(), new ConveyorBeltTileImage(Orientation.NORTH, false, Orientation.SOUTH).hashCode());
    }

    @Test
    public void shouldHasTehSameHashCodeAsAnotherImageWithSameOutputAndInputIgnoreInputOrder() {
        conveyorBeltTileImage = new ConveyorBeltTileImage(Orientation.NORTH, false, Orientation.SOUTH, Orientation.EAST);
        assertEquals(conveyorBeltTileImage.hashCode(), new ConveyorBeltTileImage(Orientation.NORTH, false, Orientation.EAST, Orientation.SOUTH).hashCode());
    }

}