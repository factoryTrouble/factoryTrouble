package de.uni_bremen.factroytrouble.gui.generator.board.tile;

import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.gameobjects.Wall;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static de.uni_bremen.factroytrouble.gui.TestUtil.assertImageEquals;
import static org.mockito.Mockito.*;

/**
 * @author Andre
 */
@RunWith(MockitoJUnitRunner.class)
public class TileLaserDispatcherTest {

    private TileLaserDispatcher tileLaserDispatcher;
    @Mock private Tile tile1;
    @Mock private Tile tile2;
    @Mock private Tile tile3;
    @Mock private Wall wall;

    private Map<Orientation, Tile> neighbors1;
    private Map<Orientation, Tile> neighbors2;
    private Map<Orientation, Tile> neighbors3;
    private List<Wall> walls;

    private BufferedImage tileImage;

    private BufferedImage singleVerticalLaser;
    private BufferedImage doubleVerticalLaser;
    private BufferedImage tripleVerticalLaser;

    private BufferedImage singleHorizontalLaser;
    private BufferedImage doubleHorizontalLaser;
    private BufferedImage tripleHorizontalLaser;

    @Before
    public void setUp() throws Exception {
        tileLaserDispatcher = new TileLaserDispatcher();
        tileLaserDispatcher.init();

        neighbors1 = new HashMap<>();
        neighbors2 = new HashMap<>();
        neighbors3 = new HashMap<>();
        walls = new ArrayList<>();
        when(tile1.getNeighbors()).thenReturn(neighbors1);
        when(tile2.getNeighbors()).thenReturn(neighbors2);
        when(tile3.getNeighbors()).thenReturn(neighbors3);

        tileImage = ImageIO.read(getClass().getResourceAsStream("/game/tiles/empty.png"));

        singleVerticalLaser = ImageIO.read(getClass().getResourceAsStream("/game/tiles/singleVerticalLaser.png"));
        doubleVerticalLaser = ImageIO.read(getClass().getResourceAsStream("/game/tiles/doubleVerticalLaser.png"));
        tripleVerticalLaser = ImageIO.read(getClass().getResourceAsStream("/game/tiles/tripleVerticalLaser.png"));

        singleHorizontalLaser = ImageIO.read(getClass().getResourceAsStream("/game/tiles/singleHorizontalLaser.png"));
        doubleHorizontalLaser = ImageIO.read(getClass().getResourceAsStream("/game/tiles/doubleHorizontalLaser.png"));
        tripleHorizontalLaser = ImageIO.read(getClass().getResourceAsStream("/game/tiles/tripleHorizontalLaser.png"));
    }

    @Test
    public void shouldAddASingleLaserInTheNorthForCurrentTile() {
        addWallOnCurrentTile(Orientation.NORTH, 1);
        BufferedImage newTileImageWithLaser = tileLaserDispatcher.getTileWithLaser(tile1, tileImage);
        assertImageEquals(singleVerticalLaser, newTileImageWithLaser);
    }

    @Test
    public void shouldAddADoubleLaserInTheNorthForCurrentTile() {
        addWallOnCurrentTile(Orientation.NORTH, 2);
        BufferedImage newTileImageWithLaser = tileLaserDispatcher.getTileWithLaser(tile1, tileImage);
        assertImageEquals(doubleVerticalLaser, newTileImageWithLaser);
    }

    @Test
    public void shouldAddATripleLaserInTheNorthForCurrentTile() {
        addWallOnCurrentTile(Orientation.NORTH, 3);
        BufferedImage newTileImageWithLaser = tileLaserDispatcher.getTileWithLaser(tile1, tileImage);
        assertImageEquals(tripleVerticalLaser, newTileImageWithLaser);
    }

    @Test
    public void shouldAddASingleLaserInTheSouthForCurrentTile() {
        addWallOnCurrentTile(Orientation.SOUTH, 1);
        BufferedImage newTileImageWithLaser = tileLaserDispatcher.getTileWithLaser(tile1, tileImage);
        assertImageEquals(singleVerticalLaser, newTileImageWithLaser);
    }

    @Test
    public void shouldAddADoubleLaserInTheSouthForCurrentTile() {
        addWallOnCurrentTile(Orientation.SOUTH, 2);
        BufferedImage newTileImageWithLaser = tileLaserDispatcher.getTileWithLaser(tile1, tileImage);
        assertImageEquals(doubleVerticalLaser, newTileImageWithLaser);
    }

