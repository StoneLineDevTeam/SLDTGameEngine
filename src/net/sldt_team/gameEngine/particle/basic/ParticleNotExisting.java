package net.sldt_team.gameEngine.particle.basic;

import net.sldt_team.gameEngine.particle.Particle;
import net.sldt_team.gameEngine.renderengine.helper.ColorHelper;
import net.sldt_team.gameEngine.renderengine.FontRenderer;
import net.sldt_team.gameEngine.renderengine.RenderEngine;

/**
 * @exclude
 */
public class ParticleNotExisting extends Particle {

    public ParticleNotExisting(float x, float y, float width, float height, int time, Object obj) {
        super(x, y, width, height, time);
    }

    public void renderParticle(RenderEngine renderEngine, FontRenderer fontRenderer) {
        renderEngine.bindColor(ColorHelper.WHITE);
        renderEngine.renderQuad(particleX, particleY, particleWidth, particleHeight);
    }

    public void updateParticle() {
    }
}
