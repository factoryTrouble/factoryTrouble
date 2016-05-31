/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.editor.data;

/**
 * Repräsenation der Bodenfläche eines Tiles
 *
 * @author Andre
 */
public enum GroundFill {

    /**
     * Leeres Feld
     */
    EMPTY("ti"),
    /**
     * Zahnrad in Uhrzeigersinn
     */
    GEAR_CW("ge"),
    /**
     * Zahnrad gegen den Uhrzeigersinn
     */
    GEAR_CCW("ge_rl"),
    /**
     * Förderband
     */
    CONVEYOR_BELT("be"),
    /**
     * Express Förderband
     */
    EXPRESS_CONVEYOR_BELT("be_#_ex"),
    /**
     * Werkstatt
     */
    REPAIR("ws"),
    /**
     * Flagge
     */
    FLAG(""),
    /**
     * Loch
     */
    HOLE("ho"),
    /**;
     * Startpostion (entspricht einfachen Tile)
     */
    START("ti");

    private final String boardFileDescription;

    private GroundFill(String boardFileDescription) {
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
