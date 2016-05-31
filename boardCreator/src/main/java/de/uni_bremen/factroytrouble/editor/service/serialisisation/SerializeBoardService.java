/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.editor.service.serialisisation;

import de.uni_bremen.factroytrouble.editor.component.Tile;
import de.uni_bremen.factroytrouble.editor.data.FieldData;
import de.uni_bremen.factroytrouble.editor.data.FieldObject;
import de.uni_bremen.factroytrouble.editor.data.GroundFill;
import de.uni_bremen.factroytrouble.editor.data.Orientation;
import de.uni_bremen.factroytrouble.editor.service.view.GridPaneUtilService;
import javafx.scene.layout.GridPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Serialisiert ein Grid von Tiles zu einer Textuellen beschreibgung
 *
 * @author Andre
 */
@Service
public class SerializeBoardService {

    @Autowired private GridPaneUtilService gridPaneUtilService;

    /**
     * Serialisiert ein Grid von Tiles zu einer Textuellen beschreibgung
     *
     * @param columns
     *      Anzahl der Spalten im Grid
     *
     * @param rows
     *      Anzahl der Spalten im Grid
     *
     * @param boardGridPage
     *      Grid mit Tiles
     *
     * @return
     *      Serialisiertes Grid
     *
     */
    public String serializeBoardGrid(Integer columns, Integer rows, GridPane boardGridPage) {
        StringBuilder stringBuilder = new StringBuilder();
        Integer startPositionCount = 1;
        for(int row = rows - 1; row >= 0; row--) {
            for(int column = 0; column < columns; column++) {
                Tile tile = (Tile) gridPaneUtilService.getNodeFromGridPane(boardGridPage, column, row);
                serializeTileGround(stringBuilder, tile);
                serializeWallAndObjects(stringBuilder, tile);
                if(tile.isStartPosition()) {
                    stringBuilder.append("_s").append(startPositionCount);
                    startPositionCount++;
                }
                if(column + 1 != columns) {
                    stringBuilder.append(",");
                }
            }
            if(row != 0) {
                stringBuilder.append(System.getProperty("line.separator"));
            }
        }
        return stringBuilder.toString();
    }

    private void serializeWallAndObjects(StringBuilder stringBuilder, Tile tile) {
        for (Orientation orientation : Orientation.values()) {
            FieldData fieldData = tile.getFieldData(orientation);
            while (fieldData != null) {
                if(FieldObject.WALL.equals(fieldData.getFieldObject())) {
                    stringBuilder.append("_").append(fieldData.getFieldObject().getBoardFileDescription()).append(orientation.getBoardFileDescription());
                } else {
                    stringBuilder.append(fieldData.getFieldObject().getBoardFileDescription());
                }
                fieldData = fieldData.getConnectedTo();
            }
        }
    }

    private void serializeTileGround(StringBuilder stringBuilder, Tile tile) {
        if(GroundFill.EXPRESS_CONVEYOR_BELT.equals(tile.getGroundFill())) {
            stringBuilder.append(tile.getGroundFill().getBoardFileDescription().replace("#", tile.getOrientation().getBoardFileDescription()));
        }
        else if(GroundFill.CONVEYOR_BELT.equals(tile.getGroundFill())) {
            stringBuilder.append(tile.getGroundFill().getBoardFileDescription()).append("_").append(tile.getOrientation().getBoardFileDescription());
        }
        else {
            stringBuilder.append(tile.getGroundFill().getBoardFileDescription());
        }
    }

}
