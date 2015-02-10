package net.sldt_team.gameEngine.renderengine;

import net.sldt_team.gameEngine.renderengine.helper.ColorHelper;
import net.sldt_team.gameEngine.renderengine.helper.EnvironmentHelper;

public class Material {

    /**
     * The index of this texture in Graphic Card for easy access by GL
     */
    protected final int openGLIndex;

    /**
     * The width of this texture
     */
    protected final int textureWidth;

    /**
     * The height of this texture
     */
    protected final int textureHeight;

    /**
     * Is this texture created at runtime instead of loaded from asset
     */
    protected final boolean isRuntimeTexture;

    /**
     * The overwrite color for this texture
     */
    protected ColorHelper textureOverwriteColor;

    /**
     * Is the texture's color overwritten ?
     */
    protected boolean hasColorBeenOverwritten;

    /**
     * Texture environment (RGB or RGBA) for color replacement
     */
    protected EnvironmentHelper textureEnv;

    /**
     * Creates a texture file, used by RenderEngine
     * @param a Texture ID in graphics card
     * @param w Texture width
     * @param h Texture height
     */
    protected Material(int a, int w, int h, boolean runtime) {
        openGLIndex = a;
        textureWidth = w;
        textureHeight = h;

        textureEnv = EnvironmentHelper.RGB_COLOR_REPLACEMENT;
        isRuntimeTexture = runtime;
    }

    /**
     * Sets the overwritten color for this texture
     */
    public void setOverwriteColor(ColorHelper color){
        if (color == null){
            hasColorBeenOverwritten = false;
            textureOverwriteColor = null;
            return;
        }
        hasColorBeenOverwritten = true;
        textureOverwriteColor = color;
    }

    /**
     * Sets color replacement environment
     */
    public void setTextureEnvironment(EnvironmentHelper helper){
        textureEnv = helper;
    }
}
