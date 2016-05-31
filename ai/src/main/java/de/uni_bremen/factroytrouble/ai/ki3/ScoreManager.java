/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki3;

import de.uni_bremen.factroytrouble.ai.ais.ActRAIPlayer;
import de.uni_bremen.factroytrouble.board.Board;
import de.uni_bremen.factroytrouble.board.Dock;
import de.uni_bremen.factroytrouble.board.Field;
import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.gameobjects.*;
import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.observer.Event;
import de.uni_bremen.factroytrouble.observer.GameObserver;
import de.uni_bremen.factroytrouble.player.*;
import de.uni_bremen.factroytrouble.spring.SpringConfigHolder;
import org.apache.log4j.Logger;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Die Klasse kodiert den Zustand des Boards, der Roboter und die Handkarten des ActRAIPlayer,
 * welche dieser jede Runde erhält. Über die entsprechenden getter können die Codes dann
 * angerufen und weiterverarbeitet werden.
 *
 * @author Stefan
 */
public class ScoreManager implements GameObserver {
    private static final Logger LOGGER = Logger.getLogger(ScoreManager.class);
    private final int gameId;

    private Master gameMaster;
    private Board board;
    private ActRAIPlayer actrPlayer; 

    private String[][] boardState;
    private List<String> handState;
    private Map<Robot, String> robotsState;
    private Map<Integer,Point> flagState;

    private List<Tile> lasers;
    int maxAbsX = 0;
    int maxAbsY = 0;

    MasterFactory mFactory;

    public ScoreManager(int gameId, ActRAIPlayer actrPlayer) {
        this.gameId = gameId;
        mFactory = SpringConfigHolder.getInstance().getContext().getBean(GameMasterFactory.class);
        this.actrPlayer = actrPlayer; 
        init();
    }
    
    //Konstruktor für Tests!
    public ScoreManager(MasterFactory factory, ActRAIPlayer actrPlayer){
        this.gameId = 0;
        this.actrPlayer = actrPlayer;
        this.mFactory = factory;
        init();
    }

    /**
     * Initialisiert den ScoreManager
     */
    public void init() {
        gameMaster = mFactory.getMaster(gameId);
        board = gameMaster.getBoard();

        if (board == null) {
            LOGGER.error("Auf dem GameMaster wurde noch kein GameBoard initialisiert!");
            return;
        }

        List<Tile> tiles = new ArrayList<>();
        lasers = new ArrayList<>();
        robotsState = new HashMap<>();
        flagState = new HashMap<>();

        for (Field field : board.getFields().values()) {
            //t-o-d-o minX auch möglich!
            Point check = board.getAbsoluteCoordinates(field.getTiles().get(new Point(Field.DIMENSION - 1, 0))); 
            if (check.x > maxAbsX) {
                maxAbsX = check.x;
            }
            check = (field instanceof Dock)
                    ? board.getAbsoluteCoordinates(field.getTiles().get(new Point(0, Dock.SHORT_SIDE - 1)))
                    : board.getAbsoluteCoordinates(field.getTiles().get(new Point(0, Field.DIMENSION - 1)));
            if (check.y > maxAbsY) {
                maxAbsY = check.y;
            }
            tiles.addAll(field.getTiles().values());
        }
        
        boardState = new String[maxAbsX + 1][maxAbsY + 1];
        
        encodeInitialState(tiles);
    }
    
    /**
     * @return Spielfeldzustand.
     */
    public String[][] getBoardStateAsArray() {
        return boardState;
    }
    
    /**
     * @return Kodierter Spielfeldzustand.
     */
    public String getBoardState() {
        String out = "\"";
        for (int yy = 0; yy <= maxAbsY; yy++) {
            out += boardState[0][yy];
            for (int xx = 1; xx < boardState.length; xx++)
                out += "," + boardState[xx][yy];
            out += ";";
        }
        return out + "\"";
    }

    /**
     * @return Kodierter Zustand aller vorhandenen Roboter als Map.
     */
    public Map<Robot, String> getRobotsStateAsMap() {
        return robotsState;
    }
    
    /**
     * @return Map mit Positionen der Flaggen.
     */
    public Map<Integer,Point> getFlagState(){
        return flagState;
    }
    
    /**
     * @return Kodierter Zustand aller vorhandenen Roboter.
     */
    public String getRobotsState() {
        if(robotsState.size() == 0){
            gameMaster.attachObserver(this);
            for(Player player:gameMaster.getPlayers())
                initRobotsState(player);
        }
        String out = "\"";
        for (Robot robot : robotsState.keySet()) {
            out += robotsState.get(robot);
        }
        return out + "\"";
    }

