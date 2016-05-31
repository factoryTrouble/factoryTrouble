/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.player;

import de.uni_bremen.factroytrouble.board.Board;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Thorben
 */
@Component
public class GameMasterFactory implements MasterFactory {

    // Der einzige Master; Kann von der TestFactory üebrschrieben werden
    protected static Map<Integer, Master> masterMap = new ConcurrentHashMap<>();

    @Override
    public synchronized Master getMaster(int gameId) {
        Master master = masterMap.get(gameId);
        if (master == null) {
            master = new GameMaster(gameId, new BoardManager(gameId, new FieldLoader(gameId)));
            masterMap.put(gameId, master);
        }
        return master;
    }
    
    @Override
    public synchronized void deleteMaster(int gameId){
        masterMap.remove(gameId);
    }

    @Override
    public synchronized Board getBoardClone(int gameId) {
        Master master = getMaster(gameId);
        if (master == null) {
            return null;
        }
        return master.getBoard().clone();
    }

    @Override
    public synchronized Board getBoardClone(Board board) {
        return board.clone();
    }

}
