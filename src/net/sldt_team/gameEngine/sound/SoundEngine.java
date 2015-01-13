package net.sldt_team.gameEngine.sound;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class SoundEngine {

    private Map<String, File> soundsMap;
    private SoundSystem system;

    private float globalSoundLevel;

    /**
     * @exclude
     */
    public SoundEngine(Logger log) {
        soundsMap = new HashMap<String, File>();
        system = new SoundSystem(log);
        initSoundEngine();
    }

    /**
     * Registers a new sound for your game (args: sound name, sound file)
     */
    public void addSound(String name, File path) {
        soundsMap.put(name, path);
    }

    private void initSoundEngine() {
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

    public void setGlobalSoundLevel(float newLvl){
        globalSoundLevel = newLvl;
        for (Map.Entry entry : soundsMap.entrySet()){
            String sndName = (String) entry.getKey();
            if (isPlaying(sndName)){
                system.setSoundLevel(sndName, globalSoundLevel);
            }
        }
    }

    /**
     * Start playing a sound (args: The sound name, should the sound be played in loop, an amplifier
     */
    public void playSound(String name, boolean toLoop, int amplifier) {
        system.playSound(name);
        system.setSoundLevel(name, globalSoundLevel * amplifier);
        system.setLoopingSound(name, toLoop);
    }
}
