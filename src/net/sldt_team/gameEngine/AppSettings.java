package net.sldt_team.gameEngine;

import java.util.HashMap;
import java.util.Map;

public class AppSettings {

    private int resolutionX;
    private int resolutionY;
    private boolean isFullScreenWindow;

    /**
     * Initializes settings for your app
     *
     * @param sizeX      The screen resolution X
     * @param sizeY      The screen resolution Y
     * @param fullscreen Should this app be fullscreen, or not ?
     */
    public AppSettings(int sizeX, int sizeY, boolean fullscreen) {
        resolutionX = sizeX;
        resolutionY = sizeY;
        isFullScreenWindow = !fullscreen;
    }

    /**
     * Returns a map containing all window settings provided in the constructor
     */
    public Map<String, Object> getWindowSettings() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("SLDT_WINDOW_WIDTH", resolutionX);
        map.put("SLDT_WINDOW_HEIGHT", resolutionY);
        map.put("SLDT_WINDOW_FULLSCREEN", isFullScreenWindow);
        return map;
    }
}
