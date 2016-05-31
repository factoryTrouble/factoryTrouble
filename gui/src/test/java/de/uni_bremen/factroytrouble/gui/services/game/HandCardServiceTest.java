package de.uni_bremen.factroytrouble.gui.services.game;

import de.uni_bremen.factroytrouble.gui.controller.GameScreenController;
import de.uni_bremen.factroytrouble.gui.generator.card.CardGenerator;
import de.uni_bremen.factroytrouble.gui.services.GameEngineWrapper;
import de.uni_bremen.factroytrouble.player.ProgramCard;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class HandCardServiceTest {

    @Mock private GameEngineWrapper gameEngineWrapper;
    @Mock private GameScreenController gameScreenController;
    @Mock private CardGenerator cardGenerator;
    @Mock private RegisterCardsService registerCardsService;
    @InjectMocks private HandCardService handCardService = new HandCardService();

    @Mock private ProgramCard programCard;
    private List<ProgramCard> programCards;

    @Mock private AnchorPane cardPane;
    @Mock private ObservableList<Node> children;
    private ProgramCard[] programCardsInsideGameScreenController;

    @Before
    public void setUp() throws Exception {
        programCards = new ArrayList<>();
        programCards.add(programCard);
        when(gameEngineWrapper.getCards(0)).thenReturn(programCards);
        programCardsInsideGameScreenController = new ProgramCard[1];
        when(gameScreenController.getCards()).thenReturn(programCardsInsideGameScreenController);
        when(cardGenerator.generateCard(programCard)).thenReturn(ImageIO.read(getClass().getResourceAsStream("/game/cards/empty.png")));

        when(gameScreenController.getCardPane()).thenReturn(cardPane);
        when(cardPane.getChildren()).thenReturn(children);
    }

    @Test
    public void shouldGetAllCardsFromTheGameEngine() {
        handCardService.showCards(0);
        verify(gameEngineWrapper).getCards(0);
    }

    @Test
    public void shouldSetTheAProgramCardFromTheGameEngineInsideTheGameScreenController() {
        handCardService.showCards(0);
        assertEquals(programCard, programCardsInsideGameScreenController[0]);
    }

    @Test
    public void shouldGenerateTheProgrammCardWhenTheCardShows() {
        handCardService.showCards(0);
        verify(cardGenerator).generateCard(programCard);
    }

    @Test
    public void shouldAddAnImageToView() {
        handCardService.showCards(0);
        verify(children).add(any(ImageView.class));
    }

    @Test
    public void shouldAddACard() {
        handCardService.addCard(programCard);
        assertEquals(programCard, programCardsInsideGameScreenController[0]);
    }

    @Test
    public void shouldAddACardWhenSpaceIsEmpty() {
        assertTrue(handCardService.addCard(programCard));
    }

    @Test
    public void shouldNotAddACardWhenSpaceIsEmpty() {
        programCardsInsideGameScreenController[0] = programCard;
        assertFalse(handCardService.addCard(programCard));
    }

    @Test
    public void shouldGenerateTheCardWhenSpaceIsEmpty() {
        handCardService.addCard(programCard);
        verify(cardGenerator).generateCard(programCard);
    }

    @Test
    public void shouldRemoveAllCardsFromCardView() {
        handCardService.addCard(programCard);
        handCardService.dropAllCards();
        verify(children).remove(any(ImageView.class));
    }

}
