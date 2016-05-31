/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ais;

import de.uni_bremen.factroytrouble.ai.AIPlayer;
import de.uni_bremen.factroytrouble.ai.ki1.*;
import de.uni_bremen.factroytrouble.ai.ki1.behavior.DynamicBehaviour;
import de.uni_bremen.factroytrouble.ai.ki1.behavior.StaticBehaviour;
import de.uni_bremen.factroytrouble.ai.ki1.configreader.AgentConfigReader;
import de.uni_bremen.factroytrouble.ai.ki1.memory.KeyUnit;
import de.uni_bremen.factroytrouble.ai.ki1.memory.MemoryUnit;
import de.uni_bremen.factroytrouble.ai.ki1.planning.CurrentPlannerOne;
import de.uni_bremen.factroytrouble.ai.ki1.planning.FuturePlannerOne;
import de.uni_bremen.factroytrouble.ai.ki1.planning.PathPlanner;
import de.uni_bremen.factroytrouble.ai.ki1.planning.TacticUnit.Tactics;
import de.uni_bremen.factroytrouble.api.ki1.*;
import de.uni_bremen.factroytrouble.api.ki1.memory.Chunk;
import de.uni_bremen.factroytrouble.api.ki1.planning.CurrentPlanning;
import de.uni_bremen.factroytrouble.api.ki1.planning.FuturePlanning;
import de.uni_bremen.factroytrouble.api.ki1.planning.Plans;
import de.uni_bremen.factroytrouble.board.Board;
import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.exceptions.KeyNotFoundException;
import de.uni_bremen.factroytrouble.exceptions.KeyNotFoundRuntimeException;
import de.uni_bremen.factroytrouble.exceptions.NotAllRegistersFilledException;
import de.uni_bremen.factroytrouble.exceptions.PropertyFileNotFoundException;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.observer.Event;
import de.uni_bremen.factroytrouble.player.MasterFactory;
import de.uni_bremen.factroytrouble.player.OptionCard;
import de.uni_bremen.factroytrouble.player.Player;
import de.uni_bremen.factroytrouble.player.ProgramCard;
import org.apache.log4j.Logger;

import java.awt.Point;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 1000 Commit
 *
 * @author Frederik
 *
 */
// TODO visual.setRound nutzen!!
public class AIPlayer1 extends AIBase implements Control, AIPlayer {

    private static final Logger LOGGER = Logger.getLogger(AIPlayer1.class);

    private Visual visual;
    private MemoryUnit memory;
    private PathPlanner pathPlanner;
    private FuturePlanning future;
    private CurrentPlanning current;
    private Logic logic;
    private StaticBehaviour personality;
    private DynamicBehaviour emotions;
    private Response response;

    private TimerUnit timer;

    private int ownID;
    private String name;
    private Map<Integer, Player> players;
    private Board board;

    private Robot ownRobot = player.getRobot();

    private List<ProgramCard> cards;

    private int nextFlag;
    private int lookUpAreaLength;

    private AgentConfigReader config;

    private boolean init;

    private static final int AGENTNUMBER = 1;

    private int round;

    /**
     * Erzeugt eine ControlUnit die einen Roboter hat. Um den Agenten zu
     * initialisieren muss die Methode {@link AIPlayer1(Board)} aufgerufen
     * werden.
     *
     * @param player
     *            die Spieler Instance
     */
    public AIPlayer1(Integer gameId, Player player) {
        super(gameId, player);
        init = false;
        round = 0;
    }

