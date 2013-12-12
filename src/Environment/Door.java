package Environment;

import Components.InteractableGraphicsComponent;
import Components.PhysicsComponent;
import WIP.Transform;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 12/12/13
 * Time: 10:40 PM
 */
public class Door extends Interactable{

    public Door(Transform transform, InteractableGraphicsComponent g, PhysicsComponent physicsComponent) {
        super(transform, g, physicsComponent);
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
