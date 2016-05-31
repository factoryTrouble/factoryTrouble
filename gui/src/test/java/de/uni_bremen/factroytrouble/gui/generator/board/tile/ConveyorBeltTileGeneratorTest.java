package de.uni_bremen.factroytrouble.gui.generator.board.tile;

import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.gameobjects.ConveyorBelt;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import static de.uni_bremen.factroytrouble.gui.TestUtil.assertImageEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConveyorBeltTileGeneratorTest {

    @InjectMocks private ConveyorBeltTileGenerator conveyorBeltTileGenerator = new ConveyorBeltTileGenerator();
    private Map<Orientation, Tile> neighbors = new HashMap<>();
    private Tile tileMock;
    private Tile tileMock2;
    private Tile tileMock3;
    private ConveyorBelt conveyorBeltMock;
    private ConveyorBelt conveyorBeltMock2;
    private ConveyorBelt conveyorBeltMock3;

    @Before
    public void setUp() throws Exception {
        conveyorBeltTileGenerator.init();
        tileMock = mock(Tile.class);
        tileMock2 = mock(Tile.class);
        tileMock3 = mock(Tile.class);
        conveyorBeltMock = mock(ConveyorBelt.class);
        conveyorBeltMock2 = mock(ConveyorBelt.class);
        conveyorBeltMock3 = mock(ConveyorBelt.class);
        when(tileMock.getNeighbors()).thenReturn(neighbors);
    }
    
    @Test
    public void shouldReturnANorthConeyorBeltImageWhenTileHasANorthConeyorBelt() throws Exception {
        when(tileMock.getFieldObject()).thenReturn(conveyorBeltMock);
        when(conveyorBeltMock.getOrientation()).thenReturn(Orientation.NORTH);
        when(conveyorBeltMock.isExpress()).thenReturn(false);

        BufferedImage image = conveyorBeltTileGenerator.generateImage(tileMock);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/conveyorBelt/straight/roller_straight_up.png")), image);
    }

    @Test
    public void shouldReturnASouthConeyorBeltImageWhenTileHasASouthConeyorBelt() throws Exception {
        when(tileMock.getFieldObject()).thenReturn(conveyorBeltMock);
        when(conveyorBeltMock.getOrientation()).thenReturn(Orientation.SOUTH);
        when(conveyorBeltMock.isExpress()).thenReturn(false);

        BufferedImage image = conveyorBeltTileGenerator.generateImage(tileMock);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/conveyorBelt/straight/roller_straight_down.png")), image);
    }

    @Test
    public void shouldReturnAWestConeyorBeltImageWhenTileHasAWestConeyorBelt() throws Exception {
        when(tileMock.getFieldObject()).thenReturn(conveyorBeltMock);
        when(conveyorBeltMock.getOrientation()).thenReturn(Orientation.WEST);
        when(conveyorBeltMock.isExpress()).thenReturn(false);

        BufferedImage image = conveyorBeltTileGenerator.generateImage(tileMock);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/conveyorBelt/straight/roller_straight_left.png")), image);
    }

    @Test
    public void shouldReturnAnEastConeyorBeltImageWhenTileHasAnEastConeyorBelt() throws Exception {
        when(tileMock.getFieldObject()).thenReturn(conveyorBeltMock);
        when(conveyorBeltMock.getOrientation()).thenReturn(Orientation.EAST);
        when(conveyorBeltMock.isExpress()).thenReturn(false);

        BufferedImage image = conveyorBeltTileGenerator.generateImage(tileMock);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/conveyorBelt/straight/roller_straight_right.png")), image);
    }

    @Test
    public void shouldReturnAnExpressNorthConeyorBeltImageWhenTileHasAnExpressNorthConeyorBelt() throws Exception {
        when(tileMock.getFieldObject()).thenReturn(conveyorBeltMock);
        when(conveyorBeltMock.getOrientation()).thenReturn(Orientation.NORTH);
        when(conveyorBeltMock.isExpress()).thenReturn(true);

        BufferedImage image = conveyorBeltTileGenerator.generateImage(tileMock);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/expressConveyorBelt/straight/roller_express_straight_up.png")), image);
    }

    @Test
    public void shouldReturnAnExpressSouthConeyorBeltImageWhenTileHasAnExpressSouthConeyorBelt() throws Exception {
        when(tileMock.getFieldObject()).thenReturn(conveyorBeltMock);
        when(conveyorBeltMock.getOrientation()).thenReturn(Orientation.SOUTH);
        when(conveyorBeltMock.isExpress()).thenReturn(true);

        BufferedImage image = conveyorBeltTileGenerator.generateImage(tileMock);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/expressConveyorBelt/straight/roller_express_straight_down.png")), image);
    }

    @Test
    public void shouldReturnAnExpressWestConeyorBeltImageWhenTileHasAnExpressWestConeyorBelt() throws Exception {
        when(tileMock.getFieldObject()).thenReturn(conveyorBeltMock);
        when(conveyorBeltMock.getOrientation()).thenReturn(Orientation.WEST);
        when(conveyorBeltMock.isExpress()).thenReturn(true);

        BufferedImage image = conveyorBeltTileGenerator.generateImage(tileMock);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/expressConveyorBelt/straight/roller_express_straight_left.png")), image);
    }

    @Test
    public void shouldReturnAnExpressEastConeyorBeltImageWhenTileHasAnExpressEastConeyorBelt() throws Exception {
        when(tileMock.getFieldObject()).thenReturn(conveyorBeltMock);
        when(conveyorBeltMock.getOrientation()).thenReturn(Orientation.EAST);
        when(conveyorBeltMock.isExpress()).thenReturn(true);

        BufferedImage image = conveyorBeltTileGenerator.generateImage(tileMock);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/expressConveyorBelt/straight/roller_express_straight_right.png")), image);
    }

    @Test
    public void shouldReturnACurveConveyorFromDownToLeftBeltWhenNeighborIsConveyorBeltToo() throws Exception{
        when(tileMock.getFieldObject()).thenReturn(conveyorBeltMock);
        when(conveyorBeltMock.getOrientation()).thenReturn(Orientation.WEST);
        neighbors.put(Orientation.SOUTH, tileMock2);
        when(tileMock2.getFieldObject()).thenReturn(conveyorBeltMock2);
        when(conveyorBeltMock2.getOrientation()).thenReturn(Orientation.NORTH);

        BufferedImage image = conveyorBeltTileGenerator.generateImage(tileMock);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/conveyorBelt/leftCurve/roller_ccw_left.png")), image);
    }

    @Test
    public void shouldReturnACurveConveyorFromDownToRightBeltWhenNeighborIsConveyorBeltToo() throws Exception{
        when(tileMock.getFieldObject()).thenReturn(conveyorBeltMock);
        when(conveyorBeltMock.getOrientation()).thenReturn(Orientation.EAST);
        neighbors.put(Orientation.SOUTH, tileMock2);
        when(tileMock2.getFieldObject()).thenReturn(conveyorBeltMock2);
        when(conveyorBeltMock2.getOrientation()).thenReturn(Orientation.NORTH);

        BufferedImage image = conveyorBeltTileGenerator.generateImage(tileMock);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/conveyorBelt/rightCurve/roller_cw_right.png")), image);
    }

    @Test
    public void shouldReturnACurveConveyorFromUpToLeftBeltWhenNeighborIsConveyorBeltToo() throws Exception{
        when(tileMock.getFieldObject()).thenReturn(conveyorBeltMock);
        when(conveyorBeltMock.getOrientation()).thenReturn(Orientation.WEST);
        neighbors.put(Orientation.NORTH, tileMock2);
        when(tileMock2.getFieldObject()).thenReturn(conveyorBeltMock2);
        when(conveyorBeltMock2.getOrientation()).thenReturn(Orientation.SOUTH);

        BufferedImage image = conveyorBeltTileGenerator.generateImage(tileMock);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/conveyorBelt/rightCurve/roller_cw_left.png")), image);
    }

    @Test
    public void shouldReturnACurveConveyorFromUpToRightBeltWhenNeighborIsConveyorBeltToo() throws Exception{
        when(tileMock.getFieldObject()).thenReturn(conveyorBeltMock);
        when(conveyorBeltMock.getOrientation()).thenReturn(Orientation.EAST);
        neighbors.put(Orientation.NORTH, tileMock2);
        when(tileMock2.getFieldObject()).thenReturn(conveyorBeltMock2);
        when(conveyorBeltMock2.getOrientation()).thenReturn(Orientation.SOUTH);

        BufferedImage image = conveyorBeltTileGenerator.generateImage(tileMock);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/conveyorBelt/leftCurve/roller_ccw_right.png")), image);
    }

    @Test
    public void shouldReturnACurveConveyorFromLeftToDownBeltWhenNeighborIsConveyorBeltToo() throws Exception{
        when(tileMock.getFieldObject()).thenReturn(conveyorBeltMock);
        when(conveyorBeltMock.getOrientation()).thenReturn(Orientation.SOUTH);
        neighbors.put(Orientation.WEST, tileMock2);
        when(tileMock2.getFieldObject()).thenReturn(conveyorBeltMock2);
        when(conveyorBeltMock2.getOrientation()).thenReturn(Orientation.EAST);

        BufferedImage image = conveyorBeltTileGenerator.generateImage(tileMock);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/conveyorBelt/rightCurve/roller_cw_down.png")), image);
    }

    @Test
    public void shouldReturnACurveConveyorFromLeftToUpBeltWhenNeighborIsConveyorBeltToo() throws Exception{
        when(tileMock.getFieldObject()).thenReturn(conveyorBeltMock);
        when(conveyorBeltMock.getOrientation()).thenReturn(Orientation.NORTH);
        neighbors.put(Orientation.WEST, tileMock2);
        when(tileMock2.getFieldObject()).thenReturn(conveyorBeltMock2);
        when(conveyorBeltMock2.getOrientation()).thenReturn(Orientation.EAST);

        BufferedImage image = conveyorBeltTileGenerator.generateImage(tileMock);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/conveyorBelt/leftCurve/roller_ccw_up.png")), image);
    }

    @Test
    public void shouldReturnACurveConveyorFromRightToDownBeltWhenNeighborIsConveyorBeltToo() throws Exception{
        when(tileMock.getFieldObject()).thenReturn(conveyorBeltMock);
        when(conveyorBeltMock.getOrientation()).thenReturn(Orientation.SOUTH);
        neighbors.put(Orientation.EAST, tileMock2);
        when(tileMock2.getFieldObject()).thenReturn(conveyorBeltMock2);
        when(conveyorBeltMock2.getOrientation()).thenReturn(Orientation.WEST);

        BufferedImage image = conveyorBeltTileGenerator.generateImage(tileMock);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/conveyorBelt/leftCurve/roller_ccw_down.png")), image);
    }

    @Test
    public void shouldReturnACurveConveyorFromRightToUpBeltWhenNeighborIsConveyorBeltToo() throws Exception{
        when(tileMock.getFieldObject()).thenReturn(conveyorBeltMock);
        when(conveyorBeltMock.getOrientation()).thenReturn(Orientation.NORTH);
        neighbors.put(Orientation.EAST, tileMock2);
        when(tileMock2.getFieldObject()).thenReturn(conveyorBeltMock2);
        when(conveyorBeltMock2.getOrientation()).thenReturn(Orientation.WEST);

        BufferedImage image = conveyorBeltTileGenerator.generateImage(tileMock);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/conveyorBelt/rightCurve/roller_cw_up.png")), image);
    }

    @Test
    public void shouldReturnACurveExpressConveyorFromDownToRightBeltWhenNeighborIsConveyorBeltToo() throws Exception{
        when(tileMock.getFieldObject()).thenReturn(conveyorBeltMock);
        when(conveyorBeltMock.getOrientation()).thenReturn(Orientation.EAST);
        when(conveyorBeltMock.isExpress()).thenReturn(true);
        neighbors.put(Orientation.SOUTH, tileMock2);
        when(tileMock2.getFieldObject()).thenReturn(conveyorBeltMock2);
        when(conveyorBeltMock2.getOrientation()).thenReturn(Orientation.NORTH);

        BufferedImage image = conveyorBeltTileGenerator.generateImage(tileMock);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/expressConveyorBelt/rightCurve/roller_express_cw_right.png")), image);
    }

    @Test
    public void shouldReturnACurveExpressConveyorFromUpToLeftBeltWhenNeighborIsConveyorBeltToo() throws Exception{
        when(tileMock.getFieldObject()).thenReturn(conveyorBeltMock);
        when(conveyorBeltMock.getOrientation()).thenReturn(Orientation.WEST);
        when(conveyorBeltMock.isExpress()).thenReturn(true);
        neighbors.put(Orientation.NORTH, tileMock2);
        when(tileMock2.getFieldObject()).thenReturn(conveyorBeltMock2);
        when(conveyorBeltMock2.getOrientation()).thenReturn(Orientation.SOUTH);

        BufferedImage image = conveyorBeltTileGenerator.generateImage(tileMock);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/expressConveyorBelt/rightCurve/roller_express_cw_left.png")), image);
    }

    @Test
    public void shouldReturnAExpressCurveConveyorFromUpToRightBeltWhenNeighborIsConveyorBeltToo() throws Exception{
        when(tileMock.getFieldObject()).thenReturn(conveyorBeltMock);
        when(conveyorBeltMock.getOrientation()).thenReturn(Orientation.EAST);
        when(conveyorBeltMock.isExpress()).thenReturn(true);
        neighbors.put(Orientation.NORTH, tileMock2);
        when(tileMock2.getFieldObject()).thenReturn(conveyorBeltMock2);
        when(conveyorBeltMock2.getOrientation()).thenReturn(Orientation.SOUTH);

        BufferedImage image = conveyorBeltTileGenerator.generateImage(tileMock);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/expressConveyorBelt/leftCurve/roller_express_ccw_right.png")), image);
    }

    @Test
    public void shouldReturnAExpressCurveConveyorFromLeftToDownBeltWhenNeighborIsConveyorBeltToo() throws Exception{
        when(tileMock.getFieldObject()).thenReturn(conveyorBeltMock);
        when(conveyorBeltMock.getOrientation()).thenReturn(Orientation.SOUTH);
        when(conveyorBeltMock.isExpress()).thenReturn(true);
        neighbors.put(Orientation.WEST, tileMock2);
        when(tileMock2.getFieldObject()).thenReturn(conveyorBeltMock2);
        when(conveyorBeltMock2.getOrientation()).thenReturn(Orientation.EAST);

        BufferedImage image = conveyorBeltTileGenerator.generateImage(tileMock);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/expressConveyorBelt/rightCurve/roller_express_cw_down.png")), image);
    }

    @Test
    public void shouldReturnAExpressCurveConveyorFromLeftToUpBeltWhenNeighborIsConveyorBeltToo() throws Exception{
        when(tileMock.getFieldObject()).thenReturn(conveyorBeltMock);
        when(conveyorBeltMock.getOrientation()).thenReturn(Orientation.NORTH);
        when(conveyorBeltMock.isExpress()).thenReturn(true);
        neighbors.put(Orientation.WEST, tileMock2);
        when(tileMock2.getFieldObject()).thenReturn(conveyorBeltMock2);
        when(conveyorBeltMock2.getOrientation()).thenReturn(Orientation.EAST);

        BufferedImage image = conveyorBeltTileGenerator.generateImage(tileMock);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/expressConveyorBelt/leftCurve/roller_express_ccw_up.png")), image);
    }

    @Test
    public void shouldReturnAExpressCurveConveyorFromRightToDownBeltWhenNeighborIsConveyorBeltToo() throws Exception{
        when(tileMock.getFieldObject()).thenReturn(conveyorBeltMock);
        when(conveyorBeltMock.getOrientation()).thenReturn(Orientation.SOUTH);
        when(conveyorBeltMock.isExpress()).thenReturn(true);
        neighbors.put(Orientation.EAST, tileMock2);
        when(tileMock2.getFieldObject()).thenReturn(conveyorBeltMock2);
        when(conveyorBeltMock2.getOrientation()).thenReturn(Orientation.WEST);

        BufferedImage image = conveyorBeltTileGenerator.generateImage(tileMock);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/expressConveyorBelt/leftCurve/roller_express_ccw_down.png")), image);
    }

    @Test
    public void shouldReturnAExpressCurveConveyorFromRightToUpBeltWhenNeighborIsConveyorBeltToo() throws Exception{
        when(tileMock.getFieldObject()).thenReturn(conveyorBeltMock);
        when(conveyorBeltMock.getOrientation()).thenReturn(Orientation.NORTH);
        when(conveyorBeltMock.isExpress()).thenReturn(true);
        neighbors.put(Orientation.EAST, tileMock2);
        when(tileMock2.getFieldObject()).thenReturn(conveyorBeltMock2);
        when(conveyorBeltMock2.getOrientation()).thenReturn(Orientation.WEST);

        BufferedImage image = conveyorBeltTileGenerator.generateImage(tileMock);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/expressConveyorBelt/rightCurve/roller_express_cw_up.png")), image);
    }

    @Test
    public void shouldAddATTileDownFormLeftAndRight() throws Exception{
        when(tileMock.getFieldObject()).thenReturn(conveyorBeltMock);
        when(conveyorBeltMock.getOrientation()).thenReturn(Orientation.SOUTH);
        neighbors.put(Orientation.EAST, tileMock2);
        neighbors.put(Orientation.WEST, tileMock3);
        when(tileMock2.getFieldObject()).thenReturn(conveyorBeltMock2);
        when(tileMock3.getFieldObject()).thenReturn(conveyorBeltMock3);
        when(conveyorBeltMock2.getOrientation()).thenReturn(Orientation.WEST);
        when(conveyorBeltMock3.getOrientation()).thenReturn(Orientation.EAST);

        BufferedImage image = conveyorBeltTileGenerator.generateImage(tileMock);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/conveyorBelt/tToMiddle/roller_ccw_cw_down.png")), image);
    }

    @Test
    public void shouldAddAnExpressTTileDownFormLeftAndRight() throws Exception{
        when(tileMock.getFieldObject()).thenReturn(conveyorBeltMock);
        when(conveyorBeltMock.getOrientation()).thenReturn(Orientation.SOUTH);
        when(conveyorBeltMock.isExpress()).thenReturn(true);
        neighbors.put(Orientation.EAST, tileMock2);
        neighbors.put(Orientation.WEST, tileMock3);
        when(tileMock2.getFieldObject()).thenReturn(conveyorBeltMock2);
        when(tileMock3.getFieldObject()).thenReturn(conveyorBeltMock3);
        when(conveyorBeltMock2.getOrientation()).thenReturn(Orientation.WEST);
        when(conveyorBeltMock3.getOrientation()).thenReturn(Orientation.EAST);

        BufferedImage image = conveyorBeltTileGenerator.generateImage(tileMock);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/expressConveyorBelt/tToMiddle/roller_express_ccw_cw_down.png")), image);
    }

    @Test
    public void shouldAddATTileUpFormLeftAndRight() throws Exception{
        when(tileMock.getFieldObject()).thenReturn(conveyorBeltMock);
        when(conveyorBeltMock.getOrientation()).thenReturn(Orientation.NORTH);
        neighbors.put(Orientation.EAST, tileMock2);
        neighbors.put(Orientation.WEST, tileMock3);
        when(tileMock2.getFieldObject()).thenReturn(conveyorBeltMock2);
        when(tileMock3.getFieldObject()).thenReturn(conveyorBeltMock3);
        when(conveyorBeltMock2.getOrientation()).thenReturn(Orientation.WEST);
        when(conveyorBeltMock3.getOrientation()).thenReturn(Orientation.EAST);

        BufferedImage image = conveyorBeltTileGenerator.generateImage(tileMock);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/conveyorBelt/tToMiddle/roller_ccw_cw_up.png")), image);
    }

    @Test
    public void shouldAddAnExpressTTileUpFormLeftAndRight() throws Exception{
        when(tileMock.getFieldObject()).thenReturn(conveyorBeltMock);
        when(conveyorBeltMock.getOrientation()).thenReturn(Orientation.NORTH);
        when(conveyorBeltMock.isExpress()).thenReturn(true);
        neighbors.put(Orientation.EAST, tileMock2);
        neighbors.put(Orientation.WEST, tileMock3);
        when(tileMock2.getFieldObject()).thenReturn(conveyorBeltMock2);
        when(tileMock3.getFieldObject()).thenReturn(conveyorBeltMock3);
        when(conveyorBeltMock2.getOrientation()).thenReturn(Orientation.WEST);
        when(conveyorBeltMock3.getOrientation()).thenReturn(Orientation.EAST);

        BufferedImage image = conveyorBeltTileGenerator.generateImage(tileMock);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/expressConveyorBelt/tToMiddle/roller_express_ccw_cw_up.png")), image);
    }

    @Test
    public void shouldAddATTileLeftFormUpAndDown() throws Exception{
        when(tileMock.getFieldObject()).thenReturn(conveyorBeltMock);
        when(conveyorBeltMock.getOrientation()).thenReturn(Orientation.WEST);
        neighbors.put(Orientation.SOUTH, tileMock2);
        neighbors.put(Orientation.NORTH, tileMock3);
        when(tileMock2.getFieldObject()).thenReturn(conveyorBeltMock2);
        when(tileMock3.getFieldObject()).thenReturn(conveyorBeltMock3);
        when(conveyorBeltMock2.getOrientation()).thenReturn(Orientation.NORTH);
        when(conveyorBeltMock3.getOrientation()).thenReturn(Orientation.SOUTH);

        BufferedImage image = conveyorBeltTileGenerator.generateImage(tileMock);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/conveyorBelt/tToMiddle/roller_ccw_cw_left.png")), image);
    }

    @Test
    public void shouldAddAnExpressTTileLeftFormUpAndDown() throws Exception{
        when(tileMock.getFieldObject()).thenReturn(conveyorBeltMock);
        when(conveyorBeltMock.getOrientation()).thenReturn(Orientation.WEST);
        when(conveyorBeltMock.isExpress()).thenReturn(true);
        neighbors.put(Orientation.SOUTH, tileMock2);
        neighbors.put(Orientation.NORTH, tileMock3);
        when(tileMock2.getFieldObject()).thenReturn(conveyorBeltMock2);
        when(tileMock3.getFieldObject()).thenReturn(conveyorBeltMock3);
        when(conveyorBeltMock2.getOrientation()).thenReturn(Orientation.NORTH);
        when(conveyorBeltMock3.getOrientation()).thenReturn(Orientation.SOUTH);

        BufferedImage image = conveyorBeltTileGenerator.generateImage(tileMock);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/expressConveyorBelt/tToMiddle/roller_express_ccw_cw_left.png")), image);
    }

    @Test
    public void shouldAddATTileRightFormUpAndDown() throws Exception{
        when(tileMock.getFieldObject()).thenReturn(conveyorBeltMock);
        when(conveyorBeltMock.getOrientation()).thenReturn(Orientation.EAST);
        neighbors.put(Orientation.SOUTH, tileMock2);
        neighbors.put(Orientation.NORTH, tileMock3);
        when(tileMock2.getFieldObject()).thenReturn(conveyorBeltMock2);
        when(tileMock3.getFieldObject()).thenReturn(conveyorBeltMock3);
        when(conveyorBeltMock2.getOrientation()).thenReturn(Orientation.NORTH);
        when(conveyorBeltMock3.getOrientation()).thenReturn(Orientation.SOUTH);

        BufferedImage image = conveyorBeltTileGenerator.generateImage(tileMock);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/conveyorBelt/tToMiddle/roller_ccw_cw_right.png")), image);
    }

    @Test
    public void shouldAddAnExpressTTileRightFormUpAndDown() throws Exception{
        when(tileMock.getFieldObject()).thenReturn(conveyorBeltMock);
        when(conveyorBeltMock.getOrientation()).thenReturn(Orientation.EAST);
        when(conveyorBeltMock.isExpress()).thenReturn(true);
        neighbors.put(Orientation.SOUTH, tileMock2);
        neighbors.put(Orientation.NORTH, tileMock3);
        when(tileMock2.getFieldObject()).thenReturn(conveyorBeltMock2);
        when(tileMock3.getFieldObject()).thenReturn(conveyorBeltMock3);
        when(conveyorBeltMock2.getOrientation()).thenReturn(Orientation.NORTH);
        when(conveyorBeltMock3.getOrientation()).thenReturn(Orientation.SOUTH);

        BufferedImage image = conveyorBeltTileGenerator.generateImage(tileMock);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/expressConveyorBelt/tToMiddle/roller_express_ccw_cw_right.png")), image);
    }

    @Test
    public void shouldAddATTileDownFormUpAndLeft() throws Exception{
        when(tileMock.getFieldObject()).thenReturn(conveyorBeltMock);
        when(conveyorBeltMock.getOrientation()).thenReturn(Orientation.SOUTH);
        neighbors.put(Orientation.NORTH, tileMock2);
        neighbors.put(Orientation.WEST, tileMock3);
        when(tileMock2.getFieldObject()).thenReturn(conveyorBeltMock2);
        when(tileMock3.getFieldObject()).thenReturn(conveyorBeltMock3);
        when(conveyorBeltMock2.getOrientation()).thenReturn(Orientation.SOUTH);
        when(conveyorBeltMock3.getOrientation()).thenReturn(Orientation.EAST);

        BufferedImage image = conveyorBeltTileGenerator.generateImage(tileMock);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/conveyorBelt/tToRight/roller_cw_straight_down.png")), image);
    }

    @Test
    public void shouldAddAnExpressTTileDownFormUpAndLeft() throws Exception{
        when(tileMock.getFieldObject()).thenReturn(conveyorBeltMock);
        when(conveyorBeltMock.getOrientation()).thenReturn(Orientation.SOUTH);
        when(conveyorBeltMock.isExpress()).thenReturn(true);
        neighbors.put(Orientation.NORTH, tileMock2);
        neighbors.put(Orientation.WEST, tileMock3);
        when(tileMock2.getFieldObject()).thenReturn(conveyorBeltMock2);
        when(tileMock3.getFieldObject()).thenReturn(conveyorBeltMock3);
        when(conveyorBeltMock2.getOrientation()).thenReturn(Orientation.SOUTH);
        when(conveyorBeltMock3.getOrientation()).thenReturn(Orientation.EAST);

        BufferedImage image = conveyorBeltTileGenerator.generateImage(tileMock);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/expressConveyorBelt/tToRight/roller_express_cw_straight_down.png")), image);
    }

    @Test
    public void shouldAddATTileUpFormDownAndRight() throws Exception{
        when(tileMock.getFieldObject()).thenReturn(conveyorBeltMock);
        when(conveyorBeltMock.getOrientation()).thenReturn(Orientation.NORTH);
        neighbors.put(Orientation.SOUTH, tileMock2);
        neighbors.put(Orientation.EAST, tileMock3);
        when(tileMock2.getFieldObject()).thenReturn(conveyorBeltMock2);
        when(tileMock3.getFieldObject()).thenReturn(conveyorBeltMock3);
        when(conveyorBeltMock2.getOrientation()).thenReturn(Orientation.NORTH);
        when(conveyorBeltMock3.getOrientation()).thenReturn(Orientation.WEST);

        BufferedImage image = conveyorBeltTileGenerator.generateImage(tileMock);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/conveyorBelt/tToRight/roller_cw_straight_up.png")), image);
    }

    @Test
    public void shouldAddAnExpressTTileUpFormDownAndRight() throws Exception{
        when(tileMock.getFieldObject()).thenReturn(conveyorBeltMock);
        when(conveyorBeltMock.getOrientation()).thenReturn(Orientation.NORTH);
        when(conveyorBeltMock.isExpress()).thenReturn(true);
        neighbors.put(Orientation.SOUTH, tileMock2);
        neighbors.put(Orientation.EAST, tileMock3);
        when(tileMock2.getFieldObject()).thenReturn(conveyorBeltMock2);
        when(tileMock3.getFieldObject()).thenReturn(conveyorBeltMock3);
        when(conveyorBeltMock2.getOrientation()).thenReturn(Orientation.NORTH);
        when(conveyorBeltMock3.getOrientation()).thenReturn(Orientation.WEST);

        BufferedImage image = conveyorBeltTileGenerator.generateImage(tileMock);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/expressConveyorBelt/tToRight/roller_express_cw_straight_up.png")), image);
    }

    @Test
    public void shouldAddATTileLeftFormUpAndRight() throws Exception{
        when(tileMock.getFieldObject()).thenReturn(conveyorBeltMock);
        when(conveyorBeltMock.getOrientation()).thenReturn(Orientation.WEST);
        neighbors.put(Orientation.NORTH, tileMock2);
        neighbors.put(Orientation.EAST, tileMock3);
        when(tileMock2.getFieldObject()).thenReturn(conveyorBeltMock2);
        when(tileMock3.getFieldObject()).thenReturn(conveyorBeltMock3);
        when(conveyorBeltMock2.getOrientation()).thenReturn(Orientation.SOUTH);
        when(conveyorBeltMock3.getOrientation()).thenReturn(Orientation.WEST);

        BufferedImage image = conveyorBeltTileGenerator.generateImage(tileMock);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/conveyorBelt/tToRight/roller_cw_straight_left.png")), image);
    }

    @Test
    public void shouldAddAnExpressTTileLeftFormUpAndRight() throws Exception{
        when(tileMock.getFieldObject()).thenReturn(conveyorBeltMock);
        when(conveyorBeltMock.getOrientation()).thenReturn(Orientation.WEST);
        when(conveyorBeltMock.isExpress()).thenReturn(true);
        neighbors.put(Orientation.NORTH, tileMock2);
        neighbors.put(Orientation.EAST, tileMock3);
        when(tileMock2.getFieldObject()).thenReturn(conveyorBeltMock2);
        when(tileMock3.getFieldObject()).thenReturn(conveyorBeltMock3);
        when(conveyorBeltMock2.getOrientation()).thenReturn(Orientation.SOUTH);
        when(conveyorBeltMock3.getOrientation()).thenReturn(Orientation.WEST);

        BufferedImage image = conveyorBeltTileGenerator.generateImage(tileMock);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/expressConveyorBelt/tToRight/roller_express_cw_straight_left.png")), image);
    }

    @Test
    public void shouldAddATTileRightFormDownAndLeft() throws Exception{
        when(tileMock.getFieldObject()).thenReturn(conveyorBeltMock);
        when(conveyorBeltMock.getOrientation()).thenReturn(Orientation.EAST);
        neighbors.put(Orientation.SOUTH, tileMock2);
        neighbors.put(Orientation.WEST, tileMock3);
        when(tileMock2.getFieldObject()).thenReturn(conveyorBeltMock2);
        when(tileMock3.getFieldObject()).thenReturn(conveyorBeltMock3);
        when(conveyorBeltMock2.getOrientation()).thenReturn(Orientation.NORTH);
        when(conveyorBeltMock3.getOrientation()).thenReturn(Orientation.EAST);

        BufferedImage image = conveyorBeltTileGenerator.generateImage(tileMock);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/conveyorBelt/tToRight/roller_cw_straight_right.png")), image);
    }

    @Test
    public void shouldAddAnExpressTTileRightFormDownAndLeft() throws Exception{
        when(tileMock.getFieldObject()).thenReturn(conveyorBeltMock);
        when(conveyorBeltMock.getOrientation()).thenReturn(Orientation.EAST);
        when(conveyorBeltMock.isExpress()).thenReturn(true);
        neighbors.put(Orientation.SOUTH, tileMock2);
        neighbors.put(Orientation.WEST, tileMock3);
        when(tileMock2.getFieldObject()).thenReturn(conveyorBeltMock2);
        when(tileMock3.getFieldObject()).thenReturn(conveyorBeltMock3);
        when(conveyorBeltMock2.getOrientation()).thenReturn(Orientation.NORTH);
        when(conveyorBeltMock3.getOrientation()).thenReturn(Orientation.EAST);

        BufferedImage image = conveyorBeltTileGenerator.generateImage(tileMock);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/expressConveyorBelt/tToRight/roller_express_cw_straight_right.png")), image);
    }

    //TODO

    @Test
    public void shouldAddATTileDownFormUpAndRight() throws Exception{
        when(tileMock.getFieldObject()).thenReturn(conveyorBeltMock);
        when(conveyorBeltMock.getOrientation()).thenReturn(Orientation.SOUTH);
        neighbors.put(Orientation.NORTH, tileMock2);
        neighbors.put(Orientation.EAST, tileMock3);
        when(tileMock2.getFieldObject()).thenReturn(conveyorBeltMock2);
        when(tileMock3.getFieldObject()).thenReturn(conveyorBeltMock3);
        when(conveyorBeltMock2.getOrientation()).thenReturn(Orientation.SOUTH);
        when(conveyorBeltMock3.getOrientation()).thenReturn(Orientation.WEST);

        BufferedImage image = conveyorBeltTileGenerator.generateImage(tileMock);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/conveyorBelt/tToLeft/roller_ccw_straight_down.png")), image);
    }

    @Test
    public void shouldAddAnExpressTTileDownFormUpAndRight() throws Exception{
        when(tileMock.getFieldObject()).thenReturn(conveyorBeltMock);
        when(conveyorBeltMock.getOrientation()).thenReturn(Orientation.SOUTH);
        when(conveyorBeltMock.isExpress()).thenReturn(true);
        neighbors.put(Orientation.NORTH, tileMock2);
        neighbors.put(Orientation.EAST, tileMock3);
        when(tileMock2.getFieldObject()).thenReturn(conveyorBeltMock2);
        when(tileMock3.getFieldObject()).thenReturn(conveyorBeltMock3);
        when(conveyorBeltMock2.getOrientation()).thenReturn(Orientation.SOUTH);
        when(conveyorBeltMock3.getOrientation()).thenReturn(Orientation.WEST);

        BufferedImage image = conveyorBeltTileGenerator.generateImage(tileMock);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/expressConveyorBelt/tToLeft/roller_express_ccw_straight_down.png")), image);
    }

    @Test
    public void shouldAddATTileUpFormDownAndLeft() throws Exception{
        when(tileMock.getFieldObject()).thenReturn(conveyorBeltMock);
        when(conveyorBeltMock.getOrientation()).thenReturn(Orientation.NORTH);
        neighbors.put(Orientation.SOUTH, tileMock2);
        neighbors.put(Orientation.WEST, tileMock3);
        when(tileMock2.getFieldObject()).thenReturn(conveyorBeltMock2);
        when(tileMock3.getFieldObject()).thenReturn(conveyorBeltMock3);
        when(conveyorBeltMock2.getOrientation()).thenReturn(Orientation.NORTH);
        when(conveyorBeltMock3.getOrientation()).thenReturn(Orientation.EAST);

        BufferedImage image = conveyorBeltTileGenerator.generateImage(tileMock);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/conveyorBelt/tToLeft/roller_ccw_straight_up.png")), image);
    }

    @Test
    public void shouldAddAnExpressTTileUpFormDownAndLeft() throws Exception{
        when(tileMock.getFieldObject()).thenReturn(conveyorBeltMock);
        when(conveyorBeltMock.getOrientation()).thenReturn(Orientation.NORTH);
        when(conveyorBeltMock.isExpress()).thenReturn(true);
        neighbors.put(Orientation.SOUTH, tileMock2);
        neighbors.put(Orientation.WEST, tileMock3);
        when(tileMock2.getFieldObject()).thenReturn(conveyorBeltMock2);
        when(tileMock3.getFieldObject()).thenReturn(conveyorBeltMock3);
        when(conveyorBeltMock2.getOrientation()).thenReturn(Orientation.NORTH);
        when(conveyorBeltMock3.getOrientation()).thenReturn(Orientation.EAST);

        BufferedImage image = conveyorBeltTileGenerator.generateImage(tileMock);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/expressConveyorBelt/tToLeft/roller_express_ccw_straight_up.png")), image);
    }

    @Test
    public void shouldAddATTileLeftFormDownAndRight() throws Exception{
        when(tileMock.getFieldObject()).thenReturn(conveyorBeltMock);
        when(conveyorBeltMock.getOrientation()).thenReturn(Orientation.WEST);
        neighbors.put(Orientation.SOUTH, tileMock2);
        neighbors.put(Orientation.EAST, tileMock3);
        when(tileMock2.getFieldObject()).thenReturn(conveyorBeltMock2);
        when(tileMock3.getFieldObject()).thenReturn(conveyorBeltMock3);
        when(conveyorBeltMock2.getOrientation()).thenReturn(Orientation.NORTH);
        when(conveyorBeltMock3.getOrientation()).thenReturn(Orientation.WEST);

        BufferedImage image = conveyorBeltTileGenerator.generateImage(tileMock);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/conveyorBelt/tToLeft/roller_ccw_straight_left.png")), image);
    }

    @Test
    public void shouldAddAnExpressTTileLeftFormDownAndRight() throws Exception{
        when(tileMock.getFieldObject()).thenReturn(conveyorBeltMock);
        when(conveyorBeltMock.getOrientation()).thenReturn(Orientation.WEST);
        when(conveyorBeltMock.isExpress()).thenReturn(true);
        neighbors.put(Orientation.SOUTH, tileMock2);
        neighbors.put(Orientation.EAST, tileMock3);
        when(tileMock2.getFieldObject()).thenReturn(conveyorBeltMock2);
        when(tileMock3.getFieldObject()).thenReturn(conveyorBeltMock3);
        when(conveyorBeltMock2.getOrientation()).thenReturn(Orientation.NORTH);
        when(conveyorBeltMock3.getOrientation()).thenReturn(Orientation.WEST);

        BufferedImage image = conveyorBeltTileGenerator.generateImage(tileMock);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/expressConveyorBelt/tToLeft/roller_express_ccw_straight_left.png")), image);
    }

    @Test
    public void shouldAddATTileRightFormUpAndLeft() throws Exception{
        when(tileMock.getFieldObject()).thenReturn(conveyorBeltMock);
        when(conveyorBeltMock.getOrientation()).thenReturn(Orientation.EAST);
        neighbors.put(Orientation.NORTH, tileMock2);
        neighbors.put(Orientation.WEST, tileMock3);
        when(tileMock2.getFieldObject()).thenReturn(conveyorBeltMock2);
        when(tileMock3.getFieldObject()).thenReturn(conveyorBeltMock3);
        when(conveyorBeltMock2.getOrientation()).thenReturn(Orientation.SOUTH);
        when(conveyorBeltMock3.getOrientation()).thenReturn(Orientation.EAST);

        BufferedImage image = conveyorBeltTileGenerator.generateImage(tileMock);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/conveyorBelt/tToLeft/roller_ccw_straight_right.png")), image);
    }

    @Test
    public void shouldAddAnExpressTTileRightFormUpAndLeft() throws Exception{
        when(tileMock.getFieldObject()).thenReturn(conveyorBeltMock);
        when(conveyorBeltMock.getOrientation()).thenReturn(Orientation.EAST);
        when(conveyorBeltMock.isExpress()).thenReturn(true);
        neighbors.put(Orientation.NORTH, tileMock2);
        neighbors.put(Orientation.WEST, tileMock3);
        when(tileMock2.getFieldObject()).thenReturn(conveyorBeltMock2);
        when(tileMock3.getFieldObject()).thenReturn(conveyorBeltMock3);
        when(conveyorBeltMock2.getOrientation()).thenReturn(Orientation.SOUTH);
        when(conveyorBeltMock3.getOrientation()).thenReturn(Orientation.EAST);

        BufferedImage image = conveyorBeltTileGenerator.generateImage(tileMock);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/expressConveyorBelt/tToLeft/roller_express_ccw_straight_right.png")), image);
    }

}