package net.sldt_team.gameEngine.particle.basic;

import net.sldt_team.gameEngine.particle.Particle;
import net.sldt_team.gameEngine.renderengine.helper.ColorHelper;
import net.sldt_team.gameEngine.renderengine.FontRenderer;
import net.sldt_team.gameEngine.renderengine.RenderEngine;

/**
 * @exclude
 */
public class ParticleButtonClick extends Particle {

    public int colorAlteratorMultiplier;

    public ParticleButtonClick(float x, float y, float size, float size1, int lifeTime, Object obj) {
        super(x, y, 16, 16, 1000);
    }

    public void renderParticle(RenderEngine renderEngine, FontRenderer fontRenderer) {
        renderEngine.bindColor(new ColorHelper(200, 200, colorAlteratorMultiplier, 255));
        renderEngine.renderQuad(particleX, particleY, particleWidth, particleHeight);
    }

    public void updateParticle() {
        if (colorAlteratorMultiplier <= 255) {
            colorAlteratorMultiplier++;
        } else if (colorAlteratorMultiplier >= 255) {
            colorAlteratorMultiplier--;
        }
    }
}
