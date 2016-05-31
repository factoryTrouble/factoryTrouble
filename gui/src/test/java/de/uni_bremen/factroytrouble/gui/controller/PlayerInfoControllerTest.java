package de.uni_bremen.factroytrouble.gui.controller;

import de.saxsys.javafx.test.JfxRunner;
import de.uni_bremen.factroytrouble.gui.TestUtil;
import de.uni_bremen.factroytrouble.gui.services.GameEngineWrapper;
import de.uni_bremen.factroytrouble.spring.SpringConfigHolder;
import javafx.scene.image.ImageView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by johannes.gesenhues on 22.01.16.
 */
@RunWith(JfxRunner.class)
public class PlayerInfoControllerTest {
    @Mock private GameEngineWrapper gameEngineWrapper;
    @Mock private GameFieldController gameFieldController;
    @Mock private ApplicationContext applicationContext;
    @Mock private List<ImageView> flagViews;
    PlayerInfoController playerInfoController;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        SpringConfigHolder.getInstance().setContext(applicationContext);
        when(applicationContext.getBean(GameEngineWrapper.class)).thenReturn(gameEngineWrapper);
        when(applicationContext.getBean(GameFieldController.class)).thenReturn(gameFieldController);
        when(gameEngineWrapper.getFlagCount(any())).thenReturn(2);
        when(flagViews.get(anyInt())).thenReturn(new ImageView());
        playerInfoController = new PlayerInfoController("TestPlayerName");
    }


    @Test
    public void shouldCallgameEngineWrapperGetPlayerNumberByNameInInitRobot() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        TestUtil.callPrivateMethodeWithoutParameter("initRobot", playerInfoController);
        verify(gameEngineWrapper).getPlayerNumberByName(anyString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotSetHpWhenLessThanZero(){
        playerInfoController.setHp(-1);
    }

    @Test
    public void shouldSetFlagTouched() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException{
        TestUtil.callPrivateMethodeWithoutParameter("initFlags", playerInfoController);
        playerInfoController.hitFlag(2);
        assertEquals(flagViews.get(0).getOpacity(), 1, 0.1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldNotSetHpWhenGreaterThanTen(){
        playerInfoController.setHp(11);
    }

    @Test
    public void shouldSetTheHPWhenEqualsZero() throws Exception {
        playerInfoController.setHp(0);
        ImageView[] health = (ImageView[]) TestUtil.getPrivateField("health", playerInfoController);
        assertEquals(0.3, health[0].getOpacity(), 0.01);
    }

    @Test
    public void shouldSetTheHPWhenEqualsTen() throws Exception {
        playerInfoController.setHp(10);
        ImageView[] health = (ImageView[]) TestUtil.getPrivateField("health", playerInfoController);
        assertEquals(1, health[9].getOpacity(), 0.01);
    }


    @Test
    public void shouldSetTheHPWhenEqualsFive() throws Exception {
        playerInfoController.setHp(5);
        ImageView[] health = (ImageView[]) TestUtil.getPrivateField("health", playerInfoController);
        assertEquals(1, health[4].getOpacity(), 0.01);
        assertEquals(0.3, health[5].getOpacity(), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotSetLpWhenLessThanZero(){
        playerInfoController.setLp(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotSetLpWhenGreaterThanFour(){
        playerInfoController.setLp(4);
    }

    @Test
    public void shouldSetTheLPWhenEqualsZero() throws Exception {
        playerInfoController.setLp(0);
        ImageView[] health = (ImageView[]) TestUtil.getPrivateField("lives", playerInfoController);
        assertEquals(0.3, health[0].getOpacity(), 0.01);
    }

    @Test
    public void shouldSetTheHPWhenEqualsThree() throws Exception {
        playerInfoController.setLp(3);
        ImageView[] health = (ImageView[]) TestUtil.getPrivateField("lives", playerInfoController);
        assertEquals(1, health[2].getOpacity(), 0.01);
    }

    @Test
    public void shouldSetPlayerActive() {
        playerInfoController.setActive(true);
        assertEquals("-fx-background-color: #00ff19;", playerInfoController.getStyle());
    }

    @Test
    public void shouldSetPlayerNotActive() {
        playerInfoController.setActive(false);
        assertEquals("-fx-background-color: #ffffff;", playerInfoController.getStyle());
    }

    @Test
    public void shouldSetPlayerFinish() {
        playerInfoController.setFinished(true);
        assertEquals("-fx-background-color: #ff898e;", playerInfoController.getStyle());
    }

    @Test
    public void shouldSetPlayerNotFinish() {
        playerInfoController.setFinished(false);
        assertEquals("-fx-background-color: #ffffff;", playerInfoController.getStyle());
    }

}