/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.editor.service.save;

import de.uni_bremen.factroytrouble.editor.service.serialisisation.SerializeBoardService;
import javafx.scene.layout.GridPane;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Speichert ein Board oder Dock
 *
 * @author Andre
 */
@Service
public class SaveBoardService {

    private static final Logger LOGGER = Logger.getLogger(SaveBoardService.class);
    private static final Integer COLUMNS = 12;
    private static final Integer BOARD_HEIGHT = 12;
    private static final Integer DOCK_HEIGHT = 4;

    @Autowired private SaveInUserDirService saveInUserDirService;
    @Autowired private SerializeBoardService serializeBoardService;

    /**
     * Speichert ein Board
     *
     * @param boardGrid
     *      Grid des Boards
     *
     * @param boardName
     *      Name des Boards
     */
    public void saveBoard(GridPane boardGrid, String boardName) {
        LOGGER.debug("Save board " +  boardName);
        saveInUserDirService.saveBoard(serializeBoardService.serializeBoardGrid(COLUMNS, BOARD_HEIGHT, boardGrid), boardName);
    }

    /**
     * Speichert ein Dock
     *
     * @param boardGrid
     *      Grid des Docks
     *
     * @param boardName
     *      Names des Dock
     */
    public void saveDock(GridPane boardGrid, String boardName) {
        LOGGER.debug("Save dock " +  boardName);
        saveInUserDirService.saveDock(serializeBoardService.serializeBoardGrid(COLUMNS, DOCK_HEIGHT, boardGrid), boardName);
    }

}
