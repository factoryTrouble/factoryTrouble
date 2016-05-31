package de.uni_bremen.factroytrouble.player;

import de.uni_bremen.factroytrouble.board.Board;
import de.uni_bremen.factroytrouble.board.Dock;
import de.uni_bremen.factroytrouble.board.Field;
import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.gameobjects.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.awt.Point;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

//import de.uni_bremen.factroytrouble.board.GameBoard;

/**
 * @author Stefan
 */
@RunWith(MockitoJUnitRunner.class)
@Ignore("Original Bretter entfernt. Dadurch kommt es zu Problemen")
public class BoardManagerTest {
    private static final int GAME_ID = 0;
    BoardManager manager;
    FieldLoader loader;
    Map<Point, Tile> tilesIsland;
    Map<Point, Tile> tilesDeath;

    private Board board;

    @Mock
    private Hole hole;
    @Mock
    private Workshop advanced;
    @Mock
    private Workshop common;
    @Mock
    private ConveyorBelt expNorth;
    @Mock
    private ConveyorBelt expEast;
    @Mock
    private ConveyorBelt expSouth;
    @Mock
    private ConveyorBelt expWest;
    @Mock
    private ConveyorBelt comNorth;
    @Mock
    private ConveyorBelt comEast;
    @Mock
    private ConveyorBelt comSouth;
    @Mock
    private ConveyorBelt comWest;

    @Before
    public void setup() {
        loader = new FieldLoader(GAME_ID);
        manager = new BoardManager(GAME_ID, loader);
        // Workshops
        when(advanced.isAdvancedWorkshop()).thenReturn(true);
        when(common.isAdvancedWorkshop()).thenReturn(false);
        // ConveyorBelt
        when(expNorth.isExpress()).thenReturn(true);
        when(expNorth.getOrientation()).thenReturn(Orientation.NORTH);
        when(expEast.isExpress()).thenReturn(true);
        when(expEast.getOrientation()).thenReturn(Orientation.EAST);
        when(expSouth.isExpress()).thenReturn(true);
        when(expSouth.getOrientation()).thenReturn(Orientation.SOUTH);
        when(expWest.isExpress()).thenReturn(true);
        when(expWest.getOrientation()).thenReturn(Orientation.WEST);
        when(comNorth.isExpress()).thenReturn(false);
        when(comNorth.getOrientation()).thenReturn(Orientation.NORTH);
        when(comEast.isExpress()).thenReturn(false);
        when(comEast.getOrientation()).thenReturn(Orientation.EAST);
        when(comSouth.isExpress()).thenReturn(false);
        when(comSouth.getOrientation()).thenReturn(Orientation.SOUTH);
        when(comWest.isExpress()).thenReturn(false);
        when(comWest.getOrientation()).thenReturn(Orientation.WEST);
    }

    /*
     * 
     */
    /*
     * @Test public void getBoardTest(){ board =
     * manager.buildBoard("islandHop"); Map<Point,Tile> tiles =
     * board.getFields().get(new Point(0,0)).getTiles();
     * System.out.println(tiles.size());
     * 
     * /*for(int yy = 0; yy < 4; yy++){ for(int xx = 0; xx < 12; xx++){
     * if(tiles.get(new Point(xx,yy)).getWalls().size()>0) System.out.println(
     * "Wall: " + xx + "," + yy); } }
     */
    /*
     * assertTrue(tiles.get(new
     * Point(1,1)).getWalls().get(0).getOrientation().equals(Orientation.WEST));
     * assertTrue(tiles.get(new
     * Point(3,1)).getWalls().get(0).getOrientation().equals(Orientation.WEST));
     * assertTrue(tiles.get(new
     * Point(5,1)).getWalls().get(0).getOrientation().equals(Orientation.WEST));
     * assertTrue(tiles.get(new
     * Point(6,1)).getWalls().get(0).getOrientation().equals(Orientation.WEST));
     * assertTrue(tiles.get(new
     * Point(7,1)).getWalls().get(0).getOrientation().equals(Orientation.WEST));
     * assertTrue(tiles.get(new
     * Point(9,1)).getWalls().get(0).getOrientation().equals(Orientation.WEST));
     * assertTrue(tiles.get(new
     * Point(11,1)).getWalls().get(0).getOrientation().equals(Orientation.WEST))
     * ; //richtiges Field geladen? tiles = board.getFields().get(new
     * Point(0,1)).getTiles(); assertTrue(tiles.get(new
     * Point(1,2)).getFieldObject() instanceof Hole); assertTrue(tiles.get(new
     * Point(1,1)).getFieldObject() instanceof Hole); assertTrue(tiles.get(new
     * Point(2,1)).getFieldObject() instanceof Hole); Workshop shop =
     * (Workshop)tiles.get(new Point(5,5)).getFieldObject(); assertTrue(shop
     * instanceof Workshop && shop.isAdvancedWorkshop()); }
     */

    /*
     * Testen der Drehung anhand des GameBoards "checkmate"
     */
    @Test
    public void turnTest() {
        board = manager.buildBoard("checkmate");
        Map<Point, Tile> tiles = board.getFields().get(new Point(0, 1)).getTiles();
        assertTrue(tiles.size() == Field.DIMENSION * Field.DIMENSION);
        Set<Point> tilePoints = tiles.keySet();
        // Walls
        Map<Point, Orientation> walls = new HashMap<>();
        walls.put(new Point(2, 0), Orientation.SOUTH);
        walls.put(new Point(4, 0), Orientation.SOUTH);
        walls.put(new Point(7, 0), Orientation.SOUTH);
        walls.put(new Point(9, 0), Orientation.SOUTH);
        walls.put(new Point(3, 1), Orientation.NORTH);
        walls.put(new Point(5, 1), Orientation.NORTH);
        walls.put(new Point(6, 1), Orientation.NORTH);
        walls.put(new Point(8, 1), Orientation.NORTH);
        walls.put(new Point(0, 2), Orientation.WEST);
        walls.put(new Point(11, 2), Orientation.EAST);
        walls.put(new Point(1, 3), Orientation.EAST);
        walls.put(new Point(10, 3), Orientation.WEST);
        walls.put(new Point(0, 4), Orientation.WEST);
        walls.put(new Point(11, 4), Orientation.EAST);
        walls.put(new Point(1, 5), Orientation.EAST);
        walls.put(new Point(10, 5), Orientation.WEST);
        walls.put(new Point(1, 6), Orientation.EAST);
        walls.put(new Point(10, 6), Orientation.WEST);
        walls.put(new Point(0, 7), Orientation.WEST);
        walls.put(new Point(11, 7), Orientation.EAST);
        walls.put(new Point(1, 8), Orientation.EAST);
        walls.put(new Point(10, 8), Orientation.WEST);
        walls.put(new Point(0, 9), Orientation.WEST);
        walls.put(new Point(11, 9), Orientation.EAST);
        walls.put(new Point(3, 10), Orientation.SOUTH);
        walls.put(new Point(5, 10), Orientation.SOUTH);
        walls.put(new Point(6, 10), Orientation.SOUTH);
        walls.put(new Point(8, 10), Orientation.SOUTH);
        walls.put(new Point(2, 11), Orientation.NORTH);
        walls.put(new Point(4, 11), Orientation.NORTH);
        walls.put(new Point(7, 11), Orientation.NORTH);
        walls.put(new Point(9, 11), Orientation.NORTH);
        List<Wall> tileWall = new ArrayList<>();
        for (Point point : tilePoints) {
            tileWall = tiles.get(point).getWalls();
            if (tileWall.size() > 0 && tileWall.get(0).getOrientation().equals(walls.get(point))) {
                assertTrue(tileWall.size() == 1);
                walls.remove(point);
            } else {
                assertTrue(tileWall.size() == 0 && !tileWall.contains(point));
            }
        }
        assertTrue(walls.isEmpty());
        Map<Point, FieldObject> obj = new HashMap<>();
        // Holes
        obj.put(new Point(3, 8), hole);
        obj.put(new Point(5, 4), hole);
        obj.put(new Point(6, 7), hole);
        obj.put(new Point(8, 5), hole);
        tilePoints.forEach(point -> {
            if (tiles.get(point).getFieldObject() instanceof Hole) {
                obj.remove(point);
            } else {
                assertTrue(!obj.containsKey(point));
            }
        });
        assertTrue(obj.isEmpty());
        // Workshop
        obj.put(new Point(0, 0), common);
        obj.put(new Point(11, 11), common);
        obj.put(new Point(6, 5), advanced);
        obj.put(new Point(5, 6), advanced);
        tilePoints.forEach(point -> {
            if (tiles.get(point).getFieldObject() instanceof Workshop) {
                assertTrue(((Workshop) tiles.get(point).getFieldObject())
                        .isAdvancedWorkshop() == ((Workshop) obj.get(point)).isAdvancedWorkshop());
                obj.remove(point);
            } else {
                assertTrue(!obj.containsKey(point));
            }
        });
        assertTrue(obj.isEmpty());
        // ConveyorBelt
        obj.put(new Point(1, 1), expNorth);
        obj.put(new Point(1, 2), expNorth);
        obj.put(new Point(1, 3), expNorth);
        obj.put(new Point(1, 4), expNorth);
        obj.put(new Point(1, 5), expNorth);
        obj.put(new Point(1, 6), expNorth);
        obj.put(new Point(1, 7), expNorth);
        obj.put(new Point(1, 8), expNorth);
        obj.put(new Point(1, 9), expNorth);
        obj.put(new Point(1, 10), expEast);
        obj.put(new Point(2, 10), expEast);
        obj.put(new Point(3, 10), expEast);
        obj.put(new Point(4, 10), expEast);
        obj.put(new Point(5, 10), expEast);
        obj.put(new Point(6, 10), expEast);
        obj.put(new Point(7, 10), expEast);
        obj.put(new Point(8, 10), expEast);
        obj.put(new Point(9, 10), expEast);
        obj.put(new Point(10, 10), expSouth);
        obj.put(new Point(10, 9), expSouth);
        obj.put(new Point(10, 8), expSouth);
        obj.put(new Point(10, 7), expSouth);
        obj.put(new Point(10, 6), expSouth);
        obj.put(new Point(10, 5), expSouth);
        obj.put(new Point(10, 4), expSouth);
        obj.put(new Point(10, 3), expSouth);
        obj.put(new Point(10, 2), expSouth);
        obj.put(new Point(10, 10), expSouth);
        obj.put(new Point(10, 1), expWest);
        obj.put(new Point(9, 1), expWest);
        obj.put(new Point(8, 1), expWest);
        obj.put(new Point(7, 1), expWest);
        obj.put(new Point(6, 1), expWest);
        obj.put(new Point(5, 1), expWest);
        obj.put(new Point(4, 1), expWest);
        obj.put(new Point(3, 1), expWest);
        obj.put(new Point(2, 1), expWest);
        obj.put(new Point(2, 3), comEast);
        obj.put(new Point(2, 5), comEast);
        obj.put(new Point(2, 7), comEast);
        obj.put(new Point(2, 9), comEast);
        obj.put(new Point(3, 2), comEast);
        obj.put(new Point(3, 4), comEast);
        obj.put(new Point(3, 6), comEast);
        obj.put(new Point(4, 3), comEast);
        obj.put(new Point(4, 5), comEast);
        obj.put(new Point(4, 7), comEast);
        obj.put(new Point(4, 9), comEast);
        obj.put(new Point(5, 2), comEast);
        obj.put(new Point(5, 8), comEast);
        obj.put(new Point(6, 3), comWest);
        obj.put(new Point(6, 9), comWest);
        obj.put(new Point(7, 2), comWest);
        obj.put(new Point(7, 4), comWest);
        obj.put(new Point(7, 6), comWest);
        obj.put(new Point(7, 8), comWest);
        obj.put(new Point(8, 3), comWest);
        obj.put(new Point(8, 7), comWest);
        obj.put(new Point(8, 9), comWest);
        obj.put(new Point(9, 2), comWest);
        obj.put(new Point(9, 4), comWest);
        obj.put(new Point(9, 6), comWest);
        obj.put(new Point(9, 8), comWest);
        tilePoints.forEach(point -> {
            if (tiles.get(point).getFieldObject() instanceof ConveyorBelt) {
                assertTrue(((ConveyorBelt) tiles.get(point).getFieldObject())
                        .isExpress() == ((ConveyorBelt) obj.get(point)).isExpress());
                assertTrue(((ConveyorBelt) tiles.get(point).getFieldObject()).getOrientation()
                        .equals(((ConveyorBelt) obj.get(point)).getOrientation()));
                obj.remove(point);
            } else {
                assertTrue(!obj.containsKey(point));
            }
        });
        assertTrue(obj.isEmpty());
        int countEmpty = 0;
        for (Point point : tilePoints) {
            if (tiles.get(point).getFieldObject() == null) {
                countEmpty++;
            }
        }
        // +2 wegen bereits gesetzter Flaggen
        assertEquals(countEmpty + 2, 74);
    }

