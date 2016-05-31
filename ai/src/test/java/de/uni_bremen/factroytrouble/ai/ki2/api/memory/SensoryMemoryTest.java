package de.uni_bremen.factroytrouble.ai.ki2.api.memory;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import de.uni_bremen.factroytrouble.ai.ais.AIPlayer2;
import de.uni_bremen.factroytrouble.ai.ki2.ai.memory.ScullySensoryMemory;
import de.uni_bremen.factroytrouble.ai.ki2.ai.memory.ScullyVisual;
import de.uni_bremen.factroytrouble.board.Board;
import de.uni_bremen.factroytrouble.board.Field;
import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.gameobjects.ConveyorBelt;
import de.uni_bremen.factroytrouble.gameobjects.Flag;
import de.uni_bremen.factroytrouble.gameobjects.Gear;
import de.uni_bremen.factroytrouble.gameobjects.Hole;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.gameobjects.Wall;
import de.uni_bremen.factroytrouble.player.ProgramCard;

@RunWith(MockitoJUnitRunner.class)
public class SensoryMemoryTest {

    @Mock
    private Robot robot1, robot2;
    @Mock
    private Board mockBoard;
    @Mock
    private Field mockField;
    @Mock
    private Hole hole1, hole2;
    @Mock
    private Gear gear1, gear2;
    @Mock
    private Flag flag1, flag2;
    @Mock
    private ConveyorBelt cb1, cb2, cb3, cb4;
    @Mock
    private Wall wall1, wall2;
    @Mock
    private ScullyVisual visual;
    @Mock
    private ProgramCard mockCard1, mockCard2, mockCard3, mockCard4, mockCard5;
    @Mock
    private AIPlayer2 player;
    private Map<Point, Tile> mockTiles;

    private SensoryMemory sensMemory;

    @Before
    public void setUp() throws Exception {
        Mockito.when(robot1.getName()).thenReturn("Scully");
        Mockito.when(flag1.getNumber()).thenReturn(1);
        Mockito.when(visual.getFlagPosition(1)).thenReturn(new Point(5, 5));
        sensMemory = new ScullySensoryMemory(visual, player);
        mockTiles = defaultTileMap();
        HashMap<Point, Field> map = new HashMap<>();
        map.put(new Point(0, 0), mockField);
        Mockito.when(mockBoard.getFields()).thenReturn(map);
        Mockito.when(mockField.getTiles()).thenReturn(mockTiles);

    }

    @Test
    public void shouldReturnNullNoSuchKey() {

        List<String> tempList = new ArrayList<>();
        tempList.add("nothing");
        assertEquals(null, sensMemory.getInformation(tempList));
    }

    @Test
    public void shouldReturnNullListSizeZero() {
        List<String> tempList = new ArrayList<>();
        assertEquals(null, sensMemory.getInformation(tempList));

    }

    @Test
    public void shouldGetFlagPosition() {
        List<String> tempList = new ArrayList<>();
        tempList.add("flag");
        tempList.add("1");
        assertEquals(new Point(5, 5), sensMemory.getInformation(tempList).getInformation().get(0));

    }

    @Test
    public void shouldGetNextFlag() {
        Mockito.when(visual.getNextFlag()).thenReturn(new Point(6, 6));
        List<String> tempList = new ArrayList<>();
        tempList.add("flag");
        tempList.add("next");
        assertEquals(new Point(6, 6), sensMemory.getInformation(tempList).getInformation().get(0));

    }

    @Test
    public void shouldGetAllHoles() {
        List<Point> tempHolePoints = new ArrayList<>();
        tempHolePoints.add(new Point(0, 0));
        tempHolePoints.add(new Point(0, 1));
        tempHolePoints.add(new Point(0, 2));
        tempHolePoints.add(new Point(0, 3));
        Mockito.when(visual.getHoles()).thenReturn(tempHolePoints);
        List<String> tempList = new ArrayList<>();
        tempList.add("holes");
        assertEquals(tempHolePoints, sensMemory.getInformation(tempList).getInformation().get(0));

    }

    @Test
    public void shouldGetAllWallsNorth() {
        List<Point> tempPoints = new ArrayList<>();
        tempPoints.add(new Point(0, 0));
        tempPoints.add(new Point(0, 1));
        tempPoints.add(new Point(0, 2));
        tempPoints.add(new Point(0, 3));
        Mockito.when(visual.getWallsNorth()).thenReturn(tempPoints);
        List<String> tempList = new ArrayList<>();
        tempList.add("walls");
        tempList.add("north");
        assertEquals(tempPoints, sensMemory.getInformation(tempList).getInformation().get(0));
    }

