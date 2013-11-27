package WIP;

import Components.GraphicsComponent;
import Components.PhysicsComponent;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 14.11.13
 * Time: 14:26
 */
public abstract class NPC extends Actor {

    public NPC(String name, Transform transform, GraphicsComponent graphicsComponent,
               PhysicsComponent physicsComponent) {
        super(name, transform, graphicsComponent, physicsComponent);
    }

    @Override
    public void update() {
    }

    @Override
    public boolean isCollidable() {
        return false;
    }
}
