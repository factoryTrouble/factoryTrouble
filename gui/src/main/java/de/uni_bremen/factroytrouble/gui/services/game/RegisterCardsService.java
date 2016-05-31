/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gui.services.game;

import de.uni_bremen.factroytrouble.gui.controller.GameScreenController;
import de.uni_bremen.factroytrouble.gui.controller.components.RegisterCardImageView;
import de.uni_bremen.factroytrouble.gui.generator.card.CardGenerator;
import de.uni_bremen.factroytrouble.gui.generator.card.EmptyCard;
import de.uni_bremen.factroytrouble.gui.services.GameEngineWrapper;
import de.uni_bremen.factroytrouble.player.ProgramCard;
import javafx.scene.image.ImageView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Verwaltet die Register eines Spielers.
 * <p>
 * Created by johannes.gesenhues on 22.12.15.
 */
@Service
public class RegisterCardsService {
    private int playerNumber;
    private boolean[] lockedRegisters;
    private Map<ProgramCard, ImageView> registerImageViews = new HashMap<>();

    @Autowired private GameEngineWrapper gameEngineWrapper;
    @Autowired private GameScreenController gameScreenController;
    @Autowired private CardGenerator cardGenerator;
    @Autowired private HandCardService handCardService;

    /**
     * Fügt die 5 Register dem GameScreen hinzu. Diese werden außerdem auch in
     * dem Array registers gespeichert, wobei die Karten von links nach rechts
     * angefangen bei 0 nummeriert sind.
     */
    public void initRegisters(int playerNumber) {
        this.playerNumber = playerNumber;
        this.lockedRegisters = gameEngineWrapper.getLockedRegisters(playerNumber);
        ProgramCard programCard = new EmptyCard();
        for (int i = 0; i < lockedRegisters.length; i++) {
            if (lockedRegisters[i]) {
                gameScreenController.getRegisters()[i] = gameEngineWrapper.getCardInRegister(playerNumber, i);
                drawRegisterCard(i, gameEngineWrapper.getCardInRegister(playerNumber, i));
            } else {
                gameScreenController.getRegisters()[i] = programCard;
                drawRegisterCard(i, programCard);
            }
        }
    }

    private void setRegisterCard(int pos, ProgramCard programCard) {
        gameScreenController.getRegisters()[pos] = programCard;
        drawRegisterCard(pos, programCard);
        gameEngineWrapper.emptyRegister(playerNumber, pos);
        gameEngineWrapper.fillRegister(playerNumber, pos, programCard);
    }

    /**
     * Fügt eine Karte einem Register hinzu, sofern dieses Register leer ist.
     *
     * @param programCard
     * @return
     */
    public boolean addRegisterCard(ProgramCard programCard) {
        for (int i = 0; i < gameScreenController.getRegisters().length; i++) {
            if (gameScreenController.getRegisters()[i] instanceof EmptyCard) {
                setRegisterCard(i, programCard);
                return true;
            }
        }
        return false;
    }

    private void drawRegisterCard(int pos, ProgramCard programCard) {
        RegisterCardImageView register = new RegisterCardImageView((BufferedImage) cardGenerator.generateCard(programCard), pos);
        gameScreenController.getRegisterPane().getChildren().add(register);
        register.setOnMouseClicked(event -> {
            if (programCard instanceof EmptyCard)
                return;
            if (removeCard(pos, programCard))
                handCardService.addCard(programCard);

        });
        registerImageViews.put(programCard, register);
        gameScreenController.getRegisters()[pos] = programCard;

    }

    /**
     * Entfernt die Karte programCard aus dem Register pos, wenn der Register nicht gelocked ist.
     * wenn der Register nicht gelocked ist entfernt die Methode zusätzlich die ProgrammCard ausdem
     * vom GameScreen
     * @param pos die Position also Registernummer
     * @param programCard
     * @return gibt zurück ob der Register gelocked ist
     */
    private boolean removeCard(int pos, ProgramCard programCard) {
        if (lockedRegisters[pos]) {
            return false;
        }
        gameScreenController.getRegisters()[pos] = new EmptyCard();
        gameScreenController.getRegisterPane().getChildren().remove(registerImageViews.get(programCard));
        return true;

    }

    /**
     * Entfernt alle ProgrammCards aus den Registern
     * @param playerNumber der aktive Player
     */
    public void dropAllCards(int playerNumber) {
        registerImageViews.keySet().forEach(programCard ->
                gameScreenController.getRegisterPane().getChildren().remove(registerImageViews.get(programCard))
        );

        initRegisters(playerNumber);
    }


    /**
     * Gibt zurück ob alle Register besetzt sind
     *
     * @return alle Register besetzt
     */
    public boolean allRegisterCardsAreSet() {
        for (int i = 0; i < gameScreenController.getRegisters().length; i++) {
            if (gameScreenController.getRegisters()[i] instanceof EmptyCard)
                return false;
        }
        return true;
    }
}
