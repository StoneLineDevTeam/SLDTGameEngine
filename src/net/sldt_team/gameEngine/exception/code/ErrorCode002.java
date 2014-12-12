package net.sldt_team.gameEngine.exception.code;

import net.sldt_team.gameEngine.exception.GameError;

/**
 * @exclude
 */
public class ErrorCode002 implements GameError {

    public String getDescription() {
        return "DATA_FILE_ENCODER_EXCEPTION";
    }

    public String getCode() {
        return "GameEngine-ERROR 002";
    }
}
