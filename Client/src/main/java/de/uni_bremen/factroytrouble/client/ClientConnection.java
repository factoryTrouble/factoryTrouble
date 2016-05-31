/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.client;

import com.esotericsoftware.kryonet.Client;
import de.uni_bremen.factroytrouble.networkCore.NetworkConfig;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by johannes.gesenhues on 04.12.15.
 */
@Service
public class ClientConnection {
    static Client client = null;

    private void connect() throws IOException, InterruptedException {
        client = new Client(320000,320000);
        client.start();
        new NetworkConfig(client.getKryo());
        client.connect(5000, NetworkConfig.SERVER_DOMAIN, NetworkConfig.TCP_PORT, NetworkConfig.UDP_PORT);
    }


}