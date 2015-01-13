package net.sldt_team.gameEngine.renderengine.decoders;

import net.sldt_team.gameEngine.GameApplication;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class PNGTextureDecoder implements ITextureDecoder {
    private PNGDecoderHelper curStream;

    public void initialize(InputStream stream) {
        try {
            curStream = new PNGDecoderHelper(stream);
        } catch (IOException e) {
            GameApplication.log.warning("Unable to load texture");
        }
    }

    public ByteBuffer getTextureData() {
        try {
            ByteBuffer buf = ByteBuffer.allocateDirect(4 * curStream.getWidth() * curStream.getHeight());
            curStream.decode(buf, curStream.getWidth() * 4, PNGDecoderHelper.Format.RGBA);
            buf.flip();
            return buf;
        } catch (IOException e) {
            GameApplication.log.warning("Unable to decode texture");
        }
        return null;
    }

    public int getTextureWidth() {
        return curStream.getWidth();
    }

    public int getTextureHeight() {
        return curStream.getHeight();
    }

    public void clearBuffers() {
        curStream = null;
    }

    public String getDecoderName() {
        return "PNGDecoder";
    }
}