    @Test
    public void shouldAddATripleLaserInTheSouthForCurrentTile() {
        addWallOnCurrentTile(Orientation.SOUTH, 3);
        BufferedImage newTileImageWithLaser = tileLaserDispatcher.getTileWithLaser(tile1, tileImage);
        assertImageEquals(tripleVerticalLaser, newTileImageWithLaser);
    }

    @Test
    public void shouldAddASingleLaserInTheWestForCurrentTile() {
        addWallOnCurrentTile(Orientation.WEST, 1);
        BufferedImage newTileImageWithLaser = tileLaserDispatcher.getTileWithLaser(tile1, tileImage);
        assertImageEquals(singleHorizontalLaser, newTileImageWithLaser);
    }

    @Test
    public void shouldAddADoubleLaserInTheWestForCurrentTile() {
        addWallOnCurrentTile(Orientation.WEST, 2);
        BufferedImage newTileImageWithLaser = tileLaserDispatcher.getTileWithLaser(tile1, tileImage);
        assertImageEquals(doubleHorizontalLaser, newTileImageWithLaser);
    }

    @Test
    public void shouldAddATripleLaserInTheWestForCurrentTile() {
        addWallOnCurrentTile(Orientation.WEST, 3);
        BufferedImage newTileImageWithLaser = tileLaserDispatcher.getTileWithLaser(tile1, tileImage);
        assertImageEquals(tripleHorizontalLaser, newTileImageWithLaser);
    }

    @Test
    public void shouldAddASingleLaserInTheEastForCurrentTile() {
        addWallOnCurrentTile(Orientation.EAST, 1);
        BufferedImage newTileImageWithLaser = tileLaserDispatcher.getTileWithLaser(tile1, tileImage);
        assertImageEquals(singleHorizontalLaser, newTileImageWithLaser);
    }

    @Test
    public void shouldAddADoubleLaserInTheEastForCurrentTile() {
        addWallOnCurrentTile(Orientation.EAST, 2);
        BufferedImage newTileImageWithLaser = tileLaserDispatcher.getTileWithLaser(tile1, tileImage);
        assertImageEquals(doubleHorizontalLaser, newTileImageWithLaser);
    }

    @Test
    public void shouldAddATripleLaserInTheEastForCurrentTile() {
        addWallOnCurrentTile(Orientation.EAST, 3);
        BufferedImage newTileImageWithLaser = tileLaserDispatcher.getTileWithLaser(tile1, tileImage);
        assertImageEquals(tripleHorizontalLaser, newTileImageWithLaser);
    }

    @Test
    public void shouldAddASingleLaserFromTheNorthWhenNeighborHasASingleLaser() {
        addNeighborInDirection(Orientation.NORTH, 1, true);
        BufferedImage newTileImageWithLaser = tileLaserDispatcher.getTileWithLaser(tile1, tileImage);
        assertImageEquals(singleVerticalLaser, newTileImageWithLaser);
    }

    @Test
    public void shouldAddADoubleLaserFromTheNorthWhenNeighborHasASingleLaser() {
        addNeighborInDirection(Orientation.NORTH, 2, true);
        BufferedImage newTileImageWithLaser = tileLaserDispatcher.getTileWithLaser(tile1, tileImage);
        assertImageEquals(doubleVerticalLaser, newTileImageWithLaser);
    }

    @Test
    public void shouldAddATripleLaserFromTheNorthWhenNeighborHasASingleLaser() {
        addNeighborInDirection(Orientation.NORTH, 3, true);
        BufferedImage newTileImageWithLaser = tileLaserDispatcher.getTileWithLaser(tile1, tileImage);
        assertImageEquals(tripleVerticalLaser, newTileImageWithLaser);
    }

    @Test
    public void shouldAddASingleLaserFromTheSouthWhenNeighborHasASingleLaser() {
        addNeighborInDirection(Orientation.SOUTH, 1, true);
        BufferedImage newTileImageWithLaser = tileLaserDispatcher.getTileWithLaser(tile1, tileImage);
        assertImageEquals(singleVerticalLaser, newTileImageWithLaser);
    }

    @Test
    public void shouldAddADoubleLaserFromTheSouthWhenNeighborHasASingleLaser() {
        addNeighborInDirection(Orientation.SOUTH, 2, true);
        BufferedImage newTileImageWithLaser = tileLaserDispatcher.getTileWithLaser(tile1, tileImage);
        assertImageEquals(doubleVerticalLaser, newTileImageWithLaser);
    }

