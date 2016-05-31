package de.uni_bremen.factroytrouble.editor.controller;

import de.saxsys.javafx.test.JfxRunner;
import de.uni_bremen.factroytrouble.editor.TestUtil;
import de.uni_bremen.factroytrouble.editor.component.BoardAndDockItem;
import de.uni_bremen.factroytrouble.editor.component.Tile;
import de.uni_bremen.factroytrouble.editor.container.image.TileGroundImageContainer;
import de.uni_bremen.factroytrouble.editor.data.GroundFill;
import de.uni_bremen.factroytrouble.editor.data.Orientation;
import de.uni_bremen.factroytrouble.editor.data.TileWithPosition;
import de.uni_bremen.factroytrouble.editor.service.save.FileNameAlertService;
import de.uni_bremen.factroytrouble.editor.service.save.SaveCourseService;
import de.uni_bremen.factroytrouble.editor.service.serialisisation.DeserializeBoardService;
import de.uni_bremen.factroytrouble.editor.service.tile.GenerateFlagService;
import de.uni_bremen.factroytrouble.editor.service.view.AlertService;
import de.uni_bremen.factroytrouble.editor.service.view.GridPaneUtilService;
import de.uni_bremen.factroytrouble.editor.service.view.initalisation.InitBoardAndDockListService;
import de.uni_bremen.factroytrouble.editor.service.view.initalisation.InitialTileGridService;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;

import javax.imageio.ImageIO;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

@RunWith(JfxRunner.class)
public class CourseCreateControllerTest {

    public static final String HEADLINE = "Course kann nicht gespeichert werden";
    @Mock private InitBoardAndDockListService initBoardAndDockListService;
    @Mock private InitialTileGridService initialTileGridService;
    @Mock private DeserializeBoardService deserializeBoardService;
    @Mock private GridPaneUtilService gridPaneUtilService;
    @Mock private GenerateFlagService generateFlagService;
    @Mock private TileGroundImageContainer tileGroundImageContainer;
    @Mock private AlertService alertService;
    @Mock private SaveCourseService saveCourseService;
    @Mock private FileNameAlertService fileNameAlertService;
    @InjectMocks private CourseCreateController courseCreateController;

    @Mock private Tile tile1;
    @Mock private Tile tile2;
    @Mock private Tile tile3;
    @Mock private Tile tile4;
    @Mock private BoardAndDockItem boardAndDockItem;
    private GridPane gridPane;
    private Map<Integer, TileWithPosition> flags;

    @Captor private ArgumentCaptor<EventHandler<MouseEvent>> eventArgumentCaptor;

    @Before
    public void setUp() throws Exception{
        courseCreateController = new CourseCreateController();
        MockitoAnnotations.initMocks(this);
        flags = new HashMap<>();
        gridPane = new GridPane();
        TestUtil.injectPrivateField("flags", courseCreateController, flags);
        TestUtil.injectPrivateField("boardGrid", courseCreateController, gridPane);
        TestUtil.injectPrivateField("dockGrid", courseCreateController, gridPane);
        when(gridPaneUtilService.getNodeFromGridPane(any(GridPane.class), anyInt(), anyInt())).thenReturn(tile1, tile2, tile3, tile4);
        when(generateFlagService.generateFlagTileImage(anyInt())).thenReturn(ImageIO.read(getClass().getResourceAsStream("/game/tiles/flag_with_1.png")));
        when(tileGroundImageContainer.getImageForGround(GroundFill.EMPTY)).thenReturn(ImageIO.read(getClass().getResourceAsStream("/game/tiles/empty.png")));
    }

    @Test
    public void shouldAddABoard() {
        courseCreateController.setBoard(boardAndDockItem, Orientation.NORTH);
        verify(deserializeBoardService).deserializeBoard(eq(gridPane), anyList(), eq(12), eq(12), eq(Orientation.NORTH));
    }

    @Test
    public void shouldAddADock() {
        courseCreateController.setDock(boardAndDockItem);
        verify(deserializeBoardService).deserializeBoard(eq(gridPane), anyList(), eq(12), eq(4), eq(Orientation.NORTH));
    }

    @Test
    public void shouldResetAllTilesBeforeBoardSet() {
        courseCreateController.setBoard(boardAndDockItem, Orientation.NORTH);
        verify(initialTileGridService).fillBoardTilePane(eq(gridPane));
    }

    @Test
    public void shouldAddClickHandlerToAllTilesWhenAddABoard() {
        when(tile1.getGroundFill()).thenReturn(GroundFill.EMPTY);
        courseCreateController.setBoard(boardAndDockItem, Orientation.NORTH);
        verify(tile1).addAdditionalClickEvent(any());
    }

    @Test
    public void shouldNotAddClickHandlerToANonEmptyTileWhenAddABoard() {
        when(tile2.getGroundFill()).thenReturn(GroundFill.HOLE);
        courseCreateController.setBoard(boardAndDockItem, Orientation.NORTH);
        verify(tile2, never()).addAdditionalClickEvent(any());
    }

