package fr.sldt.gameEngine.gui;

import fr.sldt.gameEngine.GameApplication;
import fr.sldt.gameEngine.renderengine.ColorRenderer;
import fr.sldt.gameEngine.renderengine.FontRenderer;
import fr.sldt.gameEngine.renderengine.RenderEngine;

public abstract class Gui implements IGui {

    protected GameApplication theGame;

    public void setGame(GameApplication game) {
        theGame = game;
    }

    public abstract void renderGui(RenderEngine renderEngine, FontRenderer fontRenderer);

    public abstract void onGuiInit();

    public abstract void updateGui();

    public void onRender(RenderEngine renderEngine, FontRenderer fontRenderer) {
        renderEngine.bindColor(new ColorRenderer(0, 0, 0, 64));
        renderEngine.renderQuad(0, 0, GameApplication.getScreenWidth(), GameApplication.getScreenHeight());

        renderGui(renderEngine, fontRenderer);
    }

    public void onUpdate() {
        updateGui();
    }
}
