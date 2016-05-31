package de.uni_bremen.factroytrouble.editor.service.view.initalisation;

import de.saxsys.javafx.test.JfxRunner;
import de.uni_bremen.factroytrouble.editor.component.EditorTileFillToolIcon;
import de.uni_bremen.factroytrouble.editor.container.image.ConveyorBeltImageContainer;
import de.uni_bremen.factroytrouble.editor.container.image.TileGroundImageContainer;
import de.uni_bremen.factroytrouble.editor.container.image.WallAndObjectImageContainer;
import de.uni_bremen.factroytrouble.editor.data.ConveyorBeltTileImage;
import de.uni_bremen.factroytrouble.editor.data.GroundFill;
import de.uni_bremen.factroytrouble.editor.data.Orientation;
import javafx.scene.layout.GridPane;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.imageio.ImageIO;

import static org.mockito.Mockito.*;

@RunWith(JfxRunner.class)
public class InitialEditorSidebarServiceTest {

    @Mock private TileGroundImageContainer tileGroundImageContainer;
    @Mock private ConveyorBeltImageContainer conveyorBeltImageContainer;
    @Mock private WallAndObjectImageContainer wallAndObjectImageContainer;
    @InjectMocks private InitialEditorSidebarService initialEditorSidebarService;

    @Mock private GridPane groundGrid;
    @Mock private GridPane objectGrid;
    @Mock private GridPane markerGrid;

    @Before
    public void setUp() throws Exception {
        initialEditorSidebarService = new InitialEditorSidebarService();
        MockitoAnnotations.initMocks(this);
        when(tileGroundImageContainer.getImageForGround(any(GroundFill.class))).thenReturn(ImageIO.read(getClass().getResourceAsStream("/game/tiles/empty.png")));
        when(conveyorBeltImageContainer.getConyorbeltImage(any(ConveyorBeltTileImage.class))).thenReturn(ImageIO.read(getClass().getResourceAsStream("/game/tiles/empty.png")));
        when(wallAndObjectImageContainer.getWall(any(Orientation.class))).thenReturn(ImageIO.read(getClass().getResourceAsStream("/game/tiles/empty.png")));
        when(wallAndObjectImageContainer.getPusher(any(Orientation.class), anyBoolean())).thenReturn(ImageIO.read(getClass().getResourceAsStream("/game/tiles/empty.png")));
        when(wallAndObjectImageContainer.getLaser(anyInt(), anyBoolean())).thenReturn(ImageIO.read(getClass().getResourceAsStream("/game/tiles/empty.png")));
    }

    @Test
    public void shouldAdd13ElementsToGroundGrid() {
        initialEditorSidebarService.fillPartGrids(groundGrid, objectGrid, markerGrid);
        verify(groundGrid, times(13)).add(any(EditorTileFillToolIcon.class), anyInt(), anyInt());
    }

    @Test
    public void shouldAdd6ElementsToObjectGrid() {
        initialEditorSidebarService.fillPartGrids(groundGrid, objectGrid, markerGrid);
        verify(objectGrid, times(6)).add(any(EditorTileFillToolIcon.class), anyInt(), anyInt());
    }

    @Test
    public void shouldAddOneElementsToObjectGrid() {
        initialEditorSidebarService.fillPartGrids(groundGrid, objectGrid, markerGrid);
        verify(markerGrid).add(any(EditorTileFillToolIcon.class), anyInt(), anyInt());
    }

}