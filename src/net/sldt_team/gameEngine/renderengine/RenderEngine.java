package net.sldt_team.gameEngine.renderengine;

import static org.lwjgl.opengl.GL11.*;

import net.sldt_team.gameEngine.GameApplication;
import net.sldt_team.gameEngine.IExceptionHandler;
import net.sldt_team.gameEngine.exception.GameException;
import net.sldt_team.gameEngine.renderengine.animation.Animation;
import net.sldt_team.gameEngine.renderengine.assetSystem.Asset;
import net.sldt_team.gameEngine.renderengine.decoders.ITextureDecoder;
import net.sldt_team.gameEngine.renderengine.helper.TextureFormatHelper;
import net.sldt_team.gameEngine.renderengine.helper.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class RenderEngine {

    private Map<String, Material> textureMap;
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

    private final Material gradientTop;
    private final Material gradientBottom;
    private final Material gradientLeft;
    private final Material gradientRight;
    private final Material missingMat;

    private boolean scissoringEnabled;
    private boolean blendingEnabled;

    private boolean translationMatrixAdded;

    public final AssetsManager assetsManager;

    /**
     * @exclude
     */
    protected IExceptionHandler exceptionHandler;

    /**
     * @exclude
     */
    protected Logger logger;

    private Material currentBoundMaterial;

    /**
     * @exclude
     */
    public RenderEngine(IExceptionHandler handler, AssetsManager assets, Logger log) {
        assetsManager = assets;
        textureMap = new HashMap<String, Material>();
        animationMap = new HashMap<String, Animation>();
        exceptionHandler = handler;
        logger = log;

        assets.initialize(this);

        gradientTop = getMaterial("renderEngine/gradients/top");
        gradientBottom = getMaterial("renderEngine/gradients/bottom");
        gradientLeft = getMaterial("renderEngine/gradients/left");
        gradientRight = getMaterial("renderEngine/gradients/right");
        missingMat = getMaterial("renderEngine/missingTex");

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
            GameApplication.engineLogger.severe("RENDER ENGINE : Decoder " + textureDecoder.getDecoderName() + " has failed reading stream " + stream + " !");
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
     * Captures a part of the screen and return a buffered image corresponding to the captured pixels
     * NOTE : Do not call this method each frames, otherwise you may cause a memory leak
     */
    public BufferedImage captureScreen(int x, int y, int width, int height){
        //Gets pixels using OpenGL
        GL11.glReadBuffer(GL11.GL_FRONT);
        ByteBuffer screenBuffer = BufferUtils.createByteBuffer(width * height * 4);
        GL11.glReadPixels(x, y, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, screenBuffer);

        //Create a BufferedImage from pixels
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for(int imgY = 0; imgY < height; imgY++) {
            for(int imgX = 0; imgX < width; imgX++) {
                int i = imgY * width * 4 + imgX * 4;
                int r = screenBuffer.get(i) & 0xFF;
                int g = screenBuffer.get(i + 1) & 0xFF;
                int b = screenBuffer.get(i + 2) & 0xFF;
                image.setRGB(imgX, imgY, (0xFF << 24) | (r << 16) | (g << 8) | b);
            }
        }

        //Flip image from down to up
        AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
        tx.translate(0, -image.getHeight(null));
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        image = op.filter(image, null);

        return image;
    }

    /**
     * Creates a material from a byte array.
     * NOTE : You'll need to handle errors yourself.
     * NOTE_2 : Do not call this method each frames, otherwise you may cause a memory leak
     */
    public Material createRuntimeMaterial(String format, byte[] data) throws IOException {
        ByteBuffer openGLTexture = mountTexture(format, new ByteArrayInputStream(data));
        glEnable(GL_TEXTURE_2D);
        int id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, currentTexWidth, currentTexHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, openGLTexture);
        glDisable(GL_TEXTURE_2D);
        return new Material(id, currentTexWidth, currentTexHeight, true);
    }

    /**
     * Destroys the given runtime created material.
     * NOTE : Do not call this method each frames, otherwise you may cause a memory leak
     */
    public void destroyRuntimeMaterial(Material t) {
        if (!t.isRuntimeTexture){
            logger.warning("Tried to delete a non-runtime created texture !");
            return;
        }
        glDeleteTextures(t.openGLIndex);
        t.textureOverwriteColor = null;
        t.setTextureEnvironment(null);
        t.hasColorBeenOverwritten = false;
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
     * Returns a material from given material path (You don't need to specify extension, the engine knows it)
     */
    public Material getMaterial(String path) {
        String texPath = "materials/" + path;
        try {
            if (textureMap.get(texPath) != null) {
                return textureMap.get(texPath);
            } else {
                return missingMat;
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
     * Returns an animation from the given path (Don't need to specify any file extension, the engine knows it)
     */
    public Animation getAnimation(String path) {
        if (animationMap.containsKey(path)) {
            return animationMap.get(path);
        }
        String f = "animations/" + path + ".anim";
        if (!assetsManager.hasAsset(f)){
            logger.warning("Failed to load animation : no such file");
            return null;
        }
        Asset asset = assetsManager.getAsset(f);
        ByteArrayInputStream stream = new ByteArrayInputStream(asset.assetData);
        AnimationFile file = new AnimationFile(stream);
        Animation anim = file.loadFile();
        animationMap.put(path, anim);
        try {
            stream.close();
        } catch (IOException e) {
            logger.warning("There was an IO error while loading animation : " + f);
        }
        return anim;
    }

    private void loadTextureFromAsset(String name, byte[] data) {
        int id = -1;
        try {
            boolean flag = false;
            ByteBuffer texture = null;
            if (data == null) {
                flag = true;
            } else {
                String extension = "";
                int i = name.lastIndexOf('.');
                if (i > 0 && i < name.length() - 1) {
                    extension = name.substring(i + 1).toLowerCase();
                }
                name = name.replace("." + extension, "");

                texture = mountTexture(extension, new ByteArrayInputStream(data));
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
        textureMap.put(name, new Material(id, currentTexWidth, currentTexHeight, false));
    }

    /**
     * Reloads this rendering engine
     */
    public void reloadRenderingEngine() {
        List<Asset> assetList = assetsManager.getAllLoadedAssets();
        for (Asset asset : assetList) {
            if (asset.isTexture) {
                loadTextureFromAsset(asset.assetName, asset.assetData);
            }
        }
    }

    /**
     * Binds a material (args : the material from getMaterial)
     */
    public void bindMaterial(Material material) {
        if (material == null) {
            bindMissingMaterial();
            return;
        }
        usingColor = false;
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, material.openGLIndex);
        currentBoundMaterial = material;
    }

    /**
     * Binds missing material (This is for MissingMaterial lovers #MISSING_TEXTURE#) !!
     */
    public void bindMissingMaterial() {
        bindMaterial(missingMat);
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

    /**
     * Removes the current translation matrix
     */
    public void removeTranslationMatrix(){
        glPopMatrix();
        translationMatrixAdded = false;
    }

    /**
     * Renders the current bound texture with texture coords (args : X-Coord, Y-Coord, Width, Height, TextureCoord X, TextureCoord Y, TextureCoord X1, TextureCoord Y1)
     */
    public void renderTexturedQuadWithTextureCoords(float x, float y, float width, float height, float u, float v, float u1, float v1) {
        float texX = u / currentBoundMaterial.textureWidth;
        float texY = v / currentBoundMaterial.textureHeight;

        float texW = texX + (u1 / currentBoundMaterial.textureWidth);
        float texH = texY + (v1 / currentBoundMaterial.textureHeight);

        if (isRotated || isScaled) {
            x = 0;
            y = 0;
            addRotationScale(x, y, width, height);
        }

        if (currentBoundMaterial.hasColorBeenOverwritten && currentBoundMaterial.textureOverwriteColor != null) {
            switch (currentBoundMaterial.textureEnv) {
                case RGB_COLOR_REPLACEMENT:
                    glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
                    break;
                case RGBA_COLOR_REPLACEMENT:
                    glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_ADD);
                    break;
            }
            glColor4f(currentBoundMaterial.textureOverwriteColor.getRed(), currentBoundMaterial.textureOverwriteColor.getGreen(), currentBoundMaterial.textureOverwriteColor.getBlue(), currentBoundMaterial.textureOverwriteColor.getAlpha());
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
        if (currentBoundMaterial.hasColorBeenOverwritten) {
            glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            if (currentBoundMaterial.textureEnv == EnvironmentHelper.RGBA_COLOR_REPLACEMENT) {
                glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
            }
        }
        if (isRotated || isScaled) {
            resetRotationScale();
            if (translationMatrixAdded) {
                removeTranslationMatrix();
            }
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
        } else if (currentBoundMaterial.hasColorBeenOverwritten && currentBoundMaterial.textureOverwriteColor != null) {
            switch (currentBoundMaterial.textureEnv) {
                case RGB_COLOR_REPLACEMENT:
                    glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
                    break;
                case RGBA_COLOR_REPLACEMENT:
                    glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_ADD);
                    break;
            }
            glColor4f(currentBoundMaterial.textureOverwriteColor.getRed(), currentBoundMaterial.textureOverwriteColor.getGreen(), currentBoundMaterial.textureOverwriteColor.getBlue(), currentBoundMaterial.textureOverwriteColor.getAlpha());
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
        } else if (currentBoundMaterial.hasColorBeenOverwritten) {
            glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            if (currentBoundMaterial.textureEnv == EnvironmentHelper.RGBA_COLOR_REPLACEMENT) {
                glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
            }
        }
        if (isRotated || isScaled) {
            resetRotationScale();
            if (translationMatrixAdded) {
                removeTranslationMatrix();
            }
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
        } else if (currentBoundMaterial.hasColorBeenOverwritten && currentBoundMaterial.textureOverwriteColor != null) {
            switch (currentBoundMaterial.textureEnv) {
                case RGB_COLOR_REPLACEMENT:
                    glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
                    break;
                case RGBA_COLOR_REPLACEMENT:
                    glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_ADD);
                    break;
            }
            glColor4f(currentBoundMaterial.textureOverwriteColor.getRed(), currentBoundMaterial.textureOverwriteColor.getGreen(), currentBoundMaterial.textureOverwriteColor.getBlue(), currentBoundMaterial.textureOverwriteColor.getAlpha());
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
        } else if (currentBoundMaterial.hasColorBeenOverwritten) {
            glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            if (currentBoundMaterial.textureEnv == EnvironmentHelper.RGBA_COLOR_REPLACEMENT) {
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
        } else if (currentBoundMaterial.hasColorBeenOverwritten && currentBoundMaterial.textureOverwriteColor != null) {
            switch (currentBoundMaterial.textureEnv) {
                case RGB_COLOR_REPLACEMENT:
                    glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
                    break;
                case RGBA_COLOR_REPLACEMENT:
                    glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_ADD);
                    break;
            }
            glColor4f(currentBoundMaterial.textureOverwriteColor.getRed(), currentBoundMaterial.textureOverwriteColor.getGreen(), currentBoundMaterial.textureOverwriteColor.getBlue(), currentBoundMaterial.textureOverwriteColor.getAlpha());
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
        } else if (currentBoundMaterial.hasColorBeenOverwritten) {
            glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            if (currentBoundMaterial.textureEnv == EnvironmentHelper.RGBA_COLOR_REPLACEMENT) {
                glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
            }
        }
        if (isRotated || isScaled) {
            resetRotationScale();
            if (translationMatrixAdded) {
                removeTranslationMatrix();
            }
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
                    bindMaterial(gradientTop);
                    break;
                case BOTTOM:
                    gradientBottom.setOverwriteColor(color);
                    bindMaterial(gradientBottom);
                    break;
                case LEFT:
                    gradientLeft.setOverwriteColor(color);
                    bindMaterial(gradientLeft);
                    break;
                case RIGHT:
                    gradientRight.setOverwriteColor(color);
                    bindMaterial(gradientRight);
                    break;
            }
            renderQuad(x, y, width, height);
        }
    }
}
