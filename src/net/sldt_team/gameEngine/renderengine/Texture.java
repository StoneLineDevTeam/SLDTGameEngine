package net.sldt_team.gameEngine.renderengine;

public class Texture {

    protected final int openGLIndex;
    protected final int textureWidth;
    protected final int textureHeight;

    protected Texture(int a, int w, int h){
        openGLIndex = a;
        textureWidth = w;
        textureHeight = h;
    }
}
