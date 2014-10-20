package net.sldt_team.gameEngine.controls;

import net.sldt_team.gameEngine.renderengine.FontRenderer;
import net.sldt_team.gameEngine.renderengine.RenderEngine;

public interface ScreenComponent {

    /**
     * Render Current Component
     */
    public void renderComponent(RenderEngine renderEngine, FontRenderer fontRenderer);

    /**
     * Update the current Component
     */
    public void updateComponent();

    /**
     * Called when the component is removed from a screen
     */
    public void onComponentRemove();

    /**
     * Called when this component is added to a screen
     */
    public void onComponentAdd();

    /**
     * Returns respectively X coord and Y coord of the component
     */
    public int getX();
    public int getY();
}
