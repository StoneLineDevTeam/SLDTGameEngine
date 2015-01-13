package net.sldt_team.gameEngine.sound;

import org.lwjgl.BufferUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * @exclude
 */
public class SoundEntry {
    /**
     * Buffers hold sound data.
     */
    protected IntBuffer buffer = BufferUtils.createIntBuffer(1);

    /**
     * Sources are points emitting sound.
     */
    protected IntBuffer source = BufferUtils.createIntBuffer(1);

    /**
     * Position of the source sound.
     */
    protected FloatBuffer sourcePos = BufferUtils.createFloatBuffer(3).put(new float[]{0.0f, 0.0f, 0.0f});

    /**
     * Velocity of the source sound.
     */
    protected FloatBuffer sourceVel = BufferUtils.createFloatBuffer(3).put(new float[]{0.0f, 0.0f, 0.0f});

    /**
     * Position of the listener.
     */
    protected FloatBuffer listenerPos = BufferUtils.createFloatBuffer(3).put(new float[]{0.0f, 0.0f, 0.0f});

    /**
     * Velocity of the listener.
     */
    protected FloatBuffer listenerVel = BufferUtils.createFloatBuffer(3).put(new float[]{0.0f, 0.0f, 0.0f});

    /**
     * Orientation of the listener. (first 3 elements are "at", second 3 are "up")
     */
    protected FloatBuffer listenerOri = BufferUtils.createFloatBuffer(6).put(new float[]{0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f});

    protected InputStream soundPath;

    public SoundEntry(String path) {
        try {
            soundPath = new FileInputStream(new File(path));
            source.rewind();
            buffer.rewind();
            sourcePos.rewind();
            sourceVel.rewind();
            listenerPos.rewind();
            listenerVel.rewind();
            listenerOri.rewind();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