    @Override
    public void initialize() {
        try {
            config = AgentConfigReader.getInstance(AGENTNUMBER);
        } catch (IOException e) {
            LOGGER.error("agent_" + AGENTNUMBER + ".properties" + " existiert nicht! " + e);
            throw new PropertyFileNotFoundException();
        }
        try {
            name = config.getStringProperty("KI.Name");
        } catch (KeyNotFoundException e) {
            LOGGER.error("Der Key KI.Name existiert nicht in der agent_" + AGENTNUMBER + ".properties " + e);
            throw new KeyNotFoundRuntimeException("KI.Name");
        }
        try {
            lookUpAreaLength = config.getIntProperty("Visual.LookupAreaLength");
        } catch (KeyNotFoundException e) {
            LOGGER.error(
                    "Der Key Visual.LookupAreaLength existiert nicht in der agent_" + AGENTNUMBER + ".properties" + e);
            throw new KeyNotFoundRuntimeException("Visual.LookupAreaLength");
        }
        this.board = mFactory.getMaster(gameId).getBoard();
        List<Player> tmpPlayers = mFactory.getMaster(gameId).getPlayers();
        players = createPlayerMap(tmpPlayers);
        ownID = createOwnID();
        timer = new TimerUnit();
        personality = new StaticBehaviour(Integer.toString(AGENTNUMBER));
        emotions = new DynamicBehaviour(getRobotName(), this);
        memory = new MemoryUnit();
        visual = new VisualUnit(memory, board, players, ownID, timer);
        logic = new LogicOne();
        try {
            future = new FuturePlannerOne(this, timer, personality);
        } catch (IOException e) {
            LOGGER.error("FuturePlannerOne konnte nicht erzeugt werden", e);
            throw new RuntimeException("AIPlayer1 kann nicht ohne FuturePlanner funktionieren", e);
        }
        future.setController(this);
        pathPlanner = new PathPlanner(timer, this, future, logic);
        current = new CurrentPlannerOne(this, timer, future, logic, pathPlanner);
        pathPlanner.setCurrentPlanner(current);
        response = new ResponseUnit(ownRobot, this);
        mFactory.getMaster(gameId).attachObserver(this);
        init = true;
    }

    /**
     * Erzeugt eine Map von Spieler gameIds auf Spieler.
     *
     * @param playerList
     *            Liste der Spieler vom GameMaster
     * @return Map von gameIds auf Spieler
     */
    private Map<Integer, Player> createPlayerMap(List<Player> playerList) {
        Map<Integer, Player> tmpPlayers = new HashMap<Integer, Player>();
        for (int i = 0; i < playerList.size(); i++) {
            tmpPlayers.put(i, playerList.get(i));
        }
        return tmpPlayers;
    }

