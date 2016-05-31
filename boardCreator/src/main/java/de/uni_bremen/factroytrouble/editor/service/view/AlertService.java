/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.editor.service.view;

import javafx.scene.control.Alert;
import org.springframework.stereotype.Service;

/**
 * Zeigt ein Alert
 *
 * @author André
 */
@Service
public class AlertService {

    /**
     * Zeigt einen Alert
     *
     * @param headline
     *      Text der Überschrift
     *
     * @param content
     *      Inhalt des Alerts
     *
     * @param alertType
     *      Typ des Alerts
     */
    public void showAlert(String headline, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(headline);
        alert.setHeaderText(null);
        alert.setContentText(content);

        alert.showAndWait();
    }

    /**
     * Zeigt einen Info Alert
     *
     * @param headline
     *      Text der Überschrift
     *
     * @param content
     *      Inhalt des Alerts
     */
    public void showInfoAlert(String headline, String content) {
        showAlert(headline, content, Alert.AlertType.INFORMATION);
    }

}
