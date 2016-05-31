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
import java.util.Random;

import de.uni_bremen.factroytrouble.ai.ki2.api.memory.LongTermMemory;
import de.uni_bremen.factroytrouble.ai.ki2.api.memory.Thought;
import de.uni_bremen.factroytrouble.ai.ki2.api.memory.WorkingMemory;
import de.uni_bremen.factroytrouble.ai.ki2.api.timeFeeling.MemoryTicker;
import de.uni_bremen.factroytrouble.ai.ki2.timeFeeling.ScullyMemoryTicker;

public class ScullyLongTermMemory implements LongTermMemory {

    // Kategorien, die als Ansatzpunkte für das Memory Netz dienen
    private Thought boardCategory;
    private List<String> boardNames;
    private static final String[] BOARD_NAMES = { "flag", "conveyor", "tile", "laser", "gear", "wall" };

    private Thought robotsCategory;
    private List<String> robotsNames;
    private static final String[] ROBOTS_NAMES = { "flinch_bot", "funkey", "mace_bot", "pound_bot", "lethar_bot",
            "titan_t17", "squint_bot", "whirlbot" };

    private Thought plansCategory;
    private List<String> plansNames;
    private static final String[] PLANS_NAMES = { "register" };

    private Thought experienceCategory;
    private List<String> expierienceNames;
    private static final String[] EXPERIENCE_NAMES = {};

    private Thought miscCategory;

    private WorkingMemory superMemory;
    private ScullyLongTermMemoryStorer storer;
    private ScullyLongTermMemoryFinder finder;
    private ScullyLongTermMemoryHelper helper;

    private Random rngesus;

    private MemoryTicker ticker;

    static final int DUMMY_VALUE_FOR_CONNECTIONS = 1;
    static final int MAX_NUMBER_OF_FLAGS = 4;
    static final int MAX_NUMBER_OF_ROBOTS = 8;
    static final int MAX_NUMBER_OF_PLAYERS = 8;
    static final String expectedName = "expectedName";

    // Alle Gespeicherten Gedanken als direkte Liste
    // Wird für sicheres Bearbeiten in der Memory benötigt
    private List<Thought> allThoughts;

    public ScullyLongTermMemory(WorkingMemory superMemory) {
        ticker = new ScullyMemoryTicker();
        ticker.addMemory(this);
        allThoughts = new ArrayList<>();
        helper = new ScullyLongTermMemoryHelper(this);
        storer = new ScullyLongTermMemoryStorer(this);
        finder = new ScullyLongTermMemoryFinder(this);
        boardCategory = new ScullyThought("boardCategory");
        robotsCategory = new ScullyThought("robotsCategory");
        plansCategory = new ScullyThought("plansCategory");
        miscCategory = new ScullyThought("miscCategory");
        experienceCategory = new ScullyThought("experienceCategory");
        this.superMemory = superMemory;
        fillNameLists();
        rngesus = new Random();
        addSubCategories();
    }

    /**
     * Sucht eine Information aus dem Gedanken Netzwerk. Unter Umständen wird
     * aus Gründen der Cognitivität ein ähnlicher aber falscher Gedanke
     * zurückgegeben. Die übergebene Liste enthält die Anfrage als Strings.
     * Siehe Thgought.getThoughtName() für Namenskonvention.
     * 
     * @param keys
     *            die übergebene Anfrage als String Liste
     * 
     * @return der gesuchte Gedanke oder null.
     */
    @Override
    public Thought getInformation(List<String> keys) {
        ticker.tick();
        return finder.getInformation(keys);
    }

    /**
     * @deprecated NICHT BENUTZEN!!! Hilfsmethode die nicht zur API gehört.
     *             Nimmt eine Anfrage in Form einer String List und filtert den
     *             Namen des gesuchten Thought heraus.
     * 
     * 
     * 
     * @param keys
     *            die Anfrage als String Liste
     * 
     * @retrurn der Name des Thought
     */
    @Deprecated
    public String getThoughtNameFromList(List<String> keys) {
        return helper.getThoughtNameFromList(keys);
    }

    /**
     * @deprecated NICHT BENUTZEN!!! Hilfsmethode die nicht zur API gehört. Wird
     *             von der getInformation Methode benutzt. Durchsucht cognitiv
     *             einen Gedanken und die verlinkten Gedanken nach einem
     *             gesuchten Gedanken. Der gesuchte Gedanke wird in einer String
     *             Liste beschrieben.
     * 
     * @param asked
     *            Die Suchanfrage
     * @param search
     *            Der durchsuchte Gedanke.
     * @return Der gesuchte Gedanke.
     */
    @Deprecated
    public Thought searchMap(List<String> asked, Thought search) {
        return finder.searchMap(asked, search);
    }

    /**
     * @deprecated NICHT BENUTZEN!!! Hilfsmethode die nicht zur API gehört.
     *             Vergleicht cognitiv zwei Gedanken. Überprüft ob zwei nicht
     *             exakt gleiche Gedanken trotzdem den gleichen Inhalt haben.
     * 
     * @param asked
     *            Der erste überprüfte Gedanke.
     * @param other
     *            Der zweite überprüfte Gedanke.
     * @return True wenn beide Gedanken als gleich angesehen werden können,
     *         sonst false.
     */
    @Deprecated
    public boolean wasThisThoughtMeant(Thought asked, Thought other) {
        if (asked.getThoughtName().equals(other.getThoughtName())) {
            return true;
        }
        return false;
    }

