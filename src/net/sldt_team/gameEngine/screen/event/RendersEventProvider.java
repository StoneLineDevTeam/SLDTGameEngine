package net.sldt_team.gameEngine.screen.event;

import net.sldt_team.gameEngine.renderengine.FontRenderer;
import net.sldt_team.gameEngine.renderengine.RenderEngine;

public interface RendersEventProvider {

    /**
     * Renders event : render before and after everything
     * 1 - Before rendering everything (You can use this to cancel the draw of the default background)
     * 2 - After rendering everything
     */
    public void preRenderScreen(RenderEngine renderEngine, FontRenderer fontRenderer);

    public void postRenderScreen(RenderEngine renderEngine, FontRenderer fontRenderer);

}
