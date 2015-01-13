package net.sldt_team.gameEngine.util;

import net.sldt_team.gameEngine.GameApplication;
import org.lwjgl.input.Mouse;

public class GuiUtilities {

    /**
     * Returns a float corresponding of screen's center X supporting an object width
     */
    public static float getCenteredObjectX(float objectWidth) {
        return (GameApplication.getScreenWidth() / 2) - (objectWidth / 2);
    }

    /**
     * Returns a float corresponding of screen's center Y supporting an object height
     */
    public static float getCenteredObjectY(float objectHeight) {
        return (GameApplication.getScreenHeight() / 2) - (objectHeight / 2);
    }

    /**
     * Returns an int array corresponding to X and Y of your cursor
     */
    public static int[] getMousePosition(){
        int x = Mouse.getX();
        int y = GameApplication.getScreenHeight() - Mouse.getY();
        return new int[]{x, y};
    }

    /**
     * Used to overwrite the default cursor ; when you press a button on your mouse, this will return true.
     */
    public static boolean mousePressed(){
        int numBut = Mouse.getButtonCount();
        for (int i = 0 ; i < numBut ; i++){
            if (Mouse.isButtonDown(i)){
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true when your mouse cursor is in a defined rect
     */
    public static boolean isMouseInRect(int x, int y, int width, int height){
        if (Mouse.getX() >= x && Mouse.getX() <= (x + width)){
            if ((GameApplication.getScreenHeight() - Mouse.getY()) >= y && (GameApplication.getScreenHeight() - Mouse.getY()) <= (y + height)){
                return true;
            }
        }
        return false;
    }
}