    /*
     * Testen des GameBoards "riskyExchange" samt korrekter Drehungen. Für
     * Erzeugung backflip erforderlich
     */
    @Test
    public void riskyExchangeTest() {
        board = manager.buildBoard("riskyExchange");
        Map<Point, Tile> tiles = board.getFields().get(new Point(0, 1)).getTiles();
        assertTrue(board != null);
        // Anzahl und Art der Fields (Dock, Field)
        assertTrue(board.getFields().size() == 2);
        assertTrue(board.getFields().get(new Point(0, 0)) instanceof Dock);
        assertTrue(board.getFields().get(new Point(0, 1)) instanceof Field);
        // Startpositionen
        assertTrue(manager.getAvailableBoards().get("riskyExchange")
                .equals(Arrays.asList(loader.getStartPoints("DOCK_B"))));
        // Field anhand charakteristischer Punkte identifizieren, korrekte
        // Drehung (backflip)
        assertTrue(tiles.get(new Point(3, 3)).getFieldObject() instanceof Gear);
        assertTrue(((GameGear) tiles.get(new Point(3, 3)).getFieldObject()).rotatesLeft());
        assertTrue(tiles.get(new Point(10, 10)).getFieldObject() instanceof Gear);
        assertTrue(!((GameGear) tiles.get(new Point(10, 10)).getFieldObject()).rotatesLeft());
        // Flaggenpunkte
        assertTrue(((GameFlag) tiles.get(new Point(7, 10)).getFieldObject()).getNumber() == 1);
        assertTrue(((GameFlag) tiles.get(new Point(9, 4)).getFieldObject()).getNumber() == 2);
        assertTrue(((GameFlag) tiles.get(new Point(1, 7)).getFieldObject()).getNumber() == 3);
        // Wall-Orientation entsprechend angepasst (backflip)
        assertTrue(tiles.get(new Point(1, 1)).getWalls().get(0).getOrientation().equals(Orientation.SOUTH));
        // Laser nach Drehung (backflip) korrekt platziert
        Wall wall = tiles.get(new Point(11, 9)).getWalls().get(0);
        assertTrue(wall.hasLaser() == 1 && wall.getOrientation().equals(Orientation.EAST));
        // ConveyorBelt nach Drehung (backflip) korrekt angepasst
        ConveyorBelt belt = (ConveyorBelt) tiles.get(new Point(7, 5)).getFieldObject();
        assertTrue(belt.isExpress() && belt.getOrientation().equals(Orientation.EAST));
        belt = (ConveyorBelt) tiles.get(new Point(7, 6)).getFieldObject();
        assertTrue(!belt.isExpress() && belt.getOrientation().equals(Orientation.WEST));
    }

    /*
     * Testen des GameBoards "checkmate". Für die Erzeugung ist ein backflip und
     * turnRight erforderlich. Wenn erfolgreich, dann arbeiten die Drehungen
     * korrekt, sodass diese im Folgenden nicht mehr getestet werden müssen.
     */
    @Test
    public void checkmateTest() {
        board = manager.buildBoard("checkmate");
        Map<Point, Tile> tiles = board.getFields().get(new Point(0, 1)).getTiles();
        assertTrue(board != null);
        // Anzahl und Art der Fields (Dock, Field)
        assertTrue(board.getFields().size() == 2);
        assertTrue(board.getFields().get(new Point(0, 0)) instanceof Dock);
        assertTrue(board.getFields().get(new Point(0, 1)) instanceof Field);
        // Startpositionen
        assertTrue(
                manager.getAvailableBoards().get("checkmate").equals(Arrays.asList(loader.getStartPoints("DOCK_A"))));
        // Field anhand charakteristischer Punkte identifizieren, korrekte
        // Drehung (turnRight, backflip)
        ConveyorBelt belt = (ConveyorBelt) tiles.get(new Point(2, 9)).getFieldObject();
        assertTrue(belt instanceof ConveyorBelt);// && !belt.isExpress() &&
                                                 // belt.getOrientation().equals(Orientation.EAST)
        belt = (ConveyorBelt) tiles.get(new Point(9, 2)).getFieldObject();
        assertTrue(belt instanceof ConveyorBelt && !belt.isExpress() && belt.getOrientation().equals(Orientation.WEST));
        assertTrue(tiles.get(new Point(9, 9)).getFieldObject() == null);
        assertTrue(tiles.get(new Point(5, 4)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(3, 8)).getFieldObject() instanceof Hole);
        // Wall-Orientation entpsrechend angepasst: backflip & rightturn
        tiles.get(new Point(3, 1)).getWalls().get(0).getOrientation().equals(Orientation.NORTH);
        // Flaggenpunkte
        assertTrue(((GameFlag) tiles.get(new Point(7, 9)).getFieldObject()).getNumber() == 1);
        assertTrue(((GameFlag) tiles.get(new Point(3, 3)).getFieldObject()).getNumber() == 2);
    }

