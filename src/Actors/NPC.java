package Actors;

import Components.ActorGraphicsComponent;
import Components.PhysicsComponent;
import WIP.Transform;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 14.11.13
 * Time: 14:26
 */

/**
 * Abstract for all actors that are not controlled by the player
 */
public abstract class NPC extends Actor {

    public NPC(String name, Transform transform, ActorGraphicsComponent graphicsComponent,
               PhysicsComponent physicsComponent) {
        super(name, transform, graphicsComponent, physicsComponent);
    }

    @Override
    public boolean isCollidable() {
        return true;
    }
}
