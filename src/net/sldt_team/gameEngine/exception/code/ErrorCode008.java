package net.sldt_team.gameEngine.exception.code;

import net.sldt_team.gameEngine.exception.GameError;

/**
 * @exclude
 */
public class ErrorCode008 implements GameError {

    public String getDescription() {
        return "ASSET_FORMAT_ERROR";
    }

    public String getCode() {
        return "GameEngine-ERROR 008";
    }
}
