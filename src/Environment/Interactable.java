package Environment;

import Components.InteractableGraphicsComponent;
import WIP.Transform;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 12/12/13
 * Time: 10:23 PM
 */
public abstract class Interactable extends WorldSpace {

    protected boolean activated;

    public Interactable(Transform transform, InteractableGraphicsComponent g) {
        super(transform, g);
    }

    @Override
    public boolean isWall() {
        return false;
    }

    @Override
    public boolean isFloor() {
        return false;
    }

    @Override
    public boolean isInteractable() {
        return true;
    }

    public boolean isActivated() {
        return activated;
    }

    public abstract void activate();
}
