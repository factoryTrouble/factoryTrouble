package de.uni_bremen.factroytrouble.gui.controller;

import de.saxsys.javafx.test.JfxRunner;
import de.uni_bremen.factroytrouble.gui.TestUtil;
import de.uni_bremen.factroytrouble.gui.controller.util.ChangeViewService;
import de.uni_bremen.factroytrouble.gui.services.game.SaveGameStateService;
import de.uni_bremen.factroytrouble.spring.PostConstructTaskScheduler;
import de.uni_bremen.factroytrouble.spring.SpringConfigHolder;
import de.uni_bremen.factroytrouble.gui.testrunner.JavaFXControllerTestRunner;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import static de.uni_bremen.factroytrouble.gui.TestUtil.*;

@RunWith(JfxRunner.class)
public class FXMLControllerTest extends JavaFXControllerTestRunner {

    @Mock private GameScreenController gameScreenController;
    @Mock private NewGameScreenController newGameScreenController;
    @Mock private OptionScreenController optionScreenController;
    @Mock private MenuScreenController menuScreenController;
    @Mock private ChangeViewService changeViewService;
    @Mock private ActionEvent actionEvent;
    @Mock private PostConstructTaskScheduler postConstructTaskScheduler;
    @Mock private ApplicationContext applicationContext;
    @Mock private SaveGameStateService saveGameStateService;
    @InjectMocks private TestFXMLController testFXMLController = new TestFXMLController();

    private MenuBar menuBar = new MenuBar();
    @Mock private ObservableList<Menu> menus;
    @Mock private Menu menu;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        TestUtil.injectPrivateFieldIntoParent("menuBar", testFXMLController, menuBar);
        menuBar.getMenus().add(menu);

        testFXMLController.setFxmlFilePath("/views/testView.fxml");
        SpringConfigHolder.getInstance().setContext(applicationContext);
    }

    @Test
    public void shouldInsertItselfToPostConstructTaskScheduler() {
        testFXMLController.initialize(null, null);
        verify(postConstructTaskScheduler).add(eq(testFXMLController));
    }

    @Test
    public void shouldCreateANewViewWhenExternalsTryToGet() throws Exception{
        Parent firstInitView = testFXMLController.view;
        assertNotEquals(firstInitView, testFXMLController.getView());
    }

    @Test
    public void shouldSetOtherControllersFromSpringConfig() {
        testFXMLController.postConstructTask();
        verify(applicationContext, times(5)).getBean(any(Class.class));
    }

    @Test
    public void shouldLoadTheViewAfterPropertiesAreSet() throws Exception {
        assertNull(testFXMLController.view);
        testFXMLController.afterPropertiesSet();
        assertNotNull(testFXMLController.view);
    }

    @Test
    public void shouldCreateTheCorrectViewWhenExternalsTryToGet() throws Exception{
        testFXMLController.loadFXML();
        Parent testView = new FXMLLoader().load(getClass().getResourceAsStream("/views/testView.fxml"));
        assertParentEquals(testView, testFXMLController.getView());
    }

    @Test
    public void shouldGoBackToMenuViaMenuBarItem() throws Exception {
        testFXMLController.handleMenuScreenItemAction(actionEvent);
        verify(menuScreenController).getView();
        verify(changeViewService).changeView(any(Parent.class), any(Parent.class));
    }

    @Test
    public void shouldGoBackToNewGameScreenViaMenuBarBtn() throws Exception {
        when(actionEvent.getSource()).thenReturn(testFXMLController.newGameScreenMenuItem);
        testFXMLController.handleNewGameScreenItemAction(actionEvent);
        verify(newGameScreenController).getView();
        verify(changeViewService).changeView(any(Parent.class), any(Parent.class));
    }

    @Test
    public void shouldGoBackToOptionScreenViaMenuBarBtn() throws Exception {
        when(actionEvent.getSource()).thenReturn(testFXMLController.optionScreenMenuItem);
        testFXMLController.handleOptionScreenItemAction(actionEvent);
        verify(optionScreenController).getView();
        verify(changeViewService).changeView(any(Parent.class), any(Parent.class));
    }

    @Test
    public void shouldLoadView() throws Exception {
        testFXMLController.loadFXML();
        Parent testView = new FXMLLoader().load(getClass().getResourceAsStream("/views/testView.fxml"));
        assertParentEquals(testView, testFXMLController.view);
    }

    private class TestFXMLController extends FXMLController {

        @Override
        public void setFxmlFilePath(String filePath) {
            this.fxmlFilePath = filePath;
        }
    }

}