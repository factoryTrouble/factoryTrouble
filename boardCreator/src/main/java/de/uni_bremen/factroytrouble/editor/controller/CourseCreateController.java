/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.editor.controller;

import de.uni_bremen.factroytrouble.editor.component.BoardAndDockItem;
import de.uni_bremen.factroytrouble.editor.component.Tile;
import de.uni_bremen.factroytrouble.editor.container.image.TileGroundImageContainer;
import de.uni_bremen.factroytrouble.editor.data.GroundFill;
import de.uni_bremen.factroytrouble.editor.data.Orientation;
import de.uni_bremen.factroytrouble.editor.data.TileWithPosition;
import de.uni_bremen.factroytrouble.editor.service.save.FileNameAlertService;
import de.uni_bremen.factroytrouble.editor.service.save.SaveCourseService;
import de.uni_bremen.factroytrouble.editor.service.serialisisation.DeserializeBoardService;
import de.uni_bremen.factroytrouble.editor.service.tile.GenerateFlagService;
import de.uni_bremen.factroytrouble.editor.service.view.AlertService;
import de.uni_bremen.factroytrouble.editor.service.view.GridPaneUtilService;
import de.uni_bremen.factroytrouble.editor.service.view.initalisation.InitBoardAndDockListService;
import de.uni_bremen.factroytrouble.editor.service.view.initalisation.InitialTileGridService;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import java.awt.Point;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Controller zum erstellen von Course
 *
 * @author Andre
 */
@Controller
public class CourseCreateController extends AbstractEditorController {

    private static final String CAN_NOT_SAVE_COURSE = "Course kann nicht gespeichert werden";
    private static final Integer MAX_FLAG_COUNT = 99;

    @FXML private GridPane boardAndDock;
    @FXML private GridPane boardGrid;
    @FXML private GridPane dockGrid;
    @FXML private GridPane markerGrid;

    @Autowired private InitBoardAndDockListService initBoardAndDockListService;
    @Autowired private InitialTileGridService initialTileGridService;
    @Autowired private DeserializeBoardService deserializeBoardService;
    @Autowired private GridPaneUtilService gridPaneUtilService;
    @Autowired private GenerateFlagService generateFlagService;
    @Autowired private TileGroundImageContainer tileGroundImageContainer;
    @Autowired private AlertService alertService;
    @Autowired private SaveCourseService saveCourseService;
    @Autowired private FileNameAlertService fileNameAlertService;

    private boolean boardSet;
    private boolean dockSet;

    private String boardName;
    private String dockName;
    private Orientation boardOrientation;

