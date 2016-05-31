/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.editor.service.view.initalisation;

import de.uni_bremen.factroytrouble.editor.component.EditorTileFillToolIcon;
import de.uni_bremen.factroytrouble.editor.container.image.ConveyorBeltImageContainer;
import de.uni_bremen.factroytrouble.editor.container.image.TileGroundImageContainer;
import de.uni_bremen.factroytrouble.editor.container.image.WallAndObjectImageContainer;
import de.uni_bremen.factroytrouble.editor.data.ConveyorBeltTileImage;
import de.uni_bremen.factroytrouble.editor.data.FieldObject;
import de.uni_bremen.factroytrouble.editor.data.GroundFill;
import de.uni_bremen.factroytrouble.editor.data.Orientation;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.layout.GridPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Initalisiert alle Werkzeuge
 *
 * @author Andre
 */
@Service
public class InitialEditorSidebarService {

    @Autowired private TileGroundImageContainer tileGroundImageContainer;
    @Autowired private ConveyorBeltImageContainer conveyorBeltImageContainer;
    @Autowired private WallAndObjectImageContainer wallAndObjectImageContainer;

    /**
     * Füllt alle Editorflächen
     *
     * @param groundGrid
     *      Grid für die Bodenflächen
     *
     * @param objectGrid
     *      Grid für Wände und mit den Wänden verbundene Objekte
     *
     * @param markerGrid
     *      Grid für Markierungen
     */
    public void fillPartGrids(GridPane groundGrid, GridPane objectGrid, GridPane markerGrid) {
        fillGroundGrid(groundGrid);
        fillObjectGrid(objectGrid);
        markerGrid.add(new EditorTileFillToolIcon(SwingFXUtils.toFXImage(tileGroundImageContainer.getImageForGround(GroundFill.START), null), "Startposition", GroundFill.START), 0, 0);
    }

    private void fillObjectGrid(GridPane objectGrid) {
        objectGrid.add(new EditorTileFillToolIcon(SwingFXUtils.toFXImage(wallAndObjectImageContainer.getWall(Orientation.SOUTH), null), "Wand", FieldObject.WALL), 0, 0);
        objectGrid.add(new EditorTileFillToolIcon(SwingFXUtils.toFXImage(wallAndObjectImageContainer.getPusher(Orientation.SOUTH, true), null), "Pusher Ungerade", FieldObject.PUSHER_ODD), 0, 1);
        objectGrid.add(new EditorTileFillToolIcon(SwingFXUtils.toFXImage(wallAndObjectImageContainer.getPusher(Orientation.SOUTH, false), null), "Pusher Gerade", FieldObject.PUSHER_EVEN), 1, 1);

        objectGrid.add(new EditorTileFillToolIcon(SwingFXUtils.toFXImage(wallAndObjectImageContainer.getLaser(1, false), null), "Laser einfach", FieldObject.LASER_SINGLE), 0, 2);
        objectGrid.add(new EditorTileFillToolIcon(SwingFXUtils.toFXImage(wallAndObjectImageContainer.getLaser(2, false), null), "Laser doppelt", FieldObject.LASER_DOUBLE), 1, 2);
        objectGrid.add(new EditorTileFillToolIcon(SwingFXUtils.toFXImage(wallAndObjectImageContainer.getLaser(3, false), null), "Laser dreifach", FieldObject.LASER_TRIPLE), 2, 2);
    }

