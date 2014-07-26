package fr.sldt.gameEngine.util;

import fr.sldt.gameEngine.GameApplication;
import fr.sldt.gameEngine.exception.GameException;
import fr.sldt.gameEngine.exception.code.ErrorCode003;

import java.util.Random;

public class MathUtilities {

    /**
     * Returns random integers between par1 and par2 (you need to specify witch random)
     */
    public static int generateRandomInteger(int par1, int par2, Random par3Random){
        if (par1 > par2) {
            throw new GameException(new ErrorCode003());
        }

        long range = (long)par2 - (long)par1 + 1;

        long fraction = (long)(range * par3Random.nextDouble());
        return (int)(fraction + par1);
    }

    public static float getCenteredObjectX(float objectWidth){
        return (GameApplication.getScreenWidth() / 2) - (objectWidth / 2);
    }

    public static float getCenteredObjectY(float objectHeight){
        return (GameApplication.getScreenHeight() / 2) - (objectHeight / 2);
    }
}
