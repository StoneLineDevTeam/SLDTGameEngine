package net.sldt_team.gameEngine.screen.components;

import net.sldt_team.gameEngine.screen.components.handler.ButtonHandler;
import net.sldt_team.gameEngine.input.MouseInput;
import net.sldt_team.gameEngine.renderengine.helper.ColorHelper;
import net.sldt_team.gameEngine.renderengine.FontRenderer;
import net.sldt_team.gameEngine.renderengine.RenderEngine;
import net.sldt_team.gameEngine.renderengine.Material;

public class QuitButton implements IScreenComponent {

    protected String buttonName;
    private Material butMaterial;

    private int buttonX;
    private int buttonY;
    private int buttonWidth;
    private int buttonHeight;
    protected boolean isMouseOver;
    protected Runnable buttonAction;

    protected MouseInput input;

    public QuitButton(String str, int x, int y, int width, int height, RenderEngine renderEngine) {
        buttonName = str;
        buttonX = x;
        buttonY = y;
        isMouseOver = false;
        buttonWidth = width;
        buttonHeight = height;
        butMaterial = renderEngine.getMaterial("buttons/stop");

        input = new MouseInput(new ButtonHandler(width, height, this));
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
     * Set the action when clicking (See ComponentAction.java)
     */
    public void setButtonAction(Runnable action) {
        buttonAction = action;
    }

    /**
     * Update the current Component
     */
    public void updateComponent() {
        input.updateInput();

        ButtonHandler handler = (ButtonHandler) input.getHandler();
        isMouseOver = handler.isMouseOver;
        if (handler.clicked) {
            buttonAction.run();
            handler.clicked = false;
        }
    }

    /**
     * Render Current Component
     */
    public void renderComponent(RenderEngine renderEngine, FontRenderer fontRenderer) {
        renderEngine.bindMaterial(butMaterial);
        if (isMouseOver) {
            renderEngine.renderTexturedQuadWithTextureCoords(buttonX, buttonY, buttonWidth, buttonHeight, 0, 128, 128, 128);
        } else {
            renderEngine.renderTexturedQuadWithTextureCoords(buttonX, buttonY, buttonWidth, buttonHeight, 0, 0, 128, 128);
        }
        fontRenderer.setRenderingSize(5);
        fontRenderer.setRenderingColor(new ColorHelper(0, 255, 255));
        fontRenderer.renderString(buttonName, (buttonX + buttonWidth / 2) - (fontRenderer.getStringWidth(buttonName) / 2), (buttonY + buttonHeight / 2) - (fontRenderer.getCharWidth() / 2));
    }
}