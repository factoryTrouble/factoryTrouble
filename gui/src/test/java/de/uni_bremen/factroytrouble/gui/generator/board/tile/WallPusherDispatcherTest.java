package de.uni_bremen.factroytrouble.gui.generator.board.tile;

import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.gameobjects.Wall;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import static de.uni_bremen.factroytrouble.gui.TestUtil.assertImageEquals;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WallPusherDispatcherTest {

    @InjectMocks private WallPusherDispatcher wallPusherDispatcher = new WallPusherDispatcher();
    @Mock private Wall wallMock;  

    @Before
    public void setUp() {
        wallPusherDispatcher.init();
    }

    @Test
    public void shouldPlaceAWallInNorthWithOddPusher() throws Exception {
        when(wallMock.getOrientation()).thenReturn(Orientation.NORTH);
        when(wallMock.getPusherPhases()).thenReturn(new int[3]);

        BufferedImage image = wallPusherDispatcher.dispatchPusher(wallMock);
        assertImageEquals(getPusherImage("odd", "up"), image);
    }

    @Test
    public void shouldPlaceAWallInSouthWithOddPusher() throws Exception {
        when(wallMock.getOrientation()).thenReturn(Orientation.SOUTH);
        when(wallMock.getPusherPhases()).thenReturn(new int[3]);

        BufferedImage image = wallPusherDispatcher.dispatchPusher(wallMock);;
        assertImageEquals(getPusherImage("odd", "down"), image);
    }

    @Test
    public void shouldPlaceAWallInWestWithOddPusher() throws Exception {
        when(wallMock.getOrientation()).thenReturn(Orientation.WEST);
        when(wallMock.getPusherPhases()).thenReturn(new int[3]);

        BufferedImage image = wallPusherDispatcher.dispatchPusher(wallMock);;
        assertImageEquals(getPusherImage("odd", "left"), image);
    }

    @Test
    public void shouldPlaceAWallInEastWithOddPusher() throws Exception {
        when(wallMock.getOrientation()).thenReturn(Orientation.EAST);
        when(wallMock.getPusherPhases()).thenReturn(new int[3]);

        BufferedImage image = wallPusherDispatcher.dispatchPusher(wallMock);;
        assertImageEquals(getPusherImage("odd", "right"), image);
    }

    @Test
    public void shouldPlaceAWallInNorthWithEvenPusher() throws Exception {
        when(wallMock.getOrientation()).thenReturn(Orientation.NORTH);
        when(wallMock.getPusherPhases()).thenReturn(new int[2]);

        BufferedImage image = wallPusherDispatcher.dispatchPusher(wallMock);;
        assertImageEquals(getPusherImage("even", "up"), image);
    }

    @Test
    public void shouldPlaceAWallInSouthWithEvenPusher() throws Exception {
        when(wallMock.getOrientation()).thenReturn(Orientation.SOUTH);
        when(wallMock.getPusherPhases()).thenReturn(new int[2]);

        BufferedImage image = wallPusherDispatcher.dispatchPusher(wallMock);;
        assertImageEquals(getPusherImage("even", "down"), image);
    }

    @Test
    public void shouldPlaceAWallInWestWithEvenPusher() throws Exception {
        when(wallMock.getOrientation()).thenReturn(Orientation.WEST);
        when(wallMock.getPusherPhases()).thenReturn(new int[2]);

        BufferedImage image = wallPusherDispatcher.dispatchPusher(wallMock);;
        assertImageEquals(getPusherImage("even", "left"), image);
    }

    @Test
    public void shouldPlaceAWallInEastWithEvenPusher() throws Exception {
        when(wallMock.getOrientation()).thenReturn(Orientation.EAST);
        when(wallMock.getPusherPhases()).thenReturn(new int[2]);

        BufferedImage image = wallPusherDispatcher.dispatchPusher(wallMock);;
        assertImageEquals(getPusherImage("even", "right"), image);
    }

    @Test
    public void shouldReturnNullWhenWallHasNoPusher() {
        when(wallMock.getPusherPhases()).thenReturn(new int[0]);
        assertNull(wallPusherDispatcher.dispatchPusher(wallMock));
    }

    private BufferedImage getPusherImage(String oddEven, String direction) throws Exception {
        return ImageIO.read(getClass().getResourceAsStream("/game/tiles/pusher/pusher-" + oddEven + "_" + direction + ".png"));

    }

}