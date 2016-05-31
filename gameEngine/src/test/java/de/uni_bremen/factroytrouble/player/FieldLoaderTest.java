package de.uni_bremen.factroytrouble.player;

import de.uni_bremen.factroytrouble.board.Dock;
import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.gameobjects.*;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * @author Stefan
 */
@RunWith(MockitoJUnitRunner.class)
@Ignore("Original Bretter entfernt. Dadurch kommt es zu Problemen")
public class FieldLoaderTest {
    private static final int GAME_ID = 0;
    private String testFileLoad = "DOCK_LOADER_TEST";
    private FieldLoader fieldLoader = new FieldLoader(GAME_ID);

    private int maxX = 0;
    private int maxY = 0;
    
    private String[] testFieldLines = {"ti_wno_wea_wso_wwe,ti_wnol_weall_wsolll,ti_wwep2,ti_wnop135,ge_wnol_weall_wsolll_wwe,ge_rl_wnol_weall_wsolll_wwe,ti,ti,ti,ti,ti,ti",
            "be_no_wnol_weall_wsolll_wwe,be_ea_wnol_weall_wsolll_wwe,be_so_wnol_weall_wsolll_wwe,be_we_wnol_weall_wsolll_wwe,be_no_ex_wnol_weall_wsolll_wwe,be_ea_ex_wnol_weall_wsolll_wwe,be_so_ex_wnol_weall_wsolll_wwe,be_we_ex_wnol_weall_wsolll_wwe,ti,ti,ti,ti",
            "ws_wnol_weall_wsolll_wwe,ws_ad_wnol_weall_wsolll_wwe,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti",
            "ho_wnol_weall_wsolll_wwe,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti,ti"};
    
    private Map<Point, Tile> dockMock = new HashMap<>();
    
    private Map<Point, Tile> tiles;
    private String testFile = fieldLoader.getClass().getResource("/fields/").toString().replaceFirst("file:", "");
    private BufferedWriter bufWriter;
    private File file;

    @Mock
    private ConveyorBelt comNorth, comEast, comSouth, comWest, expNorth, expEast, expSouth, expWest;
    @Mock
    private Tile tile,tile00,tile10,tile20,tile30,tile40,tile50
    ,tile01,tile11,tile21,tile31,tile41,tile51,tile61,tile71
    ,tile02,tile12,tile03;
    @Mock
    private Wall wno, wea, wso, wwe, wnol, weall,wsolll, wwep2, wnop135;
    @Mock
    private Gear geLeft, geRight;
    @Mock
    private Workshop ws, wsAd;
    @Mock
    private Hole ho;

    private static final Logger LOADER_TEST_LOGGER = Logger.getLogger(FieldLoader.class);

