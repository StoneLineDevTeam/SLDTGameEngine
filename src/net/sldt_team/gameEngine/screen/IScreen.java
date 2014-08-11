package net.sldt_team.gameEngine.screen;

import net.sldt_team.gameEngine.renderengine.FontRenderer;
import net.sldt_team.gameEngine.renderengine.RenderEngine;

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
