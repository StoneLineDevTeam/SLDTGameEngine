package net.sldt_team.gameEngine.particle;

import net.sldt_team.gameEngine.renderengine.FontRenderer;
import net.sldt_team.gameEngine.renderengine.RenderEngine;

public abstract class Particle implements IParticle{

    public float particleX = 0.0F;
    public float particleY = 0.0F;
    public float particleWidth = 0.0F;
    public float particleHeight = 0.0F;
    public int lifeTime = 0;

    public Particle(float x, float y, float width, float height, int time) {
        particleX = x;
        particleY = y;
        particleWidth = width;
        particleHeight = height;
        lifeTime = time;
    }

    protected void doUpdate(){
        updateParticle();
    }

    protected void doRender(RenderEngine renderEngine, FontRenderer fontRenderer){
        renderParticle(renderEngine, fontRenderer);
    }

    /**
     * Renders the current particle
     */
    public abstract void renderParticle(RenderEngine renderEngine, FontRenderer fontRenderer);

    /**
     * Updates the current particle
     */
    public abstract void updateParticle();
}