    @Test
    public void shouldAddATripleLaserFromTheSouthWhenNeighborHasASingleLaser() {
        addNeighborInDirection(Orientation.SOUTH, 3, true);
        BufferedImage newTileImageWithLaser = tileLaserDispatcher.getTileWithLaser(tile1, tileImage);
        assertImageEquals(tripleVerticalLaser, newTileImageWithLaser);
    }

    @Test
    public void shouldAddASingleLaserFromTheWestWhenNeighborHasASingleLaser() {
        addNeighborInDirection(Orientation.WEST, 1, true);
        BufferedImage newTileImageWithLaser = tileLaserDispatcher.getTileWithLaser(tile1, tileImage);
        assertImageEquals(singleHorizontalLaser, newTileImageWithLaser);
    }

    @Test
    public void shouldAddADoubleLaserFromTheWestWhenNeighborHasASingleLaser() {
        addNeighborInDirection(Orientation.WEST, 2, true);
        BufferedImage newTileImageWithLaser = tileLaserDispatcher.getTileWithLaser(tile1, tileImage);
        assertImageEquals(doubleHorizontalLaser, newTileImageWithLaser);
    }

    @Test
    public void shouldAddATripleLaserFromTheWestWhenNeighborHasASingleLaser() {
        addNeighborInDirection(Orientation.WEST, 3, true);
        BufferedImage newTileImageWithLaser = tileLaserDispatcher.getTileWithLaser(tile1, tileImage);
        assertImageEquals(tripleHorizontalLaser, newTileImageWithLaser);
    }

    @Test
    public void shouldAddASingleLaserFromTheEastWhenNeighborHasASingleLaser() {
        addNeighborInDirection(Orientation.EAST, 1, true);
        BufferedImage newTileImageWithLaser = tileLaserDispatcher.getTileWithLaser(tile1, tileImage);
        assertImageEquals(singleHorizontalLaser, newTileImageWithLaser);
    }

    @Test
    public void shouldAddADoubleLaserFromTheEastWhenNeighborHasASingleLaser() {
        addNeighborInDirection(Orientation.EAST, 2, true);
        BufferedImage newTileImageWithLaser = tileLaserDispatcher.getTileWithLaser(tile1, tileImage);
        assertImageEquals(doubleHorizontalLaser, newTileImageWithLaser);
    }

    @Test
    public void shouldAddATripleLaserFromTheEastWhenNeighborHasASingleLaser() {
        addNeighborInDirection(Orientation.EAST, 3, true);
        BufferedImage newTileImageWithLaser = tileLaserDispatcher.getTileWithLaser(tile1, tileImage);
        assertImageEquals(tripleHorizontalLaser, newTileImageWithLaser);
    }

    @Test
    public void shouldAddASingleLaserFromTheNorthWhenNeighborHasASingleLaserAndHasNoNeighbor() {
        addNeighborInDirection(Orientation.NORTH, 1, false);
        BufferedImage newTileImageWithLaser = tileLaserDispatcher.getTileWithLaser(tile1, tileImage);
        assertImageEquals(singleVerticalLaser, newTileImageWithLaser);
    }

    @Test
    public void shouldAddADoubleLaserFromTheNorthWhenNeighborHasASingleLaserAndHasNoNeighbor() {
        addNeighborInDirection(Orientation.NORTH, 2, false);
        BufferedImage newTileImageWithLaser = tileLaserDispatcher.getTileWithLaser(tile1, tileImage);
        assertImageEquals(doubleVerticalLaser, newTileImageWithLaser);
    }

    @Test
    public void shouldAddATripleLaserFromTheNorthWhenNeighborHasASingleLaserAndHasNoNeighbor() {
        addNeighborInDirection(Orientation.NORTH, 3, false);
        BufferedImage newTileImageWithLaser = tileLaserDispatcher.getTileWithLaser(tile1, tileImage);
        assertImageEquals(tripleVerticalLaser, newTileImageWithLaser);
    }

    @Test
    public void shouldAddASingleLaserFromTheSouthWhenNeighborHasASingleLaserAndHasNoNeighbor() {
        addNeighborInDirection(Orientation.SOUTH, 1, false);
        BufferedImage newTileImageWithLaser = tileLaserDispatcher.getTileWithLaser(tile1, tileImage);
        assertImageEquals(singleVerticalLaser, newTileImageWithLaser);
    }

