package net.sldt_team.gameEngine.renderengine;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.sldt_team.gameEngine.GameApplication;
import net.sldt_team.gameEngine.renderengine.helper.ColorHelper;
import net.sldt_team.gameEngine.renderengine.animation.*;

import java.io.*;

/**
 * @exclude
 */
public class AnimationFile {

    private InputStream fileData;

    protected AnimationFile(InputStream data) {
        fileData = data;
    }

    protected Animation loadFile() {
        try {
            GameApplication.engineLogger.info("Loading animation...");
            Gson gson = new GsonBuilder().create();
            AbstractAnimationFile file = gson.fromJson(new InputStreamReader(fileData), AbstractAnimationFile.class);
            TextureFrame[] frames = new TextureFrame[file.frames.length];
            if (file.frames.length == 0) {
                GameApplication.engineLogger.severe("Failed to load animation : Animation frames must have, at least, 1 entry !");
                return null;
            }
            for (int i = 0; i < file.frames.length; i++) {
                AbstractFrameColor c = file.frames[i].color;
                if (file.usingUV) {
                    AbstractFrameUV uvCoords = file.frames[i].uv;
                    frames[i] = new TextureFrame(new ColorHelper(c.r, c.g, c.b, c.a), file.frames[i].scale, file.frames[i].path, file.frames[i].id, uvCoords.u, uvCoords.v, uvCoords.u1, uvCoords.v1);
                } else {
                    frames[i] = new TextureFrame(new ColorHelper(c.r, c.g, c.b, c.a), file.frames[i].scale, file.frames[i].path, file.frames[i].id);
                }
                GameApplication.engineLogger.info("Frame found : " + frames[i].toString());
            }
            Animation anim = new Animation(file.interval, file.type, frames, file.usingUV, file.texEnv);
            GameApplication.engineLogger.info("Interval is " + anim.getInterval());
            GameApplication.engineLogger.info("Animation successfully loaded !");
            return anim;
        } catch (Exception e) {
            GameApplication.engineLogger.severe("Failed to load animation : " + e.getClass().getName() + "_" + e.getMessage());
        }
        return null;
    }
}
