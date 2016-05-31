/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gui.controller.components;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;

import java.awt.image.BufferedImage;

/**
 * Created by johannes.gesenhues on 30.12.15.
 * <p>
 * Diese Klasse entspricht einem Board Bild auf dem GameFieldController
 */
public class BoardImageView extends ImageView {
    private final int size;

    public BoardImageView(BufferedImage bufferedImage) {
        super(SwingFXUtils.toFXImage(bufferedImage, null));
        size = bufferedImage.getWidth();
        setPreserveRatio(true);
        setFitWidth(size);
    }

    public void setZoom(double zoom) {
        setFitWidth(size * zoom);
    }
}
