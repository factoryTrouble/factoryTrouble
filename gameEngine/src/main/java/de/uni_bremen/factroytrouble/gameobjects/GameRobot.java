/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gameobjects;

import de.uni_bremen.factroytrouble.board.Tile;
import de.uni_bremen.factroytrouble.observer.Event;
import de.uni_bremen.factroytrouble.observer.GameObserver;
import de.uni_bremen.factroytrouble.player.GameMasterFactory;
import de.uni_bremen.factroytrouble.player.MasterFactory;
import de.uni_bremen.factroytrouble.player.OptionCard;
import de.uni_bremen.factroytrouble.player.ProgramCard;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Stefan
 */
public class GameRobot implements Robot {

    public static final String REGISTER_WITH_SPACE_IN_THE_END = "Register ";
    public static final String FROM_ROBOT = "von Roboter";
    private static final Logger LOG_BOT = Logger.getLogger(Robot.class);
    private final int gameId;

    private Orientation orientation;
    private boolean poweredDown;
    private int currentHP; // Annahme: Hit Point = Damage Token
    private int currentLP;
    private int flagCounter;
    private int regPhase;
    private Tile currentTile;
    private Tile respawnPoint;
    private String name;

    private List<OptionCard> options = new ArrayList<>();
    private boolean[] emptyLocked;
    private boolean[] isLocked;
    private ProgramCard[] registers;
    private boolean wasKilled = false;

    private List<GameObserver> dieSpammer = new ArrayList<>();

    private MasterFactory factory = new GameMasterFactory();

    public GameRobot(int gameId, Tile dockTile, Orientation orientation, String name) {
        this.gameId = gameId;
        this.orientation = orientation;
        poweredDown = false;
        currentHP = 0;
        currentLP = 3;
        flagCounter = 0;
        regPhase = 0;
        this.name = name;
        currentTile = dockTile;
        respawnPoint = dockTile;
        registers = new ProgramCard[MAX_REGISTERS];
        emptyLocked = new boolean[MAX_REGISTERS];
        isLocked = emptyLocked.clone();
    }

    @Override
    public void turn(boolean turnLeft) {
        orientation = Orientation.getNextDirection(orientation, turnLeft);
        spamAll(Event.TURNED, null);
    }

    @Override
    public void powerDown() {
        poweredDown = true;
        spamAll(Event.POWERED_DOWN, null);
    }

    @Override
    public void wakeUp() {
        poweredDown = false;
        spamAll(Event.WAKED_UP, null);
    }

    @Override
    public void takeDamage(Object shooter) {
        if (currentHP < 10) {
            currentHP += 1;
            LOG_BOT.info("Roboter ".concat(name).concat(" nimmt 1 Schaden"));
        }

        if (currentHP > 4 && currentHP < 10) { // Pruefe ob locking erforderlich
            lockNextRegister();
        }
        spamAll(Event.SHOT, shooter);
    }

    @Override
    public void heal() {
        if (currentHP > 0 && currentHP < 10) {
            currentHP -= 1;
            LOG_BOT.info("Roboter ".concat(name).concat(" wird geheilt"));
        } else {
            return;
        }
        freeNextRegister();
        spamAll(Event.HEALED, null);
    }

    @Override
    public void healFully() {
        currentHP = 0;
        isLocked = emptyLocked.clone();
        spamAll(Event.HEALED, null);
    }

    @Override
    public void kill() {
        if (wasKilled) {
            return;
        }
        if (currentTile != null) {
            currentTile.setRobot(null);
        }
        currentTile = null;
        if (currentLP > 1) {
            currentLP -= 1;
            // Falls nicht im Master umgesetzt:
            // Option Card auswählen und abwerfen
            currentHP = 2;
            freeAllRegister();
        } else {
            respawnPoint = null;
            currentLP = 0;
        }
        wasKilled = true;
        LOG_BOT.info("Roboter ".concat(name).concat(" ist zerstört"));
        spamAll(Event.KILLED, null);
    }