    @Before
    public void setUp() {
        bufWriter = null;
        file = new File(testFile + testFileLoad);
        try {
            bufWriter = new BufferedWriter(new FileWriter(file));
            for (String line : testFieldLines) {
                bufWriter.write(line);
                if (!line.equals(testFieldLines[testFieldLines.length - 1])) {
                    bufWriter.newLine();
                }
            }
        } catch (IOException e) {
            LOADER_TEST_LOGGER.error("Die Testdatei zum Einlesen von Fields konnte nicht erzeugt werden.", e);
        } finally {
            if (bufWriter != null) {
                try {
                    bufWriter.close();
                } catch (IOException e) {
                    LOADER_TEST_LOGGER
                            .error("Der BufferedWriter zum Erzeugen der Testdatei konnte nicht geschlossen werden.", e);
                }
            }
        }
        //Für TestFile erforderlich
        fieldLoader.loadFields();
        tiles = fieldLoader.getField(new Point(1, 1), testFileLoad).getTiles();
        dockMock.put(new Point(0,0), tile00);
        dockMock.put(new Point(1,0), tile10);
        dockMock.put(new Point(2,0), tile20);
        dockMock.put(new Point(3,0), tile30);
        dockMock.put(new Point(4,0), tile40);
        dockMock.put(new Point(5,0), tile50);
        dockMock.put(new Point(0,1), tile01);
        dockMock.put(new Point(1,1), tile11);
        dockMock.put(new Point(2,1), tile21);
        dockMock.put(new Point(3,1), tile31);
        dockMock.put(new Point(4,1), tile41);
        dockMock.put(new Point(5,1), tile51);
        dockMock.put(new Point(6,1), tile61);
        dockMock.put(new Point(7,1), tile71);
        dockMock.put(new Point(0,2), tile02);
        dockMock.put(new Point(1,2), tile12);
        dockMock.put(new Point(0,3), tile03);
        //dockMock auffüllen
        for(int yy = 0; yy < Dock.SHORT_SIDE; yy++){
            for(int xx = 0; xx < Dock.DIMENSION; xx ++){
                if(dockMock.get(new Point(xx, yy)) == null){
                    dockMock.put(new Point(xx, yy), tile);
                }
            }
        }
        when(tile00.getWalls()).thenReturn(new ArrayList<Wall>(Arrays.asList(new Wall[]{wno,wea,wso,wwe})));
        when(tile10.getWalls()).thenReturn(new ArrayList<Wall>(Arrays.asList(new Wall[]{wnol,weall,wsolll})));
        when(tile20.getWalls()).thenReturn(new ArrayList<Wall>(Arrays.asList(new Wall[]{wwep2})));
        when(tile30.getWalls()).thenReturn(new ArrayList<Wall>(Arrays.asList(new Wall[]{wnop135})));
        when(tile40.getWalls()).thenReturn(new ArrayList<Wall>(Arrays.asList(new Wall[]{wnol,weall,wsolll,wwe})));
        when(tile50.getWalls()).thenReturn(new ArrayList<Wall>(Arrays.asList(new Wall[]{wnol,weall,wsolll,wwe})));
        when(tile01.getWalls()).thenReturn(new ArrayList<Wall>(Arrays.asList(new Wall[]{wnol,weall,wsolll,wwe})));
        when(tile11.getWalls()).thenReturn(new ArrayList<Wall>(Arrays.asList(new Wall[]{wnol,weall,wsolll,wwe})));
        when(tile21.getWalls()).thenReturn(new ArrayList<Wall>(Arrays.asList(new Wall[]{wnol,weall,wsolll,wwe})));
        when(tile31.getWalls()).thenReturn(new ArrayList<Wall>(Arrays.asList(new Wall[]{wnol,weall,wsolll,wwe})));
        when(tile41.getWalls()).thenReturn(new ArrayList<Wall>(Arrays.asList(new Wall[]{wnol,weall,wsolll,wwe})));
        when(tile51.getWalls()).thenReturn(new ArrayList<Wall>(Arrays.asList(new Wall[]{wnol,weall,wsolll,wwe})));
        when(tile61.getWalls()).thenReturn(new ArrayList<Wall>(Arrays.asList(new Wall[]{wnol,weall,wsolll,wwe})));
        when(tile71.getWalls()).thenReturn(new ArrayList<Wall>(Arrays.asList(new Wall[]{wnol,weall,wsolll,wwe})));
        when(tile02.getWalls()).thenReturn(new ArrayList<Wall>(Arrays.asList(new Wall[]{wnol,weall,wsolll,wwe})));
        when(tile12.getWalls()).thenReturn(new ArrayList<Wall>(Arrays.asList(new Wall[]{wnol,weall,wsolll,wwe})));
        when(tile03.getWalls()).thenReturn(new ArrayList<Wall>(Arrays.asList(new Wall[]{wnol,weall,wsolll,wwe})));
        when(tile.getWalls()).thenReturn(new ArrayList<Wall>());
        
        when(tile00.getFieldObject()).thenReturn(null);
        when(tile10.getFieldObject()).thenReturn(null);
        when(tile20.getFieldObject()).thenReturn(null);
        when(tile30.getFieldObject()).thenReturn(null);
        when(tile40.getFieldObject()).thenReturn(geRight);
        when(tile50.getFieldObject()).thenReturn(geLeft);
        when(tile01.getFieldObject()).thenReturn(comNorth);
        when(tile11.getFieldObject()).thenReturn(comEast);
        when(tile21.getFieldObject()).thenReturn(comSouth);
        when(tile31.getFieldObject()).thenReturn(comWest);
        when(tile41.getFieldObject()).thenReturn(expNorth);
        when(tile51.getFieldObject()).thenReturn(expEast);
        when(tile61.getFieldObject()).thenReturn(expSouth);
        when(tile71.getFieldObject()).thenReturn(expWest);
        when(tile02.getFieldObject()).thenReturn(ws);
        when(tile12.getFieldObject()).thenReturn(wsAd);
        when(tile03.getFieldObject()).thenReturn(ho);
        when(tile.getFieldObject()).thenReturn(null);
        
        when(wno.getPusherPhases()).thenReturn(new int[]{});
        when(wea.getPusherPhases()).thenReturn(new int[]{});
        when(wso.getPusherPhases()).thenReturn(new int[]{});
        when(wwe.getPusherPhases()).thenReturn(new int[]{});
        when(wnol.getPusherPhases()).thenReturn(new int[]{});
        when(weall.getPusherPhases()).thenReturn(new int[]{});
        when(wsolll.getPusherPhases()).thenReturn(new int[]{});
        when(wwep2.getPusherPhases()).thenReturn(new int[]{2});
        when(wnop135.getPusherPhases()).thenReturn(new int[]{1,3,5});
        
        when(wno.hasLaser()).thenReturn(0);
        when(wea.hasLaser()).thenReturn(0);
        when(wso.hasLaser()).thenReturn(0);
        when(wwe.hasLaser()).thenReturn(0);
        when(wnol.hasLaser()).thenReturn(1);
        when(weall.hasLaser()).thenReturn(2);
        when(wsolll.hasLaser()).thenReturn(3);
        when(wwep2.hasLaser()).thenReturn(0);
        when(wnop135.hasLaser()).thenReturn(0);
        
        when(wno.getOrientation()).thenReturn(Orientation.NORTH);
        when(wea.getOrientation()).thenReturn(Orientation.EAST);
        when(wso.getOrientation()).thenReturn(Orientation.SOUTH);
        when(wwe.getOrientation()).thenReturn(Orientation.WEST);
        when(wnol.getOrientation()).thenReturn(Orientation.NORTH);
        when(weall.getOrientation()).thenReturn(Orientation.EAST);
        when(wsolll.getOrientation()).thenReturn(Orientation.SOUTH);
        when(wwep2.getOrientation()).thenReturn(Orientation.WEST);
        when(wnop135.getOrientation()).thenReturn(Orientation.NORTH);
        
        when(comNorth.isExpress()).thenReturn(false);
        when(comNorth.getOrientation()).thenReturn(Orientation.NORTH);
        when(comEast.isExpress()).thenReturn(false);
        when(comEast.getOrientation()).thenReturn(Orientation.EAST);
        when(comSouth.isExpress()).thenReturn(false);
        when(comSouth.getOrientation()).thenReturn(Orientation.SOUTH);
        when(comWest.isExpress()).thenReturn(false);
        when(comWest.getOrientation()).thenReturn(Orientation.WEST);
        
        when(expNorth.isExpress()).thenReturn(true);
        when(expNorth.getOrientation()).thenReturn(Orientation.NORTH);
        when(expEast.isExpress()).thenReturn(true);
        when(expEast.getOrientation()).thenReturn(Orientation.EAST);
        when(expSouth.isExpress()).thenReturn(true);
        when(expSouth.getOrientation()).thenReturn(Orientation.SOUTH);
        when(expWest.isExpress()).thenReturn(true);
        when(expWest.getOrientation()).thenReturn(Orientation.WEST);
        
        when(geLeft.rotatesLeft()).thenReturn(true);
        when(geRight.rotatesLeft()).thenReturn(false);
        
        when(wsAd.isAdvancedWorkshop()).thenReturn(true);
        when(ws.isAdvancedWorkshop()).thenReturn(false);
    }

