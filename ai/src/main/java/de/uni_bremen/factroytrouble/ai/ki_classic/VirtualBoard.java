/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki_classic;

import de.uni_bremen.factroytrouble.ai.ki_classic.path_finding.dykstra.DykstraHandler;
import de.uni_bremen.factroytrouble.api.ki_classic.ActivateBoardAnswer;
import de.uni_bremen.factroytrouble.api.ki_classic.ImpossibleOrientationException;
import de.uni_bremen.factroytrouble.api.ki_classic.MoveDirectionAnswer;
import de.uni_bremen.factroytrouble.board.Field;
import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.gameobjects.*;
import de.uni_bremen.factroytrouble.player.*;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Stellt eine eigene Repraesentation des Bretts zur Verfuegung und enthaelt
 * viel der Logik der klassischen KI.
 * 
 * 
 * @author Markus, johannes.gesenhues
 */
public class VirtualBoard {
    private static final int WALL_EAST = 0; // size 1
    private static final int WALL_NORTH = 1;// size 1
    private static final int WALL_WEST = 2;// size 1
    private static final int WALL_SOUTH = 3;// size 1
    private static final int EAST_LASER = 4;// size 2
    private static final int NORTH_LASER = 6;// size 2
    private static final int WEST_LASER = 8;// size 2
    private static final int SOUTH_LASER = 10;// size 2
    private static final int GROUND_DESCRIPTION = 12;// size 3
    private static final int HOLE = 1;
    private static final int SPINNER = 2;
    private static final int CONVEYOR_BELT = 3;
    private static final int FLAG = 4;
    private static final int WORKSHOP = 5;
    private static final int SPINNER_CLOCKWISE = 15;// size 1
    private static final int CONVEYOR_BELT_FAST = 15;// size 1
    private static final int CONVEYOR_BELT_DIRECTION = 16;// size 2
    private static final int FLAG_NUMBER = 15;// size 3
    private static final int PUSHER_DIRECTION = 18;// size 2
    private static final int PUSHER_FIRST = 20;// size 1
    private final Master master;
    private final Map<Point, Tile> tileMap;
    private final DykstraHandler dykstra;
    private int maxX;
    private int minY;
    private int minX;
    private int maxY;
    private int[][] boardArray;
    private int min;

    /**
     * Erzeugt neue Instanz.
     * 
     * @param master
     *            zu benutzender master fuer Referenzen.
     * @param dykstra
     *            zu benutzender Dykstra.
     */
    public VirtualBoard(Master master, DykstraHandler dykstra) {
        this.master = master;
        this.tileMap = new HashMap<>();
        maxX = 0;
        minY = 0;
        minX = 0;
        maxY = 0;
        Map<Point, Tile> tileMapLocal = convertBoardToMap(master.getBoard().getFields());
        extractBoard(tileMapLocal);
        this.dykstra = dykstra;
    }

    /*
     * min wird am Anfang einer Zugberechnung auf Integer.MAX_VALUE gesetzt.
     */
    public void resetMin() {
        min = Integer.MAX_VALUE;
    }

    /**
     * macht einen Zug fuer den angegebenen Spieler. Ruft im wesentlichen
     * recursivelyIterateCards auf.
     *
     * @param player
     *            der Spieler, fuer den der Zug gemacht werden soll.
     */
    public int[] performRoundVirtualRound(Player player) {
        resetMin();
        int[] cardChoices = new int[5];
        recursivelyIterateCards(player.getPlayerCards(), player.getRobot().getCurrentTile().getAbsoluteCoordinates(),
                player.getRobot().getOrientation(), new int[5], 0, player.getRobot().getFlagCounterStatus() + 1,
                cardChoices, player);
        return cardChoices;
    }

    /*
     * kommt val in list zwischen low und high vor?
     */
    private boolean contains(int[] list, int val, int low, int high) {
        for (int i = low; i < high && i < list.length; ++i) {
            if (list[i] == val) {
                return true;
            }
        }
        return false;
    }

    /*
     * gegeben das Brett board und die Position point, wo bin ich wenn ich mich
     * nach dir bewege?
     */
    private Point moveOneStepInDirection(int[][] board, Point point, Orientation dir) {
        if (point == null || point.x < 0 || point.y < 0) {
            return new Point(-1, -1);
        }
        switch (dir) {
        case EAST:
            return eastCase(board, point);
        case NORTH:
            return northCase(board, point);
        case WEST:
            return westCase(board, point);
        case SOUTH:
            return southCase(board, point);
        default:
            throw new ImpossibleOrientationException(dir);
        }
    }

