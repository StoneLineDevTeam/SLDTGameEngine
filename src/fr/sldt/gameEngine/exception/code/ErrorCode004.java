package fr.sldt.gameEngine.exception.code;

import fr.sldt.gameEngine.exception.GameError;

public class ErrorCode004 implements GameError{

    public String getDescription() {
        return "MD5_FILE_RETRIEVER_FAILURE";
    }

    @Override
    public String getCode() {
        return "GameEngine-ERROR 004";
    }
}
