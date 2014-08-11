package net.sldt_team.gameEngine.gui;

import net.sldt_team.gameEngine.renderengine.FontRenderer;
import net.sldt_team.gameEngine.renderengine.RenderEngine;

public interface IGui {
    public void renderGui(RenderEngine renderEngine, FontRenderer fontRenderer);

    public void onGuiInit();

    public void updateGui();
}