    @Override
    public void storeInMemory(Thought thought) {
        ticker.tick();
        allThoughts.add(thought);
        storer.storeInMemory(thought);
    }

    @Override
    public void power(Thought thought1, Thought thought2, int factor) throws IllegalArgumentException {
        thought1.power(thought2, factor);
        thought2.power(thought1, factor);
    }

    @Override
    public void weakness(Thought thought1, Thought thought2, int factor) {
        thought1.weak(thought2, factor);
        thought2.weak(thought1, factor);
    }

    @Override
    public void tickle() {
        for (Thought t : allThoughts) {
            for (Thought tt : t.getThoughts().keySet()) {
                t.weak(tt, 1);
            }
        }
    }

    public ScullyLongTermMemoryHelper getHelper() {
        return helper;
    }

    /**
     * @deprecated nur für Nutzung innerhalb der LongTermMemoryKlassen.
     * 
     * @return Liste von Boardnamen
     */
    @Deprecated
    public List<String> getBoardNames() {
        return boardNames;
    }

    /**
     * @deprecated nur für Nutzung innerhalb der LongTermMemoryKlassen.
     * 
     * @return Liste von Roboternamen
     */
    @Deprecated
    public List<String> getRobotsNames() {
        return robotsNames;
    }

    /**
     * @deprecated nur für Nutzung innerhalb der LongTermMemoryKlassen.
     * 
     * @return Liste von Plannamen
     */
    @Deprecated
    public List<String> getPlansNames() {
        return plansNames;
    }

    /**
     * @deprecated nur für Nutzung innerhalb der LongTermMemoryKlassen.
     * 
     * @return Liste von Erfahrungen
     */
    @Deprecated
    public List<String> getExpierienceNames() {
        return expierienceNames;
    }

    /**
     * @deprecated nur für Nutzung innerhalb der LongTermMemoryKlassen.
     * 
     * @return Boardkategorien
     */
    @Deprecated
    public Thought getBoardCategory() {
        return boardCategory;
    }

    /**
     * @deprecated nur für Nutzung innerhalb der LongTermMemoryKlassen.
     * 
     * @return Roboterkategorien
     */
    @Deprecated
    public Thought getRobotsCategory() {
        return robotsCategory;
    }

    /**
     * @deprecated nur für Nutzung innerhalb der LongTermMemoryKlassen.
     * 
     * @return Plankategorien
     */
    @Deprecated
    public Thought getPlansCategory() {
        return plansCategory;
    }

    /**
     * @deprecated nur für Nutzung innerhalb der LongTermMemoryKlassen.
     * 
     * @return Erfahrungskategorien
     */
    @Deprecated
    public Thought getExperienceCategory() {
        return experienceCategory;
    }

    /**
     * @deprecated nur für Nutzung innerhalb der LongTermMemoryKlassen.
     * 
     * @return miscs
     */
    @Deprecated
    public Thought getMiscCategory() {
        return miscCategory;
    }

    /**
     * @deprecated nur für Nutzung innerhalb der LongTermMemoryKlassen.
     * 
     * @return Supermemory
     */
    @Deprecated
    public WorkingMemory getSuperMemory() {
        return superMemory;
    }

    /**
     * @deprecated nur für Nutzung innerhalb der LongTermMemoryKlassen.
     * 
     * @return storer
     */
    @Deprecated
    public ScullyLongTermMemoryStorer getStorer() {
        return storer;
    }

    /**
     * @deprecated nur für Nutzung innerhalb der LongTermMemoryKlassen.
     * 
     * @return finder
     */
    @Deprecated
    public ScullyLongTermMemoryFinder getFinder() {
        return finder;
    }

    /**
     * @deprecated nur für Nutzung innerhalb der LongTermMemoryKlassen.
     * 
     * @return rngesus
     */
    @Deprecated
    public Random getRNGesus() {
        return rngesus;
    }

    /**
     * Füllt die Category Attribute mit einigen Thoughts die Únterkategorien
     * enthalten.
     */
    private void addSubCategories() {
        for (String s : boardNames) {
            Thought t = new ScullyThought(s);
            boardCategory.connectThoughts(t);
            t.connectThoughts(boardCategory);
        }
        for (String s : robotsNames) {
            Thought t = new ScullyThought(s);
            robotsCategory.connectThoughts(t);
            t.connectThoughts(robotsCategory);
        }
        for (String s : plansNames) {
            Thought t = new ScullyThought(s);
            plansCategory.connectThoughts(t);
            t.connectThoughts(plansCategory);
        }
        for (String s : expierienceNames) {
            Thought t = new ScullyThought(s);
            experienceCategory.connectThoughts(t);
            t.connectThoughts(experienceCategory);
        }
    }

    /**
     * Füllt die Namenslisten Attribute mit dem Inhalt der Finalen Namens
     * Arrays.
     */
    private void fillNameLists() {
        boardNames = new ArrayList<String>();
        for (String s : BOARD_NAMES) {
            boardNames.add(s);
        }
        robotsNames = new ArrayList<String>();
        for (String s : ROBOTS_NAMES) {
            robotsNames.add(s);
        }
        plansNames = new ArrayList<String>();
        for (String s : PLANS_NAMES) {
            plansNames.add(s);
        }
        expierienceNames = new ArrayList<String>();
        for (String s : EXPERIENCE_NAMES) {
            expierienceNames.add(s);
        }
    }
}
