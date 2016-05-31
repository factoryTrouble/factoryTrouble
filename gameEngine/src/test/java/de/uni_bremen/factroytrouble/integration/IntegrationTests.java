package de.uni_bremen.factroytrouble.integration;

import de.uni_bremen.factroytrouble.board.Board;
import de.uni_bremen.factroytrouble.board.Dock;
import de.uni_bremen.factroytrouble.board.Field;
import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.gameobjects.FieldObject;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.gameobjects.Wall;
import de.uni_bremen.factroytrouble.player.*;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author Thorben
 */
@Ignore("Original Bretter entfernt. Dadurch kommt es zu Problemen")
public class IntegrationTests {
    private static final int GAME_ID = 0;

    private static final Logger INTEGRATION_LOGGER = Logger.getLogger(IntegrationTests.class);

    
    private Master master;
    private Board board;
    private Dock dock;
    private List<Field> fields = new ArrayList<>();
    private List<Player> players = new ArrayList<>();
    private List<Robot> robots = new ArrayList<>();

    private MasterFactory factory = new GameMasterFactory();

    @Before
    public void init() {
        master = factory.getMaster(GAME_ID);

        createPlayers();
        fillInRobots();

        master.init();
        players.forEach(player -> master.addPlayer(player));

        master.initialiseBoard("checkmate");

        board = master.getBoard();

        fields.addAll(master.getBoard().getFields().values());
        for (Field field : fields) {
            if (field instanceof Dock) {
                dock = (Dock) field;
                break;
            }
        }
        INTEGRATION_LOGGER.info("\n\n     Integrations-Test startet jetzt!");
    }
    
    @After
    public void tearDown(){
        factory.deleteMaster(GAME_ID);
    }

    private void createPlayers() {
        String[] robotNames = {"Bob Meister", "LocalHorst", "Saturn Vulkan Vulcan", "AndréOhlroBot", "CaptainFalko",
                "MAH_BOI", "PokéBot", "Stalin"};
        for (int i = 0; i < 8; i++) {
            PlayerFactory fac = new GamePlayerFactory();
            Player player = fac.createNewPlayer(GAME_ID, robotNames[i], "NA");
            players.add(player);
        }
    }

    private void fillInRobots() {
        for (int i = 0; i < players.size(); i++) {
            robots.add(players.get(i).getRobot());
        }
    }

    @Test
    public void getterTest() {
        Board board = master.getBoard();
        List<Field> fields = new ArrayList<>();
        fields.addAll(board.getFields().values());
        List<Player> players = master.getPlayers();

        assertEquals(this.players, players);
        assertEquals(this.fields, fields);
    }

    @Test
    public void cloneBoardTest() {
        Board board1 = board;
        Board board2 = factory.getBoardClone(GAME_ID);

        assertFalse(board1 == board2);

        Map<Point, Field> fields1 = board1.getFields();
        Map<Point, Field> fields2 = board2.getFields();

        assertEquals(fields1.size(), fields2.size());
        fields1.keySet().forEach(point -> {
            Map<Point, Tile> tiles1 = fields1.get(point).getTiles();
            Map<Point, Tile> tiles2 = fields2.get(point).getTiles();

            assertEquals(tiles1.size(), tiles2.size());
            tiles1.keySet().forEach(point2 -> {
                Tile tile1 = tiles1.get(point2);
                Tile tile2 = tiles2.get(point2);

                FieldObject object1 = tile1.getFieldObject();
                FieldObject object2 = tile2.getFieldObject();

                assertTrue(object1 == null ? object2 == null : object2 != null);
                if (object1 != null) {
                    assertEquals(object1.getClass(), object2.getClass());
                }

                List<Wall> walls1 = tile1.getWalls();
                List<Wall> walls2 = tile2.getWalls();

                assertEquals(walls1.size(), walls2.size());
                for (int i = 0; i < walls1.size(); i++) {
                    Wall wall1 = walls1.get(i);
                    Wall wall2 = walls2.get(i);

                    assertTrue(wall1.getOrientation() == wall2.getOrientation());

                    int[] phases1 = wall1.getPusherPhases();
                    int[] phases2 = wall2.getPusherPhases();

                    assertEquals(phases1.length, phases2.length);
                    for (int ii = 0; ii < walls1.size(); ii++) {
                        assertEquals(phases1[ii], phases2[ii]);
                    }
                }

                Robot robot1 = tile1.getRobot();
                Robot robot2 = tile2.getRobot();

                assertTrue(robot1 == null ? robot2 == null : robot2 != null);
                if (robot1 != null) {
                    assertEquals(robot1.getName(), robot2.getName());
                    assertEquals(robot1.getFlagCounterStatus(), robot2.getFlagCounterStatus());
                    assertEquals(robot1.getHP(), robot2.getHP());
                    assertEquals(robot1.getLives(), robot2.getLives());
                    assertEquals(robot1.getOrientation(), robot2.getOrientation());

                    List<OptionCard> options1 = robot1.getOptions();
                    List<OptionCard> options2 = robot2.getOptions();

                    assertEquals(options1.size(), options2.size());
                    for (int iii = 0; iii < options1.size(); iii++) {
                        assertEquals(options1.get(iii).getClass(), options2.get(iii).getClass());
                    }
                }
            });
        });
    }

