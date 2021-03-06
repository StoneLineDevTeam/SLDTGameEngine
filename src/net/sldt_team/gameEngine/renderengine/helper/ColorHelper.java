package net.sldt_team.gameEngine.renderengine.helper;

public class ColorHelper {

    private float red;
    private float green;
    private float blue;
    private float alpha;

    /**
     * The red color
     */
    public static ColorHelper RED = new ColorHelper(255, 0, 0);

    /**
     * The green color
     */
    public static ColorHelper GREEN = new ColorHelper(0, 255, 0);

    /**
     * The blue color
     */
    public static ColorHelper BLUE = new ColorHelper(0, 0, 255);

    /**
     * The black color
     */
    public static ColorHelper BLACK = new ColorHelper(0, 0, 0);

    /**
     * The white color
     */
    public static ColorHelper WHITE = new ColorHelper(255, 255, 255);

    /**
     * The yellow color
     */
    public static ColorHelper YELLOW = new ColorHelper(255, 255, 0);

    /**
     * Defines a new color using int-RGB
     * @param r Red-Element
     * @param g Green-Element
     * @param b Blue-Element
     */
    public ColorHelper(int r, int g, int b) {
        red = analyseColor(r);
        green = analyseColor(g);
        blue = analyseColor(b);
        alpha = 1.0F;
    }

    /**
     * Defines a new color using float-RGBA (0-1)
     * @param r Red-Element
     * @param g Green-Element
     * @param b Blue-Element
     * @param a Alpha-Element
     */
    public ColorHelper(float r, float g, float b, float a){
        red = r;
        green = g;
        blue = b;
        alpha = a;
    }

    /**
     * Defines a new color using float-RGB (0-1)
     * @param r Red-Element
     * @param g Green-Element
     * @param b Blue-Element
     */
    public ColorHelper(float r, float g, float b){
        red = r;
        green = g;
        blue = b;
        alpha = 1.0F;
    }

    /**
     * Defines a new color using int-RGBA
     * @param r Red-Element
     * @param g Green-Element
     * @param b Blue-Element
     * @param a Alpha-Element
     */
    public ColorHelper(int r, int g, int b, int a) {
        red = analyseColor(r);
        green = analyseColor(g);
        blue = analyseColor(b);
        alpha = analyseColor(a);
    }

    private float analyseColor(float colorPart) {
        return Math.abs(colorPart / 255);
    }

    /**
     * Returns Red-Element as float
     */
    public float getRed() {
        return red;
    }

    /**
     * Returns Green-Element as float
     */
    public float getGreen() {
        return green;
    }

    /**
     * Returns Blue-Element as float
     */
    public float getBlue() {
        return blue;
    }

    /**
     * Returns Alpha-Element as float
     */
    public float getAlpha() {
        return alpha;
    }

    /**
     * Replaces the color elements (R, G, B, A)
     */
    public void replaceColorElements(int r, int g, int b, int a){
        red = analyseColor(r);
        green = analyseColor(g);
        blue = analyseColor(b);
        alpha = analyseColor(a);
    }
}
