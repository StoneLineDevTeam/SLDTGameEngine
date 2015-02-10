package net.sldt_team.gameEngine.sound;

import net.sldt_team.gameEngine.GameApplication;
import net.sldt_team.gameEngine.sound.helper.SoundStateHelper;
import net.sldt_team.gameEngine.util.FileUtilities;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SoundEngine {

    private Map<String, Sound> soundsMap;
    private SoundSystem system;

    private float globalSoundLevel;

    private Sound currentBoundSound;

    /**
     * @exclude
     */
    public SoundEngine() {
        soundsMap = new HashMap<String, Sound>();
        system = new SoundSystem();
        initSoundEngine();
    }

    private void initSoundEngine() {
        File files = new File(FileUtilities.getResourcesDirectory() + File.separator + "sounds" + File.separator);
        File[] fs = new File(FileUtilities.getResourcesDirectory() + File.separator + "sounds" + File.separator).listFiles();
        if (fs == null){
            GameApplication.engineLogger.info("No sounds directory found, skipping SoundEngine load...");
            return;
        }
        loadDirectory(files);
    }
    private void loadDirectory(File f){
        if (f.isDirectory()){
            File[] files = f.listFiles();
            if (files == null){
                return;
            }
            for (File f1 : files){
                if (f1.isDirectory()){
                    loadDirectory(f1);
                } else {
                    String s2 = workPath(f1);
                    Sound sound = new Sound(f1);
                    EnumSoundLoadError b = system.loadSound(sound);
                    if (b != EnumSoundLoadError.ERROR_NULL){
                        GameApplication.engineLogger.warning("Failed to load sound, " + s2 + " : " + b.name());
                    } else {
                        soundsMap.put(s2, sound);
                        GameApplication.engineLogger.info("Successfully added sound, " + s2);
                    }
                }
            }
        }
    }
    private String workPath(File f){
        if (!f.isFile()){
            GameApplication.engineLogger.warning("Tried to work a sound directory instead of a file !");
            return null;
        }
        String s = f.getPath();
        String s1 = FileUtilities.getResourcesDirectory() + File.separator + "sounds" + File.separator;
        s = s.replace(s1, "");
        s = s.replace(File.separatorChar, '.');
        s = s.replace("." + FileUtilities.getFileExtension(f), "");
        GameApplication.engineLogger.info("Worked file " + f + ", result is : '" + s + "'");
        return s;
    }

    /**
     * @exclude
     */
    public void onClosingGame() {
        for (Map.Entry e : soundsMap.entrySet()){
            Sound sound = (Sound) e.getValue();
            system.deleteSound(sound);
        }
        soundsMap.clear();
    }

    /**
     * Returns the list of all loaded sounds
     */
    public String[] getLoadedSoundList(){
        String[] result = new String[soundsMap.size()];
        int i = 0;
        for (Map.Entry entry : soundsMap.entrySet()){
            String sndName = (String) entry.getKey();
            result[i] = sndName;
            i++;
        }
        return result;
    }

    /**
     * Sets the global sound level multiplier and update all sounds with the new level
     */
    public void setGlobalSoundLevelMultiplier(float newLvl){
        globalSoundLevel = newLvl;
        for (Map.Entry entry : soundsMap.entrySet()){
            String sndName = (String) entry.getKey();
            bindSound(sndName);
            if (isPlaying()){
                system.setSoundLevel(currentBoundSound, system.getSoundLevel(currentBoundSound) * globalSoundLevel);
            }
        }
    }

    /**
     * Returns the global sound level multiplier
     */
    public float getGlobalSoundLevelMultiplier(){
        return globalSoundLevel;
    }

    /**
     * Binds a sound to use by it's own name
     */
    public void bindSound(String name){
        if (!soundsMap.containsKey(name)){
            GameApplication.engineLogger.warning("Tried to bind an invalid sound");
            return;
        }
        currentBoundSound = soundsMap.get(name);
    }

    /**
     * Sets the state for the current sound
     */
    public void setState(SoundStateHelper helper){
        switch (helper){
            case PLAY:
                system.setLoopingSound(currentBoundSound, false);
                system.playSound(currentBoundSound);
                break;
            case PLAY_LOOP:
                system.setLoopingSound(currentBoundSound, true);
                system.playSound(currentBoundSound);
                break;
            case PAUSE:
                system.pauseSound(currentBoundSound);
                break;
            case STOP:
                system.stopSound(currentBoundSound);
                break;
        }
    }

    /**
     * Sets the level for the current sound
     */
    public void setLevel(float newLevel){
        if (currentBoundSound != null) {
            system.setSoundLevel(currentBoundSound, globalSoundLevel * newLevel);
        }
    }

    /**
     * Returns whenever the current sound is currently played
     */
    public boolean isPlaying() {
        return currentBoundSound != null && system.isPlayingSound(currentBoundSound);
    }
}
