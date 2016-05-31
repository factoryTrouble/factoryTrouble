/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.editor.component;

import de.uni_bremen.factroytrouble.editor.ApplicationSettings;
import de.uni_bremen.factroytrouble.editor.controller.CourseCreateController;
import de.uni_bremen.factroytrouble.editor.data.Orientation;
import de.uni_bremen.factroytrouble.editor.spring.SpringConfigHolder;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.List;

/**
 * Repräsentation eines Boards oder eines Docks zum füllen eines Courses
 *
 * @author Andre
 */
public class BoardAndDockItem extends GridPane{

    private static final double BUTTON_WIDTH = 60.0;
    private String name;
    private List<String> seralizedBoard;
    private boolean dock;

    private Label boardName;
    private Label buttonLabel;
    private GridPane buttonGrid;

    private Button northButton;
    private Button eastButton;
    private Button southButton;
    private Button westButton;

    /**
     * Erstellt einen neues Board oder Dock
     *
     * @param name
     *      Der Name
     * @param dock
     *      Handelt es sich um ein Dock
     */
    public BoardAndDockItem(String name, boolean dock) {
        super();
        this.name = name;
        this.dock = dock;
        boardName = new Label(name);
        boardName.setWrapText(true);
        add(boardName, 0, 0);
        setStyle();
        addButtonsAndClickEvents(dock);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getSeralizedBoard() {
        return seralizedBoard;
    }

    public void setSeralizedBoard(List<String> seralizedBoard) {
        this.seralizedBoard = seralizedBoard;
    }

    public boolean isDock() {
        return dock;
    }

    public void setDock(boolean dock) {
        this.dock = dock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BoardAndDockItem that = (BoardAndDockItem) o;

        if (dock != that.dock) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (dock ? 1 : 0);
        return result;
    }

    private void addButtonsAndClickEvents(boolean dock) {
        if(dock) {
            setOnMouseClicked(event -> SpringConfigHolder.getInstance().getContext().getBean(CourseCreateController.class).setDock(this));
            return;
        }

        buttonLabel = new Label("Oben ist:");
        buttonLabel.setWrapText(true);
        add(buttonLabel, 0, 1);

        buttonGrid = new GridPane();
        addButtons();
        northButton.setOnMouseClicked(new BoardButtonClickHandler(this, Orientation.NORTH));
        eastButton.setOnMouseClicked(new BoardButtonClickHandler(this, Orientation.EAST));
        southButton.setOnMouseClicked(new BoardButtonClickHandler(this, Orientation.SOUTH));
        westButton.setOnMouseClicked(new BoardButtonClickHandler(this, Orientation.WEST));

        buttonGrid.add(northButton, 0, 0);
        buttonGrid.add(eastButton, 1, 0);
        buttonGrid.add(southButton, 2, 0);
        buttonGrid.add(westButton, 3, 0);
        add(buttonGrid, 0, 2);
    }

    private void addButtons() {
        northButton = new Button("Oben");
        northButton.setMinWidth(BUTTON_WIDTH);
        eastButton = new Button("Rechts");
        eastButton.setMinWidth(BUTTON_WIDTH);
        southButton = new Button("Unten");
        southButton.setMinWidth(BUTTON_WIDTH);
        westButton = new Button("Links");
        westButton.setMinWidth(BUTTON_WIDTH);
    }

    private void setStyle() {
        setWidth(300.0);
        setPadding(new Insets(10, 0, 10, 0));
        setStyle("-fx-border-width: 0 0 2 0; -fx-border-color: " + ApplicationSettings.MIDNIGHT_BLUE + ";");
    }

    private class BoardButtonClickHandler implements EventHandler<MouseEvent> {

        private Orientation orientation;
        BoardAndDockItem parentInstance;

        public BoardButtonClickHandler(BoardAndDockItem parentInstance, Orientation orientation) {
            this.parentInstance = parentInstance;
            this.orientation = orientation;
        }

        @Override
        public void handle(MouseEvent event) {
            SpringConfigHolder.getInstance().getContext().getBean(CourseCreateController.class).setBoard(parentInstance, orientation);
        }
    }
}
