package fr.sldt.gameEngine.exception.code;

import fr.sldt.gameEngine.exception.GameError;

public class ErrorCode009 implements GameError {
    public String getDescription() {
        return "JVM_FATAL_ERROR";
    }

    public String getCode() {
        return "ERROR 009";
    }
}
