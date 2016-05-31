/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki3;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Schnittstelle zu Act-R
 * 
 * @author Thorben
 */
public class ActRUser {

    private static final Logger LOGGER = Logger.getLogger(ActRUser.class);
    private static final String PATH = System.getProperty("user.home").concat(File.separator).concat("factoryTrouble")
            .concat(File.separator).concat("ki3");

    private static final String RUN_LISP;

    static {
        String system = System.getProperty("os.name").split(" ")[0].toLowerCase();
        String marks = "";
        switch (system) {
        case "windows":
            system += File.separator.concat("wx86cl64.exe");
            marks = "\"";
            break;
        case "mac":
            system += File.separator.concat("dx86cl64");
            break;
        default:
            system = "unix".concat(File.separator).concat("lx86cl64");
            break;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(marks).append(PATH).append(File.separator).append("ccl").append(File.separator).append(system)
                .append(marks);

        RUN_LISP = builder.toString();
    }

    private static final String LOAD_ACT_R = "(load \"actr7/load-act-r.lisp\")";
    private static final String LOAD_AI_BASE = "(load \"ai/";
    private static final String LOAD_DATA_RESTORER = "(load \"ai/data-restorer.lisp\")";
    private static final String STORE_DATA_IN_DM = "(set-data ";
    private static final String MAKE_MOVE = "(make-move ";

    private String boardState;
    private String robotsState;
    private String handState;
    private int[] selected = new int[] { -1, -1, -1, -1, -1 };
    boolean interrupted;

    boolean powerDownStatus;

    private String aiName;

    private Runtime runtime = Runtime.getRuntime();
    private Process proc;
    private BufferedReader stdout;
    private BufferedWriter stdin;

    private boolean powerDownRequest = false;

    private ScoreManager scoreManager;
    private PathFinder pathFinder;

    public ActRUser(ScoreManager scoreManager) {
        this.scoreManager = scoreManager;
        new AI3ResourceFileHandler().copyResourcesToDir(PATH);
    }

    public void saveCurrentGameStatus() {
        boardState = scoreManager.getBoardState();
        handState = scoreManager.getHandState();
        robotsState = scoreManager.getRobotsState();
    }

    public void runPathFinder() {
        pathFinder = new PathFinder(scoreManager);
        pathFinder.execute();
        List<Point> checkPoints=pathFinder.getPath();
    }

    public boolean loadActR(String aiName) {
        try {
            // Prozess starten
            LOGGER.info("LISP wird gestartet");
            proc = runtime.exec(RUN_LISP, null, new File(PATH));

            stdin = new BufferedWriter(new OutputStreamWriter(proc.getOutputStream()));
            stdout = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            execCommand(stdin, LOAD_ACT_R, true);
            emptyOutputStream(null);
            execCommand(stdin, LOAD_AI_BASE.concat(aiName).concat(".lisp\")"), true);

            emptyOutputStream(null);
            execCommand(stdin, LOAD_DATA_RESTORER, true);
            emptyOutputStream(null);
            if ((boardState != null) && (robotsState != null) && (handState != null)) {
                execCommand(stdin, STORE_DATA_IN_DM.concat(boardState).concat(" ").concat(robotsState).concat(" ")
                        .concat(handState).concat(")"), true);
                emptyOutputStream(null);

            }
        } catch (IOException e) {
            LOGGER.error("Die Kommandozeileneingaben konnten nicht verarbeitet werden", e);
            return false;
        }
        LOGGER.info("Der angeforderte KI-Agent wurde erfolgreich geladen");
        return true;
    }

    public int[] terminateTurn() {
        interrupted = true;
        terminateLISP();
        return selected;
    }

    public int[] makeMove(String realtime) {
        selected = new int[] { -1, -1, -1, -1, -1 };
        interrupted = false;
        try {
            // Zug durchführen
            execCommand(stdin, MAKE_MOVE.concat(realtime).concat(")"), true);

            // Ergebnis auslesen
            String line;
            int[] cards = new int[5];
            LOGGER.info("LISP-output:");
            while (true) {
                line = stdout.readLine();
                if (interrupted) {
                    return null;
                }
                if (line.contains("FILL REGISTER WITH CARDS")) {
                    break;
                }
                if (line.contains("WITH CARD ")) {
                    setSelected(line, true);
                }
                if (line.contains("REMOVE CARD ")) {
                    setSelected(line, false);
                }
                if (line.contains("POWERDOWN")) {
                    powerDownRequest = true;
                }
                if (line.contains("STAYACTIVE")) {
                    powerDownRequest = false;
                }
                LOGGER.info(line);
            }
            LOGGER.info(line);
            String[] toPut = line.substring(25, line.length()).split(" ");

            for (int i = 0; i < toPut.length; i++) {
                cards[i] = Integer.valueOf(toPut[i]);
            }

            emptyOutputStream(null);

            // Ergebnis zurückgeben
            LOGGER.info("Zug wurde erfolgreich berechnet");
            return cards;
        } catch (IOException e) {
            LOGGER.error("Die Kommandozeileneingaben konnten nicht verarbeitet werden", e);
            return null;
        } finally {
            if (!interrupted) {
                terminateLISP();
            }
        }
    }

    public boolean getPowerDownStatus() {
        return powerDownStatus;
    }

    private boolean terminateLISP() {
        try {
            if (proc != null) {
                stdin.flush();
                stdin.close();
                stdout.close();

                proc.destroy();
            }
        } catch (IOException e) {
            LOGGER.error("LISP-Prozess konnte nicht terminiert werden", e);
            return false;
        }
        return true;
    }

    public String getactiveAIName() {
        return aiName;
    }

    public boolean getPowerDownRequest() {
        return powerDownRequest;
    }

    private void execCommand(BufferedWriter writer, String command, boolean logCommand) throws IOException {
        if (logCommand) {
            LOGGER.info("LISP-Befehl ".concat(command).concat(" wird ausgeführt"));
        }
        writer.write(command);
        writer.newLine();
        writer.flush();
    }

    private void emptyOutputStream(List<String> notPrinted) throws IOException {
        // Bisherigen Output beseitigen
        execCommand(stdin, "(+ 0 0)", false);

        String next = null;
        StringBuilder builder = new StringBuilder();

        if (notPrinted != null) {
            for (int i = 0; i < notPrinted.size(); i++) {
                builder.append(notPrinted).append("\n");
            }
        }

        while (!(next = stdout.readLine()).equals("? 0")) {
            builder.append(next).append("\n");
        }
        if (builder.length() > 0) {
            LOGGER.info("LISP-output:\n".concat(builder.toString()));
        }
    }

    private void setSelected(String trace, boolean fill) {
        String[] splitted = trace.split(" ");
        if (fill) {
            selected[Integer.parseInt(splitted[2])] = Integer.parseInt(splitted[5]);
        } else {
            selected[Integer.parseInt(splitted[4])] = -1;
        }
    }
}