    /**
     * Sucht die ID des eigenen Spielers aus der Liste der Spieler
     *
     * @return Spieler ID
     */
    private int createOwnID() {
        int tmpID = 0;
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getRobot().getName().equals(player.getRobot().getName())) {
                tmpID = i;
            }
        }
        return tmpID;
    }

    public int getGameId() {
        return gameId;
    }

    @Override
    public Placement getPlayerPlacement(int id) {
        return visual.getPlayerPlacement(id);
    }

    @Override
    public Placement getFlagPosition(int number) {
        return visual.getFlagPosition(number);
    }

    @Override
    public BoardArea getArea(Point center, int radius) {
        return visual.getArea(center, radius);
    }

    @Override
    public void startPlanning() {
        future.startPlanning();
    }

    @Override
    public Plans getCurrentPlan() {
        return future.getCurrentPlan();
    }

    @Override
    public List<ProgramCard> planTurn() {
        return current.planTurn();
    }

    @Override
    public void setCards(List<ProgramCard> cards) {
        response.setCards(cards);
    }

    @Override
    public int getAgentNumber() {
        return AGENTNUMBER;
    }

    @Override
    public int getHP(int playerId) {
        return visual.getHP(playerId);
    }

    @Override
    public int getLives(int playerId) {
        return visual.getLives(playerId);
    }

    @Override
    public boolean isPoweredDown(int playerId) {
        return visual.isPoweredDown(playerId);
    }

    @Override
    public Placement getRespawnPoint(int playerId) {
        return visual.getRespawnPoint(playerId);
    }

    @Override
    public List<ProgramCard> getLockedCards(int playerId) {
        return visual.getLockedCards(playerId);
    }

    @Override
    public List<OptionCard> getOptionCards(int playerId) {
        return visual.getOptionCards(playerId);
    }

    @Override
    public ProgramCard getCard(int number) {
        return visual.getCard(number);
    }

    public String getKIName() {
        return name;
    }

    public Board getBoard() {
        return board;
    }

    @Override
    public Object requestData(RequestType request, Object otherRequest) {
        if (request == null && otherRequest == null) {
            return null;
        }
        if (request != null) {
            return getRequestTypeData(request);
        }
        if (otherRequest != null) {
            return getOtherRequestData(otherRequest);
        }
        return null;

    }

    private Object getRequestTypeData(RequestType request) {
        switch (request) {
        case CURRENTTILE:
            return getCurrentTileFromMemory();
        case NEXTFLAG:
            return getNextFlagFromMemory();
        case HP:
            return getHPFromMemory();
        case LP:
            return getLPFromMemory();
        case ORIENTATION:
            return getOrientationFromMemory();
        case RESPAWNPOINT:
            return getRespawnPointFromMemory();
        case POWERDOWN:
            throw new UnsupportedOperationException("ToDo: Visual braucht eine Methode");
        case REGISTERS:
            throw new UnsupportedOperationException("ToDo: Sobald Robot einen Getter für die Register hat.");
        case TACTICPOWER:
            return getTacticPower();
        case TACTIC:
            return getTactic();
        default:
            return null;
        }
    }

    /**
     * Returned die stärke der anzuwendenden Taktic aus dem Currentplanner
     * 
     * @return Integer der di stärke der Taktik repräsentiert
     */
    public int getTacticPower() {
        return current.getTacticPower();
    }

    /**
     * Gibt die aktuelle Taktik des Currentplanners zurück
     * 
     * @return siehe oben
     */
    public Tactics getTactic() {
        return current.getTactic();
    }

    /**
     * Sucht in der Memory nach der Position des eigenen Roboters. Ist er nicht
     * enthalten wird die Visual nach der Position gefragt.
     *
     * @return Tile auf dem der Spielr steht
     */
    private Tile getCurrentTileFromMemory() {
        Chunk tmpChunk;
        tmpChunk = memory.retrieveChunk(new KeyUnit(String.format(VisualUnit.PLAYER_POS_KEY, ownID)));
        if (tmpChunk == null) {
            Placement tmpPlacement = visual.getPlayerPlacement(ownID);
            if (tmpPlacement == null) {
                return null;
            }
            return tmpPlacement.getTile();
        }
        return ((PlacementUnit) tmpChunk.getData()).getTile();
    }

    /**
     * Sucht in der Memory nach der Position der nächsten Flagge. Ist diese
     * nicht enthalten wird die Visual nach der Position gefragt.
     *
     * @return Tile auf dem die Nächste Flagge steht
     */
    private Tile getNextFlagFromMemory() {
        Chunk tmpChunk;
        tmpChunk = memory.retrieveChunk(new KeyUnit(String.format(VisualUnit.FLAG_POS_KEY, nextFlag)));
        if (tmpChunk == null || tmpChunk.getData() == null) {
            Placement tmpPlacement = visual.getFlagPosition(nextFlag);
            if (tmpPlacement == null) {
                return null;
            }
            return tmpPlacement.getTile();
        }
        return ((PlacementUnit) tmpChunk.getData()).getTile();
    }

    /**
     * Sucht in der Memory nach den HP des eigenen Roboters. Ist diese nicht
     * enthalten wird die Visual nach den HP gefragt.
     *
     * @return Integer
     */
    private Integer getHPFromMemory() {
        Chunk tmpChunk;
        tmpChunk = memory.retrieveChunk(new KeyUnit(String.format(VisualUnit.HP_KEY, ownID)));
        if (tmpChunk == null) {
            return visual.getHP(ownID);
        }
        return (Integer) tmpChunk.getData();
    }

    /**
     * Sucht in der Memory nach den LP des eigenen Roboters. Ist diese nicht
     * enthalten wird die Visual nach den LP gefragt.
     *
     * @return Integer
     */
    private Integer getLPFromMemory() {
        Chunk tmpChunk;
        tmpChunk = memory.retrieveChunk(new KeyUnit(String.format(VisualUnit.LIVES_KEY, ownID)));
        if (tmpChunk == null) {
            return visual.getLives(ownID);
        }
        return (Integer) tmpChunk.getData();
    }

    /**
     * Sucht in der Memory nach der Orientierung des eigenen Roboters. Ist diese
     * nicht enthalten wird die Visual nach der Orientierung gefragt.
     *
     * @return Orientation des eigenen Roboters
     */
    private Orientation getOrientationFromMemory() {
        Chunk tmpChunk;
        tmpChunk = memory.retrieveChunk(new KeyUnit(String.format(VisualUnit.PLAYER_POS_KEY, ownID)));
        if (tmpChunk == null) {
            Placement tmpPlacement = visual.getPlayerPlacement(ownID);
            if (tmpPlacement == null) {
                return null;
            }
            return tmpPlacement.getOrientation();
        }
        return ((PlacementUnit) tmpChunk.getData()).getOrientation();
    }

    /**
     * Sucht in der Memory nach der Position des Respawnpoints des eigenen
     * Roboters. Ist diese nicht enthalten wird die Visual nach der Posiotion
     * gefragt.
     *
     * @return Placement des Respawnpoints
     */
    private Placement getRespawnPointFromMemory() {
        Chunk tmpChunk;
        tmpChunk = memory.retrieveChunk(new KeyUnit(String.format(VisualUnit.RESPAWN_KEY, ownID)));
        if (tmpChunk == null) {
            return visual.getRespawnPoint(ownID);
        }
        return (Placement) tmpChunk.getData();
    }

    private Object getOtherRequestData(Object otherRequest) {
        if (otherRequest instanceof Integer) {
            return getDataByInteger((Integer) otherRequest);
        } else if (otherRequest instanceof Point) {
            return getDataByPoint((Point) otherRequest);
        }
        return null;
    }

    private Object getDataByInteger(Integer i) {
        Chunk tmpChunk;
        tmpChunk = memory.retrieveChunk(new KeyUnit(String.format(VisualUnit.CARD_KEY, i)));
        if (tmpChunk == null) {
            return visual.getCard(i);
        }
        return (ProgramCard) tmpChunk.getData();
    }

    private Object getDataByPoint(Point point) {
        Chunk tmpChunk;
        tmpChunk = memory.retrieveChunk(new KeyUnit(String.format(VisualUnit.BOARDAREA_KEY, point.x, point.y)));
        if (tmpChunk == null) {
            BoardArea tmpArea = visual.getArea(point, lookUpAreaLength);
            if (tmpArea == null) {
                return null;
            }
            return tmpArea.getTiles().get(point);
        }
        return ((BoardArea) tmpChunk.getData()).getTiles().get(point);

    }

    @Override
    public void setPotentialReachedGoal(GoalType reachedGoal) {
        switch (reachedGoal) {
        case FLAG:
            nextFlag++;
            break;

        default:
            break;
        }

    }

    public Map<Integer, Placement> getAllPlayerPlacement() {
        Map<Integer, Placement> playerPlacement = new HashMap<Integer, Placement>();
        for (Entry<Integer, Player> e : players.entrySet()) {
            if (e.getKey() != (Integer) ownID) {
                playerPlacement.put((Integer) e.getKey(), visual.getPlayerPlacement((int) e.getKey()));
            }
        }
        return playerPlacement;

    }

    @Override
    public void executeTurn() {
        if (!init) {
            initialize();
        }
        round++;
        nextFlag = player.getRobot().getFlagCounterStatus() + 1;
        future.startPlanning();
        current.planTurn();
        for (int i = 0; i < ownRobot.getRegisters().length; i++) {
            if (ownRobot.getRegisters()[i] == null) {
                LOGGER.error("Es wurden für " + ownRobot.getName() + " nicht alle Register gefüllt! "
                        + ownRobot.getRegisters());
                throw new NotAllRegistersFilledException(ownRobot.getName());
            }
        }
        finishTurn();
    }

    @Override
    public void terminateTurn() {

    }

    @Override
    public void spam(Robot robot, Event e, Object source) {
        LOGGER.info("robot: " + robot);
        LOGGER.info("Event e: " + e);
        LOGGER.info("Event source: " + source);
        visual.spam(robot, e, source);
    }

    @Override
    public void setController(Control controller) {
        throw new UnsupportedOperationException();
    }

    public void removeCardFromHand(ProgramCard card) {
        cards.remove(card);
    }

    public int getNextFlag() {
        return nextFlag;
    }

    public void setVisual(Visual visual) {
        this.visual = visual;
    }

    public void setFuture(FuturePlanning future) {
        this.future = future;
    }

    public void setCurrent(CurrentPlanning current) {
        this.current = current;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public MasterFactory getMasterFactory() {
        return mFactory;
    }

    public void setMasterFactory(MasterFactory masterFactory) {
        this.mFactory = masterFactory;
    }

    public void setInit(boolean init) {
        this.init = init;
    }

    @Override
    public Tile getGoalTile() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getRelPosStraight(Tile t1, Tile t2) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getRelPosDiag(Tile t1, Tile t2, int ori) {
        // TODO Auto-generated method stub
        return 0;
    }

    public int getRound() {
        return round;
    }

    @Override
    public String getRobotName() {
        return getRobot().getName();
    }

    @Override
    public StaticBehaviour getStaticBehaviour() {
        return personality;
    }

    @Override
    public DynamicBehaviour getDynamicBehaviour() {
        return emotions;
    }

    @Override
    public int getOwnId() {
        return ownID;
    }
}
