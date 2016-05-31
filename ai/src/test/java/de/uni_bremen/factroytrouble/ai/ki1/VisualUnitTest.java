package de.uni_bremen.factroytrouble.ai.ki1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import de.uni_bremen.factroytrouble.ai.ki1.memory.KeyUnit;
import de.uni_bremen.factroytrouble.ai.ki1.memory.MemoryUnit;
import de.uni_bremen.factroytrouble.ai.ki1.mock.MockTile;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import de.uni_bremen.factroytrouble.api.ki1.BoardArea;
import de.uni_bremen.factroytrouble.api.ki1.Placement;
import de.uni_bremen.factroytrouble.api.ki1.Timer;
import de.uni_bremen.factroytrouble.api.ki1.memory.Chunk;
import de.uni_bremen.factroytrouble.board.Board;
import de.uni_bremen.factroytrouble.board.Dock;
import de.uni_bremen.factroytrouble.board.Field;
import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.gameobjects.Flag;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.observer.Event;
import de.uni_bremen.factroytrouble.player.Player;

/**
 * Testet die {@link VisualUnit}.
 * 
 * @author tim, artur
 */
@RunWith(MockitoJUnitRunner.class)
public class VisualUnitTest {

    private MemoryUnit mem = new MemoryUnit();

    private Timer mockTimer = Mockito.mock(Timer.class);

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void constructorFailMemory() {
        exception.expect(IllegalArgumentException.class);
        new VisualUnit((MemoryUnit) null, getDefaultBoard(), getDefaultPlayerMap(), 0, mockTimer);
    }

    @Test
    public void constructorFailBoard() {
        exception.expect(IllegalArgumentException.class);
        new VisualUnit(mem, null, getDefaultPlayerMap(), 0, mockTimer);
    }

    @Test
    public void constructorFailPlayers() {
        exception.expect(IllegalArgumentException.class);
        new VisualUnit(mem, getDefaultBoard(), null, 0, mockTimer);
    }
    
    @Test
    public void shouldStoreHpOnSpam() {
        final int ownPlayerId = 0;
        final Integer expectedHp = 7;
        final VisualUnit visualUnit = createVisualUnit();
        final Player player = addOwnPlayer(visualUnit, ownPlayerId);
        Mockito.when(player.getRobot().getLives()).thenReturn(1);
        Mockito.when(player.getRobot().getHP()).thenReturn(expectedHp);

        final KeyUnit hpKey = memKey(VisualUnit.HP_KEY, ownPlayerId);
        visualUnit.spam(player.getRobot(), Event.HEALED, null);
        assertEquals(expectedHp, getFromMem(hpKey, Integer.class));
    }
    
    @Test
    public void shouldStoreHpOnGet() {
        final int ownPlayerId = 0;
        final Integer expectedHp = 7;
        final VisualUnit visualUnit = createVisualUnit();
        final Player player = addOwnPlayer(visualUnit, ownPlayerId);
        Mockito.when(player.getRobot().getLives()).thenReturn(1);
        Mockito.when(player.getRobot().getHP()).thenReturn(expectedHp);

        final KeyUnit hpKey = memKey(VisualUnit.HP_KEY, ownPlayerId);
        assertEquals(expectedHp.intValue(), visualUnit.getHP(ownPlayerId));
        assertEquals(expectedHp, getFromMem(hpKey, Integer.class));   
    }
    
    @Test
    public void shouldStoreLivesOnSpam() {
        final int ownPlayerId = 0;
        final Integer expectedLives = 2;
        final VisualUnit visualUnit = createVisualUnit();
        final Player player = addOwnPlayer(visualUnit, ownPlayerId);
        Mockito.when(player.getRobot().getLives()).thenReturn(expectedLives);
        
        final KeyUnit livesKey = memKey(VisualUnit.LIVES_KEY, ownPlayerId);
        final Robot source = Mockito.mock(Robot.class);
        visualUnit.spam(player.getRobot(), Event.KILLED, source);
        assertEquals(expectedLives, getFromMem(livesKey, Integer.class));
    }
    
    @Test
    public void shouldStoreLivesOnGet() {
        final int ownPlayerId = 0;
        final Integer expectedLives = 2;
        final VisualUnit visualUnit = createVisualUnit();
        final Player player = addOwnPlayer(visualUnit, ownPlayerId);
        Mockito.when(player.getRobot().getLives()).thenReturn(expectedLives);
        
        final KeyUnit livesKey = memKey(VisualUnit.LIVES_KEY, ownPlayerId);
        assertEquals(expectedLives.intValue(), visualUnit.getLives(ownPlayerId));
        assertEquals(expectedLives, getFromMem(livesKey, Integer.class)); 
    }
    
