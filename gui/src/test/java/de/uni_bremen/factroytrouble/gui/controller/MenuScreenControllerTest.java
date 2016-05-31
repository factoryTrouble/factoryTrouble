package de.uni_bremen.factroytrouble.gui.controller;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.saxsys.javafx.test.JfxRunner;
import de.uni_bremen.factroytrouble.gui.controller.util.ChangeViewService;
import de.uni_bremen.factroytrouble.gui.services.game.SaveGameStateService;
import de.uni_bremen.factroytrouble.gui.testrunner.JavaFXControllerTestRunner;
import javafx.event.ActionEvent;
import javafx.scene.Parent;

@RunWith(JfxRunner.class)
public class MenuScreenControllerTest extends JavaFXControllerTestRunner{

    @Mock
    private GameScreenController gameScreenController;
    @Mock
    private NewGameScreenController newGameScreenController;
    @Mock
    private OptionScreenController optionScreenController;
    @Mock
    private ChangeViewService changeViewService;
    @Mock
    private ActionEvent actionEvent;
    @Mock
    private FXMLController fxmlController;
    @Mock 
    private SaveGameStateService saveGameStateService;
    @InjectMocks
    private MenuScreenController menuScreenController = new MenuScreenController();    

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void shouldGoToNewGameScreenWhenButtonHandlerTriggerd()
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        callPrivateHandleMethod("handleNewGameBtnAction", menuScreenController, actionEvent);
        verify(newGameScreenController).getView();
        verify(saveGameStateService).finishGame();
        verify(changeViewService).changeView(any(Parent.class), any(Parent.class));
    }
    
    @Test
    public void shouldGoToOptionScreenWhenButtonHandlerTriggerd()
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        callPrivateHandleMethod("handleOptionBtnAction", menuScreenController, actionEvent);
        verify(optionScreenController).getView();
        verify(changeViewService).changeView(any(Parent.class), any(Parent.class));
    }
    
    @Test
    public void shouldExitWhenButtonHandlerTriggerd()
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    }

// Reflections für private Methoden -------------------------------------------------------------------------------------------
    
    private void callHandleContinueGameItemActionMethod(ActionEvent event)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method handleContinueGameItemAction = menuScreenController.getClass().getDeclaredMethod("handleContinueGameItemAction",
                ActionEvent.class);
        handleContinueGameItemAction.setAccessible(true);
        handleContinueGameItemAction.invoke(menuScreenController, event);
    }
    
    private void callHandleNewGameScreenItemActionMethod(ActionEvent event)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method handleNewGameScreenItemAction = menuScreenController.getClass().getDeclaredMethod("handleNewGameScreenItemAction",
                ActionEvent.class);
        handleNewGameScreenItemAction.setAccessible(true);
        handleNewGameScreenItemAction.invoke(menuScreenController, event);
    }
    
    private void callHandleOptionScreenItemActionMethod(ActionEvent event)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method handleOptionScreenItemAction = menuScreenController.getClass().getDeclaredMethod("handleOptionScreenItemAction",
                ActionEvent.class);
        handleOptionScreenItemAction.setAccessible(true);
        handleOptionScreenItemAction.invoke(menuScreenController, event);
    }
}
