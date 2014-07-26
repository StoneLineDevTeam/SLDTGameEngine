package fr.sldt.gameEngine.extentions;

public class Extention {

    private String extName;
    private String extVersion;

    public Extention(String name, String version){
        extName = name;
        extVersion = version;
    }

    public String getExtentionName(){
        return extName;
    }
    public String getExtentionVersion(){
        return extVersion;
    }
}
