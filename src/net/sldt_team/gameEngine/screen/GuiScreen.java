package net.sldt_team.gameEngine.screen;

import net.sldt_team.gameEngine.gui.Gui;
import net.sldt_team.gameEngine.renderengine.FontRenderer;
import net.sldt_team.gameEngine.renderengine.RenderEngine;

public abstract class GuiScreen extends Screen {

    private Gui currentGui;

    public void displayGui(Gui par1Gui) {
        isButtonEnabled = false;
        par1Gui.setGame(theGame);
        par1Gui.onGuiInit();
        currentGui = par1Gui;
    }

    public void clearGui() {
        currentGui = null;
        isButtonEnabled = true;
    }

    public void onTick() {
        super.onTick();
        if (currentGui != null) {
            currentGui.onUpdate();
        }
    }

    public void drawWindow(RenderEngine renderEngine, FontRenderer fontRenderer) {
        super.drawWindow(renderEngine, fontRenderer);
        if (currentGui != null) {
            currentGui.onRender(renderEngine, fontRenderer);
        }
    }

    /**
    public void updateScreen() {
    }

    public void renderScreen(RenderEngine renderEngine, FontRenderer fontRenderer) {
    }

    public void initScreen() {
    }

    public void onExitingScreen() {
    }
     */
}
