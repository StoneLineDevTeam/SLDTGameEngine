package fr.sldt.gameEngine.util;

import fr.sldt.gameEngine.GameApplication;
import fr.sldt.gameEngine.exception.GameException;
import fr.sldt.gameEngine.exception.code.ErrorCode004;

import java.io.*;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class FileUtilities {
    public static void delete(File file)
            throws IOException {

        if(file.isDirectory()){
            System.out.println("Deleting directory " + file.getAbsolutePath());
            //directory is empty, then delete it
            if(file.list().length==0){
                if (file.delete())
                    System.out.println("Directory is deleted : " + file.getAbsolutePath());
            }else{
                //list all the directory contents
                String files[] = file.list();

                for (String temp : files) {
                    //construct the file structure
                    File fileDelete = new File(file, temp);

                    //recursive delete
                    delete(fileDelete);
                }

                //check the directory again, if empty then delete it
                if(file.list().length==0){
                    if (file.delete())
                        System.out.println("Directory is deleted : " + file.getAbsolutePath());
                }
            }
        }else{
            //if file, then delete it
            if (file.delete())
                System.out.println("File is deleted : " + file.getAbsolutePath());
        }
    }

    private static byte[] createChecksum(InputStream stream){
        try {
            byte[] buffer = new byte[1024];
            MessageDigest complete = MessageDigest.getInstance("MD5");
            int numRead;

            do {
                numRead = stream.read(buffer);
                if (numRead > 0) {
                    complete.update(buffer, 0, numRead);
                }
            } while (numRead != -1);

            stream.close();
            return complete.digest();
        } catch(Exception e){
            throw new GameException(new ErrorCode004());
        }
    }

    public static String getMD5Checksum(InputStream stream){
        byte[] b = createChecksum(stream);
        String result = "";

        for (int i=0; i < b.length; i++) {
            result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        return result;
    }

    public static File getResourcesDirectory(){
        return new File(GameApplication.getGameDir() + File.separator + "resources" + File.separator);
    }

    public static File getSavesDirectory(){
        return new File(GameApplication.getGameDir() + File.separator + "save" + File.separator);
    }

    public static File getFile(String s){
        return new File(GameApplication.getGameDir() + File.separator, s);
    }

    public static boolean copyFile(File from, File to){
        if (!from.exists())
            return false;

        if (from.isDirectory()){
            if (!to.exists()) {
                to.mkdirs();
            }
            File[] files = from.listFiles();
            List<Boolean> bools = new ArrayList<Boolean>();
            for (File f : files){
                if (f.isDirectory()){
                    bools.add(copyFile(f, new File(to + File.separator + f.getName())));
                } else {
                    bools.add(copySingleFile(f, new File(to + File.separator + f.getName())));
                }
            }
            for (Boolean b : bools){
                if (!b){
                    return false;
                }
            }
            return true;
        } else {
            return copySingleFile(from, to);
        }
    }

    private static boolean copySingleFile(File theFile, File newFile){
        try {
            FileInputStream in = new FileInputStream(theFile);
            FileOutputStream out = new FileOutputStream(newFile);

            byte[] b = new byte[1024];
            int length;
            while ((length = in.read(b)) > 0){
                out.write(b, 0, length);
            }

            in.close();
            out.close();
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    public static String getFileExtention(File f){
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
}
