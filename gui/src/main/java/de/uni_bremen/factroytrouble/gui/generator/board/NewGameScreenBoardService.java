/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gui.generator.board;

import de.uni_bremen.factroytrouble.gui.generator.image.LoadImageService;
import de.uni_bremen.factroytrouble.gui.generator.image.SaveImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Integration verschiedener Services, um Resourcenschonenden die Preview Bilder zu halten.
 */
@Service
public class NewGameScreenBoardService {

    private static final String BOARDS_PREVIEW_FOLDER = File.separator + "boards" + File.separator + "preview";
    private static final String PNG_FILE_SUFFIX = ".png";
    @Autowired private SaveImageService saveImageService;
    @Autowired private LoadImageService loadImageService;
    @Autowired private BoardGenerator boardGenerator;

    /**
     * Gibt ein Vorschaubild für ein Spielfeld zurück. Wenn dieses noch nicht vorhanden ist, wird dieses erstellt
     *
     * @param boardName
     *      Name des Boards
     * @return
     *      Bild des Boars
     */
    public Image getPreviewImage(String boardName) {
        Image image = loadImageService.getImageByFileName(getFileName(boardName));
        if(image != null) {
            return image;
        }
        saveImageService.saveResized((BufferedImage) boardGenerator.generateBoard(boardName), BOARDS_PREVIEW_FOLDER, boardName + PNG_FILE_SUFFIX, 10);
        return loadImageService.getImageByFileName(getFileName(boardName));
    }

    private String getFileName(String boardName) {
        return BOARDS_PREVIEW_FOLDER + File.separator + boardName + PNG_FILE_SUFFIX;
    }

}
