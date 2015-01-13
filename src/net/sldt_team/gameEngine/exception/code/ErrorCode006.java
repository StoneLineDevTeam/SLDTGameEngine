package net.sldt_team.gameEngine.exception.code;

import net.sldt_team.gameEngine.exception.IGameError;

/**
 * @exclude
 */
public class ErrorCode006 implements IGameError {

    public String getDescription() {
        return "WORKING_DIRECTORY_GENERATOR_FAILURE";
    }

    public String getCode() {
        return "GameEngine-ERROR 006";
    }
}
