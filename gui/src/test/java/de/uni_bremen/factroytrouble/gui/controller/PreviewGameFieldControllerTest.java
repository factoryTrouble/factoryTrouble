package de.uni_bremen.factroytrouble.gui.controller;

import de.saxsys.javafx.test.JfxRunner;
import de.uni_bremen.factroytrouble.board.Board;
import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.gui.controller.components.RespawnImageView;
import de.uni_bremen.factroytrouble.gui.generator.board.BoardConverterService;
import de.uni_bremen.factroytrouble.gui.generator.board.ImageBoardGenerator;
import de.uni_bremen.factroytrouble.gui.services.GameEngineWrapper;
import de.uni_bremen.factroytrouble.gui.testrunner.JavaFXControllerTestRunner;
import de.uni_bremen.factroytrouble.player.Player;
import de.uni_bremen.factroytrouble.spring.SpringConfigHolder;
import javafx.beans.property.DoubleProperty;
import javafx.scene.control.ScrollPane;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.imageio.ImageIO;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static de.uni_bremen.factroytrouble.gui.TestUtil.injectPrivateFieldIntoParent;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(JfxRunner.class)
public class PreviewGameFieldControllerTest extends JavaFXControllerTestRunner {

    @Mock private GameEngineWrapper gameEngineWrapper;
    @Mock private NewGameScreenController newGameScreenController;
    @Mock private ImageBoardGenerator boardGenerator;
    @Mock private BoardConverterService boardConverterService;
    @InjectMocks private PreviewGameFieldController previewGameFieldController = new PreviewGameFieldController();

    @Mock private ScrollPane scrollPane;
    @Mock private DoubleProperty zoom;

    @Mock private Player player;
    @Mock private Robot robot;
    @Mock private Tile tile;
    @Mock private Board board;

    @Mock private AnnotationConfigApplicationContext context;

    private Map<String, RobotController> robots;
    private Map<String, RespawnImageView> respawns;

    private Map<Integer, String> selectedPlayers;
    private List<Player> players;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        SpringConfigHolder.getInstance().setContext(context);

        robots = new HashMap<>();
        respawns = new HashMap<>();
        selectedPlayers = new HashMap<>();
        players = new ArrayList<>();

        selectedPlayers.put(0, "test");
        players.add(player);

        when(context.getBean(GameEngineWrapper.class)).thenReturn(gameEngineWrapper);
        when(context.getBean(BoardConverterService.class)).thenReturn(boardConverterService);
        when(newGameScreenController.getActiveBoard()).thenReturn("testBoard");
        when(boardGenerator.generateBoard(anyString())).thenReturn(ImageIO.read(getClass().getResourceAsStream("/game/testBoard.png")));
        when(newGameScreenController.getSelectedPlayers()).thenReturn(selectedPlayers);
        when(gameEngineWrapper.getPlayers()).thenReturn(players);
        when(gameEngineWrapper.getActiveBoard()).thenReturn(board);
        when(gameEngineWrapper.getRobotNameByNumber(anyInt())).thenReturn("test");

        when(player.getRobot()).thenReturn(robot);
        when(robot.getName()).thenReturn("test");
        when(robot.getCurrentTile()).thenReturn(tile);
        when(robot.getOrientation()).thenReturn(Orientation.NORTH);
        when(board.getAbsoluteCoordinates(tile)).thenReturn(new Point(0,0));

        injectPrivateFieldIntoParent("scrlPane", previewGameFieldController, scrollPane);
        injectPrivateFieldIntoParent("zoom", previewGameFieldController, zoom);
        injectPrivateFieldIntoParent("robots", previewGameFieldController, robots);
        injectPrivateFieldIntoParent("respawns", previewGameFieldController, respawns);
    }

    @Test
    public void shouldGetTheImage() {
        selectedPlayers.clear();
        previewGameFieldController.getImage("test");
        verify(boardGenerator).generateBoard("test");
    }

    @Test
    public void shouldNotShowThePreviewWhenNoActiveBoardSet() {
        selectedPlayers.clear();
        when(newGameScreenController.getActiveBoard()).thenReturn("");
        previewGameFieldController.showPreview();
        verify(gameEngineWrapper, never()).removeAllPlayers();
    }

    @Test
    public void shouldRemoveAllPlayersOnLoadPreview() {
        selectedPlayers.clear();
        previewGameFieldController.showPreview();
        verify(gameEngineWrapper).removeAllPlayers();
    }

    @Test
    public void shouldCreateANewPlayerInsideTheGameEngine() {
        previewGameFieldController.showPreview();
        verify(gameEngineWrapper).createPlayer(anyString(), eq("test"));
    }

    @Test
    public void shouldAddRobotToRobotsMap() {
        previewGameFieldController.showPreview();
        assertEquals(1, robots.size());
    }

    @Test
    public void shouldAddRespawnPointToRespawnMap() {
        previewGameFieldController.showPreview();
        assertEquals(1, respawns.size());
    }

    @Test
    public void shouldInitTheBoard() {
        previewGameFieldController.showPreview();
        verify(gameEngineWrapper).initialiseBoard(anyString());
    }

    @Test
    public void shouldRefreshTheRobotController() {
        previewGameFieldController.showPreview();
        assertEquals(new Point(0,0), robots.get("test").getPosition());
    }

}