    @Test
    public void testFileDimensionTest(){
        for (Point point : tiles.keySet()) {
            if ((int) point.getX() > maxX) {
                maxX = (int) point.getX();
            }
            if ((int) point.getY() > maxY) {
                maxY = (int) point.getY();
            }
        }
        assertEquals(Dock.DIMENSION - 1, maxX);
        assertEquals(Dock.SHORT_SIDE - 1, maxY);
        deleteFile();
    }
    
    @Test
    public void testFileWallTest(){
        List<Wall> dockWalls;
        List<Wall> tilesWalls;
        for(Point pp:dockMock.keySet()){
            dockWalls = dockMock.get(pp).getWalls();
            tilesWalls = tiles.get(pp).getWalls();
            assertEquals(dockWalls.size(), tilesWalls.size());
            for(int ii = 0; ii < dockWalls.size(); ii++){
                assertEquals(dockWalls.get(ii).hasLaser(), tilesWalls.get(ii).hasLaser());
                for(int jj = 0; jj < dockWalls.get(ii).getPusherPhases().length; jj++){
                    assertEquals(dockWalls.get(ii).getPusherPhases()[jj], tilesWalls.get(ii).getPusherPhases()[jj]);
                }
                assertEquals(dockWalls.get(ii).getOrientation(), tilesWalls.get(ii).getOrientation());
            }
        }
        deleteFile();
    }
    
