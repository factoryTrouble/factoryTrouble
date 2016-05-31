/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki2.ai.memory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;

import de.uni_bremen.factroytrouble.ai.ki2.api.memory.Thought;

/**
 * 
 * @author Thore
 * 
 *         Hilfsklasse für das LTM. Sollte nur von ScullyLongTermMemory aus
 *         aufgerufen werden. Wenn ihr sie direkt benutzt macht ihr ziemlich
 *         sicher was falsch.
 * 
 */
public class ScullyLongTermMemoryHelper {

    private static final Logger LOGGER = Logger.getLogger(ScullyLongTermMemoryHelper.class);

    ScullyLongTermMemory main;

    public ScullyLongTermMemoryHelper(ScullyLongTermMemory main) {
        this.main = main;
    }

    /**
     * NICHT BENUTZEN!!! Hilfsmethode die nicht zur API gehört. Nimmt eine
     * Anfrage in Form einer String List und filtert den Namen des gesuchten
     * Thought heraus.
     * 
     * @param keys
     *            die Anfrage als String Liste
     * 
     * @retrurn der Name des Thought
     */
    public String getThoughtNameFromList(List<String> keys) {
        String name = "";
        if (keys.contains(ScullyLongTermMemory.expectedName)) {
            return keys.get(keys.indexOf(ScullyLongTermMemory.expectedName) + 1);
        }
        for (int i = 0; i < keys.size(); i++) {
            name += keys.get(i);
            if (i < keys.size() - 1) {
                name += "_";
            }
        }
        if (!"".equals(name)) {
            keys.add(ScullyLongTermMemory.expectedName);
            keys.add(name);
            return name;
        }
        return null;
    }

    /**
     * NICHT BENUTZEN!!! Hilfsmethode die nicht zur API gehört. Sucht mögliche
     * Gedanken mit denen der neue Gedanke verbunden werden kann.
     * 
     * @param thought
     *            der neue Gedanke.
     * @return eine Liste mit Namen zu denen der neue Gedanke verbunden werden
     *         kann.
     */
    public List<Thought> findToughtsToConnectTo(Thought thought) {
        LOGGER.debug("findToughtsToConnectTo: Suche Gedanken zu denen Thought: " + thought.getThoughtName()
                + " verbunden werden könnte.");
        List<Thought> connectors = new ArrayList<Thought>();
        List<String> names = possibleNamesToConnectTo(thought);
        LOGGER.debug("findToughtsToConnectTo: Mögliche Namen von Gedanken zu denen verbunden werden könnte: " + names);
        for (String s : names) {
            LOGGER.debug(
                    "findToughtsToConnectTo: Durchsuche STM nach Name " + s + " um Gedanken zum verbinden zu finden.");
            List<String> buff = new ArrayList<String>();
            buff.add(s);
            Thought t = main.getSuperMemory().getThoughtFromShortTerm(buff);
            if (t != null) {
                LOGGER.debug("findToughtsToConnectTo: Gedanke " + t.getThoughtName() + " in STM gefunden.");
                connectors.add(t);
            }
        }
        if (connectors.isEmpty()) {
            LOGGER.debug("findToughtsToConnectTo: Keine Gedanken in STM gefunden, suche mögliche Kategorie in LTM.");
            connectors.add(findCategoryToConnectTo(thought));
        }
        LOGGER.debug("findToughtsToConnectTo: Folgende Gedanken wurden gefunden: " + connectors);
        return connectors;
    }

