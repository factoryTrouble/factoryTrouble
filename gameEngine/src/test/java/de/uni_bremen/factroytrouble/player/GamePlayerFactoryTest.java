package de.uni_bremen.factroytrouble.player;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;


@RunWith(MockitoJUnitRunner.class)
public class GamePlayerFactoryTest {
    private static final int GAME_ID = 0;

    private GamePlayerFactory gpf;

    @Test
    public void testCreateNewPlayer() {
        gpf = new GamePlayerFactory();

        assertEquals(gpf.createNewPlayer(GAME_ID, "SPINBOT", "AGENT_1").getRobot().getName(), "SPINBOT");
        assertEquals(gpf.createNewPlayer(GAME_ID, null, "AGENT_1").getRobot().getName(), null);
    }

}
