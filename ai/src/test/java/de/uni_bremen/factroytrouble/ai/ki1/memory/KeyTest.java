package de.uni_bremen.factroytrouble.ai.ki1.memory;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameter;
import org.mockito.Mockito;

import de.uni_bremen.factroytrouble.gameobjects.FieldObject;
import de.uni_bremen.factroytrouble.gameobjects.Flag;
import de.uni_bremen.factroytrouble.gameobjects.Robot;

public class KeyTest {
    @Parameter
    Robot robby;
    Robot roberta;
    RobotEvent event;
    FieldObject flag;
    FieldObject flag2;

    KeyUnit keyEvent;
    KeyUnit keyRobot;
    KeyUnit keyRobot2;
    KeyUnit keyOther;
    KeyUnit keyFieldObject;
    KeyUnit keyFieldObject2;
    KeyUnit keyEmpty;

    @Before
    public void create() {
        robby = Mockito.mock(Robot.class);
        roberta = Mockito.mock(Robot.class);
        event = Mockito.mock(RobotEvent.class);
        Mockito.when(event.getRobot()).thenReturn(robby);
        Mockito.when(event.getEventType()).thenReturn(RobotEvent.EventType.KILLEDME);
        flag = (FieldObject) Mockito.mock(Flag.class);
        Mockito.when(((Flag) flag).getNumber()).thenReturn(1);
        flag2 = (FieldObject) Mockito.mock(Flag.class);
        Mockito.when(((Flag) flag2).getNumber()).thenReturn(2);

        keyEvent = new KeyUnit(event);
        keyRobot = new KeyUnit(robby);
        keyRobot2 = new KeyUnit(roberta);
        keyOther = new KeyUnit(keyEvent);
        keyFieldObject = new KeyUnit(flag);
        keyFieldObject2 = new KeyUnit(flag2);
        keyEmpty = new KeyUnit(null);
    }

    @Test
    public void testEqualsBothKeysEvents() {
        assertEquals(keyEvent, keyEvent); // event == event
    }

    @Test
    public void testEqualsEventAndNull() {
        assertNotNull(keyEvent); // event != null
    }

    @Test
    public void testEqualsEventAndFlag() {
        assertNotEquals(keyEvent, flag); // event != flag
    }

    @Test
    public void testEqualsEventAndRobot() {
        assertNotEquals(keyEvent, keyRobot); // event != robot
    }

    @Test
    public void testEqualsRobotAndEvent() {
        assertNotEquals(keyRobot, keyEvent); // robot != event
    }

    @Test
    public void testEqualsFieldobjectAndEvent() {
        assertNotEquals(keyFieldObject, keyEvent); // fieldObject != event
    }

    @Test
    public void testEqualsEventAndFieldobject() {
        assertNotEquals(keyEvent, keyFieldObject); // event != fieldObject
    }

    @Test
    public void testEqualsBothKeysFieldobjectsSame() {
        assertEquals(keyFieldObject, keyFieldObject); // fieldObject ==  fieldObject
    }

    @Test
    public void testEqualsEmptykeyAndRobot() {
        assertNotEquals(keyEmpty, keyRobot); // empty != robot
    }

    @Test
    public void testEqualsOtherkeyAndRobot() {
        assertNotEquals(keyOther, keyRobot); // other != robot
    }

    @Test
    public void testEqualsBothKeysFieldobjects() {
        assertNotEquals(keyFieldObject, keyFieldObject2);
    }

    @Test
    public void testEqualsBothKeysRobots() {
        assertNotEquals(keyRobot, keyRobot2); // robot1 != robot2
    }

    @Test
    public void testGetResultType() {
        assertEquals(keyEvent.getResultType(), RobotEvent.class);
        assertEquals(keyRobot.getResultType(), Robot.class);
        assertEquals(keyFieldObject.getResultType(), FieldObject.class);
        assertEquals(keyOther.getResultType(), keyEvent.getClass());
    }
}
