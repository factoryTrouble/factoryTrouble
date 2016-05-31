package de.uni_bremen.factroytrouble.editor.container.image;

import de.uni_bremen.factroytrouble.editor.data.ConveyorBeltTileImage;
import de.uni_bremen.factroytrouble.editor.data.Orientation;
import org.junit.Before;
import org.junit.Test;

import javax.imageio.ImageIO;

import static de.uni_bremen.factroytrouble.editor.TestUtil.assertImageEquals;

public class ConveyorBeltImageContainerTest {

    private ConveyorBeltImageContainer conveyorBeltImageContainer;

    @Before
    public void setUp() {
        conveyorBeltImageContainer = new ConveyorBeltImageContainer();
        conveyorBeltImageContainer.init();
    }

    @Test
    public void shouldGetAConveyorBeltByConveyorBeltTileImage() throws Exception{
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/conveyorBelt/straight/roller_straight_up.png")), conveyorBeltImageContainer.getConyorbeltImage(new ConveyorBeltTileImage(Orientation.NORTH, false, Orientation.SOUTH)));
    }

}