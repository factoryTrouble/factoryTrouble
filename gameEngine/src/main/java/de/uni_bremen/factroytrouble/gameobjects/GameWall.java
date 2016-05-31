/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gameobjects;

/**
 * @author Stefan
 */
public class GameWall implements Wall {
    private Orientation orientation;
    private boolean hasLaser;
    private int laser;
    private int[] pusherPhases;

    public GameWall(Orientation orientation, int... pusherPhases) {
        this.orientation = orientation;
        this.pusherPhases = pusherPhases;
    }

    public GameWall(Orientation orientation) {
        this.orientation = orientation;
        pusherPhases = new int[] { -1 };
    }

    @Override
    public Orientation getOrientation() {
        return orientation;
    }

    @Override
    public void setLaser() {
        laser++;
        if (!hasLaser) {
            hasLaser = true;
        }
    }

    @Override
    public int hasLaser() {
        return laser;
    }

    @Override
    public int[] getPusherPhases() {
        return pusherPhases;
    }

    @Override
    public Wall clone() {
        int[] newPhases = new int[pusherPhases.length];
        for (int i = 0; i < newPhases.length; i++) {
            newPhases[i] = pusherPhases[i];
        }
        Wall wall = new GameWall(orientation, newPhases);
        for(int i = 0; i < laser; i++){
            wall.setLaser();
        }
        return wall;
    }
}
