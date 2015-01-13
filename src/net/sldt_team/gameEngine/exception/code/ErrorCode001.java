package net.sldt_team.gameEngine.exception.code;

import net.sldt_team.gameEngine.exception.IGameError;

/**
 * @exclude
 */
public class ErrorCode001 implements IGameError {

    public String getDescription() {
        return "GAME_DATA_FILE_EXCEPTION";
    }

    public String getCode() {
        return "GameEngine-ERROR 001";
    }
}
