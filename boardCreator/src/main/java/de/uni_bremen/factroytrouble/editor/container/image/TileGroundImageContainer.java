/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.editor.container.image;

import de.uni_bremen.factroytrouble.editor.data.GroundFill;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Hält die Bilder für Bodenflächen ausser Förderbänder
 *
 * @author Andre
 *
 */
@Component
public class TileGroundImageContainer {

    private Map<GroundFill, BufferedImage> groundTiles = new HashMap<>();

    /**
     * Lädt die Bilder in den Container
     */
    @PostConstruct
    public void init() {
        try {
            groundTiles.put(GroundFill.EMPTY, ImageIO.read(getClass().getResourceAsStream("/game/tiles/empty.png")));
            groundTiles.put(GroundFill.GEAR_CW, ImageIO.read(getClass().getResourceAsStream("/game/tiles/gear-cw.png")));
            groundTiles.put(GroundFill.GEAR_CCW, ImageIO.read(getClass().getResourceAsStream("/game/tiles/gear-ccw.png")));
            groundTiles.put(GroundFill.REPAIR, ImageIO.read(getClass().getResourceAsStream("/game/tiles/repair.png")));
            groundTiles.put(GroundFill.FLAG, ImageIO.read(getClass().getResourceAsStream("/game/tiles/flag.png")));
            groundTiles.put(GroundFill.HOLE, ImageIO.read(getClass().getResourceAsStream("/game/tiles/void.png")));
            groundTiles.put(GroundFill.START, ImageIO.read(getClass().getResourceAsStream("/game/tiles/start.png")));
            groundTiles.put(GroundFill.FLAG, ImageIO.read(getClass().getResourceAsStream("/game/tiles/flag.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gibt das Bild anhand der Bodenfüllung zurück
     * @param groundFill
     *      Art der Bodenfüllung
     * @return
     *      Das Bild zu der Bodenfüllung
     */
    public BufferedImage getImageForGround(GroundFill groundFill) {
        if(GroundFill.CONVEYOR_BELT.equals(groundFill) || GroundFill.EXPRESS_CONVEYOR_BELT.equals(groundFill)) {
            throw new ConveyorBeltIsToSpecialException();
        }
        return groundTiles.get(groundFill);
    }

    class ConveyorBeltIsToSpecialException extends RuntimeException {
        private ConveyorBeltIsToSpecialException() {
            super("A conveyor belt is to special to just get on single image. Use ConveyorBeltImageContainer instead");
        }
    }

}
