package de.uni_bremen.factroytrouble.editor.container.image;

import de.uni_bremen.factroytrouble.editor.data.GroundFill;
import org.junit.Before;
import org.junit.Test;

import javax.imageio.ImageIO;

import static de.uni_bremen.factroytrouble.editor.TestUtil.assertImageEquals;

public class TileGroundImageContainerTest {

    private TileGroundImageContainer tileGroundImageContainer;

    @Before
    public void setUp() {
        tileGroundImageContainer = new TileGroundImageContainer();
        tileGroundImageContainer.init();
    }

    @Test
    public void shouldGetATileGround() throws Exception {
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/empty.png")), tileGroundImageContainer.getImageForGround(GroundFill.EMPTY));
    }

    @Test(expected = TileGroundImageContainer.ConveyorBeltIsToSpecialException.class)
    public void shouldThrowAConveyorBeltIsToSpecialExceptionWhenUserRequestAConveyorBelt() {
        tileGroundImageContainer.getImageForGround(GroundFill.CONVEYOR_BELT);
    }

    @Test(expected = TileGroundImageContainer.ConveyorBeltIsToSpecialException.class)
    public void shouldThrowAConveyorBeltIsToSpecialExceptionWhenUserRequestAnExpressConveyorBelt() {
        tileGroundImageContainer.getImageForGround(GroundFill.EXPRESS_CONVEYOR_BELT);
    }

}