package de.uni_bremen.factroytrouble.editor.factory;

import de.uni_bremen.factroytrouble.editor.component.Tile;
import de.uni_bremen.factroytrouble.editor.data.GroundFill;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TileFactoryTest {

    private TileFactory tileFactory;

    @Before
    public void setUp() {
        tileFactory = new TileFactory();
        tileFactory.init();
    }

    @Test
    public void shouldCreateATileWithPredefinedImage() {
        Tile tile = tileFactory.getTile(GroundFill.EMPTY);
        assertNotNull(tile.getImage());
    }

    @Test
    public void shouldCreateAnTileWithNoImageWhenGroundNotRecognized() {
        Tile tile = tileFactory.getTile(GroundFill.CONVEYOR_BELT);
        assertNull(tile.getImage());
    }

    @Test
    public void shouldSetGroundFillForTile() {
        Tile tile = tileFactory.getTile(GroundFill.CONVEYOR_BELT);
        assertEquals(GroundFill.CONVEYOR_BELT, tile.getGroundFill());
    }

}