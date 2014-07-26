package fr.sldt.gameEngine.exception.code;

import fr.sldt.gameEngine.exception.GameError;

public class ErrorCode002 implements GameError{

    public String getDescription() {
        return "DATA_FILE_ENCODER_EXCEPTION";
    }

    public String getCode() {
        return "GameEngine-ERROR 002";
    }
}
