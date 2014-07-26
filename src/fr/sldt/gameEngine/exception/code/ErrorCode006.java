package fr.sldt.gameEngine.exception.code;

import fr.sldt.gameEngine.exception.GameError;

public class ErrorCode006 implements GameError {

    public String getDescription() {
        return "WORKING_DIRECTORY_GENERATOR_FAILURE";
    }

    public String getCode() {
        return "GameEngine-ERROR 006";
    }
}
