package net.sldt_team.gameEngine.gui;

import net.sldt_team.gameEngine.GameApplication;
import net.sldt_team.gameEngine.renderengine.FontRenderer;
import net.sldt_team.gameEngine.renderengine.RenderEngine;
import net.sldt_team.gameEngine.renderengine.Material;
import org.lwjgl.input.Mouse;

public class GuiButton {

    private Material button;

    private int buttonX;
    private int buttonY;
    private int buttonWidth;
    private int buttonHeight;
    private boolean isMouseOver;
    private boolean clicked = false;
    private String text;
    private Runnable action;

    private int px;
    private int py;

    /**
     * Creates a Button to be used with anything that isn't a screen
     *
     * @param str     Button text
     * @param pixelsX Button U1
     * @param pixelsY Button V1
     * @param b       Button texture
     * @param x       Button X-Coord
     * @param y       Button Y-Coord
     * @param width   Button width
     * @param height  Button height
     */
    public GuiButton(String str, int pixelsX, int pixelsY, Material b, int x, int y, int width, int height) {
        button = b;
        buttonX = x;
        buttonY = y;
        buttonWidth = width;
        buttonHeight = height;
        text = str;

        px = pixelsX;
        py = pixelsY;
    }

    /**
     * Sets the action of the button
     */
    public void setAction(Runnable a) {
        action = a;
    }

    /**
     * Renders the button
     */
    public void render(RenderEngine renderEngine, FontRenderer fontRenderer) {
        renderEngine.bindMaterial(button);
        if (isMouseOver) {
            renderEngine.renderTexturedQuadWithTextureCoords(buttonX, buttonY, buttonWidth, buttonHeight, 0, py, px, py);
        } else {
            renderEngine.renderTexturedQuadWithTextureCoords(buttonX, buttonY, buttonWidth, buttonHeight, 0, 0, px, py);
        }
        fontRenderer.unbindColor();
        fontRenderer.setRenderingSize(5);
        fontRenderer.renderString(text, (buttonX + buttonWidth / 2) - (fontRenderer.getStringWidth(text) / 2), (buttonY + buttonHeight / 2) - (fontRenderer.getCharWidth() / 2));
    }

    /**
     * Updates the button
     */
    public void update() {
        int mouseX = Mouse.getX();
        int mouseY = GameApplication.getScreenHeight() - Mouse.getY();

        if ((mouseX >= buttonX) && ((mouseX <= (buttonX + buttonWidth)))) {
            if ((mouseY >= buttonY) && (mouseY <= (buttonY + buttonHeight))) {
                if ((mouseY > buttonY) && (mouseY < (buttonY + buttonHeight))) {
                    isMouseOver = true;
                } else {
                    isMouseOver = true;
                }
            } else {
                isMouseOver = false;
            }
        } else {
            isMouseOver = false;
        }

        if (Mouse.isButtonDown(0)) {
            if ((mouseX >= buttonX) && ((mouseX <= (buttonX + buttonWidth)))) {
                if ((mouseY >= buttonY) && (mouseY <= (buttonY + buttonHeight))) {
                    if ((mouseY > buttonY) && (mouseY < (buttonY + buttonHeight))) {
                        clicked = true;
                        return;
                    } else {
                        clicked = true;
                        return;
                    }
                }
            }
        }

        if (clicked) {
            action.run();
            clicked = false;
        }
    }
}
