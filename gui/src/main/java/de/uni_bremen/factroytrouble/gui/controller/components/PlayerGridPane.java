/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gui.controller.components;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class PlayerGridPane extends GridPane {

    public PlayerGridPane() {
        setGridLinesVisible(true);
        setPrefHeight(800);
        setMinWidth(400);
        setPrefWidth(400);
        setMaxWidth(400);
        ColumnConstraints cc = new ColumnConstraints();
        cc.setMinWidth(400);
        cc.setPrefWidth(400);
        cc.setMaxWidth(400);
        cc.setHgrow(Priority.ALWAYS);
        getColumnConstraints().add(cc);
    }
}
