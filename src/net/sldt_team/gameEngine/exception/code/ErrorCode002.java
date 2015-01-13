package net.sldt_team.gameEngine.exception.code;

import net.sldt_team.gameEngine.exception.IGameError;

/**
 * @exclude
 */
public class ErrorCode002 implements IGameError {

    public String getDescription() {
        return "DATA_FILE_ENCODER_EXCEPTION";
    }

    public String getCode() {
        return "GameEngine-ERROR 002";
    }
}
