package de.uni_bremen.factroytrouble.editor.service.save;

import de.uni_bremen.factroytrouble.editor.service.serialisisation.SerializeBoardService;
import javafx.scene.layout.GridPane;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SaveBoardServiceTest {

    @Mock private SaveInUserDirService saveInUserDirService;
    @Mock private SerializeBoardService serializeBoardService;
    @InjectMocks private SaveBoardService saveBoardService = new SaveBoardService();

    @Mock private GridPane gridPane;

    @Before
    public void setUp() {
        when(serializeBoardService.serializeBoardGrid(anyInt(), anyInt(), any(GridPane.class))).thenReturn("TEST");
    }

    @Test
    public void shouldSerializeTheBoardInSaveProcess() {
        saveBoardService.saveBoard(gridPane, "TEST");
        verify(serializeBoardService).serializeBoardGrid(eq(12), eq(12), eq(gridPane));
    }

    @Test
    public void shouldSerializeTheDockInSaveProcess() {
        saveBoardService.saveDock(gridPane, "TEST");
        verify(serializeBoardService).serializeBoardGrid(eq(12), eq(4), eq(gridPane));
    }

    @Test
    public void shouldSaveTheBoardInsideTheUserHomeDirectory() {
        saveBoardService.saveBoard(gridPane, "TEST_NAME");
        verify(saveInUserDirService).saveBoard("TEST", "TEST_NAME");
    }

    @Test
    public void shouldSaveTheDockInsideTheUserHomeDirectory() {
        saveBoardService.saveDock(gridPane, "TEST_NAME");
        verify(saveInUserDirService).saveDock("TEST", "TEST_NAME");
    }
}