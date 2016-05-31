/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gui;

import javafx.scene.image.Image;

/**
 * Klasse für die Grundeinstellungen.
 * Enthält die niedrigste Auflösung und den Filepath zu den Screens.
 */
public class ApplicationSettings {
    public static final String JAVA_FX_VIEW_PATH = "/views/";
    public static final String APPLICATION_NAME = "FactoryTrouble";

    public static final Integer SPLASH_SCREEN_WIDTH = 600;
    public static final Integer SPLASH_SCREEN_HEIGHT = 400;
    public static final Integer WIDTH = 1024;
    public static final Integer HEIGHT = 768;
    public static final Integer MIN_WIDTH = 640;
    public static final Integer MIN_HEIGHT = 480;

    public static final Image APPLICATION_ICON = new Image(ApplicationSettings.class.getResourceAsStream("/game/icon.png"));
    public static final Image SPLASH_SCREEN = new Image(ApplicationSettings.class.getResourceAsStream("/game/splash.gif"));

    //Color Codes by http://flatuicolors.com/
    public static final String MIDNIGHT_BLUE = "#2c3e50"; //Background
    public static final String ORANGE = "#f39c12"; //Buttons, Listen, etc
    public static final String PUMPKIN = "#d35400"; //Akzente für Buttons, Listen, etc
    public static final String CLOUDS = "#ecf0f1"; //Text

    //App Args
    public static final String AI_TEST_MODE = "AITestMode";
    public static final String DEBUG_MODE = "DebugMode";
}
