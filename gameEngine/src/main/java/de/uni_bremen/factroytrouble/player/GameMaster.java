/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.player;

import de.uni_bremen.factroytrouble.ai.AIPlayer;
import de.uni_bremen.factroytrouble.board.Board;
import de.uni_bremen.factroytrouble.board.Dock;
import de.uni_bremen.factroytrouble.board.Field;
import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.gameobjects.*;
import de.uni_bremen.factroytrouble.observer.Event;
import de.uni_bremen.factroytrouble.observer.GameObserver;
import org.apache.log4j.Logger;

import java.awt.Point;
import java.util.*;

/**
 * 
 * @author Thore, Stefan
 *
 */
public class GameMaster implements Master,GameObserver {

    private static final Logger LOGGER = Logger.getLogger(GameMaster.class);
    private final int gameId;

    private List<GameObserver> dieSpammer = new LinkedList<>();

    private List<Player> players;
    private List<Robot> robots;

    private Board board;
    // speichert die aktuelle Countdownzahl.
    private int tick;

    private Deck deck;
    private List<ProgramCard> dealtCards;

    private int lastFlag;
    private Player winner;

    private Map<Robot, Boolean> changeRequests = new HashMap<>();

    private final BoardManager boardManager;

    public GameMaster(int gameId, BoardManager boardManager) {
        this.gameId = gameId;
        this.boardManager = boardManager;
        init();
    }

    /*
     * @author Thore, Stefan (Ergänzungen bzgl. KI)
     */
    @Override
    public double countdown() {
        List<Player> notFinished = notFinished();
        
        if(notFinished == null)
            return -1;
        
        if(notFinished.size() == 1 && !(notFinished.get(0) instanceof AIPlayer)){
            LOGGER.info("Countdown für menschliche Spieler wird noch nicht unterstützt!");
            return -1;
        }
        
        if(notFinished.isEmpty())
            return 0;
        
        String msg = "Countdown startet, warte auf ";
        for(int ii = 0; ii < notFinished.size(); ii++){
            msg = msg.concat(notFinished.toString());
            if(ii < notFinished.size()-1){
                msg = msg.concat(",");
            }
        }
        LOGGER.info(msg.concat("."));
        
        double timer = TIMER;
        Calendar now = Calendar.getInstance();
        Calendar thirtySecLater = Calendar.getInstance();
        thirtySecLater.add(Calendar.SECOND, (int) timer);
        LOGGER.info("Time now: " + now.getTimeInMillis());
        tick = 0;
        while (now.getTimeInMillis() < thirtySecLater.getTimeInMillis()) {
            for(int ii = 0; ii < notFinished.size(); ii ++){
                if(notFinished.get(ii).isDone())
                    notFinished.remove(ii);
            }
            if(notFinished.isEmpty()){
                return tick;
            }
            
            now = Calendar.getInstance();
            timer = (thirtySecLater.getTimeInMillis() - now.getTimeInMillis()) / 1000;
            LOGGER.info("Countdown Tick: " + tick);
            tick++;
            try {
                Thread.sleep(999);
            } catch (InterruptedException e) {
                LOGGER.error(e);
            }
        }
        LOGGER.info("Countdown over!");
        
        for(int ii = 0; ii < notFinished.size(); ii++){
            try{
                Player notReady = notFinished.get(ii);
                if(notReady instanceof AIPlayer){
                    ((AIPlayer)notReady).terminateTurn();
                }
                notReady.fillEmptyRegisters();
                notReady.finishTurn();
            }catch(UnsupportedOperationException e){
                LOGGER.error("fillEmptyRegisters von ".concat(notFinished.get(ii).toString()).concat(" nicht unterstützt!"));
            }
        }
        
        return timer;
    }
    
    /**
     * @author ToFy
     * 
     *         Wird von GUI nach Mitteilung von dessen Observer abgefragt, um
     *         die aktuelle Countdownzahl abzufragen.
     * 
     * @return den aktuellen Tick des Countdown
     */
    @Override
    public int getCountdownTick() {
        return tick;
    }

