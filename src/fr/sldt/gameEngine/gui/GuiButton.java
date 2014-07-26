package fr.sldt.gameEngine.gui;

import fr.sldt.gameEngine.GameApplication;
import fr.sldt.gameEngine.controls.ComponentAction;
import fr.sldt.gameEngine.renderengine.FontRenderer;
import fr.sldt.gameEngine.renderengine.RenderEngine;
import org.lwjgl.input.Mouse;

public class GuiButton {

    private int button;
    private int buttonClick;

    private int buttonX;
    private int buttonY;
    private int buttonWidth;
    private int buttonHeight;
    private boolean isMouseOver;
    private boolean clicked = false;
    public String text;
    private ComponentAction action;

    public GuiButton(String str, int b, int bc, int x, int y, int width, int height) {
        button = b;
        buttonClick = bc;
        buttonX = x;
        buttonY = y;
        buttonWidth = width;
        buttonHeight = height;
        text = str;
    }

    public void setAction(ComponentAction a) {
        action = a;
    }

    public void render(RenderEngine renderEngine, FontRenderer fontRenderer) {
        if (isMouseOver) {
            renderEngine.bindTexture(buttonClick);
            renderEngine.renderQuad(buttonX, buttonY, buttonWidth, buttonHeight);
        } else {
            renderEngine.bindTexture(button);
            renderEngine.renderQuad(buttonX, buttonY, buttonWidth, buttonHeight);
        }
        fontRenderer.unbindColor();
        fontRenderer.setRenderingSize(5);
        fontRenderer.renderString(text, (buttonX + buttonWidth / 2) - (fontRenderer.getStringWidth(text) / 2), (buttonY + buttonHeight / 2) - (fontRenderer.CHAR_WIDTH / 2));
    }

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
            action.actionPerformed();
            clicked = false;
        }
    }
}
