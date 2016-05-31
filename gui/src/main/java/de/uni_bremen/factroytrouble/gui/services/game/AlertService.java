/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gui.services.game;

import de.uni_bremen.factroytrouble.gui.services.GameEngineWrapper;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Modality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AlertService {
    public static final String AI_NOT_READY = "KI ist noch nicht fertig";
    public static final String NO_BOARD_CHOSEN = "Kein Spielfeld ausgewählt";
    public static final String NOT_ALL_CARDS_CHOSEN = "Nicht alle Karten gewählt";
    public static final String ALL_PLAYERS_FINISHED = "Alle Spieler fertig";
    public static final String ALL_PLAYERS_HAVE_MOVED = "Alle Spieler haben gezogen";
    public static final String EVALUATION_PHASE_STARTS = "Auswertungsphase beginnt";
    public static final String NEXT_PLAYER_TITLE = "Nächster Spieler";
    public static final String HAS_TURN = " ist am zug";
    public static final String IS_POWERED_DOWN = " setzt diese Runde aus";
    public static final String TOO_FEW_PLAYERS = "Zu wenig Spieler gewählt";

    @Autowired private GameEngineWrapper gameEngineWrapper;

    public void showAlert(String title, String headerText, String contentText) {
        Alert popUp = new Alert(Alert.AlertType.INFORMATION);
        popUp.setTitle(title);
        popUp.setHeaderText(headerText);
        popUp.setContentText(contentText);
        popUp.initModality(Modality.APPLICATION_MODAL);
        popUp.showAndWait();
    }

    public void showEndRoundAlert() {
        showAlert(ALL_PLAYERS_FINISHED, ALL_PLAYERS_HAVE_MOVED, EVALUATION_PHASE_STARTS);
    }

    public void showNotAllCardsSet() {
        showAlert(NOT_ALL_CARDS_CHOSEN, NOT_ALL_CARDS_CHOSEN, NOT_ALL_CARDS_CHOSEN);
    }

    public void showNoBoardChosenAlert() {
        showAlert(NO_BOARD_CHOSEN, NO_BOARD_CHOSEN, NO_BOARD_CHOSEN);
    }

    public void showTooFewPlayersAlert() {
        showAlert(TOO_FEW_PLAYERS, TOO_FEW_PLAYERS, TOO_FEW_PLAYERS);
    }

    public void showAiNotReadyAlert(){
        showAlert(AI_NOT_READY,AI_NOT_READY,AI_NOT_READY);

    }
    public void showNextPlayerAlert(int activePlayer) {
        String playerName = gameEngineWrapper.getPlayerByNumber(activePlayer).getRobot().getName();
        showAlert(NEXT_PLAYER_TITLE, playerName, playerName + HAS_TURN);
    }
    
    public void showPowerDownAlert(int activePlayer) {
        String playerName = gameEngineWrapper.getPlayerByNumber(activePlayer).getRobot().getName();
        showAlert(NEXT_PLAYER_TITLE, playerName, playerName + IS_POWERED_DOWN);
    }

    /**
     * Zeigt ein Bestätigungsdialog
     *
     * @param title
     *      Title der Box
     *
     * @param content
     *      Frage
     *
     * @param confirmButton
     *      Beschriftung des Bestätigen Buttons
     *
     * @param cancelButton
     *      Beschriftung des Abbrechen Buttons
     *
     * @return
     *      True wenn Okay gedrückt, sonst false.
     */
    public boolean showConfirm(String title, String content, String confirmButton, String cancelButton) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(content);

        ButtonType comfirmButtonBtn = new ButtonType(confirmButton);
        ButtonType cancelButtonBtn = new ButtonType(cancelButton, ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(comfirmButtonBtn, cancelButtonBtn);

        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == comfirmButtonBtn;
    }

    /**
     * Zeigt einen Alert, in dem man einen Wert eingeben muss
     *
     * @param title
     *      Title der Box
     *
     * @param content
     *      Frage
     *
     * @param defaultInputContent
     *      Standard Wert in der Input Bxo
     *
     * @return
     *      Den wert aus der Box oder null
     */
    public String showTextAlert(String title, String content, String defaultInputContent) {
        TextInputDialog dialog = new TextInputDialog(defaultInputContent);
        dialog.setTitle(title);
        dialog.setContentText(content);
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            return result.get();
        }
        return null;
    }
}
