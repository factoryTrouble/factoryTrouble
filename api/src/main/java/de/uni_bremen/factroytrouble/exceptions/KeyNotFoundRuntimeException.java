/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.exceptions;

/**
 * Exception falls ein Key nicht in der Properties Datei gefunden wurde.
 * @author Falko, Frederik
 *
 */
public class KeyNotFoundRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public KeyNotFoundRuntimeException(){
        super("Key not found");
    }

    public KeyNotFoundRuntimeException(String key){
        super(String.format("Key '%s' not found", key));
    }
}