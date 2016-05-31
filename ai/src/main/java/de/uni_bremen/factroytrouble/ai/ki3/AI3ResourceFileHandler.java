/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.ai.ki3;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Lädt die Resourcen für den KI-Agenten 3
 *
 * @author Thorben, Andre
 */
public class AI3ResourceFileHandler {

    private static final Logger LOGGER = Logger.getLogger(AI3ResourceFileHandler.class);

    /**
     * Kopiert die Resourcen in ein gegebenes Verzeichnis
     *
     * @param path
     *      Ordner, in dem alles angelegt werden soll
     */
    public void copyResourcesToDir(String path) {
        File dest = new File(path);

        if (dest.exists()) {
            LOGGER.info("Ressourcen bereits vorhanden");
            return;
        }
        try {
            File zip = copyZipToUserDir(path);
            unzipFile(dest, zip);
            setExecutionLevelToFiles(path);
        } catch (IOException e) {
            LOGGER.error("Die Ressourcen konnten nicht ins Home-Verzeichnis kopiert werden", e);
        }
    }

    private File copyZipToUserDir(String path) throws IOException {
        File zip = new File(path.concat(File.separator).concat("ki3.zip"));
        FileUtils.copyInputStreamToFile(getClass().getResourceAsStream("/ki3.zip"), zip);
        return zip;
    }

    private void unzipFile(File dest, File zip) throws IOException {
        ZipFile zipFile = new ZipFile(zip);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();

        byte[] buffer = new byte[16384];
        int len;

        while (entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) entries.nextElement();

            String entryFileName = entry.getName();

            int lastIndex = entryFileName.lastIndexOf('/');
            String internalPathToEntry = entryFileName.substring(0, lastIndex + 1);
            File dir = new File(dest, internalPathToEntry);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            if (!entry.isDirectory()) {
                BufferedOutputStream bos = new BufferedOutputStream(
                        new FileOutputStream(new File(dest, entryFileName)));

                BufferedInputStream bis = new BufferedInputStream(zipFile
                        .getInputStream(entry));

                while ((len = bis.read(buffer)) > 0) {
                    bos.write(buffer, 0, len);
                }

                bos.flush();
                bos.close();
                bis.close();
            }
        }
        zipFile.close();
        zip.delete();
    }

    private void setExecutionLevelToFiles(String path) {
        File file = new File(path.concat(File.separator).concat("ccl").concat(File.separator).concat("unix").concat(File.separator).concat("lx86cl64"));
        file.setExecutable(true);
        file = new File(path.concat(File.separator).concat("ccl").concat(File.separator).concat("mac").concat(File.separator).concat("dx86cl64"));
        file.setExecutable(true);
    }

}
