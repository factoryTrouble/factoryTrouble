package de.uni_bremen.factroytrouble.gui.services.game;

import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.gui.controller.GameScreenController;
import de.uni_bremen.factroytrouble.gui.services.GameEngineWrapper;
import de.uni_bremen.factroytrouble.player.Master;
import de.uni_bremen.factroytrouble.player.Player;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PowerDownServiceTest {

    @Mock private GameEngineWrapper gameEngineWrapper;
    @Mock private GameScreenController gameScreenController;
    @Mock private Master master;
    @Mock private Player player1;
    @Mock private Player player2;
    @Mock private Robot robot;
    
    @InjectMocks private PowerDownService powerDownService = new PowerDownService();
    
    private List<Player> playerList = new ArrayList<>();
    
    @Before
    public void setUp() {
        playerList.add(player1);
        playerList.add(player2);
    }

    @Test@Ignore
    public void shouldCall_GameEngineWrapper_GetPlayerByNumber_In_Perform() {
        when(gameEngineWrapper.getPlayerByNumber(anyInt())).thenReturn(player1);
        when(player1.getRobot()).thenReturn(robot);
        powerDownService.perform(0);
        verify(gameEngineWrapper.getPlayerByNumber(0).getRobot()).powerDown();
    }
}
