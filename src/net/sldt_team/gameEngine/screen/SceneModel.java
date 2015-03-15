package net.sldt_team.gameEngine.screen;

import net.sldt_team.gameEngine.screen.model.IModel;
import net.sldt_team.gameEngine.util.Vector2D;

public class SceneModel {

    /**
     * @exclude
     */
    protected final IModel theModel;

    private Vector2D position;

    protected int modelSceneID;

    /**
     * Creates a new model to work with the SceneScreen
     * @param mdl The model to render
     * @param initPos The initial position
     */
    public SceneModel(IModel mdl, Vector2D initPos){
        theModel = mdl;
        position = initPos;
    }

    /**
     * Sets the position of this model
     */
    public void setPosition(Vector2D newPos){
        position = newPos;
    }

    /**
     * Returns the position of this model
     */
    public Vector2D getPosition(){
        return position;
    }

    /**
     * Returns the model shape for this model
     */
    public IModel getModel(){
        return theModel;
    }

    /**
     * Returns the ID of this model in the Scene
     */
    public int getModelSceneID(){
        return modelSceneID;
    }
}
