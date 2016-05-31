package de.uni_bremen.factroytrouble.gui.observer;

import de.uni_bremen.factroytrouble.gui.services.GameEngineWrapper;
import de.uni_bremen.factroytrouble.observer.GameObserver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GUIEngineObserverRegisterHandlerTest {

    @Mock private GameEngineWrapper gameEngineWrapper;
    @Mock private GameObserver gameObserver;
    @InjectMocks private GUIEngineObserverRegisterHandler guiEngineObserverRegisterHandler = new GUIEngineObserverRegisterHandler();

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenObserverIsNull() {
        guiEngineObserverRegisterHandler.registerMe(null);
        verify(gameEngineWrapper, never()).attachObserver(any(GameObserver.class));
    }

    @Test
    public void shouldRegisterAnObserver() {
        guiEngineObserverRegisterHandler.registerMe(gameObserver);
        verify(gameEngineWrapper).attachObserver(eq(gameObserver));
    }


}