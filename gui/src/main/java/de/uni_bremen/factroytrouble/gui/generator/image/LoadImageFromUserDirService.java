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
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Lädt ein Bild aus dem Benutzerverzeichnis des Anwenders
 */
@Service
public class LoadImageFromUserDirService implements LoadImageService {

    private static final Logger LOGGER = Logger.getLogger(LoadImageFromUserDirService.class);

    @Override
    public Image getImageByFileName(String fileName) {
        try {
            LOGGER.debug("Try to load " + System.getProperty("user.home") + File.separator + "factoryTrouble" + fileName);
            return ImageIO.read(new FileInputStream(System.getProperty("user.home") + File.separator + "factoryTrouble" + fileName));
        } catch (IOException e) {
            LOGGER.info("Could not load image. Return null", e);
            return null;
        }
    }

}