    @Test
    public void shouldStorePowerdownOnSpamPowerdownEvent() {
        final int ownPlayerId = 0;
        final boolean expectedPowerdown = false;
        final VisualUnit visualUnit = createVisualUnit();
        final Player player = addOwnPlayer(visualUnit, ownPlayerId);
        Mockito.when(player.getRobot().isPoweredDown()).thenReturn(expectedPowerdown);
        
        final KeyUnit powdownKey = memKey(VisualUnit.POWERDOWN_KEY, ownPlayerId);
        visualUnit.spam(player.getRobot(), Event.POWERED_DOWN, null);
        assertEquals(expectedPowerdown, getFromMem(powdownKey, Boolean.class));
    }
    
    @Test
    public void shouldStorePowerdownOnSpamWakeUpEvent() {
        final int ownPlayerId = 0;
        final boolean expectedPowerdown = true;
        final VisualUnit visualUnit = createVisualUnit();
        final Player player = addOwnPlayer(visualUnit, ownPlayerId);
        Mockito.when(player.getRobot().isPoweredDown()).thenReturn(expectedPowerdown);
        
        final KeyUnit powdownKey = memKey(VisualUnit.POWERDOWN_KEY, ownPlayerId);
        visualUnit.spam(player.getRobot(), Event.WAKED_UP, null);
        assertEquals(expectedPowerdown, getFromMem(powdownKey, Boolean.class));
    }
    
    @Test
    public void shouldStorePowerdownOnGet() {
        final int ownPlayerId = 0;
        final boolean expectedPowerdown = false;
        final VisualUnit visualUnit = createVisualUnit();
        final Player player = addOwnPlayer(visualUnit, ownPlayerId);
        Mockito.when(player.getRobot().isPoweredDown()).thenReturn(expectedPowerdown);
        
        final KeyUnit powdownKey = memKey(VisualUnit.POWERDOWN_KEY, ownPlayerId);
        assertEquals(expectedPowerdown, visualUnit.isPoweredDown(ownPlayerId));
        assertEquals(expectedPowerdown, getFromMem(powdownKey, Boolean.class)); 
    }
    
    @Test
    @Ignore
    public void shouldStoreRespawnOnSpam() {
        final int ownPlayerId = 0;
        final Point pt00 = new Point(0,0);
        final Board board = getDefaultBoard();
        final Tile tile = board.getFields().get(pt00).getTiles().get(pt00);
        final Flag flag = Mockito.mock(Flag.class);
        Mockito.when(tile.getFieldObject()).thenReturn(flag);
        Mockito.when(tile.getAbsoluteCoordinates()).thenReturn(pt00);
        final Placement expectedRespawn = new PlacementUnit(pt00, null, tile);
        final VisualUnit visualUnit = createVisualUnit();
        final Player player = addOwnPlayer(visualUnit, ownPlayerId);
        Mockito.when(player.getRobot().getCurrentTile()).thenReturn(tile);
        
        final KeyUnit respawnKey = memKey(VisualUnit.RESPAWN_KEY, ownPlayerId);
        visualUnit.spam(player.getRobot(), Event.MOVED, null);
        assertEquals(expectedRespawn, getFromMem(respawnKey, Placement.class)); 
    }
    
    @Test
    public void shouldStoreRespawnOnGet() {
      //TODO
    }
    
    @Test
    public void shouldStorePlacementOnSpam() {
        //TODO
    }
    
    @Test
    public void shouldStorePlacementOnGet() {
      //TODO
    }
    
    @Test
    public void shouldStoreCardOnGet() {
      //TODO
    }
    
    @Test
    public void shouldStoreShovedEventOnSpam() {
        //TODO
    }
    
    @Test
    public void shouldStoreShotEventOnSpam() {
        //TODO
    }
    
    @Test
    public void shouldStoreKilledEventOnSpam() {
        //TODO
    }
    

    private VisualUnit createVisualUnit() {
        return new VisualUnit(mem, getDefaultBoard());
    }
    
    private Player addPlayer(VisualUnit vu, int id) {
        final Player player = Mockito.mock(Player.class);
        final Robot robot = Mockito.mock(Robot.class);
        Mockito.when(player.getRobot()).thenReturn(robot);
        Mockito.when(player.getRobot().getLives()).thenReturn(1);
        vu.addPlayer(player, id);
        return player;
    }
    
    private Player addOwnPlayer(VisualUnit vu, int id) {
        final Player player = Mockito.mock(Player.class);
        final Robot robot = Mockito.mock(Robot.class);
        Mockito.when(player.getRobot()).thenReturn(robot);
        Mockito.when(player.getRobot().getLives()).thenReturn(1);
        vu.addPlayer(player, id);
        return player;
    }
    
    private <T> T getFromMem(KeyUnit key, Class<T> clazz) {
        Chunk chunk = mem.retrieveChunk(key);
        if(chunk == null) {
            return null;
        }
        return clazz.cast(chunk.getData());
    }
    
    private KeyUnit memKey(String base, Object ... objs) {
        return new KeyUnit(String.format(base, objs));
    }

