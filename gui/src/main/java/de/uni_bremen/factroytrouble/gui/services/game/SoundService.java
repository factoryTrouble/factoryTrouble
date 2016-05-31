/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gui.services.game;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import de.uni_bremen.factroytrouble.gui.controller.components.RobotImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

@Service
public class SoundService {
    private static final Logger LOGGER = Logger.getLogger(RobotImageView.class);
    
    private boolean activated = true;

    public void playSound(String sound) {
        if(!activated){
            return;
        }
        Media hit = new Media(getClass().getResource("/sounds/boardsounds/" + sound).toString());
        try {
            MediaPlayer mediaPlayer = new MediaPlayer(hit);
            mediaPlayer.setVolume(30);
            mediaPlayer.setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {
                    mediaPlayer.dispose();
                }
            });
            mediaPlayer.play();
        } catch (NullPointerException e) {
            LOGGER.error("Failed to load audio", e);
            return;
        }

    }

    public boolean getActivated(){
        return activated;
    }
    
    public void setActivated(boolean activated){
        this.activated = activated;
    }
    
}