    @Test
    public void activateBoardTest1() {

        setStartPositions();
        fillAllRegisters1();
        for(Player pp:players){
            pp.finishTurn();
        }
        master.activateBoard();

        // Positionen vor Cleanpup
        assertEquals(fields.get(0).getTiles().get(new Point(5, 1)), robots.get(0).getCurrentTile());
        assertEquals(fields.get(1).getTiles().get(new Point(1, 3)), robots.get(1).getCurrentTile());
        assertEquals(null, robots.get(2).getCurrentTile());
        assertEquals(fields.get(1).getTiles().get(new Point(6, 2)), robots.get(3).getCurrentTile());
        assertEquals(fields.get(1).getTiles().get(new Point(0, 0)), robots.get(4).getCurrentTile());
        assertEquals(fields.get(0).getTiles().get(new Point(10, 1)), robots.get(5).getCurrentTile());
        assertEquals(null, robots.get(6).getCurrentTile());
        assertEquals(fields.get(0).getTiles().get(new Point(11, 1)), robots.get(7).getCurrentTile());

        master.cleanup();

        // Positionen nach Cleanup
        assertEquals(fields.get(0).getTiles().get(new Point(5, 1)), robots.get(0).getCurrentTile());
        assertEquals(fields.get(1).getTiles().get(new Point(1, 3)), robots.get(1).getCurrentTile());
        assertEquals(fields.get(0).getTiles().get(new Point(3, 1)), robots.get(2).getCurrentTile());
        assertEquals(fields.get(1).getTiles().get(new Point(6, 2)), robots.get(3).getCurrentTile());
        assertEquals(fields.get(1).getTiles().get(new Point(0, 0)), robots.get(4).getCurrentTile());
        assertEquals(fields.get(0).getTiles().get(new Point(10, 1)), robots.get(5).getCurrentTile());
        assertEquals(fields.get(0).getTiles().get(new Point(0, 3)), robots.get(6).getCurrentTile());
        assertEquals(fields.get(0).getTiles().get(new Point(11, 1)), robots.get(7).getCurrentTile());

        // Blickrichtung
        assertEquals(Orientation.SOUTH, robots.get(0).getOrientation());
        assertEquals(Orientation.SOUTH, robots.get(1).getOrientation());
        assertEquals(Orientation.SOUTH, robots.get(2).getOrientation());
        assertEquals(Orientation.NORTH, robots.get(3).getOrientation());
        assertEquals(Orientation.SOUTH, robots.get(4).getOrientation());
        assertEquals(Orientation.NORTH, robots.get(5).getOrientation());
        assertEquals(Orientation.WEST, robots.get(6).getOrientation());
        assertEquals(Orientation.NORTH, robots.get(7).getOrientation());

        // Schaden und Leben
        assertEquals(10, robots.get(0).getHP());
        assertEquals(10, robots.get(1).getHP());
        assertEquals(8, robots.get(2).getHP());
        assertEquals(9, robots.get(3).getHP());
        assertEquals(9, robots.get(4).getHP());
        assertEquals(10, robots.get(5).getHP());
        assertEquals(8, robots.get(6).getHP());
        assertEquals(10, robots.get(7).getHP());
        assertEquals(3, robots.get(0).getLives());
        assertEquals(3, robots.get(1).getLives());
        assertEquals(2, robots.get(2).getLives());
        assertEquals(3, robots.get(3).getLives());
        assertEquals(3, robots.get(4).getLives());
        assertEquals(3, robots.get(5).getLives());
        assertEquals(2, robots.get(6).getLives());
        assertEquals(3, robots.get(7).getLives());

        // Gewinner steht noch nicht fest
        assertEquals(null, master.getWinner());
    }

    @Test
    public void activateBoardTest2() {

        setStartPositions();
        fillAllRegisters2();
        for(Player pp:players){
            pp.finishTurn();
        }
        master.activateBoard();

        // Positionen vor Cleanup
        assertEquals(fields.get(1).getTiles().get(new Point(2, 1)), robots.get(0).getCurrentTile());
        assertEquals(null, robots.get(1).getCurrentTile());
        assertEquals(fields.get(1).getTiles().get(new Point(1, 1)), robots.get(2).getCurrentTile());
        assertEquals(fields.get(1).getTiles().get(new Point(7, 2)), robots.get(3).getCurrentTile());
        assertEquals(fields.get(1).getTiles().get(new Point(1, 2)), robots.get(4).getCurrentTile());
        assertEquals(fields.get(0).getTiles().get(new Point(10, 1)), robots.get(5).getCurrentTile());
        assertEquals(fields.get(1).getTiles().get(new Point(5, 2)), robots.get(6).getCurrentTile());
        assertEquals(fields.get(0).getTiles().get(new Point(11, 1)), robots.get(7).getCurrentTile());

        master.cleanup();

        // Positionen nach Cleanup
        assertEquals(fields.get(1).getTiles().get(new Point(2, 1)), robots.get(0).getCurrentTile());
        assertEquals(fields.get(0).getTiles().get(new Point(6, 1)), robots.get(1).getCurrentTile());
        assertEquals(fields.get(1).getTiles().get(new Point(1, 1)), robots.get(2).getCurrentTile());
        assertEquals(fields.get(1).getTiles().get(new Point(7, 2)), robots.get(3).getCurrentTile());
        assertEquals(fields.get(1).getTiles().get(new Point(1, 2)), robots.get(4).getCurrentTile());
        assertEquals(fields.get(0).getTiles().get(new Point(10, 1)), robots.get(5).getCurrentTile());
        assertEquals(fields.get(1).getTiles().get(new Point(5, 2)), robots.get(6).getCurrentTile());
        assertEquals(fields.get(0).getTiles().get(new Point(11, 1)), robots.get(7).getCurrentTile());

        // Blickrichtung
        assertEquals(Orientation.NORTH, robots.get(0).getOrientation());
        assertEquals(Orientation.NORTH, robots.get(1).getOrientation());
        assertEquals(Orientation.WEST, robots.get(2).getOrientation());
        assertEquals(Orientation.NORTH, robots.get(3).getOrientation());
        assertEquals(Orientation.EAST, robots.get(4).getOrientation());
        assertEquals(Orientation.NORTH, robots.get(5).getOrientation());
        assertEquals(Orientation.EAST, robots.get(6).getOrientation());
        assertEquals(Orientation.NORTH, robots.get(7).getOrientation());

        // Schaden und Leben
        assertEquals(8, robots.get(0).getHP());
        assertEquals(8, robots.get(1).getHP());
        assertEquals(6, robots.get(2).getHP());
        assertEquals(9, robots.get(3).getHP());
        assertEquals(9, robots.get(4).getHP());
        assertEquals(10, robots.get(5).getHP());
        assertEquals(8, robots.get(6).getHP());
        assertEquals(10, robots.get(7).getHP());
        assertEquals(3, robots.get(0).getLives());
        assertEquals(2, robots.get(1).getLives());
        assertEquals(3, robots.get(2).getLives());
        assertEquals(3, robots.get(3).getLives());
        assertEquals(3, robots.get(4).getLives());
        assertEquals(3, robots.get(5).getLives());
        assertEquals(3, robots.get(6).getLives());
        assertEquals(3, robots.get(7).getLives());

        // Gewinner steht noch nicht fest
        assertEquals(null, master.getWinner());
    }

