package de.uni_bremen.factroytrouble.gui.controller.components;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.image.BufferedImage;

/**
 * Created by johannes.gesenhues on 21.01.16.
 */
public class HandCardImageTest {
    private HandCardImage handCardImage;

    @Before
    public void setUp() throws Exception {
        BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        handCardImage = new HandCardImage(bufferedImage, 10);

    }


    @Test
    public void shouldCreateHandCardImageWithCorrectSettings(){
        Assert.assertTrue(handCardImage.getFitWidth() == 85);
        Assert.assertTrue(handCardImage.getFitHeight() == 96);
        Assert.assertTrue(handCardImage.getX() == 550);
        Assert.assertTrue(handCardImage.isPreserveRatio());
    }

}