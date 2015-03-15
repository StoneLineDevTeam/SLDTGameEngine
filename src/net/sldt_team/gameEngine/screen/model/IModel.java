package net.sldt_team.gameEngine.screen.model;

import net.sldt_team.gameEngine.renderengine.FontRenderer;
import net.sldt_team.gameEngine.renderengine.RenderEngine;
import net.sldt_team.gameEngine.screen.SceneModel;

import java.util.List;

public interface IModel {
    /**
     * Called to render the model
     * NOTE : The model is already translated to SceneModel's X, Y
     */
    public void renderModel(float x, float y, RenderEngine renderEngine, FontRenderer fontRenderer);

    /**
     * Returns a quad to design the collision shape
     */
    public Quad getCollisionModel();

    /**
     * Can this model collide with the given model
     */
    public boolean shouldCollide(SceneModel otherModel);

    /**
     * Should the collision algorithm run for this model or not
     */
    public boolean hasCollisions();
}
