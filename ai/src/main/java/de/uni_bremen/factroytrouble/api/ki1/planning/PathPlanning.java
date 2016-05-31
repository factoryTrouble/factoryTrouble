/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.api.ki1.planning;

import java.awt.Point;
import java.util.List;

import de.uni_bremen.factroytrouble.api.ki1.Control;
import de.uni_bremen.factroytrouble.board.Tile;

public interface PathPlanning {
    /**
     * Berechnet mit A* einen kürzesten Weg.
     * 
     * @param start
     *            Start{@link Tile}
     * @param target
     *            End{@link Tile}
     * @return Der gefundene Weg als Liste von {@link Tile}s
     */
    List<Tile> getPath(Tile start, Tile target);

    /**
     * Berechnet mit A* einen alternativen Weg, falls ein bisheriger gefunden,
     * aber nicht angewandt werden konnte.
     * 
     * @param start
     *            Start{@link Tile}
     * @param target
     *            End{@link Tile}
     * @param blockedPoints
     *            Punkte aus einem vorherigen Weg, die nicht mehr abgelaufen
     *            werden sollen
     * @return Der gefundene Weg als Liste von {@link Tile}s
     */
    List<Tile> getAlternativePath(Tile start, Tile target, List<Point> blockedPoints);

    /**
     * Prüft, ob von einem {@link Tile} zu einem Nachbar{@link Tile} gegangen
     * werden kann, also keine {qlink Wall} dazwischen liegt.
     * 
     * @param direction
     *            die Gehrichtung
     * @param startTile
     *            Startfeld
     * @param targetTile
     *            Zielfeld
     * @return Wand im Weg, oder nicht
     */
    public boolean isWallInWay(int direction, Tile startTile, Tile targetTile);

    /**
     * Gibt zu einem gegebenen {@link Point} das entsprechende {@link Tile} auf
     * dem SpielBrett zurück.
     * 
     * @param coordinates
     *            absolute Koordinaten
     * @return die Kachel, oder null falls Koordinaten nicht auf dem Brett
     */
    public Tile getTile(Point coordinates);
    
    public void setController(Control control);

}