    /**
     * @author Thore
     * 
     *         Hilfsmethode fuer countdown um den Letzten Spieler zu bestimmen.
     * 
     * @return Der Letzte Spieler der nicht "done" angesagt hat. {@code null}
     *         wenn mehr oder weniger als ein Spieler ready sind.
     */
    @Deprecated
    private Player lastPlayer() {
        List<Player> lasts = players;
        Player last = null;
        for (Player p : players) {
            if (p.isDone()) {
                lasts.remove(p);
            }
        }
        if (lasts.size() == 1) {
            last = lasts.get(0);
        }
        return last;
    }

    /**
     * @author Stefan
     */
    @Override
    public void init() {
        players = new ArrayList<Player>();
        robots = new ArrayList<Robot>();
        deck = new Deck(gameId);
        dealtCards = new ArrayList<>();// new
        board = null;
        lastFlag = 0;
        winner = null;
        boardManager.init();
        changeRequests = new HashMap<>();
    }

    @Override
    public boolean initialiseBoard(String board) {
        if (board == null || !boardManager.getAvailableBoards().keySet().contains(board)) {
            LOGGER.error("Gewähltes Spielbrett nicht vorhanden");
            return false;
        }
        // Board bauen und setzen
        LOGGER.info("Initialisiere GameBoard ".concat(board));
        this.board = boardManager.buildBoard(board);
        putRobotsOnBoard(board);

        lastFlag = this.board.getHighestFlagNumber();

        robots.forEach(robot -> changeRequests.put(robot, false));

        return true;
    }

    @Override
    public Board getBoard() {
        return board;
    }

    @Override
    public List<String> getAvailableBoards() {
        List<String> boardList = new ArrayList<>();
        boardList.addAll(boardManager.getAvailableBoards().keySet());
        boardList.sort(null);

        return boardList;
    }

    private void putRobotsOnBoard(String board) {
        Point[] allStartPoints = boardManager.getStartPositions(board);
        List<Integer> startPointIndexes = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            startPointIndexes.add(i);
        }
        Collections.shuffle(startPointIndexes);
        Dock dock = findDock();
        Collection<Field> allFields = this.board.getFields().values();
        Field[] fields = new Field[allFields.size() - 1];

        if (dock == null) {
            LOGGER.error("Es befindet sich kein Dock auf dem Feld!");
            return;
        }

        int i = 0;
        for (Field field : allFields) {
            if (field instanceof Dock) {
                continue;
            }
            fields[i] = field;
            i++;
        }

        if (robots.size() > allStartPoints.length) {
            LOGGER.error("Es gibt mehr Roboter (robots.size() = " + robots.size() + ") als"
                    + " Startpunkte (allStartPoints.size() = " + allStartPoints.length + ")!");
            return;
        }

        if (robots.size() != players.size()) {
            LOGGER.error("Roboter- und Spieleranzahl unterscheiden sich (Spieler: " + players.size() + " , Roboter: "
                    + robots.size() + " !");
            return;
        }

