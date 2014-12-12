package net.sldt_team.gameEngine.exception.code;

import net.sldt_team.gameEngine.exception.GameError;

/**
 * @exclude
 */
public class ErrorCode004 implements GameError {

    public String getDescription() {
        return "MD5_FILE_RETRIEVER_FAILURE";
    }

    public String getCode() {
        return "GameEngine-ERROR 004";
    }
}
