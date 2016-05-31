/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gui.services.game;

import de.uni_bremen.factroytrouble.gui.services.GameEngineWrapper;
import de.uni_bremen.factroytrouble.gui.controller.GameScreenController;
import de.uni_bremen.factroytrouble.gui.controller.components.HandCardImage;
import de.uni_bremen.factroytrouble.gui.generator.card.CardGenerator;
import de.uni_bremen.factroytrouble.player.ProgramCard;
import javafx.scene.image.ImageView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Verwaltet die Handkarten eines Spielers.
 * <p>
 * Created by johannes.gesenhues on 22.12.15.
 */
@Service
public class HandCardService {
    private Map<ProgramCard, ImageView> cardImageViews = new HashMap<>();
    @Autowired private GameEngineWrapper gameEngineWrapper;
    @Autowired private GameScreenController gameScreenController;
    @Autowired private CardGenerator cardGenerator;
    @Autowired private RegisterCardsService registerCardsService;

    /**
     * Macht die Karten sichtbar.
     *
     * @param playerNumber Der Index des Spielers
     */
    public void showCards(int playerNumber) {
        final int[] pos = {0};
        gameEngineWrapper.getCards(playerNumber).forEach(card -> {
            gameScreenController.getCards()[pos[0]] = card;
            drawHandCard(pos[0], card);
            pos[0]++;
        });
    }

    /**
     * Fügt die 10 Handkarten dem GameScreen hinzu. Diese werden außerdem auch in
     * dem Array cards gespeichert, wobei die Karten von links nach rechts
     * angefangen bei 0 nummeriert sind.
     *
     * @param programCard die Programmkarte
     */

    public boolean addCard(ProgramCard programCard) {
        for (int i = 0; i < gameScreenController.getCards().length; i++) {
            if (gameScreenController.getCards()[i] == null) {
                setHandCard(i, programCard);
                return true;
            }
        }
        return false;
    }

    private void setHandCard(int pos, ProgramCard programCard) {
        gameScreenController.getCards()[pos] = programCard;
        drawHandCard(pos, programCard);
    }

    /**
     * Fügt eine Handkarte auf dem GameScreen hinzu
     *
     * @param pos         Die Karten Position
     * @param programCard Die Karte
     */
    public void drawHandCard(int pos, ProgramCard programCard) {
        ImageView card = new HandCardImage((BufferedImage) cardGenerator.generateCard(programCard), pos);
        gameScreenController.getCardPane().getChildren().add(card);
        card.setOnMouseClicked(event -> {
            if (registerCardsService.addRegisterCard(programCard))
                removeCard(pos, programCard);
            }
        );
        cardImageViews.put(programCard, card);
        gameScreenController.getCards()[pos] = programCard;
    }

    private void removeCard(int pos, ProgramCard programCard) {
        gameScreenController.getCards()[pos] = null;
        gameScreenController.getCardPane().getChildren().remove(cardImageViews.get(programCard));
    }

    /**
     * Entfernt alle Handkarten vom GameScreen
     */
    public void dropAllCards() {
        cardImageViews.keySet().forEach(programCard ->
                gameScreenController.getCardPane().getChildren().remove(cardImageViews.get(programCard)));
    }


}
