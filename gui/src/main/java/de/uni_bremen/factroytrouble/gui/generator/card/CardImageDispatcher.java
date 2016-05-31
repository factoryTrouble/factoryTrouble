/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gui.generator.card;

import de.uni_bremen.factroytrouble.player.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Component
public class CardImageDispatcher {

    private BufferedImage left;
    private BufferedImage right;
    private BufferedImage uTurn;
    private BufferedImage back;
    private BufferedImage move1;
    private BufferedImage move2;
    private BufferedImage move3;
    private BufferedImage empty;

    @PostConstruct
    public void init() {
        try {
            initializeCardImages();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Wählt anhand der Programmkarte das Kartentemplate.
     * 
     * @param programCard
     * @return
     */
    public BufferedImage dispatch(ProgramCard programCard) {
        if(programCard instanceof TurnLeftCard) {
            return left;
        }
        if(programCard instanceof TurnRightCard) {
            return right;
        }
        if(programCard instanceof UturnCard) {
            return uTurn;
        }
        if(programCard instanceof MoveBackwardCard) {
            return back;
        }
        if(programCard instanceof MoveForwardCard) {
            return dispatchMoveForwardCard((MoveForwardCard) programCard);
        }
        return empty;
    }

    private BufferedImage dispatchMoveForwardCard(MoveForwardCard moveForwardCard) {
        if(moveForwardCard.getRange() == 1) {
            return move1;
        }
        if(moveForwardCard.getRange() == 2) {
            return move2;
        }
        if(moveForwardCard.getRange() == 3) {
            return move3;
        }
        return empty;
    }

    private void initializeCardImages() throws IOException {
        left = ImageIO.read(getClass().getResourceAsStream("/game/cards/left.png"));
        right = ImageIO.read(getClass().getResourceAsStream("/game/cards/right.png"));
        uTurn = ImageIO.read(getClass().getResourceAsStream("/game/cards/u-turn.png"));
        back = ImageIO.read(getClass().getResourceAsStream("/game/cards/back.png"));
        move1 = ImageIO.read(getClass().getResourceAsStream("/game/cards/move_1.png"));
        move2 = ImageIO.read(getClass().getResourceAsStream("/game/cards/move_2.png"));
        move3 = ImageIO.read(getClass().getResourceAsStream("/game/cards/move_3.png"));
        empty = ImageIO.read(getClass().getResourceAsStream("/game/cards/empty.png"));
    }

}
