package WIP;

import Components.GraphicsComponent;
import Components.PhysicsComponent;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 07.11.13
 * Time: 15:19
 */
public class Wall extends WorldSpace {

    @Override
    public boolean isCollidable() {
        return true;
    }

    public Wall(Transform transform, GraphicsComponent g, PhysicsComponent p) {
        super(transform, g);
    }
}