    /*
     * Testen des GameBoards "dizzyDash".
     */
    @Test
    public void dizzyDashTest() {
        board = manager.buildBoard("dizzyDash");
        Map<Point, Tile> tiles = board.getFields().get(new Point(0, 1)).getTiles();
        assertTrue(board != null);
        // Anzahl und Art der Fields (Dock, Field)
        assertTrue(board.getFields().size() == 2);
        assertTrue(board.getFields().get(new Point(0, 0)) instanceof Dock);
        assertTrue(board.getFields().get(new Point(0, 1)) instanceof Field);
        // Startpositionen
        assertTrue(
                manager.getAvailableBoards().get("dizzyDash").equals(Arrays.asList(loader.getStartPoints("DOCK_B"))));
        // Field anhand charakteristischer Punkte identifizieren
        Workshop shop = (Workshop) tiles.get(new Point(2, 8)).getFieldObject();
        assertTrue(shop instanceof Workshop && !shop.isAdvancedWorkshop());
        shop = (Workshop) tiles.get(new Point(8, 8)).getFieldObject();
        assertTrue(shop instanceof Workshop && shop.isAdvancedWorkshop());
        Wall wall = tiles.get(new Point(6, 8)).getWalls().get(0);
        assertTrue(wall.getOrientation().equals(Orientation.EAST) && wall.hasLaser() == 1);
        wall = tiles.get(new Point(5, 8)).getWalls().get(0);
        assertTrue(wall.getOrientation().equals(Orientation.WEST) && wall.hasLaser() == 0);
        // Flaggenpunkte
        assertTrue(((GameFlag) tiles.get(new Point(5, 7)).getFieldObject()).getNumber() == 1);
        assertTrue(((GameFlag) tiles.get(new Point(10, 0)).getFieldObject()).getNumber() == 2);
        assertTrue(((GameFlag) tiles.get(new Point(1, 5)).getFieldObject()).getNumber() == 3);
    }

    /*
     * Testen des GameBoards "islandHop".
     */
    @Test
    public void islandHopTest() {
        board = manager.buildBoard("islandHop");
        Map<Point, Tile> tiles = board.getFields().get(new Point(0, 1)).getTiles();
        assertTrue(board != null);
        // Anzahl und Art der Fields (Dock, Field)
        assertTrue(board.getFields().size() == 2);
        assertTrue(board.getFields().get(new Point(0, 0)) instanceof Dock);
        assertTrue(board.getFields().get(new Point(0, 1)) instanceof Field);
        // Startpositionen
        assertTrue(
                manager.getAvailableBoards().get("islandHop").equals(Arrays.asList(loader.getStartPoints("DOCK_A"))));
        // Field anhand charakteristischer Punkte identifizieren
        assertTrue(tiles.get(new Point(1, 2)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(1, 1)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(2, 1)).getFieldObject() instanceof Hole);
        Workshop shop = (Workshop) tiles.get(new Point(5, 5)).getFieldObject();
        assertTrue(shop instanceof Workshop && shop.isAdvancedWorkshop());
        // Flaggenpunkte
        assertTrue(((GameFlag) tiles.get(new Point(6, 10)).getFieldObject()).getNumber() == 1);
        assertTrue(((GameFlag) tiles.get(new Point(1, 5)).getFieldObject()).getNumber() == 2);
        assertTrue(((GameFlag) tiles.get(new Point(11, 7)).getFieldObject()).getNumber() == 3);
    }

    /*
     * Testen des GameBoards "chopShopChallenge".
     */
    @Test
    public void chopShopChallengeTest() {
        board = manager.buildBoard("chopShopChallenge");
        Map<Point, Tile> tiles = board.getFields().get(new Point(0, 1)).getTiles();
        assertTrue(board != null);
        // Anzahl und Art der Fields (Dock, Field)
        assertTrue(board.getFields().size() == 2);
        assertTrue(board.getFields().get(new Point(0, 0)) instanceof Dock);
        assertTrue(board.getFields().get(new Point(0, 1)) instanceof Field);
        // Startpositionen
        assertTrue(manager.getAvailableBoards().get("chopShopChallenge")
                .equals(Arrays.asList(loader.getStartPoints("DOCK_A"))));
        // Field anhand charakteristischer Punkte identifizieren
        Gear gear = (Gear) tiles.get(new Point(3, 3)).getFieldObject();
        assertTrue(gear instanceof Gear && gear.rotatesLeft());
        Wall wall = tiles.get(new Point(3, 5)).getWalls().get(0);
        assertTrue(wall instanceof Wall && wall.hasLaser() == 2 && wall.getOrientation().equals(Orientation.SOUTH));
        assertTrue(tiles.get(new Point(8, 2)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(10, 10)).getFieldObject() instanceof Hole);
        Workshop shop = (Workshop) tiles.get(new Point(7, 2)).getFieldObject();
        assertTrue(shop instanceof Workshop && shop.isAdvancedWorkshop());
        // Flaggenpunkte
        assertTrue(((GameFlag) tiles.get(new Point(4, 2)).getFieldObject()).getNumber() == 1);
        assertTrue(((GameFlag) tiles.get(new Point(9, 0)).getFieldObject()).getNumber() == 2);
        assertTrue(((GameFlag) tiles.get(new Point(1, 1)).getFieldObject()).getNumber() == 3);
        assertTrue(((GameFlag) tiles.get(new Point(11, 4)).getFieldObject()).getNumber() == 4);
    }

    /*
     * Testen des GameBoards "twister".
     */
    @Test
    public void twisterTest() {
        board = manager.buildBoard("twister");
        Map<Point, Tile> tiles = board.getFields().get(new Point(0, 1)).getTiles();
        assertTrue(board != null);
        // Anzahl und Art der Fields (Dock, Field)
        assertTrue(board.getFields().size() == 2);
        assertTrue(board.getFields().get(new Point(0, 0)) instanceof Dock);
        assertTrue(board.getFields().get(new Point(0, 1)) instanceof Field);
        // Startpositionen
        assertTrue(manager.getAvailableBoards().get("twister").equals(Arrays.asList(loader.getStartPoints("DOCK_B"))));
        // Field anhand charakteristischer Punkte identifizieren
        Gear gear = (Gear) tiles.get(new Point(2, 3)).getFieldObject();
        assertTrue(gear instanceof Gear && !gear.rotatesLeft());
        Wall wall = tiles.get(new Point(5, 3)).getWalls().get(0);
        assertTrue(wall instanceof Wall && wall.hasLaser() == 1 && wall.getOrientation().equals(Orientation.WEST));
        Workshop shop = (Workshop) tiles.get(new Point(8, 8)).getFieldObject();
        assertTrue(shop instanceof Workshop && shop.isAdvancedWorkshop());
        shop = (Workshop) tiles.get(new Point(2, 8)).getFieldObject();
        assertTrue(shop instanceof Workshop && !shop.isAdvancedWorkshop());
        ConveyorBelt belt = (ConveyorBelt) tiles.get(new Point(7, 4)).getFieldObject();
        assertTrue(belt instanceof ConveyorBelt && belt.isExpress() && belt.getOrientation().equals(Orientation.EAST));
        belt = (ConveyorBelt) tiles.get(new Point(1, 4)).getFieldObject();
        assertTrue(belt instanceof ConveyorBelt && belt.isExpress() && belt.getOrientation().equals(Orientation.EAST));
        belt = (ConveyorBelt) tiles.get(new Point(7, 10)).getFieldObject();
        assertTrue(belt instanceof ConveyorBelt && belt.isExpress() && belt.getOrientation().equals(Orientation.EAST));
        belt = (ConveyorBelt) tiles.get(new Point(1, 10)).getFieldObject();
        assertTrue(belt instanceof ConveyorBelt && belt.isExpress() && belt.getOrientation().equals(Orientation.EAST));
        // Flaggenpunkte
        assertTrue(((GameFlag) tiles.get(new Point(2, 2)).getFieldObject()).getNumber() == 1);
        assertTrue(((GameFlag) tiles.get(new Point(3, 9)).getFieldObject()).getNumber() == 2);
        assertTrue(((GameFlag) tiles.get(new Point(9, 9)).getFieldObject()).getNumber() == 3);
        assertTrue(((GameFlag) tiles.get(new Point(8, 2)).getFieldObject()).getNumber() == 4);
    }