    /**
     * NICHT BENUTZEN!!! Hilfsmethode die nicht zur API gehört. Sucht die
     * Kategorie zu der der neue Gedanke verlinkt werden kann.
     * 
     * @param thought
     *            der neue Gedanke
     * @return die Kategorie zu der der Gedanke hinzugefügt.
     */
    public Thought findCategoryToConnectTo(Thought thought) {
        LOGGER.debug(
                "findCategoryToConnectTo: Suche Kategorie zu der Gedanke " + thought.getThoughtName() + " gehört.");
        Thought connect = null;
        List<String> name = splitUpThoughtName(thought.getThoughtName());
        LOGGER.debug("findCategoryToConnectTo: Teile Namen des Gedanken " + thought.getThoughtName() + " auf in " + name
                + ".");
        for (String s : name) {
            LOGGER.debug("findCategoryToConnectTo: Suche Kategorien zu Namensteil " + s + ".");
            Thought cat = null;
            cat = findCategoryToString(s);
            LOGGER.debug("findCategoryToConnectTo: Kategorie " + cat + " gefunden.");
            if ((connect == null || thoughtNameEquals(connect, main.getMiscCategory())) && cat != null) {
                connect = cat;
            } else {
                connect = main.getMiscCategory();
                LOGGER.debug(
                        "findCategoryToConnectTo: Neue Kategorie gefunden. Kategorie nicht eindeutig bestimmbar, MiscCategory gewählt.");
                break;
            }
        }
        LOGGER.debug("findCategoryToConnectTo: Zum Thought " + thought.getThoughtName()
                + " wurden folgende Kategorie gefunden " + connect.getThoughtName());
        return connect;
    }

    /**
     * NICHT BENUTZEN!!! Hilfsmethode die nicht zur API gehört. Sucht die
     * Kategorie zu der der gegebenen Name zugeordnet wird.
     * 
     * @param s
     *            der gegebene Name.
     * @return die Kategorie
     */
    public Thought findCategoryToString(String s) {
        Thought connect = null;
        if (main.getBoardNames().contains(s)) {
            connect = main.getBoardCategory();
        } else if (main.getRobotsNames().contains(s)) {
            connect = main.getRobotsCategory();
        } else if (main.getPlansNames().contains(s)) {
            connect = main.getPlansCategory();
        } else if (main.getExpierienceNames().contains(s)) {
            connect = main.getExperienceCategory();
        } else {
            connect = main.getMiscCategory();
        }
        return connect;
    }

    /**
     * NICHT BENUTZEN!!! Hilfsmethode die nicht zur API gehört. Sucht kognitiv
     * mögliche Namen von Gedanken zu denen der neue Gedanke hinzugefügt werden
     * kann.
     * 
     * @param thought
     *            Der neue Gedanke
     * @return Mögliche Namen.
     */
    public List<String> possibleNamesToConnectTo(Thought thought) {
        List<String> connect = new ArrayList<String>();
        String name = thought.getThoughtName();
        List<String> nameParts = splitUpThoughtName(name);
        findConnectionsForFlag(connect, nameParts);
        if (stringListsShareOneElement(nameParts, main.getRobotsNames())) {
            findConnectionsToRobot(connect, nameParts);
        }
        return connect;
    }

    /**
     * NICHT BENUTZEN!!! Hilfsmethode die nicht zur API gehört. Überprüft ob der
     * neue Gedanke zur FlaggenKategorie gehört und fügte mögliche Namen zur
     * Liste der verlinkbaren Thoughts hinzu.
     * 
     * @param connect
     *            Liste der verlinkten Thoughtnamen
     * @param nameParts
     *            Namensteile des neuen Gedanken
     */
    public void findConnectionsForFlag(List<String> connect, List<String> nameParts) {
        try {
            int number = Integer.parseInt(nameParts.get(nameParts.indexOf("flag") + 1));
            if (nameParts.size() > 2 && number <= ScullyLongTermMemory.MAX_NUMBER_OF_FLAGS && number > 0) {
                String connectString = "flag_" + number;
                connect.add(connectString);
            }
        } catch (NumberFormatException | IndexOutOfBoundsException i) {
            LOGGER.debug(i);
        }
        if (nameParts.contains("flag") || nameParts.contains("nextFlag")) {
            connect.add("flag");
        }
    }

    /**
     * NICHT BENUTZEN!!! Hilfsmethode die nicht zur API gehört. Fügt der connect
     * Liste Namen hinzu, zu deren Thoughts Verbindungen hergestellt werden
     * könnten.
     * 
     * @param connect
     * @param nameParts
     */
    public void findConnectionsToRobot(List<String> connect, List<String> nameParts) {
        for (String s : nameParts) {
            if (main.getRobotsNames().contains(s) && nameParts.size() > nameParts.indexOf(s) + 1) {
                connect.add(s);
            } else if (main.getRobotsNames().contains(s)) {
                connect.add("robot");
            }
        }
    }

