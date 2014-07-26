package fr.sldt.gameEngine.exception.code;

import fr.sldt.gameEngine.exception.GameError;

public class ErrorCode005 implements GameError{

    public String getDescription() {
        return "SLDT_COPYRIGHT_BROKEN";
    }

    public String getCode() {
        return "GameEngine-ERROR 005";
    }
}
