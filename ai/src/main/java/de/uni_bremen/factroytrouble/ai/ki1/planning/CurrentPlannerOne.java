/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki1.planning;

import java.awt.Point;
import java.io.IOException;
import java.util.*;

import de.uni_bremen.factroytrouble.exceptions.ConfigPropertyNotFoundRuntimeException;
import de.uni_bremen.factroytrouble.exceptions.KeyNotFoundRuntimeException;
import de.uni_bremen.factroytrouble.exceptions.PropertyFileNotFoundException;
import org.apache.log4j.Logger;

import de.uni_bremen.factroytrouble.api.ki1.Control;
import de.uni_bremen.factroytrouble.api.ki1.Logic;
import de.uni_bremen.factroytrouble.api.ki1.Placement;
import de.uni_bremen.factroytrouble.api.ki1.Timer;
import de.uni_bremen.factroytrouble.api.ki1.Control.GoalType;
import de.uni_bremen.factroytrouble.api.ki1.Control.RequestType;
import de.uni_bremen.factroytrouble.api.ki1.Logic.Move;
import de.uni_bremen.factroytrouble.api.ki1.planning.CurrentPlanning;
import de.uni_bremen.factroytrouble.api.ki1.planning.FuturePlanning;
import de.uni_bremen.factroytrouble.api.ki1.planning.Goals;
import de.uni_bremen.factroytrouble.api.ki1.planning.Plans;
import de.uni_bremen.factroytrouble.ai.ais.AIPlayer1;
import de.uni_bremen.factroytrouble.ai.ki1.PlacementUnit;
import de.uni_bremen.factroytrouble.ai.ki1.configreader.AgentConfigReader;
import de.uni_bremen.factroytrouble.ai.ki1.configreader.StaticBehaviourConfigReader;
import de.uni_bremen.factroytrouble.ai.ki1.planning.TacticUnit.Tactics;
import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.exceptions.KeyNotFoundException;
import de.uni_bremen.factroytrouble.gameobjects.ConveyorBelt;
import de.uni_bremen.factroytrouble.gameobjects.Hole;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.player.Master;
import de.uni_bremen.factroytrouble.player.MasterFactory;
import de.uni_bremen.factroytrouble.player.MoveBackwardCard;
import de.uni_bremen.factroytrouble.player.MoveForwardCard;
import de.uni_bremen.factroytrouble.player.ProgramCard;
import de.uni_bremen.factroytrouble.player.TurnLeftCard;
import de.uni_bremen.factroytrouble.player.TurnRightCard;
import de.uni_bremen.factroytrouble.player.UturnCard;

/**
 * Implementiert {@link CurrentPlanning}. Plant den Zug für die aktuelle Runde
 * mithilfe eines {@link Plan}s.
 * 
 * @author Roland
 *
 */
public class CurrentPlannerOne implements CurrentPlanning {
    private static final Logger LOGGER = Logger.getLogger("planner");
    private Control controller;
    private Timer timerUnit;
    private FuturePlanning futurePlanner;
    private Logic logic;
    // gibt an, ob in die Richtung eines Ziels gegangen wurde(weil keins
    // erreicht werden konnte)
    boolean goDirGoal = false;
    private GoalTile highestGoalTile;
    private Goals currentGoal;
    // cards in deck used and unused
    private List<ProgramCard> cards = new ArrayList<ProgramCard>();
    private Plans currentPlan;
    private Tile currentTile;
    private int distToTarget;
    private int reachableDist;
    // tile which bestpathDir reached with cards, only set if 1. path not
    // completely succeeded
    private Tile bestPathDirEnd;
    private int bestPathDirOri;
    // path from start to goal if 1. path not suceeded completely
    private List<ProgramCard> bestPathDir;
    // used cards of deck
    private List<Integer> globalUsedCards = new LinkedList<Integer>();
    private List<Integer> usedDirCards = new LinkedList<Integer>();
    private int numberOfCardsToPlace;
    // simulationdata which will be set by tryToFollowPath, is data from last
    // execution
    private int simulationOrientation;
    private Tile simulationTile;
    private int simulationPosInPath;
    private PathPlanner pathPlanner;
    private AgentConfigReader config;
    private StaticBehaviourConfigReader bConfig;
    private int gearCost;
    private boolean foundConveyor;
    private String robotName;
    private int debugTimerSBPD;
    private int tacticPower;
    private Tactics currentTactic;

