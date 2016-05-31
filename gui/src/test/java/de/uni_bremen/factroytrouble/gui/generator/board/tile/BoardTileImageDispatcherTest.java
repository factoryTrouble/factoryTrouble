package de.uni_bremen.factroytrouble.gui.generator.board.tile;

import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.gameobjects.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static de.uni_bremen.factroytrouble.gui.TestUtil.*;

@RunWith(MockitoJUnitRunner.class)
public class BoardTileImageDispatcherTest {

    @InjectMocks  private BoardTileImageDispatcher boardTileImageDispatcher = new BoardTileImageDispatcher();
    @Mock private FlagTileGenerator flagTileGenerator;
    @Mock private ConveyorBeltTileGenerator conveyorBeltTileGenerator;
    @Mock private TileWallGenerator tileWallGenerator;
    @Mock private TileLaserDispatcher tileLaserDispatcher;
    private Tile tileMock;
    private Wall wallMock;
    private ArgumentCaptor<BufferedImage> bufferedImageArgumentCaptor;

    @Before
    public void setUp() throws Exception {
        boardTileImageDispatcher.init();
        tileMock = mock(Tile.class);
        wallMock = mock(Wall.class);
        when(wallMock.getPusherPhases()).thenReturn(new int[0]);
        bufferedImageArgumentCaptor = ArgumentCaptor.forClass(BufferedImage.class);
    }

    @Test
    public void shouldReturnAHoleImageWhenTileHasAHole() throws Exception {
        when(tileMock.getFieldObject()).thenReturn(mock(Hole.class));
        boardTileImageDispatcher.dispatch(tileMock);

        captureLaserDispatchBufferedImage();
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/void.png")), bufferedImageArgumentCaptor.getValue());
    }

    @Test
    public void shouldReturnAWorkshopImageWhenTileHasAWorkshop() throws Exception {
        when(tileMock.getFieldObject()).thenReturn(mock(Workshop.class));
        boardTileImageDispatcher.dispatch(tileMock);

        captureLaserDispatchBufferedImage();
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/repair.png")), bufferedImageArgumentCaptor.getValue());
    }

    @Test
    public void shouldReturnAClockWiseGearImageWhenTileHasAClockWiseGear() throws Exception {
        Gear gearMock = mock(Gear.class);
        when(tileMock.getFieldObject()).thenReturn(gearMock);
        when(gearMock.rotatesLeft()).thenReturn(false);
        boardTileImageDispatcher.dispatch(tileMock);

        captureLaserDispatchBufferedImage();
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/gear-cw.png")), bufferedImageArgumentCaptor.getValue());
    }

    @Test
    public void shouldReturnACounterClockWiseGearImageWhenTileHasACounterClockWiseGear() throws Exception {
        Gear gearMock = mock(Gear.class);
        when(tileMock.getFieldObject()).thenReturn(gearMock);
        when(gearMock.rotatesLeft()).thenReturn(true);
        boardTileImageDispatcher.dispatch(tileMock);

        captureLaserDispatchBufferedImage();
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/gear-ccw.png")), bufferedImageArgumentCaptor.getValue());
    }

    @Test
    public void shouldReturnAnEmptyTileWhenNothingOnTheTile() throws Exception {
        boardTileImageDispatcher.dispatch(tileMock);

        captureLaserDispatchBufferedImage();
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/empty.png")), bufferedImageArgumentCaptor.getValue());
    }

    @Test
    public void shouldCallTheFlagGeneratorWhenTileHasAFlagFieldObject() {
        when(tileMock.getFieldObject()).thenReturn(mock(Flag.class));
        boardTileImageDispatcher.dispatch(tileMock);
        verify(flagTileGenerator).generateFlagTileImage(tileMock);
    }

    @Test
    public void shouldCallTheConvoyerBeltTileGenereratorWhenFieldObjectIsAnConvoyerBelt() {
        when(tileMock.getFieldObject()).thenReturn(mock(ConveyorBelt.class));
        boardTileImageDispatcher.dispatch(tileMock);
        verify(conveyorBeltTileGenerator).generateImage(tileMock);
    }

    @Test
    public void shouldCallTheWallgeneratorWhenTileHasAWall() {
        List<Wall> walls = new ArrayList<>();
        walls.add(wallMock);
        when(tileMock.getWalls()).thenReturn(walls);

        boardTileImageDispatcher.dispatch(tileMock);
        verify(tileWallGenerator).generateTileWithWall(eq(tileMock), any(BufferedImage.class));
    }

    private void captureLaserDispatchBufferedImage() {
        verify(tileLaserDispatcher).getTileWithLaser(any(Tile.class), bufferedImageArgumentCaptor.capture());
    }

}