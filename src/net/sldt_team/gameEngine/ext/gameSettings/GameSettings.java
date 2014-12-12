package net.sldt_team.gameEngine.ext.gameSettings;

import net.sldt_team.gameEngine.GameApplication;
import net.sldt_team.gameEngine.format.datafile.GameDataFile;
import net.sldt_team.gameEngine.util.FileUtilities;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class GameSettings {

    private File settingsFile;

    /**
     * Are particles activated
     */
    public boolean isParticlesActivated;

    /**
     * Using VSync (Warning : You must check this boolean yourself ; use GLUtilities.switchVSync to switch VSync)
     */
    public boolean useVsync;

    /**
     * Using game cursor (Warning : You must check this boolean yourself)
     */
    public boolean useGameCursor;

    /**
     * Should the game engine show FPS in the upper left corner of the screen ?
     */
    public boolean showFPS;

    private Map<String, Object> settingsMap;

    /**
     * @exclude
     */
    public GameSettings() {
        settingsMap = new HashMap<String, Object>();
        if (!FileUtilities.getSavesDirectory().exists()) {
            FileUtilities.getSavesDirectory().mkdirs();
        }
        settingsFile = new File(FileUtilities.getSavesDirectory() + File.separator + "settings");
    }

    /**
     * Registers an additional setting linked to your game
     */
    public void registerAdditionalSetting(String name, Object value) {
        settingsMap.put(name, value);
    }

    /**
     * Returns the value of an additional setting of your game
     */
    public Setting getAdditionalSettingValue(String name) {
        return new Setting(settingsMap.get(name));
    }

    /**
     * Returns true when the given additional setting exists
     */
    public boolean isAdditionalSettingExisting(String name) {
        return settingsMap.containsKey(name);
    }

    /**
     * Changes the value for an additional setting (args: setting name, setting new value)
     */
    public void setAdditionalSetting(String name, Object newValue) {
        settingsMap.remove(name);
        settingsMap.put(name, newValue);
    }

    /**
     * @exclude
     */
    public void saveSettings() {
        GameApplication.log.info("Saving game settings...");
        GameDataFile data = new GameDataFile(settingsFile, false);

        data.addKeyValue("particles", String.valueOf(isParticlesActivated));
        data.addKeyValue("vsync", String.valueOf(useVsync));
        data.addKeyValue("cursor", String.valueOf(useGameCursor));
        data.addKeyValue("fps", String.valueOf(showFPS));
        for (Map.Entry entry : settingsMap.entrySet()) {
            data.addKeyValue((String) entry.getKey(), String.valueOf(entry.getValue()));
        }

        data.isReady = true;
        data.saveData();
        GameApplication.log.info("Game settings saved.");
    }

    /**
     * @exclude
     */
    public void loadSettings() {
        GameApplication.log.info("Loading game settings...");
        GameDataFile data = new GameDataFile(settingsFile, true);

        if (data.isReady) {
            Map<String, String> content = data.getData();

            isParticlesActivated = Boolean.valueOf(content.get("particles"));
            useVsync = Boolean.valueOf(content.get("vsync"));
            useGameCursor = Boolean.valueOf(content.get("cursor"));
            showFPS = Boolean.valueOf(content.get("fps"));
            content.remove("particles");
            content.remove("vsync");
            content.remove("cursor");
            content.remove("fps");
            for (Map.Entry entry : content.entrySet()) {
                GameApplication.log.info("Reading additional setting : " + String.valueOf(entry.getKey()));
                String s = (String) entry.getValue();
                Object obj;
                try {
                    obj = Integer.parseInt(s);
                } catch (Exception e) {
                    GameApplication.log.info("No int found trying boolean");
                    try {
                        if (s.equals("false") || s.equals("true")) {
                            obj = Boolean.parseBoolean(s);
                        } else {
                            throw new RuntimeException();
                        }
                    } catch (Exception e1) {
                        GameApplication.log.info("No boolean found trying float");
                        try {
                            obj = Float.parseFloat(s);
                        } catch (Exception e2) {
                            GameApplication.log.info("No float found trying double");
                            try {
                                obj = Double.parseDouble(s);
                            } catch (Exception e3) {
                                GameApplication.log.info("No double found trying byte");
                                try {
                                    obj = Byte.parseByte(s);
                                } catch (Exception e4) {
                                    GameApplication.log.info("No byte found ; defaulting to string");
                                    obj = s;
                                }
                            }
                        }
                    }
                }
                settingsMap.put((String) entry.getKey(), obj);
            }
        }
        GameApplication.log.info("Game settings loaded.");
    }
}
