/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.editor.component;

import de.uni_bremen.factroytrouble.editor.ApplicationSettings;
import de.uni_bremen.factroytrouble.editor.data.FieldObject;
import de.uni_bremen.factroytrouble.editor.data.GroundFill;
import de.uni_bremen.factroytrouble.editor.data.Orientation;
import de.uni_bremen.factroytrouble.editor.service.util.ActiveEditorService;
import de.uni_bremen.factroytrouble.editor.spring.SpringConfigHolder;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

/**
 * Werzeug zum Bearbeiten des Spielfeldes.
 * Hier: Bodenflächen, Wände, Objekte am Wänden und Positionsmarken
 *
 * @author Andre
 *
 */
public class EditorTileFillToolIcon extends GridPane {

    private static final Double IMAGE_SIZE = 75.0;
    private static final Double PADDING = 5.0;
    private static final String BORDER_COLOR = ApplicationSettings.ORANGE;

    private GroundFill groundFill;
    private FieldObject fieldObject;
    private Orientation orientation;
    private ImageView toolImage;
    private Label toolLabel;

    /**
     * Erstellt ein neues EditorTileFillToolIcon.
     * @param fillImage
     *      Vorschaubild
     * @param label
     *      Label-Text
     * @param groundFill
     *      Art von Bodenfüllung
     */
    public EditorTileFillToolIcon(Image fillImage, String label, GroundFill groundFill) {
        super();
        this.groundFill = groundFill;
        initContent(fillImage, label);
        initClickHandler();
    }

    /**
     * Erstellt ein neues EditorTileFillToolIcon.
     * @param fillImage
     *      Vorschaubild
     * @param label
     *      Label-Text
     * @param groundFill
     *      Art von Bodenfüllung
     * @param orientation
     *      Die Orientierung für den Boden
     */
    public EditorTileFillToolIcon(Image fillImage, String label, GroundFill groundFill, Orientation orientation) {
        super();
        this.groundFill = groundFill;
        this.orientation = orientation;
        initContent(fillImage, label);
        initClickHandler();
    }

    /**
     * Erstellt ein neues EditorTileFillToolIcon.
     * @param fillImage
     *      Vorschaubild
     * @param label
     *      Label-Text
     * @param fieldObject
     *      Art der Wand oder das Objekt an der Wand
     */
    public EditorTileFillToolIcon(Image fillImage, String label, FieldObject fieldObject) {
        super();
        this.fieldObject = fieldObject;
        initContent(fillImage, label);
        initClickHandler();
    }

    /**
     * Setzt das Icon auf einen nicht geklickten zustand zurück
     */
    public void resetClickedStatus() {
        setPadding(new Insets(PADDING));
        setStyle("-fx-border-width: 0px");
    }

    /**
     * Gibt die Art der Bodenfüllung zurück
     * @return
     *      Die Bodenfüllung
     */
    public GroundFill getGroundFill() {
        return groundFill;
    }

    /**
     * Gibt die Art der Wand oder das mit der Wand verbundene Objekt zurück
     * @return
     *      Art der Wand oder einen mit einer wand verbundenes Objekt
     */
    public FieldObject getFieldObject() {
        return fieldObject;
    }

    /**
     * Gibt die Orientierung zurück
     * @return
     *      Die spezifische Orientierung für das verbundene Objekt
     */
    public Orientation getOrientation() {
        return orientation;
    }

    /**
     * Setzt die Bodenfüllung
     *
     * @param groundFill
     *      Art der Bodenfüllung
     */
    public void setGroundFill(GroundFill groundFill) {
        this.groundFill = groundFill;
    }

    /**
     * Setzt die Art der Wand oder das mit der Wand verbundene Objekt
     *
     * @param fieldObject
     *      Art der Wand oder einen mit einer wand verbundenes Objekt
     */
    public void setFieldObject(FieldObject fieldObject) {
        this.fieldObject = fieldObject;
    }

    /**
     * Setzt die Orientierung
     * @param orientation
     *      Die spezifische Orientierung für das verbundene Objekt
     */
    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    @Override
    public String toString() {
        return "EditorTileFillToolIcon{" +
                "groundFill=" + groundFill +
                ", fieldObject=" + fieldObject +
                ", orientation=" + orientation +
                '}';
    }

    private void initContent(Image fillImage, String label) {
        toolImage = new ImageView(fillImage);
        toolImage.setFitHeight(IMAGE_SIZE);
        toolImage.setFitWidth(IMAGE_SIZE);
        this.toolLabel = new Label(label);
        toolLabel.setWrapText(true);
        add(toolImage, 0, 0);
        add(this.toolLabel, 0, 1);
        setPadding(new Insets(PADDING));
    }

    private void initClickHandler() {
        setOnMouseClicked(mouseEvent -> {
            setPadding(new Insets(0));
            setStyle("-fx-border-width: " + PADDING.intValue() + "px;  -fx-border-style: solid; -fx-border-color: " + BORDER_COLOR + ";");
            SpringConfigHolder.getInstance().getContext().getBean(ActiveEditorService.class).getActiveEditor().setActiveTool(this);
        });
    }
}
