package de.uni_bremen.factroytrouble.gui.controller.components;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.image.BufferedImage;

/**
 * Created by johannes.gesenhues on 21.01.16.
 */
public class BoardImageViewTest {
    private BoardImageView boardImageView;

    @Before
    public void setUp() throws Exception {
        BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
       boardImageView =  new BoardImageView(bufferedImage);

    }

    @Test
    public void shouldSetFitWidthTo10() throws Exception {
        boardImageView.setZoom(10);
        Assert.assertTrue(boardImageView.getFitWidth()== 10);
    }
}