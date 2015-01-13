package net.sldt_team.gameEngine.renderengine.animation;

import net.sldt_team.gameEngine.GameApplication;
import net.sldt_team.gameEngine.renderengine.RenderEngine;
import net.sldt_team.gameEngine.renderengine.Texture;
import net.sldt_team.gameEngine.renderengine.helper.EnvironmentHelper;
import org.lwjgl.opengl.GL11;

public class Animation {

    private TextureFrame[] textures;
    private final int interval;
    private int currentTexture = 0;
    private boolean canIncrase = true;
    private final EnumAnimationType animationType;
    private final boolean isUsingUV;

    private int ticks;

    private EnvironmentHelper textureEnv;

    /**
     * Creates a new animation
     *
     * @param i      Interval between texture changes
     * @param type   The type of animation
     * @param frames The different frames of this animation
     * @param uv     Is this animation using UV texture's coordinates
     */
    public Animation(int i, EnumAnimationType type, TextureFrame[] frames, boolean uv, EnvironmentHelper env) {
        interval = i;
        animationType = type;
        textures = frames;
        isUsingUV = uv;
        textureEnv = env;
    }

    /**
     * Returns animation's type
     */
    public EnumAnimationType getType() {
        return animationType;
    }

    /**
     * Returns the interval of time between frame change
     */
    public int getInterval() {
        return interval;
    }

    /**
     * Sets the frame to render (work only for STATE_RENDERED types)
     */
    public void setCurrentFrameID(String id) {
        int i = 0;
        for (TextureFrame tex : textures) {
            if (tex.id.equals(id)) {
                break;
            }
            i++;
        }
        if (animationType == EnumAnimationType.STATE_RENDERED && i < textures.length) {
            TextureFrame frame = textures[i];
            if (frame != null) {
                currentTexture = i;
            }
        }
    }

    private void nextTexture() {
        if (animationType == EnumAnimationType.ONEWAY_RENDERED) {
            if ((currentTexture + 1) < textures.length) {
                currentTexture++;
            } else {
                currentTexture = 0;
            }
        } else if (animationType == EnumAnimationType.TWOWAY_RENDERED) {
            if (currentTexture <= 0 || !(currentTexture >= (textures.length - 1)) && canIncrase) {
                currentTexture++;
            } else {
                canIncrase = false;
                currentTexture--;
                if (currentTexture <= 0) {
                    canIncrase = true;
                }
            }
        } else if (animationType == EnumAnimationType.FONT_RENDERED) {
            if ((currentTexture + 1) < textures.length) {
                currentTexture++;
            } else {
                currentTexture = 0;
            }
        }
    }

    /**
     * Returns the current texture that is being drawn (used by animated fonts)
     */
    public Texture getCurrentTexture(RenderEngine renderEngine) {
        return renderEngine.loadTexture(getCurrentFrame().path);
    }

    /**
     * @exclude
     */
    protected TextureFrame getCurrentFrame() {
        return textures[currentTexture];
    }

    /**
     * Updates this animation
     */
    public void update() {
        if (animationType == EnumAnimationType.STATE_RENDERED) {
            return;
        }
        ticks++;
        if (ticks >= getInterval()) {
            nextTexture();
            ticks = 0;
        }
    }

    /**
     * Renders this animation (args : Instance of RenderEngine, X-Coord, Y-Coord, Width, Height)
     */
    public void render(RenderEngine renderEngine, float x, float y, float width, float height) {
        renderEngine.addTranslationMatrix(x, y);

        TextureFrame frame = getCurrentFrame();
        Texture i = renderEngine.loadTexture(frame.path);
        if (i == null) {
            GameApplication.log.warning("Unable to render format : 'Unexpected Error Calling renderEngine.loadTexture' !");
            return;
        }
        i.setOverwriteColor(frame.color);
        i.setTextureEnvironment(textureEnv);
        renderEngine.bindTexture(i);

        renderEngine.enableMiddleRotationScale();
        renderEngine.setScaleLevel(frame.scale);

        if (isUsingUV) {
            renderEngine.renderTexturedQuadWithTextureCoords(x, y, width, height, frame.uCoord, frame.vCoord, frame.u1Coord, frame.v1Coord);
        } else {
            renderEngine.renderQuad(x, y, width, height);
        }
    }
}
