package fr.sldt.gameEngine.particle;

import fr.sldt.gameEngine.renderengine.FontRenderer;
import fr.sldt.gameEngine.renderengine.RenderEngine;

public interface IParticle {
    public void renderParticle(RenderEngine renderEngine, FontRenderer fontRenderer);
    public void updateParticle();
}
