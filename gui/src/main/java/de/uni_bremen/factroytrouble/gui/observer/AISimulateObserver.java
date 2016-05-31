/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gui.observer;

import de.uni_bremen.factroytrouble.gameobjects.Robot;
import de.uni_bremen.factroytrouble.observer.Event;
import de.uni_bremen.factroytrouble.observer.GameObserver;
import org.apache.log4j.Logger;

/**
 * Observer, der alle Events nur ins Log schreibt ohne weitere Verarbeitung
 *
 * @Author André
 */
public class AISimulateObserver implements GameObserver {

    private static final Logger LOGGER = Logger.getLogger(AISimulateObserver.class);
    private static final String TAB = "    ";

    @Override
    public void spam(Robot robot, Event event, Object source) {
        LOGGER.info("Observer Event:" + System.getProperty("line.separator") + TAB +
            "Robot: " + robot.getName() + System.getProperty("line.separator") + TAB +
            "Event: " + event.name());
    }

}
