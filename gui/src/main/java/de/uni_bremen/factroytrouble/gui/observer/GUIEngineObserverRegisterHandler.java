/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gui.observer;

import de.uni_bremen.factroytrouble.gui.services.GameEngineWrapper;
import de.uni_bremen.factroytrouble.observer.GameObserver;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Registriert die GUI bei der Spielmechanik als Observer um PUSH Events zu erhalten.
 *
 * @author André
 */
@Component("GUIEngineObserverRegisterHandler")
public class GUIEngineObserverRegisterHandler implements ObserverRegisterHandler<GameObserver> {

    private static final Logger LOGGER = Logger.getLogger(GUIEngineObserverRegisterHandler.class);
    private static final String PARAM_IS_NULL_EXCEPTION_TEXT = "Observer must not be null!";

    @Autowired private GameEngineWrapper gameEngineWrapper;

    /**
     * Registriet die GUI bei der Spielmechanik.
     * <p>
     * Weiters:
     *
     * @inheritDoc
     */
    @Override
    public void registerMe(GameObserver objectToRegister) {
        if (objectToRegister == null) {
            throw new IllegalArgumentException(PARAM_IS_NULL_EXCEPTION_TEXT);
        }
        LOGGER.debug("Try to register " + objectToRegister);
        gameEngineWrapper.attachObserver(objectToRegister);
    }

}
