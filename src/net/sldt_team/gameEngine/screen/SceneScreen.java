package net.sldt_team.gameEngine.screen;

import net.sldt_team.gameEngine.GameApplication;
import net.sldt_team.gameEngine.network.client.NetworkManagerClient;
import net.sldt_team.gameEngine.particle.ParticleEngine;
import net.sldt_team.gameEngine.renderengine.FontRenderer;
import net.sldt_team.gameEngine.renderengine.Material;
import net.sldt_team.gameEngine.renderengine.RenderEngine;
import net.sldt_team.gameEngine.renderengine.animation.Animation;
import net.sldt_team.gameEngine.util.MathUtilities;
import net.sldt_team.gameEngine.util.misc.EnumCollisionSide;
import net.sldt_team.gameEngine.screen.model.Quad;
import net.sldt_team.gameEngine.sound.SoundEngine;
import net.sldt_team.gameEngine.util.Vector2D;

public abstract class SceneScreen extends GuiScreen{

    private SceneModel[] modelList;
    private Material backgroundMat;
    private Animation backgroundAnim;

    /**
     * The GameApplication's SoundEngine instance
     */
    protected SoundEngine soundEngine;

    /**
     * The GameApplication's NetworkManagerClient instance for multi-player games
     */
    protected NetworkManagerClient clientManager;

    /**
     * The GameApplication's ParticleEngine instance
     */
    protected ParticleEngine particleEngine;

    /**
     * Creates a Scene for a 2D Game
     * @param maxModels The maximum number of models that can be spawned at a time ; set to -1 if you've no models to add
     */
    public SceneScreen(int maxModels){
        if (maxModels == -1){
            modelList = null;
        } else {
            modelList = new SceneModel[maxModels];
        }
    }

    /**
     * Adds a model to this screen, and returns the index of it, returns -1 if fails
     */
    public int addModel(SceneModel mdl){
        for (int i = 0 ; i < modelList.length ; i++){
            if (modelList[i] == null){
                modelList[i] = mdl;
                mdl.modelSceneID = i;
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns the model with the given index
     */
    public SceneModel getModel(int index){
        if (index < 0 || index >= modelList.length){
            return null;
        }
        return modelList[index];
    }

    /**
     * Removes a model with the given index
     */
    public void removeModel(int index){
        if (index < 0 || index >= modelList.length){
            return;
        }
        modelList[index] = null;
    }

    /**
     *  Sets the background as a static material
     */
    public void setBackgroundMaterial(String matName){
        backgroundAnim = null;
        backgroundMat = theGame.renderEngine.getMaterial("backgrounds/" + matName);
    }

    /**
     *  Sets the background as an animation
     */
    public void setBackgroundAnimation(String animName){
        backgroundMat = null;
        backgroundAnim = theGame.renderEngine.getAnimation("backgrounds/" + animName);
    }

    /**
     * @exclude
     */
    public void setGame(GameApplication game) {
        super.setGame(game);
        soundEngine = game.soundEngine;
        particleEngine = game.particleEngine;
        clientManager = game.clientManager;
    }

    /**
     * @exclude
     */
    public void onTick() {
        if (backgroundAnim != null && backgroundMat == null){
            backgroundAnim.update();
        }
        super.onTick();
        if (modelList == null) { return; }
        for (SceneModel mdl : modelList) {
            if (mdl == null) { continue; }
            if (mdl.getPosition().getY() < -128 || mdl.getPosition().getY() > GameApplication.getScreenHeight() + 128 || mdl.getPosition().getX() < -128 || mdl.getPosition().getX() > GameApplication.getScreenWidth() + 128) {
                removeModel(mdl.modelSceneID);
                continue;
            }
            if (mdl.theModel.hasCollisions()){
                for (SceneModel mdl1 : modelList){
                    if (mdl1 == null) { continue; }
                    if (mdl1 != mdl && mdl1.theModel.hasCollisions()){
                        if (!mdl1.theModel.shouldCollide(mdl)){ continue; }
                        Quad quad = mdl.theModel.getCollisionModel();
                        Quad quad1 = mdl1.theModel.getCollisionModel();
                        EnumCollisionSide side = MathUtilities.testCollisionBetweenQuads(mdl.getPosition(), mdl1.getPosition(), quad, quad1);
                        if (side != null){
                            onModelCollision(mdl, mdl1, side);
                        }
                    }
                }
            }
        }
    }

    /**
     * @exclude
     */
    public void drawWindow(RenderEngine renderEngine, FontRenderer fontRenderer) {
        if (backgroundAnim != null && backgroundMat == null){
            backgroundAnim.render(renderEngine, 0, 0, GameApplication.getScreenWidth(), GameApplication.getScreenHeight());
        } else if (backgroundAnim == null && backgroundMat != null) {
            renderEngine.bindMaterial(backgroundMat);
            renderEngine.renderQuad(0, 0, GameApplication.getScreenWidth(), GameApplication.getScreenHeight());
        }
        super.drawWindow(renderEngine, fontRenderer);
        if (modelList == null) { return; }
        for (SceneModel mdl : modelList){
            if (mdl == null) { continue; }
            Vector2D pos = mdl.getPosition();
            renderEngine.addTranslationMatrix((float)pos.getX(), (float)pos.getY());
            mdl.theModel.renderModel((float)pos.getX(), (float)pos.getY(), renderEngine, fontRenderer);
            renderEngine.removeTranslationMatrix();
        }
    }

    /**
     * @exclude
     */
    public void onExit() {
        super.onExit();
        soundEngine = null;
        particleEngine = null;
        clientManager = null;
        if (modelList == null) { return; }
        for (int i = 0 ; i < modelList.length ; i++){
            modelList[i] = null;
        }
        modelList = null;
    }

    /**
     * Called when two models collides
     */
    protected abstract void onModelCollision(SceneModel firstModel, SceneModel secondModel, EnumCollisionSide collisionSide);
}
