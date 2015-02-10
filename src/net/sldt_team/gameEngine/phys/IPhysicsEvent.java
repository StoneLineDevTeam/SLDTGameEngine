package net.sldt_team.gameEngine.phys;

public interface IPhysicsEvent {

    /**
     * Called when two objects collides
     */
    public void onObjectCollision(PhysicsObject obj1, PhysicsObject obj2, EnumCollisionSide collideSide);

    /**
     * Called when an object gets deleted
     */
    public void onObjectDelete(PhysicsObject obj);
}
