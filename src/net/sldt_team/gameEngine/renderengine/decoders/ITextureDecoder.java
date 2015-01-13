package net.sldt_team.gameEngine.renderengine.decoders;

import java.io.InputStream;
import java.nio.ByteBuffer;

public interface ITextureDecoder {

    /**
     * Initializes this format interpreter
     */
    public void initialize(InputStream stream);

    /**
     * Returns the data of the texture for OpenGL mount
     */
    public ByteBuffer getTextureData();

    /**
     * Returns the width of this texture
     */
    public int getTextureWidth();

    /**
     * Returns the height of this texture
     */
    public int getTextureHeight();

    /**
     * Called at the end of the texture load to clear your buffers if you have, so we can reuse the same instance
     */
    public void clearBuffers();

    /**
     * Returns the name of this decoder
     */
    public String getDecoderName();
}
