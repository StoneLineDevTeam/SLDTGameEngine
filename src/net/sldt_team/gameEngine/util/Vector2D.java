package net.sldt_team.gameEngine.util;

public class Vector2D {

    private double xCoord;
    private double yCoord;

    public Vector2D(double x, double y){
        xCoord = x;
        yCoord = y;
    }

    public double getX(){
        return xCoord;
    }

    public double getY(){
        return yCoord;
    }

    public double distance(Vector2D vec){
        /**
         * A
         * |\
         * | \
         * |__\
         * B   C
         *
         * AC^2 = AB^2 + BC^2
         *
         * A(x, y)
         * |\
         * | \
         * |__\
         * C    B(x1, y1)
         *
         * AC = (y - y1)
         * CB = (x - x1)
         */

        double yDist = yCoord - vec.yCoord;
        double xDist = xCoord - vec.xCoord;
        double pithagora = (StrictMath.pow(xDist, 2) + StrictMath.pow(yDist, 2));
        return StrictMath.sqrt(pithagora);
    }

    public void add(Vector2D vec){
        xCoord += vec.xCoord;
        yCoord += vec.yCoord;
    }

    public void subtract(Vector2D vec){
        xCoord -= vec.xCoord;
        yCoord -= vec.yCoord;
    }

    public void multiply(Vector2D vec){
        xCoord *= vec.xCoord;
        yCoord *= vec.yCoord;
    }

    public void set(double x, double y){
        xCoord = x;
        yCoord = y;
    }
}
