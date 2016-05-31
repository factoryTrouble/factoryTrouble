/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gui.generator.image;

import java.awt.Image;

/**
 * Speichert ein Bild
 */
public interface SaveImageService {

    /**
     * Speichert ein Bild mit gegebenen Pfad
     * @param image
     *      Das Bild (muss Renderable sein)
     * @param path
     *      Der Pfad, wo das Bild gespeichert werden soll
     * @param fileName
     *      Der Name des Bildes mit Dateieindung
     */
    void save(Image image, String path, String fileName);

    /**
     * Speichert ein Bild mit gegebenen Pfad. Zusätzlich wird das bild noch um einen Faktor verkleinert
     * @param image
     *      Das Bild (muss Renderable sein)
     * @param path
     *      Der Pfad, wo das Bild gespeichert werden soll
     * @param fileName
     *      Der Name des Bildes mit Dateieindung
     * @param resizeFactor
     *      Um wie viel das Bild verkleinert werden soll
     */
    void saveResized(Image image, String path, String fileName, Integer resizeFactor);

}
