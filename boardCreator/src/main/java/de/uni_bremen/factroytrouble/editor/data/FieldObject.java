/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.editor.data;

/**
 * Eine Wand oder ein an eine Wand angeschlossenes Objekt
 *
 * @author
 */
public enum FieldObject {

    /**
     * Wand
     */
    WALL("w"),
    /**
     * Einfacher Laser
     */
    LASER_SINGLE("l"),
    /**
     * Doppelter Laser
     */
    LASER_DOUBLE("ll"),
    /**
     * Dreifacher Laser
     */
    LASER_TRIPLE("lll"),
    /**
     * Pusher in den ungeraden Phasen
     */
    PUSHER_ODD("p135"),
    /**
     * Pusher in den geraden Phasen
     */
    PUSHER_EVEN("p24");

    private final String boardFileDescription;

    FieldObject(String boardFileDescription) {
        this.boardFileDescription = boardFileDescription;
    }

    /**
     * Gibt die Beschreibung für ein FIELD oder DOCK Manual zurück
     *
     * @return
     *      Textuelle Beschreibung des Bodenelementes
     */
    public String getBoardFileDescription() {
        return boardFileDescription;
    }
}
