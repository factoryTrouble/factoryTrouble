package de.uni_bremen.factroytrouble.ai.ki_classic;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.player.GameMoveBackwardCard;
import de.uni_bremen.factroytrouble.player.GameMoveForwardCard;
import de.uni_bremen.factroytrouble.player.GameTurnLeftCard;
import de.uni_bremen.factroytrouble.player.GameTurnRightCard;
import de.uni_bremen.factroytrouble.player.GameUturnCard;
import de.uni_bremen.factroytrouble.player.ProgramCard;

public class VirtualBoardTest {

    private BoardMocker bm;

    @Before
    public void setup() {
        bm = new BoardMocker();
    }

    @Test
    public void shouldMoveForwardToTest() {
        bm.fillField(6);
        bm.setFlagTile(new Point(0, 5));
        bm.setStartTile(new Point(0, 0), Orientation.NORTH);

        List<ProgramCard> cards = new ArrayList<>();
        cards.add(new GameMoveForwardCard(0, 100, 1));
        cards.add(new GameTurnLeftCard(100));
        cards.add(new GameMoveForwardCard(0, 100, 1));
        cards.add(new GameMoveForwardCard(0, 100, 1));
        cards.add(new GameTurnLeftCard(100));
        cards.add(new GameMoveForwardCard(0, 100, 1));
        cards.add(new GameTurnLeftCard(100));
        cards.add(new GameMoveForwardCard(0, 100, 1));
        cards.add(new GameTurnLeftCard(100));

        List<ProgramCard> expected = new ArrayList<>();
        expected.add(new GameMoveForwardCard(0, 100, 1));
        expected.add(new GameMoveForwardCard(0, 100, 1));
        expected.add(new GameMoveForwardCard(0, 100, 1));
        expected.add(new GameMoveForwardCard(0, 100, 1));
        expected.add(new GameMoveForwardCard(0, 100, 1));

        assertEquals(expected, playRound(cards));
    }

    @Test
    public void shouldMoveAroundHole() {
        bm.fillField(3);
        bm.setFlagTile(new Point(1, 2));
        bm.setStartTile(new Point(0, 0), Orientation.NORTH);
        bm.setHole(new Point(0, 1));

        List<ProgramCard> cards = new ArrayList<>();
        cards.add(new GameTurnRightCard(100));
        cards.add(new GameMoveForwardCard(0, 100, 1));
        cards.add(new GameTurnLeftCard(100));

        cards.add(new GameUturnCard(100));
        cards.add(new GameUturnCard(100));

        cards.add(new GameMoveForwardCard(0, 100, 1));
        cards.add(new GameMoveForwardCard(0, 100, 1));

        cards.add(new GameMoveBackwardCard(0, 100));
        cards.add(new GameMoveBackwardCard(0, 100));
        cards.add(new GameMoveBackwardCard(0, 100));

        List<ProgramCard> expected = new ArrayList<>();
        expected.add(new GameTurnRightCard(100));
        expected.add(new GameMoveForwardCard(0, 100, 1));
        expected.add(new GameTurnLeftCard(100));
        expected.add(new GameMoveForwardCard(0, 100, 1));
        expected.add(new GameMoveForwardCard(0, 100, 1));

        assertEquals(expected, playRound(cards));
    }

    @Test
    public void shouldMoveAroundWall() {
        bm.fillField(3);
        bm.setFlagTile(new Point(1, 2));
        bm.setStartTile(new Point(0, 0), Orientation.NORTH);
        bm.setWall(new Point(0, 1), Orientation.SOUTH);

        List<ProgramCard> cards = new ArrayList<>();
        cards.add(new GameTurnRightCard(100));
        cards.add(new GameMoveForwardCard(0, 100, 1));
        cards.add(new GameTurnLeftCard(100));
        cards.add(new GameMoveForwardCard(0, 100, 1));
        cards.add(new GameMoveForwardCard(0, 100, 1));

        cards.add(new GameMoveBackwardCard(0, 100));
        cards.add(new GameMoveBackwardCard(0, 100));
        cards.add(new GameMoveBackwardCard(0, 100));
        cards.add(new GameUturnCard(100));
        cards.add(new GameUturnCard(100));

        List<ProgramCard> expected = new ArrayList<>();
        expected.add(new GameTurnRightCard(100));
        expected.add(new GameMoveForwardCard(0, 100, 1));
        expected.add(new GameTurnLeftCard(100));
        expected.add(new GameMoveForwardCard(0, 100, 1));
        expected.add(new GameMoveForwardCard(0, 100, 1));

        assertEquals(expected, playRound(cards));
    }

    private List<ProgramCard> playRound(List<ProgramCard> choices) {
        VirtualBoard vb = new VirtualBoard(bm.getMaster(), bm.getDykstra());
        when(bm.getPlayer().getPlayerCards()).thenReturn(choices);
        int[] indexChoices = vb.performRoundVirtualRound(bm.getPlayer());
        List<ProgramCard> cardChoices = new ArrayList<>();
        for (int i : indexChoices) {
            cardChoices.add(choices.get(i));
        }
        return cardChoices;
    }
}
