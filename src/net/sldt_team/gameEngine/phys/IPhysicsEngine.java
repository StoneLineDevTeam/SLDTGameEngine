package net.sldt_team.gameEngine.phys;

import net.sldt_team.gameEngine.util.Vector2D;

public interface IPhysicsEngine {

    /**
     * Returns true if obj1 should collide with obj2
     */
    public boolean checkCollision(PhysicsObject obj1, PhysicsObject obj2);

    /**
     * Returns the side of collision for obj1 colliding with obj2
     */
    public EnumCollisionSide calculateCollisionSide(PhysicsObject obj1, PhysicsObject obj2);

    /**
     * Returns a vector corresponding to the velocity of obj1 when it collides with obj2
     * NOTE : If checkCollidion returns always false, this will never be called.
     */
    public Vector2D calculateCollision(PhysicsObject obj1, PhysicsObject obj2);

    /**
     * Updates a physical object, use it to handles object's velocity
     * NOTE : You must implement this method and do something, otherwise object's velocity is useless !
     */
    public void updatePhysicsObject(PhysicsObject obj);
}
