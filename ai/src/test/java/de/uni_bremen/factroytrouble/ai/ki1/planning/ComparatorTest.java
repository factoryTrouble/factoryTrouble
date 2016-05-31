package de.uni_bremen.factroytrouble.ai.ki1.planning;

import static org.junit.Assert.*;

import org.junit.Test;

public class ComparatorTest {
    GoalFlippedOrderComparator comp=new GoalFlippedOrderComparator();
   
    @Test
    public void firstGoalHigherValue() {
        Goal goal1=new GoalTile(10, null);
        Goal goal2=new GoalTile(5,null);
        assertEquals(-1,comp.compare(goal1, goal2));
    }
    
    @Test
    public void firstGoalLowerValue() {
        Goal goal1=new GoalTile(10, null);
        Goal goal2=new GoalTile(25,null);
        assertEquals(1,comp.compare(goal1, goal2));
    }
    
    @Test
    public void firstGoalSameValue() {
        Goal goal1=new GoalTile(5, null);
        Goal goal2=new GoalTile(5,null);
        assertEquals(0,comp.compare(goal1, goal2));
    }
    @Test
    public void firstGoalNull() {
        Goal goal2=new GoalTile(5,null);
        assertEquals(0,comp.compare(null, goal2));
    }

}
