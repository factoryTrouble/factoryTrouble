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
import de.uni_bremen.factroytrouble.editor.container.image.ConveyorBeltImageContainer;
import de.uni_bremen.factroytrouble.editor.data.ConveyorBeltTileImage;
import de.uni_bremen.factroytrouble.editor.data.GroundFill;
import de.uni_bremen.factroytrouble.editor.data.Orientation;
import de.uni_bremen.factroytrouble.editor.service.view.GridPaneUtilService;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

/**
 * Wählt aus, welches Förderband angezeigt werden soll
 *
 * @author Andre
 */
@Service
public class ConveyorBeltTileDispatcherService {

    private static final Logger LOGGER = Logger.getLogger(ConveyorBeltTileDispatcherService.class);

    @Autowired private ConveyorBeltImageContainer conveyorBeltImageContainer;
    @Autowired private GridPaneUtilService gridPaneUtilService;

    /**
     * Gibt bei gegebenden Editortool das richtige, initale Förderband zurück
     *
     * @param editorTool
     *      EditorTool, aus dem das Förderband gewählt werden soll
     *
     * @return
     *      Bild des Förderbandes
     */
    public BufferedImage getConveyorBeltByEditorTool(EditorTileFillToolIcon editorTool) {
        return conveyorBeltImageContainer.getConyorbeltImage(new ConveyorBeltTileImage(editorTool.getOrientation(), GroundFill.EXPRESS_CONVEYOR_BELT.equals(editorTool.getGroundFill()), editorTool.getOrientation().getOppositeDirection()));
    }

    /**
     * Setzt das Förderband für ein Tile, sowie für einen direkten Nachbarn neu, um Ecken verarbeiten zu können
     *
     * @param tileGrid
     *      Grid mit allem Tiles
     *
     * @param lastSetColumn
     *      Spalte des zuletzt gesetzen Tiles
     *
     * @param lastSetRow
     *      Zeile des zuletzt gesetzten Tiles
     *
     */
    public void dispatchNeighborsInGrid(GridPane tileGrid, Integer lastSetColumn, Integer lastSetRow) {
        Tile lastSetTile = (Tile) gridPaneUtilService.getNodeFromGridPane(tileGrid, lastSetColumn, lastSetRow);
        if(!GroundFill.EXPRESS_CONVEYOR_BELT.equals(lastSetTile.getGroundFill()) && !GroundFill.CONVEYOR_BELT.equals(lastSetTile.getGroundFill())) {
            LOGGER.debug("Clicked tile is not a conveyor belt");
            return;
        }
        dispatchLastSetConveyorBeltImage(tileGrid, lastSetColumn, lastSetRow, lastSetTile);

        Integer nextInDirectionColumn = lastSetColumn + lastSetTile.getOrientation().getColumnDelta();
        Integer nextInDirectionRow = lastSetRow + lastSetTile.getOrientation().getRowDelta();
        Node nextInConveyorBeltDirection = gridPaneUtilService.getNodeFromGridPane(tileGrid, nextInDirectionColumn, nextInDirectionRow);
        if(nextInConveyorBeltDirection == null) {
            return;
        }
        Tile nextInDirectionTile = (Tile) nextInConveyorBeltDirection;
        if(!GroundFill.EXPRESS_CONVEYOR_BELT.equals(nextInDirectionTile.getGroundFill()) && !GroundFill.CONVEYOR_BELT.equals(nextInDirectionTile.getGroundFill())) {
            LOGGER.debug("The next tile in the last set conveyor belt direction is not an conveyor belt");
            return;
        }
        ConveyorBeltTileImage nextInDirectionConveyorBeltTileImage = new ConveyorBeltTileImage(nextInDirectionTile.getOrientation(), GroundFill.EXPRESS_CONVEYOR_BELT.equals(nextInDirectionTile.getGroundFill()));
        isConveyorBeltInDirection(tileGrid, nextInDirectionColumn, nextInDirectionRow, nextInDirectionConveyorBeltTileImage, nextInDirectionTile.getOrientation().getOppositeDirection());
        isConveyorBeltInDirection(tileGrid, nextInDirectionColumn, nextInDirectionRow, nextInDirectionConveyorBeltTileImage, nextInDirectionTile.getOrientation().getNextDirection(true));
        isConveyorBeltInDirection(tileGrid, nextInDirectionColumn, nextInDirectionRow, nextInDirectionConveyorBeltTileImage, nextInDirectionTile.getOrientation().getNextDirection(false));
        if(nextInDirectionConveyorBeltTileImage.getInputOrientations().isEmpty()) {
            nextInDirectionConveyorBeltTileImage.getInputOrientations().add(nextInDirectionConveyorBeltTileImage.getOutputOrientation().getOppositeDirection());
        }
        nextInDirectionTile.setTileImage(SwingFXUtils.toFXImage(conveyorBeltImageContainer.getConyorbeltImage(nextInDirectionConveyorBeltTileImage), null));
    }

    private void dispatchLastSetConveyorBeltImage(GridPane tileGrid, Integer lastSetColumn, Integer lastSetRow, Tile lastSetTile) {
        ConveyorBeltTileImage lastSetConveyorBeltImage = new ConveyorBeltTileImage(lastSetTile.getOrientation(), GroundFill.EXPRESS_CONVEYOR_BELT.equals(lastSetTile.getGroundFill()));
        isConveyorBeltInDirection(tileGrid, lastSetColumn, lastSetRow, lastSetConveyorBeltImage, lastSetTile.getOrientation().getOppositeDirection());
        isConveyorBeltInDirection(tileGrid, lastSetColumn, lastSetRow, lastSetConveyorBeltImage, lastSetTile.getOrientation().getNextDirection(true));
        isConveyorBeltInDirection(tileGrid, lastSetColumn, lastSetRow, lastSetConveyorBeltImage, lastSetTile.getOrientation().getNextDirection(false));
        if(lastSetConveyorBeltImage.getInputOrientations().isEmpty()) {
            lastSetConveyorBeltImage.getInputOrientations().add(lastSetConveyorBeltImage.getOutputOrientation().getOppositeDirection());
        }
        lastSetTile.setTileImage(SwingFXUtils.toFXImage(conveyorBeltImageContainer.getConyorbeltImage(lastSetConveyorBeltImage), null));
    }

    private void isConveyorBeltInDirection(GridPane tileGrid, Integer lastSetColumn, Integer lastSetRow, ConveyorBeltTileImage lastSetConveyorBeltImage, Orientation orientation) {
        Node neighborNode = gridPaneUtilService.getNodeFromGridPane(tileGrid, lastSetColumn + orientation.getColumnDelta(), lastSetRow + orientation.getRowDelta());
        if(neighborNode != null) {
            Tile neighborTile = (Tile) neighborNode;
            if(isAConveyorBeltAndHasOutputWhereInput(orientation, neighborTile)) {
                lastSetConveyorBeltImage.getInputOrientations().add(orientation);
            }
        }
    }

    private boolean isAConveyorBeltAndHasOutputWhereInput(Orientation orientation, Tile neighborTile) {
        return (GroundFill.EXPRESS_CONVEYOR_BELT.equals(neighborTile.getGroundFill()) || GroundFill.CONVEYOR_BELT.equals(neighborTile.getGroundFill())) && orientation.getOppositeDirection().equals(neighborTile.getOrientation());
    }

}