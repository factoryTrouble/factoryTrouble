/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki1;

import org.apache.log4j.Logger;

import de.uni_bremen.factroytrouble.api.ki1.Timer;

/**
 * {@inheritDoc}
 * 
 * @author tim
 */
public class TimerUnit implements Timer {
    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(TimerUnit.class);
    /**
     * Falls {@code true}, gleicht der Timer Berechnungszeiten mit den
     * angegebenen, menschlichen Zeiten an.
     */
    private static final boolean SLEEPING = true;

    /*
     * (non-Javadoc)
     * 
     * @see de.uni_bremen.factroytrouble.ai.Timer#run(java.util.concurrent.Callable,
     * int, java.lang.String)
     */
    @Override
    public <T> T run(Calculation<T> func, int humanTime, String task) throws InterruptedException {
        long start = System.currentTimeMillis();
        T result = func.exec();
        long passedTime = System.currentTimeMillis() - start;
        long expected = (long) humanTime * 1000;

        LOGGER.debug(String.format("%s: %dms (human: %dms)", task, passedTime, expected));
        if (SLEEPING && passedTime < expected) {
            Thread.sleep(expected - passedTime);
        }
        return result;
    }
}
