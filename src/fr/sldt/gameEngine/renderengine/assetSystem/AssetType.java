package fr.sldt.gameEngine.renderengine.assetSystem;

public class AssetType {

    private String fileType;

    /**
     * Defines an asset type (Requieres you to provide the file format)
     */
    public AssetType(Type type){
        if (type == Type.GAF_FILE){
            fileType = "GAF";
        } else if (type == Type.JAR_FILE){
            fileType = "JAR";
        } else if (type == Type.ZIP_FILE) {
            fileType = "ZIP";
        }
    }

    /**
     * Returns true if it's a ZIP file
     */
    public boolean isZIP(){
        return fileType.equals("ZIP") || fileType.equals("JAR");
    }

    /**
     * Returns true if it's an ASSETS file (SLDT's assets file format)
     */
    public boolean isGAF(){
        return fileType.equals("GAF");
    }

    /**
     * Returns the file extention
     */
    public String getAssetFileExtention(){
        return fileType.toLowerCase();
    }
}
