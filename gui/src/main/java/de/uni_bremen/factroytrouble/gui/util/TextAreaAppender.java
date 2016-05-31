/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.gui.util;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import org.apache.log4j.WriterAppender;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Log4J Appender für eine JavaFX Textbox
 *
 * @Author André
 */
public class TextAreaAppender extends WriterAppender {

    private TextArea textArea;

    /**
     * Erzeugt einen neuen Appender
     *
     * @param textArea
     *      Die Textarea, in die alles geschrieben werden soll
     */
    public TextAreaAppender(TextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void append(LoggingEvent loggingEvent) {
        if (textArea != null) {
            Platform.runLater(() ->textArea.appendText("[" + loggingEvent.getLevel().toString() + "]" +
                    "[" + loggingEvent.getLocationInformation().getClassName() +
                    " (" + loggingEvent.getLocationInformation().getMethodName() + ")" +
                    ":" + loggingEvent.getLocationInformation().getLineNumber() + "  -  " +
                    loggingEvent.getMessage().toString() +
                    System.getProperty("line.separator")));
        }
    }
}
