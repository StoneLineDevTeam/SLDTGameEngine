package fr.sldt.gameEngine;

import java.util.HashMap;
import java.util.Map;

public class AppSettings {

    private int resolutionX;
    private int resolutionY;
    private boolean isFullScreenWindow;
    private boolean isWindowResizable;

    public AppSettings(int sizeX, int sizeY, boolean fullscreen, boolean resizable){
        resolutionX = sizeX;
        resolutionY = sizeY;
        isFullScreenWindow = !fullscreen;
        isWindowResizable = resizable;
    }

    public Map<String, Object> getWindowSettings(){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("SLDT_WINDOW_WIDTH", resolutionX);
        map.put("SLDT_WINDOW_HEIGHT", resolutionY);
        map.put("SLDT_WINDOW_FULLSCREEN", isFullScreenWindow);
        map.put("SLDT_WINDOW_RESIZABLE", isWindowResizable);
        return map;
    }
}
