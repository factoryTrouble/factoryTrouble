package de.uni_bremen.factroytrouble.ai.ki1.memory;

import static org.junit.Assert.*;

import de.uni_bremen.factroytrouble.api.ki1.memory.MemoryEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameter;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

import de.uni_bremen.factroytrouble.gameobjects.Robot;

public class EventTest {
    @Parameter
    MemoryEvent event;
    Robot roberta;
    private static final int round = 3;
    
    @Before
    public void create(){
        roberta = mock(Robot.class);
        Mockito.when(roberta.getName()).thenReturn("roberta");
        event = new RobotEvent(roberta, RobotEvent.EventType.KILLEDME, round);
    }
    
    @Test
    public void testIncrement(){
        event.increment();
        assertEquals(2, (int) event.getCount());
    }
    
    @Test
    public void testGetRobot(){
        assertEquals(roberta, ((RobotEvent) event).getRobot());
    }
    
    @Test
    public void testEventType(){
        assertEquals(RobotEvent.EventType.KILLEDME, ((RobotEvent) event).getEventType());
    }

    @Test
    public void testEventUnitEqualsSameReference() {
        assertEquals(event, event);
    }

    @Test
    public void testEventUnitEqualsEqual() {
        RobotEvent otherEvent = new RobotEvent(roberta, RobotEvent.EventType.KILLEDME, round);
        assertEquals(event, otherEvent);
    }

    @Test
    public void testEventUnitEqualsFirstNull() {
        assertNotEquals(null, event);
    }

    @Test
    public void testEventUnitEqualsOtherNull() {
        assertNotEquals(event, null);
    }

    @Test
    public void testEventUnitEqualsNotSameClass() {
        assertNotEquals(event, new Integer(42));
    }

    @Test
    public void testEventUnitEqualsCountsDiffer() {
        RobotEvent otherEvent = new RobotEvent(roberta, RobotEvent.EventType.KILLEDME, round);
        event.increment();
        assertNotEquals(event, otherEvent);
    }

    @Test
    public void testEventUnitEqualsDifferentEventType() {
        RobotEvent otherEvent = new RobotEvent(roberta, RobotEvent.EventType.SHOTME, round);
        assertNotEquals(event, otherEvent);
    }

    @Test
    public void testEventUnitEqualsRobotNamesDiffer() {
        Robot robert = mock(Robot.class);
        Mockito.when(robert.getName()).thenReturn("robert");
        RobotEvent otherEvent = new RobotEvent(robert, RobotEvent.EventType.KILLEDME, round);
        assertNotEquals(event, otherEvent);
    }

    @Test
    public void testEventUnitEqualsOtherRobotNull() {
        RobotEvent otherEvent = new RobotEvent(null, RobotEvent.EventType.KILLEDME, round);
        assertNotEquals(event, otherEvent);
    }
}
