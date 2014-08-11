package net.sldt_team.gameEngine.format.texturefile;

import net.sldt_team.gameEngine.GameApplication;
import net.sldt_team.gameEngine.renderengine.RenderEngine;

import java.io.*;
import java.nio.ByteBuffer;

public class GameTextureFile {

    private File fileLocation;
    private InputStream fileStream;

    public GameTextureFile(File file){
        fileLocation = new File(file + ".gtf");
    }

    public InputStream decodeAsPNG(){
        try {
            FileInputStream in = new FileInputStream(fileLocation);
            OutputStream out = new ByteArrayOutputStream();

            byte[] b = new byte[1024];
            int length;
            while ((length = in.read(b)) > 0) {
                for (int i = 0; i < b.length; i++) {
                    byte b1 = b[i];
                    b[i] = (byte) (b1 ^ 16);
                }
                out.write(b, 0, length);
            }

            in.close();
            return new ByteArrayInputStream(((ByteArrayOutputStream)out).toByteArray());
        } catch (IOException e){
            GameApplication.log.warning("Unable to decode texture file : " + fileLocation);
        }
        return null;
    }

    public ByteBuffer openGLDecode(RenderEngine renderEngine){
        if (fileStream == null){
            throw new NullPointerException("Unable to decode PNG input stream, cause : FILE_INPUT_STREAM = null");
        }
        try {
            return renderEngine.mountTexture(fileStream);
        } catch (IOException e) {
            GameApplication.log.warning("Unable to decode PNG stream");
        }
        return null;
    }
}