    /*
     * Testen des GameBoards "bloodbathChess".
     */
    @Test
    public void bloodbathChess() {
        board = manager.buildBoard("bloodbathChess");
        Map<Point, Tile> tiles = board.getFields().get(new Point(0, 1)).getTiles();
        assertTrue(board != null);
        // Anzahl und Art der Fields (Dock, Field)
        assertTrue(board.getFields().size() == 2);
        assertTrue(board.getFields().get(new Point(0, 0)) instanceof Dock);
        assertTrue(board.getFields().get(new Point(0, 1)) instanceof Field);
        // Startpositionen
        assertTrue(manager.getAvailableBoards().get("bloodbathChess")
                .equals(Arrays.asList(loader.getStartPoints("DOCK_A"))));
        // Field anhand charakteristischer Punkte identifizieren
        Workshop shop = (Workshop) tiles.get(new Point(0, 0)).getFieldObject();
        assertTrue(shop instanceof Workshop && !shop.isAdvancedWorkshop());
        shop = (Workshop) tiles.get(new Point(5, 6)).getFieldObject();
        assertTrue(shop instanceof Workshop && shop.isAdvancedWorkshop());
        ConveyorBelt belt = (ConveyorBelt) tiles.get(new Point(10, 10)).getFieldObject();
        assertTrue(belt instanceof ConveyorBelt && belt.isExpress() && belt.getOrientation().equals(Orientation.SOUTH));
        belt = (ConveyorBelt) tiles.get(new Point(5, 2)).getFieldObject();
        assertTrue(belt instanceof ConveyorBelt && !belt.isExpress() && belt.getOrientation().equals(Orientation.EAST));
        assertTrue(tiles.get(new Point(3, 8)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(5, 4)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(6, 7)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(8, 5)).getFieldObject() instanceof Hole);
        // Flaggenpunkte
        assertTrue(((GameFlag) tiles.get(new Point(6, 6)).getFieldObject()).getNumber() == 1);
        assertTrue(((GameFlag) tiles.get(new Point(2, 2)).getFieldObject()).getNumber() == 2);
        assertTrue(((GameFlag) tiles.get(new Point(8, 4)).getFieldObject()).getNumber() == 3);
        assertTrue(((GameFlag) tiles.get(new Point(3, 7)).getFieldObject()).getNumber() == 4);
    }

    /*
     * Testen des GameBoards "aroundTheWorld".
     */
    @Test
    public void aroundTheWorldTest() {
        board = manager.buildBoard("aroundTheWorld");
        Map<Point, Tile> tiles = board.getFields().get(new Point(0, 1)).getTiles();
        assertTrue(board != null);
        // Anzahl und Art der Fields (Dock, Field)
        assertTrue(board.getFields().size() == 3);
        assertTrue(board.getFields().get(new Point(0, 0)) instanceof Dock);
        assertTrue(board.getFields().get(new Point(0, 1)) instanceof Field);
        assertTrue(board.getFields().get(new Point(0, 2)) instanceof Field);
        // Startpositionen
        assertTrue(manager.getAvailableBoards().get("aroundTheWorld")
                .equals(Arrays.asList(loader.getStartPoints("DOCK_A"))));
        // Erstes Field anhand charakteristischer Punkte identifizieren
        Gear gear = (Gear) tiles.get(new Point(2, 2)).getFieldObject();
        assertTrue(gear instanceof Gear && !gear.rotatesLeft());
        gear = (Gear) tiles.get(new Point(9, 3)).getFieldObject();
        assertTrue(gear instanceof Gear && !gear.rotatesLeft());
        gear = (Gear) tiles.get(new Point(6, 7)).getFieldObject();
        assertTrue(gear instanceof Gear && gear.rotatesLeft());
        Wall wall = tiles.get(new Point(6, 3)).getWalls().get(0);
        assertTrue(wall instanceof Wall && wall.hasLaser() == 1 && wall.getOrientation().equals(Orientation.EAST));
        Workshop shop = (Workshop) tiles.get(new Point(8, 3)).getFieldObject();
        assertTrue(shop instanceof Workshop && shop.isAdvancedWorkshop());
        shop = (Workshop) tiles.get(new Point(3, 2)).getFieldObject();
        assertTrue(shop instanceof Workshop && !shop.isAdvancedWorkshop());
        ConveyorBelt belt = (ConveyorBelt) tiles.get(new Point(7, 4)).getFieldObject();
        assertTrue(belt instanceof ConveyorBelt && belt.isExpress() && belt.getOrientation().equals(Orientation.EAST));
        belt = (ConveyorBelt) tiles.get(new Point(1, 4)).getFieldObject();
        assertTrue(belt instanceof ConveyorBelt && belt.isExpress() && belt.getOrientation().equals(Orientation.EAST));
        belt = (ConveyorBelt) tiles.get(new Point(7, 10)).getFieldObject();
        assertTrue(belt instanceof ConveyorBelt && belt.isExpress() && belt.getOrientation().equals(Orientation.EAST));
        belt = (ConveyorBelt) tiles.get(new Point(1, 10)).getFieldObject();
        assertTrue(belt instanceof ConveyorBelt && belt.isExpress() && belt.getOrientation().equals(Orientation.EAST));
        // Flaggenpunkte
        assertTrue(((GameFlag) tiles.get(new Point(9, 11)).getFieldObject()).getNumber() == 1);
        assertTrue(((GameFlag) tiles.get(new Point(5, 1)).getFieldObject()).getNumber() == 3);
        // Zweites Field anhand charakteristischer Punkte identifizieren
        tiles = board.getFields().get(new Point(0, 2)).getTiles();
        assertTrue(tiles.get(new Point(1, 2)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(1, 1)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(2, 1)).getFieldObject() instanceof Hole);
        shop = (Workshop) tiles.get(new Point(6, 6)).getFieldObject();
        assertTrue(shop instanceof Workshop && shop.isAdvancedWorkshop());
        // Flaggenpunkte
        assertTrue(((GameFlag) tiles.get(new Point(6, 10)).getFieldObject()).getNumber() == 2);
    }

    /*
     * Testen des GameBoards "deathTrap".
     */
    @Test
    public void deathTrapTest() {
        board = manager.buildBoard("deathTrap");
        Map<Point, Tile> tiles = board.getFields().get(new Point(0, 1)).getTiles();
        assertTrue(board != null);
        // Anzahl und Art der Fields (Dock, Field)
        assertTrue(board.getFields().size() == 2);
        assertTrue(board.getFields().get(new Point(0, 0)) instanceof Dock);
        assertTrue(board.getFields().get(new Point(0, 1)) instanceof Field);
        // Startpositionen
        assertTrue(
                manager.getAvailableBoards().get("deathTrap").equals(Arrays.asList(loader.getStartPoints("DOCK_A"))));
        assertTrue(tiles.get(new Point(1, 2)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(1, 1)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(2, 1)).getFieldObject() instanceof Hole);
        Workshop shop = (Workshop) tiles.get(new Point(5, 5)).getFieldObject();
        assertTrue(shop instanceof Workshop && shop.isAdvancedWorkshop());
        // Flaggenpunkte
        assertTrue(((GameFlag) tiles.get(new Point(7, 4)).getFieldObject()).getNumber() == 1);
        assertTrue(((GameFlag) tiles.get(new Point(0, 7)).getFieldObject()).getNumber() == 2);
        assertTrue(((GameFlag) tiles.get(new Point(6, 6)).getFieldObject()).getNumber() == 3);
    }

    /*
     * Testen des GameBoards "pilgrimage".
     */
    @Test
    public void pilgrimageTest() {
        board = manager.buildBoard("pilgrimage");
        Map<Point, Tile> tiles = board.getFields().get(new Point(0, 1)).getTiles();
        assertTrue(board != null);
        // Anzahl und Art der Fields (Dock, Field)
        assertTrue(board.getFields().size() == 3);
        assertTrue(board.getFields().get(new Point(0, 0)) instanceof Dock);
        assertTrue(board.getFields().get(new Point(0, 1)) instanceof Field);
        assertTrue(board.getFields().get(new Point(0, 2)) instanceof Field);
        // Startpositionen
        assertTrue(
                manager.getAvailableBoards().get("pilgrimage").equals(Arrays.asList(loader.getStartPoints("DOCK_A"))));
        // Field 1 anhand charakteristischer Punkte identifizieren
        Gear gear = (Gear) tiles.get(new Point(1, 1)).getFieldObject();
        assertTrue(gear instanceof Gear && !gear.rotatesLeft());
        gear = (Gear) tiles.get(new Point(1, 10)).getFieldObject();
        assertTrue(gear instanceof Gear && !gear.rotatesLeft());
        assertTrue(tiles.get(new Point(11, 10)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(9, 1)).getFieldObject() instanceof Hole);
        Workshop shop = (Workshop) tiles.get(new Point(4, 7)).getFieldObject();
        assertTrue(shop instanceof Workshop && shop.isAdvancedWorkshop());
        // Flaggenpunkte
        assertTrue(((GameFlag) tiles.get(new Point(9, 4)).getFieldObject()).getNumber() == 2);
        assertTrue(((GameFlag) tiles.get(new Point(2, 9)).getFieldObject()).getNumber() == 3);
        // Field 2 anhand charakteristischer Punkte identifizieren
        tiles = board.getFields().get(new Point(0, 2)).getTiles();
        assertTrue(tiles.get(new Point(5, 5)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(5, 6)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(5, 7)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(4, 6)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(6, 6)).getFieldObject() instanceof Hole);
        // Flaggenpunkte
        assertTrue(((GameFlag) tiles.get(new Point(4, 3)).getFieldObject()).getNumber() == 1);
    }

