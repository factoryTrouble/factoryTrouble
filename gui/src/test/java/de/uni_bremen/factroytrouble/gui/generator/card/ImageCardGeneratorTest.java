package de.uni_bremen.factroytrouble.gui.generator.card;

import de.uni_bremen.factroytrouble.player.ProgramCard;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

import static org.mockito.Mockito.*;
import static de.uni_bremen.factroytrouble.gui.TestUtil.assertImageEquals;

@RunWith(MockitoJUnitRunner.class)
public class ImageCardGeneratorTest {

    @Mock private CardImageDispatcher cardImageDispatcher;
    @Mock private ProgramCard programCard;
    @InjectMocks private ImageCardGenerator imageCardGenerator = new ImageCardGenerator();

    @Before
    public void setUp() throws Exception {
        imageCardGenerator.init();
        when(cardImageDispatcher.dispatch(any(ProgramCard.class))).thenReturn(ImageIO.read(getClass().getResourceAsStream("/game/cards/empty.png")));
    }

    @Test
    public void shouldGenerateACardWithPriorityGreaterHundret() throws Exception{
        when(programCard.getPriority()).thenReturn(123);
        BufferedImage bufferedImage = (BufferedImage) imageCardGenerator.generateCard(programCard);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/cards/cardPrioGreate100.png")), bufferedImage);
    }

    @Test
    public void shouldGenerateACardWithPriorityLessHundretGreaterTen() throws Exception{
        when(programCard.getPriority()).thenReturn(12);
        BufferedImage bufferedImage = (BufferedImage) imageCardGenerator.generateCard(programCard);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/cards/cardPrioLess100Greater10.png")), bufferedImage);
    }

    @Test
    public void shouldGenerateACardWithPriorityLessTen() throws Exception{
        when(programCard.getPriority()).thenReturn(1);
        BufferedImage bufferedImage = (BufferedImage) imageCardGenerator.generateCard(programCard);
        assertImageEquals(ImageIO.read(getClass().getResourceAsStream("/game/cards/cardPrioLess10.png")), bufferedImage);
    }

}