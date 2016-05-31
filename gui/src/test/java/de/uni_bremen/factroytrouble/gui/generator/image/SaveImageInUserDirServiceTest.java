package de.uni_bremen.factroytrouble.gui.generator.image;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

import static org.junit.Assert.*;
import static de.uni_bremen.factroytrouble.gui.TestUtil.*;

public class SaveImageInUserDirServiceTest {

    private static final String TEST_PATH = "/test";
    private static final String TEST_FILE_NAME = "test.png";
    private static final String USER_HOME = System.getProperty("user.home") + "/factoryTrouble";

    private SaveImageInUserDirService saveImageInUserDirService;

    @Before
    public void setUp() {
        saveImageInUserDirService = new SaveImageInUserDirService();
    }

    @After
    public void tearDown() {
        File file = new File(USER_HOME + TEST_PATH + "/" + TEST_FILE_NAME);
        file.delete();
        file = new File(USER_HOME + TEST_PATH);
        file.delete();
    }

    @Test
    public void shouldSaveAnBufferedImage() throws Exception {
        BufferedImage bufferedImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        saveImageInUserDirService.save(bufferedImage, TEST_PATH, TEST_FILE_NAME);
        assertImageEquals(bufferedImage, ImageIO.read(new FileInputStream(USER_HOME + TEST_PATH + "/" + TEST_FILE_NAME)));
    }

    @Test
    public void shouldResizeAnImage() throws Exception {
        BufferedImage bufferedImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        saveImageInUserDirService.saveResized(bufferedImage, TEST_PATH, TEST_FILE_NAME, 10);
        assertEquals(10, ImageIO.read(new FileInputStream(USER_HOME + TEST_PATH + "/" + TEST_FILE_NAME)).getHeight());
        assertEquals(10, ImageIO.read(new FileInputStream(USER_HOME + TEST_PATH + "/" + TEST_FILE_NAME)).getWidth());
    }

    @Test
    public void shouldCreateTheFileWhenFolderExists() throws Exception {
        new File(USER_HOME + TEST_PATH).mkdirs();
        BufferedImage bufferedImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        saveImageInUserDirService.save(bufferedImage, TEST_PATH, TEST_FILE_NAME);
        assertImageEquals(bufferedImage, ImageIO.read(new FileInputStream(USER_HOME + TEST_PATH + "/" + TEST_FILE_NAME)));
    }

}