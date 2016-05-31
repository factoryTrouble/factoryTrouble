/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gui.services;

import de.uni_bremen.factroytrouble.board.Board;
import de.uni_bremen.factroytrouble.gui.controller.GameScreenController;
import de.uni_bremen.factroytrouble.gui.services.game.AIExecutionService;
import de.uni_bremen.factroytrouble.gui.services.game.EndBtnService;
import de.uni_bremen.factroytrouble.gui.services.game.PerformRoundForPlayerService;
import de.uni_bremen.factroytrouble.network.EngineWrapper;
import de.uni_bremen.factroytrouble.observer.GameObserver;
import de.uni_bremen.factroytrouble.player.*;
import de.uni_bremen.factroytrouble.spring.PostConstructTask;
import de.uni_bremen.factroytrouble.spring.PostConstructTaskScheduler;
import de.uni_bremen.factroytrouble.spring.SpringConfigHolder;
import javafx.application.Platform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class GameEngineWrapper implements PostConstructTask, EngineWrapper {
    private static final int GAME_ID = 0;


    @Autowired private MasterFactory masterFactory;
    @Autowired private PlayerFactory playerFactory;
    @Autowired private PostConstructTaskScheduler postConstructTaskScheduler;

    private AIExecutionService aiExecutionService;
    private GameScreenController gameScreenController;
    private EndBtnService endBtnService;
    private PerformRoundForPlayerService performRoundForPlayerService;

    @PostConstruct
    public void init() {
        postConstructTaskScheduler.add(this);
    }

    @Override
    public void postConstructTask() {
        aiExecutionService = SpringConfigHolder.getInstance().getContext().getBean(AIExecutionService.class);
        gameScreenController = SpringConfigHolder.getInstance().getContext().getBean(GameScreenController.class);
        endBtnService = SpringConfigHolder.getInstance().getContext().getBean(EndBtnService.class);
        performRoundForPlayerService = SpringConfigHolder.getInstance().getContext().getBean(PerformRoundForPlayerService.class);
    }

    @Override
    public void initMaster() {
        masterFactory.getMaster(GAME_ID).init();
    }

    /**
     * Gibt den Spieler an dem übergebenen Index zurück
     *
     * @param playerNumber der Spieler Index
     * @return der Spieler
     */
    @Override
    public Player getPlayerByNumber(int playerNumber) {
        return masterFactory.getMaster(GAME_ID).getPlayers().get(playerNumber);
    }

    /**
     * Erzeugt einen Spieler mit dem übergebenen Namen und mit dem übergebenen Typ
     *
     * @param robotName der Name des Roboters
     * @param kiName    der Typ des Spielers(Human,AI_Agent1,...)
     */
    @Override
    public void createPlayer(String robotName, String kiName) {
        Player player = playerFactory.createNewPlayer(GAME_ID, robotName, kiName);
        masterFactory.getMaster(GAME_ID).addPlayer(player);
    }

    /**
     * Gibt die Anzahl der Lebenspunkte des Spielers mit dem gegebenen Index zurück
     *
     * @param playerNumber der Spieler Index
     * @return Anzahl der Lebenspunkte
     */
    @Override
    public int getPlayerHP(int playerNumber) {
        return masterFactory.getMaster(GAME_ID).getPlayers().get(playerNumber).getRobot().getHP();
    }

    /**
     * Gibt die Karte aus den Handkarten an der übergebenen Position, des übergebenen Spieler Indexes zurück
     *
     * @param playerNumber der Spieler Index
     * @param cardPos      der Karten Index
     * @return die Karte
     */
    @Override
    public ProgramCard getCard(int playerNumber, int cardPos) {
        return masterFactory.getMaster(GAME_ID).getPlayers().get(playerNumber).getPlayerCards().get(cardPos);
    }

    /**
     * Gibt die Karte aus dem Register an der übergebenen Position, des übergebenen Spieler Indexes zurück
     *
     * @param playerNumber der Spieler Index
     * @param cardPos      der Karten Index
     * @return die Karte
     */
    @Override
    public ProgramCard getCardInRegister(int playerNumber, int cardPos) {
        return masterFactory.getMaster(GAME_ID).getPlayers().get(playerNumber).getRobot().getRegisters()[cardPos];
    }

    /**
     * Leer den Register des Spielers mit dem übergebenen Index an der übergebenen Position
     *
     * @param playerNumber der Spieler Index
     * @param cardPos      der Register Index
     */
    @Override
    public void emptyRegister(int playerNumber, int cardPos) {
        //masterFactory.getMaster(GAME_ID).getPlayers().get(playerNumber).getRobot().emptyRegister(cardPos);
        masterFactory.getMaster(GAME_ID).getPlayers().get(playerNumber).emptyRegister(cardPos);
    }

    /**
     * Setzt die übergebene Karte an der übergebenen Position für den Spieler an dem übergebenen Index
     *
     * @param playerNumber der Spieler Index
     * @param cardPos      der Register Index
     * @param programCard  die einzusetzende Karte
     */
    @Override
    public void fillRegister(int playerNumber, int cardPos, ProgramCard programCard) {
        masterFactory.getMaster(GAME_ID).getPlayers().get(playerNumber).fillRegister(cardPos, programCard);
    }

    /**
     * Gibt das Array der gelockeden Register zurück
     *
     * @param playerNumber der Index des Spielers
     * @return die gelockeden Register
     */
    @Override
    public boolean[] getLockedRegisters(int playerNumber) {
        return masterFactory.getMaster(GAME_ID).getPlayers().get(playerNumber).getRobot().registerLockStatus();
    }

    /**
     * Gibt die Anzahl der Spieler zurück
     *
     * @return Anzahl der Spieler
     */
    @Override
    public int getPlayerCount() {
        return masterFactory.getMaster(GAME_ID).getPlayers().size();
    }

    /**
     * Startet die Runden Evaluation
     */
    @Override
    public void startRound() {
        gameScreenController.hideButtons(true);
        masterFactory.getMaster(GAME_ID).activateBoard();
        masterFactory.getMaster(GAME_ID).cleanup();
        if (masterFactory.getMaster(GAME_ID).getWinner() != null) {
            endGame();
        }
        else {
            prepareRound();
        }
    }

    /**
     * Wechselt auf den ResultScreen im GUI Thread
     */
    @Override
    public void endGame() {
        Platform.runLater(() ->
                gameScreenController.endGame());
    }

    /**
     * Startet eine neue Runde
     */
    @Override
    public void prepareRound() {
        setAllPlayersNotFinished();
        dealCards();
        aiExecutionService.executeAIs();
        Platform.runLater(() -> gameScreenController.setNextButton(endBtnService.hasNextHumanPlayer(0)));
        if (endBtnService.hasNextHumanPlayer(-1)) {
            int firstPlayer = endBtnService.getNextHumanPlayer(-1);
            gameScreenController.setActivePlayer(firstPlayer);
            performRoundForPlayerService.start(firstPlayer);
        }
        gameScreenController.hideButtons(false);
    }


    /**
     * Gibt die Spieler zurück
     *
     * @return die Spieler
     */
    @Override
    public List<Player> getPlayers() {
        return masterFactory.getMaster(GAME_ID).getPlayers();
    }

    /**
     * Gibt die Karten für einen Spieler zurück
     *
     * @param playerNumber die Nummer des Spielers
     * @return die Karten des Spielers
     */
    @Override
    public List<ProgramCard> getCards(int playerNumber) {
        return masterFactory.getMaster(GAME_ID).getPlayers().get(playerNumber).getPlayerCards();
    }

    /**
     * Fügt den Observer dem Master hinzu
     *
     * @param guiEngineObserver den Observer der Hinzugefügt werden soll
     */
    @Override
    public void attachObserver(GameObserver guiEngineObserver) {
        masterFactory.getMaster(GAME_ID).attachObserver(guiEngineObserver);
    }

    /**
     * Gibt das aktive Board zurück
     *
     * @return das aktive Board
     */
    @Override
    public Board getActiveBoard() {
        return masterFactory.getMaster(GAME_ID).getBoard();
    }

    /**
     * Erzeugt das Board mit dem übergebenen Namen im Master und liefert das erzeugte Board zurück
     *
     * @param boardName der Name des Boards
     * @return das Board mit dem gegebenen Namen
     */
    @Override
    public Board getBoard(String boardName) {
        masterFactory.getMaster(GAME_ID).initialiseBoard(boardName);
        return masterFactory.getMaster(GAME_ID).getBoard();
    }

    /**
     * Erzeugt das Board auf dem Master
     *
     * @param boardName der Boardname
     */
    @Override
    public void initialiseBoard(String boardName) {
        masterFactory.getMaster(GAME_ID).initialiseBoard(boardName);

    }

    /**
     * Verteilt die Karten auf die Spieler
     */
    @Override
    public void dealCards() {
        masterFactory.getMaster(GAME_ID).dealCardsToPlayers();
    }

    /**
     * Gibt eine Liste mit den Namen aller Boards zurück
     *
     * @return Liste der Boardnamen
     */
    @Override
    public List<String> getAvailableBoards() {
        return masterFactory.getMaster(GAME_ID).getAvailableBoards();
    }

    public GameScreenController getGameScreenController() {
        return gameScreenController;
    }

    /**
     * Gibt den
     *
     * @param robotNumber
     * @return
     */
    @Override
    public String getRobotNameByNumber(int robotNumber) {
        return Master.ROBOT_NAMES.get(robotNumber);
    }

    /**
     * Gibt die Roboter
     *
     * @param robotName
     * @return
     */
    @Override
    public int getPlayerNumberByName(String robotName) {
        return Master.ROBOT_NAMES.indexOf(robotName);
    }

    /**
     * Gibt den Index des Roboters mit diesem Namen im Playerarray im Master zurück
     *
     * @param robotName
     * @return
     */
    @Override
    public int getPlayerIndexInPlayersByName(String robotName) {
        for (int index = 0; index < getPlayerCount(); index++) {
            if (getPlayerByNumber(index).getRobot().getName().equals(robotName))
                return index;
        }
        return -1;
    }

    /**
     * Entfernt alle Spieler aus dem Spiel
     */
    @Override
    public void removeAllPlayers() {
        masterFactory.getMaster(GAME_ID).removeAllPlayers();
    }

    /**
     * Gibt den Gewinner Spieler zurück oder null, wenn es keinen gibt
     *
     * @return der Gewinner oder Null
     */
    @Override
    public Player getWinner() {
        return masterFactory.getMaster(GAME_ID).getWinner();
    }

    /**
     * Setzt alle GamePlayerController auf nicht finished
     */
    private void setAllPlayersNotFinished() {
        gameScreenController.getPlayerInfos().forEach(playerInfoController ->
                playerInfoController.setFinished(false));
    }

    @Override
    public void changePowerDownForPlayer(int playerNumber) {
        masterFactory.getMaster(GAME_ID).requestPowerDownStatusChange(getPlayerByNumber(playerNumber).getRobot());
    }
    
    @Override
    public void deleteMaster(int gameId){
        masterFactory.deleteMaster(gameId);
    }
    
    @Override
    public int getFlagCount(String board){
        return masterFactory.getMaster(GAME_ID).getFlagCount(board);
    }

    /**
     * Gibt den GameMaster zurück
     * @return
     */
    public Master getMaster() {
        return masterFactory.getMaster(GAME_ID);
    }
}
