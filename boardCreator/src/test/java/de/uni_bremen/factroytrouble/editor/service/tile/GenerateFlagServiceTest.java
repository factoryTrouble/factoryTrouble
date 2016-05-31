package de.uni_bremen.factroytrouble.editor.service.tile;

import org.junit.Before;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import static de.uni_bremen.factroytrouble.editor.TestUtil.assertImageEquals;
import static org.mockito.Mockito.mock;

/**
 * Created by andreohlrogge on 19.03.16.
 */
public class GenerateFlagServiceTest {

    private GenerateFlagService generateFlagService;

    @Before
    public void setUp() {
        generateFlagService = new GenerateFlagService();
        generateFlagService.init();
    }

    @Test
    public void shouldGenerateTheFlagImageWithANumberLessThen10() throws Exception {
        BufferedImage flagTileImage = generateFlagService.generateFlagTileImage(1);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/flag_with_1.png")), flagTileImage);
    }

    @Test
    public void shouldGenerateTheFlagImageWithANumberGreaterThen9AndLessThen100() throws Exception {
        BufferedImage flagTileImage = generateFlagService.generateFlagTileImage(10);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/flag_with_10.png")), flagTileImage);
    }

    @Test(expected = GenerateFlagService.FlagNumberToHighException.class)
    public void shouldThrowAFlagNumberToHighExceptionWhenFlagNumberGreaterThen99() {
        generateFlagService.generateFlagTileImage(100);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowANullPointerExceptionWhenTileDoNotHaveAFlagObject() {
        generateFlagService.generateFlagTileImage(null);
    }

}