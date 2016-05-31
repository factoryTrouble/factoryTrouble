/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.editor.service.save;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Speichert Elemente im Benutzerverzeichnis des Benutzers
 *
 * @author Andre
 */
@Service
public class SaveInUserDirService {

    private static final Logger LOGGER = Logger.getLogger(SaveInUserDirService.class);
    private static final String USER_DIR_PATH = System.getProperty("user.home") + File.separator + "factoryTrouble";
    private static final String FIELD_PREFIX = "FIELD_";
    private static final String DOCK_PREFIX = "DOCK_";

    /**
     * Speichert ein Board im Benutzerverzeichnis
     *
     * @param board
     *      Serialisiertes Board
     *
     * @param boardName
     *      Name des Boards
     */
    public void saveBoard(String board, String boardName) {
        File filePath = new File(USER_DIR_PATH + File.separator + "boards" + File.separator + "descriptions");
        writeFile(filePath, board, FIELD_PREFIX, boardName);
    }

    /**
     * Speichert ein Dock im Benutzerverzeichnis
     *
     * @param board
     *      Serialisiertes Dock
     *
     * @param boardName
     *      Name des Docks
     */
    public void saveDock(String board, String boardName) {
        File filePath = new File(USER_DIR_PATH + File.separator + "boards" + File.separator + "descriptions");
        writeFile(filePath, board, DOCK_PREFIX, boardName);
    }

    /**
     * Speichert ein Course in einer speziellen Datei
     *
     * @param courseDescription
     *      Beschreibung des Course
     *
     * @param courseManualName
     *      Name der Course Datei
     */
    public void saveCourse(String courseDescription, String courseManualName) {
        File filePath = new File(USER_DIR_PATH + File.separator + "course");
        if(!filePath.exists()) {
            filePath.mkdirs();
        }
        File courseManual = new File(filePath.getAbsolutePath() + File.separator + courseManualName);
        boolean courseManualAlwaysExists = courseManual.exists();
        BufferedWriter bw = null;
        try {
            if(!courseManualAlwaysExists) {
                courseManual.createNewFile();
            }
            FileWriter fw = new FileWriter(courseManual.getAbsoluteFile(), true);
            bw = new BufferedWriter(fw);
            if(courseManualAlwaysExists) {
                bw.write(System.getProperty("line.separator"));
            }
            bw.write(courseDescription);
        } catch (IOException e) {
            LOGGER.error("Fail to write file", e);
        } finally {
            if(bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    LOGGER.error("Fail to close writer", e);
                }
            }
        }
    }

    private void writeFile(File filePath, String content, String filePrefix, String fieldName) {
        if(!filePath.exists()) {
            filePath.mkdirs();
        }
        File boardFile = new File(filePath.getPath() + File.separator + filePrefix + fieldName);
        BufferedWriter bw = null;
        try {
            FileWriter fw = new FileWriter(boardFile.getAbsoluteFile());
            bw = new BufferedWriter(fw);
            bw.write(content);
        } catch (IOException e) {
            LOGGER.error("Fail to write file", e);
        } finally {
            if(bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    LOGGER.error("Fail to close writer", e);
                }
            }
        }
    }

}
