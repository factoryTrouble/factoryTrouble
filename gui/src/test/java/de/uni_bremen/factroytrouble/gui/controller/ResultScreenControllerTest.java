package de.uni_bremen.factroytrouble.gui.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import java.lang.reflect.InvocationTargetException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.saxsys.javafx.test.JfxRunner;
import de.uni_bremen.factroytrouble.gui.controller.util.ChangeViewService;
import de.uni_bremen.factroytrouble.gui.services.GameEngineWrapper;
import de.uni_bremen.factroytrouble.gui.testrunner.JavaFXControllerTestRunner;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.Label;

@RunWith(JfxRunner.class)
public class ResultScreenControllerTest extends JavaFXControllerTestRunner {

    private static final String TEST_TEXT = "Test1";

    private static final Integer TEST_LABEL = 1;
    @Mock
    private MenuScreenController menuScreenController;
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
    private GameEngineWrapper gameEngineWrapper;
    @InjectMocks
    private ResultScreenController resultScreenController = new ResultScreenController();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldGoBackToMainMenuWhenButtonHandlerTriggered()
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        callPrivateHandleMethod("handleBackToMenuBtnAction", resultScreenController, actionEvent);
        verify(menuScreenController).getView();
        verify(changeViewService).changeView(any(Parent.class), any(Parent.class));
    }
    
    @Test
    public void shouldSetWinnerLabelAccordingly()
            throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Label label = new Label();
        injectJavaFXObjectToController("winnerLabel", resultScreenController, label);
        resultScreenController.setWinnerLabel("Winner");
        assertEquals("Winner", label.getText());
    }
}
