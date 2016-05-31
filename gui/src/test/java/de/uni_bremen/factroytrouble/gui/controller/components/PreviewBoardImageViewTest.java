package de.uni_bremen.factroytrouble.gui.controller.components;

import org.junit.Before;
import org.junit.Test;

import java.awt.image.BufferedImage;

import static org.junit.Assert.assertTrue;

/**
 * Created by johannes.gesenhues on 21.01.16.
 */
public class PreviewBoardImageViewTest {
    PreviewBoardImageView previewBoardImageView;

    @Before
    public void setUp() throws Exception {
        BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        previewBoardImageView = new PreviewBoardImageView(bufferedImage);
    }



    @Test
    public void shouldCreatePreviewBoardImageViewWithCorrectSettings() {
        assertTrue(previewBoardImageView.getFitHeight() == 150);
        assertTrue(previewBoardImageView.isPreserveRatio());
    }
}