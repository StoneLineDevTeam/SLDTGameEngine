package net.sldt_team.gameEngine.screen.components;

import net.sldt_team.gameEngine.GameApplication;
import net.sldt_team.gameEngine.screen.components.handler.ButtonHandler;
import net.sldt_team.gameEngine.misc.gameSettings.GameSettings;
import net.sldt_team.gameEngine.input.MouseInput;
import net.sldt_team.gameEngine.particle.ParticleEngine;
import net.sldt_team.gameEngine.renderengine.helper.ColorHelper;
import net.sldt_team.gameEngine.renderengine.FontRenderer;
import net.sldt_team.gameEngine.renderengine.RenderEngine;
import net.sldt_team.gameEngine.renderengine.Material;
import net.sldt_team.gameEngine.screen.Screen;

public class NormalButton implements IScreenComponent {

    protected String buttonName;
    private Material butMaterial;

    private int buttonX;
    private int buttonY;
    private int buttonWidth;
    private int buttonHeight;
    protected boolean isMouseOver;

    protected Screen parentScreen;
    private int id;

    private GameSettings gameSettings;
    private ParticleEngine particleEngine;

    protected MouseInput input;

    public NormalButton(String str, int x, int y, int width, int height, GameApplication theGame) {
        buttonName = str;
        buttonX = x;
        buttonY = y;
        isMouseOver = false;
        buttonWidth = width;
        buttonHeight = height;
        butMaterial = theGame.renderEngine.getMaterial("buttons/normal");

        parentScreen = theGame.getCurrentFrame();
        id = parentScreen.getComponentsCount();

        gameSettings = theGame.gameSettings;
        particleEngine = theGame.particleEngine;

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
        id = parentScreen.getComponentsCount() - 1;
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
            parentScreen.actionPerformed(id, this);
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
            if (gameSettings.isParticlesActivated) {
                particleEngine.spawnParticle("button", buttonX - 16, (buttonY + buttonHeight / 2) - 16, buttonX - 16, (buttonY + buttonHeight / 2) - 16, 1, null);
                particleEngine.spawnParticle("button", (buttonX + buttonWidth) + 16, (buttonY + buttonHeight / 2) - 16, (buttonX + buttonWidth) + 16, (buttonY + buttonHeight / 2) - 16, 1, null);
            }
        } else {
            renderEngine.renderTexturedQuadWithTextureCoords(buttonX, buttonY, buttonWidth, buttonHeight, 0, 0, 128, 128);
        }
        if (!isMouseOver) {
            fontRenderer.setRenderingSize(6);
            fontRenderer.setRenderingColor(new ColorHelper(0, 255, 255));
            fontRenderer.renderString(buttonName, (buttonX + buttonWidth / 2) - (fontRenderer.getStringWidth(buttonName) / 2), (buttonY + buttonHeight / 2) - (fontRenderer.getCharWidth() / 2));
        } else {
            fontRenderer.setRenderingSize(6);
            fontRenderer.setRenderingColor(new ColorHelper(255, 255, 0));
            fontRenderer.renderString(buttonName, (buttonX + buttonWidth / 2) - (fontRenderer.getStringWidth(buttonName) / 2), (buttonY + buttonHeight / 2) - (fontRenderer.getCharWidth() / 2));
        }
    }
}
