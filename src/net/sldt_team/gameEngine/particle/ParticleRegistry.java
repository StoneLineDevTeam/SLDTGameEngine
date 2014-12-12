package net.sldt_team.gameEngine.particle;

import net.sldt_team.gameEngine.particle.basic.ParticleButtonClick;
import net.sldt_team.gameEngine.particle.basic.ParticleNotExisting;

import java.util.HashMap;
import java.util.Map;

public class ParticleRegistry {

    private static final Map<String, Class<? extends Particle>> definedParticles = new HashMap<String, Class<? extends Particle>>();

    /**
     * This function retrieves a particle class from a particle name
     */
    public static Class<? extends Particle> findDeclaredParticle(String name) {
        if (!definedParticles.containsKey(name)) {
            return ParticleNotExisting.class;
        }
        return definedParticles.get(name);
    }

    /**
     * Registers a particle with given name and class
     */
    public static void registerParticle(String name, Class<? extends Particle> particleClass) {
        definedParticles.put(name, particleClass);
    }

    static {
        definedParticles.put("button", ParticleButtonClick.class);
    }
}
