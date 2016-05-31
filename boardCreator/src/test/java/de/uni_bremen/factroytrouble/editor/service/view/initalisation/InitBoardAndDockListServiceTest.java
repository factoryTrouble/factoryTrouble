package de.uni_bremen.factroytrouble.editor.service.view.initalisation;

import de.saxsys.javafx.test.JfxRunner;
import de.uni_bremen.factroytrouble.editor.component.BoardAndDockItem;
import de.uni_bremen.factroytrouble.editor.service.load.FileContentService;
import javafx.collections.ObservableList;
import javafx.scene.layout.GridPane;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(JfxRunner.class)
public class InitBoardAndDockListServiceTest {

    private static final String TEST_NAME = "EinSehrLangeUndUnoetigLangerNameFuerEinenTestUmDopplungenZuVermeiden";
    private static final String USER_DIR_PATH = System.getProperty("user.home") + File.separator + "factoryTrouble" + File.separator + "boards" + File.separator + "descriptions";
    private static final String FIELD_PREFIX = "FIELD_";
    private static final String DOCK_PREFIX = "DOCK_";

    @Mock private FileContentService fileContentService;

    @InjectMocks private InitBoardAndDockListService initBoardAndDockListService;

    @Mock private GridPane gridPane;

    @Before
    public void setUp() {
        initBoardAndDockListService = new InitBoardAndDockListService();
        MockitoAnnotations.initMocks(this);
        when(gridPane.getChildren()).thenReturn(mock(ObservableList.class));
    }

    @After
    public void tearDown() {
        File descriptionDir = new File(USER_DIR_PATH);
        for(File file : descriptionDir.listFiles(pathname -> pathname.getName().contains(TEST_NAME))) {
            file.delete();
        }
    }

    @Test
    public void shouldAddABoardToList() throws Exception {
        ArgumentCaptor<BoardAndDockItem> boardAndDockItemArgumentCaptor = ArgumentCaptor.forClass(BoardAndDockItem.class);
        createTestFile(FIELD_PREFIX);
        initBoardAndDockListService.fillTreeView(gridPane);
        verify(gridPane, atLeast(1)).add(boardAndDockItemArgumentCaptor.capture(), anyInt(), anyInt());
        assertTrue(boardAndDockItemArgumentCaptor.getAllValues().contains(new BoardAndDockItem(FIELD_PREFIX + TEST_NAME, false)));
    }

    @Test
    public void shouldAddADockToList() throws Exception {
        ArgumentCaptor<BoardAndDockItem> boardAndDockItemArgumentCaptor = ArgumentCaptor.forClass(BoardAndDockItem.class);
        createTestFile(DOCK_PREFIX);
        initBoardAndDockListService.fillTreeView(gridPane);
        verify(gridPane, atLeast(1)).add(boardAndDockItemArgumentCaptor.capture(), anyInt(), anyInt());
        assertTrue(boardAndDockItemArgumentCaptor.getAllValues().contains(new BoardAndDockItem(DOCK_PREFIX + TEST_NAME, true)));
    }

    private void createTestFile(String prefix) throws Exception{
        new File(USER_DIR_PATH).mkdirs();
        File testFile = new File(USER_DIR_PATH + File.separator + prefix + TEST_NAME);
        testFile.createNewFile();
        BufferedWriter bw = null;
        try {
            FileWriter fw = new FileWriter(testFile.getAbsoluteFile());
            bw = new BufferedWriter(fw);
            bw.write("ti");
        } finally {
            if(bw != null) {
                bw.close();
            }
        }
    }

}