    private Point eastCase(int[][] board, Point point) {
        Point answer = new Point();
        if ((board[point.x][point.y] >> WALL_EAST) % 2 == 1) {
            answer = point;
        } else if (point.x + 1 == board.length) {
            answer = new Point(-1, -1);
        } else if ((board[point.x + 1][point.y] >> WALL_WEST) % 2 == 1) {
            answer = point;
        } else if ((board[point.x + 1][point.y] >> GROUND_DESCRIPTION) % 8 == HOLE) {
            answer = new Point(-1, -1);
        } else {
            answer = new Point(point.x + 1, point.y);
        }
        return answer;
    }

    private Point northCase(int[][] board, Point point) {
        Point answer = new Point();

        if ((board[point.x][point.y] >> WALL_NORTH) % 2 == 1) {
            answer = point;
        } else if (point.y + 1 == board[point.x].length) {
            answer = new Point(-1, -1);
        } else if ((board[point.x][point.y + 1] >> WALL_SOUTH) % 2 == 1) {
            answer = point;
        } else if ((board[point.x][point.y + 1] >> GROUND_DESCRIPTION) % 8 == HOLE) {
            answer = new Point(-1, -1);
        } else {
            answer = new Point(point.x, point.y + 1);
        }
        return answer;
    }

    private Point westCase(int[][] board, Point point) {
        Point answer = new Point();

        if ((board[point.x][point.y] >> WALL_WEST) % 2 == 1) {
            answer = point;
        } else if (point.x == 0) {
            answer = new Point(-1, -1);
        } else if ((board[point.x - 1][point.y] >> WALL_EAST) % 2 == 1) {
            answer = point;
        } else if ((board[point.x - 1][point.y] >> GROUND_DESCRIPTION) % 8 == HOLE) {
            answer = new Point(-1, -1);
        } else {
            answer = new Point(point.x - 1, point.y);
        }
        return answer;
    }

    private Point southCase(int[][] board, Point point) {
        Point answer = new Point();

        if ((board[point.x][point.y] >> WALL_SOUTH) % 2 == 1) {
            answer = point;
        } else if (point.y == 0) {
            answer = new Point(-1, -1);
        } else if ((board[point.x][point.y - 1] >> WALL_NORTH) % 2 == 1) {
            answer = point;
        } else if ((board[point.x][point.y - 1] >> GROUND_DESCRIPTION) % 8 == HOLE) {
            answer = new Point(-1, -1);
        } else {
            answer = new Point(point.x, point.y - 1);
        }
        return answer;
    }

    /*
     * auf dem board b steht ein Roboter auf p, sieht nacht rotation, und spielt
     * card. Wohin bewegt ihn die Karte?
     */
    private MoveDirectionAnswer apply(int[][] b, ProgramCard card, Point p, Orientation rotation) {
        MoveDirectionAnswer answer = new MoveDirectionAnswer();
        answer.setPoint(p);
        answer.setOrientation(rotation);
        if (card instanceof TurnLeftCard) {
            answer.setOrientation(Orientation.getNextDirection(rotation, true));
        } else if (card instanceof TurnRightCard) {
            answer.setOrientation(Orientation.getNextDirection(rotation, false));
        } else if (card instanceof UturnCard) {
            answer.setOrientation(Orientation.getOppositeDirection(rotation));
        } else if (card instanceof MoveBackwardCard) {
            Point temp = p;
            for (int i = 0; i > card.getRange(); --i) {
                temp = moveOneStepInDirection(b, temp, Orientation.getOppositeDirection(rotation));
            }
            answer.setPoint(temp);
        } else if (card instanceof MoveForwardCard) {
            if (card.getRange() < 0) {
                Point temp = p;
                for (int i = 0; i > card.getRange(); --i) {
                    temp = moveOneStepInDirection(b, temp, Orientation.getOppositeDirection(rotation));
                }
            } else {
                Point temp = p;
                for (int i = 0; i < card.getRange(); ++i) {
                    temp = moveOneStepInDirection(b, temp, rotation);
                }
                answer.setPoint(temp);
            }
        }
        return answer;
    }