    @Test
    public void activateBoardTest3() {

        setStartPositions();
        fillAllRegisters3();
        for(Player pp:players){
            pp.finishTurn();
        }
        master.activateBoard();

        // Positionen vor Cleanup
        assertEquals(dock.getTiles().get(new Point(2, 1)), robots.get(0).getCurrentTile());
        assertEquals(dock.getTiles().get(new Point(2, 2)), robots.get(1).getCurrentTile());
        assertEquals(dock.getTiles().get(new Point(3, 3)), robots.get(2).getCurrentTile());
        assertEquals(dock.getTiles().get(new Point(11, 2)), robots.get(3).getCurrentTile());
        assertEquals(null, robots.get(4).getCurrentTile());
        assertEquals(dock.getTiles().get(new Point(10, 1)), robots.get(5).getCurrentTile());
        assertEquals(dock.getTiles().get(new Point(1, 3)), robots.get(6).getCurrentTile());
        assertEquals(dock.getTiles().get(new Point(11, 1)), robots.get(7).getCurrentTile());

        master.cleanup();

        // Positionen nach Cleanup
        assertEquals(dock.getTiles().get(new Point(2, 1)), robots.get(0).getCurrentTile());
        assertEquals(dock.getTiles().get(new Point(2, 2)), robots.get(1).getCurrentTile());
        assertEquals(dock.getTiles().get(new Point(3, 3)), robots.get(2).getCurrentTile());
        assertEquals(dock.getTiles().get(new Point(11, 2)), robots.get(3).getCurrentTile());
        assertEquals(dock.getTiles().get(new Point(1, 1)), robots.get(4).getCurrentTile());
        assertEquals(dock.getTiles().get(new Point(10, 1)), robots.get(5).getCurrentTile());
        assertEquals(dock.getTiles().get(new Point(1, 3)), robots.get(6).getCurrentTile());
        assertEquals(dock.getTiles().get(new Point(11, 1)), robots.get(7).getCurrentTile());

        // Blickrichtung
        assertEquals(Orientation.NORTH, robots.get(0).getOrientation());
        assertEquals(Orientation.WEST, robots.get(1).getOrientation());
        assertEquals(Orientation.WEST, robots.get(2).getOrientation());
        assertEquals(Orientation.SOUTH, robots.get(3).getOrientation());
        assertEquals(Orientation.EAST, robots.get(4).getOrientation());
        assertEquals(Orientation.NORTH, robots.get(5).getOrientation());
        assertEquals(Orientation.EAST, robots.get(6).getOrientation());
        assertEquals(Orientation.NORTH, robots.get(7).getOrientation());

        // Schaden und Leben
        assertEquals(8, robots.get(0).getHP());
        assertEquals(9, robots.get(1).getHP());
        assertEquals(6, robots.get(2).getHP());
        assertEquals(10, robots.get(3).getHP());
        assertEquals(8, robots.get(4).getHP());
        assertEquals(10, robots.get(5).getHP());
        assertEquals(10, robots.get(6).getHP());
        assertEquals(9, robots.get(7).getHP());
        assertEquals(3, robots.get(0).getLives());
        assertEquals(3, robots.get(1).getLives());
        assertEquals(3, robots.get(2).getLives());
        assertEquals(3, robots.get(3).getLives());
        assertEquals(2, robots.get(4).getLives());
        assertEquals(3, robots.get(5).getLives());
        assertEquals(3, robots.get(6).getLives());
        assertEquals(3, robots.get(7).getLives());

        // Gewinner steht noch nicht fest
        assertEquals(null, master.getWinner());
    }

    @Test
    public void moveCorrectlyAfterRespawn() {
        setStartPositions();
        fillAllRegisters4();
        for(Player pp:players){
            pp.finishTurn();
        }
        master.activateBoard();
        master.cleanup();

        fillAllRegisters4();
        for(Player pp:players){
            pp.finishTurn();
        }
        master.activateBoard();
        master.cleanup();

        // Zielfeld
        Tile tile1 = robots.get(0).getCurrentTile();
        Tile tile2 = robots.get(1).getCurrentTile();
        assertEquals(fields.get(1).getTiles().get(new Point(6, 2)), tile1);
        assertEquals(new Point(6, 6), tile1.getAbsoluteCoordinates());
        assertEquals(dock.getTiles().get(new Point(6, 2)), tile2);
        assertEquals(new Point(6, 2), tile2.getAbsoluteCoordinates());

        // Blickrichtung
        assertEquals(Orientation.NORTH, robots.get(0).getOrientation());
        assertEquals(Orientation.SOUTH, robots.get(1).getOrientation());

        // Schaden und Leben
        assertEquals(10, robots.get(0).getHP());
        assertEquals(8, robots.get(1).getHP());
        assertEquals(3, robots.get(0).getLives());
        assertEquals(2, robots.get(1).getLives());
    }