    @Test
    public void shouldAddADoubleLaserFromTheSouthWhenNeighborHasASingleLaserAndHasNoNeighbor() {
        addNeighborInDirection(Orientation.SOUTH, 2, false);
        BufferedImage newTileImageWithLaser = tileLaserDispatcher.getTileWithLaser(tile1, tileImage);
        assertImageEquals(doubleVerticalLaser, newTileImageWithLaser);
    }

    @Test
    public void shouldAddATripleLaserFromTheSouthWhenNeighborHasASingleLaserAndHasNoNeighbor() {
        addNeighborInDirection(Orientation.SOUTH, 3, false);
        BufferedImage newTileImageWithLaser = tileLaserDispatcher.getTileWithLaser(tile1, tileImage);
        assertImageEquals(tripleVerticalLaser, newTileImageWithLaser);
    }

    @Test
    public void shouldAddASingleLaserFromTheWestWhenNeighborHasASingleLaserAndHasNoNeighbor() {
        addNeighborInDirection(Orientation.WEST, 1, false);
        BufferedImage newTileImageWithLaser = tileLaserDispatcher.getTileWithLaser(tile1, tileImage);
        assertImageEquals(singleHorizontalLaser, newTileImageWithLaser);
    }

    @Test
    public void shouldAddADoubleLaserFromTheWestWhenNeighborHasASingleLaserAndHasNoNeighbor() {
        addNeighborInDirection(Orientation.WEST, 2, false);
        BufferedImage newTileImageWithLaser = tileLaserDispatcher.getTileWithLaser(tile1, tileImage);
        assertImageEquals(doubleHorizontalLaser, newTileImageWithLaser);
    }

    @Test
    public void shouldAddATripleLaserFromTheWestWhenNeighborHasASingleLaserAndHasNoNeighbor() {
        addNeighborInDirection(Orientation.WEST, 3, false);
        BufferedImage newTileImageWithLaser = tileLaserDispatcher.getTileWithLaser(tile1, tileImage);
        assertImageEquals(tripleHorizontalLaser, newTileImageWithLaser);
    }

    @Test
    public void shouldAddASingleLaserFromTheEastWhenNeighborHasASingleLaserAndHasNoNeighbor() {
        addNeighborInDirection(Orientation.EAST, 1, false);
        BufferedImage newTileImageWithLaser = tileLaserDispatcher.getTileWithLaser(tile1, tileImage);
        assertImageEquals(singleHorizontalLaser, newTileImageWithLaser);
    }

    @Test
    public void shouldAddADoubleLaserFromTheEastWhenNeighborHasASingleLaserAndHasNoNeighbor() {
        addNeighborInDirection(Orientation.EAST, 2, false);
        BufferedImage newTileImageWithLaser = tileLaserDispatcher.getTileWithLaser(tile1, tileImage);
        assertImageEquals(doubleHorizontalLaser, newTileImageWithLaser);
    }

    @Test
    public void shouldAddATripleLaserFromTheEastWhenNeighborHasASingleLaserAndHasNoNeighbor() {
        addNeighborInDirection(Orientation.EAST, 3, false);
        BufferedImage newTileImageWithLaser = tileLaserDispatcher.getTileWithLaser(tile1, tileImage);
        assertImageEquals(tripleHorizontalLaser, newTileImageWithLaser);
    }

    @Test
    public void shouldNotAddALaserWhenNeighborHasWallInOpposideDirectionInTheNorth() {
        addNeighborInDirectionAndWallInOppsoiteDirection(Orientation.NORTH);
        BufferedImage newTileImageWithLaser = tileLaserDispatcher.getTileWithLaser(tile1, tileImage);
        assertImageEquals(tileImage, newTileImageWithLaser);
    }

    @Test
    public void shouldNotAddALaserWhenNeighborHasWallInOpposideDirectionInTheSouth() {
        addNeighborInDirectionAndWallInOppsoiteDirection(Orientation.SOUTH);
        BufferedImage newTileImageWithLaser = tileLaserDispatcher.getTileWithLaser(tile1, tileImage);
        assertImageEquals(tileImage, newTileImageWithLaser);
    }

    @Test
    public void shouldNotAddALaserWhenNeighborHasWallInOpposideDirectionInTheWest() {
        addNeighborInDirectionAndWallInOppsoiteDirection(Orientation.WEST);
        BufferedImage newTileImageWithLaser = tileLaserDispatcher.getTileWithLaser(tile1, tileImage);
        assertImageEquals(tileImage, newTileImageWithLaser);
    }

