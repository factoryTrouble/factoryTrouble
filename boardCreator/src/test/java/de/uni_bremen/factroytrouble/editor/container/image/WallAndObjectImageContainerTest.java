package de.uni_bremen.factroytrouble.editor.container.image;

import de.uni_bremen.factroytrouble.editor.data.Orientation;
import org.junit.Before;
import org.junit.Test;

import javax.imageio.ImageIO;

import static de.uni_bremen.factroytrouble.editor.TestUtil.assertImageEquals;

public class WallAndObjectImageContainerTest {

    private WallAndObjectImageContainer wallAndObjectImageContainer;

    @Before
    public void setUp() {
        wallAndObjectImageContainer = new WallAndObjectImageContainer();
        wallAndObjectImageContainer.init();
    }

    @Test
    public void shouldGetAWall() throws Exception {
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/wall/wall_up.png")), wallAndObjectImageContainer.getWall(Orientation.NORTH));
    }

    @Test
    public void shouldGetAnEvenPusher() throws Exception {
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/pusher/pusher-even_up.png")), wallAndObjectImageContainer.getPusher(Orientation.NORTH, false));
    }

    @Test
    public void shouldGetAnOddPusher() throws Exception {
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/pusher/pusher-odd_up.png")), wallAndObjectImageContainer.getPusher(Orientation.NORTH, true));
    }

    @Test
    public void shouldAHorizontalLaser() throws Exception {
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/laser/laserHorizontal.png")), wallAndObjectImageContainer.getLaser(1, true));
    }

    @Test
    public void shouldAVerticalLaser() throws Exception {
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/laser/laser.png")), wallAndObjectImageContainer.getLaser(1, false));
    }
}