    @Override
    public boolean fillRegister(int register, ProgramCard card) {
        if (isLocked[register] || registers[register] != null) {
            return false;
        }
        registers[register] = card;
        LOG_BOT.info("Programmkarte " + card + " in Register " + register + " von Roboter " + name + " geladen");
        spamAll(Event.REGISTER_FILLED, null);
        return true;
    }

    @Override
    public ProgramCard emptyRegister(int register) {
        if (isLocked[register]) {
            return null;
        }
        ProgramCard regCard = registers[register];
        registers[register] = null;
        spamAll(Event.REGISTER_EMPTIED, null);
        return regCard;
    }

    @Override
    public List<ProgramCard> emptyAllRegister() {
        List<ProgramCard> removed = new ArrayList<>();
        for (int ii = 0; ii < MAX_REGISTERS; ii++) {
            if (!isLocked[ii]) {
                removed.add(registers[ii]);
                LOG_BOT.info("Programmkarte " + registers[ii] + " wird aus Register " + ii + " von Roboter " + name
                        + " entfernt");
                registers[ii] = null;
            } else {
                LOG_BOT.info("Register " + ii + " von Roboter " + name
                        + " ist gelockt; Enthaltene Programmkarte wird nicht entfernt");
            }
        }
        spamAll(Event.REGISTER_EMPTIED, null);
        return removed;
    }

    @Override
    public void executeNext() {
        if (!poweredDown && !wasKilled) {
            registers[regPhase].execute(this);
            regPhase += 1;
            if (regPhase == MAX_REGISTERS) {
                regPhase = 0;
            }
        }
    }

    @Override
    public void lockNextRegister() {
        for (int ii = MAX_REGISTERS - 1; ii >= 0; ii--) {
            if (!isLocked[ii]) {
                isLocked[ii] = true;
                LOG_BOT.info((REGISTER_WITH_SPACE_IN_THE_END + ii).concat(" " + FROM_ROBOT + " ").concat(name)
                        .concat(" wird gesperrt"));
                fillRegisterIfEmpty(ii);
                spamAll(Event.REGISTER_LOCKED, null);
                break;
            }
        }
    }

    @Override
    public void lockRegister(int register) {
        if (isLocked[register]) {
            return;
        }
        isLocked[register] = true;
        LOG_BOT.info((REGISTER_WITH_SPACE_IN_THE_END + register).concat(" " + FROM_ROBOT + " ").concat(name)
                .concat(" wird gesperrt"));
        fillRegisterIfEmpty(register);
        spamAll(Event.REGISTER_LOCKED, null);
    }

    @Override
    public void freeNextRegister() {
        for (int ii = 0; ii < MAX_REGISTERS; ii++) {
            if (isLocked[ii]) {
                factory.getMaster(gameId).layOffCard(registers[ii]);
                isLocked[ii] = false;
                registers[ii] = null;
                LOG_BOT.info((REGISTER_WITH_SPACE_IN_THE_END + ii).concat(" " + FROM_ROBOT + " ").concat(name)
                        .concat(" wird entsperrt"));
                spamAll(Event.REGISTER_UNLOCKED, null);
                break;
            }
        }
    }

    @Override
    public void freeRegister(int register) {
        factory.getMaster(gameId).layOffCard(registers[register]);
        isLocked[register] = false;
        registers[register] = null;
        LOG_BOT.info((REGISTER_WITH_SPACE_IN_THE_END + register).concat(" " + FROM_ROBOT + " ").concat(name)
                .concat(" wird entsperrt"));
        spamAll(Event.REGISTER_UNLOCKED, null);
    }

    private void freeAllRegister() {
        for (int i = 0; i < isLocked.length; i++) {
            freeRegister(i);
        }
    }

