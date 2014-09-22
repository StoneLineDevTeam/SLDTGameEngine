package net.sldt_team.gameEngine.screen.event;

import net.sldt_team.gameEngine.gui.Gui;

/**
 * WARNING : This class needs your screen to be extending GuiScreen, otherwise nothing will be called.
 */
public interface GuisEventProvider {

    /**
     * Guis events (called when gui displayed and cleared)
     * 1 - Gui displayed
     * 2 - Gui cleared
     */
    public void onGuiDisplayed(Gui gui);
    public void onGuiCleared();

    /**
     * Guis events (called when can gui displayed and can gui cleared)
     * 1 - Can gui displayed
     * 2 - Can gui cleared
     */
    public boolean canGuiDisplay(Gui gui);
    public boolean canGuiClear(Gui gui);
}
