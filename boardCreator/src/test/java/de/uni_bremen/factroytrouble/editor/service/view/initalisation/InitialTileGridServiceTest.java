package de.uni_bremen.factroytrouble.editor.service.view.initalisation;

import de.uni_bremen.factroytrouble.editor.component.Tile;
import de.uni_bremen.factroytrouble.editor.factory.TileFactory;
import javafx.scene.layout.GridPane;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class InitialTileGridServiceTest {

    @Mock private TileFactory tileFactory;
    @InjectMocks private InitialTileGridService initialTileGridService = new InitialTileGridService();

    @Mock private GridPane tileGridPane;

    @Test
    public void shouldFillTheTileGridForABoard() {
        initialTileGridService.fillBoardTilePane(tileGridPane);
        verify(tileGridPane, times(144)).add(any(Tile.class), anyInt(), anyInt());
    }

    @Test
    public void shouldFillTheTileGridForADock() {
        initialTileGridService.fillDockTilePane(tileGridPane);
        verify(tileGridPane, times(48)).add(any(Tile.class), anyInt(), anyInt());
    }

}