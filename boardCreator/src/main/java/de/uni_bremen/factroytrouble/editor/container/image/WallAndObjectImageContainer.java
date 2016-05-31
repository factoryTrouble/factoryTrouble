/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.editor.container.image;

import de.uni_bremen.factroytrouble.editor.data.Orientation;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Hält die Bilder für Wände und mit den Wänden verbundenen Objekten
 *
 * @author Andre
 *
 */
@Component
public class WallAndObjectImageContainer {

   private Map<Orientation, BufferedImage> wallMap = new HashMap<>();
   private Map<Orientation, BufferedImage> pusherOddMap = new HashMap<>();
   private Map<Orientation, BufferedImage> pusherEvenMap = new HashMap<>();
   private Map<Integer, BufferedImage> laserHorizontalMap = new HashMap<>();
   private Map<Integer, BufferedImage> laserVerticalMap = new HashMap<>();

    /**
     * Lädt die Bilder initali in den Container
     */
    @PostConstruct
    public void init() {
        try {
            wallMap.put(Orientation.SOUTH, ImageIO.read(getClass().getResourceAsStream("/game/tiles/wall/wall_down.png")));
            wallMap.put(Orientation.NORTH, ImageIO.read(getClass().getResourceAsStream("/game/tiles/wall/wall_up.png")));
            wallMap.put(Orientation.WEST, ImageIO.read(getClass().getResourceAsStream("/game/tiles/wall/wall_left.png")));
            wallMap.put(Orientation.EAST, ImageIO.read(getClass().getResourceAsStream("/game/tiles/wall/wall_right.png")));

            pusherOddMap.put(Orientation.SOUTH, ImageIO.read(getClass().getResourceAsStream("/game/tiles/pusher/pusher-odd_down.png")));
            pusherOddMap.put(Orientation.NORTH, ImageIO.read(getClass().getResourceAsStream("/game/tiles/pusher/pusher-odd_up.png")));
            pusherOddMap.put(Orientation.WEST, ImageIO.read(getClass().getResourceAsStream("/game/tiles/pusher/pusher-odd_left.png")));
            pusherOddMap.put(Orientation.EAST, ImageIO.read(getClass().getResourceAsStream("/game/tiles/pusher/pusher-odd_right.png")));

            pusherEvenMap.put(Orientation.SOUTH, ImageIO.read(getClass().getResourceAsStream("/game/tiles/pusher/pusher-even_down.png")));
            pusherEvenMap.put(Orientation.NORTH, ImageIO.read(getClass().getResourceAsStream("/game/tiles/pusher/pusher-even_up.png")));
            pusherEvenMap.put(Orientation.WEST, ImageIO.read(getClass().getResourceAsStream("/game/tiles/pusher/pusher-even_left.png")));
            pusherEvenMap.put(Orientation.EAST, ImageIO.read(getClass().getResourceAsStream("/game/tiles/pusher/pusher-even_right.png")));

            laserHorizontalMap.put(1, ImageIO.read(getClass().getResourceAsStream("/game/tiles/laser/laserHorizontal.png")));
            laserHorizontalMap.put(2, ImageIO.read(getClass().getResourceAsStream("/game/tiles/laser/doublelaserHorizontal.png")));
            laserHorizontalMap.put(3, ImageIO.read(getClass().getResourceAsStream("/game/tiles/laser/triplelaserHorizontal.png")));

            laserVerticalMap.put(1, ImageIO.read(getClass().getResourceAsStream("/game/tiles/laser/laser.png")));
            laserVerticalMap.put(2, ImageIO.read(getClass().getResourceAsStream("/game/tiles/laser/doublelaser.png")));
            laserVerticalMap.put(3, ImageIO.read(getClass().getResourceAsStream("/game/tiles/laser/triplelaser.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gibt eine Wand in gegebener Richtung zurück
     *
     * @param orientation
     *      Die Richtung der Wand
     * @return
     *      Bild der Wand in gegebener Richtung
     */
    public BufferedImage getWall(Orientation orientation) {
        return wallMap.get(orientation);
    }

    /**
     * Gibt das Bild eines Pushers zurück
     * @param orientation
     *      Richtung des Pushers
     * @param isOdd
     *      Ist der Pusher in den ungeraden Runden aktiv
     * @return
     *      Bild des Pushers
     */
    public BufferedImage getPusher(Orientation orientation, boolean isOdd) {
        if(isOdd) {
            return pusherOddMap.get(orientation);
        }
        return pusherEvenMap.get(orientation);
    }

    /**
     * Gibt das Bild eines Lases zurück
     * @param count
     *      Anzahl der Laserstrahlen (1-3 sind aktuell hinterlegt)
     * @param isHorizontal
     *      Ist der Laser horizontal
     * @return
     *      Bild des Lasers
     */
    public BufferedImage getLaser(Integer count, boolean isHorizontal) {
        if(isHorizontal) {
            return laserHorizontalMap.get(count);
        }
        return laserVerticalMap.get(count);
    }

}