    /*
     * Ausgelagerte Statements aus activateBoard()
     */
    private boolean isProperPoint(Point p) {
        return p != null && p.x >= 0 && p.y >= 0;
    }

    private boolean isExpressConveyorBelt(int[][] b, Point pCopy) {
        return isProperPoint(pCopy) && ((b[pCopy.x][pCopy.y] >> GROUND_DESCRIPTION) % 8 == CONVEYOR_BELT)
                && ((b[pCopy.x][pCopy.y] >> CONVEYOR_BELT_FAST) % 2 == 1);
    }

    private boolean isConveyorBelt(int[][] b, Point pCopy) {
        return isProperPoint(pCopy) && ((b[pCopy.x][pCopy.y] >> GROUND_DESCRIPTION) % 8 == CONVEYOR_BELT);
    }

    private boolean isPusherInThisPhase(int[][] b, Point pCopy, int registerPhase) {
        return isProperPoint(pCopy) && ((b[pCopy.x][pCopy.y] >> (PUSHER_FIRST + registerPhase)) % 2 == 1);
    }

    private boolean isSpinner(int[][] b, Point pCopy) {
        return isProperPoint(pCopy) && ((b[pCopy.x][pCopy.y] >> GROUND_DESCRIPTION) % 8 == SPINNER);
    }

    private boolean isFlag(int[][] b, Point pCopy) {
        return isProperPoint(pCopy) && ((b[pCopy.x][pCopy.y] >> GROUND_DESCRIPTION) % 8 == FLAG);
    }

    /*
     * ein Aktivieren der Brettelemente
     */
    private ActivateBoardAnswer activateBoard(int[][] b, Point p, Orientation r, int registerPhase, int flagToReach) {
        Orientation rCopy = r;
        Point pCopy = p;
        if (isExpressConveyorBelt(b, pCopy)) {
            int or1 = (b[pCopy.x][pCopy.y] >> CONVEYOR_BELT_DIRECTION) % 4;
            pCopy = moveOneStepInDirection(b, pCopy, Orientation.values()[or1]);
            if (isConveyorBelt(b, pCopy)) {
                int or2 = (b[pCopy.x][pCopy.y] >> CONVEYOR_BELT_DIRECTION) % 4;
                rCopy = Orientation.values()[(rCopy.ordinal() + or2 - or1 + 4) % 4];
            }
        }
        if (isConveyorBelt(b, pCopy)) {
            int or1 = (b[pCopy.x][pCopy.y] >> CONVEYOR_BELT_DIRECTION) % 4;
            pCopy = moveOneStepInDirection(b, pCopy, Orientation.values()[or1]);
            if (isConveyorBelt(b, pCopy)) {
                int or2 = (b[pCopy.x][pCopy.y] >> CONVEYOR_BELT_DIRECTION) % 4;
                rCopy = Orientation.values()[(rCopy.ordinal() + or2 - or1 + 4) % 4];
            }
        }

        if (isPusherInThisPhase(b, pCopy, registerPhase)) {
            pCopy = moveOneStepInDirection(b, pCopy,
                    Orientation.values()[(b[pCopy.x][pCopy.y] >> PUSHER_DIRECTION) % 4]);
        }

        if (isSpinner(b, pCopy)) {
            boolean clockwise = (b[pCopy.x][pCopy.y] >> SPINNER_CLOCKWISE) % 2 == 1;
            rCopy = Orientation.getNextDirection(rCopy, !clockwise);
        }

        boolean flagReached = false;
        if (isFlag(b, pCopy)) {
            int flagNum = (b[pCopy.x][pCopy.y] >> FLAG_NUMBER) % 8;
            if (flagNum == flagToReach) {
                flagReached = true;
            }
        }

        return new ActivateBoardAnswer(pCopy, rCopy, flagReached);
    }

