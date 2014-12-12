package net.sldt_team.gameEngine.particle;

import net.sldt_team.gameEngine.renderengine.FontRenderer;
import net.sldt_team.gameEngine.renderengine.RenderEngine;

public abstract class Particle {

    /**
     * The current particle X (Random generated)
     */
    protected float particleX = 0.0F;

    /**
     * The current particle Y (Random generated)
     */
    protected float particleY = 0.0F;

    /**
     * The current particle Width (Random generated)
     */
    protected float particleWidth = 0.0F;

    /**
     * The current particle Height (Random generated)
     */
    protected float particleHeight = 0.0F;

    /**
     * The current particle life-time in ticks (Random generated)
     */
    protected int lifeTime = 0;

    /**
     * @exclude
     */
    protected Particle(float x, float y, float width, float height, int time) {
        particleX = x;
        particleY = y;
        particleWidth = width;
        particleHeight = height;
        lifeTime = time;
    }

    /**
     * @exclude
     */
    protected void doUpdate() {
        updateParticle();
    }

    /**
     * @exclude
     */
    protected void doRender(RenderEngine renderEngine, FontRenderer fontRenderer) {
        renderParticle(renderEngine, fontRenderer);
    }

    /**
     * Renders the current particle
     */
    protected abstract void renderParticle(RenderEngine renderEngine, FontRenderer fontRenderer);

    /**
     * Updates the current particle
     */
    protected abstract void updateParticle();
}
