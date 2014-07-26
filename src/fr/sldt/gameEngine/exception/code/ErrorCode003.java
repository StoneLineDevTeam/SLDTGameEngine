package fr.sldt.gameEngine.exception.code;

import fr.sldt.gameEngine.exception.GameError;

public class ErrorCode003 implements GameError {

    public String getDescription() {
        return "RANDOM_INTEGER_GENERATOR_FAILURE";
    }

    public String getCode() {
        return "GameEngine-ERROR 003";
    }
}
