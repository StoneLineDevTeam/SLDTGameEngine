package net.sldt_team.gameEngine.format.texturefile;

import net.sldt_team.gameEngine.GameApplication;
import net.sldt_team.gameEngine.renderengine.decoders.PNGDecoderHelper;

import java.io.*;
import java.nio.ByteBuffer;

/**
 * Useless because never finished
 */
public class GameTextureFile {

    private InputStream fileStream;

    private ByteBuffer openGLBuffer;
    private int texWidth;
    private int texHeight;

    public GameTextureFile(InputStream stream) {
        fileStream = stream;
        try {
            openGLBuffer = openGLDecode();
        } catch (IOException e) {
            GameApplication.log.severe("Unable to decode texture file");
        }
    }

    public ByteBuffer getData(){
        return openGLBuffer;
    }
    public int getWidth(){
        return texWidth;
    }
    public int getHeight(){
        return texHeight;
    }

    private InputStream decodeAsPNG() {
        try {
            OutputStream out = new ByteArrayOutputStream();

            byte[] b = new byte[1024];
            int length;
            while ((length = fileStream.read(b)) > 0) {
                for (int i = 0; i < b.length; i++) {
                    byte b1 = b[i];
                    b[i] = (byte) (b1 ^ 16);
                }
                out.write(b, 0, length);
            }


            fileStream.close();
            return new ByteArrayInputStream(((ByteArrayOutputStream) out).toByteArray());
        } catch (IOException e) {
            GameApplication.log.warning("Unable to decode texture file");
        }
        return null;
    }

    private ByteBuffer openGLDecode() throws IOException {
        InputStream forPNGDecoderHelper = decodeAsPNG();
        if (forPNGDecoderHelper != null){
            PNGDecoderHelper helper = new PNGDecoderHelper(forPNGDecoderHelper);
            texWidth = helper.getWidth();
            texHeight = helper.getHeight();
            ByteBuffer buf = ByteBuffer.allocateDirect(4 * texWidth * texHeight);
            helper.decode(buf, texWidth * 4, PNGDecoderHelper.Format.RGBA);
            buf.flip();
            return buf;
        }
        return null;
    }
}
