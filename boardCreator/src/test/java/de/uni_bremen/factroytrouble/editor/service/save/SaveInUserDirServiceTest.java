package de.uni_bremen.factroytrouble.editor.service.save;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static de.uni_bremen.factroytrouble.editor.TestUtil.readContentFromFile;
import static org.junit.Assert.*;

public class SaveInUserDirServiceTest {

    private static final String TEST_NAME = "EinSehrLangeUndUnoetigLangerNameFuerEinenTestUmDopplungenZuVermeiden";
    private static final String USER_DIR_PATH = System.getProperty("user.home") + File.separator + "factoryTrouble";
    private static final String BOARD_AND_DOCK_PATH = USER_DIR_PATH + File.separator + "boards" + File.separator + "descriptions";
    private static final String COURSE_PATH = USER_DIR_PATH + File.separator + "course";
    private static final String FILE_CONTENT = "FileContent";
    private static final String TEST_COURSE_MANUAL_NAME = "TEST_CUSTOM_COURSE_MANUAL";
    private SaveInUserDirService saveInUserDirService;

    @Before
    public void setUp() {
        saveInUserDirService = new SaveInUserDirService();
    }

    @After
    public void tearDown() {
        File descriptionDir = new File(BOARD_AND_DOCK_PATH);
        if(descriptionDir.exists()) {
            for(File file : descriptionDir.listFiles(pathname -> pathname.getName().contains(TEST_NAME))) {
                file.delete();
            }
        }
        for(File file : new File(COURSE_PATH).listFiles(pathname -> pathname.getName().equals(TEST_COURSE_MANUAL_NAME))) {
            file.delete();
        }
    }

    @Test
    public void shouldSaveABoard() throws Exception {
        saveInUserDirService.saveBoard(FILE_CONTENT, TEST_NAME);
        assertEquals(FILE_CONTENT, readContentFromFile(BOARD_AND_DOCK_PATH + File.separator + "FIELD_" + TEST_NAME));
    }

    @Test
    public void shouldSaveADock() throws Exception {
        saveInUserDirService.saveDock(FILE_CONTENT, TEST_NAME);
        assertEquals(FILE_CONTENT, readContentFromFile(BOARD_AND_DOCK_PATH + File.separator + "DOCK_" + TEST_NAME));
    }

    @Test
    public void shouldSaveACourse() throws Exception {
        saveInUserDirService.saveCourse(FILE_CONTENT, TEST_COURSE_MANUAL_NAME);
        assertEquals(FILE_CONTENT, readContentFromFile(COURSE_PATH + File.separator + TEST_COURSE_MANUAL_NAME));
    }

    @Test
    public void shouldAddALineSeparatorForTheSecondCousre() throws Exception {
        saveInUserDirService.saveCourse(FILE_CONTENT, TEST_COURSE_MANUAL_NAME);
        saveInUserDirService.saveCourse(FILE_CONTENT, TEST_COURSE_MANUAL_NAME);
        assertEquals(FILE_CONTENT + System.getProperty("line.separator") + FILE_CONTENT, readContentFromFile(COURSE_PATH + File.separator + TEST_COURSE_MANUAL_NAME));
    }

}