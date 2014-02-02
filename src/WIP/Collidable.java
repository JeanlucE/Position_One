package WIP;

import Components.GraphicsComponent;
import Components.PhysicsComponent;

/**
 * Created with IntelliJ IDEA.
 * User: Jean-Luc
 * Date: 11.12.13
 * Time: 19:18
 */
//TODO make interface
public abstract class Collidable extends GameObject {
    private PhysicsComponent physicsComponent;

    public Collidable(Transform transform, GraphicsComponent graphic, PhysicsComponent phys) {
        super(transform, graphic);
        this.physicsComponent = phys;
        if (phys != null) {
            phys.setParent(transform);
        }
    }

    public PhysicsComponent getCollider() {
        return physicsComponent;
    }

    public void setCollider(PhysicsComponent physicsComponent) {
        this.physicsComponent = physicsComponent;
    }

    @Override
    public boolean isCollidable() {
        return true;
    }

    public abstract void update();
}
