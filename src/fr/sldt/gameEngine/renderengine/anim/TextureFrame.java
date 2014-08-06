package fr.sldt.gameEngine.renderengine.anim;

import fr.sldt.gameEngine.renderengine.ColorRenderer;

public class TextureFrame {

    public final ColorRenderer color;
    public final float scale;
    public final String path;

    public TextureFrame(ColorRenderer c, float s, String p){
        color = c;
        scale = s;
        path = p;
    }

    public String toString(){
        return "Color(" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ", " + color.getAlpha() + ");scale = " + scale + ";path = " + path;
    }
}
