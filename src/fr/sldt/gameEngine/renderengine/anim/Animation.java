package fr.sldt.gameEngine.renderengine.anim;

public class Animation {

    private TextureFrame[] textures;
    private int interval;
    private int currentTexture = 0;

    public Animation(int i, TextureFrame[] frames){
        interval = i;
        textures = frames;
    }

    public int getInterval(){
        return interval;
    }

    public void nextTexture(){
        if ((currentTexture + 1) < textures.length) {
            currentTexture++;
        } else {
            currentTexture = 0;
        }
    }

    public TextureFrame getCurrentTexture(){
        return textures[currentTexture];
    }
}