    /*
     * Map wird zu integer-array. benutzt die bit-encodings am Anfang der
     * Klasse.
     */
    private void extractBoard(Map<Point, Tile> tileMap) {
        boardArray = new int[maxX + 1 - minX][maxY + 1 - minY];
        tileMap.forEach((pos, tile) -> {
            int val = 0;
            if (tile.hasWall(Orientation.EAST)) {
                val += 1 << WALL_EAST;
                Wall w = tile.getWall(Orientation.EAST);
                val += w.hasLaser() << EAST_LASER;
                int[] phases = w.getPusherPhases();
                boolean pushes = false;
                if (phases != null) {
                    for (int i = 0; i < 5; ++i) {
                        if (contains(phases, i, 0, 5)) {
                            val += 1 << (PUSHER_FIRST + i);
                            if (!pushes) {
                                val += 0 << PUSHER_DIRECTION;
                                pushes = true;
                            }
                        }
                    }
                }
                // assume laserMag <= 3
            }
            if (tile.hasWall(Orientation.NORTH)) {
                val += 1 << WALL_NORTH;
                Wall w = tile.getWall(Orientation.NORTH);
                val += w.hasLaser() << NORTH_LASER;
                int[] phases = w.getPusherPhases();
                boolean pushes = false;
                if (phases != null) {
                    for (int i = 0; i < 5; ++i) {
                        if (contains(phases, i, 0, 5)) {
                            val += 1 << (PUSHER_FIRST + i);
                            if (!pushes) {
                                val += 1 << PUSHER_DIRECTION;
                                pushes = true;
                            }
                        }
                    }
                }

            }
            if (tile.hasWall(Orientation.WEST)) {
                val += 1 << WALL_WEST;
                Wall w = tile.getWall(Orientation.WEST);
                val += w.hasLaser() << WEST_LASER;
                int[] phases = w.getPusherPhases();
                boolean pushes = false;
                if (phases != null) {
                    for (int i = 0; i < 5; ++i) {
                        if (contains(phases, i, 0, 5)) {
                            val += 1 << (PUSHER_FIRST + i);
                            if (!pushes) {
                                val += 2 << PUSHER_DIRECTION;
                                pushes = true;
                            }
                        }
                    }
                }

            }
            if (tile.hasWall(Orientation.SOUTH)) {
                val += 1 << WALL_SOUTH;
                Wall w = tile.getWall(Orientation.SOUTH);
                val += w.hasLaser() << SOUTH_LASER;
                int[] phases = w.getPusherPhases();
                boolean pushes = false;
                if (phases != null) {
                    for (int i = 0; i < 5; ++i) {
                        if (contains(phases, i, 0, 5)) {
                            val += 1 << (PUSHER_FIRST + i);
                            if (!pushes) {
                                val += 3 << PUSHER_DIRECTION;
                                pushes = true;
                            }
                        }
                    }
                }

            }
            if (tile.getFieldObject() instanceof Hole) {
                val += HOLE << GROUND_DESCRIPTION;
            }
            if (tile.getFieldObject() instanceof Gear) {
                val += SPINNER << GROUND_DESCRIPTION;
                if (!((Gear) tile.getFieldObject()).rotatesLeft()) {
                    val += 1 << SPINNER_CLOCKWISE;
                }
            }
            if (tile.getFieldObject() instanceof ConveyorBelt) {
                val += CONVEYOR_BELT << GROUND_DESCRIPTION;
                if (((ConveyorBelt) tile.getFieldObject()).isExpress()) {
                    val += 1 << CONVEYOR_BELT_FAST;
                }
                Orientation o = ((ConveyorBelt) tile.getFieldObject()).getOrientation();
                if (o == Orientation.NORTH) {
                    val += 1 << CONVEYOR_BELT_DIRECTION;
                }
                if (o == Orientation.WEST) {
                    val += 2 << CONVEYOR_BELT_DIRECTION;
                }
                if (o == Orientation.SOUTH) {
                    val += 3 << CONVEYOR_BELT_DIRECTION;
                }
            }
            if (tile.getFieldObject() instanceof Flag) {
                val += FLAG << GROUND_DESCRIPTION;
                val += ((Flag) tile.getFieldObject()).getNumber() << FLAG_NUMBER;
            }
            if (tile.getFieldObject() instanceof Workshop) {
                val += WORKSHOP << GROUND_DESCRIPTION;
            }

            boardArray[pos.x - minX][pos.y - minY] = val;

        });
    }

    /*
     * Convertiert die Map von Feldern in eine Map mit allen Tiles, mit den
     * Absoluten Coordinaten als Key
     */
    private Map<Point, Tile> convertBoardToMap(Map<Point, Field> fieldMap) {
        fieldMap.values().forEach(field -> addAllTilesToMap(field.getTiles()));
        return tileMap;
    }

