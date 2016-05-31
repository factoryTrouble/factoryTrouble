/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki2.ai.planning;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import de.uni_bremen.factroytrouble.ai.ais.AIPlayer2;
import de.uni_bremen.factroytrouble.ai.ki2.ai.decision.consciousness.ScullyApproach;
import de.uni_bremen.factroytrouble.ai.ki2.api.decision.consciousness.Approach;
import de.uni_bremen.factroytrouble.ai.ki2.api.decision.consciousness.ConsciousnessUnit;
import de.uni_bremen.factroytrouble.ai.ki2.api.decision.unconsciousness.UnconsciousnessUnit;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.player.GameMoveBackwardCard;
import de.uni_bremen.factroytrouble.player.GameMoveForwardCard;
import de.uni_bremen.factroytrouble.player.GameTurnLeftCard;
import de.uni_bremen.factroytrouble.player.GameTurnRightCard;
import de.uni_bremen.factroytrouble.player.GameUturnCard;
import de.uni_bremen.factroytrouble.player.ProgramCard;

public class CardFinder extends Finder {

    private ConsciousnessUnit cons;
    private HashMap<List<ProgramCard>, Integer> planList;
    private int planCounter;
    private AIPlayer2 ai;

    public CardFinder(ConsciousnessUnit consciousness, UnconsciousnessUnit uncons, AIPlayer2 ai) {
        super(uncons);
        this.ai = ai;
        cons = consciousness;
        planList = new HashMap<>();
        planCounter = 0;
    }

    /**
     * Finde Karten für einen gegebenen Weg
     * 
     * @param way
     *            der Weg
     * @return die zu legenden Karten
     */
    public List<ProgramCard> findCards(List<Integer> way) {
        List<ProgramCard> right = getHandCards("Rechtsdrehkarten");
        List<ProgramCard> left = getHandCards("Linksdrehkarten");
        List<ProgramCard> uTurn = getHandCards("Uturnkarten");
        List<ProgramCard> back = getHandCards("Rückwärtskarten");
        List<ProgramCard> forward1 = getHandCards("1vorwärtskarten");
        List<ProgramCard> forward2 = getHandCards("2vorwärtskarten");
        List<ProgramCard> forward3 = getHandCards("3vorwärtskarten");
        List<ProgramCard> cards = new ArrayList<>();
        List<ProgramCard> add;
        int counter = 0;
        for (int key : way) {
            counter++;
            switch (key) {
            case -1:
                add = turnLeft(right, left, uTurn);
                cards.addAll(add);
                break;
            case -2:
                add = uTurn(right, left, uTurn);
                cards.addAll(add);
                break;
            case -3:
                add = turnRight(right, left, uTurn);
                cards.addAll(add);
                break;
            default:
                add = moveForward(key, forward1, forward2, forward3, 5 - cards.size(),
                        getEndpoint(way.subList(0, counter - 1)), findOrientation(way.subList(0, counter - 1)));
                cards.addAll(add);
            }
            if (add.isEmpty()) {
                return add;
            }
        }
        planCounter++;
        List<ProgramCard> remainingCards = new ArrayList<>();
        remainingCards.addAll(right);
        remainingCards.addAll(left);
        remainingCards.addAll(uTurn);
        remainingCards.addAll(back);
        remainingCards.addAll(forward1);
        remainingCards.addAll(forward2);
        remainingCards.addAll(forward3);
        cards = fillCards(cards, remainingCards);
        Point end = getEndpoint(toIntList(cards));
        if (getConveyor().containsKey(end)) {
            end = getConveyor().get(end).getPointAfterConveyor();
        }
        planList.putIfAbsent(cards, getDistanceToGoal(end));
        return sendPlans();
    }

