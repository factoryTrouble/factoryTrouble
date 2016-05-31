/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gui.generator.card;

import de.uni_bremen.factroytrouble.player.ProgramCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Generiert Bilder der Karten
 *
 * @author Andre Ohlrogge
 *
 */
@Component
public class ImageCardGenerator implements CardGenerator<Image> {

    private static final Integer NUMBER_START_X = 26;
    private static final Integer NUMBER_START_Y = 11;
    private static final Integer NUMBER_WIDTH = 19;
    public static final int CARD_HEIGHT = 153;
    public static final int CARD_WIDTH = 99;

    private BufferedImage[] cardValues;

    @Autowired private CardImageDispatcher cardImageDispatcher;

    @PostConstruct
    public void init() {
        try {
            initializeCardValues();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Image> generateAllAllCards(List<ProgramCard> programCards) {
        List<Image> cardImages = new ArrayList<>();
        programCards.forEach(programCard -> {cardImages.add(generateCard(programCard));});
        return cardImages;
    }

    @Override
    public Image generateCard(ProgramCard programCard) {
        BufferedImage combinedBufferdImage = new BufferedImage(CARD_WIDTH, CARD_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics combinedGraphic = combinedBufferdImage.getGraphics();
        combinedGraphic.drawImage(cardImageDispatcher.dispatch(programCard), 0, 0, null);

        Integer firstPriorityValue = (programCard.getPriority() - programCard.getPriority() % 100) / 100;
        Integer secondPriorityValue = ((programCard.getPriority() - firstPriorityValue * 100) - ((programCard.getPriority() - firstPriorityValue * 100) % 10)) / 10;
        Integer thirdPriorityValue = programCard.getPriority() - firstPriorityValue * 100 - secondPriorityValue * 10;

        if(firstPriorityValue > 0) {
            combinedGraphic.drawImage(cardValues[firstPriorityValue], NUMBER_START_X, NUMBER_START_Y, null);
        }
        if((firstPriorityValue > 0) || (firstPriorityValue == 0 && secondPriorityValue > 0)) {
            combinedGraphic.drawImage(cardValues[secondPriorityValue], NUMBER_START_X + NUMBER_WIDTH, NUMBER_START_Y, null);
        }
        combinedGraphic.drawImage(cardValues[thirdPriorityValue], NUMBER_START_X + (NUMBER_WIDTH * 2), NUMBER_START_Y, null);
        return combinedBufferdImage;
    }

    private void initializeCardValues() throws IOException {
        cardValues = new BufferedImage[10];
        cardValues[0] = ImageIO.read(getClass().getResourceAsStream("/game/cards/numbers/0.png"));
        cardValues[1] = ImageIO.read(getClass().getResourceAsStream("/game/cards/numbers/1.png"));
        cardValues[2] = ImageIO.read(getClass().getResourceAsStream("/game/cards/numbers/2.png"));
        cardValues[3] = ImageIO.read(getClass().getResourceAsStream("/game/cards/numbers/3.png"));
        cardValues[4] = ImageIO.read(getClass().getResourceAsStream("/game/cards/numbers/4.png"));
        cardValues[5] = ImageIO.read(getClass().getResourceAsStream("/game/cards/numbers/5.png"));
        cardValues[6] = ImageIO.read(getClass().getResourceAsStream("/game/cards/numbers/6.png"));
        cardValues[7] = ImageIO.read(getClass().getResourceAsStream("/game/cards/numbers/7.png"));
        cardValues[8] = ImageIO.read(getClass().getResourceAsStream("/game/cards/numbers/8.png"));
        cardValues[9] = ImageIO.read(getClass().getResourceAsStream("/game/cards/numbers/9.png"));
    }
}
