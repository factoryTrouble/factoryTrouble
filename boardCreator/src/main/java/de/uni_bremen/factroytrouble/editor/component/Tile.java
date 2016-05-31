/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.editor.component;

import de.uni_bremen.factroytrouble.editor.data.FieldData;
import de.uni_bremen.factroytrouble.editor.data.GroundFill;
import de.uni_bremen.factroytrouble.editor.data.Orientation;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Repräsentiert ein Tile auf dem Board.
 *
 * @author Andre
 */
public class Tile extends ImageView {

    private GroundFill groundFill;
    private Orientation orientation;
    private Map<Orientation, FieldData> fieldDatas;
    private boolean startPosition;
    private Integer flagNumber;

    /**
     * Erzeugt ein neues Tile
     */
    public Tile() {
        super();
        setUpTile();
    }

    /**
     * Erzeugt ein neues Tile mit vordefiniertem Bild
     * @param image
     *      Angeziegt Bild vom Tile
     */
    public Tile(Image image) {
       super(image);
        setUpTile();
    }

    /**
     * Gibt die Art des Boden zurück
     * @return
     *      Art des Boden
     */
    public GroundFill getGroundFill() {
        return groundFill;
    }

    /**
     * Setzt die Art des Bodens und entfernt alle Elemente (Wände, Laser, Pusher und Startmarken) von diesem Feld
     * @param groundFill
     *      Art des Bodens
     */
    public void setGroundFill(GroundFill groundFill) {
        fieldDatas.clear();
        startPosition = false;
        this.groundFill = groundFill;
    }

    /**
     * Gibt für Förderbänder die Richtung zurück
     * @return
     *      Richtung des Förderbandes, ansonsten null
     */
    public Orientation getOrientation() {
        return orientation;
    }

    /**
     * Setzt die Orientierung für die Förderbänder
     * @param orientation
     *      Orientierung für ein Förderband
     */
    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    /**
     * Fügt eine Wand oder ein mit einer Wand verbundes Objekt hinzu
     *
     * @param fieldData
     *      Wand oder ein mit einer Wand verbundenes Objekt
     * @param orientation
     *      Richtung, in der die Wand oder das mit der Wand verbundene Objekt steht
     */
    public void addFieldData(FieldData fieldData, Orientation orientation) {
        fieldDatas.put(orientation, fieldData);
    }

    /**
     * Löscht eine Wand oder ein mit einer Wand verbundes Obejekt in gegebener Richtung
     * @param orientation
     *      Richtung, in der eine Wand oder eins mit einer Wand verbundes Objekt gelöscht werden soll
     */
    public void removeFieldData(Orientation orientation) {
        fieldDatas.remove(orientation);
    }

    /**
     * Gibt eine Wand oder ein mit einer Wand verbundes Objekt zurück
     * @param orientation
     *      Richtung, aus dem man die Wand oder ein mit einer Wand verbundes Objekt bekommen möchte
     * @return
     *      Die Wand oder ein mit einer Wand verbundenes Objekt in gegebener Richtung oder null
     */
    public FieldData getFieldData(Orientation orientation) {
        return fieldDatas.get(orientation);
    }

    /**
     * Prüft, ob sich eine Wand oder ein mit einer Wand verbundenes sich in gegeben Richtung befindet
     * @param orientation
     *      Die Richtung, wo geprüft werden soll
     * @return
     *      True, wann ein Objekt exisistiert, sonst False
     */
    public boolean fieldDataExists(Orientation orientation) {
        return fieldDatas.containsKey(orientation);
    }

    /**
     * Befindet sich eine Startmarkierung auf dem Feld
     * @return
     *      True = Ja, sonst False
     */
    public boolean isStartPosition() {
        return startPosition;
    }

    /**
     * Makiert das Feld mit einer Startmarkierung
     * @param startPosition
     *      Ist es eine Starmarkierung
     */
    public void setStartPosition(boolean startPosition) {
        this.startPosition = startPosition;
    }

    /**
     * Setzt ein zusammengesetztes Bild (Boden + Wand) und spiechert das Original intern zwischen
     * @param image
     *      Das Bild, was im folgenden angezeigt werden soll
     */
    public void setTileImage(Image image) {
        setImage(image);
    }

    /**
     * Gibt die Flaggen Nummer zurücl
     * @return
     *      Die Flaggennummer
     */
    public Integer getFlagNumber() {
        return flagNumber;
    }

    /**
     * Setzt die Flaggennummer
     * @param flagNumber
     *      Die Flaggennummer
     */
    public void setFlagNumber(Integer flagNumber) {
        this.flagNumber = flagNumber;
    }

    /**
     * Wrapper für setOnMouseClicked
     * @param value
     */
    public void addAdditionalClickEvent(EventHandler<? super MouseEvent> value) {
        setOnMouseClicked(value);
    }

    private void setUpTile() {
        setFitWidth(50.0);
        setFitHeight(50.0);
        fieldDatas = new HashMap<>();
    }
}
