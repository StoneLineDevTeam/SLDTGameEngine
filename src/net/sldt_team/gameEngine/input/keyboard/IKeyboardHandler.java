package net.sldt_team.gameEngine.input.keyboard;

public interface IKeyboardHandler {

    /**
     * Called when a key has been typed
     */
    public void keyTyped(char c, int key);

    /**
     * Called in loop while user presses a key
     */
    public void keyPressed(char c, int key);
}
