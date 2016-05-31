/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.editor.service.view;

import de.uni_bremen.factroytrouble.editor.ApplicationSettings;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.stereotype.Service;

/**
 * Wechselt die View im aktiven Fenster
 *
 * @author Andre
 *
 */
@Service
public class ChangeViewService {

    /**
     * Wechselt die View
     * @param currentView
     *      Die Stage, in welche die neue View reingeladen werden soll
     * @param targetView
     *      Die View
     */
    public void changeView(Parent currentView, Parent targetView) {
        Stage stage = (Stage) currentView.getScene().getWindow();
        Scene scene = new Scene(targetView, stage.getWidth(), stage.getHeight());
        stage.setScene(scene);
        scene.getStylesheets().add( getClass().getResource(ApplicationSettings.JAVA_FX_VIEW_PATH + "application.css").toExternalForm());
        stage.show();
    }

}
