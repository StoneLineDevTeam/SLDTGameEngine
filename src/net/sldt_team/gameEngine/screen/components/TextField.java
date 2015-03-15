package net.sldt_team.gameEngine.screen.components;

import net.sldt_team.gameEngine.screen.components.handler.TextFieldFocusHandler;
import net.sldt_team.gameEngine.screen.components.handler.TextFieldHandler;
import net.sldt_team.gameEngine.input.KeyboardInput;
import net.sldt_team.gameEngine.input.MouseInput;
import net.sldt_team.gameEngine.renderengine.helper.ColorHelper;
import net.sldt_team.gameEngine.renderengine.FontRenderer;
import net.sldt_team.gameEngine.renderengine.RenderEngine;
import org.lwjgl.input.Keyboard;

public class TextField implements IScreenComponent {

    protected int areaX;
    protected int areaY;
    protected int areaWidth;
    protected int areaHeight;
    protected boolean hasFocus = false;
    protected String text = "";

    private int tick;
    private boolean drawCursor;

    private KeyboardInput input;
    private MouseInput input2;

    /**
     * Returns the text in the typing area
     */
    public String getText() {
        return text;
    }

    /**
     * Set the text in the typing area
     */
    public void setText(final String text) {
        this.text = text;
    }

    public TextField(int x, int y, int width, int height) {
        super();
        areaX = x;
        areaY = y;
        areaWidth = width;
        areaHeight = height;

        input = new KeyboardInput(new TextFieldHandler(this));
        input2 = new MouseInput(new TextFieldFocusHandler(width, height, this));
    }

    /**
     * Called when this component is added to a screen
     */
    public void onComponentAdd() {
        Keyboard.enableRepeatEvents(true);
    }

    public int getX() {
        return areaX;
    }

    public int getY() {
        return areaY;
    }

    /**
     * Called when the component is removed from a screen
     */
    public void onComponentRemove() {
        Keyboard.enableRepeatEvents(false);
    }

    /**
     * Render Current Component
     */
    public void renderComponent(RenderEngine renderEngine, FontRenderer fontRenderer) {
        if (hasFocus) {
            renderEngine.bindColor(new ColorHelper(255, 255, 0));
        } else {
            renderEngine.bindColor(ColorHelper.WHITE);
        }
        renderEngine.renderQuad(areaX, areaY, areaWidth, areaHeight);
        fontRenderer.setRenderingColor(ColorHelper.BLACK);
        fontRenderer.renderString(text, areaX + 10, areaY + 10);
        if (drawCursor && hasFocus) {
            renderEngine.bindColor(ColorHelper.BLACK);
            renderEngine.renderQuad(areaX + fontRenderer.getStringWidth(text), areaY + 10, 5F, areaHeight - 20);
        }
    }

    /**
     * Returns true when mouse cursor is in this component, otherwise it returns false
     */
    public boolean isMouseInside() {
        TextFieldFocusHandler handler = (TextFieldFocusHandler) input2.getHandler();
        return handler.isMouseOver;
    }

    /**
     * Update the current Component
     */
    public void updateComponent() {
        input.updateInput();
        input2.updateInput();

        TextFieldHandler handler = (TextFieldHandler) input.getHandler();
        text = handler.text;

        tick++;
        if (tick >= 20) {
            drawCursor = !drawCursor;
            tick = 0;
        }

        hasFocus = isMouseInside();
    }
}
