package net.sldt_team.gameEngine.exception.code;

import net.sldt_team.gameEngine.exception.GameError;

/**
 * @exclude
 */
public class ErrorCode001 implements GameError {

    public String getDescription() {
        return "GAME_DATA_FILE_EXCEPTION";
    }

    public String getCode() {
        return "GameEngine-ERROR 001";
    }
}
