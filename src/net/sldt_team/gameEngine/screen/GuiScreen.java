package net.sldt_team.gameEngine.screen;

import net.sldt_team.gameEngine.gui.Gui;
import net.sldt_team.gameEngine.renderengine.FontRenderer;
import net.sldt_team.gameEngine.renderengine.RenderEngine;
import net.sldt_team.gameEngine.screen.event.GuisEventProvider;

public abstract class GuiScreen extends Screen {

    private Gui currentGui;

    public void displayGui(Gui par1Gui) {
        if (this instanceof GuisEventProvider){
            GuisEventProvider provider = (GuisEventProvider)this;
            if (!provider.canGuiDisplay(par1Gui)) {
                return;
            }
        }

        areControlsEnabled = false;
        par1Gui.setGame(theGame);
        par1Gui.onGuiInit();
        currentGui = par1Gui;

        if (this instanceof GuisEventProvider){
            GuisEventProvider provider = (GuisEventProvider)this;
            provider.onGuiDisplayed(par1Gui);
        }
    }

    public void clearGui() {
        if (this instanceof GuisEventProvider){
            GuisEventProvider provider = (GuisEventProvider)this;
            if (!provider.canGuiClear(currentGui)) {
                return;
            }
        }

        currentGui = null;
        areControlsEnabled = true;

        if (this instanceof GuisEventProvider){
            GuisEventProvider provider = (GuisEventProvider)this;
            provider.onGuiCleared();
        }
    }

    public void onTick() {
        if (currentGui != null) {
            currentGui.onUpdate();
        }
        super.onTick();
    }

    public void drawWindow(RenderEngine renderEngine, FontRenderer fontRenderer) {
        super.drawWindow(renderEngine, fontRenderer);
        if (currentGui != null) {
            currentGui.onRender(renderEngine, fontRenderer);
        }
    }
}
