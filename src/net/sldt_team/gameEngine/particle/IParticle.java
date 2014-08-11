package net.sldt_team.gameEngine.particle;

import net.sldt_team.gameEngine.renderengine.FontRenderer;
import net.sldt_team.gameEngine.renderengine.RenderEngine;

public interface IParticle {
    public void renderParticle(RenderEngine renderEngine, FontRenderer fontRenderer);
    public void updateParticle();
}
