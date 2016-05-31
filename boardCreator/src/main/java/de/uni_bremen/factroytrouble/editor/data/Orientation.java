/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.editor.data;

/**
 * Richtung eines Tiles
 *
 * @author Andre
 */
public enum Orientation {

    /**
     * Osten, bzw. Rechts
     */
    EAST("ea", 0, 1),
    /**
     * Norden, bzw. Oben
     */
    NORTH("no", -1, 0),
    /**
     * Westen, bzw. Links
     */
    WEST("we", 0, -1),
    /**
     * Süden, bzw. Unten
     */
    SOUTH("so", 1, 0);

    private final String boardFileDescription;
    private final Integer rowDelta;
    private final Integer columnDelta;


    private Orientation(String boardFileDescription, Integer rowDelta, Integer columnDelta) {
        this.boardFileDescription = boardFileDescription;
        this.rowDelta = rowDelta;
        this.columnDelta = columnDelta;
    }

    /**
     * Gibt die nächste Richtng an
     * @param left
     *      True = Nächste richtung gegen den Uhrzeigersinn
     *      False = nächste Richtung in Uhrzeigersinn
     * @return
     *      Die Richtung
     */
    public Orientation getNextDirection(boolean left) {
        return values()[(this.ordinal() + (!left ? 1 : 3)) % 4];
    }

    /**
     * Gibt die gegerüberliegende Richtung zurück
     *
     * @return
     *      Die Richtung
     */
    public Orientation getOppositeDirection() {
        return values()[(this.ordinal() + 2) % 4];
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

    /**
     * Gibt zurück, in welcher Richtung sich eine Zeile verändert
     * @return
     *      Deltas zwischen der einer Betrachten Zeile und den Nachbarn in gegebener Richtung ({-1,0,1})
     */
    public Integer getRowDelta() {
        return rowDelta;
    }

    /**
     * Gibt zurück, in welcher Richtung sich eine Spalte verändert
     * @return
     *      Deltas zwischen der einer Betrachten Spalte und den Nachbarn in gegebener Richtung ({-1,0,1})
     */
    public Integer getColumnDelta() {
        return columnDelta;
    }
}
