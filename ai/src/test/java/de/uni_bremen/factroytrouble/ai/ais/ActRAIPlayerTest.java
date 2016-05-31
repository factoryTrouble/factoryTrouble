package de.uni_bremen.factroytrouble.ai.ais;

import de.uni_bremen.factroytrouble.ai.AIPlayer;
import de.uni_bremen.factroytrouble.ai.ki3.ActRUser;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.player.GamePlayer;
import de.uni_bremen.factroytrouble.player.ProgramCard;
import de.uni_bremen.factroytrouble.spring.InitSpring;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

/**
 * @author Thorben
 */
@RunWith(MockitoJUnitRunner.class)
public class ActRAIPlayerTest {
    private static final int GAME_ID = 0;

    @Mock
    Robot robot;
    @Mock
    ActRUser user;
    @Mock
    ProgramCard card1;
    @Mock
    ProgramCard card2;
    @Mock
    ProgramCard card3;
    @Mock
    ProgramCard card4;
    @Mock
    ProgramCard card5;

    List<ProgramCard> cardList = new ArrayList<>();

    AIPlayer player;

    @Before
    public void setUp() throws Exception {
        InitSpring.init();
        when(robot.getRegisters()).thenReturn(new ProgramCard[] { null, null, null, null, null });

        when(user.makeMove("t")).thenReturn(new int[] { 4, 1, 3, 2, -1 });
        
        cardList.add(card1);
        cardList.add(card2);
        cardList.add(card3);
        cardList.add(card4);
        cardList.add(card5);

        player = new ActRAIPlayer(GAME_ID, new GamePlayer(robot), user);
    }

    @Test
    public void executeTurnTest() {
        player.giveCards(cardList);

        player.executeTurn();
    }

}