        for (int ii = 0; ii < robots.size(); ii++) {
            Robot robot = robots.get(ii);
            if (robot.getCurrentTile() != null) {
                robot.getCurrentTile().setRobot(null);
            }
            Point startPoint = allStartPoints[startPointIndexes.get(ii)];
            Tile startTile = dock.getTiles().get(startPoint);
            startTile.setRobot(robot);
            robot.setCurrentTile(startTile);
            robot.setRespawnPoint();
        }
        this.board.buildBoard(dock, fields);
    }

    private Dock findDock() {
        Collection<Field> fields = this.board.getFields().values();
        for (Field field : fields) {
            if (field instanceof Dock) {
                return (Dock) field;
            }
        }
        return null;
    }

    /**
     * @author Thore
     * 
     *         Füegt einen Spieler zum Spiel hinzu. Überprüft ob die
     *         Maximalanzahl an Spielern überschritten wurde.
     * 
     * @param player
     *            , der zu hinzufügende Spieler
     * @return {@code false}, falls die Maximalanzahl an Spielern überschritten
     *         wurde, oder Spieler bereits im Spiel ist, sonst {@code true}.
     */
    @Override
    public boolean addPlayer(Player player) {
        if (players.size() >= MAX_PLAYERS) {
            LOGGER.error("Maximal Anzahl an Spielern schon erreicht.");
            return false;
        }
        if (players.contains(player)) {
            LOGGER.error("Spieler " + player.toString() + " ist bereits im Spiel.");
            return false;
        }
        players.add(player);
        robots.add(player.getRobot());
        LOGGER.info("Player hinzugefügt: " + player.toString());
        return true;
    }

    @Override
    public List<Player> getPlayers() {
        return players;
    }

    @Override
    public Player getWinner() {
        findWinner(true);
        return winner;
    }

    @Override
    public boolean isDraw() {
        return !findWinner(true);
    }

    @Override
    public int getFlagCount(String board) {
        return boardManager.getFlagCount(board);
    }

    /*
     * @author Thore
     */
    @Override
    public boolean canMove(Robot robot, boolean forward) {
        Orientation direction = robot.getOrientation();
        direction = forward ? direction : Orientation.getOppositeDirection(direction);
        Tile currentTile = robot.getCurrentTile();
        Tile futureTile = board.findNextTile(currentTile, direction);
        if (currentTile.hasWall(direction) || futureTile.hasWall(Orientation.getOppositeDirection(direction))) {
            return false;
        }
        Robot futureRobot = futureTile.getRobot();
        return !(futureRobot != null && !canPushOtherRobot(robot, futureRobot));
    }

    /*
     * @author Thore
     */
    @Override
    public boolean canPushOtherRobot(Robot robot1, Robot robot2) {
        Tile posiOne = robot1.getCurrentTile();
        Tile posiTwo = robot2.getCurrentTile();
        Orientation pushDirection = orientationToTile(posiOne, posiTwo);
        Tile robo2FuturePosi = board.findNextTile(posiTwo, pushDirection);
        if (!tileNextToTile(posiOne, posiTwo)) {
            return false;
        }
        if (!robot1.getOrientation().equals(pushDirection)
                && board.findNextTile(posiOne, Orientation.getOppositeDirection(pushDirection)).getRobot() == null) {
            return false;
        }
        if (posiOne.hasWall(pushDirection) || posiTwo.hasWall(pushDirection)
                || posiTwo.hasWall(Orientation.getOppositeDirection(pushDirection))
                || robo2FuturePosi.hasWall(Orientation.getOppositeDirection(pushDirection))) {
            return false;
        }
        Robot robo3 = robo2FuturePosi.getRobot();
        if (robo3 != null) {
            return canPushOtherRobot(robot2, robo3);
        }
        return true;
    }

    /**
     * @author Thore
     * 
     *         Hilfsmethode, die eine Orientation zurückgibt, abhängig davon, in
     *         welche Richtung Tile zwei von Tile eins liegt. Wenn sie nicht
     *         benachbart sind (tileNextToTile {@code false}) wird {@code null}
     *         zurückgegeben.
     * 
     * @param one
     *            eins
     * 
     * @param one
     *            zwei
     * 
     * @return Die Richtung als Orientation oder {@code null}
     */
    private Orientation orientationToTile(Tile one, Tile two) {
        if (!tileNextToTile(one, two)) {
            return null;
        }
        Point posiOne = one.getCoordinates();
        Point posiTwo = two.getCoordinates();
        if (posiTwo.getX() > posiOne.getX()) {
            return Orientation.EAST;
        }
        if (posiTwo.getX() < posiOne.getX()) {
            return Orientation.WEST;
        }
        if (posiTwo.getY() > posiOne.getY()) {
            return Orientation.NORTH;
        }
        if (posiTwo.getY() < posiOne.getY()) {
            return Orientation.SOUTH;
        }
        return null;
    }

    /*
     * @author Thore
     * 
     * Hilfsmethode, die überprüft ob die Kachel one, Kachel two berührt.
     */
    private boolean tileNextToTile(Tile one, Tile two) {

        for (int i = 0; i < 4; i++) {
            Tile nextTile = board.findNextTile(one, Orientation.values()[i]);
            if (nextTile == two) {
                return true;
            }
        }

        return false;
    }

    /*
     * @author Thore
     */
    @Override
    public void activateBoard() {
        if(countdown() < 0){
            return;
        }
            
        LOGGER.info("Bewegungsphase startet jetzt");
        for (int i = 0; i < 5; i++) {
            LOGGER.info("Registerphase " + (i + 1) + " wird eingeleitet");
            board.executeNextRegisters();
            LOGGER.info("Expresslaufbänder werden bewegt");
            board.moveConveyors(true);
            LOGGER.info("Laufbänder werden bewegt");
            board.moveConveyors(false);
            LOGGER.info("Pusher werden aktiviert");
            board.activatePushers(i);
            LOGGER.info("Zahnräder werden bewegt");
            board.rotateGears();
            LOGGER.info("Laser werden gefeuert");
            board.fireLasers();
            LOGGER.info("Flaggen und Werkstätten werden berührt");
            board.touchCheckpointsAndFlags();
            if (winner == null) {
                LOGGER.info("Gewinner wird gesucht");
                findWinner(false);
            } else {
                LOGGER.warn("Gewinner steht bereits fest: " + winner.getRobot().getName());
                continue;
            }
            if (winner != null) {
                LOGGER.info("Gewinner steht fest: " + winner.getRobot().getName());
            }
        }
        findWinner(true);
        for (Robot r : robots) {
            stripRobotFromCards(r);
        }
        LOGGER.info("PowerDown-stati werden nach Spielerwunsch geändert");
        players.forEach(player -> {
            stripPlayerFromCards(player);
            powerDownChange(player.getRobot());
        });
        checkDeck();
    }

    @Override
    public void dealCardsToPlayers() {
        for (Player player : players) {
            Robot robby = player.getRobot();
            if(robby.isPoweredDown()){
                robby.healFully();
                robby.emptyAllRegister().forEach(card -> deck.layOffCard(card));
                changeRequests.put(robby, true); //Hotfix: PD für max eine Runde!
            }else if (!robby.isPoweredDown() && robby.getLives() > 0) {
                int damageOnRobot = 10 - robby.getHP();
                int cardsToDeal = 9 - damageOnRobot;
                List<ProgramCard> cards = new ArrayList<ProgramCard>();
                for (int i = 0; i < cardsToDeal; i++) {
                    cards.add(dealCard());
                }
                player.giveCards(cards);
            }
        }
    }

    private void stripPlayerFromCards(Player player) {
        List<ProgramCard> cards = player.discardCards();
        cards.forEach(card -> layOffCard(card));
    }

    private void stripRobotFromCards(Robot robot) {
        List<ProgramCard> cards = robot.emptyAllRegister();
        cards.forEach(card -> layOffCard(card));
    }

    private boolean findWinner(boolean checkForLastAlive) {
        if (winner != null) {
            return true;
        }
        List<Player> alive = new ArrayList<>();
        for (Player player : players) {
            Robot robot = player.getRobot();
            if (robot.getLives() <= 0) {
                continue;
            }
            if (robot.getFlagCounterStatus() == lastFlag) {
                winner = player;
                return true;
            }
            alive.add(player);
        }
        if (checkForLastAlive && (alive.size() == 1)) {
            winner = alive.get(0);
        }
        return !alive.isEmpty();
    }

    @Override
    public List<Robot> cleanup() {
        LOGGER.info("Aufräumphase startet jetzt");
        List<Robot> deadRobots = new ArrayList<>();
        robots.forEach(robot -> {
            Tile tile = robot.getCurrentTile();
            if (tile == null) {
                if (robot.getLives() > 0) {
                    board.respawnRobot(robot);
                } else {
                    deadRobots.add(robot);
                }
                return;
            }
            FieldObject object = tile.getFieldObject();
            if ((object instanceof Workshop) || (object instanceof Flag)) {
                robot.heal();
            }
        });

        this.robots.removeAll(deadRobots);

        return deadRobots;
    }

    @Override
    public ProgramCard dealCard() {
        ProgramCard dealt = deck.dealCard();
        dealtCards.add(dealt);
        return dealt;
    }

    @Override
    public void layOffCard(ProgramCard card) {
        dealtCards.remove(card);
        deck.layOffCard(card);
    }

    @Override
    public void attachObserver(GameObserver observer) {
        dieSpammer.add(observer);
        board.attachObserver(observer);
        players.forEach(player -> player.getRobot().attachObserver(observer));
    }

    @Override
    public void removeObserver(GameObserver observer) {
        dieSpammer.remove(observer);
        board.removeObserver(observer);
        players.forEach(player -> player.getRobot().removeObserver(observer));
    }

    @Override
    public List<GameObserver> getObserverList() {
        return dieSpammer;
    }

    @Override
    public void removeAllPlayers() {
        players = new ArrayList<>();
        robots = new ArrayList<>();
        LOGGER.info("Alle registrierten Spieler entfernt");
    }

    @Override
    public boolean requestPowerDownStatusChange(Robot robot) {
        if (changeRequests.keySet().contains(robot)) {
            changeRequests.put(robot, true);
            return true;
        }
        return false;
    }

    private void powerDownChange(Robot robot) {
        if (changeRequests.get(robot)) {
            boolean poweredDown = robot.isPoweredDown();
            if (poweredDown) {
                robot.wakeUp();
            } else {
                robot.powerDown();
            }
            changeRequests.put(robot, false); //Hotfix: PD für max eine Runde!
        }
    }

    /*
     * Die Methode überprüft, ob am Ende einer Runde noch Karten vorhanden sind, die nicht in einem gelockten
     * Register stecken.
     */
    private void checkDeck() {
        for (Robot rob : robots) {
            for (int ii = 0; ii < rob.registerLockStatus().length; ii++) {
                if (rob.registerLockStatus()[ii]) {
                    dealtCards.remove(rob.getRegisters()[ii]);
                }
            }
        }
        if (!dealtCards.isEmpty()) {
            String forgottenCards = "";
            for(ProgramCard card : dealtCards){
                forgottenCards += card.getPriority() + "; ";
            }
            LOGGER.error("Es wurden folgende Karten regelwidrig nicht zurückgegeben:" + forgottenCards);
        }
    }
    
    /*
     * @author Stefan
     * 
     * Die Methode prüft ob menschliche oder KI-Spieler noch nicht fertig sind.
     * Haben alle Spieler bis auf einen menschlichen Spieler ihre Runde beendet, so wird
     * dieser zurückgegeben.
     * Haben mehrere Ki-Spieler ihre Runde nicht beendet, so werden diese zurückgegeben.
     * Alle anderen Fälle stellen Fehlerfälle dar wie beispielsweise mehrere KI-Spieler und 
     * ein menschlicher Spieler haben ihre Runde noch nicht beendet. In diesem Fall wird null
     * zurück gegeben.
     */
    private List<Player> notFinished(){
        List<Player> aiNotFinished = new ArrayList<>();
        List<Player> humanNotFinished = new ArrayList<>();
        
        for(int ii = 0; ii < players.size(); ii++){
            Player player = players.get(ii); 
            if(!player.isDone()){
                if(player instanceof AIPlayer){
                    aiNotFinished.add(player);
                }else{
                    humanNotFinished.add(player);
                }
                    
            }
        }
        
        if(humanNotFinished.size() == 1 && aiNotFinished.isEmpty())
            return humanNotFinished;
        if(humanNotFinished.isEmpty() && !aiNotFinished.isEmpty())
            return aiNotFinished;
        if(humanNotFinished.isEmpty() && aiNotFinished.isEmpty())
            return new ArrayList<Player>();
        
        LOGGER.error("Noch nicht fertige KI-Agenten: ".concat(Integer.toString(aiNotFinished.size())
                .concat(". Noch nicht fertige menschl. Spieler: ").concat(Integer.toString(humanNotFinished.size()).concat("."))));
        
        return null;
        
    }

    @Override
    public void spam(Robot robot, Event event, Object source) {
        
    }

}
