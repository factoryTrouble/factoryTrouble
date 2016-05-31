/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ais;

import de.uni_bremen.factroytrouble.ai.AIFactory;
import de.uni_bremen.factroytrouble.ai.AIPlayer;
import de.uni_bremen.factroytrouble.player.MasterFactory;
import de.uni_bremen.factroytrouble.player.Player;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.uni_bremen.factroytrouble.player.PlayerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Implementierung der AIFactory
 * 
 * @author Thorben
 */
@Component
public class GlobalAIFactory implements AIFactory {

    private static final String PATH = "de.uni_bremen.factroytrouble.ai.ais.";
    private static final Logger LOGGER = Logger.getLogger(GlobalAIFactory.class);

    private boolean factoriesInitialized = false;

    @Autowired
    private MasterFactory mFactory;
    @Autowired
    private PlayerFactory pFactory;

    @Override
    public AIPlayer getAIPlayer(int gameId,String aiName, Player player) {
        try {
            Class<?> aiClass = Class.forName(PATH.concat(aiName));
            if (!AIPlayer.class.isAssignableFrom(aiClass)) {
                throw new ClassNotFoundException();
            }
            Constructor<?> aiConstructor = aiClass.getConstructor(Integer.class, Player.class);
            AIPlayer aiPlayer = (AIPlayer) aiConstructor.newInstance(gameId,player);
            LOGGER.info("Neue KI vom Typ [".concat(aiName).concat("] erzeugt"));
            return aiPlayer;
        } catch (ClassNotFoundException e) {
            LOGGER.error("Eine KI mit angegebenem Namen existiert nicht!", e);
        } catch (NoSuchMethodException e) {
            LOGGER.error("Die angegebene KI besitzt keinen Konstruktor mit dem Parameter [Integer, Player]!", e);
        } catch (SecurityException e) {
            LOGGER.error("Error in AIFactory", e);
        } catch (InstantiationException e) {
            LOGGER.error("Die angegebene KI konnte nicht instantiiert werden!", e);
        } catch (IllegalAccessException e) {
            LOGGER.error("Die angegebene KI besitzt keinen public-Konstruktor mit dem Parameter [Robot]", e);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Der angegebenen KI wurde kein Roboter im Konstruktor übergeben!", e);
        } catch (InvocationTargetException e) {
            LOGGER.error("Error in AIFactory", e);
        }
        return new RandomAI(gameId,player);
    }

}
