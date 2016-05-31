package de.uni_bremen.factroytrouble.ai.ais;

import de.uni_bremen.factroytrouble.ai.AIFactory;
import de.uni_bremen.factroytrouble.ai.AIPlayer;
import de.uni_bremen.factroytrouble.player.Player;
import de.uni_bremen.factroytrouble.spring.InitSpring;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

/**
 * @author Thorben
 */
@RunWith(MockitoJUnitRunner.class)
public class GlobalAIFactoryTest {
    private static final int GAME_ID = 0;

    @Mock
    Player player;
    
    AIFactory factory;

    @Before
    public void setUp() throws Exception {
        InitSpring.init();
        factory = new GlobalAIFactory();
    }
    
    @Test
    public void instantiationTest() throws Exception{
        AIPlayer player = factory.getAIPlayer(GAME_ID, "blarg", this.player);
        assertTrue(player.getClass().getSimpleName().equals("RandomAI"));
        
        player = factory.getAIPlayer(GAME_ID,"RandomAI", this.player);
        assertNotEquals(null, player);
        assertTrue(player.getClass().getSimpleName().equals("RandomAI"));
    }

}
