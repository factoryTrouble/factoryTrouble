package de.uni_bremen.factroytrouble.gui.controller.components;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import org.junit.Before;
import org.junit.Test;

import static de.uni_bremen.factroytrouble.gui.TestUtil.assertImageEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by johannes.gesenhues on 21.01.16.
 */
public class PreviewRobotImageViewTest {
    PreviewRobotImageView previewRobotImageView;

    @Before
    public void setUp() throws Exception {
        previewRobotImageView = new PreviewRobotImageView(0);
    }



    @Test
    public void shouldCreatePreviewBoardImageViewWithCorrectSettings() {
        assertTrue(previewRobotImageView.getFitHeight() == 49);
        assertTrue(previewRobotImageView.isPreserveRatio());
        assertTrue(previewRobotImageView.isPickOnBounds());
        assertTrue(previewRobotImageView.getViewport().equals(Rectangle2D.EMPTY));
        assertImageEquals(SwingFXUtils.fromFXImage(previewRobotImageView.getImage(), null),SwingFXUtils.fromFXImage(new Image(PreviewRobotImageView.PATH + 0 + ".png"),null));

    }

}