/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.editor.service.load;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Lädt den Inhalt einer Datei
 */
@Service
public class FileContentService {

    private static final Logger LOGGER = Logger.getLogger(FileContentService.class);

    /**
     * Liest den Inhalt einer Datei und legt diesen Zeile für Zeile in eine Liste
     *
     * @param file
     *      Datei, die gelsen werden soll
     *
     * @return
     *      Inahlt der Datei als Liste
     */
    public List<String> getFileContentSeperatedByLine(File file) {
        List<String> fileContentLines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                fileContentLines.add(line);
            }
        } catch (IOException e) {
            LOGGER.error("Fail to read file", e);
        }
        return fileContentLines;
    }

}