    @Test
    public void shouldGetAllWallsEast() {
        List<Point> tempPoints = new ArrayList<>();
        tempPoints.add(new Point(0, 0));
        tempPoints.add(new Point(0, 1));
        tempPoints.add(new Point(0, 2));
        tempPoints.add(new Point(0, 3));
        Mockito.when(visual.getWallsEast()).thenReturn(tempPoints);
        List<String> tempList = new ArrayList<>();
        tempList.add("walls");
        tempList.add("east");
        assertEquals(tempPoints, sensMemory.getInformation(tempList).getInformation().get(0));
    }

    @Test
    public void shouldGetAllWallsSouth() {
        List<Point> tempPoints = new ArrayList<>();
        tempPoints.add(new Point(0, 0));
        tempPoints.add(new Point(0, 1));
        tempPoints.add(new Point(0, 2));
        tempPoints.add(new Point(0, 3));
        Mockito.when(visual.getWallsSouth()).thenReturn(tempPoints);
        List<String> tempList = new ArrayList<>();
        tempList.add("walls");
        tempList.add("south");
        assertEquals(tempPoints, sensMemory.getInformation(tempList).getInformation().get(0));
    }

    @Test
    public void shouldGetAllWallsWest() {
        List<Point> tempPoints = new ArrayList<>();
        tempPoints.add(new Point(0, 0));
        tempPoints.add(new Point(0, 1));
        tempPoints.add(new Point(0, 2));
        tempPoints.add(new Point(0, 3));
        Mockito.when(visual.getWallsWest()).thenReturn(tempPoints);
        List<String> tempList = new ArrayList<>();
        tempList.add("walls");
        tempList.add("west");
        assertEquals(tempPoints, sensMemory.getInformation(tempList).getInformation().get(0));
    }

    @Test
    public void shouldGetGearsRight() {
        List<Point> tempPoints = new ArrayList<>();
        tempPoints.add(new Point(0, 0));
        tempPoints.add(new Point(0, 1));
        tempPoints.add(new Point(0, 2));
        tempPoints.add(new Point(0, 3));
        Mockito.when(visual.getGearsRight()).thenReturn(tempPoints);
        List<String> tempList = new ArrayList<>();
        tempList.add("right");
        tempList.add("gears");
        assertEquals(tempPoints, sensMemory.getInformation(tempList).getInformation().get(0));
    }

    @Test
    public void shouldGetGearsLeft() {
        List<Point> tempPoints = new ArrayList<>();
        tempPoints.add(new Point(0, 0));
        tempPoints.add(new Point(0, 1));
        tempPoints.add(new Point(0, 2));
        tempPoints.add(new Point(0, 3));
        Mockito.when(visual.getGearsLeft()).thenReturn(tempPoints);
        List<String> tempList = new ArrayList<>();
        tempList.add("left");
        tempList.add("gears");
        assertEquals(tempPoints, sensMemory.getInformation(tempList).getInformation().get(0));
    }

    @Test
    public void shouldGetHighestPoint() {
        Point tempPoints = new Point(0, 3);
        Mockito.when(visual.getHighestPoint()).thenReturn(tempPoints);
        List<String> tempList = new ArrayList<>();
        tempList.add("highest");
        tempList.add("point");
        assertEquals(tempPoints, sensMemory.getInformation(tempList).getInformation().get(0));
    }

    @Test
    public void shouldGetLockedCards() {
        List<ProgramCard> tempCards = new ArrayList<>();
        tempCards.add(mockCard1);
        tempCards.add(mockCard2);
        tempCards.add(mockCard3);
        Mockito.when(visual.getLockedCards()).thenReturn(tempCards);
        List<String> temp = new ArrayList<>();
        temp.add("cards");
        temp.add("locked");
        assertEquals(tempCards, sensMemory.getInformation(temp).getInformation().get(0));
    }

    @Test
    public void shouldGetOwnCards() {
        List<String> tempList = new ArrayList<>();
        tempList.add("cards");
        tempList.add("player");
        List<ProgramCard> cards = new ArrayList<>();
        cards.add(mockCard1);
        cards.add(mockCard2);
        cards.add(mockCard3);
        cards.add(mockCard4);
        cards.add(mockCard5);
        Mockito.when(visual.getCards()).thenReturn(cards);
        List<ProgramCard> actualListOfCards = (List<ProgramCard>) (Object) sensMemory.getInformation(tempList)
                .getInformation();
        assertEquals(5, actualListOfCards.size());
    }

    private Map<Point, Tile> defaultTileMap() {
        Map<Point, Tile> m = new HashMap<>();
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                Tile tile = Mockito.mock(Tile.class);
                Mockito.when(tile.getCoordinates()).thenReturn(new Point(i, j));
                Mockito.when(tile.getAbsoluteCoordinates()).thenReturn(new Point(i, j));
                m.put(new Point(i, j), tile);
            }
        }
        return m;
    }

}
