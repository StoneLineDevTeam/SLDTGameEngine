package net.sldt_team.gameEngine.util;

import net.sldt_team.gameEngine.exception.GameException;
import net.sldt_team.gameEngine.exception.code.ErrorCode003;
import net.sldt_team.gameEngine.screen.model.Quad;
import net.sldt_team.gameEngine.util.misc.EnumCollisionSide;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MathUtilities {

    /**
     * Returns random integers between par1 and par2 (you need to specify witch random)
     */
    public static int generateRandomInteger(int par1, int par2, Random par3Random) {
        if (par1 > par2) {
            throw new GameException(new ErrorCode003());
        }

        long range = (long) par2 - (long) par1 + 1;

        long fraction = (long) (range * par3Random.nextDouble());
        return (int) (fraction + par1);
    }

    /**
     * Returns if numToCheck is first or no
     */
    public static boolean isNumberFirst(int numToCheck){
        if (numToCheck == 1){
            return false;
        }

        int[] MFB = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31};
        int divisor = 0;
        int i = 0;
        while ((divisor ^ 2) < numToCheck){
            if (i < MFB.length) {
                divisor = MFB[i];
            } else {
                divisor += 2;
            }
            if (divisor == numToCheck){
                i++;
                continue;
            }
            int rest = numToCheck % divisor;
            if (rest == 0){
                return false;
            }
            i++;
        }
        return true;
    }

    /**
     * Returns first factors of a number
     */
    public static int[] decompileNumberInFactors(int numToDecompile){
        if (isNumberFirst(numToDecompile)){
            return new int[]{numToDecompile};
        }
        List<Integer> list = new ArrayList<Integer>();
        int divisor = 0;
        while (divisor < numToDecompile){
            divisor++;
            if (!isNumberFirst(divisor)){
                continue;
            }
            int rest = numToDecompile % divisor;
            if (rest == 0){
                list.add(divisor);
                numToDecompile = numToDecompile / divisor;
                divisor = 0;
            }
        }
        int[] ints = new int[list.size()];
        for (int j = 0 ; j < list.size() ; j++){
            ints[j] = list.get(j);
        }
        return ints;
    }

    /**
     * Returns the nearest power of two of a given integer
     */
    public static int findNearestPowerOfTwo(int x){
        if ((x & (x-1)) == 0 ) return x;
        for(int i=1; i<32; i = i*2){
            x |= ( x >>> i);
        }
        return x + 1;
    }

    /**
     * Returns integer part of a double
     */
    public static int getIntegerPart(double d){
        double fractionalPart = d % 1;
        return (int)(d - fractionalPart);
    }

    /**
     * Returns fractional part of a number
     */
    public static float getFractionalPart(double d){
        return (float)(d % 1);
    }

    /**
     * Is the given number a power of two
     */
    public static boolean isPowerOfTwo(int x) {
        while (((x % 2) == 0) && x > 1) {
            x /= 2;
        }
        return (x == 1);
    }

    /**
     * Tests the collision between two quads (vectors are absolute positions of objects and quads are there collision meshes
     */
    public static EnumCollisionSide testCollisionBetweenQuads(Vector2D objPos, Vector2D obj1Pos, Quad objCollideMesh, Quad obj1CollideMesh){
        float x = (float) (objPos.getX() + objCollideMesh.quadX);
        float y = (float) (objPos.getY() + objCollideMesh.quadY);

        float x1 = (float) (obj1Pos.getX() + obj1CollideMesh.quadX);
        float y1 = (float) (obj1Pos.getY() + obj1CollideMesh.quadY);

        float quadCenterX = (x + (objCollideMesh.quadWidth / 2));
        float quadCenterY = (y + (objCollideMesh.quadHeight / 2));

        float quad1CenterX = (x1 + (obj1CollideMesh.quadWidth / 2));
        float quad1CenterY = (y1 + (obj1CollideMesh.quadHeight / 2));

        float w = (float) (0.5 * (objCollideMesh.quadWidth + obj1CollideMesh.quadWidth));
        float h = (float) (0.5 * (objCollideMesh.quadHeight + obj1CollideMesh.quadHeight));
        float dx = quadCenterX - quad1CenterX;
        float dy = quadCenterY - quad1CenterY;

        if (Math.abs(dx) <= w && Math.abs(dy) <= h) {
            /* collision! */
            float wy = w * dy;
            float hx = h * dx;

            if (wy > hx) {
                if (wy > -hx) {
                    /* collision at the bottom */
                    return EnumCollisionSide.SIDE_BOTTOM;
                } else {
                    /* on the left */
                    return EnumCollisionSide.SIDE_LEFT;
                }
            } else {
                if (wy > -hx) {
                    /* on the right */
                    return EnumCollisionSide.SIDE_RIGHT;
                } else {
                    /* at the top */
                    return EnumCollisionSide.SIDE_TOP;
                }
            }
        }
        return null;
    }
}
