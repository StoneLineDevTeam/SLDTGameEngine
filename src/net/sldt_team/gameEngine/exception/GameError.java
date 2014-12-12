package net.sldt_team.gameEngine.exception;

public interface GameError {

    /**
     * Returns the description of this error code
     */
    public String getDescription();

    /**
     * Returns the error code
     */
    public String getCode();
}
