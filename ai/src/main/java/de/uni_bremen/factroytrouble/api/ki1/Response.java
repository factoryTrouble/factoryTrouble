/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.api.ki1;

import java.util.List;

import de.uni_bremen.factroytrouble.player.ProgramCard;

/**
 * Das Response Modul ist für ausgehende, spielbeeinträchtigende Aktionen
 * eines Roboters zuständig.
 * 
 * @author tim
 */
public interface Response {
    /**
     * Füllt die Register mit den gegebenen Karten.
     * 
     * @param cards
     *            die Karten in Reihenfolge ihrer Register
     */
    void setCards(List<ProgramCard> cards);
}