    /**
     * schaut ob ein Speizialfall zutrifft.
     * 
     * @return Zu legende Karten
     */
    public List<ProgramCard> specialCase() {
        List<ProgramCard> forward1 = getHandCards("1vorwärtskarten");
        List<ProgramCard> forward2 = getHandCards("2vorwärtskarten");
        List<ProgramCard> forward3 = getHandCards("3vorwärtskarten");
        List<ProgramCard> all = getHandCards("");

        List<ProgramCard> cards = new ArrayList<>();
        // Keine Move Karten
        if (forward1.isEmpty() && forward2.isEmpty() && forward3.isEmpty()) {
            fillCards(cards, all);
            return cards;
        }
        // nur eine Karte in der Hand
        if (all.size() == 1) {
            cards.add(all.get(0));
            return cards;
        }
        // nur zwei Karten in der Hand
        if (all.size() == 2) {
            cards.add(all.get(0));
            cards.add(all.get(1));
            cards = fillRandom(cards, new ArrayList<>());
            planList.putIfAbsent(cards, getDistanceToGoal(getEndpoint(toIntList(cards))));
            cards.clear();
            cards.add(all.get(1));
            cards.add(all.get(0));
            cards = fillRandom(cards, new ArrayList<>());
            planList.putIfAbsent(cards, getDistanceToGoal(getEndpoint(toIntList(cards))));
            cards = getNextPlan();
            return cards;
        }
        return cards;
    }

    /**
     * Füllt den Plan mit Drehkarten auf.
     * 
     * @param cards
     *            zu füllender Plan
     * @param remainingCards
     *            Übrige Karten
     * @return gefüllter Plan
     */
    private List<ProgramCard> fillCards(List<ProgramCard> cards, List<ProgramCard> remainingCards) {
        List<ProgramCard> turnCards = new ArrayList<>();
        List<Orientation> bestOrients = findBestOrient(cards);
        for (Orientation orient : bestOrients) {
            int turn = findOrientation(toIntList(cards)).ordinal() - orient.ordinal();
            turn = (turn > 0) ? turn - 4 : turn;
            switch (turn) {
            case -1:
                turnCards.addAll(turnLeft(getHandCards("Rechtsdrehkarten"), getHandCards("Linksdrehkarten"),
                        getHandCards("Uturnkarten")));
                break;
            case -2:
                turnCards.addAll(uTurn(getHandCards("Rechtsdrehkarten"), getHandCards("Linksdrehkarten"),
                        getHandCards("Uturnkarten")));
                break;
            case -3:
                turnCards.addAll(turnRight(getHandCards("Rechtsdrehkarten"), getHandCards("Linksdrehkarten"),
                        getHandCards("Uturnkarten")));
                break;
            default:
                turnCards.addAll(noTurn(getHandCards("Rechtsdrehkarten"), getHandCards("Linksdrehkarten"),
                        getHandCards("Uturnkarten")));
            }
            if (turnCards.size() + cards.size() == 5) {
                cards.addAll(turnCards);
                break;
            }
            turnCards.clear();
        }
        return fillRandom(cards, remainingCards).subList(0, 5);
    }

    /**
     * Füllt den Plan mit übrigen Karten auf.
     * 
     * @param cards
     *            zu füllender Plan
     * @param remainingCards
     *            Übrige Karten
     * @return gefüllter Plan
     */
    private List<ProgramCard> fillRandom(List<ProgramCard> cards, List<ProgramCard> remainingCards) {
        for (ProgramCard randomCard : remainingCards) {
            if (cards.size() + getLockedCards().size() >= 5) {
                break;
            }
            cards.add(randomCard);
        }
        for (ProgramCard lockedCard : getLockedCards()) {
            cards.add(lockedCard);
        }
        return cards;
    }

    /**
     * Gibt zurück, in welche Richtung der Roboter am besten schauen sollte,
     * nachdem er den Weg gegangen ist
     * 
     * @param cards,
     *            zu legende Karten
     * 
     * @return die Richtung
     */
    private List<Orientation> findBestOrient(List<ProgramCard> cards) {
        Point pos = getEndpoint(toIntList(cards));
        int dist = Integer.MAX_VALUE;
        Orientation tempOrient = null;
        List<Orientation> bestOrient = new ArrayList<>();
        List<Orientation> orients = new ArrayList<>();
        orients.addAll(Arrays.asList(Orientation.values()));
        while (bestOrient.size() < 4) {
            for (Orientation orient : orients) {
                int orientDist = getDistanceToGoal(getNeighbors(pos, orient));
                if (!noObstacleInWay(pos, orient)) {
                    orientDist++;
                }
                if (orientDist < dist) {
                    dist = orientDist;
                    tempOrient = orient;
                }
            }
            bestOrient.add(tempOrient);
            dist = Integer.MAX_VALUE;
            orients.remove(tempOrient);
        }
        return bestOrient;
    }