    @Override
    public void touchFlag() {
        flagCounter += 1;
        spamAll(Event.TOUCH_FLAG, null);
    }

    @Override
    public Orientation getOrientation() {
        return orientation;
    }

    @Override
    public int getHP() {
        return 10 - currentHP;
    }

    @Override
    public int getLives() {
        return currentLP;
    }

    @Override
    public Tile getCurrentTile() {
        return currentTile;
    }

    @Override
    public void setCurrentTile(Tile tile) {
        currentTile = tile;
        spamAll(Event.MOVED, null);
    }

    @Override
    public void setRespawnPoint() {
        respawnPoint = currentTile;
        wasKilled = false;
    }

    @Override
    public Tile getRespawnPoint() {
        return respawnPoint;
    }

    @Override
    public int getFlagCounterStatus() {
        return flagCounter;
    }

    @Override
    public int getNextPriority() {
        return registers[regPhase].getPriority();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ProgramCard[] getRegisters() {
        return registers;
    }

    @Override
    public void addOption(OptionCard card) {
        options.add(card);
        spamAll(Event.OPTION_RECEIVED, null);
    }

    @Override
    public void removeOption(OptionCard card) {
        options.remove(card);
        spamAll(Event.OPTION_REMOVED, null);
    }

    public int getRegPhase() {
        return regPhase;
    }

    @Override
    public List<OptionCard> getOptions() {
        return options;
    }

    @Override
    public List<ProgramCard> fillEmptyRegisters(List<ProgramCard> cards) {
        Collections.shuffle(cards);
        for (int i = 0; i < MAX_REGISTERS; i++) {
            if (registers[i] == null) {
                ProgramCard card = cards.remove(0);
                registers[i] = card;
                LOG_BOT.info("Programmkarte " + card + " zufällig ausgewählt und in Register " + i + " von Roboter "
                        + name + " geladen");
            }
        }
        spamAll(Event.REGISTER_FILLED, null);
        return cards;
    }

    @Override
    public boolean isPoweredDown() {
        return poweredDown;
    }

    @Override
    public boolean respawn(Tile tile) {
        if (currentLP <= 0) {
            return false;
        }
        if (currentTile != null) {
            currentTile.setRobot(null);
        }
        setCurrentTile(tile);
        tile.setRobot(this);
        wasKilled = false;
        regPhase = 0;
        spamAll(Event.RESPAWN, null);
        return true;
    }

    @Override
    public Robot clone(Tile clonedTile) {
        Robot newRobot = new GameRobot(gameId, clonedTile, orientation, name);

        while (flagCounter != newRobot.getFlagCounterStatus()) {
            newRobot.touchFlag();
        }

        while (getHP() != newRobot.getHP()) {
            newRobot.takeDamage(null);
        }

        while (currentLP != newRobot.getLives()) {
            Tile tile = newRobot.getCurrentTile();
            newRobot.kill();
            newRobot.respawn(tile);
        }

        options.forEach(card -> newRobot.addOption(card));

        return newRobot;
    }

    @Override
    public void attachObserver(GameObserver observer) {
        if (!dieSpammer.contains(observer)) {
            dieSpammer.add(observer);
        }
    }

    @Override
    public void removeObserver(GameObserver observer) {
        dieSpammer.remove(observer);
    }

    @Override
    public List<GameObserver> getObserverList() {
        return dieSpammer;
    }

    private void spamAll(Event event, Object source) {
        dieSpammer.forEach(spammer -> spammer.spam(this, event, source));
    }

    @Override
    public boolean[] registerLockStatus() {
        return isLocked;
    }

    private void fillRegisterIfEmpty(int register) {
        if (registers[register] == null) {
            registers[register] = factory.getMaster(gameId).dealCard();
            LOG_BOT.info("Register " + register + " von Roboter " + name + " gefüllt mit Karte vom Deck");
            spamAll(Event.REGISTER_FILLED, null);
        }
    }
}