    @Test
    public void dieThroughLaserTest() {
        setStartPositions();

        // erstes Leben verlieren; alle anderen Roboter sterben komplett
        for(Player pp:players){
            pp.finishTurn();
        }
        fillAllRegisters5();
        master.activateBoard();
        master.cleanup();

        for(Player pp:players){
            pp.finishTurn();
        }
        fillAllRegisters6();
        master.activateBoard();
        master.cleanup();

        for(Player pp:players){
            pp.finishTurn();
        }
        fillAllRegisters6();
        master.activateBoard();
        master.cleanup();

        assertEquals(0, players.get(0).getRobot().getLives());
        assertEquals(0, players.get(1).getRobot().getLives());
        assertEquals(3, players.get(2).getRobot().getLives());
        assertEquals(0, players.get(3).getRobot().getLives());
        assertEquals(0, players.get(4).getRobot().getLives());
        assertEquals(2, players.get(5).getRobot().getLives());
        assertEquals(8, players.get(5).getRobot().getHP());
        assertEquals(0, players.get(6).getRobot().getLives());
        assertEquals(0, players.get(7).getRobot().getLives());

        // resetten und zweites Leben verlieren
        Robot resetMe = players.get(2).getRobot();
        Robot destinedToDie = players.get(5).getRobot();
        board.respawnRobot(resetMe);
        resetMe.turn(true);
        destinedToDie.turn(true);

        fillAllRegisters5();
        master.activateBoard();
        master.cleanup();

        fillAllRegisters6();
        master.activateBoard();
        master.cleanup();

        assertEquals(3, players.get(2).getRobot().getLives());
        assertEquals(1, players.get(5).getRobot().getLives());
        assertEquals(8, players.get(5).getRobot().getHP());

        // resetten und 3. Leben verlieren
        resetMe = players.get(2).getRobot();
        destinedToDie = players.get(5).getRobot();
        board.respawnRobot(resetMe);

        resetMe.turn(true);
        destinedToDie.turn(true);
        destinedToDie.turn(true);

        fillAllRegisters5();
        master.activateBoard();
        master.cleanup();

        fillAllRegisters6();
        master.activateBoard();
        master.cleanup();

        assertEquals(3, players.get(2).getRobot().getLives());
        assertEquals(0, players.get(5).getRobot().getLives());
        assertEquals(0, players.get(5).getRobot().getHP());

    }

    private void setStartPositions() {
        Tile tile = dock.getTiles().get(new Point(5, 1));
        robots.get(0).getCurrentTile().setRobot(null);
        robots.get(0).setCurrentTile(tile);
        robots.get(0).setRespawnPoint();
        tile.setRobot(robots.get(0));

        tile = dock.getTiles().get(new Point(6, 1));
        robots.get(1).getCurrentTile().setRobot(null);
        robots.get(1).setCurrentTile(tile);
        robots.get(1).setRespawnPoint();
        tile.setRobot(robots.get(1));

        tile = dock.getTiles().get(new Point(3, 1));
        robots.get(2).getCurrentTile().setRobot(null);
        robots.get(2).setCurrentTile(tile);
        robots.get(2).setRespawnPoint();
        tile.setRobot(robots.get(2));

        tile = dock.getTiles().get(new Point(8, 1));
        robots.get(3).getCurrentTile().setRobot(null);
        robots.get(3).setCurrentTile(tile);
        robots.get(3).setRespawnPoint();
        tile.setRobot(robots.get(3));

        tile = dock.getTiles().get(new Point(1, 1));
        robots.get(4).getCurrentTile().setRobot(null);
        robots.get(4).setCurrentTile(tile);
        robots.get(4).setRespawnPoint();
        tile.setRobot(robots.get(4));

        tile = dock.getTiles().get(new Point(10, 1));
        robots.get(5).getCurrentTile().setRobot(null);
        robots.get(5).setCurrentTile(tile);
        robots.get(5).setRespawnPoint();
        tile.setRobot(robots.get(5));

        tile = dock.getTiles().get(new Point(0, 1));
        robots.get(6).getCurrentTile().setRobot(null);
        robots.get(6).setCurrentTile(tile);
        robots.get(6).setRespawnPoint();
        tile.setRobot(robots.get(6));

        tile = dock.getTiles().get(new Point(11, 1));
        robots.get(7).getCurrentTile().setRobot(null);
        robots.get(7).setCurrentTile(tile);
        robots.get(7).setRespawnPoint();
        tile.setRobot(robots.get(7));
    }