    /**
     * Sucht Karten um vorwärts zu gehen
     * 
     * @param key,
     *            Wie weit vorwärts gegangen werden soll
     * @param forward1,
     *            move1 Karten
     * @param forward2,
     *            move2 Karten
     * @param forward3,
     *            move3 Karten
     * @param availableCards,
     *            maximale Anzahl zu legender Karten
     * @return Karten zur Bewegung
     */
    private List<ProgramCard> moveForward(int key, List<ProgramCard> forward1, List<ProgramCard> forward2,
            List<ProgramCard> forward3, int availableCards, Point currentPosition, Orientation currentOrientation) {
        int threeSize = forward3.size();
        int twoSize = forward2.size();
        int oneSize = forward1.size();
        int three = 0;
        int two = 0;
        int one = 0;
        int steps = key;
        List<Integer> move = new ArrayList<>(Arrays.asList(0, 0, 0));
        int currentAmount = 0;
        int bestAmount = 0;
        while (true) {
            List<Integer> amount;

            amount = moveOneStep(availableCards, steps, three, threeSize, 3, currentPosition, currentOrientation);
            three += amount.get(0);
            steps -= amount.get(0) * 3 + amount.get(1);
            currentAmount += amount.get(0) * 3 + amount.get(1);

            amount = moveOneStep(availableCards, steps, two, twoSize, 2, currentPosition, currentOrientation);
            two += amount.get(0);
            steps -= amount.get(0) * 2 + amount.get(1);
            currentAmount += amount.get(0) * 2 + amount.get(1);

            amount = moveOneStep(availableCards, steps, one, oneSize, 1, currentPosition, currentOrientation);
            one += amount.get(0);
            steps -= amount.get(0) + amount.get(1);
            currentAmount += amount.get(0) + amount.get(1);

            if (bestAmount < currentAmount) {
                move.clear();
                move.add(three);
                move.add(two);
                move.add(one);
                bestAmount = currentAmount;
            }
            if (three > 0) {
                three--;
                threeSize = three;
                steps += 3;
                currentAmount -= 3;

            } else {
                if (two > 0) {
                    two--;
                    twoSize = two;
                    steps += 2;
                    currentAmount -= 2;

                } else {
                    break;
                }
            }
        }
        List<ProgramCard> cards = addCards(move, forward3, forward2, forward1);
        if (availableCards - cards.size() < 0) {
            return new ArrayList<>();
        }
        return cards;
    }

    /**
     * Fügt move Karten einer Sorte ein
     * 
     * @param availableCards,
     *            Max Anzahl zu legender Karten
     * @param key,
     *            zu erreichende Distanz
     * @param card,
     *            bisherige Karten der Sorte
     * @param cardAmount,
     *            Anzahl des Vorkommens der Karte
     * @param step,
     *            Kartensorte
     * 
     * @return Menge an hinzugefügten Karten
     */
    private List<Integer> moveOneStep(int availableCards, int key, int card, int cardAmount, int step,
            Point currentPosition, Orientation currentOrientation) {
        int amount = 0;
        int cards = card;
        int conveyorMoves = 0;
        Point newPosition = currentPosition;

        for (int a = key; a - conveyorMoves > step - 1; a -= step) {
            if (availableCards == cards || cardAmount == cards) {
                List<Integer> result = new ArrayList<>();
                result.add(amount);
                result.add(conveyorMoves);
                return result;
            }
            cards++;
            amount++;
            for (int steps = step; steps > 0; steps--) {
                newPosition = getNeighbors(newPosition, currentOrientation);
            }
            if (getConveyor().containsKey(newPosition)
                    && (int) newPosition.distance(currentPosition) * 10 != key * 10) {
                int addedAmount = findDifference(newPosition, currentOrientation);
                newPosition = getConveyor().get(newPosition).getPointAfterConveyor();
                if (addedAmount != -99 && a - addedAmount - conveyorMoves >= 0) {
                    conveyorMoves += addedAmount;
                } else {
                    amount--;
                    break;
                }
            }
        }
        List<Integer> result = new ArrayList<>();
        result.add(amount);
        result.add(conveyorMoves);
        return result;
    }

