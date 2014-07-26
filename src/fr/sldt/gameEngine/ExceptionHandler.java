package fr.sldt.gameEngine;

import fr.sldt.gameEngine.exception.GameException;

public interface ExceptionHandler {
    public void handleException(GameException e);
}
