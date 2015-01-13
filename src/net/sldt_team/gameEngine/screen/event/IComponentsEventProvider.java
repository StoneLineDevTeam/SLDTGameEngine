package net.sldt_team.gameEngine.screen.event;

import net.sldt_team.gameEngine.screen.components.IScreenComponent;

public interface IComponentsEventProvider {

    /**
     * Component added
     */
    public void onComponentAdded(IScreenComponent component);

    /**
     * Component removed
     */
    public void onComponentRemoved();

    /**
     * Can component be added
     */
    public boolean canComponentAdd(IScreenComponent component);

    /**
     * Can component be removed
     */
    public boolean canComponentRemove(IScreenComponent component);
}
