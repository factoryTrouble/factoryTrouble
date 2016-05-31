/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.editor.container.image;

import de.uni_bremen.factroytrouble.editor.data.ConveyorBeltTileImage;
import de.uni_bremen.factroytrouble.editor.data.Orientation;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Hält alle Bilder der Conyorbelts in jeder Orientierung und Zustand
 */
@Component
public class ConveyorBeltImageContainer {

    private Map<ConveyorBeltTileImage, BufferedImage> tileImages = new HashMap<>();

    /**
     * Initialisiert die Bilder
     */
    @PostConstruct
    public void init() {
        try {
            tileImages.put(new ConveyorBeltTileImage(Orientation.SOUTH, false, Orientation.NORTH), ImageIO.read(getClass().getResourceAsStream("/game/tiles/conveyorBelt/straight/roller_straight_down.png")));
            tileImages.put(new ConveyorBeltTileImage(Orientation.NORTH, false, Orientation.SOUTH), ImageIO.read(getClass().getResourceAsStream("/game/tiles/conveyorBelt/straight/roller_straight_up.png")));
            tileImages.put(new ConveyorBeltTileImage(Orientation.WEST, false, Orientation.EAST), ImageIO.read(getClass().getResourceAsStream("/game/tiles/conveyorBelt/straight/roller_straight_left.png")));
            tileImages.put(new ConveyorBeltTileImage(Orientation.EAST, false, Orientation.WEST), ImageIO.read(getClass().getResourceAsStream("/game/tiles/conveyorBelt/straight/roller_straight_right.png")));

            tileImages.put(new ConveyorBeltTileImage(Orientation.SOUTH, false, Orientation.WEST), ImageIO.read(getClass().getResourceAsStream("/game/tiles/conveyorBelt/rightCurve/roller_cw_down.png")));
            tileImages.put(new ConveyorBeltTileImage(Orientation.NORTH, false, Orientation.EAST), ImageIO.read(getClass().getResourceAsStream("/game/tiles/conveyorBelt/rightCurve/roller_cw_up.png")));
            tileImages.put(new ConveyorBeltTileImage(Orientation.WEST, false, Orientation.NORTH), ImageIO.read(getClass().getResourceAsStream("/game/tiles/conveyorBelt/rightCurve/roller_cw_left.png")));
            tileImages.put(new ConveyorBeltTileImage(Orientation.EAST, false, Orientation.SOUTH), ImageIO.read(getClass().getResourceAsStream("/game/tiles/conveyorBelt/rightCurve/roller_cw_right.png")));

            tileImages.put(new ConveyorBeltTileImage(Orientation.SOUTH, false, Orientation.EAST), ImageIO.read(getClass().getResourceAsStream("/game/tiles/conveyorBelt/leftCurve/roller_ccw_down.png")));
            tileImages.put(new ConveyorBeltTileImage(Orientation.NORTH, false, Orientation.WEST), ImageIO.read(getClass().getResourceAsStream("/game/tiles/conveyorBelt/leftCurve/roller_ccw_up.png")));
            tileImages.put(new ConveyorBeltTileImage(Orientation.WEST, false, Orientation.SOUTH), ImageIO.read(getClass().getResourceAsStream("/game/tiles/conveyorBelt/leftCurve/roller_ccw_left.png")));
            tileImages.put(new ConveyorBeltTileImage(Orientation.EAST, false, Orientation.NORTH), ImageIO.read(getClass().getResourceAsStream("/game/tiles/conveyorBelt/leftCurve/roller_ccw_right.png")));

            tileImages.put(new ConveyorBeltTileImage(Orientation.SOUTH, false, Orientation.EAST, Orientation.NORTH), ImageIO.read(getClass().getResourceAsStream("/game/tiles/conveyorBelt/tToLeft/roller_ccw_straight_down.png")));
            tileImages.put(new ConveyorBeltTileImage(Orientation.NORTH, false, Orientation.WEST, Orientation.SOUTH), ImageIO.read(getClass().getResourceAsStream("/game/tiles/conveyorBelt/tToLeft/roller_ccw_straight_up.png")));
            tileImages.put(new ConveyorBeltTileImage(Orientation.WEST, false, Orientation.SOUTH, Orientation.EAST), ImageIO.read(getClass().getResourceAsStream("/game/tiles/conveyorBelt/tToLeft/roller_ccw_straight_left.png")));
            tileImages.put(new ConveyorBeltTileImage(Orientation.EAST, false, Orientation.NORTH, Orientation.WEST), ImageIO.read(getClass().getResourceAsStream("/game/tiles/conveyorBelt/tToLeft/roller_ccw_straight_right.png")));

            tileImages.put(new ConveyorBeltTileImage(Orientation.SOUTH, false, Orientation.WEST, Orientation.NORTH), ImageIO.read(getClass().getResourceAsStream("/game/tiles/conveyorBelt/tToRight/roller_cw_straight_down.png")));
            tileImages.put(new ConveyorBeltTileImage(Orientation.NORTH, false, Orientation.EAST, Orientation.SOUTH), ImageIO.read(getClass().getResourceAsStream("/game/tiles/conveyorBelt/tToRight/roller_cw_straight_up.png")));
            tileImages.put(new ConveyorBeltTileImage(Orientation.WEST, false, Orientation.NORTH, Orientation.EAST), ImageIO.read(getClass().getResourceAsStream("/game/tiles/conveyorBelt/tToRight/roller_cw_straight_left.png")));
            tileImages.put(new ConveyorBeltTileImage(Orientation.EAST, false, Orientation.SOUTH, Orientation.WEST), ImageIO.read(getClass().getResourceAsStream("/game/tiles/conveyorBelt/tToRight/roller_cw_straight_right.png")));

            tileImages.put(new ConveyorBeltTileImage(Orientation.SOUTH, false, Orientation.WEST, Orientation.EAST), ImageIO.read(getClass().getResourceAsStream("/game/tiles/conveyorBelt/tToMiddle/roller_ccw_cw_down.png")));
            tileImages.put(new ConveyorBeltTileImage(Orientation.NORTH, false, Orientation.WEST, Orientation.EAST), ImageIO.read(getClass().getResourceAsStream("/game/tiles/conveyorBelt/tToMiddle/roller_ccw_cw_up.png")));
            tileImages.put(new ConveyorBeltTileImage(Orientation.WEST, false, Orientation.SOUTH, Orientation.NORTH), ImageIO.read(getClass().getResourceAsStream("/game/tiles/conveyorBelt/tToMiddle/roller_ccw_cw_left.png")));
            tileImages.put(new ConveyorBeltTileImage(Orientation.EAST, false, Orientation.SOUTH, Orientation.NORTH), ImageIO.read(getClass().getResourceAsStream("/game/tiles/conveyorBelt/tToMiddle/roller_ccw_cw_right.png")));


            tileImages.put(new ConveyorBeltTileImage(Orientation.SOUTH, true, Orientation.NORTH), ImageIO.read(getClass().getResourceAsStream("/game/tiles/expressConveyorBelt/straight/roller_express_straight_down.png")));
            tileImages.put(new ConveyorBeltTileImage(Orientation.NORTH, true, Orientation.SOUTH), ImageIO.read(getClass().getResourceAsStream("/game/tiles/expressConveyorBelt/straight/roller_express_straight_up.png")));
            tileImages.put(new ConveyorBeltTileImage(Orientation.WEST, true, Orientation.EAST), ImageIO.read(getClass().getResourceAsStream("/game/tiles/expressConveyorBelt/straight/roller_express_straight_left.png")));
            tileImages.put(new ConveyorBeltTileImage(Orientation.EAST, true, Orientation.WEST), ImageIO.read(getClass().getResourceAsStream("/game/tiles/expressConveyorBelt/straight/roller_express_straight_right.png")));

            tileImages.put(new ConveyorBeltTileImage(Orientation.SOUTH, true, Orientation.WEST), ImageIO.read(getClass().getResourceAsStream("/game/tiles/expressConveyorBelt/rightCurve/roller_express_cw_down.png")));
            tileImages.put(new ConveyorBeltTileImage(Orientation.NORTH, true, Orientation.EAST), ImageIO.read(getClass().getResourceAsStream("/game/tiles/expressConveyorBelt/rightCurve/roller_express_cw_up.png")));
            tileImages.put(new ConveyorBeltTileImage(Orientation.WEST, true, Orientation.NORTH), ImageIO.read(getClass().getResourceAsStream("/game/tiles/expressConveyorBelt/rightCurve/roller_express_cw_left.png")));
            tileImages.put(new ConveyorBeltTileImage(Orientation.EAST, true, Orientation.SOUTH), ImageIO.read(getClass().getResourceAsStream("/game/tiles/expressConveyorBelt/rightCurve/roller_express_cw_right.png")));

            tileImages.put(new ConveyorBeltTileImage(Orientation.SOUTH, true, Orientation.EAST), ImageIO.read(getClass().getResourceAsStream("/game/tiles/expressConveyorBelt/leftCurve/roller_express_ccw_down.png")));
            tileImages.put(new ConveyorBeltTileImage(Orientation.NORTH, true, Orientation.WEST), ImageIO.read(getClass().getResourceAsStream("/game/tiles/expressConveyorBelt/leftCurve/roller_express_ccw_up.png")));
            tileImages.put(new ConveyorBeltTileImage(Orientation.WEST, true, Orientation.SOUTH), ImageIO.read(getClass().getResourceAsStream("/game/tiles/expressConveyorBelt/leftCurve/roller_express_ccw_left.png")));
            tileImages.put(new ConveyorBeltTileImage(Orientation.EAST, true, Orientation.NORTH), ImageIO.read(getClass().getResourceAsStream("/game/tiles/expressConveyorBelt/leftCurve/roller_express_ccw_right.png")));

            tileImages.put(new ConveyorBeltTileImage(Orientation.SOUTH, true, Orientation.EAST, Orientation.NORTH), ImageIO.read(getClass().getResourceAsStream("/game/tiles/expressConveyorBelt/tToLeft/roller_express_ccw_straight_down.png")));
            tileImages.put(new ConveyorBeltTileImage(Orientation.NORTH, true, Orientation.WEST, Orientation.SOUTH), ImageIO.read(getClass().getResourceAsStream("/game/tiles/expressConveyorBelt/tToLeft/roller_express_ccw_straight_up.png")));
            tileImages.put(new ConveyorBeltTileImage(Orientation.WEST, true, Orientation.SOUTH, Orientation.EAST), ImageIO.read(getClass().getResourceAsStream("/game/tiles/expressConveyorBelt/tToLeft/roller_express_ccw_straight_left.png")));
            tileImages.put(new ConveyorBeltTileImage(Orientation.EAST, true, Orientation.NORTH, Orientation.WEST), ImageIO.read(getClass().getResourceAsStream("/game/tiles/expressConveyorBelt/tToLeft/roller_express_ccw_straight_right.png")));

            tileImages.put(new ConveyorBeltTileImage(Orientation.SOUTH, true, Orientation.WEST, Orientation.NORTH), ImageIO.read(getClass().getResourceAsStream("/game/tiles/expressConveyorBelt/tToRight/roller_express_cw_straight_down.png")));
            tileImages.put(new ConveyorBeltTileImage(Orientation.NORTH, true, Orientation.EAST, Orientation.SOUTH), ImageIO.read(getClass().getResourceAsStream("/game/tiles/expressConveyorBelt/tToRight/roller_express_cw_straight_up.png")));
            tileImages.put(new ConveyorBeltTileImage(Orientation.WEST, true, Orientation.NORTH, Orientation.EAST), ImageIO.read(getClass().getResourceAsStream("/game/tiles/expressConveyorBelt/tToRight/roller_express_cw_straight_left.png")));
            tileImages.put(new ConveyorBeltTileImage(Orientation.EAST, true, Orientation.SOUTH, Orientation.WEST), ImageIO.read(getClass().getResourceAsStream("/game/tiles/expressConveyorBelt/tToRight/roller_express_cw_straight_right.png")));

            tileImages.put(new ConveyorBeltTileImage(Orientation.SOUTH, true, Orientation.WEST, Orientation.EAST), ImageIO.read(getClass().getResourceAsStream("/game/tiles/expressConveyorBelt/tToMiddle/roller_express_ccw_cw_down.png")));
            tileImages.put(new ConveyorBeltTileImage(Orientation.NORTH, true, Orientation.WEST, Orientation.EAST), ImageIO.read(getClass().getResourceAsStream("/game/tiles/expressConveyorBelt/tToMiddle/roller_express_ccw_cw_up.png")));
            tileImages.put(new ConveyorBeltTileImage(Orientation.WEST, true, Orientation.SOUTH, Orientation.NORTH), ImageIO.read(getClass().getResourceAsStream("/game/tiles/expressConveyorBelt/tToMiddle/roller_express_ccw_cw_left.png")));
            tileImages.put(new ConveyorBeltTileImage(Orientation.EAST, true, Orientation.SOUTH, Orientation.NORTH), ImageIO.read(getClass().getResourceAsStream("/game/tiles/expressConveyorBelt/tToMiddle/roller_express_ccw_cw_right.png")));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gibt ein Bild des ConveyorBelts zurück oder null, falls dieses nict vorhanden ist
     *
     * @param conveyorBeltTileStatus
     *      Beschreibung des Convorbelts
     * @return
     *      Das Bild zur Beschreibung
     */
    public BufferedImage getConyorbeltImage(ConveyorBeltTileImage conveyorBeltTileStatus) {
        return tileImages.get(conveyorBeltTileStatus);
    }

}
