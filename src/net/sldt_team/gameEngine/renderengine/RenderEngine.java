package net.sldt_team.gameEngine.renderengine;

import static org.lwjgl.opengl.GL11.*;

import net.sldt_team.gameEngine.GameApplication;
import net.sldt_team.gameEngine.IExceptionHandler;
import net.sldt_team.gameEngine.exception.GameException;
import net.sldt_team.gameEngine.renderengine.animation.Animation;
import net.sldt_team.gameEngine.renderengine.decoders.ITextureDecoder;
import net.sldt_team.gameEngine.renderengine.helper.*;
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

    private float rotationScalePointX;
    private float rotationScalePointY;
    private boolean rotateScaleInMiddle = true;

    private final Texture gradientTop;
    private final Texture gradientBottom;
    private final Texture gradientLeft;
    private final Texture gradientRight;
    private final Texture missingTex;

    private boolean scissoringEnabled;
    private boolean blendingEnabled;

    private boolean translationMatrixAdded;

    /**
     * @exclude
     */
    protected IExceptionHandler exceptionHandler;

    /**
     * @exclude
     */
    protected Logger logger;

    private Texture currentBoundTexture;

    /**
     * @exclude
     */
    public RenderEngine(IExceptionHandler handler, AssetManager assets, Logger log) {
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

        gradientTop.setTextureEnvironment(EnvironmentHelper.RGBA_COLOR_REPLACEMENT);
        gradientBottom.setTextureEnvironment(EnvironmentHelper.RGBA_COLOR_REPLACEMENT);
        gradientLeft.setTextureEnvironment(EnvironmentHelper.RGBA_COLOR_REPLACEMENT);
        gradientRight.setTextureEnvironment(EnvironmentHelper.RGBA_COLOR_REPLACEMENT);

        scissoringEnabled = false;
    }

    /**
     * Returns a ByteBuffer of an input texture stream
     */
    public ByteBuffer mountTexture(String fileExtension, InputStream stream) throws IOException {
        ITextureDecoder textureDecoder = TextureFormatHelper.instance.getTextureDecoderForFormat(fileExtension);
        if (textureDecoder == null || stream == null){
            try {
                throw new LWJGLException("RENDER_ENGINE_FATAL_ERROR : DECODER_FAILURE_NULL");
            } catch (LWJGLException e) {
                exceptionHandler.handleException(new GameException(e));
            }
            return null;
        }
        textureDecoder.initialize(stream);
        ByteBuffer buffer = textureDecoder.getTextureData();
        currentTexWidth = textureDecoder.getTextureWidth();
        currentTexHeight = textureDecoder.getTextureHeight();
        textureDecoder.clearBuffers();
        if (buffer == null){
            GameApplication.log.severe("RENDER ENGINE : Decoder " + textureDecoder.getDecoderName() + " has failed reading stream " + stream + " !");
        }
        stream.close();
        return buffer;
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
    public void setRotationScalePoints(float f, float f1) {
        rotationScalePointX = f;
        rotationScalePointY = f1;
        rotateScaleInMiddle = false;
    }

    /**
     * Enables middle rotations
     */
    public void enableMiddleRotationScale() {
        rotateScaleInMiddle = true;
    }

    /**
     * Sets the scale level
     */
    public void setScaleLevel(float scaleOf) {
        scale = scaleOf;
        isScaled = true;
    }

    /**
     * Adds the scissor rect to the screen (args: x-coord, y-coord, width, height)
     */
    public void addScissorRect(int x, int y, int width, int height){
        if (!scissoringEnabled) {
            glEnable(GL_SCISSOR_TEST);
        }
        glScissor(x, y, width, height);
    }

    /**
     * Removes the scissor rect
     */
    public void removeScissorRect(){
        if (scissoringEnabled){
            glDisable(GL_SCISSOR_TEST);
        }
    }

    /**
     * Disables the blending
     */
    public void disableBlending() {
        if (blendingEnabled) {
            glDisable(GL_BLEND);
            blendingEnabled = false;
        }
    }

    /**
     * Enables the blending
     */
    public void enableBlending() {
        if (!blendingEnabled) {
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        }
    }

    /**
     * Gets texture from texture path (You don't need to specify .png, the engine knows it)
     */
    public Texture loadTexture(String path) {
        String texPath = path + ".png";
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
     * Loads an animation from the given path (Don't need to specify any file extension, the engine knows it)
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
                    if (!path.exists() || !TextureFormatHelper.instance.canTextureFileBeRead(path)) {
                        flag = true;
                    }
                    if (!flag) {
                        FileInputStream stream = new FileInputStream(path);
                        texture = mountTexture(extension, stream);
                    }
                } else {
                    flag = true;
                }
            }
            if (flag || texture == null) {
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
        if (texture == null) {
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
    public void bindMissingTexture() {
        bindTexture(missingTex);
    }

    private void addRotationScale(float x, float y, float width, float height) {
        glPushMatrix();
        if (rotateScaleInMiddle) {
            glTranslatef(((x + width) / 2), ((y + height) / 2), 0);
        } else {
            glTranslatef((x + rotationScalePointX), (y + rotationScalePointY), 0);
        }

        if (isRotated)
            glRotatef(rotation, 0, 0, 1);
        if (isScaled)
            glScalef(scale, scale, 1);

        if (rotateScaleInMiddle){
            glTranslatef(-((x + width) / 2), -((y + height) / 2), 0);
        } else {
            glTranslatef(-(x + rotationScalePointX), -(y + rotationScalePointY), 0);
        }
    }
    private void resetRotationScale(){
        glPopMatrix();
        isRotated = false;
        isScaled = false;
    }

    /**
     * Adds a translation matrix
     */
    public void addTranslationMatrix(float x, float y){
        glPushMatrix();
        glTranslatef(x, y, 0);
        translationMatrixAdded = true;
    }

    private void removeTranslationMatrix(){
        glPopMatrix();
        translationMatrixAdded = false;
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
            x = 0;
            y = 0;
            addRotationScale(x, y, width, height);
        }

        if (currentBoundTexture.hasColorBeenOverwritten && currentBoundTexture.textureOverwriteColor != null) {
            switch (currentBoundTexture.textureEnv) {
                case RGB_COLOR_REPLACEMENT:
                    glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
                    break;
                case RGBA_COLOR_REPLACEMENT:
                    glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_ADD);
                    break;
            }
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
            if (currentBoundTexture.textureEnv == EnvironmentHelper.RGBA_COLOR_REPLACEMENT) {
                glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
            }
        }
        if (isRotated || isScaled) {
            resetRotationScale();
        }

        if (translationMatrixAdded) {
            removeTranslationMatrix();
        }
    }

    /**
     * Renders a filled rectangle object at screen (args : x-coord, y-coord, width, height)
     */
    public void renderQuad(float x, float y, float width, float height) {
        if (isRotated || isScaled) {
            x = 0;
            y = 0;
            addRotationScale(x, y, width, height);
        }

        if (currentColor != null && usingColor) {
            glDisable(GL_TEXTURE_2D);
            glColor4f(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), currentColor.getAlpha());
        } else if (currentBoundTexture.hasColorBeenOverwritten && currentBoundTexture.textureOverwriteColor != null) {
            switch (currentBoundTexture.textureEnv) {
                case RGB_COLOR_REPLACEMENT:
                    glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
                    break;
                case RGBA_COLOR_REPLACEMENT:
                    glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_ADD);
                    break;
            }
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
            if (currentBoundTexture.textureEnv == EnvironmentHelper.RGBA_COLOR_REPLACEMENT) {
                glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
            }
        }
        if (isRotated || isScaled) {
            resetRotationScale();
        }

        if (translationMatrixAdded) {
            removeTranslationMatrix();
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
        if (currentColor != null && usingColor) {
            glDisable(GL_TEXTURE_2D);
            glColor4f(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), currentColor.getAlpha());
        } else if (currentBoundTexture.hasColorBeenOverwritten && currentBoundTexture.textureOverwriteColor != null) {
            switch (currentBoundTexture.textureEnv) {
                case RGB_COLOR_REPLACEMENT:
                    glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
                    break;
                case RGBA_COLOR_REPLACEMENT:
                    glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_ADD);
                    break;
            }
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
            if (currentBoundTexture.textureEnv == EnvironmentHelper.RGBA_COLOR_REPLACEMENT) {
                glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
            }
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
            x = 0;
            y = 0;
            addRotationScale(x, y, width, height);
        }

        if (currentColor != null && usingColor) {
            glDisable(GL_TEXTURE_2D);
            glColor4f(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), currentColor.getAlpha());
        } else if (currentBoundTexture.hasColorBeenOverwritten && currentBoundTexture.textureOverwriteColor != null) {
            switch (currentBoundTexture.textureEnv) {
                case RGB_COLOR_REPLACEMENT:
                    glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
                    break;
                case RGBA_COLOR_REPLACEMENT:
                    glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_ADD);
                    break;
            }
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
            if (currentBoundTexture.textureEnv == EnvironmentHelper.RGBA_COLOR_REPLACEMENT) {
                glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
            }
        }
        if (isRotated || isScaled) {
            resetRotationScale();
        }

        if (translationMatrixAdded) {
            removeTranslationMatrix();
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
    public void renderGradientQuad(float x, float y, float width, float height, ColorHelper color, SideHelper... sides) {
        for (SideHelper side : sides) {
            switch (side) {
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
