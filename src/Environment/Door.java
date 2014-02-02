package Environment;

import Components.InteractableGraphicsComponent;
import Components.PhysicsComponent;
import WIP.Collidable;
import WIP.Transform;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 12/12/13
 * Time: 10:40 PM
 */
public class Door extends Interactable implements Collidable {
    private PhysicsComponent collider;

    public Door(Transform transform, InteractableGraphicsComponent g, PhysicsComponent physicsComponent) {
        super(transform, g);
        setCollider(physicsComponent);
        physicsComponent.setParent(getTransform());
    }

    public PhysicsComponent getCollider() {
        return collider;
    }

    public void setCollider(PhysicsComponent physicsComponent) {
        this.collider = physicsComponent;
    }

    @Override
    public boolean isCollidable() {
        return !isActivated();
    }

    @Override
    public void activate() {
        activated = !activated;
    }
}
