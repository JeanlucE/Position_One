package Environment;

import Components.PhysicsComponent;
import Components.StaticGraphicsComponent;
import WIP.Collidable;
import WIP.Transform;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 07.11.13
 * Time: 15:19
 */
public class Wall extends WorldSpace implements Collidable {
    private PhysicsComponent collider;

    public Wall(Transform transform, StaticGraphicsComponent g, PhysicsComponent p) {
        super(transform, g);
        setCollider(p);
        p.setParent(getTransform());
    }

    @Override
    public PhysicsComponent getCollider() {
        return collider;
    }

    @Override
    public void setCollider(PhysicsComponent physicsComponent) {
        this.collider = physicsComponent;
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
    public boolean isInteractable() {
        return false;
    }

    @Override
    public boolean isCollidable() {
        return true;
    }

    public String toString() {
        return "Wall:" + getTransform().getPosition().toString();
    }
}
