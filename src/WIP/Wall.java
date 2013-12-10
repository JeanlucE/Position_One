package WIP;

import Components.PhysicsComponent;
import Components.StaticGraphicsComponent;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 07.11.13
 * Time: 15:19
 */
public class Wall extends WorldSpace {

    private PhysicsComponent physicsComponent;

    public Wall(Transform transform, StaticGraphicsComponent g, PhysicsComponent p) {
        super(transform, g);
        physicsComponent = p;
    }

    @Override
    public boolean isCollidable() {
        return true;
    }
}
