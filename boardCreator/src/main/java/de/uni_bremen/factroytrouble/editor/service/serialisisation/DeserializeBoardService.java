/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.editor.service.serialisisation;

import de.uni_bremen.factroytrouble.editor.component.EditorTileFillToolIcon;
import de.uni_bremen.factroytrouble.editor.component.Tile;
import de.uni_bremen.factroytrouble.editor.data.FieldObject;
import de.uni_bremen.factroytrouble.editor.data.GroundFill;
import de.uni_bremen.factroytrouble.editor.data.Orientation;
import de.uni_bremen.factroytrouble.editor.service.tile.FillTileService;
import de.uni_bremen.factroytrouble.editor.service.view.GridPaneUtilService;
import javafx.scene.layout.GridPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Deseraliziert ein Board
 *
 * @author André
 */
@Service
public class DeserializeBoardService {

    private static final Pattern TILE_PATTERN = Pattern.compile("([^_]*)_*");
    private static final Pattern WALL_PATTERN = Pattern.compile("(w)[asenow]{2}((l{1,3})|(p\\d{2,3}))*");
    private static final Pattern IS_WALL_PATTEN = Pattern.compile("^w(no|so|we|ea).*");

    @Autowired private GridPaneUtilService gridPaneUtilService;
    @Autowired private FillTileService fillTileService;

    /**
     * Erstellt aus einer textuellen Beschreibung eines Board oder Dock ein gefülltes Grid der beschreibung entsprechend.
     * Dabei werden drehung beachtet
     *
     * @param tileGrid
     *      Beriets gefülltes Grid (es reichen leere Felder)
     *
     * @param boardContent
     *      Liste alle Zeilen der Textuellen Darstellung
     *
     * @param boardTileWidth
     *      Anzahl der Tiles in der Breite
     *
     * @param boardTileHeight
     *      Anzahl der Tiles in der Höhe
     *
     * @param orientation
     *      Richtung, in die das Board gedreht werden soll (Norden == Richtung, in die gedreht werden soll)
     */
    public void deserializeBoard(GridPane tileGrid, List<String> boardContent, Integer boardTileWidth, Integer boardTileHeight, Orientation orientation) {
        int row = boardTileHeight - 1;
        for(String boardRow : boardContent) {
            int column = 0;
            for(String tileInRow : boardRow.split(",")) {
                Integer newRow = getRowForOrientation(row, column, boardTileHeight -1 , boardTileWidth - 1, orientation);
                Integer newColumn = getColoumnForOrientation(column, row, boardTileWidth - 1, boardTileHeight - 1, orientation);
                dispatchTile(tileInRow, newColumn, newRow, tileGrid, orientation);
                column++;
            }
            row--;
        }
    }

    private Integer getRowForOrientation(Integer row, Integer column, Integer maxRow, Integer maxColumn, Orientation orientation) {
        if(!maxRow.equals(maxColumn) || Orientation.NORTH.equals(orientation)) {
            return row;
        }
        if(Orientation.SOUTH.equals(orientation)) {
            return maxRow - row;
        }
        if(Orientation.EAST.equals(orientation)) {
            return column;
        }
        if(Orientation.WEST.equals(orientation)) {
            return maxColumn - column;
        }
        return row;
    }

    private Integer getColoumnForOrientation(Integer column, Integer row, Integer maxColumn, Integer maxRow, Orientation orientation) {
        if(!maxColumn.equals(maxRow) || Orientation.NORTH.equals(orientation)) {
            return column;
        }
        if(Orientation.SOUTH.equals(orientation)) {
            return maxColumn - column;
        }
        if(Orientation.EAST.equals(orientation)) {
            return maxRow - row;
        }
        if(Orientation.WEST.equals(orientation)) {
            return row;
        }
        return column;
    }

