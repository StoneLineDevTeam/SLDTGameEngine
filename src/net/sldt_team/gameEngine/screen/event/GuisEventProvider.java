package net.sldt_team.gameEngine.screen.event;

import net.sldt_team.gameEngine.gui.Gui;

/**
 * WARNING : This class needs your screen to be extending GuiScreen, otherwise nothing will be called.
 */
public interface GuisEventProvider {

    /**
     * Called when a Gui is displayed
     */
    public void onGuiDisplayed(Gui gui);

    /**
     * Called when the current displayed gui is cleared (removed)
     */
    public void onGuiCleared();

    /**
     * Can gui be displayed
     */
    public boolean canGuiDisplay(Gui gui);

    /**
     * Can gui be cleared (removed)
     */
    public boolean canGuiClear(Gui gui);
}
