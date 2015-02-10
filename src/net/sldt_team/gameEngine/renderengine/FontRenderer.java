package net.sldt_team.gameEngine.renderengine;

import net.sldt_team.gameEngine.renderengine.animation.Animation;
import net.sldt_team.gameEngine.renderengine.animation.EnumAnimationType;
import net.sldt_team.gameEngine.renderengine.helper.ColorHelper;

import static org.lwjgl.opengl.GL11.*;

public class FontRenderer {

    private RenderEngine renderEngine;
    private Material fontMaterial;
    private Animation fontAnimation;
    private int CHAR_WIDTH = 16;
    private ColorHelper currentColor;

    private boolean isRotated;
    private boolean isSublined;
    private float rotation;

    private boolean forceUseGL = false;

    /**
     * @exclude
     */
    public FontRenderer(RenderEngine renderSystem, String name) {
        renderEngine = renderSystem;
        fontMaterial = renderEngine.getMaterial("fonts/" + name);
    }

    /**
     * Reloads FontRenderer using custom font name
     */
    public void reloadFontRenderer(String font) {
        fontMaterial = renderEngine.getMaterial("fonts/" + font);
        fontAnimation = null;
    }

    /**
     * Reloads FontRenderer using an Animation
     */
    public void reloadFontRenderer(Animation anim) {
        if (anim.getType() != EnumAnimationType.FONT_RENDERED) {
            renderEngine.logger.warning("Unable to reload FontRenderer using " + anim + " : Invalid AnimationType !");
            return;
        }
        fontAnimation = anim;
        fontMaterial = anim.getCurrentTexture(renderEngine);
    }

    /**
     * Returns the char width
     */
    public int getCharWidth() {
        return CHAR_WIDTH;
    }

