package net.sldt_team.gameEngine.renderengine;

import static org.lwjgl.opengl.GL11.*;

import net.sldt_team.gameEngine.ExceptionHandler;
import net.sldt_team.gameEngine.exception.GameException;
import net.sldt_team.gameEngine.util.FileUtilities;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL12;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class RenderEngine {

    private Map<String, Integer> imageMap;
    private ByteBuffer missingTexture;
    private ColorRenderer currentColor;
    private boolean usingColor;

    private int currentTexWidth;
    private int currentTexHeight;

    private boolean isScaled;
    private boolean isRotated;
    private float rotation;
    private float scale;

    protected ExceptionHandler exceptionHandler;
    protected Logger logger;

    public RenderEngine(ExceptionHandler handler, AssetManager assets, Logger log) {
        imageMap = new HashMap<String, Integer>();
        try {
            missingTexture = mountTexture(RenderEngine.class.getResourceAsStream("missingTex.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        exceptionHandler = handler;
        logger = log;

        assets.initialize(this);
    }

    public ByteBuffer mountTexture(InputStream stream) throws IOException {
        PNGDecoder decoder = new PNGDecoder(stream);
        ByteBuffer buf = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
        decoder.decode(buf, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
        buf.flip();
        currentTexWidth = decoder.getWidth();
        currentTexHeight = decoder.getHeight();
        stream.close();
        return buf;
    }

    /**
     * Sets the rotation level
     */
    public void setRotationLevel(float rotateOf){
        rotation = rotateOf;
        isRotated = true;
    }

    /**
     * Sets the scale level
     */
    public void setScaleLevel(float scaleOf){
        scale = scaleOf;
        isScaled = true;
    }

    /**
     * Gets texture id from texture name
     */
    public int loadTexture(String path) {
        int id = -1;
        try {
            if (imageMap.get(path) != null) {
                return imageMap.get(path);
            } else {
                logger.warning("Unable to get asset : imageMap.get(\"" + path + "\") = null, binding missing texture to prevent RenderEngine fatal rendering exception...");
                ByteBuffer texture = mountTexture(RenderEngine.class.getResourceAsStream("missingTex.png"));
                glEnable(GL_TEXTURE_2D);
                id = glGenTextures();
                glBindTexture(GL_TEXTURE_2D, id);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, currentTexWidth, currentTexHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, texture);
                glDisable(GL_TEXTURE_2D);
                imageMap.put(path, id);
            }
        } catch (Exception e) {
            try {
                throw new LWJGLException("RENDER_ENGINE_FATAL_ERROR : MOUNT_TEXTURE_FAILURE");
            } catch (LWJGLException e1) {
                exceptionHandler.handleException(new GameException(e1));
            }
        }
        return id;
    }

    private void loadTextureFromAsset(String name, File path) {
        int id = -1;
        try {
            boolean flag = false;
            ByteBuffer texture = missingTexture;
            if (path == null) {
                flag = true;
            } else {
                if (FileUtilities.getFileExtention(path) != null) {
                    String extension = FileUtilities.getFileExtention(path).toUpperCase();
                    if (!path.exists() || !extension.equals("PNG")) {
                        flag = true;
                    }
                    if (!flag) {
                        FileInputStream stream = new FileInputStream(path);
                        texture = mountTexture(stream);
                    }
                } else {
                    flag = true;
                }
            }
            if (flag) {
                texture = mountTexture(RenderEngine.class.getResourceAsStream("missingTex.png"));
                logger.warning("RENDER ENGINE : Could not read input stream, using missing texture for \"" + name + "\"");
            }
            glEnable(GL_TEXTURE_2D);
            id = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, id);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, currentTexWidth, currentTexHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, texture);
            glDisable(GL_TEXTURE_2D);
        } catch (IOException e) {
            try {
                throw new LWJGLException("RENDER_ENGINE_FATAL_ERROR : MOUNT_TEXTURE_FAILURE");
            } catch (LWJGLException e1) {
                exceptionHandler.handleException(new GameException(e1));
            }
        }
        imageMap.put(name, id);
    }

    /**
     * Loads a texture from a file (WARNING : this function may be deleted soon)
     */
    @Deprecated
    public int loadTextureFromFile(File path) {
        int id = -1;
        try {
            ByteBuffer texture = mountTexture(new FileInputStream(path));
            glEnable(GL_TEXTURE_2D);
            id = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, id);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, currentTexWidth, currentTexHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, texture);
            glDisable(GL_TEXTURE_2D);
        } catch (IOException e) {
            try {
                throw new LWJGLException("RENDER_ENGINE_FATAL_ERROR : MOUNT_TEXTURE_FAILURE");
            } catch (LWJGLException e1) {
                exceptionHandler.handleException(new GameException(e1));
            }
        }
        return id;
    }

    protected boolean hasAsset(String path){
        return imageMap.containsKey(path);
    }

    protected void loadAssets(ArrayList<String> namesToMap, ArrayList<File> filesToLoad){
        for (int i = 0 ; i < namesToMap.size() ; i++){
            File f = filesToLoad.get(i);
            String name = namesToMap.get(i);
            if (!name.endsWith(".png")){
                logger.info("RENDER ENGINE : Not loading asset directory :  \"" + name + "\"");
                continue;
            }
            loadTextureFromAsset(name, f);
        }
    }

    /**
     * Binds a texture (args : the texture id from loadTexture int)
     */
    public void bindTexture(int textureID) {
        usingColor = false;
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, textureID);
    }

    /**
     * Renders a filled rectangle object at screen (args : x-coord, y-coord, width, height)
     */
    public void renderQuad(float x, float y, float width, float height) {
        if (isRotated || isScaled){
            glPushMatrix();
            glTranslatef(((x + width) / 2), ((y + height) / 2), 0);
            if (isRotated)
                glRotatef(rotation, 0, 0, 1);
            if (isScaled)
                glScalef(scale, scale, 1);
            glTranslatef(-((x + width) / 2), -((y + height) / 2), 0);
        }
        if (currentColor != null && usingColor) {
            glDisable(GL_TEXTURE_2D);
            glColor4f(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), currentColor.getAlpha());
        }
        glBegin(GL_QUADS);
        {
            glTexCoord2f(0, 0);
            glVertex2f(x, y);

            glTexCoord2f(1, 0);
            glVertex2f(width + x, y);

            glTexCoord2f(1, 1);
            glVertex2f(width + x, height + y);

            glTexCoord2f(0, 1);
            glVertex2f(x, height + y);
        }
        glEnd();
        if (currentColor != null && usingColor) {
            glEnable(GL_TEXTURE_2D);
            glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
        if (isRotated || isScaled){
            glPopMatrix();
            isRotated = false;
            isScaled = false;
        }
    }

    /**
     * Renders a simple rectangle object at screen with a stroke width (args : x-coord, y-coord, width, height, stroke width)
     */
    public void renderUnfilledQuad(float x, float y, float width, float height, float stroke){
        renderQuad(x, y, width, stroke);
        renderQuad((x + width) - stroke, y, stroke, height);
        renderQuad(x, (y + height) - stroke, width, stroke);
        renderQuad(x, y, stroke, height);
    }

    /**
     * Renders a simple circle object at screen (args : x-coord, y-ccord, radius)
     */
    public void renderUnfilledCircle(float x, float y, float radius){
        glPushMatrix();
        if (isRotated || isScaled){
            glTranslatef(((x + radius) / 2), ((y + radius) / 2), 0);
            if (isRotated)
                glRotatef(rotation, 0, 0, 1);
            if (isScaled)
                glScalef(scale, scale, 1);
            glTranslatef(-((x + radius) / 2), -((y + radius) / 2), 0);
        }
        if (currentColor != null && usingColor) {
            glDisable(GL_TEXTURE_2D);
            glColor4f(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), currentColor.getAlpha());
        }
        glTranslatef(x, y, 0);

        int sides = 20;  // The amount of segment to create the circle
        glBegin(GL_LINE_LOOP);
        for (int a = 0; a < 360; a += 360 / sides)
        {
            double heading = a * 3.1415926535897932384626433832795 / 180;
            glVertex2d(Math.cos(heading) * radius, Math.sin(heading) * radius);
        }
        glEnd();
        if (currentColor != null && usingColor) {
            glEnable(GL_TEXTURE_2D);
            glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
        if (isRotated || isScaled){
            isRotated = false;
            isScaled = false;
        }
        glPopMatrix();
    }

    /**
     * Renders a filled circle object at screen (args : x-coord, y-ccord, radius)
     */
    public void renderCircle(float x, float y, float radius){
        for (int i = 0 ; i <= radius ; i++){
            renderUnfilledCircle(x, y, i);
        }
    }

    /**
     * Renders a filled triangle object at screen (args : x-coord, y-ccord, width, height, factor to use when triangle is not isocel, is the triangle iscel ?)
     */
    public void renderTriangle(float x, float y, float width, float height, float factor, boolean isEqual) {
        if (isRotated || isScaled){
            glPushMatrix();
            glTranslatef(((x + width) / 2), ((y + height) / 2), 0);
            if (isRotated)
                glRotatef(rotation, 0, 0, 1);
            if (isScaled)
                glScalef(scale, scale, 1);
            glTranslatef(-((x + width) / 2), -((y + height) / 2), 0);
        }
        if (currentColor != null && usingColor) {
            glDisable(GL_TEXTURE_2D);
            glColor4f(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), currentColor.getAlpha());
        }
        glBegin(GL_TRIANGLES);
        {
            glTexCoord2f(0, 0);
            glVertex2f(x, y + height);

            glTexCoord2f(0, 1);
            glVertex2f(width + x, y + height);

            glTexCoord2f(1, 1);
            if (!isEqual) {
                glVertex2f((x + width) / factor, y);
            } else {
                glVertex2f((x + (width / 2F)), y);
            }
        }
        glEnd();
        if (currentColor != null && usingColor) {
            glEnable(GL_TEXTURE_2D);
            glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
        if (isRotated || isScaled){
            glPopMatrix();
            isRotated = false;
            isScaled = false;
        }
    }

    /**
     * Binds a color (args : the color to bind)
     */
    public void bindColor(ColorRenderer color) {
        currentColor = color;
        usingColor = true;
    }
}
