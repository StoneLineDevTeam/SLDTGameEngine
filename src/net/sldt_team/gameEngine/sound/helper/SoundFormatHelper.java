package net.sldt_team.gameEngine.sound.helper;

import net.sldt_team.gameEngine.sound.decoders.ISoundDecoder;
import net.sldt_team.gameEngine.util.FileUtilities;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SoundFormatHelper {

    private Map<String, ISoundDecoder> readableSoundFormats = new HashMap<String, ISoundDecoder>();

    public static SoundFormatHelper instance = new SoundFormatHelper();

    public boolean canSoundFileBeRead(File f){
        return readableSoundFormats.containsKey(FileUtilities.getFileExtension(f).toUpperCase());
    }

    public void registerNewSoundFormat(String fileExtension, ISoundDecoder newFormat){
        if (!readableSoundFormats.containsKey(fileExtension.toUpperCase()) && !readableSoundFormats.containsValue(newFormat)) {
            readableSoundFormats.put(fileExtension.toUpperCase(), newFormat);
        }
    }

    public ISoundDecoder getSoundDecoderForFormat(String format){
        return readableSoundFormats.get(format.toUpperCase());
    }
}
