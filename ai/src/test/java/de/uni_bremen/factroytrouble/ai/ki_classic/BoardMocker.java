package de.uni_bremen.factroytrouble.ai.ki_classic;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import org.mockito.Mockito;

import de.uni_bremen.factroytrouble.ai.ki_classic.path_finding.dykstra.DykstraHandler;
import de.uni_bremen.factroytrouble.board.Board;
import de.uni_bremen.factroytrouble.board.Field;
import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.gameobjects.Flag;
import de.uni_bremen.factroytrouble.gameobjects.Hole;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.gameobjects.Wall;
import de.uni_bremen.factroytrouble.player.Master;
import de.uni_bremen.factroytrouble.player.Player;

/**
 * Mock des Bretts mit einem Spieler und Roboter; wird fuer die Tests der
 * klassischen KI benutzt.
 * 
 * @author Tim, Markus
 *
 */
public class BoardMocker {

    private final Master master = mock(Master.class);
    private final DykstraHandler dykstra = mock(DykstraHandler.class);

    private final Board board;
    private final Field field;
    private final Player player;
    private final Robot robot;
    private int flagCounter;

    /**
     * Erstellt eine Instanz.
     */
    public BoardMocker() {
        flagCounter = 0;
        board = mock(Board.class);
        when(master.getBoard()).thenReturn(board);
        when(board.getHighestFlagNumber()).thenReturn(flagCounter);

        field = mock(Field.class);
        Map<Point, Field> fields = new HashMap<>();
        fields.put(new Point(0, 0), field);
        when(board.getFields()).thenReturn(fields);

        player = mock(Player.class);
        robot = mock(Robot.class);
        when(player.getRobot()).thenReturn(robot);
        when(robot.getFlagCounterStatus()).thenReturn(0);
        when(robot.registerLockStatus()).thenReturn(new boolean[] { false, false, false, false, false });

    }

    /**
     * An der angegebenen Position bekommt das Brett eine Mauer.
     * 
     * @param pos
     *            Die Position der Mauer
     * @param ori
     *            Die Ausrichtung der Mauer
     */
    public void setWall(Point pos, Orientation ori) {
        Tile tile = field.getTiles().get(pos);
        Wall wall = mock(Wall.class);
        when(tile.hasWall(ori)).thenReturn(true);
        when(tile.getWall(ori)).thenReturn(wall);
    }

    /**
     * An der angegebenen Position bekommt das Brett ein Loch.
     * 
     * @param pos
     *            Die Position des Lochs
     */
    public void setHole(Point pos) {
        Tile tile = field.getTiles().get(pos);
        Hole hole = Mockito.mock(Hole.class);
        when(tile.getFieldObject()).thenReturn(hole);
        setWeight(pos, Integer.MAX_VALUE);
    }

    /**
     * An der angegebenen Position bekommt das Brett eine neue Flagge. Die
     * Nummer wird automatisch gewählt.
     * 
     * @param pos
     *            Die Position der Flagge.
     */
    public void setFlagTile(Point pos) {
        Tile flagTile = field.getTiles().get(pos);
        Flag flag = mock(Flag.class);
        when(flag.getNumber()).thenReturn(++flagCounter);
        when(flagTile.getFieldObject()).thenReturn(flag);

        for (Tile tile : field.getTiles().values()) {
            Point coords = tile.getAbsoluteCoordinates();
            int flagDistance = (int) coords.distance(pos);
            setWeight(coords, flagDistance);
        }
        when(board.getHighestFlagNumber()).thenReturn(flagCounter);

    }

    /**
     * Die Startposition fuer den Roboter wird an die angegebene Position
     * gesetzt.
     * 
     * @param pos
     *            Die neue Position des Startfelds
     * @param ori
     *            Die neue Startausrichtung des Roboters
     */
    public void setStartTile(Point pos, Orientation ori) {
        Tile startTile = field.getTiles().get(pos);
        when(robot.getOrientation()).thenReturn(ori);
        when(robot.getCurrentTile()).thenReturn(startTile);
    }

    /**
     * Fuellt das Feld mit Tiles.
     * 
     * @param size
     *            Die Groesse, die das Feld haben soll.
     */
    public void fillField(int size) {
        Map<Point, Tile> tiles = new HashMap<>();
        when(field.getTiles()).thenReturn(tiles);
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Point coords = new Point(x, y);
                tiles.put(coords, getMockTile(coords));
            }
        }
        setWeight(new Point(-1, -1), Integer.MAX_VALUE);
    }

    /**
     * Gets Master
     * 
     * @return Master
     */
    public Master getMaster() {
        return master;
    }

    /**
     * Gets Dykstra
     * 
     * @return Dykstra
     */
    public DykstraHandler getDykstra() {
        return dykstra;
    }

    /**
     * Gets Board
     * 
     * @return Board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Gets Field
     * 
     * @return Field
     */
    public Field getField() {
        return field;
    }

    /**
     * Gets Player
     * 
     * @return Player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets Robot
     * 
     * @return Robot
     */
    public Robot getRobot() {
        return robot;
    }

    private void setWeight(Point pos, int weight) {
        when(dykstra.getWeight(eq(1), eq(pos), any())).thenReturn(weight);
    }

    private Tile getMockTile(Point coords) {
        Tile tile = mock(Tile.class);
        when(tile.getAbsoluteCoordinates()).thenReturn(coords);
        when(tile.hasWall(any())).thenReturn(false);
        when(tile.getFieldObject()).thenReturn(null);
        return tile;
    }
}
