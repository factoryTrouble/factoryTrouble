package de.uni_bremen.factroytrouble.ai.ki1.planning;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import de.uni_bremen.factroytrouble.ai.ais.AIPlayer1;
import de.uni_bremen.factroytrouble.api.ki1.Control;
import de.uni_bremen.factroytrouble.api.ki1.Control.RequestType;
import de.uni_bremen.factroytrouble.board.Tile;

public class FuturePlannerOneTest {
    @Test@Ignore
    public void startPlanningShouldGenearatePlanWithGoal() throws IOException {
        Control control = Mockito.mock(AIPlayer1.class);
        Tile flagTile= Mockito.mock(Tile.class);
        FuturePlannerOne fpo = new FuturePlannerOne(null, null, null);
        fpo.setController(control);
        Mockito.when(control.requestData(RequestType.NEXTFLAG, null)).thenReturn(flagTile);
        fpo.startPlanning();

        assertEquals(flagTile, ((GoalTile)fpo.getCurrentPlan().getGoals().peek()).getTile());
    }
}