    /**
     * NICHT BENUTZEN!!! Hilfsmethode die nicht zur API gehört. Vergleicht zwei
     * Listen auf gemeinsame Inhalte.
     * 
     * @param one
     *            Liste eins
     * @param two
     *            Liste zwei
     * @return {@code true} wenn gemeinsame Inhalte, sonst {@code false}
     */
    public boolean stringListsShareOneElement(List<String> one, List<String> two) {
        List<String> oneC = stringListToLowerCase(one);
        List<String> twoC = stringListToLowerCase(two);
        for (String o : oneC) {
            if (twoC.contains(o)) {
                return true;
            }
        }
        return false;
    }

    /**
     * NICHT BENUTZEN!!! Hilfsmethode die nicht zur API gehört. Macht alle
     * Strings in einer Liste klein.
     * 
     * @param upper
     *            Liste mit Strings mit Großbuchstaben
     * @return Liste mit Strings ohne Großbuchstaben
     */
    public List<String> stringListToLowerCase(List<String> upper) {
        List<String> lower = new ArrayList<>();
        for (String s : upper) {
            lower.add(s.toLowerCase());
        }
        return lower;
    }

    /**
     * NICHT BENUTZEN!!! Hilfsmethode die nicht zur API gehört. Teilt einen
     * Namen eines Gedanken in seine Einzelteile auf. Siehe {@code Thought.java}
     * .getThoughtName() für Namenskonvention.
     * 
     * @param name
     *            Der Name des Thought
     * @return Die Einzelteile als Liste.
     */
    public List<String> splitUpThoughtName(String name) {
        List<String> nameParts = new ArrayList<String>();
        String[] buff = name.split("_");
        for (String s : buff) {
            if (s.length() > 0) {
                nameParts.add(s);
            }
        }
        return nameParts;
    }

    public int correlatingStringFactor(String one, String two) {
        if (one == null || two == null) {
            return 0;
        }
        String oneF = one.toLowerCase();
        String twoF = two.toLowerCase();
        int factor = 0;
        if (oneF.equals(twoF)) {
            return 100;
        }
        String[] oneParts = oneF.split("_");
        String[] twoParts = twoF.split("_");
        int mod = 100 / Math.max(twoParts.length, oneParts.length);
        for (String s : oneParts) {
            for (String s2 : twoParts) {
                if (wasThisStringMeant(s, s2)) {
                    factor += mod;
                }
            }
        }
        return factor;
    }

    /**
     * NICHT BENUTZEN!!! Hilfsmethode die nicht zur API gehört. Überprüft
     * Überprüft ob zwei nicht exakt gleiche Strings vergleichbaren Inhalt
     * haben.
     * 
     * @deprecated ( Hilfsmethode die nicht zur API gehört. Überprüft Überprüft
     *             ob zwei nicht exakt gleiche Strings vergleichbaren Inhalt
     *             haben.)
     * 
     * @param asked
     *            String eins
     * @param other
     *            String zwei
     * @return {@code True} wenn Inhalt vergleichbar sonst {@code false}
     */
    @Deprecated
    public boolean wasThisStringMeant(String asked, String other) {
        String askedC = asked.toLowerCase(Locale.ROOT);
        String otherC = other.toLowerCase(Locale.ROOT);
        if (askedC.equals(otherC)) {
            return true;
        }
        return false;
    }

    /**
     * NICHT BENUTZEN!!! Hilfsmethode die nicht zur API gehört. Überprüft
     * Überprüft ob zwei nicht exakt gleiche String List vergleichbaren Inhalt
     * haben.
     * 
     * @param asked
     *            String Liste eins
     * @param other
     *            String Liste zwei
     * @return {@code True} wenn Inhalt vergleichbar sonst {@code false}
     */
    public boolean wasThisStringListMeant(List<String> asked, List<String> other) {
        if (asked.equals(other)) {
            return true;
        }
        return false;
    }

    private boolean thoughtNameEquals(Thought asked, Thought other) {
        return asked.getThoughtName().equals(other.getThoughtName());
    }

}