    private void fillAllRegisters1() {
        ProgramCard[] cards0 = {new GameTurnLeftCard(9), new GameTurnLeftCard(10), new GameTurnRightCard(5),
                new GameUturnCard(1), new GameTurnRightCard(6)};
        fillRegisters(robots.get(0), cards0);
        // schaut nach Süden
        ProgramCard[] cards1 = {new GameMoveForwardCard(GAME_ID, 26, 3), new GameMoveForwardCard(GAME_ID, 22, 1), new GameUturnCard(2),
                new GameTurnLeftCard(11), new GameMoveForwardCard(GAME_ID, 18, 1)};
        fillRegisters(robots.get(1), cards1);
        // auf Laufband
        ProgramCard[] cards2 = {new GameUturnCard(3), new GameMoveForwardCard(GAME_ID, 27, 3), new GameTurnLeftCard(12),
                new GameMoveBackwardCard(GAME_ID, 16), new GameMoveBackwardCard(GAME_ID, 17)};
        fillRegisters(robots.get(2), cards2);
        // vom Brett gefallen; respawnt auf Startfeld
        ProgramCard[] cards3 = {new GameMoveForwardCard(GAME_ID, 28, 3), new GameTurnLeftCard(13),
                new GameMoveForwardCard(GAME_ID, 19, 1), new GameTurnRightCard(7), new GameMoveForwardCard(GAME_ID, 23, 2)};
        fillRegisters(robots.get(3), cards3);
        // vom Laufband verschoben
        ProgramCard[] cards4 = {new GameMoveForwardCard(GAME_ID, 29, 3), new GameTurnLeftCard(14),
                new GameMoveForwardCard(GAME_ID, 20, 1), new GameUturnCard(4), new GameTurnRightCard(8)};
        fillRegisters(robots.get(4), cards4);
        // auf Werkstatt, blickt nach Süden
        robots.get(5).powerDown();
        ProgramCard[] cards6 = {new GameMoveForwardCard(GAME_ID, 21, 1), new GameMoveForwardCard(GAME_ID, 24, 2),
                new GameMoveForwardCard(GAME_ID, 30, 3), new GameTurnLeftCard(15), new GameMoveForwardCard(GAME_ID, 25, 2)};
        fillRegisters(robots.get(6), cards6);
        // vom Brett gefallen; respawnt neben Werkstatt
        robots.get(7).powerDown();
    }

    private void fillAllRegisters2() {
        ProgramCard[] cards0 = {new GameMoveForwardCard(GAME_ID, 23, 3), new GameTurnLeftCard(7),
                new GameMoveForwardCard(GAME_ID, 18, 2), new GameTurnRightCard(2), new GameMoveForwardCard(GAME_ID, 13, 1)};
        fillRegisters(robots.get(0), cards0);
        // Auf Förderband
        ProgramCard[] cards1 = {new GameMoveForwardCard(GAME_ID, 24, 3), new GameMoveForwardCard(GAME_ID, 19, 2),
                new GameMoveForwardCard(GAME_ID, 20, 2), new GameMoveForwardCard(GAME_ID, 25, 3), new GameMoveBackwardCard(GAME_ID, 11)};
        fillRegisters(robots.get(1), cards1);
        // In Loch gelaufen
        ProgramCard[] cards2 = {new GameMoveForwardCard(GAME_ID, 26, 3), new GameTurnLeftCard(8), new GameTurnRightCard(3),
                new GameUturnCard(1), new GameMoveBackwardCard(GAME_ID, 12)};
        fillRegisters(robots.get(2), cards2);
        // Auf Förderband
        ProgramCard[] cards3 = {new GameMoveForwardCard(GAME_ID, 27, 3), new GameTurnLeftCard(9),
                new GameMoveForwardCard(GAME_ID, 14, 1), new GameTurnRightCard(4), new GameMoveForwardCard(GAME_ID, 21, 2)};
        fillRegisters(robots.get(3), cards3);
        // Auf Förderband steckengeblieben: Zielfeld auch von anderem Roboter
        // angesteuert
        ProgramCard[] cards4 = {new GameMoveForwardCard(GAME_ID, 28, 3), new GameTurnRightCard(5),
                new GameMoveForwardCard(GAME_ID, 15, 1), new GameTurnLeftCard(10), new GameMoveForwardCard(GAME_ID, 16, 1)};
        fillRegisters(robots.get(4), cards4);
        // Auf Förderband
        robots.get(5).powerDown();
        ProgramCard[] cards6 = {new GameMoveForwardCard(GAME_ID, 22, 2), new GameMoveForwardCard(GAME_ID, 29, 3),
                new GameTurnRightCard(6), new GameMoveForwardCard(GAME_ID, 30, 3), new GameMoveForwardCard(GAME_ID, 17, 1)};
        fillRegisters(robots.get(6), cards6);
        // Auf Förderband steckengeblieben: Zielfeld auch von anderem Roboter
        // angesteuert
        robots.get(7).powerDown();
    }

    private void fillAllRegisters3() {
        ProgramCard[] cards0 = {new GameMoveForwardCard(GAME_ID, 1, 1), new GameTurnLeftCard(10), new GameTurnRightCard(13),
                new GameMoveBackwardCard(GAME_ID, 19), new GameMoveForwardCard(GAME_ID, 29, 1)};
        fillRegisters(robots.get(0), cards0);
        ProgramCard[] cards1 = {new GameMoveForwardCard(GAME_ID, 2, 1), new GameTurnLeftCard(11),
                new GameMoveForwardCard(GAME_ID, 18, 3), new GameMoveBackwardCard(GAME_ID, 20), new GameMoveForwardCard(GAME_ID, 30, 2)};
        fillRegisters(robots.get(1), cards1);
        ProgramCard[] cards2 = {new GameMoveForwardCard(GAME_ID, 4, 2), new GameTurnLeftCard(12), new GameMoveBackwardCard(GAME_ID, 14),
                new GameMoveForwardCard(GAME_ID, 24, 2), new GameMoveForwardCard(GAME_ID, 28, 1)};
        fillRegisters(robots.get(2), cards2);
        ProgramCard[] cards3 = {new GameMoveForwardCard(GAME_ID, 3, 1), new GameTurnRightCard(7),
                new GameMoveForwardCard(GAME_ID, 17, 2), new GameMoveForwardCard(GAME_ID, 23, 1), new GameTurnRightCard(25)};
        fillRegisters(robots.get(3), cards3);
        // Durch Laser zerstört
        ProgramCard[] cards4 = {new GameMoveForwardCard(GAME_ID, 5, 2), new GameTurnRightCard(8),
                new GameMoveForwardCard(GAME_ID, 16, 1), new GameMoveForwardCard(GAME_ID, 22, 1), new GameMoveForwardCard(GAME_ID, 27, 1)};
        fillRegisters(robots.get(4), cards4);
        robots.get(5).powerDown();
        ProgramCard[] cards6 = {new GameMoveForwardCard(GAME_ID, 6, 2), new GameTurnRightCard(9),
                new GameMoveForwardCard(GAME_ID, 15, 1), new GameMoveForwardCard(GAME_ID, 21, 1), new GameMoveForwardCard(GAME_ID, 26, 1)};
        fillRegisters(robots.get(6), cards6);
        robots.get(7).powerDown();
    }

