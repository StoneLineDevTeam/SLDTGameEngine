package net.sldt_team.gameEngine.exception.code;

import net.sldt_team.gameEngine.exception.GameError;

public class ErrorCode007 implements GameError{
    public String getDescription() {
        return "PARTICLE_GENERATOR_FAILURE";
    }

    public String getCode() {
        return "GameEngine-ERROR 007";
    }
}
