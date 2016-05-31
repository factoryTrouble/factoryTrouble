package de.uni_bremen.factroytrouble.editor.service.tile;

import de.uni_bremen.factroytrouble.editor.container.image.WallAndObjectImageContainer;
import de.uni_bremen.factroytrouble.editor.data.FieldObject;
import de.uni_bremen.factroytrouble.editor.data.Orientation;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.imageio.ImageIO;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CombineTileServiceTest {

    @Mock private WallAndObjectImageContainer wallAndObjectImageContainer;
    @InjectMocks private CombineTileService combineTileService = new CombineTileService();

    private Image testImage;

    @Before
    public void setUp() throws Exception {
        testImage = SwingFXUtils.toFXImage(ImageIO.read(getClass().getResourceAsStream("/game/tiles/empty.png")), null);
    }

    @Test
    public void shouldCombineImageAWithWallWhenFieldObjectIsAWall() {
        combineTileService.combineGroundWithWallObject(testImage, FieldObject.WALL, Orientation.NORTH, 150);
        verify(wallAndObjectImageContainer).getWall(Orientation.NORTH);
    }

    @Test
    public void shouldCombineImageWithAnEvenPusherWhenFieldObjectIsAnEvenPusher() {
        combineTileService.combineGroundWithWallObject(testImage, FieldObject.PUSHER_EVEN, Orientation.NORTH, 150);
        verify(wallAndObjectImageContainer).getPusher(Orientation.NORTH, false);
    }

    @Test
    public void shouldCombineImageWithAnOddPusherWhenFieldObjectIsAnOddPusher() {
        combineTileService.combineGroundWithWallObject(testImage, FieldObject.PUSHER_ODD, Orientation.NORTH, 150);
        verify(wallAndObjectImageContainer).getPusher(Orientation.NORTH, true);
    }

    @Test
    public void shouldCombineImageWithWallWhenFieldObjectIsAnOddPusher() {
        combineTileService.combineGroundWithWallObject(testImage, FieldObject.PUSHER_ODD, Orientation.NORTH, 150);
        verify(wallAndObjectImageContainer).getPusher(Orientation.NORTH, true);
    }

    @Test
    public void shouldCombineImageWithSingleVerticalLaserWhenHasLaserInNorth() {
        combineTileService.combineGroundWithWallObject(testImage, FieldObject.LASER_SINGLE, Orientation.NORTH, 150);
        verify(wallAndObjectImageContainer).getLaser(1, false);
    }

    @Test
    public void shouldCombineImageWithSingleVerticalLaserWhenHasLaserInSouth() {
        combineTileService.combineGroundWithWallObject(testImage, FieldObject.LASER_SINGLE, Orientation.SOUTH, 150);
        verify(wallAndObjectImageContainer).getLaser(1, false);
    }

    @Test
    public void shouldCombineImageWithSingleHorizontalLaserWhenHasLaserInWest() {
        combineTileService.combineGroundWithWallObject(testImage, FieldObject.LASER_SINGLE, Orientation.WEST, 150);
        verify(wallAndObjectImageContainer).getLaser(1, true);
    }

    @Test
    public void shouldCombineImageWithSingleHorizontalLaserWhenHasLaserInEast() {
        combineTileService.combineGroundWithWallObject(testImage, FieldObject.LASER_SINGLE, Orientation.EAST, 150);
        verify(wallAndObjectImageContainer).getLaser(1, true);
    }

    @Test
    public void shouldCombineImageWithDoubleVerticalLaserWhenHasLaserInNorth() {
        combineTileService.combineGroundWithWallObject(testImage, FieldObject.LASER_DOUBLE, Orientation.NORTH, 150);
        verify(wallAndObjectImageContainer).getLaser(2, false);
    }

    @Test
    public void shouldCombineImageWithDoubleVerticalLaserWhenHasLaserInSouth() {
        combineTileService.combineGroundWithWallObject(testImage, FieldObject.LASER_DOUBLE, Orientation.SOUTH, 150);
        verify(wallAndObjectImageContainer).getLaser(2, false);
    }

    @Test
    public void shouldCombineImageWithDoubleHorizontalLaserWhenHasLaserInWest() {
        combineTileService.combineGroundWithWallObject(testImage, FieldObject.LASER_DOUBLE, Orientation.WEST, 150);
        verify(wallAndObjectImageContainer).getLaser(2, true);
    }

    @Test
    public void shouldCombineImageWithDoubleHorizontalLaserWhenHasLaserInEast() {
        combineTileService.combineGroundWithWallObject(testImage, FieldObject.LASER_DOUBLE, Orientation.EAST, 150);
        verify(wallAndObjectImageContainer).getLaser(2, true);
    }

    @Test
    public void shouldCombineImageWithTripleVerticalLaserWhenHasLaserInNorth() {
        combineTileService.combineGroundWithWallObject(testImage, FieldObject.LASER_TRIPLE, Orientation.NORTH, 150);
        verify(wallAndObjectImageContainer).getLaser(3, false);
    }

    @Test
    public void shouldCombineImageWithTripleVerticalLaserWhenHasLaserInSouth() {
        combineTileService.combineGroundWithWallObject(testImage, FieldObject.LASER_TRIPLE, Orientation.SOUTH, 150);
        verify(wallAndObjectImageContainer).getLaser(3, false);
    }

    @Test
    public void shouldCombineImageWithTripleHorizontalLaserWhenHasLaserInWest() {
        combineTileService.combineGroundWithWallObject(testImage, FieldObject.LASER_TRIPLE, Orientation.WEST, 150);
        verify(wallAndObjectImageContainer).getLaser(3, true);
    }

    @Test
    public void shouldCombineImageWithTripleHorizontalLaserWhenHasLaserInEast() {
        combineTileService.combineGroundWithWallObject(testImage, FieldObject.LASER_TRIPLE, Orientation.EAST, 150);
        verify(wallAndObjectImageContainer).getLaser(3, true);
    }

}