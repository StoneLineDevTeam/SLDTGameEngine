package fr.sldt.gameEngine.controls;

import fr.sldt.gameEngine.controls.handler.ButtonHandler;
import fr.sldt.gameEngine.ext.gameSettings.GameSettings;
import fr.sldt.gameEngine.input.MouseInput;
import fr.sldt.gameEngine.particle.ParticleManager;
import fr.sldt.gameEngine.renderengine.ColorRenderer;
import fr.sldt.gameEngine.renderengine.FontRenderer;
import fr.sldt.gameEngine.renderengine.RenderEngine;

public class NormalButton implements GameComponent {

    private String buttonName;
    private int normal;
    private int over;

    private int buttonX;
    private int buttonY;
    private int buttonWidth;
    private int buttonHeight;
    private boolean isMouseOver;
    private ComponentAction buttonAction;

    private GameSettings gameSettings;
    private ParticleManager particleManager;

    private MouseInput input;

    public NormalButton(String str, int x, int y, int width, int height, RenderEngine renderEngine, ParticleManager manager, GameSettings settings) {
        buttonName = str;
        buttonX = x;
        buttonY = y;
        isMouseOver = false;
        buttonWidth = width;
        buttonHeight = height;
        normal = renderEngine.loadTexture("buttons/normal.png");
        over = renderEngine.loadTexture("buttons/normal_click.png");

        gameSettings = settings;
        particleManager = manager;

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
    public void setButtonAction(ComponentAction action) {
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
            if (gameSettings.isParticlesActivated) {
                particleManager.spawnParticle("button", buttonX - 16, (buttonY + buttonHeight / 2) - 16, buttonX - 16, (buttonY + buttonHeight / 2) - 16, 1, null);
                particleManager.spawnParticle("button", (buttonX + buttonWidth) + 16, (buttonY + buttonHeight / 2) - 16, (buttonX + buttonWidth) + 16, (buttonY + buttonHeight / 2) - 16, 1, null);
            }
        } else {
            renderEngine.bindTexture(normal);
            renderEngine.renderQuad(buttonX, buttonY, buttonWidth, buttonHeight);
        }
        if (!isMouseOver) {
            fontRenderer.setRenderingSize(6);
            fontRenderer.setRenderingColor(new ColorRenderer(0, 255, 255));
            fontRenderer.renderString(buttonName, (buttonX + buttonWidth / 2) - (fontRenderer.getStringWidth(buttonName) / 2), (buttonY + buttonHeight / 2) - (fontRenderer.CHAR_WIDTH / 2));
        } else {
            fontRenderer.setRenderingSize(6);
            fontRenderer.setRenderingColor(new ColorRenderer(255, 255, 0));
            fontRenderer.renderString(buttonName, (buttonX + buttonWidth / 2) - (fontRenderer.getStringWidth(buttonName) / 2), (buttonY + buttonHeight / 2) - (fontRenderer.CHAR_WIDTH / 2));
        }
    }
}