    /**
     * getPlayerPlacement sollte null liefern, wenn es die gegebene Id nicht
     * gibt
     */
    @Test
    public void getPlayerPlacementInvalidIdTest() {
        Map<Integer, Player> players = getDefaultPlayerMap(0);
        Point relativePlayerPos = new Point(2, 7);
        Board board = getDefaultBoard();
        Field f = getDefaultField();
        Map<Point, Tile> tiles = defaulteTileMap();
        Mockito.when(f.getTiles()).thenReturn(tiles);
        Tile playerTile = f.getTiles().get(relativePlayerPos);
        playerTile.setRobot(players.get(0).getRobot());
        Mockito.when(f.getCoordinates()).thenReturn(new Point(0, 1));
        VisualUnit visualUnit = new VisualUnit(mem, board, players, 0, mockTimer);
        assertTrue(visualUnit.getPlayerPlacement(42) == null);
    }

    /**
     * getPlayerPlacement sollte null liefern, wenn kein Spieler auf dem Board
     * steht
     */
    @Test
    public void getPlayerPlacementNoPlayerTest() {
        Map<Integer, Player> players = getDefaultPlayerMap(0);
        Mockito.when(players.get(0).getRobot()).thenReturn(getDefaultRobotMap(0).get(0));

        Board board = getDefaultBoard();
        Field f = getDefaultField();
        Map<Point, Tile> tiles = defaulteTileMap();
        Mockito.when(f.getTiles()).thenReturn(tiles);
        Mockito.when(f.getCoordinates()).thenReturn(new Point(0, 1));
        VisualUnit visualUnit = new VisualUnit(mem, board, players, 0, mockTimer);
        assertTrue(visualUnit.getPlayerPlacement(0) == null);
    }

    /**
     * getPlayerPlacement sollte die korrekte Position liefern, spieler steht
     * auf dem Dock
     */
    @Test
    public void getPlayerPlacementOnePlayerTest2() {
        Map<Integer, Player> players = getDefaultPlayerMap(0);
        Player player = players.get(0);
        Mockito.when(players.get(0).getRobot()).thenReturn(getDefaultRobotMap(0).get(0));

        Board board = getDefaultBoard();
        Point playerPos = new Point(0, 0);
        Point relativePlayerPos = new Point(0, 0);
        Tile playerTile = board.getFields().get(new Point(0, 0)).getTiles().get(relativePlayerPos);
        playerTile.setRobot(player.getRobot());

        VisualUnit visualUnit = new VisualUnit(mem, board, players, 0, mockTimer);
        PlacementUnit expected = new PlacementUnit(playerPos, player.getRobot().getOrientation(),
                player.getRobot().getCurrentTile());
        assertEquals(expected, visualUnit.getPlayerPlacement(0));
    }

    /**
     * getPlayerPlacement sollte bei mehreren Robotern die korrekte Position
     * liefern
     */
    @Test
    public void getPlayerPlacementMultiplePlayersTest() {
        Board board = getDefaultBoard();
        Map<Integer, Player> players = getDefaultPlayerMap(1, 2);
        Player player1 = players.get(1);
        Player player2 = players.get(2);

        Mockito.when(players.get(1).getRobot()).thenReturn(getDefaultRobotMap(0).get(0));
        Mockito.when(players.get(2).getRobot()).thenReturn(getDefaultRobotMap(1).get(1));
        Point relativePlayer1Pos = new Point(5, 5);
        Tile player1Tile = board.getFields().get(new Point(0, 1)).getTiles().get(relativePlayer1Pos);
        player1Tile.setRobot(player1.getRobot());

        Point player2Pos = new Point(0, 1);
        Tile player2Tile = board.getFields().get(new Point(0, 0)).getTiles().get(player2Pos);
        player2Tile.setRobot(player2.getRobot());

        VisualUnit visualUnit = new VisualUnit(mem, board, players, 0, mockTimer);
        PlacementUnit expected = new PlacementUnit(player2Pos, player2.getRobot().getOrientation(),
                player2.getRobot().getCurrentTile());
        assertEquals(expected, visualUnit.getPlayerPlacement(2));
    }

    /**
     * Soll null liefern, wenn die Flagge nicht gefunden wird
     */
    @Test
    public void getFlagPositionNoFlagTest() {
        VisualUnit visualUnit = getDefaultVisualUnit();
        assertNull(visualUnit.getFlagPosition(0));
        assertNull(visualUnit.getFlagPosition(1));
    }