    /*
     * Testen des GameBoards "vaultAssault".
     */
    @Test
    public void vaultAssaultTest() {
        board = manager.buildBoard("vaultAssault");
        Map<Point, Tile> tiles = board.getFields().get(new Point(0, 1)).getTiles();
        assertTrue(board != null);
        // Anzahl und Art der Fields (Dock, Field)
        assertTrue(board.getFields().size() == 2);
        assertTrue(board.getFields().get(new Point(0, 0)) instanceof Dock);
        assertTrue(board.getFields().get(new Point(0, 1)) instanceof Field);
        // Startpositionen
        assertTrue(manager.getAvailableBoards().get("vaultAssault")
                .equals(Arrays.asList(loader.getStartPoints("DOCK_B"))));
        // Field anhand charakteristischer Punkte identifizieren
        assertTrue(tiles.get(new Point(3, 2)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(8, 2)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(3, 9)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(8, 9)).getFieldObject() instanceof Hole);
        assertTrue(!((Gear) tiles.get(new Point(0, 10)).getFieldObject()).rotatesLeft());
        // Flaggenpunkte
        assertTrue(((GameFlag) tiles.get(new Point(6, 8)).getFieldObject()).getNumber() == 1);
        assertTrue(((GameFlag) tiles.get(new Point(4, 0)).getFieldObject()).getNumber() == 2);
        assertTrue(((GameFlag) tiles.get(new Point(8, 6)).getFieldObject()).getNumber() == 3);
    }

    /*
     * Testen des GameBoards "whirlwindTour".
     */
    @Test
    public void whirlwindTourTest() {
        board = manager.buildBoard("whirlwindTour");
        Map<Point, Tile> tiles = board.getFields().get(new Point(0, 1)).getTiles();
        assertTrue(board != null);
        // Anzahl und Art der Fields (Dock, Field)
        assertTrue(board.getFields().size() == 2);
        assertTrue(board.getFields().get(new Point(0, 0)) instanceof Dock);
        assertTrue(board.getFields().get(new Point(0, 1)) instanceof Field);
        // Startpositionen
        assertTrue(manager.getAvailableBoards().get("whirlwindTour")
                .equals(Arrays.asList(loader.getStartPoints("DOCK_A"))));
        // Feld anhand charakteristischer Punkte identifizieren
        assertTrue(tiles.get(new Point(5, 5)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(6, 5)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(5, 6)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(6, 6)).getFieldObject() instanceof Hole);
        ConveyorBelt belt = (ConveyorBelt) tiles.get(new Point(4, 6)).getFieldObject();
        assertTrue(belt instanceof ConveyorBelt && !belt.isExpress() && belt.getOrientation().equals(Orientation.EAST));
        // Flaggenpunkte
        assertTrue(((GameFlag) tiles.get(new Point(8, 11)).getFieldObject()).getNumber() == 1);
        assertTrue(((GameFlag) tiles.get(new Point(3, 0)).getFieldObject()).getNumber() == 2);
        assertTrue(((GameFlag) tiles.get(new Point(11, 5)).getFieldObject()).getNumber() == 3);
    }

    /*
     * Testen des GameBoards "lostBearings".
     */
    @Test
    public void lostBearingsTest() {
        board = manager.buildBoard("lostBearings");
        Map<Point, Tile> tiles = board.getFields().get(new Point(0, 1)).getTiles();
        assertTrue(board != null);
        // Anzahl und Art der Fields (Dock, Field)
        assertTrue(board.getFields().size() == 2);
        assertTrue(board.getFields().get(new Point(0, 0)) instanceof Dock);
        assertTrue(board.getFields().get(new Point(0, 1)) instanceof Field);
        // Startpositionen
        assertTrue(manager.getAvailableBoards().get("lostBearings")
                .equals(Arrays.asList(loader.getStartPoints("DOCK_A"))));
        // Feld anhand charakteristischer Punkte identifizieren
        assertTrue(tiles.get(new Point(5, 5)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(6, 5)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(7, 5)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(6, 4)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(6, 6)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(11, 11)).getFieldObject() instanceof Hole);
        // Flaggenpunkte
        assertTrue(((GameFlag) tiles.get(new Point(1, 9)).getFieldObject()).getNumber() == 1);
        assertTrue(((GameFlag) tiles.get(new Point(10, 2)).getFieldObject()).getNumber() == 2);
        assertTrue(((GameFlag) tiles.get(new Point(2, 3)).getFieldObject()).getNumber() == 3);
    }

    /*
     * Testen des GameBoards "robotStew".
     */
    @Test
    public void robotStewTest() {
        board = manager.buildBoard("robotStew");
        Map<Point, Tile> tiles = board.getFields().get(new Point(0, 1)).getTiles();
        assertTrue(board != null);
        // Anzahl und Art der Fields (Dock, Field)
        assertTrue(board.getFields().size() == 2);
        assertTrue(board.getFields().get(new Point(0, 0)) instanceof Dock);
        assertTrue(board.getFields().get(new Point(0, 1)) instanceof Field);
        // Startpositionen
        assertTrue(
                manager.getAvailableBoards().get("robotStew").equals(Arrays.asList(loader.getStartPoints("DOCK_B"))));
        // Feld anhand charakteristischer Punkte identifizieren
        Wall wall = tiles.get(new Point(10, 1)).getWalls().get(0);
        assertTrue(wall instanceof Wall && wall.hasLaser() == 3 && wall.getOrientation().equals(Orientation.NORTH));
        wall = tiles.get(new Point(8, 6)).getWalls().get(0);
        assertTrue(wall instanceof Wall && wall.hasLaser() == 2 && wall.getOrientation().equals(Orientation.NORTH));
        assertTrue(tiles.get(new Point(3, 9)).getFieldObject() instanceof Hole);
        ConveyorBelt belt = (ConveyorBelt) tiles.get(new Point(2, 9)).getFieldObject();
        assertTrue(belt instanceof ConveyorBelt && !belt.isExpress() && belt.getOrientation().equals(Orientation.EAST));
        // Flaggenpunkte
        assertTrue(((GameFlag) tiles.get(new Point(0, 7)).getFieldObject()).getNumber() == 1);
        assertTrue(((GameFlag) tiles.get(new Point(9, 4)).getFieldObject()).getNumber() == 2);
        assertTrue(((GameFlag) tiles.get(new Point(2, 1)).getFieldObject()).getNumber() == 3);
    }

    /*
     * Testen des GameBoards "oddestSea".
     */
    @Test
    public void oddestSeaTest() {
        board = manager.buildBoard("oddestSea");
        Map<Point, Tile> tiles = board.getFields().get(new Point(0, 1)).getTiles();
        assertTrue(board != null);
        // Anzahl und Art der Fields (Dock, Field)
        assertTrue(board.getFields().size() == 3);
        assertTrue(board.getFields().get(new Point(0, 0)) instanceof Dock);
        assertTrue(board.getFields().get(new Point(0, 1)) instanceof Field);
        assertTrue(board.getFields().get(new Point(0, 2)) instanceof Field);
        // Startpositionen
        assertTrue(
                manager.getAvailableBoards().get("oddestSea").equals(Arrays.asList(loader.getStartPoints("DOCK_A"))));
        // Feld 1 anhand charakteristischer Punke identifizieren
        assertTrue(tiles.get(new Point(5, 5)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(6, 5)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(5, 6)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(6, 6)).getFieldObject() instanceof Hole);
        ConveyorBelt belt = (ConveyorBelt) tiles.get(new Point(4, 6)).getFieldObject();
        assertTrue(belt instanceof ConveyorBelt && belt.isExpress() && belt.getOrientation().equals(Orientation.EAST));
        // Feld 2 anhand charakteristischer Punkte identifizieren
        tiles = board.getFields().get(new Point(0, 2)).getTiles();
        assertTrue(tiles.get(new Point(2, 3)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(9, 3)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(2, 8)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(9, 8)).getFieldObject() instanceof Hole);
        Gear gear = (Gear) tiles.get(new Point(1, 0)).getFieldObject();
        assertTrue(gear instanceof Gear && !gear.rotatesLeft());
        // Flaggenpunkte
        assertTrue(((GameFlag) tiles.get(new Point(8, 5)).getFieldObject()).getNumber() == 1);
        assertTrue(((GameFlag) tiles.get(new Point(1, 7)).getFieldObject()).getNumber() == 2);
        assertTrue(((GameFlag) tiles.get(new Point(5, 3)).getFieldObject()).getNumber() == 3);
        assertTrue(((GameFlag) tiles.get(new Point(9, 9)).getFieldObject()).getNumber() == 4);
    }