    private Map<Integer, TileWithPosition> flags;

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
        flags = new HashMap<>();
        initMacMenu();
        initView();
        addWindowSizeChangeListener();
        addMouseClickListenerForTileGrid();
    }

    /**
     * Setzt das Board für den Course
     *
     * @param boardAndDock
     *      Repräsentation des Board
     *
     * @param orientation
     *      Richtung, in der das Board gesetzt werden soll
     */
    public void setBoard(BoardAndDockItem boardAndDock, Orientation orientation) {
        boardGrid.getChildren().clear();
        initialTileGridService.fillBoardTilePane(boardGrid);
        deserializeBoardService.deserializeBoard(boardGrid, boardAndDock.getSeralizedBoard(), 12, 12, orientation);
        boardOrientation = orientation;
        boardName = boardAndDock.getName();
        boardSet = true;
        flags.clear();
        addClickHandlerToEmptyTiles();
    }

    /**
     * Setzt das Dock für den Course
     *
     * @param boardAndDock
     *      Repräsentation des Dock
     */
    public void setDock(BoardAndDockItem boardAndDock) {
        dockGrid.getChildren().clear();
        initialTileGridService.fillDockTilePane(dockGrid);
        deserializeBoardService.deserializeBoard(dockGrid, boardAndDock.getSeralizedBoard(), 12, 4, Orientation.NORTH);
        dockName = boardAndDock.getName();
        dockSet = true;
    }

    /**
     * Setzt den Namen der View
     * @param filePath
     */
    @Override
    @Value("/views/courseCreateView.fxml")
    public void setFxmlFilePath(String filePath) {
        fxmlFilePath = filePath;
    }

    /**
     * Gibt die View des CourseCreators zurücl
     * @return
     *      Die View
     */
    @Override
    public Parent getView() {
        Parent parent = super.getView();
        initView();
        return parent;
    }

    @FXML
    private void saveBoard(ActionEvent actionEvent) {
        if(!boardSet) {
            alertService.showAlert(CAN_NOT_SAVE_COURSE, "Es muss ein Board gesetzt sein", Alert.AlertType.ERROR);
            return;
        }
        if(!dockSet) {
            alertService.showAlert(CAN_NOT_SAVE_COURSE, "Es muss ein Dock gesetzt sein", Alert.AlertType.ERROR);
            return;
        }
        if(flags.size() < 1) {
            alertService.showAlert(CAN_NOT_SAVE_COURSE, "Es muss eine Flagge gesetzt sein", Alert.AlertType.ERROR);
            return;
        }
        saveCourseService.save(fileNameAlertService.showFilenameAlert("Course"), boardName, dockName, boardOrientation, flags);
    }

    private void initView() {
        boardSet = false;
        dockSet = false;
        boardGrid.getChildren().clear();
        dockGrid.getChildren().clear();
        initBoardAndDockListService.fillTreeView(boardAndDock);
        initialTileGridService.fillBoardTilePane(boardGrid);
        initialTileGridService.fillDockTilePane(dockGrid);
        calcTileGridPadding(boardAndDock);
    }

    private void addClickHandlerToEmptyTiles() {
        for(int row = 0; row < 12; row++) {
            for (int column = 0; column < 12; column++) {
                Tile tile = (Tile) gridPaneUtilService.getNodeFromGridPane(boardGrid, column, row);
                if(!GroundFill.EMPTY.equals(tile.getGroundFill())) {
                    continue;
                }
                tile.addAdditionalClickEvent(new TileClickEvent(tile, column, 11 - row));
            }
        }
    }

    private void rearrangeFlags(Integer flagNumber) {
        new HashMap<>(flags).forEach((number, tileWithPosition) -> {
            if(number.equals(flagNumber)) {
                flags.remove(number);
            }
            if(number > flagNumber) {
                flags.remove(number);
                flags.put(number - 1, tileWithPosition);
                tileWithPosition.getTile().setFlagNumber(number - 1);
                tileWithPosition.getTile().setTileImage(SwingFXUtils.toFXImage(generateFlagService.generateFlagTileImage(number - 1), null));
            }
        });
    }

    /**
     * Click Envent für ein Tile, um eine Flagge zu setzten
     *
     * @author Andre
     */
    private class TileClickEvent implements EventHandler<MouseEvent> {
        private Tile me;
        private Integer column;
        private Integer row;
        private boolean flagSet = false;

        /**
         * Erstellt eine neues Click-Event
         *
         * @param me
         *      Tile, auf dem der Handler registriert wird
         *
         * @param column
         *      Die Spalte des Tiles
         *
         * @param row
         *      Die Zeile des Tiles
         */
        public TileClickEvent(Tile me, Integer column, Integer row) {
            this.me = me;
            this.column = column;
            this.row = row;
        }

        /**
         * Setzt oder entfernt eine Flagge auf dem eigenen Tile
         *
         * @param event
         */
        @Override
        public void handle(MouseEvent event) {
            if(!flagSet) {
                setMeAsFlag();
            } else {
                removeMeAsFlag();
            }
        }

        private void removeMeAsFlag() {
            me.setGroundFill(GroundFill.EMPTY);
            me.setTileImage(SwingFXUtils.toFXImage(tileGroundImageContainer.getImageForGround(GroundFill.EMPTY), null));
            flagSet = false;
            rearrangeFlags(me.getFlagNumber());
            me.setFlagNumber(null);
        }

        private void setMeAsFlag() {
            if(flags.size() > MAX_FLAG_COUNT) {
                return;
            }
            flags.put(flags.size() + 1, new TileWithPosition(me, new Point(column, row)));
            me.setGroundFill(GroundFill.FLAG);
            me.setTileImage(SwingFXUtils.toFXImage(generateFlagService.generateFlagTileImage(flags.size()), null));
            me.setFlagNumber(flags.size());
            flagSet = true;
        }

    }

}

