package net.sldt_team.gameEngine.exception.code;

import net.sldt_team.gameEngine.exception.IGameError;

/**
 * @exclude
 */
public class ErrorCode003 implements IGameError {

    public String getDescription() {
        return "RANDOM_INTEGER_GENERATOR_FAILURE";
    }

    public String getCode() {
        return "GameEngine-ERROR 003";
    }
}