    private void fillAllRegisters4() {
        ProgramCard[] cards0 = {new GameMoveForwardCard(GAME_ID, 2, 3), new GameTurnLeftCard(4), new GameMoveForwardCard(GAME_ID, 5, 1),
                new GameTurnRightCard(8), new GameMoveBackwardCard(GAME_ID, 9)};
        fillRegisters(robots.get(0), cards0);
        ProgramCard[] cards1 = {new GameTurnRightCard(1), new GameTurnRightCard(3), new GameMoveForwardCard(GAME_ID, 6, 3),
                new GameUturnCard(7), new GameMoveForwardCard(GAME_ID, 10, 2)};
        fillRegisters(robots.get(1), cards1);
        robots.get(2).powerDown();
        robots.get(3).powerDown();
        robots.get(4).powerDown();
        robots.get(5).powerDown();
        robots.get(6).powerDown();
        robots.get(7).powerDown();
    }

    private void fillAllRegisters5() {
        ProgramCard[] cards0 = {new GameUturnCard(6), new GameMoveForwardCard(GAME_ID, 16, 3), new GameTurnLeftCard(23),
                new GameTurnLeftCard(31), new GameTurnLeftCard(38)};
        fillRegisters(robots.get(0), cards0);
        ProgramCard[] cards1 = {new GameUturnCard(5), new GameMoveForwardCard(GAME_ID, 15, 3), new GameTurnLeftCard(22),
                new GameTurnLeftCard(30), new GameTurnLeftCard(37)};
        fillRegisters(robots.get(1), cards1);
        ProgramCard[] cards2 = {new GameMoveForwardCard(GAME_ID, 8, 2), new GameTurnRightCard(10),
                new GameMoveForwardCard(GAME_ID, 24, 1), new GameMoveBackwardCard(GAME_ID, 32), new GameMoveForwardCard(GAME_ID, 40, 1)};
        fillRegisters(robots.get(2), cards2);
        ProgramCard[] cards3 = {new GameUturnCard(4), new GameMoveForwardCard(GAME_ID, 14, 3), new GameTurnLeftCard(21),
                new GameTurnLeftCard(29), new GameTurnLeftCard(36)};
        fillRegisters(robots.get(3), cards3);
        ProgramCard[] cards4 = {new GameUturnCard(3), new GameMoveForwardCard(GAME_ID, 13, 3), new GameTurnLeftCard(20),
                new GameTurnLeftCard(28), new GameTurnLeftCard(35)};
        fillRegisters(robots.get(4), cards4);
        ProgramCard[] cards5 = {new GameMoveForwardCard(GAME_ID, 7, 2), new GameTurnRightCard(9), new GameTurnLeftCard(19),
                new GameTurnRightCard(25), new GameMoveForwardCard(GAME_ID, 39, 1)};
        fillRegisters(robots.get(5), cards5);
        ProgramCard[] cards6 = {new GameUturnCard(2), new GameMoveForwardCard(GAME_ID, 12, 3), new GameTurnLeftCard(18),
                new GameTurnLeftCard(27), new GameTurnLeftCard(34)};
        fillRegisters(robots.get(6), cards6);
        ProgramCard[] cards7 = {new GameUturnCard(1), new GameMoveForwardCard(GAME_ID, 11, 3), new GameTurnLeftCard(17),
                new GameTurnLeftCard(26), new GameTurnLeftCard(33)};
        fillRegisters(robots.get(7), cards7);
    }

