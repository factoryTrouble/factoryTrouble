package de.uni_bremen.factroytrouble.ai.ki2.ai.planning;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import de.uni_bremen.factroytrouble.ai.ais.AIPlayer2;
import de.uni_bremen.factroytrouble.ai.ki2.ai.memory.ScullyConveyorInfo;
import de.uni_bremen.factroytrouble.ai.ki2.api.decision.consciousness.Approach;
import de.uni_bremen.factroytrouble.ai.ki2.api.decision.consciousness.ConsciousnessUnit;
import de.uni_bremen.factroytrouble.ai.ki2.api.decision.unconsciousness.UnconsciousnessUnit;
import de.uni_bremen.factroytrouble.ai.ki2.api.memory.Thought;
import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.player.GameMoveBackwardCard;
import de.uni_bremen.factroytrouble.player.GameMoveForwardCard;
import de.uni_bremen.factroytrouble.player.GameTurnLeftCard;
import de.uni_bremen.factroytrouble.player.GameTurnRightCard;
import de.uni_bremen.factroytrouble.player.GameUturnCard;
import de.uni_bremen.factroytrouble.player.ProgramCard;

@RunWith(MockitoJUnitRunner.class)
public class CardFinderTest {

	@Mock
	GameTurnRightCard right1, right2, right3, right4;

	@Mock
	GameTurnLeftCard left1, left2, left3, left4;

	@Mock
	GameUturnCard uTurn1, uTurn2;

	@Mock
	GameMoveForwardCard move1_1, move1_2, move1_3, move2_1, move2_2, move3_1,
			move3_2;

	@Mock
	GameMoveBackwardCard back1;

	@Mock
	Thought thought1, thought2, thought3, thought4, thought5, thought6,
			thought7, thought8, thought9, thought10, thought11, thought12,
			thought13, thought14, thought15, thought16;
	@Mock
	UnconsciousnessUnit uncons;
	@Mock
	ConsciousnessUnit cons;

	@Mock
	AIPlayer2 aiPlayer;

	@Mock
	Robot robot;

	@Mock
	Tile tile;

	private CardFinder cardFinder;

	@Before
	public void setUp() {
		when(aiPlayer.getRobot()).thenReturn(robot);
		when(robot.getCurrentTile()).thenReturn(tile);
		when(robot.getOrientation()).thenReturn(Orientation.NORTH);

		cardFinder = new CardFinder(cons, uncons, aiPlayer);
		when(cons.decide(any(Approach.class), any(Integer.class))).thenReturn(
				true);

		when(uncons.getInformation(Arrays.asList("left", "gears"))).thenReturn(
				thought5);
		when(thought5.getInformation()).thenReturn(
				Arrays.asList(Arrays.asList()));

		when(uncons.getInformation(Arrays.asList("right", "gears")))
				.thenReturn(thought6);
		when(thought6.getInformation()).thenReturn(
				Arrays.asList(Arrays.asList()));

		when(uncons.getInformation(Arrays.asList("highest", "point")))
				.thenReturn(thought3);
		when(thought3.getInformation()).thenReturn(
				Arrays.asList(new Point(11, 25)));

		when(uncons.getInformation(Arrays.asList("cards", "locked")))
				.thenReturn(thought2);
		when(thought2.getInformation()).thenReturn(
				Arrays.asList(Arrays.asList(move1_3)));

		when(uncons.getInformation(Arrays.asList("all", "conveyor")))
				.thenReturn(thought7);
		when(thought7.getInformation()).thenReturn(
				Arrays.asList(new HashMap<Point, ScullyConveyorInfo>()));

		when(uncons.getInformation(Arrays.asList("flag", "next"))).thenReturn(
				thought9);
		when(thought9.getInformation()).thenReturn(
				Arrays.asList(new Point(3, 4)));

		when(uncons.getInformation(Arrays.asList("my", "position")))
				.thenReturn(thought10);
		when(thought10.getInformation()).thenReturn(
				Arrays.asList(new Point(1, 1)));

		when(uncons.getInformation(Arrays.asList("my", "ori"))).thenReturn(
				thought11);
		when(thought11.getInformation()).thenReturn(
				Arrays.asList(Orientation.NORTH));

		List<Point> holes = new ArrayList<>();
		holes.add(new Point(6, 6));
		when(uncons.getInformation(Arrays.asList("holes"))).thenReturn(
				thought12);
		when(thought12.getInformation()).thenReturn(Arrays.asList(holes));

		when(uncons.getInformation(Arrays.asList("walls", "north")))
				.thenReturn(thought13);
		when(thought13.getInformation()).thenReturn(
				Arrays.asList(Arrays.asList(new Point(2, 1))));

		when(uncons.getInformation(Arrays.asList("walls", "east"))).thenReturn(
				thought14);
		when(thought14.getInformation()).thenReturn(
				Arrays.asList(Arrays.asList(new Point(9, 9))));

		when(uncons.getInformation(Arrays.asList("walls", "south")))
				.thenReturn(thought15);
		when(thought15.getInformation()).thenReturn(
				Arrays.asList(Arrays.asList(new Point(2, 4))));

		when(uncons.getInformation(Arrays.asList("walls", "west"))).thenReturn(
				thought16);
		when(thought16.getInformation()).thenReturn(
				Arrays.asList(Arrays.asList(new Point(1, 1))));

		when(uncons.getInformation(Arrays.asList("cards", "player")))
				.thenReturn(thought1);
		when(thought1.getInformation()).thenReturn(
				Arrays.asList(left1, left2, left3, left4, uTurn1, back1,
						right1, right2, right3, right4, move1_1, move1_2,
						move1_3, move2_1, move2_2, move3_1, move3_2));

		when(move1_1.getRange()).thenReturn(1);
		when(move1_2.getRange()).thenReturn(1);
		when(move1_3.getRange()).thenReturn(1);
		when(move2_1.getRange()).thenReturn(2);
		when(move2_2.getRange()).thenReturn(2);
		when(move3_1.getRange()).thenReturn(3);
		when(move3_2.getRange()).thenReturn(3);
		when(back1.getRange()).thenReturn(-1);
	}