    /**
     * Soll Flagge liefern, ohne andere Flaggen
     */
    @Test
    public void getFlagPositionOneFlagTest() {
        Board board = getDefaultBoard();

        Flag flag = Mockito.mock(Flag.class);
        Mockito.when(flag.getNumber()).thenReturn(0);
        Point relativeFlagPos = new Point(3, 0);
        Tile tile = board.getFields().get(new Point(0, 1)).getTiles().get(relativeFlagPos);
        tile.setFieldObject(flag);

        PlacementUnit expectedPlacement = new PlacementUnit(new Point(3, 4), null, tile);

        VisualUnit visualUnit = new VisualUnit(mem, board, getDefaultPlayerMap(), 0, mockTimer);
        assertEquals(expectedPlacement, visualUnit.getFlagPosition(0));
        assertNull(visualUnit.getFlagPosition(1));
    }

    /**
     * Soll korrekte Flagge liefern, mit anderen Flaggen
     */
    @Test
    public void getFlagPositionMultipleFlagsTest() {
        Board board = getDefaultBoard();

        Flag flag1 = Mockito.mock(Flag.class);
        Mockito.when(flag1.getNumber()).thenReturn(0);

        Point relativeFlag1Pos = new Point(3, 0);
        Tile tile1 = board.getFields().get(new Point(0, 1)).getTiles().get(relativeFlag1Pos);
        tile1.setFieldObject(flag1);

        PlacementUnit expectedPlacement1 = new PlacementUnit(new Point(3, 4), null, tile1);

        Flag flag2 = Mockito.mock(Flag.class);
        Mockito.when(flag2.getNumber()).thenReturn(1);
        Point relativeFlag2Pos = new Point(5, 0);
        Tile tile2 = board.getFields().get(new Point(0, 1)).getTiles().get(relativeFlag2Pos);
        tile2.setFieldObject(flag2);
        PlacementUnit expectedPlacement2 = new PlacementUnit(new Point(5, 4), null, tile2);
        VisualUnit visualUnit = new VisualUnit(mem, board, getDefaultPlayerMap(), 0, mockTimer);
        assertEquals(expectedPlacement1, visualUnit.getFlagPosition(0));
        assertEquals(expectedPlacement2, visualUnit.getFlagPosition(1));
    }

    /**
     * Soll IllegalArgumentException werfen, wenn center null ist
     */
    @Test
    public void getAreaNullCenterTest() {
        exception.expect(IllegalArgumentException.class);
        getDefaultVisualUnit().getArea(null, 3);
    }

    /**
     * Soll null liefern, wenn center kein vorhandener Point ist
     */
    @Test
    public void getAreaInvalidCenterTest() {
        assertNull(getDefaultVisualUnit().getArea(new Point(-1, 0), 3));
    }

    /**
     * Soll IllegalArgumentException werfen, wenn Area Länge == 0 ist
     */
    @Test
    public void getAreaInvalidLengthTest() {
        exception.expect(IllegalArgumentException.class);
        getDefaultVisualUnit().getArea(new Point(0, 0), 0);
    }

    /**
     * Soll IllegalArgumentException werfen, wenn Area Länge gerade ist
     */
    @Test
    public void getAreaInvalidLengthTest2() {
        exception.expect(IllegalArgumentException.class);
        getDefaultVisualUnit().getArea(new Point(0, 0), 2);
    }

    /**
     * Soll korrekte Area liefern, wenn sie leer ist
     */
    @Test
    public void getEmptyAreaTest() {
        VisualUnit visualUnit = getDefaultVisualUnit();
        Map<Point, Tile> expectedTiles = getEmptyTiles(3);
        BoardArea expectedArea = new BoardAreaUnit(new Point(1, 1), expectedTiles);
        BoardArea actualArea = visualUnit.getArea(new Point(1, 1), 3);
        assertEquals(3 * 3, actualArea.getTiles().size());
        assertEquals(expectedArea.getTiles(), actualArea.getTiles());
        assertEquals(expectedArea, actualArea);

    }

    /**
     * Soll korrekte Area liefern, wenn center in der linken unteren Ecke liegt
     */
    @Test
    public void getCutoffAreaTest() {
        VisualUnit visualUnit = getDefaultVisualUnit();

        Map<Point, Tile> expectedTiles = new HashMap<Point, Tile>();
        expectedTiles.put(new Point(0, 0), getMockTile(new Point(0, 0)));
        expectedTiles.put(new Point(0, 1), getMockTile(new Point(0, 1)));
        expectedTiles.put(new Point(1, 0), getMockTile(new Point(1, 0)));
        expectedTiles.put(new Point(1, 1), getMockTile(new Point(1, 1)));

        BoardArea expectedArea = new BoardAreaUnit(new Point(0, 0), expectedTiles);

        BoardArea actualArea = visualUnit.getArea(new Point(0, 0), 3);
        assertEquals(4, actualArea.getTiles().size());
        assertEquals(expectedArea, actualArea);
    }