    private void fillGroundGrid(GridPane groundGrid) {
        groundGrid.add(new EditorTileFillToolIcon(SwingFXUtils.toFXImage(tileGroundImageContainer.getImageForGround(GroundFill.EMPTY), null), "Leere Fläche", GroundFill.EMPTY), 0, 0);
        groundGrid.add(new EditorTileFillToolIcon(SwingFXUtils.toFXImage(tileGroundImageContainer.getImageForGround(GroundFill.HOLE), null), "Loch", GroundFill.HOLE), 1, 0);
        groundGrid.add(new EditorTileFillToolIcon(SwingFXUtils.toFXImage(tileGroundImageContainer.getImageForGround(GroundFill.GEAR_CW), null), "Zahnrad im Uhrzeigersinn", GroundFill.GEAR_CW), 2, 0);
        groundGrid.add(new EditorTileFillToolIcon(SwingFXUtils.toFXImage(tileGroundImageContainer.getImageForGround(GroundFill.GEAR_CCW), null), "Zahnrad gegen Uhrzeigersinn", GroundFill.GEAR_CCW), 3, 0);
        groundGrid.add(new EditorTileFillToolIcon(SwingFXUtils.toFXImage(tileGroundImageContainer.getImageForGround(GroundFill.REPAIR), null), "Werkstatt", GroundFill.REPAIR), 4, 0);

        groundGrid.add(new EditorTileFillToolIcon(SwingFXUtils.toFXImage(conveyorBeltImageContainer.getConyorbeltImage(new ConveyorBeltTileImage(Orientation.SOUTH, false, Orientation.NORTH)), null), "Förderband nach Süden", GroundFill.CONVEYOR_BELT, Orientation.SOUTH), 0, 1);
        groundGrid.add(new EditorTileFillToolIcon(SwingFXUtils.toFXImage(conveyorBeltImageContainer.getConyorbeltImage(new ConveyorBeltTileImage(Orientation.NORTH, false, Orientation.SOUTH)), null), "Förderband nach Norden", GroundFill.CONVEYOR_BELT, Orientation.NORTH), 1, 1);
        groundGrid.add(new EditorTileFillToolIcon(SwingFXUtils.toFXImage(conveyorBeltImageContainer.getConyorbeltImage(new ConveyorBeltTileImage(Orientation.WEST, false, Orientation.EAST)), null), "Förderband nach Westen", GroundFill.CONVEYOR_BELT, Orientation.WEST), 2, 1);
        groundGrid.add(new EditorTileFillToolIcon(SwingFXUtils.toFXImage(conveyorBeltImageContainer.getConyorbeltImage(new ConveyorBeltTileImage(Orientation.EAST, false, Orientation.WEST)), null), "Förderband nach Osten", GroundFill.CONVEYOR_BELT, Orientation.EAST), 3, 1);

        groundGrid.add(new EditorTileFillToolIcon(SwingFXUtils.toFXImage(conveyorBeltImageContainer.getConyorbeltImage(new ConveyorBeltTileImage(Orientation.SOUTH, true, Orientation.NORTH)), null), "Express-Förderband nach Süden", GroundFill.EXPRESS_CONVEYOR_BELT, Orientation.SOUTH), 0, 2);
        groundGrid.add(new EditorTileFillToolIcon(SwingFXUtils.toFXImage(conveyorBeltImageContainer.getConyorbeltImage(new ConveyorBeltTileImage(Orientation.NORTH, true, Orientation.SOUTH)), null), "Express-Förderband nach Norden", GroundFill.EXPRESS_CONVEYOR_BELT, Orientation.NORTH), 1, 2);
        groundGrid.add(new EditorTileFillToolIcon(SwingFXUtils.toFXImage(conveyorBeltImageContainer.getConyorbeltImage(new ConveyorBeltTileImage(Orientation.WEST, true, Orientation.EAST)), null), "Express-Förderband nach Westen", GroundFill.EXPRESS_CONVEYOR_BELT, Orientation.WEST), 2, 2);
        groundGrid.add(new EditorTileFillToolIcon(SwingFXUtils.toFXImage(conveyorBeltImageContainer.getConyorbeltImage(new ConveyorBeltTileImage(Orientation.EAST, true, Orientation.WEST)), null), "Express-Förderband nach Osten", GroundFill.EXPRESS_CONVEYOR_BELT, Orientation.EAST), 3, 2);
    }
}
