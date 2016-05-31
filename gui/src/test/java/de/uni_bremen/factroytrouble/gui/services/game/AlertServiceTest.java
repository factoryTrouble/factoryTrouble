package de.uni_bremen.factroytrouble.gui.services.game;

import de.saxsys.javafx.test.TestInJfxThread;
import javafx.scene.control.Alert;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

/**
 * Created by johannes.gesenhues on 16.01.16.
 */
public class AlertServiceTest {
    @Mock private Alert alert;
    @InjectMocks private AlertService alertService;

    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
//        //when(endRoundService.endRound(any())).thenReturn(EndRoundService.NOT_ALL_CARDS_CHOSEN, EndRoundService.NEXT_PLAYER, EndRoundService.ALL_PLAYERS_HAD_DRAW);
//        when(resultScreenController.getView()).thenReturn(parent);
    }

    @TestInJfxThread
    public void shouldCallShowAlertWithStrings() throws Exception {
        alertService.showAlert("title","headerText", "contentText");
        verify(alertService).showAlert(anyString(),anyString(),anyString());
    }
}