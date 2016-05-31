/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gui.generator.board.tile;

import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.gameobjects.Wall;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Gibt einen Pusher für eine Wand zurück
 */
@Component
public class WallPusherDispatcher {

    private BufferedImage pusherEvenUp;
    private BufferedImage pusherEvenDown;
    private BufferedImage pusherEvenLeft;
    private BufferedImage pusherEvenRight;

    private BufferedImage pusherOddUp;
    private BufferedImage pusherOddDown;
    private BufferedImage pusherOddLeft;
    private BufferedImage pusherOddRight;

    /**
     * Initialisiert die Pusher Bilder
     */
    @PostConstruct
    public void init() {
        try {
            pusherEvenUp = ImageIO.read(getClass().getResourceAsStream("/game/tiles/pusher/pusher-even_up.png"));
            pusherEvenDown = ImageIO.read(getClass().getResourceAsStream("/game/tiles/pusher/pusher-even_down.png"));
            pusherEvenLeft = ImageIO.read(getClass().getResourceAsStream("/game/tiles/pusher/pusher-even_left.png"));
            pusherEvenRight = ImageIO.read(getClass().getResourceAsStream("/game/tiles/pusher/pusher-even_right.png"));

            pusherOddUp = ImageIO.read(getClass().getResourceAsStream("/game/tiles/pusher/pusher-odd_up.png"));
            pusherOddDown = ImageIO.read(getClass().getResourceAsStream("/game/tiles/pusher/pusher-odd_down.png"));
            pusherOddLeft = ImageIO.read(getClass().getResourceAsStream("/game/tiles/pusher/pusher-odd_left.png"));
            pusherOddRight = ImageIO.read(getClass().getResourceAsStream("/game/tiles/pusher/pusher-odd_right.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gibt das Bild für einen Pusher zurück, welcher an der gegebenden Wand befindet
     * @param wall
     *      Die Wand, an der sich der Pusher befinden soll/kann
     * @return
     *      Das Bild für diesen Pusher oder null, falls die Wand keinen Pusher besitzt
     */
    public BufferedImage dispatchPusher(Wall wall) {
        if(wall.getPusherPhases().length == 0) {
            return null;
        }
        if(wall.getOrientation().equals(Orientation.NORTH)) {
            if(wall.getPusherPhases().length == 2) {
                return pusherEvenUp;
            }
            if(wall.getPusherPhases().length == 3) {
                return pusherOddUp;
            }
        }
        if(wall.getOrientation().equals(Orientation.SOUTH)) {
            if(wall.getPusherPhases().length == 2) {
                return pusherEvenDown;
            }
            if(wall.getPusherPhases().length == 3) {
                return pusherOddDown;
            }
        }
        if(wall.getOrientation().equals(Orientation.WEST)) {
            if(wall.getPusherPhases().length == 2) {
                return pusherEvenLeft;
            }
            if(wall.getPusherPhases().length == 3) {
                return pusherOddLeft;
            }
        }
        if(wall.getPusherPhases().length == 2) {
            return pusherEvenRight;
        }
        if(wall.getPusherPhases().length == 3) {
            return pusherOddRight;
        }
        return null;
    }

}