    /**
     * Renders a shadowed string (args: the string, X-Coord, Y-Coord, shadow factor 0 for default)
     */
    public void renderShadowedString(String text, float x, float y, float factor) {
        boolean flag = false;
        if (factor <= 0.0F) {
            flag = true;
        }
        forceUseGL = true;
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_TEXTURE_2D);
        glColor4f(0.0F, 0.0F, 0.0F, 0.45F);
        if (flag) {
            drawString(text, (x + 6.59F), (y + CHAR_WIDTH) + 6.59F, CHAR_WIDTH, CHAR_WIDTH, ((CHAR_WIDTH / 2) * (-1)));
        } else {
            drawString(text, (x * factor), (y + CHAR_WIDTH) * factor, CHAR_WIDTH, CHAR_WIDTH, ((CHAR_WIDTH / 2) * (-1)));
        }
        if (currentColor != null) {
            glColor4f(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), currentColor.getAlpha());
        }
        drawString(text, x, y + CHAR_WIDTH, CHAR_WIDTH, CHAR_WIDTH, ((CHAR_WIDTH / 2) * (-1)));
        forceUseGL = false;

        if (isSublined) {
            if (currentColor != null) {
                renderEngine.bindColor(currentColor);
            } else {
                renderEngine.bindColor(new ColorHelper(0, 0, 0, 255));
            }
            renderEngine.renderQuad(x, y + CHAR_WIDTH + 5, getStringWidth(text) + 10, 5);
            renderEngine.bindColor(new ColorHelper(0, 0, 0, 100));
            if (flag) {
                renderEngine.renderQuad(x + 6.59F, y + CHAR_WIDTH + 6.59F, getStringWidth(text) + 10, 5);
            } else {
                renderEngine.renderQuad(x * factor, (y + CHAR_WIDTH) * factor, getStringWidth(text) + 10, 5);
            }
        }
    }

    /**
     * Renders a standard string (args: the text, X-Coord, Y-Coord)
     */
    public void renderString(String text, float x, float y) {
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_TEXTURE_2D);
        drawString(text, x, y + CHAR_WIDTH, CHAR_WIDTH, CHAR_WIDTH, ((CHAR_WIDTH / 2) * (-1)));
        if (isSublined) {
            if (currentColor != null) {
                renderEngine.bindColor(currentColor);
            } else {
                renderEngine.bindColor(new ColorHelper(0, 0, 0, 255));
            }
            renderEngine.renderQuad(x, y + CHAR_WIDTH + 5, getStringWidth(text) + 10, 5);
        }
    }

    /**
     * Renders a string that can dynamicaly change size (args: text, X-Coord, Y-Coord, text size)
     */
    public void renderEffectString(String text, float x, float y, int size) {
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_TEXTURE_2D);
        drawString(text, x, y + size, size, size, ((size / 2) * (-1)));
        if (isSublined) {
            if (currentColor != null) {
                renderEngine.bindColor(currentColor);
            } else {
                renderEngine.bindColor(new ColorHelper(0, 0, 0, 255));
            }
            renderEngine.renderQuad(x, y + CHAR_WIDTH + 5, calculateWidthForEffectString(text, size) + 10, 5);
        }
    }

    /**
     * @exclude
     */
    public void updateFontAnimation() {
        if (fontAnimation != null) {
            fontAnimation.update();
            fontMaterial = fontAnimation.getCurrentTexture(renderEngine);
        }
    }

    /**
     * Sets the rotation level
     */
    public void setRotationLevel(float rotateOf) {
        rotation = rotateOf;
        isRotated = true;
    }

    /**
     * Sets the sublined flag for render sublined strings
     */
    public void setFontSublined(boolean sublined) {
        isSublined = sublined;
    }

    /**
     * Unbinds the color (This function is defaulting back to black text color)
     */
    public void unbindColor() {
        currentColor = null;
    }

    /**
     * Sets the rendering size for standard string draw (WARNING : this uses power of two for texture size, so the size is being powered by two, example size 4 = 2^4 = 16)
     */
    public void setRenderingSize(int par1) {
        CHAR_WIDTH = (int) Math.pow(2, par1);
    }

    /**
     * Sets a rendering color
     */
    public void setRenderingColor(ColorHelper par1ColorHelper) {
        currentColor = par1ColorHelper;
    }

    /**
     * Get string width but with more presigion (used to calculate an effect string width)
     */
    public float calculateWidthForEffectString(String text, int charWidth) {
        return ((text.length() * charWidth) / 2F) + (float) charWidth;
    }

    /**
     * Returns the width of a given text using current set size
     */
    public int getStringWidth(String par1Str) {
        return ((par1Str.length() * CHAR_WIDTH) / 2) + CHAR_WIDTH;
    }

    private void drawString(String line, float xPos, float yPos, float charWidth, float charHeight, float xGap) {
        renderEngine.bindMaterial(fontMaterial);
        if (isRotated) {
            glPushMatrix();
            glTranslatef(((xPos + getStringWidth(line)) / 2), ((yPos + charHeight) / 2), 0);
            if (isRotated)
                glRotatef(rotation, 0, 0, 1);
            glTranslatef(-((xPos + getStringWidth(line)) / 2), -((yPos + charHeight) / 2), 0);
        }
        glBegin(GL_QUADS);
        if (!forceUseGL) {
            if (currentColor != null) {
                glColor4f(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), currentColor.getAlpha());
            } else {
                glColor4f(0.0F, 0.0F, 0.0F, 1.0F);
            }
        }
        for (int i = 0; i < line.length(); i++) {
            int code = (int) line.charAt(i);
            float dec = xGap;
            try {
                if (line.charAt(i) == ' ') {
                    dec = CHAR_WIDTH - 95;
                } else {
                    dec = xGap;
                }
            } catch (StringIndexOutOfBoundsException e) {
                e.printStackTrace();
            }

            int gridX = code % 16;
            int gridY = (code - gridX) / 16;
            float texX = (float) gridX / 16;
            float texY = (float) gridY / 16;

            glTexCoord2f(texX, texY);
            glVertex2f(((dec + charWidth) * i) + xPos, yPos - charHeight);

            glTexCoord2f(texX, texY + (1f / 16f));
            glVertex2f(((dec + charWidth) * i) + xPos, yPos);

            glTexCoord2f(texX + (1f / 16f), texY + (1f / 16f));
            glVertex2f(((dec + charWidth) * i) + xPos + charWidth, yPos);

            glTexCoord2f(texX + (1f / 16f), texY);
            glVertex2f(((dec + charWidth) * i) + xPos + charWidth, yPos - charHeight);
        }
        glEnd();
        if (isRotated) {
            glPopMatrix();
            isRotated = false;
        }
        glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
