package fr.sldt.gameEngine.exception.code;

import fr.sldt.gameEngine.exception.GameError;

public class ErrorCode001 implements GameError{

    public String getDescription() {
        return "GAME_DATA_FILE_EXCEPTION";
    }

    public String getCode() {
        return "GameEngine-ERROR 001";
    }
}