    @Test
    public void testFileConveyorTest(){
        int counter = 0;
        for(Point pp:dockMock.keySet()){
            if(tiles.get(pp).getFieldObject() instanceof ConveyorBelt){
                assertEquals(((ConveyorBelt)dockMock.get(pp).getFieldObject()).isExpress(), 
                        ((ConveyorBelt)tiles.get(pp).getFieldObject()).isExpress());
                assertEquals(((ConveyorBelt)dockMock.get(pp).getFieldObject()).getOrientation(), 
                        ((ConveyorBelt)tiles.get(pp).getFieldObject()).getOrientation());
                counter++;
            }
        }
        assertEquals(8, counter);
        deleteFile();
    }
    
    @Test
    public void testFileGearTest(){
        int counter = 0;
        for(Point pp:dockMock.keySet()){
            if(tiles.get(pp).getFieldObject() instanceof Gear){
                assertEquals(((Gear)dockMock.get(pp).getFieldObject()).rotatesLeft(), ((Gear)tiles.get(pp).getFieldObject()).rotatesLeft());
                counter++;
            }
        }
        assertEquals(2, counter);
        deleteFile();
    }
    
    @Test
    public void testFiledShopTest(){
        int counter = 0;
        for(Point pp:dockMock.keySet()){
            if(tiles.get(pp).getFieldObject() instanceof Workshop){
                assertEquals(((Workshop)dockMock.get(pp).getFieldObject()).isAdvancedWorkshop(),((Workshop)tiles.get(pp).getFieldObject()).isAdvancedWorkshop());
                counter++;
            }
        }
        assertEquals(2, counter);
        deleteFile();
    }
    
    @Test
    public void testFileHoleTest(){
        for(Point pp:dockMock.keySet()){
            if(tiles.get(pp).getFieldObject() instanceof Hole){
                assertEquals(new Point(0,3),pp);
            }
        }
        deleteFile();
    }
    
    @Test
    public void testFileTileTest(){
        int counter = 0;
        for(Point pp:dockMock.keySet()){
            if(tiles.get(pp).getFieldObject() == null){
                counter++;
            }
        }
        assertEquals(35,counter);
        deleteFile();
    }

    @Test
    public void startPositionsTest(){
        assertTrue(Arrays.deepEquals(fieldLoader.getStartPoints("DOCK_A"),
                new Point[] { new Point(5, 1), new Point(6, 1), new Point(3, 1), new Point(8, 1), new Point(1, 1),
                        new Point(10, 1), new Point(0, 1), new Point(11, 1) }));
        assertTrue(Arrays.deepEquals(fieldLoader.getStartPoints("DOCK_B"),
                new Point[] { new Point(5, 0), new Point(6, 0), new Point(3, 1), new Point(8, 1), new Point(1, 2),
                        new Point(10, 2), new Point(0, 3), new Point(11, 3) }));
    }
    
    @Test
    public void dockAWallsTest(){
        Map<Point, Tile> dockTiles = fieldLoader.getField(new Point(0, 0), "DOCK_A").getTiles();
        Map<Point, Orientation[]> walls = new HashMap<>();
        walls.put(new Point(2, 0), new Orientation[] { Orientation.SOUTH });
        walls.put(new Point(4, 0), new Orientation[] { Orientation.SOUTH });
        walls.put(new Point(7, 0), new Orientation[] { Orientation.SOUTH });
        walls.put(new Point(9, 0), new Orientation[] { Orientation.SOUTH });
        walls.put(new Point(1, 1), new Orientation[] { Orientation.WEST });
        walls.put(new Point(3, 1), new Orientation[] { Orientation.WEST });
        walls.put(new Point(5, 1), new Orientation[] { Orientation.WEST });
        walls.put(new Point(6, 1), new Orientation[] { Orientation.WEST });
        walls.put(new Point(7, 1), new Orientation[] { Orientation.WEST });
        walls.put(new Point(9, 1), new Orientation[] { Orientation.WEST });
        walls.put(new Point(11, 1), new Orientation[] { Orientation.WEST });
        walls.put(new Point(2, 3), new Orientation[] { Orientation.NORTH });
        walls.put(new Point(4, 3), new Orientation[] { Orientation.NORTH });
        walls.put(new Point(7, 3), new Orientation[] { Orientation.NORTH });
        walls.put(new Point(9, 3), new Orientation[] { Orientation.NORTH });
        for(Point pp:dockTiles.keySet()){
            if(dockTiles.get(pp).getWalls().size() > 0){
                assertEquals(1, dockTiles.get(pp).getWalls().size());
                assertEquals(walls.get(pp)[0],dockTiles.get(pp).getWalls().get(0).getOrientation());
                walls.remove(pp);
            }else{
                assertEquals(true, dockTiles.get(pp).getWalls().isEmpty());
            }
        }
        assertEquals(true, walls.isEmpty());
    }
    
