package net.sldt_team.gameEngine.input;

import net.sldt_team.gameEngine.input.keyboard.IKeyboardHandler;
import org.lwjgl.input.Keyboard;

public class KeyboardInput {

    private IKeyboardHandler theHandler;

    /**
     * Creates a keyboard input manager
     *
     * @param handler The handler to associate
     */
    public KeyboardInput(IKeyboardHandler handler) {
        theHandler = handler;
    }

    private char temporaryTypedChar;

    /**
     * You need to call this function yourself otherwise, the input handle will not work
     */
    public void updateInput() {
        while (Keyboard.next()) {
            if (Keyboard.getEventKey() != Keyboard.CHAR_NONE) {
                if (Keyboard.getEventKeyState()) {
                    temporaryTypedChar = Keyboard.getEventCharacter();
                    theHandler.keyPressed(Keyboard.getEventCharacter(), Keyboard.getEventKey());
                } else {
                    theHandler.keyTyped(temporaryTypedChar, Keyboard.getEventKey());
                }
            }
        }
    }

    /**
     * Returns associated Handler
     */
    public IKeyboardHandler getHandler() {
        return theHandler;
    }
}
