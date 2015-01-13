package net.sldt_team.gameEngine.screen.components.handler;

import net.sldt_team.gameEngine.screen.components.IScreenComponent;
import net.sldt_team.gameEngine.input.mouse.IMouseHandler;
import net.sldt_team.gameEngine.input.mouse.MouseHelper;

public class ButtonHandler implements IMouseHandler {

    private int buttonWidth;
    private int buttonHeight;
    private IScreenComponent theComponent;

    public boolean isMouseOver;
    public boolean clicked;

    public ButtonHandler(int width, int height, IScreenComponent component) {
        buttonWidth = width;
        buttonHeight = height;
        theComponent = component;
    }

    public void mousePressed(int x, int y, MouseHelper helper) {
    }

    public void mouseClicked(int x, int y, MouseHelper helper) {
        if (helper == MouseHelper.LEFT_CLICK) {
            if ((x >= theComponent.getX()) && ((x <= (theComponent.getX() + buttonWidth)))) {
                if ((y >= theComponent.getY()) && (y <= (theComponent.getY() + buttonHeight))) {
                    if ((y > theComponent.getY()) && (y < (theComponent.getY() + buttonHeight))) {
                        clicked = true;
                    } else {
                        clicked = true;
                    }
                }
            }
        }
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
