/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.editor.service.save;

import de.uni_bremen.factroytrouble.editor.data.Orientation;
import de.uni_bremen.factroytrouble.editor.data.TileWithPosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Speichert ein Course
 *
 * @author André
 */
@Service
public class SaveCourseService {

    private static final String CUSTOM_COURSE_MANUAL_NAME = "CUSTOM_COURSE_MANUAL";
    private static final String CUSTOM_COURSE_PREFIX = "aaa-custom-";
    @Autowired private SaveInUserDirService saveInUserDirService;

    /**
     * Seraliziert und speichert ein Course
     *
     * @param courseName
     *      Name des Course
     *
     * @param boardName
     *      Name des Boards
     *
     * @param dockName
     *      Name des Docks
     *
     * @param boardOrientation
     *      Richting des Boards
     *
     * @param flags
     *      Alle Flaggen
     */
    public void save(String courseName, String boardName, String dockName, Orientation boardOrientation , Map<Integer, TileWithPosition> flags) {
        String courseDescription = CUSTOM_COURSE_PREFIX + courseName + "," + dockName + "," + boardName + "_" + boardOrientation.toString().toUpperCase();
        for(int i = 1; i <= flags.size(); i++) {
            courseDescription += ",";
            courseDescription += String.valueOf((int)(flags.get(i).getPoint().getX()));
            courseDescription += "_";
            courseDescription += String.valueOf((int)(flags.get(i).getPoint().getY()));
        }
        saveInUserDirService.saveCourse(courseDescription, CUSTOM_COURSE_MANUAL_NAME);
    }

}
