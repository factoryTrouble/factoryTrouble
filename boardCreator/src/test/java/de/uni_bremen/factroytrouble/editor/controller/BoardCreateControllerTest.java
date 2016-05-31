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
public class BoardCreateControllerTest {

    @Mock private SaveBoardService saveBoardService;
    @Mock private FileNameAlertService fileNameAlertService;
    @InjectMocks private BoardCreateController boardCreateController = new BoardCreateController();

    @Test
    public void shouldRequestTheFilename() throws Exception {
        TestUtil.callPrivateMethodeWithParameter("saveBoard", boardCreateController, new Class[]{ActionEvent.class}, new ActionEvent());
        verify(fileNameAlertService).showFilenameAlert("Board");
    }

    @Test
    public void shouldNotSaveTheFileWhenTheFilenameIsEqualsNull() throws Exception {
        when(fileNameAlertService.showFilenameAlert(anyString())).thenReturn(null);
        TestUtil.callPrivateMethodeWithParameter("saveBoard", boardCreateController, new Class[]{ActionEvent.class}, new ActionEvent());
        verify(saveBoardService, never()).saveBoard(any(GridPane.class), anyString());
    }

    @Test
    public void shouldSaveTheFileWhenTheFilenameIsNotEqualsNull() throws Exception {
        when(fileNameAlertService.showFilenameAlert(anyString())).thenReturn("TEST");
        TestUtil.callPrivateMethodeWithParameter("saveBoard", boardCreateController, new Class[]{ActionEvent.class}, new ActionEvent());
        verify(saveBoardService).saveBoard(any(GridPane.class), eq("TEST"));
    }

}