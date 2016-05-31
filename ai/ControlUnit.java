package de.uni_bremen.factroytrouble.ai;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.uni_bremen.factroytrouble.ai.behavior.DynamicBehaviour;
import de.uni_bremen.factroytrouble.ai.behavior.StaticBehaviour;
import de.uni_bremen.factroytrouble.ai.configreader.AgentConfigReader;
import de.uni_bremen.factroytrouble.ai.memory.Chunk;
import de.uni_bremen.factroytrouble.ai.memory.KeyUnit;
import de.uni_bremen.factroytrouble.ai.memory.MemoryUnit;
import de.uni_bremen.factroytrouble.ai.planning.CurrentPlannerOne;
import de.uni_bremen.factroytrouble.ai.planning.CurrentPlanning;
import de.uni_bremen.factroytrouble.ai.planning.FuturePlannerOne;
import de.uni_bremen.factroytrouble.ai.planning.FuturePlanning;
import de.uni_bremen.factroytrouble.ai.planning.PathPlanner;
import de.uni_bremen.factroytrouble.ai.planning.Plans;
import de.uni_bremen.factroytrouble.board.Board;
import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.exceptions.KeyNotFoundException;
import de.uni_bremen.factroytrouble.exceptions.KeyNotFoundRuntimeException;
import de.uni_bremen.factroytrouble.exceptions.PropertyFileNotFoundException;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.observer.GameObserver;
import de.uni_bremen.factroytrouble.player.MasterFactory;
import de.uni_bremen.factroytrouble.player.OptionCard;
import de.uni_bremen.factroytrouble.player.Player;
import de.uni_bremen.factroytrouble.player.ProgramCard;

/**
 * 1000 Commit
 * 
 * @author Frederik
 *
 */
public class ControlUnit implements Control {

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

    private static final Logger LOGGER = Logger.getLogger(ControlUnit.class);

    private MasterFactory masterFactory;

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
    private boolean isDone;

    private Robot ownRobot;
    private List<ProgramCard> cards;
    private List<GameObserver> observerlist;

    private int nextFlag;
    private int lookUpAreaLength;

    private AgentConfigReader config;

    private static final int AGENTNUMBER = 1;
    

    /**
     * Erzeugt eine ControlUnit die einen Roboter hat. Um den Agenten zu
     * initialisieren muss die Methode {@link ControlUnit#initialize(Board)}
     * aufgerufen werden.
     * 
     * @param robot
     *            der zu steuernde Roboter
     */
    public ControlUnit(Robot robot, MasterFactory masterFactory) {
        ownRobot = robot;
        this.masterFactory = masterFactory;
    }

    @Override
    public void initialize(Board board) {
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
        isDone = false;
        this.board = board;
        cards = new ArrayList<ProgramCard>();
        observerlist = new ArrayList<GameObserver>();
        List<Player> tmpPlayers = masterFactory.getMaster().getPlayers();
        players = createPlayerMap(tmpPlayers);
        ownID = createOwnID();
        timer = new TimerUnit();
        personality = new StaticBehaviour();
        emotions = new DynamicBehaviour();
        memory = new MemoryUnit();
        visual = new VisualUnit(memory, board, players, ownID, timer);
        pathPlanner = new PathPlanner(timer, this);
        logic = new LogicOne();
        future = new FuturePlannerOne(this, timer, personality);
        future.setController(this);
        current = new CurrentPlannerOne(this, timer, future, logic, pathPlanner);
        response = new ResponseUnit(ownRobot, this);
        masterFactory.getMaster().attachObserver(this);
    }

    /**
     * Erzeugt eine Map von Spieler IDs auf Spieler.
     * 
     * @param playerList
     *            Liste der Spieler vom GameMaster
     * @return Map von IDs auf Spieler
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
            if (players.get(i) == this) {
                tmpID = i;
            }
        }
        return tmpID;
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
    public void scan() {
        visual.scan();
    }

    @Override
    public void update() {
        visual.update();
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
    public void giveCards(List<ProgramCard> pCards) {
        cards = pCards;

    }

    @Override
    public List<ProgramCard> discardCards() {
        List<ProgramCard> tmpCards = cards;
        cards.clear();
        return tmpCards;

    }

    @Override
    public void finishTurn() {
        discardCards();

    }

    @Override
    public Robot getRobot() {
        return ownRobot;
    }

    @Override
    public List<ProgramCard> getPlayerCards() {
        return cards;
    }

    @Override
    public void attachObserver(GameObserver observer) {
        observerlist.add(observer);

    }

    @Override
    public void removeObserver(GameObserver observer) {
        observerlist.remove(observer);

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

    @Override
    public boolean isDone() {
        return isDone;
    }

    public String getKIName() {
        return name;
    }

    public void setDone(boolean isDone) {
        this.isDone = isDone;
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
            // ToDo: Visual braucht eine Methode
            return false;
        case REGISTERS:
            // ToDo: Sobald Robot einen Getter für die Register hat.
            break;
        default:
            return null;
        }
        return null;
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
        if (tmpChunk == null) {
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

    @Override
    public void executeTurn() {
        nextFlag = ownRobot.getFlagCounterStatus() + 1;
        future.startPlanning();
        current.planTurn();
        if (ownRobot.getRegisters().length != 5) {
            LOGGER.error("Es wurden nicht alle Register gefüllt! " + ownRobot.getRegisters());
            throw new RuntimeException("Es wurden nicht alle Register gefüllt!");
        }
    }

    @Override
    public void spam(Robot robot) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setController(Control controller) {
        // TODO Auto-generated method stub
    }

    @Override
    public ProgramCard fillRegister(int register, ProgramCard card) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void fillEmptyRegisters() {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeCardFromHand(ProgramCard card) {
        cards.remove(card);
    }

    public int getNextFlag() {
        return nextFlag;
    }
}
