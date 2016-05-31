/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gameobjects;

/**
 * Gibt die möglichen Ausrichtungen eines Roboters auf dem Spielfeld an
 * 
 * @Author Thorben
 * 
 */
public enum Orientation {
    EAST("Osten"), NORTH("Norden"), WEST("Westen"), SOUTH("Süden");

    private String germanTranslation;

    private Orientation(String germanTranslation) {
        this.germanTranslation = germanTranslation;
    }

    public static Orientation getNextDirection(Orientation direction, boolean left) {

        return values()[(direction.ordinal() + (left ? 1 : 3)) % 4];

    }

    public static Orientation getOppositeDirection(Orientation direction) {
        return values()[(direction.ordinal() + 2) % 4];
    }

    @Override
    public String toString() {
        return germanTranslation;
    }
}
