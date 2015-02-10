package net.sldt_team.gameEngine.sound;

import java.io.File;

/**
 * @exclude
 */
public class Sound {

    protected int buffer = 0;
    protected int source = 0;

    protected File soundPath;

    protected Sound(File path) {
        soundPath = path;
    }
}
