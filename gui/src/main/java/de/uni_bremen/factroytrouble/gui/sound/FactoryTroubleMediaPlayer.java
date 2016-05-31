/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gui.sound;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;

@Service
public class FactoryTroubleMediaPlayer implements MediaPlayerControls {
    private static final Logger LOGGER = Logger.getLogger(FactoryTroubleMediaPlayer.class);
    private static final String SONGS_CLASSPATH_PATTERN = "classpath*:/sounds/background/**";

    private MediaPlayer mediaPlayer;
    private List<String> songs;
    private int activeSong = 0;
    private double volume = 1.0;

    @Override
    public void setup() {
        getAllSongs();
        playActiveSound();
        stop();
    }

    @Override
    public void next() {
        mediaPlayer.stop();
        mediaPlayer.dispose();
        if (activeSong == songs.size() - 1) {
            activeSong = 0;
        } else {
            activeSong++;
        }
        LOGGER.info(songs.get(activeSong));
        playActiveSound();
    }

    @Override
    public void setVolume(double volume) {
        mediaPlayer.setVolume(volume);
        this.volume = volume;
    }

    @Override
    public double getVolume() {
        return volume;
    }

    @Override
    public Integer getMediaCount() {
        return songs.size();
    }

    @Override
    public Integer getCurrentMediaIndex() {
        return activeSong;
    }

    @Override
    public boolean isPlaying() {
        return Status.PLAYING.equals(mediaPlayer.getStatus());
    }

    @Override
    public void play() {
        mediaPlayer.play();

    }

    @Override
    public void stop() {
        mediaPlayer.stop();
    }

    private void playActiveSound() {
        Media hit = new Media(getClass().getResource("/sounds/background/" + songs.get(activeSong)).toString());
        mediaPlayer = new MediaPlayer(hit);
        mediaPlayer.setVolume(volume);
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                next();
            }
        });
        mediaPlayer.play();
    }

    private void getAllSongs() {
        songs = new ArrayList<>();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = null;
        try {
            resources = resolver.getResources(SONGS_CLASSPATH_PATTERN);
        } catch (IOException e) {
            LOGGER.error("Unable to get sounds form classpath", e);
        }
        for (int i = 0; i < resources.length; i++) {
            LOGGER.info(resources[i].getFilename());
            if (resources[i].getFilename().contains(".mp3")) {
                songs.add(resources[i].getFilename());
            }
        }
    }
}