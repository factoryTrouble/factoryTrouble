package de.uni_bremen.factroytrouble.gui.controller;

import de.saxsys.javafx.test.JfxRunner;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.gui.controller.util.ChangeViewService;
import de.uni_bremen.factroytrouble.gui.services.GameEngineWrapper;
import de.uni_bremen.factroytrouble.gui.services.game.*;
import de.uni_bremen.factroytrouble.gui.testrunner.JavaFXControllerTestRunner;
import de.uni_bremen.factroytrouble.player.Player;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(JfxRunner.class)
public class GameScreenControllerTest extends JavaFXControllerTestRunner {

    @Mock private GameFieldController gameFieldController;
    @Mock private ResourceBundle resources;
    @Mock private ScrollPane scrlPane;
    @Mock private ActionEvent actionEvent;
    @Mock private Alert alert;
    @Mock private PowerDownService powerDownService;
    @Mock private AnchorPane cardPane;
    @Mock private AnchorPane registerPane;
    @Mock private EndBtnService endBtnService;
    @Mock private PerformRoundForPlayerService performRoundForPlayerService;
    @Mock private GameEngineWrapper gameEngineWrapper;
    @Mock private ResultScreenController resultScreenController;
    @Mock private Parent parent;
    @Mock private ChangeViewService changeViewService;
    @Mock private PlayerGridService playerGridService;
    @Mock private AlertService alertService;
    @Mock private PlayerInfoController playerInfoController;
    @Mock private SaveGameStateService saveGameStateService;
    @InjectMocks private GameScreenController gameScreenController;

    @Mock private Player player;
    @Mock private Robot robot;

    private Button endRoundButton;
    private ToggleButton powerDownbtn;

    @Before
    public void setUp() throws Exception {
        gameScreenController = new GameScreenController();
        MockitoAnnotations.initMocks(this);
        when(resultScreenController.getView()).thenReturn(parent);
        endRoundButton = new Button();
        powerDownbtn = new ToggleButton();

        injectJavaFXObjectToController("endRoundBtn", gameScreenController, endRoundButton);
        injectJavaFXObjectToController("powerDownbtn", gameScreenController, powerDownbtn);
        when(gameEngineWrapper.getWinner()).thenReturn(player);
        when(player.getRobot()).thenReturn(robot);
    }

    @Test
    public void shouldHandlePowerDownBtnAction() throws Exception {
        injectJavaFXObjectToController("powerDownService", gameScreenController, powerDownService);
        powerDownbtn.setSelected(true);
        callPrivateHandleMethod("handleEndRoundBtnAction", gameScreenController, actionEvent);
        verify(powerDownService).perform(anyInt());
    }

    @Test
    public void shouldHandleEndRoundBtnAction() throws Exception {
        injectJavaFXObjectToController("endBtnService", gameScreenController, endBtnService);
        callPrivateHandleMethod("handleEndRoundBtnAction", gameScreenController, actionEvent);
        verify(endBtnService).endRound(anyInt());
    }

    @Test
    public void shouldEndGame() throws IOException {
        gameScreenController.endGame();
        verify(saveGameStateService).finishGame();
        verify(changeViewService).changeView(null, parent);
    }

    @Test
    public void shouldStart() {
        gameScreenController.start(" ");
        verify(gameEngineWrapper).prepareRound();
        verify(gameFieldController).setUp(scrlPane, " ");
        verify(playerGridService).initPlayersGrid();
    }

    @Test
    public void shouldSetActivePlayer() throws Exception {
        List<PlayerInfoController> playerInfoControllerList = new ArrayList<>();
        playerInfoControllerList.add(playerInfoController);
        injectJavaFXObjectToController("playerInfos", gameScreenController, playerInfoControllerList);
        gameScreenController.setActivePlayer(0);
        verify(playerInfoController).setActive(true);
    }

    @Test
    public void shouldSetTheEndButtonToNextPlayer() throws Exception{
        gameScreenController.setNextButton(true);
        assertEquals(GameScreenController.NEXT_PLAYER, endRoundButton.getText());
    }

    @Test
    public void shouldSetTheEndButtonToEndRound() throws Exception{
        gameScreenController.setNextButton(false);
        assertEquals(GameScreenController.END_ROUND, endRoundButton.getText());
    }

    @Test
    public void shouldHideTheEndRoundButton() {
        gameScreenController.hideButtons(true);
        assertFalse(endRoundButton.isVisible());
    }

    @Test
    public void shouldShowTheEndRoundButton() {
        gameScreenController.hideButtons(false);
        assertTrue(endRoundButton.isVisible());
    }

    @Test
    public void shouldHideThePowerDownButton() {
        gameScreenController.hideButtons(true);
        assertFalse(powerDownbtn.isVisible());
    }

    @Test
    public void shouldShowThePowerDownButton() {
        gameScreenController.hideButtons(false);
        assertTrue(powerDownbtn.isVisible());
    }

}