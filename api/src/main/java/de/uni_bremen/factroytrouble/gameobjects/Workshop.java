/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gameobjects;

/**
 * Dieses Interface repräsentiert eine Werkstatt. Endet eine Registerphase eines
 * Roboters auf einer Werkstatt, wird dessen Checkpunkt darauf gesetzt (über
 * execute()). Endet der Zug des Roboters auf diesem Feld, verliert er einen
 * Schadenspunkt (über GameMaster oder Board)
 * 
 * @author Artur, Thorben
 *
 */
public interface Workshop extends FieldObject {

    /**
     * Gibt an, ob es sich um eine erweiterte Werkstatt handelt. Auf einer
     * erweiterten Werkstatt erhält ein Roboter bei Rundenende eine Optionskarte
     * 
     * @return @{code true}, wenn es sich um eine erweiterte Werkstatt
     *         handelt, @{code false} sonst
     */
    boolean isAdvancedWorkshop();

    @Override
    Workshop clone();

}
