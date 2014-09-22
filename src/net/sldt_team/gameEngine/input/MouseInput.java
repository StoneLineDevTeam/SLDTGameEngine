package net.sldt_team.gameEngine.input;

import net.sldt_team.gameEngine.GameApplication;
import net.sldt_team.gameEngine.input.mouse.MouseHandler;
import net.sldt_team.gameEngine.input.mouse.MouseHelper;
import org.lwjgl.input.Mouse;

public class MouseInput {

    private MouseHandler theHandler;
    private boolean clickedLeft;
    private boolean clickedWheel;
    private boolean clickedRight;

    public MouseInput(MouseHandler handler){
        theHandler = handler;
    }

    /**
     * You need to call this function yourself otherwise, the input handle will not work
     */
    public void updateInput(){
        int x = Mouse.getX();
        int y = GameApplication.getScreenHeight() - Mouse.getY();

        theHandler.mouseMoved(x, y);

        if (Mouse.isButtonDown(0)){
            theHandler.mousePressed(x, y, MouseHelper.LEFT_CLICK);
            clickedLeft = true;
            return;
        }
        if (Mouse.isButtonDown(1)){
            theHandler.mousePressed(x, y, MouseHelper.RIGHT_CLICK);
            clickedRight = true;
            return;
        }
        if (Mouse.isButtonDown(2)){
            theHandler.mousePressed(x, y, MouseHelper.WHEEL_CLICK);
            clickedWheel = true;
            return;
        }

        if (clickedLeft){
            theHandler.mouseClicked(x, y, MouseHelper.LEFT_CLICK);
            clickedLeft = false;
        }
        if (clickedRight){
            theHandler.mouseClicked(x, y, MouseHelper.RIGHT_CLICK);
            clickedRight = false;
        }
        if (clickedWheel){
            theHandler.mouseClicked(x, y, MouseHelper.WHEEL_CLICK);
            clickedWheel = false;
        }

        int i = Mouse.getDWheel();
        if (i < 0) {
            theHandler.mouseWheelMoved(false, true, i);
        }
        if (i > 0) {
            theHandler.mouseWheelMoved(true, false, i);
        }
    }

    public MouseHandler getHandler(){
        return theHandler;
    }
}