    /*
     * Testen von des GameBoards "againstTheGrain".
     */
    @Test
    public void againstTheGrainTest() {
        board = manager.buildBoard("againstTheGrain");
        Map<Point, Tile> tiles = board.getFields().get(new Point(0, 1)).getTiles();
        assertTrue(board != null);
        // Anzahl und Art der Fields (Dock, Field)
        assertTrue(board.getFields().size() == 3);
        assertTrue(board.getFields().get(new Point(0, 0)) instanceof Dock);
        assertTrue(board.getFields().get(new Point(0, 1)) instanceof Field);
        assertTrue(board.getFields().get(new Point(0, 2)) instanceof Field);
        // Startpositionen
        assertTrue(manager.getAvailableBoards().get("againstTheGrain")
                .equals(Arrays.asList(loader.getStartPoints("DOCK_A"))));
        // Feld 1 anhand charakteristischer Punkte identifizieren
        assertTrue(tiles.get(new Point(5, 3)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(7, 5)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(4, 6)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(8, 8)).getFieldObject() instanceof Hole);
        // Flaggenpunkte
        assertTrue(((GameFlag) tiles.get(new Point(5, 6)).getFieldObject()).getNumber() == 3);
        // Feld 2 anhand charakteristischer Punkte identifizieren
        tiles = board.getFields().get(new Point(0, 2)).getTiles();
        Wall wall = tiles.get(new Point(10, 1)).getWalls().get(0);
        assertTrue(wall instanceof Wall && wall.hasLaser() == 3 && wall.getOrientation().equals(Orientation.NORTH));
        wall = tiles.get(new Point(8, 6)).getWalls().get(0);
        assertTrue(wall instanceof Wall && wall.hasLaser() == 2 && wall.getOrientation().equals(Orientation.NORTH));
        assertTrue(tiles.get(new Point(3, 9)).getFieldObject() instanceof Hole);
        ConveyorBelt belt = (ConveyorBelt) tiles.get(new Point(2, 9)).getFieldObject();
        assertTrue(belt instanceof ConveyorBelt && !belt.isExpress() && belt.getOrientation().equals(Orientation.EAST));
        // Flaggenpunkte
        assertTrue(((GameFlag) tiles.get(new Point(10, 2)).getFieldObject()).getNumber() == 1);
        assertTrue(((GameFlag) tiles.get(new Point(3, 8)).getFieldObject()).getNumber() == 2);
    }

    /*
     * Testen des GameBoards "islandKing".
     */
    @Test
    public void islandKingTest() {
        board = manager.buildBoard("islandKing");
        Map<Point, Tile> tiles = board.getFields().get(new Point(0, 1)).getTiles();
        assertTrue(board != null);
        // Anzahl und Art der Fields (Dock, Field)
        assertTrue(board.getFields().size() == 2);
        assertTrue(board.getFields().get(new Point(0, 0)) instanceof Dock);
        assertTrue(board.getFields().get(new Point(0, 1)) instanceof Field);
        // Startpositionen
        assertTrue(
                manager.getAvailableBoards().get("islandKing").equals(Arrays.asList(loader.getStartPoints("DOCK_A"))));
        // Feld anhand charakteristischer Punkte identifizieren
        assertTrue(tiles.get(new Point(1, 1)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(1, 2)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(2, 1)).getFieldObject() instanceof Hole);
        Workshop shop = (Workshop) tiles.get(new Point(6, 6)).getFieldObject();
        assertTrue(shop instanceof Workshop && shop.isAdvancedWorkshop());
        // Flaggenpunkte
        assertTrue(((GameFlag) tiles.get(new Point(5, 7)).getFieldObject()).getNumber() == 1);
        assertTrue(((GameFlag) tiles.get(new Point(7, 4)).getFieldObject()).getNumber() == 2);
        assertTrue(((GameFlag) tiles.get(new Point(5, 5)).getFieldObject()).getNumber() == 3);
    }

    /*
     * Testen des GameBoards "tricksy".
     */
    @Test
    public void tricksyTest() {
        board = manager.buildBoard("tricksy");
        Map<Point, Tile> tiles = board.getFields().get(new Point(0, 1)).getTiles();
        assertTrue(board != null);
        // Anzahl und Art der Fields (Dock, Field)
        assertTrue(board.getFields().size() == 2);
        assertTrue(board.getFields().get(new Point(0, 0)) instanceof Dock);
        assertTrue(board.getFields().get(new Point(0, 1)) instanceof Field);
        // Startpositionen
        assertTrue(manager.getAvailableBoards().get("tricksy").equals(Arrays.asList(loader.getStartPoints("DOCK_B"))));
        // Feld anhand charakteristischer Punkte identifizieren
        assertTrue(tiles.get(new Point(5, 5)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(5, 6)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(5, 7)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(4, 6)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(6, 6)).getFieldObject() instanceof Hole);
        // Flaggenpunkte
        assertTrue(((GameFlag) tiles.get(new Point(9, 10)).getFieldObject()).getNumber() == 1);
        assertTrue(((GameFlag) tiles.get(new Point(0, 10)).getFieldObject()).getNumber() == 2);
        assertTrue(((GameFlag) tiles.get(new Point(8, 0)).getFieldObject()).getNumber() == 3);
        assertTrue(((GameFlag) tiles.get(new Point(3, 4)).getFieldObject()).getNumber() == 4);
    }

    /*
     * Testen des GameBoards "setToKill".
     */
    @Test
    public void setToKillTest() {
        board = manager.buildBoard("setToKill");
        Map<Point, Tile> tiles = board.getFields().get(new Point(0, 1)).getTiles();
        assertTrue(board != null);
        // Anzahl und Art der Fields (Dock, Field)
        assertTrue(board.getFields().size() == 2);
        assertTrue(board.getFields().get(new Point(0, 0)) instanceof Dock);
        assertTrue(board.getFields().get(new Point(0, 1)) instanceof Field);
        // Startpositionen
        assertTrue(
                manager.getAvailableBoards().get("setToKill").equals(Arrays.asList(loader.getStartPoints("DOCK_B"))));
        // Feld anhand charakteristischer Punkte identifizieren
        Gear gear = (Gear) tiles.get(new Point(1, 1)).getFieldObject();
        assertTrue(gear instanceof Gear && !gear.rotatesLeft());
        gear = (Gear) tiles.get(new Point(1, 10)).getFieldObject();
        assertTrue(gear instanceof Gear && !gear.rotatesLeft());
        assertTrue(tiles.get(new Point(11, 10)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(9, 1)).getFieldObject() instanceof Hole);
        Workshop shop = (Workshop) tiles.get(new Point(4, 7)).getFieldObject();
        assertTrue(shop instanceof Workshop && shop.isAdvancedWorkshop());
        // Flaggenpunkte
        assertTrue(((GameFlag) tiles.get(new Point(5, 11)).getFieldObject()).getNumber() == 1);
        assertTrue(((GameFlag) tiles.get(new Point(2, 0)).getFieldObject()).getNumber() == 2);
        assertTrue(((GameFlag) tiles.get(new Point(10, 2)).getFieldObject()).getNumber() == 3);
        assertTrue(((GameFlag) tiles.get(new Point(2, 7)).getFieldObject()).getNumber() == 4);
    }

    /*
     * Testen des GameBoards "factoryRejects".
     */
    @Test
    public void factoryRejectsTest() {
        board = manager.buildBoard("factoryRejects");
        Map<Point, Tile> tiles = board.getFields().get(new Point(0, 1)).getTiles();
        assertTrue(board != null);
        // Anzahl und Art der Fields (Dock, Field)
        assertTrue(board.getFields().size() == 2);
        assertTrue(board.getFields().get(new Point(0, 0)) instanceof Dock);
        assertTrue(board.getFields().get(new Point(0, 1)) instanceof Field);
        // Startpositionen
        assertTrue(manager.getAvailableBoards().get("factoryRejects")
                .equals(Arrays.asList(loader.getStartPoints("DOCK_B"))));
        // Feld anhand charakteristischer Punkte identifizieren
        Wall wall = tiles.get(new Point(1, 10)).getWalls().get(0);
        assertTrue(wall instanceof Wall && wall.hasLaser() == 3 && wall.getOrientation().equals(Orientation.SOUTH));
        wall = tiles.get(new Point(3, 5)).getWalls().get(0);
        assertTrue(wall instanceof Wall && wall.hasLaser() == 2 && wall.getOrientation().equals(Orientation.SOUTH));
        ConveyorBelt belt = (ConveyorBelt) tiles.get(new Point(9, 2)).getFieldObject();
        assertTrue(belt instanceof ConveyorBelt && !belt.isExpress() && belt.getOrientation().equals(Orientation.WEST));
        assertTrue(tiles.get(new Point(8, 2)).getFieldObject() instanceof Hole);
        // Flaggenpunkte
        assertTrue(((GameFlag) tiles.get(new Point(7, 10)).getFieldObject()).getNumber() == 1);
        assertTrue(((GameFlag) tiles.get(new Point(4, 0)).getFieldObject()).getNumber() == 2);
        assertTrue(((GameFlag) tiles.get(new Point(2, 7)).getFieldObject()).getNumber() == 3);
    }