    /**
     * Soll korrekte Area liefern, wenn center in der rechten unteren Ecke liegt
     */
    @Test
    public void getCutoffAreaTestBottomRightCorner() {
        VisualUnit visualUnit = getDefaultVisualUnit();

        Map<Point, Tile> expectedTiles = new HashMap<Point, Tile>();
        expectedTiles.put(new Point(11, 0), getMockTile(new Point(11, 0)));
        expectedTiles.put(new Point(11, 1), getMockTile(new Point(11, 1)));
        expectedTiles.put(new Point(10, 0), getMockTile(new Point(10, 0)));
        expectedTiles.put(new Point(10, 1), getMockTile(new Point(10, 1)));

        BoardArea expectedArea = new BoardAreaUnit(new Point(11, 0), expectedTiles);

        BoardArea actualArea = visualUnit.getArea(new Point(11, 0), 3);
        assertEquals(4, actualArea.getTiles().size());
        assertEquals(expectedArea, actualArea);
    }

    /**
     * Soll korrekte Area liefern, wenn center in der rechten unteren Ecke liegt
     */
    @Test
    public void getCutoffAreaTestUpperRightCorner() {
        VisualUnit visualUnit = getDefaultVisualUnit();

        Map<Point, Tile> expectedTiles = new HashMap<Point, Tile>();
        expectedTiles.put(new Point(11, 15), getMockTile(new Point(11, 15)));
        expectedTiles.put(new Point(10, 15), getMockTile(new Point(10, 15)));
        expectedTiles.put(new Point(11, 14), getMockTile(new Point(11, 14)));
        expectedTiles.put(new Point(10, 14), getMockTile(new Point(10, 14)));

        BoardArea expectedArea = new BoardAreaUnit(new Point(11, 15), expectedTiles);

        BoardArea actualArea = visualUnit.getArea(new Point(11, 15), 3);
        assertEquals(4, actualArea.getTiles().size());
        assertEquals(expectedArea, actualArea);
    }

    /**
     * Soll korrekte Area liefern, wenn center in der rechten unteren Ecke liegt
     */
    @Test
    public void getCutoffAreaTestUpperLeftCorner() {
        VisualUnit visualUnit = getDefaultVisualUnit();

        Map<Point, Tile> expectedTiles = new HashMap<Point, Tile>();
        expectedTiles.put(new Point(1, 15), getMockTile(new Point(1, 15)));
        expectedTiles.put(new Point(0, 15), getMockTile(new Point(0, 15)));
        expectedTiles.put(new Point(1, 14), getMockTile(new Point(1, 14)));
        expectedTiles.put(new Point(0, 14), getMockTile(new Point(0, 14)));

        BoardArea expectedArea = new BoardAreaUnit(new Point(0, 15), expectedTiles);

        BoardArea actualArea = visualUnit.getArea(new Point(0, 15), 3);
        assertEquals(4, actualArea.getTiles().size());
        assertEquals(expectedArea, actualArea);
    }

    /**
     * Soll eine korrekte Area liefern die auf dem Dock liegt
     */
    @Test
    public void shouldGetCorrectAreaUsualArea1() {
        VisualUnit visualUnit = getDefaultVisualUnit();
        Map<Point, Tile> expectedTiles = new HashMap<>();
        expectedTiles.put(new Point(0, 0), getMockTile(new Point(0, 0)));
        expectedTiles.put(new Point(0, 1), getMockTile(new Point(0, 1)));
        expectedTiles.put(new Point(0, 2), getMockTile(new Point(0, 2)));
        expectedTiles.put(new Point(1, 0), getMockTile(new Point(1, 0)));
        expectedTiles.put(new Point(1, 1), getMockTile(new Point(1, 1)));
        expectedTiles.put(new Point(1, 2), getMockTile(new Point(1, 2)));
        expectedTiles.put(new Point(2, 0), getMockTile(new Point(2, 0)));
        expectedTiles.put(new Point(2, 1), getMockTile(new Point(2, 1)));
        expectedTiles.put(new Point(2, 2), getMockTile(new Point(2, 2)));

        BoardArea expectedArea = new BoardAreaUnit(new Point(1, 1), expectedTiles);
        BoardArea actualArea = visualUnit.getArea(new Point(1, 1), 3);
        assertEquals(9, actualArea.getTiles().size());
        assertEquals(expectedArea, actualArea);
    }

    /**
     * Soll eine korrekte Area liefern die auf dem Dock liegt
     */
    @Test
    public void shouldGetCorrectAreaUsualArea2() {
        VisualUnit visualUnit = getDefaultVisualUnit();
        Map<Point, Tile> expectedTiles = new HashMap<>();

        expectedTiles.put(new Point(1, 1), getMockTile(new Point(1, 1)));
        expectedTiles.put(new Point(1, 2), getMockTile(new Point(1, 2)));
        expectedTiles.put(new Point(1, 3), getMockTile(new Point(1, 3)));
        expectedTiles.put(new Point(2, 1), getMockTile(new Point(2, 1)));
        expectedTiles.put(new Point(2, 2), getMockTile(new Point(2, 2)));
        expectedTiles.put(new Point(2, 3), getMockTile(new Point(2, 3)));
        expectedTiles.put(new Point(3, 1), getMockTile(new Point(3, 1)));
        expectedTiles.put(new Point(3, 2), getMockTile(new Point(3, 2)));
        expectedTiles.put(new Point(3, 3), getMockTile(new Point(3, 3)));

        BoardArea expectedArea = new BoardAreaUnit(new Point(2, 2), expectedTiles);
        BoardArea actualArea = visualUnit.getArea(new Point(2, 2), 3);
        assertEquals(9, actualArea.getTiles().size());
        assertEquals(expectedArea, actualArea);
    }