	@Test
	public void findCardsFindCards() {
		List<ProgramCard> result = new ArrayList<>();
		for (int i = 0; i < 21; i++) {
			result = cardFinder.findCards(Arrays.asList(-2, 3, -1, 2, -3));
		}
		assertEquals(result,
				Arrays.asList(uTurn1, move3_1, left1, move2_1, right1));
	}

	@Test
	public void findCardsRightTurn1() {

		when(thought1.getInformation()).thenReturn(
				Arrays.asList(left1, left2, left3, left4, uTurn1, back1,
						move1_1, move1_2, move1_3, move2_1, move2_2, move3_1,
						move3_2));

		List<ProgramCard> result = new ArrayList<>();
		for (int i = 0; i < 21; i++) {
			result = cardFinder.findCards(Arrays.asList(-3));
		}
		assertTrue(result.get(0) == uTurn1 && result.get(1) == left1);
	}

	@Test
	public void findCardsRightTurn2() {

		when(thought1.getInformation()).thenReturn(
				Arrays.asList(left1, left2, left3, left4, back1, move1_1,
						move1_2, move1_3, move2_1, move2_2, move3_1, move3_2));

		List<ProgramCard> result = new ArrayList<>();
		for (int i = 0; i < 21; i++) {
			result = cardFinder.findCards(Arrays.asList(-3));
		}
		assertTrue(result.get(0) == left1 && result.get(1) == left2
				&& result.get(2) == left3);
	}

	@Test
	public void findCardsRightTurn3() {

		when(thought1.getInformation()).thenReturn(
				Arrays.asList(uTurn1, back1, move1_1, move1_2, move1_3,
						move2_1, move2_2, move3_1, move3_2));

		List<ProgramCard> result = new ArrayList<>();
		for (int i = 0; i < 21; i++) {
			result = cardFinder.findCards(Arrays.asList(-3));
		}
		assertTrue(result.isEmpty());
	}

	@Test
	public void findCardsLeftTurn1() {

		when(thought1.getInformation()).thenReturn(
				Arrays.asList(right1, uTurn1, back1, move1_1, move1_2, move1_3,
						move2_1, move2_2, move3_1, move3_2));

		List<ProgramCard> result = new ArrayList<>();
		for (int i = 0; i < 21; i++) {
			result = cardFinder.findCards(Arrays.asList(-1));
		}
		assertTrue(result.get(0) == uTurn1 && result.get(1) == right1);
	}