    /*
     * Testen des GameBoards "optionWorld".
     */
    @Test
    public void optionWorldTest() {
        board = manager.buildBoard("optionWorld");
        Map<Point, Tile> tiles = board.getFields().get(new Point(0, 1)).getTiles();
        assertTrue(board != null);
        // Anzahl und Art der Fields (Dock, Field)
        assertTrue(board.getFields().size() == 2);
        assertTrue(board.getFields().get(new Point(0, 0)) instanceof Dock);
        assertTrue(board.getFields().get(new Point(0, 1)) instanceof Field);
        // Startpositionen
        assertTrue(
                manager.getAvailableBoards().get("optionWorld").equals(Arrays.asList(loader.getStartPoints("DOCK_B"))));
        // Feld anhand charakteristischer Punkte identifizieren
        assertTrue(tiles.get(new Point(2, 3)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(9, 3)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(2, 8)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(9, 8)).getFieldObject() instanceof Hole);
        Gear gear = (Gear) tiles.get(new Point(10, 11)).getFieldObject();
        assertTrue(gear instanceof Gear && !gear.rotatesLeft());
        // Flaggenpunkte
        assertTrue(((GameFlag) tiles.get(new Point(3, 6)).getFieldObject()).getNumber() == 1);
        assertTrue(((GameFlag) tiles.get(new Point(9, 10)).getFieldObject()).getNumber() == 2);
        assertTrue(((GameFlag) tiles.get(new Point(5, 3)).getFieldObject()).getNumber() == 3);
        assertTrue(((GameFlag) tiles.get(new Point(2, 11)).getFieldObject()).getNumber() == 4);
    }

    /*
     * Testen des GameBoards "ballLightning".
     */
    @Test
    public void ballLightningTest() {
        board = manager.buildBoard("ballLightning");
        Map<Point, Tile> tiles = board.getFields().get(new Point(0, 1)).getTiles();
        assertTrue(board != null);
        // Anzahl und Art der Fields (Dock, Field)
        assertTrue(board.getFields().size() == 2);
        assertTrue(board.getFields().get(new Point(0, 0)) instanceof Dock);
        assertTrue(board.getFields().get(new Point(0, 1)) instanceof Field);
        // Startpositionen
        assertTrue(manager.getAvailableBoards().get("ballLightning")
                .equals(Arrays.asList(loader.getStartPoints("DOCK_A"))));
        // Feld anhand charakteristischer Punkte identifizieren
        Gear gear = (Gear) tiles.get(new Point(2, 2)).getFieldObject();
        assertTrue(gear instanceof Gear && !gear.rotatesLeft());
        gear = (Gear) tiles.get(new Point(9, 3)).getFieldObject();
        assertTrue(gear instanceof Gear && !gear.rotatesLeft());
        gear = (Gear) tiles.get(new Point(6, 7)).getFieldObject();
        assertTrue(gear instanceof Gear && gear.rotatesLeft());
        Wall wall = tiles.get(new Point(6, 3)).getWalls().get(0);
        assertTrue(wall instanceof Wall && wall.hasLaser() == 1 && wall.getOrientation().equals(Orientation.EAST));
        Workshop shop = (Workshop) tiles.get(new Point(8, 3)).getFieldObject();
        assertTrue(shop instanceof Workshop && shop.isAdvancedWorkshop());
        shop = (Workshop) tiles.get(new Point(3, 2)).getFieldObject();
        assertTrue(shop instanceof Workshop && !shop.isAdvancedWorkshop());
        ConveyorBelt belt = (ConveyorBelt) tiles.get(new Point(7, 4)).getFieldObject();
        assertTrue(belt instanceof ConveyorBelt && belt.isExpress() && belt.getOrientation().equals(Orientation.EAST));
        belt = (ConveyorBelt) tiles.get(new Point(1, 4)).getFieldObject();
        assertTrue(belt instanceof ConveyorBelt && belt.isExpress() && belt.getOrientation().equals(Orientation.EAST));
        belt = (ConveyorBelt) tiles.get(new Point(7, 10)).getFieldObject();
        assertTrue(belt instanceof ConveyorBelt && belt.isExpress() && belt.getOrientation().equals(Orientation.EAST));
        belt = (ConveyorBelt) tiles.get(new Point(1, 10)).getFieldObject();
        assertTrue(belt instanceof ConveyorBelt && belt.isExpress() && belt.getOrientation().equals(Orientation.EAST));
        // Flaggenpunkte
        assertTrue(((GameFlag) tiles.get(new Point(7, 6)).getFieldObject()).getNumber() == 1);
        assertTrue(((GameFlag) tiles.get(new Point(2, 9)).getFieldObject()).getNumber() == 2);
        assertTrue(((GameFlag) tiles.get(new Point(5, 2)).getFieldObject()).getNumber() == 3);
        assertTrue(((GameFlag) tiles.get(new Point(10, 11)).getFieldObject()).getNumber() == 4);
    }

    /*
     * Testen des GameBoards "tightCollar"
     */
    @Test
    public void tightCollarTest() {
        board = manager.buildBoard("tightCollar");
        Map<Point, Tile> tiles = board.getFields().get(new Point(0, 1)).getTiles();
        assertTrue(board != null);
        // Anzahl und Art der Fields (Dock, Field)
        assertTrue(board.getFields().size() == 2);
        assertTrue(board.getFields().get(new Point(0, 0)) instanceof Dock);
        assertTrue(board.getFields().get(new Point(0, 1)) instanceof Field);
        // Startpositionen
        assertTrue(
                manager.getAvailableBoards().get("tightCollar").equals(Arrays.asList(loader.getStartPoints("DOCK_B"))));
        // Feld anhand charakteristischer Punkte identifizieren
        assertTrue(tiles.get(new Point(5, 5)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(6, 5)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(5, 6)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(6, 6)).getFieldObject() instanceof Hole);
        ConveyorBelt belt = (ConveyorBelt) tiles.get(new Point(4, 6)).getFieldObject();
        assertTrue(belt instanceof ConveyorBelt && belt.isExpress() && belt.getOrientation().equals(Orientation.EAST));
        // Flaggenpunkte
        assertTrue(((GameFlag) tiles.get(new Point(8, 11)).getFieldObject()).getNumber() == 1);
        assertTrue(((GameFlag) tiles.get(new Point(0, 6)).getFieldObject()).getNumber() == 2);
    }

    /*
     * Testen des GameBoards "interference".
     */
    @Test
    public void interferenceTest() {
        board = manager.buildBoard("interference");
        Map<Point, Tile> tiles = board.getFields().get(new Point(0, 1)).getTiles();
        assertTrue(board != null);
        // Anzahl und Art der Fields (Dock, Field)
        assertTrue(board.getFields().size() == 2);
        assertTrue(board.getFields().get(new Point(0, 0)) instanceof Dock);
        assertTrue(board.getFields().get(new Point(0, 1)) instanceof Field);
        // Startpositionen
        assertTrue(manager.getAvailableBoards().get("interference")
                .equals(Arrays.asList(loader.getStartPoints("DOCK_B"))));
        // Feld anhand charakteristischer Punkte identifizieren
        assertTrue(tiles.get(new Point(8, 3)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(5, 4)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(3, 6)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(6, 7)).getFieldObject() instanceof Hole);
        // Flaggenpunkte
        assertTrue(((GameFlag) tiles.get(new Point(5, 9)).getFieldObject()).getNumber() == 1);
        assertTrue(((GameFlag) tiles.get(new Point(7, 3)).getFieldObject()).getNumber() == 2);
        assertTrue(((GameFlag) tiles.get(new Point(0, 11)).getFieldObject()).getNumber() == 3);
    }

    /*
     * Testen des GameBoards "flagFry".
     */
    @Test
    public void flagFryTest() {
        board = manager.buildBoard("flagFry");
        Map<Point, Tile> tiles = board.getFields().get(new Point(0, 1)).getTiles();
        assertTrue(board != null);
        // Anzahl und Art der Fields (Dock, Field)
        assertTrue(board.getFields().size() == 2);
        assertTrue(board.getFields().get(new Point(0, 0)) instanceof Dock);
        assertTrue(board.getFields().get(new Point(0, 1)) instanceof Field);
        // Startpositionen
        assertTrue(manager.getAvailableBoards().get("flagFry").equals(Arrays.asList(loader.getStartPoints("DOCK_A"))));
        // Feld anhand charakteristischer Punkte identifizieren
        assertTrue(tiles.get(new Point(5, 5)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(6, 5)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(7, 5)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(6, 6)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(6, 4)).getFieldObject() instanceof Hole);
        // Flaggenpunkte
        assertTrue(((GameFlag) tiles.get(new Point(3, 8)).getFieldObject()).getNumber() == 1);
        assertTrue(((GameFlag) tiles.get(new Point(9, 8)).getFieldObject()).getNumber() == 2);
        assertTrue(((GameFlag) tiles.get(new Point(3, 1)).getFieldObject()).getNumber() == 3);
    }

