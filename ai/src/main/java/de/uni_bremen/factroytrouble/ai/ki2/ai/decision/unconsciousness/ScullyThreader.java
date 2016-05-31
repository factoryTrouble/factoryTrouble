/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki2.ai.decision.unconsciousness;

import java.util.Random;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import de.uni_bremen.factroytrouble.ai.ki2.ai.decision.consciousness.ScullyConsciousnessUnit;
import de.uni_bremen.factroytrouble.ai.ki2.api.decision.consciousness.ConsciousnessUnit;
import de.uni_bremen.factroytrouble.ai.ki2.api.decision.unconsciousness.UnconsciousnessUnit;
import de.uni_bremen.factroytrouble.player.GameMaster;

/**
 * Erstellt und startet einen Thread, der zwischen Uncon und Con spielt. Der
 * Thread wird heruntergefahren, wenn unser Zug beendet ist und aufgeweckt, wenn
 * die Planung von neuem beginnt
 * 
 * @author Sven
 *
 */
public class ScullyThreader {
    Runnable t;
    boolean run = true;
    private static final int CHANCE_OF_SEND_TO_CON = 30;
    private ScullyUnconsciousnessUnit uncon;
    private ScullyConsciousnessUnit con;
    private static final long SLEEP = 1000;
    private static final Logger LOGGER = Logger.getLogger("Con/Uncon Thread AI2");
    private Random random;
    private boolean cd = false;

    /**
     * Erstellt und startet einen Thread, der zwischen Uncon und Con spielt. Der
     * Thread wird heruntergefahren, wenn unser Zug beendet ist und aufgeweckt,
     * wenn die Planung von neuem beginnt
     */
    public ScullyThreader(UnconsciousnessUnit uncon, ConsciousnessUnit con) {
        this.con = (ScullyConsciousnessUnit) con;
        this.uncon = (ScullyUnconsciousnessUnit) uncon;
        random = new Random();
        uncon.influenceConsciousness();
    }

    /**
     * Weckt den Thread auf
     */
    public synchronized void wakeUp() {
        run = true;
        cd = false;
        t = () -> {
            while (run) {
                executeWhileRunning();
                try {
                    synchronized (t) {
                        t.wait(SLEEP);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    LOGGER.info("[KI2- Threader]Thread schläft: " + e);
                }
            }
        };
        Executors.newSingleThreadExecutor().execute(t);
    }

    private synchronized void executeWhileRunning() {
        if (!cd && con.getTime() < GameMaster.TIMER && con.getTime() > 1) {
            cd = true;
            con.panic();
        }
        if (random.nextInt() % (100 / CHANCE_OF_SEND_TO_CON) == 0)
            uncon.influenceConsciousness();
    }

    /**
     * Legt den thread schlafen, bis er mit {@code wakeUp} wieder gestartet
     * wird.
     */
    public void waitForWakeUp() {
        run = false;
    }
}
