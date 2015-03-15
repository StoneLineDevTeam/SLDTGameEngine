package net.sldt_team.gameEngine.sound;

import net.sldt_team.gameEngine.GameApplication;
import net.sldt_team.gameEngine.sound.decoders.EnumChanelFormat;
import net.sldt_team.gameEngine.sound.decoders.ISoundDecoder;
import net.sldt_team.gameEngine.sound.helper.SoundFormatHelper;
import net.sldt_team.gameEngine.util.FileUtilities;
import org.lwjgl.BufferUtils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.openal.AL10.*;

/**
 * @exclude
 */
public class SoundSystem {

    protected SoundSystem() {
    }

    private EnumSoundLoadError getALErrorAsErrorEnum(int err)
    {
        switch (err)
        {
            case AL_NO_ERROR:
                return EnumSoundLoadError.ERROR_NULL;
            case AL_INVALID_NAME:
                return EnumSoundLoadError.ERROR_INVALID_NAME;
            case AL_INVALID_ENUM:
                return EnumSoundLoadError.ERROR_INVALID_ENUM;
            case AL_INVALID_VALUE:
                return EnumSoundLoadError.ERROR_INVALID_VALUE;
            case AL_INVALID_OPERATION:
                return EnumSoundLoadError.ERROR_INVALID_OPERATION;
            case AL_OUT_OF_MEMORY:
                return EnumSoundLoadError.ERROR_MEMORY_OUT;
            default:
                return null;
        }
    }

    /* Loads the given sound and returns false when this failes or true if it succeeded ! */
    protected EnumSoundLoadError loadSound(Sound sound){
        int err;
        sound.buffer = alGenBuffers();

        err = alGetError();
        if (err != AL_NO_ERROR) {
            return getALErrorAsErrorEnum(err);
        }

        /* Read and decode the sound file */
        if (!SoundFormatHelper.instance.canSoundFileBeRead(sound.soundPath)){
            return EnumSoundLoadError.ERROR_UNKNOWN_FORMAT;
        }
        BufferedInputStream stream;
        try {
            stream = new BufferedInputStream(new FileInputStream(sound.soundPath));
        } catch (FileNotFoundException e) {
            return EnumSoundLoadError.ERROR_FILE_FINDER;
        }
        ISoundDecoder decoder = SoundFormatHelper.instance.getSoundDecoderForFormat(FileUtilities.getFileExtension(sound.soundPath));
        decoder.initialize(stream);
        EnumChanelFormat format = decoder.getChanelFormat();
        int sample = decoder.getSampleRate();
        ByteBuffer data = decoder.getData();
        if (data == null){
            GameApplication.engineLogger.severe(decoder.getName() + " has failed decoding file " + sound.soundPath.toString());
            return EnumSoundLoadError.ERROR_DECODER_FAILURE;
        }
        if (format == null){
            return EnumSoundLoadError.ERROR_INVALID_CHANEL_FORMAT;
        }
        int openALFormat = -1;
        switch (format){
            case MONO_CHANEL_8BITS:
                openALFormat = AL_FORMAT_MONO8;
                break;
            case MONO_CHANEL_16BITS:
                openALFormat = AL_FORMAT_MONO16;
                break;
            case STEREO_CHANEL_8BITS:
                openALFormat = AL_FORMAT_STEREO8;
                break;
            case STEREO_CHANEL_16BITS:
                openALFormat = AL_FORMAT_STEREO16;
                break;
        }
        alBufferData(sound.buffer, openALFormat, data, sample);
        decoder.clearBuffers();

        err = alGetError();
        if (err != AL_NO_ERROR) {
            return getALErrorAsErrorEnum(err);
        }

        try {
            stream.close();
        } catch (IOException e) {
            return EnumSoundLoadError.ERROR_IO;
        }
        /* End */

        /* Source generation */
        sound.source = alGenSources();
        err = alGetError();
        if (err != AL_NO_ERROR) {
            return getALErrorAsErrorEnum(err);
        }
        alSourcei(sound.source, AL_BUFFER, sound.buffer);
        alSourcef(sound.source, AL_PITCH, 1.0f);
        alSourcef(sound.source, AL_GAIN, 1.0f);

        /* Prepare buffers */
        FloatBuffer sourcePos = BufferUtils.createFloatBuffer(3).put(new float[]{0.0f, 0.0f, 0.0f});
        FloatBuffer sourceVel = BufferUtils.createFloatBuffer(3).put(new float[]{0.0f, 0.0f, 0.0f});
        FloatBuffer listenerPos = BufferUtils.createFloatBuffer(3).put(new float[]{0.0f, 0.0f, 0.0f});
        FloatBuffer listenerVel = BufferUtils.createFloatBuffer(3).put(new float[]{0.0f, 0.0f, 0.0f});
        FloatBuffer listenerOri = BufferUtils.createFloatBuffer(6).put(new float[]{0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f});
        sourcePos.rewind();
        sourceVel.rewind();
        listenerPos.rewind();
        listenerVel.rewind();
        listenerOri.rewind();
        /* End */

        alSource(sound.source, AL_POSITION, sourcePos);
        alSource(sound.source, AL_VELOCITY, sourceVel);
        alSourcei(sound.source, AL_LOOPING, AL_FALSE);
        err = alGetError();
        if (err != AL_NO_ERROR) {
            return getALErrorAsErrorEnum(err);
        }
        alListener(AL_POSITION, listenerPos);
        alListener(AL_VELOCITY, listenerVel);
        alListener(AL_ORIENTATION, listenerOri);

        return getALErrorAsErrorEnum(alGetError());
    }

