package de.uni_bremen.factroytrouble.gui.controller.components;

import javafx.embed.swing.SwingFXUtils;
import org.junit.Before;
import org.junit.Test;

import java.awt.image.BufferedImage;

import static de.uni_bremen.factroytrouble.gui.TestUtil.assertImageEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by johannes.gesenhues on 21.01.16.
 */
public class RegisterCardImageViewTest {
    RegisterCardImageView registerCardImageView;

    @Before
    public void setUp() throws Exception {
        BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        registerCardImageView = new RegisterCardImageView(bufferedImage,1);
    }



    @Test
    public void shouldCreateRegisterCardImageViewWithCorrectSettings() {
        assertTrue(registerCardImageView.getFitHeight() == 96);
        assertTrue(registerCardImageView.getFitWidth() == 85);
        assertTrue(registerCardImageView.getX() == 154);
        BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        assertImageEquals(SwingFXUtils.fromFXImage(registerCardImageView.getImage(), null),bufferedImage);

    }

}