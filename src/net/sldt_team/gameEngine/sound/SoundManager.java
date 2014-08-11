package net.sldt_team.gameEngine.sound;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class SoundManager {

    private Map<String, File> soundsMap;
    private SoundSystem system;

    public SoundManager(Logger log) {
        soundsMap = new HashMap<String, File>();
        system = new SoundSystem(log);
    }

    /**
     * Add a sound in the sound map to play it
     */
    public void addSound(String name, File path) {
        soundsMap.put(name, path);
    }

    public void initSoundManager(){
        for (Map.Entry entry : soundsMap.entrySet()){
            String name = (String) entry.getKey();
            File file = (File) entry.getValue();

            system.registerNewSource(file.toString(), name);
        }
        system.reloadSoundSystem();
    }

    public void onClosingGame(){
        soundsMap.clear();
        system.closeOpenALSoundSystem();
    }

    /**
     * Stop a current playing sound
     */
    public void stopSound(String name) {
        system.stopSound(name);
    }

    public boolean isPlaying(String name) {
        return system.isPlayingSound(name);
    }

    /**
     * Start playing a sound
     */
    public void playSound(String name, float soundLevel, boolean toLoop) {
        system.playSound(name);
        system.setSoundLevel(name, soundLevel);
        system.setLoopingSound(name, toLoop);
    }
}