    /*
     * Testen des GameBoards "tandemCarnage".
     */
    @Test
    public void tandemCarnageTest() {
        board = manager.buildBoard("tandemCarnage");
        Map<Point, Tile> tiles = board.getFields().get(new Point(0, 1)).getTiles();
        assertTrue(board != null);
        // Anzahl und Art der Fields (Dock, Field)
        assertTrue(board.getFields().size() == 3);
        assertTrue(board.getFields().get(new Point(0, 0)) instanceof Dock);
        assertTrue(board.getFields().get(new Point(0, 1)) instanceof Field);
        assertTrue(board.getFields().get(new Point(0, 2)) instanceof Field);
        // Startpositionen
        assertTrue(manager.getAvailableBoards().get("tandemCarnage")
                .equals(Arrays.asList(loader.getStartPoints("DOCK_A"))));
        // Feld 1 anhand charakteristischer Punkte identifizieren
        assertTrue(tiles.get(new Point(0, 1)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(2, 10)).getFieldObject() instanceof Hole);
        ConveyorBelt belt = (ConveyorBelt) tiles.get(new Point(5, 0)).getFieldObject();
        assertTrue(belt instanceof ConveyorBelt && belt.isExpress() && belt.getOrientation().equals(Orientation.SOUTH));
        Workshop shop = (Workshop) tiles.get(new Point(7, 4)).getFieldObject();
        assertTrue(shop instanceof Workshop && shop.isAdvancedWorkshop());
        shop = (Workshop) tiles.get(new Point(0, 11)).getFieldObject();
        assertTrue(shop instanceof Workshop && !shop.isAdvancedWorkshop());
        // Flaggenpunkte
        assertTrue(((GameFlag) tiles.get(new Point(2, 9)).getFieldObject()).getNumber() == 1);
        assertTrue(((GameFlag) tiles.get(new Point(9, 2)).getFieldObject()).getNumber() == 4);
        // Feld 2 anhand charakteristischer Punkte identifizieren
        tiles = board.getFields().get(new Point(0, 2)).getTiles();
        assertTrue(tiles.get(new Point(5, 5)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(6, 5)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(5, 6)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(6, 6)).getFieldObject() instanceof Hole);
        belt = (ConveyorBelt) tiles.get(new Point(4, 6)).getFieldObject();
        assertTrue(belt instanceof ConveyorBelt && belt.isExpress() && belt.getOrientation().equals(Orientation.EAST));
        // Flaggenpunkte
        assertTrue(((GameFlag) tiles.get(new Point(3, 11)).getFieldObject()).getNumber() == 2);
        assertTrue(((GameFlag) tiles.get(new Point(11, 1)).getFieldObject()).getNumber() == 3);
    }

    /*
     * Testen des GameBoards "allForOneOrOneForAll".
     */
    @Test
    public void allForOneOrOneForAllTest() {
        board = manager.buildBoard("allForOneOrOneForAll");
        Map<Point, Tile> tiles = board.getFields().get(new Point(0, 1)).getTiles();
        assertTrue(board != null);
        // Anzahl und Art der Fields (Dock, Field)
        assertTrue(board.getFields().size() == 3);
        assertTrue(board.getFields().get(new Point(0, 0)) instanceof Dock);
        assertTrue(board.getFields().get(new Point(0, 1)) instanceof Field);
        assertTrue(board.getFields().get(new Point(0, 2)) instanceof Field);
        // Startpositionen
        assertTrue(manager.getAvailableBoards().get("allForOneOrOneForAll")
                .equals(Arrays.asList(loader.getStartPoints("DOCK_B"))));
        // Feld 1 anhand charakteristischer Punkte identifizieren
        Gear gear = (Gear) tiles.get(new Point(2, 2)).getFieldObject();
        assertTrue(gear instanceof Gear && !gear.rotatesLeft());
        gear = (Gear) tiles.get(new Point(9, 3)).getFieldObject();
        assertTrue(gear instanceof Gear && !gear.rotatesLeft());
        gear = (Gear) tiles.get(new Point(6, 7)).getFieldObject();
        assertTrue(gear instanceof Gear && gear.rotatesLeft());
        Wall wall = tiles.get(new Point(6, 3)).getWalls().get(0);
        assertTrue(wall instanceof Wall && wall.hasLaser() == 1 && wall.getOrientation().equals(Orientation.EAST));
        Workshop shop = (Workshop) tiles.get(new Point(8, 3)).getFieldObject();
        assertTrue(shop instanceof Workshop && shop.isAdvancedWorkshop());
        shop = (Workshop) tiles.get(new Point(3, 2)).getFieldObject();
        assertTrue(shop instanceof Workshop && !shop.isAdvancedWorkshop());
        ConveyorBelt belt = (ConveyorBelt) tiles.get(new Point(7, 4)).getFieldObject();
        assertTrue(belt instanceof ConveyorBelt && belt.isExpress() && belt.getOrientation().equals(Orientation.EAST));
        belt = (ConveyorBelt) tiles.get(new Point(1, 4)).getFieldObject();
        assertTrue(belt instanceof ConveyorBelt && belt.isExpress() && belt.getOrientation().equals(Orientation.EAST));
        belt = (ConveyorBelt) tiles.get(new Point(7, 10)).getFieldObject();
        assertTrue(belt instanceof ConveyorBelt && belt.isExpress() && belt.getOrientation().equals(Orientation.EAST));
        belt = (ConveyorBelt) tiles.get(new Point(1, 10)).getFieldObject();
        assertTrue(belt instanceof ConveyorBelt && belt.isExpress() && belt.getOrientation().equals(Orientation.EAST));
        // Flaggenpunkte
        assertTrue(((GameFlag) tiles.get(new Point(6, 5)).getFieldObject()).getNumber() == 1);
        assertTrue(((GameFlag) tiles.get(new Point(9, 2)).getFieldObject()).getNumber() == 4);
        // Field 2 anhang charakteristischer Punkte identifizieren
        tiles = board.getFields().get(new Point(0, 2)).getTiles();
        assertTrue(tiles.get(new Point(2, 3)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(9, 3)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(2, 8)).getFieldObject() instanceof Hole);
        assertTrue(tiles.get(new Point(9, 8)).getFieldObject() instanceof Hole);
        gear = (Gear) tiles.get(new Point(10, 11)).getFieldObject();
        assertTrue(gear instanceof Gear && !gear.rotatesLeft());
        // Flaggenpunkte
        assertTrue(((GameFlag) tiles.get(new Point(8, 6)).getFieldObject()).getNumber() == 2);
        assertTrue(((GameFlag) tiles.get(new Point(2, 10)).getFieldObject()).getNumber() == 3);
    }

    /*
     * Test: Startpositionen
     */
    @Test
    public void getStartPositionsTest() {
        assertTrue(manager.getStartPositions("SollFehler").length == 0);
        assertTrue(manager.getStartPositions("checkmate").length == 8);
        Map<String, Map<String, Point>> robotStarts = manager.getRobotStarts();
        String[] avBoards = manager.getAvailableBoards().keySet()
                .toArray(new String[manager.getAvailableBoards().keySet().size()]);
        String[] rsBoards = robotStarts.keySet().toArray(new String[robotStarts.keySet().size()]);
        // Stimmen die jeweils verwendeten Boardbezeichnungen überein?
        assertTrue(Arrays.deepEquals(avBoards, rsBoards));
        // passen die Startpositionen zum Board?
        for (String board : robotStarts.keySet()) {
            Map<String, Point> rs = robotStarts.get(board);
            // Roboternamen stimmen überein: BoardManager.ROBOT_NAMES eq.
            // rs.robotNames
            String[] rNames = rs.keySet().toArray(new String[rs.keySet().size()]);
            for (String robot : rNames) {
                assertTrue(GameMaster.ROBOT_NAMES.contains(robot));
            }
            // Startpositionen stimmen überein
            List<Point> boardStart = Arrays.asList(manager.getStartPositions(board));
            for (Point point : rs.values()) {
                assertTrue(boardStart.contains(point));
            }
        }
        // Teste für zwei verschiedene Boards mit gleichem Dock, ob Roboter und
        // Positionen identisch sind
        // DOCK_A
        Point[] startBoard1 = manager.getStartPositions("islandHop");
        Point[] startBoard2 = manager.getStartPositions("chopShopChallenge");
        assertTrue(Arrays.deepEquals(startBoard1, startBoard2));
        Map<String, Point> rSt1 = manager.getRobotStarts().get("islandHop");
        Map<String, Point> rSt2 = manager.getRobotStarts().get("chopShopChallenge");
        assertTrue(Arrays.deepEquals(rSt1.values().toArray(new Point[rSt1.values().size()]),
                rSt2.values().toArray(new Point[rSt1.values().size()])));
        // DOCK_B
        startBoard1 = manager.getStartPositions("dizzyDash");
        startBoard2 = manager.getStartPositions("riskyExchange");
        assertTrue(Arrays.deepEquals(startBoard1, startBoard2));
        rSt1 = manager.getRobotStarts().get("dizzyDash");
        rSt2 = manager.getRobotStarts().get("riskyExchange");
        assertTrue(Arrays.deepEquals(rSt1.values().toArray(new Point[rSt1.values().size()]),
                rSt2.values().toArray(new Point[rSt1.values().size()])));
    }
}