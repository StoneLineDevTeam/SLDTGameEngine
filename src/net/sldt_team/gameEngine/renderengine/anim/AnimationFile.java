package net.sldt_team.gameEngine.renderengine.anim;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.sldt_team.gameEngine.GameApplication;
import net.sldt_team.gameEngine.renderengine.ColorRenderer;
import net.sldt_team.gameEngine.renderengine.RenderEngine;
import net.sldt_team.gameEngine.renderengine.Texture;
import net.sldt_team.gameEngine.util.FileUtilities;
import org.lwjgl.opengl.GL11;

import java.io.*;

public class AnimationFile {

    private File fileToLoad;
    private Animation animation;

    private int ticks;

    public AnimationFile(String file){
        fileToLoad = new File(FileUtilities.getResourcesDirectory() + File.separator + "animations" + File.separator + file + ".anim");
        if (!fileToLoad.exists()){
            GameApplication.log.severe("Failed to load animation " + file + " : no such file.");
            fileToLoad = null;
            return;
        }
        loadFile();
    }

    public void updateAnimation(){
        ticks++;
        if (ticks >= animation.getInterval()){
            animation.nextTexture();
            ticks = 0;
        }
    }

    public void renderAnimation(RenderEngine renderEngine, float x, float y, float width, float height){
        TextureFrame frame = animation.getCurrentTexture();
        Texture i = renderEngine.loadTexture(frame.path);
        renderEngine.bindTexture(i);
        renderEngine.setScaleLevel(frame.scale);
        GL11.glColor4f(frame.color.getRed(), frame.color.getGreen(), frame.color.getBlue(), frame.color.getAlpha());
        renderEngine.renderQuad(x, y, width, height);
    }

    private void loadFile(){
        try {
            GameApplication.log.info("Loading animation...");
            Gson gson = new GsonBuilder().create();
            AbstractAnimationFile file = gson.fromJson(new FileReader(fileToLoad), AbstractAnimationFile.class);
            TextureFrame[] frames = new TextureFrame[file.frames.length];
            if (file.frames.length == 0){
                GameApplication.log.severe("Failed to load animation : Animation frames must have, at least, 1 entry !");
                return;
            }
            for (int i = 0 ; i < file.frames.length ; i++){
                AbstractFrameColor c = file.frames[i].color;
                frames[i] = new TextureFrame(new ColorRenderer(c.r, c.g, c.b , c.a), file.frames[i].scale, file.frames[i].path);
                GameApplication.log.info("Frame found : " + frames[i].toString());
            }
            animation = new Animation(file.interval, frames);
            GameApplication.log.info("Interval is " + animation.getInterval());
            GameApplication.log.info("Animation successfully loaded !");
        } catch (Exception e) {
            GameApplication.log.severe("Failed to load animation : " + e.getClass().getName() + "_" + e.getMessage());
        }
    }
}
