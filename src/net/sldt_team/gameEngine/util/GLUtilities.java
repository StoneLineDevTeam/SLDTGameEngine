package net.sldt_team.gameEngine.util;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.nio.IntBuffer;

/**
 * Warning, this class uses OpenGL context
 */
public class GLUtilities {

    /**
     * Returns garphics-card's max supported version of OpenGL
     */
    public static String getGLVersion() {
        return GL11.glGetString(GL11.GL_VERSION);
    }

    /**
     * Returns max texture size supported on user's computer's graphics card
     */
    public static String getMaxTextureSize() {
        IntBuffer intmax = BufferUtils.createIntBuffer(16);
        intmax.rewind();
        GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE, intmax);

        return "GLTextureSize=" + intmax;
    }

    /**
     * Returns graphics card vendor name
     */
    public static String getGLVendor() {
        return GL11.glGetString(GL11.GL_VENDOR);
    }

    /**
     * Returns the driver version
     */
    public static String getDriverVersion() {
        return Display.getVersion();
    }

    /**
     * Returns driver name
     */
    public static String getDriverName() {
        return Display.getAdapter();
    }

    /**
     * Switchs VSync mode depending on "enable" boolean
     */
    public static void switchVSync(boolean enable) {
        Display.setVSyncEnabled(enable);
    }
}
