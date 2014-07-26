package fr.sldt.gameEngine.controls;

import fr.sldt.gameEngine.controls.handler.ButtonHandler;
import fr.sldt.gameEngine.input.MouseInput;
import fr.sldt.gameEngine.renderengine.ColorRenderer;
import fr.sldt.gameEngine.renderengine.FontRenderer;
import fr.sldt.gameEngine.renderengine.RenderEngine;

public class RoundButton implements GameComponent {

    private String buttonName;
    private int normal;
    private int over;

    private int buttonX;
    private int buttonY;
    private int buttonWidth;
    private int buttonHeight;
    private boolean isMouseOver;
    private ComponentAction buttonAction;

    private MouseInput input;

    public RoundButton(String str, int x, int y, int width, int height, RenderEngine renderEngine) {
        buttonName = str;
        buttonX = x;
        buttonY = y;
        isMouseOver = false;
        buttonWidth = width;
        buttonHeight = height;
        normal = renderEngine.loadTexture("buttons/round.png");
        over = renderEngine.loadTexture("buttons/round_click.png");

        input = new MouseInput(new ButtonHandler(width, height, this));
    }

    /**
     * Set the action when clicking (See ComponentAction.java)
     */
    public void setButtonAction(ComponentAction action) {
        buttonAction = action;
    }

    /**
     * Called when the component is removed from a screen
     */
    public void onComponentRemove() {
    }

    /**
     * Called when this component is added to a screen
     */
    public void onComponentAdd() {
    }

    public int getX() {
        return buttonX;
    }

    public int getY() {
        return buttonY;
    }

    /**
     * Update the current Component
     */
    public void updateComponent() {
        input.updateInput();

        ButtonHandler handler = (ButtonHandler) input.getHandler();
        isMouseOver = handler.isMouseOver;
        if (handler.clicked) {
            buttonAction.actionPerformed();
            handler.clicked = false;
        }
    }

    /**
     * Render Current Component
     */
    public void renderComponent(RenderEngine renderEngine, FontRenderer fontRenderer) {
        if (isMouseOver) {
            renderEngine.bindTexture(over);
            renderEngine.renderQuad(buttonX, buttonY, buttonWidth, buttonHeight);
        } else {
            renderEngine.bindTexture(normal);
            renderEngine.renderQuad(buttonX, buttonY, buttonWidth, buttonHeight);
        }
        fontRenderer.setRenderingSize(5);
        fontRenderer.setRenderingColor(new ColorRenderer(0, 255, 255));
        fontRenderer.renderString(buttonName, (buttonX + buttonWidth / 2) - (fontRenderer.getStringWidth(buttonName) / 2), (buttonY + buttonHeight / 2) - (fontRenderer.CHAR_WIDTH / 2));
    }
}