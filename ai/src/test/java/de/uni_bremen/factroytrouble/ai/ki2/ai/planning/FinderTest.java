package de.uni_bremen.factroytrouble.ai.ki2.ai.planning;

import static org.junit.Assert.assertEquals;
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

import de.uni_bremen.factroytrouble.ai.ki2.ai.memory.ScullyConveyorInfo;
import de.uni_bremen.factroytrouble.ai.ki2.api.decision.consciousness.ConsciousnessUnit;
import de.uni_bremen.factroytrouble.ai.ki2.api.decision.unconsciousness.UnconsciousnessUnit;
import de.uni_bremen.factroytrouble.ai.ki2.api.memory.Thought;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.player.GameMoveBackwardCard;
import de.uni_bremen.factroytrouble.player.GameMoveForwardCard;
import de.uni_bremen.factroytrouble.player.GameTurnLeftCard;
import de.uni_bremen.factroytrouble.player.GameTurnRightCard;
import de.uni_bremen.factroytrouble.player.GameUturnCard;
import de.uni_bremen.factroytrouble.player.ProgramCard;

@RunWith(MockitoJUnitRunner.class)
public class FinderTest {
	private Finder finder;

	@Mock
	GameTurnRightCard right1, right2, right3, right4;

	@Mock
	GameTurnLeftCard left1, left2, left3, left4;

	@Mock
	GameUturnCard uTurn1;

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

