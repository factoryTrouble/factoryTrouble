package de.uni_bremen.factroytrouble.ai.ki3;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class AI3ResourceFileHandlerTest {

    private static final String PATH = System.getProperty("user.home").concat(File.separator).concat("factoryTrouble").concat(File.separator).concat("ki3");
    private AI3ResourceFileHandler handler;

    @Before
    public void setUp() throws Exception{
        handler = new AI3ResourceFileHandler();

        new File(PATH).mkdirs();
        FileUtils.deleteDirectory(new File(PATH));
    }

    @After
    public void tearDown() throws Exception{
        FileUtils.deleteDirectory(new File(PATH));
    }

    @Test
    public void shouldCopyAndUnzipResources() {
        handler.copyResourcesToDir(PATH);
        assertEquals(3, new File(PATH).list().length);
    }

    @Test
    public void shouldNotCopyResourceWhenDirectoryAlreadyExists() throws Exception{
        new File(PATH).mkdirs();
        File testFile = new File(PATH + File.separator + "empty");
        testFile.createNewFile();
        handler.copyResourcesToDir(PATH);
        assertEquals(1, new File(PATH).list().length);
    }

}