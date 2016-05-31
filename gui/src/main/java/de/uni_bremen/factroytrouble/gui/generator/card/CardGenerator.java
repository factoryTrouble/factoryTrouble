/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gui.generator.card;

import de.uni_bremen.factroytrouble.player.ProgramCard;

import java.util.List;

/**
 * Generiert aus den PrgrammCards Opjekte, die in der GUI angezeigt werden können
 *
 * @param <T>
 *      Typ des Objektes, welches in der GUI angezeigt werden kann
 *
 * @author Andre Ohrlogge
 */
public interface CardGenerator<T> {

    /**
     * Generiert aus einen Liste von Karten, eine Liste an anzeigbaren Objekten
     *
     * @param programCards
     *      Liste der Programmkarten
     *
     * @return
     *      Liste an anzeigbaren Objekten
     */
    List<T> generateAllAllCards(List<ProgramCard> programCards);

    /**
     * Generiert aus eine Programmkarte ein anziegbares Objekt
     *
     * @param programCard
     *      Eine Programmkarte
     *
     * @return
     *      Ein anzeigbares Objekt
     */
    T generateCard(ProgramCard programCard);
}
