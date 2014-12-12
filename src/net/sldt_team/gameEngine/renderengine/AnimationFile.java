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

    private File fileToLoad;

    protected AnimationFile(String file) {
        fileToLoad = new File(file + ".anim");
        if (!fileToLoad.exists()) {
            GameApplication.log.severe("Failed to load format " + file + " : no such file.");
            fileToLoad = null;
        }
    }

    protected Animation loadFile() {
        try {
            GameApplication.log.info("Loading format...");
            Gson gson = new GsonBuilder().create();
            AbstractAnimationFile file = gson.fromJson(new FileReader(fileToLoad), AbstractAnimationFile.class);
            TextureFrame[] frames = new TextureFrame[file.frames.length];
            if (file.frames.length == 0) {
                GameApplication.log.severe("Failed to load format : Animation frames must have, at least, 1 entry !");
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
                GameApplication.log.info("Frame found : " + frames[i].toString());
            }
            Animation anim = new Animation(file.interval, file.type, frames, file.usingUV);
            GameApplication.log.info("Interval is " + anim.getInterval());
            GameApplication.log.info("Animation successfully loaded !");
            return anim;
        } catch (Exception e) {
            GameApplication.log.severe("Failed to load format : " + e.getClass().getName() + "_" + e.getMessage());
        }
        return null;
    }
}
