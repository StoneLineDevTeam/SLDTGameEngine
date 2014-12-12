package net.sldt_team.gameEngine.renderengine.animation;

import net.sldt_team.gameEngine.renderengine.helper.ColorHelper;

public class TextureFrame {

    /**
     * The frame's color
     */
    public final ColorHelper color;

    /**
     * The frame's scale
     */
    public final float scale;

    /**
     * The frame's texture path
     */
    public final String path;

    /**
     * The frame's id
     */
    public final String id;

    /**
     * U-Coord (only used if animation uses texture coordinates
     */
    public final float uCoord;

    /**
     * V-Coord (only used if animation uses texture coordinates
     */
    public final float vCoord;

    /**
     * U1-Coord (only used if animation uses texture coordinates
     */
    public final float u1Coord;

    /**
     * V1-Coord (only used if animation uses texture coordinates
     */
    public final float v1Coord;

    /**
     * Creates a TextureFrame for an animation witch isn't using texture coordinates
     *
     * @param c The frame's color
     * @param s The frame's scale
     * @param p The frame's texture path
     * @param n The frame's ID
     */
    public TextureFrame(ColorHelper c, float s, String p, String n) {
        color = c;
        scale = s;
        path = p;
        id = n;

        uCoord = -1;
        u1Coord = -1;
        vCoord = -1;
        v1Coord = -1;
    }

    /**
     * Creates a TextureFrame for an animation witch is using texture coordinates
     *
     * @param c  The frame's color
     * @param s  The frame's scale
     * @param p  The frame's texture path
     * @param n  The frame's ID
     * @param u  The frame's U-Coord
     * @param v  The frame's V-Coord
     * @param u1 The frame's U1-Coord
     * @param v1 The frame's V1-Coord
     */
    public TextureFrame(ColorHelper c, float s, String p, String n, float u, float v, float u1, float v1) {
        color = c;
        scale = s;
        path = p;
        id = n;

        uCoord = u;
        u1Coord = u1;
        vCoord = v;
        v1Coord = v1;
    }

    /**
     * @exclude
     */
    public String toString() {
        return id;
    }
}