    @Test
    public void shouldNotAddALaserWhenNeighborHasWallInOpposideDirectionInTheEast() {
        addNeighborInDirectionAndWallInOppsoiteDirection(Orientation.EAST);
        BufferedImage newTileImageWithLaser = tileLaserDispatcher.getTileWithLaser(tile1, tileImage);
        assertImageEquals(tileImage, newTileImageWithLaser);
    }

    @Test
    public void shouldNotAddALaserWhenNeighborHasNoLaserInTheNorth() {
        addNeighborInDirection(Orientation.NORTH, 0, true);
        BufferedImage newTileImageWithLaser = tileLaserDispatcher.getTileWithLaser(tile1, tileImage);
        assertImageEquals(tileImage, newTileImageWithLaser);
    }

    @Test
    public void shouldNotAddALaserWhenNeighborHasNoLaserInTheSouth() {
        addNeighborInDirection(Orientation.SOUTH, 0, true);
        BufferedImage newTileImageWithLaser = tileLaserDispatcher.getTileWithLaser(tile1, tileImage);
        assertImageEquals(tileImage, newTileImageWithLaser);
    }

    @Test
    public void shouldNotAddALaserWhenNeighborHasNoLaserInTheWest() {
        addNeighborInDirection(Orientation.WEST, 0, true);
        BufferedImage newTileImageWithLaser = tileLaserDispatcher.getTileWithLaser(tile1, tileImage);
        assertImageEquals(tileImage, newTileImageWithLaser);
    }

    @Test
    public void shouldNotAddALaserWhenNeighborHasNoLaserInTheEast() {
        addNeighborInDirection(Orientation.EAST, 0, true);
        BufferedImage newTileImageWithLaser = tileLaserDispatcher.getTileWithLaser(tile1, tileImage);
        assertImageEquals(tileImage, newTileImageWithLaser);
    }

    @Test
    public void shouldNotAddAnLaserWhenCurrentTileHasWallInNorth() {
        addWallOnCurrentTile(Orientation.NORTH, 0);
        BufferedImage newTileImageWithLaser = tileLaserDispatcher.getTileWithLaser(tile1, tileImage);
        assertImageEquals(tileImage, newTileImageWithLaser);
    }

    @Test
    public void shouldNotAddAnLaserWhenCurrentTileHasWallInSouth() {
        addWallOnCurrentTile(Orientation.SOUTH, 0);
        BufferedImage newTileImageWithLaser = tileLaserDispatcher.getTileWithLaser(tile1, tileImage);
        assertImageEquals(tileImage, newTileImageWithLaser);
    }

    @Test
    public void shouldNotAddAnLaserWhenCurrentTileHasWallInWest() {
        addWallOnCurrentTile(Orientation.WEST, 0);
        BufferedImage newTileImageWithLaser = tileLaserDispatcher.getTileWithLaser(tile1, tileImage);
        assertImageEquals(tileImage, newTileImageWithLaser);
    }

    @Test
    public void shouldNotAddAnLaserWhenCurrentTileHasWallInEast() {
        addWallOnCurrentTile(Orientation.EAST, 0);
        BufferedImage newTileImageWithLaser = tileLaserDispatcher.getTileWithLaser(tile1, tileImage);
        assertImageEquals(tileImage, newTileImageWithLaser);
    }

    private void addNeighborInDirection(Orientation orientation, Integer laserCount, boolean neighborHasNeighbor) {
        neighbors1.put(orientation, tile2);
        if(neighborHasNeighbor) {
            neighbors2.put(orientation, tile3);
        }

        when(wall.getOrientation()).thenReturn(orientation);
        when(wall.hasLaser()).thenReturn(laserCount);
        when(tile2.getWalls()).thenReturn(walls);
        walls.add(wall);
    }

    private void addNeighborInDirectionAndWallInOppsoiteDirection(Orientation orientation) {
        neighbors1.put(orientation, tile2);
        neighbors2.put(orientation, tile3);

        when(wall.getOrientation()).thenReturn(Orientation.getOppositeDirection(orientation));
        when(wall.hasLaser()).thenReturn(1);
        when(tile2.getWalls()).thenReturn(walls);
        walls.add(wall);
    }

    private void addWallOnCurrentTile(Orientation orientation, Integer laserCount) {
        when(wall.getOrientation()).thenReturn(orientation);
        when(wall.hasLaser()).thenReturn(laserCount);
        when(tile1.getWalls()).thenReturn(walls);
        walls.add(wall);
    }

}