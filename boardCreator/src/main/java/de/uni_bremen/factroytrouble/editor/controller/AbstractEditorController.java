/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.editor.controller;

import de.uni_bremen.factroytrouble.editor.component.EditorTileFillToolIcon;
import de.uni_bremen.factroytrouble.editor.component.Tile;
import de.uni_bremen.factroytrouble.editor.container.image.TileGroundImageContainer;
import de.uni_bremen.factroytrouble.editor.service.tile.CombineTileService;
import de.uni_bremen.factroytrouble.editor.service.tile.ConveyorBeltTileDispatcherService;
import de.uni_bremen.factroytrouble.editor.service.tile.FillTileService;
import de.uni_bremen.factroytrouble.editor.service.util.EvaluateClickOrientationService;
import de.uni_bremen.factroytrouble.editor.service.view.GridPaneUtilService;
import de.uni_bremen.factroytrouble.editor.service.view.initalisation.InitialEditorSidebarService;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Abstrakter Controller für Editoren, die mit einzelens Tiles arbeiten
 *
 * @author Andre
 */
public abstract class AbstractEditorController extends FXMLController {

    private static final Logger LOGGER = Logger.getLogger(AbstractEditorController.class);
    private static final Integer MINIMUM_GRID_PADDING = 50;
    private static final Integer TILE_SIZE = 50;
    private static final Integer TILE_COUNT = 12;
    private static final Integer TILE_GRIND_PANE_SIZE = TILE_SIZE * TILE_COUNT;
    private static final Double CLICKABLE_BORDER = TILE_SIZE * 0.4;

    @Autowired private GridPaneUtilService gridPaneUtilService;
    @Autowired private ConveyorBeltTileDispatcherService conveyorBeltTileDispatcherService;
    @Autowired private TileGroundImageContainer tileGroundImageContainer;
    @Autowired private EvaluateClickOrientationService evaluateClickOrientationService;
    @Autowired private CombineTileService combineTileService;
    @Autowired private InitialEditorSidebarService initialEditorSidebarService;
    @Autowired private FillTileService fillTileService;

    @FXML protected GridPane groundGrid;
    @FXML protected GridPane objectGrid;
    @FXML protected GridPane markerGrid;
    @FXML protected GridPane tileGridPane;
    @FXML protected ScrollPane tileGridScrollPane;
    @FXML protected AnchorPane tileGridAnchorPane;

    protected EditorTileFillToolIcon activeTool = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        initialEditorSidebarService.fillPartGrids(groundGrid, objectGrid, markerGrid);
        calcTileGridPadding();
        addWindowSizeChangeListener();
        addMouseClickListenerForTileGrid();
    }

    /**
     * Setzt das aktuell gewählte Werkzeug zum befüllen der Tiles
     * @param activeTool
     *      Das aktive Werkzeug
     */
    public void setActiveTool(EditorTileFillToolIcon activeTool) {
        LOGGER.debug("Set active tool: " + ((activeTool != null) ? activeTool.toString() : "null"));
        this.activeTool = activeTool;
        if(activeTool == null) {
            return;
        }
        resetTools(groundGrid);
        resetTools(objectGrid);
        resetTools(markerGrid);
    }

    protected void calcTileGridPadding(GridPane gridPane) {
        Double scrollPaneHeight = tileGridScrollPane.getHeight();
        Double scrollPaneWidth = tileGridScrollPane.getWidth();
        Double newVerticalPadding = MINIMUM_GRID_PADDING.doubleValue();
        Double newHorizontalPadding = MINIMUM_GRID_PADDING.doubleValue();
        if(scrollPaneWidth > 2 * MINIMUM_GRID_PADDING + TILE_GRIND_PANE_SIZE) {
            newVerticalPadding = (scrollPaneWidth - TILE_GRIND_PANE_SIZE) / 2;
        }
        if(scrollPaneHeight > 2 * MINIMUM_GRID_PADDING + TILE_GRIND_PANE_SIZE) {
            newHorizontalPadding = (scrollPaneHeight - TILE_GRIND_PANE_SIZE) / 2;
        }
        gridPane.setPadding(new Insets(newHorizontalPadding, newVerticalPadding, newHorizontalPadding, newVerticalPadding));
    }

    protected void calcTileGridPadding() {
        calcTileGridPadding(tileGridPane);
    }

    protected void addWindowSizeChangeListener() {
        tileGridAnchorPane.widthProperty().addListener((observableValue, oldSceneWidth, newSceneWidth) -> calcTileGridPadding());
        tileGridAnchorPane.heightProperty().addListener((observableValue, oldSceneHeight, newSceneHeight) -> calcTileGridPadding());
    }

    protected void addMouseClickListenerForTileGrid(GridPane gridPane) {
        gridPane.setOnMouseClicked(mouseEvent -> {
            if(activeTool == null) {
                return;
            }
            Point2D relativeGrindPosition = gridPane.localToScene(gridPane.getLayoutBounds().getMinX(), gridPane.getLayoutBounds().getMinY());
            Double relativeX = getGlobalTilesRelativeXPosition(mouseEvent, relativeGrindPosition);
            Double relativeY = getGlobalTilesRelativeYPosition(mouseEvent, relativeGrindPosition);
            double tileRelativeX = relativeX % TILE_SIZE;
            Integer tileColumn = getTilePosition(relativeX, tileRelativeX);
            double tileRelativeY = relativeY % TILE_SIZE;
            Integer tileRow = getTilePosition(relativeY, tileRelativeY);
            Node nodeFromGridPane = gridPaneUtilService.getNodeFromGridPane(gridPane, tileColumn, tileRow);
            if(nodeFromGridPane == null) {
                return;
            }
            Tile currentTile = (Tile) nodeFromGridPane;
            if(activeTool.getGroundFill() != null) {
                fillTileService.fillGroundWithTool(tileColumn, tileRow, currentTile, activeTool, tileGridPane);
            }
            if(activeTool.getFieldObject() != null) {
                fillTileService.addFieldObject(tileRelativeX, tileRelativeY, currentTile, TILE_SIZE, CLICKABLE_BORDER, activeTool);
            }
        });
    }

    protected void addMouseClickListenerForTileGrid() {
        addMouseClickListenerForTileGrid(tileGridPane);
    }

    private void resetTools(GridPane gridPane) {
        gridPane.getChildren().forEach(tool -> {
            if (!activeTool.equals(tool)) {
                ((EditorTileFillToolIcon)tool).resetClickedStatus();
            }
        });
    }

    private Integer getTilePosition(Double gridRelative, Double tileRelative) {
        return (int) (gridRelative - tileRelative) / TILE_SIZE;
    }

    private double getGlobalTilesRelativeYPosition(MouseEvent mouseEvent, Point2D relativeGrindPosition) {
        return mouseEvent.getSceneY() - relativeGrindPosition.getY() - tileGridPane.getPadding().getTop();
    }

    private Double getGlobalTilesRelativeXPosition(MouseEvent mouseEvent, Point2D relativeGrindPosition) {
        return mouseEvent.getSceneX() - relativeGrindPosition.getX() - tileGridPane.getPadding().getLeft();
    }
}
