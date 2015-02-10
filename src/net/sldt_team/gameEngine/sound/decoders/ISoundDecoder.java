package net.sldt_team.gameEngine.sound.decoders;

import java.io.InputStream;
import java.nio.ByteBuffer;

public interface ISoundDecoder {

    /**
     * Initializes teh sound decoder for a new stream
     */
    public void initialize(InputStream stream);

    /**
     * Returns the data of the sound for OpenAL mount
     */
    public ByteBuffer getData();

    /**
     * Returns the format of this sound's chanel (mono/stereo)
     */
    public EnumChanelFormat getChanelFormat();

    /**
     * Returns the number of samples played or recorded per seconds of this sound
     */
    public int getSampleRate();

    /**
     * Returns the name of this decoder
     */
    public String getName();

    /**
     * Called when this decoder finish to decode the current stream
     */
    public void clearBuffers();
}