	@Test
	public void findCardsLeftTurn2() {

		when(thought1.getInformation()).thenReturn(
				Arrays.asList(right1, right2, right3, back1, move1_1, move1_2,
						move1_3, move2_1, move2_2, move3_1, move3_2));

		List<ProgramCard> result = new ArrayList<>();
		for (int i = 0; i < 21; i++) {
			result = cardFinder.findCards(Arrays.asList(-1));
		}
		assertTrue(result.get(0) == right1 && result.get(1) == right2
				&& result.get(2) == right3);
	}

	@Test
	public void findCardsLeftTurn3() {

		when(thought1.getInformation()).thenReturn(
				Arrays.asList(uTurn1, back1, move1_1, move1_2, move1_3,
						move2_1, move2_2, move3_1, move3_2));

		List<ProgramCard> result = new ArrayList<>();
		for (int i = 0; i < 21; i++) {
			result = cardFinder.findCards(Arrays.asList(-1));
		}
		assertTrue(result.isEmpty());
	}

	@Test
	public void findCardsUTurn1() {

		when(thought1.getInformation()).thenReturn(
				Arrays.asList(left1, left2, back1, move1_1, move1_2, move1_3,
						move2_1, move2_2, move3_1, move3_2));

		List<ProgramCard> result = new ArrayList<>();
		for (int i = 0; i < 21; i++) {
			result = cardFinder.findCards(Arrays.asList(-2));
		}
		assertTrue(result.get(0) == left1 && result.get(1) == left2);
	}

	@Test
	public void findCardsUTurn2() {

		when(thought1.getInformation()).thenReturn(
				Arrays.asList(right1, right2, right3, back1, move1_1));

		List<ProgramCard> result = new ArrayList<>();
		for (int i = 0; i < 21; i++) {
			result = cardFinder.findCards(Arrays.asList(-2));
		}
		assertTrue(result.get(0) == right1 && result.get(1) == right2);
	}

	@Test
	public void findCardsUTurn3() {

		when(thought1.getInformation()).thenReturn(
				Arrays.asList(back1, move1_1, move1_2, move1_3, move2_1,
						move2_2, move3_1, move3_2));

		List<ProgramCard> result = new ArrayList<>();
		for (int i = 0; i < 21; i++) {
			result = cardFinder.findCards(Arrays.asList(-2));
		}
		assertTrue(result.isEmpty());
	}

	@Test
	public void findCardsMoveForward() {

		when(thought1.getInformation()).thenReturn(
				Arrays.asList(move1_1, move1_2, move1_3, move2_1, move2_2,
						move3_1, move3_2));

		List<ProgramCard> result = new ArrayList<>();
		for (int i = 0; i < 21; i++) {
			result = cardFinder.findCards(Arrays.asList(7));
		}
		assertTrue(result.get(0) == move3_1 && result.get(1) == move3_2
				&& result.get(2) == move1_1);
	}

	@Test
	public void findCardsMoveForwardAndTurn() {
		when(thought2.getInformation()).thenReturn(
				Arrays.asList(Arrays.asList()));
		when(thought1.getInformation()).thenReturn(
				Arrays.asList(back1, uTurn1, uTurn2, move3_1, right1));

		List<ProgramCard> result = new ArrayList<>();
		for (int i = 0; i < 21; i++) {
			result = cardFinder.findCards(Arrays.asList());
		}
		assertTrue(result.size() == 5);
	}

	@Test
	public void findCardsNotAccepted() {
		when(cons.decide(any(Approach.class), any(Integer.class))).thenReturn(
				false);
		when(thought1.getInformation()).thenReturn(
				Arrays.asList(move1_1, move1_2, move1_3, move2_1, move2_2,
						move3_1, move3_2));

		List<ProgramCard> result = new ArrayList<>();
		for (int i = 0; i < 21; i++) {
			result = cardFinder.findCards(Arrays.asList(7));
		}
		assertTrue(result.isEmpty());
	}
}
