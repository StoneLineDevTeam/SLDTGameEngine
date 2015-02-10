package net.sldt_team.gameEngine;

import net.sldt_team.gameEngine.renderengine.helper.TextureFormatHelper;
import net.sldt_team.gameEngine.sound.helper.SoundFormatHelper;

public interface IRegistryModifier {

    /**
     * Registers all custom texture formats for the current GameApplication
     */
    public void registerCustomTextureDecoders(TextureFormatHelper helper);

    /**
     * Registers all custom sound formats for the current GameApplication
     */
    public void registerCustomSoundDecoders(SoundFormatHelper helper);
}