    /**
     * Soll eine korrekte Area liefern die nicht auf dem Dock liegt
     */
    @Test
    public void shouldGetCorrectAreaUsualArea3() {
        VisualUnit visualUnit = getDefaultVisualUnit();
        Map<Point, Tile> expectedTiles = new HashMap<>();

        expectedTiles.put(new Point(4, 5), getMockTile(new Point(4, 5)));
        expectedTiles.put(new Point(5, 5), getMockTile(new Point(5, 5)));
        expectedTiles.put(new Point(6, 5), getMockTile(new Point(6, 5)));
        expectedTiles.put(new Point(4, 6), getMockTile(new Point(4, 6)));
        expectedTiles.put(new Point(5, 6), getMockTile(new Point(5, 6)));
        expectedTiles.put(new Point(6, 6), getMockTile(new Point(6, 6)));
        expectedTiles.put(new Point(4, 7), getMockTile(new Point(4, 7)));
        expectedTiles.put(new Point(5, 7), getMockTile(new Point(5, 7)));
        expectedTiles.put(new Point(6, 7), getMockTile(new Point(6, 7)));
        BoardArea expectedArea = new BoardAreaUnit(new Point(5, 6), expectedTiles);
        BoardArea actualArea = visualUnit.getArea(new Point(5, 6), 3);
        assertEquals(9, actualArea.getTiles().size());
        assertEquals(expectedArea, actualArea);
    }

    /**
     * Soll eine korrekte Area liefern die auf sowohl auf dem Dock wie auf dem
     * Field liegt
     */
    @Test
    public void shouldGetCorrectAreaUsualArea4() {
        VisualUnit visualUnit = getDefaultVisualUnit();
        Map<Point, Tile> expectedTiles = new HashMap<>();

        expectedTiles.put(new Point(4, 2), getMockTile(new Point(4, 2)));
        expectedTiles.put(new Point(5, 2), getMockTile(new Point(5, 2)));
        expectedTiles.put(new Point(6, 2), getMockTile(new Point(6, 2)));
        expectedTiles.put(new Point(4, 3), getMockTile(new Point(4, 3)));
        expectedTiles.put(new Point(5, 3), getMockTile(new Point(5, 3)));
        expectedTiles.put(new Point(6, 3), getMockTile(new Point(6, 3)));
        expectedTiles.put(new Point(4, 4), getMockTile(new Point(4, 4)));
        expectedTiles.put(new Point(5, 4), getMockTile(new Point(5, 4)));
        expectedTiles.put(new Point(6, 4), getMockTile(new Point(6, 4)));
        BoardArea expectedArea = new BoardAreaUnit(new Point(5, 3), expectedTiles);
        BoardArea actualArea = visualUnit.getArea(new Point(5, 3), 3);
        assertEquals(9, actualArea.getTiles().size());
        assertEquals(expectedArea, actualArea);
    }

    /**
     * Soll eine korrekte Area liefern deren Center an der unteren Kante liegt
     */
    @Test
    public void shouldGetCorrectAreaCenterOnBottomEdge() {
        VisualUnit visualUnit = getDefaultVisualUnit();
        Map<Point, Tile> expectedTiles = new HashMap<>();
        expectedTiles.put(new Point(1, 0), getMockTile(new Point(1, 0)));
        expectedTiles.put(new Point(2, 0), getMockTile(new Point(2, 0)));
        expectedTiles.put(new Point(3, 0), getMockTile(new Point(3, 0)));
        expectedTiles.put(new Point(1, 1), getMockTile(new Point(1, 1)));
        expectedTiles.put(new Point(2, 1), getMockTile(new Point(2, 1)));
        expectedTiles.put(new Point(3, 1), getMockTile(new Point(3, 1)));
        BoardArea expectedArea = new BoardAreaUnit(new Point(2, 0), expectedTiles);
        BoardArea actualArea = visualUnit.getArea(new Point(2, 0), 3);
        assertEquals(6, actualArea.getTiles().size());
        assertEquals(expectedArea, actualArea);
    }

