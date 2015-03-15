package net.sldt_team.gameEngine.renderengine;

import net.sldt_team.gameEngine.GameApplication;
import net.sldt_team.gameEngine.renderengine.assetSystem.Asset;
import net.sldt_team.gameEngine.renderengine.helper.TextureFormatHelper;
import net.sldt_team.gameEngine.util.MathUtilities;
import net.sldt_team.gameEngine.util.misc.ZipUtilities;
import net.sldt_team.gameEngine.util.FileUtilities;
import net.sldt_team.gameEngine.exception.GameException;
import net.sldt_team.gameEngine.exception.code.ErrorCode005;
import net.sldt_team.gameEngine.renderengine.assetSystem.AssetType;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AssetsManager {

    private File assetFile = null;
    private RenderEngine renderEngine;
    private AssetType type;

    private Asset[] assetsMap;

    /**
     * @exclude
     */
    public AssetsManager(String fileLocation, AssetType t) {
        if (t.isZIP() || t.isGAF()) {
            assetFile = new File(fileLocation + ".assets." + t.getAssetFileExtention());
            type = t;
        }
    }

    /**
     * @exclude
     */
    protected void initialize(RenderEngine engine) {
        renderEngine = engine;
        if (type.isZIP()) {
            loadAssetFileAsZIP();
        } else if (type.isGAF()) {
            loadAssetFileAsGAF();
        }
        renderEngine.reloadRenderingEngine();
    }

    /**
     * Returns if an asset with the given name is existing
     */
    public boolean hasAsset(String name){
        for (Asset asset : assetsMap){
            if (asset != null && asset.assetName.equals(name)){
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the asset corresponding to the given name
     */
    public Asset getAsset(String name){
        for (Asset asset : assetsMap){
            if (asset != null && asset.assetName.equals(name)){
                return asset;
            }
        }
        return null;
    }

    /**
     * Returns all loaded assets in the memory
     */
    public List<Asset> getAllLoadedAssets(){
        List<Asset> list = new ArrayList<Asset>();
        for (Asset asset : assetsMap){
            if (asset != null){
                list.add(asset);
            }
        }
        return list;
    }

    private void addAssets(ArrayList<String> namesToMap, ArrayList<File> filesToLoad){
        assetsMap = new Asset[MathUtilities.findNearestPowerOfTwo(namesToMap.size())];
        for (int i = 0 ; i < namesToMap.size() ; i++) {
            String name = namesToMap.get(i);
            File f = filesToLoad.get(i);
            if (TextureFormatHelper.instance.canTextureFileBeRead(f)) {
                try {
                    FileInputStream stream = new FileInputStream(f);
                    byte[] data = getAssetData(stream);
                    assetsMap[i] = new Asset(name, true, data);
                    stream.close();
                } catch (Exception e) {
                    renderEngine.logger.warning("ASSETS LOADER : Ignoring asset,  \"" + name + "\" : invalid or corrupted file.");
                }
            } else {
                try {
                    FileInputStream stream = new FileInputStream(f);
                    byte[] data = getAssetData(stream);
                    assetsMap[i] = new Asset(name, false, data);
                    stream.close();
                } catch (Exception e) {
                    renderEngine.logger.warning("ASSETS LOADER : Ignoring asset,  \"" + name + "\" : invalid or corrupted file.");
                }
            }
        }
    }

    private byte[] getAssetData(InputStream is) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[16384];

        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();

        return buffer.toByteArray();
    }

    private void loadAssetFileAsZIP() {
        renderEngine.logger.info("ASSETS LOADER : Extracting game assets...");
        ArrayList<String[]> var = ZipUtilities.getFiles(assetFile.toString());
        ArrayList<String> list = new ArrayList<String>();
        ArrayList<File> list1 = new ArrayList<File>();
        for (String[] var1 : var) {
            String s = var1[0];
            list.add(s);
            try {
                String s1 = s.replace("/", File.separator);
                File var2 = new File(GameApplication.getGameDir() + File.separator + "assetCache" + File.separator + s1 + " ");
                renderEngine.logger.info(var2.toString());
                File var5 = var2.getParentFile();
                if (!var5.exists()) {
                    var5.mkdirs();
                }

                File f1 = new File(GameApplication.getGameDir() + File.separator + "assetCache" + File.separator + s);
                if (!f1.isDirectory())
                    ZipUtilities.extractTo(assetFile.toString(), s, GameApplication.getGameDir() + File.separator + "assetCache" + File.separator);

                list1.add(new File(GameApplication.getGameDir() + File.separator + "assetCache" + File.separator + s1));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        renderEngine.logger.info("ASSETS LOADER : Game assets extraction ended with no errors");

        renderEngine.logger.info("ASSETS LOADER : Mounting game assets...");
        addAssets(list, list1);
        if (hasAsset("materials/backgrounds/sldtBG.png")) {
            try {
                ByteArrayInputStream stream = new ByteArrayInputStream(getAsset("materials/backgrounds/sldtBG.png").assetData);
                String s = (FileUtilities.getMD5Checksum(stream));
                stream.close();
                if (!s.equals("d59a49000715febb4560af1a4d3f0f45") && !s.equals("8aa8a9bfdf85bc1bcdebb944a24a6d9f")) {
                    throw new GameException(new ErrorCode005());
                }
            } catch (Exception e) {
                throw new GameException(new ErrorCode005());
            }
        } else {
            renderEngine.logger.severe("RenderEngine has detected that you've not included SLDT's GameEngine background file in your assets ! Please correct your environment.");
            renderEngine.logger.severe("This game will now be stopped...");
            System.exit(0);
        }
        renderEngine.logger.info("ASSETS LOADER : Game assets mounted successfully !");

        renderEngine.logger.info("ASSETS LOADER : Deleting assets extraction cache...");
        try {
            FileUtilities.delete(new File(GameApplication.getGameDir() + File.separator + "assetCache" + File.separator));
        } catch (IOException e) {
            e.printStackTrace();
        }
        renderEngine.logger.info("ASSETS LOADER : Assets extraction cache deleted successfully !");
    }

    private void loadAssetFileAsGAF() {
        try {
            File cache = new File(GameApplication.getGameDir() + File.separator + "assetCache");
            if (!cache.exists()) {
                cache.mkdirs();
            }

            FileInputStream in = new FileInputStream(assetFile);
            FileOutputStream out = new FileOutputStream(new File(cache + File.separator + "temp.dat"));

            byte[] b = new byte[1024];
            int length;
            while ((length = in.read(b)) > 0) {
                for (int i = 0; i < b.length; i++) {
                    byte b1 = b[i];
                    b[i] = (byte) (b1 ^ 1);
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

    private void loadZIP(File assetFile) {
        renderEngine.logger.info("ASSETS LOADER : Extracting game assets...");
        ArrayList<String[]> var = ZipUtilities.getFiles(assetFile.toString());
        ArrayList<String> list = new ArrayList<String>();
        ArrayList<File> list1 = new ArrayList<File>();
        for (String[] var1 : var) {
            String s = var1[0];
            list.add(s);
            try {
                String s1 = s.replace("/", File.separator);
                File var2 = new File(GameApplication.getGameDir() + File.separator + "assetCache" + File.separator + s1 + " ");
                renderEngine.logger.info(var2.toString());
                File var5 = var2.getParentFile();
                if (!var5.exists()) {
                    var5.mkdirs();
                }

                File f1 = new File(GameApplication.getGameDir() + File.separator + "assetCache" + File.separator + s);
                if (!f1.isDirectory())
                    ZipUtilities.extractTo(assetFile.toString(), s, GameApplication.getGameDir() + File.separator + "assetCache" + File.separator);

                list1.add(new File(GameApplication.getGameDir() + File.separator + "assetCache" + File.separator + s1));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        renderEngine.logger.info("ASSETS LOADER : Game assets extraction ended with no errors");

        renderEngine.logger.info("ASSETS LOADER : Mounting game assets...");
        addAssets(list, list1);
        if (hasAsset("materials/backgrounds/sldtBG.png")) {
            try {
                ByteArrayInputStream stream = new ByteArrayInputStream(getAsset("materials/backgrounds/sldtBG.png").assetData);
                String s = (FileUtilities.getMD5Checksum(stream));
                stream.close();
                if (!s.equals("d59a49000715febb4560af1a4d3f0f45") && !s.equals("8aa8a9bfdf85bc1bcdebb944a24a6d9f")) {
                    throw new GameException(new ErrorCode005());
                }
            } catch (Exception e) {
                throw new GameException(new ErrorCode005());
            }
        } else {
            renderEngine.logger.severe("RenderEngine has detected that you've not included SLDT's-GameEngine background file in your assets ! Please correct your environment.");
            renderEngine.logger.severe("This game will now be stopped...");
            System.exit(0);
        }
        renderEngine.logger.info("ASSETS LOADER : Game assets mounted successfully !");

        renderEngine.logger.info("ASSETS LOADER : Deleting assets extraction cache...");
        try {
            assetFile.delete();
            FileUtilities.delete(new File(GameApplication.getGameDir() + File.separator + "assetCache" + File.separator));
        } catch (IOException e) {
            e.printStackTrace();
        }
        renderEngine.logger.info("ASSETS LOADER : Assets extraction cache deleted successfully !");
    }
}
