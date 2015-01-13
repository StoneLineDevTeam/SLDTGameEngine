package net.sldt_team.gameEngine.screen.components.handler;

import net.sldt_team.gameEngine.screen.components.IScreenComponent;
import net.sldt_team.gameEngine.input.mouse.IMouseHandler;
import net.sldt_team.gameEngine.input.mouse.MouseHelper;

public class TextFieldFocusHandler implements IMouseHandler {
    private int buttonWidth;
    private int buttonHeight;
    private IScreenComponent theComponent;

    public boolean isMouseOver;

    public TextFieldFocusHandler(int width, int height, IScreenComponent comp) {
        buttonWidth = width;
        buttonHeight = height;
        theComponent = comp;
    }

    public void mousePressed(int x, int y, MouseHelper helper) {
    }

    public void mouseClicked(int x, int y, MouseHelper helper) {
    }

    public void mouseMoved(int x, int y) {
        if ((x >= theComponent.getX()) && ((x <= (theComponent.getX() + buttonWidth)))) {
            if ((y >= theComponent.getY()) && (y <= (theComponent.getY() + buttonHeight))) {
                if ((y > theComponent.getY()) && (y < (theComponent.getY() + buttonHeight))) {
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
    }

    public void mouseWheelMoved(boolean up, boolean down, int amount) {
    }
}