    private int findDifference(Point newPosition, Orientation currentOrientation) {
        Point movedPosition = getConveyor().get(newPosition).getPointAfterConveyor();
        if (currentOrientation == Orientation.NORTH || currentOrientation == Orientation.SOUTH) {
            return differenceY(movedPosition, newPosition, currentOrientation);
        } else {
            if (movedPosition.y != newPosition.y) {
                return -99;
            }
            if (currentOrientation == Orientation.EAST) {
                return newPosition.x - movedPosition.x;
            } else {
                return movedPosition.x - newPosition.x;
            }
        }
    }

    private int differenceY(Point movedPosition, Point newPosition, Orientation currentOrientation) {
        if (movedPosition.x != newPosition.x) {
            return -99;
        }
        if (currentOrientation == Orientation.NORTH) {
            return newPosition.y - movedPosition.y;
        } else {
            return movedPosition.y - newPosition.y;
        }
    }

    /**
     * Speichert die Karten zur Vorwärtsbewegung
     * 
     * @param move
     *            zu speichernde Karten
     * @param forward3,
     *            move3 Karten
     * @param forward2,
     *            move2 Karten
     * @param forward1,
     *            move1 Karten
     * 
     * @return Anzahl gespeicherter Karten
     */
    private List<ProgramCard> addCards(List<Integer> move, List<ProgramCard> forward3, List<ProgramCard> forward2,
            List<ProgramCard> forward1) {
        List<ProgramCard> cards = new ArrayList<>();
        int three = move.get(0);
        int two = move.get(1);
        int one = move.get(2);
        while (three > 0) {
            three--;
            cards.add(forward3.get(0));
            forward3.remove(0);
        }
        while (two > 0) {
            two--;
            cards.add(forward2.get(0));
            forward2.remove(0);
        }
        while (one > 0) {
            one--;
            cards.add(forward1.get(0));
            forward1.remove(0);
        }
        return cards;
    }

    /**
     * Sucht Karten für Rechtsdrehung
     * 
     * @param right,
     *            alle Rechtsdrehkarten
     * @param left,
     *            alle Linksdrehkarten
     * @param uTurn,
     *            alle UTurnkarten
     * @return Karten zur Rechtsdrehung
     */
    private List<ProgramCard> turnRight(List<ProgramCard> right, List<ProgramCard> left, List<ProgramCard> uTurn) {
        List<ProgramCard> cards = new ArrayList<>();

        if (!right.isEmpty()) {
            cards.add(right.get(0));
            right.remove(0);
        } else {
            if (!uTurn.isEmpty() && !left.isEmpty()) {
                cards.add(uTurn.get(0));
                uTurn.remove(0);
                cards.add(left.get(0));
                left.remove(0);
            } else {
                if (left.size() >= 3) {
                    cards.add(left.get(0));
                    left.remove(0);
                    cards.add(left.get(0));
                    left.remove(0);
                    cards.add(left.get(0));
                    left.remove(0);
                } else {
                    return new ArrayList<>();
                }
            }
        }
        return cards;
    }

    /**
     * Sucht Karten für UTurn
     * 
     * @param right,
     *            alle Rechtsdrehkarten
     * @param left,
     *            alle Linksdrehkarten
     * @param uTurn,
     *            alle UTunrdrehkarten
     * @return Karten zur UTurn
     */
    private List<ProgramCard> uTurn(List<ProgramCard> right, List<ProgramCard> left, List<ProgramCard> uTurn) {
        List<ProgramCard> cards = new ArrayList<>();
        if (!uTurn.isEmpty()) {
            cards.add(uTurn.get(0));
            uTurn.remove(0);
        } else {
            if (right.size() >= 2) {
                cards.add(right.get(0));
                right.remove(0);
                cards.add(right.get(0));
                right.remove(0);
            } else {
                if (left.size() >= 2) {
                    cards.add(left.get(0));
                    left.remove(0);
                    cards.add(left.get(0));
                    left.remove(0);
                } else {
                    return new ArrayList<>();
                }
            }
        }
        return cards;
    }

