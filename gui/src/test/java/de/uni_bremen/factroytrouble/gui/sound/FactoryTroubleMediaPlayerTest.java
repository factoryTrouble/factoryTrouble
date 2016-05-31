package de.uni_bremen.factroytrouble.gui.sound;

import de.saxsys.javafx.test.JfxRunner;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(JfxRunner.class)
@Ignore("Jenkins nimmt den MP3 Codec nicht, dadaurch laufen dies Test nicht im Jenkins")
public class FactoryTroubleMediaPlayerTest {

    private FactoryTroubleMediaPlayer factoryTroubleMediaPlayer;

    @Before
    public void setUp() {
        factoryTroubleMediaPlayer = new FactoryTroubleMediaPlayer();
        factoryTroubleMediaPlayer.setup();
    }

    @Test
    public void shouldSetUpAllSongsFromResource() {
        assertEquals(Integer.valueOf(4), factoryTroubleMediaPlayer.getMediaCount());
    }

    @Test
    public void shouldStartSongAfterSetUp() throws Exception {
        Thread.sleep(500); // Media Player benötigt Zeit zum Buffern
        assertTrue(factoryTroubleMediaPlayer.isPlaying());
    }

    @Test
    public void shouldBeTheFirstSongAfterSetUp() {
        assertEquals(Integer.valueOf(0), factoryTroubleMediaPlayer.getCurrentMediaIndex());
    }

    @Test
    public void shouldStartTheNextSoundAfterFirstOneIsOver() throws Exception {
        Thread.sleep(1500);
        assertEquals(Integer.valueOf(1), factoryTroubleMediaPlayer.getCurrentMediaIndex());
    }

    @Test
    public void shouldStopTheMedia() throws Exception {
        Thread.sleep(500); // Media Player benötigt Zeit zum Buffern
        factoryTroubleMediaPlayer.stop();
        Thread.sleep(500); // Media Player benötigt Zeit zum Buffern
        assertFalse(factoryTroubleMediaPlayer.isPlaying());
    }

    @Test
    public void shouldStopAndRestartTheMedia() throws Exception {
        Thread.sleep(500); // Media Player benötigt Zeit zum Buffern
        factoryTroubleMediaPlayer.stop();
        factoryTroubleMediaPlayer.play();
        Thread.sleep(500); // Media Player benötigt Zeit zum Buffern
        assertTrue(factoryTroubleMediaPlayer.isPlaying());
    }

    @Test
    public void shouldSelectNext() {
        factoryTroubleMediaPlayer.next();
        assertEquals(Integer.valueOf(1), factoryTroubleMediaPlayer.getCurrentMediaIndex());
    }

    @Test
    public void shouldPlayTheNextSound() throws Exception {
        factoryTroubleMediaPlayer.next();
        Thread.sleep(500); // Media Player benötigt Zeit zum Buffern
        assertTrue(factoryTroubleMediaPlayer.isPlaying());
    }

    @Test
    public void shouldPlayTheNextMediaAfterTheNextMediaIsOver() throws Exception {
        factoryTroubleMediaPlayer.next();
        Thread.sleep(1500); // Media Player benötigt Zeit zum Buffern
        assertEquals(Integer.valueOf(2), factoryTroubleMediaPlayer.getCurrentMediaIndex());
    }

    @Test
    public void shouldShiftTheActualMedia() {
        factoryTroubleMediaPlayer.next();
        factoryTroubleMediaPlayer.next();
        factoryTroubleMediaPlayer.next();
        factoryTroubleMediaPlayer.next();
        assertEquals(Integer.valueOf(0), factoryTroubleMediaPlayer.getCurrentMediaIndex());
    }

}