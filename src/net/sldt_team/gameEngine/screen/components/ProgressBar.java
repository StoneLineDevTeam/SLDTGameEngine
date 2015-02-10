package net.sldt_team.gameEngine.screen.components;

import net.sldt_team.gameEngine.renderengine.FontRenderer;
import net.sldt_team.gameEngine.renderengine.RenderEngine;
import net.sldt_team.gameEngine.renderengine.Material;

public class ProgressBar implements IScreenComponent {

    private float compX;
    private float compY;
    private float compWidth;
    private float compHeight;
    private Material compMaterial;

    private float percents;


    public ProgressBar(float x, float y, float width, float height, RenderEngine renderEngine) {
        compX = x;
        compY = y;
        compWidth = width;
        compHeight = height;
        compMaterial = renderEngine.getMaterial("components/progressBar");
    }

    public void setProgressPercentage(float f) {
        percents = f;
    }

    public void renderComponent(RenderEngine renderEngine, FontRenderer fontRenderer) {
        float u1 = (percents * 256F) / 100F;
        float w = (percents * compWidth) / 100F;

        renderEngine.bindMaterial(compMaterial);
        renderEngine.renderTexturedQuadWithTextureCoords(compX, compY, compWidth, compHeight, 0, 32, 256, 32);
        renderEngine.renderTexturedQuadWithTextureCoords(compX, compY, w, compHeight, 0, 0, u1, 32);
    }

    public void updateComponent() {
    }

    public void onComponentRemove() {
    }

    public void onComponentAdd() {
    }

    public int getX() {
        return (int) compX;
    }

    public int getY() {
        return (int) compY;
    }
}
