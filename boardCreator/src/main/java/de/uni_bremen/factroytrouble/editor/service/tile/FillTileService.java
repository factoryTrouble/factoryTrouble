/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.editor.service.tile;

import de.uni_bremen.factroytrouble.editor.component.EditorTileFillToolIcon;
import de.uni_bremen.factroytrouble.editor.component.Tile;
import de.uni_bremen.factroytrouble.editor.container.image.TileGroundImageContainer;
import de.uni_bremen.factroytrouble.editor.data.FieldData;
import de.uni_bremen.factroytrouble.editor.data.FieldObject;
import de.uni_bremen.factroytrouble.editor.data.GroundFill;
import de.uni_bremen.factroytrouble.editor.data.Orientation;
import de.uni_bremen.factroytrouble.editor.service.util.EvaluateClickOrientationService;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.layout.GridPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Füllt ein Tile mit Hilfe eine EditorIcons
 *
 * @author André
 */
@Service
public class FillTileService {

    @Autowired private EvaluateClickOrientationService evaluateClickOrientationService;
    @Autowired private CombineTileService combineTileService;
    @Autowired private ConveyorBeltTileDispatcherService conveyorBeltTileDispatcherService;
    @Autowired private TileGroundImageContainer tileGroundImageContainer;

    /**
     * Befüllt ein Feld mit einer zusätzlichen Wand (oder eine Initale) und daran angeschlossende Objekte
     *
     * @param tileRelativeX
     *      Die Relative X Position, in die geklickt wurde
     *
     * @param tileRelativeY
     *      Die Relative Y Position, in die geklickt wurde
     *
     * @param currentTile
     *      Das Tile, auf dem was hinzugefügt wurde
     *
     * @param tileSize
     *      Größe eines Tiles in Pixel (es wird von quadratischen Feldern ausgegangen)
     *
     * @param clickableBoarder
     *      Wie weit von Rand entfernt darf geklickt werden, damit dieser als eine Richtung zugeordnet werden kann
     *
     * @param activeTool
     *      Das Bearbeitungswerkzeug, welches das zu hinzuzufügende Objekt beinhaltet
     */
    public void addFieldObject(Double tileRelativeX, Double tileRelativeY, Tile currentTile, Integer tileSize, Double clickableBoarder, EditorTileFillToolIcon activeTool) {
        Orientation clickOrientation = evaluateClickOrientationService.evaluate(tileRelativeX, tileRelativeY, tileSize, clickableBoarder);
        if(clickOrientation == null) {
            return;
        }
        FieldData currentFieldData = currentTile.getFieldData(clickOrientation);
        if(currentFieldData == null) {
            currentFieldData = new FieldData();
        }
        currentFieldData.setOrientation(clickOrientation);
        currentFieldData.setFieldObject(FieldObject.WALL);
        currentTile.setTileImage(SwingFXUtils.toFXImage(combineTileService.combineGroundWithWallObject(currentTile.getImage(), FieldObject.WALL, clickOrientation, tileSize), null));
        currentTile.addFieldData(currentFieldData, clickOrientation);
        if(FieldObject.WALL.equals(activeTool.getFieldObject())) {
            return;
        }
        FieldData connectToFieldObject = currentFieldData;
        while (connectToFieldObject.getConnectedTo() != null) {
            connectToFieldObject = connectToFieldObject.getConnectedTo();
        }

        FieldData newFieldData = new FieldData();
        newFieldData.setOrientation(clickOrientation);
        newFieldData.setFieldObject(activeTool.getFieldObject());
        connectToFieldObject.setConnectedTo(newFieldData);
        currentTile.setTileImage(SwingFXUtils.toFXImage(combineTileService.combineGroundWithWallObject(currentTile.getImage(), activeTool.getFieldObject(), clickOrientation, tileSize), null));
    }

    /**
     * Befüllt ein Feld mit einer Bodenfüllung
     *
     * @param tileColumn
     *      Die X Position des Tiles im Grid
     *
     * @param tileRow
     *      Die Y Position des Tiles im Grid
     *
     * @param nodeFromGridPane
     *      Das Tile, auf das der Boden kinzugefügt werden soll
     *
     * @param activeTool
     *      Das Bearbeitungswerkzeug, welches das zu hinzuzufügende Objekt beinhaltet
     *
     * @param tileGridPane
     *      Das Grid, in dem sich das Tile befindet
     */
    public void fillGroundWithTool(Integer tileColumn, Integer tileRow, Tile nodeFromGridPane, EditorTileFillToolIcon activeTool, GridPane tileGridPane) {
        Tile tile = nodeFromGridPane;
        if (GroundFill.CONVEYOR_BELT.equals(activeTool.getGroundFill()) || GroundFill.EXPRESS_CONVEYOR_BELT.equals(activeTool.getGroundFill())) {
            tile.setTileImage(SwingFXUtils.toFXImage(conveyorBeltTileDispatcherService.getConveyorBeltByEditorTool(activeTool), null));
            tile.setGroundFill(activeTool.getGroundFill());
            tile.setOrientation(activeTool.getOrientation());
            conveyorBeltTileDispatcherService.dispatchNeighborsInGrid(tileGridPane, tileColumn, tileRow);
            return;
        }
        tile.setGroundFill(activeTool.getGroundFill());
        tile.setTileImage(SwingFXUtils.toFXImage(tileGroundImageContainer.getImageForGround(activeTool.getGroundFill()), null));
        conveyorBeltTileDispatcherService.dispatchNeighborsInGrid(tileGridPane, tileColumn, tileRow);
        if(GroundFill.START.equals(activeTool.getGroundFill())) {
            tile.setStartPosition(true);
        }
    }

}
