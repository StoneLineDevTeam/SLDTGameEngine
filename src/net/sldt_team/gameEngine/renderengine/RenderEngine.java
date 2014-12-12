package net.sldt_team.gameEngine.renderengine;

import static org.lwjgl.opengl.GL11.*;

import net.sldt_team.gameEngine.ExceptionHandler;
import net.sldt_team.gameEngine.exception.GameException;
import net.sldt_team.gameEngine.renderengine.animation.Animation;
import net.sldt_team.gameEngine.renderengine.helper.ColorHelper;
import net.sldt_team.gameEngine.renderengine.helper.PNGHelper;
import net.sldt_team.gameEngine.renderengine.helper.SideHelper;
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

    private Map<String, Texture> textureMap;
    private Map<String, Animation> animationMap;
    private ColorHelper currentColor;
    private boolean usingColor;

    private int currentTexWidth;
    private int currentTexHeight;

    private boolean isScaled;
    private boolean isRotated;
    private float rotation;
    private float scale;

    private float rotationPointX;
    private float rotationPointY;

    private final Texture gradientTop;
    private final Texture gradientBottom;
    private final Texture gradientLeft;
    private final Texture gradientRight;
    private final Texture missingTex;

    /**
     * @exclude
     */
    protected ExceptionHandler exceptionHandler;

    /**
     * @exclude
     */
    protected Logger logger;

    private Texture currentBoundTexture;

    /**
     * @exclude
     */
    public RenderEngine(ExceptionHandler handler, AssetManager assets, Logger log) {
        textureMap = new HashMap<String, Texture>();
        animationMap = new HashMap<String, Animation>();
        exceptionHandler = handler;
        logger = log;

        assets.initialize(this);

        gradientTop = loadTexture("renderEngine/gradients/top");
        gradientBottom = loadTexture("renderEngine/gradients/bottom");
        gradientLeft = loadTexture("renderEngine/gradients/left");
        gradientRight = loadTexture("renderEngine/gradients/right");
        missingTex = loadTexture("renderEngine/missingTex");
    }

    /**
     * @exclude
     */
    public ByteBuffer mountTexture(InputStream stream) throws IOException {
        PNGHelper decoder = new PNGHelper(stream);
        ByteBuffer buf = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
        decoder.decode(buf, decoder.getWidth() * 4, PNGHelper.Format.RGBA);
        buf.flip();
        currentTexWidth = decoder.getWidth();
        currentTexHeight = decoder.getHeight();
        stream.close();
        return buf;
    }

    /**
     * Sets the rotation level
     */
    public void setRotationLevel(float rotateOf) {
        rotation = rotateOf;
        isRotated = true;
    }

    /**
     * Sets the rotation points
     */
    public void setRotationPoints(float f, float f1){
        rotationPointX = f;
        rotationPointY = f1;
    }

    /**
     * Sets the scale level
     */
    public void setScaleLevel(float scaleOf) {
        scale = scaleOf;
        isScaled = true;
    }

    /**
     * Gets texture id from texture name (You don't need to specify .png, the engine knows it)
     */
    public Texture loadTexture(String path) {
        String texPath = path + ".png";
        int id;
        try {
            if (textureMap.get(texPath) != null) {
                return textureMap.get(texPath);
            } else {
                return missingTex;
            }
        } catch (Exception e) {
            try {
                throw new LWJGLException("RENDER_ENGINE_FATAL_ERROR : MOUNT_TEXTURE_FAILURE");
            } catch (LWJGLException e1) {
                exceptionHandler.handleException(new GameException(e1));
            }
        }
        return null;
    }

    /**
     * Loads an format from the given path (Don't need to specify any file extension, the engine knows it)
     */
    public Animation loadAnimation(String path) {
        if (animationMap.containsKey(path)) {
            return animationMap.get(path);
        }
        String f = FileUtilities.getResourcesDirectory() + File.separator + "animations" + File.separator + path;
        AnimationFile file = new AnimationFile(f);
        Animation anim = file.loadFile();
        animationMap.put(path, anim);
        return anim;
    }

    private void loadTextureFromAsset(String name, File path) {
        int id = -1;
        try {
            boolean flag = false;
            ByteBuffer texture = null;
            if (path == null) {
                flag = true;
            } else {
                if (FileUtilities.getFileExtension(path) != null) {
                    String extension = FileUtilities.getFileExtension(path).toUpperCase();
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
                logger.warning("RENDER ENGINE : Unable to mount texture -> \"" + name + "\"");
                return;
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
        textureMap.put(name, new Texture(id, currentTexWidth, currentTexHeight));
    }

    /**
     * Returns if an asset exists at given path
     */
    public boolean hasAsset(String path) {
        return textureMap.containsKey(path) && textureMap.get(path) != null;
    }

    /**
     * @exclude
     */
    protected void loadAssets(ArrayList<String> namesToMap, ArrayList<File> filesToLoad) {
        for (int i = 0; i < namesToMap.size(); i++) {
            File f = filesToLoad.get(i);
            String name = namesToMap.get(i);
            if (!name.endsWith(".png")) {
                logger.info("RENDER ENGINE : Not loading asset directory :  \"" + name + "\"");
                continue;
            }
            loadTextureFromAsset(name, f);
        }
    }

    /**
     * Binds a texture (args : the texture id from loadTexture int)
     */
    public void bindTexture(Texture texture) {
        if (texture == null){
            bindMissingTexture();
            return;
        }
        usingColor = false;
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, texture.openGLIndex);
        currentBoundTexture = texture;
    }

    /**
     * Binds missing texture (This is for MissingTexture lovers #MISSING_TEXTURE#) !!
     */
    public void bindMissingTexture(){
        bindTexture(missingTex);
    }

    /**
     * Renders the current bound texture with texture coords (args : X-Coord, Y-Coord, Width, Height, TextureCoord X, TextureCoord Y, TextureCoord X1, TextureCoord Y1)
     */
    public void renderTexturedQuadWithTextureCoords(float x, float y, float width, float height, float u, float v, float u1, float v1) {
        float texX = u / currentBoundTexture.textureWidth;
        float texY = v / currentBoundTexture.textureHeight;

        float texW = texX + (u1 / currentBoundTexture.textureWidth);
        float texH = texY + (v1 / currentBoundTexture.textureHeight);

        if (isRotated || isScaled) {
            glPushMatrix();
            glTranslatef(((x + width) / rotationPointX), ((y + height) / rotationPointY), 0);
            if (isRotated)
                glRotatef(rotation, 0, 0, 1);
            if (isScaled)
                glScalef(scale, scale, 1);
            glTranslatef(-((x + width) / rotationPointX), -((y + height) / rotationPointY), 0);
        }

        if (currentBoundTexture.hasColorBeenOverwritten && currentBoundTexture.textureOverwriteColor != null) {
            glColor4f(currentBoundTexture.textureOverwriteColor.getRed(), currentBoundTexture.textureOverwriteColor.getGreen(), currentBoundTexture.textureOverwriteColor.getBlue(), currentBoundTexture.textureOverwriteColor.getAlpha());
        }
        glBegin(GL_QUADS);
        {
            glTexCoord2f(texX, texY);
            glVertex2f(x, y);

            glTexCoord2f(texW, texY);
            glVertex2f(width + x, y);

            glTexCoord2f(texW, texH);
            glVertex2f(width + x, height + y);

            glTexCoord2f(texX, texH);
            glVertex2f(x, height + y);
        }
        glEnd();
        if (currentBoundTexture.hasColorBeenOverwritten) {
            glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
        if (isRotated || isScaled) {
            glPopMatrix();
            isRotated = false;
            isScaled = false;
        }
    }

    /**
     * Renders a filled rectangle object at screen (args : x-coord, y-coord, width, height)
     */
    public void renderQuad(float x, float y, float width, float height) {
        if (isRotated || isScaled) {
            glPushMatrix();
            glTranslatef(((x + width) / rotationPointX), ((y + height) / rotationPointY), 0);
            if (isRotated)
                glRotatef(rotation, 0, 0, 1);
            if (isScaled)
                glScalef(scale, scale, 1);
            glTranslatef(-((x + width) / rotationPointX), -((y + height) / rotationPointY), 0);
        }
        if (currentColor != null && usingColor) {
            glDisable(GL_TEXTURE_2D);
            glColor4f(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), currentColor.getAlpha());
        } else if (currentBoundTexture.hasColorBeenOverwritten && currentBoundTexture.textureOverwriteColor != null) {
            glColor4f(currentBoundTexture.textureOverwriteColor.getRed(), currentBoundTexture.textureOverwriteColor.getGreen(), currentBoundTexture.textureOverwriteColor.getBlue(), currentBoundTexture.textureOverwriteColor.getAlpha());
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
        } else if (currentBoundTexture.hasColorBeenOverwritten) {
            glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
        if (isRotated || isScaled) {
            glPopMatrix();
            isRotated = false;
            isScaled = false;
        }
    }

    /**
     * Renders a simple rectangle object at screen with a stroke width (args : x-coord, y-coord, width, height, stroke width)
     */
    public void renderUnfilledQuad(float x, float y, float width, float height, float stroke) {
        renderQuad(x, y, width, stroke);
        renderQuad((x + width) - stroke, y, stroke, height);
        renderQuad(x, (y + height) - stroke, width, stroke);
        renderQuad(x, y, stroke, height);
    }

    /**
     * Renders a simple circle object at screen (args : x-coord, y-ccord, radius)
     */
    public void renderUnfilledCircle(float x, float y, float radius) {
        glPushMatrix();
        if (isRotated || isScaled) {
            glTranslatef(((x + radius) / rotationPointX), ((y + radius) / rotationPointY), 0);
            if (isRotated)
                glRotatef(rotation, 0, 0, 1);
            if (isScaled)
                glScalef(scale, scale, 1);
            glTranslatef(-((x + radius) / 2), -((y + radius) / 2), 0);
        }
        if (currentColor != null && usingColor) {
            glDisable(GL_TEXTURE_2D);
            glColor4f(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), currentColor.getAlpha());
        } else if (currentBoundTexture.hasColorBeenOverwritten && currentBoundTexture.textureOverwriteColor != null) {
            glColor4f(currentBoundTexture.textureOverwriteColor.getRed(), currentBoundTexture.textureOverwriteColor.getGreen(), currentBoundTexture.textureOverwriteColor.getBlue(), currentBoundTexture.textureOverwriteColor.getAlpha());
        }
        glTranslatef(x, y, 0);

        int sides = 20;  // The amount of segment to create the circle
        glBegin(GL_LINE_LOOP);
        for (int a = 0; a < 360; a += 360 / sides) {
            double heading = a * 3.1415926535897932384626433832795 / 180;
            glVertex2d(Math.cos(heading) * radius, Math.sin(heading) * radius);
        }
        glEnd();
        if (currentColor != null && usingColor) {
            glEnable(GL_TEXTURE_2D);
            glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        } else if (currentBoundTexture.hasColorBeenOverwritten) {
            glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
        if (isRotated || isScaled) {
            isRotated = false;
            isScaled = false;
        }
        glPopMatrix();
    }

    /**
     * Renders a filled circle object at screen (args : x-coord, y-ccord, radius)
     */
    public void renderCircle(float x, float y, float radius) {
        for (float i = 0; i <= radius; i += 0.1F) {
            renderUnfilledCircle(x, y, i);
        }
    }

    /**
     * Renders a filled triangle object at screen (args : x-coord, y-ccord, width, height, factor to use when triangle is not isocel, is the triangle iscel ?)
     */
    public void renderTriangle(float x, float y, float width, float height, float factor, boolean isEqual) {
        if (isRotated || isScaled) {
            glPushMatrix();
            glTranslatef(((x + width) / rotationPointX), ((y + height) / rotationPointY), 0);
            if (isRotated)
                glRotatef(rotation, 0, 0, 1);
            if (isScaled)
                glScalef(scale, scale, 1);
            glTranslatef(-((x + width) / rotationPointX), -((y + height) / rotationPointY), 0);
        }
        if (currentColor != null && usingColor) {
            glDisable(GL_TEXTURE_2D);
            glColor4f(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), currentColor.getAlpha());
        } else if (currentBoundTexture.hasColorBeenOverwritten && currentBoundTexture.textureOverwriteColor != null) {
            glColor4f(currentBoundTexture.textureOverwriteColor.getRed(), currentBoundTexture.textureOverwriteColor.getGreen(), currentBoundTexture.textureOverwriteColor.getBlue(), currentBoundTexture.textureOverwriteColor.getAlpha());
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
        } else if (currentBoundTexture.hasColorBeenOverwritten) {
            glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
        if (isRotated || isScaled) {
            glPopMatrix();
            isRotated = false;
            isScaled = false;
        }
    }

    /**
     * Binds a color (args : the color to bind)
     */
    public void bindColor(ColorHelper color) {
        currentColor = color;
        usingColor = true;
    }

    /**
     * Renders a gradient quad object at screen (args :  x-coord, y-coord, width, height, gradient's map color, the directions of the gradient map)
     */
    public void renderGradientQuad(float x, float y, float width, float height, ColorHelper color, SideHelper... sides){
        for (SideHelper side : sides){
            switch(side){
                case TOP:
                    gradientTop.setOverwriteColor(color);
                    bindTexture(gradientTop);
                    break;
                case BOTTOM:
                    gradientBottom.setOverwriteColor(color);
                    bindTexture(gradientBottom);
                    break;
                case LEFT:
                    gradientLeft.setOverwriteColor(color);
                    bindTexture(gradientLeft);
                    break;
                case RIGHT:
                    gradientRight.setOverwriteColor(color);
                    bindTexture(gradientRight);
                    break;
            }
            renderQuad(x, y, width, height);
        }
    }
}
