/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.editor.factory;

import de.uni_bremen.factroytrouble.editor.component.Tile;
import de.uni_bremen.factroytrouble.editor.data.GroundFill;
import javafx.embed.swing.SwingFXUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Erzeugt ein neues Tile
 *
 * @author Andre
 */
@Component
public class TileFactory {

    private Map<GroundFill, BufferedImage> preDefinedTiles;

    /**
     * Inialisiert wichtige Bilder
     */
    @PostConstruct
    public void init() {
        preDefinedTiles = new HashMap<>();
        try {
            preDefinedTiles.put(GroundFill.EMPTY, ImageIO.read(getClass().getResourceAsStream("/game/tiles/empty.png")));
            preDefinedTiles.put(GroundFill.GEAR_CCW, ImageIO.read(getClass().getResourceAsStream("/game/tiles/gear-ccw.png")));
            preDefinedTiles.put(GroundFill.GEAR_CW, ImageIO.read(getClass().getResourceAsStream("/game/tiles/gear-cw.png")));
            preDefinedTiles.put(GroundFill.REPAIR, ImageIO.read(getClass().getResourceAsStream("/game/tiles/repair.png")));
            preDefinedTiles.put(GroundFill.HOLE, ImageIO.read(getClass().getResourceAsStream("/game/tiles/void.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Erstellt ein neues Tile für gegebene Füllung
     * @param groundFill
     *      Art des Bodenes
     * @return
     *      Tile für den gegbenen Boden
     */
    public Tile getTile(GroundFill groundFill) {
        Tile tile = null;
        if(preDefinedTiles.containsKey(groundFill)) {
            tile = new Tile(SwingFXUtils.toFXImage(preDefinedTiles.get(groundFill), null));
        } else {
            tile = new Tile();
        }
        tile.setGroundFill(groundFill);
        return tile;
    }

}
