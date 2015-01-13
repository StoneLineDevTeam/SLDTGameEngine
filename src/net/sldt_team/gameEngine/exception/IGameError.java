package net.sldt_team.gameEngine.exception;

public interface IGameError {

    /**
     * Returns the description of this error code
     */
    public String getDescription();

    /**
     * Returns the error code
     */
    public String getCode();
}
