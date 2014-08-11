package net.sldt_team.gameEngine.exception.code;

import net.sldt_team.gameEngine.exception.GameError;

public class ErrorCode004 implements GameError{

    public String getDescription() {
        return "MD5_FILE_RETRIEVER_FAILURE";
    }

    @Override
    public String getCode() {
        return "GameEngine-ERROR 004";
    }
}
