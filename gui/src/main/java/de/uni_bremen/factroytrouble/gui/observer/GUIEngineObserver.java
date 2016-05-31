/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gui.observer;

import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.gui.controller.GameFieldController;
import de.uni_bremen.factroytrouble.gui.controller.GameScreenController;
import de.uni_bremen.factroytrouble.gui.controller.components.FieldController;
import de.uni_bremen.factroytrouble.gui.services.game.AnimationService;
import de.uni_bremen.factroytrouble.gui.services.game.SoundService;
import de.uni_bremen.factroytrouble.spring.SpringConfigHolder;
import de.uni_bremen.factroytrouble.observer.Event;
import de.uni_bremen.factroytrouble.observer.GameObserver;

/**
 * Observer, der Events aus der Spielmechanik entgegen nimmt
 *
 * @author Andre
 */
public class GUIEngineObserver implements GameObserver {

    private GameScreenController gameScreenController;
    private FieldController fieldController;
    private SoundService soundService;
    private AnimationService animationService;

    public GUIEngineObserver() {
        gameScreenController = SpringConfigHolder.getInstance().getContext().getBean(GameScreenController.class);
        fieldController = SpringConfigHolder.getInstance().getContext().getBean(GameFieldController.class);
        soundService = SpringConfigHolder.getInstance().getContext().getBean(SoundService.class);
        animationService = SpringConfigHolder.getInstance().getContext().getBean(AnimationService.class);
    }

    @Override
    public void spam(Robot robot, Event event, Object source) {
        if (event.equals(Event.KILLED)) {
            animationService.kill(robot);
            soundService.playSound("explode.mp3");
        }
        if (event.equals(Event.RESPAWN)) {
            animationService.respawn(robot);
        }
        gameScreenController.getPlayerInfos().stream()
                .filter(playerInfo -> playerInfo.getName().equals(robot.getName())).forEach(playerInfo -> {
                    playerInfo.setHp(robot.getHP());
                    playerInfo.setLp(robot.getLives());
                    if (event.equals(Event.TOUCH_FLAG)) {
                        playerInfo.hitFlag(robot.getFlagCounterStatus());
                    }
                });

        if (robot.getCurrentTile() == null) {
            return;
        }
        if (!fieldController.getRobots().containsKey(robot.getName())) {
            return;
        }
        fieldController.getRobots().get(robot.getName()).refresh(robot.getCurrentTile().getAbsoluteCoordinates(),
                robot.getOrientation());
        if (fieldController.getRespawns().get(robot.getName()) == null) {
            return;
        }
        fieldController.getRespawns().get(robot.getName()).setPos(robot.getRespawnPoint().getAbsoluteCoordinates(),
                fieldController.getRespawnsOnPos(fieldController.getRespawns().get(robot.getName()),
                        robot.getRespawnPoint().getAbsoluteCoordinates()));

        if (event.equals(Event.HEALED)) {
            soundService.playSound("robot_repair.mp3");
        }
        if (event.equals(Event.SHOT)) {
            soundService.playSound("laser_shot.mp3");
            if (source instanceof Robot) {
                animationService.laserShot(robot, (Robot) source);
            }
        }
    }
}