    /**
     * @return Kodierte Handkarten des ActRAIPlayer.
     */
    public String getHandState() {
        encodeHand(actrPlayer.getPlayerCards());
        String out = "\"";
        for (int ii = 0; ii < handState.size(); ii++)
            out += handState.get(ii);
        return out + ";\"";
    }

    @Override
    public void spam(Robot robot, Event event, Object source) {
        for(Player player : gameMaster.getPlayers()){
            if(player.getRobot() == robot){
                encodeRobot(player,event);
                break;
            }
        }
    }

    /*
     * Die Methode kodiert das gesamte Board unter Verwendung diverser
     * Hilsmethoden. Pro Board ist encodeState nur einmal aufzurufen, denn die
     * Änderungen pro Spielzug sind minimal.
     */
    private void encodeInitialState(List<Tile> tiles) {
        FieldObject obj;
        String code;

        for (Tile tt : tiles) {
            obj = tt.getFieldObject();
            if (obj instanceof ConveyorBelt) {
                String conv = (((ConveyorBelt) obj).isExpress()) ? "2" : "1";
                code = conv + encodeOrientation(((ConveyorBelt) obj).getOrientation());
            } else if (obj instanceof Gear) {
                code = (((Gear) obj).rotatesLeft()) ? "rl" : "rr";
            } else if (obj instanceof Hole) {
                code = "de";
            } else if (obj instanceof Workshop) {
                code = (((Workshop) obj).isAdvancedWorkshop()) ? "re" : "re";
            } else if (obj instanceof Flag) {
                code = "f" + ((Flag) obj).getNumber();
                flagState.put(((Flag) obj).getNumber(),board.getAbsoluteCoordinates(tt));
                
            } else {
                code = "ti";
            }
            if (!tt.getWalls().isEmpty()) {
                code += encodeWall(tt);
            }

            boardState[board.getAbsoluteCoordinates(tt).x][board.getAbsoluteCoordinates(tt).y] = code;
        }

        encodeLaserBeams();
    }

    /*
     * Hilfsmethode zum Kodieren von Walls.
     */
    private String encodeWall(Tile tile) {
        List<Wall> walls = tile.getWalls();
        String wCode = "";

        for (Wall ww : walls) {
            wCode += "_w" + encodeOrientation(ww.getOrientation());
            if (ww.hasLaser() > 0)
                lasers.add(tile);
            else if (ww.getPusherPhases()[0] > 0)
                wCode += encodePusher(ww.getPusherPhases(), ww.getOrientation());
        }

        return wCode;
    }

    /*
     * Hilfsmethode zum Kodieren der Orientation diverser Boardelemente
     */
    private String encodeOrientation(Orientation orientation) {
        switch (orientation) {
        case NORTH:
            return "n";
        case EAST:
            return "e";
        case SOUTH:
            return "s";
        case WEST:
            return "w";
        default:
            LOGGER.error("Die Orientierung " + orientation + " konnte nicht kodiert werden!");
            return "";
        }
    }

    /*
     * Hilsmethode zum Kodieren von Pushern.
     */
    private String encodePusher(int[] phases, Orientation wallOrientation) {
        String pCode = "_p" + encodeOrientation(Orientation.getOppositeDirection(wallOrientation));
        for (int ii = 0; ii < phases.length; ii++)
            pCode += phases[ii];
        if(pCode.substring(3).matches("135"))
            return pCode.substring(0, 3).concat("3");
        if(pCode.substring(3).matches("24"))
            return pCode.substring(0, 3).concat("2");
        
        return pCode;
    }

    /*
     * Hilsmethode zum Kodieren der Tile-übergreifenden Laserstrahlen des
     * GameBoards. Wird initial nur einmal aufgerufen, denn der Verlauf der
     * Laserstrahlen ändert sich im Spielverlauf nicht mehr.
     */
    private void encodeLaserBeams() {
        for (Tile tt : lasers) {
            for (Wall wall : tt.getWalls()) {
                if (wall.hasLaser() > 0) {

boardState[board.getAbsoluteCoordinates(tt).x][board.getAbsoluteCoordinates(tt).y] += "_l"
                            + encodeOrientation(Orientation.getOppositeDirection(wall.getOrientation()))
                            + wall.hasLaser();
                    trackLaserBeam(tt, Orientation.getOppositeDirection(wall.getOrientation()), wall.hasLaser());
                }
            }
        }
    }

