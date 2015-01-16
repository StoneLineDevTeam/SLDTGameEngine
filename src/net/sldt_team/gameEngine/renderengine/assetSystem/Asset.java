package net.sldt_team.gameEngine.renderengine.assetSystem;

public class Asset {

    /**
     * Is this asset a texture
     */
    public final boolean isTexture;

    /**
     * The data of this asset
     */
    public final byte[] assetData;

    /**
     * The name of this asset
     */
    public final String assetName;

    /**
     * @exclude
     */
    public Asset(String name, boolean b, byte[] data){
        isTexture = b;
        assetData = data;
        assetName = name;
    }
}
