/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.networkCore;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import de.uni_bremen.factroytrouble.board.Board;
import de.uni_bremen.factroytrouble.gameobjects.Orientation;
import de.uni_bremen.factroytrouble.network.EngineWrapper;
import de.uni_bremen.factroytrouble.observer.GameObserver;
import de.uni_bremen.factroytrouble.player.*;

import java.awt.*;
import java.util.ArrayList;

public class NetworkConfig {
    public static final Integer TCP_PORT = 8080;
    public static final Integer UDP_PORT = 8081;
    public static final String SERVER_DOMAIN = "0.0.0.0";

    public NetworkConfig(Kryo kryo){
        ObjectSpace.registerClasses(kryo);
        kryo.register(ObjectSpace.class);
        kryo.register(ProgramCard.class);
        kryo.register(GameMoveBackwardCard.class);
        kryo.register(GameMoveForwardCard.class);
        kryo.register(GameTurnLeftCard.class);
        kryo.register(GameTurnRightCard.class);
        kryo.register(GameUturnCard.class);
        kryo.register(GameMasterFactory.class);
        kryo.register(boolean[].class);
        kryo.register(EngineWrapper.class);
        kryo.register(ArrayList.class);
        kryo.register(Orientation.class);
        kryo.register(Point.class);
        kryo.register(Board.class);
        kryo.register(de.uni_bremen.factroytrouble.gameobjects.Robot.class);
        kryo.register(GameObserver.class);
        kryo.register(NullPointerException.class);
    }
}
