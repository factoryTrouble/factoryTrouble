/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.player;

import de.uni_bremen.factroytrouble.board.Board;

/**
 * Gibt Zugriff auf den aktuellen GameMaster und Board-Kopien für die KI/ GUI
 * 
 * @author Thorben
 *
 */
public interface MasterFactory {

    /**
     * Gibt den aktuellen Master zurück; erzeugt einen neuen Master, wenn noch
     * keiner vorhanden
     * 
     * @return Den aktuellen Master
     */
    Master getMaster(int gameId);

    /**
     * Klont das Board des aktuellen Masters und gibt dieses zurück
     * 
     * @return Einen Klon vom Board des aktuellen Masters
     */
    Board getBoardClone(int gameId);

    /**
     * Klont das übergebene Board und gibt dieses zurück
     * 
     * @param board
     *            das zu klonende Board
     * 
     * @return Einen Klon des übergebenen Boards
     */
    Board getBoardClone(Board board);

    void deleteMaster(int gameId);

}
