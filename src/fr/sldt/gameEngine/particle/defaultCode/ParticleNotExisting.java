package fr.sldt.gameEngine.particle.defaultCode;

import fr.sldt.gameEngine.particle.Particle;
import fr.sldt.gameEngine.renderengine.ColorRenderer;
import fr.sldt.gameEngine.renderengine.FontRenderer;
import fr.sldt.gameEngine.renderengine.RenderEngine;

public class ParticleNotExisting extends Particle {

    public ParticleNotExisting(float x, float y, float width, float height, int time, Object obj) {
        super(x, y, width, height, time);
    }

    public void renderParticle(RenderEngine renderEngine, FontRenderer fontRenderer) {
        renderEngine.bindColor(ColorRenderer.WHITE);
        renderEngine.renderQuad(particleX, particleY, particleWidth, particleHeight);
    }

    public void updateParticle() {
    }
}
