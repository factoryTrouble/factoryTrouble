/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki2.ai.decision.consciousness;

public class Aim {

    private int maxTiles = -1;
    private int maxEnemies = -1;
    private int maxDamage = -1;
    private int maxHoles = -1;
    private int maxLasers = -1;
    private int maxConveyor = -1;

    public int getMaxTiles() {
        return maxTiles;
    }

    public void setMaxTiles(int max) {
        if (max < 0) {
            maxTiles = 0;
        } else {
            maxTiles = max;
        }

    }

    public int getMaxEnemies() {
        return maxEnemies;
    }

    public void setMaxEnemies(int max) {
        if (max < 0) {
            maxEnemies = 0;
        } else {
            maxEnemies = max;
        }
    }

    public int getMaxDamage() {
        return maxDamage;
    }

    public void setMaxDamage(int max) {
        if (max < 0) {
            maxDamage = 0;
        } else {
            maxDamage = max;
        }
    }

    public int getMaxLasers() {
        return maxLasers;
    }

    public void setMaxLasers(int max) {
        if (max < 0) {
            maxLasers = 0;
        } else {
            maxLasers = max;
        }
    }

    public int getMaxHoles() {
        return maxHoles;
    }

    public void setMaxHoles(int max) {
        if (max < 0) {
            maxHoles = 0;
        } else {
            maxHoles = max;
        }
    }

    public int getMaxConveyor() {
        return maxConveyor;
    }

    public void setMaxConveyor(int max) {
        if (max < 0) {
            maxConveyor = 0;
        } else {
            maxConveyor = max;
        }
    }
}
