package net.sldt_team.gameEngine;

import net.sldt_team.gameEngine.exception.GameException;

public interface IExceptionHandler {

    /**
     * Called when the game caught an exception (args: The exception that has been thrown)
     */
    public void handleException(GameException e);
}
