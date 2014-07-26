package fr.sldt.gameEngine.gui;

import fr.sldt.gameEngine.renderengine.FontRenderer;
import fr.sldt.gameEngine.renderengine.RenderEngine;

public interface IGui {
    public void renderGui(RenderEngine renderEngine, FontRenderer fontRenderer);

    public void onGuiInit();

    public void updateGui();
}
