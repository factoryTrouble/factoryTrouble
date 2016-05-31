package de.uni_bremen.factroytrouble.gui.services.game;

import de.uni_bremen.factroytrouble.gui.controller.GameScreenController;
import de.uni_bremen.factroytrouble.gui.generator.card.CardGenerator;
import de.uni_bremen.factroytrouble.gui.generator.card.EmptyCard;
import de.uni_bremen.factroytrouble.gui.services.GameEngineWrapper;
import de.uni_bremen.factroytrouble.player.ProgramCard;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RegisterCardsServiceTest {

    @Mock private GameScreenController gameScreenController;
    @Mock private CardGenerator cardGenerator;
    @Mock private GameEngineWrapper gameEngineWrapper;
    @Mock private HandCardService handCardService;
    @Mock private ProgramCard programCard;
    @Mock private AnchorPane anchorPane;
    @Mock private ObservableList<Node> observableList;
    @Mock private EmptyCard emptyCard;
    @Mock private Map<ProgramCard,ImageView> registerImageViews;
    @Mock private Set<ProgramCard> keySet;
    @Mock private Iterator<ProgramCard> iterator;
    @InjectMocks private RegisterCardsService registerCardsService = new RegisterCardsService();
    
    private ProgramCard[] programCards = new ProgramCard[5];
    
    @Before
    public void setUp() throws IOException{
        when(gameScreenController.getActivePlayer()).thenReturn(0);
        when(gameEngineWrapper.getPlayerHP(0)).thenReturn(2,1);
        when(gameScreenController.getRegisters()).thenReturn(programCards);
        when(gameEngineWrapper.getCard(anyInt(), anyInt())).thenReturn(programCard);
        when(cardGenerator.generateCard(any())).thenReturn(ImageIO.read(getClass().getResourceAsStream("/game/cards/cardPrioGreate100.png")));
        when(gameScreenController.getRegisterPane()).thenReturn(anchorPane);
        when(anchorPane.getChildren()).thenReturn(observableList);
        when(gameEngineWrapper.getLockedRegisters(0)).thenReturn(new boolean[]{true, false});
        when(registerImageViews.keySet()).thenReturn(keySet);
        when(keySet.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, true, false);
        
    }
    
    @Test
    public void shouldInitRegisters(){
        registerCardsService.initRegisters(0);

        verify(gameEngineWrapper).getLockedRegisters(0);     //5+5 in drawRegisterCard
//        verify(gameScreenController, times(10)).getRegisters();     //5+5 in drawRegisterCard
//        verify(gameEngineWrapper, times(8)).getCard(anyInt(), anyInt());    //4+4 in drawRegisterCard
    }
    
    @Test
    public void shouldSetRegisterCard() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException{
        callsetRegisterCardMethod(0, programCard);
        verify(gameScreenController, times(2)).getRegisters();      //1 + 1 in drawRegisterCard
        verify(gameEngineWrapper).emptyRegister(0, 0);
        verify(gameEngineWrapper).fillRegister(0, 0, programCard);
    }
    
    @Test
    public void shouldAddRegisterCardFail(){
        assertFalse(registerCardsService.addRegisterCard(programCard));
        verify(gameScreenController, times(11)).getRegisters(); //6+5 in schleife
    }
    
    @Test
    public void shouldAddRegisterCardSucess(){
        programCards[0] = emptyCard;
        assertTrue(registerCardsService.addRegisterCard(programCard));
        verify(gameScreenController, times(4)).getRegisters(); //2 in schleife + 2 in setRegisterCard
    }
    
    @Test
    public void shouldDrawRegisterCard() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException{
        calldrawRegisterCardMethod(0, programCard);
        verify(cardGenerator).generateCard(programCard);
        verify(gameScreenController).getRegisterPane();
        verify(anchorPane).getChildren();
        verify(gameScreenController).getRegisters();
        verify(registerImageViews).put(any(), any());
    }
    
    @Test @Ignore
    public void shouldRemoveCard() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException{
        callRemoveCardMethod(0, programCard);
        verify(gameEngineWrapper).getLockedRegisters(0);
        callRemoveCardMethod(1, programCard);
        verify(gameScreenController).getRegisters();
        verify(anchorPane).getChildren();
    }
    
    @Test@Ignore // Irgentwie komisch
    public void shouldDropAllCards(){
        registerCardsService.dropAllCards(0);
        verify(gameScreenController, times(7)).getRegisterPane();   //2 + 5 von initRegisters-> drawRegisterCard
        verify(anchorPane, times(7)).getChildren();
        verify(observableList, times(2)).remove(any());
    }
    
    @Test
    public void shouldAllRegisterCardsAreSetFail(){
        programCards[0] = emptyCard;
        assertFalse(registerCardsService.allRegisterCardsAreSet());
    }
    
    @Test
    public void shouldAllRegisterCardsAreSetSucess(){
        assertTrue(registerCardsService.allRegisterCardsAreSet());
    }
    
    // Reflections ----------------------------------------------------------------------------------------
    
    private void callsetRegisterCardMethod(int pos, ProgramCard programCard)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method handlesetRegisterCard = registerCardsService.getClass().getDeclaredMethod("setRegisterCard",
                int.class, ProgramCard.class);
        handlesetRegisterCard.setAccessible(true);
        handlesetRegisterCard.invoke(registerCardsService, pos, programCard);
    }
    
    private void calldrawRegisterCardMethod(int pos, ProgramCard programCard)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method handledrawRegisterCard = registerCardsService.getClass().getDeclaredMethod("drawRegisterCard",
                int.class, ProgramCard.class);
        handledrawRegisterCard.setAccessible(true);
        handledrawRegisterCard.invoke(registerCardsService, pos, programCard);
    }
    
    private void callRemoveCardMethod(int pos, ProgramCard programCard)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method handleRemoveCard = registerCardsService.getClass().getDeclaredMethod("removeCard",
                int.class, ProgramCard.class);
        handleRemoveCard.setAccessible(true);
        handleRemoveCard.invoke(registerCardsService, pos, programCard);
    }
}