    /*
     * Hilfsmethode von putBoardLaserBeam zum verfolgen der Laserstrahlen.
     */
    private void trackLaserBeam(Tile tile, Orientation laserDirection, int laser) {
        if (tile.getWall(laserDirection) != null
                && tile.getWall(Orientation.getOppositeDirection(laserDirection)) != null)
            return;

        Tile beamTile = tile.getNeighbors().get(laserDirection);
        Wall inWall = beamTile.getWall(laserDirection);
        Wall oppWall = beamTile.getWall(Orientation.getOppositeDirection(laserDirection));

        while (inWall == null && oppWall == null) {
boardState[board.getAbsoluteCoordinates(beamTile).x][board.getAbsoluteCoordinates(beamTile).y] += "_l"
                    + encodeOrientation(laserDirection) + laser;
            if(beamTile.getNeighbors().get(laserDirection) == null){
                break;
            }
            beamTile = beamTile.getNeighbors().get(laserDirection);
            inWall = beamTile.getWall(laserDirection);
            oppWall = beamTile.getWall(Orientation.getOppositeDirection(laserDirection));
        }

        if (beamTile.getWall(laserDirection) != null){
boardState[board.getAbsoluteCoordinates(beamTile).x][board.getAbsoluteCoordinates(beamTile).y] += "_l"
                    + encodeOrientation(laserDirection) + laser;
        }
    }

    private void initRobotsState(Player player){
        Robot robot = player.getRobot();
        
        String robotState = robot.getName().split(" ")[0].concat(",");
        
        robotState = (player.equals(actrPlayer)) ? robotState.concat("t,") : robotState.concat("nil,"); 
        robotState = robotState.concat("3,10,n,nil-nil-nil-nil-nil,nil,0,");

        robotState = robotState.concat(pointToString(robot.getCurrentTile())).concat(",");
        robotState = robotState.concat(pointToString(robot.getRespawnPoint())).concat(";");
        
        robotsState.put(robot, robotState);
    }
    
    private String pointToString(Tile tile){
        return Integer.toString(tile.getAbsoluteCoordinates().x).concat(":").concat(Integer.toString(tile.getAbsoluteCoordinates().y));
    }
    
    /*
     * Kodiert den Zustand des Roboters.
     */
    private void encodeRobot(Player player, Event event) {
        if(event.equals(Event.REGISTER_LOCKED)||event.equals(Event.REGISTER_UNLOCKED))
            return;
        
        Robot robot = player.getRobot();

        String robotState = robot.getName().split(" ")[0].concat(",");

        robotState = (player.equals(actrPlayer)) ? robotState.concat("t,") : robotState.concat("nil,");
        
        robotState = robotState.concat(Integer.toString(robot.getLives())).concat(",");
        robotState = robotState.concat(Integer.toString(robot.getHP())).concat(",");
        robotState = robotState.concat(encodeOrientation(robot.getOrientation())).concat(",");

        ProgramCard[] register = robot.getRegisters();
        for (int ii = 0; ii < register.length; ii++) {
            robotState = (register[ii] == null) ? robotState.concat("nil") 
                    : robotState.concat(encodeCard(register[ii]).concat("_").concat(Integer.toString(register[ii].getPriority())));
            if (ii < register.length - 1)
                robotState = robotState.concat("-");
        }

        robotState = (robot.isPoweredDown()) ? robotState.concat(",t") : robotState.concat(",nil");

        robotState = robotState.concat(",").concat(Integer.toString(robot.getFlagCounterStatus())).concat(",");
        
        if(robot.getCurrentTile() != null){
            robotState = robotState.concat(pointToString(robot.getCurrentTile())).concat(",");
        }else{
            robotState = robotState.concat("nil:nil,");
        }
        
        if(robot.getRespawnPoint()!=null){
            robotState = robotState.concat(pointToString(robot.getRespawnPoint())).concat(";");
        }else{
            robotState = robotState.concat(",nil:nil;");
        }

        if (!robotsState.containsKey(robot))
            robotsState.put(robot, robotState);
        else
            robotsState.replace(robot, robotState);
    }

    /*
     * Kodiert Handkarten, wie sie der AIPlayer auf die hand bekommt.
     */
    private void encodeHand(List<ProgramCard> hand) {
        handState = new ArrayList<>();

        for (int ii = 0; ii < hand.size(); ii++) {
            String card = encodeCard(hand.get(ii)) + "_" + hand.get(ii).getPriority();
            if (ii < hand.size() - 1) {
                card += ",";
            }
            handState.add(card);
        }
    }

    /*
     * Hilfsmethode zum Kodieren von Karten.
     */
    private String encodeCard(ProgramCard card) {
        if(card.getRange() > 0){
            return "a" + card.getRange();
        }else if(card.getRange() < 0){
            return "b1";
        }else if (card instanceof TurnLeftCard) {
            return "tl";
        } else if (card instanceof TurnRightCard) {
            return "tr";
        } else if (card instanceof UturnCard) {
            return "tu";
        } else {
            LOGGER.error("Die Programmkarte konnte nicht klassifiziert werden.");
            return "";
        }
    }
}
