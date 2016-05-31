/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gui.generator.board.tile;

import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.gameobjects.Flag;
import de.uni_bremen.factroytrouble.gui.generator.board.BoardGenerator;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Gerneriert eine Kachel mit einer Flagge.
 *
 * @author Andre
 */
@Component
public class FlagTileGenerator {

    private static final Integer MAX_FLAG_NUMBER = 100;
    private static final Integer NUMBER_WIDTH = 24;
    private static final Integer NUMBER_OFFSET_LEFT = 42;
    private static final Integer NUMBER_OFFSET_TOP = 30;

    private BufferedImage flagTileImage;
    private BufferedImage[] flagNumbers = new BufferedImage[10];

    @PostConstruct
    public void init() {
        try {
            flagTileImage = ImageIO.read(getClass().getResourceAsStream("/game/tiles/flag.png"));
            for (int i = 0; i < 10; i++) {
                flagNumbers[i] = ImageIO.read(getClass().getResourceAsStream("/game/tiles/flag_numbers/flag_" + i + ".png"));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Generiert eine Tile mit einer Flagge. Die Flage wird mit der entsprechenden Nummer gekennzeichnet.
     * @param tile
     *      Das Tile, wo die Flagge drauf ist
     * @return
     *      Bild der Kachel mit Flagge
     */
    public BufferedImage generateFlagTileImage(Tile tile) {
        Flag flag = (Flag) tile.getFieldObject();
        Integer flagNumber = flag.getNumber();
        if(flagNumber >= MAX_FLAG_NUMBER) {
            throw new FlagNumberToHighException();
        }
        BufferedImage combinedTile = new BufferedImage(BoardGenerator.DEFAULT_TILE_WIDTH, BoardGenerator.DEFAULT_TILE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics combinedTileGraphics = combinedTile.getGraphics();
        combinedTileGraphics.drawImage(flagTileImage, 0, 0, null);
        writeFlagNumber(flagNumber, combinedTileGraphics);
        return combinedTile;
    }

    private void writeFlagNumber(Integer flagNumber, Graphics combinedTileGraphics) {
        if(flagNumber < 10) {
            combinedTileGraphics.drawImage(flagNumbers[flagNumber], NUMBER_OFFSET_LEFT, NUMBER_OFFSET_TOP, null);
            return;
        }
        Integer firstFlagNumber = (flagNumber - (flagNumber % 10)) / 10;
        Integer secondFlagNumber = flagNumber - firstFlagNumber * 10;
        combinedTileGraphics.drawImage(flagNumbers[firstFlagNumber], NUMBER_OFFSET_LEFT, NUMBER_OFFSET_TOP, null);
        combinedTileGraphics.drawImage(flagNumbers[secondFlagNumber], NUMBER_OFFSET_LEFT + NUMBER_WIDTH, NUMBER_OFFSET_TOP, null);
    }

    public static class FlagNumberToHighException extends RuntimeException {
        public FlagNumberToHighException() {
            super("Counld not generate a flag with a number greater or equals 100");
        }
    }
}

