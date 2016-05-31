/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki2.ai.planning;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uni_bremen.factroytrouble.ai.ais.AIPlayer2;
import de.uni_bremen.factroytrouble.ai.ki2.api.decision.unconsciousness.UnconsciousnessUnit;
import de.uni_bremen.factroytrouble.player.ProgramCard;

public class KillpathFinder extends PathFinder {

    private AIPlayer2 aiPlayer2;

    public KillpathFinder(UnconsciousnessUnit uncons, AIPlayer2 player) {
        super(uncons);
        aiPlayer2 = player;
    }

    /**
     * Sucht einen Weg, um den nächsten Gegner zu schaden
     * 
     * @return der Weg
     */
    public List<List<Integer>> findKillPath() {
        if (checkForImmediateKill()) {
            return findPath();
        }
        setFlagPos(findNextEnemy());
        return findPath();
    }

    /**
     * Schaut wo der nächste Gegner steht
     * 
     * @return der nächste Gegner
     */
    private Point findNextEnemy() {
        Point nextEnemy = null;
        Point myPos = getPos();
        Map<Point, Integer> distances = buildDistances(myPos);
        int dist = Integer.MAX_VALUE;
        for (Entry<Point, Point> enemy : getEnemies().entrySet()) {
            Point enemyPos = enemy.getKey();
            if (distances.get(enemyPos) < dist) {
                dist = distances.get(enemyPos);
                nextEnemy = enemyPos;
            }
        }
        return nextEnemy;
    }

    /**
     * Schaut, ob man mit einer Karte jemanden zerstören kann
     * 
     * @return ob man die Karten hat
     */
    private boolean checkForImmediateKill() {

        int steps = 0;
        List<Point> forward = findNextObstacle(getPos(), getOrientation());
        if (forward.size() > 3) {
            forward = forward.subList(0, 4);
        }
        for (Point forwardPoint : forward) {
            if (getEnemies().containsKey(forwardPoint)) {
                steps += forward.indexOf(forwardPoint);
                if (getHoles().contains(getNeighbors(forwardPoint, getOrientation()))) {
                    steps += 1;
                }
                if (getHoles().contains(getNeighbors(getNeighbors(forwardPoint, getOrientation()), getOrientation()))) {
                    steps += 2;
                }
                if (getHoles().contains(
                        getNeighbors(getNeighbors(getNeighbors(forwardPoint, getOrientation()), getOrientation()),
                                getOrientation()))) {
                    steps += 3;
                }
            }
        }
        if (steps > 0 && steps < 4) {
            return checkForImmediateKillCard(steps);
        }
        return false;
    }

    /**
     * Schaut, ob man mit einer Karte jemanden zerstören kann
     * 
     * @return ob man die Karten hat
     */
    private boolean checkForImmediateKillCard(int steps) {
        ArrayList<Integer> way = new ArrayList<>();
        way.add(steps);
        List<ProgramCard> forward;
        switch (steps) {
        case 1:
            forward = getHandCards("1vorwärtskarten");
            break;
        case 2:
            forward = getHandCards("2vorwärtskarten");
            break;
        default:
            forward = getHandCards("3vorwärtskarten");
        }
        if (!forward.isEmpty()) {
            aiPlayer2.fillRegister(0, forward.get(0));
            setMyPos(getEndpoint(way));
            setMyOrient(findOrientation(way));
            return true;
        }
        return false;
    }
}
