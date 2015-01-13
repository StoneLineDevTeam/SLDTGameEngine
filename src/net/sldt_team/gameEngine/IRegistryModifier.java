package net.sldt_team.gameEngine;

import net.sldt_team.gameEngine.renderengine.helper.TextureFormatHelper;
import net.sldt_team.gameEngine.sound.SoundEngine;

public interface IRegistryModifier {

    /**
     * Registers all custom texture formats for the current GameApplication
     */
    public void registerCustomDecoders(TextureFormatHelper helper);

    public void registerCustomSounds(SoundEngine sndEngine);
}
