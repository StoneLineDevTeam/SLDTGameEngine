package fr.sldt.gameEngine.screen;

import fr.sldt.gameEngine.renderengine.FontRenderer;
import fr.sldt.gameEngine.renderengine.RenderEngine;

public interface IScreen {

    /**
     * Updates the current screen
     */
    public void updateScreen();

    /**
     * Renders the current screen
     */
    public void renderScreen(RenderEngine renderEngine, FontRenderer fontRenderer);

    /**
     * Called when entering this screen
     */
    public void initScreen();

    /**
     * Called when exiting this screen
     */
    public void onExitingScreen();
}
