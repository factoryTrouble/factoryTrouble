package de.uni_bremen.factroytrouble.editor.service.load;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import static org.junit.Assert.*;

public class FileContentServiceTest {

    private FileContentService fileContentService;
    private File testFile ;

    @Before
    public void setUp() throws Exception {
        fileContentService = new FileContentService();
        testFile = File.createTempFile("testFile", ".txt");
        initTestFileContent();
    }

    @Test
    public void shouldGetAllLinesFromFileInListItemPerLine() {
        List<String> strings = fileContentService.getFileContentSeperatedByLine(testFile);
        assertEquals(2, strings.size());
    }

    @Test
    public void shouldParseTheFirstLineCorrect() {
        List<String> strings = fileContentService.getFileContentSeperatedByLine(testFile);
        assertEquals("TESTLINE_1", strings.get(0));
    }

    @Test
    public void shouldParseTheSecondLineCorrect() {
        List<String> strings = fileContentService.getFileContentSeperatedByLine(testFile);
        assertEquals("TESTLINE_2", strings.get(1));
    }

    private void initTestFileContent() throws Exception {
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(testFile.getAbsoluteFile());
            bw = new BufferedWriter(fw);
            bw.write("TESTLINE_1" + System.getProperty("line.separator") + "TESTLINE_2");
        } finally {
            if(bw != null) {
                bw.close();
            }
        }
    }

}