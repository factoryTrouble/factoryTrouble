/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki1;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import de.uni_bremen.factroytrouble.ai.ki1.configreader.AgentConfigReader;
import de.uni_bremen.factroytrouble.ai.ki1.memory.KeyUnit;
import de.uni_bremen.factroytrouble.ai.ki1.memory.MemoryUnit;
import de.uni_bremen.factroytrouble.ai.ki1.memory.RobotEvent;
import de.uni_bremen.factroytrouble.ai.ki1.memory.RobotEvent.EventType;
import de.uni_bremen.factroytrouble.api.ki1.BoardArea;
import de.uni_bremen.factroytrouble.api.ki1.Placement;
import de.uni_bremen.factroytrouble.api.ki1.Timer;
import de.uni_bremen.factroytrouble.api.ki1.Visual;
import de.uni_bremen.factroytrouble.api.ki1.memory.Chunk;
import de.uni_bremen.factroytrouble.api.ki1.memory.Key;
import de.uni_bremen.factroytrouble.board.Board;
import de.uni_bremen.factroytrouble.board.Field;
import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.exceptions.ConfigPropertyNotFoundRuntimeException;
import de.uni_bremen.factroytrouble.exceptions.KeyNotFoundException;
import de.uni_bremen.factroytrouble.exceptions.NoIdForPlayerRuntimeException;
import de.uni_bremen.factroytrouble.gameobjects.FieldObject;
import de.uni_bremen.factroytrouble.gameobjects.Flag;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.gameobjects.Workshop;
import de.uni_bremen.factroytrouble.observer.Event;
import de.uni_bremen.factroytrouble.player.OptionCard;
import de.uni_bremen.factroytrouble.player.Player;
import de.uni_bremen.factroytrouble.player.ProgramCard;

/**
 * {@inheritDoc}
 * 
 * @author tim, artur
 *
 */
public class VisualUnit implements Visual {
    private static final Logger LOGGER = Logger.getLogger(VisualUnit.class);

    /**
     * Basis für {@link Key}s der {@link Placement}s von Robotern
     */
    public static final String PLAYER_POS_KEY = "PLAYER_POS(%d)";
    /**
     * Basis für {@link Key}s der {@link Placement}s von Flaggen.
     */
    public static final String FLAG_POS_KEY = "FLAG_POS(%d)";
    /**
     * Basis für {@link Key}s von {@link BoardArea}s.
     */
    public static final String BOARDAREA_KEY = "BOARDAREA(%d,%d)";
    /**
     * Basis für {@link Key}s von Trefferpunkten eines Roboters.
     */
    public static final String HP_KEY = "PLAYER_HP(%d)";
    /**
     * Basis für {@link Key}s von Leben eines Spielers.
     */
    public static final String LIVES_KEY = "PLAYER_LIVES(%d)";
    /**
     * Basis für {@link Key}s vom Powerdown-Status eines Spielers.
     */
    public static final String POWERDOWN_KEY = "PLAYER_POWERDOWN(%d)";
    /**
     * Basis für {@link Key}s von Respawnpunkten der Spieler.
     */
    public static final String RESPAWN_KEY = "PLAYER_RESPAWN(%d)";
    /**
     * Basis für {@link Key}s von eigenen Karten auf der Hand.
     */
    public static final String CARD_KEY = "OWN_CARD(%d)";
    /**
     * Basis für {@link Key}s der LookupArea Chunks.
     */
    private static final String LOOKUP_AREA_KEY = "LOOKUPAREA(%d)";
    /**
     * {@link Key} aller {@link RobotEvent}s.
     */
    private static final String ROBOT_EVENTS_KEY = "ROBOT_EVENTS";
    /**
     * Anzahl gespeicherte LookupArea Chunks.
     */
    private static final int NB_MEMORY_LOOKUP_AREAS;
    /**
     * Größe der {@link LookupBoardArea}s.
     */
    private static final int LOOKUP_AREA_LENGTH = 4;

    /**
     * Memory.
     */
    private MemoryUnit mem;
    /**
     * Spieleridentifikation.
     */
    private Map<Integer, Player> idToPlayer;
    /**
     * Alle {@link Tile}s des {@link Board}s, gemappt auf ihre absolute
     * Position.
     */
    private Map<Point, Tile> ptToTile;
    /**
     * Die id des eigenen Spielers.
     */
    private int ownPlayerId;

