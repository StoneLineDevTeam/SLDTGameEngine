package fr.sldt.gameEngine.renderengine;

import fr.sldt.gameEngine.GameApplication;
import fr.sldt.gameEngine.util.ZipFileUtilities;
import fr.sldt.gameEngine.util.FileUtilities;
import fr.sldt.gameEngine.exception.GameException;
import fr.sldt.gameEngine.exception.code.ErrorCode005;
import fr.sldt.gameEngine.renderengine.assetSystem.AssetType;

import java.io.*;
import java.util.ArrayList;

public class AssetManager {

    private File assetFile = null;
    private RenderEngine renderEngine;
    private AssetType type;

    public AssetManager(String fileLocation, AssetType t){
        if (t.isZIP() || t.isGAF()) {
            assetFile = new File(fileLocation + ".assets." + t.getAssetFileExtention());
            type = t;
        }
    }

    public void initialize(RenderEngine engine){
        renderEngine = engine;
        if (type.isZIP()){
            loadAssetFileAsZIP();
        } else if (type.isGAF()){
            loadAssetFileAsGAF();
        }
    }

    private void loadAssetFileAsZIP(){
        GameApplication.log.info("RENDER ENGINE : Extracting game assets...");
        ArrayList<String[]> var = ZipFileUtilities.getFiles(assetFile.toString());
        ArrayList<String> list = new ArrayList<String>();
        ArrayList<File> list1 = new ArrayList<File>();
        for (String[] var1 : var){
            String s = var1[0];
            list.add(s);
            try {
                String s1 = s.replace("/", File.separator);
                File var2 = new File(GameApplication.getGameDir() + File.separator + "assetCache" + File.separator + s1 + " ");
                System.out.println(var2.toString());
                File var5 = var2.getParentFile();
                if (!var5.exists()){
                    var5.mkdirs();
                }

                File f1 = new File(GameApplication.getGameDir() + File.separator + "assetCache" + File.separator + s);
                if (!f1.isDirectory())
                ZipFileUtilities.extractTo(assetFile.toString(), s, GameApplication.getGameDir() + File.separator + "assetCache" + File.separator);

                list1.add(new File(GameApplication.getGameDir() + File.separator + "assetCache" + File.separator + s1));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        renderEngine.logger.info("RENDER ENGINE : Game assets extraction ended with no errors");

        renderEngine.logger.info("RENDER ENGINE : Mounting game assets...");
        renderEngine.loadAssets(list, list1);
        if (renderEngine.hasAsset("backgrounds/sldtBG.png")){
            try {
                String s = (FileUtilities.getMD5Checksum(new FileInputStream(new File(GameApplication.getGameDir() + File.separator + "assetCache" + File.separator + "backgrounds" + File.separator + "sldtBG.png"))));
                if (!s.equals("d59a49000715febb4560af1a4d3f0f45")){
                    throw new GameException(new ErrorCode005());
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.exit(0);
            }
        } else {
            renderEngine.logger.severe("RenderEngine has detected that you've not included SLDT's-GameEngine background file in your assets ! Please correct your environment.");
            renderEngine.logger.severe("This game will now be stopped...");
            System.exit(0);
        }
        renderEngine.logger.info("RENDER ENGINE : Game assets mounted successfully !");

        GameApplication.log.info("RENDER ENGINE : Deleting assets extraction cache...");
        try {
            FileUtilities.delete(new File(GameApplication.getGameDir() + File.separator + "assetCache" + File.separator));
        } catch (IOException e) {
            e.printStackTrace();
        }
        GameApplication.log.info("RENDER ENGINE : Assets extraction cache deleted successfully !");
    }

    private void loadAssetFileAsGAF(){
        try {
            File cache = new File(GameApplication.getGameDir() + File.separator + "assetCache");
            if (!cache.exists()){
                cache.mkdirs();
            }

            FileInputStream in = new FileInputStream(assetFile);
            FileOutputStream out = new FileOutputStream(new File(cache + File.separator + "temp.dat"));

            byte[] b = new byte[1024];
            int length;
            while ((length = in.read(b)) > 0){
                for (int i = 0 ; i < b.length ; i++){
                    byte b1 = b[i];
                    b[i] = (byte)(b1 ^ 1);
                }
                out.write(b, 0, length);
            }

            in.close();
            out.close();

            loadZIP(new File(cache + File.separator + "temp.dat"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadZIP(File assetFile){
        GameApplication.log.info("RENDER ENGINE : Extracting game assets...");
        ArrayList<String[]> var = ZipFileUtilities.getFiles(assetFile.toString());
        ArrayList<String> list = new ArrayList<String>();
        ArrayList<File> list1 = new ArrayList<File>();
        for (String[] var1 : var){
            String s = var1[0];
            list.add(s);
            try {
                String s1 = s.replace("/", File.separator);
                File var2 = new File(GameApplication.getGameDir() + File.separator + "assetCache" + File.separator + s1 + " ");
                System.out.println(var2.toString());
                File var5 = var2.getParentFile();
                if (!var5.exists()){
                    var5.mkdirs();
                }

                File f1 = new File(GameApplication.getGameDir() + File.separator + "assetCache" + File.separator + s);
                if (!f1.isDirectory())
                    ZipFileUtilities.extractTo(assetFile.toString(), s, GameApplication.getGameDir() + File.separator + "assetCache" + File.separator);

                list1.add(new File(GameApplication.getGameDir() + File.separator + "assetCache" + File.separator + s1));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        renderEngine.logger.info("RENDER ENGINE : Game assets extraction ended with no errors");

        renderEngine.logger.info("RENDER ENGINE : Mounting game assets...");
        renderEngine.loadAssets(list, list1);
        if (renderEngine.hasAsset("backgrounds/sldtBG.png")){
            try {
                String s = (FileUtilities.getMD5Checksum(new FileInputStream(new File(GameApplication.getGameDir() + File.separator + "assetCache" + File.separator + "backgrounds" + File.separator + "sldtBG.png"))));
                if (!s.equals("d59a49000715febb4560af1a4d3f0f45")){
                    throw new GameException(new ErrorCode005());
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.exit(0);
            }
        } else {
            renderEngine.logger.severe("RenderEngine has detected that you've not included SLDT's-GameEngine background file in your assets ! Please correct your environment.");
            renderEngine.logger.severe("This game will now be stopped...");
            System.exit(0);
        }
        renderEngine.logger.info("RENDER ENGINE : Game assets mounted successfully !");

        GameApplication.log.info("RENDER ENGINE : Deleting assets extraction cache...");
        try {
            assetFile.delete();
            FileUtilities.delete(new File(GameApplication.getGameDir() + File.separator + "assetCache" + File.separator));
        } catch (IOException e) {
            e.printStackTrace();
        }
        GameApplication.log.info("RENDER ENGINE : Assets extraction cache deleted successfully !");
    }
}
