/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gui.generator.dto;

import java.awt.image.BufferedImage;

/**
 * Speichert ein Bild und die Position in Pixel
 */
public class ImagePositionDTO {

    private BufferedImage image;

    private Integer positionLeft;
    private Integer positionTop;

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public Integer getPositionLeft() {
        return positionLeft;
    }

    public void setPositionLeft(Integer positionLeft) {
        this.positionLeft = positionLeft;
    }

    public Integer getPositionX() {
        return positionTop;
    }

    public void setPositionTop(Integer positionTop) {
        this.positionTop = positionTop;
    }
}