    /**
     * Die aktuelle Spielrunde
     */
    private int round;

    static {
        try {
            NB_MEMORY_LOOKUP_AREAS = AgentConfigReader.getInstance(1).getIntProperty("Visual.LookupAreasInMemory");
        } catch (KeyNotFoundException | IOException e) {
            LOGGER.error(
                    "Config-Datei existiert nicht oder der Key Visual.LookupAreasInMemory konnte nicht gefunden werden",
                    e);
            throw new ConfigPropertyNotFoundRuntimeException(e);
        }
    }

    /**
     * Erstellt eine {@link VisualUnit}.
     * 
     * @param mem
     *            Memory
     * @param board
     *            das Spielbrett
     * @param players
     *            Spieleridentifikation
     * @param timer
     *            ein Timer
     */
    public VisualUnit(MemoryUnit mem, Board board, Map<Integer, Player> players, int ownPlayerId, Timer timer) {
        this(mem, board);
        if (players == null) {
            throw new IllegalArgumentException("null players");
        }
        this.idToPlayer = players;
        this.ownPlayerId = ownPlayerId;
    }

    public VisualUnit(MemoryUnit mem, Board board) {
        if (mem == null) {
            throw new IllegalArgumentException("null memory");
        }
        if (board == null) {
            throw new IllegalArgumentException("null board");
        }
        this.mem = mem;
        this.idToPlayer = new HashMap<>();
        this.ptToTile = new HashMap<Point, Tile>();
        for (Field field : board.getFields().values()) {
            for (Tile tile : field.getTiles().values()) {
                ptToTile.put(tile.getAbsoluteCoordinates(), tile);
            }
        }
        storeLookupAreas();
    }

    public void addPlayer(Player player, int id) {
        idToPlayer.put(id, player);
    }

    public void addOwnPlayer(Player player, int id) {
        addPlayer(player, id);
        ownPlayerId = id;
    }

    /**
     * Setzt die aktuelle Runde.
     * 
     * @param round
     *            die aktuelle Runde
     */
    public void setCurrentRound(int round) {
        this.round = round;
    }

