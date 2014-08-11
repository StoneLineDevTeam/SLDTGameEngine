package net.sldt_team.gameEngine.exception.code;

import net.sldt_team.gameEngine.exception.GameError;

public class ErrorCode009 implements GameError {
    public String getDescription() {
        return "JVM_FATAL_ERROR";
    }

    public String getCode() {
        return "ERROR 009";
    }
}
