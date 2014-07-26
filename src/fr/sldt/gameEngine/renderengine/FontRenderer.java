package fr.sldt.gameEngine.renderengine;

import static org.lwjgl.opengl.GL11.*;

public class FontRenderer {

    private RenderEngine renderEngine;
    private int fontTexture;
    public int CHAR_WIDTH = 16;
    private ColorRenderer currentColor;

    private boolean isScaled;
    private boolean isRotated;
    private float rotation;
    private float scale;

    private boolean forceUseGL = false;

    public FontRenderer(RenderEngine renderSystem, String path) {
        renderEngine = renderSystem;
        fontTexture = renderEngine.loadTexture(path);
    }

    /**
     * Renders a shadowed string
     */
    public void renderShadowedString(String text, float x, float y, float factor){
        boolean flag = false;
        if (factor <= 0.0F){
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
    }

    /**
     * Renders a standard string
     */
    public void renderString(String text, float x, float y) {
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_TEXTURE_2D);
        drawString(text, x, y + CHAR_WIDTH, CHAR_WIDTH, CHAR_WIDTH, ((CHAR_WIDTH / 2) * (-1)));
    }

    /**
     * Renders a string that can dynamicaly change size
     */
    public void renderEffectString(String text, float x, float y, int size){
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_TEXTURE_2D);
        drawString(text, x, y + size, size, size, ((size / 2) * (-1)));
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
     * @deprecated This has been deprecated due to large amount of issues with text not being rendered (use renderEffectString istead)
     */
    @Deprecated
    public void setScaleLevel(float scaleOf){
        scale = scaleOf;
        isScaled = true;
    }

    /**
     * Unbinds the color (This function is defaulting back to black text color)
     */
    public void unbindColor(){
        currentColor = null;
    }

    /**
     * Sets the rendering size for standard string draw (WARNING : this uses power of two for texture size, so the size is being powered by two, example size 4 = 2^4 = 16)
     */
    public void setRenderingSize(int par1){
        CHAR_WIDTH = (int)Math.pow(2, par1);
    }

    /**
     * Sets a rendering color
     */
    public void setRenderingColor(ColorRenderer par1ColorRenderer){
        currentColor = par1ColorRenderer;
    }

    /**
     * Get string width but with more presigion (used to calculate an effect string width)
     */
    public float calculateWidthForEffectString(String text, int charWidth){
        return (text.length() * charWidth) / 2F;
    }

    /**
     * Returns the width of a given text using current set size
     */
    public int getStringWidth(String par1Str){
        return (par1Str.length() * CHAR_WIDTH) / 2;
    }

    //line the string to draw, xpos and ypos the x-y coords of the text, charWidth and charHeight the width-height of the caracters begins to draw.
    private void drawString(String line, float xPos, float yPos, float charWidth, float charHeight, float xGap) {
        renderEngine.bindTexture(fontTexture);
        if (isRotated || isScaled){
            glPushMatrix();
            glTranslatef(((xPos + getStringWidth(line)) / 2), ((yPos + charHeight) / 2), 0);
            if (isRotated)
                glRotatef(rotation, 0, 0, 1);
            if (isScaled)
                glScalef(scale, scale, 1);
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
        for(int i = 0; i < line.length(); i++) {
            int code = (int) line.charAt(i);
            float dec = xGap;
            try {
                if (line.charAt(i) == ' '){
                    dec = CHAR_WIDTH - 95;
                } else {
                    dec = xGap;
                }
            } catch (StringIndexOutOfBoundsException e){
                e.printStackTrace();
            }
            int gridX = code % 16;
            int gridY = ( code - gridX ) / 16;
            float texX = (float) gridX / 16;
            float texY = (float) gridY / 16;

            glTexCoord2f(texX, texY);
            glVertex2f( ( ( dec + charWidth ) * i ) + xPos, yPos - charHeight);

            glTexCoord2f(texX, texY + (1f / 16f) );
            glVertex2f( ( ( dec + charWidth ) * i ) + xPos, yPos);

            glTexCoord2f(texX + (1f / 16f), texY + (1f / 16f));
            glVertex2f( ( ( dec + charWidth ) * i ) + xPos + charWidth, yPos);

            glTexCoord2f(texX + (1f / 16f), texY);
            glVertex2f( ( ( dec + charWidth ) * i ) + xPos + charWidth, yPos - charHeight);
        }
        glEnd();
        if (isRotated || isScaled){
            glPopMatrix();
            isRotated = false;
            isScaled = false;
        }
        glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
