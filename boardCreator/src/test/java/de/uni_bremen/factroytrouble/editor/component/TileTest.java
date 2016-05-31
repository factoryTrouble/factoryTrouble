package de.uni_bremen.factroytrouble.editor.component;

import de.saxsys.javafx.test.JfxRunner;
import de.uni_bremen.factroytrouble.editor.data.FieldData;
import de.uni_bremen.factroytrouble.editor.data.GroundFill;
import de.uni_bremen.factroytrouble.editor.data.Orientation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

@RunWith(JfxRunner.class)
public class TileTest {

    @Mock
    private FieldData fieldData;
    private Tile tile;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        tile = new Tile();
    }

    @Test
    public void shouldResetTheFieldObjectWhenNewGroundSet() {
        tile.addFieldData(fieldData, Orientation.EAST);
        tile.setGroundFill(GroundFill.EMPTY);
        assertFalse(tile.fieldDataExists(Orientation.EAST));
    }

    @Test
    public void shouldAddFieldData() {
        tile.addFieldData(fieldData, Orientation.EAST);
        assertEquals(fieldData, tile.getFieldData(Orientation.EAST));
    }

    @Test
    public void shouldRemoveFieldData() {
        tile.addFieldData(fieldData, Orientation.EAST);
        tile.removeFieldData(Orientation.EAST);
        assertFalse(tile.fieldDataExists(Orientation.EAST));
    }

    @Test
    public void shouldReturnNullWhenFieldDataDoNotExistsInOrientation() {
        assertNull(tile.getFieldData(Orientation.EAST));
    }

}