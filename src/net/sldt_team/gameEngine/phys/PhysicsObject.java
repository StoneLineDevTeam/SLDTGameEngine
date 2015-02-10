package net.sldt_team.gameEngine.phys;

import net.sldt_team.gameEngine.phys.types.Circle;
import net.sldt_team.gameEngine.phys.types.EnumPhysType;
import net.sldt_team.gameEngine.phys.types.Rect;
import net.sldt_team.gameEngine.util.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class PhysicsObject {

    private Vector2D velocity;
    private Vector2D position;
    private Object physType;
    private final float objectMass;
    private List<PhysicsObject> children;

    /**
     * @exclude
     */
    protected boolean shouldDelete;

    /**
     * Creates a physical object
     * @param type The rect type object
     * @param mass The mass of this object
     * @param initPos Initial spawn position
     * @param initVel Initial spawn velocity
     */
    public PhysicsObject(Rect type, float mass, Vector2D initPos, Vector2D initVel){
        physType = type;
        objectMass = mass;
        velocity = initVel;
        position = initPos;
        children = new ArrayList<PhysicsObject>();
    }

    /**
     * Creates a physical object
     * @param type The circle type object
     * @param mass The mass of this object
     * @param initPos Initial spawn position
     * @param initVel Initial spawn velocity
     */
    public PhysicsObject(Circle type, float mass, Vector2D initPos, Vector2D initVel){
        physType = type;
        objectMass = mass;
        velocity = initVel;
        position = initPos;
        children = new ArrayList<PhysicsObject>();
    }

    /**
     * Adds a child to this PhysicsObject
     */
    public void addChild(PhysicsObject child){
        children.add(child);
    }

    /**
     * Returns the velocity of this object
     */
    public Vector2D getVelocity(){
        return velocity;
    }

    /**
     * Returns the position of this object
     */
    public Vector2D getPosition(){
        return position;
    }

    /**
     * Returns the mass of this object
     */
    public float getMass(){
        return objectMass;
    }

    /**
     * Returns all children attached to this physical object
     */
    public List<PhysicsObject> getChildrenList(){
        return children;
    }

    /**
     * Mark this object for deletion
     */
    public void markForDeletion(){
        shouldDelete = true;
    }

    /**
     * Returns the type of this object
     */
    public EnumPhysType getPhysType(){
        if (physType instanceof Rect){
            return EnumPhysType.RECT;
        } else if (physType instanceof Circle){
            return EnumPhysType.CIRCLE;
        } else {
            return EnumPhysType.CUSTOM;
        }
    }

    /**
     * Returns the physical type as a rectangle
     * NOTE : May throw an exception if it's not a rectangle.
     */
    public Rect getPhysTypeAsRect(){
        return (Rect) physType;
    }

    /**
     * Returns the physical type as a circle
     * NOTE : May throw an exception if it's not a circle.
     */
    public Circle getPhysTypeAsCircle(){
        return (Circle) physType;
    }
}
