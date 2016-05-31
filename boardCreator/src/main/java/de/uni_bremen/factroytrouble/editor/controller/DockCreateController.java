/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.editor.controller;

import de.uni_bremen.factroytrouble.editor.service.save.FileNameAlertService;
import de.uni_bremen.factroytrouble.editor.service.save.SaveBoardService;
import de.uni_bremen.factroytrouble.editor.service.view.initalisation.InitialTileGridService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller zum erstellen eines Docks.
 *
 * @author Andre
 */
@Controller
public class DockCreateController extends AbstractEditorController {

    private static final String DOCK_TYPE_STRING = "Dock";

    @Autowired private SaveBoardService saveBoardService;
    @Autowired private FileNameAlertService fileNameAlertService;
    @Autowired private InitialTileGridService initialTileGridService;

    /**
     * Initalisiert den Controller
     *  - Füllt die Seitenelemente
     *  - Berechnet das Initale Padding des Tile-Grids
     *  - Fügt Change Listener hinzu
     * @param location
     *      s. JavaDoc von Initializable
     * @param resources
     *      s. JavaDoc von Initializable
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        initialTileGridService.fillDockTilePane(tileGridPane);
    }

    /**
     * Setzt den Namen der View
     * @param filePath
     */
    @Override
    @Value("/views/dockCreateView.fxml")
    public void setFxmlFilePath(String filePath) {
        fxmlFilePath = filePath;
    }

    @FXML
    private void saveBoard(ActionEvent actionEvent) {
        String filename = fileNameAlertService.showFilenameAlert(DOCK_TYPE_STRING);
        if(filename != null) {
            saveBoardService.saveDock(tileGridPane, filename);
        }
    }

}
