package fr.sldt.gameEngine.controls.handler;

import fr.sldt.gameEngine.input.keyboard.KeyboardHandler;
import org.lwjgl.input.Keyboard;

public class TextFieldHandler implements KeyboardHandler{

    private boolean isUpperCase;
    public String text = "";

    public void keyTyped(char c, int key) {
        if (key == Keyboard.KEY_BACK) {
            String txt = text;
            if (txt.length() > 0) {
                txt = txt.substring(0, txt.length() - 1);
                text = txt;
            }
        } else if (key == Keyboard.KEY_LSHIFT) {
            isUpperCase = false;
        } else {
            if (isUpperCase){
                text += Character.toUpperCase(c);
            } else {
                text += c;
            }
        }
    }

    public void keyPressed(char c, int key) {
        if (key == Keyboard.KEY_LSHIFT) {
            isUpperCase = true;
        }
    }
}
