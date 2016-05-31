/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.editor.service.view;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import org.springframework.stereotype.Service;

/**
 * Hilfsmethoden zum Arbeiten mit GridPanes
 *
 * @author Andre
 */
@Service
public class GridPaneUtilService {

    /**
     * Gibt den Knoten an eine bestimmten Stelle in einen GrindPane zurück
     *
     * Quelle: http://stackoverflow.com/questions/20655024/javafx-gridpane-retrive-specific-cell-content
     *
     * @param gridPane
     *      Die GridPane in der gesucht werden soll
     * @param col
     *      Die Spalte
     * @param row
     *      Die Zeile
     * @return
     *      Den Knoten oder null, falls keiner gefunden wird
     */
    public Node getNodeFromGridPane(GridPane gridPane, Integer col, Integer row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node).equals(col) && GridPane.getRowIndex(node).equals(row)) {
                return node;
            }
        }
        return null;
    }

}
