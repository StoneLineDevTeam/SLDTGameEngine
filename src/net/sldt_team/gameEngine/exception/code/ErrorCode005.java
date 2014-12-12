package net.sldt_team.gameEngine.exception.code;

import net.sldt_team.gameEngine.exception.GameError;

/**
 * @exclude
 */
public class ErrorCode005 implements GameError {

    public String getDescription() {
        return "SLDT_COPYRIGHT_BROKEN";
    }

    public String getCode() {
        return "GameEngine-ERROR 005";
    }
}
