package net.sldt_team.gameEngine.particle;

import com.sun.istack.internal.NotNull;
import net.sldt_team.gameEngine.util.MathUtilities;
import net.sldt_team.gameEngine.exception.GameException;
import net.sldt_team.gameEngine.exception.code.ErrorCode007;
import net.sldt_team.gameEngine.renderengine.FontRenderer;
import net.sldt_team.gameEngine.renderengine.RenderEngine;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;

public class ParticleManager {
    private Particle[] particlesMap;

    /**
     * @exclude
     */
    public ParticleManager() {
        particlesMap = new Particle[256];
    }

    /**
     * @exclude
     */
    public void doRender(RenderEngine renderEngine, FontRenderer fontRenderer) {
        for (Particle p : particlesMap) {
            if (p != null) {
                p.doRender(renderEngine, fontRenderer);
            }
        }
    }

    /**
     * Returns if a particle exists at given coords x and y
     */
    public boolean isParticleExistAtCoords(float x, float y) {
        for (Particle p : particlesMap) {
            if (p != null && p.particleX == x && p.particleY == y)
                return true;
        }
        return false;
    }

    /**
     * Spawns a particle, but be sure to complete every parameters...
     *
     * @param name          The particle name
     * @param xMin          Min spawn area X
     * @param yMin          Min spawn area Y
     * @param xMax          Max spawn area X
     * @param yMax          Max spawn area Y
     * @param particleCount The number of particles to spawn
     * @param obj           This object is an extra parameter used by certain particles
     */
    public void spawnParticle(@NotNull String name, float xMin, float yMin, float xMax, float yMax, int particleCount, Object obj) {
        for (int i = 0; i < particleCount; i++) {
            for (int j = 0; j < 256; j++) {
                if (particlesMap[j] == null) {
                    Random r = new Random();
                    int testX = MathUtilities.generateRandomInteger((int) xMin, (int) xMax, r);
                    int testY = MathUtilities.generateRandomInteger((int) yMin, (int) yMax, r);
                    int lifeTime = MathUtilities.generateRandomInteger(1000, 3000, new Random());
                    int size = MathUtilities.generateRandomInteger(1, 5, new Random());

                    if (!isParticleExistAtCoords(testX, testY)) {
                        try {
                            Class<? extends Particle> theClass = ParticleRegistry.findDeclaredParticle(name);
                            if (theClass != null) {
                                Constructor conststructor = theClass.getDeclaredConstructor(Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Integer.TYPE, Object.class);
                                Particle p = (Particle) conststructor.newInstance(testX, testY, size, size, lifeTime, obj);
                                particlesMap[j] = p;
                                break;
                            }
                        } catch (NoSuchMethodException e) {
                            throw new GameException(new ErrorCode007());
                        } catch (InvocationTargetException e) {
                            throw new GameException(new ErrorCode007());
                        } catch (InstantiationException e) {
                            throw new GameException(new ErrorCode007());
                        } catch (IllegalAccessException e) {
                            throw new GameException(new ErrorCode007());
                        }
                    }
                }
            }
        }
    }

    /**
     * @exclude
     */
    public void doUpdate() {
        for (int i = 0; i < 256; i++) {
            Particle p = particlesMap[i];
            if (p != null) {
                int var0 = MathUtilities.generateRandomInteger(1, 4, new Random());
                p.lifeTime -= var0;
                if (p.lifeTime <= 0) {
                    particlesMap[i] = null;
                    continue;
                }
                p.doUpdate();
            }
        }
    }
}
