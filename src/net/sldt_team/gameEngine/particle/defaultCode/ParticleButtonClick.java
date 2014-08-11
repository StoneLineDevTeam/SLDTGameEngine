package net.sldt_team.gameEngine.particle.defaultCode;

import net.sldt_team.gameEngine.particle.Particle;
import net.sldt_team.gameEngine.renderengine.ColorRenderer;
import net.sldt_team.gameEngine.renderengine.FontRenderer;
import net.sldt_team.gameEngine.renderengine.RenderEngine;

public class ParticleButtonClick extends Particle {

    public int colorAlteratorMultiplier;

    public ParticleButtonClick(float x, float y, float size, float size1, int lifeTime, Object obj) {
        super(x, y, 16, 16, 1000);
    }

    public void renderParticle(RenderEngine renderEngine, FontRenderer fontRenderer) {
        renderEngine.bindColor(new ColorRenderer(200,  200, colorAlteratorMultiplier, 255));
        renderEngine.renderQuad(particleX, particleY, particleWidth, particleHeight);
    }

    public void updateParticle() {
        if (colorAlteratorMultiplier <= 255){
            colorAlteratorMultiplier++;
        } else if (colorAlteratorMultiplier >= 255){
            colorAlteratorMultiplier--;
        }
    }
}
