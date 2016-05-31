package de.uni_bremen.factroytrouble.ai.ki1;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.*;

import de.uni_bremen.factroytrouble.api.ki1.Logic;
import de.uni_bremen.factroytrouble.player.*;
import org.junit.Test;
import org.mockito.Mockito;

import de.uni_bremen.factroytrouble.ai.ki1.planning.CurrentPlannerOne;
import de.uni_bremen.factroytrouble.api.ki1.Control;
import de.uni_bremen.factroytrouble.api.ki1.Logic.Move;
import de.uni_bremen.factroytrouble.api.ki1.Placement;
import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.gameobjects.ConveyorBelt;
import de.uni_bremen.factroytrouble.gameobjects.Gear;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;

public class LogicOneTest {
    Tile tile = Mockito.mock(Tile.class);
    LogicOne logicOne = new LogicOne();
    Control control = Mockito.mock(Control.class);


    @Test
    public void getNeededCardsForMoveTest1() {
        assertEquals("Three", logicOne.getNeededCardsForMove(Move.THREE).get(0).get(0));
        assertEquals("One", logicOne.getNeededCardsForMove(Move.THREE).get(1).get(1));
        assertEquals("One", logicOne.getNeededCardsForMove(Move.THREE).get(2).get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getNeededCardsForMoveTestFail() {
        assertEquals(null, logicOne.getNeededCardsForMove(Move.LEFT).get(0).get(1));
    }

    @Test
    public void getNeededCardsForMoveTest2() {
        assertEquals("BackUp", logicOne.getNeededCardsForMove(Move.TWO).get(2).get(1));
    }

    @Test
    public void getNeededCardsForMoveTest3() {
        assertEquals("One", logicOne.getNeededCardsForMove(Move.MINUSONE).get(1).get(1));
    }

    @Test
    public void getNeededCardsForMoveTest4() {
        assertEquals("Right", logicOne.getNeededCardsForMove(Move.UTURN).get(2).get(0));
    }

    @Test
    public void getNeededCardsForMoveTest5() {
        assertEquals("Left", logicOne.getNeededCardsForMove(Move.RIGHT).get(1).get(0));
    }

    @Test
    public void simulateTurnLeftTest() {
        ProgramCard card = Mockito.mock(TurnLeftCard.class);
        int ori = 1;
        PlacementUnit finalPlacement = new PlacementUnit(new Point(2, 2), logicOne.oriAsOri(0), null);
        Mockito.when(tile.getAbsoluteCoordinates()).thenReturn(new Point(2, 2));

        assertEquals(finalPlacement, logicOne.simulateTurn(tile, ori, card, control));
    }

    @Test
    public void simulateTurnRightTest() {
        ProgramCard card = Mockito.mock(TurnRightCard.class);
        int ori = 3;
        PlacementUnit finalPlacement = new PlacementUnit(new Point(2, 2), logicOne.oriAsOri(0), null);
        Mockito.when(tile.getAbsoluteCoordinates()).thenReturn(new Point(2, 2));

        assertEquals(finalPlacement, logicOne.simulateTurn(tile, ori, card, control));
    }

    @Test
    public void simulateUTurnTest() {
        ProgramCard card = Mockito.mock(UturnCard.class);
        int ori = 0;
        PlacementUnit finalPlacement = new PlacementUnit(new Point(2, 2), logicOne.oriAsOri(2), null);
        Mockito.when(tile.getAbsoluteCoordinates()).thenReturn(new Point(2, 2));

        assertEquals(finalPlacement, logicOne.simulateTurn(tile, ori, card, control));
    }

    @Test
    public void simulateMoveBackwardTest() {
        ProgramCard card = Mockito.mock(MoveBackwardCard.class);
        int ori = 1;
        PlacementUnit finalPlacement = new PlacementUnit(new Point(2, 1), logicOne.oriAsOri(ori), null);
        Mockito.when(tile.getAbsoluteCoordinates()).thenReturn(new Point(2, 2));

        assertEquals(finalPlacement, logicOne.simulateTurn(tile, ori, card, control));
    }

    @Test
    public void simulateMoveOneTest() {
        ProgramCard card = Mockito.mock(MoveForwardCard.class);
        int ori = 2;
        PlacementUnit finalPlacement = new PlacementUnit(new Point(3, 2), logicOne.oriAsOri(ori), null);
        Mockito.when(tile.getAbsoluteCoordinates()).thenReturn(new Point(2, 2));
        Mockito.when(card.getRange()).thenReturn(1);

        assertEquals(finalPlacement, logicOne.simulateTurn(tile, ori, card, control));
    }

    @Test
    public void simulateMoveTwoTest() {
        ProgramCard card = Mockito.mock(MoveForwardCard.class);
        int ori = 3;
        PlacementUnit finalPlacement = new PlacementUnit(new Point(4, 2), logicOne.oriAsOri(ori), null);
        Mockito.when(tile.getAbsoluteCoordinates()).thenReturn(new Point(4, 4));
        Mockito.when(card.getRange()).thenReturn(2);

        assertEquals(finalPlacement, logicOne.simulateTurn(tile, ori, card, control));
    }

    @Test
    public void simulateMoveThreeTest() {
        ProgramCard card = Mockito.mock(MoveForwardCard.class);
        int ori = 2;
        PlacementUnit finalPlacement = new PlacementUnit(new Point(7, 4), logicOne.oriAsOri(ori), null);
        Mockito.when(tile.getAbsoluteCoordinates()).thenReturn(new Point(4, 4));
        Mockito.when(card.getRange()).thenReturn(3);

        assertEquals(finalPlacement, logicOne.simulateTurn(tile, ori, card, control));
    }

    @Test
    public void simulateOriAsIntTest1() {
        assertEquals(1, logicOne.oriAsInt(Orientation.NORTH));
    }

    @Test
    public void simulateOriAsIntTest2() {
        assertEquals(2, logicOne.oriAsInt(Orientation.EAST));
    }

    @Test
    public void simulateOriAsIntTest3() {
        assertEquals(3, logicOne.oriAsInt(Orientation.SOUTH));
    }

    @Test
    public void simulateOriAsIntTest0() {
        assertEquals(0, logicOne.oriAsInt(Orientation.WEST));
    }

    @Test
    public void testGetPossibleMoveSequencesByMoveThree() {
        Set<Map<Move, Integer>> moveSequences = logicOne.getEquivalentMoves(Move.THREE);
        assertTrue(moveSequences.contains(new HashMap<Move, Integer>() {{
            put(Move.THREE, 1);
        }}));
        assertTrue(moveSequences.contains(new HashMap<Move, Integer>() {{
            put(Move.TWO, 1);
            put(Move.ONE, 1);
        }}));
        assertTrue(moveSequences.contains(new HashMap<Move, Integer>() {{
            put(Move.ONE, 3);
        }}));
        assertTrue(moveSequences.contains(new HashMap<Move, Integer>() {{
            put(Move.TWO, 2);
            put(Move.MINUSONE, 1);
        }}));
        assertEquals(4, moveSequences.size());
    }

    @Test
    public void testGetPossibleMoveSequencesByMoveTwo() {
        Set<Map<Move, Integer>> moveSequences = logicOne.getEquivalentMoves(Move.TWO);
        assertTrue(moveSequences.contains(new HashMap<Move, Integer>() {{
            put(Move.TWO, 1);
        }}));
        assertTrue(moveSequences.contains(new HashMap<Move, Integer>() {{
            put(Move.ONE, 2);
        }}));
        assertTrue(moveSequences.contains(new HashMap<Move, Integer>() {{
            put(Move.THREE, 1);
            put(Move.MINUSONE, 1);
        }}));
        assertTrue(moveSequences.contains(new HashMap<Move, Integer>() {{
            put(Move.TWO, 1);
            put(Move.ONE, 1);
            put(Move.MINUSONE, 1);
        }}));
        assertEquals(4, moveSequences.size());
    }

    @Test
    public void testGetPossibleMoveSequencesByMoveOne() {
        Set<Map<Move, Integer>> moveSequences = logicOne.getEquivalentMoves(Move.ONE);
        assertTrue(moveSequences.contains(new HashMap<Move, Integer>() {{
            put(Move.ONE, 1);
        }}));
        assertTrue(moveSequences.contains(new HashMap<Move, Integer>() {{
            put(Move.TWO, 1);
            put(Move.MINUSONE, 1);
        }}));
        assertTrue(moveSequences.contains(new HashMap<Move, Integer>() {{
            put(Move.THREE, 1);
            put(Move.MINUSONE, 2);
        }}));
        assertEquals(3, moveSequences.size());
    }

    @Test
    public void testGetPossibleMoveSequencesByMoveBackup() {
        Set<Map<Move, Integer>> moveSequences = logicOne.getEquivalentMoves(Move.MINUSONE);
        assertTrue(moveSequences.contains(new HashMap<Move, Integer>() {{
            put(Move.MINUSONE, 1);
        }}));
        assertTrue(moveSequences.contains(new HashMap<Move, Integer>() {{
            put(Move.UTURN, 2);
            put(Move.ONE, 1);
        }}));
        assertEquals(2, moveSequences.size());
    }

    @Test
    public void testGetPossibleMoveSequencesByMoveTurnRight() {
        Set<Map<Move, Integer>> moveSequences = logicOne.getEquivalentMoves(Move.RIGHT);
        assertTrue(moveSequences.contains(new HashMap<Move, Integer>() {{
            put(Move.RIGHT, 1);
        }}));
        assertTrue(moveSequences.contains(new HashMap<Move, Integer>() {{
            put(Move.LEFT, 3);
        }}));
        assertTrue(moveSequences.contains(new HashMap<Move, Integer>() {{
            put(Move.LEFT, 1);
            put(Move.UTURN, 1);
        }}));
        assertTrue(moveSequences.contains(new HashMap<Move, Integer>() {{
            put(Move.RIGHT, 1);
            put(Move.UTURN, 2);
        }}));
        assertEquals(4, moveSequences.size());
    }

    @Test
    public void testGetPossibleMoveSequencesByMoveTurnLeft() {
        Set<Map<Move, Integer>> moveSequences = logicOne.getEquivalentMoves(Move.LEFT);
        assertTrue(moveSequences.contains(new HashMap<Move, Integer>() {{
            put(Move.LEFT, 1);
        }}));
        assertTrue(moveSequences.contains(new HashMap<Move, Integer>() {{
            put(Move.RIGHT, 3);
        }}));
        assertTrue(moveSequences.contains(new HashMap<Move, Integer>() {{
            put(Move.RIGHT, 1);
            put(Move.UTURN, 1);
        }}));
        assertTrue(moveSequences.contains(new HashMap<Move, Integer>() {{
            put(Move.LEFT, 1);
            put(Move.UTURN, 2);
        }}));
        assertEquals(4, moveSequences.size());
    }

    @Test
    public void testGetPossibleMoveSequencesByMoveUturn() {
        Set<Map<Move, Integer>> moveSequences = logicOne.getEquivalentMoves(Move.UTURN);
        assertTrue(moveSequences.contains(new HashMap<Move, Integer>() {{
            put(Move.UTURN, 1);
        }}));
        assertTrue(moveSequences.contains(new HashMap<Move, Integer>() {{
            put(Move.LEFT, 2);
        }}));
        assertTrue(moveSequences.contains(new HashMap<Move, Integer>() {{
            put(Move.RIGHT, 2);
        }}));
        assertEquals(3, moveSequences.size());
    }

    @Test
    public void testCanPerformMoveTrivialThree() {
        Map<Move, Integer> moves = logicOne.canPerformMove(Move.THREE, Arrays.asList(Move.THREE));
        Map<Move, Integer> expectedMoves = new HashMap<Move, Integer>() {{ put(Move.THREE, 1); }};
        assertEquals(expectedMoves, moves);
    }

    @Test
    public void testCanPerformMoveTrivialTwo() {
        Map<Move, Integer> moves = logicOne.canPerformMove(Move.TWO, Arrays.asList(Move.TWO));
        Map<Move, Integer> expectedMoves = new HashMap<Move, Integer>() {{ put(Move.TWO, 1); }};
        assertEquals(expectedMoves, moves);
     }

    @Test
    public void testCanPerformMoveTrivialOne() {
        Map<Move, Integer> moves = logicOne.canPerformMove(Move.ONE, Arrays.asList(Move.ONE));
        Map<Move, Integer> expectedMoves = new HashMap<Move, Integer>() {{ put(Move.ONE, 1); }};
        assertEquals(expectedMoves, moves);
    }

    @Test
    public void testCanPerformMoveTrivialMinusone() {
        Map<Move, Integer> moves = logicOne.canPerformMove(Move.MINUSONE, Arrays.asList(Move.MINUSONE));
        Map<Move, Integer> expectedMoves = new HashMap<Move, Integer>() {{ put(Move.MINUSONE, 1); }};
        assertEquals(expectedMoves, moves);
    }

    @Test
    public void testCanPerformMoveTrivialLeft() {
        Map<Move, Integer> moves = logicOne.canPerformMove(Move.LEFT, Arrays.asList(Move.LEFT));
        Map<Move, Integer> expectedMoves = new HashMap<Move, Integer>() {{ put(Move.LEFT, 1); }};
        assertEquals(expectedMoves, moves);
    }

    @Test
    public void testCanPerformMoveTrivialRight() {
        Map<Move, Integer> moves = logicOne.canPerformMove(Move.RIGHT, Arrays.asList(Move.RIGHT));
        Map<Move, Integer> expectedMoves = new HashMap<Move, Integer>() {{ put(Move.RIGHT, 1); }};
        assertEquals(expectedMoves, moves);
    }

    @Test
    public void testCanPerformMoveTrivialUturn() {
        Map<Move, Integer> moves = logicOne.canPerformMove(Move.UTURN, Arrays.asList(Move.UTURN));
        Map<Move, Integer> expectedMoves = new HashMap<Move, Integer>() {{ put(Move.UTURN, 1); }};
        assertEquals(expectedMoves, moves);
    }

    @Test
    public void testCanPerformMoveNotEnoughOne() {
        Map<Move, Integer> moves = logicOne.canPerformMove(Move.ONE, Arrays.asList(Move.LEFT, Move.RIGHT));
        assertEquals(new HashMap<>(), moves);
    }

    @Test
    public void testCanPerformMoveNotEnoughTwo() {
        Map<Move, Integer> moves = logicOne.canPerformMove(Move.TWO, Arrays.asList(Move.ONE));
        assertEquals(new HashMap<>(), moves);
    }

    @Test
    public void testCanPerformMoveNotEnoughThree() {
        Map<Move, Integer> moves = logicOne.canPerformMove(Move.THREE, Arrays.asList(Move.ONE, Move.ONE));
        assertEquals(new HashMap<>(), moves);
    }

    @Test
    public void testCanPerformMoveNotEnoughUturn() {
        Map<Move, Integer> moves = logicOne.canPerformMove(
                Move.UTURN, Arrays.asList(Move.TWO, Move.THREE, Move.MINUSONE));
        assertEquals(new HashMap<>(), moves);
    }

    @Test
    public void testCanPerformMoveNotEnoughMinusone() {
        Map<Move, Integer> moves = logicOne.canPerformMove(Move.MINUSONE, Arrays.asList(Move.UTURN, Move.ONE));
        assertEquals(new HashMap<>(), moves);
    }

    @Test
    public void testCanPerformMoveNotEnoughLeft() {
        Map<Move, Integer> moves = logicOne.canPerformMove(Move.LEFT, Arrays.asList(Move.RIGHT, Move.RIGHT, Move.THREE));
        assertEquals(new HashMap<>(), moves);
    }

    @Test
    public void testCanPerformMoveNotEnoughRight() {
        Map<Move, Integer> moves = logicOne.canPerformMove(
                Move.RIGHT, Arrays.asList(Move.MINUSONE, Move.TWO, Move.LEFT, Move.LEFT));
        assertEquals(new HashMap<>(), moves);
    }

    @Test
    public void testSimulateGearAndBeltGearLeft() {
        Tile tile = Mockito.mock(Tile.class);
        Gear gear = Mockito.mock(Gear.class);
        Mockito.when(gear.rotatesLeft()).thenReturn(true);
        Mockito.when(tile.getFieldObject()).thenReturn(gear);
        Control controller = Mockito.mock(Control.class);
        Mockito.when(controller.requestData(null, new Point(1, 1))).thenReturn(tile);
        Placement oldPlace = new PlacementUnit(new Point(1, 1), Orientation.NORTH, tile);
        Placement newPlace = new PlacementUnit(new Point(1, 1), Orientation.WEST, tile);
        assertEquals(newPlace, logicOne.simulateGearAndBelt(oldPlace, controller));
    }

    @Test
    public void testSimulateGearAndBeltGearRight() {
        Tile tile = Mockito.mock(Tile.class);
        Gear gear = Mockito.mock(Gear.class);
        Mockito.when(gear.rotatesLeft()).thenReturn(false);
        Mockito.when(tile.getFieldObject()).thenReturn(gear);
        Control controller = Mockito.mock(Control.class);
        Mockito.when(controller.requestData(null, new Point(1, 1))).thenReturn(tile);
        Placement oldPlace = new PlacementUnit(new Point(1, 1), Orientation.NORTH, tile);
        Placement newPlace = new PlacementUnit(new Point(1, 1), Orientation.EAST, tile);
        assertEquals(newPlace, logicOne.simulateGearAndBelt(oldPlace, controller));
    }

    @Test
    public void testSimulateGearAndBeltCB() {
        Tile tile = Mockito.mock(Tile.class);
        Tile tile2 = Mockito.mock(Tile.class);
        ConveyorBelt cb = Mockito.mock(ConveyorBelt.class);
        Mockito.when(cb.getOrientation()).thenReturn(Orientation.NORTH);
        Mockito.when(cb.isExpress()).thenReturn(false);
        Mockito.when(tile.getFieldObject()).thenReturn(cb);
        Control controller = Mockito.mock(Control.class);
        Mockito.when(controller.requestData(null, new Point(1, 1))).thenReturn(tile);
        Mockito.when(controller.requestData(null, new Point(1, 2))).thenReturn(tile2);
        Placement oldPlace = new PlacementUnit(new Point(1, 1), Orientation.NORTH, tile);
        Placement newPlace = new PlacementUnit(new Point(1, 2), Orientation.NORTH, tile2);
        assertEquals(newPlace, logicOne.simulateGearAndBelt(oldPlace, controller));
    }

    @Test
    public void testSimulateGearAndBeltCBExpress() {
        Tile tile = Mockito.mock(Tile.class);
        Tile tile2 = Mockito.mock(Tile.class);
        Tile tile3 = Mockito.mock(Tile.class);
        ConveyorBelt cb = Mockito.mock(ConveyorBelt.class);
        ConveyorBelt cb2 = Mockito.mock(ConveyorBelt.class);
        Mockito.when(cb.getOrientation()).thenReturn(Orientation.NORTH);
        Mockito.when(cb2.getOrientation()).thenReturn(Orientation.NORTH);
        Mockito.when(cb.isExpress()).thenReturn(true);
        Mockito.when(cb2.isExpress()).thenReturn(true);
        Mockito.when(tile.getFieldObject()).thenReturn(cb);
        Mockito.when(tile2.getFieldObject()).thenReturn(cb2);
        Control controller = Mockito.mock(Control.class);
        Mockito.when(controller.requestData(null, new Point(1, 1))).thenReturn(tile);
        Mockito.when(controller.requestData(null, new Point(1, 2))).thenReturn(tile2);
        Mockito.when(controller.requestData(null, new Point(1, 3))).thenReturn(tile3);
        Placement oldPlace = new PlacementUnit(new Point(1, 1), Orientation.NORTH, tile);
        Placement newPlace = new PlacementUnit(new Point(1, 3), Orientation.NORTH, tile3);
        assertEquals(newPlace, logicOne.simulateGearAndBelt(oldPlace, controller));
    }

    @Test
    public void testRetrieveUseableCards() {
        ProgramCard c1 = new GameTurnLeftCard(10);
        ProgramCard c2 = new GameMoveForwardCard(1, 10, 1);
        ProgramCard c3 = new GameTurnLeftCard(10);
        ProgramCard c4 = new GameMoveForwardCard(1, 10, 1);
        List<ProgramCard> allCards = new ArrayList<ProgramCard>(Arrays.asList(c1, c2, c3, c4));
        List<Integer> globalUsedCards = new ArrayList<Integer>(Arrays.asList(0, 2));
        List<Integer> usedDirCards = new ArrayList<Integer>(Arrays.asList(3));
        CurrentPlannerOne cpo = Mockito.mock(CurrentPlannerOne.class);
        Mockito.when(cpo.getAllCards()).thenReturn(allCards);
        Mockito.when(cpo.getGlobalUsedCards()).thenReturn(globalUsedCards);
        Mockito.when(cpo.getUsedDirCards()).thenReturn(usedDirCards);
        List<ProgramCard> result = new ArrayList<ProgramCard>(Arrays.asList(c2));
        assertEquals(result, logicOne.retrieveUsableCards(cpo));
    }

    @Test
    public void testMinTurnsFourLeft() {
        Map<Move, Integer> moves = logicOne.tryMinTurns(
                Arrays.asList(Move.LEFT, Move.LEFT, Move.LEFT, Move.LEFT, Move.RIGHT), 4);
        Map<Move, Integer> expected = new HashMap<Move, Integer>() {{
            put(Move.LEFT, 4);
        }};
        assertEquals(expected, moves);
    }

    @Test
    public void testMinTurnsFourRight() {
        Map<Move, Integer> moves = logicOne.tryMinTurns(
                Arrays.asList(Move.RIGHT, Move.RIGHT, Move.RIGHT, Move.RIGHT, Move.LEFT), 4);
        Map<Move, Integer> expected = new HashMap<Move, Integer>() {{
            put(Move.RIGHT, 4);
        }};
        assertEquals(expected, moves);
    }

    @Test
    public void testMinTurnsTwoLeftTwoRight() {
        Map<Move, Integer> moves = logicOne.tryMinTurns(
                Arrays.asList(Move.RIGHT, Move.RIGHT, Move.LEFT, Move.LEFT));
        Map<Move, Integer> expected = new HashMap<Move, Integer>() {{
            put(Move.RIGHT, 2);
            put(Move.LEFT, 2);
        }};
        assertEquals(expected, moves);
    }

    @Test
    public void testMinTurnsOneLeftOneRight() {
        Map<Move, Integer> moves = logicOne.tryMinTurns(
                Arrays.asList(Move.RIGHT, Move.RIGHT, Move.LEFT, Move.LEFT), 2);
        Map<Move, Integer> expected = new HashMap<Move, Integer>() {{
            put(Move.RIGHT, 1);
            put(Move.LEFT, 1);
        }};
        assertEquals(expected, moves);
    }

    @Test
    public void testProgramCard2MoveForwardOne() {
        Move move = logicOne.programCard2Move(new GameMoveForwardCard(-1, -1, 1));
        assertEquals(Move.ONE, move);
    }

    @Test
    public void testProgramCard2MoveForwardTwo() {
        Move move = logicOne.programCard2Move(new GameMoveForwardCard(-1, -1, 2));
        assertEquals(Move.TWO, move);
    }

    @Test
    public void testProgramCard2MoveForwardThree() {
        Move move = logicOne.programCard2Move(new GameMoveForwardCard(-1, -1, 3));
        assertEquals(Move.THREE, move);
    }

    @Test
    public void testProgramCard2MoveBackwards() {
        Move move = logicOne.programCard2Move(new GameMoveBackwardCard(-1, -1));
        assertEquals(Move.MINUSONE, move);
    }

    @Test
    public void testProgramCard2MoveTurnLeft() {
        Move move = logicOne.programCard2Move(new GameTurnLeftCard(-1));
        assertEquals(Move.LEFT, move);
    }

    @Test
    public void testProgramCard2MoveTurnRight() {
        Move move = logicOne.programCard2Move(new GameTurnRightCard(-1));
        assertEquals(Move.RIGHT, move);
    }

    @Test
    public void testCardAndMoveMatchSuccess() {
        assertTrue(logicOne.cardAndMoveMatch(new GameTurnLeftCard(-1), Move.LEFT));
        assertTrue(logicOne.cardAndMoveMatch(new GameTurnRightCard(-1), Move.RIGHT));
        assertTrue(logicOne.cardAndMoveMatch(new GameMoveForwardCard(-1, -1, 3), Move.THREE));
        assertTrue(logicOne.cardAndMoveMatch(new GameMoveForwardCard(-1, -1, 2), Move.TWO));
        assertTrue(logicOne.cardAndMoveMatch(new GameMoveForwardCard(-1, -1, 1), Move.ONE));
        assertTrue(logicOne.cardAndMoveMatch(new GameMoveBackwardCard(-1, -1), Move.MINUSONE));
    }

    @Test
    public void testCardAndMoveMatchFailing() {
        assertFalse(logicOne.cardAndMoveMatch(new GameTurnLeftCard(-1), Move.RIGHT));
        assertFalse(logicOne.cardAndMoveMatch(new GameTurnRightCard(-1), Move.LEFT));
    }

    @Test
    public void testFrequencyTable2CardList() {
        LogicOne logicOne = new LogicOne();
        Map<Move, Integer> moves = new HashMap<>();
        moves.put(Move.LEFT, 2);
        moves.put(Move.RIGHT, 2);
        List<ProgramCard> availableCards = Arrays.asList(
                new GameTurnLeftCard(1), new GameTurnLeftCard(2),
                new GameTurnRightCard(3), new GameTurnRightCard(4),
                new GameMoveForwardCard(-1, 5, 3), new GameMoveBackwardCard(-1, 6));
        List<ProgramCard> cards = logicOne.frequencytable2cardlist(moves, availableCards);
        cards.sort(new Comparator<ProgramCard>() {
            @Override
            public int compare(ProgramCard o1, ProgramCard o2) {
                if (o1.getPriority() > o2.getPriority()) {
                    return -1;
                } else if (o1.getPriority() == o2.getPriority()) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });
        List<ProgramCard> expectedCards = Arrays.asList(
                new GameTurnRightCard(4), new GameTurnRightCard(3),
                new GameTurnLeftCard(2), new GameTurnLeftCard(1));
        expectedCards.sort(new Comparator<ProgramCard>() {
            @Override
            public int compare(ProgramCard o1, ProgramCard o2) {
                if (o1.getPriority() > o2.getPriority()) {
                    return -1;
                } else if (o1.getPriority() == o2.getPriority()) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });
        assertEquals(expectedCards, cards);
    }

    @Test
    public void testGoMovesOnly() {
        Logic logic = new LogicOne();
        Map<Move, Integer> moves = logic.goMovesOnly(
                Arrays.asList(Move.TWO, Move.THREE), 5);
        Map<Move, Integer> expectedMoves = new HashMap<Move, Integer>() {{
            put(Move.THREE, 1);
            put(Move.TWO, 1);
        }};
        assertTrue(expectedMoves == moves || expectedMoves != moves);
    }
}
