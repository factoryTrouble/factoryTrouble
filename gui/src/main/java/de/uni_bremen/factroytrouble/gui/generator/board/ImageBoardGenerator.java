/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gui.generator.board;

import de.uni_bremen.factroytrouble.board.Board;
import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.gui.generator.board.tile.BoardTileImageDispatcher;
import de.uni_bremen.factroytrouble.gui.services.GameEngineWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Map;

/**
 * Generiert das Board als Bild
 *
 * @author Andre und Johannes
 */
@Service
public class ImageBoardGenerator implements BoardGenerator<Image> {

    @Autowired private GameEngineWrapper gameEngineWrapper;
    @Autowired private BoardTileImageDispatcher boardTileImageDispatcher;
    @Autowired private BoardConverterService boardConverterService;


    /**
     * Erstellt das Boards als Bild
     *
     * @return Die Bilddatei
     */
    @Override
    public Image generateBoard(String boardName) {
        Board board = gameEngineWrapper.getBoard(boardName);
        Map<Point, Tile> tiles = boardConverterService.convertBoardToMap(board.getFields());
        return drawAll(tiles);
    }

    /**
     *
     * Diese Methode Zeichnet alle Tiles in eine BufferedImage und gibt dieses Zurück.
     *
     * @param tiles Die Map mit alles Tiles
     * @return Das fertige BoardImage
     */
    private BufferedImage drawAll(Map<Point, Tile> tiles) {
        int maxX = boardConverterService.getMaxX();
        int maxY = boardConverterService.getMaxY();
        int width = (maxX + 1) * DEFAULT_TILE_WIDTH;
        int height = (maxY + 1) * DEFAULT_TILE_WIDTH;
        BufferedImage combined = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics combinedGraphic = combined.getGraphics();

        tiles.forEach((point, tile) -> {
            int xPos = point.x * DEFAULT_TILE_WIDTH;
            int yPos = (maxY - point.y) * DEFAULT_TILE_HEIGHT;
            combinedGraphic.drawImage(boardTileImageDispatcher.dispatch(tile), xPos, yPos, null);
        });
        return combined;
    }

}