    @Test
    public void shouldSetAFlagOnClickOnAnEmptyTile() {
        when(tile1.getGroundFill()).thenReturn(GroundFill.EMPTY);
        courseCreateController.setBoard(boardAndDockItem, Orientation.NORTH);
        verify(tile1).addAdditionalClickEvent(eventArgumentCaptor.capture());
        eventArgumentCaptor.getValue().handle(null);
        verify(tile1).setFlagNumber(1);
        verify(tile1).setGroundFill(GroundFill.FLAG);
    }

    @Test
    public void shouldResetAFlagOnSecondClickOnAnEmptyTile() {
        when(tile1.getGroundFill()).thenReturn(GroundFill.EMPTY);
        courseCreateController.setBoard(boardAndDockItem, Orientation.NORTH);
        verify(tile1).addAdditionalClickEvent(eventArgumentCaptor.capture());
        eventArgumentCaptor.getValue().handle(null);
        eventArgumentCaptor.getValue().handle(null);
        verify(tile1).setFlagNumber(null);
        verify(tile1).setGroundFill(GroundFill.EMPTY);
    }

    @Test
    public void shouldRearrangeAllFlagsWhenFlagInTheMiddleRemoved() {
        addThreeFlags();
        eventArgumentCaptor.getAllValues().get(1).handle(null);
        verify(tile2).setFlagNumber(null);
        verify(tile3).setFlagNumber(2);
    }

    @Test
    public void shouldRearrangeAllFlagsWhenFlagInTheEndRemoved() {
        addThreeFlags();
        eventArgumentCaptor.getAllValues().get(2).handle(null);
        verify(tile3).setFlagNumber(null);
    }

    @Test
    public void shouldRearrangeAllFlagsWhenFlagInTheBeginRemoved() {
        addThreeFlags();
        eventArgumentCaptor.getAllValues().get(0).handle(null);
        verify(tile1).setFlagNumber(null);
        verify(tile2).setFlagNumber(1);
        verify(tile3).setFlagNumber(2);
    }

    @Test
    public void shouldNotSaveTheCourseWhenBoardIsNotSet() throws Exception {
        TestUtil.callPrivateMethodeWithParameter("saveBoard", courseCreateController, new Class[]{ActionEvent.class}, mock(ActionEvent.class));
        verify(alertService).showAlert(HEADLINE, "Es muss ein Board gesetzt sein", Alert.AlertType.ERROR);
    }

    @Test
    public void shouldNotSaveTheCourseWhenDockIsNotSet() throws Exception {
        courseCreateController.setBoard(boardAndDockItem, Orientation.NORTH);
        TestUtil.callPrivateMethodeWithParameter("saveBoard", courseCreateController, new Class[]{ActionEvent.class}, mock(ActionEvent.class));
        verify(alertService).showAlert(HEADLINE, "Es muss ein Dock gesetzt sein", Alert.AlertType.ERROR);
    }

    @Test
    public void shouldNotSaveTheCourseWhenFlagsAreNotSet() throws Exception {
        courseCreateController.setBoard(boardAndDockItem, Orientation.NORTH);
        courseCreateController.setDock(boardAndDockItem);
        TestUtil.callPrivateMethodeWithParameter("saveBoard", courseCreateController, new Class[]{ActionEvent.class}, mock(ActionEvent.class));
        verify(alertService).showAlert(HEADLINE, "Es muss eine Flagge gesetzt sein", Alert.AlertType.ERROR);
    }

    @Test
    public void shouldSaveTheCourse() throws Exception {
        courseCreateController.setBoard(boardAndDockItem, Orientation.NORTH);
        courseCreateController.setDock(boardAndDockItem);
        flags.put(1, new TileWithPosition(null, null));
        TestUtil.callPrivateMethodeWithParameter("saveBoard", courseCreateController, new Class[]{ActionEvent.class}, mock(ActionEvent.class));
        verify(fileNameAlertService).showFilenameAlert("Course");
        verify(saveCourseService).save(anyString(), anyString(), anyString(), any(Orientation.class), anyMap());
    }

    private void addThreeFlags() {
        when(tile1.getGroundFill()).thenReturn(GroundFill.EMPTY);
        when(tile2.getGroundFill()).thenReturn(GroundFill.EMPTY);
        when(tile3.getGroundFill()).thenReturn(GroundFill.EMPTY);
        courseCreateController.setBoard(boardAndDockItem, Orientation.NORTH);
        verify(tile1).addAdditionalClickEvent(eventArgumentCaptor.capture());
        verify(tile2).addAdditionalClickEvent(eventArgumentCaptor.capture());
        verify(tile3).addAdditionalClickEvent(eventArgumentCaptor.capture());
        eventArgumentCaptor.getAllValues().forEach( event -> event.handle(null));
    }

}