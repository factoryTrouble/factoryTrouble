package de.uni_bremen.factroytrouble.editor.controller;

import de.uni_bremen.factroytrouble.editor.TestUtil;
import de.uni_bremen.factroytrouble.editor.service.save.FileNameAlertService;
import de.uni_bremen.factroytrouble.editor.service.save.SaveBoardService;
import javafx.event.ActionEvent;
import javafx.scene.layout.GridPane;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DockCreateControllerTest {

    @Mock private SaveBoardService saveBoardService;
    @Mock private FileNameAlertService fileNameAlertService;
    @InjectMocks private DockCreateController dockCreateController = new DockCreateController();

    @Test
    public void shouldRequestTheFilename() throws Exception {
        TestUtil.callPrivateMethodeWithParameter("saveBoard", dockCreateController, new Class[]{ActionEvent.class}, new ActionEvent());
        verify(fileNameAlertService).showFilenameAlert("Dock");
    }

    @Test
    public void shouldNotSaveTheFileWhenTheFilenameIsEqualsNull() throws Exception {
        when(fileNameAlertService.showFilenameAlert(anyString())).thenReturn(null);
        TestUtil.callPrivateMethodeWithParameter("saveBoard", dockCreateController, new Class[]{ActionEvent.class}, new ActionEvent());
        verify(saveBoardService, never()).saveDock(any(GridPane.class), anyString());
    }

    @Test
    public void shouldSaveTheFileWhenTheFilenameIsNotEqualsNull() throws Exception {
        when(fileNameAlertService.showFilenameAlert(anyString())).thenReturn("TEST");
        TestUtil.callPrivateMethodeWithParameter("saveBoard", dockCreateController, new Class[]{ActionEvent.class}, new ActionEvent());
        verify(saveBoardService).saveDock(any(GridPane.class), eq("TEST"));
    }

}