package net.sldt_team.gameEngine.input.mouse;

public interface MouseHandler {
    /**
     * Called when the user has pressed a mouse button (args : x - cursor x, y - cursor y, helper - the pressed button)
     */
    public void mousePressed(int x, int y, MouseHelper helper);

    /**
     * Called when the user has clicked a mouse button (args : x - cursor x, y - cursor y, helper - the pressed button)
     */
    public void mouseClicked(int x, int y, MouseHelper helper);


    /**
     * Called when mouse is being moved arround the screen (args : x - cursor x, y - cursor y)
     */
    public void mouseMoved(int x, int y);

    /**
     * Called when the mouse wheel is moved (args : up - if wheel's moved up, down - if wheel's moved down, amount - the moving amount)
     */
    public void mouseWheelMoved(boolean up, boolean down, int amount);
}