    /*
     * Diese Methode fügt alle Tiles eines Feldes zur tileMap hinzu, wobei die
     * max und min Coordinaten gefunden werden und die Positionen in Absolute
     * Positionen umgerechnet werdeniein Feld
     *
     * @param map Die Tile Map eines Feldes
     */
    private void addAllTilesToMap(Map<Point, Tile> map) {
        map.values().forEach(tile -> {
            Point absolutePoint = tile.getAbsoluteCoordinates();
            if (maxX < absolutePoint.x)
                maxX = absolutePoint.x;
            if (maxY < absolutePoint.y)
                maxY = absolutePoint.y;
            if (minX > absolutePoint.x)
                minX = absolutePoint.x;
            if (minY > absolutePoint.y)
                minY = absolutePoint.y;
            tileMap.put(absolutePoint, tile);
        });
    }

    /**
     * Spielt eine Karte, und rekursiv alle weiteren fuer diesen Zug.
     * 
     * @param cards
     *            die Liste der verfügbaren Karten. wird nie modifiziert.
     * @param pCopy
     *            der Ort, von dem gedanklich gerade ausgegangen wird.
     * @param orientation
     *            die Ausrichtung, von der gedanklich gerade ausgegangen wird.
     * @param choiceProgress
     *            Indices relativ zu cards der bis jetzt gewählten karten. -1 wo
     *            ein Register gelockt ist.
     * @param choiceIndex
     *            bis wo ist choiceProgress gefüllt?
     * @param nextFlagToReach
     *            das Ziel, das angelaufen wird.
     * @param bestAnswerSoFar
     *            die bis jetzt günstigste Kartenauswahl.
     * @param player
     *            der Spieler, um den es geht.
     */
    private void recursivelyIterateCards(List<ProgramCard> cards, Point p, Orientation orientation,
            int[] choiceProgress,

            int choiceIndex, int nextFlagToReach, int[] bestAnswerSoFar, Player player) {
        Point pCopy = p;
        if (choiceIndex == choiceProgress.length) {
            if (pCopy == null) {
                pCopy = new Point(-1, -1);
            }

            if (nextFlagToReach > master.getBoard().getHighestFlagNumber()) {
                if (min > 0) {
                    min = 0;
                    for (int i = 0; i < bestAnswerSoFar.length && i < choiceProgress.length; ++i) {
                        bestAnswerSoFar[i] = choiceProgress[i];
                    }
                }
            } else {
                int val = dykstra.getWeight(nextFlagToReach, pCopy, orientation);
                if (val < min) {
                    min = val;
                    for (int i = 0; i < bestAnswerSoFar.length && i < choiceProgress.length; ++i) {
                        bestAnswerSoFar[i] = choiceProgress[i];
                    }
                }

            }
        } else {
            if (player.getRobot().registerLockStatus()[choiceIndex]) {
                choiceProgress[choiceIndex] = -1;
                MoveDirectionAnswer moveDirectionAnswer = apply(boardArray,
                        player.getRobot().getRegisters()[choiceIndex], pCopy, orientation);
                ActivateBoardAnswer nextState = activateBoard(boardArray, moveDirectionAnswer.getPoint(),
                        moveDirectionAnswer.getOrientation(), choiceIndex, nextFlagToReach);
                recursivelyIterateCards(cards, nextState.getPoint(), nextState.getOrientation(), choiceProgress,
                        1 + choiceIndex, nextFlagToReach + (nextState.isFlagReached() ? 1 : 0), bestAnswerSoFar,
                        player);
            } else {
                for (int i = 0; i < cards.size(); ++i) {
                    if (contains(choiceProgress, i, 0, choiceIndex)) {
                        continue;
                    }
                    choiceProgress[choiceIndex] = i;
                    MoveDirectionAnswer moveDirectionAnswer = apply(boardArray, cards.get(i), pCopy, orientation);
                    ActivateBoardAnswer nextState = activateBoard(boardArray, moveDirectionAnswer.getPoint(),
                            moveDirectionAnswer.getOrientation(), choiceIndex, nextFlagToReach);
                    recursivelyIterateCards(cards, nextState.getPoint(), nextState.getOrientation(), choiceProgress,
                            1 + choiceIndex, nextFlagToReach + (nextState.isFlagReached() ? 1 : 0), bestAnswerSoFar,
                            player);
                }
            }
        }
    }
}