    private void fillAllRegisters6() {
        ProgramCard[] cards0 = {new GameMoveForwardCard(GAME_ID, 108, 3), new GameMoveForwardCard(GAME_ID, 116, 3),
                new GameMoveForwardCard(GAME_ID, 124, 3), new GameMoveForwardCard(GAME_ID, 132, 3), new GameMoveForwardCard(GAME_ID, 140, 3)};
        fillRegisters(robots.get(0), cards0);
        ProgramCard[] cards1 = {new GameMoveForwardCard(GAME_ID, 107, 3), new GameMoveForwardCard(GAME_ID, 115, 3),
                new GameMoveForwardCard(GAME_ID, 123, 3), new GameMoveForwardCard(GAME_ID, 131, 3), new GameMoveForwardCard(GAME_ID, 139, 3)};
        fillRegisters(robots.get(1), cards1);
        ProgramCard[] cards2 = {new GameMoveForwardCard(GAME_ID, 102, 1), new GameMoveBackwardCard(GAME_ID, 109),
                new GameMoveForwardCard(GAME_ID, 118, 1), new GameMoveBackwardCard(GAME_ID, 125), new GameMoveForwardCard(GAME_ID, 134, 1)};
        fillRegisters(robots.get(2), cards2);
        ProgramCard[] cards3 = {new GameMoveForwardCard(GAME_ID, 106, 3), new GameMoveForwardCard(GAME_ID, 114, 3),
                new GameMoveForwardCard(GAME_ID, 122, 3), new GameMoveForwardCard(GAME_ID, 130, 3), new GameMoveForwardCard(GAME_ID, 138, 3)};
        fillRegisters(robots.get(3), cards3);
        ProgramCard[] cards4 = {new GameMoveForwardCard(GAME_ID, 105, 3), new GameMoveForwardCard(GAME_ID, 113, 3),
                new GameMoveForwardCard(GAME_ID, 121, 3), new GameMoveForwardCard(GAME_ID, 129, 3), new GameMoveForwardCard(GAME_ID, 137, 3)};
        fillRegisters(robots.get(4), cards4);
        ProgramCard[] cards5 = {new GameMoveBackwardCard(GAME_ID, 101), new GameMoveForwardCard(GAME_ID, 110, 1),
                new GameMoveBackwardCard(GAME_ID, 117), new GameMoveForwardCard(GAME_ID, 126, 1), new GameMoveBackwardCard(GAME_ID, 133)};
        fillRegisters(robots.get(5), cards5);
        ProgramCard[] cards6 = {new GameMoveForwardCard(GAME_ID, 104, 3), new GameMoveForwardCard(GAME_ID, 112, 3),
                new GameMoveForwardCard(GAME_ID, 120, 3), new GameMoveForwardCard(GAME_ID, 128, 3), new GameMoveForwardCard(GAME_ID, 136, 3)};
        fillRegisters(robots.get(6), cards6);
        ProgramCard[] cards7 = {new GameMoveForwardCard(GAME_ID, 103, 3), new GameMoveForwardCard(GAME_ID, 111, 3),
                new GameMoveForwardCard(GAME_ID, 119, 3), new GameMoveForwardCard(GAME_ID, 127, 3), new GameMoveForwardCard(GAME_ID, 135, 3)};
        fillRegisters(robots.get(7), cards7);
    }

    private void fillRegisters(Robot robot, ProgramCard... cards) {
        for (int i = 0; i < cards.length; i++) {
            robot.fillRegister(i, cards[i]);
        }
    }

    // Tests die Player abfragen

    @Test
    public void checkIfAllPlayersGet9CardsAtStart() {
        master.dealCardsToPlayers();
        List<Player> players = master.getPlayers();
        for (Player p : players) {
            assertEquals(9, p.getPlayerCards().size());
        }
    }

    @Test
    public void noPlayerCardNull() {
        master.dealCardsToPlayers();
        List<Player> players = master.getPlayers();
        for (Player p : players) {
            for (ProgramCard c : p.getPlayerCards()) {
                assertNotNull(c);
            }
        }
    }

    @Test
    public void playersHave4CardsAfterPlaying5Cards() {
        master.dealCardsToPlayers();
        List<Player> players = master.getPlayers();
        for (Player p : players) {
            p.fillRegister(0, p.getPlayerCards().get(0));
            p.fillRegister(1, p.getPlayerCards().get(1));
            p.fillRegister(2, p.getPlayerCards().get(2));
            p.fillRegister(3, p.getPlayerCards().get(3));
            p.fillRegister(4, p.getPlayerCards().get(4));
            assertEquals(4, p.getPlayerCards().size());
        }
    }

    @Test
    public void noPlayerCardNullAfterPlaying5Cards() {
        master.dealCardsToPlayers();
        List<Player> players = master.getPlayers();
        for (Player p : players) {
            p.fillRegister(0, p.getPlayerCards().get(0));
            p.fillRegister(1, p.getPlayerCards().get(1));
            p.fillRegister(2, p.getPlayerCards().get(2));
            p.fillRegister(3, p.getPlayerCards().get(3));
            p.fillRegister(4, p.getPlayerCards().get(4));
            for (ProgramCard c : p.getPlayerCards()) {
                assertNotNull(c);
            }
        }
    }

    @Test
    public void playersGiveOverall72CardsBackWhenNonePlayed() {
        master.dealCardsToPlayers();
        List<ProgramCard> allCards = new ArrayList<ProgramCard>();
        List<Player> players = master.getPlayers();
        for (Player p : players) {
            allCards.addAll(p.discardCards());
        }
        assertEquals(72, allCards.size());
    }

    @Test
    public void playersGiveNoNullsBack() {
        master.dealCardsToPlayers();
        List<ProgramCard> allCards = new ArrayList<ProgramCard>();
        List<Player> players = master.getPlayers();
        for (Player p : players) {
            allCards.addAll(p.discardCards());
        }
        for (ProgramCard c : allCards) {
            assertNotNull(c);
        }
    }

    @Test
    public void playersGiveOverall32CardsBackWhen5CardsPlayedByEach() {
        master.dealCardsToPlayers();
        List<ProgramCard> allCards = new ArrayList<ProgramCard>();
        List<Player> players = master.getPlayers();
        for (Player p : players) {
            p.fillRegister(0, p.getPlayerCards().get(0));
            p.fillRegister(1, p.getPlayerCards().get(1));
            p.fillRegister(2, p.getPlayerCards().get(2));
            p.fillRegister(3, p.getPlayerCards().get(3));
            p.fillRegister(4, p.getPlayerCards().get(4));
            allCards.addAll(p.discardCards());
        }
        assertEquals(32, allCards.size());
    }

    @Test
    public void playersGiveNoNullsBackAfterPlayingCards() {
        master.dealCardsToPlayers();
        List<ProgramCard> allCards = new ArrayList<ProgramCard>();
        List<Player> players = master.getPlayers();
        for (Player p : players) {
            p.fillRegister(0, p.getPlayerCards().get(0));
            p.fillRegister(1, p.getPlayerCards().get(1));
            p.fillRegister(2, p.getPlayerCards().get(2));
            p.fillRegister(3, p.getPlayerCards().get(3));
            p.fillRegister(4, p.getPlayerCards().get(4));
            allCards.addAll(p.discardCards());
        }
        for (ProgramCard c : allCards) {
            assertNotNull(c);
        }
    }

