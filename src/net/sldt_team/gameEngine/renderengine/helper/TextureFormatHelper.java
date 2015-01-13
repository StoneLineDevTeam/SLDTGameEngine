package net.sldt_team.gameEngine.renderengine.helper;

import net.sldt_team.gameEngine.renderengine.decoders.ITextureDecoder;
import net.sldt_team.gameEngine.util.FileUtilities;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class TextureFormatHelper {

    private Map<String, ITextureDecoder> readableTextureFormats = new HashMap<String, ITextureDecoder>();

    public static TextureFormatHelper instance = new TextureFormatHelper();

    public boolean canTextureFileBeRead(File f){
        return readableTextureFormats.containsKey(FileUtilities.getFileExtension(f).toUpperCase());
    }

    public void registerNewTextureFormat(String fileExtension, ITextureDecoder newFormat){
        if (!readableTextureFormats.containsKey(fileExtension.toUpperCase()) && !readableTextureFormats.containsValue(newFormat)) {
            readableTextureFormats.put(fileExtension.toUpperCase(), newFormat);
        }
    }

    public ITextureDecoder getTextureDecoderForFormat(String format){
        return readableTextureFormats.get(format.toUpperCase());
    }
}