    private void dispatchTile(String tileInRow, int column, int row, GridPane gridPane, Orientation orientation) {
        Tile tile = (Tile) gridPaneUtilService.getNodeFromGridPane(gridPane, column, row);
        Matcher tileMatcher = TILE_PATTERN.matcher(tileInRow);
        EditorTileFillToolIcon ground = new EditorTileFillToolIcon(null, null, GroundFill.EMPTY);
        List<EditorTileFillToolIcon> wallObjects = new ArrayList<>();
        boolean groundSet = false;
        while(tileMatcher.find()) {
            groundSet = dispatchSeperatedTileElement(column, row, gridPane, orientation, tile, tileMatcher, ground, wallObjects, groundSet);
        }
        if(!groundSet) {
            fillTileService.fillGroundWithTool(column, row, tile, ground, gridPane);
        }
    }

    private boolean dispatchSeperatedTileElement(int column, int row, GridPane gridPane, Orientation orientation, Tile tile, Matcher tileMatcher, EditorTileFillToolIcon ground, List<EditorTileFillToolIcon> wallObjects, boolean groundSet) {
        for (int group = 1; group <= tileMatcher.groupCount(); group++) {
            if (tileMatcher.group(group).trim().length() == 0) {
                break;
            }
            Matcher elementMatcher = IS_WALL_PATTEN.matcher(tileMatcher.group(group));
            if (elementMatcher.matches()) {
                groundSet = dispatchWallGroup(column, row, gridPane, orientation, tile, tileMatcher, ground, wallObjects, groundSet, group, elementMatcher);
            } else {
                dispatchTileGround(tileMatcher.group(group), ground);
                dispatchTileOrientation(tileMatcher.group(group), ground, orientation);
            }
        }
        return groundSet;
    }

    private boolean dispatchWallGroup(int column, int row, GridPane gridPane, Orientation orientation, Tile tile, Matcher tileMatcher, EditorTileFillToolIcon ground, List<EditorTileFillToolIcon> wallObjects, boolean groundSet, int group, Matcher elementMatcher) {
        if (!groundSet) {
            fillTileService.fillGroundWithTool(column, row, tile, ground, gridPane);
            groundSet = true;
        }
        Point clickPosition = getRelativeClickPoint(elementMatcher.group(1), orientation);
        dispatchWall(tileMatcher.group(group), wallObjects);
        for (EditorTileFillToolIcon wallTool : wallObjects) {
            fillTileService.addFieldObject(clickPosition.getX(), clickPosition.getY(), tile, 50, 20.0, wallTool);
        }
        wallObjects.clear();
        return groundSet;
    }

    private void dispatchWall(String element, List<EditorTileFillToolIcon> wallObjects) {
        Matcher wallObject = WALL_PATTERN.matcher(element);
        wallObject.find();
        List<String> usedElements = new ArrayList<>();
        for(int group = 1; group <= wallObject.groupCount(); group++) {
            String wallRegexGroupContent = wallObject.group(group);
            if(usedElements.contains(wallRegexGroupContent)) {
                continue;
            }
            if("w".equals(wallRegexGroupContent)) {
                wallObjects.add(new EditorTileFillToolIcon(null, null, FieldObject.WALL));
            }
            if("p135".equals(wallRegexGroupContent)) {
                wallObjects.add(new EditorTileFillToolIcon(null, null, FieldObject.PUSHER_ODD));
            }
            if("p24".equals(wallRegexGroupContent)) {
                wallObjects.add(new EditorTileFillToolIcon(null, null, FieldObject.PUSHER_EVEN));
            }
            if("l".equals(wallRegexGroupContent)) {
                wallObjects.add(new EditorTileFillToolIcon(null, null, FieldObject.LASER_SINGLE));
            }
            if("ll".equals(wallRegexGroupContent)) {
                wallObjects.add(new EditorTileFillToolIcon(null, null, FieldObject.LASER_DOUBLE));
            }
            if("lll".equals(wallRegexGroupContent)) {
                wallObjects.add(new EditorTileFillToolIcon(null, null, FieldObject.LASER_TRIPLE));
            }
            usedElements.add(wallRegexGroupContent);
        }
    }

