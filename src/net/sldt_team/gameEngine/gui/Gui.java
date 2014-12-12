package net.sldt_team.gameEngine.gui;

import net.sldt_team.gameEngine.GameApplication;
import net.sldt_team.gameEngine.gui.vgui.VGuiBase;
import net.sldt_team.gameEngine.renderengine.helper.ColorHelper;
import net.sldt_team.gameEngine.renderengine.FontRenderer;
import net.sldt_team.gameEngine.renderengine.RenderEngine;

public abstract class Gui {

    /**
     * Current displayed VGui (null when not using VGui)
     */
    protected VGuiBase baseGui;

    /**
     * Used to create a Gui
     * @param base When you'd like to use the VirtualGui library, please specify what main VGui control you'd like to use
     */
    public Gui(VGuiBase base){
        baseGui = base;
    }

    /**
     * An instance of the current Game App
     */
    protected GameApplication theGame;

    /**
     * @exclude
     */
    public void setGame(GameApplication game) {
        theGame = game;
    }

    /**
     * Called when gui is rendered
     */
    public abstract void renderGui(RenderEngine renderEngine, FontRenderer fontRenderer);

    /**
     * Called when gui is inited
     */
    public abstract void onGuiInit();

    /**
     * Called when gui is updated
     */
    public abstract void updateGui();

    /**
     * @exclude
     */
    public void onRender(RenderEngine renderEngine, FontRenderer fontRenderer) {
        renderEngine.bindColor(new ColorHelper(0, 0, 0, 64));
        renderEngine.renderQuad(0, 0, GameApplication.getScreenWidth(), GameApplication.getScreenHeight());

        if (baseGui != null){
            baseGui.doRendering(renderEngine, fontRenderer);
        }

        renderGui(renderEngine, fontRenderer);
    }

    /**
     * @exclude
     */
    public void onUpdate() {
        if (baseGui != null){
            baseGui.doUpdating();
        }

        updateGui();
    }
}
