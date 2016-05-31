/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gui.services.util;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parsed und verwaltet alle beim Start der Anwendung übergebenden Argumente
 *
 * @author André
 */
@Service
public class ApplicationArgumentsService {

    private static final Logger LOGGER = Logger.getLogger(ApplicationArgumentsService.class);
    private static final Pattern ARG_PATTERN = Pattern.compile("-dFT([^=]*)=((true)|(false))");

    private Map<String, Boolean> applicationArguments;

    /**
     * Initalisiert den Service
     */
    @PostConstruct
    public void init() {
        applicationArguments = new ConcurrentHashMap<>();
    }

    /**
     * Parsed die Programmargumente in eine Format, welches sich schnell lesen lässt.
     *
     * Die Form für ein Programm argument, um erkannt zu werden ist wie folgt:
     * -dFT[Name des Argumentes, ohne =]=[true|false]
     *
     * Bespiel:
     * -dFTAITestMode=false
     *
     * @param args
     *      Die Argumente vom Programmstart
     */
    public void parseArguments(String[] args) {
        for(String arg : args) {
            parse(arg);
        }
    }

    /**
     * Gibt den Wert eines Arument-Schlüssels zurück oder null, wenn dieser Wert nicht vorhanden ist
     *
     * @param argument
     *      Schlüssel des Arguments
     * @return
     *      Den Wert oder false
     */
    public Boolean getArgumentValue(String argument) {
        if(applicationArguments.get(argument) == null) {
            return false;
        }
        return applicationArguments.get(argument);
    }

    /**
     * Gibt eine Liste aller Schlüssel zurück
     *
     * @return
     *      Liste der Schlüssel
     */
    public List<String> getAllArgumentKeys() {
        return new ArrayList(applicationArguments.keySet());
    }

    private void parse(String arg) {
        Matcher argMatcher = ARG_PATTERN.matcher(arg);
        if(!argMatcher.find()) {
            LOGGER.debug("Can not parse argument " + arg);
            return;
        }
        applicationArguments.put(argMatcher.group(1), (argMatcher.group(2).equals("true")));
    }

}
