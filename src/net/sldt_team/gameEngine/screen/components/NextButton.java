package net.sldt_team.gameEngine.screen.components;

import net.sldt_team.gameEngine.GameApplication;
import net.sldt_team.gameEngine.screen.components.handler.ButtonHandler;
import net.sldt_team.gameEngine.input.MouseInput;
import net.sldt_team.gameEngine.renderengine.helper.ColorHelper;
import net.sldt_team.gameEngine.renderengine.FontRenderer;
import net.sldt_team.gameEngine.renderengine.RenderEngine;
import net.sldt_team.gameEngine.renderengine.Material;
import net.sldt_team.gameEngine.screen.Screen;

public class NextButton implements IScreenComponent {

    protected String buttonName;
    private Material butMaterial;

    private int buttonX;
    private int buttonY;
    private int buttonWidth;
    private int buttonHeight;
    protected boolean isMouseOver;

    protected Screen parentScreen;
    private int id;

    protected MouseInput input;

    public NextButton(String str, int x, int y, int width, int height, GameApplication theGame) {
        buttonName = str;
        buttonX = x;
        buttonY = y;
        isMouseOver = false;
        buttonWidth = width;
        buttonHeight = height;
        butMaterial = theGame.renderEngine.getMaterial("buttons/next");
        parentScreen = theGame.getCurrentFrame();
        id = parentScreen.getComponentsCount();

        input = new MouseInput(new ButtonHandler(width, height, this));
    }

    public void updateComponent() {
        input.updateInput();

        ButtonHandler handler = (ButtonHandler) input.getHandler();
        isMouseOver = handler.isMouseOver;
        if (handler.clicked) {
            parentScreen.actionPerformed(id, this);
            handler.clicked = false;
        }
    }

    public void onComponentRemove() {
    }

    public void onComponentAdd() {
        id = parentScreen.getComponentsCount() - 1;
    }

    public int getX() {
        return buttonX;
    }

    public int getY() {
        return buttonY;
    }

    public void renderComponent(RenderEngine renderEngine, FontRenderer fontRenderer) {
        renderEngine.bindMaterial(butMaterial);
        if (isMouseOver) {
            renderEngine.renderTexturedQuadWithTextureCoords(buttonX, buttonY, buttonWidth, buttonHeight, 0, 128, 128, 128);
        } else {
            renderEngine.renderTexturedQuadWithTextureCoords(buttonX, buttonY, buttonWidth, buttonHeight, 0, 0, 128, 128);
        }
        fontRenderer.setRenderingSize(5);
        fontRenderer.setRenderingColor(new ColorHelper(0, 255, 255));
        fontRenderer.renderString(buttonName, buttonX - fontRenderer.getStringWidth(buttonName) / 2, buttonY + buttonHeight / 2);
    }
}