    private Point getRelativeClickPoint(String orientationString, Orientation orientation) {
        Point point = null;
        if("no".equals(orientationString)) {
            point = new Point(25, 10);
            if(Orientation.SOUTH.equals(orientation)) {
                point = new Point(25, 40);
            }
            if(Orientation.EAST.equals(orientation)) {
                point = new Point(40, 25);
            }
            if(Orientation.WEST.equals(orientation)) {
                point = new Point(10, 25);
            }
        }
        if("so".equals(orientationString)) {
            point = new Point(25, 40);
            if(Orientation.SOUTH.equals(orientation)) {
                point = new Point(25, 10);
            }
            if(Orientation.EAST.equals(orientation)) {
                point = new Point(10, 25);
            }
            if(Orientation.WEST.equals(orientation)) {
                point = new Point(40, 25);
            }
        }
        if("we".equals(orientationString)) {
            point = new Point(10, 25);
            if(Orientation.SOUTH.equals(orientation)) {
                point = new Point(40, 25);
            }
            if(Orientation.EAST.equals(orientation)) {
                point = new Point(25, 10);
            }
            if(Orientation.WEST.equals(orientation)) {
                point = new Point(25, 40);
            }
        }
        if("ea".equals(orientationString)) {
            point = new Point(40, 25);
            if(Orientation.SOUTH.equals(orientation)) {
                point = new Point(10, 25);
            }
            if(Orientation.EAST.equals(orientation)) {
                point = new Point(25, 40);
            }
            if(Orientation.WEST.equals(orientation)) {
                point = new Point(25, 10);
            }
        }
        if(point == null) {
            return new Point(25,25);
        }
        return point;
    }

    private void dispatchTileOrientation(String element, EditorTileFillToolIcon editorTileFillToolIcon, Orientation orientation) {
        switch (element) {
            case "no":
                editorTileFillToolIcon.setOrientation(Orientation.NORTH);
                break;
            case "we":
                editorTileFillToolIcon.setOrientation(Orientation.WEST);
                break;
            case "so":
                editorTileFillToolIcon.setOrientation(Orientation.SOUTH);
                break;
            case "ea":
                editorTileFillToolIcon.setOrientation(Orientation.EAST);
                break;
            default:
                return;
        }
        switch (orientation) {
            case SOUTH:
                editorTileFillToolIcon.setOrientation(editorTileFillToolIcon.getOrientation().getOppositeDirection());
                break;
            case EAST:
                editorTileFillToolIcon.setOrientation(editorTileFillToolIcon.getOrientation().getNextDirection(true));
                break;
            case WEST:
                editorTileFillToolIcon.setOrientation(editorTileFillToolIcon.getOrientation().getNextDirection(false));
                break;
            default:
                break;
        }
    }

    private void dispatchTileGround(String element, EditorTileFillToolIcon editorTileFillToolIcon) {
        switch (element){
            case "ti":
                editorTileFillToolIcon.setGroundFill(GroundFill.EMPTY);
                break;
            case "ho":
                editorTileFillToolIcon.setGroundFill(GroundFill.HOLE);
                break;
            case "be":
                editorTileFillToolIcon.setGroundFill(GroundFill.CONVEYOR_BELT);
                break;
            case "ex":
                editorTileFillToolIcon.setGroundFill(GroundFill.EXPRESS_CONVEYOR_BELT);
                break;
            case "ge":
                editorTileFillToolIcon.setGroundFill(GroundFill.GEAR_CW);
                break;
            case "rl":
                editorTileFillToolIcon.setGroundFill(GroundFill.GEAR_CCW);
                break;
            case "ws":
                editorTileFillToolIcon.setGroundFill(GroundFill.REPAIR);
                break;
            default:
                break;
        }

    }

}