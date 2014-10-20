package net.sldt_team.gameEngine.screen.event;

import net.sldt_team.gameEngine.controls.ScreenComponent;

public interface ComponentsEventProvider {

    /**
     * Components events
     * 1 - Component added
     * 2 - Component removed
     */
    public void onComponentAdded(ScreenComponent component);
    public void onComponentRemoved();

    /**
     * Components events
     * 1 - Can component added
     * 2 - Can component removed
     */
    public boolean canComponentAdd(ScreenComponent component);
    public boolean canComponentRemove(ScreenComponent component);
}