    // Tests die Robots abfragen

    @Test
    public void checkIfAllRobotsHaveEmptyRegistersAtStart() {
        master.dealCardsToPlayers();
        List<Player> players = master.getPlayers();
        for (Player p : players) {
            for (ProgramCard c : p.getRobot().getRegisters()) {
                assertNull(c);
            }
        }
    }

    @Test
    public void robotsHaveNoNullInRegistersAfterPlayersPlayed5Cards() {
        master.dealCardsToPlayers();
        List<Player> players = master.getPlayers();
        for (Player p : players) {
            p.fillRegister(0, p.getPlayerCards().get(0));
            p.fillRegister(1, p.getPlayerCards().get(1));
            p.fillRegister(2, p.getPlayerCards().get(2));
            p.fillRegister(3, p.getPlayerCards().get(3));
            p.fillRegister(4, p.getPlayerCards().get(4));
            for (ProgramCard c : p.getRobot().getRegisters()) {
                assertNotNull(c);
            }
        }
    }

    @Test
    public void robotsGive40CardsOverallBackAfterGetting5Each() {
        master.dealCardsToPlayers();
        List<Player> players = master.getPlayers();
        List<ProgramCard> allCards = new ArrayList<ProgramCard>();
        for (Player p : players) {
            p.fillRegister(0, p.getPlayerCards().get(0));
            p.fillRegister(1, p.getPlayerCards().get(1));
            p.fillRegister(2, p.getPlayerCards().get(2));
            p.fillRegister(3, p.getPlayerCards().get(3));
            p.fillRegister(4, p.getPlayerCards().get(4));
        }
        for (Player p : players) {
            allCards.addAll(p.getRobot().emptyAllRegister());
        }
        assertEquals(40, allCards.size());
    }

    @Test
    public void robotsGiveNoNullsBackAfterGetting5Each() {
        master.dealCardsToPlayers();
        List<Player> players = master.getPlayers();
        List<ProgramCard> allCards = new ArrayList<ProgramCard>();
        for (Player p : players) {
            p.fillRegister(0, p.getPlayerCards().get(0));
            p.fillRegister(1, p.getPlayerCards().get(1));
            p.fillRegister(2, p.getPlayerCards().get(2));
            p.fillRegister(3, p.getPlayerCards().get(3));
            p.fillRegister(4, p.getPlayerCards().get(4));
        }
        for (Player p : players) {
            allCards.addAll(p.getRobot().emptyAllRegister());
        }
        for (ProgramCard c : allCards) {
            assertNotNull(c);
        }
    }

    // Tests die das Deck abfragen

    @Test
    public void afterDealing9CardsTo8Players12CardsLeftInDeck() {
        master.dealCardsToPlayers();
        master.dealCard();
        master.dealCard();
        master.dealCard();
        master.dealCard();
        master.dealCard();
        master.dealCard();
        master.dealCard();
        master.dealCard();
        master.dealCard();
        master.dealCard();
        master.dealCard();
        ProgramCard c12 = master.dealCard();
        ProgramCard c13 = master.dealCard();
        assertNotNull(c12);
        assertNull(c13);
    }

    @Test
    public void multiBoardTest() {
        Master master = factory.getMaster(GAME_ID);

        PlayerFactory pFactory = new GamePlayerFactory();

        master.removeAllPlayers();
        master.addPlayer(pFactory.createNewPlayer(GAME_ID, "BoxBot", "nope"));
        master.addPlayer(pFactory.createNewPlayer(GAME_ID, "HauBot", "nope"));
        master.initialiseBoard("checkmate");

        List<Player> players = master.getPlayers();

        Map<String, Robot> robots = new HashMap<>();
        robots.put(players.get(0).getRobot().getName(), players.get(0).getRobot());
        robots.put(players.get(1).getRobot().getName(), players.get(1).getRobot());

        Map<String, Robot> boardRobots = new HashMap<>();

        master.getBoard().getFields().values().forEach(field -> field.getTiles().values().forEach(tile -> {
            Robot robot = tile.getRobot();
            if (robot != null) {
                boardRobots.put(robot.getName(), robot);
            }
        }));

        assertEquals(2, players.size());
        assertEquals(2, robots.size());
        assertEquals(2, boardRobots.size());
        robots.keySet().forEach(key -> assertEquals(robots.get(key), boardRobots.get(key)));

        master.removeAllPlayers();
        master.addPlayer(pFactory.createNewPlayer(GAME_ID, "BoxBot", "nope"));
        master.addPlayer(pFactory.createNewPlayer(GAME_ID, "HauBot", "nope"));
        master.initialiseBoard("checkmate");

        players = master.getPlayers();

        HashMap<String, Robot> robots2 = new HashMap<>();
        robots2.put(players.get(0).getRobot().getName(), players.get(0).getRobot());
        robots2.put(players.get(1).getRobot().getName(), players.get(1).getRobot());

        HashMap<String, Robot> boardRobots2 = new HashMap<>();

        master.getBoard().getFields().values().forEach(field -> field.getTiles().values().forEach(tile -> {
            Robot robot = tile.getRobot();
            if (robot != null) {
                boardRobots2.put(robot.getName(), robot);
            }
        }));

        assertEquals(2, players.size());
        assertEquals(2, robots2.size());
        assertEquals(2, boardRobots2.size());
        robots2.keySet().forEach(key -> assertEquals(robots2.get(key), boardRobots2.get(key)));
    }
}
