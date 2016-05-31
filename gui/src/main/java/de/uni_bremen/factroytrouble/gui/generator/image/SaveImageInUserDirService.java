/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gui.generator.image;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Speichert ein Bild aus dem Benutzerverzeichnis des Anwenders
 */
@Service
public class SaveImageInUserDirService implements SaveImageService{

    private static final Logger LOGGER = Logger.getLogger(SaveImageInUserDirService.class);

    @Override
    public void save(Image image, String path, String fileName) {
        try {
            File filePath = new File(System.getProperty("user.home") + File.separator + "factoryTrouble" + path);
            if(!filePath.exists()) {
                filePath.mkdirs();
            }
            ImageIO.write((BufferedImage) image, "png", new FileOutputStream(System.getProperty("user.home") + File.separator + "factoryTrouble" + path + File.separator +  fileName));
        } catch (IOException e) {
            LOGGER.error("Unable to save image", e);
        }
    }

    @Override
    public void saveResized(Image image, String path, String fileName, Integer resizeFactor) {
        save(getScaledInstance((BufferedImage) image, resizeFactor), path, fileName);
    }

    private Image getScaledInstance(BufferedImage image, Integer resizeFactor) {
        BufferedImage bufferedImage = new BufferedImage(image.getWidth() / resizeFactor, image.getHeight() / resizeFactor, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = bufferedImage.getGraphics();
        graphics.drawImage(image, 0, 0, image.getWidth() / resizeFactor, image.getHeight() / resizeFactor, null);
        return bufferedImage;
    }
}
