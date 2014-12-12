package net.sldt_team.gameEngine.gui.vgui;

import net.sldt_team.gameEngine.GameApplication;
import net.sldt_team.gameEngine.renderengine.helper.ColorHelper;
import net.sldt_team.gameEngine.renderengine.FontRenderer;
import net.sldt_team.gameEngine.renderengine.RenderEngine;

public class VGuiFrame extends VGuiBase {

    private boolean draggable;

    public VGuiFrame() {
        super(GameApplication.getScreenWidth() / 2 - 128, GameApplication.getScreenHeight() / 2 - 128, 256, 256);

        setDraggable(true);
    }

    public boolean canHaveChilds() {
        return true;
    }

    public void setDraggable(boolean b) {
        draggable = b;
    }

    public boolean isDraggable() {
        return draggable;
    }

    public int[] getDraggingRect() {
        return new int[]{0, 0, vguiW, 24};
    }

    protected void onDragged(int newX, int newY) {
    }

    protected void render(RenderEngine renderEngine, FontRenderer fontRenderer) {
        renderEngine.bindColor(new ColorHelper(133, 133, 133, 200));
        renderEngine.renderQuad(vguiX, vguiY, vguiX + vguiW, vguiH + 24);

        renderEngine.bindColor(new ColorHelper(133, 133, 133, 100));
        renderEngine.renderQuad(vguiX, vguiY + 24, vguiW, vguiH - 48);
    }

    protected void update() {
    }

    protected void onRemoved() {
    }

    protected void onAdded() {
    }

    protected boolean shouldBeRendered() {
        return true;
    }

    protected boolean shouldBeUpdated() {
        return false;
    }
}
