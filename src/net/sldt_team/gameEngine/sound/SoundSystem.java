package net.sldt_team.gameEngine.sound;

import net.sldt_team.gameEngine.GameApplication;
import org.lwjgl.util.WaveData;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static org.lwjgl.openal.AL10.*;

public class SoundSystem {
    private Map<String, SoundEntry> soundMap;

    private Logger logger;

    public SoundSystem(Logger log){
        soundMap = new HashMap<String, SoundEntry>();
        logger = log;
    }

    private int loadGameSounds(){
        for (Map.Entry entry : soundMap.entrySet()) {
            SoundEntry sound = (SoundEntry) entry.getValue();

            // Load wav data into a buffer.
            alGenBuffers(sound.buffer);

            if (alGetError() != AL_NO_ERROR)
                return AL_FALSE;

            BufferedInputStream stream = new BufferedInputStream(sound.soundPath);
            WaveData waveFile = WaveData.create(stream);
            logger.info("Loading sound file " + entry.getKey());
            waveFile.data.rewind();
            alBufferData(sound.buffer.get(0), waveFile.format, waveFile.data, waveFile.samplerate);
            waveFile.dispose();
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Bind the buffer with the source.
            alGenSources(sound.source);

            if (alGetError() != AL_NO_ERROR)
                return AL_FALSE;

            alSourcei(sound.source.get(0), AL_BUFFER, sound.buffer.get(0) );
            alSourcef(sound.source.get(0), AL_PITCH, 1.0f);
            alSourcef(sound.source.get(0), AL_GAIN, 1.0f);
            alSource(sound.source.get(0), AL_POSITION, sound.sourcePos);
            alSource(sound.source.get(0), AL_VELOCITY, sound.sourceVel);
            alSourcei(sound.source.get(0), AL_LOOPING, AL_FALSE);

            // Do another error check and return.
            if (alGetError() != AL_NO_ERROR)
                return AL_FALSE;
        }
        return AL_TRUE;
    }

    private void setListenerValues() {
        for (Map.Entry entry : soundMap.entrySet()) {
            SoundEntry sound = (SoundEntry) entry.getValue();
            alListener(AL_POSITION, sound.listenerPos);
            alListener(AL_VELOCITY, sound.listenerVel);
            alListener(AL_ORIENTATION, sound.listenerOri);
        }
    }

    public void reloadSoundSystem(){
        // Load the wav data.
        if(loadGameSounds() == AL_FALSE) {
            GameApplication.log.severe("Error loading data.");
            return;
        }

        setListenerValues();
    }

    public void registerNewSource(String path, String name){
        soundMap.put(name, new SoundEntry(path));
    }

    public void closeOpenALSoundSystem() {
        for (Map.Entry entry : soundMap.entrySet()) {
            SoundEntry sound = (SoundEntry) entry.getValue();
            alDeleteSources(sound.source);
            alDeleteBuffers(sound.buffer);
        }
    }

    public void setSoundLevel(String soundName, float newLevel){
        SoundEntry sound = soundMap.get(soundName);
        if (sound != null) {
            alSourcef(sound.source.get(0), AL_GAIN, newLevel);
        }
    }

    public void setLoopingSound(String soundName, boolean loop){
        SoundEntry sound = soundMap.get(soundName);
        if (sound != null) {
            if (loop) {
                alSourcei(sound.source.get(0), AL_LOOPING, AL_TRUE);
            } else {
                alSourcei(sound.source.get(0), AL_LOOPING, AL_FALSE);
            }
        }
    }

    public void pauseSound(String soundName){
        SoundEntry sound = soundMap.get(soundName);
        if (sound != null){
            alSourcePause(sound.source.get(0));
        }
    }

    public void playSound(String soundName){
        SoundEntry sound = soundMap.get(soundName);
        if (sound != null){
            alSourcePlay(sound.source.get(0));
        }
    }

    public void stopSound(String soundName){
        SoundEntry sound = soundMap.get(soundName);
        if (sound != null){
            alSourceStop(sound.source.get(0));
        }
    }

    public boolean isPlayingSound(String soundName){
        SoundEntry sound = soundMap.get(soundName);
        if (sound != null){
            int state = alGetSourcei(sound.source.get(0), AL_SOURCE_STATE);

            return (state == AL_PLAYING);
        }
        return false;
    }
}