    public CurrentPlannerOne(Control controller, Timer timerUnit, FuturePlanning futurePlanner, Logic logic,
            PathPlanner pathPlanner) {
        setController(controller);
        setTimerUnit(timerUnit);
        setFuturePlanner(futurePlanner);
        setLogic(logic);
        this.pathPlanner = pathPlanner;
        robotName = controller.getRobotName();
        foundConveyor = false;
        try {
            config = AgentConfigReader.getInstance(controller.getAgentNumber());
            bConfig = StaticBehaviourConfigReader.getInstance(controller.getRobotName());
        } catch (IOException e) {
            config = null;
            bConfig = null;
            throw new RuntimeException(e);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @throws KeyNotFoundException
     * 
     * @see {@link de.uni_bremen.factroytrouble.api.ki1.planning.planning.CurrentPlanning#planTurn
     *      CurrentPlanning.planTurn}
     */
    @Override
    public List<ProgramCard> planTurn() {
        if (config == null || bConfig == null) {
            throw new PropertyFileNotFoundException();
        }
        try {
            setReachableDist(config.getIntProperty("Planning.ReachableDist"));
            gearCost = bConfig.getIntProperty("Planning.GearCost");
        } catch (KeyNotFoundException e) {
            LOGGER.error(e);
            throw new ConfigPropertyNotFoundRuntimeException(e);
        }
        setGlobalVars();
        LOGGER.debug(robotName + " planTurn mit Karten: " + cards);
        int caseNumber = isSpecialCase();
        // behandle Spezialfall
        if (caseNumber > 0) {
            List<ProgramCard> cardsPlacedSC = handleSpecialCase(caseNumber);
            controller.setCards(cardsPlacedSC);
            return cardsPlacedSC;
        } else {
            // behandle Normalfall
            List<ProgramCard> cardsPlaced = new LinkedList<ProgramCard>();
            cardsPlaced = handleNormalCase();
            // wenn Ziel nicht erreicht, aber noch Karten über, wähle möglichst
            // gute Restkarten
            if (cardsPlaced.size() != numberOfCardsToPlace && !simulationTile.getAbsoluteCoordinates()
                    .equals(((GoalTile) currentGoal).getTile().getAbsoluteCoordinates())) {
                LOGGER.debug("planTurn fülle mit dlbm auf: " + (numberOfCardsToPlace() - cardsPlaced.size()));
                cardsPlaced.addAll(doLeastBadMoves2(numberOfCardsToPlace - cardsPlaced.size()));
            }
            // wenn Ziel erreicht, aber Karten über, gebe Karten an board, hole
            // nächstes Ziel und ....
            // handle next goal with rest of cardstoplace reset all variables
            // except globalusedcards, numberofcardstoplace/get??
            else if (cardsPlaced.size() != numberOfCardsToPlace && simulationTile.getAbsoluteCoordinates()
                    .equals(((GoalTile) currentGoal).getTile().getAbsoluteCoordinates())) {
                handleReachedFlagCardsLeft();
                cardsPlaced.addAll(doLeastBadMoves2(numberOfCardsToPlace - cardsPlaced.size()));
            }
            // wenn Ziel erreicht, gebe bescheid
            if (simulationTile.getAbsoluteCoordinates()
                    .equals(((GoalTile) currentGoal).getTile().getAbsoluteCoordinates())) {
                controller.setPotentialReachedGoal(GoalType.FLAG);
            }
            evaluatePowerDown();
            LOGGER.debug(robotName + " Gelegte Karten: " + cardsPlaced);
            if (cardsPlaced.size() != numberOfCardsToPlace()) {
                throw new IllegalStateException(
                        "not enough cards planned to place! To place: " + numberOfCardsToPlace());
            }
            controller.setCards(cardsPlaced);
            return cardsPlaced;
        }

    }

    /**
     * Sind die HP kleiner-gleich 4*risk + 3 wird ein Powerdown angekündigt.
     */
    public void evaluatePowerDown() {
        double risk = 0.5;
        double threshold;
        int currentHP = controller.getHP(((AIPlayer1) controller).getAgentNumber() - 1);
        try {
            risk = bConfig.getDoubleProperty("Planning.Risk");
        } catch (KeyNotFoundException e) {
            throw new RuntimeException(e);
        }
        threshold = 4 * risk + 3;
        if (currentHP <= Math.round(threshold)) {
            MasterFactory mf = ((AIPlayer1) controller).getMasterFactory();
            int gid = controller.getGameId();
            Master m = mf.getMaster(gid);
            m.requestPowerDownStatusChange(((AIPlayer1) controller).getRobot());
        }
    }

    /**
     * Legt möglichst gute Karten um einem Ziel näher zu kommen, ohne sich an
     * einen Weg zu halten. Dazu wird iterativ die Karte, der noch ungenutzten
     * gelegt, die dem Ziel am nächsten kommt.
     * 
     * @param cardsToPlace
     *            Anzahl an noch zu legenden Karten
     * @return schließlich gelegte Karten
     */
    public List<ProgramCard> doLeastBadMoves(int cardsToPlace) {
        List<ProgramCard> placedCards = new LinkedList<ProgramCard>();
        // position after follow path
        Tile movedTile;
        int movedOri;
        // wenn bisher in Richtung Ziel gegangen wurde
        if (goDirGoal) {
            movedTile = bestPathDirEnd;
            movedOri = bestPathDirOri;
            LOGGER.debug(robotName + " leastBadMoves mit " + bestPathDirEnd.getAbsoluteCoordinates());
            // sonst wurde Ziel erreicht und hier ist die aktuelle pos
            // gespeichert
        } else {
            movedTile = simulationTile;
            movedOri = simulationOrientation;
            LOGGER.debug(robotName + " leastBadMoves mit " + simulationTile.getAbsoluteCoordinates());
        }
        LOGGER.debug(
                robotName + " leastBadMoves von " + movedTile.getAbsoluteCoordinates() + " in Richtung " + movedOri);
        ProgramCard currentCard;
        // iteriere über Anzahl zu legender Karten
        for (int i = 0; i < cardsToPlace; i++) {
            int bestDist = 100;
            int bestCardIndex = -1;
            Tile bestCardTile = movedTile;
            int bestCardOri = movedOri;
            // iteriere über alle Karten im Deck
            for (int j = 0; j < cards.size(); j++) {
                currentCard = cards.get(j);
                if (!globalUsedCards.contains(j)) {
                    // ATTENTION: simulationtile/ori maybe not the last pos, if
                    // more then one goal
                    Placement newPlace = logic.simulateTurn(movedTile, movedOri, currentCard, controller);
                    newPlace = logic.simulateGearAndBelt(newPlace, controller);
                    int tempOri = logic.oriAsInt(newPlace.getOrientation());
                    Tile tileAfterMove = pathPlanner.getTile(newPlace.getPosition());
                    // wenn aktuell simulierte MoveKarte nicht gehbar
                    if ((currentCard instanceof MoveForwardCard || currentCard instanceof MoveBackwardCard)
                            && !checkForMoveabilityOnBoard(movedTile, tileAfterMove, movedOri, true)) {
                        // wenn Roboter in Loch/vom Feld laufen muss, weil dies
                        // letzte Karte, fülle mit
                        // zufälligen Karten auf
                        if (j == cards.size() - 1) {
                            return fillWithRandomCards(placedCards, cardsToPlace - i);
                        }
                        continue;
                    }
                    // wenn noch Karten zu legen, aber aktuelle Karte letzte
                    // TurnKarte im Deck ist, prüfe wieviele Felder anschließend
                    // gegangen werden muss und verwerfe karte falls Loch/Rand
                    // im Weg
                    if (i < cardsToPlace - 1
                            && (currentCard instanceof TurnLeftCard || currentCard instanceof TurnRightCard)
                            && !hasUnusedTurnCard(j)) {
                        Tile mustMoveToTile = getTileInDir(newPlace.getPosition(), tempOri,
                                minTilesToMove(cardsToPlace - (i + 1)));
                        if (!checkForMoveabilityOnBoard(tileAfterMove, mustMoveToTile, tempOri, false)) {
                            continue;
                        }
                    }
                    // ATTENTION: currentgoal could also be not the right one if
                    // more then one goal
                    // prüfe ob mit dieser Karte näher an Ziel genagen werden
                    // konnte, als mit bester aus aktueller iteration
                    int dist = calcDist(tileAfterMove, ((GoalTile) currentGoal).getTile(), tempOri); // TODO
                    String key = "";
                    Integer defaultTileCost;
                    try {
                        key = "Planning.DefaultTileCost";
                        defaultTileCost = config.getIntProperty(key);
                    } catch (KeyNotFoundException e) {
                        LOGGER.error(robotName + " " + e);
                        throw new KeyNotFoundRuntimeException(key);
                    }
                    Integer laserCostFactor;
                    try {
                        key = "Planning.LaserCostFactor";
                        laserCostFactor = bConfig.getIntProperty(key);
                    } catch (KeyNotFoundException e) {
                        LOGGER.error(robotName + " " + e);
                        throw new KeyNotFoundRuntimeException(key);
                    }
                    Double risk;
                    try {
                        key = "Planning.Risk";
                        risk = bConfig.getDoubleProperty(key);
                    } catch (KeyNotFoundException e) {
                        LOGGER.error(robotName + " " + e);
                        throw new KeyNotFoundRuntimeException(key);
                    }
                    // addiere Kosten des Tiles auf Distanz
                    dist += calcTileCost(controller, tileAfterMove);
                    // addiere kosten, wenn wall-facing
                    if (currentCard instanceof TurnLeftCard || currentCard instanceof TurnRightCard
                            || currentCard instanceof UturnCard) {
                        Object nextTile = controller.requestData(null,
                                logic.calcMovePos(tileAfterMove.getAbsoluteCoordinates(), tempOri, 1));
                        if (nextTile instanceof Tile
                                && !checkForMoveabilityOnBoard(tileAfterMove, (Tile) nextTile, tempOri, true)) {
                            dist += defaultTileCost * 3;
                            // TODO muss auch aufhören sich in achse zum ziel zu
                            // dehen statt hinderniss zu umgehen
                        }
                    }
                    if (dist < bestDist) {
                        // wenn ja, wird aktuell beste Karte überschrieben
                        bestDist = dist;
                        bestCardIndex = j;
                        bestCardOri = tempOri;
                        bestCardTile = tileAfterMove;
                    }
                }
            }
            // wenn Roboter in Loch/vom Spielfeld laufen muss, weil keine
            // legbare Karte gefunden wurde fülle mit
            // zufälligen Karten auf
            if (bestCardIndex == -1) {
                return fillWithRandomCards(placedCards, cardsToPlace - i);
            }
            // füge gefundene Karte zur Liste hinzu, aktualisiere lokale
            // Position und speichere Karte als genutzt
            placedCards.add(cards.get(bestCardIndex));
            movedTile = bestCardTile;
            movedOri = bestCardOri;
            globalUsedCards.add(bestCardIndex);
        }
        return placedCards;
    }

    /**
     * Legt möglichst gute Karten um einem Ziel näher zu kommen, ohne sich an
     * einen Weg zu halten. Dazu wird iterativ die Karte, der noch ungenutzten
     * gelegt, die dem Ziel am nächsten kommt.
     * 
     * @param cardsToPlace
     *            Anzahl an noch zu legenden Karten
     * @return schließlich gelegte Karten
     */
    public List<ProgramCard> doLeastBadMoves2(int cardsToPlace) {
        List<ProgramCard> placedCards = new LinkedList<ProgramCard>();
        // position after follow path
        Tile movedTile;
        int movedOri;
        LOGGER.debug(robotName + " dlbm mit bisher gelegten Karten: " + bestPathDir + " noch zu legende Karten: "
                + cardsToPlace);
        // wenn bisher in Richtung Ziel gegangen wurde
        if (goDirGoal) {
            movedTile = bestPathDirEnd;
            movedOri = bestPathDirOri;
            LOGGER.debug(robotName + " dlbm von bestPathTile: " + bestPathDirEnd.getAbsoluteCoordinates());
            // sonst wurde Ziel erreicht und hier ist die aktuelle pos
            // gespeichert
        } else {
            movedTile = simulationTile;
            movedOri = simulationOrientation;
            LOGGER.debug(robotName + " dlbm von simTile: " + simulationTile.getAbsoluteCoordinates());
        }
        sanitycheckPos(movedTile, movedOri);
        LOGGER.debug(
                robotName + " leastBadMoves von " + movedTile.getAbsoluteCoordinates() + " in Richtung " + movedOri);
        ProgramCard currentCard;
        // iteriere über Anzahl zu legender Karten
        for (int i = 0; i < cardsToPlace; i++) {
            int bestDist = 100;
            int bestCardIndex = -1;
            Tile bestCardTile = movedTile;
            int bestCardOri = movedOri;
            // Alle für dieses Register bereits evaluierten Kartentypen
            HashSet<Logic.Move> evaluatedCardTypes = new HashSet<Logic.Move>();
            // Alle für dieses Register bereits evaluierten Startfelder von
            // Wegen zum Ziel
            HashMap<Point, Integer> evaluatedPoints = new HashMap<Point, Integer>();
            // iteriere über alle Karten im Deck
            for (int j = 0; j < cards.size(); j++) {
                currentCard = cards.get(j);
                if (!globalUsedCards.contains(j) && isNewCardType(evaluatedCardTypes, currentCard)) {
                    // ATTENTION: simulationtile/ori maybe not the last pos, if
                    // more then one goal
                    evaluatedCardTypes.add(getCardType(currentCard));
                    Placement newPlace = logic.simulateTurn(movedTile, movedOri, currentCard, controller);
                    int tempOri = logic.oriAsInt(newPlace.getOrientation());
                    Tile tileAfterMove = pathPlanner.getTile(newPlace.getPosition());
                    // wenn aktuell simulierte MoveKarte nicht gehbar
                    if ((currentCard instanceof MoveForwardCard || currentCard instanceof MoveBackwardCard)
                            && !checkForMoveabilityOnBoard(movedTile, tileAfterMove, movedOri, true)) {
                        // wenn Roboter in Loch/vom Feld laufen muss, weil dies
                        // letzte Karte, fülle mit
                        // zufälligen Karten auf
                        if (j == cards.size() - 1) {
                            LOGGER.debug("dlbm: place random cause no other card");
                            return fillWithRandomCards(placedCards, cardsToPlace - i);
                        }
                        LOGGER.debug("dlbm: current movecard not placeable " + currentCard);
                        continue;
                    }
                    newPlace = logic.simulateGearAndBelt(newPlace, controller);
                    tempOri = logic.oriAsInt(newPlace.getOrientation());
                    tileAfterMove = pathPlanner.getTile(newPlace.getPosition());
                    if (tileAfterMove.getFieldObject() != null && tileAfterMove.getFieldObject() instanceof Hole) {
                        continue;
                    }
                    // wenn noch Karten zu legen, aber aktuelle Karte letzte
                    // TurnKarte im Deck ist, prüfe wieviele Felder anschließend
                    // gegangen werden muss und verwerfe karte falls Loch/Rand
                    // im Weg
                    if (i < cardsToPlace - 1
                            && (currentCard instanceof TurnLeftCard || currentCard instanceof TurnRightCard)
                            && !hasUnusedTurnCard(j)) {
                        Tile mustMoveToTile = getTileInDir(newPlace.getPosition(), tempOri,
                                minTilesToMove(cardsToPlace - (i + 1)));
                        if (!checkForMoveabilityOnBoard(tileAfterMove, mustMoveToTile, tempOri, false)) {
                            LOGGER.debug("dlbm: current turncard passed couse would result in hole");
                            continue;
                        }
                    }
                    // ATTENTION: currentgoal could also be not the right one if
                    // more then one goal
                    // prüfe ob mit dieser Karte näher an Ziel genagen werden
                    // konnte, als mit bester aus aktueller iteration

                    // Anzahl atomrer Drehungen die zu machen sind
                    int toTurnCount = Math.abs(movedOri - getRelPosStraight(movedTile, tileAfterMove));
                    if (toTurnCount == 3) {
                        toTurnCount = 1;
                    }
                    int dist = 0;
                    // wenn bereits Weg von diesem Startpunkt berechnet wurde,
                    // nehme dessen Distanz
                    if (evaluatedPoints.containsKey(newPlace.getPosition())) {
                        dist = evaluatedPoints.get(newPlace.getPosition());
                    } else {// sonst berechne neuen Weg und speichere ihn
                        dist = pathPlanner.getPath(tileAfterMove, getGoalTile()).size();
                        evaluatedPoints.put(newPlace.getPosition(), dist);
                    }
                    if (dist < bestDist) {
                        // wenn ja, wird aktuell beste Karte überschrieben
                        bestDist = dist;
                        bestCardIndex = j;
                        bestCardOri = tempOri;
                        bestCardTile = tileAfterMove;
                    }
                }
            }
            // wenn Roboter in Loch/vom Spielfeld laufen muss, weil keine
            // legbare Karte gefunden wurde fülle mit
            // zufälligen Karten auf
            if (bestCardIndex == -1) {
                LOGGER.debug(robotName + " random cards " + placedCards);
                return fillWithRandomCards(placedCards, cardsToPlace - i);
            }
            // füge gefundene Karte zur Liste hinzu, aktualisiere lokale
            // Position und speichere Karte als genutzt
            placedCards.add(cards.get(bestCardIndex));
            movedTile = bestCardTile;
            movedOri = bestCardOri;
            globalUsedCards.add(bestCardIndex);
        }
        simulationTile = movedTile;
        simulationOrientation = movedOri;
        return placedCards;
    }

    /**
     * Berechnet die mit der Anzahl der noch zu legenden Karten und den noch
     * nicht gelegten Karten im Deck die noch mindestens zu gehende Anzahl der
     * Felder, sofern nur noch Move-Karten übrig sind.
     * 
     * @param cardsToPlace
     * @return
     */
    public int minTilesToMove(int cardsToPlace) {
        List<Integer> localUsedCards = new LinkedList<Integer>();
        int localTilesMoved = 0;
        ProgramCard localCard;
        // iteriere bis alle Karten gelegt wurden
        for (int i = 0; i < cardsToPlace; i++) {
            int cardRange = 4;
            int localBestCard = -1;
            // iteriere über alle Karten im Deck
            for (int j = 0; j < cards.size(); j++) {
                localCard = cards.get(j);
                if (localCard == null || !(localCard instanceof MoveForwardCard)) {
                    continue;
                }
                // wenn aktuelle MoveKarte noch frei und kleiner als bisher
                // gewählte, wähle sie
                if (!globalUsedCards.contains(j) && !localUsedCards.contains(j)) {
                    if (((MoveForwardCard) localCard).getRange() == 2 && cardRange > 2) {
                        cardRange = 2;
                        localBestCard = j;
                    } else if (((MoveForwardCard) localCard).getRange() == 1 && cardRange > 1) {
                        cardRange = 1;
                        localBestCard = j;
                        break;
                    }
                    if (cardRange > 3) {
                        cardRange = 3;
                        localBestCard = j;
                    }
                }
            }
            // wähle beste Karte dauerhaft
            if (cardRange < 4 && localBestCard != -1) {
                localUsedCards.add(localBestCard);
                localTilesMoved += cardRange;
            }
        }
        return localTilesMoved;
    }

    /**
     * Prüft, ob eine bestimmte Variante eine Bewegung auszuführen mit den
     * Karten im Deck durchführbar ist.
     * 
     * @param variant
     *            die Liste der Karten die gelegt werden müssten
     * @return die gelegten Karten, oder null falls nicht möglich
     */
    public List<ProgramCard> isPlaceable(List<String> variant) { // TODO check
                                                                 // if every
                                                                 // card is
                                                                 // placeable(holes)
        int potentialPlacedCard;
        List<Integer> usedCards = new LinkedList<Integer>();
        List<Integer> newUsedCards = new LinkedList<Integer>();
        usedCards.addAll(globalUsedCards);
        for (String card : variant) {
            if (card != null) {
                potentialPlacedCard = checkForCard(card, usedCards);
                if (potentialPlacedCard != -1) {
                    usedCards.add(potentialPlacedCard);
                    newUsedCards.add(potentialPlacedCard);
                } else {
                    // falls eine Karte nicht legbar ist, ist die ganze Variante
                    // nicht legbar
                    return new LinkedList<ProgramCard>();
                }

            }
        }
        globalUsedCards.addAll(newUsedCards);
        // generiert aus der Liste der indizes der genutzen Karten eine Liste
        // von Programkarten
        List<ProgramCard> cardsToPlace = new LinkedList<ProgramCard>();
        for (int i = 0; i < newUsedCards.size(); i++) {
            cardsToPlace.add(cards.get(newUsedCards.get(i)));
        }
        return cardsToPlace;
    }

    /**
     * Vergleicht 2 Programmkarten und gibt true zurück, wenn sie vom selben Typ
     * sind und ggf die selbe Range haben.
     * 
     * @param card1
     *            1. Karte
     * @param card2
     *            2. Karte
     * @return sind Karten gleich(abgesehen von Priorität)
     */
    public boolean compareCards(ProgramCard card1, ProgramCard card2) {
        if (card1.getClass().equals(card2.getClass())) {
            if (card1 instanceof MoveForwardCard && card1.getRange() != card2.getRange()) {
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * Berechnet die Distanz von einem Start- zu einem Ziel{@link Tile} inf Form
     * von zu gehenden Feldern und mindestens zu machenden Drehungen.
     * 
     * @param current
     *            Start
     * @param target
     *            Ziel
     * @param ori
     *            Orientierung des Roboters auf dem StartTile
     * @return Distanz in Feldern und Drehungen
     */
    public int calcDist(Tile current, Tile target, int ori) {
        int currentX = current.getAbsoluteCoordinates().x;
        int currentY = current.getAbsoluteCoordinates().y;
        int targetX = target.getAbsoluteCoordinates().x;
        int targetY = target.getAbsoluteCoordinates().y;

        // calc distance of tiles by diff in x plus diff in y coords
        int tileDist = Math.abs(targetX - currentX);
        tileDist += Math.abs(targetY - currentY);

        // if target is in same row/column: get relative pos of target in int
        int relPos = getRelPosStraight(current, target);

        // else get relative pos in int by other method plus bonus turn
        int bonus = 0;
        if (relPos == -1) {
            relPos = getRelPosDiag(current, target, ori);
            bonus++;
        }
        // calc turns to do by diff of orientation and relative pos of target
        int turns = Math.abs(relPos - ori);
        if (turns == 3) {
            turns = 1;
        }
        turns += bonus;
        return tileDist + turns;
    }

    /**
     * Überprüft, ob ein Ziel sich auf der selben Reihe, oder Spalte wie der
     * Startpunkt befindet. Rückgabewert kodiert das Ergebnis in Form der
     * relativen Position.
     * 
     * @param current
     *            Tile des Starts
     * @param target
     *            Tile des Ziels
     * @return -1 falls Ziel nicht in direkter Linie, 0 falls links, 1 falls
     *         oben, 2 falls rechts, 3 falls unten
     */
    @Override
    public int getRelPosStraight(Tile current, Tile target) {
        int currentX = current.getAbsoluteCoordinates().x;
        int currentY = current.getAbsoluteCoordinates().y;
        int targetX = target.getAbsoluteCoordinates().x;
        int targetY = target.getAbsoluteCoordinates().y;
        if (targetX == currentX && targetY > currentY) {
            return 1;
        } else if (targetX == currentX && targetY < currentY) {
            return 3;
        } else if (targetY == currentY && targetX > currentX) {
            return 2;
        } else if (targetY == currentY && targetX < currentX) {
            return 0;
        }
        return -1;
    }

    /**
     * Wandelt eine Liste von Tiles in eine Liste der jeweiligen absoluten
     * Koordinaten. Vor allem für den Logger gedacht.
     * 
     * @param path
     *            Weg von Tiles
     * @return Weg als Punkte
     */
    public List<Point> pathAsPointList(List<Tile> path) {
        List<Point> points = new LinkedList<Point>();
        if (path != null && !path.isEmpty() && path.get(0) != null) {
            for (Tile tile : path) {
                points.add(tile.getAbsoluteCoordinates());
            }
        }
        return points;
    }

    /**
     * Überprüft, ob ein Ziel sich diagonal von einem Start befindet. Der
     * Rückgabewert kodiert in welchem Quadranten sich das Ziel relativ
     * befindet. Da jeweils 2 Richtungen das Ergebniss bilden können, wird
     * abhängig von der Blickrichtung des Roboters die Richtung kodiert, die
     * weniger Drehungen für den Roboter bedeuten.
     * 
     * @param current
     *            Tile des Starts
     * @param target
     *            Tile des Ziels
     * @param ori
     *            Blickrichtung des Roboters
     * @return -1 falls Ziel nicht schräg vom Start, 0 falls in der linken
     *         Hälfte, 1 für obere Hälfte ...
     */
    @Override
    public int getRelPosDiag(Tile current, Tile target, int ori) {
        int currentX = current.getAbsoluteCoordinates().x;
        int currentY = current.getAbsoluteCoordinates().y;
        int targetX = target.getAbsoluteCoordinates().x;
        int targetY = target.getAbsoluteCoordinates().y;
        if (targetX > currentX && targetY > currentY) {
            if (ori == 0 || ori == 1) {
                return 1;
            }
            return 2;
        } else if (targetX > currentX && targetY < currentY) {
            if (ori == 1 || ori == 2) {
                return 2;
            }
            return 3;
        } else if (targetX < currentX && targetY < currentY) {
            if (ori == 3 || ori == 2) {
                return 3;
            }
            return 0;
        } else if (targetX < currentX && targetY > currentY) {
            if (ori == 0 || ori == 3) {
                return 0;
            }
            return 1;
        }
        return -1;

    }

    public void setLogic(Logic logic) {
        this.logic = logic;
    }

    @Override
    public void setController(Control controller) {
        this.controller = controller;
    }

    public void setTimerUnit(Timer timerUnit) {
        this.timerUnit = timerUnit;
    }

    public void setFuturePlanner(FuturePlanning futurePlanner) {
        this.futurePlanner = futurePlanner;
    }

    public void setCurrentPlan(Plans currentPlan) {
        this.currentPlan = currentPlan;
    }

    public void setDistToTarget(int dist) {
        this.distToTarget = dist;
    }

    public void setReachableDist(int reachableDist) {
        this.reachableDist = reachableDist;
    }

    public int getSimulationOrientation() {
        return simulationOrientation;
    }

    public void setSimulationOrientation(int simulationOrientation) {
        this.simulationOrientation = simulationOrientation;
    }

    public Tile getSimulationTile() {
        return simulationTile;
    }

    public void setSimulationTile(Tile simulationTile) {
        this.simulationTile = simulationTile;
    }

    public int getSimulationPosInPath() {
        return simulationPosInPath;
    }

    public void setSimulationPosInPath(int simulationPosInPath) {
        this.simulationPosInPath = simulationPosInPath;
    }

    public void setCards(List<ProgramCard> cards) {
        this.cards = cards;
    }

    public List<Integer> getGlobalUsedCards() {
        return globalUsedCards;
    }

    public void setGlobalUsedCards(List<Integer> globalUsedCards) {
        this.globalUsedCards = globalUsedCards;
    }

    public Goals getCurrentGoal() {
        return currentGoal;
    }

    public void setCurrentGoal(Goals currentGoal) {
        this.currentGoal = currentGoal;
    }

    public void setConfigReader(AgentConfigReader config) {
        this.config = config;
    }

    public void setBConfigReader(StaticBehaviourConfigReader bConfig) {
        this.bConfig = bConfig;
    }

    public List<ProgramCard> getAllCards() {
        return cards;
    }

    public List<Integer> getUsedDirCards() {
        return usedDirCards;
    }
    @Override
    public Tile getGoalTile() {
        if (currentGoal.getClass().equals(GoalTile.class)) {
            return ((GoalTile) currentGoal).getTile();
        }
        return null;
    }
    @Override
    public int getTacticPower() {
        return tacticPower;
    }
    @Override
    public Tactics getTactic() {
        return currentTactic;
    }

    /**
     * Prüft ob eine gerade Strecke! zwischen Start und Zielfeld begehbar ist.
     * 
     * @param start
     *            Startfeld
     * @param target
     *            Zielfeld
     * @param ori
     *            Startorientierung
     * @param checkWallsAlso
     *            ob auch nach Wänden geprüft werden soll
     * @return begehbar oder nicht
     */
    boolean checkForMoveabilityOnBoard(Tile start, Tile target, int ori, boolean checkWallsAlso) {
        if (target == null || start == null) {
            return false;
        }
        Tile currentTile = start;
        Point startPoint = start.getAbsoluteCoordinates();
        Point targetPoint = target.getAbsoluteCoordinates();
        int diffX = Math.abs(startPoint.x - targetPoint.x);
        int diffY = Math.abs(startPoint.y - targetPoint.y);
        int relPos = getRelPosStraight(start, target);
        // anzahl der geradeaus zu gehenden Felder bis zum Ziel
        int diff = diffX + diffY;
        Tile nextTile;
        // jeweils 1 feld in richtung zielfeld gehen
        for (int i = 0; i < diff; i++) {
            nextTile = getTileInDir(startPoint, relPos, 1);
            // wenn dieses feld nicht begehbar (nicht existent, Loch)
            if (nextTile == null || (nextTile.getFieldObject() != null && nextTile.getFieldObject() instanceof Hole)) {
                return false;
            }
            // wenn nicht begehbar weil Wand im Weg und danach auch zu testen
            // ist
            if (checkWallsAlso && pathPlanner.isWallInWay(ori, currentTile, nextTile)) {
                return false;
            }
            currentTile = nextTile;
            startPoint = nextTile.getAbsoluteCoordinates();
        }
        return true;

    }

    private void setGlobalVars() {
        tacticPower = 0;
        simulationPosInPath = 0;
        highestGoalTile = null;
        currentGoal = null;
        globalUsedCards.clear();
        usedDirCards.clear();
        bestPathDir = null;
        debugTimerSBPD = 0;
        setCurrentPlan(futurePlanner.getCurrentPlan());
        // currentTile kann nicht null sein, da Roboter immer auf einem Tile,
        // wenn er plant
        currentTile = (Tile) controller.requestData(RequestType.CURRENTTILE, null);
        getCards();
        numberOfCardsToPlace = numberOfCardsToPlace();
    }

    private void handleReachedFlagCardsLeft() {
        controller.setPotentialReachedGoal(GoalType.FLAG);
        futurePlanner.startPlanning();
        setCurrentPlan(futurePlanner.getCurrentPlan());
        goDirGoal = false;
        bestPathDir = null;
        bestPathDirEnd = null;
        bestPathDirOri = -1;
    }

    /**
     * Prüft, ob die Eingabewerte dem Endpunkt des bestPath entsprechen, wirft
     * sonst eine Exception.
     * 
     * @param endTile
     * @param endOri
     */
    private void sanitycheckPos(Tile endTile, int endOri) {
        Tile posStart = (Tile) controller.requestData(RequestType.CURRENTTILE, null);
        Orientation oriStart = (Orientation) (controller.requestData(RequestType.ORIENTATION, null));
        Placement placeBefore = new PlacementUnit(posStart.getAbsoluteCoordinates(), oriStart, posStart);
        if (bestPathDir == null) {
            return;
        }
        for (int i = 0; i < bestPathDir.size(); i++) {
            if (bestPathDir.get(i) != null) {
                LOGGER.debug(
                        "sanitycheck " + bestPathDir.get(i) + " von " + placeBefore.getTile().getAbsoluteCoordinates());
                // simuliere Karte ohne Boardelemente
                Placement calcPlace = logic.simulateTurn(placeBefore.getTile(),
                        logic.oriAsInt(placeBefore.getOrientation()), bestPathDir.get(i), controller);
                // wenn Weg in Loch führt, werfe Exception
                if (!checkForMoveabilityOnBoard(placeBefore.getTile(), calcPlace.getTile(),
                        logic.oriAsInt(placeBefore.getOrientation()), false)) {
                    throw new IllegalStateException("Robo should not have planned path into hole!!");
                } // wenn gegen Mauer gegangen wird, behalte Position bei
                else if (!checkForMoveabilityOnBoard(placeBefore.getTile(), calcPlace.getTile(),
                        logic.oriAsInt(placeBefore.getOrientation()), true)) {
                    calcPlace.setTile(placeBefore.getTile());
                    calcPlace.setPosition(placeBefore.getTile().getAbsoluteCoordinates());
                    LOGGER.debug("sanitycheck: againstWall");
                } // simuliere Boardelemente; auskommentiert, weil
                  // bestPathDirEnd auch ohne berechnet wurde
                  // calcPlace=logic.simulateGearAndBelt(calcPlace, controller);
                placeBefore = calcPlace;
                LOGGER.debug("sanitycheck iterationresult " + placeBefore.getTile().getAbsoluteCoordinates());
            }
        } // wenn berechnete Position nicht mit Eingabe übereinstimmt, werfe
          // Exception
        if (!placeBefore.getTile().getAbsoluteCoordinates().equals(endTile.getAbsoluteCoordinates())
                && endOri != logic.oriAsInt(placeBefore.getOrientation())) {
            throw new IllegalStateException("Place of subpath-end doesn't match the subpath-cards! calc: "
                    + placeBefore.getTile().getAbsoluteCoordinates() + " , " + endTile.getAbsoluteCoordinates());
        }
    }

    /**
     * Prüft, ob bereits eine Karte des seleb Typs wie die als Argument
     * übergebene ausgwertet wurde
     * 
     * @param evaluatedCardTypes
     *            bereits ausgewertete Kartentpen
     * @param currentCard
     *            aktuelle Karte
     * @return neuer Kartentyp oder nicht
     */
    private boolean isNewCardType(HashSet<Logic.Move> evaluatedCardTypes, ProgramCard currentCard) {
        Logic.Move currentCardType = getCardType(currentCard);
        return !evaluatedCardTypes.contains(currentCardType);
    }

    /**
     * Gibt zu einer Karte ihren Kartentyp zurück
     * 
     * @param card
     *            Karte dessen Typ bestimmt werden soll
     * @return Typ der Karte
     */
    private Logic.Move getCardType(ProgramCard card) {
        if (card == null) {
            return null;
        } else if (card instanceof TurnLeftCard) {
            return Logic.Move.LEFT;
        } else if (card instanceof TurnRightCard) {
            return Logic.Move.RIGHT;
        } else if (card instanceof UturnCard) {
            return Logic.Move.UTURN;
        } else if (card instanceof MoveBackwardCard) {
            return Logic.Move.MINUSONE;
        } else if (card instanceof MoveForwardCard && card.getRange() == 3) {
            return Logic.Move.THREE;
        } else if (card instanceof MoveForwardCard && card.getRange() == 2) {
            return Logic.Move.TWO;
        }
        return Logic.Move.ONE;
    }

    /**
     * Berechnet die Kosten eines Tiles unter Berücksichtigung von Feldobjekten
     * wie Laser und Gears.
     * 
     * @param control
     *            Controlunit
     * @param tile
     *            Tile dessen Kosten zu berechnen sind
     * @return Kosten des Tiles
     */
    private int calcTileCost(Control control, Tile tile) {
        PathNode temp = new PathNode(tile, this, logic);
        return temp.getTileCost(control);
    }

    /**
     * Füllt eine Liste mit zu legenden Karten mit zufälligen, noch nicht
     * benutzen Karten auf
     * 
     * @param inputCards
     *            Liste mit Karten, die schon gelegt wurden
     * @param cardsCount
     *            Noch zu legende Karten
     * @return alle gelegte Karten
     */
    private List<ProgramCard> fillWithRandomCards(List<ProgramCard> inputCards, int cardsCount) {
        List<ProgramCard> placedCards = inputCards;
        int cardsToPlaceNow = cardsCount;
        ProgramCard currentCard;
        cardsToPlaceLoop: for (int i = 0; i < cardsToPlaceNow; i++) {
            for (int j = 0; j < cards.size(); j++) {
                currentCard = cards.get(j);
                if (currentCard != null && !globalUsedCards.contains(j)) {
                    placedCards.add(currentCard);
                    globalUsedCards.add(j);
                    continue cardsToPlaceLoop;
                }
            }
            LOGGER.error("fillRandom: Keine legbare Karte gefunden! Das sollte nicht vorkommen!");
        }
        return placedCards;
    }

    /**
     * Prüft, ob noch Drehkarten im Deck sind, die noch nicht benutzt wurden.
     * 
     * @param additionalUsedCardsIndex
     *            Index einer zusätzlich bereits benutzen Karte
     * @return true wenn noch nicht benutze Drehkarte gefunden
     */
    private boolean hasUnusedTurnCard(int additionalUsedCardsIndex) {
        boolean result = false;
        ProgramCard localCard;
        for (int i = 0; i < cards.size(); i++) {
            localCard = cards.get(i);
            if (localCard != null && (localCard instanceof TurnLeftCard || localCard instanceof TurnRightCard)
                    && i != additionalUsedCardsIndex) {
                result = globalUsedCards.contains(i);
                if (!result) {
                    return true;
                }
            }
        }
        return result;
    }

    /**
     * Gibt das Tile x Felder in der angegebenen Richtung zurück.
     * 
     * @param startPoint
     *            aktueller Punkt
     * @param ori
     *            Richtung
     * @param dist
     *            Distanz
     * @return Feld in Richtung nach Distanz
     */
    private Tile getTileInDir(Point startPoint, int ori, int dist) {
        int newX = startPoint.x;
        int newY = startPoint.y;
        switch (ori) {
        case 0:
            newX -= dist;
            break;
        case 1:
            newY += dist;
            break;
        case 2:
            newX += dist;
            break;
        default:
            newY -= dist;
        }
        return pathPlanner.getTile(new Point(newX, newY));
    }

    /**
     * Ermittelt die zu legenden Karten in einem Normalfall des Kartendecks.
     * Dazu wird über die Liste der priorisierten Ziele iteriert und jeweils
     * geprüft, ob es sich um ein {@link GoalTile}, sprich ein Ziel der Form
     * goTo, oder um ein abstrakteres anderes Ziel handelt. Jeweilige
     * Unterfunktionen versuchen das aktuelle Ziel mit dem Kartendeck zu
     * erreichen.
     * 
     * @return Liste der gewählten Karten
     */
    private List<ProgramCard> handleNormalCase() {
        List<ProgramCard> turn;
        for (int j = 0; j < currentPlan.getGoals().size(); j++) {
            currentGoal = getNextHighestGoal(currentPlan);
            if (currentGoal instanceof GoalTile) {
                turn = handleGoalTile(j);
                // falls Unterfunktion in aktueller Iteration das Ziel erreicht
                // hat, gebe die gewählten Karten zurück
                if (!turn.isEmpty()) {
                    return turn;
                }
            } else if (currentGoal instanceof GoalTactic
                    && ((GoalTactic) currentGoal).getTactic().getTactic() == Tactics.ROBINSONCRUSO) {
                currentTactic = ((GoalTactic) currentGoal).getTactic().getTactic();
                tacticPower = ((GoalTactic) currentGoal).getTactic().getPowerLevel();
                turn = handleGoalTile(j);
                // falls Unterfunktion in aktueller Iteration das Ziel erreicht
                // hat, gebe die gewählten Karten zurück
                if (!turn.isEmpty()) {
                    return turn;
                }
            } else {
                turn = handleOtherGoal(j);
                // falls Unterfunktion in aktueller Iteration das Ziel erreicht
                // hat, gebe die gewählten Karten zurück
                if (!turn.isEmpty()) {
                    return turn;
                }
            }
        }
        return new LinkedList<ProgramCard>();
    }

    /**
     * Versucht im Zuge der Schleife der übergeordneten Funktion das aktuelle
     * {@link GoalTile} mit den gegebenen Karten zu erreichen, sofern dessen
     * Priorität höher als die GoNearPriorität eines ggf. bereits vorhandenen
     * nicht erreichbaren {@link GoalTile}s ist.
     * 
     * @param j
     *            Zählvariable der Schleife der übergeordneten Funktion, zeigt
     *            an, ob aktuelle iteration die Letzte ist
     * @return Unterfunktion um Zielfeld zu erreichen, ihm nahe zu kommen oder
     *         null falls noch weitere Ziele evaluiert werden sollen
     */
    private List<ProgramCard> handleGoalTile(int j) {
        // wenn dies das erste GoalTile (und damit das wichtigste ist), halte es
        // fest
        if (highestGoalTile == null) {
            highestGoalTile = (GoalTile) currentGoal;
            // falls bereits ein wichtigeres GoalTile evaluiert(und nicht
            // erreicht werden konnte)
            // und zudem das aktuelle Ziel(und damit alle nachfolgenden) eine
            // niedrigere Priorität hat,
            // als die GoNearPrio des wichtigeren früheren Ziels, gehe in
            // Richtung dessen
        } else if (highestGoalTile != null && currentGoal.getPriority() <= highestGoalTile.getDirectionPriority()) {
            return moveDirectionGoal();
        }
        // berechne Distanz von Start zu Ziel in Feldern+ min. Drehungen
        setDistToTarget(calcDist(currentTile, ((GoalTile) currentGoal).getTile(), getRoboOri()));
        // falls das Ziel geschätzt nicht in diesem Zug erreichabr ist..
        if (distToTarget > reachableDist && bestPathDir != null) {
            // und dies das letzte Ziel ist,
            // gehe in Richtung des früheren wichtigeren Ziels
            // (im Zweifel dieses selbst)
            if (j == futurePlanner.getCurrentPlan().getGoals().size() - 1) {
                return moveDirectionGoal();
            }
            // falls dies Ziel nicht erreichbar und nicht das letzte, returne
            // null und gehe damit zum nächsten Ziel
            return new LinkedList<ProgramCard>();
        }
        // falls Ziel geschätzt erreichbar, versuche es konkret zu erreichen
        List<ProgramCard> tempTurn = tryToReachGoalTile(((GoalTile) currentGoal).getTile());
        // falls geklappt, returne Karten
        if (!tempTurn.isEmpty()) {
            return tempTurn;
        }
        // falls nicht, aber dies das letzte Ziel, gehe Richtung des wichtigsten
        if (j == futurePlanner.getCurrentPlan().getGoals().size() - 1) {
            return moveDirectionGoal();
        }
        // alternativ versuche es mit dem nächsten Ziel (parent-schleife)
        return new LinkedList<ProgramCard>();
    }

    /**
     * Versucht im Zuge der Schleife der übergeordneten Funktion das aktuelle
     * nicht-GoalTile-Ziel zu erreichen, sofern dessen Priorität höher als die
     * GoNearPriorität eines ggf. bereits vorhandenen nicht erreichbaren
     * {@link GoalTile}s ist.
     * 
     * @param j
     *            Zählvariable der Schleife der übergeordneten Funktion, zeigt
     *            an, ob aktuelle iteration die Letzte ist
     * @return Unterfunktion um Zielfeld zu erreichen, ihm nahe zu kommen oder
     *         null falls noch weitere Ziele evaluiert werden sollen
     */
    private List<ProgramCard> handleOtherGoal(int j) {
        // wenn GoNear Prio eines bekannten GoalTile höher als die des aktuellen
        // Ziels(und aller nachfolgenden),
        // gehe in dessen Richtung
        if (highestGoalTile != null && currentGoal.getPriority() < highestGoalTile.getDirectionPriority()) {
            return moveDirectionGoal();
        }
        // sonst versuche aktuelles Ziel zu erreichen
        List<ProgramCard> tempTurn = tryToAchieveOtherGoal();
        if (!tempTurn.isEmpty()) {
            return tempTurn;
        }
        // falls nicht erreicht, aber dies das letzte Ziel, gehe in Richtung des
        // GoalTiles (es gibt immer mind. 1)
        if (j == futurePlanner.getCurrentPlan().getGoals().size() - 1) {
            return moveDirectionGoal();
        }
        // sonst nächste iteration
        return new LinkedList<ProgramCard>();
    }

    /**
     * Holt die aktuellen Karten im Deck.
     */
    private void getCards() {
        int numberOfCardsToGet = numberOfCardToGet();
        cards.clear();
        if (numberOfCardsToGet > 0) {
            for (int i = 1; i <= numberOfCardsToGet; i++) {
                cards.add((ProgramCard) controller.requestData(null, i));
            }
        }

    }

    /**
     * Versucht ein {@link GoalTile} zu erreichen. Berechnet dazu iterativ Pfade
     * zu dem Ziel und prüft, ob diese mit den gegebenen Karten ablaufbar sind.
     * 
     * @param target
     *            Zielfeld
     * @return gewählte Karten, oder null für nicht erreicht
     */
    private List<ProgramCard> tryToReachGoalTile(Tile target) {
        List<Point> blockedPoints = new LinkedList<Point>();
        // berechne Pfad
        List<Tile> path = pathPlanner.getPath(currentTile, target);
        LOGGER.debug(robotName + " Weg 1: " + pathAsPointList(path));
        List<ProgramCard> success;
        for (int i = 0; i < 2; i++) {
            // füge 1. Feld des Weges zu den blockierten, damit falls nötig ein
            // anderer Weg berechnet wird
            if (path == null || path.isEmpty()) {
                continue;
            }
            blockedPoints.add(path.get(0).getAbsoluteCoordinates());
            // versuche Pfad abzulaufen
            success = tryToFollowPath(path);
            // wenn cb Bewegung vorkam, rekursion für neuen Path
            /*
             * if (foundConveyor && !success.isEmpty()) {
             * LOGGER.debug(robotName+
             * " foundConveyor->rekursion; gelegte Karten: "+ success);
             * foundConveyor = false; List<ProgramCard> tempList =
             * tryToReachGoalTile(target); success=tempList; }
             */
            if (!success.isEmpty()) {
                return success;
            }
            globalUsedCards.clear();
            if (i == 1) {
                break;
            }
            // errechne alternativen Pfad
            path = pathPlanner.getAlternativePath(currentTile, target, blockedPoints);
            LOGGER.debug(robotName + " Weg " + (i + 2) + ": " + pathAsPointList(path));
        }
        // Ziel nicht erreichbar
        return new LinkedList<ProgramCard>();
    }

    /**
     * Versucht einen Pfad mit gegebenen Karten abzulaufen.
     * 
     * @param path
     *            der Pfad
     * @return gewählte Karten, oder null falls nicht möglich
     */
    private List<ProgramCard> tryToFollowPath(List<Tile> path) {
        simulationPosInPath = 0;
        List<ProgramCard> cardsPlaced = new LinkedList<ProgramCard>();
        int posInPathBeforeMove;
        simulationTile = currentTile;
        LOGGER.debug(robotName + " FollowPath  von " + simulationTile.getAbsoluteCoordinates());
        simulationOrientation = getRoboOri();
        if (simulationPosInPath >= path.size()) {
            simulationPosInPath = 0;
        }
        int relPos = getRelPosStraight(simulationTile, path.get(simulationPosInPath));
        Tile target = path.get(path.size() - 1);
        while (!(simulationTile.getAbsoluteCoordinates().equals(target.getAbsoluteCoordinates()))) {
            // wenn nächstes Tile im Pfad nicht in Blickrichtung, versuche die
            // nötige Drehung mit den Karten zu legen
            // returne null falls nicht möglich, oder zu viele Karten nötig
            if (simulationOrientation != relPos) {
                LOGGER.debug(robotName + " tryToFollowPath turn");
                cardsPlaced.addAll(tryToTurn(relPos));
                relPos = getRelPosStraight(simulationTile, path.get(simulationPosInPath));
                if (simulationOrientation != relPos || cardsPlaced.size() > numberOfCardsToPlace) {
                    LOGGER.debug(robotName + " tryToFollowPath turn nicht möglich");
                    setBestPathDir(cardsPlaced, target);
                    return new LinkedList<ProgramCard>();
                }
            }
            // Anzahl an nach vorne zu gehenden Tiles im Pfad
            int straightForward = getStraightForward(path, simulationTile, simulationPosInPath);
            // versuche diese Tiles zu gehen, wenn nicht möglich, oder zu viele
            // Karten nötig, returne null
            posInPathBeforeMove = simulationPosInPath;
            cardsPlaced.addAll(tryToMove(straightForward));
            if (!simulationTile.getAbsoluteCoordinates().equals(path.get(path.size() - 1).getAbsoluteCoordinates())) {
                Tile tileInPath = path.get(simulationPosInPath);
                relPos = getRelPosStraight(simulationTile, tileInPath);
            }
            if (foundConveyor) {
                return cardsPlaced;
            }
            if (posInPathBeforeMove + straightForward != simulationPosInPath
                    || cardsPlaced.size() > numberOfCardsToPlace) {
                LOGGER.debug(robotName + " tryToFollowPath move nicht möglich");
                setBestPathDir(cardsPlaced, target);
                return new LinkedList<ProgramCard>();
            }
        }

        // falls Ziel des Pfades mit den Karten erreicht, returne die Karten
        return cardsPlaced;
    }

    /**
     * Berechnet die Endposition eines Pfades in Richtung Ziel mit den maximal
     * zu legenden Karten und speichert ihn, falls bisher kein besserer bestand.
     * 
     * @param cardsOfDirPath
     *            Karten des Wegen in Richtung Ziel
     * @param target
     *            Zielfeld
     */
    private void setBestPathDir(List<ProgramCard> cardsOfDirPath, Tile target) {
        debugTimerSBPD++;
        int localSimOri = getRoboOri();
        Tile localSimTile = currentTile;
        List<ProgramCard> firstMax5Cards = new LinkedList<ProgramCard>();
        ProgramCard currentlCard;
        // simuliere die noch zu legenden Karten des Pfades in Richtung Ziel und
        // speichere Karte
        for (int i = 0; i < Math.min(numberOfCardsToPlace, cardsOfDirPath.size()); i++) {
            if (!cardsOfDirPath.isEmpty() && cardsOfDirPath.get(i) != null) {
                currentlCard = cardsOfDirPath.get(i);
                firstMax5Cards.add(currentlCard);
                Placement placementAfterCard = logic.simulateTurn(localSimTile, localSimOri, currentlCard, controller);
                localSimTile = pathPlanner.getTile(placementAfterCard.getPosition());
                if (localSimTile == null) {
                    LOGGER.debug(robotName + " setBestPathDir vom Board");// TODO
                                                                          // prüfen
                    return; // Pfad führt vom Board, sollte am besten nicht
                            // passieren -> sollte das nicht eher ein break
                            // sein?
                }
                localSimOri = logic.oriAsInt(placementAfterCard.getOrientation());
            }
        }
        LOGGER.debug(robotName + " setBEstPathDir " + firstMax5Cards);
        if (bestPathDirEnd != null) {
            LOGGER.debug(robotName + " setBEstPathDir original bestPathEnd: " + bestPathDirEnd.getAbsoluteCoordinates()
                    + "new one: " + localSimTile.getAbsoluteCoordinates() + " call " + debugTimerSBPD);
        }
        // wenn hier berechneter Pfad in Richtung Ziel näher an das Ziel
        // gekommen ist, als
        // ein ggf vorher bestehender, speichere ihn
        if (bestPathDirEnd == null || (bestPathDirEnd != null
                && calcDist(localSimTile, target, localSimOri) < calcDist(bestPathDirEnd, target, bestPathDirOri))) {
            LOGGER.debug(robotName + " setBEstPathDir save it ..");
            bestPathDirEnd = localSimTile;
            bestPathDir = firstMax5Cards;
            bestPathDirOri = localSimOri;
            usedDirCards.clear();
            usedDirCards.addAll(globalUsedCards);
            LOGGER.debug(robotName + " setBestPath savePos " + localSimTile.getAbsoluteCoordinates());
        }
    }

    /**
     * Versucht eine bestimmte Anzahl an Feldern mit den Karten im Deck vorwärts
     * zu gehen (geht von physischer gehbarkeit aus). Im zweifel wird möglichst
     * weit versucht zu gehen.
     * 
     * @param straightForward
     *            Anzahl an vorwärts zu gehender Felder
     * @return gelegte Programmkarten
     */
    private List<ProgramCard> tryToMove(int straightForward) {
        int tilesToMove = straightForward;
        List<List<String>> variants = new LinkedList<List<String>>();
        List<ProgramCard> cardsToPlace = new LinkedList<ProgramCard>();
        int tilesMoved = 0;
        int tilesMovedLast = 0;
        int tilesMovedSpecial = 0;
        // solange noch Felder zu gehen, prüfe ob es 1,2,3 oder mehr sind,
        // ermittle die dafür nötigen Kartenvarianten und speichere
        // die potentiell gegangene Anzahl der Felder
        outerloop: while (tilesToMove > 0 && cardsToPlace.size() < 5) {
            switch (tilesToMove) {
            case 1:
                tilesMovedLast = 1;
                variants = logic.getNeededCardsForMove(Move.ONE);
                break;
            case 2:
                tilesMovedLast = 2;
                variants = logic.getNeededCardsForMove(Move.TWO);
                break;
            case 3:
                tilesMovedLast = 3;
                variants = logic.getNeededCardsForMove(Move.THREE);
                break;
            // bei mehr als 3 zu gehenden Feldern, erstelle custom varianten
            // um nacheinander move3, move2 und move1 zu prüfen
            default:
                tilesMovedSpecial = -1;
                variants = new LinkedList<List<String>>(Arrays.asList(new LinkedList<String>(Arrays.asList("Three")),
                        new LinkedList<String>(Arrays.asList("Two")), new LinkedList<String>(Arrays.asList("One"))));
            }
            // prüfe alle Kartenvarianten auf legbarkeit
            for (List<String> variant : variants) {
                if (variant != null) {
                    List<ProgramCard> success = isPlaceable(variant);
                    // wenn eine variante legbar war und es der spezialfall mit
                    // mehr als 3 feldern war, ermittle welche Karte gelegt
                    // wurde
                    if (!success.isEmpty()) {
                        if (tilesMovedSpecial == -1 && "Three".equals(variant.get(0))) {
                            tilesMovedSpecial = 3;
                        } else if (tilesMovedSpecial == -1 && "Two".equals(variant.get(0))) {
                            tilesMovedSpecial = 2;
                        } else if (tilesMovedSpecial == -1 && "One".equals(variant.get(0))) {
                            tilesMovedSpecial = 1;
                        }
                        // speichere gelegte Karte und aktualisiere Variablen
                        // für zurückgelegten und zurückzulegenden Weg
                        cardsToPlace.addAll(success);
                        // simuliert conveyorbeltbewegung, wirft alle Karten
                        // nach cb weg und setzt Flag und bricht Schleife ab
                        for (int i = 0; i < success.size(); i++) {
                            int conveyorMoves = calcConveyerBelts(success.get(i), tilesMoved);
                            tilesMoved += conveyorMoves;
                            tilesToMove -= conveyorMoves;
                            ProgramCard tempCard = cardsToPlace.get(i);
                            ArrayList<ProgramCard> tempCardsToPlace = new ArrayList<>();
                            tempCardsToPlace.add(tempCard);
                            if (conveyorMoves != 0) {
                                cardsToPlace = tempCardsToPlace;
                                foundConveyor = true;
                                break outerloop;
                            } else {
                                foundConveyor = false;
                            }
                        }
                        // TODO : CB : nach jeder
                        // Karte den Standpunkt
                        // simulieren und abfgaren
                        // ob CB. Wenn CB nach
                        // vorne, dann tilesMoved+1
                        // / +2 / -1
                        tilesMoved += tilesMovedLast + tilesMovedSpecial;
                        tilesToMove -= tilesMovedLast + tilesMovedSpecial;
                        tilesMovedSpecial = 0;
                        // fahre mit äußerer Schleife fort
                        continue outerloop;
                    }
                }
            }

            // wenn keine variante legbar, dekrementiere zu gehenden weg, um ggf
            // Ziel wenigstens nahe zu kommen
            tilesToMove -= 1;
        }
        // Future: produce error?
        // wenn voran gekommen, berechne neue Position und orientierung
        if (!cardsToPlace.isEmpty()) {
            Point newPoint = logic.calcMovePos(simulationTile.getAbsoluteCoordinates(), simulationOrientation,
                    tilesMoved);
            simulationTile = pathPlanner.getTile(newPoint);
            simulationPosInPath += tilesMoved; // TODO kann zu outofbounds vom
                                               // path führen
        }
        return cardsToPlace;
    }

    private int calcConveyerBelts(ProgramCard programCard, int tilesMoved) {
        Point tempPoint = logic.calcMovePos(simulationTile.getAbsoluteCoordinates(), simulationOrientation, tilesMoved);
        Tile t = (Tile) controller.requestData(null, tempPoint);

        Tile tileAfterSim = (Tile) controller.requestData(null,
                (logic.simulateTurn(t, simulationOrientation, programCard, controller)).getPosition());
        if (tileAfterSim == null) {
            return 0;
        }
        if (tileAfterSim.getFieldObject() != null && tileAfterSim.getFieldObject() instanceof ConveyorBelt) {
            ConveyorBelt cb = (ConveyorBelt) tileAfterSim.getFieldObject();
            // Überprüft ob die Orientation des ConveyorBelts gleich der des
            // Roboters ist
            if ((cb.getOrientation().equals(Orientation.NORTH) && (getRoboOri() == 1))
                    || (cb.getOrientation().equals(Orientation.EAST) && (getRoboOri() == 2))
                    || (cb.getOrientation().equals(Orientation.SOUTH) && (getRoboOri() == 3))
                    || (cb.getOrientation().equals(Orientation.WEST) && (getRoboOri() == 0))) {
                if (cb.isExpress()) { // Guckt ob Express wenn ja wird 2
                                      // zurückgegeben
                    currentTile = tileAfterSim;
                    return 2;
                }
                currentTile = tileAfterSim;
                return 1;
            }
        }
        return 0;
    }

    /**
     * Prüft, ob eine Karte sich in dem Deck der Karten befindet und noch nicht
     * benutzt wurde.
     * 
     * @param card
     *            die gesuchte Karte
     * @param usedCards
     *            bereits genutze Karten
     * @return der Index der gefundenen Karte im Deck
     */
    private int checkForCard(String card, List<Integer> usedCards) {
        ProgramCard currentCard;
        for (int i = 0; i < cards.size(); i++) {
            currentCard = cards.get(i);
            if (currentCard instanceof UturnCard && "UTurn".equals(card) && !usedCards.contains(i)) {
                return i;
            } else if (currentCard instanceof MoveBackwardCard && "BackUp".equals(card) && !usedCards.contains(i)) {
                return i;
            } else if (currentCard instanceof TurnLeftCard && "Left".equals(card) && !usedCards.contains(i)) {
                return i;
            } else if (currentCard instanceof TurnRightCard && "Right".equals(card) && !usedCards.contains(i)) {
                return i;
            } else if (currentCard instanceof MoveForwardCard && ((MoveForwardCard) currentCard).getRange() == 3
                    && "Three".equals(card) && !usedCards.contains(i)) {
                return i;
            } else if (currentCard instanceof MoveForwardCard && ((MoveForwardCard) currentCard).getRange() == 2
                    && "Two".equals(card) && !usedCards.contains(i)) {
                return i;
            } else if (currentCard instanceof MoveForwardCard && ((MoveForwardCard) currentCard).getRange() == 1
                    && "One".equals(card) && !usedCards.contains(i)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Future: Fehler einbauen? Ermittelt, wie viele Felder im Pfad als nächstes
     * geradeaus gegangen werden müssen.
     * 
     * @param path
     *            Pfad aus {@link Tile}s
     * @param currentTile
     *            Feld auf dem der Roboter in der Simulation steht
     * @param posInPath
     *            das nächste Feld im Pfad
     * @return Anzahl an Feldern die geradeaus zu gehen sind
     */
    private int getStraightForward(List<Tile> path, Tile currentTile, int posInPath) {
        int straightForward = 0;
        Tile nextTile = path.get(posInPath);
        Tile lastTile = currentTile;
        // Anfangsrichtung in die zu gehen ist
        int direction = getRelPosStraight(currentTile, nextTile);
        // prüfe, wie viele Felder im Pfad weiter in die Anfangsrichtung zu
        // gehen ist
        for (int i = posInPath; i < path.size(); i++) {
            if (getRelPosStraight(lastTile, nextTile) == direction) {
                straightForward++;
                lastTile = nextTile;
                if (nextTile.getAbsoluteCoordinates().equals(path.get(path.size() - 1).getAbsoluteCoordinates())) {
                    break;
                }
                nextTile = path.get(i + 1);
            }
        }
        return straightForward;
    }

    /**
     * Versucht eine bestimmte Drehung mit den gegebenen Karten zu legen.
     * 
     * @param relPos
     *            die Richtugn in die gedreht werden soll.
     * @return gelegte Karten
     */
    private List<ProgramCard> tryToTurn(int relPos) {
        // differenz zwischen aktueller und gewünschter orientierung
        int diff = simulationOrientation - relPos;
        List<List<String>> variants = new LinkedList<List<String>>();
        List<ProgramCard> cardsToPlace = new LinkedList<ProgramCard>();
        char turned = 'e';
        // ermittle nötige Drehung und dazugehörige varianten
        if (diff == -3 || diff == 1) {
            variants = logic.getNeededCardsForMove(Move.LEFT);
            turned = 'l';
        } else if (diff == -1 || diff == 3) {
            variants = logic.getNeededCardsForMove(Move.RIGHT);
            turned = 'r';
        } else if (diff == -2 || diff == 2) {
            variants = logic.getNeededCardsForMove(Move.UTURN);
            turned = 'u';
        }
        // prüfe alle varianten auf legbarkeit
        for (List<String> variant : variants) {
            if (variant != null) {
                List<ProgramCard> success = isPlaceable(variant);
                if (!success.isEmpty()) {
                    cardsToPlace.addAll(success);
                    break;
                }
            }
        }
        // future: produce errors
        // wenn erfolgreich, setze neue orientierung
        if (!cardsToPlace.isEmpty() && turned != 'e') {
            if (turned == 'u') {
                simulationOrientation = (simulationOrientation + 2) % 4;
            } else if (turned == 'r') {
                simulationOrientation = (simulationOrientation + 1) % 4;
            } else if (turned == 'l' && simulationOrientation == 0) {
                simulationOrientation = -3;
            } else {
                simulationOrientation = simulationOrientation - 1;
            }
        }
        // wenn nicht erfolgreich und uturn gefordertn, versuche wenigstend nach
        // links oder rechts zu drehen und setze bei erfolg orierntierugn neu
        else if (turned == 'u') {
            List<String> restLeft = new LinkedList<String>(Arrays.asList("Left"));
            List<String> restRight = new LinkedList<String>(Arrays.asList("Right"));
            List<ProgramCard> success = isPlaceable(restLeft);
            if (success.isEmpty()) {
                success = isPlaceable(restRight);
            }
            if (!success.isEmpty()) {
                cardsToPlace.add(success.get(0));
                if (success.get(0) instanceof TurnLeftCard) {
                    simulationOrientation = (simulationOrientation - 1) % 4;
                }
                simulationOrientation = (simulationOrientation + 1) % 4;
            }
        }
        return cardsToPlace;
    }

    /**
     * Gibt den besten Weg in Richtung Ziel zurück, der legbar ist.
     * 
     * @return gelegte Karten
     */
    private List<ProgramCard> moveDirectionGoal() {
        goDirGoal = true;
        globalUsedCards.addAll(usedDirCards);
        usedDirCards.clear();
        if (bestPathDir == null) {
            goDirGoal = false;
            return new LinkedList<ProgramCard>();
        }
        return bestPathDir;
    }

    /**
     * Future: versucht ein otherGoal zu erreichen
     *
     * @return die gelegten Karten
     */
    private List<ProgramCard> tryToAchieveOtherGoal() {
        // If otherGoals would be implemented yet, this would handle them
        return new LinkedList<ProgramCard>();
    }

    /**
     * Behandelt die Spezialfälle des Kartendecks.
     *
     * Fall 1: Alle Karten gleich: Gib eine Liste mit zufällig gewählten Karten
     * zurück. Fall 2: Keine Karten vorhanden oder spielbar: Gib leere Liste
     * zurück. Fall 3: Maximal 2 Karten zur Verfügung Fall 4: Nur "Drehkarten"
     * Fall 5: Nur Bewegungskarten (keine Drehkarten)
     * 
     * @param caseNumber
     *            id des Spezialfalles
     * @return Liste der gewählten Karten
     */
    private List<ProgramCard> handleSpecialCase(int caseNumber) {
        List<ProgramCard> subList = new ArrayList<ProgramCard>();
        currentGoal = getNextHighestGoal(currentPlan);
        switch (caseNumber) {
        case 1:
            for (int i = 0; i < numberOfCardsToPlace; i++) {
                subList.add(cards.get(i));
            }
            return subList;
        case 2:
            return new LinkedList<ProgramCard>();
        case 3:
            return doLeastBadMoves2(cards.size());
        case 4:
            return handleOnlyTurnCards();
        case 5:
            return handleOnlyMoveCards();
        default:
            LOGGER.debug("handleSpecialCase: no case found!");
        }
        return new LinkedList<ProgramCard>();
    }

    /**
     * Behandelt den Fall, dass Nur Drehkarten zur Verfügung stehen.
     *
     * @author Simon Liedtke
     * @return
     */
    private List<ProgramCard> handleOnlyTurnCards() {
        List<ProgramCard> cards = logic.retrieveUsableCards(this);
        List<Move> moves = logic.programCards2Moves(cards);
        Map<Move, Integer> movesFrequencies = logic.getFrequencyTable(moves);

        List<ProgramCard> defaultCards = new ArrayList<>();
        for (int i = 0; i < numberOfCardsToPlace; i++) {
            defaultCards.add(cards.get(i));
        }

        Tile goalTile = getGoalTile();
        int pos = getRelPosStraight(currentTile, goalTile);

        switch (pos) {
        // wenn nicht gedreht werden soll, versuchen dass sich die Drehungen
        // gegenseitig aufheben
        case -1:
        case 1: // oben
            Map<Move, Integer> cardsAsFrequencyTable = logic.tryMinTurns(moves);
            List<ProgramCard> cardsToReturn = logic.frequencytable2cardlist(cardsAsFrequencyTable, cards);
            return cardsToReturn;
        case 0: // links
            Map<Move, Integer> canGoLeft = logic.canPerformMove(Move.LEFT, moves);
            if (canGoLeft.isEmpty()) {
                return defaultCards;
            }
            return logic.frequencytable2cardlist(canGoLeft, cards);
        case 2: // rechts
            Map<Move, Integer> canGoRight = logic.canPerformMove(Move.RIGHT, moves);
            if (canGoRight.isEmpty()) {
                return defaultCards;
            }
            return logic.frequencytable2cardlist(canGoRight, cards);
        case 3: // unten
            Map<Move, Integer> canTurnAround = logic.canPerformMove(Move.UTURN, moves);
            if (canTurnAround.isEmpty()) {
                return defaultCards;
            }
            return logic.frequencytable2cardlist(canTurnAround, cards);
        }
        return cards;
    }

    /**
     * Behandelt den Fall, dass nur Bewegungskarten zur Verfügung stehen.
     * Versucht dabei, dem Ziel möglichst nahe zu kommen.
     *
     * @author Simon Liedtke
     * @return
     */
    private List<ProgramCard> handleOnlyMoveCards() {
        Tile goalTile = getGoalTile();
        int pos = getRelPosStraight(currentTile, goalTile);

        int orientation = getRoboOri();
        // true, wenn das Ziel direkt "angeschaut" wird, sonst false
        boolean aiming_goal = orientation == pos;

        boolean goal_behind = orientation == 0 && pos == 2 || orientation == 1 && pos == 3
                || orientation == 2 && pos == 0 || orientation == 3 && pos == 1;

        int x = goalTile.getAbsoluteCoordinates().x;
        int y = goalTile.getAbsoluteCoordinates().y;
        List<ProgramCard> cards = logic.retrieveUsableCards(this);

        int distance = Integer.MAX_VALUE;
        if (aiming_goal) {
            switch (orientation) {
            case 0:
                distance = currentTile.getAbsoluteCoordinates().x - x;
                break;
            case 1:
                distance = y - currentTile.getAbsoluteCoordinates().y;
                break;
            case 2:
                distance = x - currentTile.getAbsoluteCoordinates().x;
                break;
            case 3:
                distance = currentTile.getAbsoluteCoordinates().y - y;
                break;
            }
        } else if (goal_behind) {
            switch (orientation) {
            case 0:
                distance = -(currentTile.getAbsoluteCoordinates().x - x);
                break;
            case 1:
                distance = -(y - currentTile.getAbsoluteCoordinates().y);
                break;
            case 2:
                distance = -(x - currentTile.getAbsoluteCoordinates().x);
                break;
            case 3:
                distance = -(currentTile.getAbsoluteCoordinates().y - y);
                break;
            }
        }

        if (aiming_goal || goal_behind) {
            Map<Move, Integer> moves = logic.goMovesOnly(logic.programCards2Moves(cards), distance);
            List<ProgramCard> cardList = logic.frequencytable2cardlist(moves, cards);
            return cardList;
        }
        List<ProgramCard> defaultCards = new ArrayList<>();
        for (int i = 0; i < numberOfCardsToPlace; i++) {
            defaultCards.add(cards.get(i));
        }
        return defaultCards;
    }

    /**
     * Ermittelt, ob das Deck der Karten einen Spezialfall darstellt.
     * Spezialfall 1:alle Karten gleich
     * 
     * @return id, die angibt, ob und falls ja, welcher Spezialfall vorliegt, 0
     *         bedeutet kein Spezialfall
     */
    private int isSpecialCase() {
        if (allTheSameCards()) {
            return 1;
        } else if (cards == null) {
            return 2;
        } else if (cards.size() <= 2) {
            return 3;
        } else if (onlyTurnCards()) {
            return 4;
        } else if (onlyMoveCards()) {
            return 5;
        }
        return 0;
    }

    private boolean onlyTurnCards() {
        if (cards.isEmpty())
            return false;
        for (ProgramCard card : cards) {
            if (card == null)
                continue;
            if (!(card instanceof TurnLeftCard || card instanceof TurnRightCard))
                return false;
        }
        return true;
    }

    private boolean onlyMoveCards() {
        if (cards.isEmpty())
            return false;
        for (ProgramCard card : cards) {
            if (card == null)
                continue;
            if (!(card instanceof MoveForwardCard || card instanceof MoveBackwardCard))
                return false;
        }
        return true;
    }

    /**
     * Ermittelt, ob das Deck der Karten aus ausschließlichen gleichen Karten
     * besteht.
     * 
     * @return gleiche Karten, oder nicht
     */
    private boolean allTheSameCards() {
        boolean theSame = false;
        if (!cards.isEmpty()) {
            ProgramCard last = cards.get(0);
            // gehe alle Karten durch, falls eine nicht mit der vorherigen
            // übereinstimmt, gebe dies durch false zurück
            for (ProgramCard card : cards) {
                if (card == null || compareCards(card, last)) {
                    theSame = true;
                } else {
                    return false;
                }
            }

        }
        return theSame;
    }

    /**
     * Ermittelt aus dem Schaden des Roboters die zu legenden Karten des
     * Spielers.
     * 
     * @return Anzahl zu legender Karten des Spielers
     */
    private int numberOfCardsToPlace() {
        return Math.min(numberOfCardToGet(), 5);
    }

    /**
     * Berechnet anhand der aktuellen HP die Anzahl der Karten, die der Spieler
     * erhalten wird.
     */
    private int numberOfCardToGet() {
        return (Integer) controller.requestData(RequestType.HP, null) - 1;
    }

    /**
     * Future: gebe Ziel mit höchstem Wert zurück, sobals mehrere vorkommen Gibt
     * aus einem {@link Plan} mit eine Liste von {@link Goal}s das mit dem
     * niedrigsten Prioritätswert zurück.
     * 
     * @param currentPlan
     *            Plan mit Zielen
     * @return Ziel mit niedrigsten Prioritätswert
     */
    private Goals getNextHighestGoal(Plans currentPlan) {
        PriorityQueue<Goals> goals = currentPlan.getGoals();
        return goals.poll();
    }

    /**
     * Gibt die Blickrichtung des Roboters als integer zurück.
     * 
     * @return 0 für links, 1 für oben, 2 für rechts, 3 für unten
     */
    private int getRoboOri() {
        return (Integer) logic.oriAsInt((Orientation) controller.requestData(RequestType.ORIENTATION, null));
    }

}
