package de.uni_bremen.factroytrouble.gui.generator.board;

import de.uni_bremen.factroytrouble.board.Board;
import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.gui.generator.board.tile.BoardTileImageDispatcher;
import de.uni_bremen.factroytrouble.gui.services.GameEngineWrapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.imageio.ImageIO;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import static de.uni_bremen.factroytrouble.gui.TestUtil.assertImageEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ImageBoardGeneratorTest {

    @Mock private BoardTileImageDispatcher boardTileImageDispatcher;
    @Mock private GameEngineWrapper gameEngineWrapper;
    @Mock private BoardConverterService boardConverterService;
    @InjectMocks private ImageBoardGenerator imageBoardGenerator = new ImageBoardGenerator();

    @Before
    public void setUp() throws Exception {
        createGameMasterMock();
        createBoardConverterService();
        when(boardTileImageDispatcher.dispatch(any(Tile.class))).thenReturn(ImageIO.read(getClass().getResourceAsStream("/game/tiles/empty.png")));
    }

    @Test
    public void shouldGenerateTheImage() throws Exception {
        BufferedImage image = (BufferedImage) imageBoardGenerator.generateBoard("testBoard");
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/testBoard.png")), image);
    }



    private void createGameMasterMock() {
        Board boardMock = mock(Board.class);
        when(gameEngineWrapper.getBoard(any())).thenReturn(boardMock);
    }

    private void createBoardConverterService() {
        Map<Point, Tile> field = new HashMap<>();
        Tile tileMock = mock(Tile.class);
        for(int x = 0; x < 12; x++) {
            for(int y = 0; y < 48; y++) {
                field.put(new Point(x,y), tileMock);
            }
        }
        when(boardConverterService.convertBoardToMap(anyMap())).thenReturn(field);
        when(boardConverterService.getMaxX()).thenReturn(11);
        when(boardConverterService.getMaxY()).thenReturn(47);
    }

}