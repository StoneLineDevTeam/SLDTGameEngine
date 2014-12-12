package net.sldt_team.gameEngine.screen;

import net.sldt_team.gameEngine.gui.Gui;
import net.sldt_team.gameEngine.renderengine.FontRenderer;
import net.sldt_team.gameEngine.renderengine.RenderEngine;
import net.sldt_team.gameEngine.screen.event.GuisEventProvider;

public abstract class GuiScreen extends Screen {

    private Gui currentGui;

    /**
     * Displays a new Gui
     */
    public void displayGui(Gui par1Gui) {
        if (this instanceof GuisEventProvider) {
            GuisEventProvider provider = (GuisEventProvider) this;
            if (!provider.canGuiDisplay(par1Gui)) {
                return;
            }
        }

        areControlsEnabled = false;
        par1Gui.setGame(theGame);
        par1Gui.onGuiInit();
        currentGui = par1Gui;

        if (this instanceof GuisEventProvider) {
            GuisEventProvider provider = (GuisEventProvider) this;
            provider.onGuiDisplayed(par1Gui);
        }
    }

    /**
     * Clears the current displayed Gui
     */
    public void clearGui() {
        if (this instanceof GuisEventProvider) {
            GuisEventProvider provider = (GuisEventProvider) this;
            if (!provider.canGuiClear(currentGui)) {
                return;
            }
        }

        currentGui = null;
        areControlsEnabled = true;

        if (this instanceof GuisEventProvider) {
            GuisEventProvider provider = (GuisEventProvider) this;
            provider.onGuiCleared();
        }
    }

    /**
     * @exclude
     */
    public void onTick() {
        if (currentGui != null) {
            currentGui.onUpdate();
        }
        super.onTick();
    }

    /**
     * @exclude
     */
    public void drawWindow(RenderEngine renderEngine, FontRenderer fontRenderer) {
        super.drawWindow(renderEngine, fontRenderer);
        if (currentGui != null) {
            currentGui.onRender(renderEngine, fontRenderer);
        }
    }
}
