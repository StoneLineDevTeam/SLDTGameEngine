package net.sldt_team.gameEngine.exception.code;

import net.sldt_team.gameEngine.exception.IGameError;

/**
 * @exclude
 */
public class ErrorCode005 implements IGameError {

    public String getDescription() {
        return "SLDT_COPYRIGHT_BROKEN";
    }

    public String getCode() {
        return "GameEngine-ERROR 005";
    }
}
