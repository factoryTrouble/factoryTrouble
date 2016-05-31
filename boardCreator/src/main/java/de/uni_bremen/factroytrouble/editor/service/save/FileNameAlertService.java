/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.editor.service.save;

import javafx.scene.control.TextInputDialog;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Zeigt einen Alert zum speichern von Elementen an
 *
 * @author Andre
 */
@Service
public class FileNameAlertService {

    /**
     * Zeigt den Namens abfrage Dialog
     * @param saveFileType
     *      Typ des Objektes, welchses gespeichert werdne soll
     * @return
     *      Name der neuen Datei oder null, wenn der Name nicht bestätigt wurde
     */
    public String showFilenameAlert(String saveFileType) {
        TextInputDialog fileNameDialog = new TextInputDialog("");
        fileNameDialog.setTitle(saveFileType + " speichern");
        fileNameDialog.setHeaderText("Geben Sie den Namen des " + saveFileType + " ein.\nAchtung: Wenn der Name beriets vohanden ist, wird die vorhanden Datei überschireben");
        fileNameDialog.setContentText("Name des " + saveFileType + ":");
        Optional<String> result = fileNameDialog.showAndWait();
        if (result.isPresent()){
            return result.get();
        }
        return null;
    }

}
