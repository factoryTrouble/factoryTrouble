package de.uni_bremen.factroytrouble.gui.generator.image;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

import static org.junit.Assert.*;
import static de.uni_bremen.factroytrouble.gui.TestUtil.*;

public class LoadImageFromUserDirServiceTest {

    private static final String TEST_PATH = "/test";
    private static final String TEST_FILE_NAME = "test.png";
    private static final String USER_HOME = System.getProperty("user.home") + "/factoryTrouble";

    private LoadImageFromUserDirService loadImageFromUserDirService;
    private BufferedImage bufferedImage;

    @Before
    public void setUp() throws Exception {
        loadImageFromUserDirService = new LoadImageFromUserDirService();
        bufferedImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        File path = new File(USER_HOME + TEST_PATH);
        if(!path.exists()) {
            path.mkdirs();
        }
        ImageIO.write(bufferedImage, "png", new FileOutputStream(USER_HOME + TEST_PATH +  "/" + TEST_FILE_NAME));
    }

    @After
    public void tearDown() {
        File file = new File(USER_HOME + TEST_PATH + "/" + TEST_FILE_NAME);
        file.delete();
        file = new File(USER_HOME + TEST_PATH);
        file.delete();
    }

    @Test
    public void shouldLoadAnImage() {
        assertImageEquals(bufferedImage, (BufferedImage) loadImageFromUserDirService.getImageByFileName(TEST_PATH + "/" + TEST_FILE_NAME));
    }

    @Test
    public void shouldReturnNullWhenImageNotFound() {
        assertNull(loadImageFromUserDirService.getImageByFileName(TEST_PATH + "/no_" + TEST_FILE_NAME));

    }

}