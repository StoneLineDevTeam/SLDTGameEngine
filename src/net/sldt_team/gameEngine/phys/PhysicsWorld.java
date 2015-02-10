package net.sldt_team.gameEngine.phys;

import net.sldt_team.gameEngine.GameApplication;
import net.sldt_team.gameEngine.util.MathUtilities;
import net.sldt_team.gameEngine.util.Vector2D;

import java.util.ArrayList;
import java.util.List;

public class PhysicsWorld {

    private PhysicsObject[] worldObjects;
    private IPhysicsEngine physLibrary;
    private List<IPhysicsEvent> eventHandlers;

    /**
     * @exclude
     */
    public PhysicsWorld(int numObjMax, IPhysicsEngine library){
        if (!MathUtilities.isPowerOfTwo(numObjMax)){
            GameApplication.engineLogger.severe("Unable to create physics world : max object number isn't power of two !");
            GameApplication.engineLogger.severe("PhysicsWorld has stopped...");
            return;
        }
        worldObjects = new PhysicsObject[numObjMax];
        eventHandlers = new ArrayList<IPhysicsEvent>();
        physLibrary = library;
    }

    /*
    public int cote_Collision(Rectangle heros, Rectangle tile) {


        float w = (float) (0.5 * (heros.width + tile.width));
        float h = (float) (0.5 * (heros.height + tile.height));
        float dx = (float) (heros.getCenterX() - tile.getCenterX());
        float dy = (float) (heros.getCenterY() - tile.getCenterY());

        if (dx <= w && dy <= h)
        {
            // collision!
            float wy = w * dy;
            float hx = h * dx;

            if (wy > hx)
            {
                if (wy > -hx)
                {
                    // collision at the top
                	return HAUT;
                }
                else
                {
                	// on the left
                	return GAUCHE;
                }
            }
            else
                if (wy > -hx)
                {
                    // on the right
                	return DROITE;
                }
                else
                {
                    // at the bottom
                	return BAS;
                }
        }
		return 0;

	}
     */

    /**
     * Adds an event handler to the event handlers list
     */
    public void registerEventHandler(IPhysicsEvent event){
        eventHandlers.add(event);
    }

    /**
     * Spawns a physical object in this physic world, and returns true if it has been correctly spawned
     */
    public boolean spawnPhysicsObjectInWorld(PhysicsObject newObj){
        for (int i = 0 ; i < worldObjects.length ; i++){
            if (worldObjects[i] == null){
                worldObjects[i] = newObj;
                return true;
            }
        }
        return false;
    }

    /**
     * @exclude
     */
    public void updatePhysWorld(){
        for (int i = 0 ; i < worldObjects.length ; i++){
            PhysicsObject object = worldObjects[i];
            if (object != null){
                if (object.shouldDelete){
                    worldObjects[i] = null;
                    for (IPhysicsEvent event : eventHandlers){
                        event.onObjectDelete(object);
                    }
                    return;
                }
                for (int j = 0 ; j < worldObjects.length ; j++){
                    PhysicsObject object1 = worldObjects[j];
                    if (object1 != null && object1 != object && physLibrary.checkCollision(object, object1)){
                        EnumCollisionSide side = physLibrary.calculateCollisionSide(object, object1);
                        for (IPhysicsEvent event : eventHandlers){
                            event.onObjectCollision(object, object1, side);
                        }
                        Vector2D newVel = physLibrary.calculateCollision(object, object1);
                        object.getVelocity().set(newVel);
                    }
                }
                physLibrary.updatePhysicsObject(object);
            }
        }
    }

    public void resetWorld(){
        for (int i = 0 ; i < worldObjects.length ; i++){
            PhysicsObject object = worldObjects[i];
            for (IPhysicsEvent event : eventHandlers){
                event.onObjectDelete(object);
            }
            worldObjects[i] = null;
        }
    }
}
