package de.uni_bremen.factroytrouble.ai.ki1;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Testet die {@link TimerUnit}.
 * 
 * @author tim
 */
public class TimerUnitTest {

    private TimerUnit timerUnit = new TimerUnit();
    
    @Rule
    public final ExpectedException exception = ExpectedException.none();
    
    @Test
    public void testCalculation() throws InterruptedException {
        int calc42 = timerUnit.run(() -> 42, 0, "");
        assertEquals(42, calc42);
        
        exception.expect(IllegalStateException.class);
        timerUnit.run(() -> {
            throw new IllegalStateException();
        }, 0, "");
    }
    
    @Test
    public void testSleeping() throws InterruptedException {
        //TODO
    }
}