	@Before
	public void setUp() {
		finder = new Finder(uncons);

		when(uncons.getInformation(Arrays.asList("cards", "locked")))
				.thenReturn(thought2);
		when(thought2.getInformation()).thenReturn(
				Arrays.asList(Arrays.asList(move3_2)));

		when(uncons.getInformation(Arrays.asList("left", "gears"))).thenReturn(
				thought5);
		when(thought5.getInformation()).thenReturn(
				Arrays.asList(Arrays.asList(new Point(33, 33))));

		when(uncons.getInformation(Arrays.asList("right", "gears")))
				.thenReturn(thought6);
		when(thought6.getInformation()).thenReturn(
				Arrays.asList(Arrays.asList(new Point(33, 33))));

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

		when(uncons.getInformation(Arrays.asList("highest", "point")))
				.thenReturn(thought3);
		when(thought3.getInformation()).thenReturn(
				Arrays.asList(new Point(11, 25)));

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
	public void getNeighborsNorth() {
		Point results = finder.getNeighbors(new Point(2, 4), Orientation.NORTH);
		assertEquals(results, new Point(2, 5));
	}

	@Test
	public void getNeighborsSouth() {
		Point results = finder.getNeighbors(new Point(2, 4), Orientation.SOUTH);
		assertEquals(results, new Point(2, 3));
	}

	@Test
	public void getNeighborsEast() {
		Point results = finder.getNeighbors(new Point(2, 4), Orientation.EAST);
		assertEquals(results, new Point(3, 4));
	}

	@Test
	public void getNeighborsWest() {
		Point results = finder.getNeighbors(new Point(2, 4), Orientation.WEST);
		assertEquals(results, new Point(1, 4));
	}

	@Test
	public void findOrientationNoTurning() {
		Orientation results = finder.findOrientation(Arrays.asList(2));
		assertEquals(results, Orientation.NORTH);
	}

	@Test
	public void findOrientationRightTurning() {
		Orientation results = finder.findOrientation(Arrays.asList(3, -3, 2));
		assertEquals(results, Orientation.EAST);
	}

	@Test
	public void findOrientationLeftTurning() {
		Orientation results = finder.findOrientation(Arrays.asList(3, -1, 2));
		assertEquals(results, Orientation.WEST);
	}

	@Test
	public void findOrientationUTurning() {
		Orientation results = finder.findOrientation(Arrays.asList(3, -2, 2));
		assertEquals(results, Orientation.SOUTH);
	}

	@Test
	public void findOrientationLotsOfTurning() {
		Orientation results = finder.findOrientation(Arrays.asList(-3, 2, -2,
				1, -1));
		assertEquals(results, Orientation.SOUTH);
	}

	@Test
	public void getDistanceToGoal0() {
		int results = finder.getDistanceToGoal(new Point(3, 4));
		assertEquals(results, 0);
	}

	@Test
	public void getDistanceToGoal3() {
		int results = finder.getDistanceToGoal(new Point(1, 3));
		assertEquals(results, 3);
	}

	@Test
	public void getHandCardsRight() {
		List<ProgramCard> results = finder.getHandCards("Rechtsdrehkarten");
		assertEquals(results, Arrays.asList(right1, right2, right3, right4));
	}

	@Test
	public void getHandCardsLeft() {
		List<ProgramCard> results = finder.getHandCards("Linksdrehkarten");
		assertEquals(results, Arrays.asList(left1, left2, left3, left4));
	}

	@Test
	public void getHandCardsUTurn() {
		List<ProgramCard> results = finder.getHandCards("Uturnkarten");
		assertEquals(results, Arrays.asList(uTurn1));
	}

	@Test
	public void getHandCardsBack() {
		List<ProgramCard> results = finder.getHandCards("Rückwärtskarten");
		assertEquals(results, Arrays.asList(back1));
	}

	@Test
	public void getHandCardsMove1() {
		List<ProgramCard> results = finder.getHandCards("1vorwärtskarten");
		assertEquals(results, Arrays.asList(move1_1, move1_2, move1_3));
	}

	@Test
	public void getHandCardsMove2() {
		List<ProgramCard> results = finder.getHandCards("2vorwärtskarten");
		assertEquals(results, Arrays.asList(move2_1, move2_2));
	}

	@Test
	public void getHandCardsMove3() {
		List<ProgramCard> results = finder.getHandCards("3vorwärtskarten");
		assertEquals(results, Arrays.asList(move3_1, move3_2));
	}

	@Test
	public void getHandCardsAllCards() {
		List<ProgramCard> results = finder.getHandCards("WrongKey");
		results = finder.getHandCards("wrongKey");
		assertEquals(results, Arrays.asList(left1, left2, left3, left4, uTurn1,
				back1, right1, right2, right3, right4, move1_1, move1_2,
				move1_3, move2_1, move2_2, move3_1, move3_2));
	}

	@Test
	public void getHandCardsWithoutCard() {
		when(thought1.getInformation()).thenReturn(Arrays.asList());
		List<ProgramCard> results = finder.getHandCards("3vorwärtskarten");
		assertEquals(results, Arrays.asList());
	}

	@Test
	public void getOrientation() {
		Orientation results = finder.getOrientation();
		assertEquals(results, Orientation.NORTH);
	}

	@Test
	public void getPos() {
		Point results = finder.getPos();
		assertEquals(results, new Point(1, 1));
	}

	@Test
	public void getFlagPos() {
		Point results = finder.getFlagPos();
		assertEquals(results, new Point(3, 4));
	}

	@Test
	public void getWallsNorth() {
		List<Point> results = finder.getWalls(Orientation.NORTH);
		assertEquals(results, Arrays.asList(new Point(2, 1)));
	}

	@Test
	public void getWallsSouth() {
		List<Point> results = finder.getWalls(Orientation.SOUTH);
		assertEquals(results, Arrays.asList(new Point(2, 4)));
	}

	@Test
	public void getWallsEast() {
		List<Point> results = finder.getWalls(Orientation.EAST);
		assertEquals(results, Arrays.asList(new Point(9, 9)));
	}

	@Test
	public void getWallsWest() {
		List<Point> results = finder.getWalls(Orientation.WEST);
		assertEquals(results, Arrays.asList(new Point(1, 1)));
	}

	@Test
	public void getHolesWithHoles() {
		List<Point> holes = new ArrayList<>();
		holes.add(new Point(6, 6));
		holes.addAll(finder.generateGameborder(12, 26));

		List<Point> results = finder.getHoles();
		assertEquals(results, holes);
	}

	@Test
	public void getHolesWithoutHoles() {
		List<Point> hole = new ArrayList<>();
		when(thought12.getInformation()).thenReturn(Arrays.asList(hole));

		List<Point> results = finder.getHoles();
		assertEquals(results, finder.generateGameborder(12, 26));
	}

	@Test
	public void getLockedCardsWithLockedCards() {
		List<ProgramCard> results = finder.getLockedCards();
		assertEquals(results, Arrays.asList(move3_2));
	}

	@Test(expected = RuntimeException.class)
	public void validateCardsNoCard() {
		finder.validateType(1, ProgramCard.class);
	}

	@Test(expected = RuntimeException.class)
	public void validateCardsNoPoint() {
		finder.validateType(1, Point.class);
	}

	@Test(expected = RuntimeException.class)
	public void validateCardsNoOrientation() {
		finder.validateType(1, Orientation.class);
	}

	@Test(expected = RuntimeException.class)
	public void validateCardsWrongKey() {
		finder.validateType(1, Error.class);
	}

	@Test
	public void getEndpointWithouGoal() {
		Point results = finder.getEndpoint(Arrays.asList(1, -3, 3, -4));
		assertEquals(results, new Point(3, 2));
	}

	@Test
	public void getEndpointWithGoal() {
		Point results = finder.getEndpoint(Arrays.asList(3, -3, 2, -4, 5));
		assertEquals(results, new Point(3, 4));
	}

	@Test
	public void getLockedCardsWithoutLockedCards() {
		when(thought2.getInformation()).thenReturn(
				Arrays.asList(Arrays.asList()));

		List<ProgramCard> results = finder.getLockedCards();
		assertEquals(results, Arrays.asList());
	}
}