    /**
     * {@inheritDoc}
     * 
     * Um die Position zu finden, wird erst in den vollsten Bereichen des Boards
     * gesucht.
     */
    @Override
    public Placement getPlayerPlacement(int id) {
        if (!idToPlayer.containsKey(id)) {
            return null;
        }
        // anstatt p.getRobot().getCurrentTile() zu nutzen, suchen wir kognitiv
        // auf dem Board nach dem Roboter
        Placement result = findOnBoard(area -> area.getPlayerPlacement(idToPlayer.get(id)));
        mem.storeChunk(memKey(PLAYER_POS_KEY, id), result);
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * Um die Position zu finden, wird erst in den vollsten Bereichen des Boards
     * gesucht.
     */
    @Override
    public Placement getFlagPosition(int number) {
        Placement result = findOnBoard(area -> area.getFlagPosition(number));
        mem.storeChunk(memKey(FLAG_POS_KEY, number), result);
        return result;
    }

    @Override
    public BoardArea getArea(Point center, int length) {
        validateArea(center, length);
        if (!ptToTile.containsKey(center)) {
            return null;
        }
        Map<Point, Tile> tiles = new HashMap<Point, Tile>();
        for (int x = -length / 2; x <= length / 2; x++) {
            for (int y = -length / 2; y <= length / 2; y++) {
                Point pt = new Point(center.x + x, center.y + y);
                if (ptToTile.containsKey(pt)) {
                    Tile tile = ptToTile.get(pt);
                    tiles.put(pt, tile);
                }
            }
        }
        BoardArea result = new BoardAreaUnit(center, tiles);
        mem.storeChunk(memKey(BOARDAREA_KEY, center.x, center.y), result);
        return result;
    }

    @Override
    public int getHP(int playerId) {
        int result = idToPlayer.get(playerId).getRobot().getHP();
        mem.storeChunk(memKey(HP_KEY, playerId), result);
        return result;
    }

    @Override
    public int getLives(int playerId) {
        int result = idToPlayer.get(playerId).getRobot().getLives();
        mem.storeChunk(memKey(LIVES_KEY, playerId), result);
        return result;
    }

    @Override
    public boolean isPoweredDown(int playerId) {
        boolean result = idToPlayer.get(playerId).getRobot().isPoweredDown();
        mem.storeChunk(memKey(POWERDOWN_KEY, playerId), result);
        return result;
    }

    @Override
    public Placement getRespawnPoint(int playerId) {
        Placement result = respawnPoint(idToPlayer.get(playerId).getRobot());
        mem.storeChunk(memKey(RESPAWN_KEY, playerId), result);
        return result;
    }

    @Override
    public List<ProgramCard> getLockedCards(int playerId) {
        Player player = idToPlayer.get(playerId);
        ProgramCard[] allCards = player.getRobot().getRegisters();
        boolean[] locked = player.getRobot().registerLockStatus();
        return IntStream.range(0, allCards.length).filter(i -> locked[i]).mapToObj(i -> allCards[i])
                .collect(Collectors.toList());
    }

    @Override
    public List<OptionCard> getOptionCards(int playerId) {
        return idToPlayer.get(playerId).getRobot().getOptions();
    }

    @Override
    public ProgramCard getCard(int number) {
        if (idToPlayer.get(ownPlayerId).getPlayerCards().size() < number) {
            return null;
        }
        ProgramCard result = idToPlayer.get(ownPlayerId).getPlayerCards().get(number - 1);
        mem.storeChunk(memKey(CARD_KEY, number), result);
        return result;
    }
    
    @Override
    public void spam(Robot robot, Event e, Object source) {
        if (robot == null) {
            throw new IllegalArgumentException("null robot");
        }
        if (robot.getLives() == 0) {
            return;
        }
        switch (e) {
        case MOVED:
            handleMovedEvent(robot);
            break;
        case TURNED:
            handleTurnedEvent(robot);
            break;
        case PUSHED:
            handlePushedEvent(robot, source);
            break;
        case HEALED:
            handleHealedEvent(robot);
            break;
        case SHOT:
            handleShotEvent(robot, source);
            break;
        case KILLED:
            handleKilledEvent(robot, source);
            break;
        case POWERED_DOWN:
        case WAKED_UP:
            handlePowerdownEvent(robot);
            break;
        case REGISTER_FILLED:
            break;
        case RESPAWN:
            handleRespawnEvent(robot);
            break;
        default:
            LOGGER.error("unsupported event "+e);
        }
    }
    
    private void handleMovedEvent(Robot robot) {
        if (robot.getCurrentTile() == null) {
            return; // vom Board gefallen
        }
        int id = getIdOfRobot(robot);
        FieldObject fieldObj = robot.getCurrentTile().getFieldObject();
        if (fieldObj instanceof Flag || fieldObj instanceof Workshop) {
            mem.storeChunk(memKey(RESPAWN_KEY, id), respawnPoint(robot));
        }
        Point pos = robot.getCurrentTile().getAbsoluteCoordinates();
        mem.storeChunk(memKey(PLAYER_POS_KEY, id),
                new PlacementUnit(pos, robot.getOrientation(), robot.getCurrentTile()));   
        updateLookupAreas(robot);
    }
    
    private void handleRespawnEvent(Robot robot) {
        int id = getIdOfRobot(robot);
        Point pos = robot.getCurrentTile().getAbsoluteCoordinates();
        mem.storeChunk(memKey(PLAYER_POS_KEY, id),
                new PlacementUnit(pos, robot.getOrientation(), robot.getCurrentTile())); 
        updateLookupAreas(robot);
    }
    
    private void handleTurnedEvent(Robot robot) {
        if (robot.getCurrentTile() == null) {
            return; // vom Board gefallen
        }
        int id = getIdOfRobot(robot);
        Point pos = robot.getCurrentTile().getAbsoluteCoordinates();
        mem.storeChunk(memKey(PLAYER_POS_KEY, id),
                new PlacementUnit(pos, robot.getOrientation(), robot.getCurrentTile()));   
    }
    
    private void handlePushedEvent(Robot robot, Object source) {
        Robot robotSource = source instanceof Robot ? (Robot) source : null;
        EventInvolvement eventType = getEventType(robot, robotSource);
        if (robotSource != null && eventType != EventInvolvement.BYSTANDING) {
            boolean active = eventType == EventInvolvement.ACTIVE;
            storeEvent(new RobotEvent(robotSource, EventType.SHOVEDME, active, round));
        }
    }
    
    private void handleShotEvent(Robot robot, Object source) {
        int id = getIdOfRobot(robot);
        Robot robotSource = source instanceof Robot ? (Robot) source : null;
        EventInvolvement eventType = getEventType(robot, robotSource);
        mem.storeChunk(memKey(HP_KEY, id), robot.getHP());
        if (robotSource != null && eventType != EventInvolvement.BYSTANDING) {
            boolean active = eventType == EventInvolvement.ACTIVE;
            storeEvent(new RobotEvent(robotSource, EventType.SHOTME, active, round));
        }
    }
    
    private void handleKilledEvent(Robot robot, Object source) {
        int id = getIdOfRobot(robot);
        Robot robotSource = source instanceof Robot ? (Robot) source : null;
        EventInvolvement eventType = getEventType(robot, robotSource);
        mem.storeChunk(memKey(LIVES_KEY, id), robot.getLives());
        if (robotSource != null && eventType != EventInvolvement.BYSTANDING) {
            boolean active = eventType == EventInvolvement.ACTIVE;
            storeEvent(new RobotEvent(robotSource, EventType.KILLEDME, active, round));
        }
    }
    
    private void handleHealedEvent(Robot robot) {
        int id = getIdOfRobot(robot);
        mem.storeChunk(memKey(HP_KEY, id), robot.getHP());
    }
    
    private void handlePowerdownEvent(Robot robot) {
        int id = getIdOfRobot(robot);
        mem.storeChunk(memKey(POWERDOWN_KEY, id), robot.isPoweredDown());
    }

    /**
     * Aktualisiert die {@link LookupBoardArea}s.
     * 
     * @param movedRobot
     *            ein Roboter, der sich bewegt hat
     */
    private void updateLookupAreas(Robot movedRobot) {
        storeLookupAreas();
    }

    /**
     * Merkt sich ein {@link RobotEvent} im Memory.
     * 
     * @param e
     *            das zu merkende Event
     */
    @SuppressWarnings("unchecked")
    private void storeEvent(RobotEvent e) {
        KeyUnit key = memKey(ROBOT_EVENTS_KEY);
        Chunk chunk = mem.retrieveChunk(key);
        if (chunk == null) {
            List<RobotEvent> events = new ArrayList<>();
            events.add(e);
            mem.storeChunk(key, events);
            return;
        }
        List<RobotEvent> events = (List<RobotEvent>) (List<?>) chunk.getData();
        for (RobotEvent other : events) {
            if (other.getEventType() == e.getEventType() && other.getRobot() == e.getRobot()) {
                other.increment();
                mem.storeChunk(key, events);
                return;
            }
        }
        events.add(e);
        mem.storeChunk(key, events);
    }

    private enum EventInvolvement {
        ACTIVE, AFFECTED, BYSTANDING
    }

    /**
     * Entscheidet anhand der gegebenen Aktoren den {@link EventType}.
     * 
     * @param robot
     *            erster Robot
     * @param source
     *            evtl. zweiter Robot
     * @return der {@link EventType}
     */
    private EventInvolvement getEventType(Robot robot, Robot source) {
        if (source == null) {
            return EventInvolvement.BYSTANDING;
        }
        Robot ownRobot = idToPlayer.get(ownPlayerId).getRobot();
        if (robot == ownRobot) {
            return EventInvolvement.AFFECTED;
        } else if (source == ownRobot) {
            return EventInvolvement.ACTIVE;
        }
        return EventInvolvement.BYSTANDING;
    }

    /**
     * Überprüft die Breite und den Mittelpunkt der Area. Falls ein Fehler
     * auftritt wird eine Exception geworfen mit einer entsprechenden Meldung.
     * 
     * @param center
     * @param length
     */
    private void validateArea(Point center, int length) {
        if (length == 0 || length % 2 == 0) {
            throw new IllegalArgumentException("length must be > 0 and odd but was " + length);
        }
        if (center == null) {
            throw new IllegalArgumentException("null center");
        }
    }

    /**
     * Hilfsmethode zur Erzeugung von {@link KeyUnit}s.
     * 
     * @param base
     *            Basisstring
     * @param objs
     *            Objekte zur Formatierung
     * @return die erzeugte {@link KeyUnit}
     */
    private KeyUnit memKey(String base, Object... objs) {
        return new KeyUnit(String.format(base, objs));
    }

    /**
     * Liefert die Player Id eines gegebenen Robots.
     * 
     * @param robot
     *            der Robot, dessen Player Id gesucht wird
     * @return die Id des Players, dem der Robot gehört
     */
    private int getIdOfRobot(Robot robot) {
        Optional<Entry<Integer, Player>> idTuple = idToPlayer.entrySet().stream()
                .filter(e -> e.getValue().getRobot().equals(robot)).findFirst();
        if (!idTuple.isPresent()) {
            throw new NoIdForPlayerRuntimeException(robot);
        }
        return idTuple.get().getKey();
    }

    /**
     * Speichert einige {@link LookupBoardArea}s zur schnelleren Suche in der
     * Memory.
     */
    private void storeLookupAreas() {
        List<LookupBoardArea> areas = ptToTile.keySet().stream()
                .filter(pt -> pt.x % LOOKUP_AREA_LENGTH == 0 && pt.y % LOOKUP_AREA_LENGTH == 0)
                .map(pt -> new LookupBoardArea(pt))
                .sorted((a, b) -> Integer.compare(a.getNbObjects(), b.getNbObjects())).limit(NB_MEMORY_LOOKUP_AREAS)
                .collect(Collectors.toList());

        for (int i = 0; i < areas.size(); i++) {
            mem.storeChunk(memKey(LOOKUP_AREA_KEY, i), areas.get(i));
        }
    }

    /**
     * Liefert die gespeicherten {@link LookupBoardArea}s aus der Memory.
     * 
     * @return die gespeicherten {@link LookupBoardArea}s
     */
    private List<LookupBoardArea> retrieveLookupAreas() {
        List<LookupBoardArea> crowdedAreas = new ArrayList<LookupBoardArea>();
        for (int i = 0; i < NB_MEMORY_LOOKUP_AREAS; i++) {
            String key = String.format(LOOKUP_AREA_KEY, i);
            Chunk areaChunk = mem.retrieveChunk(new KeyUnit(key));
            if (areaChunk == null) {
                storeLookupAreas();
                return retrieveLookupAreas();
            }
            LookupBoardArea area = (LookupBoardArea) areaChunk.getData();
            crowdedAreas.add(area);
        }
        return crowdedAreas;
    }

    /**
     * Sucht kognitiv nach einem Objekt auf dem Board.
     * 
     * @param mapper
     *            verarbeitet eine {@link LookupBoardArea} zu einem Objekt mit
     *            dem gesuchten Typen
     * @return {@code null}, falls nicht gefunden, sonst das gesuchte Objekt
     */
    private <T> T findOnBoard(Function<LookupBoardArea, T> mapper) {
        List<LookupBoardArea> areasDone = new ArrayList<LookupBoardArea>();
        List<LookupBoardArea> currentAreas = retrieveLookupAreas();
        Optional<T> result = Optional.empty();
        while (true) {
            result = currentAreas.stream().map(area -> {
                areasDone.add(area);
                return mapper.apply(area);
            }).filter(placement -> placement != null).findFirst();
            if (result.isPresent()) {
                return result.get();
            }
            currentAreas = currentAreas.stream().flatMap(area -> area.getNeighbors())
                    .filter(area -> !areasDone.contains(area)).collect(Collectors.toList());
            if (currentAreas.isEmpty()) {
                return null;
            }
        }
    }

    /**
     * Liefert den Respawnpunkt eines Robots.
     * 
     * @param robot
     *            der Robot, dessen Respawnpunkt gesucht wird
     * @return der Respawnpunkt
     */
    private Placement respawnPoint(Robot robot) {
        Tile tile = robot.getRespawnPoint();
        return new PlacementUnit(tile.getAbsoluteCoordinates(), null, tile);
    }

    /**
     * Hilfsklasse zum Suchen innerhalb von Bereichen.
     */
    public class LookupBoardArea {
        /**
         * Alle enthaltenen {@link Tile}s gemappt auf ihre absolute Position.
         */
        private Map<Point, Tile> tiles;
        /**
         * Anzahl relevanter Objekte.
         */
        private int nbObjects;
        /**
         * Ausgangspunkt des Bereiches.
         */
        private Point root;

        /**
         * Erstellt eine {@link LookupBoardArea} um {@code root}.
         * 
         * @param root
         *            der Mittelpunkt
         */
        public LookupBoardArea(Point root) {
            this.root = root;
            tiles = new HashMap<Point, Tile>();
            for (int x = root.x; x < root.x + LOOKUP_AREA_LENGTH; x++) {
                for (int y = root.y; y < root.y + LOOKUP_AREA_LENGTH; y++) {
                    Point pt = new Point(x, y);
                    tiles.put(pt, ptToTile.get(pt));
                }
            }
            update();
        }

        /**
         * Aktualisiert {@code nbObjects}.
         */
        public void update() {
            nbObjects = 0;
            for (Entry<Point, Tile> entry : tiles.entrySet()) {
                if (entry.getValue().getRobot() != null) {
                    nbObjects++;
                }
            }
        }

        /**
         * Liefert Anzahl relevanter Objekte innerhalb der Area.
         * 
         * @return {@code nbObjects}
         */
        public int getNbObjects() {
            return nbObjects;
        }

        /**
         * Liefert das {@link Placement} eines {@link Player}s, falls dieser in
         * der Area steht.
         * 
         * @param player
         *            der Spieler, dessen Position gesucht wird
         * @return das {@link Placement} des Spielers oder {@code null}, falls
         *         der Roboter des Spielers nicht in der Area liegt
         */
        public Placement getPlayerPlacement(Player player) {
            for (Entry<Point, Tile> entry : tiles.entrySet()) {
                Tile tile = entry.getValue();
                if (tile.getRobot() == player.getRobot()) {
                    return new PlacementUnit(entry.getKey(), tile.getRobot().getOrientation(), tile);
                }
            }
            return null;
        }

        /**
         * Liefert die Position der Flagge mit der gegebenen Nummer.
         * 
         * @param number
         *            die Nummer der gesuchten Flagge
         * @return die Position der Flagge oder {@code null}, falls die Flagge
         *         nicht in der Area liegt
         */
        public Placement getFlagPosition(int number) {
            for (Entry<Point, Tile> entry : tiles.entrySet()) {
                Tile tile = entry.getValue();
                FieldObject obj = tile.getFieldObject();
                if (obj == null || !(obj instanceof Flag)) {
                    continue;
                }
                Flag flag = (Flag) obj;
                if (flag != null && flag.getNumber() == number) {
                    return new PlacementUnit(entry.getKey(), null, tile);
                }
            }
            return null;
        }

        /**
         * Liefert benachbarte {@link LookupBoardArea}s.
         * 
         * @return benachbarte {@link LookupBoardArea}s
         */
        public Stream<LookupBoardArea> getNeighbors() {
            List<Point> points = new ArrayList<Point>();
            points.add(new Point(root.x - LOOKUP_AREA_LENGTH, root.y + LOOKUP_AREA_LENGTH));
            points.add(new Point(root.x - LOOKUP_AREA_LENGTH, root.y));
            points.add(new Point(root.x - LOOKUP_AREA_LENGTH, root.y - LOOKUP_AREA_LENGTH));
            points.add(new Point(root.x + LOOKUP_AREA_LENGTH, root.y + LOOKUP_AREA_LENGTH));
            points.add(new Point(root.x + LOOKUP_AREA_LENGTH, root.y));
            points.add(new Point(root.x + LOOKUP_AREA_LENGTH, root.y - LOOKUP_AREA_LENGTH));
            points.add(new Point(root.x, root.y + LOOKUP_AREA_LENGTH));
            points.add(new Point(root.x, root.y - LOOKUP_AREA_LENGTH));
            return points.stream().filter(pt -> ptToTile.containsKey(pt)).map(pt -> new LookupBoardArea(pt));
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + ((root == null) ? 0 : root.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            LookupBoardArea other = (LookupBoardArea) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;
            if (root == null) {
                if (other.root != null)
                    return false;
            } else if (!root.equals(other.root))
                return false;
            return true;
        }

        private VisualUnit getOuterType() {
            return VisualUnit.this;
        }
    }
}
