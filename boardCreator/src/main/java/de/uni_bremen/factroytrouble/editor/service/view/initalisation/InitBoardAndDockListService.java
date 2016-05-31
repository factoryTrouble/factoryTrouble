/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.editor.service.view.initalisation;

import de.uni_bremen.factroytrouble.editor.component.BoardAndDockItem;
import de.uni_bremen.factroytrouble.editor.service.load.FileContentService;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.FontWeight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Füllt ein CourseGrid
 *
 * @author André
 */
@Service
public class InitBoardAndDockListService {

    private static final String USER_DIR_PATH = System.getProperty("user.home") + File.separator + "factoryTrouble";

    @Autowired private FileContentService fileContentService;

    /**
     * Füllt die Seitenleiste mit allen Boards und Docks
     *
     * @param gridPane
     *      Grid, in dem alles hinzugefügt werden soll
     */
    public void fillTreeView(GridPane gridPane) {
        gridPane.getChildren().clear();
        File boardAndDockDirectory = new File(USER_DIR_PATH + File.separator + "boards" + File.separator + "descriptions");
        if(!boardAndDockDirectory.exists() || !boardAndDockDirectory.isDirectory()) {
            return;
        }
        gridPane.add(createHeadlineLabel("BOARDS"), 0, 0);
        int row = 1;
        for (File file : boardAndDockDirectory.listFiles(pathname -> pathname.getName().matches("FIELD_.*"))) {
            BoardAndDockItem item = new BoardAndDockItem(file.getName(), false);
            item.setSeralizedBoard(fileContentService.getFileContentSeperatedByLine(file));
            gridPane.add(item, 0, row);
            row++;
        }
        gridPane.add(createHeadlineLabel("DOCKS"), 0, row);
        row++;
        for (File file : boardAndDockDirectory.listFiles(pathname -> pathname.getName().matches("DOCK_.*"))) {
            BoardAndDockItem item = new BoardAndDockItem(file.getName(), true);
            item.setSeralizedBoard(fileContentService.getFileContentSeperatedByLine(file));
            gridPane.add(item, 0, row);
            row++;
        }
    }

    private Label createHeadlineLabel(String headline) {
        Label label = new Label(headline);
        label.setPadding(new Insets(10, 0, 10, 0));
        label.setStyle("-fx-font-weight: " + FontWeight.BOLD);
        return label;
    }

}