    /**
     * Soll eine korrekte Area liefern deren Center an der rechten Kante liegt
     */
    @Test
    public void shouldGetCorrectAreaCenterOnRightEdge() {
        VisualUnit visualUnit = getDefaultVisualUnit();
        Map<Point, Tile> expectedTiles = new HashMap<>();

        expectedTiles.put(new Point(10, 0), getMockTile(new Point(10, 0)));
        expectedTiles.put(new Point(10, 1), getMockTile(new Point(10, 1)));
        expectedTiles.put(new Point(10, 2), getMockTile(new Point(10, 2)));
        expectedTiles.put(new Point(11, 0), getMockTile(new Point(11, 0)));
        expectedTiles.put(new Point(11, 1), getMockTile(new Point(11, 1)));
        expectedTiles.put(new Point(11, 2), getMockTile(new Point(11, 2)));
        BoardArea expectedArea = new BoardAreaUnit(new Point(11, 1), expectedTiles);
        BoardArea actualArea = visualUnit.getArea(new Point(11, 1), 3);
        assertEquals(6, actualArea.getTiles().size());
        assertEquals(expectedArea, actualArea);
    }

    /**
     * Soll korrekte Area liefern, wenn center in der rechten unteren Ecke
     * liegt. Area beinhaltet 2 Fields
     */
    @Test
    public void getCutoffAreaTestUpperRightCornerWithTwoFields() {
        VisualUnit visualUnit = new VisualUnit(mem, getOneDockTwoFields(), getDefaultPlayerMap(), 0, mockTimer);

        Map<Point, Tile> expectedTiles = new HashMap<Point, Tile>();

        expectedTiles.put(new Point(10, 15), getMockTile(new Point(10, 15)));
        expectedTiles.put(new Point(11, 15), getMockTile(new Point(11, 15)));
        expectedTiles.put(new Point(12, 15), getMockTile(new Point(12, 15)));

        expectedTiles.put(new Point(10, 14), getMockTile(new Point(10, 14)));
        expectedTiles.put(new Point(11, 14), getMockTile(new Point(11, 14)));
        expectedTiles.put(new Point(12, 14), getMockTile(new Point(12, 14)));

        BoardArea expectedArea = new BoardAreaUnit(new Point(11, 15), expectedTiles);

        BoardArea actualArea = visualUnit.getArea(new Point(11, 15), 3);
        assertEquals(6, actualArea.getTiles().size());
        assertEquals(expectedArea, actualArea);
    }

    /**
     * Soll eine korrekte Area liefern deren Center an der rechten Kante liegt.
     * Area beinhaltet 2 Fields
     */
    @Test
    public void shouldGetCorrectAreaCenterOnRightEdgeOfDockWithTwoFields() {
        VisualUnit visualUnit = new VisualUnit(mem, getOneDockTwoFields(), getDefaultPlayerMap(), 0, mockTimer);
        Map<Point, Tile> expectedTiles = new HashMap<>();

        expectedTiles.put(new Point(10, 0), getMockTile(new Point(10, 0)));
        expectedTiles.put(new Point(10, 1), getMockTile(new Point(10, 1)));
        expectedTiles.put(new Point(10, 2), getMockTile(new Point(10, 2)));
        expectedTiles.put(new Point(11, 0), getMockTile(new Point(11, 0)));
        expectedTiles.put(new Point(11, 1), getMockTile(new Point(11, 1)));
        expectedTiles.put(new Point(11, 2), getMockTile(new Point(11, 2)));

        BoardArea expectedArea = new BoardAreaUnit(new Point(11, 1), expectedTiles);
        BoardArea actualArea = visualUnit.getArea(new Point(11, 1), 3);
        assertEquals(6, actualArea.getTiles().size());
        assertEquals(expectedArea, actualArea);
    }

    /**
     * Soll eine korrekte Area liefern deren Center an der oberen Kante liegt
     */
    @Test
    public void shouldGetCorrectAreaCenterOnUpperEdge() {
        VisualUnit visualUnit = getDefaultVisualUnit();
        Map<Point, Tile> expectedTiles = new HashMap<>();

        expectedTiles.put(new Point(4, 15), getMockTile(new Point(4, 15)));
        expectedTiles.put(new Point(5, 15), getMockTile(new Point(5, 15)));
        expectedTiles.put(new Point(6, 15), getMockTile(new Point(6, 15)));
        expectedTiles.put(new Point(4, 14), getMockTile(new Point(4, 14)));
        expectedTiles.put(new Point(5, 14), getMockTile(new Point(5, 14)));
        expectedTiles.put(new Point(6, 14), getMockTile(new Point(6, 14)));

        BoardArea expectedArea = new BoardAreaUnit(new Point(5, 15), expectedTiles);
        BoardArea actualArea = visualUnit.getArea(new Point(5, 15), 3);
        assertEquals(6, actualArea.getTiles().size());
        assertEquals(expectedArea, actualArea);
    }

