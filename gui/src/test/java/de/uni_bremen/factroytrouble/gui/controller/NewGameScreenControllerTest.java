package de.uni_bremen.factroytrouble.gui.controller;

import de.saxsys.javafx.test.JfxRunner;
import de.uni_bremen.factroytrouble.gui.TestUtil;
import de.uni_bremen.factroytrouble.gui.controller.util.ChangeViewService;
import de.uni_bremen.factroytrouble.gui.generator.board.NewGameScreenBoardService;
import de.uni_bremen.factroytrouble.gui.services.GameEngineWrapper;
import de.uni_bremen.factroytrouble.gui.testrunner.JavaFXControllerTestRunner;
import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(JfxRunner.class)
public class NewGameScreenControllerTest extends JavaFXControllerTestRunner{

    @Mock private ResourceBundle bundle;
    @Mock private GridPane gridPane;
    @Mock private GameScreenController gameScreenController;
    @Mock private MenuScreenController menuScreenController;
    @Mock private OptionScreenController optionScreenController;
    @Mock private ChangeViewService changeViewService;
    @Mock private ActionEvent actionEvent;
    @Mock private GameEngineWrapper gameEngineWrapper;
    @Mock private NewGameScreenBoardService newGameScreenBoardService;


    @InjectMocks private NewGameScreenController newGameScreenController = new NewGameScreenController();

    @Before
    public void setUp()throws Exception {
        MockitoAnnotations.initMocks(this);
        List<String> boardList = new ArrayList<>();
        boardList.add("TestBoard1");
        boardList.add("TestBoard2");
        boardList.add("TestBoard3");
        boardList.add("TestBoard4");

        when(gameEngineWrapper.getAvailableBoards()).thenReturn(boardList);
        when(newGameScreenBoardService.getPreviewImage(anyString())).thenReturn(ImageIO.read(getClass().getResourceAsStream("/game/testBoard.png")));
    }
    
    @Test
    public void shouldGoToMenuScreenWhenButtonHandlerTriggered() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        callPrivateHandleMethod("handleBackBtnAction", newGameScreenController, actionEvent);
        assertEquals(true, true);
        verify(menuScreenController).getView();
        verify(changeViewService).changeView(any(Parent.class), any(Parent.class));
    }

    @Test
    public void shouldCallGridpaneAdd8TimesWhenDrawRobotsIsCalled() throws Exception {
        GridPane gridPane = mock(GridPane.class);
        injectJavaFXObjectToController("gridPane", newGameScreenController, gridPane);
        TestUtil.callPrivateMethodeWithoutParameter("drawRobots", newGameScreenController);
        verify(gridPane, times(8)).add(any(Node.class), anyInt(), anyInt());
    }

    @Test
    public void shouldCallGridpaneAdd8TimesWhenFillSelectPlayerTypeChoiceBoxIsCalled() throws Exception {
        injectJavaFXObjectToController("gridPane", newGameScreenController, gridPane);
        TestUtil.callPrivateMethodeWithoutParameter("fillSelectPlayerTypeChoiceBox", newGameScreenController);
        verify(gridPane, times(8)).add(any(Node.class), anyInt(), anyInt());
    }

    @Test
    public void shouldCallGridpaneAdd8TimesWhenfillPlayerNameFieldsIsCalled() throws Exception {
        injectJavaFXObjectToController("gridPane", newGameScreenController, gridPane);
        TestUtil.callPrivateMethodeWithoutParameter("fillSelectPlayerTypeChoiceBox", newGameScreenController);
        verify(gridPane, times(8)).add(any(Node.class), anyInt(), anyInt());
    }

    @Test
    @Ignore
    public void shouldCallPreviewGameFieldControllerShowPreviewWhenFillGameListIsCalled() throws Exception {
        ListView gameFieldList = mock (ListView.class);
        injectJavaFXObjectToController("gameFieldList", newGameScreenController, gameFieldList);
        TestUtil.callPrivateMethodeWithoutParameter("fillGameList", newGameScreenController);

        verify(gameFieldList).setOrientation(Orientation.HORIZONTAL);
    }

    

// Reflections für private Methoden -------------------------------------------------------------------------------------------
    
    private void callHandleContinueGameItemActionMethod(ActionEvent event)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method handleContinueGameItemAction = newGameScreenController.getClass().getDeclaredMethod("handleContinueGameItemAction",
                ActionEvent.class);
        handleContinueGameItemAction.setAccessible(true);
        handleContinueGameItemAction.invoke(newGameScreenController, event);
    }
    
    private void callHandleMenuScreenItemActionMethod(ActionEvent event)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method handleMenuScreenItemAction = newGameScreenController.getClass().getDeclaredMethod("handleMenuScreenItemAction",
                ActionEvent.class);
        handleMenuScreenItemAction.setAccessible(true);
        handleMenuScreenItemAction.invoke(newGameScreenController, event);
    }
    
    private void callHandleOptionScreenItemActionMethod(ActionEvent event)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method handleOptionScreenItemAction = newGameScreenController.getClass().getDeclaredMethod("handleOptionScreenItemAction",
                ActionEvent.class);
        handleOptionScreenItemAction.setAccessible(true);
        handleOptionScreenItemAction.invoke(newGameScreenController, event);
    }

    private class TestNewGameScreenController extends NewGameScreenController {

    }

}
