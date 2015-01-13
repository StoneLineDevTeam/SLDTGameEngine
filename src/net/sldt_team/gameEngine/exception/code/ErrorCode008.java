package net.sldt_team.gameEngine.exception.code;

import net.sldt_team.gameEngine.exception.IGameError;

/**
 * @exclude
 */
public class ErrorCode008 implements IGameError {

    public String getDescription() {
        return "ASSET_FORMAT_ERROR";
    }

    public String getCode() {
        return "GameEngine-ERROR 008";
    }
}