    /**
     * Soll korrekte Area liefern
     */
    @Test
    public void getAreaTest() {
        Map<Point, Tile> expectedTiles = getEmptyTiles(3);
        Robot robot = Mockito.mock(Robot.class);
        expectedTiles.get(new Point(0, 1)).setRobot(robot);

        Flag flag = Mockito.mock(Flag.class);
        Mockito.when(flag.getNumber()).thenReturn(42);
        expectedTiles.get(new Point(2, 2)).setFieldObject(flag);
        

        Board board = getDefaultBoard();
        board.getFields().get(new Point(0, 0)).getTiles().get(new Point(0, 1)).setRobot(robot);
        board.getFields().get(new Point(0, 0)).getTiles().get(new Point(2, 2)).setFieldObject(flag);

        BoardArea expectedArea = new BoardAreaUnit(new Point(1, 1), expectedTiles);

        VisualUnit visualUnit = new VisualUnit(mem, board, getDefaultPlayerMap(), 0, mockTimer);
        BoardArea actualArea = visualUnit.getArea(new Point(1, 1), 3);
        assertEquals(3 * 3, actualArea.getTiles().size());
        assertEquals(expectedArea, actualArea);
    }

    
    // === Helfer ===
    private Map<Point, Tile> getEmptyTiles(int length) {
        Map<Point, Tile> result = new HashMap<Point, Tile>();
        for (int x = 0; x < length; x++) {
            for (int y = 0; y < length; y++) {
                Point pt = new Point(x, y);
                result.put(pt, getMockTile(pt));
            }
        }
        return result;
    }

    private VisualUnit getDefaultVisualUnit() {
        return new VisualUnit(mem, getDefaultBoard(), getDefaultPlayerMap(), 0, mockTimer);
    }

    private Map<Integer, Player> getDefaultPlayerMap(int... ids) {
        Map<Integer, Player> idMap = new HashMap<Integer, Player>();
        for (int id : ids) {
            Player player = Mockito.mock(Player.class);
            idMap.put(id, player);
        }
        return idMap;
    }

    private Map<Integer, Robot> getDefaultRobotMap(int... ids) {
        Map<Integer, Robot> idMap = new HashMap<Integer, Robot>();
        for (int id : ids) {
            Robot robot = Mockito.mock(Robot.class);
            idMap.put(id, robot);
        }
        return idMap;
    }

    private Board getOneDockTwoFields() {
        Map<Point, Field> fields = new HashMap<Point, Field>();
        fields.put(new Point(0, 0), getDefaultDock());
        fields.put(new Point(0, 1), getDefaultField());
        fields.put(new Point(1, 1), getDefaultField(new Point(1, 1)));
        Board board = Mockito.mock(Board.class);
        Mockito.when(board.getFields()).thenReturn(fields);
        return board;
    }

    private Board getDefaultBoard() {
        Map<Point, Field> fields = new HashMap<Point, Field>();
        fields.put(new Point(0, 0), getDefaultDock());
        fields.put(new Point(0, 1), getDefaultField());
        Board board = Mockito.mock(Board.class);
        Mockito.when(board.getFields()).thenReturn(fields);
        return board;

    }

    private Dock getDefaultDock() {
        Map<Point, Tile> ptToTiles = new HashMap<Point, Tile>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 12; j++) {
                Tile tile = getMockTile(new Point(j, i));
                ptToTiles.put(new Point(j, i), tile);
            }
        }
        Dock dock = Mockito.mock(Dock.class);
        Mockito.when(dock.getCoordinates()).thenReturn(new Point(0,0));
        Mockito.when(dock.getTiles()).thenReturn(ptToTiles);
        return dock;
    }

    private Field getDefaultField() {
       return getDefaultField(new Point(0, 1));
    }

    private Field getDefaultField(Point coords) {
        Map<Point, Tile> ptToTiles = new HashMap<Point, Tile>();
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                Point absCoords = new Point(j+coords.x*12, i+4+((coords.y-1)*12));
                Tile tile = getMockTile(absCoords);
                ptToTiles.put(new Point(j, i), tile);
            }
        }
        Field f = Mockito.mock(Field.class);
        Mockito.when(f.getCoordinates()).thenReturn(coords);
        Mockito.when(f.getTiles()).thenReturn(ptToTiles);
        return f;
    }

    private Map<Point, Tile> defaulteTileMap() {
        Map<Point, Tile> m = new HashMap<>();
        for (int i = 4; i < 16; i++) {
            for (int j = 0; j < 12; j++) {
                Tile tile = getMockTile(new Point(j, i));
                m.put(new Point(j, i), tile);
            }
        }
        return m;
    }
    
    private Tile getMockTile(Point absCoords) {
        //TODO MockTile mit Mockito
//        Tile tile = Mockito.mock(Tile.class);
//        Mockito.when(tile.getAbsoluteCoordinates()).thenReturn(absCoords);
//        return tile;
        return new MockTile(absCoords);
    }
}