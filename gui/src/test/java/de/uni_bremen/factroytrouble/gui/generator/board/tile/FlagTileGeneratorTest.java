package de.uni_bremen.factroytrouble.gui.generator.board.tile;

import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.gameobjects.FieldObject;
import de.uni_bremen.factroytrouble.gameobjects.Flag;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import static de.uni_bremen.factroytrouble.gui.TestUtil.assertImageEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FlagTileGeneratorTest {

    @Mock private Tile tile;
    @Mock private Flag flag;
    private FlagTileGenerator flagTileGenerator;

    @Before
    public void setUp() {
        flagTileGenerator = new FlagTileGenerator();
        flagTileGenerator.init();
        when(tile.getFieldObject()).thenReturn(flag);
    }

    @Test
    public void shouldGenerateTheFlagImageWithANumberLessThen10() throws Exception {
        when(flag.getNumber()).thenReturn(1);
        BufferedImage flagTileImage = flagTileGenerator.generateFlagTileImage(tile);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/flag_with_1.png")), flagTileImage);
    }

    @Test
    public void shouldGenerateTheFlagImageWithANumberGreaterThen9AndLessThen100() throws Exception {
        when(flag.getNumber()).thenReturn(10);
        BufferedImage flagTileImage = flagTileGenerator.generateFlagTileImage(tile);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/tiles/flag_with_10.png")), flagTileImage);
    }

    @Test(expected = FlagTileGenerator.FlagNumberToHighException.class)
    public void shouldThrowAFlagNumberToHighExceptionWhenFlagNumberGreaterThen99() {
        when(flag.getNumber()).thenReturn(100);
        flagTileGenerator.generateFlagTileImage(tile);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowANullPointerExceptionWhenTileDoNotHaveAFlagObject() {
        when(tile.getFieldObject()).thenReturn(null);
        flagTileGenerator.generateFlagTileImage(tile);
    }

    @Test(expected = ClassCastException.class)
    public void shouldThrowACanNotCastExceptionWhenGameObjectIsNotAFlag() {
        when(tile.getFieldObject()).thenReturn(mock(FieldObject.class));
        flagTileGenerator.generateFlagTileImage(tile);
    }

}