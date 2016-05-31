package de.uni_bremen.factroytrouble.gui.controller;

import de.saxsys.javafx.test.JfxRunner;
import de.uni_bremen.factroytrouble.gui.controller.util.ChangeViewService;
import de.uni_bremen.factroytrouble.gui.services.game.SoundService;
import de.uni_bremen.factroytrouble.gui.sound.MediaPlayerControls;
import de.uni_bremen.factroytrouble.gui.testrunner.JavaFXControllerTestRunner;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(JfxRunner.class)
public class OptionScreenControllerTest extends JavaFXControllerTestRunner {

    @Mock
    private FXMLController fxmlController;
    @Mock
    private GameScreenController gameScreenController;
    @Mock
    private NewGameScreenController newGameScreenController;
    @Mock private MediaPlayerControls mediaPlayerControls;
    @Mock private SoundService soundService;
    @Mock private MenuScreenController menuScreenController;
    @Mock private ChangeViewService changeViewService;
    @Mock private ActionEvent actionEvent;
    @InjectMocks private OptionScreenController optionScreenController = new OptionScreenController();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldGoBackToMainMenuWhenButtonHandlerTriggered() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        callPrivateHandleMethod("handleBackBtnAction", optionScreenController, actionEvent);
        verify(menuScreenController).getView();
        verify(changeViewService).changeView(any(Parent.class), any(Parent.class));
    }

    @Test
    public void shouldSetANewVolumeSizeWhenVolumeSlideIsDraged() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        Slider volumeSlider = new Slider();
        volumeSlider.setValue(80);
        injectJavaFXObjectToController("volumeSlider", optionScreenController, volumeSlider);

        CheckBox musicCheckBox = new CheckBox();
        musicCheckBox.setSelected(true);
        injectJavaFXObjectToController("musicCheckBox", optionScreenController, musicCheckBox);

        callPrivateHandleMethod("handleVolumeDrag", optionScreenController, actionEvent);
        verify(mediaPlayerControls).setVolume(eq(0.8));
    }

    @Test
    public void shouldUnmuteTheMusicAutomaticWhenVolumeSliderDragged() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        Slider volumeSlider = new Slider();
        volumeSlider.setValue(100);
        injectJavaFXObjectToController("volumeSlider", optionScreenController, volumeSlider);

        CheckBox musicCheckBox = new CheckBox();
        musicCheckBox.setSelected(false);
        injectJavaFXObjectToController("musicCheckBox", optionScreenController, musicCheckBox);

        callPrivateHandleMethod("handleVolumeDrag", optionScreenController, actionEvent);
        assertTrue(musicCheckBox.isSelected());
    }

    @Test
    public void shouldMuteTheMusic() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        CheckBox musicCheckBox = new CheckBox();
        musicCheckBox.setSelected(false);
        injectJavaFXObjectToController("musicCheckBox", optionScreenController, musicCheckBox);

        callPrivateHandleMethod("handleMusicCheckbox", optionScreenController, actionEvent);
        verify(mediaPlayerControls).stop();
    }
    
    @Test
    public void shouldMuteTheSound() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        CheckBox soundCheckBox = new CheckBox();
        soundCheckBox.setSelected(false);
        injectJavaFXObjectToController("soundCheckBox", optionScreenController, soundCheckBox);

        callPrivateHandleMethod("handleSoundCheckbox", optionScreenController, actionEvent);
        verify(soundService).setActivated(false);
    }

    @Test
    public void shouldUnmuteTheMusic() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        CheckBox musicCheckBox = new CheckBox();
        musicCheckBox.setSelected(true);
        injectJavaFXObjectToController("musicCheckBox", optionScreenController, musicCheckBox);

        Slider volumeSlider = new Slider();
        volumeSlider.setValue(80);
        injectJavaFXObjectToController("volumeSlider", optionScreenController, volumeSlider);

        callPrivateHandleMethod("handleMusicCheckbox", optionScreenController, actionEvent);
        verify(mediaPlayerControls).play();
    }
    
// Reflections für private Methoden -------------------------------------------------------------------------------------------
    
    private void callHandleMenuScreenItemActionMethod(ActionEvent event)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method handleMenuScreenItemAction = optionScreenController.getClass().getDeclaredMethod("handleMenuScreenItemAction",
                ActionEvent.class);
        handleMenuScreenItemAction.setAccessible(true);
        handleMenuScreenItemAction.invoke(optionScreenController, event);
    }
    
    private void callHandleContinueGameItemActionMethod(ActionEvent event)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method handleContinueGameItemAction = optionScreenController.getClass().getDeclaredMethod("handleContinueGameItemAction",
                ActionEvent.class);
        handleContinueGameItemAction.setAccessible(true);
        handleContinueGameItemAction.invoke(optionScreenController, event);
    }
    
    private void callHandleNewGameScreenItemActionMethod(ActionEvent event)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method handleNewGameScreenItemAction = optionScreenController.getClass().getDeclaredMethod("handleNewGameScreenItemAction",
                ActionEvent.class);
        handleNewGameScreenItemAction.setAccessible(true);
        handleNewGameScreenItemAction.invoke(optionScreenController, event);
    }
}