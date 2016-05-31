package de.uni_bremen.factroytrouble.ai.ki1.behavior;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;


import de.uni_bremen.factroytrouble.ai.ais.AIPlayer1;
import de.uni_bremen.factroytrouble.ai.ki1.memory.LTMUnit;
import de.uni_bremen.factroytrouble.ai.ki1.memory.MemoryUnit;
import de.uni_bremen.factroytrouble.ai.ki1.memory.ReachedFlagEvent;
import de.uni_bremen.factroytrouble.ai.ki1.memory.RobotEvent;
import de.uni_bremen.factroytrouble.api.ki1.memory.MemoryEvent;
import de.uni_bremen.factroytrouble.gameobjects.GameFlag;
import de.uni_bremen.factroytrouble.gameobjects.Robot;

@Ignore("Original Bretter entfernt. Dadurch kommt es zu Problemen")
public class DynamicBehaviourTest {
    DynamicBehaviour dynaBehave;
    DynamicBehaviour dynaBehave2;
    @Mock
    MemoryUnit memory;
    @Mock
    LTMUnit ltm;
    
    GameFlag flag1;
    GameFlag flag2;
    GameFlag flag3;
    
    Robot robo;
    
    List<MemoryEvent> mem;
    @Before
    public void setup(){
    AIPlayer1 ai1 = Mockito.mock(AIPlayer1.class);
    memory = Mockito.mock(MemoryUnit.class);
    dynaBehave = Mockito.spy(new DynamicBehaviour("funkey",ai1));
    dynaBehave.setMemoryUnit(memory);
    dynaBehave2 = new DynamicBehaviour("squintbot",ai1);
    ltm = Mockito.mock(LTMUnit.class);
    flag1 = new GameFlag(1);
    flag2 = new GameFlag(2);
    flag3 = new GameFlag(3);
    mem = new ArrayList<MemoryEvent>();
    }
    
    @Test
    public void StaticConfigTest(){
        assertTrue(dynaBehave.getAttitude()==Attitude.VERYDEFENSIVE);
        assertTrue(dynaBehave.getMood()==Mood.VERYSAD);
        assertTrue(dynaBehave.getRiskReadiness()==RiskReadiness.AVERAGE);
    }
    
    @Test
    public void LockedMoodTest(){
        dynaBehave.setHappiness(10);
        dynaBehave.changeMood();
        assertTrue(dynaBehave.getMood()==Mood.VERYSAD);
    }
    
    @Test
    public void LockedRiskReadinessTest(){
        dynaBehave2.setSchadenfreude(10);
        dynaBehave2.changeRiskReadiness();
        assertTrue(dynaBehave2.getRiskReadiness()==RiskReadiness.VERYRISKY);
    }
    
    @Test
    public void CalcEmotionReachedFlagTest(){
        int currentHappiness = dynaBehave.getHappiness();
        ReachedFlagEvent rfe = new ReachedFlagEvent(flag1, 10);
        mem.add(rfe);
        Mockito.doReturn(mem).when(dynaBehave).getLatestEvents();
        dynaBehave.calcEmotions();
        assertTrue(dynaBehave.getHappiness()==currentHappiness+DynamicBehaviour.reachedFlagBonus);
    }
    
    @Test
    public void CalcEmotionShotMeActiveTest(){
        int currentHappiness = dynaBehave.getHappiness();
        int currentSchadenfreude = dynaBehave.getSchadenfreude();
        RobotEvent robshotmea = new RobotEvent(robo, RobotEvent.EventType.SHOTME, true, 10);
        mem.add(robshotmea);
        Mockito.doReturn(mem).when(dynaBehave).getLatestEvents();
        dynaBehave.calcEmotions();
        assertTrue(dynaBehave.getHappiness()==currentHappiness+DynamicBehaviour.shotMeBonus);
        assertTrue(dynaBehave.getSchadenfreude()==currentSchadenfreude+DynamicBehaviour.shotMeBonus);
    }
    
    @Test
    public void CalcEmotionShotMePassiveTest(){
        int currentHappiness = dynaBehave.getHappiness();
        int currentAggroDeffi = dynaBehave.getAggroDeffi();
        RobotEvent robshotmea = new RobotEvent(robo, RobotEvent.EventType.SHOTME, false, 10);
        mem.add(robshotmea);
        Mockito.doReturn(mem).when(dynaBehave).getLatestEvents();
        dynaBehave.calcEmotions();
        assertTrue(dynaBehave.getHappiness()==currentHappiness-DynamicBehaviour.shotMeBonus);
        assertTrue(dynaBehave.getAggroDeffi()==currentAggroDeffi-DynamicBehaviour.shotMeBonus);
    }
    
    @Test
    public void CalcEmotionShovedMeActiveTest(){
        int currentHappiness = dynaBehave.getHappiness();
        int currentSchadenfreude = dynaBehave.getSchadenfreude();
        RobotEvent robshotmea = new RobotEvent(robo, RobotEvent.EventType.SHOVEDME, true, 10);
        mem.add(robshotmea);
        Mockito.doReturn(mem).when(dynaBehave).getLatestEvents();
        dynaBehave.calcEmotions();
        assertTrue(dynaBehave.getHappiness()==currentHappiness+DynamicBehaviour.shovedMeBonus);
        assertTrue(dynaBehave.getSchadenfreude()==currentSchadenfreude+DynamicBehaviour.shovedMeBonus);
    }
    
    @Test
    public void CalcEmotionShovedMePassiveTest(){
        int currentHappiness = dynaBehave.getHappiness();
        int currentAggroDeffi = dynaBehave.getAggroDeffi();
        RobotEvent robshotmea = new RobotEvent(robo, RobotEvent.EventType.SHOVEDME, false, 10);
        mem.add(robshotmea);
        Mockito.doReturn(mem).when(dynaBehave).getLatestEvents();
        dynaBehave.calcEmotions();
        assertTrue(dynaBehave.getHappiness()==currentHappiness-DynamicBehaviour.shovedMeBonus);
        assertTrue(dynaBehave.getAggroDeffi()==currentAggroDeffi-DynamicBehaviour.shovedMeBonus);
    }
    
    @Test
    public void CalcEmotionKilledMeActiveTest(){
        int currentHappiness = dynaBehave.getHappiness();
        int currentSchadenfreude = dynaBehave.getSchadenfreude();
        RobotEvent robshotmea = new RobotEvent(robo, RobotEvent.EventType.KILLEDME, true, 10);
        mem.add(robshotmea);
        Mockito.doReturn(mem).when(dynaBehave).getLatestEvents();
        dynaBehave.calcEmotions();
        assertTrue(dynaBehave.getHappiness()==currentHappiness+DynamicBehaviour.killedMeBonus);
        assertTrue(dynaBehave.getSchadenfreude()==currentSchadenfreude+DynamicBehaviour.killedMeBonus);
    }
    
    @Test
    public void CalcEmotionKilledMePassiveTest(){
        int currentHappiness = dynaBehave.getHappiness();
        int currentAggroDeffi = dynaBehave.getAggroDeffi();
        RobotEvent robshotmea = new RobotEvent(robo, RobotEvent.EventType.KILLEDME, false, 10);
        mem.add(robshotmea);
        Mockito.doReturn(mem).when(dynaBehave).getLatestEvents();
        dynaBehave.calcEmotions();
        assertTrue(dynaBehave.getHappiness()==currentHappiness-DynamicBehaviour.killedMeBonus);
        assertTrue(dynaBehave.getAggroDeffi()==currentAggroDeffi-DynamicBehaviour.killedMeBonus);
    }
}
