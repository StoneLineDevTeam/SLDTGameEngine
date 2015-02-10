package net.sldt_team.gameEngine.util;

import net.sldt_team.gameEngine.GameApplication;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Functions for reading a zip file
 */
public class ZipFileUtilities {

    /**
     * Returns a list of files contained in the given archive (args: archive ZIP path)
     */
    public static ArrayList<String[]> getFiles(String archive) {
        ArrayList<String[]> fileList = new ArrayList<String[]>();
        ZipInputStream zipInputStream = null;
        ZipEntry zipEntry;
        Long size;

        try {
            zipInputStream = new ZipInputStream(new FileInputStream(archive));
            zipEntry = zipInputStream.getNextEntry();
            while (zipEntry != null) {
                String[] file = new String[3];
                file[0] = zipEntry.getName();
                size = zipEntry.getSize() / 1024;
                file[1] = size.toString() + " ko";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                file[2] = simpleDateFormat.format(new Date(zipEntry.getTime()));

                fileList.add(file);
                zipEntry = zipInputStream.getNextEntry();
            }
        } catch (FileNotFoundException ex) {
            GameApplication.engineLogger.log(Level.WARNING, null, ex);
        } catch (IOException e) {
            GameApplication.engineLogger.log(Level.WARNING, null, e);
        }
        try {
            assert zipInputStream != null;
            zipInputStream.close();
        } catch (IOException ex) {
            GameApplication.engineLogger.log(Level.WARNING, null, ex);
        }
        return fileList;
    }

    /**
     * Extracts the given file (String file) from the given archive (String archive), to a given directory (String destPath)
     */
    public static void extractTo(String archive, String file, String destPath) throws IOException {
        ZipInputStream zipInputStream;
        ZipEntry zipEntry;
        byte[] buffer = new byte[2048];

        zipInputStream = new ZipInputStream(new FileInputStream(archive));
        zipEntry = zipInputStream.getNextEntry();
        while (zipEntry != null) {
            if (zipEntry.getName().equalsIgnoreCase(file)) {
                FileOutputStream fileoutputstream = new FileOutputStream(destPath + file);
                int n;

                while ((n = zipInputStream.read(buffer, 0, 2048)) > -1) {
                    fileoutputstream.write(buffer, 0, n);
                }

                fileoutputstream.close();
                zipInputStream.closeEntry();
            }
            zipEntry = zipInputStream.getNextEntry();
        }

        zipInputStream.close();
    }
}