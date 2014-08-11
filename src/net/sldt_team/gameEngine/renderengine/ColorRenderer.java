package net.sldt_team.gameEngine.renderengine;

public class ColorRenderer {

    private float red;
    private float green;
    private float blue;
    private float alpha;

    /** The red color */
    public static ColorRenderer RED = new ColorRenderer(255, 0, 0);

    /** The green color */
    public static ColorRenderer GREEN = new ColorRenderer(0, 255, 0);

    /** The blue color */
    public static ColorRenderer BLUE = new ColorRenderer(0, 0, 255);

    /** The black color */
    public static ColorRenderer BLACK = new ColorRenderer(0, 0, 0);

    /** The white color */
    public static ColorRenderer WHITE = new ColorRenderer(255, 255, 255);

    /**
     * Defines a new color using int-RGB
     */
    public ColorRenderer(int r, int g, int b){
        red = analyseColor(r);
        green = analyseColor(g);
        blue = analyseColor(b);
        alpha = 1.0F;
    }

    private float analyseColor(float colorPart){
        return colorPart / 255;
    }

    /**
     * Defines a new color using int-RGBA
     */
    public ColorRenderer(int r, int g, int b, int a){
        red = analyseColor(r);
        green = analyseColor(g);
        blue = analyseColor(b);
        alpha = analyseColor(a);
    }

    /**
     * Returns R as float
     */
    public float getRed(){
        return red;
    }

    /**
     * Returns G as float
     */
    public float getGreen(){
        return green;
    }

    /**
     * Returns B as float
     */
    public float getBlue(){
        return blue;
    }

    /**
     * Returns A as float
     */
    public float getAlpha(){
        return alpha;
    }
}
