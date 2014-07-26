package fr.sldt.gameEngine.exception.code;

import fr.sldt.gameEngine.exception.GameError;

public class ErrorCode008 implements GameError{
    public String getDescription() {
        return "ASSET_FORMAT_ERROR";
    }

    public String getCode() {
        return "GameEngine-ERROR 008";
    }
}
