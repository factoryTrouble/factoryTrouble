package de.uni_bremen.factroytrouble.gui.generator.board.tile;

import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.gameobjects.Wall;
import de.uni_bremen.factroytrouble.gui.generator.board.BoardGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static de.uni_bremen.factroytrouble.gui.TestUtil.assertImageEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TileWallGeneratorTest {

    @Mock private WallPusherDispatcher wallPusherDispatcher;
    @InjectMocks private TileWallGenerator tileWallGenerator;
    private Tile tileMock;
    private Wall wallMock;
    private BufferedImage emptyTileImage;

    @Before
    public void setUp() throws Exception {
        tileWallGenerator.init();
        tileMock = mock(Tile.class);
        wallMock = mock(Wall.class);
        when(wallMock.getPusherPhases()).thenReturn(new int[0]);
        emptyTileImage = ImageIO.read(getClass().getResourceAsStream("/game/tiles/empty.png"));
    }

    @Test
    public void shouldPlaceAWallInNorth() throws Exception {
        List<Wall> walls = new ArrayList<>();
        walls.add(wallMock);

        when(tileMock.getWalls()).thenReturn(walls);
        when(wallMock.getOrientation()).thenReturn(Orientation.NORTH);


        BufferedImage image = tileWallGenerator.generateTileWithWall(tileMock, emptyTileImage);
        assertImageEquals(getWallImage(Orientation.NORTH), image);
    }

    @Test
    public void shouldPlaceAWallInSouth() throws Exception {
        List<Wall> walls = new ArrayList<>();
        walls.add(wallMock);

        when(tileMock.getWalls()).thenReturn(walls);
        when(wallMock.getOrientation()).thenReturn(Orientation.SOUTH);

        BufferedImage image = tileWallGenerator.generateTileWithWall(tileMock, emptyTileImage);
        assertImageEquals(getWallImage(Orientation.SOUTH), image);
    }

    @Test
    public void shouldPlaceAWallInWest() throws Exception {
        List<Wall> walls = new ArrayList<>();
        walls.add(wallMock);

        when(tileMock.getWalls()).thenReturn(walls);
        when(wallMock.getOrientation()).thenReturn(Orientation.WEST);

        BufferedImage image = tileWallGenerator.generateTileWithWall(tileMock, emptyTileImage);
        assertImageEquals(getWallImage(Orientation.WEST), image);
    }

    @Test
    public void shouldPlaceAWallInEast() throws Exception {
        List<Wall> walls = new ArrayList<>();
        walls.add(wallMock);

        when(tileMock.getWalls()).thenReturn(walls);
        when(wallMock.getOrientation()).thenReturn(Orientation.EAST);

        BufferedImage image = tileWallGenerator.generateTileWithWall(tileMock, emptyTileImage);
        assertImageEquals(getWallImage(Orientation.EAST), image);
    }

    @Test
    public void shouldCallThePusherDispatcher() {
        List<Wall> walls = new ArrayList<>();
        walls.add(wallMock);

        when(tileMock.getWalls()).thenReturn(walls);
        when(wallMock.getOrientation()).thenReturn(Orientation.EAST);
        when(wallMock.hasLaser()).thenReturn(0);

        tileWallGenerator.generateTileWithWall(tileMock, emptyTileImage);
        verify(wallPusherDispatcher).dispatchPusher(wallMock);
    }

    private BufferedImage getWallImage(Orientation orientation) throws Exception {
        BufferedImage combinedFieldWithWall = new BufferedImage(BoardGenerator.DEFAULT_TILE_WIDTH, BoardGenerator.DEFAULT_TILE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics combinedGraphic = combinedFieldWithWall.getGraphics();
        combinedGraphic.drawImage(ImageIO.read(getClass().getResourceAsStream("/game/tiles/empty.png")), 0, 0, null);

        if(orientation.equals(Orientation.NORTH)) {
            combinedGraphic.drawImage(ImageIO.read(getClass().getResourceAsStream("/game/tiles/wall/wall_up.png")), 0, 0, null);
        }
        if(orientation.equals(Orientation.SOUTH)) {
            combinedGraphic.drawImage(ImageIO.read(getClass().getResourceAsStream("/game/tiles/wall/wall_down.png")), 0, 0, null);
        }
        if(orientation.equals(Orientation.WEST)) {
            combinedGraphic.drawImage(ImageIO.read(getClass().getResourceAsStream("/game/tiles/wall/wall_left.png")), 0, 0, null);
        }
        if(orientation.equals(Orientation.EAST)) {
            combinedGraphic.drawImage(ImageIO.read(getClass().getResourceAsStream("/game/tiles/wall/wall_right.png")), 0, 0, null);
        }
        return combinedFieldWithWall;
    }

    private BufferedImage addSingleLaser(BufferedImage tileWithWall, boolean isHorizontal) throws IOException {
        BufferedImage combinedFieldWithWallAndLaser = new BufferedImage(BoardGenerator.DEFAULT_TILE_WIDTH, BoardGenerator.DEFAULT_TILE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics combinedGraphic = combinedFieldWithWallAndLaser.getGraphics();
        combinedGraphic.drawImage(tileWithWall, 0, 0, null);

        if(isHorizontal) {
            combinedGraphic.drawImage(ImageIO.read(getClass().getResourceAsStream("/game/tiles/laser/laserHorizontal.png")), 0, 0, null);
        } else {
            combinedGraphic.drawImage(ImageIO.read(getClass().getResourceAsStream("/game/tiles/laser/laser.png")), 0, 0, null);

        }

        return combinedFieldWithWallAndLaser;
    }

    private BufferedImage addDoubleLaser(BufferedImage tileWithWall, boolean isHorizontal) throws IOException {
        BufferedImage combinedFieldWithWallAndLaser = new BufferedImage(BoardGenerator.DEFAULT_TILE_WIDTH, BoardGenerator.DEFAULT_TILE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics combinedGraphic = combinedFieldWithWallAndLaser.getGraphics();
        combinedGraphic.drawImage(tileWithWall, 0, 0, null);

        if(isHorizontal) {
            combinedGraphic.drawImage(ImageIO.read(getClass().getResourceAsStream("/game/tiles/laser/doublelaserHorizontal.png")), 0, 0, null);
        } else {
            combinedGraphic.drawImage(ImageIO.read(getClass().getResourceAsStream("/game/tiles/laser/doublelaser.png")), 0, 0, null);

        }

        return combinedFieldWithWallAndLaser;
    }

    private BufferedImage addTribleLaser(BufferedImage tileWithWall, boolean isHorizontal) throws IOException {
        BufferedImage combinedFieldWithWallAndLaser = new BufferedImage(BoardGenerator.DEFAULT_TILE_WIDTH, BoardGenerator.DEFAULT_TILE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics combinedGraphic = combinedFieldWithWallAndLaser.getGraphics();
        combinedGraphic.drawImage(tileWithWall, 0, 0, null);

        if(isHorizontal) {
            combinedGraphic.drawImage(ImageIO.read(getClass().getResourceAsStream("/game/tiles/laser/triplelaserHorizontal.png")), 0, 0, null);
        } else {
            combinedGraphic.drawImage(ImageIO.read(getClass().getResourceAsStream("/game/tiles/laser/triplelaser.png")), 0, 0, null);

        }

        return combinedFieldWithWallAndLaser;
    }
    
}