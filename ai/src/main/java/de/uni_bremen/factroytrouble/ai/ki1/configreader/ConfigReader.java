/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki1.configreader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import de.uni_bremen.factroytrouble.exceptions.KeyNotFoundException;



/**
 * Oberklasse zum Auslesen der RoboterEigenschaften aus einer Datei.
 * 
 * @author Frederik Brinkmann
 *
 */
public class ConfigReader {

    private static final String KEYNOTFOUND = " doesn't exist";

    
    private final Properties properties;

    protected ConfigReader(String path) throws IOException {
        properties = new Properties();
        final InputStream input = getClass().getResourceAsStream(path);
        if(input == null){
            throw new IOException();
        }
        try {
            properties.load(input);
        } finally {
            input.close();
        }
    }

    /**
     * Gibt den String zum übergebenen Key zurück. Existiert der Key nicht wirft
     * die Funktion eine KeyNotFoundException.
     *
     * 
     * @param propertyName
     *            Key nach dem in der Config gesucht wird
     * @return String zum Key
     */
    public String getStringProperty(String propertyName) throws KeyNotFoundException {
        String tmp = properties.getProperty(propertyName);
        if (tmp == null) {
            throw new KeyNotFoundException("StringProperty: " + propertyName + KEYNOTFOUND);
        }
        return tmp;
    }

    /**
     * Gibt den Double zum übergebenen Key zurück. Existiert der Key nicht wirft
     * die Funktion eine KeyNotFoundException.
     * 
     * @param propertyName
     *            Key nach dem in der Config gesucht wird
     * @return double zum Key
     */
    public double getDoubleProperty(String propertyName) throws KeyNotFoundException {
        String tmp = properties.getProperty(propertyName);
        if (tmp == null) {
            throw new KeyNotFoundException("DoubleProperty: " +propertyName + KEYNOTFOUND);
        }
        return Double.parseDouble(tmp);
    }

    /**
     * Gibt den int zum übergebenen Key zurück. Existiert der Key nicht wirft
     * die Funktion eine KeyNotFoundException.
     * 
     * @param propertyName
     *            Key nach dem in der Config gesucht wird
     * @return int zum Key
     */
    public int getIntProperty(String propertyName) throws KeyNotFoundException {
        String tmp = properties.getProperty(propertyName);
        if (tmp == null) {
            throw new KeyNotFoundException("IntProperty: " +propertyName + KEYNOTFOUND);
        }
        return Integer.parseInt(tmp);
    }

    /**
     * Gibt den Boolean zum übergebenen Key zurück. Existiert der Key nicht
     * wirft die Funktion eine KeyNotFoundException.
     * 
     * @param propertyName
     *            Key nach dem in der Config gesucht wird
     * @return Boolean zum Key
     */
    public boolean getBoolProperty(String propertyName) throws KeyNotFoundException {
        String tmp = properties.getProperty(propertyName);
        if (tmp == null) {
            throw new KeyNotFoundException("BoolProperty: " +propertyName + KEYNOTFOUND);
        }
        return Boolean.parseBoolean(tmp);
    }
}
