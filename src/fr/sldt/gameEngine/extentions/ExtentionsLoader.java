package fr.sldt.gameEngine.extentions;

import fr.sldt.gameEngine.GameApplication;
import fr.sldt.gameEngine.util.FileUtilities;
import fr.sldt.gameEngine.util.ZipFileUtilities;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExtentionsLoader {

    private List<Extention> extList;

    public ExtentionsLoader(){
        GameApplication.log.info("--> Initializing Extentions <--");
        File extdir = new File(GameApplication.getGameDir() + File.separator + "SLDTGameEngineExtentions" + File.separator);
        if (!extdir.exists()){
            if (!extdir.mkdirs()){
                GameApplication.log.warning("Could not create extentions directory...");
            }
        }

        extList = new ArrayList<Extention>();

        try {
            initializeExtentions(extdir);
        } catch (Exception e) {
            GameApplication.log.severe("A problem has occurred when trying to load extentions :");
            e.printStackTrace();
            return;
        }
        GameApplication.log.info("--> Extentions successfully initialized <--");
    }

    private Map<String, String> getExtentionInfos(File f){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(f));
            Map<String, String> result = new HashMap<String, String>();
            String line;
            while ((line = reader.readLine()) != null){
                String[] s = line.split(":->:");
                if (s.length > 1) {
                    result.put(s[0], s[1]);
                }
            }
            reader.close();
            return result;
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    private void initializeExtentions(File directoryToScan) throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        GameApplication.log.info("Scanning extentions directory...");

        File[] exts = directoryToScan.listFiles();
        int i = 0;
        assert exts != null;
        for (File f : exts){
            if (!f.isDirectory()){
                if (FileUtilities.getFileExtention(f).equals("jar")){
                    File extInfoFile = new File(GameApplication.getGameDir() + File.separator + "SLDTGameEngineExtentions" + File.separator + "EXTENTION.TXT");
                    ZipFileUtilities.extractTo(f.toString(), "EXTENTION.TXT", GameApplication.getGameDir() + File.separator + "SLDTGameEngineExtentions" + File.separator);
                    if (!extInfoFile.exists()){
                        GameApplication.log.warning("Your computer failed to extract zip file ; aborting extentions init...");
                        return;
                    }
                    Map<String, String> infos = getExtentionInfos(extInfoFile);
                    String version = infos.get("VERSION");
                    String name = infos.get("NAME");

                    new URLClassLoader(new URL[]{f.toURI().toURL()}, this.getClass().getClassLoader());
                    Extention ext = new Extention(name, version);
                    extList.add(ext);
                    extInfoFile.delete();
                    i++;
                }
            } else{
                GameApplication.log.warning("Ignoring directory : \"" + f.getName() + "\"");
            }
        }

        GameApplication.log.info("Directory scanned, " + i + " extentions found !");
    }

    public void loadUpExtentions(){
        GameApplication.log.info("Loading extentions...");
        for (Extention ext : extList) {
            GameApplication.log.info("Loaded " + ext.getExtentionName() + " V" + ext.getExtentionVersion() + " !");
        }
    }
}