    /**
     * Sucht Karten für Linksdrehung
     * 
     * @param right,
     *            alle Rechtsdrehkarten
     * @param left,
     *            alle Linksdrehkarten
     * @param uTurn,
     *            alle UTunrdrehkarten
     * @return Karten zur Linksdrehung
     */
    private List<ProgramCard> turnLeft(List<ProgramCard> right, List<ProgramCard> left, List<ProgramCard> uTurn) {
        List<ProgramCard> cards = new ArrayList<>();
        if (!left.isEmpty()) {
            cards.add(left.get(0));
            left.remove(0);
        } else {
            if (!uTurn.isEmpty() && !right.isEmpty()) {
                cards.add(uTurn.get(0));
                uTurn.remove(0);
                cards.add(right.get(0));
                right.remove(0);
            } else {
                if (right.size() >= 3) {
                    cards.add(right.get(0));
                    right.remove(0);
                    cards.add(right.get(0));
                    right.remove(0);
                    cards.add(right.get(0));
                    right.remove(0);
                } else {
                    return new ArrayList<>();
                }
            }
        }
        return cards;
    }

    /**
     * Sucht Karten um sich nur im Kreis zu drehen
     * 
     * @param right,
     *            alle Rechtsdrehkarten
     * @param left,
     *            alle Linksdrehkarten
     * @param uTurn,
     *            alle UTunrdrehkarten
     * @return Karten zur Linksdrehung
     */
    private List<ProgramCard> noTurn(List<ProgramCard> right, List<ProgramCard> left, List<ProgramCard> uTurn) {
        List<ProgramCard> cards = new ArrayList<>();
        if (uTurn.size() >= 2) {
            cards.add(uTurn.get(0));
            uTurn.remove(0);
            cards.add(uTurn.get(0));
            uTurn.remove(0);
        } else {
            if (!right.isEmpty() && !left.isEmpty()) {
                cards.add(right.get(0));
                right.remove(0);
                cards.add(left.get(0));
                left.remove(0);
            } else {
                return new ArrayList<>();
            }
        }
        return cards;
    }

    /**
     * Schreibt die Programmkarten als Integer auf
     * 
     * @param cards
     *            Programmkarten
     * @return die Liste von Integer
     */
    private List<Integer> toIntList(List<ProgramCard> cards) {
        List<Integer> intList = new ArrayList<>();
        for (ProgramCard card : cards) {
            if (card instanceof GameTurnRightCard) {
                intList.add(-3);
            }
            if (card instanceof GameTurnLeftCard) {
                intList.add(-1);
            }
            if (card instanceof GameUturnCard) {
                intList.add(-2);
            }
            if (card instanceof GameMoveForwardCard) {
                intList.add(card.getRange());
            }
            if (card instanceof GameMoveBackwardCard) {
                intList.add(-4);
            }
        }
        return intList;
    }

    /**
     * Sendet einen Plan an das Bewusstsein, wird er abgelehnt sinkt die
     * Priorität dieses Plans
     * 
     * @return Akzeptierter Plan
     */
    private List<ProgramCard> sendPlans() {
        List<ProgramCard> cards = getNextPlan();
        if (planCounter > 20) {
            Approach plan = new ScullyApproach(cards, ai, cons);
            if (cons.decide(plan, 1)) {
                return cards;
            }
            planList.replace(cards, planList.get(cards) + 2);
        }
        return new ArrayList<>();
    }

    /**
     * Holt den nächstbesten Plan
     * 
     * @return der Plan
     */
    private List<ProgramCard> getNextPlan() {
        int dist = Integer.MAX_VALUE;
        List<ProgramCard> cards = new ArrayList<>();
        for (Entry<List<ProgramCard>, Integer> possiblePlan : planList.entrySet()) {
            if (dist > possiblePlan.getValue()) {
                cards = possiblePlan.getKey();
                dist = possiblePlan.getValue();
            }
        }
        return cards;
    }
}