    protected void deleteSound(Sound sound){
        alDeleteSources(sound.source);
        alDeleteBuffers(sound.buffer);
    }

    protected void setSoundLevel(Sound sound, float newLevel) {
        alSourcef(sound.source, AL_GAIN, newLevel);
        EnumSoundLoadError err = getALErrorAsErrorEnum(alGetError());
        if (err != EnumSoundLoadError.ERROR_NULL){
            GameApplication.engineLogger.warning("Unable to set sound level : " + err.name());
        }
    }

    protected float getSoundLevel(Sound sound){
        return alGetSourcef(sound.source, AL_GAIN);
    }

    protected void setLoopingSound(Sound sound, boolean loop) {
        if (loop) {
            alSourcei(sound.source, AL_LOOPING, AL_TRUE);
        } else {
            alSourcei(sound.source, AL_LOOPING, AL_FALSE);
        }
        EnumSoundLoadError err = getALErrorAsErrorEnum(alGetError());
        if (err != EnumSoundLoadError.ERROR_NULL){
            GameApplication.engineLogger.warning("Unable to loop sound : " + err.name());
        }
    }

    protected void playSound(Sound sound) {
        alSourcePlay(sound.source);
        EnumSoundLoadError err = getALErrorAsErrorEnum(alGetError());
        if (err != EnumSoundLoadError.ERROR_NULL){
            GameApplication.engineLogger.warning("Unable to play sound : " + err.name());
        }
    }

    protected void pauseSound(Sound sound){
        alSourcePause(sound.source);
        EnumSoundLoadError err = getALErrorAsErrorEnum(alGetError());
        if (err != EnumSoundLoadError.ERROR_NULL){
            GameApplication.engineLogger.warning("Unable to pause sound : " + err.name());
        }
    }

    protected void stopSound(Sound sound) {
        alSourceStop(sound.source);
        EnumSoundLoadError err = getALErrorAsErrorEnum(alGetError());
        if (err != EnumSoundLoadError.ERROR_NULL){
            GameApplication.engineLogger.warning("Unable to stop sound : " + err.name());
        }
    }

    protected boolean isPlayingSound(Sound sound) {
        int state = alGetSourcei(sound.source, AL_SOURCE_STATE);
        return (state == AL_PLAYING);
    }
}
