/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.editor.service.view.initalisation;

import de.uni_bremen.factroytrouble.editor.data.GroundFill;
import de.uni_bremen.factroytrouble.editor.factory.TileFactory;
import javafx.scene.layout.GridPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Füllt das Grid initalial
 *
 * @author Andre
 */
@Service
public class InitialTileGridService {

    @Autowired private TileFactory tileFactory;

    /**
     * Füllt ein Board Grid Inital
     *
     * @param tilePane
     *      Grid für die Tiles
     */
    public void fillBoardTilePane(GridPane tilePane) {
        for(int row = 0; row < 12; row++) {
            for(int column = 0; column < 12; column++) {
                tilePane.add(tileFactory.getTile(GroundFill.EMPTY), column, row);
            }
        }
    }

    /**
     * Füllt ein Dock Grid Inital
     *
     * @param tilePane
     *      Grid für die Tiles
     */
    public void fillDockTilePane(GridPane tilePane) {
        for(int row = 0; row < 4; row++) {
            for(int column = 0; column < 12; column++) {
                tilePane.add(tileFactory.getTile(GroundFill.EMPTY), column, row);
            }
        }
    }
}
