package net.sldt_team.gameEngine.screen.event;

import net.sldt_team.gameEngine.components.ScreenComponent;

public interface ComponentsEventProvider {

    /**
     * Component added
     */
    public void onComponentAdded(ScreenComponent component);

    /**
     * Component removed
     */
    public void onComponentRemoved();

    /**
     * Can component be added
     */
    public boolean canComponentAdd(ScreenComponent component);

    /**
     * Can component be removed
     */
    public boolean canComponentRemove(ScreenComponent component);
}
