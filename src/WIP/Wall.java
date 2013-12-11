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
    public Wall(Transform transform, StaticGraphicsComponent g, PhysicsComponent p) {
        super(transform, g, p);
    }

    @Override
    public boolean isWall() {
        return true;
    }

    @Override
    public boolean isFloor() {
        return false;
    }

    @Override
    public boolean isCollidable() {
        return true;
    }
}