    @Test
    public void dockAObjectTest(){
        Map<Point, Tile> dockTiles = fieldLoader.getField(new Point(0, 0), "DOCK_A").getTiles();
        int emptyTiles = 0;
        for (Point point : dockTiles.keySet()) {
            if (dockTiles.get(point).getFieldObject() == null) {
                emptyTiles++;
            }
        }
        assertEquals(48, emptyTiles);
    }
    
    @Test
    public void dockBWallsTest(){
        Map<Point, Tile> dockTiles = fieldLoader.getField(new Point(0, 0), "DOCK_B").getTiles();
        Map<Point, Orientation[]> walls = new HashMap<>();
        walls.put(new Point(6, 0), new Orientation[] { Orientation.WEST });
        walls.put(new Point(6, 1), new Orientation[] { Orientation.WEST });
        walls.put(new Point(1, 2), new Orientation[] { Orientation.WEST });
        walls.put(new Point(2, 2), new Orientation[] { Orientation.WEST });
        walls.put(new Point(9, 2), new Orientation[] { Orientation.EAST });
        walls.put(new Point(10, 2), new Orientation[] { Orientation.EAST });
        walls.put(new Point(2, 3), new Orientation[] { Orientation.NORTH });
        walls.put(new Point(4, 3), new Orientation[] { Orientation.WEST, Orientation.NORTH });
        walls.put(new Point(7, 3), new Orientation[] { Orientation.NORTH, Orientation.EAST });
        walls.put(new Point(9, 3), new Orientation[] { Orientation.NORTH });
        for (Point pp : dockTiles.keySet()) {
            Wall[] wall = dockTiles.get(pp).getWalls().toArray(new Wall[dockTiles.get(pp).getWalls().size()]);
            if (wall.length > 0) {
                assertEquals(walls.get(pp).length, wall.length);
                for (int ii = 0; ii < wall.length; ii++) {
                    assertEquals(walls.get(pp)[ii], wall[ii].getOrientation());
                }
                walls.remove(pp);
            } else {
                assertTrue(dockTiles.get(pp).getWalls().isEmpty());
            }
        }
        assertTrue(walls.isEmpty());
    }
    
    @Test
    public void dockBConveyorTest(){
        Map<Point, Tile> dockTiles = fieldLoader.getField(new Point(0, 0), "DOCK_B").getTiles();
        Map<Point, ConveyorBelt> belts = new HashMap<>();
        belts.put(new Point(0, 1), comEast);
        belts.put(new Point(1, 1), comEast);
        belts.put(new Point(2, 1), comSouth);
        belts.put(new Point(2, 0), comEast);
        belts.put(new Point(3, 0), comEast);
        belts.put(new Point(4, 0), comEast);
        belts.put(new Point(7, 0), comWest);
        belts.put(new Point(8, 0), comWest);
        belts.put(new Point(9, 0), comWest);
        belts.put(new Point(9, 1), comSouth);
        belts.put(new Point(10, 1), comWest);
        belts.put(new Point(11, 1), comWest);
        for (Point pp : dockTiles.keySet()) {
            if (dockTiles.get(pp).getFieldObject() instanceof ConveyorBelt) {
                ConveyorBelt belt = (ConveyorBelt) dockTiles.get(pp).getFieldObject();
                assertEquals(belts.get(pp).isExpress(), belt.isExpress());
                assertEquals(belts.get(pp).getOrientation(), belt.getOrientation());
                belts.remove(pp);
            }
        }
        assertTrue(belts.isEmpty());
    }
    
    @Test
    public void dockBObjectTest(){
        Map<Point, Tile> dockTiles = fieldLoader.getField(new Point(0, 0), "DOCK_B").getTiles();
        int emptyTiles = 0;
        for (Point pp : dockTiles.keySet()) {
            if (dockTiles.get(pp).getFieldObject() == null) {
                emptyTiles++;
            }
        }
        assertEquals(36, emptyTiles);
    }

    @Test
    public void getFieldWrongNameTest() {
        assertEquals(null, fieldLoader.getField(new Point(1, 1), "SollFehler"));
    }

    @Test
    public void getDockWrongNameTest() {
        assertEquals(0, fieldLoader.getStartPoints("SollFehler").length);
    }
    
    private void deleteFile(){
        if (file.exists()) {
            file.delete();
        }
    }
}