/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.editor.service.tile;

import de.uni_bremen.factroytrouble.editor.container.image.WallAndObjectImageContainer;
import de.uni_bremen.factroytrouble.editor.data.FieldObject;
import de.uni_bremen.factroytrouble.editor.data.Orientation;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Verbindet eine Bodenfläche mit zusätlichen Objekten
 *
 * @author Andre
 */
@Service
public class CombineTileService {

    @Autowired private WallAndObjectImageContainer wallAndObjectImageContainer;

    /**
     * Verbindet eine Bodenfläche mit zusätlichen Objekten
     *
     * @param image
     *      Bild der Bodenfläche
     *
     * @param fieldObject
     *      Objekt, welches der Bodenfläche hinzugefügt werden soll
     *
     * @param orientation
     *      Richtung des hinzuzufügenden Objektes
     *
     * @param imageSize
     *      Größe des Bildes (in Pixel)
     *
     * @return
     *      Bild der Bodenfläche mit hinzugefügten Objekt
     */
    public BufferedImage combineGroundWithWallObject(Image image, FieldObject fieldObject, Orientation orientation, Integer imageSize) {
        BufferedImage combinedImage = SwingFXUtils.fromFXImage(image, new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_ARGB));
        Graphics graphics = combinedImage.getGraphics();
        if(FieldObject.WALL.equals(fieldObject)) {
            graphics.drawImage(wallAndObjectImageContainer.getWall(orientation), 0, 0, null);
        }
        if(FieldObject.PUSHER_EVEN.equals(fieldObject)) {
            graphics.drawImage(wallAndObjectImageContainer.getPusher(orientation, false), 0, 0, null);
        }
        if(FieldObject.PUSHER_ODD.equals(fieldObject)) {
            graphics.drawImage(wallAndObjectImageContainer.getPusher(orientation, true), 0, 0, null);
        }
        if(FieldObject.LASER_SINGLE.equals(fieldObject)) {
            graphics.drawImage(wallAndObjectImageContainer.getLaser(1, Orientation.WEST.equals(orientation) || Orientation.EAST.equals(orientation)), 0, 0, null);
        }
        if(FieldObject.LASER_DOUBLE.equals(fieldObject)) {
            graphics.drawImage(wallAndObjectImageContainer.getLaser(2, Orientation.WEST.equals(orientation) || Orientation.EAST.equals(orientation)), 0, 0, null);
        }
        if(FieldObject.LASER_TRIPLE.equals(fieldObject)) {
            graphics.drawImage(wallAndObjectImageContainer.getLaser(3, Orientation.WEST.equals(orientation) || Orientation.EAST.equals(orientation)), 0, 0, null);
        }
        return combinedImage;
    }

}
