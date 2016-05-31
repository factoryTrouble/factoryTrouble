/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki1;

import java.awt.Point;
import java.util.*;

import de.uni_bremen.factroytrouble.ai.ki1.planning.CurrentPlannerOne;
import de.uni_bremen.factroytrouble.api.ki1.Control;
import de.uni_bremen.factroytrouble.api.ki1.Logic;
import de.uni_bremen.factroytrouble.api.ki1.Placement;
import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.gameobjects.ConveyorBelt;
import de.uni_bremen.factroytrouble.gameobjects.FieldObject;
import de.uni_bremen.factroytrouble.gameobjects.Gear;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.player.MoveBackwardCard;
import de.uni_bremen.factroytrouble.player.MoveForwardCard;
import de.uni_bremen.factroytrouble.player.TurnLeftCard;
import de.uni_bremen.factroytrouble.player.TurnRightCard;
import de.uni_bremen.factroytrouble.player.UturnCard;
import de.uni_bremen.factroytrouble.player.ProgramCard;

/**
 * Implementiert das Logic Interface.
 * 
 * @author Roland
 *
 */
public class LogicOne implements Logic {

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.uni_bremen.factroytrouble.ai.Logic#simulateTurn(de.uni_bremen.factroytrouble.
     * board.Tile, int, de.uni_bremen.factroytrouble.player.ProgramCard)
     */
    @Override
    public PlacementUnit simulateTurn(Tile tile, int ori, ProgramCard card, Control control) {
        if (tile == null) {
            throw new IllegalArgumentException("null tile");
        }
        if (card == null) {
            throw new IllegalArgumentException("null card");
        }
        if (control == null) {
            throw new IllegalArgumentException("null control");
        }
        int newOri;
        Point newPoint;
        if (card instanceof TurnLeftCard) {
            newOri = (ori - 1) % 4;
            return new PlacementUnit(tile.getAbsoluteCoordinates(), oriAsOri(newOri), tile);
        } else if (card instanceof TurnRightCard) {
            newOri = (ori + 1) % 4;
            return new PlacementUnit(tile.getAbsoluteCoordinates(), oriAsOri(newOri), tile);
        } else if (card instanceof UturnCard) {
            newOri = (ori + 2) % 4;
            return new PlacementUnit(tile.getAbsoluteCoordinates(), oriAsOri(newOri), tile);
        } else if (card instanceof MoveBackwardCard) {
            newPoint = calcMovePos(tile.getAbsoluteCoordinates(), ori, -1);
        } else if (card instanceof MoveForwardCard && ((MoveForwardCard) card).getRange() == 1) {
            newPoint = calcMovePos(tile.getAbsoluteCoordinates(), ori, 1);
        } else if (card instanceof MoveForwardCard && ((MoveForwardCard) card).getRange() == 2) {
            newPoint = calcMovePos(tile.getAbsoluteCoordinates(), ori, 2);
        } else {
            newPoint = calcMovePos(tile.getAbsoluteCoordinates(), ori, 3);
        }
        return returnNewPlacement(ori, newPoint, control);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uni_bremen.factroytrouble.ai.Logic#calcMovePos(java.awt.Point, int,
     * int)
     */
    @Override
    public Point calcMovePos(Point start, int ori, int tilesMoved) {
        Point newPoint;
        switch (ori) {
        case 0:
            newPoint = new Point(start.x - tilesMoved, start.y);
            break;
        case 1:
            newPoint = new Point(start.x, start.y + tilesMoved);
            break;
        case 2:
            newPoint = new Point(start.x + tilesMoved, start.y);
            break;
        default:
            newPoint = new Point(start.x, start.y - tilesMoved);
        }
        return newPoint;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uni_bremen.factroytrouble.ai.Logic#oriAsOri(int)
     */
    @Override
    public Orientation oriAsOri(int ori) {
        switch (ori) {
        case 0:
            return Orientation.WEST;
        case 1:
            return Orientation.NORTH;
        case 2:
            return Orientation.EAST;
        default:
            return Orientation.SOUTH;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uni_bremen.factroytrouble.ai.Logic#oriAsInt(de.uni_bremen.factroytrouble.
     * gameobjects.Orientation)
     */
    @Override
    public int oriAsInt(Orientation ori) {
        switch (ori) {
        case WEST:
            return 0;
        case NORTH:
            return 1;
        case EAST:
            return 2;
        default:
            return 3;
        }
    }

    /**
     * Simuliert die Bewegung von Gears und Förderbändern.
     * 
     * @param oldPlace
     *            die alte Position
     * @return die neue Position
     */
    public Placement simulateGearAndBelt(Placement oldPlace, Control controller) {
        Tile oldTile = oldPlace.getTile();
        FieldObject fieldObject = null;
        if (oldTile != null) {
            fieldObject = oldPlace.getTile().getFieldObject();
        }
        if (fieldObject != null && fieldObject instanceof Gear) {
            if (((Gear) fieldObject).rotatesLeft()) {
                int oldOri = oriAsInt(oldPlace.getOrientation());
                oldPlace.setOrientation(oriAsOri((4 + oldOri - 1) % 4));
            } else {
                int oldOri = oriAsInt(oldPlace.getOrientation());
                oldPlace.setOrientation(oriAsOri((4 + oldOri + 1) % 4));
            }
        } else if (fieldObject != null && fieldObject instanceof ConveyorBelt) {
            ConveyorBelt cb = (ConveyorBelt) fieldObject;
            oldPlace.setPosition(calcMovePos(oldPlace.getPosition(), oriAsInt(cb.getOrientation()), 1));
            Object newTile = controller.requestData(null, oldPlace.getPosition());
            setTile(newTile, oldPlace);
            if (cb.isExpress() && newTile != null) {
                FieldObject nextFO = oldPlace.getTile().getFieldObject();
                if (nextFO != null && nextFO instanceof ConveyorBelt) {
                    oldPlace.setPosition(
                            calcMovePos(oldPlace.getPosition(), oriAsInt(((ConveyorBelt) nextFO).getOrientation()), 1));
                    newTile = controller.requestData(null, oldPlace.getPosition());
                    setTile(newTile, oldPlace);
                    // TODO calc change in orientation
                }
            }
        }
        return oldPlace;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.uni_bremen.factroytrouble.ai.Logic#getNeededCardsForMove(de.uni_bremen.
     * factroytrouble.ai.Logic.Move)
     */
    @Override
    public List<List<String>> getNeededCardsForMove(Move move) {
        switch (move) {
        case THREE:
            return putVariant("Three;Two,One;One,One,One");
        case TWO:
            return putVariant("Two;One,One;Three,BackUp");
        case ONE:
            return putVariant("One;Two,BackUp;Three,BackUp,BackUp");
        case MINUSONE:
            return putVariant("BackUp;UTurn,One;Left,Left,One;Right,Right,One");
        case UTURN:
            return putVariant("UTurn;Left,Left;Right,Right");
        case LEFT:
            return putVariant("Left;Right,UTurn;Right,Right,Right");
        default:
            return putVariant("Right;Left,UTurn;Left,Left,Left");
        }
    }

    /**
     * Gibt alle Programmkarten zurück, die noch benutzbar sind
     * 
     * @param cpo
     * @return
     */
    @Override
    public List<ProgramCard> retrieveUsableCards(CurrentPlannerOne cpo) {
        List<ProgramCard> allCards = cpo.getAllCards();
        List<Integer> globalUsedCards = cpo.getGlobalUsedCards();
        List<Integer> usedDirCards = cpo.getUsedDirCards();
        List<ProgramCard> usableCards = new LinkedList<ProgramCard>();
        for (int i = 0; i < allCards.size(); i++) {
            if (!globalUsedCards.contains(i) && !usedDirCards.contains(i)) {
                usableCards.add(allCards.get(i));
            }
        }
        return usableCards;
    }

    /* (non-Javadoc)
     * @see de.uni_bremen.factroytrouble.api.ki1.Logic#getEquivalentMoves(de.uni_bremen.factroytrouble.api.ki1.Logic.Move)
     */
    @Override
    public Set<Map<Move, Integer>> getEquivalentMoves(Move move) {
        Set<Map<Move, Integer>> moveSequences = new HashSet<>();
        switch (move) {
        case THREE:
            moveSequences.add(new HashMap<Move, Integer>() {
                {
                    put(Move.THREE, 1);
                }
            });
            moveSequences.add(new HashMap<Move, Integer>() {
                {
                    put(Move.TWO, 1);
                    put(Move.ONE, 1);
                }
            });
            moveSequences.add(new HashMap<Move, Integer>() {
                {
                    put(Move.ONE, 3);
                }
            });
            moveSequences.add(new HashMap<Move, Integer>() {
                {
                    put(Move.TWO, 2);
                    put(Move.MINUSONE, 1);
                }
            });
            break;
        case TWO:
            moveSequences.add(new HashMap<Move, Integer>() {
                {
                    put(Move.TWO, 1);
                }
            });
            moveSequences.add(new HashMap<Move, Integer>() {
                {
                    put(Move.ONE, 2);
                }
            });
            moveSequences.add(new HashMap<Move, Integer>() {
                {
                    put(Move.THREE, 1);
                    put(Move.MINUSONE, 1);
                }
            });
            moveSequences.add(new HashMap<Move, Integer>() {
                {
                    put(Move.TWO, 1);
                    put(Move.ONE, 1);
                    put(Move.MINUSONE, 1);
                }
            });
            break;
        case ONE:
            moveSequences.add(new HashMap<Move, Integer>() {
                {
                    put(Move.ONE, 1);
                }
            });
            moveSequences.add(new HashMap<Move, Integer>() {
                {
                    put(Move.TWO, 1);
                    put(Move.MINUSONE, 1);
                }
            });
            moveSequences.add(new HashMap<Move, Integer>() {
                {
                    put(Move.THREE, 1);
                    put(Move.MINUSONE, 2);
                }
            });
            break;
        case MINUSONE:
            moveSequences.add(new HashMap<Move, Integer>() {
                {
                    put(Move.MINUSONE, 1);
                }
            });
            moveSequences.add(new HashMap<Move, Integer>() {
                {
                    put(Move.UTURN, 2);
                    put(Move.ONE, 1);
                }
            });
            break;
        case RIGHT:
            moveSequences.add(new HashMap<Move, Integer>() {
                {
                    put(Move.RIGHT, 1);
                }
            });
            moveSequences.add(new HashMap<Move, Integer>() {
                {
                    put(Move.LEFT, 3);
                }
            });
            moveSequences.add(new HashMap<Move, Integer>() {
                {
                    put(Move.LEFT, 1);
                    put(Move.UTURN, 1);
                }
            });
            moveSequences.add(new HashMap<Move, Integer>() {
                {
                    put(Move.RIGHT, 1);
                    put(Move.UTURN, 2);
                }
            });
            break;
        case LEFT:
            moveSequences.add(new HashMap<Move, Integer>() {
                {
                    put(Move.LEFT, 1);
                }
            });
            moveSequences.add(new HashMap<Move, Integer>() {
                {
                    put(Move.RIGHT, 3);
                }
            });
            moveSequences.add(new HashMap<Move, Integer>() {
                {
                    put(Move.RIGHT, 1);
                    put(Move.UTURN, 1);
                }
            });
            moveSequences.add(new HashMap<Move, Integer>() {
                {
                    put(Move.LEFT, 1);
                    put(Move.UTURN, 2);
                }
            });
            break;
        case UTURN:
            moveSequences.add(new HashMap<Move, Integer>() {
                {
                    put(Move.UTURN, 1);
                }
            });
            moveSequences.add(new HashMap<Move, Integer>() {
                {
                    put(Move.LEFT, 2);
                }
            });
            moveSequences.add(new HashMap<Move, Integer>() {
                {
                    put(Move.RIGHT, 2);
                }
            });
            break;
        default:
            assert false;
        }
        return moveSequences;
    }

    /* (non-Javadoc)
     * @see de.uni_bremen.factroytrouble.api.ki1.Logic#canPerformMove(de.uni_bremen.factroytrouble.api.ki1.Logic.Move, java.util.List)
     */
    @Override
    public Map<Move, Integer> canPerformMove(Move move, List<Move> moves) {
        Map<Move, Integer> frequencyTable = getFrequencyTable(moves);
        Set<Map<Move, Integer>> moveSequences = getEquivalentMoves(move);
        outerLoop: for (Map<Move, Integer> moveSequence : moveSequences) {
            int correctFrequencies = 0;
            for (Map.Entry<Move, Integer> entry : moveSequence.entrySet()) {
                Move move1 = entry.getKey();
                // Wenn einer der benötigten Bewegungen gar nicht gemacht werden
                // kann,
                // probiere nächste mögliche Sequenz
                if (!moves.contains(move1)) {
                    continue outerLoop;
                }
                Integer requiredFrequency = entry.getValue();
                Integer givenFrequency = frequencyTable.get(move1);
                if (givenFrequency >= requiredFrequency) {
                    correctFrequencies++;
                }
            }
            // Es gibt genügend viele geforderte Bewegungen
            if (correctFrequencies == moveSequence.size()) {
                return moveSequence;
            }
        }
        return new HashMap<Move, Integer>();
    }

    /**
     * Zählt alle Vorkommen der Bewegungen und gibt sie als Häufigkeitstabelle
     * zurück.
     *
     * @author Simon Liedtke
     */
    @Override
    public Map<Move, Integer> getFrequencyTable(List<Move> moves) {
        Map<Move, Integer> table = new HashMap<>();
        for (Move move : new HashSet<Move>(moves)) {
            int frequency = Collections.frequency(moves, move);
            table.put(move, frequency);
        }
        return table;
    }

    /* (non-Javadoc)
     * @see de.uni_bremen.factroytrouble.api.ki1.Logic#tryMinTurns(java.util.List)
     */
    @Override
    public Map<Move, Integer> tryMinTurns(List<Move> moves) {
        return tryMinTurns(moves, moves.size());
    }

    /* (non-Javadoc)
     * @see de.uni_bremen.factroytrouble.api.ki1.Logic#goMovesOnly(java.util.List, int)
     */
    @Override
    public Map<Move, Integer> goMovesOnly(List<Move> moves, int steps) {
        Map<Move, Integer> mooves = getFrequencyTable(moves);
        return goMovesOnlyAcc(mooves, steps);
    }

    /**TODO Simon
     * @param accMoves
     * @param steps
     * @return
     */
    public Map<Move, Integer> goMovesOnlyAcc(Map<Move, Integer> accMoves, int steps) {
        int steps_ = steps;
        switch (steps) {
        case 0:
            return accMoves;
        case -1:
            steps_ = accMoves.get(Move.MINUSONE);
            accMoves.put(Move.MINUSONE, steps_ + 1);
            return goMovesOnlyAcc(accMoves, steps_ + 1);
        case 1:
            steps_ = accMoves.get(Move.ONE);
            accMoves.put(Move.ONE, steps_ + 1);
            return goMovesOnlyAcc(accMoves, steps_ - 1);
        case 2:
            steps_ = accMoves.get(Move.TWO);
            accMoves.put(Move.TWO, steps_ + 1);
            return goMovesOnlyAcc(accMoves, steps_ - 1);
        case 3:
        default:
            steps_ = accMoves.get(Move.THREE);
            accMoves.put(Move.THREE, steps_ + 1);
            return goMovesOnlyAcc(accMoves, steps_ - 1);
        }
    }

    /** TODO Simon
     * @param moves
     * @param maxNumOfMoves
     * @return
     */
    public Map<Move, Integer> tryMinTurns(List<Move> moves, int maxNumOfMoves) {
        List<Move> turnMoves = new ArrayList<Move>();
        Map<Move, Integer> frequencyTable = getFrequencyTable(moves);

        // lege Karten, bis maximale Zahl erreicht wurde
        while (turnMoves.size() < maxNumOfMoves) {
            Integer numOfLefts = frequencyTable.getOrDefault(Move.LEFT, new Integer(0));
            Integer numOfRights = frequencyTable.getOrDefault(Move.RIGHT, new Integer(0));

            // können mindestens 4 Karten gelegt werden?
            if (maxNumOfMoves >= 4 && turnMoves.size() + 4 <= maxNumOfMoves) {
                // können min. 4 Links-Karten gelegt werden?
                if (numOfLefts >= 4) {
                    for (int i = 0; i < 4; i++) {
                        turnMoves.add(Move.LEFT);
                    }
                    // verringer die Anzahl der Linksdrehungen in Tabelle um 4
                    frequencyTable.put(Move.LEFT, numOfLefts - 4);
                }
                // können min. 4 Rechts-Karten gelegt werden?
                else if (numOfRights >= 4) {
                    for (int i = 0; i < 4; i++) {
                        turnMoves.add(Move.RIGHT);
                    }
                    // verringer die Anzahl der Rechtsdrehungen in Tabelle um 4
                    frequencyTable.put(Move.RIGHT, numOfRights - 4);
                }
                // können 2 Links- und 2 Rechts-Karten gelegt werden?
                else if (numOfLefts >= 2 && numOfRights >= 2) {
                    for (int i = 0; i < 2; i++) {
                        turnMoves.add(Move.LEFT);
                        turnMoves.add(Move.RIGHT);
                    }
                    // verringer die Anzahl der Links- und Rechtsdrehungen in
                    // Tabelle jeweils um 2
                    frequencyTable.put(Move.LEFT, numOfLefts - 2);
                    frequencyTable.put(Move.RIGHT, numOfRights - 2);
                }
            }
            // können mindestens 2 Karten gelegt werden?
            else if (maxNumOfMoves >= 2 && turnMoves.size() + 2 <= maxNumOfMoves) {
                if (numOfLefts >= 1 && numOfRights >= 1) {
                    turnMoves.add(Move.LEFT);
                    turnMoves.add(Move.RIGHT);
                    // verringer die Anzahl der Links- und Rechtsdrehungen in
                    // Tabelle jeweils um 1
                    frequencyTable.put(Move.LEFT, numOfLefts - 1);
                    frequencyTable.put(Move.RIGHT, numOfRights - 1);
                }
            }
            // es können Karten gelegt werden, aber nur noch eine -> lege diese
            // und brich ab
            else {
                if (numOfLefts > 0) {
                    turnMoves.add(Move.LEFT);
                } else if (numOfRights > 0) {
                    turnMoves.add(Move.RIGHT);
                } else {
                    for (Map.Entry<Move, Integer> entry : frequencyTable.entrySet()) {
                        if (entry.getValue() > 1) {
                            turnMoves.add(entry.getKey());
                            break;
                        }
                    }
                }
                break;
            }

        }
        return getFrequencyTable(turnMoves);
    }

    /* (non-Javadoc)
     * @see de.uni_bremen.factroytrouble.api.ki1.Logic#programCards2Moves(java.util.List)
     */
    @Override
    public List<Move> programCards2Moves(List<ProgramCard> cards) {
        List<Move> moves = new ArrayList<Move>();
        for (ProgramCard card : cards) {
            moves.add(programCard2Move(card));
        }
        return moves;
    }

    /* (non-Javadoc)
     * @see de.uni_bremen.factroytrouble.api.ki1.Logic#programCard2Move(de.uni_bremen.factroytrouble.player.ProgramCard)
     */
    @Override
    public Move programCard2Move(ProgramCard card) {
        if (card instanceof MoveForwardCard) {
            switch (card.getRange()) {
            case 1:
                return Move.ONE;
            case 2:
                return Move.TWO;
            case 3:
                return Move.THREE;
            default:
                throw new RuntimeException(
                        String.format("unexpected value for card.getRange(): '%d'", card.getRange()));
            }
        } else if (card instanceof MoveBackwardCard) {
            return Move.MINUSONE;
        } else if (card instanceof TurnLeftCard) {
            return Move.LEFT;
        } else if (card instanceof TurnRightCard) {
            return Move.RIGHT;
        } else {
            throw new RuntimeException(String.format("unexpected type of card: '%s'", card.getClass().getName()));
        }
    }

    /* (non-Javadoc)
     * @see de.uni_bremen.factroytrouble.api.ki1.Logic#cardAndMoveMatch(de.uni_bremen.factroytrouble.player.ProgramCard, de.uni_bremen.factroytrouble.api.ki1.Logic.Move)
     */
    @Override
    public boolean cardAndMoveMatch(ProgramCard card, Move move) {
        return card instanceof MoveForwardCard
                && (move.equals(Move.ONE) || move.equals(Move.TWO) || move.equals(Move.THREE))
                || card instanceof MoveBackwardCard && move.equals(Move.MINUSONE)
                || card instanceof TurnLeftCard && move.equals(Move.LEFT)
                || card instanceof TurnRightCard && move.equals(Move.RIGHT);
    }

    /* (non-Javadoc)
     * @see de.uni_bremen.factroytrouble.api.ki1.Logic#frequencytable2cardlist(java.util.Map, java.util.List)
     */
    @Override
    public List<ProgramCard> frequencytable2cardlist(Map<Move, Integer> moves, List<ProgramCard> availableCards) {
        List<ProgramCard> cards = new ArrayList<ProgramCard>();
        for (Map.Entry<Move, Integer> entry : moves.entrySet()) {
            List<ProgramCard> tempCards = tempGetCards(entry, availableCards);
            cards.addAll(tempCards);
        }
        return cards;
    }

    /**TODO Simon
     * @param frequencyTableEntry
     * @param availableCards
     * @return
     */
    public List<ProgramCard> tempGetCards(Map.Entry<Move, Integer> frequencyTableEntry,
            List<ProgramCard> availableCards) {
        List<ProgramCard> cards = new ArrayList<ProgramCard>();
        Move move = frequencyTableEntry.getKey();
        Integer frequency = frequencyTableEntry.getValue();

        // hole ``frequency`` oft eine Karte aus ``cards``, die ``move``
        // entspricht
        for (ProgramCard card : availableCards) {
            if (cards.size() == frequency) {
                break;
            }
            if (cardAndMoveMatch(card, move)) {
                cards.add(card);
            }
        }
        return cards;
    }

    private PlacementUnit returnNewPlacement(int ori, Point newPoint, Control control) {
        Object newTile = control.requestData(null, newPoint);
        if (newTile instanceof Tile) {
            return new PlacementUnit(newPoint, oriAsOri(ori), (Tile) newTile);
        } else {
            return new PlacementUnit(newPoint, oriAsOri(ori), null);
        }
    }

    /**
     * Hilfsfunktion für simulateGearAndBelt welche prüft ob eine Rückgabe vom
     * Controller ein Tile ist und dieses in ein Placement speichert.
     * 
     * @param newTile
     *            neues Tile oder null
     * @param oldPlace
     *            ales Placement
     * @return neues Placement oder null
     */
    private Placement setTile(Object newTile, Placement oldPlace) {
        if (newTile instanceof Tile) {
            oldPlace.setTile((Tile) newTile);
        } else {
            oldPlace.setTile(null);
        }
        return oldPlace;
    }

    /**
     * Zerlegt einen String von mehreren Kartenkombinationen in eine Liste von
     * Listen von Strings, bei der jeder äußere Eintrag eine Variante zur
     * durchführugn einer Bewegung darstellt und ein innerer Eintrag eine Karte.
     * 
     * @param cards
     *            der String mit den Varianten
     * @return die Varianten als geschachtelte Liste
     */
    private List<List<String>> putVariant(String cards) {
        List<String> cardsList = new LinkedList<String>();
        List<List<String>> variants = new LinkedList<List<String>>();
        String[] variantsOfMove = cards.split(";");
        String[] cardsOfVariant;
        // iteriere über die Varianten
        for (int i = 0; i < variantsOfMove.length; i++) {
            if (variantsOfMove[i] != null) {
                // splitte die Variante in Karten
                cardsOfVariant = variantsOfMove[i].split(",");
                // iteriere über Karten der Variante und füge sie in Liste für
                // die Variante ein
                for (int j = 0; j < cardsOfVariant.length; j++) {
                    if (cardsOfVariant[j] != null) {
                        cardsList.add(cardsOfVariant[j]);
                    }
                }
                // füge die generierte Variante der Liste der Varianten hinzu
                List<String> cardsListCopy = new LinkedList<String>(cardsList);
                variants.add(cardsListCopy);
                cardsList.clear();
            }

        }
        return variants;

    }
}
