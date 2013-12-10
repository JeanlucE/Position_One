package WIP;

import Components.ActorGraphicsComponent;
import Components.PhysicsComponent;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 14.11.13
 * Time: 14:26
 */
public abstract class NPC extends Actor {

    public NPC(String name, Transform transform, ActorGraphicsComponent graphicsComponent,
               PhysicsComponent physicsComponent) {
        super(name, transform, graphicsComponent, physicsComponent);
    }

    @Override
    public abstract void update();

    @Override
    public boolean isCollidable() {
        return false;
    }
}
