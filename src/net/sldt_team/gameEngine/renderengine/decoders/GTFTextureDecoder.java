package net.sldt_team.gameEngine.renderengine.decoders;

import net.sldt_team.gameEngine.format.texturefile.GameTextureFile;

import java.io.InputStream;
import java.nio.ByteBuffer;

public class GTFTextureDecoder implements ITextureDecoder {

    private GameTextureFile textureFile;

    public void initialize(InputStream stream) {
        textureFile = new GameTextureFile(stream);
    }

    public ByteBuffer getTextureData() {
        return textureFile.getData();
    }

    public int getTextureWidth() {
        return textureFile.getWidth();
    }

    public int getTextureHeight() {
        return textureFile.getHeight();
    }

    public void clearBuffers() {
        textureFile = null;
    }

    public String getDecoderName() {
        return "GTFDecoder";
    }
}
