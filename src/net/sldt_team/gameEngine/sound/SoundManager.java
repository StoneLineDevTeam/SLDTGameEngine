package net.sldt_team.gameEngine.sound;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class SoundManager {

    private Map<String, File> soundsMap;
    private SoundSystem system;

    /**
     * @exclude
     */
    public SoundManager(Logger log) {
        soundsMap = new HashMap<String, File>();
        system = new SoundSystem(log);
    }

    /**
     * Registers a new sound for your game (args: sound name, sound file)
     */
    public void addSound(String name, File path) {
        soundsMap.put(name, path);
    }

    /**
     * Inits the sound manager (You must call this method after registering game sounds) - Call it under initGame() otherwise it'll crash due to OpenAL Main thread not created...
     */
    public void initSoundManager() {
        for (Map.Entry entry : soundsMap.entrySet()) {
            String name = (String) entry.getKey();
            File file = (File) entry.getValue();

            system.registerNewSource(file.toString(), name);
        }
        system.reloadSoundSystem();
    }

    /**
     * @exclude
     */
    public void onClosingGame() {
        soundsMap.clear();
        system.closeOpenALSoundSystem();
    }

    /**
     * Stop a current playing sound
     */
    public void stopSound(String name) {
        system.stopSound(name);
    }

    /**
     * Returns whenever the given sound name is currently played
     */
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
