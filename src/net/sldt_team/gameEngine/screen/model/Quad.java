package net.sldt_team.gameEngine.screen.model;

public class Quad {

    /**
     * @exclude
     */
    public final float quadX;

    /**
     * @exclude
     */
    public final float quadY;

    /**
     * @exclude
     */
    public final float quadWidth;

    /**
     * @exclude
     */
    public final float quadHeight;

    /**
     * Creates a quad relative to a model's position ((0, 0) is equal to the model's origin)
     * @param x The x coord
     * @param y The y coord
     * @param width The width
     * @param height The height
     */
    public Quad(float x, float y, float width, float height){
        quadX = x;
        quadY = y;
        quadWidth = width;
        quadHeight = height;
    }
}
